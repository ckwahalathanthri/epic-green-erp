package lk.epicgreen.erp.notification.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

/**
 * NotificationTemplate entity
 * Represents reusable notification templates for different channels
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "notification_templates", indexes = {
    @Index(name = "idx_template_code", columnList = "template_code"),
    @Index(name = "idx_notification_type", columnList = "notification_type")
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
    @NotBlank(message = "Template code is required")
    @Size(max = 50)
    @Column(name = "template_code", nullable = false, unique = true, length = 50)
    private String templateCode;
    
    /**
     * Template name (display name)
     */
    @NotBlank(message = "Template name is required")
    @Size(max = 100)
    @Column(name = "template_name", nullable = false, length = 100)
    private String templateName;
    
    /**
     * Notification type (EMAIL, SMS, PUSH, IN_APP)
     */
    @NotBlank(message = "Notification type is required")
    @Column(name = "notification_type", nullable = false, length = 10)
    private String notificationType;
    
    /**
     * Subject (for email/push notifications)
     */
    @Size(max = 200)
    @Column(name = "subject", length = 200)
    private String subject;
    
    /**
     * Body template (with placeholders)
     */
    @NotBlank(message = "Body template is required")
    @Column(name = "body_template", nullable = false, columnDefinition = "TEXT")
    private String bodyTemplate;
    
    /**
     * Variables (JSON array of available placeholders)
     */
    @Column(name = "variables", columnDefinition = "JSON")
    private String variables;
    
    /**
     * Is active
     */
    @Column(name = "is_active")
    private Boolean isActive;
    
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
     * Check if active
     */
    @Transient
    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }
    
    /**
     * Check if has subject
     */
    @Transient
    public boolean hasSubject() {
        return subject != null && !subject.isEmpty();
    }
    
    /**
     * Check if has variables
     */
    @Transient
    public boolean hasVariables() {
        return variables != null && !variables.isEmpty();
    }
    
    /**
     * Render template with variables
     */
    @Transient
    public String render(java.util.Map<String, String> values) {
        String rendered = bodyTemplate;
        
        if (values != null) {
            for (java.util.Map.Entry<String, String> entry : values.entrySet()) {
                String placeholder = "{{" + entry.getKey() + "}}";
                rendered = rendered.replace(placeholder, entry.getValue());
            }
        }
        
        return rendered;
    }
    
    /**
     * Render subject with variables
     */
    @Transient
    public String renderSubject(java.util.Map<String, String> values) {
        if (subject == null) {
            return null;
        }
        
        String rendered = subject;
        
        if (values != null) {
            for (java.util.Map.Entry<String, String> entry : values.entrySet()) {
                String placeholder = "{{" + entry.getKey() + "}}";
                rendered = rendered.replace(placeholder, entry.getValue());
            }
        }
        
        return rendered;
    }
    
    /**
     * Activate template
     */
    public void activate() {
        this.isActive = true;
    }
    
    /**
     * Deactivate template
     */
    public void deactivate() {
        this.isActive = false;
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (isActive == null) {
            isActive = true;
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
