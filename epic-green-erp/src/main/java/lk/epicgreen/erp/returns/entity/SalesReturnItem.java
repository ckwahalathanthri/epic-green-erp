package lk.epicgreen.erp.returns.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.sales.entity.InvoiceItem;
import lk.epicgreen.erp.warehouse.entity.WarehouseLocation;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * SalesReturnItem entity
 * Represents line items in sales returns
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "sales_return_items", indexes = {
    @Index(name = "idx_sales_return_item_return", columnList = "sales_return_id"),
    @Index(name = "idx_sales_return_item_invoice_item", columnList = "invoice_item_id"),
    @Index(name = "idx_sales_return_item_product", columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesReturnItem extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Sales return reference (header)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_return_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sales_return_item_return"))
    private SalesReturn salesReturn;
    
    /**
     * Invoice item reference (optional)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_item_id", foreignKey = @ForeignKey(name = "fk_sales_return_item_invoice_item"))
    private InvoiceItem invoiceItem;
    
    /**
     * Product reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sales_return_item_product"))
    private Product product;
    
    /**
     * Warehouse location (where returned goods are stored)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", foreignKey = @ForeignKey(name = "fk_sales_return_item_location"))
    private WarehouseLocation location;
    
    /**
     * Item description (optional override)
     */
    @Column(name = "item_description", length = 500)
    private String itemDescription;
    
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
     * Return quantity
     */
    @Column(name = "return_quantity", precision = 15, scale = 3, nullable = false)
    private BigDecimal returnQuantity;
    
    /**
     * Accepted quantity (passed quality inspection)
     */
    @Column(name = "accepted_quantity", precision = 15, scale = 3)
    private BigDecimal acceptedQuantity;
    
    /**
     * Rejected quantity (failed quality inspection)
     */
    @Column(name = "rejected_quantity", precision = 15, scale = 3)
    private BigDecimal rejectedQuantity;
    
    /**
     * Unit of measurement
     */
    @Column(name = "unit", nullable = false, length = 10)
    private String unit;
    
    /**
     * Unit price (at time of original sale)
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
     * Return reason (DAMAGED, DEFECTIVE, WRONG_ITEM, EXPIRED, QUALITY_ISSUE, OTHER)
     */
    @Column(name = "return_reason", length = 50)
    private String returnReason;
    
    /**
     * Return action (RETURN_TO_STOCK, WRITE_OFF, REWORK, SEND_TO_SUPPLIER)
     */
    @Column(name = "return_action", length = 30)
    private String returnAction;
    
    /**
     * Quality status (PENDING, GOOD, ACCEPTABLE, POOR, REJECTED)
     */
    @Column(name = "quality_status", length = 20)
    private String qualityStatus;
    
    /**
     * Quality remarks
     */
    @Column(name = "quality_remarks", length = 500)
    private String qualityRemarks;
    
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
        if (returnQuantity == null || unitPrice == null) {
            lineTotal = BigDecimal.ZERO;
            return;
        }
        
        // Calculate base amount
        BigDecimal baseAmount = returnQuantity.multiply(unitPrice);
        
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
        if (returnQuantity == null || unitPrice == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal baseAmount = returnQuantity.multiply(unitPrice);
        
        if (discountAmount != null) {
            return baseAmount.subtract(discountAmount);
        }
        
        return baseAmount;
    }
    
    /**
     * Checks if item passed quality inspection
     */
    @Transient
    public boolean passedQualityInspection() {
        return "GOOD".equals(qualityStatus) || "ACCEPTABLE".equals(qualityStatus);
    }
    
    /**
     * Checks if item failed quality inspection
     */
    @Transient
    public boolean failedQualityInspection() {
        return "POOR".equals(qualityStatus) || "REJECTED".equals(qualityStatus);
    }
    
    /**
     * Gets acceptance percentage
     */
    @Transient
    public BigDecimal getAcceptancePercentage() {
        if (returnQuantity == null || returnQuantity.compareTo(BigDecimal.ZERO) == 0 || acceptedQuantity == null) {
            return BigDecimal.ZERO;
        }
        return acceptedQuantity.divide(returnQuantity, 4, RoundingMode.HALF_UP)
                               .multiply(new BigDecimal("100"));
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (acceptedQuantity == null) {
            acceptedQuantity = BigDecimal.ZERO;
        }
        if (rejectedQuantity == null) {
            rejectedQuantity = BigDecimal.ZERO;
        }
        if (qualityStatus == null) {
            qualityStatus = "PENDING";
        }
        if (returnAction == null) {
            returnAction = "RETURN_TO_STOCK";
        }
        
        // Calculate line total
        calculateLineTotal();
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Recalculate line total
        calculateLineTotal();
        
        // Validate: accepted + rejected = return quantity
        if (acceptedQuantity != null && rejectedQuantity != null) {
            BigDecimal total = acceptedQuantity.add(rejectedQuantity);
            if (total.compareTo(returnQuantity) > 0) {
                throw new IllegalStateException(
                    "Accepted + Rejected quantity cannot exceed return quantity"
                );
            }
        }
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
