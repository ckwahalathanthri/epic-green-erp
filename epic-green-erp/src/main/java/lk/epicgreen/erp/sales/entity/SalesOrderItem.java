package lk.epicgreen.erp.sales.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.product.entity.Product;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * SalesOrderItem entity
 * Represents line items in sales orders
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "sales_order_items", indexes = {
    @Index(name = "idx_sales_order_item_order", columnList = "sales_order_id"),
    @Index(name = "idx_sales_order_item_product", columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderItem extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Sales order reference (header)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sales_order_item_order"))
    private SalesOrder salesOrder;
    
    /**
     * Product reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sales_order_item_product"))
    private Product product;
    
    /**
     * Item description (optional override)
     */
    @Column(name = "item_description", length = 500)
    private String itemDescription;
    
    /**
     * Ordered quantity
     */
    @Column(name = "ordered_quantity", precision = 15, scale = 3, nullable = false)
    private BigDecimal orderedQuantity;
    
    /**
     * Dispatched quantity (from dispatch notes)
     */
    @Column(name = "dispatched_quantity", precision = 15, scale = 3)
    private BigDecimal dispatchedQuantity;
    
    /**
     * Invoiced quantity (from invoices)
     */
    @Column(name = "invoiced_quantity", precision = 15, scale = 3)
    private BigDecimal invoicedQuantity;
    
    /**
     * Pending quantity (ordered - dispatched)
     */
    @Column(name = "pending_quantity", precision = 15, scale = 3)
    private BigDecimal pendingQuantity;
    
    /**
     * Unit of measurement
     */
    @Column(name = "unit", nullable = false, length = 10)
    private String unit;
    
    /**
     * Unit price
     */
    @Column(name = "unit_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal unitPrice;
    
    /**
     * Discount percentage
     */
    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;
    
    /**
     * Discount amount
     */
    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount;
    
    /**
     * Tax percentage
     */
    @Column(name = "tax_percentage", precision = 5, scale = 2)
    private BigDecimal taxPercentage;
    
    /**
     * Tax amount
     */
    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount;
    
    /**
     * Line total (quantity * price - discount + tax)
     */
    @Column(name = "line_total", precision = 15, scale = 2)
    private BigDecimal lineTotal;
    
    /**
     * Notes
     */
    @Column(name = "notes", length = 500)
    private String notes;
    
    /**
     * Calculates line total with discount and tax
     */
    @Transient
    public void calculateLineTotal() {
        if (orderedQuantity == null || unitPrice == null) {
            lineTotal = BigDecimal.ZERO;
            return;
        }
        
        // Calculate base amount
        BigDecimal baseAmount = orderedQuantity.multiply(unitPrice);
        
        // Calculate discount
        BigDecimal discountAmt = BigDecimal.ZERO;
        if (discountAmount != null) {
            discountAmt = discountAmount;
        } else if (discountPercentage != null) {
            discountAmt = baseAmount.multiply(discountPercentage)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }
        
        BigDecimal amountAfterDiscount = baseAmount.subtract(discountAmt);
        
        // Calculate tax
        BigDecimal taxAmt = BigDecimal.ZERO;
        if (taxAmount != null) {
            taxAmt = taxAmount;
        } else if (taxPercentage != null) {
            taxAmt = amountAfterDiscount.multiply(taxPercentage)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }
        
        lineTotal = amountAfterDiscount.add(taxAmt);
        this.discountAmount = discountAmt;
        this.taxAmount = taxAmt;
    }
    
    /**
     * Gets net amount (before tax)
     */
    @Transient
    public BigDecimal getNetAmount() {
        if (orderedQuantity == null || unitPrice == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal baseAmount = orderedQuantity.multiply(unitPrice);
        
        if (discountAmount != null) {
            return baseAmount.subtract(discountAmount);
        }
        
        return baseAmount;
    }
    
    /**
     * Gets pending quantity (ordered - dispatched)
     */
    @Transient
    public void calculatePendingQuantity() {
        if (orderedQuantity == null) {
            pendingQuantity = BigDecimal.ZERO;
            return;
        }
        
        BigDecimal dispatched = dispatchedQuantity != null ? dispatchedQuantity : BigDecimal.ZERO;
        pendingQuantity = orderedQuantity.subtract(dispatched);
        
        if (pendingQuantity.compareTo(BigDecimal.ZERO) < 0) {
            pendingQuantity = BigDecimal.ZERO;
        }
    }
    
    /**
     * Checks if fully dispatched
     */
    @Transient
    public boolean isFullyDispatched() {
        if (orderedQuantity == null || dispatchedQuantity == null) {
            return false;
        }
        return dispatchedQuantity.compareTo(orderedQuantity) >= 0;
    }
    
    /**
     * Checks if partially dispatched
     */
    @Transient
    public boolean isPartiallyDispatched() {
        if (dispatchedQuantity == null || dispatchedQuantity.compareTo(BigDecimal.ZERO) == 0) {
            return false;
        }
        return !isFullyDispatched();
    }
    
    /**
     * Gets dispatch percentage
     */
    @Transient
    public BigDecimal getDispatchPercentage() {
        if (orderedQuantity == null || orderedQuantity.compareTo(BigDecimal.ZERO) == 0 || dispatchedQuantity == null) {
            return BigDecimal.ZERO;
        }
        return dispatchedQuantity.divide(orderedQuantity, 4, RoundingMode.HALF_UP)
                                 .multiply(new BigDecimal("100"));
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (dispatchedQuantity == null) {
            dispatchedQuantity = BigDecimal.ZERO;
        }
        if (invoicedQuantity == null) {
            invoicedQuantity = BigDecimal.ZERO;
        }
        
        // Calculate line total and pending quantity
        calculateLineTotal();
        calculatePendingQuantity();
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Recalculate line total and pending quantity
        calculateLineTotal();
        calculatePendingQuantity();
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
