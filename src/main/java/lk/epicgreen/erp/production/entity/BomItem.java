package lk.epicgreen.erp.production.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.product.entity.Product;
import lombok.*;

import java.math.BigDecimal;

/**
 * BomItem entity
 * Represents individual raw materials required in a BOM
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "bom_items", indexes = {
    @Index(name = "idx_bom_id", columnList = "bom_id"),
    @Index(name = "idx_raw_material_id", columnList = "raw_material_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BomItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * BOM reference (header)
     */
    @NotNull(message = "BOM is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bom_id", nullable = false, foreignKey = @ForeignKey(name = "fk_bom_item_bom"))
    private BillOfMaterials bom;
    
    /**
     * Raw material (input)
     */
    @NotNull(message = "Raw material is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raw_material_id", nullable = false, foreignKey = @ForeignKey(name = "fk_bom_item_raw_material"))
    private Product rawMaterial;
    
    /**
     * Quantity required
     */
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @Column(name = "quantity_required", nullable = false, precision = 15, scale = 3)
    private BigDecimal quantityRequired;
    
    /**
     * Unit of measure
     */
    @NotNull(message = "UOM is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uom_id", nullable = false, foreignKey = @ForeignKey(name = "fk_bom_item_uom"))
    private UnitOfMeasure uom;
    
    /**
     * Wastage percentage (e.g., 5.00 for 5%)
     */
    @DecimalMin(value = "0.00", message = "Wastage percentage must be positive or zero")
    @DecimalMax(value = "100.00", message = "Wastage percentage cannot exceed 100")
    @Column(name = "wastage_percentage", precision = 5, scale = 2)
    private BigDecimal wastagePercentage;
    
    /**
     * Standard cost per unit
     */
    @PositiveOrZero(message = "Standard cost must be positive or zero")
    @Column(name = "standard_cost", precision = 15, scale = 2)
    private BigDecimal standardCost;
    
    /**
     * Sequence number (for production steps)
     */
    @Column(name = "sequence_number")
    private Integer sequenceNumber;
    
    /**
     * Is critical component (must be available to start production)
     */
    @Column(name = "is_critical")
    private Boolean isCritical;
    
    /**
     * Remarks
     */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    /**
     * Check if critical
     */
    @Transient
    public boolean isCritical() {
        return Boolean.TRUE.equals(isCritical);
    }
    
    /**
     * Calculate quantity with wastage
     */
    @Transient
    public BigDecimal getQuantityWithWastage() {
        if (quantityRequired == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal wastage = wastagePercentage != null ? wastagePercentage : BigDecimal.ZERO;
        BigDecimal wastageMultiplier = BigDecimal.ONE.add(wastage.divide(new BigDecimal("100"), 4, java.math.RoundingMode.HALF_UP));
        return quantityRequired.multiply(wastageMultiplier).setScale(3, java.math.RoundingMode.HALF_UP);
    }
    
    /**
     * Calculate total cost (quantity × standard cost)
     */
    @Transient
    public BigDecimal getTotalCost() {
        if (quantityRequired == null || standardCost == null) {
            return BigDecimal.ZERO;
        }
        return getQuantityWithWastage().multiply(standardCost).setScale(2, java.math.RoundingMode.HALF_UP);
    }
    
    /**
     * Calculate total cost for specific production quantity
     */
    @Transient
    public BigDecimal getTotalCostForQuantity(BigDecimal productionQuantity) {
        if (productionQuantity == null || productionQuantity.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal requiredQty = getQuantityWithWastage().multiply(productionQuantity);
        BigDecimal cost = standardCost != null ? standardCost : BigDecimal.ZERO;
        return requiredQty.multiply(cost).setScale(2, java.math.RoundingMode.HALF_UP);
    }
    
    @PrePersist
    @PreUpdate
    protected void onSave() {
        if (wastagePercentage == null) {
            wastagePercentage = BigDecimal.ZERO;
        }
        if (isCritical == null) {
            isCritical = false;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BomItem)) return false;
        BomItem bomItem = (BomItem) o;
        return id != null && id.equals(bomItem.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
