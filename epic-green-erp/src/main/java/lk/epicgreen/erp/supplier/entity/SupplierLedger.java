package lk.epicgreen.erp.supplier.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * SupplierLedger entity
 * Represents financial transactions with suppliers (purchases, payments, credits)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "supplier_ledger", indexes = {
    @Index(name = "idx_supplier_ledger_supplier", columnList = "supplier_id"),
    @Index(name = "idx_supplier_ledger_date", columnList = "transaction_date"),
    @Index(name = "idx_supplier_ledger_type", columnList = "transaction_type"),
    @Index(name = "idx_supplier_ledger_reference", columnList = "reference_number")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierLedger extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Reference to supplier
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false, foreignKey = @ForeignKey(name = "fk_supplier_ledger_supplier"))
    private Supplier supplier;
    
    /**
     * Transaction date
     */
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;
    
    /**
     * Transaction type (PURCHASE, PAYMENT, CREDIT_NOTE, DEBIT_NOTE, OPENING_BALANCE)
     */
    @Column(name = "transaction_type", nullable = false, length = 30)
    private String transactionType;
    
    /**
     * Reference number (PO number, payment voucher, GRN number, etc.)
     */
    @Column(name = "reference_number", length = 50)
    private String referenceNumber;
    
    /**
     * Reference ID (ID of related entity - PO, payment, etc.)
     */
    @Column(name = "reference_id")
    private Long referenceId;
    
    /**
     * Reference type (PURCHASE_ORDER, PAYMENT, GRN, etc.)
     */
    @Column(name = "reference_type", length = 30)
    private String referenceType;
    
    /**
     * Description of the transaction
     */
    @Column(name = "description", length = 500)
    private String description;
    
    /**
     * Debit amount (purchases, additional charges)
     */
    @Column(name = "debit_amount", precision = 15, scale = 2)
    private BigDecimal debitAmount;
    
    /**
     * Credit amount (payments, credit notes)
     */
    @Column(name = "credit_amount", precision = 15, scale = 2)
    private BigDecimal creditAmount;
    
    /**
     * Balance after this transaction
     */
    @Column(name = "balance", precision = 15, scale = 2)
    private BigDecimal balance;
    
    /**
     * Currency code
     */
    @Column(name = "currency", length = 10)
    private String currency;
    
    /**
     * Due date (for purchase transactions)
     */
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    /**
     * Payment status (PENDING, PAID, PARTIALLY_PAID, OVERDUE)
     */
    @Column(name = "payment_status", length = 20)
    private String paymentStatus;
    
    /**
     * Is reconciled flag
     */
    @Column(name = "is_reconciled")
    private Boolean isReconciled;
    
    /**
     * Reconciled date
     */
    @Column(name = "reconciled_date")
    private LocalDate reconciledDate;
    
    /**
     * Reconciled by user
     */
    @Column(name = "reconciled_by", length = 50)
    private String reconciledBy;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Gets transaction amount (debit - credit)
     */
    @Transient
    public BigDecimal getTransactionAmount() {
        BigDecimal debit = debitAmount != null ? debitAmount : BigDecimal.ZERO;
        BigDecimal credit = creditAmount != null ? creditAmount : BigDecimal.ZERO;
        return debit.subtract(credit);
    }
    
    /**
     * Checks if transaction is overdue
     */
    @Transient
    public boolean isOverdue() {
        if (dueDate == null || "PAID".equals(paymentStatus)) {
            return false;
        }
        return LocalDate.now().isAfter(dueDate);
    }
    
    /**
     * Gets days overdue
     */
    @Transient
    public long getDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (currency == null) {
            currency = "LKR";
        }
        if (isReconciled == null) {
            isReconciled = false;
        }
        if (debitAmount == null) {
            debitAmount = BigDecimal.ZERO;
        }
        if (creditAmount == null) {
            creditAmount = BigDecimal.ZERO;
        }
        if (paymentStatus == null && debitAmount.compareTo(BigDecimal.ZERO) > 0) {
            paymentStatus = "PENDING";
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SupplierLedger)) return false;
        SupplierLedger that = (SupplierLedger) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
