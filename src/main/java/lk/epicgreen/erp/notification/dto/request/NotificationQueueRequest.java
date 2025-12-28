package lk.epicgreen.erp.notifications.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * DTO for creating/updating Notification Queue
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationQueueRequest {

    private Long recipientUserId;

    @Email(message = "Valid email address is required")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String recipientEmail;

    @Size(max = 20, message = "Mobile number must not exceed 20 characters")
    private String recipientMobile;

    @NotBlank(message = "Notification type is required")
    @Pattern(regexp = "^(EMAIL|SMS|PUSH|IN_APP)$", 
             message = "Notification type must be one of: EMAIL, SMS, PUSH, IN_APP")
    private String notificationType;

    private Long templateId;

    @Size(max = 200, message = "Subject must not exceed 200 characters")
    private String subject;

    @NotBlank(message = "Message is required")
    @Size(max = 10000, message = "Message must not exceed 10000 characters")
    private String message;

    @Min(value = 1, message = "Priority must be >= 1")
    @Max(value = 10, message = "Priority must be <= 10")
    private Integer priority;

    @Pattern(regexp = "^(PENDING|SENT|FAILED|CANCELLED)$", 
             message = "Status must be one of: PENDING, SENT, FAILED, CANCELLED")
    private String status;

    @Min(value = 0, message = "Retry count must be >= 0")
    private Integer retryCount;

    @Min(value = 1, message = "Max retries must be >= 1")
    @Max(value = 10, message = "Max retries must be <= 10")
    private Integer maxRetries;

    private LocalDateTime scheduledAt;

    private LocalDateTime sentAt;

    @Size(max = 1000, message = "Error message must not exceed 1000 characters")
    private String errorMessage;
}
