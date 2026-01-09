package lk.epicgreen.erp.audit.service;

import lk.epicgreen.erp.audit.dto.request.AuditLogRequest;
import lk.epicgreen.erp.audit.dto.response.AuditLogResponse;
import lk.epicgreen.erp.audit.entity.AuditLog;
import lk.epicgreen.erp.audit.entity.ErrorLog;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Service interface for Audit Log entity business logic
 * 
 * Audit logs are immutable - no UPDATE or DELETE operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface AuditLogService {

    /**
     * Create audit log entry
     */
    AuditLogResponse createAuditLog(AuditLogRequest request);



    /**
     * Get Audit Log by ID
     */
    AuditLogResponse getAuditLogById(Long id);

    /**
     * Get all Audit Logs (paginated)
     */
    PageResponse<AuditLogResponse> getAllAuditLogs(Pageable pageable);

    /**
     * Get Audit Logs by user
     */
    List<AuditLogResponse> getAuditLogsByUser(Long userId);

    Page<AuditLog> getAuditLogsByUserId(Long userId,Pageable pageable);
    Page<AuditLog> getAuditLogsByUsername(String username,Pageable pageable);

    Page<AuditLog> getAuditLogsByModule(String moduleName,Pageable pageable);

    List<AuditLog> getEntityHistory(String entityType);
    List<AuditLog> getAuditLogsByDate(LocalDate date);

    List<AuditLog> getRecentAuditLogs(Pageable limit);
    Page<AuditLog> getFailedAuditLogs(Pageable pageable);
//    Map<String, Object>  getAuditStatistics();
    Map<String,Object> getActivityStatisticsCount();
    Map<String,Object> getActivityStatistics(LocalDateTime startDate,LocalDateTime endDate);

    Map<String,Object> getUserActivitySummary(Long userId);
    Map<String,Object> getUserActivitySummary(Long userId, LocalDateTime startDate,LocalDateTime endDate);
    List<Map<String,Object>> getActionTypeDistribution();
    
    List<Map<String,Object>> getMostViewedPages();

    List<Map<String,Object>> getMostViewedPages(LocalDateTime start,LocalDateTime stop);
    void deleteOldAuditLogs(int daysToKeep);
    List<AuditLog> getTodayAuditLogs();


    /**
     * Get Audit Logs by entity
     */
    List<AuditLogResponse> getAuditLogsByEntity(String entityType, Long entityId);

    /**
     * Get Audit Logs by operation type
     */
    PageResponse<AuditLogResponse> getAuditLogsByOperationType(String operationType, Pageable pageable);

    /**
     * Get Audit Logs by status
     */
    PageResponse<AuditLogResponse> getAuditLogsByStatus(String status, Pageable pageable);

    /**
     * Get Audit Logs by date range
     */
    List<AuditLogResponse> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get Audit Logs by action
     */
    List<AuditLogResponse> getAuditLogsByAction(String action);

    /**
     * Get failed operations
     */
    List<AuditLogResponse> getFailedOperations();

    /**
     * Search Audit Logs
     */
    PageResponse<AuditLogResponse> searchAuditLogs(String keyword, Pageable pageable);

    /**
     * Get Audit Logs by session
     */
    List<AuditLogResponse> getAuditLogsBySession(String sessionId);

    /**
     * Get Audit Logs by IP address
     */
    List<AuditLogResponse> getAuditLogsByIpAddress(String ipAddress);

    List<Map<String, Object>> getHourlyActivity(LocalDate date);

    Map<String, Object> getAuditStatistics();

    ErrorLog getErrorLogById(Long id);

    Page<ErrorLog> getAllErrorLogs(Pageable pageable);
    List<ErrorLog> getAllErrorLogs();

    Page<ErrorLog> getErrorLogsBySeverity(String severityLevel, Pageable pageable);

    List<ErrorLog> getCriticalErrors(Pageable pageable);

    List<ErrorLog> getUnresolvedErrors(Pageable pageable);

    ErrorLog updateErrorStatus(Long errorId, String status);

//    ErrorLog assignError(Long errorId, Long userId);
}
