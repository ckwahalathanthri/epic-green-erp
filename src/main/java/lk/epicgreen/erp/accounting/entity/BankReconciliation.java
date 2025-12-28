package lk.epicgreen.erp.accounting.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * BankReconciliation entity
 * Represents bank reconciliation statements
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "bank_reconciliations", indexes = {
    @Index(name = "idx_reconciliation_number", columnList = "reconciliation_number"),
    @Index(name = "idx_bank_account_id", columnList = "bank_account_id"),
    @Index(name = "idx_statement_date", columnList = "statement_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankReconciliation extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Reconciliation number (unique identifier)
     */
    @NotBlank(message = "Reconciliation number is required")
    @Size(max = 30)
    @Column(name = "reconciliation_number", nullable = false, unique = true, length = 30)
    private String reconciliationNumber;
    
    /**
     * Bank account
     */
    @NotNull(message = "Bank account is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id", nullable = false, foreignKey = @ForeignKey(name = "fk_bank_reconciliation_bank_account"))
    private BankAccount bankAccount;
    
    /**
     * Bank statement date
     */
    @NotNull(message = "Statement date is required")
    @Column(name = "statement_date", nullable = false)
    private LocalDate statementDate;
    
    /**
     * Bank statement balance (as per bank)
     */
    @NotNull(message = "Statement balance is required")
    @Column(name = "statement_balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal statementBalance;
    
    /**
     * Book balance (as per books)
     */
    @NotNull(message = "Book balance is required")
    @Column(name = "book_balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal bookBalance;
    
    /**
     * Reconciled balance (after adjustments)
     */
    @Column(name = "reconciled_balance", precision = 15, scale = 2)
    private BigDecimal reconciledBalance;
    
    /**
     * Status (DRAFT, IN_PROGRESS, COMPLETED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Reconciled by (user)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reconciled_by", foreignKey = @ForeignKey(name = "fk_bank_reconciliation_reconciled_by"))
    private User reconciledBy;
    
    /**
     * Reconciled timestamp
     */
    @Column(name = "reconciled_at")
    private LocalDateTime reconciledAt;
    
    /**
     * Remarks
     */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    /**
     * Status checks
     */
    @Transient
    public boolean isDraft() {
        return "DRAFT".equals(status);
    }
    
    @Transient
    public boolean isInProgress() {
        return "IN_PROGRESS".equals(status);
    }
    
    @Transient
    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }
    
    /**
     * Get difference (statement - book balance)
     */
    @Transient
    public BigDecimal getDifference() {
        BigDecimal statement = statementBalance != null ? statementBalance : BigDecimal.ZERO;
        BigDecimal book = bookBalance != null ? bookBalance : BigDecimal.ZERO;
        return statement.subtract(book);
    }
    
    /**
     * Check if reconciled
     */
    @Transient
    public boolean isReconciled() {
        return isCompleted() && getDifference().compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * Check if has difference
     */
    @Transient
    public boolean hasDifference() {
        return getDifference().compareTo(BigDecimal.ZERO) != 0;
    }
    
    /**
     * Get absolute difference
     */
    @Transient
    public BigDecimal getAbsoluteDifference() {
        return getDifference().abs();
    }
    
    /**
     * Workflow methods
     */
    public void startReconciliation() {
        if (!isDraft()) {
            throw new IllegalStateException("Only draft reconciliations can be started");
        }
        this.status = "IN_PROGRESS";
    }
    
    public void complete(User user) {
        if (!isInProgress()) {
            throw new IllegalStateException("Only in-progress reconciliations can be completed");
        }
        this.status = "COMPLETED";
        this.reconciledBy = user;
        this.reconciledAt = LocalDateTime.now();
        this.reconciledBalance = statementBalance;
    }
    
    public void reopen() {
        if (!isCompleted()) {
            throw new IllegalStateException("Only completed reconciliations can be reopened");
        }
        this.status = "IN_PROGRESS";
        this.reconciledBy = null;
        this.reconciledAt = null;
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "DRAFT";
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankReconciliation)) return false;
        BankReconciliation that = (BankReconciliation) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
