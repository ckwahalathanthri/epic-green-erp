package lk.epicgreen.erp.audit.repository;

import lk.epicgreen.erp.audit.entity.ErrorLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ErrorLog Repository
 * Repository for error log data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find error logs by severity level
     */
    List<ErrorLog> findBySeverityLevel(String severityLevel);
    
    /**
     * Find error logs by severity level with pagination
     */
    Page<ErrorLog> findBySeverityLevel(String severityLevel, Pageable pageable);
    
    /**
     * Find error logs by error type
     */
    List<ErrorLog> findByErrorType(String errorType);
    
    /**
     * Find error logs by error type with pagination
     */
    Page<ErrorLog> findByErrorType(String errorType, Pageable pageable);
    
    /**
     * Find error logs by status
     */
    List<ErrorLog> findByStatus(String status);
    
    /**
     * Find error logs by status with pagination
     */
    Page<ErrorLog> findByStatus(String status, Pageable pageable);
    
    /**
     * Find error logs by module name
     */
    List<ErrorLog> findByModuleName(String moduleName);
    
    /**
     * Find error logs by module name with pagination
     */
    Page<ErrorLog> findByModuleName(String moduleName, Pageable pageable);
    
    /**
     * Find error logs by user ID
     */
    List<ErrorLog> findByUserId(Long userId);
    
    /**
     * Find error logs by user ID with pagination
     */
    Page<ErrorLog> findByUserId(Long userId, Pageable pageable);
    
    /**
     * Find error logs by assigned user
     */
    List<ErrorLog> findByAssignedTo(Long assignedTo);
    
    /**
     * Find error logs by assigned user with pagination
     */
    Page<ErrorLog> findByAssignedTo(Long assignedTo, Pageable pageable);
    
    /**
     * Find error logs by environment
     */
    List<ErrorLog> findByEnvironment(String environment);
    
    /**
     * Find error logs by environment with pagination
     */
    Page<ErrorLog> findByEnvironment(String environment, Pageable pageable);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find error logs by error timestamp between dates
     */
    List<ErrorLog> findByErrorTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find error logs by error timestamp between dates with pagination
     */
    Page<ErrorLog> findByErrorTimestampBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Find error logs by severity and date range
     */
    List<ErrorLog> findBySeverityLevelAndErrorTimestampBetween(
        String severityLevel, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find error logs by status and date range
     */
    List<ErrorLog> findByStatusAndErrorTimestampBetween(
        String status, LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find error logs by severity and status
     */
    List<ErrorLog> findBySeverityLevelAndStatus(String severityLevel, String status);
    
    /**
     * Find error logs by severity and status with pagination
     */
    Page<ErrorLog> findBySeverityLevelAndStatus(String severityLevel, String status, Pageable pageable);
    
    /**
     * Find error logs by module and severity
     */
    List<ErrorLog> findByModuleNameAndSeverityLevel(String moduleName, String severityLevel);
    
    /**
     * Find error logs by error type and status
     */
    List<ErrorLog> findByErrorTypeAndStatus(String errorType, String status);
    
    // ===================================================================
    // NOTIFICATION QUERIES
    // ===================================================================
    
    /**
     * Find unnotified errors
     */
    List<ErrorLog> findByIsNotified(Boolean isNotified);
    
    /**
     * Find unnotified critical errors
     */
    @Query("SELECT e FROM ErrorLog e WHERE e.isNotified = false AND e.severityLevel = 'CRITICAL' " +
           "ORDER BY e.errorTimestamp DESC")
    List<ErrorLog> findUnnotifiedCriticalErrors();
    
    /**
     * Find unnotified high severity errors
     */
    @Query("SELECT e FROM ErrorLog e WHERE e.isNotified = false " +
           "AND (e.severityLevel = 'CRITICAL' OR e.severityLevel = 'HIGH') " +
           "ORDER BY e.errorTimestamp DESC")
    List<ErrorLog> findUnnotifiedHighSeverityErrors();
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count errors by severity level
     */
    @Query("SELECT COUNT(e) FROM ErrorLog e WHERE e.severityLevel = :severityLevel")
    Long countBySeverityLevel(@Param("severityLevel") String severityLevel);
    
    /**
     * Count errors by status
     */
    @Query("SELECT COUNT(e) FROM ErrorLog e WHERE e.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count errors by module
     */
    @Query("SELECT COUNT(e) FROM ErrorLog e WHERE e.moduleName = :moduleName")
    Long countByModule(@Param("moduleName") String moduleName);
    
    /**
     * Count unresolved errors
     */
    @Query("SELECT COUNT(e) FROM ErrorLog e WHERE e.status IN ('NEW', 'ACKNOWLEDGED', 'IN_PROGRESS')")
    Long countUnresolvedErrors();
    
    /**
     * Get error severity distribution
     */
    @Query("SELECT e.severityLevel, COUNT(e) as errorCount FROM ErrorLog e " +
           "GROUP BY e.severityLevel ORDER BY errorCount DESC")
    List<Object[]> getErrorSeverityDistribution();
    
    /**
     * Get error type distribution
     */
    @Query("SELECT e.errorType, COUNT(e) as errorCount FROM ErrorLog e " +
           "GROUP BY e.errorType ORDER BY errorCount DESC")
    List<Object[]> getErrorTypeDistribution();
    
    /**
     * Get error status distribution
     */
    @Query("SELECT e.status, COUNT(e) as errorCount FROM ErrorLog e " +
           "GROUP BY e.status ORDER BY errorCount DESC")
    List<Object[]> getErrorStatusDistribution();
    
    /**
     * Get errors by module
     */
    @Query("SELECT e.moduleName, COUNT(e) as errorCount FROM ErrorLog e " +
           "GROUP BY e.moduleName ORDER BY errorCount DESC")
    List<Object[]> getErrorsByModule();
    
    /**
     * Get hourly error count
     */
    @Query("SELECT HOUR(e.errorTimestamp) as hour, COUNT(e) as errorCount FROM ErrorLog e " +
           "WHERE e.errorTimestamp BETWEEN :startDate AND :endDate " +
           "GROUP BY HOUR(e.errorTimestamp) ORDER BY hour")
    List<Object[]> getHourlyErrorCount(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);
    
    /**
     * Get daily error count
     */
    @Query("SELECT DATE(e.errorTimestamp) as date, COUNT(e) as errorCount FROM ErrorLog e " +
           "WHERE e.errorTimestamp BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE(e.errorTimestamp) ORDER BY date")
    List<Object[]> getDailyErrorCount(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);
    
    /**
     * Get top exceptions
     */
    @Query("SELECT e.exceptionClass, COUNT(e) as occurrenceCount FROM ErrorLog e " +
           "GROUP BY e.exceptionClass ORDER BY occurrenceCount DESC")
    List<Object[]> getTopExceptions();
    
    /**
     * Get most error-prone modules
     */
    @Query("SELECT e.moduleName, COUNT(e) as errorCount FROM ErrorLog e " +
           "WHERE e.severityLevel IN ('CRITICAL', 'HIGH') " +
           "GROUP BY e.moduleName ORDER BY errorCount DESC")
    List<Object[]> getMostErrorProneModules();
    
    /**
     * Get average resolution time
     */
    @Query("SELECT AVG(TIMESTAMPDIFF(MINUTE, e.errorTimestamp, e.resolvedAt)) FROM ErrorLog e " +
           "WHERE e.resolvedAt IS NOT NULL")
    Double getAverageResolutionTimeMinutes();
    
    /**
     * Get errors with high occurrence count
     */
    @Query("SELECT e FROM ErrorLog e WHERE e.occurrenceCount > :threshold " +
           "ORDER BY e.occurrenceCount DESC")
    List<ErrorLog> getFrequentErrors(@Param("threshold") Integer threshold);
    
    // ===================================================================
    // SEARCH
    // ===================================================================
    
    /**
     * Search error logs
     */
    @Query("SELECT e FROM ErrorLog e WHERE " +
           "LOWER(e.errorMessage) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.exceptionClass) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.moduleName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.className) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.methodName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ErrorLog> searchErrorLogs(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find recent error logs
     */
    @Query("SELECT e FROM ErrorLog e ORDER BY e.errorTimestamp DESC")
    List<ErrorLog> findRecentErrorLogs(Pageable pageable);
    
    /**
     * Find errors by exception class
     */
    List<ErrorLog> findByExceptionClass(String exceptionClass);
    
    /**
     * Find errors by exception class ordered by timestamp
     */
    List<ErrorLog> findByExceptionClassOrderByErrorTimestampDesc(String exceptionClass);
    
    /**
     * Delete old error logs
     */
    @Query("DELETE FROM ErrorLog e WHERE e.errorTimestamp < :beforeDate AND e.status = 'CLOSED'")
    void deleteOldErrorLogs(@Param("beforeDate") LocalDateTime beforeDate);
}
