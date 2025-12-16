package lk.epicgreen.erp.mobilesync.repository;

import lk.epicgreen.erp.mobilesync.entity.SyncQueue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * SyncQueue Repository
 * Repository for sync queue data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface SyncQueueRepository extends JpaRepository<SyncQueue, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find sync queue by queue code
     */
    Optional<SyncQueue> findByQueueCode(String queueCode);
    
    /**
     * Check if sync queue exists by queue code
     */
    boolean existsByQueueCode(String queueCode);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find sync queues by sync type
     */
    List<SyncQueue> findBySyncType(String syncType);
    
    /**
     * Find sync queues by sync type with pagination
     */
    Page<SyncQueue> findBySyncType(String syncType, Pageable pageable);
    
    /**
     * Find sync queues by sync direction
     */
    List<SyncQueue> findBySyncDirection(String syncDirection);
    
    /**
     * Find sync queues by sync direction with pagination
     */
    Page<SyncQueue> findBySyncDirection(String syncDirection, Pageable pageable);
    
    /**
     * Find sync queues by status
     */
    List<SyncQueue> findByStatus(String status);
    
    /**
     * Find sync queues by status with pagination
     */
    Page<SyncQueue> findByStatus(String status, Pageable pageable);
    
    /**
     * Find sync queues by priority
     */
    List<SyncQueue> findByPriority(String priority);
    
    /**
     * Find sync queues by priority with pagination
     */
    Page<SyncQueue> findByPriority(String priority, Pageable pageable);
    
    /**
     * Find sync queues by entity type
     */
    List<SyncQueue> findByEntityType(String entityType);
    
    /**
     * Find sync queues by entity type with pagination
     */
    Page<SyncQueue> findByEntityType(String entityType, Pageable pageable);
    
    /**
     * Find sync queues by user
     */
    List<SyncQueue> findByUserId(Long userId);
    
    /**
     * Find sync queues by user with pagination
     */
    Page<SyncQueue> findByUserId(Long userId, Pageable pageable);
    
    /**
     * Find sync queues by device
     */
    List<SyncQueue> findByDeviceId(String deviceId);
    
    /**
     * Find sync queues by device with pagination
     */
    Page<SyncQueue> findByDeviceId(String deviceId, Pageable pageable);
    
    /**
     * Find sync queues by session
     */
    List<SyncQueue> findBySessionId(String sessionId);
    
    /**
     * Find sync queues by session ordered by priority
     */
    List<SyncQueue> findBySessionIdOrderByPriorityDescCreatedAtAsc(String sessionId);
    
    // ===================================================================
    // FIND BY ENTITY REFERENCE
    // ===================================================================
    
    /**
     * Find sync queue by entity type and entity ID
     */
    Optional<SyncQueue> findByEntityTypeAndEntityId(String entityType, Long entityId);
    
    /**
     * Find sync queues by entity type and entity ID
     */
    List<SyncQueue> findAllByEntityTypeAndEntityId(String entityType, Long entityId);
    
    /**
     * Find sync queue by entity reference
     */
    Optional<SyncQueue> findByEntityReference(String entityReference);
    
    /**
     * Find sync queues by entity reference
     */
    List<SyncQueue> findAllByEntityReference(String entityReference);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find sync queues by created at between dates
     */
    List<SyncQueue> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find sync queues by created at between dates with pagination
     */
    Page<SyncQueue> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Find sync queues by scheduled for between dates
     */
    List<SyncQueue> findByScheduledForBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find sync queues by processed at between dates
     */
    List<SyncQueue> findByProcessedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find sync queues by sync type and status
     */
    List<SyncQueue> findBySyncTypeAndStatus(String syncType, String status);
    
    /**
     * Find sync queues by sync type and status with pagination
     */
    Page<SyncQueue> findBySyncTypeAndStatus(String syncType, String status, Pageable pageable);
    
    /**
     * Find sync queues by sync direction and status
     */
    List<SyncQueue> findBySyncDirectionAndStatus(String syncDirection, String status);
    
    /**
     * Find sync queues by user and status
     */
    List<SyncQueue> findByUserIdAndStatus(Long userId, String status);
    
    /**
     * Find sync queues by device and status
     */
    List<SyncQueue> findByDeviceIdAndStatus(String deviceId, String status);
    
    /**
     * Find sync queues by entity type and status
     */
    List<SyncQueue> findByEntityTypeAndStatus(String entityType, String status);
    
    /**
     * Find sync queues by priority and status
     */
    List<SyncQueue> findByPriorityAndStatus(String priority, String status);
    
    // ===================================================================
    // CUSTOM QUERIES - QUEUE PROCESSING
    // ===================================================================
    
    /**
     * Find pending sync queues
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE sq.status = 'PENDING' " +
           "ORDER BY sq.priority DESC, sq.createdAt ASC")
    List<SyncQueue> findPendingSyncQueues();
    
    /**
     * Find pending sync queues with pagination
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE sq.status = 'PENDING' " +
           "ORDER BY sq.priority DESC, sq.createdAt ASC")
    Page<SyncQueue> findPendingSyncQueues(Pageable pageable);
    
    /**
     * Find sync queues ready to process
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE sq.status = 'PENDING' " +
           "AND (sq.scheduledFor IS NULL OR sq.scheduledFor <= :now) " +
           "ORDER BY sq.priority DESC, sq.createdAt ASC")
    List<SyncQueue> findSyncQueuesReadyToProcess(@Param("now") LocalDateTime now);
    
    /**
     * Find sync queues ready to process with limit
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE sq.status = 'PENDING' " +
           "AND (sq.scheduledFor IS NULL OR sq.scheduledFor <= :now) " +
           "ORDER BY sq.priority DESC, sq.createdAt ASC")
    List<SyncQueue> findSyncQueuesReadyToProcess(@Param("now") LocalDateTime now, Pageable pageable);
    
    /**
     * Find high priority pending queues
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE sq.status = 'PENDING' " +
           "AND sq.priority = 'HIGH' " +
           "ORDER BY sq.createdAt ASC")
    List<SyncQueue> findHighPriorityPendingQueues();
    
    /**
     * Find processing sync queues
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE sq.status = 'PROCESSING' " +
           "ORDER BY sq.processedAt ASC")
    List<SyncQueue> findProcessingSyncQueues();
    
    /**
     * Find failed sync queues for retry
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE sq.status = 'FAILED' " +
           "AND sq.retryCount < :maxRetries " +
           "AND (sq.nextRetryAt IS NULL OR sq.nextRetryAt <= :now) " +
           "ORDER BY sq.priority DESC, sq.createdAt ASC")
    List<SyncQueue> findFailedQueuesForRetry(@Param("maxRetries") Integer maxRetries, 
                                             @Param("now") LocalDateTime now);
    
    /**
     * Find stuck sync queues
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE sq.status = 'PROCESSING' " +
           "AND sq.processedAt < :thresholdTime")
    List<SyncQueue> findStuckSyncQueues(@Param("thresholdTime") LocalDateTime thresholdTime);
    
    // ===================================================================
    // CUSTOM QUERIES - USER/DEVICE SPECIFIC
    // ===================================================================
    
    /**
     * Find user's pending sync queues
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE sq.userId = :userId " +
           "AND sq.status = 'PENDING' " +
           "ORDER BY sq.priority DESC, sq.createdAt ASC")
    List<SyncQueue> findUserPendingSyncQueues(@Param("userId") Long userId);
    
    /**
     * Find device's pending sync queues
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE sq.deviceId = :deviceId " +
           "AND sq.status = 'PENDING' " +
           "ORDER BY sq.priority DESC, sq.createdAt ASC")
    List<SyncQueue> findDevicePendingSyncQueues(@Param("deviceId") String deviceId);
    
    /**
     * Find user's recent sync queues
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE sq.userId = :userId " +
           "ORDER BY sq.createdAt DESC")
    List<SyncQueue> findUserRecentSyncQueues(@Param("userId") Long userId, Pageable pageable);
    
    /**
     * Find device's recent sync queues
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE sq.deviceId = :deviceId " +
           "ORDER BY sq.createdAt DESC")
    List<SyncQueue> findDeviceRecentSyncQueues(@Param("deviceId") String deviceId, Pageable pageable);
    
    // ===================================================================
    // CUSTOM QUERIES - SEARCH
    // ===================================================================
    
    /**
     * Search sync queues
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE " +
           "LOWER(sq.queueCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(sq.syncType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(sq.entityType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(sq.username) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<SyncQueue> searchSyncQueues(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find recent sync queues
     */
    @Query("SELECT sq FROM SyncQueue sq ORDER BY sq.createdAt DESC")
    List<SyncQueue> findRecentSyncQueues(Pageable pageable);
    
    // ===================================================================
    // UPDATE OPERATIONS
    // ===================================================================
    
    /**
     * Update queue status
     */
    @Modifying
    @Query("UPDATE SyncQueue sq SET sq.status = :status, sq.processedAt = :processedAt " +
           "WHERE sq.id = :id")
    void updateStatus(@Param("id") Long id, 
                     @Param("status") String status, 
                     @Param("processedAt") LocalDateTime processedAt);
    
    /**
     * Update retry information
     */
    @Modifying
    @Query("UPDATE SyncQueue sq SET sq.retryCount = :retryCount, " +
           "sq.nextRetryAt = :nextRetryAt, sq.lastRetryAt = :lastRetryAt " +
           "WHERE sq.id = :id")
    void updateRetryInfo(@Param("id") Long id, 
                        @Param("retryCount") Integer retryCount,
                        @Param("nextRetryAt") LocalDateTime nextRetryAt,
                        @Param("lastRetryAt") LocalDateTime lastRetryAt);
    
    /**
     * Mark as processing
     */
    @Modifying
    @Query("UPDATE SyncQueue sq SET sq.status = 'PROCESSING', " +
           "sq.processedAt = :processedAt WHERE sq.id = :id")
    void markAsProcessing(@Param("id") Long id, @Param("processedAt") LocalDateTime processedAt);
    
    /**
     * Mark as completed
     */
    @Modifying
    @Query("UPDATE SyncQueue sq SET sq.status = 'COMPLETED', " +
           "sq.completedAt = :completedAt WHERE sq.id = :id")
    void markAsCompleted(@Param("id") Long id, @Param("completedAt") LocalDateTime completedAt);
    
    /**
     * Mark as failed
     */
    @Modifying
    @Query("UPDATE SyncQueue sq SET sq.status = 'FAILED', " +
           "sq.errorMessage = :errorMessage, sq.failedAt = :failedAt WHERE sq.id = :id")
    void markAsFailed(@Param("id") Long id, 
                     @Param("errorMessage") String errorMessage,
                     @Param("failedAt") LocalDateTime failedAt);
    
    /**
     * Cancel sync queue
     */
    @Modifying
    @Query("UPDATE SyncQueue sq SET sq.status = 'CANCELLED', " +
           "sq.cancelledAt = :cancelledAt, sq.cancellationReason = :reason WHERE sq.id = :id")
    void cancelSyncQueue(@Param("id") Long id, 
                        @Param("cancelledAt") LocalDateTime cancelledAt,
                        @Param("reason") String reason);
    
    /**
     * Cancel user's pending queues
     */
    @Modifying
    @Query("UPDATE SyncQueue sq SET sq.status = 'CANCELLED', " +
           "sq.cancelledAt = :cancelledAt, sq.cancellationReason = :reason " +
           "WHERE sq.userId = :userId AND sq.status = 'PENDING'")
    void cancelUserPendingQueues(@Param("userId") Long userId,
                                @Param("cancelledAt") LocalDateTime cancelledAt,
                                @Param("reason") String reason);
    
    /**
     * Cancel device's pending queues
     */
    @Modifying
    @Query("UPDATE SyncQueue sq SET sq.status = 'CANCELLED', " +
           "sq.cancelledAt = :cancelledAt, sq.cancellationReason = :reason " +
           "WHERE sq.deviceId = :deviceId AND sq.status = 'PENDING'")
    void cancelDevicePendingQueues(@Param("deviceId") String deviceId,
                                  @Param("cancelledAt") LocalDateTime cancelledAt,
                                  @Param("reason") String reason);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count sync queues by sync type
     */
    @Query("SELECT COUNT(sq) FROM SyncQueue sq WHERE sq.syncType = :syncType")
    Long countBySyncType(@Param("syncType") String syncType);
    
    /**
     * Count sync queues by status
     */
    @Query("SELECT COUNT(sq) FROM SyncQueue sq WHERE sq.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count sync queues by priority
     */
    @Query("SELECT COUNT(sq) FROM SyncQueue sq WHERE sq.priority = :priority")
    Long countByPriority(@Param("priority") String priority);
    
    /**
     * Count pending queues
     */
    @Query("SELECT COUNT(sq) FROM SyncQueue sq WHERE sq.status = 'PENDING'")
    Long countPendingQueues();
    
    /**
     * Count processing queues
     */
    @Query("SELECT COUNT(sq) FROM SyncQueue sq WHERE sq.status = 'PROCESSING'")
    Long countProcessingQueues();
    
    /**
     * Count failed queues
     */
    @Query("SELECT COUNT(sq) FROM SyncQueue sq WHERE sq.status = 'FAILED'")
    Long countFailedQueues();
    
    /**
     * Get sync type distribution
     */
    @Query("SELECT sq.syncType, COUNT(sq) as queueCount FROM SyncQueue sq " +
           "GROUP BY sq.syncType ORDER BY queueCount DESC")
    List<Object[]> getSyncTypeDistribution();
    
    /**
     * Get status distribution
     */
    @Query("SELECT sq.status, COUNT(sq) as queueCount FROM SyncQueue sq " +
           "GROUP BY sq.status ORDER BY queueCount DESC")
    List<Object[]> getStatusDistribution();
    
    /**
     * Get priority distribution
     */
    @Query("SELECT sq.priority, COUNT(sq) as queueCount FROM SyncQueue sq " +
           "GROUP BY sq.priority ORDER BY queueCount DESC")
    List<Object[]> getPriorityDistribution();
    
    /**
     * Get entity type distribution
     */
    @Query("SELECT sq.entityType, COUNT(sq) as queueCount FROM SyncQueue sq " +
           "GROUP BY sq.entityType ORDER BY queueCount DESC")
    List<Object[]> getEntityTypeDistribution();
    
    /**
     * Get most active users
     */
    @Query("SELECT sq.username, COUNT(sq) as queueCount FROM SyncQueue sq " +
           "WHERE sq.username IS NOT NULL " +
           "GROUP BY sq.username ORDER BY queueCount DESC")
    List<Object[]> getMostActiveUsers();
    
    /**
     * Get most active devices
     */
    @Query("SELECT sq.deviceId, COUNT(sq) as queueCount FROM SyncQueue sq " +
           "WHERE sq.deviceId IS NOT NULL " +
           "GROUP BY sq.deviceId ORDER BY queueCount DESC")
    List<Object[]> getMostActiveDevices();
    
    /**
     * Get average processing time
     */
    @Query("SELECT AVG(TIMESTAMPDIFF(SECOND, sq.processedAt, sq.completedAt)) " +
           "FROM SyncQueue sq WHERE sq.status = 'COMPLETED' " +
           "AND sq.processedAt IS NOT NULL AND sq.completedAt IS NOT NULL")
    Double getAverageProcessingTimeSeconds();
    
    /**
     * Get hourly queue count
     */
    @Query("SELECT HOUR(sq.createdAt) as hour, COUNT(sq) as queueCount FROM SyncQueue sq " +
           "WHERE sq.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY HOUR(sq.createdAt) ORDER BY hour")
    List<Object[]> getHourlyQueueCount(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);
    
    /**
     * Get daily queue count
     */
    @Query("SELECT DATE(sq.createdAt) as date, COUNT(sq) as queueCount FROM SyncQueue sq " +
           "WHERE sq.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE(sq.createdAt) ORDER BY date")
    List<Object[]> getDailyQueueCount(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);
    
    // ===================================================================
    // CLEANUP
    // ===================================================================
    
    /**
     * Delete completed sync queues
     */
    @Query("DELETE FROM SyncQueue sq WHERE sq.status = 'COMPLETED' " +
           "AND sq.completedAt < :beforeDate")
    void deleteCompletedSyncQueues(@Param("beforeDate") LocalDateTime beforeDate);
    
    /**
     * Delete cancelled sync queues
     */
    @Query("DELETE FROM SyncQueue sq WHERE sq.status = 'CANCELLED' " +
           "AND sq.cancelledAt < :beforeDate")
    void deleteCancelledSyncQueues(@Param("beforeDate") LocalDateTime beforeDate);
}
