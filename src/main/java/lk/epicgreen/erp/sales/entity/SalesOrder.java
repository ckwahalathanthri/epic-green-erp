package lk.epicgreen.erp.sales.entity;


import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.entity.CustomerAddress;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * SalesOrder entity
 * Represents customer sales orders
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "sales_orders", indexes = {
    @Index(name = "idx_order_number", columnList = "order_number"),
    @Index(name = "idx_order_date", columnList = "order_date"),
    @Index(name = "idx_customer_id", columnList = "customer_id"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_sales_rep_id", columnList = "sales_rep_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrder extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Order number (unique identifier)
     */
    @NotBlank(message = "Order number is required")
    @Size(max = 30)
    @Column(name = "order_number", nullable = false, unique = true, length = 30)
    private String orderNumber;
    
    /**
     * Order date
     */
    @NotNull(message = "Order date is required")
    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;
    
    /**
     * Customer reference
     */
    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sales_order_customer"))
    private Customer customer;

    @Column
    private String DeliveryStatus;

    @Column
    private String PaymentStatus;
    
    /**
     * Customer PO number
     */
    @Size(max = 50)
    @Column(name = "customer_po_number", length = 50)
    private String customerPoNumber;

    @Column
    private String priority;


    /**
     * Customer PO date
     */
    @Column(name = "customer_po_date")
    private LocalDate customerPoDate;
    
    /**
     * Billing address
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_address_id", foreignKey = @ForeignKey(name = "fk_sales_order_billing_address"))
    private CustomerAddress billingAddress;
    
    /**
     * Shipping address
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_address_id", foreignKey = @ForeignKey(name = "fk_sales_order_shipping_address"))
    private CustomerAddress shippingAddress;
    
    /**
     * Warehouse (fulfillment location)
     */
    @NotNull(message = "Warehouse is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sales_order_warehouse"))
    private Warehouse warehouse;
    
    /**
     * Sales representative
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_rep_id", foreignKey = @ForeignKey(name = "fk_sales_order_sales_rep"))
    private User salesRep;
    
    /**
     * Order type (REGULAR, URGENT, ADVANCE_ORDER)
     */
    @Column(name = "order_type", length = 20)
    private String orderType;
    
    /**
     * Status (DRAFT, CONFIRMED, PENDING_APPROVAL, APPROVED, PROCESSING, PACKED, DISPATCHED, DELIVERED, CANCELLED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Payment mode (CASH, CHEQUE, CREDIT, BANK_TRANSFER)
     */
    @NotBlank(message = "Payment mode is required")
    @Column(name = "payment_mode", nullable = false, length = 20)
    private String paymentMode;
    
    /**
     * Delivery mode (SELF_PICKUP, COMPANY_DELIVERY, COURIER)
     */
    @Column(name = "delivery_mode", length = 20)
    private String deliveryMode;
    
    /**
     * Expected delivery date
     */
    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;
    
    /**
     * Subtotal (sum of line totals before tax)
     */
    @PositiveOrZero(message = "Subtotal must be positive or zero")
    @Column(name = "subtotal", precision = 15, scale = 2)
    private BigDecimal subtotal;
    
    /**
     * Tax amount
     */
    @PositiveOrZero(message = "Tax amount must be positive or zero")
    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount;
    
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
     * Freight/delivery charges
     */
    @PositiveOrZero(message = "Freight charges must be positive or zero")
    @Column(name = "freight_charges", precision = 15, scale = 2)
    private BigDecimal freightCharges;
    
    /**
     * Total amount
     */
    @PositiveOrZero(message = "Total amount must be positive or zero")
    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;
    
    /**
     * Approved by (user)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by", foreignKey = @ForeignKey(name = "fk_sales_order_approved_by"))
    private User approvedBy;
    
    /**
     * Approval timestamp
     */
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    
    /**
     * Remarks
     */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    /**
     * Order items
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SalesOrderItem> items = new ArrayList<>();
    
    /**
     * Add order item
     */
    public void addItem(SalesOrderItem item) {
        item.setOrder(this);
        items.add(item);
    }
    
    /**
     * Remove order item
     */
    public void removeItem(SalesOrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }
    
    /**
     * Status checks
     */
    @Transient
    public boolean isDraft() {
        return "DRAFT".equals(status);
    }
    
    @Transient
    public boolean isConfirmed() {
        return "CONFIRMED".equals(status);
    }
    
    @Transient
    public boolean isPendingApproval() {
        return "PENDING_APPROVAL".equals(status);
    }
    
    @Transient
    public boolean isApproved() {
        return "APPROVED".equals(status);
    }
    
    @Transient
    public boolean isProcessing() {
        return "PROCESSING".equals(status);
    }
    
    @Transient
    public boolean isPacked() {
        return "PACKED".equals(status);
    }
    
    @Transient
    public boolean isDispatched() {
        return "DISPATCHED".equals(status);
    }
    
    @Transient
    public boolean isDelivered() {
        return "DELIVERED".equals(status);
    }
    
    @Transient
    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }
    
    /**
     * Order type checks
     */
    @Transient
    public boolean isRegular() {
        return "REGULAR".equals(orderType);
    }
    
    @Transient
    public boolean isUrgent() {
        return "URGENT".equals(orderType);
    }
    
    @Transient
    public boolean isAdvanceOrder() {
        return "ADVANCE_ORDER".equals(orderType);
    }
    
    /**
     * Payment mode checks
     */
    @Transient
    public boolean isCashPayment() {
        return "CASH".equals(paymentMode);
    }
    
    @Transient
    public boolean isChequePayment() {
        return "CHEQUE".equals(paymentMode);
    }
    
    @Transient
    public boolean isCreditPayment() {
        return "CREDIT".equals(paymentMode);
    }
    
    @Transient
    public boolean isBankTransfer() {
        return "BANK_TRANSFER".equals(paymentMode);
    }
    
    /**
     * Permission checks
     */
    @Transient
    public boolean canEdit() {
        return isDraft();
    }
    
    @Transient
    public boolean canApprove() {
        return isPendingApproval() || isConfirmed();
    }
    
    @Transient
    public boolean canCancel() {
        return !isDelivered() && !isCancelled();
    }
    
    /**
     * Calculate totals
     */
    @Transient
    public void calculateTotals() {
        // Calculate subtotal from items
        subtotal = items.stream()
                .map(SalesOrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate discount amount if percentage is set
        if (discountPercentage != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            discountAmount = subtotal.multiply(discountPercentage)
                    .divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
        }
        
        // Calculate total
        BigDecimal subtotalAfterDiscount = subtotal.subtract(discountAmount != null ? discountAmount : BigDecimal.ZERO);
        BigDecimal tax = taxAmount != null ? taxAmount : BigDecimal.ZERO;
        BigDecimal freight = freightCharges != null ? freightCharges : BigDecimal.ZERO;
        totalAmount = subtotalAfterDiscount.add(tax).add(freight);
    }
    
    /**
     * Workflow methods
     */
    public void confirm() {
        if (!isDraft()) {
            throw new IllegalStateException("Only draft orders can be confirmed");
        }
        this.status = "CONFIRMED";
    }
    
    public void submitForApproval() {
        if (!isConfirmed()) {
            throw new IllegalStateException("Only confirmed orders can be submitted for approval");
        }
        this.status = "PENDING_APPROVAL";
    }
    
    public void approve(User approver) {
        if (!isPendingApproval() && !isConfirmed()) {
            throw new IllegalStateException("Only pending or confirmed orders can be approved");
        }
        this.status = "APPROVED";
        this.approvedBy = approver;
        this.approvedAt = LocalDateTime.now();
    }
    
    public void startProcessing() {
        if (!isApproved()) {
            throw new IllegalStateException("Only approved orders can be processed");
        }
        this.status = "PROCESSING";
    }
    
    public void markAsPacked() {
        if (!isProcessing()) {
            throw new IllegalStateException("Only processing orders can be packed");
        }
        this.status = "PACKED";
    }
    
    public void dispatch() {
        if (!isPacked()) {
            throw new IllegalStateException("Only packed orders can be dispatched");
        }
        this.status = "DISPATCHED";
    }
    
    public void markAsDelivered() {
        if (!isDispatched()) {
            throw new IllegalStateException("Only dispatched orders can be marked as delivered");
        }
        this.status = "DELIVERED";
    }
    
    public void cancel() {
        if (!canCancel()) {
            throw new IllegalStateException("This order cannot be cancelled");
        }
        this.status = "CANCELLED";
    }
    

    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "DRAFT";
        }
        if (orderType == null) {
            orderType = "REGULAR";
        }
        if (deliveryMode == null) {
            deliveryMode = "COMPANY_DELIVERY";
        }
        if (subtotal == null) {
            subtotal = BigDecimal.ZERO;
        }
        if (taxAmount == null) {
            taxAmount = BigDecimal.ZERO;
        }
        if (discountPercentage == null) {
            discountPercentage = BigDecimal.ZERO;
        }
        if (discountAmount == null) {
            discountAmount = BigDecimal.ZERO;
        }
        if (freightCharges == null) {
            freightCharges = BigDecimal.ZERO;
        }
        if (totalAmount == null) {
            totalAmount = BigDecimal.ZERO;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesOrder)) return false;
        SalesOrder that = (SalesOrder) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
