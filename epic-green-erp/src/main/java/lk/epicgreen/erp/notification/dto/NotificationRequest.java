package lk.epicgreen.erp.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Notification Request DTO
 * Request for sending notifications (email, SMS, push, in-app)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationRequest {
    
    /**
     * Template code (if using template)
     */
    private String templateCode;
    
    /**
     * Notification type (EMAIL, SMS, PUSH, IN_APP)
     */
    @NotBlank(message = "Notification type is required")
    private String notificationType;
    
    /**
     * Category (TRANSACTION, ALERT, REMINDER, MARKETING, SYSTEM)
     */
    private String category;
    
    /**
     * Recipients
     */
    @Valid
    @NotNull(message = "At least one recipient is required")
    private List<Recipient> recipients;
    
    /**
     * Sender information
     */
    private String sender;
    
    private String senderName;
    
    /**
     * Subject (for email) / Title (for push/in-app)
     */
    private String subject;
    
    /**
     * Body content (plain text)
     */
    private String body;
    
    /**
     * HTML body (for email)
     */
    private String htmlBody;
    
    /**
     * Variables (for template substitution)
     */
    private Map<String, Object> variables;
    
    /**
     * CC (for email)
     */
    private List<String> cc;
    
    /**
     * BCC (for email)
     */
    private List<String> bcc;
    
    /**
     * Reply to (for email)
     */
    private String replyTo;
    
    /**
     * Attachments
     */
    @Valid
    private List<Attachment> attachments;
    
    /**
     * Priority (HIGH, MEDIUM, LOW)
     */
    private String priority;
    
    /**
     * Scheduled time (when to send)
     */
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime scheduledTime;
    
    /**
     * Reference information
     */
    private String referenceType; // ORDER, INVOICE, PAYMENT, etc.
    
    private Long referenceId;
    
    private String referenceNumber;
    
    /**
     * Batch ID (for bulk sending)
     */
    private String batchId;
    
    /**
     * Send immediately (bypass queue)
     */
    private Boolean sendImmediately;
    
    /**
     * Retry configuration
     */
    private Integer maxRetryAttempts;
    
    /**
     * User ID (if recipient is a user)
     */
    private Long userId;
    
    /**
     * Notes
     */
    private String notes;
    
    /**
     * Additional options
     */
    private Map<String, Object> additionalOptions;
    
    /**
     * Recipient
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Recipient {
        
        /**
         * Recipient address (email, phone, user ID, device token)
         */
        @NotBlank(message = "Recipient is required")
        private String recipient;
        
        /**
         * Recipient name
         */
        private String recipientName;
        
        /**
         * User ID (if recipient is a user)
         */
        private Long userId;
        
        /**
         * Recipient type (EMAIL, PHONE, USER, DEVICE)
         */
        private String recipientType;
        
        /**
         * Custom variables (recipient-specific variables)
         */
        private Map<String, Object> variables;
    }
    
    /**
     * Attachment
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Attachment {
        
        /**
         * File name
         */
        @NotBlank(message = "File name is required")
        private String fileName;
        
        /**
         * File path (server path)
         */
        private String filePath;
        
        /**
         * File content (Base64 encoded)
         */
        private String fileContent;
        
        /**
         * Content type (MIME type)
         */
        private String contentType;
        
        /**
         * File size in bytes
         */
        private Long fileSizeBytes;
    }
    
    /**
     * Notification Types
     */
    public static class NotificationTypes {
        public static final String EMAIL = "EMAIL";
        public static final String SMS = "SMS";
        public static final String PUSH = "PUSH";
        public static final String IN_APP = "IN_APP";
    }
    
    /**
     * Categories
     */
    public static class Categories {
        public static final String TRANSACTION = "TRANSACTION";
        public static final String ALERT = "ALERT";
        public static final String REMINDER = "REMINDER";
        public static final String MARKETING = "MARKETING";
        public static final String SYSTEM = "SYSTEM";
    }
    
    /**
     * Priorities
     */
    public static class Priorities {
        public static final String HIGH = "HIGH";
        public static final String MEDIUM = "MEDIUM";
        public static final String LOW = "LOW";
    }
    
    /**
     * Common Event Triggers
     */
    public static class EventTriggers {
        // Order events
        public static final String ORDER_CREATED = "ORDER_CREATED";
        public static final String ORDER_CONFIRMED = "ORDER_CONFIRMED";
        public static final String ORDER_SHIPPED = "ORDER_SHIPPED";
        public static final String ORDER_DELIVERED = "ORDER_DELIVERED";
        public static final String ORDER_CANCELLED = "ORDER_CANCELLED";
        
        // Payment events
        public static final String PAYMENT_RECEIVED = "PAYMENT_RECEIVED";
        public static final String PAYMENT_OVERDUE = "PAYMENT_OVERDUE";
        public static final String PAYMENT_REMINDER = "PAYMENT_REMINDER";
        
        // Invoice events
        public static final String INVOICE_CREATED = "INVOICE_CREATED";
        public static final String INVOICE_SENT = "INVOICE_SENT";
        public static final String INVOICE_PAID = "INVOICE_PAID";
        public static final String INVOICE_OVERDUE = "INVOICE_OVERDUE";
        
        // Inventory events
        public static final String STOCK_LOW = "STOCK_LOW";
        public static final String STOCK_OUT = "STOCK_OUT";
        public static final String REORDER_POINT = "REORDER_POINT";
        
        // User events
        public static final String USER_REGISTERED = "USER_REGISTERED";
        public static final String USER_LOGIN = "USER_LOGIN";
        public static final String PASSWORD_RESET = "PASSWORD_RESET";
        public static final String ACCOUNT_ACTIVATED = "ACCOUNT_ACTIVATED";
        
        // System events
        public static final String SYSTEM_ERROR = "SYSTEM_ERROR";
        public static final String BACKUP_COMPLETED = "BACKUP_COMPLETED";
        public static final String REPORT_GENERATED = "REPORT_GENERATED";
    }
}
