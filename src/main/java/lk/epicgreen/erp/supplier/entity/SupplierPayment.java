package lk.epicgreen.erp.supplier.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_payments")
@Data
public class SupplierPayment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "payment_number", unique = true, nullable = false, length = 50)
    private String paymentNumber;
    
    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;
    
    @Column(name = "supplier_name", length = 200)
    private String supplierName;
    
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;
    
    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod; // CASH, CHEQUE, BANK_TRANSFER, CREDIT_CARD, ONLINE
    
    @Column(name = "payment_status", nullable = false, length = 50)
    private String paymentStatus; // PENDING, COMPLETED, CANCELLED, FAILED
    
    @Column(name = "reference_number", length = 100)
    private String referenceNumber;
    
    @Column(name = "cheque_number", length = 50)
    private String chequeNumber;
    
    @Column(name = "cheque_date")
    private LocalDate chequeDate;
    
    @Column(name = "bank_name", length = 200)
    private String bankName;
    
    @Column(name = "bank_account", length = 50)
    private String bankAccount;
    
    @Column(name = "transaction_id", length = 100)
    private String transactionId;
    
    @Column(name = "purchase_order_id")
    private Long purchaseOrderId;
    
    @Column(name = "invoice_number", length = 50)
    private String invoiceNumber;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "paid_by", length = 100)
    private String paidBy;
    
    @Column(name = "approved_by", length = 100)
    private String approvedBy;
    
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    
    @Column(name = "created_by", length = 100)
    private String createdBy;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Validation method
    @PrePersist
    @PreUpdate
    public void validatePayment() {
        // Validate amount
        if (this.amount == null || this.amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }
        
        // Validate cheque details if payment method is CHEQUE
        if ("CHEQUE".equals(this.paymentMethod)) {
            if (this.chequeNumber == null || this.chequeNumber.trim().isEmpty()) {
                throw new IllegalArgumentException("Cheque number is required for cheque payments");
            }
            if (this.chequeDate == null) {
                throw new IllegalArgumentException("Cheque date is required for cheque payments");
            }
        }
        
        // Validate bank details if payment method is BANK_TRANSFER
        if ("BANK_TRANSFER".equals(this.paymentMethod)) {
            if (this.bankName == null || this.bankName.trim().isEmpty()) {
                throw new IllegalArgumentException("Bank name is required for bank transfer payments");
            }
        }
    }
    
    // Helper method to mark payment as completed
    public void markAsCompleted(String approvedBy) {
        this.paymentStatus = "COMPLETED";
        this.approvedBy = approvedBy;
        this.approvedAt = LocalDateTime.now();
    }
    
    // Helper method to cancel payment
    public void cancel() {
        if ("COMPLETED".equals(this.paymentStatus)) {
            throw new IllegalStateException("Cannot cancel a completed payment");
        }
        this.paymentStatus = "CANCELLED";
    }
}
