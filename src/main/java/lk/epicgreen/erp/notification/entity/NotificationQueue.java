package lk.epicgreen.erp.notification.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lk.epicgreen.erp.admin.entity.User;
import lombok.*;

import java.time.LocalDateTime;

/**
 * NotificationQueue entity
 * Represents queued notifications for delivery
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "notifications_queue", indexes = {
    @Index(name = "idx_recipient_user", columnList = "recipient_user_id"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_notification_type", columnList = "notification_type"),
    @Index(name = "idx_scheduled_at", columnList = "scheduled_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationQueue {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Recipient user (optional, for registered users)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_user_id", foreignKey = @ForeignKey(name = "fk_notification_queue_recipient"))
    private User recipientUser;
    
    /**
     * Recipient email (for email notifications)
     */
    @Email(message = "Invalid email format")
    @Size(max = 100)
    @Column(name = "recipient_email", length = 100)
    private String recipientEmail;
    
    /**
     * Recipient mobile (for SMS notifications)
     */
    @Size(max = 20)
    @Column(name = "recipient_mobile", length = 20)
    private String recipientMobile;
    
    /**
     * Notification type (EMAIL, SMS, PUSH, IN_APP)
     */
    @NotBlank(message = "Notification type is required")
    @Column(name = "notification_type", nullable = false, length = 10)
    private String notificationType;
    
    /**
     * Template reference (optional)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", foreignKey = @ForeignKey(name = "fk_notification_queue_template"))
    private NotificationTemplate template;
    
    /**
     * Subject (for email/push)
     */
    @Size(max = 200)
    @Column(name = "subject", length = 200)
    private String subject;
    
    /**
     * Message (rendered body)
     */
    @NotBlank(message = "Message is required")
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;
    
    /**
     * Priority (1-10, 1 = highest)
     */
    @Min(1)
    @Max(10)
    @Column(name = "priority")
    private Integer priority;
    
    /**
     * Status (PENDING, SENT, FAILED, CANCELLED)
     */
    @Column(name = "status", length = 10)
    private String status;
    
    /**
     * Retry count
     */
    @PositiveOrZero(message = "Retry count must be positive or zero")
    @Column(name = "retry_count")
    private Integer retryCount;
    
    /**
     * Max retries allowed
     */
    @Positive(message = "Max retries must be positive")
    @Column(name = "max_retries")
    private Integer maxRetries;
    
    /**
     * Scheduled send time (for delayed notifications)
     */
    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;
    
    /**
     * Sent timestamp
     */
    @Column(name = "sent_at")
    private LocalDateTime sentAt;
    
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
     * Notification type checks
     */
    @Transient
    public boolean isEmail() {
        return "EMAIL".equals(notificationType);
    }
    
    @Transient
    public boolean isSms() {
        return "SMS".equals(notificationType);
    }
    
    @Transient
    public boolean isPush() {
        return "PUSH".equals(notificationType);
    }
    
    @Transient
    public boolean isInApp() {
        return "IN_APP".equals(notificationType);
    }
    
    /**
     * Status checks
     */
    @Transient
    public boolean isPending() {
        return "PENDING".equals(status);
    }
    
    @Transient
    public boolean isSent() {
        return "SENT".equals(status);
    }
    
    @Transient
    public boolean isFailed() {
        return "FAILED".equals(status);
    }
    
    @Transient
    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }
    
    /**
     * Check if can retry
     */
    @Transient
    public boolean canRetry() {
        int retries = retryCount != null ? retryCount : 0;
        int maxRetry = maxRetries != null ? maxRetries : 3;
        return retries < maxRetry;
    }
    
    /**
     * Get remaining retries
     */
    @Transient
    public int getRemainingRetries() {
        int retries = retryCount != null ? retryCount : 0;
        int maxRetry = maxRetries != null ? maxRetries : 3;
        return Math.max(0, maxRetry - retries);
    }
    
    /**
     * Check if high priority
     */
    @Transient
    public boolean isHighPriority() {
        return priority != null && priority <= 3;
    }
    
    /**
     * Check if scheduled
     */
    @Transient
    public boolean isScheduled() {
        return scheduledAt != null;
    }
    
    /**
     * Check if ready to send
     */
    @Transient
    public boolean isReadyToSend() {
        if (!isPending()) {
            return false;
        }
        
        if (scheduledAt == null) {
            return true;
        }
        
        return LocalDateTime.now().isAfter(scheduledAt) || LocalDateTime.now().isEqual(scheduledAt);
    }
    
    /**
     * Mark as sent
     */
    public void markAsSent() {
        if (!isPending()) {
            throw new IllegalStateException("Only pending notifications can be marked as sent");
        }
        this.status = "SENT";
        this.sentAt = LocalDateTime.now();
    }
    
    /**
     * Mark as failed
     */
    public void markAsFailed(String error) {
        this.status = "FAILED";
        this.errorMessage = error;
        
        int retries = retryCount != null ? retryCount : 0;
        this.retryCount = retries + 1;
    }
    
    /**
     * Cancel notification
     */
    public void cancel() {
        if (isSent()) {
            throw new IllegalStateException("Cannot cancel sent notifications");
        }
        this.status = "CANCELLED";
    }
    
    /**
     * Retry notification
     */
    public void retry() {
        if (!canRetry()) {
            throw new IllegalStateException("Maximum retries exceeded");
        }
        this.status = "PENDING";
        this.errorMessage = null;
    }
    
    /**
     * Get notification summary
     */
    @Transient
    public String getNotificationSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(notificationType);
        
        if (recipientUser != null) {
            summary.append(" to ").append(recipientUser.getUsername());
        } else if (recipientEmail != null) {
            summary.append(" to ").append(recipientEmail);
        } else if (recipientMobile != null) {
            summary.append(" to ").append(recipientMobile);
        }
        
        summary.append(" - ").append(status);
        
        if (priority != null && isHighPriority()) {
            summary.append(" (HIGH PRIORITY)");
        }
        
        return summary.toString();
    }
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = "PENDING";
        }
        if (priority == null) {
            priority = 5;
        }
        if (retryCount == null) {
            retryCount = 0;
        }
        if (maxRetries == null) {
            maxRetries = 3;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationQueue)) return false;
        NotificationQueue that = (NotificationQueue) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
