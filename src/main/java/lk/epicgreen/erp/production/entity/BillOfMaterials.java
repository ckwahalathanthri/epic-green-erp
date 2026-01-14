package lk.epicgreen.erp.production.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.production.dto.response.BillOfMaterialsResponse;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * BillOfMaterials (BOM) entity
 * Represents the recipe/formula for producing finished goods
 * Defines raw materials, quantities, and costs required
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "bill_of_materials", indexes = {
    @Index(name = "idx_bom_code", columnList = "bom_code"),
    @Index(name = "idx_finished_product_id", columnList = "finished_product_id"),
    @Index(name = "idx_is_active", columnList = "is_active")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillOfMaterials extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * BOM code (unique identifier)
     */
    @NotBlank(message = "BOM code is required")
    @Size(max = 30)
    @Column(name = "bom_code", nullable = false, unique = true, length = 30)
    private String bomCode;
    
    /**
     * Finished product (output)
     */
    @NotNull(message = "Finished product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finished_product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_bom_finished_product"))
    private Product finishedProduct;
    
    /**
     * BOM version (e.g., "1.0", "2.0")
     */
    @Size(max = 10)
    @Column(name = "bom_version", length = 10)
    private String bomVersion;
    
    /**
     * Output quantity per production run
     */
    @NotNull(message = "Output quantity is required")
    @Positive(message = "Output quantity must be positive")
    @Column(name = "output_quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal outputQuantity;
    
    /**
     * Output unit of measure
     */
    @NotNull(message = "Output UOM is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "output_uom_id", nullable = false, foreignKey = @ForeignKey(name = "fk_bom_output_uom"))
    private UnitOfMeasure outputUom;
    
    /**
     * Estimated production time in minutes
     */
    @PositiveOrZero(message = "Production time must be positive or zero")
    @Column(name = "production_time_minutes")
    private Integer productionTimeMinutes;
    
    /**
     * Labor cost per production run
     */
    @PositiveOrZero(message = "Labor cost must be positive or zero")
    @Column(name = "labor_cost", precision = 15, scale = 2)
    private BigDecimal laborCost;
    
    /**
     * Overhead cost per production run
     */
    @PositiveOrZero(message = "Overhead cost must be positive or zero")
    @Column(name = "overhead_cost", precision = 15, scale = 2)
    private BigDecimal overheadCost;
    
    /**
     * Is active
     */
    @Column(name = "is_active")
    private Boolean isActive;
    
    /**
     * Effective from date
     */
    @NotNull(message = "Effective from date is required")
    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;
    
    /**
     * Effective to date (null = no end date)
     */
    @Column(name = "effective_to")
    private LocalDate effectiveTo;
    
    /**
     * Remarks
     */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    /**
     * BOM items (raw materials required)
     */
    @OneToMany(mappedBy = "bom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BomItem> items = new ArrayList<>();
    
    /**
     * Add BOM item
     */
    public void addItem(BomItem item) {
        item.setBom(this);
        items.add(item);
    }
    
    /**
     * Remove BOM item
     */
    public void removeItem(BomItem item) {
        items.remove(item);
        item.setBom(null);
    }
    
    /**
     * Check if active
     */
    @Transient
    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }
    
    /**
     * Check if currently effective
     */
    @Transient
    public boolean isCurrentlyEffective() {
        LocalDate today = LocalDate.now();
        if (!isActive()) {
            return false;
        }
        if (effectiveFrom != null && today.isBefore(effectiveFrom)) {
            return false;
        }
        if (effectiveTo != null && today.isAfter(effectiveTo)) {
            return false;
        }
        return true;
    }
    
    /**
     * Check if effective on a specific date
     */
    @Transient
    public boolean isEffectiveOn(LocalDate date) {
        if (!isActive()) {
            return false;
        }
        if (effectiveFrom != null && date.isBefore(effectiveFrom)) {
            return false;
        }
        if (effectiveTo != null && date.isAfter(effectiveTo)) {
            return false;
        }
        return true;
    }
    
    /**
     * Calculate total material cost
     */
    @Transient
    public BigDecimal getTotalMaterialCost() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(BomItem::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Calculate total production cost (material + labor + overhead)
     */
    @Transient
    public BigDecimal getTotalProductionCost() {
        BigDecimal materialCost = getTotalMaterialCost();
        BigDecimal labor = laborCost != null ? laborCost : BigDecimal.ZERO;
        BigDecimal overhead = overheadCost != null ? overheadCost : BigDecimal.ZERO;
        return materialCost.add(labor).add(overhead);
    }
    
    /**
     * Calculate cost per unit
     */
    @Transient
    public BigDecimal getCostPerUnit() {
        if (outputQuantity == null || outputQuantity.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return getTotalProductionCost().divide(outputQuantity, 2, java.math.RoundingMode.HALF_UP);
    }
    
    /**
     * Get total number of items
     */
    @Transient
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (isActive == null) {
            isActive = true;
        }
        if (bomVersion == null) {
            bomVersion = "1.0";
        }
        if (outputQuantity == null) {
            outputQuantity = new BigDecimal("1.000");
        }
        if (laborCost == null) {
            laborCost = BigDecimal.ZERO;
        }
        if (overheadCost == null) {
            overheadCost = BigDecimal.ZERO;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BillOfMaterialsResponse)) return false;
        BillOfMaterialsResponse that = (BillOfMaterialsResponse) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
