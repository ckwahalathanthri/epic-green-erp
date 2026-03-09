package lk.epicgreen.erp.warehouse.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "physical_inventories", indexes = {
    @Index(name = "idx_count_number", columnList = "count_number"),
    @Index(name = "idx_warehouse_id", columnList = "warehouse_id"),
    @Index(name = "idx_count_status", columnList = "count_status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalInventory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "count_number", unique = true, nullable = false, length = 50)
    private String countNumber;
    
    @Column(name = "count_date", nullable = false)
    private LocalDate countDate;
    
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;
    
    @Column(name = "count_type", length = 20, nullable = false)
    private String countType; // FULL_COUNT, CYCLE_COUNT, SPOT_CHECK
    
    @Column(name = "count_status", length = 20, nullable = false)
    private String countStatus; // DRAFT, IN_PROGRESS, COMPLETED, POSTED, CANCELLED
    
    @Column(name = "total_items_counted")
    private Integer totalItemsCounted = 0;
    
    @Column(name = "total_discrepancies")
    private Integer totalDiscrepancies = 0;
    
    @Column(name = "total_variance_value", precision = 15, scale = 2)
    private BigDecimal totalVarianceValue = BigDecimal.ZERO;
    
    @Column(name = "scheduled_date")
    private LocalDate scheduledDate;
    
    @Column(name = "started_at")
    private LocalDateTime startedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @Column(name = "posted_at")
    private LocalDateTime postedAt;
    
    @Column(name = "counted_by", length = 100)
    private String countedBy;
    
    @Column(name = "verified_by", length = 100)
    private String verifiedBy;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_by", length = 100)
    private String createdBy;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (countStatus == null) {
            countStatus = "DRAFT";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public void start() {
        this.countStatus = "IN_PROGRESS";
        this.startedAt = LocalDateTime.now();
    }
    
    public void complete() {
        this.countStatus = "COMPLETED";
        this.completedAt = LocalDateTime.now();
    }
    
    public void post() {
        this.countStatus = "POSTED";
        this.postedAt = LocalDateTime.now();
    }
    
    public void cancel() {
        this.countStatus = "CANCELLED";
    }
}
