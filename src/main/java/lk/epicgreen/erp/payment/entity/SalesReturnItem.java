package lk.epicgreen.erp.payment.entity;


import lk.epicgreen.erp.admin.entity.TaxRate;
import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.product.entity.Product;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * SalesReturnItem entity
 * Represents individual items in sales returns
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "sales_return_items", indexes = {
    @Index(name = "idx_return_id", columnList = "return_id"),
    @Index(name = "idx_product_id", columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesReturnItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Sales return reference (header)
     */
    @NotNull(message = "Sales return is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sales_return_item_return"))
    private SalesReturn salesReturn;
    
    /**
     * Product reference
     */
    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sales_return_item_product"))
    private Product product;
    
    /**
     * Batch number
     */
    @Size(max = 50)
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    /**
     * Quantity returned
     */
    @NotNull(message = "Quantity returned is required")
    @Positive(message = "Quantity returned must be positive")
    @Column(name = "quantity_returned", nullable = false, precision = 15, scale = 3)
    private BigDecimal quantityReturned;
    
    /**
     * Unit of measure
     */
    @NotNull(message = "UOM is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uom_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sales_return_item_uom"))
    private UnitOfMeasure uom;
    
    /**
     * Unit price
     */
    @NotNull(message = "Unit price is required")
    @Positive(message = "Unit price must be positive")
    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;
    
    /**
     * Tax rate reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_rate_id", foreignKey = @ForeignKey(name = "fk_sales_return_item_tax_rate"))
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
     * Return reason
     */
    @Column(name = "return_reason", columnDefinition = "TEXT")
    private String returnReason;
    
    /**
     * Disposition (RESALEABLE, DAMAGED, EXPIRED, SCRAP)
     */
    @Column(name = "disposition", length = 20)
    private String disposition;
    
    /**
     * Remarks
     */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    /**
     * Disposition checks
     */
    @Transient
    public boolean isResaleable() {
        return "RESALEABLE".equals(disposition);
    }
    
    @Transient
    public boolean isDamaged() {
        return "DAMAGED".equals(disposition);
    }
    
    @Transient
    public boolean isExpired() {
        return "EXPIRED".equals(disposition);
    }
    
    @Transient
    public boolean isScrap() {
        return "SCRAP".equals(disposition);
    }
    
    /**
     * Calculate line totals
     */
    @Transient
    public void calculateLineTotals() {
        if (quantityReturned == null || unitPrice == null) {
            lineTotal = BigDecimal.ZERO;
            return;
        }
        
        // Base amount
        BigDecimal baseAmount = quantityReturned.multiply(unitPrice);
        
        // Calculate tax
        if (taxRate != null) {
            taxAmount = baseAmount.multiply(taxRate.getTaxPercentage())
                    .divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
        } else if (taxAmount == null) {
            taxAmount = BigDecimal.ZERO;
        }
        
        // Line total (base + tax for returns)
        lineTotal = baseAmount.add(taxAmount);
    }
    
    /**
     * Get base amount (before tax)
     */
    @Transient
    public BigDecimal getBaseAmount() {
        if (quantityReturned == null || unitPrice == null) {
            return BigDecimal.ZERO;
        }
        return quantityReturned.multiply(unitPrice);
    }
    
    @PrePersist
    @PreUpdate
    protected void onSave() {
        if (disposition == null) {
            disposition = "RESALEABLE";
        }
        if (taxAmount == null) {
            taxAmount = BigDecimal.ZERO;
        }
        calculateLineTotals();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesReturnItem)) return false;
        SalesReturnItem that = (SalesReturnItem) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
