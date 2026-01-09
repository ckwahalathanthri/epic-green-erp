package lk.epicgreen.erp.accounting.entity;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * GeneralLedger entity
 * Represents detailed general ledger entries for all accounts
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "general_ledger", indexes = {
    @Index(name = "idx_transaction_date", columnList = "transaction_date"),
    @Index(name = "idx_account_id", columnList = "account_id"),
    @Index(name = "idx_journal_id", columnList = "journal_id"),
    @Index(name = "idx_period_id", columnList = "period_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralLedger {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Transaction date
     */
    @NotNull(message = "Transaction date is required")
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;
    
    /**
     * Financial period
     */
    @NotNull(message = "Period is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", nullable = false, foreignKey = @ForeignKey(name = "fk_general_ledger_period"))
    private FinancialPeriod period;
    
    /**
     * Account
     */
    @NotNull(message = "Account is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(name = "fk_general_ledger_account"))
    private ChartOfAccounts account;
    
    /**
     * Journal entry
     */
    @NotNull(message = "Journal entry is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", nullable = false, foreignKey = @ForeignKey(name = "fk_general_ledger_journal"))
    private JournalEntry journal;
    
    /**
     * Journal entry line
     */
    @NotNull(message = "Journal entry line is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_line_id", nullable = false, foreignKey = @ForeignKey(name = "fk_general_ledger_journal_line"))
    private JournalEntryLine journalLine;
    
    /**
     * Description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * Debit amount
     */
    @PositiveOrZero(message = "Debit amount must be positive or zero")
    @Column(name = "debit_amount", precision = 15, scale = 2)
    private BigDecimal debitAmount;
    
    /**
     * Credit amount
     */
    @PositiveOrZero(message = "Credit amount must be positive or zero")
    @Column(name = "credit_amount", precision = 15, scale = 2)
    private BigDecimal creditAmount;
    
    /**
     * Running balance
     */
    @NotNull(message = "Balance is required")
    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;
    
    /**
     * Source type (e.g., SALES_ORDER, INVOICE, PAYMENT)
     */
    @Size(max = 50)
    @Column(name = "source_type", length = 50)
    private String sourceType;
    
    /**
     * Source ID
     */
    @Column(name = "source_id")
    private Long sourceId;
    
    /**
     * Created timestamp
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /**
     * Check if debit entry
     */
    @Transient
    public boolean isDebit() {
        BigDecimal debit = debitAmount != null ? debitAmount : BigDecimal.ZERO;
        BigDecimal credit = creditAmount != null ? creditAmount : BigDecimal.ZERO;
        return debit.compareTo(credit) > 0;
    }
    
    /**
     * Check if credit entry
     */
    @Transient
    public boolean isCredit() {
        BigDecimal debit = debitAmount != null ? debitAmount : BigDecimal.ZERO;
        BigDecimal credit = creditAmount != null ? creditAmount : BigDecimal.ZERO;
        return credit.compareTo(debit) > 0;
    }
    
    /**
     * Get net amount (debit - credit)
     */
    @Transient
    public BigDecimal getNetAmount() {
        BigDecimal debit = debitAmount != null ? debitAmount : BigDecimal.ZERO;
        BigDecimal credit = creditAmount != null ? creditAmount : BigDecimal.ZERO;
        return debit.subtract(credit);
    }
    
    @PrePersist
    protected void onCreate() {
        if (debitAmount == null) {
            debitAmount = BigDecimal.ZERO;
        }
        if (creditAmount == null) {
            creditAmount = BigDecimal.ZERO;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        // General ledger entries should be immutable after creation
        throw new IllegalStateException("General ledger entries cannot be modified after creation");
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeneralLedger)) return false;
        GeneralLedger that = (GeneralLedger) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
