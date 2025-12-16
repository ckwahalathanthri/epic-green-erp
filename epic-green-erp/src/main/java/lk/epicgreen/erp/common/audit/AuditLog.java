package lk.epicgreen.erp.common.audit;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity for storing detailed audit logs
 * Tracks all create, update, delete operations across the system
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "audit_logs", indexes = {
    @Index(name = "idx_audit_entity", columnList = "entity_name, entity_id"),
    @Index(name = "idx_audit_user", columnList = "performed_by"),
    @Index(name = "idx_audit_action", columnList = "action"),
    @Index(name = "idx_audit_timestamp", columnList = "timestamp")
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
     * Entity name (e.g., "Product", "Customer")
     */
    @Column(name = "entity_name", nullable = false, length = 100)
    private String entityName;
    
    /**
     * Entity ID
     */
    @Column(name = "entity_id", nullable = false, length = 50)
    private String entityId;
    
    /**
     * Action performed (CREATE, UPDATE, DELETE, VIEW, etc.)
     */
    @Column(name = "action", nullable = false, length = 20)
    private String action;
    
    /**
     * User who performed the action
     */
    @Column(name = "performed_by", nullable = false, length = 50)
    private String performedBy;
    
    /**
     * Timestamp of the action
     */
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    /**
     * Old values (JSON format)
     */
    @Column(name = "old_values", columnDefinition = "TEXT")
    private String oldValues;
    
    /**
     * New values (JSON format)
     */
    @Column(name = "new_values", columnDefinition = "TEXT")
    private String newValues;
    
    /**
     * Changed fields (comma-separated)
     */
    @Column(name = "changed_fields", length = 500)
    private String changedFields;
    
    /**
     * IP address of the user
     */
    @Column(name = "ip_address", length = 50)
    private String ipAddress;
    
    /**
     * User agent (browser/device info)
     */
    @Column(name = "user_agent", length = 255)
    private String userAgent;
    
    /**
     * Request URL
     */
    @Column(name = "request_url", length = 500)
    private String requestUrl;
    
    /**
     * HTTP method (GET, POST, PUT, DELETE)
     */
    @Column(name = "http_method", length = 10)
    private String httpMethod;
    
    /**
     * Status (SUCCESS, FAILURE)
     */
    @Column(name = "status", length = 20)
    private String status;
    
    /**
     * Error message (if status is FAILURE)
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    /**
     * Additional details (JSON format)
     */
    @Column(name = "details", columnDefinition = "TEXT")
    private String details;
    
    /**
     * Duration in milliseconds
     */
    @Column(name = "duration_ms")
    private Long durationMs;
    
    /**
     * Module name (e.g., "PRODUCT", "SALES")
     */
    @Column(name = "module", length = 50)
    private String module;
    
    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
        if (status == null) {
            status = "SUCCESS";
        }
    }
}
