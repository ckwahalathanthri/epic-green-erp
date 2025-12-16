package lk.epicgreen.erp.returns.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.sales.entity.Invoice;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * CreditNote entity
 * Represents credit notes issued for sales returns
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "credit_notes", indexes = {
    @Index(name = "idx_credit_note_number", columnList = "credit_note_number"),
    @Index(name = "idx_credit_note_date", columnList = "credit_note_date"),
    @Index(name = "idx_credit_note_customer", columnList = "customer_id"),
    @Index(name = "idx_credit_note_invoice", columnList = "invoice_id"),
    @Index(name = "idx_credit_note_return", columnList = "sales_return_id"),
    @Index(name = "idx_credit_note_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditNote extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Credit note number (unique identifier)
     */
    @Column(name = "credit_note_number", nullable = false, unique = true, length = 50)
    private String creditNoteNumber;
    
    /**
     * Credit note date
     */
    @Column(name = "credit_note_date", nullable = false)
    private LocalDate creditNoteDate;
    
    /**
     * Customer reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_credit_note_customer"))
    private Customer customer;
    
    /**
     * Original invoice reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", foreignKey = @ForeignKey(name = "fk_credit_note_invoice"))
    private Invoice invoice;
    
    /**
     * Sales return reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_return_id", foreignKey = @ForeignKey(name = "fk_credit_note_return"))
    private SalesReturn salesReturn;
    
    /**
     * Credit note type (SALES_RETURN, PRICE_ADJUSTMENT, DISCOUNT, DAMAGE_CLAIM, OTHER)
     */
    @Column(name = "credit_note_type", length = 30)
    private String creditNoteType;
    
    /**
     * Reason for credit note
     */
    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;
    
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
     * Credit amount (amount credited to customer)
     */
    @Column(name = "credit_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal creditAmount;
    
    /**
     * Applied amount (amount applied to outstanding invoices)
     */
    @Column(name = "applied_amount", precision = 15, scale = 2)
    private BigDecimal appliedAmount;
    
    /**
     * Unapplied amount (credit amount - applied amount)
     */
    @Column(name = "unapplied_amount", precision = 15, scale = 2)
    private BigDecimal unappliedAmount;
    
    /**
     * Status (DRAFT, PENDING_APPROVAL, APPROVED, APPLIED, CANCELLED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
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
     * Is posted to ledger
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
     * Terms and conditions
     */
    @Column(name = "terms_and_conditions", columnDefinition = "TEXT")
    private String termsAndConditions;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Internal notes
     */
    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes;

    /**
     * Is approved flag
     */
    @Column(name = "is_approved")
    private Boolean isApproved;

    /**
     * Rejection reason
     */
    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    /**
     * Rejected date
     */
    @Column(name = "rejected_date")
    private LocalDate rejectedDate;

    /**
     * Remaining credit amount (unapplied amount)
     */
    @Column(name = "remaining_credit_amount", precision = 15, scale = 2)
    private BigDecimal remainingCreditAmount;

    /**
     * Is applied flag
     */
    @Column(name = "is_applied")
    private Boolean isApplied;

    /**
     * Applied date
     */
    @Column(name = "applied_date")
    private LocalDate appliedDate;

    /**
     * Is refunded flag
     */
    @Column(name = "is_refunded")
    private Boolean isRefunded;

    /**
     * Refunded date
     */
    @Column(name = "refunded_date")
    private LocalDate refundedDate;

    /**
     * Refund type (CASH, BANK_TRANSFER, CHECK, etc.)
     */
    @Column(name = "refund_type", length = 30)
    private String refundType;

    /**
     * Calculates unapplied amount
     */
    @Transient
    public void calculateUnappliedAmount() {
        if (creditAmount == null) {
            unappliedAmount = BigDecimal.ZERO;
            return;
        }
        
        BigDecimal applied = appliedAmount != null ? appliedAmount : BigDecimal.ZERO;
        unappliedAmount = creditAmount.subtract(applied);
    }
    
    /**
     * Checks if credit note is fully applied
     */
    @Transient
    public boolean isFullyApplied() {
        if (creditAmount == null || appliedAmount == null) {
            return false;
        }
        return appliedAmount.compareTo(creditAmount) >= 0;
    }
    
    /**
     * Checks if credit note has unapplied amount
     */
    @Transient
    public boolean hasUnappliedAmount() {
        if (unappliedAmount == null) {
            return false;
        }
        return unappliedAmount.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Checks if can be posted to ledger
     */
    @Transient
    public boolean canPost() {
        return "APPROVED".equals(status) && !isPosted;
    }
    
    /**
     * Checks if can be edited
     */
    @Transient
    public boolean canEdit() {
        return "DRAFT".equals(status);
    }
    
    /**
     * Gets application percentage
     */
    @Transient
    public BigDecimal getApplicationPercentage() {
        if (creditAmount == null || creditAmount.compareTo(BigDecimal.ZERO) == 0 || appliedAmount == null) {
            return BigDecimal.ZERO;
        }
        return appliedAmount.divide(creditAmount, 4, RoundingMode.HALF_UP)
                           .multiply(new BigDecimal("100"));
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "DRAFT";
        }
        if (creditNoteType == null) {
            creditNoteType = "SALES_RETURN";
        }
        if (currency == null) {
            currency = "LKR";
        }
        if (exchangeRate == null) {
            exchangeRate = BigDecimal.ONE;
        }
        if (appliedAmount == null) {
            appliedAmount = BigDecimal.ZERO;
        }
        if (isPosted == null) {
            isPosted = false;
        }
        if (isApproved == null) {
            isApproved = false;
        }
        if (isApplied == null) {
            isApplied = false;
        }
        if (isRefunded == null) {
            isRefunded = false;
        }

        // Calculate unapplied amount
        calculateUnappliedAmount();
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Recalculate unapplied amount
        calculateUnappliedAmount();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreditNote)) return false;
        CreditNote that = (CreditNote) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
