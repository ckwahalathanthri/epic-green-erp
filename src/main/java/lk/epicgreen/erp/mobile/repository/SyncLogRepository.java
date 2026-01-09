package lk.epicgreen.erp.mobile.repository;

import lk.epicgreen.erp.mobile.entity.SyncLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for SyncLog entity
 * Based on ACTUAL database schema: sync_logs table
 * 
 * Fields: user_id (BIGINT), device_id, 
 *         device_type (ENUM: ANDROID, IOS), app_version,
 *         sync_type (ENUM: FULL, INCREMENTAL, PUSH, PULL),
 *         sync_direction (ENUM: UPLOAD, DOWNLOAD, BIDIRECTIONAL),
 *         sync_status (ENUM: INITIATED, IN_PROGRESS, COMPLETED, FAILED, CANCELLED),
 *         records_uploaded, records_downloaded, conflicts_detected, conflicts_resolved,
 *         error_message, started_at, completed_at, duration_seconds
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface SyncLogRepository extends JpaRepository<SyncLog, Long>, JpaSpecificationExecutor<SyncLog> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find sync logs by user
     */
    List<SyncLog> findByUserId(Long userId);
    
    /**
     * Find sync logs by user with pagination
     */
    Page<SyncLog> findByUserId(Long userId, Pageable pageable);
    List<SyncLog> findByDeviceId(String deviceId);

    List<SyncLog> findBySyncStatusIn(List<String> statuses);

    
    /**
     * Find sync logs by user and device
     */
    List<SyncLog> findByUserIdAndDeviceId(Long userId, String deviceId);

    List<SyncLog> findByConflictsDetectedGreaterThan(int count);

    List<SyncLog> findByConflictsResolvedLessThanConflictsDetected();
    List<SyncLog> findByUserIdAndConflictsResolvedLessThanConflictsDetected(Long userId);


    
    /**
     * Find sync logs by user and device with pagination
     */
    Page<SyncLog> findByUserIdAndDeviceId(Long userId, String deviceId, Pageable pageable);
    
    /**
     * Find sync logs by device type
     */
    List<SyncLog> findByDeviceType(String deviceType);
    
    /**
     * Find sync logs by device type with pagination
     */
    Page<SyncLog> findByDeviceType(String deviceType, Pageable pageable);
    
    /**
     * Find sync logs by sync status
     */
    List<SyncLog> findBySyncStatus(String syncStatus);
    
    /**
     * Find sync logs by sync status with pagination
     */
    Page<SyncLog> findBySyncStatus(String syncStatus, Pageable pageable);
    
    /**
     * Find sync logs by sync type
     */
    List<SyncLog> findBySyncType(String syncType);
    
    /**
     * Find sync logs by sync direction
     */
    List<SyncLog> findBySyncDirection(String syncDirection);
    
    /**
     * Find sync logs by started at time range
     */
    List<SyncLog> findByStartedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Find sync logs by started at time range with pagination
     */
    Page<SyncLog> findByStartedAtBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search sync logs by device ID containing (case-insensitive)
     */
    Page<SyncLog> findByDeviceIdContainingIgnoreCase(String deviceId, Pageable pageable);
    
    /**
     * Search sync logs by multiple criteria
     */
    @Query("SELECT sl FROM SyncLog sl WHERE " +
           "(:userId IS NULL OR sl.user.id = :userId) AND " +
           "(:deviceId IS NULL OR LOWER(sl.deviceId) LIKE LOWER(CONCAT('%', :deviceId, '%'))) AND " +
           "(:deviceType IS NULL OR sl.deviceType = :deviceType) AND " +
           "(:syncType IS NULL OR sl.syncType = :syncType) AND " +
           "(:syncStatus IS NULL OR sl.syncStatus = :syncStatus) AND " +
           "(:startTime IS NULL OR sl.startedAt >= :startTime) AND " +
           "(:endTime IS NULL OR sl.startedAt <= :endTime)")
    Page<SyncLog> searchSyncLogs(
            @Param("userId") Long userId,
            @Param("deviceId") String deviceId,
            @Param("deviceType") String deviceType,
            @Param("syncType") String syncType,
            @Param("syncStatus") String syncStatus,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count sync logs by sync status
     */
    long countBySyncStatus(String syncStatus);
    
    /**
     * Count sync logs by user
     */
    long countByUserId(Long userId);
    
    /**
     * Count sync logs by user and device
     */
    long countByUserIdAndDeviceId(Long userId, String deviceId);
    
    /**
     * Count sync logs in time range
     */
    long countByStartedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find initiated sync logs
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.syncStatus = 'INITIATED' ORDER BY sl.startedAt")
    List<SyncLog> findInitiatedSyncLogs();
    
    /**
     * Find in-progress sync logs
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.syncStatus = 'IN_PROGRESS' ORDER BY sl.startedAt")
    List<SyncLog> findInProgressSyncLogs();
    
    /**
     * Find completed sync logs
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.syncStatus = 'COMPLETED' ORDER BY sl.startedAt DESC")
    List<SyncLog> findCompletedSyncLogs();
    
    /**
     * Find failed sync logs
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.syncStatus = 'FAILED' ORDER BY sl.startedAt DESC")
    List<SyncLog> findFailedSyncLogs();
    
    /**
     * Find cancelled sync logs
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.syncStatus = 'CANCELLED' ORDER BY sl.startedAt DESC")
    List<SyncLog> findCancelledSyncLogs();
    
    /**
     * Find full sync logs
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.syncType = 'FULL' ORDER BY sl.startedAt DESC")
    List<SyncLog> findFullSyncLogs();
    
    /**
     * Find incremental sync logs
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.syncType = 'INCREMENTAL' ORDER BY sl.startedAt DESC")
    List<SyncLog> findIncrementalSyncLogs();
    
    /**
     * Find Android sync logs
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.deviceType = 'ANDROID' ORDER BY sl.startedAt DESC")
    List<SyncLog> findAndroidSyncLogs();
    
    /**
     * Find iOS sync logs
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.deviceType = 'IOS' ORDER BY sl.startedAt DESC")
    List<SyncLog> findIosSyncLogs();
    
    /**
     * Find sync logs with conflicts
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.conflictsDetected > 0 ORDER BY sl.startedAt DESC")
    List<SyncLog> findSyncLogsWithConflicts();
    
    /**
     * Find latest sync by user and device
     */
    @Query("SELECT sl FROM SyncLog sl WHERE sl.user.id = :userId AND sl.deviceId = :deviceId " +
           "ORDER BY sl.startedAt DESC LIMIT 1")
    SyncLog findLatestSyncByUserAndDevice(@Param("userId") Long userId, @Param("deviceId") String deviceId);
    
    /**
     * Find sync logs by user, device and status
     */
    List<SyncLog> findByUserIdAndDeviceIdAndSyncStatus(Long userId, String deviceId, String syncStatus);
    
    /**
     * Get total records uploaded by user
     */
    @Query("SELECT SUM(sl.recordsUploaded) FROM SyncLog sl WHERE sl.user.id = :userId " +
           "AND sl.syncStatus = 'COMPLETED'")
    Long getTotalRecordsUploadedByUser(@Param("userId") Long userId);
    
    /**
     * Get total records downloaded by user
     */
    @Query("SELECT SUM(sl.recordsDownloaded) FROM SyncLog sl WHERE sl.user.id = :userId " +
           "AND sl.syncStatus = 'COMPLETED'")
    Long getTotalRecordsDownloadedByUser(@Param("userId") Long userId);
    
    /**
     * Get total conflicts detected by user
     */
    @Query("SELECT SUM(sl.conflictsDetected) FROM SyncLog sl WHERE sl.user.id = :userId")
    Long getTotalConflictsDetectedByUser(@Param("userId") Long userId);
    
    /**
     * Get sync log statistics
     */
    @Query("SELECT " +
           "COUNT(sl) as totalSyncs, " +
           "SUM(CASE WHEN sl.syncStatus = 'INITIATED' THEN 1 ELSE 0 END) as initiatedSyncs, " +
           "SUM(CASE WHEN sl.syncStatus = 'IN_PROGRESS' THEN 1 ELSE 0 END) as inProgressSyncs, " +
           "SUM(CASE WHEN sl.syncStatus = 'COMPLETED' THEN 1 ELSE 0 END) as completedSyncs, " +
           "SUM(CASE WHEN sl.syncStatus = 'FAILED' THEN 1 ELSE 0 END) as failedSyncs, " +
           "SUM(sl.recordsUploaded) as totalUploaded, " +
           "SUM(sl.recordsDownloaded) as totalDownloaded, " +
           "SUM(sl.conflictsDetected) as totalConflicts " +
           "FROM SyncLog sl")
    Object getSyncLogStatistics();
    
    /**
     * Get sync logs grouped by status
     */
    @Query("SELECT sl.syncStatus, COUNT(sl) as syncCount, " +
           "SUM(sl.recordsUploaded) as totalUploaded, SUM(sl.recordsDownloaded) as totalDownloaded " +
           "FROM SyncLog sl GROUP BY sl.syncStatus ORDER BY syncCount DESC")
    List<Object[]> getSyncLogsByStatus();
    
    /**
     * Get sync logs grouped by device type
     */
    @Query("SELECT sl.deviceType, COUNT(sl) as syncCount, " +
           "SUM(sl.recordsUploaded) as totalUploaded, SUM(sl.recordsDownloaded) as totalDownloaded " +
           "FROM SyncLog sl GROUP BY sl.deviceType ORDER BY syncCount DESC")
    List<Object[]> getSyncLogsByDeviceType();
    
    /**
     * Get sync logs grouped by sync type
     */
    @Query("SELECT sl.syncType, COUNT(sl) as syncCount " +
           "FROM SyncLog sl GROUP BY sl.syncType ORDER BY syncCount DESC")
    List<Object[]> getSyncLogsBySyncType();
    
    /**
     * Get daily sync summary
     */
//    @Query("SELECT DATE(sl.startedAt) as syncDate, COUNT(sl) as syncCount, " +
//           "SUM(sl.recordsUploaded) as totalUploaded, SUM(sl.recordsDownloaded) as totalDownloaded " +
//           "FROM SyncLog sl WHERE sl.startedAt BETWEEN :startTime AND :endTime " +
//           "GROUP BY DATE(sl.startedAt) ORDER BY syncDate DESC")
//    List<Object[]> getDailySyncSummary(
//            @Param("startTime") LocalDateTime startTime,
//            @Param("endTime") LocalDateTime endTime);
    
    /**
     * Find today's sync logs
     */
    @Query("SELECT sl FROM SyncLog sl WHERE DATE(sl.startedAt) = CURRENT_DATE ORDER BY sl.startedAt DESC")
    List<SyncLog> findTodaySyncLogs();


    
    /**
     * Find all sync logs ordered by started at
     */
    List<SyncLog> findAllByOrderByStartedAtDesc();
    
    /**
     * Get average sync duration by sync type
     */
    @Query("SELECT sl.syncType, AVG(sl.durationSeconds) as avgDuration " +
           "FROM SyncLog sl WHERE sl.syncStatus = 'COMPLETED' AND sl.durationSeconds IS NOT NULL " +
           "GROUP BY sl.syncType")
    List<Object[]> getAverageSyncDurationBySyncType();
}
