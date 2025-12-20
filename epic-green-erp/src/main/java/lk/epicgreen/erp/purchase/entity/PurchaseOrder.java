package lk.epicgreen.erp.purchase.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.supplier.entity.Supplier;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * PurchaseOrder entity
 * Represents purchase orders for raw materials and other goods
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "purchase_orders", indexes = {
    @Index(name = "idx_po_number", columnList = "po_number"),
    @Index(name = "idx_po_date", columnList = "po_date"),
    @Index(name = "idx_po_supplier", columnList = "supplier_id"),
    @Index(name = "idx_po_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrder extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Purchase order number (unique identifier)
     */
    @Column(name = "po_number", nullable = false, unique = true, length = 50)
    private String poNumber;
    
    /**
     * PO date
     */
    @Column(name = "po_date", nullable = false)
    private LocalDate poDate;
    
    /**
     * Supplier reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false, foreignKey = @ForeignKey(name = "fk_po_supplier"))
    private Supplier supplier;
    
    /**
     * Expected delivery date
     */
    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;
    
    /**
     * Delivery address
     */
    @Column(name = "delivery_address", columnDefinition = "TEXT")
    private String deliveryAddress;
    
    /**
     * Delivery contact person
     */
    @Column(name = "delivery_contact_person", length = 100)
    private String deliveryContactPerson;
    
    /**
     * Delivery contact number
     */
    @Column(name = "delivery_contact_number", length = 20)
    private String deliveryContactNumber;
    
    /**
     * PO type (RAW_MATERIAL, PACKAGING, SERVICES, OTHERS)
     */
    @Column(name = "po_type", length = 30)
    private String poType;
    
    /**
     * Currency
     */
    @Column(name = "currency", nullable = false, length = 10)
    private String currency;
    
    /**
     * Exchange rate (if foreign currency)
     */
    @Column(name = "exchange_rate", precision = 15, scale = 6)
    private BigDecimal exchangeRate;
    
    /**
     * Subtotal (before tax and discount)
     */
    @Column(name = "subtotal", precision = 15, scale = 2, nullable = false)
    private BigDecimal subtotal;
    
    /**
     * Discount amount
     */
    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount;
    
    /**
     * Discount percentage
     */
    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;
    
    /**
     * Tax amount
     */
    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount;
    
    /**
     * Tax percentage
     */
    @Column(name = "tax_percentage", precision = 5, scale = 2)
    private BigDecimal taxPercentage;
    
    /**
     * Shipping cost
     */
    @Column(name = "shipping_cost", precision = 15, scale = 2)
    private BigDecimal shippingCost;
    
    /**
     * Other charges
     */
    @Column(name = "other_charges", precision = 15, scale = 2)
    private BigDecimal otherCharges;
    
    /**
     * Total amount (subtotal - discount + tax + shipping + other)
     */
    @Column(name = "total_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmount;
    
    /**
     * Payment terms (e.g., "Net 30", "50% advance, 50% on delivery")
     */
    @Column(name = "payment_terms", length = 255)
    private String paymentTerms;
    
    /**
     * Payment days
     */
    @Column(name = "payment_days")
    private Integer paymentDays;
    
    /**
     * Status (DRAFT, PENDING_APPROVAL, APPROVED, SENT, ACKNOWLEDGED, PARTIAL, COMPLETED, CANCELLED, CLOSED)
     */
    @Column(name = "status", nullable = false, length = 30)
    private String status;
    
    /**
     * Prepared by (user who created the PO)
     */
    @Column(name = "prepared_by", length = 50)
    private String preparedBy;
    
    /**
     * Approved by (user who approved)
     */
    @Column(name = "approved_by", length = 50)
    private String approvedBy;
    
    /**
     * Approval date
     */
    @Column(name = "approval_date")
    private LocalDate approvalDate;
    
    /**
     * Sent date (date PO was sent to supplier)
     */
    @Column(name = "sent_date")
    private LocalDate sentDate;
    
    /**
     * Acknowledged date (date supplier acknowledged)
     */
    @Column(name = "acknowledged_date")
    private LocalDate acknowledgedDate;
    
    /**
     * Terms and conditions
     */
    @Column(name = "terms_and_conditions", columnDefinition = "TEXT")
    private String termsAndConditions;
    
    /**
     * Notes/Remarks
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Internal notes (not visible to supplier)
     */
    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes;
    
    /**
     * Total items count
     */
    @Column(name = "total_items")
    private Integer totalItems;

    // ==================== NEW FIELDS FOR SERVICE LAYER ====================
    
    /**
     * Order number (denormalized for quick access)
     */
    @Column(name = "order_number_direct", length = 50)
    private String orderNumber;
    
    /**
     * Supplier ID (denormalized)
     */
    @Column(name = "supplier_id_direct")
    private Long supplierId;
    
    /**
     * Supplier name (denormalized)
     */
    @Column(name = "supplier_name", length = 200)
    private String supplierName;
    
    /**
     * Warehouse ID (denormalized) - Note: You'll need to add warehouse relationship separately
     */
    @Column(name = "warehouse_id_direct")
    private Long warehouseId;
    
    /**
     * Warehouse name (denormalized)
     */
    @Column(name = "warehouse_name", length = 200)
    private String warehouseName;
    
    /**
     * Order date (alias for poDate)
     */
    @Column(name = "order_date")
    private LocalDate orderDate;
    
    /**
     * Ordered date (when order was placed)
     */
    @Column(name = "ordered_date")
    private LocalDate orderedDate;
    
    /**
     * Order type (STANDARD, URGENT, SPOT)
     */
    @Column(name = "order_type", length = 20)
    private String orderType;
    
    /**
     * Priority (LOW, NORMAL, HIGH, URGENT)
     */
    @Column(name = "priority", length = 20)
    private String priority;
    
    /**
     * Payment status (PENDING, PARTIAL, PAID, OVERPAID)
     */
    @Column(name = "payment_status", length = 20)
    private String paymentStatus;
    
    /**
     * Is approved flag
     */
    @Column(name = "is_approved")
    private Boolean isApproved;
    
    /**
     * Is received flag
     */
    @Column(name = "is_received")
    private Boolean isReceived;
    
    /**
     * Is paid flag
     */
    @Column(name = "is_paid")
    private Boolean isPaid;
    
    /**
     * Approved by user ID
     */
    @Column(name = "approved_by_user_id")
    private Long approvedByUserId;
    
    /**
     * Approval notes
     */
    @Column(name = "approval_notes", length = 500)
    private String approvalNotes;
    
    /**
     * Received date
     */
    @Column(name = "received_date")
    private LocalDate receivedDate;
    
    /**
     * Paid date
     */
    @Column(name = "paid_date")
    private LocalDate paidDate;
    
    /**
     * Subtotal amount (denormalized)
     */
    @Column(name = "subtotal_amount", precision = 15, scale = 2)
    private BigDecimal subtotalAmount;
    
    /**
     * Tax amount direct (denormalized)
     */
    @Column(name = "tax_amount_direct", precision = 15, scale = 2)
    private BigDecimal taxAmountDirect;
    
    /**
     * Shipping amount (denormalized)
     */
    @Column(name = "shipping_amount", precision = 15, scale = 2)
    private BigDecimal shippingAmount;
    
    /**
     * Paid amount
     */
    @Column(name = "paid_amount", precision = 15, scale = 2)
    private BigDecimal paidAmount;
    
    /**
     * Balance amount
     */
    @Column(name = "balance_amount", precision = 15, scale = 2)
    private BigDecimal balanceAmount;
    
    /**
     * Received quantity
     */
    @Column(name = "received_quantity", precision = 15, scale = 3)
    private BigDecimal receivedQuantity;
    
    /**
     * Cancellation reason
     */
    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;
    
    /**
     * Cancelled date
     */
    @Column(name = "cancelled_date")
    private LocalDate cancelledDate;
    
    /**
     * Rejection reason
     */
    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;
    
    /**
     * Rejected date
     */
    @Column(name = "rejected_date")
    private LocalDate rejectedDate;

    // ==================== HELPER METHODS ====================
    
    /**
     * Gets is approved status (null-safe)
     */
    public Boolean getIsApproved() {
        return isApproved != null ? isApproved : false;
    }
    
    /**
     * Gets is received status (null-safe)
     */
    public Boolean getIsReceived() {
        return isReceived != null ? isReceived : false;
    }
    
    /**
     * Gets is paid status (null-safe)
     */
    public Boolean getIsPaid() {
        return isPaid != null ? isPaid : false;
    }
    
    /**
     * Gets supplier ID (from relationship or direct field)
     */
    @Transient
    public Long getSupplierId() {
        if (supplier != null) {
            return supplier.getId();
        }
        return supplierId;
    }
    
    /**
     * Gets warehouse ID (from direct field)
     */
    @Transient
    public Long getWarehouseId() {
        return warehouseId;
    }
    
    /**
     * Gets order date (orderDate or poDate)
     */
    public LocalDate getOrderDate() {
        return orderDate != null ? orderDate : poDate;
    }
    
    /**
     * Gets subtotal amount (subtotalAmount or subtotal)
     */
    public BigDecimal getSubtotalAmount() {
        return subtotalAmount != null ? subtotalAmount : (subtotal != null ? subtotal : BigDecimal.ZERO);
    }
    
    /**
     * Sets subtotal amount from Double
     */
    public void setSubtotalAmount(Double amount) {
        this.subtotalAmount = amount != null ? BigDecimal.valueOf(amount) : BigDecimal.ZERO;
    }
    
    /**
     * Sets subtotal amount from BigDecimal
     */
    public void setSubtotalAmount(BigDecimal amount) {
        this.subtotalAmount = amount;
    }
    
    /**
     * Gets tax amount (taxAmountDirect or taxAmount)
     */
    public BigDecimal getTaxAmount() {
        return taxAmountDirect != null ? taxAmountDirect : (taxAmount != null ? taxAmount : BigDecimal.ZERO);
    }
    
    /**
     * Sets tax amount from Double
     */
    public void setTaxAmount(Double amount) {
        this.taxAmountDirect = amount != null ? BigDecimal.valueOf(amount) : BigDecimal.ZERO;
    }
    
    /**
     * Sets tax amount from BigDecimal
     */
    public void setTaxAmount(BigDecimal amount) {
        this.taxAmountDirect = amount;
    }
    
    /**
     * Gets shipping amount (shippingAmount or shippingCost)
     */
    public BigDecimal getShippingAmount() {
        return shippingAmount != null ? shippingAmount : (shippingCost != null ? shippingCost : BigDecimal.ZERO);
    }
    
    /**
     * Sets shipping amount from Double
     */
    public void setShippingAmount(Double amount) {
        this.shippingAmount = amount != null ? BigDecimal.valueOf(amount) : BigDecimal.ZERO;
    }
    
    /**
     * Sets shipping amount from BigDecimal
     */
    public void setShippingAmount(BigDecimal amount) {
        this.shippingAmount = amount;
    }
    
    /**
     * Gets paid amount (null-safe)
     */
    public BigDecimal getPaidAmount() {
        return paidAmount != null ? paidAmount : BigDecimal.ZERO;
    }
    
    /**
     * Sets paid amount from double
     */
    public void setPaidAmount(double amount) {
        this.paidAmount = BigDecimal.valueOf(amount);
    }
    
    /**
     * Sets paid amount from Double
     */
    public void setPaidAmount(Double amount) {
        this.paidAmount = amount != null ? BigDecimal.valueOf(amount) : BigDecimal.ZERO;
    }
    
    /**
     * Sets paid amount from BigDecimal
     */
    public void setPaidAmount(BigDecimal amount) {
        this.paidAmount = amount;
    }
    
    /**
     * Gets balance amount (calculated or stored)
     */
    public BigDecimal getBalanceAmount() {
        if (balanceAmount != null) {
            return balanceAmount;
        }
        BigDecimal total = totalAmount != null ? totalAmount : BigDecimal.ZERO;
        BigDecimal paid = paidAmount != null ? paidAmount : BigDecimal.ZERO;
        return total.subtract(paid);
    }
    
    /**
     * Sets balance amount from double
     */
    public void setBalanceAmount(double amount) {
        this.balanceAmount = BigDecimal.valueOf(amount);
    }
    
    /**
     * Sets balance amount from BigDecimal
     */
    public void setBalanceAmount(BigDecimal amount) {
        this.balanceAmount = amount;
    }
    
    /**
     * Gets received quantity (null-safe)
     */
    public BigDecimal getReceivedQuantity() {
        return receivedQuantity != null ? receivedQuantity : BigDecimal.ZERO;
    }
    
    /**
     * Sets received quantity from double
     */
    public void setReceivedQuantity(double quantity) {
        this.receivedQuantity = BigDecimal.valueOf(quantity);
    }
    
    /**
     * Sets received quantity from BigDecimal
     */
    public void setReceivedQuantity(BigDecimal quantity) {
        this.receivedQuantity = quantity;
    }

    // ==================== EXISTING RELATIONSHIPS ====================

    /**
     * PO items
     */
    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<PurchaseOrderItem> items = new HashSet<>();
    
    /**
     * Goods receipt notes
     */
    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<GoodsReceiptNote> goodsReceiptNotes = new HashSet<>();
    
    /**
     * Adds a PO item
     */
    public void addItem(PurchaseOrderItem item) {
        item.setPurchaseOrder(this);
        items.add(item);
    }
    
    /**
     * Removes a PO item
     */
    public void removeItem(PurchaseOrderItem item) {
        items.remove(item);
        item.setPurchaseOrder(null);
    }
    
    /**
     * Calculates totals based on items
     */
    @Transient
    public void calculateTotals() {
        if (items == null || items.isEmpty()) {
            subtotal = BigDecimal.ZERO;
            totalAmount = BigDecimal.ZERO;
            return;
        }
        
        // Calculate subtotal
        subtotal = items.stream()
            .map(PurchaseOrderItem::getLineTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Apply discount
        BigDecimal afterDiscount = subtotal;
        if (discountAmount != null && discountAmount.compareTo(BigDecimal.ZERO) > 0) {
            afterDiscount = subtotal.subtract(discountAmount);
        } else if (discountPercentage != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            discountAmount = subtotal.multiply(discountPercentage).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            afterDiscount = subtotal.subtract(discountAmount);
        }
        
        // Apply tax
        BigDecimal afterTax = afterDiscount;
        if (taxAmount != null && taxAmount.compareTo(BigDecimal.ZERO) > 0) {
            afterTax = afterDiscount.add(taxAmount);
        } else if (taxPercentage != null && taxPercentage.compareTo(BigDecimal.ZERO) > 0) {
            taxAmount = afterDiscount.multiply(taxPercentage).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            afterTax = afterDiscount.add(taxAmount);
        }
        
        // Add shipping and other charges
        totalAmount = afterTax;
        if (shippingCost != null) {
            totalAmount = totalAmount.add(shippingCost);
        }
        if (otherCharges != null) {
            totalAmount = totalAmount.add(otherCharges);
        }
    }
    
    /**
     * Checks if PO is pending approval
     */
    @Transient
    public boolean isPendingApproval() {
        return "PENDING_APPROVAL".equals(status);
    }
    
    /**
     * Checks if PO is approved
     */
    @Transient
    public boolean isApproved() {
        return "APPROVED".equals(status) || "SENT".equals(status) || "ACKNOWLEDGED".equals(status) || 
               "PARTIAL".equals(status) || "COMPLETED".equals(status);
    }
    
    /**
     * Checks if PO can be edited
     */
    @Transient
    public boolean canEdit() {
        return "DRAFT".equals(status);
    }
    
    /**
     * Checks if PO can be approved
     */
    @Transient
    public boolean canApprove() {
        return "PENDING_APPROVAL".equals(status);
    }
    
    /**
     * Checks if PO can receive goods
     */
    @Transient
    public boolean canReceiveGoods() {
        return "SENT".equals(status) || "ACKNOWLEDGED".equals(status) || "PARTIAL".equals(status);
    }

    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "DRAFT";
        }
        if (currency == null) {
            currency = "LKR";
        }
        if (exchangeRate == null) {
            exchangeRate = BigDecimal.ONE;
        }
        if (totalItems == null) {
            totalItems = 0;
        }
        // Initialize new fields
        if (isApproved == null) {
            isApproved = false;
        }
        if (isReceived == null) {
            isReceived = false;
        }
        if (isPaid == null) {
            isPaid = false;
        }
        if (paymentStatus == null) {
            paymentStatus = "PENDING";
        }
        if (orderType == null) {
            orderType = "STANDARD";
        }
        if (priority == null) {
            priority = "NORMAL";
        }
        if (receivedQuantity == null) {
            receivedQuantity = BigDecimal.ZERO;
        }
        if (paidAmount == null) {
            paidAmount = BigDecimal.ZERO;
        }
        
        // Sync denormalized fields
        if (orderNumber == null && poNumber != null) {
            orderNumber = poNumber;
        }
        if (supplier != null) {
            if (supplierId == null) {
                supplierId = supplier.getId();
            }
            if (supplierName == null) {
                supplierName = supplier.getName();
            }
        }
        if (orderDate == null && poDate != null) {
            orderDate = poDate;
        }
        if (subtotalAmount == null && subtotal != null) {
            subtotalAmount = subtotal;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Update total items count
        if (items != null) {
            totalItems = items.size();
        }
        // Sync denormalized fields
        if (orderNumber == null && poNumber != null) {
            orderNumber = poNumber;
        }
        if (supplier != null) {
            if (supplierId == null) {
                supplierId = supplier.getId();
            }
            if (supplierName == null) {
                supplierName = supplier.getName();
            }
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PurchaseOrder)) return false;
        PurchaseOrder that = (PurchaseOrder) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
