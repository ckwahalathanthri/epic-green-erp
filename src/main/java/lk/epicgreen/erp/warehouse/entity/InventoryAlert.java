package lk.epicgreen.erp.warehouse.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_alerts", indexes = {
    @Index(name = "idx_product_warehouse", columnList = "product_id,warehouse_id"),
    @Index(name = "idx_alert_type", columnList = "alert_type"),
    @Index(name = "idx_alert_status", columnList = "alert_status"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryAlert {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "alert_type", length = 30, nullable = false)
    private String alertType; // LOW_STOCK, REORDER_POINT, OVERSTOCK, EXPIRING_SOON, EXPIRED, ZERO_STOCK
    
    @Column(name = "alert_severity", length = 20, nullable = false)
    private String alertSeverity; // LOW, MEDIUM, HIGH, CRITICAL
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_code", length = 50)
    private String productCode;
    
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;
    
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    @Column(name = "current_quantity", precision = 15, scale = 3)
    private BigDecimal currentQuantity;
    
    @Column(name = "threshold_quantity", precision = 15, scale = 3)
    private BigDecimal thresholdQuantity;
    
    @Column(name = "alert_message", columnDefinition = "TEXT")
    private String alertMessage;
    
    @Column(name = "alert_status", length = 20, nullable = false)
    private String alertStatus; // ACTIVE, ACKNOWLEDGED, RESOLVED, DISMISSED
    
    @Column(name = "triggered_at", nullable = false)
    private LocalDateTime triggeredAt;
    
    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;
    
    @Column(name = "acknowledged_by", length = 100)
    private String acknowledgedBy;
    
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    
    @Column(name = "resolved_by", length = 100)
    private String resolvedBy;
    
    @Column(name = "resolution_notes", columnDefinition = "TEXT")
    private String resolutionNotes;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (triggeredAt == null) {
            triggeredAt = LocalDateTime.now();
        }
        if (alertStatus == null) {
            alertStatus = "ACTIVE";
        }
    }
    
    public void acknowledge(String acknowledgedBy) {
        this.alertStatus = "ACKNOWLEDGED";
        this.acknowledgedAt = LocalDateTime.now();
        this.acknowledgedBy = acknowledgedBy;
    }
    
    public void resolve(String resolvedBy, String notes) {
        this.alertStatus = "RESOLVED";
        this.resolvedAt = LocalDateTime.now();
        this.resolvedBy = resolvedBy;
        this.resolutionNotes = notes;
    }
    
    public void dismiss() {
        this.alertStatus = "DISMISSED";
    }
}
