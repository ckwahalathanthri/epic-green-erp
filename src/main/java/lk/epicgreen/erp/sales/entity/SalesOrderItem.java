package lk.epicgreen.erp.sales.entity;


import lk.epicgreen.erp.admin.entity.TaxRate;
import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.product.entity.Product;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * SalesOrderItem entity
 * Represents individual line items in sales orders
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "sales_order_items", indexes = {
    @Index(name = "idx_order_id", columnList = "order_id"),
    @Index(name = "idx_product_id", columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Sales order reference (header)
     */
    @NotNull(message = "Sales order is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sales_order_item_order"))
    private SalesOrder order;
    
    /**
     * Product reference
     */
    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sales_order_item_product"))
    private Product product;
    
    /**
     * Batch number (optional)
     */
    @Size(max = 50)
    @Column(name = "batch_number", length = 50)
    private String batchNumber;

    @Column
    private BigDecimal Discount;
    
    /**
     * Quantity ordered
     */
    @NotNull(message = "Quantity ordered is required")
    @Positive(message = "Quantity ordered must be positive")
    @Column(name = "quantity_ordered", nullable = false, precision = 15, scale = 3)
    private BigDecimal quantityOrdered;
    
    /**
     * Quantity delivered
     */
    @PositiveOrZero(message = "Quantity delivered must be positive or zero")
    @Column(name = "quantity_delivered", precision = 15, scale = 3)
    private BigDecimal quantityDelivered;
    
    /**
     * Unit of measure
     */
    @NotNull(message = "UOM is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uom_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sales_order_item_uom"))
    private UnitOfMeasure uom;

    @Column
    private double quantity;
    
    /**
     * Unit price
     */
    @NotNull(message = "Unit price is required")
    @Positive(message = "Unit price must be positive")
    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;
    
    /**
     * Discount percentage
     */
    @DecimalMin(value = "0.00")
    @DecimalMax(value = "100.00")
    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;
    
    /**
     * Discount amount
     */
    @PositiveOrZero(message = "Discount amount must be positive or zero")
    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount;



    
    /**
     * Tax rate reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_rate_id", foreignKey = @ForeignKey(name = "fk_sales_order_item_tax_rate"))
    private TaxRate taxRate;
    
    /**
     * Tax amount
     */
    @PositiveOrZero(message = "Tax amount must be positive or zero")
    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount;
    
    /**
     * Line total
     */
    @NotNull(message = "Line total is required")
    @PositiveOrZero(message = "Line total must be positive or zero")
    @Column(name = "line_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal lineTotal;
    
    /**
     * Remarks
     */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    /**
     * Get quantity pending (calculated field)
     */
    @Transient
    public BigDecimal getQuantityPending() {
        BigDecimal ordered = quantityOrdered != null ? quantityOrdered : BigDecimal.ZERO;
        BigDecimal delivered = quantityDelivered != null ? quantityDelivered : BigDecimal.ZERO;
        return ordered.subtract(delivered);
    }
    
    /**
     * Check if fully delivered
     */
    @Transient
    public boolean isFullyDelivered() {
        return getQuantityPending().compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * Check if partially delivered
     */
    @Transient
    public boolean isPartiallyDelivered() {
        BigDecimal delivered = quantityDelivered != null ? quantityDelivered : BigDecimal.ZERO;
        return delivered.compareTo(BigDecimal.ZERO) > 0 && !isFullyDelivered();
    }
    
    /**
     * Get delivery percentage
     */
    @Transient
    public BigDecimal getDeliveryPercentage() {
        if (quantityOrdered == null || quantityOrdered.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal delivered = quantityDelivered != null ? quantityDelivered : BigDecimal.ZERO;
        return delivered.divide(quantityOrdered, 2, java.math.RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
    }
    
    /**
     * Calculate line totals
     */
    @Transient
    public void calculateLineTotals() {
        if (quantityOrdered == null || unitPrice == null) {
            lineTotal = BigDecimal.ZERO;
            return;
        }
        
        // Base amount
        BigDecimal baseAmount = quantityOrdered.multiply(unitPrice);
        
        // Calculate discount
        if (discountPercentage != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            discountAmount = baseAmount.multiply(discountPercentage)
                    .divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
        } else if (discountAmount == null) {
            discountAmount = BigDecimal.ZERO;
        }
        
        // Amount after discount
        BigDecimal amountAfterDiscount = baseAmount.subtract(discountAmount);
        
        // Calculate tax
        if (taxRate != null) {
            taxAmount = amountAfterDiscount.multiply(taxRate.getTaxPercentage())
                    .divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
        } else if (taxAmount == null) {
            taxAmount = BigDecimal.ZERO;
        }
        
        // Line total
        lineTotal = amountAfterDiscount.add(taxAmount);
    }
    
    @PrePersist
    @PreUpdate
    protected void onSave() {
        if (quantityDelivered == null) {
            quantityDelivered = BigDecimal.ZERO;
        }
        if (discountPercentage == null) {
            discountPercentage = BigDecimal.ZERO;
        }
        if (discountAmount == null) {
            discountAmount = BigDecimal.ZERO;
        }
        if (taxAmount == null) {
            taxAmount = BigDecimal.ZERO;
        }
        calculateLineTotals();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesOrderItem)) return false;
        SalesOrderItem that = (SalesOrderItem) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }



}
