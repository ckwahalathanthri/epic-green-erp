package lk.epicgreen.erp.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for Notification Template response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplateResponse {

    private Long id;
    private String templateCode;
    private String templateName;
    private String notificationType;
    private String subject;
    private String bodyTemplate;
    private Map<String, Object> variables;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private Long createdBy;
    private String createdByName;
    private LocalDateTime updatedAt;
    private Long updatedBy;

    public String getSubjectTemplate() {
        return subject;
    }
}
