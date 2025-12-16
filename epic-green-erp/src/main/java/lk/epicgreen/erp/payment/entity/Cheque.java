package lk.epicgreen.erp.payment.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.customer.entity.Customer;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Cheque entity
 * Represents cheque payment details
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "cheques", indexes = {
    @Index(name = "idx_cheque_number", columnList = "cheque_number"),
    @Index(name = "idx_cheque_date", columnList = "cheque_date"),
    @Index(name = "idx_cheque_customer", columnList = "customer_id"),
    @Index(name = "idx_cheque_status", columnList = "status"),
    @Index(name = "idx_cheque_deposit_date", columnList = "deposit_date")
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
     * Cheque number
     */
    @Column(name = "cheque_number", nullable = false, length = 50)
    private String chequeNumber;
    
    /**
     * Cheque date
     */
    @Column(name = "cheque_date", nullable = false)
    private LocalDate chequeDate;
    
    /**
     * Customer reference (payer)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_cheque_customer"))
    private Customer customer;
    
    /**
     * Bank name
     */
    @Column(name = "bank_name", nullable = false, length = 200)
    private String bankName;
    
    /**
     * Bank branch
     */
    @Column(name = "bank_branch", length = 100)
    private String bankBranch;
    
    /**
     * Bank account number
     */
    @Column(name = "bank_account", length = 50)
    private String bankAccount;
    
    /**
     * Cheque amount
     */
    @Column(name = "cheque_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal chequeAmount;
    
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
     * Payee name (usually company name)
     */
    @Column(name = "payee_name", length = 200)
    private String payeeName;
    
    /**
     * Cheque type (ACCOUNT_PAYEE, BEARER, ORDER)
     */
    @Column(name = "cheque_type", length = 20)
    private String chequeType;
    
    /**
     * Status (RECEIVED, DEPOSITED, CLEARED, BOUNCED, CANCELLED, RETURNED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Received date
     */
    @Column(name = "received_date")
    private LocalDate receivedDate;
    
    /**
     * Received by
     */
    @Column(name = "received_by", length = 50)
    private String receivedBy;
    
    /**
     * Deposit date (when deposited to bank)
     */
    @Column(name = "deposit_date")
    private LocalDate depositDate;
    
    /**
     * Deposited by
     */
    @Column(name = "deposited_by", length = 50)
    private String depositedBy;
    
    /**
     * Deposit bank (our bank where cheque is deposited)
     */
    @Column(name = "deposit_bank", length = 200)
    private String depositBank;
    
    /**
     * Deposit account (our bank account)
     */
    @Column(name = "deposit_account", length = 50)
    private String depositAccount;
    
    /**
     * Deposit reference number
     */
    @Column(name = "deposit_reference", length = 100)
    private String depositReference;
    
    /**
     * Cleared date (when cheque is cleared)
     */
    @Column(name = "cleared_date")
    private LocalDate clearedDate;
    
    /**
     * Bounced date (if cheque bounced)
     */
    @Column(name = "bounced_date")
    private LocalDate bouncedDate;
    
    /**
     * Bounce reason
     */
    @Column(name = "bounce_reason", length = 500)
    private String bounceReason;
    
    /**
     * Bank charges (for bounced cheques)
     */
    @Column(name = "bank_charges", precision = 15, scale = 2)
    private BigDecimal bankCharges;
    
    /**
     * Is post-dated cheque
     */
    @Column(name = "is_post_dated")
    private Boolean isPostDated;
    
    /**
     * Maturity date (for post-dated cheques)
     */
    @Column(name = "maturity_date")
    private LocalDate maturityDate;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Checks if cheque is cleared
     */
    @Transient
    public boolean isCleared() {
        return "CLEARED".equals(status);
    }
    
    /**
     * Checks if cheque is bounced
     */
    @Transient
    public boolean isBounced() {
        return "BOUNCED".equals(status);
    }
    
    /**
     * Checks if cheque is deposited
     */
    @Transient
    public boolean isDeposited() {
        return "DEPOSITED".equals(status) || "CLEARED".equals(status);
    }
    
    /**
     * Checks if cheque can be deposited
     */
    @Transient
    public boolean canDeposit() {
        if ("RECEIVED".equals(status)) {
            // If post-dated, check maturity date
            if (isPostDated && maturityDate != null) {
                return !LocalDate.now().isBefore(maturityDate);
            }
            return true;
        }
        return false;
    }
    
    /**
     * Checks if cheque is post-dated and not yet mature
     */
    @Transient
    public boolean isPendingMaturity() {
        if (!isPostDated || maturityDate == null) {
            return false;
        }
        return LocalDate.now().isBefore(maturityDate);
    }
    
    /**
     * Gets days to maturity (for post-dated cheques)
     */
    @Transient
    public Long getDaysToMaturity() {
        if (!isPendingMaturity()) {
            return 0L;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), maturityDate);
    }
    
    /**
     * Gets days since deposit
     */
    @Transient
    public Long getDaysSinceDeposit() {
        if (depositDate == null) {
            return 0L;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(depositDate, LocalDate.now());
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "RECEIVED";
        }
        if (currency == null) {
            currency = "LKR";
        }
        if (exchangeRate == null) {
            exchangeRate = BigDecimal.ONE;
        }
        if (chequeType == null) {
            chequeType = "ACCOUNT_PAYEE";
        }
        if (isPostDated == null) {
            isPostDated = false;
        }
        if (receivedDate == null) {
            receivedDate = LocalDate.now();
        }
        
        // Check if post-dated
        if (chequeDate != null && chequeDate.isAfter(LocalDate.now())) {
            isPostDated = true;
            maturityDate = chequeDate;
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
