package lk.epicgreen.erp.payment.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.customer.entity.Customer;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Payment entity
 * Represents customer payments
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "payments", indexes = {
    @Index(name = "idx_payment_number", columnList = "payment_number"),
    @Index(name = "idx_payment_date", columnList = "payment_date"),
    @Index(name = "idx_payment_customer", columnList = "customer_id"),
    @Index(name = "idx_payment_method", columnList = "payment_method"),
    @Index(name = "idx_payment_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Payment number (unique identifier)
     */
    @Column(name = "payment_number", nullable = false, unique = true, length = 50)
    private String paymentNumber;
    
    /**
     * Payment date
     */
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;
    
    /**
     * Customer reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_payment_customer"))
    private Customer customer;
    
    /**
     * Payment method (CASH, CHEQUE, BANK_TRANSFER, CREDIT_CARD, DEBIT_CARD, MOBILE_PAYMENT)
     */
    @Column(name = "payment_method", nullable = false, length = 30)
    private String paymentMethod;
    
    /**
     * Payment amount
     */
    @Column(name = "payment_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal paymentAmount;
    
    /**
     * Allocated amount (sum of allocations to invoices)
     */
    @Column(name = "allocated_amount", precision = 15, scale = 2)
    private BigDecimal allocatedAmount;
    
    /**
     * Unallocated amount (payment amount - allocated amount)
     */
    @Column(name = "unallocated_amount", precision = 15, scale = 2)
    private BigDecimal unallocatedAmount;
    
    /**
     * Currency
     */
    @Column(name = "currency", length = 10)
    private String currency;
    
    /**
     * Exchange rate (for foreign currency)
     */
    @Column(name = "exchange_rate", precision = 15, scale = 6)
    private BigDecimal exchangeRate;
    
    /**
     * Reference number (transaction reference, receipt number, etc.)
     */
    @Column(name = "reference_number", length = 100)
    private String referenceNumber;
    
    /**
     * Bank name (for bank transfers, cheques)
     */
    @Column(name = "bank_name", length = 200)
    private String bankName;
    
    /**
     * Bank account number (customer's bank account)
     */
    @Column(name = "bank_account", length = 50)
    private String bankAccount;
    
    /**
     * Bank branch
     */
    @Column(name = "bank_branch", length = 100)
    private String bankBranch;
    
    /**
     * Cheque reference (for cheque payments)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cheque_id", foreignKey = @ForeignKey(name = "fk_payment_cheque"))
    private Cheque cheque;
    
    /**
     * Status (DRAFT, PENDING, CLEARED, BOUNCED, CANCELLED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Payment cleared date (when payment is cleared/confirmed)
     */
    @Column(name = "cleared_date")
    private LocalDate clearedDate;
    
    /**
     * Is posted to ledger
     */
    @Column(name = "is_posted")
    private Boolean isPosted;
    
    /**
     * Posted date
     */
    @Column(name = "posted_date")
    private LocalDate postedDate;
    
    /**
     * Posted by
     */
    @Column(name = "posted_by", length = 50)
    private String postedBy;
    
    /**
     * Received by (person who received payment)
     */
    @Column(name = "received_by", length = 50)
    private String receivedBy;
    
    /**
     * Receipt number
     */
    @Column(name = "receipt_number", length = 50)
    private String receiptNumber;
    
    /**
     * Description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Customer ID (denormalized for performance)
     */
    @Column(name = "customer_id_direct")
    private Long customerId;
    
    /**
     * Customer name (denormalized for performance)
     */
    @Column(name = "customer_name", length = 200)
    private String customerName;
    
    /**
     * Payment type (alias for paymentMethod)
     */
    @Transient
    private String paymentType;
    
    /**
     * Paid amount (alias for paymentAmount)
     */
    @Transient
    private BigDecimal paidAmount;
    
    /**
     * Is allocated (payment has been allocated to invoices)
     */
    @Column(name = "is_allocated")
    private Boolean isAllocated;
    
    /**
     * Is reconciled (payment has been reconciled)
     */
    @Column(name = "is_reconciled")
    private Boolean isReconciled;
    
    /**
     * Reference type (INVOICE, SALES_ORDER, ADVANCE)
     */
    @Column(name = "reference_type", length = 30)
    private String referenceType;
    
    /**
     * Reference ID
     */
    @Column(name = "reference_id")
    private Long referenceId;
    
    /**
     * Received date (when payment was actually received)
     */
    @Column(name = "received_date")
    private LocalDate receivedDate;
    
    /**
     * Failure reason (for failed payments)
     */
    @Column(name = "failure_reason", length = 500)
    private String failureReason;
    
    /**
     * Failed date
     */
    @Column(name = "failed_date")
    private LocalDate failedDate;
    
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
     * Reconciliation date
     */
    @Column(name = "reconciliation_date")
    private LocalDate reconciliationDate;
    
    /**
     * Bank account ID (for bank transfers)
     */
    @Column(name = "bank_account_id")
    private Long bankAccountId;
    
    /**
     * Cheque ID reference (direct link, denormalized)
     */
    @Transient
    private Long chequeIdDirect;
    
    /**
     * Payment allocations (to invoices)
     */
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    @Builder.Default
    private Set<PaymentAllocation> allocations = new HashSet<>();
    
    /**
     * Adds a payment allocation
     */
    public void addAllocation(PaymentAllocation allocation) {
        allocation.setPayment(this);
        allocations.add(allocation);
    }
    
    /**
     * Removes a payment allocation
     */
    public void removeAllocation(PaymentAllocation allocation) {
        allocations.remove(allocation);
        allocation.setPayment(null);
    }
    
    /**
     * Calculates allocated and unallocated amounts
     */
    @Transient
    public void calculateAllocations() {
        if (allocations == null || allocations.isEmpty()) {
            allocatedAmount = BigDecimal.ZERO;
            unallocatedAmount = paymentAmount;
            return;
        }
        
        allocatedAmount = allocations.stream()
            .map(PaymentAllocation::getAllocatedAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        unallocatedAmount = paymentAmount.subtract(allocatedAmount);
    }
    
    /**
     * Checks if payment is fully allocated
     */
    @Transient
    public boolean isFullyAllocated() {
        if (paymentAmount == null || allocatedAmount == null) {
            return false;
        }
        return allocatedAmount.compareTo(paymentAmount) >= 0;
    }
    
    /**
     * Checks if payment has unallocated amount
     */
    @Transient
    public boolean hasUnallocatedAmount() {
        if (unallocatedAmount == null) {
            return false;
        }
        return unallocatedAmount.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Checks if payment is cleared
     */
    @Transient
    public boolean isCleared() {
        return "CLEARED".equals(status);
    }
    
    /**
     * Checks if payment is bounced (for cheque)
     */
    @Transient
    public boolean isBounced() {
        return "BOUNCED".equals(status);
    }
    
    /**
     * Checks if can be posted to ledger
     */
    @Transient
    public boolean canPost() {
        return "CLEARED".equals(status) && !isPosted;
    }
    
    /**
     * Checks if can be edited
     */
    @Transient
    public boolean canEdit() {
        return "DRAFT".equals(status) || "PENDING".equals(status);
    }
    
    /**
     * Gets payment amount (base method)
     */
    public BigDecimal getAmount() {
        return this.paymentAmount;
    }
    
    /**
     * Sets payment amount (base method)
     */
    public void setAmount(BigDecimal amount) {
        this.paymentAmount = amount;
        this.paidAmount = amount;
    }
    
    /**
     * Gets paid amount (alias for paymentAmount)
     */
    public BigDecimal getPaidAmount() {
        return this.paymentAmount;
    }
    
    /**
     * Sets paid amount (alias for paymentAmount)
     */
    public void setPaidAmount(BigDecimal paidAmount) {
        this.paymentAmount = paidAmount;
        this.paidAmount = paidAmount;
    }
    
    /**
     * Gets payment type (alias for paymentMethod)
     */
    public String getPaymentType() {
        return this.paymentMethod;
    }
    
    /**
     * Sets payment type (alias for paymentMethod)
     */
    public void setPaymentType(String paymentType) {
        this.paymentMethod = paymentType;
        this.paymentType = paymentType;
    }
    
    /**
     * Gets cheque ID
     */
    public Long getChequeId() {
        return cheque != null ? cheque.getId() : chequeIdDirect;
    }
    
    /**
     * Sets cheque ID (for denormalized access)
     */
    public void setChequeId(Long chequeId) {
        this.chequeIdDirect = chequeId;
    }
    
    /**
     * Checks if is cheque payment
     */
    @Transient
    public boolean isChequePayment() {
        return "CHEQUE".equals(paymentMethod);
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
        if (allocatedAmount == null) {
            allocatedAmount = BigDecimal.ZERO;
        }
        if (unallocatedAmount == null) {
            unallocatedAmount = paymentAmount;
        }
        if (isPosted == null) {
            isPosted = false;
        }
        if (isAllocated == null) {
            isAllocated = false;
        }
        if (isReconciled == null) {
            isReconciled = false;
        }
        // Sync customer ID and name from customer object
        if (customer != null) {
            if (customerId == null) {
                customerId = customer.getId();
            }
            if (customerName == null) {
                customerName = customer.getCustomerName();
            }
        }
        // Sync alias fields
        if (paidAmount == null && paymentAmount != null) {
            paidAmount = paymentAmount;
        }
        if (paymentAmount == null && paidAmount != null) {
            paymentAmount = paidAmount;
        }
        if (paymentType == null && paymentMethod != null) {
            paymentType = paymentMethod;
        }
        if (paymentMethod == null && paymentType != null) {
            paymentMethod = paymentType;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Recalculate allocations
        calculateAllocations();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;
        Payment payment = (Payment) o;
        return id != null && id.equals(payment.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
