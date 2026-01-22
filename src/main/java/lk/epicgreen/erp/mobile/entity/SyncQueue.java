package lk.epicgreen.erp.mobile.entity;


import lk.epicgreen.erp.admin.entity.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * SyncQueue entity
 * Represents pending synchronization items in the queue
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "sync_queue", indexes = {
    @Index(name = "idx_user_device", columnList = "user_id, device_id"),
    @Index(name = "idx_entity", columnList = "entity_type, entity_id"),
    @Index(name = "idx_sync_status", columnList = "sync_status"),
    @Index(name = "idx_priority", columnList = "priority")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncQueue {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * User reference
     */
    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sync_queue_user"))
    private User user;
    
    /**
     * Device ID
     */
    @NotBlank(message = "Device ID is required")
    @Size(max = 100)
    @Column(name = "device_id", nullable = false, length = 100)
    private String deviceId;
    
    /**
     * Entity type (e.g., SALES_ORDER, PAYMENT, CUSTOMER)
     */
    @NotBlank(message = "Entity type is required")
    @Size(max = 50)
    @Column(name = "entity_type", nullable = false, length = 50)
    private String entityType;
    
    /**
     * Entity ID (record ID)
     */
    @NotNull(message = "Entity ID is required")
    @Column(name = "entity_id", nullable = false)
    private Long entityId;
    
    /**
     * Operation type (INSERT, UPDATE, DELETE)
     */
    @NotBlank(message = "Operation type is required")
    @Column(name = "operation_type", nullable = false, length = 10)
    private String operationType;
    
    /**
     * Data snapshot (JSON)
     */
    @Column(name = "data_snapshot", columnDefinition = "JSON")
    private String dataSnapshot;
    
    /**
     * Sync status (PENDING, IN_PROGRESS, SYNCED, FAILED, CONFLICT)
     */
    @Column(name = "sync_status", length = 20)
    private String syncStatus;
    
    /**
     * Priority (1-10, 1 = highest)
     */
    @Min(1)
    @Max(10)
    @Column(name = "priority")
    private Integer priority;
    
    /**
     * Retry count
     */
    @PositiveOrZero(message = "Retry count must be positive or zero")
    @Column(name = "retry_count")
    private Integer retryCount;
    
    /**
     * Max retries allowed
     */
    @Positive(message = "Max retries must be positive")
    @Column(name = "max_retries")
    private Integer maxRetries;
    
    /**
     * Error message (if failed)
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    /**
     * Created timestamp
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /**
     * Synced timestamp
     */
    @Column(name = "synced_at")
    private LocalDateTime syncedAt;
    
    /**
     * Operation type checks
     */
    @Transient
    public boolean isInsert() {
        return "INSERT".equals(operationType);
    }
    
    @Transient
    public boolean isUpdate() {
        return "UPDATE".equals(operationType);
    }
    
    @Transient
    public boolean isDelete() {
        return "DELETE".equals(operationType);
    }
    
    /**
     * Status checks
     */
    @Transient
    public boolean isPending() {
        return "PENDING".equals(syncStatus);
    }
    
    @Transient
    public boolean isInProgress() {
        return "IN_PROGRESS".equals(syncStatus);
    }
    
    @Transient
    public boolean isSynced() {
        return "SYNCED".equals(syncStatus);
    }
    
    @Transient
    public boolean isFailed() {
        return "FAILED".equals(syncStatus);
    }
    
    @Transient
    public boolean isConflict() {
        return "CONFLICT".equals(syncStatus);
    }
    
    /**
     * Check if can retry
     */
    @Transient
    public boolean canRetry() {
        int retries = retryCount != null ? retryCount : 0;
        int maxRetry = maxRetries != null ? maxRetries : 3;
        return retries < maxRetry;
    }
    
    /**
     * Get remaining retries
     */
    @Transient
    public int getRemainingRetries() {
        int retries = retryCount != null ? retryCount : 0;
        int maxRetry = maxRetries != null ? maxRetries : 3;
        return Math.max(0, maxRetry - retries);
    }
    
    /**
     * Check if high priority
     */
    @Transient
    public boolean isHighPriority() {
        return priority != null && priority <= 3;
    }
    
    /**
     * Check if low priority
     */
    @Transient
    public boolean isLowPriority() {
        return priority != null && priority >= 7;
    }
    
    /**
     * Start sync
     */
    public void startSync() {
        if (!isPending() && !isFailed()) {
            throw new IllegalStateException("Only pending or failed items can be synced");
        }
        this.syncStatus = "IN_PROGRESS";
    }
    
    /**
     * Mark as synced
     */
    public void markSynced() {
        if (!isInProgress()) {
            throw new IllegalStateException("Only in-progress items can be marked as synced");
        }
        this.syncStatus = "SYNCED";
        this.syncedAt = LocalDateTime.now();
    }
    
    /**
     * Mark as failed
     */
    public void markFailed(String error) {
        this.syncStatus = "FAILED";
        this.errorMessage = error;
        
        int retries = retryCount != null ? retryCount : 0;
        this.retryCount = retries + 1;
    }
    
    /**
     * Mark as conflict
     */
    public void markConflict(String error) {
        this.syncStatus = "CONFLICT";
        this.errorMessage = error;
    }
    
    /**
     * Retry sync
     */
    public void retry() {
        if (!canRetry()) {
            throw new IllegalStateException("Maximum retries exceeded");
        }
        this.syncStatus = "PENDING";
        this.errorMessage = null;
    }
    
    /**
     * Get sync summary
     */
    @Transient
    public String getSyncSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(entityType).append(" #").append(entityId);
        summary.append(" - ").append(operationType);
        summary.append(" - ").append(syncStatus);
        if (priority != null) {
            summary.append(" (Priority: ").append(priority).append(")");
        }
        return summary.toString();
    }
    
    @PrePersist
    protected void onCreate() {
        if (syncStatus == null) {
            syncStatus = "PENDING";
        }
        if (priority == null) {
            priority = 5;
        }
        if (retryCount == null) {
            retryCount = 0;
        }
        if (maxRetries == null) {
            maxRetries = 3;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SyncQueue)) return false;
        SyncQueue syncQueue = (SyncQueue) o;
        return id != null && id.equals(syncQueue.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
