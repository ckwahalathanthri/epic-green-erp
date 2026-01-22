package lk.epicgreen.erp.mobile.repository;

import lk.epicgreen.erp.mobile.entity.SyncQueue;
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
 * Repository interface for SyncQueue entity
 * Based on ACTUAL database schema: sync_queue table
 * 
 * Fields: user_id (BIGINT), device_id, entity_type, entity_id (BIGINT),
 *         operation_type (ENUM: INSERT, UPDATE, DELETE),
 *         data_snapshot (JSON),
 *         sync_status (ENUM: PENDING, IN_PROGRESS, SYNCED, FAILED, CONFLICT),
 *         priority, retry_count, max_retries, error_message,
 *         created_at, synced_at
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface SyncQueueRepository extends JpaRepository<SyncQueue, Long>, JpaSpecificationExecutor<SyncQueue> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find sync queue items by user
     */
    List<SyncQueue> findByUserId(Long userId);
    List<SyncQueue> findByDeviceId(String id);


    
    /**
     * Find sync queue items by user with pagination
     */
    Page<SyncQueue> findByUserId(Long userId, Pageable pageable);
    
    /**
     * Find sync queue items by user and device
     */
    List<SyncQueue> findByUserIdAndDeviceId(Long userId, String deviceId);
    
    /**
     * Find sync queue items by user and device with pagination
     */
    Page<SyncQueue> findByUserIdAndDeviceId(Long userId, String deviceId, Pageable pageable);
    
    /**
     * Find sync queue items by sync status
     */
    List<SyncQueue> findBySyncStatus(String syncStatus);
    
    /**
     * Find sync queue items by sync status with pagination
     */
    Page<SyncQueue> findBySyncStatus(String syncStatus, Pageable pageable);
    
    /**
     * Find sync queue items by entity type
     */
    List<SyncQueue> findByEntityType(String entityType);
    
    /**
     * Find sync queue items by entity type and entity ID
     */
    List<SyncQueue> findByEntityTypeAndEntityId(String entityType, Long entityId);
    
    /**
     * Find sync queue items by operation type
     */
    List<SyncQueue> findByOperationType(String operationType);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count sync queue items by sync status
     */
    long countBySyncStatus(String syncStatus);
    
    /**
     * Count sync queue items by user
     */
    long countByUserId(Long userId);
    
    /**
     * Count sync queue items by user and device
     */
    long countByUserIdAndDeviceId(Long userId, String deviceId);
    
    // ==================== DELETE METHODS ====================
    
    /**
     * Delete all synced items
     */
    @Modifying
    @Query("DELETE FROM SyncQueue sq WHERE sq.syncStatus = 'SYNCED'")
    void deleteAllSynced();
    
    /**
     * Delete synced items older than specified date
     */
    @Modifying
    @Query("DELETE FROM SyncQueue sq WHERE sq.syncStatus = 'SYNCED' AND sq.syncedAt < :cutoffDate")
    void deleteSyncedBefore(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    // ==================== CUSTOM QUERIES ====================

    @Query("SELECT sq FROM SyncQueue sq " +
            "WHERE sq.deviceId LIKE CONCAT('%', :keyword, '%') " +
            "OR sq.entityType LIKE CONCAT('%', :keyword, '%') " +
            "OR sq.operationType LIKE CONCAT('%', :keyword, '%') " +
            "OR sq.syncStatus LIKE CONCAT('%', :keyword, '%') " +
            "OR sq.errorMessage LIKE CONCAT('%', :keyword, '%')")
    Page<SyncQueue> searchSyncQueue(@Param("keyword") String keyword, Pageable pageable);

    
    /**
     * Find pending sync queue items
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE sq.syncStatus = 'PENDING' " +
           "ORDER BY sq.priority DESC, sq.createdAt ASC")
    List<SyncQueue> findPendingSyncQueueItems();
    
    /**
     * Find in-progress sync queue items
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE sq.syncStatus = 'IN_PROGRESS' " +
           "ORDER BY sq.createdAt")
    List<SyncQueue> findInProgressSyncQueueItems();
    
    /**
     * Find synced sync queue items
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE sq.syncStatus = 'SYNCED' " +
           "ORDER BY sq.syncedAt DESC")
    List<SyncQueue> findSyncedSyncQueueItems();
    
    /**
     * Find failed sync queue items
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE sq.syncStatus = 'FAILED' " +
           "ORDER BY sq.createdAt DESC")
    List<SyncQueue> findFailedSyncQueueItems();
    
    /**
     * Find conflict sync queue items
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE sq.syncStatus = 'CONFLICT' " +
           "ORDER BY sq.createdAt DESC")
    List<SyncQueue> findConflictSyncQueueItems();
    
    /**
     * Find pending items by user and device ordered by priority
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE sq.user.id = :userId AND sq.deviceId = :deviceId " +
           "AND sq.syncStatus = 'PENDING' ORDER BY sq.priority DESC, sq.createdAt ASC")
    List<SyncQueue> findPendingItemsByUserAndDevice(
            @Param("userId") Long userId, 
            @Param("deviceId") String deviceId);
    
    /**
     * Find items ready for retry
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE sq.syncStatus = 'FAILED' " +
           "AND sq.retryCount < sq.maxRetries ORDER BY sq.priority DESC, sq.createdAt ASC")
    List<SyncQueue> findItemsReadyForRetry();
    
    /**
     * Find high priority pending items
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE sq.syncStatus = 'PENDING' " +
           "AND sq.priority >= :priority ORDER BY sq.priority DESC, sq.createdAt ASC")
    List<SyncQueue> findHighPriorityPendingItems(@Param("priority") Integer priority);
    
    /**
     * Find items by user, device and status
     */
    List<SyncQueue> findByUserIdAndDeviceIdAndSyncStatus(Long userId, String deviceId, String syncStatus);
    
    /**
     * Find items by entity type and status
     */
    List<SyncQueue> findByEntityTypeAndSyncStatus(String entityType, String syncStatus);
    
    /**
     * Get sync queue statistics
     */
    @Query("SELECT " +
           "COUNT(sq) as totalItems, " +
           "SUM(CASE WHEN sq.syncStatus = 'PENDING' THEN 1 ELSE 0 END) as pendingItems, " +
           "SUM(CASE WHEN sq.syncStatus = 'IN_PROGRESS' THEN 1 ELSE 0 END) as inProgressItems, " +
           "SUM(CASE WHEN sq.syncStatus = 'SYNCED' THEN 1 ELSE 0 END) as syncedItems, " +
           "SUM(CASE WHEN sq.syncStatus = 'FAILED' THEN 1 ELSE 0 END) as failedItems, " +
           "SUM(CASE WHEN sq.syncStatus = 'CONFLICT' THEN 1 ELSE 0 END) as conflictItems " +
           "FROM SyncQueue sq")
    Object getSyncQueueStatistics();
    
    /**
     * Get sync queue grouped by status
     */
    @Query("SELECT sq.syncStatus, COUNT(sq) as itemCount " +
           "FROM SyncQueue sq GROUP BY sq.syncStatus ORDER BY itemCount DESC")
    List<Object[]> getSyncQueueByStatus();
    
    /**
     * Get sync queue grouped by entity type
     */
    @Query("SELECT sq.entityType, COUNT(sq) as itemCount, " +
           "SUM(CASE WHEN sq.syncStatus = 'PENDING' THEN 1 ELSE 0 END) as pendingCount " +
           "FROM SyncQueue sq GROUP BY sq.entityType ORDER BY itemCount DESC")
    List<Object[]> getSyncQueueByEntityType();
    
    /**
     * Get sync queue grouped by operation type
     */
    @Query("SELECT sq.operationType, COUNT(sq) as itemCount " +
           "FROM SyncQueue sq GROUP BY sq.operationType ORDER BY itemCount DESC")
    List<Object[]> getSyncQueueByOperationType();
    
    /**
     * Find all sync queue items ordered by priority and created at
     */
    List<SyncQueue> findAllByOrderByPriorityDescCreatedAtAsc();
    
    /**
     * Get oldest pending item
     */
    @Query("SELECT sq FROM SyncQueue sq WHERE sq.syncStatus = 'PENDING' " +
           "ORDER BY sq.createdAt ASC ")
    SyncQueue getOldestPendingItem();
}
