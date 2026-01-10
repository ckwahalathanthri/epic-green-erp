package lk.epicgreen.erp.audit.entity;


import lk.epicgreen.erp.admin.entity.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * AuditLog entity
 * Comprehensive audit trail for all system operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "audit_logs", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_entity", columnList = "entity_type, entity_id"),
    @Index(name = "idx_operation_type", columnList = "operation_type"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * User reference (nullable for system operations)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_audit_log_user"))
    private User user;

    @Column
    private String actionType;
    /**
     * Username (denormalized for performance)
     */
    @Size(max = 50)
    @Column(name = "username", length = 50)
    private String username;
    
    /**
     * Action performed (e.g., "Create Order", "Update Customer")
     */
    @NotBlank(message = "Action is required")
    @Size(max = 50)
    @Column(name = "action", nullable = false, length = 50)
    private String action;

    @Column
    private boolean IsReconciled;
    
    /**
     * Entity type (e.g., SALES_ORDER, CUSTOMER, PAYMENT)
     */
    @NotBlank(message = "Entity type is required")
    @Size(max = 50)
    @Column(name = "entity_type", nullable = false, length = 50)
    private String entityType;
    
    /**
     * Entity ID (record ID)
     */
    @Column(name = "entity_id")
    private Long entityId;

    @Column(name="module_type")
    private String model;
    
    /**
     * Entity name (for display)
     */
    @Size(max = 200)
    @Column(name = "entity_name", length = 200)
    private String entityName;
    
    /**
     * Operation type (CREATE, UPDATE, DELETE, VIEW, APPROVE, REJECT, LOGIN, LOGOUT)
     */
    @NotBlank(message = "Operation type is required")
    @Column(name = "operation_type", nullable = false, length = 10)
    private String operationType;
    
    /**
     * Old values (before change) - JSON
     */
    @Column(name = "old_values", columnDefinition = "JSON")
    private String oldValues;
    
    /**
     * New values (after change) - JSON
     */
    @Column(name = "new_values", columnDefinition = "JSON")
    private String newValues;
    
    /**
     * Changed fields - JSON array
     */
    @Column(name = "changed_fields", columnDefinition = "JSON")
    private String changedFields;
    
    /**
     * IP address
     */
    @Size(max = 45)
    @Column(name = "ip_address", length = 45)
    private String ipAddress;
    
    /**
     * User agent (browser/app info)
     */
    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;
    
    /**
     * Session ID
     */
    @Size(max = 100)
    @Column(name = "session_id", length = 100)
    private String sessionId;
    
    /**
     * Status (SUCCESS, FAILED)
     */
    @Column(name = "status", length = 10)
    private String status;
    
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
     * Operation type checks
     */
    @Transient
    public boolean isCreate() {
        return "CREATE".equals(operationType);
    }
    
    @Transient
    public boolean isUpdate() {
        return "UPDATE".equals(operationType);
    }
    
    @Transient
    public boolean isDelete() {
        return "DELETE".equals(operationType);
    }
    
    @Transient
    public boolean isView() {
        return "VIEW".equals(operationType);
    }
    
    @Transient
    public boolean isApprove() {
        return "APPROVE".equals(operationType);
    }
    
    @Transient
    public boolean isReject() {
        return "REJECT".equals(operationType);
    }
    
    @Transient
    public boolean isLogin() {
        return "LOGIN".equals(operationType);
    }
    
    @Transient
    public boolean isLogout() {
        return "LOGOUT".equals(operationType);
    }
    
    /**
     * Status checks
     */
    @Transient
    public boolean isSuccess() {
        return "SUCCESS".equals(status);
    }
    
    @Transient
    public boolean isFailed() {
        return "FAILED".equals(status);
    }
    
    /**
     * Check if has changes
     */
    @Transient
    public boolean hasChanges() {
        return changedFields != null && !changedFields.isEmpty();
    }
    
    /**
     * Check if has old values
     */
    @Transient
    public boolean hasOldValues() {
        return oldValues != null && !oldValues.isEmpty();
    }
    
    /**
     * Check if has new values
     */
    @Transient
    public boolean hasNewValues() {
        return newValues != null && !newValues.isEmpty();
    }
    
    /**
     * Get audit summary
     */
    @Transient
    public String getAuditSummary() {
        StringBuilder summary = new StringBuilder();
        if (username != null) {
            summary.append(username);
        } else {
            summary.append("SYSTEM");
        }
        summary.append(" - ").append(operationType);
        summary.append(" - ").append(entityType);
        if (entityId != null) {
            summary.append(" #").append(entityId);
        }
        if (entityName != null) {
            summary.append(" (").append(entityName).append(")");
        }
        summary.append(" - ").append(status);
        return summary.toString();
    }
    
    /**
     * Check if from mobile device
     */
    @Transient
    public boolean isFromMobile() {
        if (userAgent == null) {
            return false;
        }
        String agent = userAgent.toLowerCase();
        return agent.contains("android") || agent.contains("iphone") || agent.contains("ipad");
    }
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = "SUCCESS";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Audit logs should be immutable after creation
        throw new IllegalStateException("Audit logs cannot be modified after creation");
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuditLog)) return false;
        AuditLog auditLog = (AuditLog) o;
        return id != null && id.equals(auditLog.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
