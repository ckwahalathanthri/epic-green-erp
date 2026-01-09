package lk.epicgreen.erp.accounting.entity;



import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JournalEntry entity
 * Represents accounting journal entries (header)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "journal_entries", indexes = {
    @Index(name = "idx_journal_number", columnList = "journal_number"),
    @Index(name = "idx_journal_date", columnList = "journal_date"),
    @Index(name = "idx_period_id", columnList = "period_id"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_source", columnList = "source_type, source_id")
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
     * Journal number (unique identifier)
     */
    @NotBlank(message = "Journal number is required")
    @Size(max = 30)
    @Column(name = "journal_number", nullable = false, unique = true, length = 30)
    private String journalNumber;
    
    /**
     * Journal date
     */
    @NotNull(message = "Journal date is required")
    @Column(name = "journal_date", nullable = false)
    private LocalDate journalDate;
    
    /**
     * Financial period
     */
    @NotNull(message = "Period is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", nullable = false, foreignKey = @ForeignKey(name = "fk_journal_entry_period"))
    private FinancialPeriod period;
    
    /**
     * Entry type (MANUAL, AUTOMATED, OPENING_BALANCE, CLOSING, ADJUSTMENT)
     */
    @Column(name = "entry_type", length = 20)
    private String entryType;
    
    /**
     * Source type (e.g., SALES_ORDER, INVOICE, PAYMENT)
     */
    @Size(max = 50)
    @Column(name = "source_type", length = 50)
    private String sourceType;
    
    /**
     * Source ID (source document ID)
     */
    @Column(name = "source_id")
    private Long sourceId;
    
    /**
     * Source reference (source document number)
     */
    @Size(max = 50)
    @Column(name = "source_reference", length = 50)
    private String sourceReference;

    @Column(name = "is_posted")
    private Boolean isPosted;

    @Column(name = "is_reversed")
    private Boolean IsReversed;
    
    /**
     * Description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * Total debit amount
     */
    @NotNull(message = "Total debit is required")
    @PositiveOrZero(message = "Total debit must be positive or zero")
    @Column(name = "total_debit", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalDebit;
    
    /**
     * Total credit amount
     */
    @NotNull(message = "Total credit is required")
    @PositiveOrZero(message = "Total credit must be positive or zero")
    @Column(name = "total_credit", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalCredit;
    
    /**
     * Status (DRAFT, POSTED, CANCELLED)
     */
    @Column(name = "status", nullable = false, length = 10)
    private String status;
    
    /**
     * Posted by (user)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_by", foreignKey = @ForeignKey(name = "fk_journal_entry_posted_by"))
    private User postedBy;
    
    /**
     * Posted timestamp
     */
    @Column(name = "posted_at")
    private LocalDateTime postedAt;
    
    /**
     * Approved by (user)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by", foreignKey = @ForeignKey(name = "fk_journal_entry_approved_by"))
    private User approvedBy;
    
    /**
     * Approval timestamp
     */
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    
    /**
     * Journal entry lines
     */
    @OneToMany(mappedBy = "journal", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("lineNumber ASC")
    @Builder.Default
    private List<JournalEntryLine> lines = new ArrayList<>();
    
    /**
     * Add journal line
     */
    public void addLine(JournalEntryLine line) {
        line.setJournal(this);
        lines.add(line);
    }

    
    /**
     * Remove journal line
     */
    public void removeLine(JournalEntryLine line) {
        lines.remove(line);
        line.setJournal(null);
    }
    
    /**
     * Status checks
     */
    @Transient
    public boolean isDraft() {
        return "DRAFT".equals(status);
    }
    
    @Transient
    public boolean isPosted() {
        return "POSTED".equals(status);
    }
    
    @Transient
    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }
    
    /**
     * Entry type checks
     */
    @Transient
    public boolean isManual() {
        return "MANUAL".equals(entryType);
    }
    
    @Transient
    public boolean isAutomated() {
        return "AUTOMATED".equals(entryType);
    }
    
    @Transient
    public boolean isOpeningBalance() {
        return "OPENING_BALANCE".equals(entryType);
    }
    
    @Transient
    public boolean isClosing() {
        return "CLOSING".equals(entryType);
    }
    
    @Transient
    public boolean isAdjustment() {
        return "ADJUSTMENT".equals(entryType);
    }
    
    /**
     * Check if balanced (debit = credit)
     */
    @Transient
    public boolean isBalanced() {
        if (totalDebit == null || totalCredit == null) {
            return false;
        }
        return totalDebit.compareTo(totalCredit) == 0;
    }
    
    /**
     * Get difference (debit - credit)
     */
    @Transient
    public BigDecimal getDifference() {
        BigDecimal debit = totalDebit != null ? totalDebit : BigDecimal.ZERO;
        BigDecimal credit = totalCredit != null ? totalCredit : BigDecimal.ZERO;
        return debit.subtract(credit);
    }
    
    /**
     * Check if can be edited
     */
    @Transient
    public boolean canEdit() {
        return isDraft();
    }
    
    /**
     * Check if can be posted
     */
    @Transient
    public boolean canPost() {
        return isDraft() && isBalanced() && period != null && period.canPost();
    }
    
    /**
     * Calculate totals from lines
     */
    @Transient
    public void calculateTotals() {
        totalDebit = lines.stream()
                .map(JournalEntryLine::getDebitAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        totalCredit = lines.stream()
                .map(JournalEntryLine::getCreditAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Validate entry
     */
    @Transient
    public void validate() {
        if (lines.isEmpty()) {
            throw new IllegalStateException("Journal entry must have at least one line");
        }
        
        calculateTotals();
        
        if (!isBalanced()) {
            throw new IllegalStateException("Journal entry is not balanced. Difference: " + getDifference());
        }
        
        if (period == null) {
            throw new IllegalStateException("Period is required");
        }
        
        if (!period.canPost()) {
            throw new IllegalStateException("Cannot post to this period");
        }
    }
    
    /**
     * Post journal entry
     */
    public void post(User user) {
        if (!canPost()) {
            throw new IllegalStateException("Journal entry cannot be posted");
        }
        
        validate();
        
        this.status = "POSTED";
        this.postedBy = user;
        this.postedAt = LocalDateTime.now();
    }
    
    /**
     * Cancel journal entry
     */
    public void cancel() {
        if (isCancelled()) {
            throw new IllegalStateException("Journal entry is already cancelled");
        }
        this.status = "CANCELLED";
    }
    
    /**
     * Approve journal entry
     */
    public void approve(User user) {
        this.approvedBy = user;
        this.approvedAt = LocalDateTime.now();
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "DRAFT";
        }
        if (entryType == null) {
            entryType = "MANUAL";
        }
        if (totalDebit == null) {
            totalDebit = BigDecimal.ZERO;
        }
        if (totalCredit == null) {
            totalCredit = BigDecimal.ZERO;
        }
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
