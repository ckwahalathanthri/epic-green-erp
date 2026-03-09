package lk.epicgreen.erp.warehouse.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reorder_points",
    uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "warehouse_id"}),
    indexes = {
        @Index(name = "idx_product_id", columnList = "product_id"),
        @Index(name = "idx_warehouse_id", columnList = "warehouse_id"),
        @Index(name = "idx_status", columnList = "status")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReorderPoint {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_code", length = 50)
    private String productCode;
    
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;
    
    @Column(name = "min_stock_level", precision = 15, scale = 3, nullable = false)
    private BigDecimal minStockLevel;
    
    @Column(name = "max_stock_level", precision = 15, scale = 3)
    private BigDecimal maxStockLevel;
    
    @Column(name = "reorder_point", precision = 15, scale = 3, nullable = false)
    private BigDecimal reorderPoint;
    
    @Column(name = "reorder_quantity", precision = 15, scale = 3)
    private BigDecimal reorderQuantity;
    
    @Column(name = "safety_stock", precision = 15, scale = 3)
    private BigDecimal safetyStock;
    
    @Column(name = "lead_time_days")
    private Integer leadTimeDays;
    
    @Column(name = "average_daily_usage", precision = 15, scale = 3)
    private BigDecimal averageDailyUsage;
    
    @Column(name = "status", length = 20, nullable = false)
    private String status; // ACTIVE, INACTIVE
    
    @Column(name = "last_triggered_date")
    private LocalDateTime lastTriggeredDate;
    
    @Column(name = "auto_reorder_enabled")
    private Boolean autoReorderEnabled = false;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = "ACTIVE";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public boolean shouldReorder(BigDecimal currentStock) {
        return currentStock.compareTo(reorderPoint) <= 0;
    }
    
    public boolean isBelowMinimum(BigDecimal currentStock) {
        return currentStock.compareTo(minStockLevel) < 0;
    }
    
    public boolean isAboveMaximum(BigDecimal currentStock) {
        return maxStockLevel != null && currentStock.compareTo(maxStockLevel) > 0;
    }
}
