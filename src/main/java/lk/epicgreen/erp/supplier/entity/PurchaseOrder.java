package lk.epicgreen.erp.supplier.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_orders")
@Data
public class PurchaseOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "po_number", unique = true, nullable = false, length = 50)
    private String poNumber;
    
    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;
    
    @Column(name = "supplier_name", length = 200)
    private String supplierName;
    
    @Column(name = "supplier_email", length = 100)
    private String supplierEmail;
    
    @Column(name = "supplier_phone", length = 20)
    private String supplierPhone;
    
    @Column(name = "po_date", nullable = false)
    private LocalDate poDate;
    
    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;
    
    @Column(name = "delivery_address", length = 500)
    private String deliveryAddress;
    
    @Column(name = "po_status", nullable = false, length = 50)
    private String poStatus; // DRAFT, SUBMITTED, APPROVED, REJECTED, PARTIALLY_RECEIVED, RECEIVED, CANCELLED
    
    @Column(name = "payment_terms", length = 100)
    private String paymentTerms;
    
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;
    
    @Column(name = "subtotal", precision = 15, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;
    
    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount;
    
    @Column(name = "tax_percentage", precision = 5, scale = 2)
    private BigDecimal taxPercentage;
    
    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount;
    
    @Column(name = "shipping_cost", precision = 15, scale = 2)
    private BigDecimal shippingCost;
    
    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "terms_and_conditions", columnDefinition = "TEXT")
    private String termsAndConditions;
    
    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderItem> items = new ArrayList<>();
    
    @Column(name = "created_by", length = 100)
    private String createdBy;
    
    @Column(name = "approved_by", length = 100)
    private String approvedBy;
    
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Lifecycle callbacks for automatic calculations
    @PrePersist
    @PreUpdate
    public void calculateTotals() {
        if (items != null && !items.isEmpty()) {
            // Calculate subtotal from items
            this.subtotal = items.stream()
                .map(item -> {
                    BigDecimal itemTotal = item.getUnitPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity()));
                    if (item.getDiscountAmount() != null) {
                        itemTotal = itemTotal.subtract(item.getDiscountAmount());
                    }
                    return itemTotal;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            this.subtotal = BigDecimal.ZERO;
        }
        
        // Calculate discount amount
        if (this.discountPercentage != null && this.discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            this.discountAmount = this.subtotal
                .multiply(this.discountPercentage)
                .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
        } else if (this.discountAmount == null) {
            this.discountAmount = BigDecimal.ZERO;
        }
        
        // Calculate amount after discount
        BigDecimal amountAfterDiscount = this.subtotal.subtract(this.discountAmount);
        
        // Calculate tax amount
        if (this.taxPercentage != null && this.taxPercentage.compareTo(BigDecimal.ZERO) > 0) {
            this.taxAmount = amountAfterDiscount
                .multiply(this.taxPercentage)
                .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
        } else if (this.taxAmount == null) {
            this.taxAmount = BigDecimal.ZERO;
        }
        
        // Calculate total amount
        this.totalAmount = amountAfterDiscount
            .add(this.taxAmount)
            .add(this.shippingCost != null ? this.shippingCost : BigDecimal.ZERO);
    }
    
    // Helper methods
    public void addItem(PurchaseOrderItem item) {
        items.add(item);
        item.setPurchaseOrder(this);
    }
    
    public void removeItem(PurchaseOrderItem item) {
        items.remove(item);
        item.setPurchaseOrder(null);
    }
}
