package lk.epicgreen.erp.sales.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

import lk.epicgreen.erp.admin.entity.TaxRate;
import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.product.entity.Product;
import lombok.*;

import java.math.BigDecimal;

/**
 * InvoiceItem entity
 * Represents individual line items in invoices
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "invoice_items", indexes = {
    @Index(name = "idx_invoice_id", columnList = "invoice_id"),
    @Index(name = "idx_product_id", columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Invoice reference (header)
     */
    @NotNull(message = "Invoice is required")
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
     * Product reference
     */
    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_invoice_item_product"))
    private Product product;
    
    /**
     * Batch number
     */
    @Size(max = 50)
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    /**
     * Quantity
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
    @JoinColumn(name = "uom_id", nullable = false, foreignKey = @ForeignKey(name = "fk_invoice_item_uom"))
    private UnitOfMeasure uom;
    
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
    @JoinColumn(name = "tax_rate_id", foreignKey = @ForeignKey(name = "fk_invoice_item_tax_rate"))
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
     * Calculate line totals
     */
    @Transient
    public void calculateLineTotals() {
        if (quantity == null || unitPrice == null) {
            lineTotal = BigDecimal.ZERO;
            return;
        }
        
        // Base amount
        BigDecimal baseAmount = quantity.multiply(unitPrice);
        
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
    
    /**
     * Get base amount (before discount and tax)
     */
    @Transient
    public BigDecimal getBaseAmount() {
        if (quantity == null || unitPrice == null) {
            return BigDecimal.ZERO;
        }
        return quantity.multiply(unitPrice);
    }
    
    /**
     * Get amount after discount (before tax)
     */
    @Transient
    public BigDecimal getAmountAfterDiscount() {
        BigDecimal base = getBaseAmount();
        BigDecimal discount = discountAmount != null ? discountAmount : BigDecimal.ZERO;
        return base.subtract(discount);
    }
    
    @PrePersist
    @PreUpdate
    protected void onSave() {
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
        if (!(o instanceof InvoiceItem)) return false;
        InvoiceItem that = (InvoiceItem) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
