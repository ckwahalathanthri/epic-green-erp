package lk.epicgreen.erp.notification.service;

import lk.epicgreen.erp.notification.dto.NotificationRequest;
import lk.epicgreen.erp.notification.entity.NotificationQueue;
import lk.epicgreen.erp.notification.entity.NotificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Notification Service Interface
 * Service for notification operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface NotificationService {
    
    // ===================================================================
    // NOTIFICATION TEMPLATE OPERATIONS
    // ===================================================================
    
    /**
     * Create notification template
     */
    NotificationTemplate createTemplate(NotificationTemplate template);
    
    /**
     * Update notification template
     */
    NotificationTemplate updateTemplate(Long templateId, NotificationTemplate template);
    
    /**
     * Delete notification template
     */
    void deleteTemplate(Long templateId);
    
    /**
     * Get template by ID
     */
    NotificationTemplate getTemplateById(Long templateId);
    
    /**
     * Get template by code
     */
    NotificationTemplate getTemplateByCode(String templateCode);
    
    /**
     * Get all templates
     */
    List<NotificationTemplate> getAllTemplates();
    
    /**
     * Get all templates with pagination
     */
    Page<NotificationTemplate> getAllTemplates(Pageable pageable);
    
    /**
     * Get templates by notification type
     */
    List<NotificationTemplate> getTemplatesByType(String notificationType);
    
    /**
     * Get templates by category
     */
    List<NotificationTemplate> getTemplatesByCategory(String category);
    
    /**
     * Get templates by event trigger
     */
    List<NotificationTemplate> getTemplatesByEventTrigger(String eventTrigger);
    
    /**
     * Get active templates
     */
    List<NotificationTemplate> getActiveTemplates();
    
    /**
     * Search templates
     */
    Page<NotificationTemplate> searchTemplates(String keyword, Pageable pageable);
    
    /**
     * Activate template
     */
    NotificationTemplate activateTemplate(Long templateId);
    
    /**
     * Deactivate template
     */
    NotificationTemplate deactivateTemplate(Long templateId);
    
    // ===================================================================
    // NOTIFICATION SENDING OPERATIONS
    // ===================================================================
    
    /**
     * Send notification using template
     */
    NotificationQueue sendNotification(String templateCode, Map<String, Object> variables, 
                                      String recipient);
    
    /**
     * Send notification using template to multiple recipients
     */
    List<NotificationQueue> sendNotification(String templateCode, Map<String, Object> variables, 
                                            List<String> recipients);
    
    /**
     * Send notification with request
     */
    NotificationQueue sendNotification(NotificationRequest request);
    
    /**
     * Send bulk notifications
     */
    List<NotificationQueue> sendBulkNotifications(NotificationRequest request);
    
    /**
     * Send email notification
     */
    NotificationQueue sendEmail(String to, String subject, String body);
    
    /**
     * Send email notification with HTML
     */
    NotificationQueue sendEmail(String to, String subject, String body, String htmlBody);
    
    /**
     * Send SMS notification
     */
    NotificationQueue sendSms(String to, String message);
    
    /**
     * Send push notification
     */
    NotificationQueue sendPush(String deviceToken, String title, String message);
    
    /**
     * Send in-app notification
     */
    NotificationQueue sendInAppNotification(Long userId, String title, String message);
    
    /**
     * Schedule notification
     */
    NotificationQueue scheduleNotification(NotificationRequest request, LocalDateTime scheduledTime);
    
    /**
     * Schedule bulk notifications
     */
    List<NotificationQueue> scheduleBulkNotifications(NotificationRequest request, 
                                                     LocalDateTime scheduledTime);
    
    // ===================================================================
    // NOTIFICATION QUEUE OPERATIONS
    // ===================================================================
    
    /**
     * Get notification by ID
     */
    NotificationQueue getNotificationById(Long id);
    
    /**
     * Get all notifications
     */
    List<NotificationQueue> getAllNotifications();
    
    /**
     * Get all notifications with pagination
     */
    Page<NotificationQueue> getAllNotifications(Pageable pageable);
    
    /**
     * Get notifications by status
     */
    List<NotificationQueue> getNotificationsByStatus(String status);
    
    /**
     * Get notifications by status with pagination
     */
    Page<NotificationQueue> getNotificationsByStatus(String status, Pageable pageable);
    
    /**
     * Get pending notifications
     */
    List<NotificationQueue> getPendingNotifications();
    
    /**
     * Get notifications ready to send
     */
    List<NotificationQueue> getNotificationsReadyToSend();
    
    /**
     * Get notifications ready to send with limit
     */
    List<NotificationQueue> getNotificationsReadyToSend(int limit);
    
    /**
     * Get failed notifications
     */
    List<NotificationQueue> getFailedNotifications();
    
    /**
     * Get failed notifications for retry
     */
    List<NotificationQueue> getFailedNotificationsForRetry();
    
    /**
     * Get notifications by recipient
     */
    List<NotificationQueue> getNotificationsByRecipient(String recipient);
    
    /**
     * Get notifications by user ID
     */
    List<NotificationQueue> getNotificationsByUserId(Long userId);
    
    /**
     * Get notifications by batch ID
     */
    List<NotificationQueue> getNotificationsByBatchId(String batchId);
    
    /**
     * Search notifications
     */
    Page<NotificationQueue> searchNotifications(String keyword, Pageable pageable);
    
    /**
     * Cancel notification
     */
    boolean cancelNotification(Long id);
    
    /**
     * Cancel batch notifications
     */
    int cancelBatchNotifications(String batchId);
    
    // ===================================================================
    // NOTIFICATION PROCESSING
    // ===================================================================
    
    /**
     * Process notification queue
     */
    void processNotificationQueue();
    
    /**
     * Process pending notifications
     */
    int processPendingNotifications();
    
    /**
     * Process pending notifications with limit
     */
    int processPendingNotifications(int limit);
    
    /**
     * Retry failed notifications
     */
    int retryFailedNotifications();
    
    /**
     * Retry failed notifications with limit
     */
    int retryFailedNotifications(int limit);
    
    /**
     * Process single notification
     */
    boolean processNotification(Long notificationId);
    
    /**
     * Retry single notification
     */
    boolean retryNotification(Long notificationId);
    
    /**
     * Handle notification success
     */
    void handleNotificationSuccess(Long notificationId, String providerMessageId, 
                                   String providerResponse);
    
    /**
     * Handle notification failure
     */
    void handleNotificationFailure(Long notificationId, String errorMessage, 
                                   String errorDetails);
    
    /**
     * Check for stuck notifications
     */
    List<NotificationQueue> checkForStuckNotifications();
    
    /**
     * Reset stuck notifications
     */
    int resetStuckNotifications();
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    /**
     * Get batch progress
     */
    Map<String, Object> getBatchProgress(String batchId);
    
    /**
     * Get batch statistics
     */
    Map<String, Long> getBatchStatistics(String batchId);
    
    /**
     * Is batch complete
     */
    boolean isBatchComplete(String batchId);
    
    // ===================================================================
    // STATISTICS AND REPORTS
    // ===================================================================
    
    /**
     * Get notification statistics
     */
    Map<String, Object> getNotificationStatistics();
    
    /**
     * Get notification statistics by date range
     */
    Map<String, Object> getNotificationStatistics(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get notification status distribution
     */
    List<Map<String, Object>> getNotificationStatusDistribution();
    
    /**
     * Get notification type distribution
     */
    List<Map<String, Object>> getNotificationTypeDistribution();
    
    /**
     * Get notification priority distribution
     */
    List<Map<String, Object>> getNotificationPriorityDistribution();
    
    /**
     * Get template usage statistics
     */
    List<Map<String, Object>> getTemplateUsageStatistics();
    
    /**
     * Get success rate
     */
    Double getSuccessRate();
    
    /**
     * Get success rate by type
     */
    List<Map<String, Object>> getSuccessRateByType();
    
    /**
     * Get provider performance
     */
    List<Map<String, Object>> getProviderPerformance();
    
    /**
     * Get hourly notification count
     */
    List<Map<String, Object>> getHourlyNotificationCount(LocalDateTime startDate, 
                                                         LocalDateTime endDate);
    
    /**
     * Get daily notification count
     */
    List<Map<String, Object>> getDailyNotificationCount(LocalDateTime startDate, 
                                                        LocalDateTime endDate);
    
    /**
     * Get dashboard statistics
     */
    Map<String, Object> getDashboardStatistics();
    
    // ===================================================================
    // TEMPLATE RENDERING
    // ===================================================================
    
    /**
     * Render template with variables
     */
    String renderTemplate(String templateContent, Map<String, Object> variables);
    
    /**
     * Render template subject
     */
    String renderSubject(NotificationTemplate template, Map<String, Object> variables);
    
    /**
     * Render template body
     */
    String renderBody(NotificationTemplate template, Map<String, Object> variables);
    
    /**
     * Render HTML body
     */
    String renderHtmlBody(NotificationTemplate template, Map<String, Object> variables);
    
    // ===================================================================
    // CLEANUP OPERATIONS
    // ===================================================================
    
    /**
     * Delete old sent notifications
     */
    int deleteOldSentNotifications(int daysToKeep);
    
    /**
     * Delete old cancelled notifications
     */
    int deleteOldCancelledNotifications(int daysToKeep);
    
    /**
     * Cleanup old notifications
     */
    Map<String, Integer> cleanupOldNotifications(int daysToKeep);
}
