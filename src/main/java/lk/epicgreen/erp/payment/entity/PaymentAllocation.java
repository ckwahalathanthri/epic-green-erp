package lk.epicgreen.erp.payment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lk.epicgreen.erp.sales.entity.Invoice;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * PaymentAllocation entity
 * Represents bill-to-bill settlement (allocating payment to specific invoices)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "payment_allocations", indexes = {
    @Index(name = "idx_payment_id", columnList = "payment_id"),
    @Index(name = "idx_invoice_id", columnList = "invoice_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentAllocation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Payment reference
     */
    @NotNull(message = "Payment is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false, foreignKey = @ForeignKey(name = "fk_payment_allocation_payment"))
    private Payment payment;
    
    /**
     * Invoice reference
     */
    @NotNull(message = "Invoice is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false, foreignKey = @ForeignKey(name = "fk_payment_allocation_invoice"))
    private Invoice invoice;
    
    /**
     * Allocated amount
     */
    @NotNull(message = "Allocated amount is required")
    @Positive(message = "Allocated amount must be positive")
    @Column(name = "allocated_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal allocatedAmount;
    
    /**
     * Allocation date
     */
    @NotNull(message = "Allocation date is required")
    @Column(name = "allocation_date", nullable = false)
    private LocalDate allocationDate;
    
    /**
     * Remarks
     */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    /**
     * Created timestamp
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /**
     * Created by (user ID)
     */
    @Column(name = "created_by")
    private Long createdBy;
    
    /**
     * Get allocation summary
     */
    @Transient
    public String getAllocationSummary() {
        StringBuilder summary = new StringBuilder();
        if (payment != null) {
            summary.append("Payment: ").append(payment.getPaymentNumber());
        }
        summary.append(" → ");
        if (invoice != null) {
            summary.append("Invoice: ").append(invoice.getInvoiceNumber());
        }
        summary.append(" - Amount: ").append(allocatedAmount);
        return summary.toString();
    }
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (allocationDate == null) {
            allocationDate = LocalDate.now();
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentAllocation)) return false;
        PaymentAllocation that = (PaymentAllocation) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
