package lk.epicgreen.erp.report.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.report.entity.SavedReport;
import lk.epicgreen.erp.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Report Statistics Controller
 * REST controller for report statistics and analytics
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/reports/statistics")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReportStatisticsController {
    
    private final ReportService reportService;
    
    // Statistics Operations
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getReportStatistics() {
        Map<String, Object> statistics = reportService.getReportStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Statistics retrieved successfully"));
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getReportStatisticsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        Map<String, Object> statistics = reportService.getReportStatistics(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(statistics, "Statistics by date range retrieved successfully"));
    }
    
    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = reportService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved successfully"));
    }
    
    // Distribution Statistics
    @GetMapping("/type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getReportTypeDistribution() {
        List<Map<String, Object>> distribution = reportService.getReportTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Type distribution retrieved successfully"));
    }
    
    @GetMapping("/category-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getCategoryDistribution() {
        List<Map<String, Object>> distribution = reportService.getCategoryDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Category distribution retrieved successfully"));
    }
    
    @GetMapping("/format-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getFormatDistribution() {
        List<Map<String, Object>> distribution = reportService.getFormatDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Format distribution retrieved successfully"));
    }
    
    // Activity Statistics
    @GetMapping("/most-generated")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMostGeneratedReports() {
        List<Map<String, Object>> reports = reportService.getMostGeneratedReports();
        return ResponseEntity.ok(ApiResponse.success(reports, "Most generated reports retrieved successfully"));
    }
    
    @GetMapping("/most-active-users")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMostActiveUsers() {
        List<Map<String, Object>> users = reportService.getMostActiveUsers();
        return ResponseEntity.ok(ApiResponse.success(users, "Most active users retrieved successfully"));
    }
    
    // Time-Based Statistics
    @GetMapping("/daily-count")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getDailyReportCount(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<Map<String, Object>> counts = reportService.getDailyReportCount(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(counts, "Daily report count retrieved successfully"));
    }
    
    // Export Operations
    @PostMapping("/export/{id}/pdf")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<byte[]> exportToPdf(@PathVariable Long id) {
        log.info("Exporting report to PDF: {}", id);
        SavedReport report = reportService.getSavedReportById(id);
        byte[] data = reportService.exportToPdf(report);
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + report.getReportCode() + ".pdf")
            .body(data);
    }
    
    @PostMapping("/export/{id}/excel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<byte[]> exportToExcel(@PathVariable Long id) {
        log.info("Exporting report to Excel: {}", id);
        SavedReport report = reportService.getSavedReportById(id);
        byte[] data = reportService.exportToExcel(report);
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + report.getReportCode() + ".xlsx")
            .body(data);
    }
    
    @PostMapping("/export/{id}/csv")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<byte[]> exportToCsv(@PathVariable Long id) {
        log.info("Exporting report to CSV: {}", id);
        SavedReport report = reportService.getSavedReportById(id);
        byte[] data = reportService.exportToCsv(report);
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + report.getReportCode() + ".csv")
            .body(data);
    }
    
    @PostMapping("/export/{id}/html")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<String> exportToHtml(@PathVariable Long id) {
        log.info("Exporting report to HTML: {}", id);
        SavedReport report = reportService.getSavedReportById(id);
        String html = reportService.exportToHtml(report);
        return ResponseEntity.ok(html);
    }
}
