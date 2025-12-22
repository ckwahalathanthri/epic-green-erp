package lk.epicgreen.erp.audit.repository;

import lk.epicgreen.erp.audit.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AuditLog Repository
 * Repository for audit log data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find audit logs by user ID
     */
    List<AuditLog> findByUserId(Long userId);
    
    /**
     * Find audit logs by user ID with pagination
     */
    Page<AuditLog> findByUserId(Long userId, Pageable pageable);
    
    /**
     * Find audit logs by username
     */
    List<AuditLog> findByUsername(String username);
    
    /**
     * Find audit logs by username with pagination
     */
    Page<AuditLog> findByUsername(String username, Pageable pageable);
    
    /**
     * Find audit logs by module name
     */
    List<AuditLog> findByModuleName(String moduleName);
    
    /**
     * Find audit logs by module name with pagination
     */
    Page<AuditLog> findByModuleName(String moduleName, Pageable pageable);
    
    /**
     * Find audit logs by action type
     */
    List<AuditLog> findByActionType(String actionType);
    
    /**
     * Find audit logs by action type with pagination
     */
    Page<AuditLog> findByActionType(String actionType, Pageable pageable);
    
    /**
     * Find audit logs by entity type
     */
    List<AuditLog> findByEntityType(String entityType);
    
    /**
     * Find audit logs by entity type with pagination
     */
    Page<AuditLog> findByEntityType(String entityType, Pageable pageable);
    
    /**
     * Find audit logs by IP address
     */
    List<AuditLog> findByIpAddress(String ipAddress);
    
    /**
     * Find audit logs by IP address with pagination
     */
    Page<AuditLog> findByIpAddress(String ipAddress, Pageable pageable);
    
    /**
     * Find audit logs by success status
     */
    List<AuditLog> findByIsSuccessful(Boolean isSuccessful);
    
    /**
     * Find audit logs by success status with pagination
     */
    Page<AuditLog> findByIsSuccessful(Boolean isSuccessful, Pageable pageable);
    
    // ===================================================================
    // FIND BY ENTITY
    // ===================================================================
    
    /**
     * Find audit logs by entity type and entity ID
     */
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId);
    
    /**
     * Find audit logs by entity type and entity ID with pagination
     */
    Page<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId, Pageable pageable);
    
    /**
     * Find audit logs by entity type and entity ID ordered by action timestamp descending
     */
    List<AuditLog> findByEntityTypeAndEntityIdOrderByActionTimestampDesc(String entityType, Long entityId);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find audit logs by action timestamp between dates
     */
    List<AuditLog> findByActionTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find audit logs by action timestamp between dates with pagination
     */
    Page<AuditLog> findByActionTimestampBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Find audit logs by user ID and action timestamp between dates
     */
    List<AuditLog> findByUserIdAndActionTimestampBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find audit logs by user ID and action timestamp between dates with pagination
     */
    Page<AuditLog> findByUserIdAndActionTimestampBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Find audit logs by module name and action timestamp between dates
     */
    List<AuditLog> findByModuleNameAndActionTimestampBetween(String moduleName, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find audit logs by module name and action timestamp between dates with pagination
     */
    Page<AuditLog> findByModuleNameAndActionTimestampBetween(String moduleName, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Find audit logs by action type and action timestamp between dates
     */
    List<AuditLog> findByActionTypeAndActionTimestampBetween(String actionType, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find audit logs by success status and action timestamp between dates
     */
    List<AuditLog> findByIsSuccessfulAndActionTimestampBetween(Boolean isSuccessful, LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find audit logs by module name and action type
     */
    List<AuditLog> findByModuleNameAndActionType(String moduleName, String actionType);
    
    /**
     * Find audit logs by module name and action type with pagination
     */
    Page<AuditLog> findByModuleNameAndActionType(String moduleName, String actionType, Pageable pageable);
    
    /**
     * Find audit logs by user ID and action type
     */
    List<AuditLog> findByUserIdAndActionType(Long userId, String actionType);
    
    /**
     * Find audit logs by user ID and module name
     */
    List<AuditLog> findByUserIdAndModuleName(Long userId, String moduleName);
    
    /**
     * Find audit logs by user ID, module name, and action type
     */
    List<AuditLog> findByUserIdAndModuleNameAndActionType(Long userId, String moduleName, String actionType);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Count audit logs by user ID
     */
    @Query("SELECT COUNT(a) FROM AuditLog a WHERE a.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    /**
     * Count audit logs by module name
     */
    @Query("SELECT COUNT(a) FROM AuditLog a WHERE a.moduleName = :moduleName")
    Long countByModuleName(@Param("moduleName") String moduleName);
    
    /**
     * Count audit logs by action type
     */
    @Query("SELECT COUNT(a) FROM AuditLog a WHERE a.actionType = :actionType")
    Long countByActionType(@Param("actionType") String actionType);
    
    /**
     * Count failed actions
     */
    @Query("SELECT COUNT(a) FROM AuditLog a WHERE a.isSuccessful = false")
    Long countFailedActions();
    
    /**
     * Get total execution time by module
     */
    @Query("SELECT SUM(a.executionTimeMillis) FROM AuditLog a WHERE a.moduleName = :moduleName")
    Long getTotalExecutionTimeByModule(@Param("moduleName") String moduleName);
    
    /**
     * Get average execution time by module
     */
    @Query("SELECT AVG(a.executionTimeMillis) FROM AuditLog a WHERE a.moduleName = :moduleName")
    Double getAverageExecutionTimeByModule(@Param("moduleName") String moduleName);
    
    /**
     * Find slow queries (execution time > threshold)
     */
    @Query("SELECT a FROM AuditLog a WHERE a.executionTimeMillis > :thresholdMillis ORDER BY a.executionTimeMillis DESC")
    List<AuditLog> findSlowQueries(@Param("thresholdMillis") Long thresholdMillis);
    
    /**
     * Find slow queries with pagination
     */
    @Query("SELECT a FROM AuditLog a WHERE a.executionTimeMillis > :thresholdMillis ORDER BY a.executionTimeMillis DESC")
    Page<AuditLog> findSlowQueries(@Param("thresholdMillis") Long thresholdMillis, Pageable pageable);
    
    /**
     * Get most active users
     */
    @Query("SELECT a.username, COUNT(a) as actionCount FROM AuditLog a " +
           "GROUP BY a.username ORDER BY actionCount DESC")
    List<Object[]> getMostActiveUsers();
    
    /**
     * Get most used modules
     */
    @Query("SELECT a.moduleName, COUNT(a) as actionCount FROM AuditLog a " +
           "GROUP BY a.moduleName ORDER BY actionCount DESC")
    List<Object[]> getMostUsedModules();
    
    /**
     * Get action type distribution
     */
    @Query("SELECT a.actionType, COUNT(a) as actionCount FROM AuditLog a " +
           "GROUP BY a.actionType ORDER BY actionCount DESC")
    List<Object[]> getActionTypeDistribution();
    
    /**
     * Get hourly activity
     */
    @Query("SELECT HOUR(a.actionTimestamp) as hour, COUNT(a) as actionCount FROM AuditLog a " +
           "WHERE a.actionTimestamp BETWEEN :startDate AND :endDate " +
           "GROUP BY HOUR(a.actionTimestamp) ORDER BY hour")
    List<Object[]> getHourlyActivity(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);
    
    /**
     * Get daily activity
     */
    @Query("SELECT DATE(a.actionTimestamp) as date, COUNT(a) as actionCount FROM AuditLog a " +
           "WHERE a.actionTimestamp BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE(a.actionTimestamp) ORDER BY date")
    List<Object[]> getDailyActivity(@Param("startDate") LocalDateTime startDate, 
                                    @Param("endDate") LocalDateTime endDate);
    
    /**
     * Search audit logs
     */
    @Query("SELECT a FROM AuditLog a WHERE " +
           "LOWER(a.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.moduleName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.actionType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.actionDescription) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.entityType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.entityName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<AuditLog> searchAuditLogs(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find recent audit logs
     */
    @Query("SELECT a FROM AuditLog a ORDER BY a.actionTimestamp DESC")
    List<AuditLog> findRecentAuditLogs(Pageable pageable);
    
    /**
     * Find audit logs by user and date range
     */
    @Query("SELECT a FROM AuditLog a WHERE a.userId = :userId " +
           "AND a.actionTimestamp BETWEEN :startDate AND :endDate " +
           "ORDER BY a.actionTimestamp DESC")
    List<AuditLog> findByUserAndDateRange(@Param("userId") Long userId,
                                          @Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);
    
    /**
     * Get IP addresses by user
     */
    @Query("SELECT DISTINCT a.ipAddress FROM AuditLog a WHERE a.userId = :userId")
    List<String> getIpAddressesByUser(@Param("userId") Long userId);
    
    /**
     * Get user sessions
     */
    @Query("SELECT DISTINCT a.sessionId FROM AuditLog a WHERE a.userId = :userId " +
           "AND a.actionTimestamp BETWEEN :startDate AND :endDate")
    List<String> getUserSessions(@Param("userId") Long userId,
                                 @Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate);
    
    /**
     * Delete old audit logs
     */
    @Query("DELETE FROM AuditLog a WHERE a.actionTimestamp < :beforeDate")
    void deleteOldAuditLogs(@Param("beforeDate") LocalDateTime beforeDate);

    /**
     * Counts audit logs by entity
     *
     * @param entityName Entity name
     * @param entityId Entity ID
     * @return Count
     */
    long countByEntityNameAndEntityId(String entityName, String entityId);

    /**
     * Counts audit logs by user in date range
     *
     * @param performedBy Username
     * @param startDate Start date
     * @param endDate End date
     * @return Count
     */
    long countByPerformedByAndTimestampBetween(
            String performedBy,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    /**
     * Finds audit logs with search criteria
     *
     * @param entityName Entity name (optional)
     * @param action Action (optional)
     * @param performedBy User (optional)
     * @param startDate Start date (optional)
     * @param endDate End date (optional)
     * @param pageable Pagination
     * @return Page of audit logs
     */
    @Query("SELECT a FROM AuditLog a WHERE " +
            "(:entityName IS NULL OR a.entityName = :entityName) AND " +
            "(:action IS NULL OR a.action = :action) AND " +
            "(:performedBy IS NULL OR a.performedBy = :performedBy) AND " +
            "(:startDate IS NULL OR a.timestamp >= :startDate) AND " +
            "(:endDate IS NULL OR a.timestamp <= :endDate) " +
            "ORDER BY a.timestamp DESC")
    Page<AuditLog> search(
            @Param("entityName") String entityName,
            @Param("action") String action,
            @Param("performedBy") String performedBy,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    /**
     * Deletes old audit logs
     *
     * @param beforeDate Date before which to delete
     * @return Number of deleted records
     */
    long deleteByTimestampBefore(LocalDateTime beforeDate);
}
