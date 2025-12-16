package lk.epicgreen.erp.mobilesync.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.time.LocalDateTime;

/**
 * SyncLog entity
 * Tracks all synchronization activities between mobile devices and server
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "sync_logs", indexes = {
    @Index(name = "idx_sync_log_device", columnList = "device_id"),
    @Index(name = "idx_sync_log_user", columnList = "user_id"),
    @Index(name = "idx_sync_log_timestamp", columnList = "sync_timestamp"),
    @Index(name = "idx_sync_log_status", columnList = "sync_status"),
    @Index(name = "idx_sync_log_type", columnList = "sync_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncLog extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Device identifier (unique device ID)
     */
    @Column(name = "device_id", nullable = false, length = 100)
    private String deviceId;
    
    /**
     * Device name
     */
    @Column(name = "device_name", length = 100)
    private String deviceName;
    
    /**
     * Device type (ANDROID, IOS, WEB)
     */
    @Column(name = "device_type", length = 20)
    private String deviceType;
    
    /**
     * User ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    /**
     * Username
     */
    @Column(name = "username", length = 50)
    private String username;
    
    /**
     * Sync timestamp (when sync started)
     */
    @Column(name = "sync_timestamp", nullable = false)
    private LocalDateTime syncTimestamp;
    
    /**
     * Started at (alias for syncTimestamp)
     */
    @Column(name = "started_at")
    private LocalDateTime startedAt;
    
    /**
     * Sync completed timestamp
     */
    @Column(name = "sync_completed_timestamp")
    private LocalDateTime syncCompletedTimestamp;
    
    /**
     * Completed at (alias for syncCompletedTimestamp)
     */
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    /**
     * Sync type (FULL, INCREMENTAL, UPLOAD, DOWNLOAD)
     */
    @Column(name = "sync_type", nullable = false, length = 20)
    private String syncType;
    
    /**
     * Sync direction (UPLOAD, DOWNLOAD, BIDIRECTIONAL)
     */
    @Column(name = "sync_direction", length = 20)
    private String syncDirection;
    
    /**
     * Sync status (IN_PROGRESS, COMPLETED, FAILED, PARTIAL)
     */
    @Column(name = "sync_status", nullable = false, length = 20)
    private String syncStatus;
    
    /**
     * Status (alias for syncStatus for compatibility)
     */
    @Column(name = "status", length = 20)
    private String status;
    
    /**
     * Sync log code (unique identifier)
     */
    @Column(name = "sync_log_code", unique = true, length = 50)
    private String syncLogCode;
    
    /**
     * Session ID
     */
    @Column(name = "session_id", length = 100)
    private String sessionId;
    
    /**
     * Entity type (e.g., CUSTOMER, SALES_ORDER, PAYMENT, PRODUCT)
     */
    @Column(name = "entity_type", length = 50)
    private String entityType;
    
    /**
     * Total records to sync
     */
    @Column(name = "total_records")
    private Integer totalRecords;
    
    /**
     * Records synced successfully
     */
    @Column(name = "synced_records")
    private Integer syncedRecords;
    
    /**
     * Records synced (alias for syncedRecords)
     */
    @Column(name = "records_synced")
    private Integer recordsSynced;
    
    /**
     * Records failed
     */
    @Column(name = "failed_records")
    private Integer failedRecords;
    
    /**
     * Conflicts detected
     */
    @Column(name = "conflicts_detected")
    private Integer conflictsDetected;
    
    /**
     * Has conflicts flag
     */
    @Column(name = "has_conflicts")
    private Boolean hasConflicts;
    
    /**
     * Conflict resolution strategy (SERVER_WINS, CLIENT_WINS, MANUAL)
     */
    @Column(name = "conflict_resolution", length = 20)
    private String conflictResolution;
    
    /**
     * Data size in bytes
     */
    @Column(name = "data_size_bytes")
    private Long dataSizeBytes;
    
    /**
     * Duration in milliseconds
     */
    @Column(name = "duration_millis")
    private Long durationMillis;
    
    /**
     * Last sync token (for incremental sync)
     */
    @Column(name = "last_sync_token", length = 100)
    private String lastSyncToken;
    
    /**
     * Client version
     */
    @Column(name = "client_version", length = 20)
    private String clientVersion;
    
    /**
     * Server version
     */
    @Column(name = "server_version", length = 20)
    private String serverVersion;
    
    /**
     * Network type (WIFI, MOBILE_DATA, OFFLINE)
     */
    @Column(name = "network_type", length = 20)
    private String networkType;
    
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
     * Sync details (JSON)
     */
    @Column(name = "sync_details", columnDefinition = "TEXT")
    private String syncDetails;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Gets sync duration in seconds
     */
    @Transient
    public Long getDurationSeconds() {
        if (durationMillis == null) {
            return 0L;
        }
        return durationMillis / 1000;
    }
    
    /**
     * Gets sync success percentage
     */
    @Transient
    public Double getSuccessPercentage() {
        if (totalRecords == null || totalRecords == 0) {
            return 0.0;
        }
        return (syncedRecords != null ? syncedRecords : 0) * 100.0 / totalRecords;
    }
    
    /**
     * Gets sync failure percentage
     */
    @Transient
    public Double getFailurePercentage() {
        if (totalRecords == null || totalRecords == 0) {
            return 0.0;
        }
        return (failedRecords != null ? failedRecords : 0) * 100.0 / totalRecords;
    }
    
    /**
     * Checks if sync is in progress
     */
    @Transient
    public boolean isInProgress() {
        return "IN_PROGRESS".equals(syncStatus);
    }
    
    /**
     * Checks if sync completed successfully
     */
    @Transient
    public boolean isCompleted() {
        return "COMPLETED".equals(syncStatus);
    }
    
    /**
     * Checks if sync failed
     */
    @Transient
    public boolean isFailed() {
        return "FAILED".equals(syncStatus);
    }
    
    /**
     * Checks if sync had conflicts
     */
    @Transient
    public boolean hasConflicts() {
        return conflictsDetected != null && conflictsDetected > 0;
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (syncStatus == null) {
            syncStatus = "IN_PROGRESS";
        }
        if (status == null) {
            status = syncStatus;
        }
        if (syncType == null) {
            syncType = "INCREMENTAL";
        }
        if (syncDirection == null) {
            syncDirection = "BIDIRECTIONAL";
        }
        if (syncTimestamp == null) {
            syncTimestamp = LocalDateTime.now();
        }
        if (startedAt == null) {
            startedAt = syncTimestamp;
        }
        if (totalRecords == null) {
            totalRecords = 0;
        }
        if (syncedRecords == null) {
            syncedRecords = 0;
        }
        if (recordsSynced == null) {
            recordsSynced = syncedRecords;
        }
        if (failedRecords == null) {
            failedRecords = 0;
        }
        if (conflictsDetected == null) {
            conflictsDetected = 0;
        }
        if (hasConflicts == null) {
            hasConflicts = conflictsDetected > 0;
        }
        if (syncLogCode == null) {
            syncLogCode = "SYNC-" + System.currentTimeMillis();
        }
        // Sync aliases
        if (completedAt != null && syncCompletedTimestamp == null) {
            syncCompletedTimestamp = completedAt;
        }
        if (syncCompletedTimestamp != null && completedAt == null) {
            completedAt = syncCompletedTimestamp;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SyncLog)) return false;
        SyncLog that = (SyncLog) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
