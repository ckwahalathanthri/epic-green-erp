package lk.epicgreen.erp.payment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.customer.entity.Customer;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Payment entity
 * Represents customer payments (cash, cheque, bank transfer, etc.)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "payments", indexes = {
    @Index(name = "idx_payment_number", columnList = "payment_number"),
    @Index(name = "idx_payment_date", columnList = "payment_date"),
    @Index(name = "idx_customer_id", columnList = "customer_id"),
    @Index(name = "idx_payment_mode", columnList = "payment_mode"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_cheque_number", columnList = "cheque_number")
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
    @NotBlank(message = "Payment number is required")
    @Size(max = 30)
    @Column(name = "payment_number", nullable = false, unique = true, length = 30)
    private String paymentNumber;
    
    /**
     * Payment date
     */
    @NotNull(message = "Payment date is required")
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;
    
    /**
     * Customer reference
     */
    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_payment_customer"))
    private Customer customer;
    
    /**
     * Payment mode (CASH, CHEQUE, BANK_TRANSFER, CREDIT_CARD, ONLINE)
     */
    @NotBlank(message = "Payment mode is required")
    @Column(name = "payment_mode", nullable = false, length = 20)
    private String paymentMode;
    
    /**
     * Total amount received
     */
    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be positive")
    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;
    
    /**
     * Allocated amount (allocated to invoices)
     */
    @PositiveOrZero(message = "Allocated amount must be positive or zero")
    @Column(name = "allocated_amount", precision = 15, scale = 2)
    private BigDecimal allocatedAmount;
    
    /**
     * Status (DRAFT, PENDING, CLEARED, BOUNCED, CANCELLED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Bank name (for bank/cheque payments)
     */
    @Size(max = 100)
    @Column(name = "bank_name", length = 100)
    private String bankName;
    
    /**
     * Bank branch
     */
    @Size(max = 100)
    @Column(name = "bank_branch", length = 100)
    private String bankBranch;
    
    /**
     * Cheque number
     */
    @Size(max = 50)
    @Column(name = "cheque_number", length = 50)
    private String chequeNumber;
    
    /**
     * Cheque date
     */
    @Column(name = "cheque_date")
    private LocalDate chequeDate;
    
    /**
     * Cheque clearance date
     */
    @Column(name = "cheque_clearance_date")
    private LocalDate chequeClearanceDate;
    
    /**
     * Bank reference number (for bank transfers)
     */
    @Size(max = 50)
    @Column(name = "bank_reference_number", length = 50)
    private String bankReferenceNumber;
    
    /**
     * Collected by (user who collected payment)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collected_by", foreignKey = @ForeignKey(name = "fk_payment_collected_by"))
    private User collectedBy;
    
    /**
     * Collection timestamp
     */
    @Column(name = "collected_at")
    private LocalDateTime collectedAt;
    
    /**
     * Approved by (user who approved payment)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by", foreignKey = @ForeignKey(name = "fk_payment_approved_by"))
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
     * Payment allocations
     */
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PaymentAllocation> allocations = new ArrayList<>();
    
    /**
     * Add payment allocation
     */
    public void addAllocation(PaymentAllocation allocation) {
        allocation.setPayment(this);
        allocations.add(allocation);
    }
    
    /**
     * Remove payment allocation
     */
    public void removeAllocation(PaymentAllocation allocation) {
        allocations.remove(allocation);
        allocation.setPayment(null);
    }
    
    /**
     * Get unallocated amount (calculated field)
     */
    @Transient
    public BigDecimal getUnallocatedAmount() {
        BigDecimal total = totalAmount != null ? totalAmount : BigDecimal.ZERO;
        BigDecimal allocated = allocatedAmount != null ? allocatedAmount : BigDecimal.ZERO;
        return total.subtract(allocated);
    }
    
    /**
     * Status checks
     */
    @Transient
    public boolean isDraft() {
        return "DRAFT".equals(status);
    }
    
    @Transient
    public boolean isPending() {
        return "PENDING".equals(status);
    }
    
    @Transient
    public boolean isCleared() {
        return "CLEARED".equals(status);
    }
    
    @Transient
    public boolean isBounced() {
        return "BOUNCED".equals(status);
    }
    
    @Transient
    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }
    
    /**
     * Payment mode checks
     */
    @Transient
    public boolean isCash() {
        return "CASH".equals(paymentMode);
    }
    
    @Transient
    public boolean isCheque() {
        return "CHEQUE".equals(paymentMode);
    }
    
    @Transient
    public boolean isBankTransfer() {
        return "BANK_TRANSFER".equals(paymentMode);
    }
    
    @Transient
    public boolean isCreditCard() {
        return "CREDIT_CARD".equals(paymentMode);
    }
    
    @Transient
    public boolean isOnline() {
        return "ONLINE".equals(paymentMode);
    }
    
    /**
     * Check if fully allocated
     */
    @Transient
    public boolean isFullyAllocated() {
        return getUnallocatedAmount().compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * Check if partially allocated
     */
    @Transient
    public boolean isPartiallyAllocated() {
        BigDecimal allocated = allocatedAmount != null ? allocatedAmount : BigDecimal.ZERO;
        return allocated.compareTo(BigDecimal.ZERO) > 0 && !isFullyAllocated();
    }
    
    /**
     * Check if can be edited
     */
    @Transient
    public boolean canEdit() {
        return isDraft();
    }
    
    /**
     * Check if can be cancelled
     */
    @Transient
    public boolean canCancel() {
        return !isCancelled() && allocatedAmount.compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * Workflow methods
     */
    public void submit() {
        if (!isDraft()) {
            throw new IllegalStateException("Only draft payments can be submitted");
        }
        this.status = "PENDING";
    }
    
    public void clear() {
        if (!isPending()) {
            throw new IllegalStateException("Only pending payments can be cleared");
        }
        this.status = "CLEARED";
        if (isCheque() && chequeClearanceDate == null) {
            this.chequeClearanceDate = LocalDate.now();
        }
    }
    
    public void markAsBounced() {
        if (!isPending() && !isCleared()) {
            throw new IllegalStateException("Only pending or cleared payments can be bounced");
        }
        this.status = "BOUNCED";
    }
    
    public void cancel() {
        if (!canCancel()) {
            throw new IllegalStateException("Cannot cancel this payment");
        }
        this.status = "CANCELLED";
    }
    
    public void approve(User approver) {
        if (isCancelled()) {
            throw new IllegalStateException("Cannot approve cancelled payment");
        }
        this.approvedBy = approver;
        this.approvedAt = LocalDateTime.now();
    }
    
    /**
     * Allocate amount to invoice
     */
    public void allocate(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Allocation amount must be positive");
        }
        
        BigDecimal currentAllocated = allocatedAmount != null ? allocatedAmount : BigDecimal.ZERO;
        BigDecimal newAllocated = currentAllocated.add(amount);
        
        if (newAllocated.compareTo(totalAmount) > 0) {
            throw new IllegalStateException("Cannot allocate more than total amount");
        }
        
        this.allocatedAmount = newAllocated;
    }
    
    /**
     * Deallocate amount
     */
    public void deallocate(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deallocation amount must be positive");
        }
        
        BigDecimal currentAllocated = allocatedAmount != null ? allocatedAmount : BigDecimal.ZERO;
        BigDecimal newAllocated = currentAllocated.subtract(amount);
        
        if (newAllocated.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Cannot deallocate more than allocated amount");
        }
        
        this.allocatedAmount = newAllocated;
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "DRAFT";
        }
        if (allocatedAmount == null) {
            allocatedAmount = BigDecimal.ZERO;
        }
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
