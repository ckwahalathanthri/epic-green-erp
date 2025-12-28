package lk.epicgreen.erp.mobile.repository;

import lk.epicgreen.erp.mobile.entity.SyncConflict;
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
 * Repository interface for SyncConflict entity
 * Based on ACTUAL database schema: sync_conflicts table
 * 
 * Fields: user_id (BIGINT), device_id, entity_type, entity_id (BIGINT),
 *         server_data (JSON), client_data (JSON),
 *         conflict_type (ENUM: UPDATE_UPDATE, UPDATE_DELETE, VERSION_MISMATCH),
 *         resolution_strategy (ENUM: SERVER_WINS, CLIENT_WINS, MANUAL, MERGE),
 *         status (ENUM: DETECTED, RESOLVED, IGNORED),
 *         resolved_data (JSON), resolved_by (BIGINT), resolved_at, detected_at
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface SyncConflictRepository extends JpaRepository<SyncConflict, Long>, JpaSpecificationExecutor<SyncConflict> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find sync conflicts by user
     */
    List<SyncConflict> findByUserId(Long userId);
    
    /**
     * Find sync conflicts by user with pagination
     */
    Page<SyncConflict> findByUserId(Long userId, Pageable pageable);
    
    /**
     * Find sync conflicts by user and device
     */
    List<SyncConflict> findByUserIdAndDeviceId(Long userId, String deviceId);
    
    /**
     * Find sync conflicts by user and device with pagination
     */
    Page<SyncConflict> findByUserIdAndDeviceId(Long userId, String deviceId, Pageable pageable);
    
    /**
     * Find sync conflicts by status
     */
    List<SyncConflict> findByStatus(String status);
    
    /**
     * Find sync conflicts by status with pagination
     */
    Page<SyncConflict> findByStatus(String status, Pageable pageable);
    
    /**
     * Find sync conflicts by entity type
     */
    List<SyncConflict> findByEntityType(String entityType);
    
    /**
     * Find sync conflicts by entity type and entity ID
     */
    List<SyncConflict> findByEntityTypeAndEntityId(String entityType, Long entityId);
    
    /**
     * Find sync conflicts by conflict type
     */
    List<SyncConflict> findByConflictType(String conflictType);
    
    /**
     * Find sync conflicts by resolution strategy
     */
    List<SyncConflict> findByResolutionStrategy(String resolutionStrategy);
    
    /**
     * Find sync conflicts by resolved by user
     */
    List<SyncConflict> findByResolvedBy(Long resolvedBy);
    
    /**
     * Find sync conflicts by detected at time range
     */
    List<SyncConflict> findByDetectedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count sync conflicts by status
     */
    long countByStatus(String status);
    
    /**
     * Count sync conflicts by user
     */
    long countByUserId(Long userId);
    
    /**
     * Count sync conflicts by user and device
     */
    long countByUserIdAndDeviceId(Long userId, String deviceId);
    
    // ==================== DELETE METHODS ====================
    
    /**
     * Delete all resolved conflicts
     */
    @Modifying
    @Query("DELETE FROM SyncConflict sc WHERE sc.status = 'RESOLVED'")
    void deleteAllResolved();
    
    /**
     * Delete resolved conflicts older than specified date
     */
    @Modifying
    @Query("DELETE FROM SyncConflict sc WHERE sc.status = 'RESOLVED' AND sc.resolvedAt < :cutoffDate")
    void deleteResolvedBefore(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find detected sync conflicts
     */
    @Query("SELECT sc FROM SyncConflict sc WHERE sc.status = 'DETECTED' " +
           "ORDER BY sc.detectedAt DESC")
    List<SyncConflict> findDetectedSyncConflicts();
    
    /**
     * Find resolved sync conflicts
     */
    @Query("SELECT sc FROM SyncConflict sc WHERE sc.status = 'RESOLVED' " +
           "ORDER BY sc.resolvedAt DESC")
    List<SyncConflict> findResolvedSyncConflicts();
    
    /**
     * Find ignored sync conflicts
     */
    @Query("SELECT sc FROM SyncConflict sc WHERE sc.status = 'IGNORED' " +
           "ORDER BY sc.detectedAt DESC")
    List<SyncConflict> findIgnoredSyncConflicts();
    
    /**
     * Find update-update conflicts
     */
    @Query("SELECT sc FROM SyncConflict sc WHERE sc.conflictType = 'UPDATE_UPDATE' " +
           "ORDER BY sc.detectedAt DESC")
    List<SyncConflict> findUpdateUpdateConflicts();
    
    /**
     * Find update-delete conflicts
     */
    @Query("SELECT sc FROM SyncConflict sc WHERE sc.conflictType = 'UPDATE_DELETE' " +
           "ORDER BY sc.detectedAt DESC")
    List<SyncConflict> findUpdateDeleteConflicts();
    
    /**
     * Find version mismatch conflicts
     */
    @Query("SELECT sc FROM SyncConflict sc WHERE sc.conflictType = 'VERSION_MISMATCH' " +
           "ORDER BY sc.detectedAt DESC")
    List<SyncConflict> findVersionMismatchConflicts();
    
    /**
     * Find manual resolution conflicts
     */
    @Query("SELECT sc FROM SyncConflict sc WHERE sc.resolutionStrategy = 'MANUAL' " +
           "AND sc.status = 'DETECTED' ORDER BY sc.detectedAt")
    List<SyncConflict> findManualResolutionConflicts();
    
    /**
     * Find conflicts by user, device and status
     */
    List<SyncConflict> findByUserIdAndDeviceIdAndStatus(Long userId, String deviceId, String status);
    
    /**
     * Find conflicts by entity type and status
     */
    List<SyncConflict> findByEntityTypeAndStatus(String entityType, String status);
    
    /**
     * Find conflicts resolved in time range
     */
    @Query("SELECT sc FROM SyncConflict sc WHERE sc.resolvedAt BETWEEN :startTime AND :endTime " +
           "ORDER BY sc.resolvedAt DESC")
    List<SyncConflict> findConflictsResolvedBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * Get sync conflict statistics
     */
    @Query("SELECT " +
           "COUNT(sc) as totalConflicts, " +
           "SUM(CASE WHEN sc.status = 'DETECTED' THEN 1 ELSE 0 END) as detectedConflicts, " +
           "SUM(CASE WHEN sc.status = 'RESOLVED' THEN 1 ELSE 0 END) as resolvedConflicts, " +
           "SUM(CASE WHEN sc.status = 'IGNORED' THEN 1 ELSE 0 END) as ignoredConflicts " +
           "FROM SyncConflict sc")
    Object getSyncConflictStatistics();
    
    /**
     * Get sync conflicts grouped by status
     */
    @Query("SELECT sc.status, COUNT(sc) as conflictCount " +
           "FROM SyncConflict sc GROUP BY sc.status ORDER BY conflictCount DESC")
    List<Object[]> getSyncConflictsByStatus();
    
    /**
     * Get sync conflicts grouped by conflict type
     */
    @Query("SELECT sc.conflictType, COUNT(sc) as conflictCount, " +
           "SUM(CASE WHEN sc.status = 'DETECTED' THEN 1 ELSE 0 END) as detectedCount " +
           "FROM SyncConflict sc GROUP BY sc.conflictType ORDER BY conflictCount DESC")
    List<Object[]> getSyncConflictsByType();
    
    /**
     * Get sync conflicts grouped by entity type
     */
    @Query("SELECT sc.entityType, COUNT(sc) as conflictCount " +
           "FROM SyncConflict sc GROUP BY sc.entityType ORDER BY conflictCount DESC")
    List<Object[]> getSyncConflictsByEntityType();
    
    /**
     * Get sync conflicts grouped by resolution strategy
     */
    @Query("SELECT sc.resolutionStrategy, COUNT(sc) as conflictCount " +
           "FROM SyncConflict sc GROUP BY sc.resolutionStrategy ORDER BY conflictCount DESC")
    List<Object[]> getSyncConflictsByResolutionStrategy();
    
    /**
     * Find today's detected conflicts
     */
    @Query("SELECT sc FROM SyncConflict sc WHERE DATE(sc.detectedAt) = CURRENT_DATE " +
           "ORDER BY sc.detectedAt DESC")
    List<SyncConflict> findTodayDetectedConflicts();
    
    /**
     * Find all sync conflicts ordered by detected at
     */
    List<SyncConflict> findAllByOrderByDetectedAtDesc();
    
    /**
     * Get oldest unresolved conflict
     */
    @Query("SELECT sc FROM SyncConflict sc WHERE sc.status = 'DETECTED' " +
           "ORDER BY sc.detectedAt ASC LIMIT 1")
    SyncConflict getOldestUnresolvedConflict();
}
