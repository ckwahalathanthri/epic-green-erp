package lk.epicgreen.erp.warehouse.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lk.epicgreen.erp.product.entity.Product;
import lombok.*;

import java.math.BigDecimal;

/**
 * StockAdjustmentItem entity
 * Represents individual line items in stock adjustments
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "stock_adjustment_items", indexes = {
    @Index(name = "idx_adjustment_id", columnList = "adjustment_id"),
    @Index(name = "idx_product_id", columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockAdjustmentItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Stock adjustment reference (header)
     */
    @NotNull(message = "Adjustment is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adjustment_id", nullable = false, foreignKey = @ForeignKey(name = "fk_stock_adjustment_item_adjustment"))
    private StockAdjustment adjustment;
    
    /**
     * Product reference
     */
    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_stock_adjustment_item_product"))
    private Product product;
    
    /**
     * Batch number
     */
    @Size(max = 50)
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    /**
     * Location reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", foreignKey = @ForeignKey(name = "fk_stock_adjustment_item_location"))
    private WarehouseLocation location;
    
    /**
     * Quantity adjusted (positive for surplus, negative for deficit/damage/expiry)
     */
    @NotNull(message = "Adjusted quantity is required")
    @Column(name = "quantity_adjusted", nullable = false, precision = 15, scale = 3)
    private BigDecimal quantityAdjusted;
    
    /**
     * Unit cost
     */
    @Column(name = "unit_cost", precision = 15, scale = 2)
    private BigDecimal unitCost;
    
    /**
     * Total value (quantity × unit cost)
     */
    @Column(name = "total_value", precision = 15, scale = 2)
    private BigDecimal totalValue;
    
    /**
     * Remarks for this line
     */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    /**
     * Check if quantity is positive (surplus)
     */
    @Transient
    public boolean isSurplus() {
        return quantityAdjusted != null && quantityAdjusted.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Check if quantity is negative (deficit/damage)
     */
    @Transient
    public boolean isDeficit() {
        return quantityAdjusted != null && quantityAdjusted.compareTo(BigDecimal.ZERO) < 0;
    }
    
    /**
     * Get absolute quantity
     */
    @Transient
    public BigDecimal getAbsoluteQuantity() {
        if (quantityAdjusted == null) {
            return BigDecimal.ZERO;
        }
        return quantityAdjusted.abs();
    }
    
    /**
     * Calculate total value
     */
    @Transient
    public void calculateTotalValue() {
        if (quantityAdjusted != null && unitCost != null) {
            totalValue = quantityAdjusted.multiply(unitCost);
        } else {
            totalValue = BigDecimal.ZERO;
        }
    }
    
    @PrePersist
    @PreUpdate
    protected void onSave() {
        calculateTotalValue();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockAdjustmentItem)) return false;
        StockAdjustmentItem that = (StockAdjustmentItem) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
