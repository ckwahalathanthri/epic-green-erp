package lk.epicgreen.erp.audit.repository;

import lk.epicgreen.erp.audit.entity.ErrorLog;
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
 * Repository interface for ErrorLog entity
 * Based on ACTUAL database schema: error_logs table
 * 
 * Fields: error_type, error_code, error_message, stack_trace,
 *         request_url, request_method, request_body,
 *         user_id (BIGINT), ip_address, user_agent,
 *         severity (ENUM: LOW, MEDIUM, HIGH, CRITICAL),
 *         is_resolved, resolved_by (BIGINT), resolved_at, created_at
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long>, JpaSpecificationExecutor<ErrorLog> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find error logs by error type
     */
    List<ErrorLog> findByErrorType(String errorType);
    List<ErrorLog> findBySeverityAndIsResolvedFalse(String severity);

    /**
     * Find error logs by error type with pagination
     */
    Page<ErrorLog> findByErrorType(String errorType, Pageable pageable);
    
    /**
     * Find error logs by error code
     */
    List<ErrorLog> findByErrorCode(String errorCode);
    
    /**
     * Find error logs by severity
     */
    List<ErrorLog> findBySeverity(String severity);
    
    /**
     * Find error logs by severity with pagination
     */
    Page<ErrorLog> findBySeverity(String severity, Pageable pageable);
    
    /**
     * Find error logs by user
     */
    List<ErrorLog> findByUserId(Long userId);
    
    /**
     * Find error logs by user with pagination
     */
    Page<ErrorLog> findByUserId(Long userId, Pageable pageable);
    
    /**
     * Find unresolved error logs
     */
    List<ErrorLog> findByIsResolvedFalse();
    
    /**
     * Find unresolved error logs with pagination
     */
    Page<ErrorLog> findByIsResolvedFalse(Pageable pageable);
    
    /**
     * Find resolved error logs
     */
    List<ErrorLog> findByIsResolvedTrue();
    
    /**
     * Find resolved error logs with pagination
     */
    Page<ErrorLog> findByIsResolvedTrue(Pageable pageable);
    
    /**
     * Find error logs by resolved by user
     */
    List<ErrorLog> findByResolvedBy(Long resolvedBy);
    
    /**
     * Find error logs by created at time range
     */
    List<ErrorLog> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Find error logs by created at time range with pagination
     */
    Page<ErrorLog> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search error logs by error message containing (case-insensitive)
     */
    Page<ErrorLog> findByErrorMessageContainingIgnoreCase(String errorMessage, Pageable pageable);
    
    /**
     * Search error logs by request URL containing (case-insensitive)
     */
    Page<ErrorLog> findByRequestUrlContainingIgnoreCase(String requestUrl, Pageable pageable);
    
    /**
     * Search error logs by multiple criteria
     */
    @Query("SELECT el FROM ErrorLog el WHERE " +
           "(:errorType IS NULL OR LOWER(el.errorType) LIKE LOWER(CONCAT('%', :errorType, '%'))) AND " +
           "(:errorCode IS NULL OR el.errorCode = :errorCode) AND " +
           "(:severity IS NULL OR el.severity = :severity) AND " +
           "(:isResolved IS NULL OR el.isResolved = :isResolved) AND " +
           "(:userId IS NULL OR el.user.id = :userId) AND " +
           "(:startTime IS NULL OR el.createdAt >= :startTime) AND " +
           "(:endTime IS NULL OR el.createdAt <= :endTime)")
    Page<ErrorLog> searchErrorLogs(
            @Param("errorType") String errorType,
            @Param("errorCode") String errorCode,
            @Param("severity") String severity,
            @Param("isResolved") Boolean isResolved,
            @Param("userId") Long userId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count error logs by error type
     */
    long countByErrorType(String errorType);
    
    /**
     * Count error logs by severity
     */
    long countBySeverity(String severity);
    
    /**
     * Count unresolved error logs
     */
    long countByIsResolvedFalse();
    
    /**
     * Count resolved error logs
     */
    long countByIsResolvedTrue();
    
    /**
     * Count error logs in time range
     */
    long countByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find LOW severity errors
     */
    @Query("SELECT el FROM ErrorLog el WHERE el.severity = 'LOW' ORDER BY el.createdAt DESC")
    List<ErrorLog> findLowSeverityErrors();
    
    /**
     * Find MEDIUM severity errors
     */
    @Query("SELECT el FROM ErrorLog el WHERE el.severity = 'MEDIUM' ORDER BY el.createdAt DESC")
    List<ErrorLog> findMediumSeverityErrors();
    
    /**
     * Find HIGH severity errors
     */
    @Query("SELECT el FROM ErrorLog el WHERE el.severity = 'HIGH' ORDER BY el.createdAt DESC")
    List<ErrorLog> findHighSeverityErrors();
    
    /**
     * Find CRITICAL severity errors
     */
    @Query("SELECT el FROM ErrorLog el WHERE el.severity = 'CRITICAL' ORDER BY el.createdAt DESC")
    List<ErrorLog> findCriticalSeverityErrors();
    
    /**
     * Find unresolved critical errors
     */
    @Query("SELECT el FROM ErrorLog el WHERE el.severity = 'CRITICAL' AND el.isResolved = false " +
           "ORDER BY el.createdAt DESC")
    List<ErrorLog> findUnresolvedCriticalErrors();
    
    /**
     * Find unresolved high severity errors
     */
    @Query("SELECT el FROM ErrorLog el WHERE el.severity = 'HIGH' AND el.isResolved = false " +
           "ORDER BY el.createdAt DESC")
    List<ErrorLog> findUnresolvedHighSeverityErrors();
    
    /**
     * Find error logs by severity and resolution status
     */
    List<ErrorLog> findBySeverityAndIsResolved(String severity, Boolean isResolved);
    
    /**
     * Find error logs by request method
     */
    List<ErrorLog> findByRequestMethod(String requestMethod);
    
    /**
     * Find error logs by IP address
     */
    List<ErrorLog> findByIpAddress(String ipAddress);
    
    /**
     * Find errors resolved in time range
     */
    @Query("SELECT el FROM ErrorLog el WHERE el.resolvedAt BETWEEN :startTime AND :endTime " +
           "ORDER BY el.resolvedAt DESC")
    List<ErrorLog> findErrorsResolvedBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * Get error log statistics
     */
    @Query("SELECT " +
           "COUNT(el) as totalErrors, " +
           "SUM(CASE WHEN el.severity = 'LOW' THEN 1 ELSE 0 END) as lowSeverityErrors, " +
           "SUM(CASE WHEN el.severity = 'MEDIUM' THEN 1 ELSE 0 END) as mediumSeverityErrors, " +
           "SUM(CASE WHEN el.severity = 'HIGH' THEN 1 ELSE 0 END) as highSeverityErrors, " +
           "SUM(CASE WHEN el.severity = 'CRITICAL' THEN 1 ELSE 0 END) as criticalSeverityErrors, " +
           "SUM(CASE WHEN el.isResolved = true THEN 1 ELSE 0 END) as resolvedErrors, " +
           "SUM(CASE WHEN el.isResolved = false THEN 1 ELSE 0 END) as unresolvedErrors " +
           "FROM ErrorLog el")
    Object getErrorLogStatistics();
    
    /**
     * Get error logs grouped by error type
     */
    @Query("SELECT el.errorType, COUNT(el) as errorCount, " +
           "SUM(CASE WHEN el.isResolved = false THEN 1 ELSE 0 END) as unresolvedCount " +
           "FROM ErrorLog el GROUP BY el.errorType ORDER BY errorCount DESC")
    List<Object[]> getErrorLogsByType();
    
    /**
     * Get error logs grouped by severity
     */
    @Query("SELECT el.severity, COUNT(el) as errorCount, " +
           "SUM(CASE WHEN el.isResolved = false THEN 1 ELSE 0 END) as unresolvedCount " +
           "FROM ErrorLog el GROUP BY el.severity ORDER BY errorCount DESC")
    List<Object[]> getErrorLogsBySeverity();
    
    /**
     * Get error logs grouped by error code
     */
    @Query("SELECT el.errorCode, COUNT(el) as errorCount " +
           "FROM ErrorLog el WHERE el.errorCode IS NOT NULL " +
           "GROUP BY el.errorCode ORDER BY errorCount DESC")
    List<Object[]> getErrorLogsByErrorCode();
    
    /**
     * Get daily error log summary
     */
//    @Query("SELECT DATE(el.createdAt) as errorDate, COUNT(el) as errorCount, " +
//           "SUM(CASE WHEN el.severity = 'CRITICAL' THEN 1 ELSE 0 END) as criticalCount, " +
//           "SUM(CASE WHEN el.isResolved = true THEN 1 ELSE 0 END) as resolvedCount " +
//           "FROM ErrorLog el WHERE el.createdAt BETWEEN :startTime AND :endTime " +
//           "GROUP BY DATE(el.createdAt) ORDER BY errorDate DESC")
//    List<Object[]> getDailyErrorLogSummary(
//            @Param("startTime") LocalDateTime startTime,
//            @Param("endTime") LocalDateTime endTime);
    
    /**
     * Find today's error logs
     */
    @Query("SELECT el FROM ErrorLog el WHERE DATE(el.createdAt) = CURRENT_DATE ORDER BY el.createdAt DESC")
    List<ErrorLog> findTodayErrorLogs();
    
    /**
     * Find all error logs ordered by created at
     */
    List<ErrorLog> findAllByOrderByCreatedAtDesc();
    
    /**
     * Find recent errors by type
     */
//    @Query("SELECT el FROM ErrorLog el WHERE el.errorType = :errorType " +
//           "ORDER BY el.createdAt DESC LIMIT :limit")
//    List<ErrorLog> findRecentErrorsByType(
//            @Param("errorType") String errorType,
//            @Param("limit") int limit);
    
    /**
     * Get most common errors
     */
    @Query("SELECT el.errorType, el.errorMessage, COUNT(el) as occurrenceCount " +
           "FROM ErrorLog el WHERE el.createdAt >= :sinceTime " +
           "GROUP BY el.errorType, el.errorMessage ORDER BY occurrenceCount DESC")
    List<Object[]> getMostCommonErrors(@Param("sinceTime") LocalDateTime sinceTime);
    
    /**
     * Get error resolution rate
     */
    @Query("SELECT " +
           "COUNT(el) as totalErrors, " +
           "SUM(CASE WHEN el.isResolved = true THEN 1 ELSE 0 END) as resolvedErrors, " +
           "(SUM(CASE WHEN el.isResolved = true THEN 1 ELSE 0 END) * 100.0 / COUNT(el)) as resolutionRate " +
           "FROM ErrorLog el WHERE el.createdAt >= :sinceTime")
    Object getErrorResolutionRate(@Param("sinceTime") LocalDateTime sinceTime);
    
    /**
     * Find errors by request URL pattern
     */
    @Query("SELECT el FROM ErrorLog el WHERE LOWER(el.requestUrl) LIKE LOWER(CONCAT('%', :urlPattern, '%')) " +
           "ORDER BY el.createdAt DESC")
    List<ErrorLog> findErrorsByRequestUrlPattern(@Param("urlPattern") String urlPattern);
}
