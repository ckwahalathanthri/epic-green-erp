package lk.epicgreen.erp.sales.entity;

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
 * SalesOrder Entity
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "sales_orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SalesOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_number", unique = true, nullable = false, length = 50)
    private String orderNumber;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    @Column(name = "customer_name", length = 200)
    private String customerName;
    
    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;
    
    @Column(name = "delivery_date")
    private LocalDate deliveryDate;
    
    @Column(name = "required_date")
    private LocalDate requiredDate;
    
    @Column(name = "shipped_date")
    private LocalDate shippedDate;
    
    @Column(name = "order_type", length = 50)
    private String orderType;
    
    @Column(name = "priority", length = 20)
    private String priority;
    
    @Column(name = "status", length = 50)
    private String status;
    
    @Column(name = "sales_rep_id")
    private Long salesRepId;
    
    @Column(name = "sales_rep_name", length = 200)
    private String salesRepName;
    
    // Boolean flags - WITH "is" prefix in field names (Lombok generates isXxx() and setXxx() automatically)
    @Column(name = "is_approved")
    private Boolean isApproved;
    
    @Column(name = "is_dispatched")
    private Boolean isDispatched;
    
    @Column(name = "is_invoiced")
    private Boolean isInvoiced;
    
    @Column(name = "is_paid")
    private Boolean isPaid;
    
    // Approval fields
    @Column(name = "approval_date")
    private LocalDate approvalDate;
    
    @Column(name = "approved_by")
    private String approvedBy;
    
    @Column(name = "approval_notes", length = 500)
    private String approvalNotes;
    
    // Rejection fields
    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;
    
    @Column(name = "rejected_date")
    private LocalDate rejectedDate;
    
    // Dispatch fields
    @Column(name = "dispatch_note_id")
    private Long dispatchNoteId;
    
    @Column(name = "dispatched_date")
    private LocalDate dispatchedDate;
    
    @Column(name = "delivery_status", length = 50)
    private String deliveryStatus;
    
    // Invoice fields
    @Column(name = "invoice_id")
    private Long invoiceId;
    
    @Column(name = "invoiced_date")
    private LocalDate invoicedDate;
    
    // Payment fields
    @Column(name = "payment_status", length = 50)
    private String paymentStatus;
    
    @Column(name = "paid_date")
    private LocalDate paidDate;
    
    @Column(name = "paid_amount", precision = 15, scale = 2)
    private BigDecimal paidAmount;
    
    // Cancellation fields
    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;
    
    @Column(name = "cancelled_date")
    private LocalDate cancelledDate;
    
    // Amounts - BigDecimal for precision
    @Column(name = "subtotal_amount", precision = 15, scale = 2)
    private BigDecimal subtotalAmount;
    
    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount;
    
    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount;
    
    @Column(name = "shipping_amount", precision = 15, scale = 2)
    private BigDecimal shippingAmount;
    
    @Column(name = "total_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmount;
    
    @Column(name = "notes", length = 1000)
    private String notes;
    
    @Column(name = "shipping_address", length = 500)
    private String shippingAddress;
    
    @Column(name = "billing_address", length = 500)
    private String billingAddress;
    
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
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
