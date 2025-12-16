package lk.epicgreen.erp.customer.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * CustomerLedger entity
 * Tracks all financial transactions with customers (sales, payments, returns)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "customer_ledger", indexes = {
    @Index(name = "idx_customer_ledger_customer", columnList = "customer_id"),
    @Index(name = "idx_customer_ledger_date", columnList = "transaction_date"),
    @Index(name = "idx_customer_ledger_type", columnList = "transaction_type"),
    @Index(name = "idx_customer_ledger_ref", columnList = "reference_number"),
    @Index(name = "idx_customer_ledger_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerLedger extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Customer reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_customer_ledger_customer"))
    private Customer customer;
    
    /**
     * Customer ID (for direct access)
     */
    @Column(name = "customer_id_direct")
    private Long customerId;
    
    /**
     * Customer name (denormalized for performance)
     */
    @Column(name = "customer_name", length = 200)
    private String customerName;
    
    /**
     * Transaction date
     */
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;
    
    /**
     * Transaction type (SALE, PAYMENT, RETURN, ADJUSTMENT, OPENING_BALANCE)
     */
    @Column(name = "transaction_type", nullable = false, length = 30)
    private String transactionType;
    
    /**
     * Reference number (invoice number, receipt number, etc.)
     */
    @Column(name = "reference_number", length = 50)
    private String referenceNumber;
    
    /**
     * Reference ID (invoice ID, payment ID, etc.)
     */
    @Column(name = "reference_id")
    private Long referenceId;
    
    /**
     * Invoice ID
     */
    @Column(name = "invoice_id")
    private Long invoiceId;
    
    /**
     * Payment ID
     */
    @Column(name = "payment_id")
    private Long paymentId;
    
    /**
     * Sales order ID
     */
    @Column(name = "sales_order_id")
    private Long salesOrderId;
    
    /**
     * Reference type (SALES_INVOICE, PAYMENT, CREDIT_NOTE, DEBIT_NOTE)
     */
    @Column(name = "reference_type", length = 30)
    private String referenceType;
    
    /**
     * Debit amount (increases receivable - sales, debit notes)
     */
    @Column(name = "debit_amount", precision = 15, scale = 2)
    private BigDecimal debitAmount;
    
    /**
     * Credit amount (decreases receivable - payments, credit notes)
     */
    @Column(name = "credit_amount", precision = 15, scale = 2)
    private BigDecimal creditAmount;
    
    /**
     * Balance after this transaction
     */
    @Column(name = "balance", precision = 15, scale = 2)
    private BigDecimal balance;
    
    /**
     * Currency
     */
    @Column(name = "currency", length = 10)
    private String currency;
    
    /**
     * Due date (for sales)
     */
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    /**
     * Payment method (CASH, CHEQUE, BANK_TRANSFER, CREDIT_CARD, MOBILE_PAYMENT)
     */
    @Column(name = "payment_method", length = 30)
    private String paymentMethod;
    
    /**
     * Cheque number (if payment method is CHEQUE)
     */
    @Column(name = "cheque_number", length = 50)
    private String chequeNumber;
    
    /**
     * Cheque date
     */
    @Column(name = "cheque_date")
    private LocalDate chequeDate;
    
    /**
     * Bank name (for cheques/bank transfers)
     */
    @Column(name = "bank_name", length = 100)
    private String bankName;
    
    /**
     * Transaction reference (bank ref, mobile payment ref)
     */
    @Column(name = "transaction_reference", length = 100)
    private String transactionReference;
    
    /**
     * Description
     */
    @Column(name = "description", length = 500)
    private String description;
    
    /**
     * Status (PENDING, CLEARED, BOUNCED, CANCELLED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
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
     * Reconciled date (alias for compatibility)
     */
    @Column(name = "reconciled_date")
    private LocalDate reconciledDate;
    
    /**
     * Is reversed/cancelled
     */
    @Column(name = "is_reversed")
    private Boolean isReversed;
    
    /**
     * Reversal reason
     */
    @Column(name = "reversal_reason", length = 500)
    private String reversalReason;
    
    /**
     * Reversed date
     */
    @Column(name = "reversed_date")
    private LocalDate reversedDate;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
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
     * Sets entry type (alias for setTransactionType)
     */
    public void setEntryType(String entryType) {
        this.transactionType = entryType;
    }
    
    /**
     * Gets entry type (alias for getTransactionType)
     */
    @Transient
    public String getEntryType() {
        return this.transactionType;
    }
    
    /**
     * Checks if transaction is overdue
     */
    @Transient
    public boolean isOverdue() {
        if (dueDate == null || !"SALE".equals(transactionType)) {
            return false;
        }
        return LocalDate.now().isAfter(dueDate) && balance != null && balance.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Gets days overdue
     */
    @Transient
    public Long getDaysOverdue() {
        if (!isOverdue()) {
            return 0L;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }
    
    /**
     * Checks if cheque is bounced
     */
    @Transient
    public boolean isChequeBounced() {
        return "BOUNCED".equals(status);
    }
    
    /**
     * Checks if transaction is settled
     */
    @Transient
    public boolean isSettled() {
        return balance == null || balance.compareTo(BigDecimal.ZERO) == 0;
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "PENDING";
        }
        if (isReconciled == null) {
            isReconciled = false;
        }
        if (isReversed == null) {
            isReversed = false;
        }
        if (currency == null) {
            currency = "LKR";
        }
        if (debitAmount == null) {
            debitAmount = BigDecimal.ZERO;
        }
        if (creditAmount == null) {
            creditAmount = BigDecimal.ZERO;
        }
        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
        // Sync customerId with customer object
        if (customer != null && customerId == null) {
            customerId = customer.getId();
        }
        if (customer != null && customerName == null) {
            customerName = customer.getCustomerName();
        }
        // Sync reconciled dates
        if (reconciledDate == null && reconciliationDate != null) {
            reconciledDate = reconciliationDate;
        }
        if (reconciliationDate == null && reconciledDate != null) {
            reconciliationDate = reconciledDate;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerLedger)) return false;
        CustomerLedger that = (CustomerLedger) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
