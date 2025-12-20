package lk.epicgreen.erp.supplier.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * SupplierLedger Entity - Tracks all financial transactions with suppliers
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "supplier_ledgers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SupplierLedger {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;
    
    @Column(name = "supplier_name", length = 200)
    private String supplierName;
    
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;
    
    @Column(name = "transaction_type", nullable = false, length = 50)
    private String transactionType; // PURCHASE, PAYMENT, CREDIT_NOTE, DEBIT_NOTE, OPENING_BALANCE
    
    @Column(name = "reference_type", length = 50)
    private String referenceType; // PURCHASE_ORDER, PAYMENT, ADJUSTMENT
    
    @Column(name = "reference_id")
    private Long referenceId;
    
    @Column(name = "reference_number", length = 100)
    private String referenceNumber;
    
    @Column(name = "description", length = 500)
    private String description;
    
    // Debit = Money we owe to supplier (increases balance)
    @Column(name = "debit_amount", precision = 15, scale = 2)
    private BigDecimal debitAmount;
    
    // Credit = Money we paid to supplier (decreases balance)
    @Column(name = "credit_amount", precision = 15, scale = 2)
    private BigDecimal creditAmount;
    
    // Running balance after this transaction
    @Column(name = "balance_amount", precision = 15, scale = 2)
    private BigDecimal balanceAmount;
    
    // Payment details
    @Column(name = "payment_type", length = 50)
    private String paymentType; // CASH, CHEQUE, BANK_TRANSFER, CREDIT
    
    @Column(name = "cheque_no", length = 50)
    private String chequeNo;
    
    @Column(name = "cheque_date")
    private LocalDate chequeDate;
    
    @Column(name = "bank", length = 200)
    private String bank;
    
    @Column(name = "bank_account", length = 100)
    private String bankAccount;
    
    @Column(name = "transaction_reference", length = 200)
    private String transactionReference;
    
    @Column(name = "recorded_by", length = 100)
    private String recordedBy;
    
    @Column(name = "notes", length = 1000)
    private String notes;
    
    @Column(name = "is_reconciled")
    private Boolean isReconciled;
    
    @Column(name = "reconciled_date")
    private LocalDate reconciledDate;
    
    // Audit fields
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (isReconciled == null) {
            isReconciled = false;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
