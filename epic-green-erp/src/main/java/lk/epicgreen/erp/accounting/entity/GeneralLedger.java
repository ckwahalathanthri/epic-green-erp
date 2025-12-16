package lk.epicgreen.erp.accounting.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * GeneralLedger entity
 * Represents general ledger entries with running balance
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "general_ledger", indexes = {
    @Index(name = "idx_general_ledger_account", columnList = "account_id"),
    @Index(name = "idx_general_ledger_period", columnList = "financial_period_id"),
    @Index(name = "idx_general_ledger_date", columnList = "transaction_date"),
    @Index(name = "idx_general_ledger_entry", columnList = "journal_entry_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralLedger extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Account reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(name = "fk_general_ledger_account"))
    private ChartOfAccounts account;
    
    /**
     * Financial period
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financial_period_id", nullable = false, foreignKey = @ForeignKey(name = "fk_general_ledger_period"))
    private FinancialPeriod financialPeriod;
    
    /**
     * Journal entry reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id", foreignKey = @ForeignKey(name = "fk_general_ledger_entry"))
    private JournalEntry journalEntry;
    
    /**
     * Journal entry line reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_line_id", foreignKey = @ForeignKey(name = "fk_general_ledger_line"))
    private JournalEntryLine journalEntryLine;
    
    /**
     * Transaction date
     */
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;
    
    /**
     * Transaction type (OPENING, SALES, PURCHASE, PAYMENT, RECEIPT, ADJUSTMENT, CLOSING, OTHER)
     */
    @Column(name = "transaction_type", length = 30)
    private String transactionType;
    
    /**
     * Reference number (e.g., journal entry number, invoice number)
     */
    @Column(name = "reference_number", length = 100)
    private String referenceNumber;
    
    /**
     * Reference ID (reference to source document)
     */
    @Column(name = "reference_id")
    private Long referenceId;
    
    /**
     * Reference type (e.g., JOURNAL_ENTRY, SALES_INVOICE, PAYMENT)
     */
    @Column(name = "reference_type", length = 50)
    private String referenceType;
    
    /**
     * Debit amount
     */
    @Column(name = "debit_amount", precision = 15, scale = 2)
    private BigDecimal debitAmount;
    
    /**
     * Credit amount
     */
    @Column(name = "credit_amount", precision = 15, scale = 2)
    private BigDecimal creditAmount;
    
    /**
     * Balance (running balance after this transaction)
     */
    @Column(name = "balance", precision = 15, scale = 2)
    private BigDecimal balance;
    
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
     * Base currency debit amount
     */
    @Column(name = "base_debit_amount", precision = 15, scale = 2)
    private BigDecimal baseDebitAmount;
    
    /**
     * Base currency credit amount
     */
    @Column(name = "base_credit_amount", precision = 15, scale = 2)
    private BigDecimal baseCreditAmount;
    
    /**
     * Base currency balance
     */
    @Column(name = "base_balance", precision = 15, scale = 2)
    private BigDecimal baseBalance;
    
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
     * Is reconciled
     */
    @Column(name = "is_reconciled")
    private Boolean isReconciled;
    
    /**
     * Reconciliation date
     */
    @Column(name = "reconciliation_date")
    private LocalDate reconciliationDate;
    
    /**
     * Gets net amount (debit - credit)
     */
    @Transient
    public BigDecimal getNetAmount() {
        BigDecimal debit = debitAmount != null ? debitAmount : BigDecimal.ZERO;
        BigDecimal credit = creditAmount != null ? creditAmount : BigDecimal.ZERO;
        return debit.subtract(credit);
    }
    
    /**
     * Checks if is debit entry
     */
    @Transient
    public boolean isDebitEntry() {
        BigDecimal debit = debitAmount != null ? debitAmount : BigDecimal.ZERO;
        BigDecimal credit = creditAmount != null ? creditAmount : BigDecimal.ZERO;
        return debit.compareTo(credit) > 0;
    }
    
    /**
     * Checks if is credit entry
     */
    @Transient
    public boolean isCreditEntry() {
        BigDecimal debit = debitAmount != null ? debitAmount : BigDecimal.ZERO;
        BigDecimal credit = creditAmount != null ? creditAmount : BigDecimal.ZERO;
        return credit.compareTo(debit) > 0;
    }
    
    /**
     * Calculates base currency amounts
     */
    @Transient
    public void calculateBaseAmounts() {
        if (exchangeRate == null || exchangeRate.compareTo(BigDecimal.ZERO) == 0) {
            exchangeRate = BigDecimal.ONE;
        }
        
        if (debitAmount != null) {
            baseDebitAmount = debitAmount.multiply(exchangeRate);
        } else {
            baseDebitAmount = BigDecimal.ZERO;
        }
        
        if (creditAmount != null) {
            baseCreditAmount = creditAmount.multiply(exchangeRate);
        } else {
            baseCreditAmount = BigDecimal.ZERO;
        }
        
        if (balance != null) {
            baseBalance = balance.multiply(exchangeRate);
        }
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (debitAmount == null) {
            debitAmount = BigDecimal.ZERO;
        }
        if (creditAmount == null) {
            creditAmount = BigDecimal.ZERO;
        }
        if (currency == null) {
            currency = "LKR";
        }
        if (exchangeRate == null) {
            exchangeRate = BigDecimal.ONE;
        }
        if (isReconciled == null) {
            isReconciled = false;
        }
        
        // Calculate base amounts
        calculateBaseAmounts();
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Recalculate base amounts
        calculateBaseAmounts();
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
