package lk.epicgreen.erp.accounting.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.math.BigDecimal;

/**
 * JournalEntryLine entity
 * Represents line items in journal entries (debits and credits)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "journal_entry_lines", indexes = {
    @Index(name = "idx_journal_entry_line_entry", columnList = "journal_entry_id"),
    @Index(name = "idx_journal_entry_line_account", columnList = "account_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntryLine extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Journal entry reference (header)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id", nullable = false, foreignKey = @ForeignKey(name = "fk_journal_entry_line_entry"))
    private JournalEntry journalEntry;
    
    /**
     * Line number (sequence)
     */
    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;
    
    /**
     * Account reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(name = "fk_journal_entry_line_account"))
    private ChartOfAccounts account;
    
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
     * Base currency debit amount (converted to base currency)
     */
    @Column(name = "base_debit_amount", precision = 15, scale = 2)
    private BigDecimal baseDebitAmount;
    
    /**
     * Base currency credit amount (converted to base currency)
     */
    @Column(name = "base_credit_amount", precision = 15, scale = 2)
    private BigDecimal baseCreditAmount;
    
    /**
     * Description
     */
    @Column(name = "description", length = 500)
    private String description;
    
    /**
     * Notes
     */
    @Column(name = "notes", length = 500)
    private String notes;
    
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
        if (debitAmount == null && creditAmount == null) {
            return false;
        }
        BigDecimal debit = debitAmount != null ? debitAmount : BigDecimal.ZERO;
        BigDecimal credit = creditAmount != null ? creditAmount : BigDecimal.ZERO;
        return debit.compareTo(credit) > 0;
    }
    
    /**
     * Checks if is credit entry
     */
    @Transient
    public boolean isCreditEntry() {
        if (debitAmount == null && creditAmount == null) {
            return false;
        }
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
        
        // Calculate base amounts
        calculateBaseAmounts();
        
        // Validate: Either debit or credit must be non-zero, but not both
        if (debitAmount.compareTo(BigDecimal.ZERO) > 0 && creditAmount.compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("A line cannot have both debit and credit amounts");
        }
        
        if (debitAmount.compareTo(BigDecimal.ZERO) == 0 && creditAmount.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalStateException("A line must have either debit or credit amount");
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Recalculate base amounts
        calculateBaseAmounts();
        
        // Validate: Either debit or credit must be non-zero, but not both
        BigDecimal debit = debitAmount != null ? debitAmount : BigDecimal.ZERO;
        BigDecimal credit = creditAmount != null ? creditAmount : BigDecimal.ZERO;
        
        if (debit.compareTo(BigDecimal.ZERO) > 0 && credit.compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("A line cannot have both debit and credit amounts");
        }
        
        if (debit.compareTo(BigDecimal.ZERO) == 0 && credit.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalStateException("A line must have either debit or credit amount");
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JournalEntryLine)) return false;
        JournalEntryLine that = (JournalEntryLine) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
