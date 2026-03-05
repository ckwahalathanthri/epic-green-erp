package lk.epicgreen.erp.sales.entity;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice_payments", indexes = {
    @Index(name = "idx_payment_number", columnList = "payment_number"),
    @Index(name = "idx_invoice_id", columnList = "invoice_id"),
    @Index(name = "idx_payment_date", columnList = "payment_date")
})
@Data
public class InvoicePayment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "payment_number", unique = true, length = 50)
    private String paymentNumber; // PAY-2026-001
    
    @Column(name = "invoice_id", nullable = false)
    private Long invoiceId;
    
    @Column(name = "invoice_number", length = 50)
    private String invoiceNumber;
    
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;
    
    @Column(name = "payment_method", length = 20, nullable = false)
    private String paymentMethod; // CASH, CHEQUE, BANK_TRANSFER, CARD, ONLINE
    
    @Column(name = "payment_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal paymentAmount;
    
    @Column(name = "reference_number", length = 100)
    private String referenceNumber; // Cheque number, transaction ID, etc.
    
    @Column(name = "bank_name", length = 100)
    private String bankName;
    
    @Column(name = "cheque_number", length = 50)
    private String chequeNumber;
    
    @Column(name = "cheque_date")
    private LocalDate chequeDate;
    
    @Column(name = "payment_status", length = 20, nullable = false)
    private String paymentStatus; // PENDING, CLEARED, BOUNCED, CANCELLED
    
    @Column(name = "received_by", length = 100)
    private String receivedBy;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    @PreUpdate
    public void setTimestamps() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }
    
    public void markAsCleared() {
        this.paymentStatus = "CLEARED";
    }
    
    public void markAsBounced() {
        this.paymentStatus = "BOUNCED";
    }
}