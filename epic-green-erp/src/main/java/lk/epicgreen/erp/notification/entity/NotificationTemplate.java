package lk.epicgreen.erp.notification.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

/**
 * NotificationTemplate entity
 * Represents notification templates for emails, SMS, push notifications
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "notification_templates", indexes = {
    @Index(name = "idx_notification_template_code", columnList = "template_code"),
    @Index(name = "idx_notification_template_type", columnList = "notification_type"),
    @Index(name = "idx_notification_template_category", columnList = "category"),
    @Index(name = "idx_notification_template_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationTemplate extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Template code (unique identifier)
     */
    @Column(name = "template_code", nullable = false, unique = true, length = 50)
    private String templateCode;
    
    /**
     * Template name
     */
    @Column(name = "template_name", nullable = false, length = 200)
    private String templateName;
    
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
     * Event trigger (ORDER_CREATED, PAYMENT_RECEIVED, STOCK_LOW, etc.)
     */
    @Column(name = "event_trigger", length = 50)
    private String eventTrigger;
    
    /**
     * Subject (for email) / Title (for push/in-app)
     */
    @Column(name = "subject", length = 500)
    private String subject;
    
    /**
     * Body template (supports variables like {{customerName}}, {{orderNumber}})
     */
    @Column(name = "body_template", columnDefinition = "TEXT")
    private String bodyTemplate;
    
    /**
     * HTML body template (for email)
     */
    @Column(name = "html_body_template", columnDefinition = "TEXT")
    private String htmlBodyTemplate;
    
    /**
     * SMS template (for SMS notifications)
     */
    @Column(name = "sms_template", length = 500)
    private String smsTemplate;
    
    /**
     * Variables (JSON - list of available variables)
     */
    @Column(name = "variables", columnDefinition = "TEXT")
    private String variables;
    
    /**
     * Default sender (email address or phone number)
     */
    @Column(name = "default_sender", length = 100)
    private String defaultSender;
    
    /**
     * Default sender name
     */
    @Column(name = "default_sender_name", length = 100)
    private String defaultSenderName;
    
    /**
     * Reply to (email address)
     */
    @Column(name = "reply_to", length = 100)
    private String replyTo;
    
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
     * Is active
     */
    @Column(name = "is_active")
    private Boolean isActive;
    
    /**
     * Is system template (cannot be deleted)
     */
    @Column(name = "is_system")
    private Boolean isSystem;
    
    /**
     * Requires approval
     */
    @Column(name = "requires_approval")
    private Boolean requiresApproval;
    
    /**
     * Auto send (send automatically when triggered)
     */
    @Column(name = "auto_send")
    private Boolean autoSend;
    
    /**
     * Status (ACTIVE, INACTIVE, DRAFT, ARCHIVED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Locale (language)
     */
    @Column(name = "locale", length = 10)
    private String locale;
    
    /**
     * Description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Usage count
     */
    @Column(name = "usage_count")
    private Integer usageCount;
    
    /**
     * Checks if is email type
     */
    @Transient
    public boolean isEmailType() {
        return "EMAIL".equals(notificationType);
    }
    
    /**
     * Checks if is SMS type
     */
    @Transient
    public boolean isSmsType() {
        return "SMS".equals(notificationType);
    }
    
    /**
     * Checks if is push type
     */
    @Transient
    public boolean isPushType() {
        return "PUSH".equals(notificationType);
    }
    
    /**
     * Checks if is in-app type
     */
    @Transient
    public boolean isInAppType() {
        return "IN_APP".equals(notificationType);
    }
    
    /**
     * Checks if can be deleted
     */
    @Transient
    public boolean canDelete() {
        return !isSystem;
    }
    
    /**
     * Checks if can be edited
     */
    @Transient
    public boolean canEdit() {
        return !isSystem || "DRAFT".equals(status);
    }
    
    /**
     * Gets template for notification type
     */
    @Transient
    public String getTemplateForType() {
        if (isSmsType()) {
            return smsTemplate;
        } else if (isEmailType() && htmlBodyTemplate != null) {
            return htmlBodyTemplate;
        } else {
            return bodyTemplate;
        }
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "ACTIVE";
        }
        if (isActive == null) {
            isActive = true;
        }
        if (isSystem == null) {
            isSystem = false;
        }
        if (requiresApproval == null) {
            requiresApproval = false;
        }
        if (autoSend == null) {
            autoSend = false;
        }
        if (priority == null) {
            priority = "MEDIUM";
        }
        if (usageCount == null) {
            usageCount = 0;
        }
        if (locale == null) {
            locale = "en";
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationTemplate)) return false;
        NotificationTemplate that = (NotificationTemplate) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
