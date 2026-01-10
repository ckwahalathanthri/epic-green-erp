package lk.epicgreen.erp.audit.repository;

import lk.epicgreen.erp.audit.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for AuditLog entity
 * Based on ACTUAL database schema: audit_logs table
 * 
 * Fields: user_id (BIGINT), username, action, entity_type, entity_id (BIGINT), entity_name,
 *         operation_type (ENUM: CREATE, UPDATE, DELETE, VIEW, APPROVE, REJECT, LOGIN, LOGOUT),
 *         old_values (JSON), new_values (JSON), changed_fields (JSON),
 *         ip_address, user_agent, session_id,
 *         status (ENUM: SUCCESS, FAILED),
 *         error_message, created_at
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find audit logs by user
     */
    List<AuditLog> findByUserId(Long userId);
    
    /**
     * Find audit logs by user with pagination
     */
    Page<AuditLog> findByUserId(Long userId, Pageable pageable);

    Page<AuditLog> findByModel(String model,Pageable pageable);

    Page<AuditLog> getAuditLogsByActionType(String actionType,Pageable pageable);

    List<AuditLog> findByCreatedAt(LocalDateTime date);
    
    /**
     * Find audit logs by username
     */
    List<AuditLog> findByUsername(String username);
    
    /**
     * Find audit logs by username with pagination
     */
    Page<AuditLog> findByUsername(String username, Pageable pageable);
    
    /**
     * Find audit logs by operation type
     */
    List<AuditLog> findByOperationType(String operationType);
    
    /**
     * Find audit logs by operation type with pagination
     */
    Page<AuditLog> findByOperationType(String operationType, Pageable pageable);
    
    /**
     * Find audit logs by entity type
     */
    List<AuditLog> findByEntityType(String entityType);
    
    /**
     * Find audit logs by entity type with pagination
     */
    Page<AuditLog> findByEntityType(String entityType, Pageable pageable);
    
    /**
     * Find audit logs by entity type and entity ID
     */
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId);
    
    /**
     * Find audit logs by entity type and entity ID with pagination
     */
    Page<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId, Pageable pageable);
    
    /**
     * Find audit logs by status
     */
    List<AuditLog> findByStatus(String status);
    
    /**
     * Find audit logs by status with pagination
     */
    Page<AuditLog> findByStatus(String status, Pageable pageable);
    
    /**
     * Find audit logs by session ID
     */
    List<AuditLog> findBySessionId(String sessionId);
    
    /**
     * Find audit logs by created at time range
     */
    List<AuditLog> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Find audit logs by created at time range with pagination
     */
    Page<AuditLog> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search audit logs by action containing (case-insensitive)
     */
    Page<AuditLog> findByActionContainingIgnoreCase(String action, Pageable pageable);

    List<AuditLog> findByOrderByCreatedAtDesc(Pageable limit);

    @Query("DELETE FROM AuditLog al WHERE al.createdAt < :cutodDate")
   long deleteAuditLogByCreatedAtBefore(LocalDateTime cutodDate);




    List<AuditLog> findByAction(String action);

    /**
     * Search audit logs by entity name containing (case-insensitive)
     */
    Page<AuditLog> findByEntityNameContainingIgnoreCase(String entityName, Pageable pageable);
    
    /**
     * Search audit logs by multiple criteria
     */
    @Query("SELECT al FROM AuditLog al WHERE " +
           "(:userId IS NULL OR al.user.id = :userId) AND " +
           "(:username IS NULL OR LOWER(al.username) LIKE LOWER(CONCAT('%', :username, '%'))) AND " +
           "(:action IS NULL OR LOWER(al.action) LIKE LOWER(CONCAT('%', :action, '%'))) AND " +
           "(:entityType IS NULL OR al.entityType = :entityType) AND " +
           "(:operationType IS NULL OR al.operationType = :operationType) AND " +
           "(:status IS NULL OR al.status = :status) AND " +
           "(:startTime IS NULL OR al.createdAt >= :startTime) AND " +
           "(:endTime IS NULL OR al.createdAt <= :endTime)")
    Page<AuditLog> searchAuditLogs(
            @Param("userId") Long userId,
            @Param("username") String username,
            @Param("action") String action,
            @Param("entityType") String entityType,
            @Param("operationType") String operationType,
            @Param("status") String status,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count audit logs by user
     */
    long countByUserId(Long userId);
    
    /**
     * Count audit logs by operation type
     */
    long countByOperationType(String operationType);

    long countByCreatedAtBetweenAndUser_Id(LocalDateTime start,LocalDateTime stop,Long id);

    long countByAction(String action);
    long count();
    
    /**
     * Count audit logs by entity type
     */
    long countByEntityType(String entityType);
    
    /**
     * Count audit logs by status
     */
    long countByStatus(String status);
    
    /**
     * Count audit logs in time range
     */
    long countByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find CREATE operation logs
     */
    @Query("SELECT al FROM AuditLog al WHERE al.operationType = 'CREATE' ORDER BY al.createdAt DESC")
    List<AuditLog> findCreateOperationLogs();
    
    /**
     * Find UPDATE operation logs
     */
    @Query("SELECT al FROM AuditLog al WHERE al.operationType = 'UPDATE' ORDER BY al.createdAt DESC")
    List<AuditLog> findUpdateOperationLogs();
    
    /**
     * Find DELETE operation logs
     */
    @Query("SELECT al FROM AuditLog al WHERE al.operationType = 'DELETE' ORDER BY al.createdAt DESC")
    List<AuditLog> findDeleteOperationLogs();
    
    /**
     * Find VIEW operation logs
     */
    @Query("SELECT al FROM AuditLog al WHERE al.operationType = 'VIEW' ORDER BY al.createdAt DESC")
    List<AuditLog> findViewOperationLogs();
    
    /**
     * Find APPROVE operation logs
     */
    @Query("SELECT al FROM AuditLog al WHERE al.operationType = 'APPROVE' ORDER BY al.createdAt DESC")
    List<AuditLog> findApproveOperationLogs();
    
    /**
     * Find REJECT operation logs
     */
    @Query("SELECT al FROM AuditLog al WHERE al.operationType = 'REJECT' ORDER BY al.createdAt DESC")
    List<AuditLog> findRejectOperationLogs();
    
    /**
     * Find LOGIN operation logs
     */
    @Query("SELECT al FROM AuditLog al WHERE al.operationType = 'LOGIN' ORDER BY al.createdAt DESC")
    List<AuditLog> findLoginOperationLogs();
    
    /**
     * Find LOGOUT operation logs
     */
    @Query("SELECT al FROM AuditLog al WHERE al.operationType = 'LOGOUT' ORDER BY al.createdAt DESC")
    List<AuditLog> findLogoutOperationLogs();
    
    /**
     * Find successful operation logs
     */
    @Query("SELECT al FROM AuditLog al WHERE al.status = 'SUCCESS' ORDER BY al.createdAt DESC")
    List<AuditLog> findSuccessfulOperationLogs();
    
    /**
     * Find failed operation logs
     */
    @Query("SELECT al FROM AuditLog al WHERE al.status = 'FAILED' ORDER BY al.createdAt DESC")
    List<AuditLog> findFailedOperationLogs();
    
    /**
     * Find audit logs by user and operation type
     */
    List<AuditLog> findByUserIdAndOperationType(Long userId, String operationType);
    
    /**
     * Find audit logs by entity type and operation type
     */
    List<AuditLog> findByEntityTypeAndOperationType(String entityType, String operationType);
    
    /**
     * Find audit logs by IP address
     */
    List<AuditLog> findByIpAddress(String ipAddress);
    
    /**
     * Find recent audit logs for entity
     */
    @Query("SELECT al FROM AuditLog al WHERE al.entityType = :entityType AND al.entityId = :entityId " +
           "ORDER BY al.createdAt DESC")
    List<AuditLog> findRecentAuditLogsForEntity(
            @Param("entityType") String entityType, 
            @Param("entityId") Long entityId);
    
    /**
     * Get audit log statistics
     */
    @Query("SELECT " +
           "COUNT(al) as totalLogs, " +
           "SUM(CASE WHEN al.operationType = 'CREATE' THEN 1 ELSE 0 END) as createOps, " +
           "SUM(CASE WHEN al.operationType = 'UPDATE' THEN 1 ELSE 0 END) as updateOps, " +
           "SUM(CASE WHEN al.operationType = 'DELETE' THEN 1 ELSE 0 END) as deleteOps, " +
           "SUM(CASE WHEN al.operationType = 'VIEW' THEN 1 ELSE 0 END) as viewOps, " +
           "SUM(CASE WHEN al.status = 'SUCCESS' THEN 1 ELSE 0 END) as successLogs, " +
           "SUM(CASE WHEN al.status = 'FAILED' THEN 1 ELSE 0 END) as failedLogs " +
           "FROM AuditLog al")
    Object getAuditLogStatistics();
    
    /**
     * Get audit logs grouped by operation type
     */
    @Query("SELECT al.operationType, COUNT(al) as logCount " +
           "FROM AuditLog al GROUP BY al.operationType ORDER BY logCount DESC")
    List<Object[]> getAuditLogsByOperationType();
    
    /**
     * Get audit logs grouped by entity type
     */
    @Query("SELECT al.entityType, COUNT(al) as logCount " +
           "FROM AuditLog al GROUP BY al.entityType ORDER BY logCount DESC")
    List<Object[]> getAuditLogsByEntityType();
    
    /**
     * Get audit logs grouped by user
     */
    @Query("SELECT al.user.id, al.username, COUNT(al) as logCount " +
           "FROM AuditLog al GROUP BY al.user.id, al.username ORDER BY logCount DESC")
    List<Object[]> getAuditLogsByUser();
    
    /**
     * Get audit logs grouped by status
     */
    @Query("SELECT al.status, COUNT(al) as logCount " +
           "FROM AuditLog al GROUP BY al.status ORDER BY logCount DESC")
    List<Object[]> getAuditLogsByStatus();
    
    /**
     * Get daily audit log summary
     */
//    @Query("SELECT DATE(al.createdAt) as logDate, COUNT(al) as logCount, " +
//           "SUM(CASE WHEN al.status = 'SUCCESS' THEN 1 ELSE 0 END) as successCount, " +
//           "SUM(CASE WHEN al.status = 'FAILED' THEN 1 ELSE 0 END) as failedCount " +
//           "FROM AuditLog al WHERE al.createdAt BETWEEN :startTime AND :endTime " +
//           "GROUP BY DATE(al.createdAt) ORDER BY logDate DESC")
//    List<Object[]> getDailyAuditLogSummary(
//            @Param("startTime") LocalDateTime startTime,
//            @Param("endTime") LocalDateTime endTime);
//
    /**
     * Find today's audit logs
     */
    @Query("SELECT al FROM AuditLog al WHERE DATE(al.createdAt) = CURRENT_DATE ORDER BY al.createdAt DESC")
    List<AuditLog> findTodayAuditLogs();
    
    /**
     * Find all audit logs ordered by created at
     */
    List<AuditLog> findAllByOrderByCreatedAtDesc();
    
    /**
     * Get user activity timeline
     */
    @Query("SELECT al FROM AuditLog al WHERE al.user.id = :userId " +
           "AND al.createdAt BETWEEN :startTime AND :endTime " +
           "ORDER BY al.createdAt DESC")
    List<AuditLog> getUserActivityTimeline(
            @Param("userId") Long userId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * Get entity modification history
     */
    @Query("SELECT al FROM AuditLog al WHERE al.entityType = :entityType " +
           "AND al.entityId = :entityId AND al.operationType IN ('CREATE', 'UPDATE', 'DELETE') " +
           "ORDER BY al.createdAt ASC")
    List<AuditLog> getEntityModificationHistory(
            @Param("entityType") String entityType,
            @Param("entityId") Long entityId);
    
    /**
     * Find most active users
     */
    @Query("SELECT al.user.id, al.username, COUNT(al) as activityCount " +
           "FROM AuditLog al WHERE al.createdAt >= :sinceTime " +
           "GROUP BY al.user.id, al.username ORDER BY activityCount DESC")
    List<Object[]> findMostActiveUsers(@Param("sinceTime") LocalDateTime sinceTime);
}
