package lk.epicgreen.erp.warehouse.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.product.entity.Product;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * StockAdjustmentItem entity
 * Detail line items for stock adjustments
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "stock_adjustment_items", indexes = {
    @Index(name = "idx_stock_adj_item_adjustment", columnList = "stock_adjustment_id"),
    @Index(name = "idx_stock_adj_item_product", columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockAdjustmentItem extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Stock adjustment reference (header)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_adjustment_id", nullable = false, foreignKey = @ForeignKey(name = "fk_stock_adj_item_adjustment"))
    private StockAdjustment stockAdjustment;
    
    /**
     * Product reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_stock_adj_item_product"))
    private Product product;
    
    /**
     * Warehouse location
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", foreignKey = @ForeignKey(name = "fk_stock_adj_item_location"))
    private WarehouseLocation location;
    
    /**
     * Batch number
     */
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    /**
     * Serial number
     */
    @Column(name = "serial_number", length = 50)
    private String serialNumber;
    
    /**
     * System quantity (quantity in system before adjustment)
     */
    @Column(name = "system_quantity", precision = 15, scale = 3, nullable = false)
    private BigDecimal systemQuantity;
    
    /**
     * Physical quantity (actual counted quantity)
     */
    @Column(name = "physical_quantity", precision = 15, scale = 3, nullable = false)
    private BigDecimal physicalQuantity;
    
    /**
     * Adjustment quantity (physical - system, can be positive or negative)
     */
    @Column(name = "adjustment_quantity", precision = 15, scale = 3, nullable = false)
    private BigDecimal adjustmentQuantity;
    
    /**
     * Unit of measurement
     */
    @Column(name = "unit", length = 10)
    private String unit;
    
    /**
     * Cost per unit
     */
    @Column(name = "cost_per_unit", precision = 15, scale = 2)
    private BigDecimal costPerUnit;
    
    /**
     * Total value (adjustment quantity * cost per unit)
     */
    @Column(name = "total_value", precision = 15, scale = 2)
    private BigDecimal totalValue;
    
    /**
     * Currency
     */
    @Column(name = "currency", length = 10)
    private String currency;
    
    /**
     * Reason for discrepancy
     */
    @Column(name = "reason", length = 500)
    private String reason;
    
    /**
     * Notes
     */
    @Column(name = "notes", length = 500)
    private String notes;
    
    /**
     * Gets variance percentage
     */
    @Transient
    public BigDecimal getVariancePercentage() {
        if (systemQuantity == null || systemQuantity.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal variance = adjustmentQuantity != null ? adjustmentQuantity : BigDecimal.ZERO;
        return variance.divide(systemQuantity, 4, RoundingMode.HALF_UP)
                      .multiply(new BigDecimal("100"));
    }
    
    /**
     * Checks if there is a discrepancy
     */
    @Transient
    public boolean hasDiscrepancy() {
        return adjustmentQuantity != null && adjustmentQuantity.compareTo(BigDecimal.ZERO) != 0;
    }
    
    /**
     * Checks if adjustment is positive (surplus)
     */
    @Transient
    public boolean isSurplus() {
        return adjustmentQuantity != null && adjustmentQuantity.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Checks if adjustment is negative (shortage)
     */
    @Transient
    public boolean isShortage() {
        return adjustmentQuantity != null && adjustmentQuantity.compareTo(BigDecimal.ZERO) < 0;
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        
        // Calculate adjustment quantity if not set
        if (adjustmentQuantity == null && systemQuantity != null && physicalQuantity != null) {
            adjustmentQuantity = physicalQuantity.subtract(systemQuantity);
        }
        
        // Calculate total value if not set
        if (totalValue == null && adjustmentQuantity != null && costPerUnit != null) {
            totalValue = adjustmentQuantity.multiply(costPerUnit);
        }
        
        if (currency == null) {
            currency = "LKR";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Recalculate adjustment quantity
        if (systemQuantity != null && physicalQuantity != null) {
            adjustmentQuantity = physicalQuantity.subtract(systemQuantity);
        }
        
        // Recalculate total value
        if (adjustmentQuantity != null && costPerUnit != null) {
            totalValue = adjustmentQuantity.multiply(costPerUnit);
        }
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
