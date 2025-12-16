package lk.epicgreen.erp.notification.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.time.LocalDateTime;

/**
 * NotificationQueue entity
 * Represents queued notifications waiting to be sent
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "notification_queue", indexes = {
    @Index(name = "idx_notification_queue_status", columnList = "status"),
    @Index(name = "idx_notification_queue_type", columnList = "notification_type"),
    @Index(name = "idx_notification_queue_priority", columnList = "priority, scheduled_time"),
    @Index(name = "idx_notification_queue_recipient", columnList = "recipient"),
    @Index(name = "idx_notification_queue_template", columnList = "template_id"),
    @Index(name = "idx_notification_queue_scheduled", columnList = "scheduled_time")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationQueue extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Template reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", foreignKey = @ForeignKey(name = "fk_notification_queue_template"))
    private NotificationTemplate template;
    
    /**
     * Notification type (EMAIL, SMS, PUSH, IN_APP)
     */
    @Column(name = "notification_type", nullable = false, length = 20)
    private String notificationType;
    
    /**
     * Category (TRANSACTION, ALERT, REMINDER, MARKETING, SYSTEM)
     */
    @Column(name = "category", length = 30)
    private String category;
    
    /**
     * Recipient (email address, phone number, user ID, device token)
     */
    @Column(name = "recipient", nullable = false, length = 200)
    private String recipient;
    
    /**
     * Recipient name
     */
    @Column(name = "recipient_name", length = 100)
    private String recipientName;
    
    /**
     * User ID (if recipient is a user)
     */
    @Column(name = "user_id")
    private Long userId;
    
    /**
     * Sender (email address or phone number)
     */
    @Column(name = "sender", length = 100)
    private String sender;
    
    /**
     * Sender name
     */
    @Column(name = "sender_name", length = 100)
    private String senderName;
    
    /**
     * Subject (for email) / Title (for push/in-app)
     */
    @Column(name = "subject", length = 500)
    private String subject;
    
    /**
     * Body content (final rendered content)
     */
    @Column(name = "body", columnDefinition = "TEXT")
    private String body;
    
    /**
     * HTML body (for email)
     */
    @Column(name = "html_body", columnDefinition = "TEXT")
    private String htmlBody;
    
    /**
     * Variables (JSON - variable values used)
     */
    @Column(name = "variables", columnDefinition = "TEXT")
    private String variables;
    
    /**
     * CC (comma-separated email addresses)
     */
    @Column(name = "cc", length = 500)
    private String cc;
    
    /**
     * BCC (comma-separated email addresses)
     */
    @Column(name = "bcc", length = 500)
    private String bcc;
    
    /**
     * Reply to
     */
    @Column(name = "reply_to", length = 100)
    private String replyTo;
    
    /**
     * Attachments (JSON - list of attachment paths)
     */
    @Column(name = "attachments", columnDefinition = "TEXT")
    private String attachments;
    
    /**
     * Priority (HIGH, MEDIUM, LOW)
     */
    @Column(name = "priority", length = 10)
    private String priority;
    
    /**
     * Status (PENDING, PROCESSING, SENT, FAILED, CANCELLED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Scheduled time (when to send)
     */
    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;
    
    /**
     * Sent time
     */
    @Column(name = "sent_time")
    private LocalDateTime sentTime;
    
    /**
     * Failed time
     */
    @Column(name = "failed_time")
    private LocalDateTime failedTime;
    
    /**
     * Retry count
     */
    @Column(name = "retry_count")
    private Integer retryCount;
    
    /**
     * Max retry attempts
     */
    @Column(name = "max_retry_attempts")
    private Integer maxRetryAttempts;
    
    /**
     * Last retry time
     */
    @Column(name = "last_retry_time")
    private LocalDateTime lastRetryTime;
    
    /**
     * Next retry time
     */
    @Column(name = "next_retry_time")
    private LocalDateTime nextRetryTime;
    
    /**
     * Error message
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    /**
     * Error details
     */
    @Column(name = "error_details", columnDefinition = "TEXT")
    private String errorDetails;
    
    /**
     * Provider (email provider, SMS gateway, push service)
     */
    @Column(name = "provider", length = 50)
    private String provider;
    
    /**
     * Provider message ID (external reference)
     */
    @Column(name = "provider_message_id", length = 100)
    private String providerMessageId;
    
    /**
     * Provider response
     */
    @Column(name = "provider_response", columnDefinition = "TEXT")
    private String providerResponse;
    
    /**
     * Reference type (e.g., ORDER, INVOICE, PAYMENT)
     */
    @Column(name = "reference_type", length = 50)
    private String referenceType;
    
    /**
     * Reference ID
     */
    @Column(name = "reference_id")
    private Long referenceId;
    
    /**
     * Reference number
     */
    @Column(name = "reference_number", length = 50)
    private String referenceNumber;
    
    /**
     * Batch ID (for bulk sending)
     */
    @Column(name = "batch_id", length = 50)
    private String batchId;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Checks if is pending
     */
    @Transient
    public boolean isPending() {
        return "PENDING".equals(status);
    }
    
    /**
     * Checks if is processing
     */
    @Transient
    public boolean isProcessing() {
        return "PROCESSING".equals(status);
    }
    
    /**
     * Checks if is sent
     */
    @Transient
    public boolean isSent() {
        return "SENT".equals(status);
    }
    
    /**
     * Checks if is failed
     */
    @Transient
    public boolean isFailed() {
        return "FAILED".equals(status);
    }
    
    /**
     * Checks if can retry
     */
    @Transient
    public boolean canRetry() {
        if (retryCount == null) {
            retryCount = 0;
        }
        if (maxRetryAttempts == null) {
            maxRetryAttempts = 3;
        }
        return isFailed() && retryCount < maxRetryAttempts;
    }
    
    /**
     * Checks if should send now
     */
    @Transient
    public boolean shouldSendNow() {
        if (!isPending()) {
            return false;
        }
        if (scheduledTime == null) {
            return true;
        }
        return LocalDateTime.now().isAfter(scheduledTime);
    }
    
    /**
     * Gets minutes until scheduled
     */
    @Transient
    public Long getMinutesUntilScheduled() {
        if (scheduledTime == null) {
            return 0L;
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(scheduledTime)) {
            return 0L;
        }
        return java.time.Duration.between(now, scheduledTime).toMinutes();
    }
    
    /**
     * Gets minutes since sent
     */
    @Transient
    public Long getMinutesSinceSent() {
        if (sentTime == null) {
            return null;
        }
        return java.time.Duration.between(sentTime, LocalDateTime.now()).toMinutes();
    }
    
    /**
     * Checks if is high priority
     */
    @Transient
    public boolean isHighPriority() {
        return "HIGH".equals(priority);
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "PENDING";
        }
        if (priority == null) {
            priority = "MEDIUM";
        }
        if (retryCount == null) {
            retryCount = 0;
        }
        if (maxRetryAttempts == null) {
            maxRetryAttempts = 3;
        }
        if (scheduledTime == null) {
            scheduledTime = LocalDateTime.now();
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
