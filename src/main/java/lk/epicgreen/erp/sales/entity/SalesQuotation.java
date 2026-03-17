package lk.epicgreen.erp.sales.entity;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Sales Quotation Entity
 * Represents a price quote offered to a customer before creating a sales order
 */
@Entity
@Table(name = "sales_quotations")
@Data
public class SalesQuotation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "quotation_number", unique = true, nullable = false, length = 50)
    private String quotationNumber; // QT-2026-001
    
    @Column(name = "quotation_date", nullable = false)
    private LocalDate quotationDate;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    @Column(name = "customer_name", length = 200)
    private String customerName;
    
    @Column(name = "customer_email", length = 100)
    private String customerEmail;
    
    @Column(name = "customer_phone", length = 20)
    private String customerPhone;
    
    @Column(name = "valid_until")
    private LocalDate validUntil; // Quotation validity date
    
    @Column(name = "quotation_status", length = 20, nullable = false)
    private String quotationStatus; // DRAFT, PENDING, APPROVED, REJECTED, CONVERTED, EXPIRED
    
    @Column(name = "reference_number", length = 100)
    private String referenceNumber; // Customer PO or reference
    
    @Column(name = "subtotal", precision = 15, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;
    
    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage = BigDecimal.ZERO;
    
    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    @Column(name = "tax_rate", precision = 5, scale = 2)
    private BigDecimal taxRate = BigDecimal.ZERO; // VAT/GST rate
    
    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;
    
    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    @Column(name = "currency", length = 10)
    private String currency = "LKR";
    
    @Column(name = "payment_terms", length = 50)
    private String paymentTerms; // CASH, CREDIT_30, CREDIT_60, CREDIT_90
    
    @Column(name = "delivery_terms", length = 100)
    private String deliveryTerms;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "terms_and_conditions", columnDefinition = "TEXT")
    private String termsAndConditions;
    
    @Column(name = "prepared_by", length = 100)
    private String preparedBy;
    
    @Column(name = "approved_by", length = 100)
    private String approvedBy;
    
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    
    @Column(name = "converted_to_order_id")
    private Long convertedToOrderId; // Link to sales order if converted
    
    @Column(name = "converted_at")
    private LocalDateTime convertedAt;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relationship with quotation items
    @OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SalesQuotationItem> items = new ArrayList<>();
    
    /**
     * Automatically calculate totals before persist and update
     */
    @PrePersist
    @PreUpdate
    public void calculateTotals() {
        // Calculate subtotal from all items
        if (items != null && !items.isEmpty()) {
            subtotal = items.stream()
                .map(SalesQuotationItem::getLineTotal)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            subtotal = BigDecimal.ZERO;
        }
        
        // Calculate discount amount
        if (discountPercentage != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            discountAmount = subtotal.multiply(discountPercentage).divide(BigDecimal.valueOf(100));
        } else {
            discountAmount = BigDecimal.ZERO;
        }
        
        // Calculate amount after discount
        BigDecimal amountAfterDiscount = subtotal.subtract(discountAmount);
        
        // Calculate tax amount
        if (taxRate != null && taxRate.compareTo(BigDecimal.ZERO) > 0) {
            taxAmount = amountAfterDiscount.multiply(taxRate).divide(BigDecimal.valueOf(100));
        } else {
            taxAmount = BigDecimal.ZERO;
        }
        
        // Calculate total amount
        totalAmount = amountAfterDiscount.add(taxAmount);
        
        // Set timestamps
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * Approve quotation
     */
    public void approve(String approver) {
        this.quotationStatus = "APPROVED";
        this.approvedBy = approver;
        this.approvedAt = LocalDateTime.now();
    }
    
    /**
     * Reject quotation
     */
    public void reject() {
        this.quotationStatus = "REJECTED";
    }
    
    /**
     * Convert quotation to order
     */
    public void convertToOrder(Long orderId) {
        this.quotationStatus = "CONVERTED";
        this.convertedToOrderId = orderId;
        this.convertedAt = LocalDateTime.now();
    }
    
    /**
     * Check if quotation is expired
     */
    public boolean isExpired() {
        if (validUntil == null) {
            return false;
        }
        return LocalDate.now().isAfter(validUntil);
    }
    
    /**
     * Check if quotation can be converted to order
     */
    public boolean canConvertToOrder() {
        return "APPROVED".equals(quotationStatus) && !isExpired();
    }
}
