package lk.epicgreen.erp.audit.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ErrorLog entity
 * Tracks system errors, exceptions, and failures
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "error_logs", indexes = {
    @Index(name = "idx_error_log_severity", columnList = "severity_level"),
    @Index(name = "idx_error_log_type", columnList = "error_type"),
    @Index(name = "idx_error_log_timestamp", columnList = "error_timestamp"),
    @Index(name = "idx_error_log_user", columnList = "user_id"),
    @Index(name = "idx_error_log_status", columnList = "status"),
    @Index(name = "idx_error_log_module", columnList = "module_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorLog extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Error timestamp
     */
    @Column(name = "error_timestamp", nullable = false)
    private LocalDateTime errorTimestamp;
    
    /**
     * Severity level (CRITICAL, HIGH, MEDIUM, LOW, INFO)
     */
    @Column(name = "severity_level", nullable = false, length = 20)
    private String severityLevel;
    
    /**
     * Error type (EXCEPTION, VALIDATION, BUSINESS, DATABASE, NETWORK, SECURITY, etc.)
     */
    @Column(name = "error_type", nullable = false, length = 30)
    private String errorType;
    
    /**
     * Error code (application-specific error code)
     */
    @Column(name = "error_code", length = 50)
    private String errorCode;
    
    /**
     * Error message
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    /**
     * Exception class name
     */
    @Column(name = "exception_class", length = 200)
    private String exceptionClass;
    
    /**
     * Stack trace
     */
    @Column(name = "stack_trace", columnDefinition = "TEXT")
    private String stackTrace;
    
    /**
     * Root cause
     */
    @Column(name = "root_cause", columnDefinition = "TEXT")
    private String rootCause;
    
    /**
     * Module name where error occurred
     */
    @Column(name = "module_name", length = 50)
    private String moduleName;
    
    /**
     * Class name where error occurred
     */
    @Column(name = "class_name", length = 200)
    private String className;
    
    /**
     * Method name where error occurred
     */
    @Column(name = "method_name", length = 200)
    private String methodName;
    
    /**
     * Line number where error occurred
     */
    @Column(name = "line_number")
    private Integer lineNumber;
    
    /**
     * User ID (if error is related to a user action)
     */
    @Column(name = "user_id")
    private Long userId;
    
    /**
     * Username
     */
    @Column(name = "username", length = 50)
    private String username;
    
    /**
     * Request URL
     */
    @Column(name = "request_url", length = 500)
    private String requestUrl;
    
    /**
     * Request method (GET, POST, PUT, DELETE)
     */
    @Column(name = "request_method", length = 10)
    private String requestMethod;
    
    /**
     * Request parameters (JSON)
     */
    @Column(name = "request_parameters", columnDefinition = "TEXT")
    private String requestParameters;
    
    /**
     * Request body (JSON)
     */
    @Column(name = "request_body", columnDefinition = "TEXT")
    private String requestBody;
    
    /**
     * Response status code
     */
    @Column(name = "response_status")
    private Integer responseStatus;
    
    /**
     * IP address
     */
    @Column(name = "ip_address", length = 45)
    private String ipAddress;
    
    /**
     * User agent
     */
    @Column(name = "user_agent", length = 500)
    private String userAgent;
    
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
     * Server name/hostname
     */
    @Column(name = "server_name", length = 100)
    private String serverName;
    
    /**
     * Application version
     */
    @Column(name = "application_version", length = 20)
    private String applicationVersion;
    
    /**
     * Environment (PRODUCTION, STAGING, DEVELOPMENT)
     */
    @Column(name = "environment", length = 20)
    private String environment;
    
    /**
     * Status (NEW, ACKNOWLEDGED, IN_PROGRESS, RESOLVED, CLOSED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Assigned to (user ID who is handling this error)
     */
    @Column(name = "assigned_to")
    private Long assignedTo;
    
    /**
     * Assigned to username
     */
    @Column(name = "assigned_to_username", length = 50)
    private String assignedToUsername;
    
    /**
     * Resolved at
     */
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    
    /**
     * Resolved by
     */
    @Column(name = "resolved_by")
    private Long resolvedBy;
    
    /**
     * Resolved by username
     */
    @Column(name = "resolved_by_username", length = 50)
    private String resolvedByUsername;
    
    /**
     * Resolution notes
     */
    @Column(name = "resolution_notes", columnDefinition = "TEXT")
    private String resolutionNotes;
    
    /**
     * Occurrence count (how many times this error occurred)
     */
    @Column(name = "occurrence_count")
    private Integer occurrenceCount;
    
    /**
     * First occurrence
     */
    @Column(name = "first_occurrence")
    private LocalDateTime firstOccurrence;
    
    /**
     * Last occurrence
     */
    @Column(name = "last_occurrence")
    private LocalDateTime lastOccurrence;
    
    /**
     * Is notified (notification sent to admin)
     */
    @Column(name = "is_notified")
    private Boolean isNotified;
    
    /**
     * Notification sent at
     */
    @Column(name = "notification_sent_at")
    private LocalDateTime notificationSentAt;
    
    /**
     * Tags (comma-separated tags)
     */
    @Column(name = "tags", length = 500)
    private String tags;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Checks if is critical
     */
    @Transient
    public boolean isCritical() {
        return "CRITICAL".equals(severityLevel);
    }
    
    /**
     * Checks if is high severity
     */
    @Transient
    public boolean isHighSeverity() {
        return "HIGH".equals(severityLevel);
    }
    
    /**
     * Checks if is new
     */
    @Transient
    public boolean isNew() {
        return "NEW".equals(status);
    }
    
    /**
     * Checks if is resolved
     */
    @Transient
    public boolean isResolved() {
        return "RESOLVED".equals(status) || "CLOSED".equals(status);
    }
    
    /**
     * Gets resolution time in hours
     */
    @Transient
    public Long getResolutionTimeHours() {
        if (resolvedAt == null || errorTimestamp == null) {
            return null;
        }
        return java.time.Duration.between(errorTimestamp, resolvedAt).toHours();
    }
    
    /**
     * Gets hours since error
     */
    @Transient
    public Long getHoursSinceError() {
        if (errorTimestamp == null) {
            return null;
        }
        return java.time.Duration.between(errorTimestamp, LocalDateTime.now()).toHours();
    }
    
    /**
     * Checks if should notify
     */
    @Transient
    public boolean shouldNotify() {
        return !isNotified && (isCritical() || isHighSeverity());
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (errorTimestamp == null) {
            errorTimestamp = LocalDateTime.now();
        }
        if (status == null) {
            status = "NEW";
        }
        if (severityLevel == null) {
            severityLevel = "MEDIUM";
        }
        if (occurrenceCount == null) {
            occurrenceCount = 1;
        }
        if (firstOccurrence == null) {
            firstOccurrence = errorTimestamp;
        }
        if (lastOccurrence == null) {
            lastOccurrence = errorTimestamp;
        }
        if (isNotified == null) {
            isNotified = false;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorLog)) return false;
        ErrorLog that = (ErrorLog) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
