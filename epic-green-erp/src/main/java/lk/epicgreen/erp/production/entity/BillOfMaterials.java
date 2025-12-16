package lk.epicgreen.erp.production.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.product.entity.Product;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * BillOfMaterials (BOM) entity
 * Represents recipe/formula for producing finished goods
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "bill_of_materials", indexes = {
    @Index(name = "idx_bom_number", columnList = "bom_number"),
    @Index(name = "idx_bom_product", columnList = "product_id"),
    @Index(name = "idx_bom_status", columnList = "status")
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
     * BOM number (unique identifier)
     */
    @Column(name = "bom_number", nullable = false, unique = true, length = 50)
    private String bomNumber;
    
    /**
     * BOM name
     */
    @Column(name = "bom_name", nullable = false, length = 200)
    private String bomName;
    
    /**
     * BOM version (e.g., "v1.0", "v2.1")
     */
    @Column(name = "bom_version", length = 20)
    private String bomVersion;
    
    /**
     * Product being produced (finished good)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_bom_product"))
    private Product product;
    
    /**
     * Output quantity (this BOM produces this quantity)
     */
    @Column(name = "output_quantity", precision = 15, scale = 3, nullable = false)
    private BigDecimal outputQuantity;
    
    /**
     * Output unit
     */
    @Column(name = "output_unit", nullable = false, length = 10)
    private String outputUnit;
    
    /**
     * BOM type (STANDARD, ALTERNATE, PROTOTYPE)
     */
    @Column(name = "bom_type", length = 20)
    private String bomType;
    
    /**
     * Description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * Production instructions
     */
    @Column(name = "production_instructions", columnDefinition = "TEXT")
    private String productionInstructions;
    
    /**
     * Processing time (in minutes)
     */
    @Column(name = "processing_time_minutes")
    private Integer processingTimeMinutes;
    
    /**
     * Labor cost per unit
     */
    @Column(name = "labor_cost", precision = 15, scale = 2)
    private BigDecimal laborCost;
    
    /**
     * Overhead cost per unit
     */
    @Column(name = "overhead_cost", precision = 15, scale = 2)
    private BigDecimal overheadCost;
    
    /**
     * Total material cost (calculated from items)
     */
    @Column(name = "total_material_cost", precision = 15, scale = 2)
    private BigDecimal totalMaterialCost;
    
    /**
     * Total cost (material + labor + overhead)
     */
    @Column(name = "total_cost", precision = 15, scale = 2)
    private BigDecimal totalCost;
    
    /**
     * Currency
     */
    @Column(name = "currency", length = 10)
    private String currency;
    
    /**
     * Valid from date
     */
    @Column(name = "valid_from")
    private LocalDate validFrom;
    
    /**
     * Valid to date
     */
    @Column(name = "valid_to")
    private LocalDate validTo;
    
    /**
     * Status (DRAFT, ACTIVE, INACTIVE, OBSOLETE)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Is default BOM for the product
     */
    @Column(name = "is_default")
    private Boolean isDefault;
    
    /**
     * Approved by
     */
    @Column(name = "approved_by", length = 50)
    private String approvedBy;
    
    /**
     * Approval date
     */
    @Column(name = "approval_date")
    private LocalDate approvalDate;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Total items count
     */
    @Column(name = "total_items")
    private Integer totalItems;
    
    /**
     * BOM items (ingredients/components)
     */
    @OneToMany(mappedBy = "billOfMaterials", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sequenceNumber ASC")
    @Builder.Default
    private Set<BomItem> items = new HashSet<>();
    
    /**
     * Adds a BOM item
     */
    public void addItem(BomItem item) {
        item.setBillOfMaterials(this);
        items.add(item);
    }
    
    /**
     * Removes a BOM item
     */
    public void removeItem(BomItem item) {
        items.remove(item);
        item.setBillOfMaterials(null);
    }
    
    /**
     * Calculates total material cost from items
     */
    @Transient
    public void calculateTotalMaterialCost() {
        if (items == null || items.isEmpty()) {
            totalMaterialCost = BigDecimal.ZERO;
            totalCost = BigDecimal.ZERO;
            return;
        }
        
        totalMaterialCost = items.stream()
            .map(BomItem::getTotalCost)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate total cost
        totalCost = totalMaterialCost;
        if (laborCost != null) {
            totalCost = totalCost.add(laborCost);
        }
        if (overheadCost != null) {
            totalCost = totalCost.add(overheadCost);
        }
    }
    
    /**
     * Gets cost per output unit
     */
    @Transient
    public BigDecimal getCostPerUnit() {
        if (totalCost == null || outputQuantity == null || outputQuantity.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return totalCost.divide(outputQuantity, 4, RoundingMode.HALF_UP);
    }
    
    /**
     * Checks if BOM is active
     */
    @Transient
    public boolean isActive() {
        if (!"ACTIVE".equals(status)) {
            return false;
        }
        
        LocalDate today = LocalDate.now();
        
        if (validFrom != null && today.isBefore(validFrom)) {
            return false;
        }
        
        if (validTo != null && today.isAfter(validTo)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Checks if BOM is valid on a specific date
     */
    @Transient
    public boolean isValidOn(LocalDate date) {
        if (validFrom != null && date.isBefore(validFrom)) {
            return false;
        }
        
        if (validTo != null && date.isAfter(validTo)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Checks if BOM can be edited
     */
    @Transient
    public boolean canEdit() {
        return "DRAFT".equals(status);
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "DRAFT";
        }
        if (bomType == null) {
            bomType = "STANDARD";
        }
        if (isDefault == null) {
            isDefault = false;
        }
        if (currency == null) {
            currency = "LKR";
        }
        if (totalItems == null) {
            totalItems = 0;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Update total items count
        if (items != null) {
            totalItems = items.size();
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BillOfMaterials)) return false;
        BillOfMaterials that = (BillOfMaterials) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
