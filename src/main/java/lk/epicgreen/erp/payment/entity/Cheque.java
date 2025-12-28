package lk.epicgreen.erp.payment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.customer.entity.Customer;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Cheque entity
 * Represents cheque tracking and PDC (Post-Dated Cheque) management
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "cheques", indexes = {
    @Index(name = "idx_cheque_number", columnList = "cheque_number"),
    @Index(name = "idx_cheque_date", columnList = "cheque_date"),
    @Index(name = "idx_customer_id", columnList = "customer_id"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_clearance_date", columnList = "clearance_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cheque extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Payment reference
     */
    @NotNull(message = "Payment is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false, foreignKey = @ForeignKey(name = "fk_cheque_payment"))
    private Payment payment;
    
    /**
     * Cheque number
     */
    @NotBlank(message = "Cheque number is required")
    @Size(max = 50)
    @Column(name = "cheque_number", nullable = false, length = 50)
    private String chequeNumber;
    
    /**
     * Cheque date (date on cheque)
     */
    @NotNull(message = "Cheque date is required")
    @Column(name = "cheque_date", nullable = false)
    private LocalDate chequeDate;
    
    /**
     * Cheque amount
     */
    @NotNull(message = "Cheque amount is required")
    @Positive(message = "Cheque amount must be positive")
    @Column(name = "cheque_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal chequeAmount;
    
    /**
     * Bank name
     */
    @NotBlank(message = "Bank name is required")
    @Size(max = 100)
    @Column(name = "bank_name", nullable = false, length = 100)
    private String bankName;
    
    /**
     * Bank branch
     */
    @Size(max = 100)
    @Column(name = "bank_branch", length = 100)
    private String bankBranch;
    
    /**
     * Account number
     */
    @Size(max = 50)
    @Column(name = "account_number", length = 50)
    private String accountNumber;
    
    /**
     * Customer reference
     */
    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_cheque_customer"))
    private Customer customer;
    
    /**
     * Status (RECEIVED, DEPOSITED, CLEARED, BOUNCED, RETURNED, CANCELLED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Deposit date (when deposited to bank)
     */
    @Column(name = "deposit_date")
    private LocalDate depositDate;
    
    /**
     * Clearance date (when cleared by bank)
     */
    @Column(name = "clearance_date")
    private LocalDate clearanceDate;
    
    /**
     * Bounce reason (if bounced)
     */
    @Column(name = "bounce_reason", columnDefinition = "TEXT")
    private String bounceReason;
    
    /**
     * Bounce charges
     */
    @PositiveOrZero(message = "Bounce charges must be positive or zero")
    @Column(name = "bounce_charges", precision = 15, scale = 2)
    private BigDecimal bounceCharges;
    
    /**
     * Remarks
     */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    /**
     * Status checks
     */
    @Transient
    public boolean isReceived() {
        return "RECEIVED".equals(status);
    }
    
    @Transient
    public boolean isDeposited() {
        return "DEPOSITED".equals(status);
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
    public boolean isReturned() {
        return "RETURNED".equals(status);
    }
    
    @Transient
    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }
    
    /**
     * Check if PDC (Post-Dated Cheque)
     */
    @Transient
    public boolean isPostDated() {
        return chequeDate != null && chequeDate.isAfter(LocalDate.now());
    }
    
    /**
     * Check if overdue (cheque date passed but not cleared)
     */
    @Transient
    public boolean isOverdue() {
        if (isCleared() || isBounced() || isCancelled()) {
            return false;
        }
        return chequeDate != null && chequeDate.isBefore(LocalDate.now());
    }
    
    /**
     * Get days until cheque date
     */
    @Transient
    public long getDaysUntilChequeDate() {
        if (chequeDate == null) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), chequeDate);
    }
    
    /**
     * Get days since deposit
     */
    @Transient
    public long getDaysSinceDeposit() {
        if (depositDate == null) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(depositDate, LocalDate.now());
    }
    
    /**
     * Check if can be deposited
     */
    @Transient
    public boolean canDeposit() {
        return isReceived() && !isPostDated();
    }
    
    /**
     * Workflow methods
     */
    public void deposit() {
        if (!canDeposit()) {
            throw new IllegalStateException("Cheque cannot be deposited");
        }
        this.status = "DEPOSITED";
        this.depositDate = LocalDate.now();
    }
    
    public void clear() {
        if (!isDeposited()) {
            throw new IllegalStateException("Only deposited cheques can be cleared");
        }
        this.status = "CLEARED";
        this.clearanceDate = LocalDate.now();
    }
    
    public void bounce(String reason, BigDecimal charges) {
        if (!isDeposited() && !isCleared()) {
            throw new IllegalStateException("Only deposited or cleared cheques can be bounced");
        }
        this.status = "BOUNCED";
        this.bounceReason = reason;
        this.bounceCharges = charges;
    }
    
    public void returnToCustomer() {
        if (isCleared() || isBounced()) {
            throw new IllegalStateException("Cannot return cleared or bounced cheques");
        }
        this.status = "RETURNED";
    }
    
    public void cancel() {
        if (isCleared()) {
            throw new IllegalStateException("Cannot cancel cleared cheques");
        }
        this.status = "CANCELLED";
    }
    
    /**
     * Get cheque summary
     */
    @Transient
    public String getChequeSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Cheque #").append(chequeNumber);
        summary.append(" - ").append(bankName);
        if (bankBranch != null) {
            summary.append(" (").append(bankBranch).append(")");
        }
        summary.append(" - ").append(chequeAmount);
        summary.append(" - Date: ").append(chequeDate);
        if (isPostDated()) {
            summary.append(" (PDC)");
        }
        return summary.toString();
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "RECEIVED";
        }
        if (bounceCharges == null) {
            bounceCharges = BigDecimal.ZERO;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cheque)) return false;
        Cheque cheque = (Cheque) o;
        return id != null && id.equals(cheque.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
