package lk.epicgreen.erp.audit.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ActivityLog entity
 * Tracks user activities like login, logout, page views, feature usage
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "activity_logs", indexes = {
    @Index(name = "idx_activity_log_user", columnList = "user_id"),
    @Index(name = "idx_activity_log_activity", columnList = "activity_type"),
    @Index(name = "idx_activity_log_timestamp", columnList = "activity_timestamp"),
    @Index(name = "idx_activity_log_session", columnList = "session_id"),
    @Index(name = "idx_activity_log_page", columnList = "page_url")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLog extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * User ID
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
     * Activity type (LOGIN, LOGOUT, PAGE_VIEW, FEATURE_USED, DOWNLOAD, UPLOAD, etc.)
     */
    @Column(name = "activity_type", nullable = false, length = 30)
    private String activityType;
    
    /**
     * Activity description
     */
    @Column(name = "activity_description", length = 500)
    private String activityDescription;
    
    /**
     * Activity timestamp
     */
    @Column(name = "activity_timestamp", nullable = false)
    private LocalDateTime activityTimestamp;
    
    /**
     * Module name (ADMIN, SALES, PURCHASE, INVENTORY, etc.)
     */
    @Column(name = "module_name", length = 50)
    private String moduleName;
    
    /**
     * Feature name (specific feature used)
     */
    @Column(name = "feature_name", length = 100)
    private String featureName;
    
    /**
     * Page URL
     */
    @Column(name = "page_url", length = 500)
    private String pageUrl;
    
    /**
     * Page title
     */
    @Column(name = "page_title", length = 200)
    private String pageTitle;
    
    /**
     * Referrer URL
     */
    @Column(name = "referrer_url", length = 500)
    private String referrerUrl;
    
    /**
     * Session ID
     */
    @Column(name = "session_id", length = 100)
    private String sessionId;
    
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
     * Operating system
     */
    @Column(name = "operating_system", length = 50)
    private String operatingSystem;
    
    /**
     * Browser
     */
    @Column(name = "browser", length = 50)
    private String browser;
    
    /**
     * Location (city, country)
     */
    @Column(name = "location", length = 200)
    private String location;
    
    /**
     * Duration in milliseconds (for timed activities)
     */
    @Column(name = "duration_millis")
    private Long durationMillis;
    
    /**
     * Start time (for timed activities)
     */
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    /**
     * End time (for timed activities)
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
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
     * Metadata (JSON - additional activity data)
     */
    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;
    
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
     * Gets duration in seconds
     */
    @Transient
    public Double getDurationSeconds() {
        if (durationMillis == null) {
            return 0.0;
        }
        return durationMillis / 1000.0;
    }
    
    /**
     * Gets duration in minutes
     */
    @Transient
    public Double getDurationMinutes() {
        if (durationMillis == null) {
            return 0.0;
        }
        return durationMillis / (1000.0 * 60.0);
    }
    
    /**
     * Checks if is login activity
     */
    @Transient
    public boolean isLoginActivity() {
        return "LOGIN".equals(activityType);
    }
    
    /**
     * Checks if is logout activity
     */
    @Transient
    public boolean isLogoutActivity() {
        return "LOGOUT".equals(activityType);
    }
    
    /**
     * Checks if is page view
     */
    @Transient
    public boolean isPageView() {
        return "PAGE_VIEW".equals(activityType);
    }
    
    /**
     * Checks if is mobile device
     */
    @Transient
    public boolean isMobileDevice() {
        return "MOBILE".equals(deviceType);
    }
    
    /**
     * Gets hours since activity
     */
    @Transient
    public Long getHoursSinceActivity() {
        if (activityTimestamp == null) {
            return null;
        }
        return java.time.Duration.between(activityTimestamp, LocalDateTime.now()).toHours();
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (activityTimestamp == null) {
            activityTimestamp = LocalDateTime.now();
        }
        if (isSuccessful == null) {
            isSuccessful = true;
        }
        
        // Calculate duration if start and end times are set
        if (durationMillis == null && startTime != null && endTime != null) {
            durationMillis = java.time.Duration.between(startTime, endTime).toMillis();
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivityLog)) return false;
        ActivityLog that = (ActivityLog) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
