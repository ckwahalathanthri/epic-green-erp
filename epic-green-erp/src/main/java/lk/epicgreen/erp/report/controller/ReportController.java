package lk.epicgreen.erp.report.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.report.dto.ReportRequest;
import lk.epicgreen.erp.report.entity.SavedReport;
import lk.epicgreen.erp.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Report Controller
 * REST controller for report operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReportController {
    
    private final ReportService reportService;
    
    // ===================================================================
    // REPORT GENERATION
    // ===================================================================
    
    /**
     * Generate report
     */
    @PostMapping("/generate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateReport(
        @Valid @RequestBody ReportRequest request
    ) {
        log.info("Generating report: {}", request.getReportType());
        SavedReport report = reportService.generateReport(request);
        return ResponseEntity.ok(ApiResponse.success(report, "Report generated successfully"));
    }
    
    /**
     * Generate report asynchronously
     */
    @PostMapping("/generate/async")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateReportAsync(
        @Valid @RequestBody ReportRequest request
    ) {
        log.info("Generating report asynchronously: {}", request.getReportType());
        SavedReport report = reportService.generateReportAsync(request);
        return ResponseEntity.ok(ApiResponse.success(report, "Report generation started"));
    }
    
    /**
     * Regenerate report
     */
    @PostMapping("/{id}/regenerate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> regenerateReport(@PathVariable Long id) {
        log.info("Regenerating report with id: {}", id);
        SavedReport report = reportService.regenerateReport(id);
        return ResponseEntity.ok(ApiResponse.success(report, "Report regenerated successfully"));
    }
    
    /**
     * Download report
     */
    @GetMapping("/{id}/download")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long id) {
        log.info("Downloading report with id: {}", id);
        
        SavedReport report = reportService.getSavedReportById(id);
        byte[] data = reportService.downloadReport(id);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(getMediaType(report.getFormat()));
        headers.setContentDispositionFormData("attachment", 
            report.getReportCode() + "." + report.getFormat());
        
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
    
    /**
     * Download report by code
     */
    @GetMapping("/code/{code}/download")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<byte[]> downloadReportByCode(@PathVariable String code) {
        log.info("Downloading report with code: {}", code);
        
        SavedReport report = reportService.getSavedReportByCode(code);
        byte[] data = reportService.downloadReportByCode(code);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(getMediaType(report.getFormat()));
        headers.setContentDispositionFormData("attachment", 
            report.getReportCode() + "." + report.getFormat());
        
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
    
    // ===================================================================
    // SALES REPORTS
    // ===================================================================
    
    @PostMapping("/sales/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateSalesSummaryReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateSalesSummaryReport(startDate, endDate, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Sales summary report generated"));
    }
    
    @PostMapping("/sales/by-customer")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateSalesByCustomerReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateSalesByCustomerReport(startDate, endDate, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Sales by customer report generated"));
    }
    
    @PostMapping("/sales/by-product")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateSalesByProductReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateSalesByProductReport(startDate, endDate, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Sales by product report generated"));
    }
    
    @PostMapping("/sales/daily")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateDailySalesReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateDailySalesReport(date, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Daily sales report generated"));
    }
    
    @PostMapping("/sales/monthly")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateMonthlySalesReport(
        @RequestParam int year,
        @RequestParam int month,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateMonthlySalesReport(year, month, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Monthly sales report generated"));
    }
    
    // ===================================================================
    // INVENTORY REPORTS
    // ===================================================================
    
    @PostMapping("/inventory/stock-summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateStockSummaryReport(
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateStockSummaryReport(format);
        return ResponseEntity.ok(ApiResponse.success(report, "Stock summary report generated"));
    }
    
    @PostMapping("/inventory/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateLowStockReport(
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateLowStockReport(format);
        return ResponseEntity.ok(ApiResponse.success(report, "Low stock report generated"));
    }
    
    @PostMapping("/inventory/stock-movement")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateStockMovementReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateStockMovementReport(startDate, endDate, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Stock movement report generated"));
    }
    
    @PostMapping("/inventory/stock-valuation")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateStockValuationReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateStockValuationReport(date, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Stock valuation report generated"));
    }
    
    // ===================================================================
    // PRODUCTION REPORTS
    // ===================================================================
    
    @PostMapping("/production/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateProductionSummaryReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateProductionSummaryReport(startDate, endDate, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Production summary report generated"));
    }
    
    @PostMapping("/production/daily")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateDailyProductionReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateDailyProductionReport(date, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Daily production report generated"));
    }
    
    // ===================================================================
    // FINANCIAL REPORTS
    // ===================================================================
    
    @PostMapping("/finance/payment-summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generatePaymentSummaryReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generatePaymentSummaryReport(startDate, endDate, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Payment summary report generated"));
    }
    
    @PostMapping("/finance/outstanding-payments")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateOutstandingPaymentsReport(
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateOutstandingPaymentsReport(format);
        return ResponseEntity.ok(ApiResponse.success(report, "Outstanding payments report generated"));
    }
    
    @PostMapping("/finance/profit-loss")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateProfitLossReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateProfitLossReport(startDate, endDate, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Profit & Loss report generated"));
    }
    
    // ===================================================================
    // SAVED REPORT OPERATIONS
    // ===================================================================
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<Page<SavedReport>>> getAllSavedReports(Pageable pageable) {
        Page<SavedReport> reports = reportService.getAllSavedReports(pageable);
        return ResponseEntity.ok(ApiResponse.success(reports, "Reports retrieved successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<SavedReport>> getSavedReportById(@PathVariable Long id) {
        SavedReport report = reportService.getSavedReportById(id);
        return ResponseEntity.ok(ApiResponse.success(report, "Report retrieved successfully"));
    }
    
    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<SavedReport>> getSavedReportByCode(@PathVariable String code) {
        SavedReport report = reportService.getSavedReportByCode(code);
        return ResponseEntity.ok(ApiResponse.success(report, "Report retrieved successfully"));
    }
    
    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<Page<SavedReport>>> getSavedReportsByType(
        @PathVariable String type,
        Pageable pageable
    ) {
        Page<SavedReport> reports = reportService.getSavedReportsByType(type, pageable);
        return ResponseEntity.ok(ApiResponse.success(reports, "Reports retrieved successfully"));
    }
    
    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<List<SavedReport>>> getSavedReportsByCategory(
        @PathVariable String category
    ) {
        List<SavedReport> reports = reportService.getSavedReportsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(reports, "Reports retrieved successfully"));
    }
    
    @GetMapping("/my-reports")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<Page<SavedReport>>> getMyReports(
        @RequestParam Long userId,
        Pageable pageable
    ) {
        Page<SavedReport> reports = reportService.getSavedReportsByUser(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(reports, "Your reports retrieved successfully"));
    }
    
    @GetMapping("/my-favorites")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<List<SavedReport>>> getMyFavorites(@RequestParam Long userId) {
        List<SavedReport> reports = reportService.getUserFavoriteReports(userId);
        return ResponseEntity.ok(ApiResponse.success(reports, "Favorite reports retrieved successfully"));
    }
    
    @GetMapping("/public")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<Page<SavedReport>>> getPublicReports(Pageable pageable) {
        Page<SavedReport> reports = reportService.getPublicReports(pageable);
        return ResponseEntity.ok(ApiResponse.success(reports, "Public reports retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<Page<SavedReport>>> searchReports(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<SavedReport> reports = reportService.searchSavedReports(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(reports, "Search results retrieved successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteSavedReport(@PathVariable Long id) {
        reportService.deleteSavedReport(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Report deleted successfully"));
    }
    
    @PutMapping("/{id}/favorite")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<SavedReport>> markAsFavorite(@PathVariable Long id) {
        SavedReport report = reportService.markAsFavorite(id);
        return ResponseEntity.ok(ApiResponse.success(report, "Report marked as favorite"));
    }
    
    @DeleteMapping("/{id}/favorite")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<ApiResponse<SavedReport>> unmarkAsFavorite(@PathVariable Long id) {
        SavedReport report = reportService.unmarkAsFavorite(id);
        return ResponseEntity.ok(ApiResponse.success(report, "Report unmarked as favorite"));
    }
    
    @PutMapping("/{id}/public")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SavedReport>> makeReportPublic(@PathVariable Long id) {
        SavedReport report = reportService.makeReportPublic(id);
        return ResponseEntity.ok(ApiResponse.success(report, "Report made public"));
    }
    
    @PutMapping("/{id}/private")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SavedReport>> makeReportPrivate(@PathVariable Long id) {
        SavedReport report = reportService.makeReportPrivate(id);
        return ResponseEntity.ok(ApiResponse.success(report, "Report made private"));
    }
    
    // ===================================================================
    // SCHEDULED REPORTS
    // ===================================================================
    
    @PostMapping("/schedule")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SavedReport>> scheduleReport(
        @Valid @RequestBody ReportRequest request,
        @RequestParam String frequency
    ) {
        SavedReport report = reportService.scheduleReport(request, frequency);
        return ResponseEntity.ok(ApiResponse.success(report, "Report scheduled successfully"));
    }
    
    @GetMapping("/scheduled")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SavedReport>>> getScheduledReports() {
        List<SavedReport> reports = reportService.getScheduledReports();
        return ResponseEntity.ok(ApiResponse.success(reports, "Scheduled reports retrieved successfully"));
    }
    
    @DeleteMapping("/{id}/schedule")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SavedReport>> cancelScheduledReport(@PathVariable Long id) {
        SavedReport report = reportService.cancelScheduledReport(id);
        return ResponseEntity.ok(ApiResponse.success(report, "Scheduled report cancelled"));
    }
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = reportService.getReportStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved successfully"));
    }
    
    @GetMapping("/statistics/type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTypeDistribution() {
        List<Map<String, Object>> distribution = reportService.getReportTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Type distribution retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = reportService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
    
    // ===================================================================
    // HELPER METHODS
    // ===================================================================
    
    private MediaType getMediaType(String format) {
        return switch (format.toUpperCase()) {
            case "PDF" -> MediaType.APPLICATION_PDF;
            case "EXCEL", "XLSX" -> MediaType.APPLICATION_OCTET_STREAM;
            case "CSV" -> MediaType.parseMediaType("text/csv");
            case "HTML" -> MediaType.TEXT_HTML;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }
}
