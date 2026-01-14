package lk.epicgreen.erp.notification.mapper;

import lk.epicgreen.erp.notification.dto.request.NotificationTemplateRequest;
import lk.epicgreen.erp.notification.dto.response.NotificationTemplateResponse;
import lk.epicgreen.erp.notification.entity.NotificationTemplate;

import org.springframework.stereotype.Component;

/**
 * Mapper for NotificationTemplate entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class NotificationTemplateMapper {

    public NotificationTemplate toEntity(NotificationTemplateRequest request) {
        if (request == null) {
            return null;
        }

        return NotificationTemplate.builder()
            .templateCode(request.getTemplateCode())
            .templateName(request.getTemplateName())
            .notificationType(request.getNotificationType())
            .subject(request.getSubject())
            .bodyTemplate(request.getBodyTemplate())
            .variables(request.getVariables().toString())
            .isActive(request.getIsActive() != null ? request.getIsActive() : true)
            .build();
    }

    public void updateEntityFromRequest(NotificationTemplateRequest request, NotificationTemplate template) {
        if (request == null || template == null) {
            return;
        }

        template.setTemplateCode(request.getTemplateCode());
        template.setTemplateName(request.getTemplateName());
        template.setNotificationType(request.getNotificationType());
        template.setSubject(request.getSubject());
        template.setBodyTemplate(request.getBodyTemplate());
        template.setVariables(request.getVariables().toString());
        template.setIsActive(request.getIsActive());
    }

    public NotificationTemplateResponse toResponse(NotificationTemplate template) {
        if (template == null) {
            return null;
        }

        return NotificationTemplateResponse.builder()
            .id(template.getId())
            .templateCode(template.getTemplateCode())
            .templateName(template.getTemplateName())
            .notificationType(template.getNotificationType())
            .subject(template.getSubject())
            .bodyTemplate(template.getBodyTemplate())
//            .variables(template.getVariables())
            .isActive(template.getIsActive())
            .createdAt(template.getCreatedAt())
            .createdBy(template.getCreatedBy())
            .updatedAt(template.getUpdatedAt())
            .updatedBy(template.getUpdatedBy())
            .build();
    }
}
