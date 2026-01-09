package lk.epicgreen.erp.audit.entity;


import lk.epicgreen.erp.admin.entity.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * ErrorLog entity
 * Tracks application errors and exceptions for debugging and monitoring
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "error_logs", indexes = {
    @Index(name = "idx_error_type", columnList = "error_type"),
    @Index(name = "idx_severity", columnList = "severity"),
    @Index(name = "idx_created_at", columnList = "created_at"),
    @Index(name = "idx_is_resolved", columnList = "is_resolved")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Error type (e.g., VALIDATION, DATABASE, NETWORK, BUSINESS_LOGIC)
     */
    @NotBlank(message = "Error type is required")
    @Size(max = 50)
    @Column(name = "error_type", nullable = false, length = 50)
    private String errorType;
    
    /**
     * Error code (application-specific error code)
     */
    @Size(max = 20)
    @Column(name = "error_code", length = 20)
    private String errorCode;
    
    /**
     * Error message
     */
    @NotBlank(message = "Error message is required")
    @Column(name = "error_message", nullable = false, columnDefinition = "TEXT")
    private String errorMessage;
    
    /**
     * Stack trace
     */
    @Column(name = "stack_trace", columnDefinition = "TEXT")
    private String stackTrace;
    
    /**
     * Request URL
     */
    @Size(max = 500)
    @Column(name = "request_url", length = 500)
    private String requestUrl;
    
    /**
     * Request method (GET, POST, PUT, DELETE)
     */
    @Size(max = 10)
    @Column(name = "request_method", length = 10)
    private String requestMethod;
    
    /**
     * Request body
     */
    @Column(name = "request_body", columnDefinition = "TEXT")
    private String requestBody;
    
    /**
     * User reference (nullable for unauthenticated requests)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_error_log_user"))
    private User user;
    
    /**
     * IP address
     */
    @Size(max = 45)
    @Column(name = "ip_address", length = 45)
    private String ipAddress;
    
    /**
     * User agent
     */
    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;
    
    /**
     * Severity (LOW, MEDIUM, HIGH, CRITICAL)
     */
    @Column(name = "severity", length = 10)
    private String severity;
    
    /**
     * Is resolved
     */
    @Column(name = "is_resolved")
    private Boolean isResolved;
    
    /**
     * Resolved by (user who resolved)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by", foreignKey = @ForeignKey(name = "fk_error_log_resolved_by"))
    private User resolvedBy;
    
    /**
     * Resolved timestamp
     */
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    
    /**
     * Created timestamp
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /**
     * Error type checks
     */
    @Transient
    public boolean isValidationError() {
        return "VALIDATION".equals(errorType);
    }
    
    @Transient
    public boolean isDatabaseError() {
        return "DATABASE".equals(errorType);
    }
    
    @Transient
    public boolean isNetworkError() {
        return "NETWORK".equals(errorType);
    }
    
    @Transient
    public boolean isBusinessLogicError() {
        return "BUSINESS_LOGIC".equals(errorType);
    }
    
    @Transient
    public boolean isSecurityError() {
        return "SECURITY".equals(errorType);
    }
    
    @Transient
    public boolean isAuthenticationError() {
        return "AUTHENTICATION".equals(errorType);
    }
    
    @Transient
    public boolean isAuthorizationError() {
        return "AUTHORIZATION".equals(errorType);
    }
    
    /**
     * Severity checks
     */
    @Transient
    public boolean isLow() {
        return "LOW".equals(severity);
    }
    
    @Transient
    public boolean isMedium() {
        return "MEDIUM".equals(severity);
    }
    
    @Transient
    public boolean isHigh() {
        return "HIGH".equals(severity);
    }
    
    @Transient
    public boolean isCritical() {
        return "CRITICAL".equals(severity);
    }
    
    /**
     * Request method checks
     */
    @Transient
    public boolean isGet() {
        return "GET".equals(requestMethod);
    }
    
    @Transient
    public boolean isPost() {
        return "POST".equals(requestMethod);
    }
    
    @Transient
    public boolean isPut() {
        return "PUT".equals(requestMethod);
    }
    
    @Transient
    public boolean isDelete() {
        return "DELETE".equals(requestMethod);
    }
    
    /**
     * Check if resolved
     */
    @Transient
    public boolean isResolved() {
        return Boolean.TRUE.equals(isResolved);
    }
    
    /**
     * Check if has stack trace
     */
    @Transient
    public boolean hasStackTrace() {
        return stackTrace != null && !stackTrace.isEmpty();
    }
    
    /**
     * Check if has request details
     */
    @Transient
    public boolean hasRequestDetails() {
        return requestUrl != null || requestMethod != null || requestBody != null;
    }
    
    /**
     * Resolve error
     */
    public void resolve(User resolver) {
        if (isResolved()) {
            throw new IllegalStateException("Error is already resolved");
        }
        this.isResolved = true;
        this.resolvedBy = resolver;
        this.resolvedAt = LocalDateTime.now();
    }
    
    /**
     * Reopen error
     */
    public void reopen() {
        if (!isResolved()) {
            throw new IllegalStateException("Error is not resolved");
        }
        this.isResolved = false;
        this.resolvedBy = null;
        this.resolvedAt = null;
    }
    
    /**
     * Get error summary
     */
    @Transient
    public String getErrorSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(severity).append(" - ").append(errorType);
        if (errorCode != null) {
            summary.append(" [").append(errorCode).append("]");
        }
        summary.append(": ").append(errorMessage, 0, Math.min(100, errorMessage.length()));
        if (errorMessage.length() > 100) {
            summary.append("...");
        }
        if (isResolved()) {
            summary.append(" - RESOLVED");
        }
        return summary.toString();
    }
    
    /**
     * Get short stack trace (first 10 lines)
     */
    @Transient
    public String getShortStackTrace() {
        if (!hasStackTrace()) {
            return null;
        }
        
        String[] lines = stackTrace.split("\n");
        int maxLines = Math.min(10, lines.length);
        StringBuilder shortTrace = new StringBuilder();
        
        for (int i = 0; i < maxLines; i++) {
            shortTrace.append(lines[i]).append("\n");
        }
        
        if (lines.length > 10) {
            shortTrace.append("... (").append(lines.length - 10).append(" more lines)");
        }
        
        return shortTrace.toString();
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
    
    /**
     * Get age in hours
     */
    @Transient
    public long getAgeHours() {
        if (createdAt == null) {
            return 0;
        }
        return java.time.Duration.between(createdAt, LocalDateTime.now()).toHours();
    }
    
    /**
     * Get age in days
     */
    @Transient
    public long getAgeDays() {
        if (createdAt == null) {
            return 0;
        }
        return java.time.Duration.between(createdAt, LocalDateTime.now()).toDays();
    }
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (severity == null) {
            severity = "MEDIUM";
        }
        if (isResolved == null) {
            isResolved = false;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorLog)) return false;
        ErrorLog errorLog = (ErrorLog) o;
        return id != null && id.equals(errorLog.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
