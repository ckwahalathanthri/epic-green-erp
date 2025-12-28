package lk.epicgreen.erp.notifications.service;

import lk.epicgreen.erp.notifications.dto.request.NotificationQueueRequest;
import lk.epicgreen.erp.notifications.dto.response.NotificationQueueResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Service interface for Notification Queue entity business logic
 * 
 * Notification Queue Status Workflow:
 * PENDING → SENT (sent successfully)
 * PENDING → FAILED (send failed, can retry)
 * Any status → CANCELLED (manually cancelled)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface NotificationQueueService {

    /**
     * Create notification (queue for sending)
     */
    NotificationQueueResponse createNotification(NotificationQueueRequest request);

    /**
     * Create notification from template
     */
    NotificationQueueResponse createFromTemplate(String templateCode, Long recipientUserId, 
                                                  Map<String, Object> variables);

    /**
     * Update notification (only if PENDING)
     */
    NotificationQueueResponse updateNotification(Long id, NotificationQueueRequest request);

    /**
     * Send notification immediately
     */
    void sendNotification(Long id);

    /**
     * Mark notification as sent
     */
    void markAsSent(Long id);

    /**
     * Mark notification as failed
     */
    void markAsFailed(Long id, String errorMessage);

    /**
     * Cancel notification
     */
    void cancelNotification(Long id);

    /**
     * Retry failed notification
     */
    void retryNotification(Long id);

    /**
     * Delete notification
     */
    void deleteNotification(Long id);

    /**
     * Get Notification by ID
     */
    NotificationQueueResponse getNotificationById(Long id);

    /**
     * Get all Notifications (paginated)
     */
    PageResponse<NotificationQueueResponse> getAllNotifications(Pageable pageable);

    /**
     * Get Notifications by status
     */
    PageResponse<NotificationQueueResponse> getNotificationsByStatus(String status, Pageable pageable);

    /**
     * Get Notifications by type
     */
    List<NotificationQueueResponse> getNotificationsByType(String notificationType);

    /**
     * Get Notifications by recipient
     */
    List<NotificationQueueResponse> getNotificationsByRecipient(Long recipientUserId);

    /**
     * Get pending notifications
     */
    List<NotificationQueueResponse> getPendingNotifications();

    /**
     * Get failed notifications
     */
    List<NotificationQueueResponse> getFailedNotifications();

    /**
     * Get scheduled notifications
     */
    List<NotificationQueueResponse> getScheduledNotifications();

    /**
     * Get notifications due for sending
     */
    List<NotificationQueueResponse> getDueNotifications();

    /**
     * Process notification queue (send due notifications)
     */
    void processQueue();

    /**
     * Search notifications
     */
    PageResponse<NotificationQueueResponse> searchNotifications(String keyword, Pageable pageable);

    /**
     * Check if can update
     */
    boolean canUpdate(Long id);

    /**
     * Check if can delete
     */
    boolean canDelete(Long id);
}
