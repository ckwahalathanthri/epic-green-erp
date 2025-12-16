package lk.epicgreen.erp.mobilesync.repository;

import lk.epicgreen.erp.mobilesync.entity.SyncLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * SyncLog Repository
 * Repository for sync log data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface SyncLogRepository extends JpaRepository<SyncLog, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find sync log by sync log code
     */
    Optional<SyncLog> findBySyncLogCode(String syncLogCode);
    
    /**
     * Check if sync log exists by sync log code
     */
    boolean existsBySyncLogCode(String syncLogCode);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find sync logs by sync type
     */
    List<SyncLog> findBySyncType(String syncType);
    
    /**
     * Find sync logs by sync type with pagination
     */
    Page<SyncLog> findBySyncType(String syncType, Pageable pageable);
    
    /**
     * Find sync logs by sync direction
     */
    List<SyncLog> findBySyncDirection(String syncDirection);
    
    /**
     * Find sync logs by sync direction with pagination
     */
    Page<SyncLog> findBySyncDirection(String syncDirection, Pageable pageable);
    
    /**
     * Find sync logs by status
     */
    List<SyncLog> findByStatus(String status);
    
    /**
     * Find sync logs by status with pagination
     */
    Page<SyncLog> findByStatus(String status, Pageable pageable);
    
    /**
     * Find sync logs by entity type
     */
    List<SyncLog> findByEntityType(String entityType);
    
    /**
     * Find sync logs by entity type with pagination
     */
    Page<SyncLog> findByEntityType(String entityType, Pageable pageable);
    
    /**
     * Find sync logs by user
     */
    List<SyncLog> findByUserId(Long userId);
    
    /**
     * Find sync logs by user with pagination
     */
    Page<SyncLog> findByUserId(Long userId, Pageable pageable);
    
    /**
     * Find sync logs by device
     */
    List<SyncLog> findByDeviceId(String deviceId);
    
    /**
     * Find sync logs by device with pagination
     */
    Page<SyncLog> findByDeviceId(String deviceId, Pageable pageable);
    
    /**
     * Find sync logs by session
     */
    List<SyncLog> findBySessionId(String sessionId);
    
    /**
     * Find sync logs by session ordered by started time
     */
    List<SyncLog> findBySessionIdOrderByStartedAtAsc(String sessionId);
    
    /**
     * Find sync logs by has conflicts
     */
    List<SyncLog> findByHasConflicts(Boolean hasConflicts);
    
    /**
     * Find sync logs by has conflicts with pagination
     */
    Page<SyncLog> findByHasConflicts(Boolean hasConflicts, Pageable pageable);
    
    /**
     * Find sync logs by is retry
     */
    List<SyncLog> findByIsRetry(Boolean isRetry);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find sync logs by started at between dates
     */
    List<SyncLog> findByStartedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find sync logs by started at between dates with pagination
     */
    Page<SyncLog> findByStartedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Find sync logs by completed at between dates
     */
    List<SyncLog> findByCompletedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find sync logs by sync type and status
     */
    List<SyncLog> findBySyncTypeAndStatus(String syncType, String status);
    
    /**
     * Find sync logs by sync type and status with pagination
     */
    Page<SyncLog> findBySyncTypeAndStatus(String syncType, String status, Pageable pageable);
    
    /**
     * Find sync logs by sync direction and status
     */
    List<SyncLog> findBySyncDirectionAndStatus(String syncDirection, String status);
    
    /**
     * Find sync logs by user and status
     */
    List<SyncLog> findByUserIdAndStatus(Long userId, String status);
    
    /**
     * Find sync logs by device and status
     */
    List<SyncLog> findByDeviceIdAndStatus(String deviceId, String status);
    
    /**
     * Find sync logs by entity type and status
     */
    List<SyncLog> findByEntityTypeAndStatus(String entityType, String status);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search sync logs
     */
    @Query("SELECT sl FROM SyncLog sl WHERE " +
           "LOWER(sl.syncLogCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(sl.syncType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(sl.entityType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(sl.username) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<SyncLog> searchSyncLogs(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find successful sync logs
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.status = 'SUCCESS' ORDER BY sl.startedAt DESC")
    List<SyncLog> findSuccessfulSyncLogs();
    
    /**
     * Find successful sync logs with pagination
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.status = 'SUCCESS' ORDER BY sl.startedAt DESC")
    Page<SyncLog> findSuccessfulSyncLogs(Pageable pageable);
    
    /**
     * Find failed sync logs
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.status = 'FAILED' ORDER BY sl.startedAt DESC")
    List<SyncLog> findFailedSyncLogs();
    
    /**
     * Find failed sync logs with pagination
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.status = 'FAILED' ORDER BY sl.startedAt DESC")
    Page<SyncLog> findFailedSyncLogs(Pageable pageable);
    
    /**
     * Find pending sync logs
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.status = 'PENDING' ORDER BY sl.startedAt ASC")
    List<SyncLog> findPendingSyncLogs();
    
    /**
     * Find in progress sync logs
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.status = 'IN_PROGRESS' ORDER BY sl.startedAt ASC")
    List<SyncLog> findInProgressSyncLogs();
    
    /**
     * Find sync logs with conflicts
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.hasConflicts = true ORDER BY sl.startedAt DESC")
    List<SyncLog> findSyncLogsWithConflicts();
    
    /**
     * Find sync logs with conflicts with pagination
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.hasConflicts = true ORDER BY sl.startedAt DESC")
    Page<SyncLog> findSyncLogsWithConflicts(Pageable pageable);
    
    /**
     * Find recent sync logs
     */
    @Query("SELECT sl FROM SyncLog sl ORDER BY sl.startedAt DESC")
    List<SyncLog> findRecentSyncLogs(Pageable pageable);
    
    /**
     * Find user's recent sync logs
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.userId = :userId ORDER BY sl.startedAt DESC")
    List<SyncLog> findUserRecentSyncLogs(@Param("userId") Long userId, Pageable pageable);
    
    /**
     * Find device's recent sync logs
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.deviceId = :deviceId ORDER BY sl.startedAt DESC")
    List<SyncLog> findDeviceRecentSyncLogs(@Param("deviceId") String deviceId, Pageable pageable);
    
    /**
     * Find last successful sync by user and entity type
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.userId = :userId " +
           "AND sl.entityType = :entityType AND sl.status = 'SUCCESS' " +
           "ORDER BY sl.completedAt DESC")
    Optional<SyncLog> findLastSuccessfulSync(@Param("userId") Long userId, 
                                             @Param("entityType") String entityType);
    
    /**
     * Find last sync by device and entity type
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.deviceId = :deviceId " +
           "AND sl.entityType = :entityType " +
           "ORDER BY sl.startedAt DESC")
    Optional<SyncLog> findLastSyncByDevice(@Param("deviceId") String deviceId, 
                                           @Param("entityType") String entityType);
    
    /**
     * Find sync logs requiring retry
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.status = 'FAILED' " +
           "AND sl.retryCount < :maxRetries " +
           "ORDER BY sl.startedAt ASC")
    List<SyncLog> findSyncLogsRequiringRetry(@Param("maxRetries") Integer maxRetries);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count sync logs by sync type
     */
    @Query("SELECT COUNT(sl) FROM SyncLog sl WHERE sl.syncType = :syncType")
    Long countBySyncType(@Param("syncType") String syncType);
    
    /**
     * Count sync logs by sync direction
     */
    @Query("SELECT COUNT(sl) FROM SyncLog sl WHERE sl.syncDirection = :syncDirection")
    Long countBySyncDirection(@Param("syncDirection") String syncDirection);
    
    /**
     * Count sync logs by status
     */
    @Query("SELECT COUNT(sl) FROM SyncLog sl WHERE sl.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count sync logs by user
     */
    @Query("SELECT COUNT(sl) FROM SyncLog sl WHERE sl.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    /**
     * Count successful syncs
     */
    @Query("SELECT COUNT(sl) FROM SyncLog sl WHERE sl.status = 'SUCCESS'")
    Long countSuccessfulSyncs();
    
    /**
     * Count failed syncs
     */
    @Query("SELECT COUNT(sl) FROM SyncLog sl WHERE sl.status = 'FAILED'")
    Long countFailedSyncs();
    
    /**
     * Count syncs with conflicts
     */
    @Query("SELECT COUNT(sl) FROM SyncLog sl WHERE sl.hasConflicts = true")
    Long countSyncsWithConflicts();
    
    /**
     * Get sync type distribution
     */
    @Query("SELECT sl.syncType, COUNT(sl) as syncCount FROM SyncLog sl " +
           "GROUP BY sl.syncType ORDER BY syncCount DESC")
    List<Object[]> getSyncTypeDistribution();
    
    /**
     * Get sync direction distribution
     */
    @Query("SELECT sl.syncDirection, COUNT(sl) as syncCount FROM SyncLog sl " +
           "GROUP BY sl.syncDirection ORDER BY syncCount DESC")
    List<Object[]> getSyncDirectionDistribution();
    
    /**
     * Get status distribution
     */
    @Query("SELECT sl.status, COUNT(sl) as syncCount FROM SyncLog sl " +
           "GROUP BY sl.status ORDER BY syncCount DESC")
    List<Object[]> getStatusDistribution();
    
    /**
     * Get entity type distribution
     */
    @Query("SELECT sl.entityType, COUNT(sl) as syncCount FROM SyncLog sl " +
           "GROUP BY sl.entityType ORDER BY syncCount DESC")
    List<Object[]> getEntityTypeDistribution();
    
    /**
     * Get hourly sync count
     */
    @Query("SELECT HOUR(sl.startedAt) as hour, COUNT(sl) as syncCount FROM SyncLog sl " +
           "WHERE sl.startedAt BETWEEN :startDate AND :endDate " +
           "GROUP BY HOUR(sl.startedAt) ORDER BY hour")
    List<Object[]> getHourlySyncCount(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);
    
    /**
     * Get daily sync count
     */
    @Query("SELECT DATE(sl.startedAt) as date, COUNT(sl) as syncCount FROM SyncLog sl " +
           "WHERE sl.startedAt BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE(sl.startedAt) ORDER BY date")
    List<Object[]> getDailySyncCount(@Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate);
    
    /**
     * Get most active users
     */
    @Query("SELECT sl.username, COUNT(sl) as syncCount FROM SyncLog sl " +
           "WHERE sl.username IS NOT NULL " +
           "GROUP BY sl.username ORDER BY syncCount DESC")
    List<Object[]> getMostActiveUsers();
    
    /**
     * Get most active devices
     */
    @Query("SELECT sl.deviceId, COUNT(sl) as syncCount FROM SyncLog sl " +
           "WHERE sl.deviceId IS NOT NULL " +
           "GROUP BY sl.deviceId ORDER BY syncCount DESC")
    List<Object[]> getMostActiveDevices();
    
    /**
     * Get average sync duration
     */
    @Query("SELECT AVG(sl.durationMillis) FROM SyncLog sl WHERE sl.durationMillis IS NOT NULL")
    Double getAverageSyncDuration();
    
    /**
     * Get average records synced
     */
    @Query("SELECT AVG(sl.recordsSynced) FROM SyncLog sl WHERE sl.recordsSynced IS NOT NULL")
    Double getAverageRecordsSynced();
    
    /**
     * Get total records synced
     */
    @Query("SELECT SUM(sl.recordsSynced) FROM SyncLog sl WHERE sl.recordsSynced IS NOT NULL")
    Long getTotalRecordsSynced();
    
    /**
     * Get sync success rate
     */
    @Query("SELECT (COUNT(CASE WHEN sl.status = 'SUCCESS' THEN 1 END) * 100.0 / COUNT(*)) " +
           "FROM SyncLog sl")
    Double getSyncSuccessRate();
    
    /**
     * Find slow syncs
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.durationMillis > :thresholdMillis " +
           "ORDER BY sl.durationMillis DESC")
    List<SyncLog> findSlowSyncs(@Param("thresholdMillis") Long thresholdMillis, Pageable pageable);
    
    /**
     * Delete old sync logs
     */
    @Query("DELETE FROM SyncLog sl WHERE sl.startedAt < :beforeDate")
    void deleteOldSyncLogs(@Param("beforeDate") LocalDateTime beforeDate);
}
