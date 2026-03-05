package lk.epicgreen.erp.sales.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Order Status History Entity
 * Tracks all status changes for audit trail
 */
@Entity
@Table(name = "order_status_history", indexes = {
    @Index(name = "idx_order_id", columnList = "order_id"),
    @Index(name = "idx_changed_at", columnList = "changed_at")
})
@Data
public class OrderStatusHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    @Column(name = "order_number", length = 50)
    private String orderNumber;
    
    @Column(name = "previous_status", length = 20)
    private String previousStatus;
    
    @Column(name = "new_status", length = 20, nullable = false)
    private String newStatus;
    
    @Column(name = "changed_by", length = 100, nullable = false)
    private String changedBy;
    
    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;
    
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    @Column(name = "ip_address", length = 50)
    private String ipAddress;
    
    @PrePersist
    public void setTimestamp() {
        if (changedAt == null) {
            changedAt = LocalDateTime.now();
        }
    }
}