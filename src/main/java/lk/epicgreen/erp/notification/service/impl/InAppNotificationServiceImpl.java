package lk.epicgreen.erp.notification.service.impl;

import lk.epicgreen.erp.notification.dto.request.InAppNotificationRequest;
import lk.epicgreen.erp.notification.dto.response.InAppNotificationResponse;
import lk.epicgreen.erp.notification.entity.InAppNotification;
import lk.epicgreen.erp.notification.mapper.InAppNotificationMapper;
import lk.epicgreen.erp.notification.repository.InAppNotificationRepository;

import lk.epicgreen.erp.notification.service.InAppNotificationService;

import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of InAppNotificationService interface
 *
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class InAppNotificationServiceImpl implements InAppNotificationService {

    private final InAppNotificationRepository notificationRepository;
    private final InAppNotificationMapper notificationMapper;


    @Transactional
    public InAppNotificationResponse createNotification(InAppNotificationRequest request) {
        log.info("Creating in-app notification for user: {}", request.getUserId());

        InAppNotification notification = notificationMapper.toEntity(request);
        InAppNotification savedNotification = notificationRepository.save(notification);

        log.info("In-app notification created successfully with ID: {}", savedNotification.getId());

        return notificationMapper.toResponse(savedNotification);
    }


    @Transactional
    public InAppNotificationResponse updateNotification(Long id, InAppNotificationRequest request) {
        log.info("Updating in-app notification: {}", id);

        InAppNotification notification = findNotificationById(id);
        notificationMapper.updateEntityFromRequest(request, notification);

        InAppNotification updatedNotification = notificationRepository.save(notification);
        log.info("In-app notification updated successfully");

        return notificationMapper.toResponse(updatedNotification);
    }

    @Override
    @Transactional
    public void markAsRead(Long id) {
        log.info("Marking notification as read: {}", id);

        InAppNotification notification = findNotificationById(id);

        if (notification.getIsRead()) {
            log.debug("Notification {} is already read", id);
            return;
        }

        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());
        notificationRepository.save(notification);

        log.info("Notification marked as read");
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        log.info("Marking all notifications as read for user: {}", userId);

        List<InAppNotification> unreadNotifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        LocalDateTime now = LocalDateTime.now();

        for (InAppNotification notification : unreadNotifications) {
            notification.setIsRead(true);
            notification.setReadAt(now);
        }

        notificationRepository.saveAll(unreadNotifications);
        log.info("Marked {} notifications as read", unreadNotifications.size());
    }

    @Override
    @Transactional
    public void deleteNotification(Long id) {
        log.info("Deleting in-app notification: {}", id);

        notificationRepository.deleteById(id);
        log.info("In-app notification deleted successfully");
    }

    @Override
    @Transactional
    public void deleteAllForUser(Long userId) {
        log.info("Deleting all notifications for user: {}", userId);

        long deletedCount = notificationRepository.deleteByUserId(userId);
        log.info("Deleted {} notifications for user {}", deletedCount, userId);
    }

    @Override
    public InAppNotificationResponse getNotificationById(Long id) {
        InAppNotification notification = findNotificationById(id);
        return notificationMapper.toResponse(notification);
    }

    @Override
    public PageResponse<InAppNotificationResponse> getAllNotifications(Pageable pageable) {
        Page<InAppNotification> notificationPage = notificationRepository.findAll(pageable);
        return createPageResponse(notificationPage);
    }

    @Override
    public List<InAppNotificationResponse> getNotificationsByUser(Long userId) {
        List<InAppNotification> notifications = notificationRepository.findByUserId(userId);
        return notifications.stream()
            .map(notificationMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<InAppNotificationResponse> getUnreadNotificationsByUser(Long userId) {
        List<InAppNotification> notifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        return notifications.stream()
            .map(notificationMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<InAppNotificationResponse> getReadNotificationsByUser(Long userId, Pageable pageable) {
        Page<InAppNotification> notificationPage = notificationRepository.findByUserIdAndIsReadTrue(userId, pageable);
        return createPageResponse(notificationPage);
    }

    @Override
    public List<InAppNotificationResponse> getNotificationsByType(String notificationType) {
        List<InAppNotification> notifications = notificationRepository.findByNotificationType(notificationType);
        return notifications.stream()
            .map(notificationMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<InAppNotificationResponse> getActiveNotifications(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        List<InAppNotification> notifications = notificationRepository.findActiveNotifications(userId, now);
        return notifications.stream()
            .map(notificationMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public Long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Override
    @Transactional
    public void cleanupExpiredNotifications() {
        log.info("Cleaning up expired notifications");

        LocalDateTime now = LocalDateTime.now();
        int deletedCount = notificationRepository.deleteByExpiresAtBefore(now);

        log.info("Cleaned up {} expired notifications", deletedCount);
    }

    @Override
    public PageResponse<InAppNotificationResponse> searchNotifications(String keyword, Pageable pageable) {
        Page<InAppNotification> notificationPage = notificationRepository.searchNotifications(keyword, pageable);
        return createPageResponse(notificationPage);
    }

    @Override
    public Map<String, Object> getNotificationStatistics() {
        return null;
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private InAppNotification findNotificationById(Long id) {
        return notificationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("In-App Notification not found: " + id));
    }

    private PageResponse<InAppNotificationResponse> createPageResponse(Page<InAppNotification> notificationPage) {
        List<InAppNotificationResponse> content = notificationPage.getContent().stream()
            .map(notificationMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<InAppNotificationResponse>builder()
            .content(content)
            .pageNumber(notificationPage.getNumber())
            .pageSize(notificationPage.getSize())
            .totalElements(notificationPage.getTotalElements())
            .totalPages(notificationPage.getTotalPages())
            .last(notificationPage.isLast())
            .first(notificationPage.isFirst())
            .empty(notificationPage.isEmpty())
            .build();
    }
}
