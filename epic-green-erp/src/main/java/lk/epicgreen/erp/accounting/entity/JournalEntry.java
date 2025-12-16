package lk.epicgreen.erp.accounting.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * JournalEntry entity
 * Represents journal entry headers for double-entry bookkeeping
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "journal_entries", indexes = {
    @Index(name = "idx_journal_entry_number", columnList = "entry_number"),
    @Index(name = "idx_journal_entry_date", columnList = "entry_date"),
    @Index(name = "idx_journal_entry_period", columnList = "financial_period_id"),
    @Index(name = "idx_journal_entry_status", columnList = "status"),
    @Index(name = "idx_journal_entry_type", columnList = "entry_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntry extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Entry number (unique identifier)
     */
    @Column(name = "entry_number", nullable = false, unique = true, length = 50)
    private String entryNumber;
    
    /**
     * Entry date
     */
    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;
    
    /**
     * Financial period
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financial_period_id", nullable = false, foreignKey = @ForeignKey(name = "fk_journal_entry_period"))
    private FinancialPeriod financialPeriod;
    
    /**
     * Entry type (MANUAL, SALES, PURCHASE, PAYMENT, RECEIPT, ADJUSTMENT, CLOSING, OPENING, OTHER)
     */
    @Column(name = "entry_type", nullable = false, length = 30)
    private String entryType;
    
    /**
     * Reference number (e.g., invoice number, payment number)
     */
    @Column(name = "reference_number", length = 100)
    private String referenceNumber;
    
    /**
     * Reference ID (reference to source document)
     */
    @Column(name = "reference_id")
    private Long referenceId;
    
    /**
     * Reference type (e.g., SALES_INVOICE, PAYMENT, PURCHASE)
     */
    @Column(name = "reference_type", length = 50)
    private String referenceType;
    
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
     * Total debit amount
     */
    @Column(name = "total_debit", precision = 15, scale = 2)
    private BigDecimal totalDebit;
    
    /**
     * Total credit amount
     */
    @Column(name = "total_credit", precision = 15, scale = 2)
    private BigDecimal totalCredit;
    
    /**
     * Difference (should be zero for valid entry)
     */
    @Column(name = "difference", precision = 15, scale = 2)
    private BigDecimal difference;
    
    /**
     * Status (DRAFT, PENDING_APPROVAL, APPROVED, POSTED, REJECTED, CANCELLED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Is posted to general ledger
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
     * Approved by
     */
    @Column(name = "approved_by", length = 50)
    private String approvedBy;
    
    /**
     * Approval date
     */
    @Column(name = "approval_date")
    private LocalDate approvalDate;
    
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
     * Total lines count
     */
    @Column(name = "total_lines")
    private Integer totalLines;
    
    /**
     * Journal entry lines
     */
    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("lineNumber ASC")
    @Builder.Default
    private Set<JournalEntryLine> lines = new HashSet<>();
    
    /**
     * Adds a journal entry line
     */
    public void addLine(JournalEntryLine line) {
        line.setJournalEntry(this);
        lines.add(line);
    }
    
    /**
     * Removes a journal entry line
     */
    public void removeLine(JournalEntryLine line) {
        lines.remove(line);
        line.setJournalEntry(null);
    }
    
    /**
     * Calculates totals from lines
     */
    @Transient
    public void calculateTotals() {
        if (lines == null || lines.isEmpty()) {
            totalDebit = BigDecimal.ZERO;
            totalCredit = BigDecimal.ZERO;
            difference = BigDecimal.ZERO;
            return;
        }
        
        totalDebit = lines.stream()
            .map(JournalEntryLine::getDebitAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        totalCredit = lines.stream()
            .map(JournalEntryLine::getCreditAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        difference = totalDebit.subtract(totalCredit);
    }
    
    /**
     * Checks if entry is balanced (debit = credit)
     */
    @Transient
    public boolean isBalanced() {
        if (difference == null) {
            calculateTotals();
        }
        return difference.compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * Checks if can be posted
     */
    @Transient
    public boolean canPost() {
        return "APPROVED".equals(status) && !isPosted && isBalanced();
    }
    
    /**
     * Checks if can be edited
     */
    @Transient
    public boolean canEdit() {
        return "DRAFT".equals(status);
    }
    
    /**
     * Checks if requires approval
     */
    @Transient
    public boolean requiresApproval() {
        return "MANUAL".equals(entryType);
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
        if (isPosted == null) {
            isPosted = false;
        }
        if (totalLines == null) {
            totalLines = 0;
        }
        
        // Auto-set entry type based on reference type
        if (entryType == null && referenceType != null) {
            if (referenceType.contains("INVOICE")) {
                entryType = "SALES";
            } else if (referenceType.contains("PURCHASE")) {
                entryType = "PURCHASE";
            } else if (referenceType.contains("PAYMENT")) {
                entryType = "PAYMENT";
            } else {
                entryType = "OTHER";
            }
        }
        
        if (entryType == null) {
            entryType = "MANUAL";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Update total lines count
        if (lines != null) {
            totalLines = lines.size();
        }
        
        // Recalculate totals
        calculateTotals();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JournalEntry)) return false;
        JournalEntry that = (JournalEntry) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
