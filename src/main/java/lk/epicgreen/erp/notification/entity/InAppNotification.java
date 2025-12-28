package lk.epicgreen.erp.notification.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lk.epicgreen.erp.admin.entity.User;
import lombok.*;

import java.time.LocalDateTime;

/**
 * InAppNotification entity
 * Represents in-app notifications displayed in user dashboard
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "in_app_notifications", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_is_read", columnList = "is_read"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InAppNotification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * User reference
     */
    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_in_app_notification_user"))
    private User user;
    
    /**
     * Notification title
     */
    @NotBlank(message = "Notification title is required")
    @Size(max = 200)
    @Column(name = "notification_title", nullable = false, length = 200)
    private String notificationTitle;
    
    /**
     * Notification message
     */
    @NotBlank(message = "Notification message is required")
    @Column(name = "notification_message", nullable = false, columnDefinition = "TEXT")
    private String notificationMessage;
    
    /**
     * Notification type (INFO, WARNING, ERROR, SUCCESS)
     */
    @Column(name = "notification_type", length = 10)
    private String notificationType;
    
    /**
     * Action URL (optional link to relevant page)
     */
    @Size(max = 500)
    @Column(name = "action_url", length = 500)
    private String actionUrl;
    
    /**
     * Is read
     */
    @Column(name = "is_read")
    private Boolean isRead;
    
    /**
     * Read timestamp
     */
    @Column(name = "read_at")
    private LocalDateTime readAt;
    
    /**
     * Expiry timestamp (optional)
     */
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    
    /**
     * Created timestamp
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /**
     * Notification type checks
     */
    @Transient
    public boolean isInfo() {
        return "INFO".equals(notificationType);
    }
    
    @Transient
    public boolean isWarning() {
        return "WARNING".equals(notificationType);
    }
    
    @Transient
    public boolean isError() {
        return "ERROR".equals(notificationType);
    }
    
    @Transient
    public boolean isSuccess() {
        return "SUCCESS".equals(notificationType);
    }
    
    /**
     * Check if read
     */
    @Transient
    public boolean isRead() {
        return Boolean.TRUE.equals(isRead);
    }
    
    /**
     * Check if unread
     */
    @Transient
    public boolean isUnread() {
        return !isRead();
    }
    
    /**
     * Check if expired
     */
    @Transient
    public boolean isExpired() {
        if (expiresAt == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    /**
     * Check if has action URL
     */
    @Transient
    public boolean hasActionUrl() {
        return actionUrl != null && !actionUrl.isEmpty();
    }
    
    /**
     * Check if has expiry
     */
    @Transient
    public boolean hasExpiry() {
        return expiresAt != null;
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
    
    /**
     * Check if new (less than 24 hours old)
     */
    @Transient
    public boolean isNew() {
        return getAgeHours() < 24;
    }
    
    /**
     * Mark as read
     */
    public void markAsRead() {
        if (isRead()) {
            return;
        }
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }
    
    /**
     * Mark as unread
     */
    public void markAsUnread() {
        this.isRead = false;
        this.readAt = null;
    }
    
    /**
     * Set expiry (hours from now)
     */
    public void setExpiryHours(int hours) {
        this.expiresAt = LocalDateTime.now().plusHours(hours);
    }
    
    /**
     * Set expiry (days from now)
     */
    public void setExpiryDays(int days) {
        this.expiresAt = LocalDateTime.now().plusDays(days);
    }
    
    /**
     * Clear expiry
     */
    public void clearExpiry() {
        this.expiresAt = null;
    }
    
    /**
     * Get CSS class for notification type
     */
    @Transient
    public String getCssClass() {
        if (isInfo()) {
            return "notification-info";
        } else if (isWarning()) {
            return "notification-warning";
        } else if (isError()) {
            return "notification-error";
        } else if (isSuccess()) {
            return "notification-success";
        }
        return "notification-default";
    }
    
    /**
     * Get icon name for notification type
     */
    @Transient
    public String getIconName() {
        if (isInfo()) {
            return "info-circle";
        } else if (isWarning()) {
            return "exclamation-triangle";
        } else if (isError()) {
            return "times-circle";
        } else if (isSuccess()) {
            return "check-circle";
        }
        return "bell";
    }
    
    /**
     * Get notification summary
     */
    @Transient
    public String getNotificationSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(notificationType).append(": ");
        summary.append(notificationTitle);
        
        if (isRead()) {
            summary.append(" (READ)");
        } else {
            summary.append(" (UNREAD)");
        }
        
        if (isNew()) {
            summary.append(" [NEW]");
        }
        
        return summary.toString();
    }
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (isRead == null) {
            isRead = false;
        }
        if (notificationType == null) {
            notificationType = "INFO";
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InAppNotification)) return false;
        InAppNotification that = (InAppNotification) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
