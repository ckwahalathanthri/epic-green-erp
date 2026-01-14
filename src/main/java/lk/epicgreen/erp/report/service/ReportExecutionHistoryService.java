package lk.epicgreen.erp.report.service;

// import lk.epicgreen.erp.reports.dto.request.ReportExecutionHistoryRequest;
// import lk.epicgreen.erp.reports.dto.response.ReportExecutionHistoryResponse;
import lk.epicgreen.erp.report.dto.request.ReportExecutionHistoryRequest;
import lk.epicgreen.erp.report.dto.response.ReportExecutionHistoryResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Service interface for Report Execution History entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface ReportExecutionHistoryService {

    /**
     * Create execution history record
     */
    ReportExecutionHistoryResponse createExecutionHistory(ReportExecutionHistoryRequest request);

    /**
     * Start report execution (status=RUNNING)
     */
    ReportExecutionHistoryResponse startExecution(Long reportId, Long executedBy, Map<String, Object> parameters);

    /**
     * Mark execution as completed
     */
    void markAsCompleted(Long id, String outputFilePath, Integer executionTimeMs);

    /**
     * Mark execution as failed
     */
    void markAsFailed(Long id, String errorMessage, Integer executionTimeMs);

    /**
     * Update execution history
     */
    ReportExecutionHistoryResponse updateExecutionHistory(Long id, ReportExecutionHistoryRequest request);

    /**
     * Delete execution history
     */
    void deleteExecutionHistory(Long id);

    /**
     * Get Execution History by ID
     */
    ReportExecutionHistoryResponse getExecutionHistoryById(Long id);

    /**
     * Get all Execution Histories (paginated)
     */
    PageResponse<ReportExecutionHistoryResponse> getAllExecutionHistories(Pageable pageable);

    /**
     * Get Execution Histories by report
     */
    List<ReportExecutionHistoryResponse> getExecutionHistoriesByReport(Long reportId);

    /**
     * Get Execution Histories by user
     */
    List<ReportExecutionHistoryResponse> getExecutionHistoriesByUser(Long executedBy);

    /**
     * Get Execution Histories by status
     */
    PageResponse<ReportExecutionHistoryResponse> getExecutionHistoriesByStatus(String status, Pageable pageable);

    /**
     * Get Execution Histories by date range
     */
    List<ReportExecutionHistoryResponse> getExecutionHistoriesByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get running executions
     */
    List<ReportExecutionHistoryResponse> getRunningExecutions();

    /**
     * Get failed executions
     */
    List<ReportExecutionHistoryResponse> getFailedExecutions();

    /**
     * Get recent executions for report
     */
    List<ReportExecutionHistoryResponse> getRecentExecutionsForReport(Long reportId, int limit);

    /**
     * Get execution statistics for report
     */
    Map<String, Object> getExecutionStatistics(Long reportId);

    /**
     * Search execution histories
     */
    PageResponse<ReportExecutionHistoryResponse> searchExecutionHistories(String keyword, Pageable pageable);

    /**
     * Cleanup old execution histories
     */
    void cleanupOldHistories(int daysToKeep);
}
