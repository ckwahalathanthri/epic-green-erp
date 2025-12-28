package lk.epicgreen.erp.audit.service;

import lk.epicgreen.erp.audit.dto.request.ErrorLogRequest;
import lk.epicgreen.erp.audit.dto.response.ErrorLogResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for Error Log entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface ErrorLogService {

    /**
     * Create error log entry
     */
    ErrorLogResponse createErrorLog(ErrorLogRequest request);

    /**
     * Update error log (for resolution only)
     */
    ErrorLogResponse updateErrorLog(Long id, ErrorLogRequest request);

    /**
     * Resolve error
     */
    void resolveError(Long id, Long resolvedBy);

    /**
     * Delete error log
     */
    void deleteErrorLog(Long id);

    /**
     * Get Error Log by ID
     */
    ErrorLogResponse getErrorLogById(Long id);

    /**
     * Get all Error Logs (paginated)
     */
    PageResponse<ErrorLogResponse> getAllErrorLogs(Pageable pageable);

    /**
     * Get Error Logs by severity
     */
    PageResponse<ErrorLogResponse> getErrorLogsBySeverity(String severity, Pageable pageable);

    /**
     * Get Error Logs by error type
     */
    List<ErrorLogResponse> getErrorLogsByErrorType(String errorType);

    /**
     * Get unresolved errors
     */
    List<ErrorLogResponse> getUnresolvedErrors();

    /**
     * Get resolved errors
     */
    PageResponse<ErrorLogResponse> getResolvedErrors(Pageable pageable);

    /**
     * Get critical errors
     */
    List<ErrorLogResponse> getCriticalErrors();

    /**
     * Get Error Logs by date range
     */
    List<ErrorLogResponse> getErrorLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get Error Logs by user
     */
    List<ErrorLogResponse> getErrorLogsByUser(Long userId);

    /**
     * Search Error Logs
     */
    PageResponse<ErrorLogResponse> searchErrorLogs(String keyword, Pageable pageable);

    /**
     * Get error count by type
     */
    Long getErrorCountByType(String errorType);

    /**
     * Get error count by severity
     */
    Long getErrorCountBySeverity(String severity);
}
