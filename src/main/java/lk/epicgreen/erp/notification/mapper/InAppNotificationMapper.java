package lk.epicgreen.erp.notification.mapper;

import lk.epicgreen.erp.admin.repository.UserRepository;
import lk.epicgreen.erp.notification.dto.request.InAppNotificationRequest;
import lk.epicgreen.erp.notification.dto.response.InAppNotificationResponse;
import lk.epicgreen.erp.notification.entity.InAppNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper for InAppNotification entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class InAppNotificationMapper {

    @Autowired
    UserRepository userRepository;

    public InAppNotification toEntity(InAppNotificationRequest request) {
        if (request == null) {
            return null;
        }

        return InAppNotification.builder()
            .user(userRepository.findById(request.getUserId()).orElse(null))
            .notificationTitle(request.getNotificationTitle())
            .notificationMessage(request.getNotificationMessage())
            .notificationType(request.getNotificationType() != null ? request.getNotificationType() : "INFO")
            .actionUrl(request.getActionUrl())
            .isRead(request.getIsRead() != null ? request.getIsRead() : false)
            .readAt(request.getReadAt())
            .expiresAt(request.getExpiresAt())
            .build();
    }

    public void updateEntityFromRequest(InAppNotificationRequest request, InAppNotification notification) {
        if (request == null || notification == null) {
            return;
        }

        notification.setUser(userRepository.findById(request.getUserId()).orElse(null));
        notification.setNotificationTitle(request.getNotificationTitle());
        notification.setNotificationMessage(request.getNotificationMessage());
        notification.setNotificationType(request.getNotificationType());
        notification.setActionUrl(request.getActionUrl());
        notification.setIsRead(request.getIsRead());
        notification.setReadAt(request.getReadAt());
        notification.setExpiresAt(request.getExpiresAt());
    }

    public InAppNotificationResponse toResponse(InAppNotification notification) {
        if (notification == null) {
            return null;
        }

        return InAppNotificationResponse.builder()
            .id(notification.getId())
            .userId(notification.getUser().getId())
            .notificationTitle(notification.getNotificationTitle())
            .notificationMessage(notification.getNotificationMessage())
            .notificationType(notification.getNotificationType())
            .actionUrl(notification.getActionUrl())
            .isRead(notification.getIsRead())
            .readAt(notification.getReadAt())
            .expiresAt(notification.getExpiresAt())
            .createdAt(notification.getCreatedAt())
            .build();
    }
}
