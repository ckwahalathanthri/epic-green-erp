package lk.epicgreen.erp.notification.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Map;

/**
 * DTO for creating/updating Notification Template
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplateRequest {

    @NotBlank(message = "Template code is required")
    @Size(max = 50, message = "Template code must not exceed 50 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Template code must contain only uppercase letters, numbers, hyphens and underscores")
    private String templateCode;

    @NotBlank(message = "Template name is required")
    @Size(max = 100, message = "Template name must not exceed 100 characters")
    private String templateName;

    @NotBlank(message = "Notification type is required")
    @Pattern(regexp = "^(EMAIL|SMS|PUSH|IN_APP)$", 
             message = "Notification type must be one of: EMAIL, SMS, PUSH, IN_APP")
    private String notificationType;

    @Size(max = 200, message = "Subject must not exceed 200 characters")
    private String subject;

    @NotBlank(message = "Body template is required")
    @Size(max = 10000, message = "Body template must not exceed 10000 characters")
    private String bodyTemplate;

    private Map<String, Object> variables;

    private Boolean isActive;
}
