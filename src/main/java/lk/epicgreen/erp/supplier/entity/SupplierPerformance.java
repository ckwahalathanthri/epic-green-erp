package lk.epicgreen.erp.supplier.entity;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_performance_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierPerformance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;
    
    @Column(name = "total_purchase_orders")
    private Integer totalPurchaseOrders = 0;
    
    @Column(name = "total_purchase_amount", precision = 15, scale = 2)
    private BigDecimal totalPurchaseAmount = BigDecimal.ZERO;
    
    @Column(name = "total_payments_made", precision = 15, scale = 2)
    private BigDecimal totalPaymentsMade = BigDecimal.ZERO;
    
    @Column(name = "outstanding_balance", precision = 15, scale = 2)
    private BigDecimal outstandingBalance = BigDecimal.ZERO;
    
    @Column(name = "average_payment_days")
    private Integer averagePaymentDays = 0;
    
    @Column(name = "on_time_payment_percentage", precision = 5, scale = 2)
    private Double onTimePaymentPercentage = 0.0;
    
    @Column(name = "last_purchase_date")
    private LocalDateTime lastPurchaseDate;
    
    @Column(name = "last_payment_date")
    private LocalDateTime lastPaymentDate;
    
    @Column(name = "updated_by", length = 100)
    private String updatedBy;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
