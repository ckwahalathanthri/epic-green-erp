package lk.epicgreen.erp.report.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import lk.epicgreen.erp.common.dto.PageResponse;
import lk.epicgreen.erp.report.dto.request.ReportRequest;
import lk.epicgreen.erp.report.dto.response.SavedReportResponse;
import org.springframework.stereotype.Service;


public interface ReportService  {

    SavedReportResponse generateReport(ReportRequest request);

    SavedReportResponse generateReportAsync(ReportRequest request);

	SavedReportResponse regenerateReport(Long id);

	SavedReportResponse getSavedReportById(Long id);

	byte[] downloadReport(Long id);

	SavedReportResponse getSavedReportByCode(String code);

    byte[] downloadReportByCode(String code);

    SavedReportResponse generateSalesSummaryReport(LocalDate startDate, LocalDate endDate, String format);

    SavedReportResponse generateSalesByCustomerReport(LocalDate startDate, LocalDate endDate, String format);

	SavedReportResponse generateSalesByProductReport(LocalDate startDate, LocalDate endDate, String format);

	SavedReportResponse generateSalesBySalesRepReport(LocalDate startDate, LocalDate endDate, String format);

	SavedReportResponse generateDailySalesReport(LocalDate date, String format);

	SavedReportResponse generateMonthlySalesReport(LocalDate startDate, LocalDate endDate, String format);

	SavedReportResponse generateSalesTrendReport(LocalDate startDate, LocalDate endDate, String format);

	SavedReportResponse generateStockSummaryReport(String format);

	SavedReportResponse generateStockByWarehouseReport(String format);

	SavedReportResponse generateLowStockReport(String format);

    SavedReportResponse generateStockMovementReport(LocalDate startDate, LocalDate endDate, String format);

	SavedReportResponse generateStockValuationReport(LocalDate date, String format);

    SavedReportResponse generateExpiringStockReport(int daysAhead, String format);

	SavedReportResponse generatePurchaseSummaryReport(LocalDate startDate, LocalDate endDate, String format);

	SavedReportResponse generatePurchaseBySupplierReport(LocalDate startDate, LocalDate endDate, String format);

	SavedReportResponse generatePurchaseByProductReport(LocalDate startDate, LocalDate endDate, String format);

	SavedReportResponse generateMonthlyPurchaseReport(LocalDate startDate, LocalDate endDate, String format);

	SavedReportResponse generateProductionSummaryReport(LocalDate startDate, LocalDate endDate, String format);

	SavedReportResponse generateProductionByProductReport(LocalDate startDate, LocalDate endDate, String format);

	SavedReportResponse generateProductionEfficiencyReport(LocalDate startDate, LocalDate endDate, String format);

	SavedReportResponse generateRawMaterialConsumptionReport(LocalDate startDate, LocalDate endDate, String format);

	SavedReportResponse generateDailyProductionReport(LocalDate date, String format);

	SavedReportResponse generatePaymentSummaryReport(LocalDate startDate, LocalDate endDate, String format);

	SavedReportResponse generateOutstandingPaymentsReport(String format);

	SavedReportResponse generatePaymentByCustomerReport(LocalDate startDate, LocalDate endDate, String format);

	SavedReportResponse generateProfitLossReport(LocalDate startDate, LocalDate endDate, String format);

	SavedReportResponse generateBalanceSheetReport(LocalDate date, String format);

	SavedReportResponse generateCashFlowReport(LocalDate startDate, LocalDate endDate, String format);

	SavedReportResponse generateCustomerSummaryReport(String format);

	SavedReportResponse generateCustomerTransactionReport(Long customerId, LocalDate startDate, LocalDate endDate,
			String format);

	SavedReportResponse generateCustomerOutstandingReport(String format);

	SavedReportResponse generateTopCustomersReport(LocalDate startDate, LocalDate endDate, int limit, String format);

	SavedReportResponse generateSupplierSummaryReport(String format);

	SavedReportResponse generateSupplierTransactionReport(Long supplierId, LocalDate startDate, LocalDate endDate,
			String format);

	SavedReportResponse generateSupplierOutstandingReport(String format);

    Map<String, Object> getReportStatistics();

	Map<String, Object> getReportStatistics(LocalDateTime startDate, LocalDateTime endDate);

	Map<String, Object> getDashboardStatistics();

	List<Map<String, Object>> getReportTypeDistribution();

	List<Map<String, Object>> getCategoryDistribution();

	List<Map<String, Object>> getFormatDistribution();

	List<Map<String, Object>> getMostGeneratedReports();

	List<Map<String, Object>> getMostActiveUsers();

	List<Map<String, Object>> getDailyReportCount(LocalDate startDate, LocalDate endDate);

	byte[] exportToPdf(SavedReportResponse report);

	byte[] exportToExcel(SavedReportResponse report);

	byte[] exportToCsv(SavedReportResponse report);

	String exportToHtml(SavedReportResponse report);

	List<SavedReportResponse> getAllSavedReports();

	PageResponse<SavedReportResponse> getAllSavedReports(Pageable pageable);

	PageResponse<SavedReportResponse> getSavedReportsByType(String type, Pageable pageable);

	List<SavedReportResponse> getSavedReportsByCategory(String category);

	PageResponse<SavedReportResponse> getSavedReportsByUser(Long userId, Pageable pageable);

	List<SavedReportResponse> getUserRecentReports(Long userId, int limit);

	List<SavedReportResponse> getUserFavoriteReports(Long userId);

	PageResponse<SavedReportResponse> getPublicReports(Pageable pageable);

	PageResponse<SavedReportResponse> searchSavedReports(String keyword, Pageable pageable);

	void deleteSavedReport(Long id);

	SavedReportResponse markAsFavorite(Long id);

	SavedReportResponse unmarkAsFavorite(Long id);

	SavedReportResponse makeReportPublic(Long id);

	SavedReportResponse makeReportPrivate(Long id);

	SavedReportResponse scheduleReport(ReportRequest request, String frequency);

	List<SavedReportResponse> getScheduledReports();

	List<lk.epicgreen.erp.report.dto.response.SavedReportResponse> getScheduledReportsDue();

	int executeScheduledReports();

	lk.epicgreen.erp.report.dto.response.SavedReportResponse cancelScheduledReport(Long id);

	int deleteOldReports(int daysToKeep);

	Long getTotalFileSize();

	Long getUserTotalFileSize(Long userId);
    
}
