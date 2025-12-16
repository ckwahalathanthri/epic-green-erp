package lk.epicgreen.erp.notification.repository;

import lk.epicgreen.erp.notification.entity.NotificationQueue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * NotificationQueue Repository
 * Repository for notification queue data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface NotificationQueueRepository extends JpaRepository<NotificationQueue, Long> {
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find notifications by notification type
     */
    List<NotificationQueue> findByNotificationType(String notificationType);
    
    /**
     * Find notifications by notification type with pagination
     */
    Page<NotificationQueue> findByNotificationType(String notificationType, Pageable pageable);
    
    /**
     * Find notifications by category
     */
    List<NotificationQueue> findByCategory(String category);
    
    /**
     * Find notifications by category with pagination
     */
    Page<NotificationQueue> findByCategory(String category, Pageable pageable);
    
    /**
     * Find notifications by status
     */
    List<NotificationQueue> findByStatus(String status);
    
    /**
     * Find notifications by status with pagination
     */
    Page<NotificationQueue> findByStatus(String status, Pageable pageable);
    
    /**
     * Find notifications by priority
     */
    List<NotificationQueue> findByPriority(String priority);
    
    /**
     * Find notifications by priority with pagination
     */
    Page<NotificationQueue> findByPriority(String priority, Pageable pageable);
    
    /**
     * Find notifications by recipient
     */
    List<NotificationQueue> findByRecipient(String recipient);
    
    /**
     * Find notifications by recipient with pagination
     */
    Page<NotificationQueue> findByRecipient(String recipient, Pageable pageable);
    
    /**
     * Find notifications by user ID
     */
    List<NotificationQueue> findByUserId(Long userId);
    
    /**
     * Find notifications by user ID with pagination
     */
    Page<NotificationQueue> findByUserId(Long userId, Pageable pageable);
    
    /**
     * Find notifications by batch ID
     */
    List<NotificationQueue> findByBatchId(String batchId);
    
    /**
     * Find notifications by batch ID with pagination
     */
    Page<NotificationQueue> findByBatchId(String batchId, Pageable pageable);
    
    /**
     * Find notifications by reference type
     */
    List<NotificationQueue> findByReferenceType(String referenceType);
    
    /**
     * Find notifications by reference type with pagination
     */
    Page<NotificationQueue> findByReferenceType(String referenceType, Pageable pageable);
    
    /**
     * Find notifications by provider
     */
    List<NotificationQueue> findByProvider(String provider);
    
    /**
     * Find notifications by provider with pagination
     */
    Page<NotificationQueue> findByProvider(String provider, Pageable pageable);
    
    // ===================================================================
    // FIND BY REFERENCE
    // ===================================================================
    
    /**
     * Find notifications by reference type and reference ID
     */
    List<NotificationQueue> findByReferenceTypeAndReferenceId(String referenceType, Long referenceId);
    
    /**
     * Find notifications by reference type and reference number
     */
    List<NotificationQueue> findByReferenceTypeAndReferenceNumber(String referenceType, String referenceNumber);
    
    // ===================================================================
    // FIND BY STATUS
    // ===================================================================
    
    /**
     * Find pending notifications
     */
    @Query("SELECT n FROM NotificationQueue n WHERE n.status = 'PENDING' " +
           "ORDER BY n.priority ASC, n.scheduledTime ASC")
    List<NotificationQueue> findPendingNotifications();
    
    /**
     * Find pending notifications with pagination
     */
    @Query("SELECT n FROM NotificationQueue n WHERE n.status = 'PENDING' " +
           "ORDER BY n.priority ASC, n.scheduledTime ASC")
    Page<NotificationQueue> findPendingNotifications(Pageable pageable);
    
    /**
     * Find notifications ready to send (pending and scheduled time <= now)
     */
    @Query("SELECT n FROM NotificationQueue n WHERE n.status = 'PENDING' " +
           "AND n.scheduledTime <= :now ORDER BY n.priority ASC, n.scheduledTime ASC")
    List<NotificationQueue> findNotificationsReadyToSend(@Param("now") LocalDateTime now);
    
    /**
     * Find notifications ready to send with limit
     */
    @Query("SELECT n FROM NotificationQueue n WHERE n.status = 'PENDING' " +
           "AND n.scheduledTime <= :now ORDER BY n.priority ASC, n.scheduledTime ASC")
    List<NotificationQueue> findNotificationsReadyToSend(@Param("now") LocalDateTime now, Pageable pageable);

    
    /**
     * Find failed notifications that can be retried
     */
    @Query("SELECT n FROM NotificationQueue n WHERE n.status = 'FAILED' " +
           "AND n.retryCount < n.maxRetryAttempts " +
           "AND (n.nextRetryTime IS NULL OR n.nextRetryTime <= :now) " +
           "ORDER BY n.priority ASC")
    List<NotificationQueue> findFailedNotificationsForRetry(@Param("now") LocalDateTime now);
    
    /**
     * Find failed notifications that can be retried with limit
     */
    @Query("SELECT n FROM NotificationQueue n WHERE n.status = 'FAILED' " +
           "AND n.retryCount < n.maxRetryAttempts " +
           "AND (n.nextRetryTime IS NULL OR n.nextRetryTime <= :now) " +
           "ORDER BY n.priority ASC")
    List<NotificationQueue> findFailedNotificationsForRetry(@Param("now") LocalDateTime now, Pageable pageable);
    
    /**
     * Find processing notifications (stuck)
     */
    @Query("SELECT n FROM NotificationQueue n WHERE n.status = 'PROCESSING' " +
           "AND n.updatedAt < :threshold")
    List<NotificationQueue> findStuckNotifications(@Param("threshold") LocalDateTime threshold);
    
    // ===================================================================
    // FIND BY PRIORITY
    // ===================================================================
    
    /**
     * Find high priority pending notifications
     */
    @Query("SELECT n FROM NotificationQueue n WHERE n.status = 'PENDING' " +
           "AND n.priority = 'HIGH' ORDER BY n.scheduledTime ASC")
    List<NotificationQueue> findHighPriorityPendingNotifications();
    
    /**
     * Find pending notifications by priority
     */
    @Query("SELECT n FROM NotificationQueue n WHERE n.status = 'PENDING' " +
           "AND n.priority = :priority ORDER BY n.scheduledTime ASC")
    List<NotificationQueue> findPendingNotificationsByPriority(@Param("priority") String priority);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find notifications by scheduled time between dates
     */
    List<NotificationQueue> findByScheduledTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find notifications by scheduled time between dates with pagination
     */
    Page<NotificationQueue> findByScheduledTimeBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Find notifications by sent time between dates
     */
    List<NotificationQueue> findBySentTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find notifications by sent time between dates with pagination
     */
    Page<NotificationQueue> findBySentTimeBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Find notifications by status and scheduled time between dates
     */
    List<NotificationQueue> findByStatusAndScheduledTimeBetween(
        String status, LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    /**
     * Count notifications in batch
     */
    @Query("SELECT COUNT(n) FROM NotificationQueue n WHERE n.batchId = :batchId")
    Long countByBatchId(@Param("batchId") String batchId);
    
    /**
     * Count sent notifications in batch
     */
    @Query("SELECT COUNT(n) FROM NotificationQueue n WHERE n.batchId = :batchId AND n.status = 'SENT'")
    Long countSentByBatchId(@Param("batchId") String batchId);
    
    /**
     * Count failed notifications in batch
     */
    @Query("SELECT COUNT(n) FROM NotificationQueue n WHERE n.batchId = :batchId AND n.status = 'FAILED'")
    Long countFailedByBatchId(@Param("batchId") String batchId);
    
    /**
     * Get batch progress
     */
    @Query("SELECT n.status, COUNT(n) as count FROM NotificationQueue n " +
           "WHERE n.batchId = :batchId GROUP BY n.status")
    List<Object[]> getBatchProgress(@Param("batchId") String batchId);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count notifications by status
     */
    @Query("SELECT COUNT(n) FROM NotificationQueue n WHERE n.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count notifications by notification type
     */
    @Query("SELECT COUNT(n) FROM NotificationQueue n WHERE n.notificationType = :notificationType")
    Long countByNotificationType(@Param("notificationType") String notificationType);
    
    /**
     * Count notifications by priority
     */
    @Query("SELECT COUNT(n) FROM NotificationQueue n WHERE n.priority = :priority")
    Long countByPriority(@Param("priority") String priority);
    
    /**
     * Get notification status distribution
     */
    @Query("SELECT n.status, COUNT(n) as notificationCount FROM NotificationQueue n " +
           "GROUP BY n.status ORDER BY notificationCount DESC")
    List<Object[]> getNotificationStatusDistribution();
    
    /**
     * Get notification type distribution
     */
    @Query("SELECT n.notificationType, COUNT(n) as notificationCount FROM NotificationQueue n " +
           "GROUP BY n.notificationType ORDER BY notificationCount DESC")
    List<Object[]> getNotificationTypeDistribution();
    
    /**
     * Get notification priority distribution
     */
    @Query("SELECT n.priority, COUNT(n) as notificationCount FROM NotificationQueue n " +
           "GROUP BY n.priority ORDER BY notificationCount DESC")
    List<Object[]> getNotificationPriorityDistribution();
    
    /**
     * Get hourly notification count
     */
    @Query("SELECT HOUR(n.scheduledTime) as hour, COUNT(n) as notificationCount FROM NotificationQueue n " +
           "WHERE n.scheduledTime BETWEEN :startDate AND :endDate " +
           "GROUP BY HOUR(n.scheduledTime) ORDER BY hour")
    List<Object[]> getHourlyNotificationCount(@Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);
    
    /**
     * Get daily notification count
     */
    @Query("SELECT DATE(n.scheduledTime) as date, COUNT(n) as notificationCount FROM NotificationQueue n " +
           "WHERE n.scheduledTime BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE(n.scheduledTime) ORDER BY date")
    List<Object[]> getDailyNotificationCount(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);
    
    /**
     * Get success rate
     */
    @Query("SELECT " +
           "CAST(SUM(CASE WHEN n.status = 'SENT' THEN 1 ELSE 0 END) AS DOUBLE) / COUNT(n) * 100 " +
           "FROM NotificationQueue n WHERE n.status IN ('SENT', 'FAILED')")
    Double getSuccessRate();
    
    /**
     * Get success rate by notification type
     */
    @Query("SELECT n.notificationType, " +
           "CAST(SUM(CASE WHEN n.status = 'SENT' THEN 1 ELSE 0 END) AS DOUBLE) / COUNT(n) * 100 as successRate " +
           "FROM NotificationQueue n WHERE n.status IN ('SENT', 'FAILED') " +
           "GROUP BY n.notificationType")
    List<Object[]> getSuccessRateByType();
    
    /**
     * Get provider performance
     */
    @Query("SELECT n.provider, COUNT(n) as totalCount, " +
           "SUM(CASE WHEN n.status = 'SENT' THEN 1 ELSE 0 END) as sentCount, " +
           "SUM(CASE WHEN n.status = 'FAILED' THEN 1 ELSE 0 END) as failedCount " +
           "FROM NotificationQueue n WHERE n.provider IS NOT NULL " +
           "GROUP BY n.provider")
    List<Object[]> getProviderPerformance();
    
    // ===================================================================
    // SEARCH
    // ===================================================================
    
    /**
     * Search notifications
     */
    @Query("SELECT n FROM NotificationQueue n WHERE " +
           "LOWER(n.recipient) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(n.subject) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(n.body) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(n.referenceNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<NotificationQueue> searchNotifications(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find recent notifications
     */
    @Query("SELECT n FROM NotificationQueue n ORDER BY n.createdAt DESC")
    List<NotificationQueue> findRecentNotifications(Pageable pageable);
    
    // ===================================================================
    // UPDATE OPERATIONS
    // ===================================================================
    
    /**
     * Update notification status
     */
    @Modifying
    @Query("UPDATE NotificationQueue n SET n.status = :status, n.updatedAt = :updatedAt " +
           "WHERE n.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") String status, 
                     @Param("updatedAt") LocalDateTime updatedAt);
    
    /**
     * Update retry count and next retry time
     */
    @Modifying
    @Query("UPDATE NotificationQueue n SET n.retryCount = :retryCount, " +
           "n.nextRetryTime = :nextRetryTime, n.lastRetryTime = :lastRetryTime, " +
           "n.updatedAt = :updatedAt WHERE n.id = :id")
    void updateRetryInfo(@Param("id") Long id, @Param("retryCount") Integer retryCount,
                        @Param("nextRetryTime") LocalDateTime nextRetryTime,
                        @Param("lastRetryTime") LocalDateTime lastRetryTime,
                        @Param("updatedAt") LocalDateTime updatedAt);
    
    /**
     * Mark notification as sent
     */
    @Modifying
    @Query("UPDATE NotificationQueue n SET n.status = 'SENT', n.sentTime = :sentTime, " +
           "n.providerMessageId = :providerMessageId, n.providerResponse = :providerResponse, " +
           "n.updatedAt = :updatedAt WHERE n.id = :id")
    void markAsSent(@Param("id") Long id, @Param("sentTime") LocalDateTime sentTime,
                   @Param("providerMessageId") String providerMessageId,
                   @Param("providerResponse") String providerResponse,
                   @Param("updatedAt") LocalDateTime updatedAt);
    
    /**
     * Mark notification as failed
     */
    @Modifying
    @Query("UPDATE NotificationQueue n SET n.status = 'FAILED', n.failedTime = :failedTime, " +
           "n.errorMessage = :errorMessage, n.errorDetails = :errorDetails, " +
           "n.updatedAt = :updatedAt WHERE n.id = :id")
    void markAsFailed(@Param("id") Long id, @Param("failedTime") LocalDateTime failedTime,
                     @Param("errorMessage") String errorMessage,
                     @Param("errorDetails") String errorDetails,
                     @Param("updatedAt") LocalDateTime updatedAt);
    
    /**
     * Cancel notification
     */
    @Modifying
    @Query("UPDATE NotificationQueue n SET n.status = 'CANCELLED', n.updatedAt = :updatedAt " +
           "WHERE n.id = :id AND n.status = 'PENDING'")
    int cancelNotification(@Param("id") Long id, @Param("updatedAt") LocalDateTime updatedAt);
    
    /**
     * Cancel batch notifications
     */
    @Modifying
    @Query("UPDATE NotificationQueue n SET n.status = 'CANCELLED', n.updatedAt = :updatedAt " +
           "WHERE n.batchId = :batchId AND n.status = 'PENDING'")
    int cancelBatchNotifications(@Param("batchId") String batchId, 
                                 @Param("updatedAt") LocalDateTime updatedAt);
    
    // ===================================================================
    // DELETE OPERATIONS
    // ===================================================================
    
    /**
     * Delete old sent notifications
     */
    @Modifying
    @Query("DELETE FROM NotificationQueue n WHERE n.status = 'SENT' " +
           "AND n.sentTime < :beforeDate")
    int deleteOldSentNotifications(@Param("beforeDate") LocalDateTime beforeDate);
    
    /**
     * Delete old cancelled notifications
     */
    @Modifying
    @Query("DELETE FROM NotificationQueue n WHERE n.status = 'CANCELLED' " +
           "AND n.updatedAt < :beforeDate")
    int deleteOldCancelledNotifications(@Param("beforeDate") LocalDateTime beforeDate);
}
