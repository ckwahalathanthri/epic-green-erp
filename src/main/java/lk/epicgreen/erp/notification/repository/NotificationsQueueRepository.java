package lk.epicgreen.erp.notification.repository;

import lk.epicgreen.erp.notification.entity.NotificationQueue;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for NotificationsQueue entity
 * Based on ACTUAL database schema: notifications_queue table
 *
 * Fields: recipient_user_id (BIGINT), recipient_email, recipient_mobile,
 *         notification_type (ENUM: EMAIL, SMS, PUSH, IN_APP),
 *         template_id (BIGINT), subject, message,
 *         priority, status (ENUM: PENDING, SENT, FAILED, CANCELLED),
 *         retry_count, max_retries, scheduled_at, sent_at, error_message, created_at
 *
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface NotificationsQueueRepository extends JpaRepository<NotificationQueue, Long>, JpaSpecificationExecutor<NotificationQueue> {

    // ==================== FINDER METHODS ====================

    /**
     * Find notifications by recipient user
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.recipientUser.id = :recipientUserId")
    List<NotificationQueue> findByRecipientUserId(@Param("recipientUserId") Long recipientUserId);

    /**
     * Find notifications by recipient user with pagination
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.recipientUser.id = :recipientUserId")
    Page<NotificationQueue> findByRecipientUserId(@Param("recipientUserId") Long recipientUserId, Pageable pageable);

    /**
     * Find notifications by recipient email
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.recipientEmail = :recipientEmail")
    List<NotificationQueue> findByRecipientEmail(@Param("recipientEmail") String recipientEmail);

    /**
     * Find notifications by notification type
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.notificationType = :notificationType")
    List<NotificationQueue> findByNotificationType(@Param("notificationType") String notificationType);

    /**
     * Find notifications by notification type with pagination
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.notificationType = :notificationType")
    Page<NotificationQueue> findByNotificationType(@Param("notificationType") String notificationType, Pageable pageable);

    /**
     * Find notifications by status
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.status = :status")
    List<NotificationQueue> findByStatus(@Param("status") String status);

    /**
     * Find notifications by status with pagination
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.status = :status")
    Page<NotificationQueue> findByStatus(@Param("status") String status, Pageable pageable);

    /**
     * Find notifications by template
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.template.id = :templateId")
    List<NotificationQueue> findByTemplateId(@Param("templateId") Long templateId);

    /**
     * Find notifications by scheduled at time range
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.scheduledAt BETWEEN :startTime AND :endTime")
    List<NotificationQueue> findByScheduledAtBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * Find notifications by created at time range
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.createdAt BETWEEN :startTime AND :endTime")
    List<NotificationQueue> findByCreatedAtBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * Find notifications by created at time range with pagination
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.createdAt BETWEEN :startTime AND :endTime")
    Page<NotificationQueue> findByCreatedAtBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, Pageable pageable);

    // ==================== SEARCH METHODS ====================

    /**
     * Search notifications by subject containing (case-insensitive)
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE LOWER(nq.subject) LIKE LOWER(CONCAT('%', :subject, '%'))")
    Page<NotificationQueue> findBySubjectContainingIgnoreCase(@Param("subject") String subject, Pageable pageable);

    /**
     * Search notifications by message containing (case-insensitive)
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE LOWER(nq.message) LIKE LOWER(CONCAT('%', :message, '%'))")
    Page<NotificationQueue> findByMessageContainingIgnoreCase(@Param("message") String message, Pageable pageable);

    /**
     * Search notifications by multiple criteria
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE " +
            "(:recipientUserId IS NULL OR nq.recipientUser.id = :recipientUserId) AND " +
            "(:recipientEmail IS NULL OR LOWER(nq.recipientEmail) LIKE LOWER(CONCAT('%', :recipientEmail, '%'))) AND " +
            "(:notificationType IS NULL OR nq.notificationType = :notificationType) AND " +
            "(:status IS NULL OR nq.status = :status) AND " +
            "(:startTime IS NULL OR nq.createdAt >= :startTime) AND " +
            "(:endTime IS NULL OR nq.createdAt <= :endTime)")
    Page<NotificationQueue> searchNotifications(
            @Param("recipientUserId") Long recipientUserId,
            @Param("recipientEmail") String recipientEmail,
            @Param("notificationType") String notificationType,
            @Param("status") String status,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);

    // ==================== COUNT METHODS ====================

    /**
     * Count notifications by status
     */
    @Query("SELECT COUNT(nq) FROM NotificationQueue nq WHERE nq.status = :status")
    long countByStatus(@Param("status") String status);

    /**
     * Count notifications by recipient user
     */
    @Query("SELECT COUNT(nq) FROM NotificationQueue nq WHERE nq.recipientUser.id = :recipientUserId")
    long countByRecipientUserId(@Param("recipientUserId") Long recipientUserId);

    /**
     * Count notifications by notification type
     */
    @Query("SELECT COUNT(nq) FROM NotificationQueue nq WHERE nq.notificationType = :notificationType")
    long countByNotificationType(@Param("notificationType") String notificationType);

    /**
     * Count notifications in time range
     */
    @Query("SELECT COUNT(nq) FROM NotificationQueue nq WHERE nq.createdAt BETWEEN :startTime AND :endTime")
    long countByCreatedAtBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    // ==================== DELETE METHODS ====================

    /**
     * Delete all sent notifications
     */
    @Modifying
    @Query("DELETE FROM NotificationQueue nq WHERE nq.status = 'SENT'")
    void deleteAllSent();

    /**
     * Delete sent notifications older than specified date
     */
    @Modifying
    @Query("DELETE FROM NotificationQueue nq WHERE nq.status = 'SENT' AND nq.sentAt < :cutoffDate")
    void deleteSentBefore(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Delete cancelled notifications older than specified date
     */
    @Modifying
    @Query("DELETE FROM NotificationQueue nq WHERE nq.status = 'CANCELLED' AND nq.createdAt < :cutoffDate")
    void deleteCancelledBefore(@Param("cutoffDate") LocalDateTime cutoffDate);

    // ==================== CUSTOM QUERIES ====================

    /**
     * Find pending notifications
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.status = 'PENDING' " +
            "ORDER BY nq.priority DESC, nq.createdAt ASC")
    List<NotificationQueue> findPendingNotifications();

    /**
     * Find sent notifications
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.status = 'SENT' " +
            "ORDER BY nq.sentAt DESC")
    List<NotificationQueue> findSentNotifications();

    /**
     * Find failed notifications
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.status = 'FAILED' " +
            "ORDER BY nq.createdAt DESC")
    List<NotificationQueue> findFailedNotifications();

    /**
     * Find cancelled notifications
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.status = 'CANCELLED' " +
            "ORDER BY nq.createdAt DESC")
    List<NotificationQueue> findCancelledNotifications();

    /**
     * Find pending notifications ordered by priority
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.status = 'PENDING' " +
            "AND (nq.scheduledAt IS NULL OR nq.scheduledAt <= CURRENT_TIMESTAMP) " +
            "ORDER BY nq.priority DESC, nq.createdAt ASC")
    List<NotificationQueue> findPendingNotificationsOrderedByPriority();

    /**
     * Find scheduled notifications
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.status = 'PENDING' " +
            "AND nq.scheduledAt > CURRENT_TIMESTAMP ORDER BY nq.scheduledAt")
    List<NotificationQueue> findScheduledNotifications();

    /**
     * Find notifications due for sending
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.status = 'PENDING' " +
            "AND (nq.scheduledAt IS NULL OR nq.scheduledAt <= CURRENT_TIMESTAMP) " +
            "ORDER BY nq.priority DESC, nq.createdAt ASC")
    List<NotificationQueue> findNotificationsDueForSending();

    /**
     * Find notifications ready for retry
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.status = 'FAILED' " +
            "AND nq.retryCount < nq.maxRetries ORDER BY nq.priority DESC, nq.createdAt ASC")
    List<NotificationQueue> findNotificationsReadyForRetry();

    /**
     * Find high priority pending notifications
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.status = 'PENDING' " +
            "AND nq.priority >= :priority ORDER BY nq.priority DESC, nq.createdAt ASC")
    List<NotificationQueue> findHighPriorityPendingNotifications(@Param("priority") Integer priority);

    /**
     * Find EMAIL notifications
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.notificationType = 'EMAIL' " +
            "ORDER BY nq.createdAt DESC")
    List<NotificationQueue> findEmailNotifications();

    /**
     * Find SMS notifications
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.notificationType = 'SMS' " +
            "ORDER BY nq.createdAt DESC")
    List<NotificationQueue> findSmsNotifications();

    /**
     * Find PUSH notifications
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.notificationType = 'PUSH' " +
            "ORDER BY nq.createdAt DESC")
    List<NotificationQueue> findPushNotifications();

    /**
     * Find IN_APP notifications
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.notificationType = 'IN_APP' " +
            "ORDER BY nq.createdAt DESC")
    List<NotificationQueue> findInAppNotifications();

    /**
     * Find notifications by user and status
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.recipientUser.id = :recipientUserId AND nq.status = :status")
    List<NotificationQueue> findByRecipientUserIdAndStatus(@Param("recipientUserId") Long recipientUserId, @Param("status") String status);

    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.status = :status AND nq.scheduledAt <= :dateTime")
    List<NotificationQueue> findByStatusAndScheduledAtLessThanEqual(@Param("status") String status, @Param("dateTime") LocalDateTime dateTime);

    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.id = :batchId")
    List<NotificationQueue> findByBatchId(@Param("batchId") String batchId);

    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.status = :status AND nq.scheduledAt IS NOT NULL")
    List<NotificationQueue> findByStatusAndScheduledAtNotNull(@Param("status") String status);

    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.status = :status AND nq.createdAt <= :dateTime")
    List<NotificationQueue> findByStatusAndCreatedAtLessThanEqual(@Param("status") String status, @Param("dateTime") LocalDateTime dateTime);

    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.status = :status AND nq.sentAt <= :dateTime")
    List<NotificationQueue> findByStatusAndSentAtLessThanEqual(@Param("status") String status, @Param("dateTime") LocalDateTime dateTime);

    /**
     * Find notifications by type and status
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.notificationType = :notificationType AND nq.status = :status")
    List<NotificationQueue> findByNotificationTypeAndStatus(@Param("notificationType") String notificationType, @Param("status") String status);

    /**
     * Find notifications sent in time range
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.sentAt BETWEEN :startTime AND :endTime " +
            "ORDER BY nq.sentAt DESC")
    List<NotificationQueue> findNotificationsSentBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * Get notifications queue statistics
     */
    @Query("SELECT " +
            "COUNT(nq) as totalNotifications, " +
            "SUM(CASE WHEN nq.status = 'PENDING' THEN 1 ELSE 0 END) as pendingNotifications, " +
            "SUM(CASE WHEN nq.status = 'SENT' THEN 1 ELSE 0 END) as sentNotifications, " +
            "SUM(CASE WHEN nq.status = 'FAILED' THEN 1 ELSE 0 END) as failedNotifications, " +
            "SUM(CASE WHEN nq.status = 'CANCELLED' THEN 1 ELSE 0 END) as cancelledNotifications " +
            "FROM NotificationQueue nq")
    Object getNotificationQueueStatistics();

    /**
     * Get notifications grouped by status
     */
    @Query("SELECT nq.status, COUNT(nq) as notificationCount " +
            "FROM NotificationQueue nq GROUP BY nq.status ORDER BY notificationCount DESC")
    List<Object[]> getNotificationsByStatus();

    /**
     * Get notifications grouped by notification type
     */
    @Query("SELECT nq.notificationType, COUNT(nq) as notificationCount, " +
            "SUM(CASE WHEN nq.status = 'SENT' THEN 1 ELSE 0 END) as sentCount " +
            "FROM NotificationQueue nq GROUP BY nq.notificationType ORDER BY notificationCount DESC")
    List<Object[]> getNotificationsByType();

    /**
     * Get daily notification summary
     */
//    @Query("SELECT DATE(nq.createdAt) as notificationDate, COUNT(nq) as notificationCount, " +
//           "SUM(CASE WHEN nq.status = 'SENT' THEN 1 ELSE 0 END) as sentCount, " +
//           "SUM(CASE WHEN nq.status = 'FAILED' THEN 1 ELSE 0 END) as failedCount " +
//           "FROM NotificationQueue nq WHERE nq.createdAt BETWEEN :startTime AND :endTime " +
//           "GROUP BY DATE(nq.createdAt) ORDER BY notificationDate DESC")
//    List<Object[]> getDailyNotificationSummary(
//            @Param("startTime") LocalDateTime startTime,
//            @Param("endTime") LocalDateTime endTime);

    /**
     * Find today's notifications
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE DATE(nq.createdAt) = CURRENT_DATE " +
            "ORDER BY nq.createdAt DESC")
    List<NotificationQueue> findTodayNotifications();

    /**
     * Find all notifications ordered by created at
     */
    @Query("SELECT nq FROM NotificationQueue nq ORDER BY nq.createdAt DESC")
    List<NotificationQueue> findAllByOrderByCreatedAtDesc();

    /**
     * Get oldest pending notification
     */
    @Query("SELECT nq FROM NotificationQueue nq WHERE nq.status = 'PENDING' " +
            "ORDER BY nq.createdAt ASC ")
    NotificationQueue getOldestPendingNotification();

    /**
     * Get notification delivery rate
     */
    @Query("SELECT " +
            "COUNT(nq) as totalNotifications, " +
            "SUM(CASE WHEN nq.status = 'SENT' THEN 1 ELSE 0 END) as sentNotifications, " +
            "(SUM(CASE WHEN nq.status = 'SENT' THEN 1 ELSE 0 END) * 100.0 / COUNT(nq)) as deliveryRate " +
            "FROM NotificationQueue nq WHERE nq.createdAt >= :sinceTime")
    Object getNotificationDeliveryRate(@Param("sinceTime") LocalDateTime sinceTime);
}