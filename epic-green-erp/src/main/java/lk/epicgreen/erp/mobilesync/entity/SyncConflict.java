package lk.epicgreen.erp.mobilesync.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.time.LocalDateTime;

/**
 * SyncConflict entity
 * Represents conflicts detected during synchronization
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "sync_conflicts", indexes = {
    @Index(name = "idx_sync_conflict_device", columnList = "device_id"),
    @Index(name = "idx_sync_conflict_user", columnList = "user_id"),
    @Index(name = "idx_sync_conflict_entity", columnList = "entity_type, entity_id"),
    @Index(name = "idx_sync_conflict_status", columnList = "resolution_status"),
    @Index(name = "idx_sync_conflict_detected", columnList = "detected_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncConflict extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Device identifier
     */
    @Column(name = "device_id", nullable = false, length = 100)
    private String deviceId;
    
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
     * Entity type (e.g., CUSTOMER, SALES_ORDER, PAYMENT)
     */
    @Column(name = "entity_type", nullable = false, length = 50)
    private String entityType;
    
    /**
     * Entity ID
     */
    @Column(name = "entity_id", nullable = false)
    private Long entityId;
    
    /**
     * Conflict type (VERSION_MISMATCH, CONCURRENT_UPDATE, DELETE_CONFLICT, DUPLICATE)
     */
    @Column(name = "conflict_type", nullable = false, length = 30)
    private String conflictType;
    
    /**
     * Detected at
     */
    @Column(name = "detected_at", nullable = false)
    private LocalDateTime detectedAt;
    
    /**
     * Client version
     */
    @Column(name = "client_version")
    private Long clientVersion;
    
    /**
     * Server version
     */
    @Column(name = "server_version")
    private Long serverVersion;
    
    /**
     * Client data (JSON)
     */
    @Column(name = "client_data", columnDefinition = "TEXT")
    private String clientData;
    
    /**
     * Server data (JSON)
     */
    @Column(name = "server_data", columnDefinition = "TEXT")
    private String serverData;
    
    /**
     * Conflict details (description of what conflicted)
     */
    @Column(name = "conflict_details", columnDefinition = "TEXT")
    private String conflictDetails;
    
    /**
     * Resolution status (UNRESOLVED, RESOLVED, AUTO_RESOLVED, CLIENT_WINS, SERVER_WINS, MERGED)
     */
    @Column(name = "resolution_status", nullable = false, length = 20)
    private String resolutionStatus;
    
    /**
     * Resolution strategy (CLIENT_WINS, SERVER_WINS, LAST_WRITE_WINS, MERGE, MANUAL)
     */
    @Column(name = "resolution_strategy", length = 30)
    private String resolutionStrategy;
    
    /**
     * Resolved data (JSON - the final data after resolution)
     */
    @Column(name = "resolved_data", columnDefinition = "TEXT")
    private String resolvedData;
    
    /**
     * Resolved by
     */
    @Column(name = "resolved_by", length = 50)
    private String resolvedBy;
    
    /**
     * Resolved at
     */
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    
    /**
     * Resolution notes
     */
    @Column(name = "resolution_notes", columnDefinition = "TEXT")
    private String resolutionNotes;
    
    /**
     * Auto resolve enabled
     */
    @Column(name = "auto_resolve_enabled")
    private Boolean autoResolveEnabled;
    
    /**
     * Sync log ID
     */
    @Column(name = "sync_log_id")
    private Long syncLogId;
    
    /**
     * Sync queue ID
     */
    @Column(name = "sync_queue_id")
    private Long syncQueueId;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Checks if is unresolved
     */
    @Transient
    public boolean isUnresolved() {
        return "UNRESOLVED".equals(resolutionStatus);
    }
    
    /**
     * Checks if is resolved
     */
    @Transient
    public boolean isResolved() {
        return "RESOLVED".equals(resolutionStatus) || 
               "AUTO_RESOLVED".equals(resolutionStatus) ||
               "CLIENT_WINS".equals(resolutionStatus) ||
               "SERVER_WINS".equals(resolutionStatus) ||
               "MERGED".equals(resolutionStatus);
    }
    
    /**
     * Checks if was auto resolved
     */
    @Transient
    public boolean isAutoResolved() {
        return "AUTO_RESOLVED".equals(resolutionStatus);
    }
    
    /**
     * Checks if requires manual resolution
     */
    @Transient
    public boolean requiresManualResolution() {
        return isUnresolved() && !"MANUAL".equals(resolutionStrategy);
    }
    
    /**
     * Gets resolution time in minutes
     */
    @Transient
    public Long getResolutionTimeMinutes() {
        if (resolvedAt == null || detectedAt == null) {
            return null;
        }
        return java.time.Duration.between(detectedAt, resolvedAt).toMinutes();
    }
    
    /**
     * Gets time since detection in minutes
     */
    @Transient
    public Long getMinutesSinceDetection() {
        if (detectedAt == null) {
            return null;
        }
        return java.time.Duration.between(detectedAt, LocalDateTime.now()).toMinutes();
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (resolutionStatus == null) {
            resolutionStatus = "UNRESOLVED";
        }
        if (detectedAt == null) {
            detectedAt = LocalDateTime.now();
        }
        if (autoResolveEnabled == null) {
            autoResolveEnabled = false;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SyncConflict)) return false;
        SyncConflict that = (SyncConflict) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
