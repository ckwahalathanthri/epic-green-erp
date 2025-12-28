package lk.epicgreen.erp.notifications.mapper;

import lk.epicgreen.erp.notifications.dto.request.NotificationQueueRequest;
import lk.epicgreen.erp.notifications.dto.response.NotificationQueueResponse;
import lk.epicgreen.erp.notifications.entity.NotificationQueue;
import org.springframework.stereotype.Component;

/**
 * Mapper for NotificationQueue entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class NotificationQueueMapper {

    public NotificationQueue toEntity(NotificationQueueRequest request) {
        if (request == null) {
            return null;
        }

        return NotificationQueue.builder()
            .recipientUserId(request.getRecipientUserId())
            .recipientEmail(request.getRecipientEmail())
            .recipientMobile(request.getRecipientMobile())
            .notificationType(request.getNotificationType())
            .subject(request.getSubject())
            .message(request.getMessage())
            .priority(request.getPriority() != null ? request.getPriority() : 5)
            .status(request.getStatus() != null ? request.getStatus() : "PENDING")
            .retryCount(request.getRetryCount() != null ? request.getRetryCount() : 0)
            .maxRetries(request.getMaxRetries() != null ? request.getMaxRetries() : 3)
            .scheduledAt(request.getScheduledAt())
            .sentAt(request.getSentAt())
            .errorMessage(request.getErrorMessage())
            .build();
    }

    public void updateEntityFromRequest(NotificationQueueRequest request, NotificationQueue notification) {
        if (request == null || notification == null) {
            return;
        }

        notification.setRecipientUserId(request.getRecipientUserId());
        notification.setRecipientEmail(request.getRecipientEmail());
        notification.setRecipientMobile(request.getRecipientMobile());
        notification.setNotificationType(request.getNotificationType());
        notification.setSubject(request.getSubject());
        notification.setMessage(request.getMessage());
        notification.setPriority(request.getPriority());
        notification.setStatus(request.getStatus());
        notification.setRetryCount(request.getRetryCount());
        notification.setMaxRetries(request.getMaxRetries());
        notification.setScheduledAt(request.getScheduledAt());
        notification.setSentAt(request.getSentAt());
        notification.setErrorMessage(request.getErrorMessage());
    }

    public NotificationQueueResponse toResponse(NotificationQueue notification) {
        if (notification == null) {
            return null;
        }

        return NotificationQueueResponse.builder()
            .id(notification.getId())
            .recipientUserId(notification.getRecipientUserId())
            .recipientEmail(notification.getRecipientEmail())
            .recipientMobile(notification.getRecipientMobile())
            .notificationType(notification.getNotificationType())
            .templateId(notification.getTemplate() != null ? notification.getTemplate().getId() : null)
            .templateCode(notification.getTemplate() != null ? notification.getTemplate().getTemplateCode() : null)
            .subject(notification.getSubject())
            .message(notification.getMessage())
            .priority(notification.getPriority())
            .status(notification.getStatus())
            .retryCount(notification.getRetryCount())
            .maxRetries(notification.getMaxRetries())
            .scheduledAt(notification.getScheduledAt())
            .sentAt(notification.getSentAt())
            .errorMessage(notification.getErrorMessage())
            .createdAt(notification.getCreatedAt())
            .build();
    }
}
