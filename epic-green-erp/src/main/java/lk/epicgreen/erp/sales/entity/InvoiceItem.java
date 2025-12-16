package lk.epicgreen.erp.sales.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.product.entity.Product;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * InvoiceItem entity
 * Represents line items in invoices
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "invoice_items", indexes = {
    @Index(name = "idx_invoice_item_invoice", columnList = "invoice_id"),
    @Index(name = "idx_invoice_item_order_item", columnList = "order_item_id"),
    @Index(name = "idx_invoice_item_dispatch_item", columnList = "dispatch_item_id"),
    @Index(name = "idx_invoice_item_product", columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItem extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Invoice reference (header)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false, foreignKey = @ForeignKey(name = "fk_invoice_item_invoice"))
    private Invoice invoice;
    
    /**
     * Sales order item reference (optional)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", foreignKey = @ForeignKey(name = "fk_invoice_item_order_item"))
    private SalesOrderItem orderItem;
    
    /**
     * Dispatch item reference (optional)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispatch_item_id", foreignKey = @ForeignKey(name = "fk_invoice_item_dispatch_item"))
    private DispatchItem dispatchItem;
    
    /**
     * Product reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_invoice_item_product"))
    private Product product;
    
    /**
     * Item description (optional override)
     */
    @Column(name = "item_description", length = 500)
    private String itemDescription;
    
    /**
     * Quantity
     */
    @Column(name = "quantity", precision = 15, scale = 3, nullable = false)
    private BigDecimal quantity;
    
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
        if (quantity == null || unitPrice == null) {
            lineTotal = BigDecimal.ZERO;
            return;
        }
        
        // Calculate base amount
        BigDecimal baseAmount = quantity.multiply(unitPrice);
        
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
        if (quantity == null || unitPrice == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal baseAmount = quantity.multiply(unitPrice);
        
        if (discountAmount != null) {
            return baseAmount.subtract(discountAmount);
        }
        
        return baseAmount;
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        
        // Calculate line total
        calculateLineTotal();
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Recalculate line total
        calculateLineTotal();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvoiceItem)) return false;
        InvoiceItem that = (InvoiceItem) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
