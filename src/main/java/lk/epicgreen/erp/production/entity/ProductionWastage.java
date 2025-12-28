package lk.epicgreen.erp.production.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.product.entity.Product;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ProductionWastage entity
 * Tracks wastage and losses during production
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "production_wastage", indexes = {
    @Index(name = "idx_wo_id", columnList = "wo_id"),
    @Index(name = "idx_wastage_date", columnList = "wastage_date"),
    @Index(name = "idx_wastage_type", columnList = "wastage_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionWastage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Work order reference
     */
    @NotNull(message = "Work order is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wo_id", nullable = false, foreignKey = @ForeignKey(name = "fk_production_wastage_wo"))
    private WorkOrder workOrder;
    
    /**
     * Wastage date
     */
    @NotNull(message = "Wastage date is required")
    @Column(name = "wastage_date", nullable = false)
    private LocalDate wastageDate;
    
    /**
     * Product (raw material or finished product)
     */
    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_production_wastage_product"))
    private Product product;
    
    /**
     * Wastage type (MATERIAL, PRODUCTION, QUALITY_REJECTION)
     */
    @NotBlank(message = "Wastage type is required")
    @Column(name = "wastage_type", nullable = false, length = 20)
    private String wastageType;
    
    /**
     * Quantity wasted
     */
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @Column(name = "quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal quantity;
    
    /**
     * Unit of measure
     */
    @NotNull(message = "UOM is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uom_id", nullable = false, foreignKey = @ForeignKey(name = "fk_production_wastage_uom"))
    private UnitOfMeasure uom;
    
    /**
     * Unit cost
     */
    @PositiveOrZero(message = "Unit cost must be positive or zero")
    @Column(name = "unit_cost", precision = 15, scale = 2)
    private BigDecimal unitCost;
    
    /**
     * Total value (quantity × unit cost)
     */
    @PositiveOrZero(message = "Total value must be positive or zero")
    @Column(name = "total_value", precision = 15, scale = 2)
    private BigDecimal totalValue;
    
    /**
     * Reason for wastage
     */
    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;
    
    /**
     * Created timestamp
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /**
     * Created by (user ID)
     */
    @Column(name = "created_by")
    private Long createdBy;
    
    /**
     * Check if material wastage
     */
    @Transient
    public boolean isMaterialWastage() {
        return "MATERIAL".equals(wastageType);
    }
    
    /**
     * Check if production wastage
     */
    @Transient
    public boolean isProductionWastage() {
        return "PRODUCTION".equals(wastageType);
    }
    
    /**
     * Check if quality rejection
     */
    @Transient
    public boolean isQualityRejection() {
        return "QUALITY_REJECTION".equals(wastageType);
    }
    
    /**
     * Calculate total value
     */
    @Transient
    public void calculateTotalValue() {
        if (quantity != null && unitCost != null) {
            totalValue = quantity.multiply(unitCost).setScale(2, java.math.RoundingMode.HALF_UP);
        }
    }
    
    /**
     * Get wastage summary
     */
    @Transient
    public String getWastageSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(wastageType).append(" - ");
        if (product != null) {
            summary.append(product.getProductName());
        }
        summary.append(" - ").append(quantity);
        if (uom != null) {
            summary.append(" ").append(uom.getUomCode());
        }
        if (reason != null) {
            summary.append(" (").append(reason).append(")");
        }
        return summary.toString();
    }
    
    @PrePersist
    @PreUpdate
    protected void onSave() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (wastageDate == null) {
            wastageDate = LocalDate.now();
        }
        calculateTotalValue();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductionWastage)) return false;
        ProductionWastage that = (ProductionWastage) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
