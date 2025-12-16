package lk.epicgreen.erp.payment.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.sales.entity.Invoice;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * PaymentAllocation entity
 * Represents allocation of payments to specific invoices
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "payment_allocations", indexes = {
    @Index(name = "idx_payment_allocation_payment", columnList = "payment_id"),
    @Index(name = "idx_payment_allocation_invoice", columnList = "invoice_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentAllocation extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Payment reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false, foreignKey = @ForeignKey(name = "fk_payment_allocation_payment"))
    private Payment payment;
    
    /**
     * Invoice reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false, foreignKey = @ForeignKey(name = "fk_payment_allocation_invoice"))
    private Invoice invoice;
    
    /**
     * Invoice number (for reference)
     */
    @Column(name = "invoice_number", length = 50)
    private String invoiceNumber;
    
    /**
     * Invoice balance before allocation
     */
    @Column(name = "invoice_balance_before", precision = 15, scale = 2)
    private BigDecimal invoiceBalanceBefore;
    
    /**
     * Allocated amount
     */
    @Column(name = "allocated_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal allocatedAmount;
    
    /**
     * Invoice balance after allocation
     */
    @Column(name = "invoice_balance_after", precision = 15, scale = 2)
    private BigDecimal invoiceBalanceAfter;
    
    /**
     * Currency
     */
    @Column(name = "currency", length = 10)
    private String currency;
    
    /**
     * Exchange rate (if different from payment)
     */
    @Column(name = "exchange_rate", precision = 15, scale = 6)
    private BigDecimal exchangeRate;
    
    /**
     * Payment ID (denormalized for performance)
     */
    @Column(name = "payment_id_direct")
    private Long paymentId;
    
    /**
     * Invoice ID (denormalized for performance)
     */
    @Column(name = "invoice_id_direct")
    private Long invoiceId;
    
    /**
     * Customer ID (from payment, denormalized)
     */
    @Column(name = "customer_id")
    private Long customerId;
    
    /**
     * Customer name (from payment, denormalized)
     */
    @Column(name = "customer_name", length = 200)
    private String customerName;
    
    /**
     * Allocation date
     */
    @Column(name = "allocation_date")
    private java.time.LocalDate allocationDate;
    
    /**
     * Allocation type (FULL, PARTIAL, ADVANCE)
     */
    @Column(name = "allocation_type", length = 20)
    private String allocationType;
    
    /**
     * Status (ACTIVE, REVERSED, CANCELLED)
     */
    @Column(name = "status", length = 20)
    private String status;
    
    /**
     * Is reversed
     */
    @Column(name = "is_reversed")
    private Boolean isReversed;
    
    /**
     * Reversal date
     */
    @Column(name = "reversal_date")
    private java.time.LocalDate reversalDate;
    
    /**
     * Reversal reason
     */
    @Column(name = "reversal_reason", length = 500)
    private String reversalReason;
    
    /**
     * Notes
     */
    @Column(name = "notes", length = 500)
    private String notes;
    
    /**
     * Gets payment ID
     */
    @Transient
    public Long getPaymentId() {
        return payment != null ? payment.getId() : paymentId;
    }
    
    /**
     * Sets payment ID (for denormalized access)
     */
    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
    
    /**
     * Gets invoice ID
     */
    @Transient
    public Long getInvoiceId() {
        return invoice != null ? invoice.getId() : invoiceId;
    }
    
    /**
     * Sets invoice ID (for denormalized access)
     */
    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }
    
    /**
     * Calculates invoice balance after allocation
     */
    @Transient
    public void calculateBalanceAfter() {
        if (invoiceBalanceBefore == null || allocatedAmount == null) {
            invoiceBalanceAfter = BigDecimal.ZERO;
            return;
        }
        
        invoiceBalanceAfter = invoiceBalanceBefore.subtract(allocatedAmount);
        
        // Ensure balance doesn't go negative
        if (invoiceBalanceAfter.compareTo(BigDecimal.ZERO) < 0) {
            invoiceBalanceAfter = BigDecimal.ZERO;
        }
    }
    
    /**
     * Checks if invoice is fully paid after this allocation
     */
    @Transient
    public boolean isInvoiceFullyPaid() {
        return invoiceBalanceAfter != null && 
               invoiceBalanceAfter.compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * Gets the percentage of invoice paid by this allocation
     */
    @Transient
    public BigDecimal getAllocationPercentage() {
        if (invoiceBalanceBefore == null || 
            invoiceBalanceBefore.compareTo(BigDecimal.ZERO) == 0 || 
            allocatedAmount == null) {
            return BigDecimal.ZERO;
        }
        
        return allocatedAmount.divide(invoiceBalanceBefore, 4, RoundingMode.HALF_UP)
                              .multiply(new BigDecimal("100"));
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (currency == null) {
            currency = "LKR";
        }
        if (exchangeRate == null) {
            exchangeRate = BigDecimal.ONE;
        }
        if (status == null) {
            status = "ACTIVE";
        }
        if (isReversed == null) {
            isReversed = false;
        }
        // Sync payment ID
        if (payment != null && paymentId == null) {
            paymentId = payment.getId();
        }
        // Sync invoice ID
        if (invoice != null && invoiceId == null) {
            invoiceId = invoice.getId();
        }
        if (allocationDate == null) {
            allocationDate = java.time.LocalDate.now();
        }
        
        // Calculate balance after
        calculateBalanceAfter();
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Recalculate balance after
        calculateBalanceAfter();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentAllocation)) return false;
        PaymentAllocation that = (PaymentAllocation) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
