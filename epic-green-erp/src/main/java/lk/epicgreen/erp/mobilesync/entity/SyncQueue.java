package lk.epicgreen.erp.mobilesync.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.time.LocalDateTime;

/**
 * SyncQueue entity
 * Represents queue of records waiting to be synchronized
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "sync_queue", indexes = {
    @Index(name = "idx_sync_queue_device", columnList = "device_id"),
    @Index(name = "idx_sync_queue_user", columnList = "user_id"),
    @Index(name = "idx_sync_queue_status", columnList = "sync_status"),
    @Index(name = "idx_sync_queue_entity", columnList = "entity_type, entity_id"),
    @Index(name = "idx_sync_queue_priority", columnList = "priority, created_at"),
    @Index(name = "idx_sync_queue_scheduled", columnList = "scheduled_time")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncQueue extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Device identifier
     */
    @Column(name = "device_id", nullable = false, length = 100)
    private String deviceId;
    
    /**
     * Queue code (unique identifier)
     */
    @Column(name = "queue_code", unique = true, length = 50)
    private String queueCode;
    
    /**
     * User ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    /**
     * Entity type (e.g., CUSTOMER, SALES_ORDER, PAYMENT, PRODUCT)
     */
    @Column(name = "entity_type", nullable = false, length = 50)
    private String entityType;
    
    /**
     * Entity ID
     */
    @Column(name = "entity_id", nullable = false)
    private Long entityId;
    
    /**
     * Operation type (CREATE, UPDATE, DELETE)
     */
    @Column(name = "operation_type", nullable = false, length = 20)
    private String operationType;
    
    /**
     * Sync direction (UPLOAD, DOWNLOAD)
     */
    @Column(name = "sync_direction", nullable = false, length = 20)
    private String syncDirection;
    
    /**
     * Sync status (PENDING, IN_PROGRESS, COMPLETED, FAILED, CONFLICT)
     */
    @Column(name = "sync_status", nullable = false, length = 20)
    private String syncStatus;
    
    /**
     * Priority (1 = highest, 10 = lowest)
     */
    @Column(name = "priority")
    private Integer priority;
    
    /**
     * Retry count
     */
    @Column(name = "retry_count")
    private Integer retryCount;
    
    /**
     * Max retry attempts
     */
    @Column(name = "max_retry_attempts")
    private Integer maxRetryAttempts;
    
    /**
     * Scheduled time (when to sync)
     */
    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;
    
    /**
     * Last sync attempt time
     */
    @Column(name = "last_sync_attempt_time")
    private LocalDateTime lastSyncAttemptTime;
    
    /**
     * Last retry at
     */
    @Column(name = "last_retry_at")
    private LocalDateTime lastRetryAt;
    
    /**
     * Next retry at
     */
    @Column(name = "next_retry_at")
    private LocalDateTime nextRetryAt;
    
    /**
     * Completed time
     */
    @Column(name = "completed_time")
    private LocalDateTime completedTime;
    
    /**
     * Entity data (JSON)
     */
    @Column(name = "entity_data", columnDefinition = "TEXT")
    private String entityData;
    
    /**
     * Entity version (for optimistic locking)
     */
    @Column(name = "entity_version")
    private Long entityVersion;
    
    /**
     * Server version (last known server version)
     */
    @Column(name = "server_version")
    private Long serverVersion;
    
    /**
     * Sync token
     */
    @Column(name = "sync_token", length = 100)
    private String syncToken;
    
    /**
     * Error message
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    /**
     * Error details
     */
    @Column(name = "error_details", columnDefinition = "TEXT")
    private String errorDetails;
    
    /**
     * Conflict ID (if conflict detected)
     */
    @Column(name = "conflict_id")
    private Long conflictId;
    
    /**
     * Dependencies (comma-separated list of queue IDs that must sync first)
     */
    @Column(name = "dependencies", length = 500)
    private String dependencies;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Checks if is pending
     */
    @Transient
    public boolean isPending() {
        return "PENDING".equals(syncStatus);
    }
    
    /**
     * Checks if is in progress
     */
    @Transient
    public boolean isInProgress() {
        return "IN_PROGRESS".equals(syncStatus);
    }
    
    /**
     * Checks if is completed
     */
    @Transient
    public boolean isCompleted() {
        return "COMPLETED".equals(syncStatus);
    }
    
    /**
     * Checks if is failed
     */
    @Transient
    public boolean isFailed() {
        return "FAILED".equals(syncStatus);
    }
    
    /**
     * Checks if has conflict
     */
    @Transient
    public boolean hasConflict() {
        return "CONFLICT".equals(syncStatus);
    }
    
    /**
     * Checks if can retry
     */
    @Transient
    public boolean canRetry() {
        if (retryCount == null) {
            retryCount = 0;
        }
        if (maxRetryAttempts == null) {
            maxRetryAttempts = 3;
        }
        return isFailed() && retryCount < maxRetryAttempts;
    }
    
    /**
     * Checks if should sync now
     */
    @Transient
    public boolean shouldSyncNow() {
        if (!isPending()) {
            return false;
        }
        if (scheduledTime == null) {
            return true;
        }
        return LocalDateTime.now().isAfter(scheduledTime);
    }
    
    /**
     * Gets time until scheduled
     */
    @Transient
    public Long getMinutesUntilScheduled() {
        if (scheduledTime == null) {
            return 0L;
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(scheduledTime)) {
            return 0L;
        }
        return java.time.Duration.between(now, scheduledTime).toMinutes();
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (syncStatus == null) {
            syncStatus = "PENDING";
        }
        if (priority == null) {
            priority = 5; // Medium priority
        }
        if (retryCount == null) {
            retryCount = 0;
        }
        if (maxRetryAttempts == null) {
            maxRetryAttempts = 3;
        }
        if (scheduledTime == null) {
            scheduledTime = LocalDateTime.now();
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SyncQueue)) return false;
        SyncQueue that = (SyncQueue) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
