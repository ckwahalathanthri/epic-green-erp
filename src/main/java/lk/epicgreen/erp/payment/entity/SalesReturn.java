package lk.epicgreen.erp.payment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.sales.entity.Invoice;
import lk.epicgreen.erp.sales.entity.SalesOrder;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * SalesReturn entity
 * Represents customer returns of products
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "sales_returns", indexes = {
    @Index(name = "idx_return_number", columnList = "return_number"),
    @Index(name = "idx_return_date", columnList = "return_date"),
    @Index(name = "idx_customer_id", columnList = "customer_id"),
    @Index(name = "idx_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesReturn extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Return number (unique identifier)
     */
    @NotBlank(message = "Return number is required")
    @Size(max = 30)
    @Column(name = "return_number", nullable = false, unique = true, length = 30)
    private String returnNumber;
    
    /**
     * Return date
     */
    @NotNull(message = "Return date is required")
    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;
    
    /**
     * Original sales order (optional)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_sales_return_order"))
    private SalesOrder order;
    
    /**
     * Original invoice (optional)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", foreignKey = @ForeignKey(name = "fk_sales_return_invoice"))
    private Invoice invoice;
    
    /**
     * Customer reference
     */
    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sales_return_customer"))
    private Customer customer;
    
    /**
     * Warehouse (receiving returns)
     */
    @NotNull(message = "Warehouse is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sales_return_warehouse"))
    private Warehouse warehouse;
    
    /**
     * Return type (QUALITY_ISSUE, WRONG_PRODUCT, DAMAGED, EXPIRED, CUSTOMER_REQUEST, OTHER)
     */
    @NotBlank(message = "Return type is required")
    @Column(name = "return_type", nullable = false, length = 20)
    private String returnType;
    
    /**
     * Status (PENDING, APPROVED, REJECTED, RECEIVED, PROCESSED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Approved by (user)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by", foreignKey = @ForeignKey(name = "fk_sales_return_approved_by"))
    private User approvedBy;
    
    /**
     * Approval timestamp
     */
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    
    /**
     * Subtotal
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
     * Total amount
     */
    @PositiveOrZero(message = "Total amount must be positive or zero")
    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;
    
    /**
     * Refund mode (CASH, CHEQUE, CREDIT_NOTE, BANK_TRANSFER)
     */
    @Column(name = "refund_mode", length = 20)
    private String refundMode;
    
    /**
     * Refund status (PENDING, PROCESSED, COMPLETED)
     */
    @Column(name = "refund_status", length = 20)
    private String refundStatus;
    
    /**
     * Remarks
     */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    /**
     * Return items
     */
    @OneToMany(mappedBy = "salesReturn", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SalesReturnItem> items = new ArrayList<>();
    
    /**
     * Add return item
     */
    public void addItem(SalesReturnItem item) {
        item.setSalesReturn(this);
        items.add(item);
    }
    
    /**
     * Remove return item
     */
    public void removeItem(SalesReturnItem item) {
        items.remove(item);
        item.setSalesReturn(null);
    }
    
    /**
     * Status checks
     */
    @Transient
    public boolean isPending() {
        return "PENDING".equals(status);
    }
    
    @Transient
    public boolean isApproved() {
        return "APPROVED".equals(status);
    }
    
    @Transient
    public boolean isRejected() {
        return "REJECTED".equals(status);
    }
    
    @Transient
    public boolean isReceived() {
        return "RECEIVED".equals(status);
    }
    
    @Transient
    public boolean isProcessed() {
        return "PROCESSED".equals(status);
    }
    
    /**
     * Return type checks
     */
    @Transient
    public boolean isQualityIssue() {
        return "QUALITY_ISSUE".equals(returnType);
    }
    
    @Transient
    public boolean isWrongProduct() {
        return "WRONG_PRODUCT".equals(returnType);
    }
    
    @Transient
    public boolean isDamaged() {
        return "DAMAGED".equals(returnType);
    }
    
    @Transient
    public boolean isExpired() {
        return "EXPIRED".equals(returnType);
    }
    
    @Transient
    public boolean isCustomerRequest() {
        return "CUSTOMER_REQUEST".equals(returnType);
    }
    
    /**
     * Refund mode checks
     */
    @Transient
    public boolean isCashRefund() {
        return "CASH".equals(refundMode);
    }
    
    @Transient
    public boolean isChequeRefund() {
        return "CHEQUE".equals(refundMode);
    }
    
    @Transient
    public boolean isCreditNote() {
        return "CREDIT_NOTE".equals(refundMode);
    }
    
    @Transient
    public boolean isBankTransfer() {
        return "BANK_TRANSFER".equals(refundMode);
    }
    
    /**
     * Refund status checks
     */
    @Transient
    public boolean isRefundPending() {
        return "PENDING".equals(refundStatus);
    }
    
    @Transient
    public boolean isRefundProcessed() {
        return "PROCESSED".equals(refundStatus);
    }
    
    @Transient
    public boolean isRefundCompleted() {
        return "COMPLETED".equals(refundStatus);
    }
    
    /**
     * Calculate totals
     */
    @Transient
    public void calculateTotals() {
        subtotal = items.stream()
                .map(SalesReturnItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        taxAmount = items.stream()
                .map(SalesReturnItem::getTaxAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        totalAmount = subtotal.add(taxAmount != null ? taxAmount : BigDecimal.ZERO);
    }
    
    /**
     * Workflow methods
     */
    public void approve(User approver) {
        if (!isPending()) {
            throw new IllegalStateException("Only pending returns can be approved");
        }
        this.status = "APPROVED";
        this.approvedBy = approver;
        this.approvedAt = LocalDateTime.now();
    }
    
    public void reject() {
        if (!isPending()) {
            throw new IllegalStateException("Only pending returns can be rejected");
        }
        this.status = "REJECTED";
    }
    
    public void markAsReceived() {
        if (!isApproved()) {
            throw new IllegalStateException("Only approved returns can be received");
        }
        this.status = "RECEIVED";
    }
    
    public void process() {
        if (!isReceived()) {
            throw new IllegalStateException("Only received returns can be processed");
        }
        this.status = "PROCESSED";
    }
    
    public void processRefund() {
        if (!isProcessed()) {
            throw new IllegalStateException("Return must be processed before refund");
        }
        this.refundStatus = "PROCESSED";
    }
    
    public void completeRefund() {
        if (!isRefundProcessed()) {
            throw new IllegalStateException("Refund must be processed before completion");
        }
        this.refundStatus = "COMPLETED";
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "PENDING";
        }
        if (refundMode == null) {
            refundMode = "CREDIT_NOTE";
        }
        if (refundStatus == null) {
            refundStatus = "PENDING";
        }
        if (subtotal == null) {
            subtotal = BigDecimal.ZERO;
        }
        if (taxAmount == null) {
            taxAmount = BigDecimal.ZERO;
        }
        if (totalAmount == null) {
            totalAmount = BigDecimal.ZERO;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesReturn)) return false;
        SalesReturn that = (SalesReturn) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
