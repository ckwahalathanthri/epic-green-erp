package lk.epicgreen.erp.customer.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * CustomerLedger entity
 * Represents financial transactions with customers (sales, payments, returns, adjustments)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "customer_ledger", indexes = {
    @Index(name = "idx_customer_id", columnList = "customer_id"),
    @Index(name = "idx_transaction_date", columnList = "transaction_date"),
    @Index(name = "idx_transaction_type", columnList = "transaction_type"),
    @Index(name = "idx_reference", columnList = "reference_type, reference_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerLedger {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Customer reference
     */
    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_customer_ledger_customer"))
    private Customer customer;
    
    /**
     * Transaction date
     */
    @NotNull(message = "Transaction date is required")
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;
    
    /**
     * Transaction type (SALE, PAYMENT, RETURN, CREDIT_NOTE, DEBIT_NOTE, ADJUSTMENT)
     */
    @NotBlank(message = "Transaction type is required")
    @Column(name = "transaction_type", nullable = false, length = 20)
    private String transactionType;
    
    /**
     * Reference type (source document type, e.g., SALES_ORDER, PAYMENT_RECEIPT)
     */
    @Size(max = 50)
    @Column(name = "reference_type", length = 50)
    private String referenceType;
    
    /**
     * Reference ID (source document ID)
     */
    @Column(name = "reference_id")
    private Long referenceId;
    
    /**
     * Reference number (source document number)
     */
    @Size(max = 50)
    @Column(name = "reference_number", length = 50)
    private String referenceNumber;
    
    /**
     * Description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * Debit amount (increases receivable - sales, debit notes)
     */
    @PositiveOrZero(message = "Debit amount must be positive or zero")
    @Column(name = "debit_amount", precision = 15, scale = 2)
    private BigDecimal debitAmount;
    
    /**
     * Credit amount (decreases receivable - payments, returns, credit notes)
     */
    @PositiveOrZero(message = "Credit amount must be positive or zero")
    @Column(name = "credit_amount", precision = 15, scale = 2)
    private BigDecimal creditAmount;
    
    /**
     * Running balance (cumulative balance after this transaction)
     */
    @NotNull(message = "Balance is required")
    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;
    
    /**
     * Created timestamp
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /**
     * Created by (user ID)
     */
    @Column(name = "created_by")
    private Long createdBy;
    
    /**
     * Check if sale transaction
     */
    @Transient
    public boolean isSale() {
        return "SALE".equals(transactionType);
    }
    
    /**
     * Check if payment transaction
     */
    @Transient
    public boolean isPayment() {
        return "PAYMENT".equals(transactionType);
    }
    
    /**
     * Check if return transaction
     */
    @Transient
    public boolean isReturn() {
        return "RETURN".equals(transactionType);
    }
    
    /**
     * Check if credit note
     */
    @Transient
    public boolean isCreditNote() {
        return "CREDIT_NOTE".equals(transactionType);
    }
    
    /**
     * Check if debit note
     */
    @Transient
    public boolean isDebitNote() {
        return "DEBIT_NOTE".equals(transactionType);
    }
    
    /**
     * Check if adjustment
     */
    @Transient
    public boolean isAdjustment() {
        return "ADJUSTMENT".equals(transactionType);
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
     * Get transaction direction (+1 for debit/increase, -1 for credit/decrease)
     */
    @Transient
    public int getDirection() {
        BigDecimal debit = debitAmount != null ? debitAmount : BigDecimal.ZERO;
        BigDecimal credit = creditAmount != null ? creditAmount : BigDecimal.ZERO;
        
        if (debit.compareTo(credit) > 0) {
            return 1; // Debit - increases receivable
        } else if (credit.compareTo(debit) > 0) {
            return -1; // Credit - decreases receivable
        }
        return 0;
    }
    
    /**
     * Check if transaction increases receivable
     */
    @Transient
    public boolean increasesReceivable() {
        return getDirection() > 0;
    }
    
    /**
     * Check if transaction decreases receivable
     */
    @Transient
    public boolean decreasesReceivable() {
        return getDirection() < 0;
    }
    
    /**
     * Get formatted transaction summary
     */
    @Transient
    public String getTransactionSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(transactionType);
        if (referenceNumber != null) {
            summary.append(" - ").append(referenceNumber);
        }
        if (description != null) {
            summary.append(" - ").append(description);
        }
        return summary.toString();
    }
    
    @PrePersist
    protected void onCreate() {
        if (debitAmount == null) {
            debitAmount = BigDecimal.ZERO;
        }
        if (creditAmount == null) {
            creditAmount = BigDecimal.ZERO;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Ledger entries should be immutable after creation
        throw new IllegalStateException("Ledger entries cannot be modified after creation");
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
