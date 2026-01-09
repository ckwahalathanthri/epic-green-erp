package lk.epicgreen.erp.notification.service;

import lk.epicgreen.erp.notification.dto.request.InAppNotificationRequest;
import lk.epicgreen.erp.notification.dto.response.InAppNotificationResponse;

import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Service interface for In-App Notification entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface InAppNotificationService {

    /**
     * Create in-app notification
     */
    InAppNotificationResponse createNotification(InAppNotificationRequest request);

    /**
     * Update in-app notification
     */
    InAppNotificationResponse updateNotification(Long id, InAppNotificationRequest request);

    /**
     * Mark notification as read
     */
    void markAsRead(Long id);

    /**
     * Mark all notifications as read for user
     */
    void markAllAsRead(Long userId);

    /**
     * Delete notification
     */
    void deleteNotification(Long id);

    /**
     * Delete all notifications for user
     */
    void deleteAllForUser(Long userId);

    /**
     * Get Notification by ID
     */
    InAppNotificationResponse getNotificationById(Long id);

    /**
     * Get all Notifications (paginated)
     */
    PageResponse<InAppNotificationResponse> getAllNotifications(Pageable pageable);

    /**
     * Get Notifications by user
     */
    List<InAppNotificationResponse> getNotificationsByUser(Long userId);

    /**
     * Get unread notifications by user
     */
    List<InAppNotificationResponse> getUnreadNotificationsByUser(Long userId);

    /**
     * Get read notifications by user
     */
    PageResponse<InAppNotificationResponse> getReadNotificationsByUser(Long userId, Pageable pageable);

    /**
     * Get Notifications by type
     */
    List<InAppNotificationResponse> getNotificationsByType(String notificationType);

    /**
     * Get active notifications (not expired)
     */
    List<InAppNotificationResponse> getActiveNotifications(Long userId);

    /**
     * Get unread count for user
     */
    Long getUnreadCount(Long userId);

    /**
     * Clean up expired notifications
     */
    void cleanupExpiredNotifications();

    /**
     * Search notifications
     */
    PageResponse<InAppNotificationResponse> searchNotifications(String keyword, Pageable pageable);

    Map<String, Object> getNotificationStatistics();
}
