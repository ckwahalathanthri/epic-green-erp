package lk.epicgreen.erp.mobile.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lk.epicgreen.erp.admin.entity.User;
import lombok.*;

import java.time.LocalDateTime;

/**
 * SyncConflict entity
 * Represents data conflicts that occur during synchronization
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "sync_conflicts", indexes = {
    @Index(name = "idx_user_device", columnList = "user_id, device_id"),
    @Index(name = "idx_entity", columnList = "entity_type, entity_id"),
    @Index(name = "idx_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncConflict {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * User reference
     */
    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sync_conflict_user"))
    private User user;
    
    /**
     * Device ID
     */
    @NotBlank(message = "Device ID is required")
    @Size(max = 100)
    @Column(name = "device_id", nullable = false, length = 100)
    private String deviceId;
    
    /**
     * Entity type (e.g., SALES_ORDER, PAYMENT)
     */
    @NotBlank(message = "Entity type is required")
    @Size(max = 50)
    @Column(name = "entity_type", nullable = false, length = 50)
    private String entityType;
    
    /**
     * Entity ID
     */
    @NotNull(message = "Entity ID is required")
    @Column(name = "entity_id", nullable = false)
    private Long entityId;
    
    /**
     * Server data (JSON)
     */
    @NotBlank(message = "Server data is required")
    @Column(name = "server_data", nullable = false, columnDefinition = "JSON")
    private String serverData;
    
    /**
     * Client data (JSON)
     */
    @NotBlank(message = "Client data is required")
    @Column(name = "client_data", nullable = false, columnDefinition = "JSON")
    private String clientData;
    
    /**
     * Conflict type (UPDATE_UPDATE, UPDATE_DELETE, VERSION_MISMATCH)
     */
    @NotBlank(message = "Conflict type is required")
    @Column(name = "conflict_type", nullable = false, length = 20)
    private String conflictType;
    
    /**
     * Resolution strategy (SERVER_WINS, CLIENT_WINS, MANUAL, MERGE)
     */
    @Column(name = "resolution_strategy", length = 20)
    private String resolutionStrategy;
    
    /**
     * Status (DETECTED, RESOLVED, IGNORED)
     */
    @Column(name = "status", length = 10)
    private String status;
    
    /**
     * Resolved data (JSON, final merged data)
     */
    @Column(name = "resolved_data", columnDefinition = "JSON")
    private String resolvedData;
    
    /**
     * Resolved by (user who resolved)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by", foreignKey = @ForeignKey(name = "fk_sync_conflict_resolved_by"))
    private User resolvedBy;
    
    /**
     * Resolved timestamp
     */
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    
    /**
     * Detected timestamp
     */
    @Column(name = "detected_at")
    private LocalDateTime detectedAt;
    
    /**
     * Conflict type checks
     */
    @Transient
    public boolean isUpdateUpdate() {
        return "UPDATE_UPDATE".equals(conflictType);
    }
    
    @Transient
    public boolean isUpdateDelete() {
        return "UPDATE_DELETE".equals(conflictType);
    }
    
    @Transient
    public boolean isVersionMismatch() {
        return "VERSION_MISMATCH".equals(conflictType);
    }
    
    /**
     * Resolution strategy checks
     */
    @Transient
    public boolean isServerWins() {
        return "SERVER_WINS".equals(resolutionStrategy);
    }
    
    @Transient
    public boolean isClientWins() {
        return "CLIENT_WINS".equals(resolutionStrategy);
    }
    
    @Transient
    public boolean isManual() {
        return "MANUAL".equals(resolutionStrategy);
    }
    
    @Transient
    public boolean isMerge() {
        return "MERGE".equals(resolutionStrategy);
    }
    
    /**
     * Status checks
     */
    @Transient
    public boolean isDetected() {
        return "DETECTED".equals(status);
    }
    
    @Transient
    public boolean isResolved() {
        return "RESOLVED".equals(status);
    }
    
    @Transient
    public boolean isIgnored() {
        return "IGNORED".equals(status);
    }
    
    /**
     * Resolve conflict with server wins
     */
    public void resolveWithServerWins(User resolver) {
        if (!isDetected()) {
            throw new IllegalStateException("Only detected conflicts can be resolved");
        }
        this.resolutionStrategy = "SERVER_WINS";
        this.resolvedData = serverData;
        this.status = "RESOLVED";
        this.resolvedBy = resolver;
        this.resolvedAt = LocalDateTime.now();
    }
    
    /**
     * Resolve conflict with client wins
     */
    public void resolveWithClientWins(User resolver) {
        if (!isDetected()) {
            throw new IllegalStateException("Only detected conflicts can be resolved");
        }
        this.resolutionStrategy = "CLIENT_WINS";
        this.resolvedData = clientData;
        this.status = "RESOLVED";
        this.resolvedBy = resolver;
        this.resolvedAt = LocalDateTime.now();
    }
    
    /**
     * Resolve conflict manually
     */
    public void resolveManually(String mergedData, User resolver) {
        if (!isDetected()) {
            throw new IllegalStateException("Only detected conflicts can be resolved");
        }
        this.resolutionStrategy = "MANUAL";
        this.resolvedData = mergedData;
        this.status = "RESOLVED";
        this.resolvedBy = resolver;
        this.resolvedAt = LocalDateTime.now();
    }
    
    /**
     * Resolve conflict with merge
     */
    public void resolveWithMerge(String mergedData, User resolver) {
        if (!isDetected()) {
            throw new IllegalStateException("Only detected conflicts can be resolved");
        }
        this.resolutionStrategy = "MERGE";
        this.resolvedData = mergedData;
        this.status = "RESOLVED";
        this.resolvedBy = resolver;
        this.resolvedAt = LocalDateTime.now();
    }
    
    /**
     * Ignore conflict
     */
    public void ignore() {
        if (!isDetected()) {
            throw new IllegalStateException("Only detected conflicts can be ignored");
        }
        this.status = "IGNORED";
    }
    
    /**
     * Reopen conflict
     */
    public void reopen() {
        if (!isResolved() && !isIgnored()) {
            throw new IllegalStateException("Only resolved or ignored conflicts can be reopened");
        }
        this.status = "DETECTED";
        this.resolvedData = null;
        this.resolvedBy = null;
        this.resolvedAt = null;
    }
    
    /**
     * Get conflict summary
     */
    @Transient
    public String getConflictSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(entityType).append(" #").append(entityId);
        summary.append(" - ").append(conflictType);
        summary.append(" - ").append(status);
        if (resolutionStrategy != null) {
            summary.append(" (").append(resolutionStrategy).append(")");
        }
        return summary.toString();
    }
    
    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = "DETECTED";
        }
        if (resolutionStrategy == null) {
            resolutionStrategy = "MANUAL";
        }
        if (detectedAt == null) {
            detectedAt = LocalDateTime.now();
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
