package lk.epicgreen.erp.sales.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.entity.CustomerAddress;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Invoice entity
 * Represents customer invoices for sales
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "invoices", indexes = {
    @Index(name = "idx_invoice_number", columnList = "invoice_number"),
    @Index(name = "idx_invoice_date", columnList = "invoice_date"),
    @Index(name = "idx_customer_id", columnList = "customer_id"),
    @Index(name = "idx_payment_status", columnList = "payment_status"),
    @Index(name = "idx_due_date", columnList = "due_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Invoice number (unique identifier)
     */
    @NotBlank(message = "Invoice number is required")
    @Size(max = 30)
    @Column(name = "invoice_number", nullable = false, unique = true, length = 30)
    private String invoiceNumber;
    
    /**
     * Invoice date
     */
    @NotNull(message = "Invoice date is required")
    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;
    
    /**
     * Sales order reference
     */
    @NotNull(message = "Sales order is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_invoice_order"))
    private SalesOrder order;
    
    /**
     * Dispatch note reference (optional)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispatch_id", foreignKey = @ForeignKey(name = "fk_invoice_dispatch"))
    private DispatchNote dispatch;
    
    /**
     * Customer reference
     */
    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_invoice_customer"))
    private Customer customer;
    
    /**
     * Billing address
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_address_id", foreignKey = @ForeignKey(name = "fk_invoice_billing_address"))
    private CustomerAddress billingAddress;
    
    /**
     * Invoice type (TAX_INVOICE, CREDIT_NOTE, DEBIT_NOTE)
     */
    @Column(name = "invoice_type", length = 20)
    private String invoiceType;
    
    /**
     * Payment terms (e.g., "Net 30 days")
     */
    @Size(max = 100)
    @Column(name = "payment_terms", length = 100)
    private String paymentTerms;
    
    /**
     * Due date
     */
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    /**
     * Subtotal (before tax and discount)
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
     * Discount amount
     */
    @PositiveOrZero(message = "Discount amount must be positive or zero")
    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount;
    
    /**
     * Freight charges
     */
    @PositiveOrZero(message = "Freight charges must be positive or zero")
    @Column(name = "freight_charges", precision = 15, scale = 2)
    private BigDecimal freightCharges;
    
    /**
     * Total amount
     */
    @NotNull(message = "Total amount is required")
    @PositiveOrZero(message = "Total amount must be positive or zero")
    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;
    
    /**
     * Paid amount
     */
    @PositiveOrZero(message = "Paid amount must be positive or zero")
    @Column(name = "paid_amount", precision = 15, scale = 2)
    private BigDecimal paidAmount;
    
    /**
     * Payment status (UNPAID, PARTIAL, PAID, OVERDUE)
     */
    @Column(name = "payment_status", length = 10)
    private String paymentStatus;
    
    /**
     * Status (DRAFT, POSTED, CANCELLED)
     */
    @Column(name = "status", nullable = false, length = 10)
    private String status;
    
    /**
     * Remarks
     */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    /**
     * Invoice items
     */
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InvoiceItem> items = new ArrayList<>();
    
    /**
     * Add invoice item
     */
    public void addItem(InvoiceItem item) {
        item.setInvoice(this);
        items.add(item);
    }
    
    /**
     * Remove invoice item
     */
    public void removeItem(InvoiceItem item) {
        items.remove(item);
        item.setInvoice(null);
    }
    
    /**
     * Get balance amount (calculated field)
     */
    @Transient
    public BigDecimal getBalanceAmount() {
        BigDecimal total = totalAmount != null ? totalAmount : BigDecimal.ZERO;
        BigDecimal paid = paidAmount != null ? paidAmount : BigDecimal.ZERO;
        return total.subtract(paid);
    }
    
    /**
     * Status checks
     */
    @Transient
    public boolean isDraft() {
        return "DRAFT".equals(status);
    }
    
    @Transient
    public boolean isPosted() {
        return "POSTED".equals(status);
    }
    
    @Transient
    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }
    
    /**
     * Invoice type checks
     */
    @Transient
    public boolean isTaxInvoice() {
        return "TAX_INVOICE".equals(invoiceType);
    }
    
    @Transient
    public boolean isCreditNote() {
        return "CREDIT_NOTE".equals(invoiceType);
    }
    
    @Transient
    public boolean isDebitNote() {
        return "DEBIT_NOTE".equals(invoiceType);
    }
    
    /**
     * Payment status checks
     */
    @Transient
    public boolean isUnpaid() {
        return "UNPAID".equals(paymentStatus);
    }
    
    @Transient
    public boolean isPartiallyPaid() {
        return "PARTIAL".equals(paymentStatus);
    }
    
    @Transient
    public boolean isPaid() {
        return "PAID".equals(paymentStatus);
    }
    
    @Transient
    public boolean isOverdue() {
        return "OVERDUE".equals(paymentStatus);
    }
    
    /**
     * Check if overdue (past due date and not paid)
     */
    @Transient
    public boolean isOverdueNow() {
        if (isPaid() || isCancelled()) {
            return false;
        }
        return dueDate != null && LocalDate.now().isAfter(dueDate);
    }
    
    /**
     * Get days overdue
     */
    @Transient
    public long getDaysOverdue() {
        if (!isOverdueNow()) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }
    
    /**
     * Calculate totals
     */
    @Transient
    public void calculateTotals() {
        // Calculate subtotal from items
        subtotal = items.stream()
                .map(InvoiceItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate total
        BigDecimal subtotalAfterDiscount = subtotal.subtract(discountAmount != null ? discountAmount : BigDecimal.ZERO);
        BigDecimal tax = taxAmount != null ? taxAmount : BigDecimal.ZERO;
        BigDecimal freight = freightCharges != null ? freightCharges : BigDecimal.ZERO;
        totalAmount = subtotalAfterDiscount.add(tax).add(freight);
    }
    
    /**
     * Update payment status based on paid amount
     */
    @Transient
    public void updatePaymentStatus() {
        if (isCancelled()) {
            return;
        }
        
        BigDecimal balance = getBalanceAmount();
        BigDecimal total = totalAmount != null ? totalAmount : BigDecimal.ZERO;
        
        if (balance.compareTo(BigDecimal.ZERO) == 0) {
            paymentStatus = "PAID";
        } else if (paidAmount != null && paidAmount.compareTo(BigDecimal.ZERO) > 0) {
            paymentStatus = "PARTIAL";
        } else if (isOverdueNow()) {
            paymentStatus = "OVERDUE";
        } else {
            paymentStatus = "UNPAID";
        }
    }
    
    /**
     * Workflow methods
     */
    public void post() {
        if (!isDraft()) {
            throw new IllegalStateException("Only draft invoices can be posted");
        }
        this.status = "POSTED";
        updatePaymentStatus();
    }
    
    public void cancel() {
        if (isPaid()) {
            throw new IllegalStateException("Paid invoices cannot be cancelled");
        }
        this.status = "CANCELLED";
    }
    
    /**
     * Record payment
     */
    public void recordPayment(BigDecimal amount) {
        if (isCancelled()) {
            throw new IllegalStateException("Cannot record payment on cancelled invoice");
        }
        if (!isPosted()) {
            throw new IllegalStateException("Invoice must be posted before recording payment");
        }
        
        BigDecimal currentPaid = paidAmount != null ? paidAmount : BigDecimal.ZERO;
        paidAmount = currentPaid.add(amount);
        
        // Ensure paid amount doesn't exceed total
        if (paidAmount.compareTo(totalAmount) > 0) {
            paidAmount = totalAmount;
        }
        
        updatePaymentStatus();
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "DRAFT";
        }
        if (invoiceType == null) {
            invoiceType = "TAX_INVOICE";
        }
        if (paymentStatus == null) {
            paymentStatus = "UNPAID";
        }
        if (taxAmount == null) {
            taxAmount = BigDecimal.ZERO;
        }
        if (discountAmount == null) {
            discountAmount = BigDecimal.ZERO;
        }
        if (freightCharges == null) {
            freightCharges = BigDecimal.ZERO;
        }
        if (paidAmount == null) {
            paidAmount = BigDecimal.ZERO;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Invoice)) return false;
        Invoice invoice = (Invoice) o;
        return id != null && id.equals(invoice.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
