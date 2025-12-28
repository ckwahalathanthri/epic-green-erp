package lk.epicgreen.erp.report.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.report.dto.ReportRequest;
import lk.epicgreen.erp.report.entity.SavedReport;
import lk.epicgreen.erp.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;

/**
 * Report Controller
 * REST controller for report generation operations
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
    
    // Report Generation
    @PostMapping("/generate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateReport(@Valid @RequestBody ReportRequest request) {
        log.info("Generating report: {}", request.getReportType());
        SavedReport report = reportService.generateReport(request);
        return ResponseEntity.ok(ApiResponse.success(report, "Report generated successfully"));
    }
    
    @PostMapping("/generate/async")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateReportAsync(@Valid @RequestBody ReportRequest request) {
        log.info("Generating report asynchronously: {}", request.getReportType());
        SavedReport report = reportService.generateReportAsync(request);
        return ResponseEntity.ok(ApiResponse.success(report, "Report generation started"));
    }
    
    @PostMapping("/{id}/regenerate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> regenerateReport(@PathVariable Long id) {
        log.info("Regenerating report: {}", id);
        SavedReport report = reportService.regenerateReport(id);
        return ResponseEntity.ok(ApiResponse.success(report, "Report regenerated successfully"));
    }
    
    @GetMapping("/{id}/download")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long id) {
        log.info("Downloading report: {}", id);
        SavedReport report = reportService.getSavedReportById(id);
        byte[] data = reportService.downloadReport(id);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(getMediaType(report.getFormat()));
        headers.setContentDispositionFormData("attachment", report.getReportCode() + "." + report.getFormat().toLowerCase());
        
        return ResponseEntity.ok().headers(headers).body(data);
    }
    
    @GetMapping("/code/{code}/download")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR', 'USER')")
    public ResponseEntity<byte[]> downloadReportByCode(@PathVariable String code) {
        log.info("Downloading report by code: {}", code);
        SavedReport report = reportService.getSavedReportByCode(code);
        byte[] data = reportService.downloadReportByCode(code);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(getMediaType(report.getFormat()));
        headers.setContentDispositionFormData("attachment", report.getReportCode() + "." + report.getFormat().toLowerCase());
        
        return ResponseEntity.ok().headers(headers).body(data);
    }
    
    // Sales Reports
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
    
    @PostMapping("/sales/by-sales-rep")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateSalesBySalesRepReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateSalesBySalesRepReport(startDate, endDate, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Sales by sales rep report generated"));
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
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateMonthlySalesReport(startDate, endDate, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Monthly sales report generated"));
    }
    
    @PostMapping("/sales/trend")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateSalesTrendReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateSalesTrendReport(startDate, endDate, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Sales trend report generated"));
    }
    
    // Inventory Reports
    @PostMapping("/inventory/stock-summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateStockSummaryReport(@RequestParam(defaultValue = "PDF") String format) {
        SavedReport report = reportService.generateStockSummaryReport(format);
        return ResponseEntity.ok(ApiResponse.success(report, "Stock summary report generated"));
    }
    
    @PostMapping("/inventory/stock-by-warehouse")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateStockByWarehouseReport(@RequestParam(defaultValue = "PDF") String format) {
        SavedReport report = reportService.generateStockByWarehouseReport(format);
        return ResponseEntity.ok(ApiResponse.success(report, "Stock by warehouse report generated"));
    }
    
    @PostMapping("/inventory/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateLowStockReport(@RequestParam(defaultValue = "PDF") String format) {
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
    
    @PostMapping("/inventory/expiring-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateExpiringStockReport(
        @RequestParam(defaultValue = "30") int daysAhead,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateExpiringStockReport(daysAhead, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Expiring stock report generated"));
    }
    
    // Purchase Reports
    @PostMapping("/purchase/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generatePurchaseSummaryReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generatePurchaseSummaryReport(startDate, endDate, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Purchase summary report generated"));
    }
    
    @PostMapping("/purchase/by-supplier")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generatePurchaseBySupplierReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generatePurchaseBySupplierReport(startDate, endDate, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Purchase by supplier report generated"));
    }
    
    @PostMapping("/purchase/by-product")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generatePurchaseByProductReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generatePurchaseByProductReport(startDate, endDate, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Purchase by product report generated"));
    }
    
    @PostMapping("/purchase/monthly")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateMonthlyPurchaseReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateMonthlyPurchaseReport(startDate, endDate, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Monthly purchase report generated"));
    }
    
    // Production Reports
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
    
    @PostMapping("/production/by-product")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateProductionByProductReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateProductionByProductReport(startDate, endDate, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Production by product report generated"));
    }
    
    @PostMapping("/production/efficiency")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateProductionEfficiencyReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateProductionEfficiencyReport(startDate, endDate, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Production efficiency report generated"));
    }
    
    @PostMapping("/production/raw-material-consumption")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateRawMaterialConsumptionReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateRawMaterialConsumptionReport(startDate, endDate, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Raw material consumption report generated"));
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
    
    // Financial Reports
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
    public ResponseEntity<ApiResponse<SavedReport>> generateOutstandingPaymentsReport(@RequestParam(defaultValue = "PDF") String format) {
        SavedReport report = reportService.generateOutstandingPaymentsReport(format);
        return ResponseEntity.ok(ApiResponse.success(report, "Outstanding payments report generated"));
    }
    
    @PostMapping("/finance/payment-by-customer")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generatePaymentByCustomerReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generatePaymentByCustomerReport(startDate, endDate, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Payment by customer report generated"));
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
    
    @PostMapping("/finance/balance-sheet")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateBalanceSheetReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateBalanceSheetReport(date, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Balance sheet report generated"));
    }
    
    @PostMapping("/finance/cash-flow")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateCashFlowReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateCashFlowReport(startDate, endDate, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Cash flow report generated"));
    }
    
    // Customer Reports
    @PostMapping("/customer/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateCustomerSummaryReport(@RequestParam(defaultValue = "PDF") String format) {
        SavedReport report = reportService.generateCustomerSummaryReport(format);
        return ResponseEntity.ok(ApiResponse.success(report, "Customer summary report generated"));
    }
    
    @PostMapping("/customer/transaction")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateCustomerTransactionReport(
        @RequestParam Long customerId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateCustomerTransactionReport(customerId, startDate, endDate, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Customer transaction report generated"));
    }
    
    @PostMapping("/customer/outstanding")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateCustomerOutstandingReport(@RequestParam(defaultValue = "PDF") String format) {
        SavedReport report = reportService.generateCustomerOutstandingReport(format);
        return ResponseEntity.ok(ApiResponse.success(report, "Customer outstanding report generated"));
    }
    
    @PostMapping("/customer/top-customers")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateTopCustomersReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateTopCustomersReport(startDate, endDate, limit, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Top customers report generated"));
    }
    
    // Supplier Reports
    @PostMapping("/supplier/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateSupplierSummaryReport(@RequestParam(defaultValue = "PDF") String format) {
        SavedReport report = reportService.generateSupplierSummaryReport(format);
        return ResponseEntity.ok(ApiResponse.success(report, "Supplier summary report generated"));
    }
    
    @PostMapping("/supplier/transaction")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateSupplierTransactionReport(
        @RequestParam Long supplierId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        SavedReport report = reportService.generateSupplierTransactionReport(supplierId, startDate, endDate, format);
        return ResponseEntity.ok(ApiResponse.success(report, "Supplier transaction report generated"));
    }
    
    @PostMapping("/supplier/outstanding")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'REPORT_GENERATOR')")
    public ResponseEntity<ApiResponse<SavedReport>> generateSupplierOutstandingReport(@RequestParam(defaultValue = "PDF") String format) {
        SavedReport report = reportService.generateSupplierOutstandingReport(format);
        return ResponseEntity.ok(ApiResponse.success(report, "Supplier outstanding report generated"));
    }
    
    // Helper Method
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
