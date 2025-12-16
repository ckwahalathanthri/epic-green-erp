package lk.epicgreen.erp.audit.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.time.LocalDateTime;

/**
 * AuditLog entity
 * Tracks all system activities, data changes, and user actions
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "audit_logs", indexes = {
    @Index(name = "idx_audit_log_entity", columnList = "entity_type, entity_id"),
    @Index(name = "idx_audit_log_user", columnList = "user_id"),
    @Index(name = "idx_audit_log_action", columnList = "action_type"),
    @Index(name = "idx_audit_log_timestamp", columnList = "action_timestamp"),
    @Index(name = "idx_audit_log_module", columnList = "module_name"),
    @Index(name = "idx_audit_log_ip", columnList = "ip_address")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Module name (ADMIN, SUPPLIER, PRODUCT, WAREHOUSE, PURCHASE, PRODUCTION, etc.)
     */
    @Column(name = "module_name", nullable = false, length = 50)
    private String moduleName;
    
    /**
     * Entity type (e.g., USER, CUSTOMER, PRODUCT, INVOICE)
     */
    @Column(name = "entity_type", nullable = false, length = 50)
    private String entityType;
    
    /**
     * Entity ID
     */
    @Column(name = "entity_id")
    private Long entityId;
    
    /**
     * Entity name/description
     */
    @Column(name = "entity_name", length = 200)
    private String entityName;
    
    /**
     * Action type (CREATE, UPDATE, DELETE, VIEW, APPROVE, REJECT, POST, etc.)
     */
    @Column(name = "action_type", nullable = false, length = 30)
    private String actionType;
    
    /**
     * Action description
     */
    @Column(name = "action_description", columnDefinition = "TEXT")
    private String actionDescription;
    
    /**
     * Action timestamp
     */
    @Column(name = "action_timestamp", nullable = false)
    private LocalDateTime actionTimestamp;
    
    /**
     * User ID who performed the action
     */
    @Column(name = "user_id")
    private Long userId;
    
    /**
     * Username
     */
    @Column(name = "username", length = 50)
    private String username;
    
    /**
     * User role
     */
    @Column(name = "user_role", length = 50)
    private String userRole;
    
    /**
     * Old values (JSON - before change)
     */
    @Column(name = "old_values", columnDefinition = "TEXT")
    private String oldValues;
    
    /**
     * New values (JSON - after change)
     */
    @Column(name = "new_values", columnDefinition = "TEXT")
    private String newValues;
    
    /**
     * Changed fields (comma-separated list of changed field names)
     */
    @Column(name = "changed_fields", length = 1000)
    private String changedFields;
    
    /**
     * IP address
     */
    @Column(name = "ip_address", length = 45)
    private String ipAddress;
    
    /**
     * User agent (browser info)
     */
    @Column(name = "user_agent", length = 500)
    private String userAgent;
    
    /**
     * Request method (GET, POST, PUT, DELETE)
     */
    @Column(name = "request_method", length = 10)
    private String requestMethod;
    
    /**
     * Request URL
     */
    @Column(name = "request_url", length = 500)
    private String requestUrl;
    
    /**
     * Request parameters (JSON)
     */
    @Column(name = "request_parameters", columnDefinition = "TEXT")
    private String requestParameters;
    
    /**
     * Response status code
     */
    @Column(name = "response_status")
    private Integer responseStatus;
    
    /**
     * Execution time in milliseconds
     */
    @Column(name = "execution_time_millis")
    private Long executionTimeMillis;
    
    /**
     * Session ID
     */
    @Column(name = "session_id", length = 100)
    private String sessionId;
    
    /**
     * Device type (DESKTOP, MOBILE, TABLET)
     */
    @Column(name = "device_type", length = 20)
    private String deviceType;
    
    /**
     * Device ID
     */
    @Column(name = "device_id", length = 100)
    private String deviceId;
    
    /**
     * Location (city, country)
     */
    @Column(name = "location", length = 200)
    private String location;
    
    /**
     * Is successful
     */
    @Column(name = "is_successful")
    private Boolean isSuccessful;
    
    /**
     * Error message (if not successful)
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    /**
     * Tags (comma-separated tags for categorization)
     */
    @Column(name = "tags", length = 500)
    private String tags;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Gets execution time in seconds
     */
    @Transient
    public Double getExecutionTimeSeconds() {
        if (executionTimeMillis == null) {
            return 0.0;
        }
        return executionTimeMillis / 1000.0;
    }
    
    /**
     * Checks if is create action
     */
    @Transient
    public boolean isCreateAction() {
        return "CREATE".equals(actionType);
    }
    
    /**
     * Checks if is update action
     */
    @Transient
    public boolean isUpdateAction() {
        return "UPDATE".equals(actionType);
    }
    
    /**
     * Checks if is delete action
     */
    @Transient
    public boolean isDeleteAction() {
        return "DELETE".equals(actionType);
    }
    
    /**
     * Checks if is view action
     */
    @Transient
    public boolean isViewAction() {
        return "VIEW".equals(actionType);
    }
    
    /**
     * Gets hours since action
     */
    @Transient
    public Long getHoursSinceAction() {
        if (actionTimestamp == null) {
            return null;
        }
        return java.time.Duration.between(actionTimestamp, LocalDateTime.now()).toHours();
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (actionTimestamp == null) {
            actionTimestamp = LocalDateTime.now();
        }
        if (isSuccessful == null) {
            isSuccessful = true;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuditLog)) return false;
        AuditLog that = (AuditLog) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
