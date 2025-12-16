package lk.epicgreen.erp.sales.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * SalesOrder entity
 * Represents customer orders for products
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "sales_orders", indexes = {
    @Index(name = "idx_sales_order_number", columnList = "order_number"),
    @Index(name = "idx_sales_order_date", columnList = "order_date"),
    @Index(name = "idx_sales_order_customer", columnList = "customer_id"),
    @Index(name = "idx_sales_order_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrder extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Order number (unique identifier)
     */
    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;
    
    /**
     * Order date
     */
    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;
    
    /**
     * Customer reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sales_order_customer"))
    private Customer customer;
    
    /**
     * Warehouse for dispatch
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", foreignKey = @ForeignKey(name = "fk_sales_order_warehouse"))
    private Warehouse warehouse;
    
    /**
     * Order type (STANDARD, EXPORT, SAMPLE, RETURN)
     */
    @Column(name = "order_type", length = 20)
    private String orderType;
    
    /**
     * Customer reference number (customer's PO number)
     */
    @Column(name = "customer_reference", length = 50)
    private String customerReference;
    
    /**
     * Expected delivery date
     */
    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;
    
    /**
     * Delivery address (reference to customer address)
     */
    @Column(name = "delivery_address_id")
    private Long deliveryAddressId;
    
    /**
     * Delivery instructions
     */
    @Column(name = "delivery_instructions", columnDefinition = "TEXT")
    private String deliveryInstructions;
    
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
     * Status (DRAFT, PENDING_APPROVAL, APPROVED, CONFIRMED, PARTIAL, DISPATCHED, INVOICED, COMPLETED, CANCELLED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Payment terms
     */
    @Column(name = "payment_terms", length = 255)
    private String paymentTerms;
    
    /**
     * Payment method
     */
    @Column(name = "payment_method", length = 30)
    private String paymentMethod;
    
    /**
     * Sales representative
     */
    @Column(name = "sales_representative", length = 50)
    private String salesRepresentative;
    
    /**
     * Priority (LOW, MEDIUM, HIGH, URGENT)
     */
    @Column(name = "priority", length = 20)
    private String priority;
    
    /**
     * Approved by
     */
    @Column(name = "approved_by", length = 50)
    private String approvedBy;
    
    /**
     * Approval date
     */
    @Column(name = "approval_date")
    private LocalDate approvalDate;
    
    /**
     * Notes (visible to customer)
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Internal notes (internal only)
     */
    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes;
    
    /**
     * Total items count
     */
    @Column(name = "total_items")
    private Integer totalItems;
    
    /**
     * Sales order items
     */
    @OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    @Builder.Default
    private Set<SalesOrderItem> items = new HashSet<>();
    
    /**
     * Dispatch notes
     */
    @OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<DispatchNote> dispatchNotes = new HashSet<>();
    
    /**
     * Invoices
     */
    @OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Invoice> invoices = new HashSet<>();
    
    /**
     * Adds a sales order item
     */
    public void addItem(SalesOrderItem item) {
        item.setSalesOrder(this);
        items.add(item);
    }
    
    /**
     * Removes a sales order item
     */
    public void removeItem(SalesOrderItem item) {
        items.remove(item);
        item.setSalesOrder(null);
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
            .map(SalesOrderItem::getLineTotal)
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
    }
    
    /**
     * Gets total dispatched quantity for all items
     */
    @Transient
    public BigDecimal getTotalDispatchedQuantity() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
            .map(SalesOrderItem::getDispatchedQuantity)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Gets total pending quantity for all items
     */
    @Transient
    public BigDecimal getTotalPendingQuantity() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
            .map(SalesOrderItem::getPendingQuantity)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Checks if order is fully dispatched
     */
    @Transient
    public boolean isFullyDispatched() {
        if (items == null || items.isEmpty()) {
            return false;
        }
        return items.stream().allMatch(SalesOrderItem::isFullyDispatched);
    }
    
    /**
     * Checks if order is pending approval
     */
    @Transient
    public boolean isPendingApproval() {
        return "PENDING_APPROVAL".equals(status);
    }
    
    /**
     * Checks if order is approved
     */
    @Transient
    public boolean isApproved() {
        return "APPROVED".equals(status) || "CONFIRMED".equals(status);
    }
    
    /**
     * Checks if order can be edited
     */
    @Transient
    public boolean canEdit() {
        return "DRAFT".equals(status);
    }
    
    /**
     * Checks if order can be dispatched
     */
    @Transient
    public boolean canDispatch() {
        return "CONFIRMED".equals(status) || "PARTIAL".equals(status);
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "DRAFT";
        }
        if (orderType == null) {
            orderType = "STANDARD";
        }
        if (priority == null) {
            priority = "MEDIUM";
        }
        if (currency == null) {
            currency = "LKR";
        }
        if (exchangeRate == null) {
            exchangeRate = BigDecimal.ONE;
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
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesOrder)) return false;
        SalesOrder that = (SalesOrder) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
