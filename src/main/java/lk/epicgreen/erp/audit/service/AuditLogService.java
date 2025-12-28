package lk.epicgreen.erp.audit.service;

import lk.epicgreen.erp.audit.dto.request.AuditLogRequest;
import lk.epicgreen.erp.audit.dto.response.AuditLogResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

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
}
