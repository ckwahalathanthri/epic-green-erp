package lk.epicgreen.erp.report.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import lk.epicgreen.erp.report.dto.request.ReportRequest;
import lk.epicgreen.erp.report.dto.response.SavedReportResponse;
import lk.epicgreen.erp.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Saved Report Controller
 * REST controller for saved report management
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/reports/saved")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class SavedReportController {
    
    private final ReportService reportService;
    
    // Query Operations
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<SavedReportResponse>> getSavedReportById(@PathVariable Long id) {
        SavedReportResponse report = reportService.getSavedReportById(id);
        return ResponseEntity.ok(ApiResponse.success(report, "Report retrieved successfully"));
    }
    
    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<SavedReportResponse>> getSavedReportByCode(@PathVariable String code) {
        SavedReportResponse report = reportService.getSavedReportByCode(code);
        return ResponseEntity.ok(ApiResponse.success(report, "Report retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<SavedReportResponse>>> getAllSavedReports(Pageable pageable) {
        PageResponse<SavedReportResponse> reports = reportService.getAllSavedReports(pageable);
        return ResponseEntity.ok(ApiResponse.success(reports, "Reports retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<List<SavedReportResponse>>> getAllSavedReportsList() {
        List<SavedReportResponse> reports = reportService.getAllSavedReports();
        return ResponseEntity.ok(ApiResponse.success(reports, "Reports list retrieved successfully"));
    }
    
    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<SavedReportResponse>>> getSavedReportsByType(@PathVariable String type, Pageable pageable) {
        PageResponse<SavedReportResponse> reports = reportService.getSavedReportsByType(type, pageable);
        return ResponseEntity.ok(ApiResponse.success(reports, "Reports by type retrieved successfully"));
    }
    
    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<List<SavedReportResponse>>> getSavedReportsByCategory(@PathVariable String category) {
        List<SavedReportResponse> reports = reportService.getSavedReportsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(reports, "Reports by category retrieved successfully"));
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<SavedReportResponse>>> getSavedReportsByUser(@PathVariable Long userId, Pageable pageable) {
        PageResponse<SavedReportResponse> reports = reportService.getSavedReportsByUser(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(reports, "User reports retrieved successfully"));
    }
    
    @GetMapping("/user/{userId}/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<List<SavedReportResponse>>> getUserRecentReports(@PathVariable Long userId, @RequestParam(defaultValue = "10") int limit) {
        List<SavedReportResponse> reports = reportService.getUserRecentReports(userId, limit);
        return ResponseEntity.ok(ApiResponse.success(reports, "User recent reports retrieved successfully"));
    }
    
    @GetMapping("/user/{userId}/favorites")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<List<SavedReportResponse>>> getUserFavoriteReports(@PathVariable Long userId) {
        List<SavedReportResponse> reports = reportService.getUserFavoriteReports(userId);
        return ResponseEntity.ok(ApiResponse.success(reports, "User favorite reports retrieved successfully"));
    }
    
    @GetMapping("/public")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<SavedReportResponse>>> getPublicReports(Pageable pageable) {
        PageResponse<SavedReportResponse> reports = reportService.getPublicReports(pageable);
        return ResponseEntity.ok(ApiResponse.success(reports, "Public reports retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<SavedReportResponse>>> searchSavedReports(@RequestParam String keyword, Pageable pageable) {
        PageResponse<SavedReportResponse> reports = reportService.searchSavedReports(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(reports, "Search results retrieved successfully"));
    }
    
    // Management Operations
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteSavedReport(@PathVariable Long id) {
        log.info("Deleting saved report: {}", id);
        reportService.deleteSavedReport(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Report deleted successfully"));
    }
    
    @PutMapping("/{id}/favorite")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<SavedReportResponse>> markAsFavorite(@PathVariable Long id) {
        log.info("Marking report as favorite: {}", id);
        SavedReportResponse report = reportService.markAsFavorite(id);
        return ResponseEntity.ok(ApiResponse.success(report, "Report marked as favorite"));
    }
    
    @DeleteMapping("/{id}/favorite")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<SavedReportResponse>> unmarkAsFavorite(@PathVariable Long id) {
        log.info("Unmarking report as favorite: {}", id);
        SavedReportResponse report = reportService.unmarkAsFavorite(id);
        return ResponseEntity.ok(ApiResponse.success(report, "Report unmarked as favorite"));
    }
    
    @PutMapping("/{id}/public")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SavedReportResponse>> makeReportPublic(@PathVariable Long id) {
        log.info("Making report public: {}", id);
        SavedReportResponse report = reportService.makeReportPublic(id);
        return ResponseEntity.ok(ApiResponse.success(report, "Report made public"));
    }
    
    @PutMapping("/{id}/private")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SavedReportResponse>> makeReportPrivate(@PathVariable Long id) {
        log.info("Making report private: {}", id);
        SavedReportResponse report = reportService.makeReportPrivate(id);
        return ResponseEntity.ok(ApiResponse.success(report, "Report made private"));
    }
    
    // Scheduled Reports
    @PostMapping("/schedule")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SavedReportResponse>> scheduleReport(
        @Valid @RequestBody ReportRequest request,
        @RequestParam String frequency
    ) {
        log.info("Scheduling report: {}", request.getReportType());
        SavedReportResponse report = reportService.scheduleReport(request, frequency);
        return ResponseEntity.ok(ApiResponse.success(report, "Report scheduled successfully"));
    }
    
    @GetMapping("/scheduled")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SavedReportResponse>>> getScheduledReports() {
        List<SavedReportResponse> reports = reportService.getScheduledReports();
        return ResponseEntity.ok(ApiResponse.success(reports, "Scheduled reports retrieved successfully"));
    }
    
    @GetMapping("/scheduled/due")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SavedReportResponse>>> getScheduledReportsDue() {
        List<SavedReportResponse> reports = reportService.getScheduledReportsDue();
        return ResponseEntity.ok(ApiResponse.success(reports, "Scheduled reports due retrieved successfully"));
    }
    
    @PostMapping("/scheduled/execute")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> executeScheduledReports() {
        log.info("Executing scheduled reports");
        int executed = reportService.executeScheduledReports();
        return ResponseEntity.ok(ApiResponse.success(executed, executed + " scheduled reports executed"));
    }
    
    @DeleteMapping("/{id}/schedule")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SavedReportResponse>> cancelScheduledReport(@PathVariable Long id) {
        log.info("Cancelling scheduled report: {}", id);
        SavedReportResponse report = reportService.cancelScheduledReport(id);
        return ResponseEntity.ok(ApiResponse.success(report, "Scheduled report cancelled"));
    }
    
    // Cleanup Operations
    @DeleteMapping("/cleanup")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Integer>> deleteOldReports(@RequestParam(defaultValue = "90") int daysToKeep) {
        log.info("Deleting reports older than {} days", daysToKeep);
        int deleted = reportService.deleteOldReports(daysToKeep);
        return ResponseEntity.ok(ApiResponse.success(deleted, deleted + " reports deleted"));
    }
    
    @GetMapping("/total-file-size")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Long>> getTotalFileSize() {
        Long totalSize = reportService.getTotalFileSize();
        return ResponseEntity.ok(ApiResponse.success(totalSize, "Total file size retrieved"));
    }
    
    @GetMapping("/user/{userId}/total-file-size")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Long>> getUserTotalFileSize(@PathVariable Long userId) {
        Long totalSize = reportService.getUserTotalFileSize(userId);
        return ResponseEntity.ok(ApiResponse.success(totalSize, "User total file size retrieved"));
    }
}
