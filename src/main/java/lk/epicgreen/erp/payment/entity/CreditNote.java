package lk.epicgreen.erp.payment.entity;


import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.sales.entity.Invoice;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * CreditNote entity
 * Represents credit notes issued to customers for returns or adjustments
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "credit_notes", indexes = {
    @Index(name = "idx_credit_note_number", columnList = "credit_note_number"),
    @Index(name = "idx_credit_note_date", columnList = "credit_note_date"),
    @Index(name = "idx_customer_id", columnList = "customer_id"),
    @Index(name = "idx_status", columnList = "status")
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
    @NotBlank(message = "Credit note number is required")
    @Size(max = 30)
    @Column(name = "credit_note_number", nullable = false, unique = true, length = 30)
    private String creditNoteNumber;
    
    /**
     * Credit note date
     */
    @NotNull(message = "Credit note date is required")
    @Column(name = "credit_note_date", nullable = false)
    private LocalDate creditNoteDate;
    
    /**
     * Customer reference
     */
    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_credit_note_customer"))
    private Customer customer;
    
    /**
     * Sales return reference (if applicable)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_id", foreignKey = @ForeignKey(name = "fk_credit_note_return"))
    private SalesReturn salesReturn;
    
    /**
     * Original invoice reference (if applicable)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", foreignKey = @ForeignKey(name = "fk_credit_note_invoice"))
    private Invoice invoice;
    
    /**
     * Reason (SALES_RETURN, DISCOUNT_ADJUSTMENT, BILLING_ERROR, GOODWILL, OTHER)
     */
    @NotBlank(message = "Reason is required")
    @Column(name = "reason", nullable = false, length = 30)
    private String reason;
    
    /**
     * Subtotal
     */
    @NotNull(message = "Subtotal is required")
    @PositiveOrZero(message = "Subtotal must be positive or zero")
    @Column(name = "subtotal", nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal;
    
    /**
     * Tax amount
     */
    @PositiveOrZero(message = "Tax amount must be positive or zero")
    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount;
    
    /**
     * Total amount
     */
    @NotNull(message = "Total amount is required")
    @PositiveOrZero(message = "Total amount must be positive or zero")
    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;
    
    /**
     * Utilized amount (used against invoices)
     */
    @PositiveOrZero(message = "Utilized amount must be positive or zero")
    @Column(name = "utilized_amount", precision = 15, scale = 2)
    private BigDecimal utilizedAmount;
    
    /**
     * Status (DRAFT, ISSUED, UTILIZED, EXPIRED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Expiry date
     */
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    
    /**
     * Remarks
     */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    /**
     * Get balance amount (calculated field)
     */
    @Transient
    public BigDecimal getBalanceAmount() {
        BigDecimal total = totalAmount != null ? totalAmount : BigDecimal.ZERO;
        BigDecimal utilized = utilizedAmount != null ? utilizedAmount : BigDecimal.ZERO;
        return total.subtract(utilized);
    }
    
    /**
     * Status checks
     */
    @Transient
    public boolean isDraft() {
        return "DRAFT".equals(status);
    }
    
    @Transient
    public boolean isIssued() {
        return "ISSUED".equals(status);
    }
    
    @Transient
    public boolean isUtilized() {
        return "UTILIZED".equals(status);
    }
    
    @Transient
    public boolean isExpired() {
        return "EXPIRED".equals(status);
    }
    
    /**
     * Reason checks
     */
    @Transient
    public boolean isSalesReturn() {
        return "SALES_RETURN".equals(reason);
    }
    
    @Transient
    public boolean isDiscountAdjustment() {
        return "DISCOUNT_ADJUSTMENT".equals(reason);
    }
    
    @Transient
    public boolean isBillingError() {
        return "BILLING_ERROR".equals(reason);
    }
    
    @Transient
    public boolean isGoodwill() {
        return "GOODWILL".equals(reason);
    }
    
    /**
     * Check if fully utilized
     */
    @Transient
    public boolean isFullyUtilized() {
        return getBalanceAmount().compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * Check if partially utilized
     */
    @Transient
    public boolean isPartiallyUtilized() {
        BigDecimal utilized = utilizedAmount != null ? utilizedAmount : BigDecimal.ZERO;
        return utilized.compareTo(BigDecimal.ZERO) > 0 && !isFullyUtilized();
    }
    
    /**
     * Check if expired
     */
    @Transient
    public boolean isExpiredNow() {
        if (expiryDate == null) {
            return false;
        }
        return LocalDate.now().isAfter(expiryDate);
    }
    
    /**
     * Check if can be utilized
     */
    @Transient
    public boolean canBeUtilized() {
        return isIssued() && !isExpiredNow() && getBalanceAmount().compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Workflow methods
     */
    public void issue() {
        if (!isDraft()) {
            throw new IllegalStateException("Only draft credit notes can be issued");
        }
        this.status = "ISSUED";
    }
    
    public void utilize(BigDecimal amount) {
        if (!canBeUtilized()) {
            throw new IllegalStateException("Credit note cannot be utilized");
        }
        
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Utilization amount must be positive");
        }
        
        BigDecimal currentUtilized = utilizedAmount != null ? utilizedAmount : BigDecimal.ZERO;
        BigDecimal newUtilized = currentUtilized.add(amount);
        
        if (newUtilized.compareTo(totalAmount) > 0) {
            throw new IllegalStateException("Cannot utilize more than total amount");
        }
        
        this.utilizedAmount = newUtilized;
        
        // Update status if fully utilized
        if (isFullyUtilized()) {
            this.status = "UTILIZED";
        }
    }
    
    public void expire() {
        if (isUtilized() || isExpired()) {
            throw new IllegalStateException("Cannot expire utilized or already expired credit notes");
        }
        this.status = "EXPIRED";
    }
    
    /**
     * Calculate totals
     */
    @Transient
    public void calculateTotals() {
        BigDecimal sub = subtotal != null ? subtotal : BigDecimal.ZERO;
        BigDecimal tax = taxAmount != null ? taxAmount : BigDecimal.ZERO;
        totalAmount = sub.add(tax);
    }

    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "DRAFT";
        }
        if (taxAmount == null) {
            taxAmount = BigDecimal.ZERO;
        }
        if (utilizedAmount == null) {
            utilizedAmount = BigDecimal.ZERO;
        }
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
