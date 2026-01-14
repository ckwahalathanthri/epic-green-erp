package lk.epicgreen.erp.notification.repository;

import lk.epicgreen.erp.notification.entity.InAppNotification;
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
 * Repository interface for InAppNotification entity
 * Based on ACTUAL database schema: in_app_notifications table
 *
 * Fields: user_id (BIGINT), notification_title, notification_message,
 *         notification_type (ENUM: INFO, WARNING, ERROR, SUCCESS),
 *         action_url, is_read, read_at, expires_at, created_at
 *
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface InAppNotificationRepository extends JpaRepository<InAppNotification, Long>, JpaSpecificationExecutor<InAppNotification> {

    // ==================== FINDER METHODS ====================

    /**
     * Find in-app notifications by user
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.user.id = :userId")
    List<InAppNotification> findByUserId(@Param("userId") Long userId);

    @Query("SELECT ian FROM InAppNotification ian WHERE ian.user.id = :userId AND ian.isRead = false")
    List<InAppNotification> findByUserIdAndIsReadFalse(@Param("userId") Long userId);

    /**
     * Find in-app notifications by user with pagination
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.user.id = :userId")
    Page<InAppNotification> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Modifying
    @Query("DELETE FROM InAppNotification ian WHERE ian.user.id = :userId")
    long deleteByUserId(@Param("userId") Long userId);

    @Query("SELECT ian FROM InAppNotification ian WHERE ian.user.id = :userId AND ian.isRead = true")
    Page<InAppNotification> findByUserIdAndIsReadTrue(@Param("userId") Long userId, Pageable pageable);

    /**
     * Find in-app notifications by notification type
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.notificationType = :notificationType")
    List<InAppNotification> findByNotificationType(@Param("notificationType") String notificationType);

    /**
     * Find in-app notifications by notification type with pagination
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.notificationType = :notificationType")
    Page<InAppNotification> findByNotificationType(@Param("notificationType") String notificationType, Pageable pageable);

    /**
     * Find unread in-app notifications
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.isRead = false")
    List<InAppNotification> findByIsReadFalse();

    /**
     * Find unread in-app notifications with pagination
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.isRead = false")
    Page<InAppNotification> findByIsReadFalse(Pageable pageable);

    /**
     * Find read in-app notifications
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.isRead = true")
    List<InAppNotification> findByIsReadTrue();

    /**
     * Find read in-app notifications with pagination
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.isRead = true")
    Page<InAppNotification> findByIsReadTrue(Pageable pageable);

    /**
     * Find in-app notifications by created at time range
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.createdAt BETWEEN :startTime AND :endTime")
    List<InAppNotification> findByCreatedAtBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT ian FROM InAppNotification ian WHERE ian.user.id = :userId " +
            "AND (ian.expiresAt IS NULL OR ian.expiresAt >= :now) " +
            "ORDER BY ian.createdAt DESC")
    List<InAppNotification> findActiveNotifications(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    /**
     * Find in-app notifications by created at time range with pagination
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.createdAt BETWEEN :startTime AND :endTime")
    Page<InAppNotification> findByCreatedAtBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, Pageable pageable);

    @Modifying
    @Query("DELETE FROM InAppNotification ian WHERE ian.expiresAt < :time")
    int deleteByExpiresAtBefore(@Param("time") LocalDateTime time);

    // ==================== SEARCH METHODS ====================

    /**
     * Search in-app notifications by title containing (case-insensitive)
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE LOWER(ian.notificationTitle) LIKE LOWER(CONCAT('%', :notificationTitle, '%'))")
    Page<InAppNotification> findByNotificationTitleContainingIgnoreCase(@Param("notificationTitle") String notificationTitle, Pageable pageable);

    /**
     * Search in-app notifications by message containing (case-insensitive)
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE LOWER(ian.notificationMessage) LIKE LOWER(CONCAT('%', :notificationMessage, '%'))")
    Page<InAppNotification> findByNotificationMessageContainingIgnoreCase(@Param("notificationMessage") String notificationMessage, Pageable pageable);

    /**
     * Search in-app notifications by multiple criteria
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE " +
            "(:userId IS NULL OR ian.user.id = :userId) AND " +
            "(:notificationType IS NULL OR ian.notificationType = :notificationType) AND " +
            "(:isRead IS NULL OR ian.isRead = :isRead) AND " +
            "(:startTime IS NULL OR ian.createdAt >= :startTime) AND " +
            "(:endTime IS NULL OR ian.createdAt <= :endTime)")
    Page<InAppNotification> searchInAppNotifications(
            @Param("userId") Long userId,
            @Param("notificationType") String notificationType,
            @Param("isRead") Boolean isRead,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);

    @Query("SELECT ian FROM InAppNotification ian WHERE " +
            "LOWER(ian.notificationTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(ian.notificationMessage) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<InAppNotification> searchNotifications(@Param("keyword") String keyword, Pageable pageable);

    // ==================== COUNT METHODS ====================

    /**
     * Count in-app notifications by user
     */
    @Query("SELECT COUNT(ian) FROM InAppNotification ian WHERE ian.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    /**
     * Count unread in-app notifications by user
     */
    @Query("SELECT COUNT(ian) FROM InAppNotification ian WHERE ian.user.id = :userId AND ian.isRead = false")
    long countByUserIdAndIsReadFalse(@Param("userId") Long userId);

    /**
     * Count read in-app notifications by user
     */
    @Query("SELECT COUNT(ian) FROM InAppNotification ian WHERE ian.user.id = :userId AND ian.isRead = true")
    long countByUserIdAndIsReadTrue(@Param("userId") Long userId);

    /**
     * Count in-app notifications by notification type
     */
    @Query("SELECT COUNT(ian) FROM InAppNotification ian WHERE ian.notificationType = :notificationType")
    long countByNotificationType(@Param("notificationType") String notificationType);

    /**
     * Count in-app notifications in time range
     */
    @Query("SELECT COUNT(ian) FROM InAppNotification ian WHERE ian.createdAt BETWEEN :startTime AND :endTime")
    long countByCreatedAtBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    // ==================== DELETE METHODS ====================

    /**
     * Delete all read notifications
     */
    @Modifying
    @Query("DELETE FROM InAppNotification ian WHERE ian.isRead = true")
    void deleteAllRead();

    /**
     * Delete read notifications older than specified date
     */
    @Modifying
    @Query("DELETE FROM InAppNotification ian WHERE ian.isRead = true AND ian.readAt < :cutoffDate")
    void deleteReadBefore(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Delete expired notifications
     */
    @Modifying
    @Query("DELETE FROM InAppNotification ian WHERE ian.expiresAt < CURRENT_TIMESTAMP")
    void deleteExpiredNotifications();

    /**
     * Delete all notifications for a user
     */
    @Modifying
    @Query("DELETE FROM InAppNotification ian WHERE ian.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    // ==================== CUSTOM QUERIES ====================

    /**
     * Find unread notifications by user
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.user.id = :userId AND ian.isRead = false " +
            "ORDER BY ian.createdAt DESC")
    List<InAppNotification> findUnreadNotificationsByUser(@Param("userId") Long userId);

    /**
     * Find unread notifications by user with pagination
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.user.id = :userId AND ian.isRead = false " +
            "ORDER BY ian.createdAt DESC")
    Page<InAppNotification> findUnreadNotificationsByUser(@Param("userId") Long userId, Pageable pageable);

    /**
     * Find INFO notifications
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.notificationType = 'INFO' " +
            "ORDER BY ian.createdAt DESC")
    List<InAppNotification> findInfoNotifications();

    /**
     * Find WARNING notifications
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.notificationType = 'WARNING' " +
            "ORDER BY ian.createdAt DESC")
    List<InAppNotification> findWarningNotifications();

    /**
     * Find ERROR notifications
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.notificationType = 'ERROR' " +
            "ORDER BY ian.createdAt DESC")
    List<InAppNotification> findErrorNotifications();

    /**
     * Find SUCCESS notifications
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.notificationType = 'SUCCESS' " +
            "ORDER BY ian.createdAt DESC")
    List<InAppNotification> findSuccessNotifications();

    /**
     * Find notifications by user and notification type
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.user.id = :userId AND ian.notificationType = :notificationType")
    List<InAppNotification> findByUserIdAndNotificationType(@Param("userId") Long userId, @Param("notificationType") String notificationType);

    /**
     * Find notifications by user and read status
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.user.id = :userId AND ian.isRead = :isRead")
    List<InAppNotification> findByUserIdAndIsRead(@Param("userId") Long userId, @Param("isRead") Boolean isRead);

    /**
     * Find notifications by user, ordered by created at
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.user.id = :userId " +
            "ORDER BY ian.createdAt DESC")
    List<InAppNotification> findByUserOrderByCreatedAt(@Param("userId") Long userId);

    /**
     * Find valid (not expired) notifications by user
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.user.id = :userId " +
            "AND (ian.expiresAt IS NULL OR ian.expiresAt >= CURRENT_TIMESTAMP) " +
            "ORDER BY ian.createdAt DESC")
    List<InAppNotification> findValidNotificationsByUser(@Param("userId") Long userId);

    /**
     * Find expired notifications
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.expiresAt < CURRENT_TIMESTAMP " +
            "ORDER BY ian.expiresAt")
    List<InAppNotification> findExpiredNotifications();

    /**
     * Find notifications expiring soon
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.expiresAt BETWEEN CURRENT_TIMESTAMP AND :futureTime " +
            "ORDER BY ian.expiresAt")
    List<InAppNotification> findNotificationsExpiringSoon(@Param("futureTime") LocalDateTime futureTime);

    /**
     * Find notifications read in time range
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE ian.readAt BETWEEN :startTime AND :endTime " +
            "ORDER BY ian.readAt DESC")
    List<InAppNotification> findNotificationsReadBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * Get in-app notification statistics
     */
    @Query("SELECT " +
            "COUNT(ian) as totalNotifications, " +
            "SUM(CASE WHEN ian.isRead = false THEN 1 ELSE 0 END) as unreadNotifications, " +
            "SUM(CASE WHEN ian.isRead = true THEN 1 ELSE 0 END) as readNotifications, " +
            "SUM(CASE WHEN ian.notificationType = 'INFO' THEN 1 ELSE 0 END) as infoNotifications, " +
            "SUM(CASE WHEN ian.notificationType = 'WARNING' THEN 1 ELSE 0 END) as warningNotifications, " +
            "SUM(CASE WHEN ian.notificationType = 'ERROR' THEN 1 ELSE 0 END) as errorNotifications, " +
            "SUM(CASE WHEN ian.notificationType = 'SUCCESS' THEN 1 ELSE 0 END) as successNotifications " +
            "FROM InAppNotification ian")
    Object getInAppNotificationStatistics();

    /**
     * Get in-app notification statistics by user
     */
    @Query("SELECT " +
            "COUNT(ian) as totalNotifications, " +
            "SUM(CASE WHEN ian.isRead = false THEN 1 ELSE 0 END) as unreadNotifications, " +
            "SUM(CASE WHEN ian.isRead = true THEN 1 ELSE 0 END) as readNotifications " +
            "FROM InAppNotification ian WHERE ian.user.id = :userId")
    Object getInAppNotificationStatisticsByUser(@Param("userId") Long userId);

    /**
     * Get in-app notifications grouped by notification type
     */
    @Query("SELECT ian.notificationType, COUNT(ian) as notificationCount, " +
            "SUM(CASE WHEN ian.isRead = false THEN 1 ELSE 0 END) as unreadCount " +
            "FROM InAppNotification ian GROUP BY ian.notificationType ORDER BY notificationCount DESC")
    List<Object[]> getInAppNotificationsByType();

    /**
     * Get daily in-app notification summary
     */
//    @Query("SELECT DATE(ian.createdAt) as notificationDate, COUNT(ian) as notificationCount, " +
//           "SUM(CASE WHEN ian.isRead = true THEN 1 ELSE 0 END) as readCount " +
//           "FROM InAppNotification ian WHERE ian.createdAt BETWEEN :startTime AND :endTime " +
//           "GROUP BY DATE(ian.createdAt) ORDER BY notificationDate DESC")
//    List<Object[]> getDailyInAppNotificationSummary(
//            @Param("startTime") LocalDateTime startTime,
//            @Param("endTime") LocalDateTime endTime);

    /**
     * Find today's notifications
     */
    @Query("SELECT ian FROM InAppNotification ian WHERE DATE(ian.createdAt) = CURRENT_DATE " +
            "ORDER BY ian.createdAt DESC")
    List<InAppNotification> findTodayNotifications();

    /**
     * Find all in-app notifications ordered by created at
     */
    @Query("SELECT ian FROM InAppNotification ian ORDER BY ian.createdAt DESC")
    List<InAppNotification> findAllByOrderByCreatedAtDesc();

    /**
     * Mark all notifications as read for user
     */
    @Modifying
    @Query("UPDATE InAppNotification ian SET ian.isRead = true, ian.readAt = CURRENT_TIMESTAMP " +
            "WHERE ian.user.id = :userId AND ian.isRead = false")
    void markAllAsReadByUser(@Param("userId") Long userId);

    /**
     * Get notification read rate by user
     */
    @Query("SELECT " +
            "COUNT(ian) as totalNotifications, " +
            "SUM(CASE WHEN ian.isRead = true THEN 1 ELSE 0 END) as readNotifications, " +
            "(SUM(CASE WHEN ian.isRead = true THEN 1 ELSE 0 END) * 100.0 / COUNT(ian)) as readRate " +
            "FROM InAppNotification ian WHERE ian.user.id = :userId AND ian.createdAt >= :sinceTime")
    Object getNotificationReadRateByUser(@Param("userId") Long userId, @Param("sinceTime") LocalDateTime sinceTime);
}