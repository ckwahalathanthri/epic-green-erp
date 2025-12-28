package lk.epicgreen.erp.notifications.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * DTO for creating In-App Notification
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InAppNotificationRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Notification title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String notificationTitle;

    @NotBlank(message = "Notification message is required")
    @Size(max = 5000, message = "Message must not exceed 5000 characters")
    private String notificationMessage;

    @Pattern(regexp = "^(INFO|WARNING|ERROR|SUCCESS)$", 
             message = "Notification type must be one of: INFO, WARNING, ERROR, SUCCESS")
    private String notificationType;

    @Size(max = 500, message = "Action URL must not exceed 500 characters")
    private String actionUrl;

    private Boolean isRead;

    private LocalDateTime readAt;

    private LocalDateTime expiresAt;
}
