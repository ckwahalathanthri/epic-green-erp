package lk.epicgreen.erp.accounting.entity;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * JournalEntryLine entity
 * Represents individual lines in journal entries
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "journal_entry_lines", indexes = {
    @Index(name = "idx_journal_id", columnList = "journal_id"),
    @Index(name = "idx_account_id", columnList = "account_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntryLine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Journal entry reference (header)
     */
    @NotNull(message = "Journal entry is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", nullable = false, foreignKey = @ForeignKey(name = "fk_journal_entry_line_journal"))
    private JournalEntry journal;
    
    /**
     * Line number (sequence within journal entry)
     */
    @NotNull(message = "Line number is required")
    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;
    
    /**
     * Account reference
     */
    @NotNull(message = "Account is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(name = "fk_journal_entry_line_account"))
    private ChartOfAccounts account;
    
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
     * Description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * Cost center (optional dimension)
     */
    @Size(max = 50)
    @Column(name = "cost_center", length = 50)
    private String costCenter;
    
    /**
     * Dimension 1 (optional analytical dimension)
     */
    @Size(max = 50)
    @Column(name = "dimension1", length = 50)
    private String dimension1;
    
    /**
     * Dimension 2 (optional analytical dimension)
     */
    @Size(max = 50)
    @Column(name = "dimension2", length = 50)
    private String dimension2;
    
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
    
    /**
     * Get absolute amount
     */
    @Transient
    public BigDecimal getAbsoluteAmount() {
        return getNetAmount().abs();
    }
    
    /**
     * Validate line
     */
    @Transient
    public void validate() {
        BigDecimal debit = debitAmount != null ? debitAmount : BigDecimal.ZERO;
        BigDecimal credit = creditAmount != null ? creditAmount : BigDecimal.ZERO;
        
        if (debit.compareTo(BigDecimal.ZERO) == 0 && credit.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalStateException("Line must have either debit or credit amount");
        }
        
        if (debit.compareTo(BigDecimal.ZERO) > 0 && credit.compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("Line cannot have both debit and credit amounts");
        }
        
        if (account == null) {
            throw new IllegalStateException("Account is required");
        }
        
        if (account.isGroupAccount()) {
            throw new IllegalStateException("Cannot post to group account");
        }
    }
    
    @PrePersist
    @PreUpdate
    protected void onSave() {
        if (debitAmount == null) {
            debitAmount = BigDecimal.ZERO;
        }
        if (creditAmount == null) {
            creditAmount = BigDecimal.ZERO;
        }
        validate();
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
