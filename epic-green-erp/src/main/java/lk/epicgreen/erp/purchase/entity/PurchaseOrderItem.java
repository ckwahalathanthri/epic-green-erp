package lk.epicgreen.erp.purchase.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.product.entity.Product;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * PurchaseOrderItem entity
 * Represents line items in purchase orders
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "purchase_order_items", indexes = {
    @Index(name = "idx_po_item_po", columnList = "purchase_order_id"),
    @Index(name = "idx_po_item_product", columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderItem extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Purchase order reference (header)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_po_item_purchase_order"))
    private PurchaseOrder purchaseOrder;
    
    /**
     * Product reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_po_item_product"))
    private Product product;
    
    /**
     * Item description (can override product description)
     */
    @Column(name = "item_description", length = 500)
    private String itemDescription;
    
    /**
     * Ordered quantity
     */
    @Column(name = "ordered_quantity", precision = 15, scale = 3, nullable = false)
    private BigDecimal orderedQuantity;
    
    /**
     * Received quantity (from GRNs)
     */
    @Column(name = "received_quantity", precision = 15, scale = 3)
    private BigDecimal receivedQuantity;
    
    /**
     * Pending quantity (ordered - received)
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
     * Line discount percentage
     */
    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;
    
    /**
     * Line discount amount
     */
    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount;
    
    /**
     * Line tax percentage
     */
    @Column(name = "tax_percentage", precision = 5, scale = 2)
    private BigDecimal taxPercentage;
    
    /**
     * Line tax amount
     */
    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount;
    
    /**
     * Line total (quantity * unitPrice - discount + tax)
     */
    @Column(name = "line_total", precision = 15, scale = 2, nullable = false)
    private BigDecimal lineTotal;
    
    /**
     * Expected delivery date for this item
     */
    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;
    
    /**
     * Notes/Specifications
     */
    @Column(name = "notes", length = 500)
    private String notes;
    
    /**
     * Calculates line total
     */
    @Transient
    public void calculateLineTotal() {
        if (orderedQuantity == null || unitPrice == null) {
            lineTotal = BigDecimal.ZERO;
            return;
        }
        
        // Base amount
        BigDecimal baseAmount = orderedQuantity.multiply(unitPrice);
        
        // Apply discount
        BigDecimal afterDiscount = baseAmount;
        if (discountAmount != null && discountAmount.compareTo(BigDecimal.ZERO) > 0) {
            afterDiscount = baseAmount.subtract(discountAmount);
        } else if (discountPercentage != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            discountAmount = baseAmount.multiply(discountPercentage).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            afterDiscount = baseAmount.subtract(discountAmount);
        }
        
        // Apply tax
        lineTotal = afterDiscount;
        if (taxAmount != null && taxAmount.compareTo(BigDecimal.ZERO) > 0) {
            lineTotal = afterDiscount.add(taxAmount);
        } else if (taxPercentage != null && taxPercentage.compareTo(BigDecimal.ZERO) > 0) {
            taxAmount = afterDiscount.multiply(taxPercentage).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            lineTotal = afterDiscount.add(taxAmount);
        }
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
        
        if (discountAmount != null && discountAmount.compareTo(BigDecimal.ZERO) > 0) {
            return baseAmount.subtract(discountAmount);
        } else if (discountPercentage != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discount = baseAmount.multiply(discountPercentage).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            return baseAmount.subtract(discount);
        }
        
        return baseAmount;
    }
    
    /**
     * Checks if item is fully received
     */
    @Transient
    public boolean isFullyReceived() {
        if (orderedQuantity == null || receivedQuantity == null) {
            return false;
        }
        return receivedQuantity.compareTo(orderedQuantity) >= 0;
    }
    
    /**
     * Checks if item is partially received
     */
    @Transient
    public boolean isPartiallyReceived() {
        if (receivedQuantity == null) {
            return false;
        }
        return receivedQuantity.compareTo(BigDecimal.ZERO) > 0 && !isFullyReceived();
    }
    
    /**
     * Gets received percentage
     */
    @Transient
    public BigDecimal getReceivedPercentage() {
        if (orderedQuantity == null || orderedQuantity.compareTo(BigDecimal.ZERO) == 0 || receivedQuantity == null) {
            return BigDecimal.ZERO;
        }
        return receivedQuantity.divide(orderedQuantity, 4, RoundingMode.HALF_UP)
                              .multiply(new BigDecimal("100"));
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (receivedQuantity == null) {
            receivedQuantity = BigDecimal.ZERO;
        }
        if (pendingQuantity == null && orderedQuantity != null) {
            pendingQuantity = orderedQuantity.subtract(receivedQuantity);
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Update pending quantity
        if (orderedQuantity != null && receivedQuantity != null) {
            pendingQuantity = orderedQuantity.subtract(receivedQuantity);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PurchaseOrderItem)) return false;
        PurchaseOrderItem that = (PurchaseOrderItem) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
