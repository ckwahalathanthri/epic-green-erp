package lk.epicgreen.erp.mobile.entity;


import lk.epicgreen.erp.admin.entity.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * SyncLog entity
 * Tracks synchronization sessions between mobile devices and server
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "sync_logs", indexes = {
    @Index(name = "idx_user_device", columnList = "user_id, device_id"),
    @Index(name = "idx_sync_status", columnList = "sync_status"),
    @Index(name = "idx_started_at", columnList = "started_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * User reference
     */
    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sync_log_user"))
    private User user;
    
    /**
     * Device ID (unique identifier for device)
     */
    @NotBlank(message = "Device ID is required")
    @Size(max = 100)
    @Column(name = "device_id", nullable = false, length = 100)
    private String deviceId;
    
    /**
     * Device type (ANDROID, IOS)
     */
    @NotBlank(message = "Device type is required")
    @Column(name = "device_type", nullable = false, length = 10)
    private String deviceType;
    
    /**
     * App version
     */
    @Size(max = 20)
    @Column(name = "app_version", length = 20)
    private String appVersion;
    
    /**
     * Sync type (FULL, INCREMENTAL, PUSH, PULL)
     */
    @NotBlank(message = "Sync type is required")
    @Column(name = "sync_type", nullable = false, length = 20)
    private String syncType;
    
    /**
     * Sync direction (UPLOAD, DOWNLOAD, BIDIRECTIONAL)
     */
    @NotBlank(message = "Sync direction is required")
    @Column(name = "sync_direction", nullable = false, length = 20)
    private String syncDirection;
    
    /**
     * Sync status (INITIATED, IN_PROGRESS, COMPLETED, FAILED, CANCELLED)
     */
    @NotBlank(message = "Sync status is required")
    @Column(name = "sync_status", nullable = false, length = 20)
    private String syncStatus;
    
    /**
     * Records uploaded count
     */
    @PositiveOrZero(message = "Records uploaded must be positive or zero")
    @Column(name = "records_uploaded")
    private Integer recordsUploaded;
    
    /**
     * Records downloaded count
     */
    @PositiveOrZero(message = "Records downloaded must be positive or zero")
    @Column(name = "records_downloaded")
    private Integer recordsDownloaded;
    
    /**
     * Conflicts detected count
     */
    @PositiveOrZero(message = "Conflicts detected must be positive or zero")
    @Column(name = "conflicts_detected")
    private Integer conflictsDetected;
    
    /**
     * Conflicts resolved count
     */
    @PositiveOrZero(message = "Conflicts resolved must be positive or zero")
    @Column(name = "conflicts_resolved")
    private Integer conflictsResolved;
    
    /**
     * Error message (if failed)
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    /**
     * Started timestamp
     */
    @NotNull(message = "Started timestamp is required")
    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;
    
    /**
     * Completed timestamp
     */
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    /**
     * Duration in seconds
     */
    @PositiveOrZero(message = "Duration must be positive or zero")
    @Column(name = "duration_seconds")
    private Integer durationSeconds;
    
    /**
     * Device type checks
     */
    @Transient
    public boolean isAndroid() {
        return "ANDROID".equals(deviceType);
    }
    
    @Transient
    public boolean isIOS() {
        return "IOS".equals(deviceType);
    }
    
    /**
     * Sync type checks
     */
    @Transient
    public boolean isFull() {
        return "FULL".equals(syncType);
    }
    
    @Transient
    public boolean isIncremental() {
        return "INCREMENTAL".equals(syncType);
    }
    
    @Transient
    public boolean isPush() {
        return "PUSH".equals(syncType);
    }
    
    @Transient
    public boolean isPull() {
        return "PULL".equals(syncType);
    }
    
    /**
     * Direction checks
     */
    @Transient
    public boolean isUpload() {
        return "UPLOAD".equals(syncDirection);
    }
    
    @Transient
    public boolean isDownload() {
        return "DOWNLOAD".equals(syncDirection);
    }
    
    @Transient
    public boolean isBidirectional() {
        return "BIDIRECTIONAL".equals(syncDirection);
    }
    
    /**
     * Status checks
     */
    @Transient
    public boolean isInitiated() {
        return "INITIATED".equals(syncStatus);
    }
    
    @Transient
    public boolean isInProgress() {
        return "IN_PROGRESS".equals(syncStatus);
    }
    
    @Transient
    public boolean isCompleted() {
        return "COMPLETED".equals(syncStatus);
    }
    
    @Transient
    public boolean isFailed() {
        return "FAILED".equals(syncStatus);
    }
    
    @Transient
    public boolean isCancelled() {
        return "CANCELLED".equals(syncStatus);
    }
    
    /**
     * Get total records processed
     */
    @Transient
    public int getTotalRecordsProcessed() {
        int uploaded = recordsUploaded != null ? recordsUploaded : 0;
        int downloaded = recordsDownloaded != null ? recordsDownloaded : 0;
        return uploaded + downloaded;
    }
    
    /**
     * Get unresolved conflicts count
     */
    @Transient
    public int getUnresolvedConflicts() {
        int detected = conflictsDetected != null ? conflictsDetected : 0;
        int resolved = conflictsResolved != null ? conflictsResolved : 0;
        return detected - resolved;
    }
    
    /**
     * Check if has conflicts
     */
    @Transient
    public boolean hasConflicts() {
        return conflictsDetected != null && conflictsDetected > 0;
    }
    
    /**
     * Check if has unresolved conflicts
     */
    @Transient
    public boolean hasUnresolvedConflicts() {
        return getUnresolvedConflicts() > 0;
    }
    
    /**
     * Calculate and update duration
     */
    public void updateDuration() {
        if (startedAt != null && completedAt != null) {
            durationSeconds = (int) java.time.Duration.between(startedAt, completedAt).getSeconds();
        }
    }
    
    /**
     * Start sync
     */
    public void start() {
        this.syncStatus = "IN_PROGRESS";
        this.startedAt = LocalDateTime.now();
    }
    
    /**
     * Complete sync successfully
     */
    public void complete() {
        this.syncStatus = "COMPLETED";
        this.completedAt = LocalDateTime.now();
        updateDuration();
    }
    
    /**
     * Mark sync as failed
     */
    public void fail(String error) {
        this.syncStatus = "FAILED";
        this.errorMessage = error;
        this.completedAt = LocalDateTime.now();
        updateDuration();
    }
    
    /**
     * Cancel sync
     */
    public void cancel() {
        this.syncStatus = "CANCELLED";
        this.completedAt = LocalDateTime.now();
        updateDuration();
    }
    
    /**
     * Get sync summary
     */
    @Transient
    public String getSyncSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(deviceType).append(" - ").append(syncType);
        summary.append(" (").append(syncDirection).append(")");
        summary.append(" - ").append(syncStatus);
        if (isCompleted()) {
            summary.append(" - ").append(getTotalRecordsProcessed()).append(" records");
            if (durationSeconds != null) {
                summary.append(" in ").append(durationSeconds).append("s");
            }
        }
        return summary.toString();
    }
    
    @PrePersist
    protected void onCreate() {
        if (syncStatus == null) {
            syncStatus = "INITIATED";
        }
        if (recordsUploaded == null) {
            recordsUploaded = 0;
        }
        if (recordsDownloaded == null) {
            recordsDownloaded = 0;
        }
        if (conflictsDetected == null) {
            conflictsDetected = 0;
        }
        if (conflictsResolved == null) {
            conflictsResolved = 0;
        }
        if (startedAt == null) {
            startedAt = LocalDateTime.now();
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SyncLog)) return false;
        SyncLog syncLog = (SyncLog) o;
        return id != null && id.equals(syncLog.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
