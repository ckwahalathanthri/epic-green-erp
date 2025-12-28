package lk.epicgreen.erp.notifications.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Notification Queue response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationQueueResponse {

    private Long id;
    private Long recipientUserId;
    private String recipientUserName;
    private String recipientEmail;
    private String recipientMobile;
    private String notificationType;
    private Long templateId;
    private String templateCode;
    private String subject;
    private String message;
    private Integer priority;
    private String status;
    private Integer retryCount;
    private Integer maxRetries;
    private LocalDateTime scheduledAt;
    private LocalDateTime sentAt;
    private String errorMessage;
    private LocalDateTime createdAt;
}
