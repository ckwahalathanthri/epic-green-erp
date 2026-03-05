package lk.epicgreen.erp.sales.entity;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Sales Invoice Entity
 * Financial document generated from dispatches
 */
@Entity
@Table(name = "sales_invoices", indexes = {
    @Index(name = "idx_invoice_number", columnList = "invoice_number"),
    @Index(name = "idx_order_id", columnList = "order_id"),
    @Index(name = "idx_invoice_status", columnList = "invoice_status"),
    @Index(name = "idx_invoice_date", columnList = "invoice_date"),
    @Index(name = "idx_due_date", columnList = "due_date")
})
@Data
public class SalesInvoice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "invoice_number", unique = true, nullable = false, length = 50)
    private String invoiceNumber; // INV-2026-001
    
    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;
    
    @Column(name = "invoice_type", length = 20, nullable = false)
    private String invoiceType; // TAX_INVOICE, PROFORMA, CREDIT_NOTE, DEBIT_NOTE
    
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    @Column(name = "order_number", length = 50)
    private String orderNumber;
    
    @Column(name = "dispatch_id")
    private Long dispatchId;
    
    @Column(name = "dispatch_number", length = 50)
    private String dispatchNumber;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    @Column(name = "customer_name", length = 200, nullable = false)
    private String customerName;
    
    @Column(name = "customer_email", length = 100)
    private String customerEmail;
    
    @Column(name = "customer_phone", length = 20)
    private String customerPhone;
    
    @Column(name = "customer_tax_id", length = 50)
    private String customerTaxId; // VAT/GST number
    
    @Column(name = "billing_address", columnDefinition = "TEXT")
    private String billingAddress;
    
    @Column(name = "shipping_address", columnDefinition = "TEXT")
    private String shippingAddress;
    
    @Column(name = "invoice_status", length = 20, nullable = false)
    private String invoiceStatus; // DRAFT, SENT, PARTIAL_PAID, PAID, OVERDUE, CANCELLED, VOID
    
    @Column(name = "payment_term_id")
    private Long paymentTermId;
    
    @Column(name = "payment_terms", length = 50)
    private String paymentTerms; // CASH, CREDIT_30, CREDIT_60, CREDIT_90
    
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    @Column(name = "subtotal", precision = 15, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;
    
    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage = BigDecimal.ZERO;
    
    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    @Column(name = "tax_rate", precision = 5, scale = 2)
    private BigDecimal taxRate = BigDecimal.ZERO;
    
    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;
    
    @Column(name = "shipping_charge", precision = 15, scale = 2)
    private BigDecimal shippingCharge = BigDecimal.ZERO;
    
    @Column(name = "adjustment_amount", precision = 15, scale = 2)
    private BigDecimal adjustmentAmount = BigDecimal.ZERO;
    
    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    @Column(name = "paid_amount", precision = 15, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;
    
    @Column(name = "balance_amount", precision = 15, scale = 2)
    private BigDecimal balanceAmount = BigDecimal.ZERO;
    
    @Column(name = "currency", length = 10)
    private String currency = "LKR";
    
    @Column(name = "exchange_rate", precision = 15, scale = 6)
    private BigDecimal exchangeRate = BigDecimal.ONE;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes;
    
    @Column(name = "terms_and_conditions", columnDefinition = "TEXT")
    private String termsAndConditions;
    
    @Column(name = "sent_to_customer")
    private Boolean sentToCustomer = false;
    
    @Column(name = "sent_at")
    private LocalDateTime sentAt;
    
    @Column(name = "created_by", length = 100)
    private String createdBy;
    
    @Column(name = "approved_by", length = 100)
    private String approvedBy;
    
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    
    @Column(name = "cancelled_by", length = 100)
    private String cancelledBy;
    
    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;
    
    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SalesInvoiceItem> items = new ArrayList<>();
    
    @PrePersist
    @PreUpdate
    public void calculateTotals() {
        // Calculate subtotal from items
        if (items != null && !items.isEmpty()) {
            subtotal = items.stream()
                .map(SalesInvoiceItem::getLineTotal)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            subtotal = BigDecimal.ZERO;
        }
        
        // Calculate discount
        if (discountPercentage != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            discountAmount = subtotal.multiply(discountPercentage).divide(BigDecimal.valueOf(100));
        } else {
            discountAmount = BigDecimal.ZERO;
        }
        
        BigDecimal amountAfterDiscount = subtotal.subtract(discountAmount);
        
        // Calculate tax
        if (taxRate != null && taxRate.compareTo(BigDecimal.ZERO) > 0) {
            taxAmount = amountAfterDiscount.multiply(taxRate).divide(BigDecimal.valueOf(100));
        } else {
            taxAmount = BigDecimal.ZERO;
        }
        
        // Calculate total
        totalAmount = amountAfterDiscount.add(taxAmount)
            .add(shippingCharge != null ? shippingCharge : BigDecimal.ZERO)
            .add(adjustmentAmount != null ? adjustmentAmount : BigDecimal.ZERO);
        
        // Calculate balance
        balanceAmount = totalAmount.subtract(paidAmount != null ? paidAmount : BigDecimal.ZERO);
        
        // Auto-update status based on payment
        updatePaymentStatus();
        
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }
    
    private void updatePaymentStatus() {
        if (balanceAmount.compareTo(BigDecimal.ZERO) <= 0) {
            if (!"PAID".equals(invoiceStatus)) {
                invoiceStatus = "PAID";
            }
        } else if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
            if (!"PARTIAL_PAID".equals(invoiceStatus)) {
                invoiceStatus = "PARTIAL_PAID";
            }
        } else if (dueDate != null && LocalDate.now().isAfter(dueDate)) {
            if (!"OVERDUE".equals(invoiceStatus) && !"PAID".equals(invoiceStatus)) {
                invoiceStatus = "OVERDUE";
            }
        }
    }
    
    public void sendToCustomer() {
        this.sentToCustomer = true;
        this.sentAt = LocalDateTime.now();
        if ("DRAFT".equals(invoiceStatus)) {
            this.invoiceStatus = "SENT";
        }
    }
    
    public void markAsPaid() {
        this.invoiceStatus = "PAID";
        this.paidAmount = this.totalAmount;
        this.balanceAmount = BigDecimal.ZERO;
    }
    
    public void cancel(String canceller, String reason) {
        this.invoiceStatus = "CANCELLED";
        this.cancelledBy = canceller;
        this.cancelledAt = LocalDateTime.now();
        this.cancellationReason = reason;
    }
    
    public boolean isOverdue() {
        return dueDate != null && LocalDate.now().isAfter(dueDate) && 
               balanceAmount.compareTo(BigDecimal.ZERO) > 0;
    }
}