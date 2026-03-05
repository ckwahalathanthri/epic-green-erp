package lk.epicgreen.erp.sales.entity;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_fulfillment", indexes = {
    @Index(name = "idx_order_id", columnList = "order_id"),
    @Index(name = "idx_fulfillment_status", columnList = "fulfillment_status")
})
@Data
public class OrderFulfillment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_id", unique = true, nullable = false)
    private Long orderId;
    
    @Column(name = "order_number", length = 50)
    private String orderNumber;
    
    @Column(name = "fulfillment_status", length = 20, nullable = false)
    private String fulfillmentStatus; // PENDING, PICKING, PACKING, READY_TO_DISPATCH, DISPATCHED, DELIVERED, COMPLETED
    
    @Column(name = "total_quantity_ordered", precision = 15, scale = 3)
    private BigDecimal totalQuantityOrdered = BigDecimal.ZERO;
    
    @Column(name = "total_quantity_picked", precision = 15, scale = 3)
    private BigDecimal totalQuantityPicked = BigDecimal.ZERO;
    
    @Column(name = "total_quantity_packed", precision = 15, scale = 3)
    private BigDecimal totalQuantityPacked = BigDecimal.ZERO;
    
    @Column(name = "total_quantity_dispatched", precision = 15, scale = 3)
    private BigDecimal totalQuantityDispatched = BigDecimal.ZERO;
    
    @Column(name = "total_quantity_delivered", precision = 15, scale = 3)
    private BigDecimal totalQuantityDelivered = BigDecimal.ZERO;
    
    @Column(name = "picking_list_count")
    private Integer pickingListCount = 0;
    
    @Column(name = "packing_slip_count")
    private Integer packingSlipCount = 0;
    
    @Column(name = "dispatch_count")
    private Integer dispatchCount = 0;
    
    @Column(name = "picking_progress", precision = 5, scale = 2)
    private BigDecimal pickingProgress = BigDecimal.ZERO;
    
    @Column(name = "packing_progress", precision = 5, scale = 2)
    private BigDecimal packingProgress = BigDecimal.ZERO;
    
    @Column(name = "dispatch_progress", precision = 5, scale = 2)
    private BigDecimal dispatchProgress = BigDecimal.ZERO;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    @PreUpdate
    public void calculateProgress() {
        if (totalQuantityOrdered != null && totalQuantityOrdered.compareTo(BigDecimal.ZERO) > 0) {
            if (totalQuantityPicked != null) {
                pickingProgress = totalQuantityPicked.multiply(BigDecimal.valueOf(100))
                    .divide(totalQuantityOrdered, 2, java.math.RoundingMode.HALF_UP);
            }
            if (totalQuantityPacked != null) {
                packingProgress = totalQuantityPacked.multiply(BigDecimal.valueOf(100))
                    .divide(totalQuantityOrdered, 2, java.math.RoundingMode.HALF_UP);
            }
            if (totalQuantityDispatched != null) {
                dispatchProgress = totalQuantityDispatched.multiply(BigDecimal.valueOf(100))
                    .divide(totalQuantityOrdered, 2, java.math.RoundingMode.HALF_UP);
            }
        }
        
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }
}