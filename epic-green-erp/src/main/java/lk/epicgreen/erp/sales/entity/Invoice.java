package lk.epicgreen.erp.sales.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.customer.entity.Customer;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Invoice entity
 * Represents sales invoices
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "invoices", indexes = {
    @Index(name = "idx_invoice_number", columnList = "invoice_number"),
    @Index(name = "idx_invoice_date", columnList = "invoice_date"),
    @Index(name = "idx_invoice_order", columnList = "sales_order_id"),
    @Index(name = "idx_invoice_dispatch", columnList = "dispatch_note_id"),
    @Index(name = "idx_invoice_customer", columnList = "customer_id"),
    @Index(name = "idx_invoice_status", columnList = "status"),
    @Index(name = "idx_invoice_due_date", columnList = "due_date")
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
    @Column(name = "invoice_number", nullable = false, unique = true, length = 50)
    private String invoiceNumber;
    
    /**
     * Invoice date
     */
    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;
    
    /**
     * Sales order reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_order_id", foreignKey = @ForeignKey(name = "fk_invoice_order"))
    private SalesOrder salesOrder;
    
    /**
     * Dispatch note reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispatch_note_id", foreignKey = @ForeignKey(name = "fk_invoice_dispatch"))
    private DispatchNote dispatchNote;
    
    /**
     * Customer reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_invoice_customer"))
    private Customer customer;
    
    /**
     * Invoice type (SALES, PROFORMA, CREDIT_NOTE, DEBIT_NOTE)
     */
    @Column(name = "invoice_type", length = 20)
    private String invoiceType;
    
    /**
     * Customer reference number
     */
    @Column(name = "customer_reference", length = 50)
    private String customerReference;
    
    /**
     * Billing address (reference to customer address)
     */
    @Column(name = "billing_address_id")
    private Long billingAddressId;
    
    /**
     * Billing address text (copy of address at time of invoice)
     */
    @Column(name = "billing_address_text", columnDefinition = "TEXT")
    private String billingAddressText;
    
    /**
     * Currency
     */
    @Column(name = "currency", length = 10)
    private String currency;
    
    /**
     * Exchange rate (for foreign currency)
     */
    @Column(name = "exchange_rate", precision = 15, scale = 6)
    private BigDecimal exchangeRate;
    
    /**
     * Subtotal (sum of line totals before discount and tax)
     */
    @Column(name = "subtotal", precision = 15, scale = 2)
    private BigDecimal subtotal;
    
    /**
     * Discount amount
     */
    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount;
    
    /**
     * Discount percentage
     */
    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;
    
    /**
     * Tax amount
     */
    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount;
    
    /**
     * Tax percentage
     */
    @Column(name = "tax_percentage", precision = 5, scale = 2)
    private BigDecimal taxPercentage;
    
    /**
     * Shipping charges
     */
    @Column(name = "shipping_charges", precision = 15, scale = 2)
    private BigDecimal shippingCharges;
    
    /**
     * Other charges
     */
    @Column(name = "other_charges", precision = 15, scale = 2)
    private BigDecimal otherCharges;
    
    /**
     * Total amount (subtotal - discount + tax + shipping + other)
     */
    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;
    
    /**
     * Paid amount
     */
    @Column(name = "paid_amount", precision = 15, scale = 2)
    private BigDecimal paidAmount;
    
    /**
     * Balance amount (total - paid)
     */
    @Column(name = "balance_amount", precision = 15, scale = 2)
    private BigDecimal balanceAmount;
    
    /**
     * Payment terms
     */
    @Column(name = "payment_terms", length = 255)
    private String paymentTerms;
    
    /**
     * Due date
     */
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    /**
     * Status (DRAFT, PENDING, APPROVED, SENT, PAID, PARTIAL, OVERDUE, CANCELLED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Payment status (UNPAID, PARTIAL, PAID, OVERPAID)
     */
    @Column(name = "payment_status", length = 20)
    private String paymentStatus;
    
    /**
     * Is posted to ledger
     */
    @Column(name = "is_posted")
    private Boolean isPosted;
    
    /**
     * Posted date
     */
    @Column(name = "posted_date")
    private LocalDate postedDate;
    
    /**
     * Terms and conditions
     */
    @Column(name = "terms_and_conditions", columnDefinition = "TEXT")
    private String termsAndConditions;
    
    /**
     * Notes (visible to customer)
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Internal notes
     */
    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes;
    
    /**
     * Total items count
     */
    @Column(name = "total_items")
    private Integer totalItems;
    
    /**
     * Invoice items
     */
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    @Builder.Default
    private Set<InvoiceItem> items = new HashSet<>();
    
    /**
     * Adds an invoice item
     */
    public void addItem(InvoiceItem item) {
        item.setInvoice(this);
        items.add(item);
    }
    
    /**
     * Removes an invoice item
     */
    public void removeItem(InvoiceItem item) {
        items.remove(item);
        item.setInvoice(null);
    }
    
    /**
     * Calculates totals from items
     */
    @Transient
    public void calculateTotals() {
        if (items == null || items.isEmpty()) {
            subtotal = BigDecimal.ZERO;
            totalAmount = BigDecimal.ZERO;
            return;
        }
        
        // Calculate subtotal
        subtotal = items.stream()
            .map(InvoiceItem::getLineTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate discount
        BigDecimal discountAmt = BigDecimal.ZERO;
        if (discountAmount != null) {
            discountAmt = discountAmount;
        } else if (discountPercentage != null) {
            discountAmt = subtotal.multiply(discountPercentage)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }
        
        BigDecimal amountAfterDiscount = subtotal.subtract(discountAmt);
        
        // Calculate tax
        BigDecimal taxAmt = BigDecimal.ZERO;
        if (taxAmount != null) {
            taxAmt = taxAmount;
        } else if (taxPercentage != null) {
            taxAmt = amountAfterDiscount.multiply(taxPercentage)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }
        
        // Calculate total
        totalAmount = amountAfterDiscount.add(taxAmt);
        
        if (shippingCharges != null) {
            totalAmount = totalAmount.add(shippingCharges);
        }
        
        if (otherCharges != null) {
            totalAmount = totalAmount.add(otherCharges);
        }
        
        this.discountAmount = discountAmt;
        this.taxAmount = taxAmt;
        
        // Calculate balance
        calculateBalance();
    }
    
    /**
     * Calculates balance amount
     */
    @Transient
    public void calculateBalance() {
        if (totalAmount == null) {
            balanceAmount = BigDecimal.ZERO;
            return;
        }
        
        BigDecimal paid = paidAmount != null ? paidAmount : BigDecimal.ZERO;
        balanceAmount = totalAmount.subtract(paid);
    }
    
    /**
     * Checks if invoice is overdue
     */
    @Transient
    public boolean isOverdue() {
        if (dueDate == null) {
            return false;
        }
        
        if ("PAID".equals(paymentStatus)) {
            return false;
        }
        
        return LocalDate.now().isAfter(dueDate);
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
     * Checks if fully paid
     */
    @Transient
    public boolean isFullyPaid() {
        return "PAID".equals(paymentStatus);
    }
    
    /**
     * Checks if can be edited
     */
    @Transient
    public boolean canEdit() {
        return "DRAFT".equals(status);
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "DRAFT";
        }
        if (invoiceType == null) {
            invoiceType = "SALES";
        }
        if (paymentStatus == null) {
            paymentStatus = "UNPAID";
        }
        if (currency == null) {
            currency = "LKR";
        }
        if (exchangeRate == null) {
            exchangeRate = BigDecimal.ONE;
        }
        if (paidAmount == null) {
            paidAmount = BigDecimal.ZERO;
        }
        if (isPosted == null) {
            isPosted = false;
        }
        if (totalItems == null) {
            totalItems = 0;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Update total items count
        if (items != null) {
            totalItems = items.size();
        }
        
        // Update payment status based on amounts
        if (totalAmount != null && paidAmount != null) {
            if (paidAmount.compareTo(BigDecimal.ZERO) == 0) {
                paymentStatus = "UNPAID";
            } else if (paidAmount.compareTo(totalAmount) >= 0) {
                paymentStatus = "PAID";
            } else if (paidAmount.compareTo(totalAmount) > 0) {
                paymentStatus = "OVERPAID";
            } else {
                paymentStatus = "PARTIAL";
            }
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
