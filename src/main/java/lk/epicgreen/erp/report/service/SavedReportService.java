package lk.epicgreen.erp.reports.service;

import lk.epicgreen.erp.reports.dto.request.SavedReportRequest;
import lk.epicgreen.erp.reports.dto.response.SavedReportResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Service interface for Saved Report entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface SavedReportService {

    /**
     * Create saved report
     */
    SavedReportResponse createReport(SavedReportRequest request, Long createdBy);

    /**
     * Update saved report
     */
    SavedReportResponse updateReport(Long id, SavedReportRequest request);

    /**
     * Delete saved report
     */
    void deleteReport(Long id);

    /**
     * Get Saved Report by ID
     */
    SavedReportResponse getReportById(Long id);

    /**
     * Get Saved Report by code
     */
    SavedReportResponse getReportByCode(String reportCode);

    /**
     * Get all Saved Reports (paginated)
     */
    PageResponse<SavedReportResponse> getAllReports(Pageable pageable);

    /**
     * Get Reports by type
     */
    List<SavedReportResponse> getReportsByType(String reportType);

    /**
     * Get Reports by category
     */
    List<SavedReportResponse> getReportsByCategory(String reportCategory);

    /**
     * Get Reports created by user
     */
    List<SavedReportResponse> getReportsByCreator(Long createdBy);

    /**
     * Get public reports
     */
    List<SavedReportResponse> getPublicReports();

    /**
     * Get user's accessible reports (created by user + public)
     */
    List<SavedReportResponse> getUserAccessibleReports(Long userId);

    /**
     * Execute report
     */
    byte[] executeReport(Long reportId, Map<String, Object> parameters, Long executedBy);

    /**
     * Schedule report execution
     */
    void scheduleReport(Long reportId, String cronExpression);

    /**
     * Search reports
     */
    PageResponse<SavedReportResponse> searchReports(String keyword, Pageable pageable);

    /**
     * Check if can delete
     */
    boolean canDelete(Long id);
}
