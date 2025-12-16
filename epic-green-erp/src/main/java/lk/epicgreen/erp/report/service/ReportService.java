package lk.epicgreen.erp.report.service;

import lk.epicgreen.erp.report.dto.ReportRequest;
import lk.epicgreen.erp.report.entity.SavedReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Report Service Interface
 * Service for report generation and management
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface ReportService {
    
    // ===================================================================
    // REPORT GENERATION
    // ===================================================================
    
    /**
     * Generate report
     */
    SavedReport generateReport(ReportRequest request);
    
    /**
     * Generate and save report
     */
    SavedReport generateAndSaveReport(ReportRequest request);
    
    /**
     * Generate report in background
     */
    SavedReport generateReportAsync(ReportRequest request);
    
    /**
     * Re-generate existing report
     */
    SavedReport regenerateReport(Long reportId);
    
    /**
     * Download report
     */
    byte[] downloadReport(Long reportId);
    
    /**
     * Download report by code
     */
    byte[] downloadReportByCode(String reportCode);
    
    // ===================================================================
    // SALES REPORTS
    // ===================================================================
    
    /**
     * Generate sales summary report
     */
    SavedReport generateSalesSummaryReport(LocalDate startDate, LocalDate endDate, String format);
    
    /**
     * Generate sales by customer report
     */
    SavedReport generateSalesByCustomerReport(LocalDate startDate, LocalDate endDate, String format);
    
    /**
     * Generate sales by product report
     */
    SavedReport generateSalesByProductReport(LocalDate startDate, LocalDate endDate, String format);
    
    /**
     * Generate sales by sales representative report
     */
    SavedReport generateSalesBySalesRepReport(LocalDate startDate, LocalDate endDate, String format);
    
    /**
     * Generate daily sales report
     */
    SavedReport generateDailySalesReport(LocalDate date, String format);
    
    /**
     * Generate monthly sales report
     */
    SavedReport generateMonthlySalesReport(int year, int month, String format);
    
    /**
     * Generate sales trend report
     */
    SavedReport generateSalesTrendReport(LocalDate startDate, LocalDate endDate, String format);
    
    // ===================================================================
    // INVENTORY REPORTS
    // ===================================================================
    
    /**
     * Generate stock summary report
     */
    SavedReport generateStockSummaryReport(String format);
    
    /**
     * Generate stock by warehouse report
     */
    SavedReport generateStockByWarehouseReport(Long warehouseId, String format);
    
    /**
     * Generate low stock report
     */
    SavedReport generateLowStockReport(String format);
    
    /**
     * Generate stock movement report
     */
    SavedReport generateStockMovementReport(LocalDate startDate, LocalDate endDate, String format);
    
    /**
     * Generate stock valuation report
     */
    SavedReport generateStockValuationReport(LocalDate date, String format);
    
    /**
     * Generate expiring stock report
     */
    SavedReport generateExpiringStockReport(int daysAhead, String format);
    
    // ===================================================================
    // PURCHASE REPORTS
    // ===================================================================
    
    /**
     * Generate purchase summary report
     */
    SavedReport generatePurchaseSummaryReport(LocalDate startDate, LocalDate endDate, String format);
    
    /**
     * Generate purchase by supplier report
     */
    SavedReport generatePurchaseBySupplierReport(LocalDate startDate, LocalDate endDate, String format);
    
    /**
     * Generate purchase by product report
     */
    SavedReport generatePurchaseByProductReport(LocalDate startDate, LocalDate endDate, String format);
    
    /**
     * Generate monthly purchase report
     */
    SavedReport generateMonthlyPurchaseReport(int year, int month, String format);
    
    // ===================================================================
    // PRODUCTION REPORTS
    // ===================================================================
    
    /**
     * Generate production summary report
     */
    SavedReport generateProductionSummaryReport(LocalDate startDate, LocalDate endDate, String format);
    
    /**
     * Generate production by product report
     */
    SavedReport generateProductionByProductReport(LocalDate startDate, LocalDate endDate, String format);
    
    /**
     * Generate production efficiency report
     */
    SavedReport generateProductionEfficiencyReport(LocalDate startDate, LocalDate endDate, String format);
    
    /**
     * Generate raw material consumption report
     */
    SavedReport generateRawMaterialConsumptionReport(LocalDate startDate, LocalDate endDate, String format);
    
    /**
     * Generate daily production report
     */
    SavedReport generateDailyProductionReport(LocalDate date, String format);
    
    // ===================================================================
    // FINANCIAL REPORTS
    // ===================================================================
    
    /**
     * Generate payment summary report
     */
    SavedReport generatePaymentSummaryReport(LocalDate startDate, LocalDate endDate, String format);
    
    /**
     * Generate outstanding payments report
     */
    SavedReport generateOutstandingPaymentsReport(String format);
    
    /**
     * Generate payment by customer report
     */
    SavedReport generatePaymentByCustomerReport(LocalDate startDate, LocalDate endDate, String format);
    
    /**
     * Generate profit and loss report
     */
    SavedReport generateProfitLossReport(LocalDate startDate, LocalDate endDate, String format);
    
    /**
     * Generate balance sheet report
     */
    SavedReport generateBalanceSheetReport(LocalDate date, String format);
    
    /**
     * Generate cash flow report
     */
    SavedReport generateCashFlowReport(LocalDate startDate, LocalDate endDate, String format);
    
    // ===================================================================
    // CUSTOMER REPORTS
    // ===================================================================
    
    /**
     * Generate customer summary report
     */
    SavedReport generateCustomerSummaryReport(String format);
    
    /**
     * Generate customer transaction report
     */
    SavedReport generateCustomerTransactionReport(Long customerId, LocalDate startDate, 
                                                  LocalDate endDate, String format);
    
    /**
     * Generate customer outstanding report
     */
    SavedReport generateCustomerOutstandingReport(String format);
    
    /**
     * Generate top customers report
     */
    SavedReport generateTopCustomersReport(LocalDate startDate, LocalDate endDate, 
                                          int limit, String format);
    
    // ===================================================================
    // SUPPLIER REPORTS
    // ===================================================================
    
    /**
     * Generate supplier summary report
     */
    SavedReport generateSupplierSummaryReport(String format);
    
    /**
     * Generate supplier transaction report
     */
    SavedReport generateSupplierTransactionReport(Long supplierId, LocalDate startDate, 
                                                  LocalDate endDate, String format);
    
    /**
     * Generate supplier outstanding report
     */
    SavedReport generateSupplierOutstandingReport(String format);
    
    // ===================================================================
    // SAVED REPORT OPERATIONS
    // ===================================================================
    
    /**
     * Get saved report by ID
     */
    SavedReport getSavedReportById(Long id);
    
    /**
     * Get saved report by code
     */
    SavedReport getSavedReportByCode(String reportCode);
    
    /**
     * Get all saved reports
     */
    List<SavedReport> getAllSavedReports();
    
    /**
     * Get all saved reports with pagination
     */
    Page<SavedReport> getAllSavedReports(Pageable pageable);
    
    /**
     * Get saved reports by report type
     */
    List<SavedReport> getSavedReportsByType(String reportType);
    
    /**
     * Get saved reports by report type with pagination
     */
    Page<SavedReport> getSavedReportsByType(String reportType, Pageable pageable);
    
    /**
     * Get saved reports by category
     */
    List<SavedReport> getSavedReportsByCategory(String category);
    
    /**
     * Get saved reports by user
     */
    List<SavedReport> getSavedReportsByUser(Long userId);
    
    /**
     * Get saved reports by user with pagination
     */
    Page<SavedReport> getSavedReportsByUser(Long userId, Pageable pageable);
    
    /**
     * Get user's recent reports
     */
    List<SavedReport> getUserRecentReports(Long userId, int limit);
    
    /**
     * Get user's favorite reports
     */
    List<SavedReport> getUserFavoriteReports(Long userId);
    
    /**
     * Get public reports
     */
    List<SavedReport> getPublicReports();
    
    /**
     * Get public reports with pagination
     */
    Page<SavedReport> getPublicReports(Pageable pageable);
    
    /**
     * Search saved reports
     */
    Page<SavedReport> searchSavedReports(String keyword, Pageable pageable);
    
    /**
     * Delete saved report
     */
    void deleteSavedReport(Long id);
    
    /**
     * Mark report as favorite
     */
    SavedReport markAsFavorite(Long reportId);
    
    /**
     * Unmark report as favorite
     */
    SavedReport unmarkAsFavorite(Long reportId);
    
    /**
     * Make report public
     */
    SavedReport makeReportPublic(Long reportId);
    
    /**
     * Make report private
     */
    SavedReport makeReportPrivate(Long reportId);
    
    // ===================================================================
    // SCHEDULED REPORTS
    // ===================================================================
    
    /**
     * Schedule report
     */
    SavedReport scheduleReport(ReportRequest request, String frequency);
    
    /**
     * Get scheduled reports
     */
    List<SavedReport> getScheduledReports();
    
    /**
     * Get scheduled reports due for execution
     */
    List<SavedReport> getScheduledReportsDue();
    
    /**
     * Execute scheduled reports
     */
    int executeScheduledReports();
    
    /**
     * Cancel scheduled report
     */
    SavedReport cancelScheduledReport(Long reportId);
    
    // ===================================================================
    // REPORT STATISTICS
    // ===================================================================
    
    /**
     * Get report statistics
     */
    Map<String, Object> getReportStatistics();
    
    /**
     * Get report statistics by date range
     */
    Map<String, Object> getReportStatistics(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get report type distribution
     */
    List<Map<String, Object>> getReportTypeDistribution();
    
    /**
     * Get category distribution
     */
    List<Map<String, Object>> getCategoryDistribution();
    
    /**
     * Get format distribution
     */
    List<Map<String, Object>> getFormatDistribution();
    
    /**
     * Get most generated reports
     */
    List<Map<String, Object>> getMostGeneratedReports();
    
    /**
     * Get most active users
     */
    List<Map<String, Object>> getMostActiveUsers();
    
    /**
     * Get daily report count
     */
    List<Map<String, Object>> getDailyReportCount(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get dashboard statistics
     */
    Map<String, Object> getDashboardStatistics();
    
    // ===================================================================
    // REPORT EXPORT
    // ===================================================================
    
    /**
     * Export report to PDF
     */
    byte[] exportToPdf(SavedReport report);
    
    /**
     * Export report to Excel
     */
    byte[] exportToExcel(SavedReport report);
    
    /**
     * Export report to CSV
     */
    byte[] exportToCsv(SavedReport report);
    
    /**
     * Export report to HTML
     */
    String exportToHtml(SavedReport report);
    
    // ===================================================================
    // CLEANUP OPERATIONS
    // ===================================================================
    
    /**
     * Delete old reports
     */
    int deleteOldReports(int daysToKeep);
    
    /**
     * Get total file size
     */
    Long getTotalFileSize();
    
    /**
     * Get user's total file size
     */
    Long getUserTotalFileSize(Long userId);
}
