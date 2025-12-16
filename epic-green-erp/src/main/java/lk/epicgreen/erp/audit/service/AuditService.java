package lk.epicgreen.erp.audit.service;

import lk.epicgreen.erp.audit.entity.ActivityLog;
import lk.epicgreen.erp.audit.entity.AuditLog;
import lk.epicgreen.erp.audit.entity.ErrorLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Audit Service Interface
 * Service for audit log operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface AuditService {
    
    // ===================================================================
    // AUDIT LOG OPERATIONS
    // ===================================================================
    
    /**
     * Create audit log
     */
    AuditLog createAuditLog(AuditLog auditLog);
    
    /**
     * Get audit log by ID
     */
    AuditLog getAuditLogById(Long id);
    
    /**
     * Get all audit logs
     */
    List<AuditLog> getAllAuditLogs();
    
    /**
     * Get all audit logs with pagination
     */
    Page<AuditLog> getAllAuditLogs(Pageable pageable);
    
    /**
     * Get audit logs by user ID
     */
    List<AuditLog> getAuditLogsByUserId(Long userId);
    
    /**
     * Get audit logs by user ID with pagination
     */
    Page<AuditLog> getAuditLogsByUserId(Long userId, Pageable pageable);
    
    /**
     * Get audit logs by username
     */
    List<AuditLog> getAuditLogsByUsername(String username);
    
    /**
     * Get audit logs by username with pagination
     */
    Page<AuditLog> getAuditLogsByUsername(String username, Pageable pageable);
    
    /**
     * Get audit logs by module name
     */
    List<AuditLog> getAuditLogsByModule(String moduleName);
    
    /**
     * Get audit logs by module name with pagination
     */
    Page<AuditLog> getAuditLogsByModule(String moduleName, Pageable pageable);
    
    /**
     * Get audit logs by action type
     */
    List<AuditLog> getAuditLogsByActionType(String actionType);
    
    /**
     * Get audit logs by action type with pagination
     */
    Page<AuditLog> getAuditLogsByActionType(String actionType, Pageable pageable);
    
    /**
     * Get audit logs by entity
     */
    List<AuditLog> getAuditLogsByEntity(String entityType, Long entityId);
    
    /**
     * Get audit logs by entity with pagination
     */
    Page<AuditLog> getAuditLogsByEntity(String entityType, Long entityId, Pageable pageable);
    
    /**
     * Get entity history (audit trail)
     */
    List<AuditLog> getEntityHistory(String entityType, Long entityId);
    
    /**
     * Get audit logs by date range
     */
    List<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get audit logs by date range with pagination
     */
    Page<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Get audit logs by date (today, yesterday, this week, etc.)
     */
    List<AuditLog> getAuditLogsByDate(LocalDate date);
    
    /**
     * Get today's audit logs
     */
    List<AuditLog> getTodayAuditLogs();
    
    /**
     * Get failed audit logs
     */
    List<AuditLog> getFailedAuditLogs();
    
    /**
     * Get failed audit logs with pagination
     */
    Page<AuditLog> getFailedAuditLogs(Pageable pageable);
    
    /**
     * Search audit logs
     */
    Page<AuditLog> searchAuditLogs(String keyword, Pageable pageable);
    
    /**
     * Get recent audit logs
     */
    List<AuditLog> getRecentAuditLogs(int limit);
    
    /**
     * Get slow queries
     */
    List<AuditLog> getSlowQueries(Long thresholdMillis);
    
    /**
     * Get slow queries with pagination
     */
    Page<AuditLog> getSlowQueries(Long thresholdMillis, Pageable pageable);
    
    /**
     * Delete old audit logs
     */
    void deleteOldAuditLogs(int daysToKeep);
    
    // ===================================================================
    // ACTIVITY LOG OPERATIONS
    // ===================================================================
    
    /**
     * Create activity log
     */
    ActivityLog createActivityLog(ActivityLog activityLog);
    
    /**
     * Get activity log by ID
     */
    ActivityLog getActivityLogById(Long id);
    
    /**
     * Get all activity logs
     */
    List<ActivityLog> getAllActivityLogs();
    
    /**
     * Get all activity logs with pagination
     */
    Page<ActivityLog> getAllActivityLogs(Pageable pageable);
    
    /**
     * Get activity logs by user ID
     */
    List<ActivityLog> getActivityLogsByUserId(Long userId);
    
    /**
     * Get activity logs by user ID with pagination
     */
    Page<ActivityLog> getActivityLogsByUserId(Long userId, Pageable pageable);
    
    /**
     * Get activity logs by activity type
     */
    List<ActivityLog> getActivityLogsByType(String activityType);
    
    /**
     * Get activity logs by activity type with pagination
     */
    Page<ActivityLog> getActivityLogsByType(String activityType, Pageable pageable);
    
    /**
     * Get activity logs by session ID
     */
    List<ActivityLog> getActivityLogsBySessionId(String sessionId);
    
    /**
     * Get login activities by user ID
     */
    List<ActivityLog> getLoginActivitiesByUserId(Long userId);
    
    /**
     * Get last login by user ID
     */
    ActivityLog getLastLoginByUserId(Long userId);
    
    /**
     * Get failed login attempts
     */
    List<ActivityLog> getFailedLoginAttempts();
    
    /**
     * Get failed login attempts with pagination
     */
    Page<ActivityLog> getFailedLoginAttempts(Pageable pageable);
    
    /**
     * Get failed login attempts by user ID
     */
    List<ActivityLog> getFailedLoginAttemptsByUserId(Long userId);
    
    /**
     * Get page views by user ID
     */
    List<ActivityLog> getPageViewsByUserId(Long userId);
    
    /**
     * Get page views by user ID with pagination
     */
    Page<ActivityLog> getPageViewsByUserId(Long userId, Pageable pageable);
    
    /**
     * Search activity logs
     */
    Page<ActivityLog> searchActivityLogs(String keyword, Pageable pageable);
    
    /**
     * Get recent activity logs
     */
    List<ActivityLog> getRecentActivityLogs(int limit);
    
    /**
     * Get current user sessions
     */
    List<ActivityLog> getCurrentUserSessions();
    
    /**
     * Delete old activity logs
     */
    void deleteOldActivityLogs(int daysToKeep);
    
    // ===================================================================
    // STATISTICS AND REPORTS
    // ===================================================================
    
    /**
     * Get audit statistics
     */
    Map<String, Object> getAuditStatistics();
    
    /**
     * Get audit statistics by date range
     */
    Map<String, Object> getAuditStatistics(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get activity statistics
     */
    Map<String, Object> getActivityStatistics();
    
    /**
     * Get activity statistics by date range
     */
    Map<String, Object> getActivityStatistics(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get user activity summary
     */
    Map<String, Object> getUserActivitySummary(Long userId);
    
    /**
     * Get user activity summary by date range
     */
    Map<String, Object> getUserActivitySummary(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get most active users
     */
    List<Map<String, Object>> getMostActiveUsers();
    
    /**
     * Get most active users by date range
     */
    List<Map<String, Object>> getMostActiveUsers(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get most used modules
     */
    List<Map<String, Object>> getMostUsedModules();
    
    /**
     * Get action type distribution
     */
    List<Map<String, Object>> getActionTypeDistribution();
    
    /**
     * Get activity type distribution
     */
    List<Map<String, Object>> getActivityTypeDistribution();
    
    /**
     * Get device type distribution
     */
    List<Map<String, Object>> getDeviceTypeDistribution();
    
    /**
     * Get most viewed pages
     */
    List<Map<String, Object>> getMostViewedPages();
    
    /**
     * Get most viewed pages by date range
     */
    List<Map<String, Object>> getMostViewedPages(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get hourly activity
     */
    List<Map<String, Object>> getHourlyActivity(LocalDate date);
    
    /**
     * Get daily activity
     */
    List<Map<String, Object>> getDailyActivity(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get weekly activity
     */
    List<Map<String, Object>> getWeeklyActivity(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get monthly activity
     */
    List<Map<String, Object>> getMonthlyActivity(int year);
    
    // ===================================================================
    // ERROR LOG OPERATIONS
    // ===================================================================
    
    /**
     * Create error log
     */
    ErrorLog createErrorLog(ErrorLog errorLog);
    
    /**
     * Get error log by ID
     */
    ErrorLog getErrorLogById(Long id);
    
    /**
     * Get all error logs
     */
    List<ErrorLog> getAllErrorLogs();
    
    /**
     * Get all error logs with pagination
     */
    Page<ErrorLog> getAllErrorLogs(Pageable pageable);
    
    /**
     * Get error logs by severity level
     */
    List<ErrorLog> getErrorLogsBySeverity(String severityLevel);
    
    /**
     * Get error logs by severity level with pagination
     */
    Page<ErrorLog> getErrorLogsBySeverity(String severityLevel, Pageable pageable);
    
    /**
     * Get critical errors
     */
    List<ErrorLog> getCriticalErrors();
    
    /**
     * Get critical errors with pagination
     */
    Page<ErrorLog> getCriticalErrors(Pageable pageable);
    
    /**
     * Get unresolved errors
     */
    List<ErrorLog> getUnresolvedErrors();
    
    /**
     * Get unresolved errors with pagination
     */
    Page<ErrorLog> getUnresolvedErrors(Pageable pageable);
    
    /**
     * Update error status
     */
    ErrorLog updateErrorStatus(Long errorId, String status);
    
    /**
     * Assign error to user
     */
    ErrorLog assignError(Long errorId, Long userId);
    
    /**
     * Resolve error
     */
    ErrorLog resolveError(Long errorId, String resolutionNotes);
}
