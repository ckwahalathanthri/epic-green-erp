package lk.epicgreen.erp.report.service;

import lk.epicgreen.erp.report.dto.ReportRequest;
import lk.epicgreen.erp.report.entity.SavedReport;
import lk.epicgreen.erp.report.repository.SavedReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Report Service Implementation
 * Implementation of report service operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReportServiceImpl implements ReportService {
    
    private final SavedReportRepository savedReportRepository;
    
    // ===================================================================
    // REPORT GENERATION
    // ===================================================================
    
    @Override
    public SavedReport generateReport(ReportRequest request) {
        log.info("Generating report: {}", request.getReportType());
        
        long startTime = System.currentTimeMillis();
        
        // Create saved report
        SavedReport report = SavedReport.builder()
            .reportCode(generateReportCode())
            .reportName(request.getReportName())
            .reportType(request.getReportType())
            .category(request.getCategory())
            .format(request.getFormat())
            .status("PROCESSING")
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .reportParameters(request.getParametersAsJson())
            .lastGeneratedAt(LocalDateTime.now())
            .build();
        
        report = savedReportRepository.save(report);
        
        try {
            // Generate report data based on report type
            byte[] reportData = generateReportData(request);
            
            // Calculate metrics
            long generationTime = System.currentTimeMillis() - startTime;
            
            // Update report
            report.setStatus("COMPLETED");
            report.setFilePath("/reports/" + report.getReportCode() + "." + request.getFormat());
            report.setFileSizeBytes((long) reportData.length);
            report.setGenerationTimeMillis(generationTime);
            report.setRecordCount(calculateRecordCount(reportData));
            
            report = savedReportRepository.save(report);
            
            log.info("Report generated successfully: {} in {} ms", 
                report.getReportCode(), generationTime);
            
        } catch (Exception e) {
            log.error("Error generating report", e);
            report.setStatus("FAILED");
            report.setErrorMessage(e.getMessage());
            savedReportRepository.save(report);
            throw new RuntimeException("Failed to generate report: " + e.getMessage(), e);
        }
        
        return report;
    }
    
    @Override
    public SavedReport generateAndSaveReport(ReportRequest request) {
        return generateReport(request);
    }
    
    @Override
    @Async
    public SavedReport generateReportAsync(ReportRequest request) {
        return generateReport(request);
    }
    
    @Override
    public SavedReport regenerateReport(Long reportId) {
        SavedReport existingReport = getSavedReportById(reportId);
        
        // Create new request from existing report
        ReportRequest request = ReportRequest.builder()
            .reportName(existingReport.getReportName())
            .reportType(existingReport.getReportType())
            .category(existingReport.getCategory())
            .format(existingReport.getFormat())
            .startDate(existingReport.getStartDate())
            .endDate(existingReport.getEndDate())
            .build();
        
        return generateReport(request);
    }
    
    @Override
    @Transactional(readOnly = true)
    public byte[] downloadReport(Long reportId) {
        SavedReport report = getSavedReportById(reportId);
        
        if (!"COMPLETED".equals(report.getStatus())) {
            throw new RuntimeException("Report is not ready for download");
        }
        
        // Load report file
        return loadReportFile(report.getFilePath());
    }
    
    @Override
    @Transactional(readOnly = true)
    public byte[] downloadReportByCode(String reportCode) {
        SavedReport report = getSavedReportByCode(reportCode);
        return downloadReport(report.getId());
    }
    
    // ===================================================================
    // SALES REPORTS
    // ===================================================================
    
    @Override
    public SavedReport generateSalesSummaryReport(LocalDate startDate, LocalDate endDate, String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Sales Summary Report")
            .reportType("SALES_SUMMARY")
            .category("SALES")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateSalesByCustomerReport(LocalDate startDate, LocalDate endDate, String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Sales by Customer Report")
            .reportType("SALES_BY_CUSTOMER")
            .category("SALES")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateSalesByProductReport(LocalDate startDate, LocalDate endDate, String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Sales by Product Report")
            .reportType("SALES_BY_PRODUCT")
            .category("SALES")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateSalesBySalesRepReport(LocalDate startDate, LocalDate endDate, String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Sales by Sales Representative Report")
            .reportType("SALES_BY_SALES_REP")
            .category("SALES")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateDailySalesReport(LocalDate date, String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Daily Sales Report - " + date)
            .reportType("DAILY_SALES")
            .category("SALES")
            .format(format)
            .startDate(date)
            .endDate(date)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateMonthlySalesReport(int year, int month, String format) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        
        ReportRequest request = ReportRequest.builder()
            .reportName("Monthly Sales Report - " + year + "-" + month)
            .reportType("MONTHLY_SALES")
            .category("SALES")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateSalesTrendReport(LocalDate startDate, LocalDate endDate, String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Sales Trend Report")
            .reportType("SALES_TREND")
            .category("SALES")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .build();
        
        return generateReport(request);
    }
    
    // ===================================================================
    // INVENTORY REPORTS
    // ===================================================================
    
    @Override
    public SavedReport generateStockSummaryReport(String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Stock Summary Report")
            .reportType("STOCK_SUMMARY")
            .category("INVENTORY")
            .format(format)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateStockByWarehouseReport(Long warehouseId, String format) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("warehouseId", warehouseId);
        
        ReportRequest request = ReportRequest.builder()
            .reportName("Stock by Warehouse Report")
            .reportType("STOCK_BY_WAREHOUSE")
            .category("INVENTORY")
            .format(format)
            .parameters(parameters)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateLowStockReport(String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Low Stock Report")
            .reportType("LOW_STOCK")
            .category("INVENTORY")
            .format(format)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateStockMovementReport(LocalDate startDate, LocalDate endDate, String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Stock Movement Report")
            .reportType("STOCK_MOVEMENT")
            .category("INVENTORY")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateStockValuationReport(LocalDate date, String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Stock Valuation Report")
            .reportType("STOCK_VALUATION")
            .category("INVENTORY")
            .format(format)
            .startDate(date)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateExpiringStockReport(int daysAhead, String format) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("daysAhead", daysAhead);
        
        ReportRequest request = ReportRequest.builder()
            .reportName("Expiring Stock Report")
            .reportType("EXPIRING_STOCK")
            .category("INVENTORY")
            .format(format)
            .parameters(parameters)
            .build();
        
        return generateReport(request);
    }
    
    // ===================================================================
    // PURCHASE REPORTS
    // ===================================================================
    
    @Override
    public SavedReport generatePurchaseSummaryReport(LocalDate startDate, LocalDate endDate, String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Purchase Summary Report")
            .reportType("PURCHASE_SUMMARY")
            .category("PURCHASE")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generatePurchaseBySupplierReport(LocalDate startDate, LocalDate endDate, String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Purchase by Supplier Report")
            .reportType("PURCHASE_BY_SUPPLIER")
            .category("PURCHASE")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generatePurchaseByProductReport(LocalDate startDate, LocalDate endDate, String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Purchase by Product Report")
            .reportType("PURCHASE_BY_PRODUCT")
            .category("PURCHASE")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateMonthlyPurchaseReport(int year, int month, String format) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        
        ReportRequest request = ReportRequest.builder()
            .reportName("Monthly Purchase Report - " + year + "-" + month)
            .reportType("MONTHLY_PURCHASE")
            .category("PURCHASE")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .build();
        
        return generateReport(request);
    }
    
    // ===================================================================
    // PRODUCTION REPORTS
    // ===================================================================
    
    @Override
    public SavedReport generateProductionSummaryReport(LocalDate startDate, LocalDate endDate, String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Production Summary Report")
            .reportType("PRODUCTION_SUMMARY")
            .category("PRODUCTION")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateProductionByProductReport(LocalDate startDate, LocalDate endDate, String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Production by Product Report")
            .reportType("PRODUCTION_BY_PRODUCT")
            .category("PRODUCTION")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateProductionEfficiencyReport(LocalDate startDate, LocalDate endDate, String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Production Efficiency Report")
            .reportType("PRODUCTION_EFFICIENCY")
            .category("PRODUCTION")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateRawMaterialConsumptionReport(LocalDate startDate, LocalDate endDate, String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Raw Material Consumption Report")
            .reportType("RAW_MATERIAL_CONSUMPTION")
            .category("PRODUCTION")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateDailyProductionReport(LocalDate date, String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Daily Production Report - " + date)
            .reportType("DAILY_PRODUCTION")
            .category("PRODUCTION")
            .format(format)
            .startDate(date)
            .endDate(date)
            .build();
        
        return generateReport(request);
    }
    
    // ===================================================================
    // FINANCIAL REPORTS
    // ===================================================================
    
    @Override
    public SavedReport generatePaymentSummaryReport(LocalDate startDate, LocalDate endDate, String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Payment Summary Report")
            .reportType("PAYMENT_SUMMARY")
            .category("FINANCE")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateOutstandingPaymentsReport(String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Outstanding Payments Report")
            .reportType("OUTSTANDING_PAYMENTS")
            .category("FINANCE")
            .format(format)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generatePaymentByCustomerReport(LocalDate startDate, LocalDate endDate, String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Payment by Customer Report")
            .reportType("PAYMENT_BY_CUSTOMER")
            .category("FINANCE")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateProfitLossReport(LocalDate startDate, LocalDate endDate, String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Profit & Loss Report")
            .reportType("PROFIT_LOSS")
            .category("FINANCE")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateBalanceSheetReport(LocalDate date, String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Balance Sheet Report")
            .reportType("BALANCE_SHEET")
            .category("FINANCE")
            .format(format)
            .startDate(date)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateCashFlowReport(LocalDate startDate, LocalDate endDate, String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Cash Flow Report")
            .reportType("CASH_FLOW")
            .category("FINANCE")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .build();
        
        return generateReport(request);
    }
    
    // ===================================================================
    // CUSTOMER & SUPPLIER REPORTS
    // ===================================================================
    
    @Override
    public SavedReport generateCustomerSummaryReport(String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Customer Summary Report")
            .reportType("CUSTOMER_SUMMARY")
            .category("CUSTOMER")
            .format(format)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateCustomerTransactionReport(Long customerId, LocalDate startDate, 
                                                        LocalDate endDate, String format) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("customerId", customerId);
        
        ReportRequest request = ReportRequest.builder()
            .reportName("Customer Transaction Report")
            .reportType("CUSTOMER_TRANSACTION")
            .category("CUSTOMER")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .parameters(parameters)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateCustomerOutstandingReport(String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Customer Outstanding Report")
            .reportType("CUSTOMER_OUTSTANDING")
            .category("CUSTOMER")
            .format(format)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateTopCustomersReport(LocalDate startDate, LocalDate endDate, 
                                                 int limit, String format) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("limit", limit);
        
        ReportRequest request = ReportRequest.builder()
            .reportName("Top Customers Report")
            .reportType("TOP_CUSTOMERS")
            .category("CUSTOMER")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .parameters(parameters)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateSupplierSummaryReport(String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Supplier Summary Report")
            .reportType("SUPPLIER_SUMMARY")
            .category("SUPPLIER")
            .format(format)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateSupplierTransactionReport(Long supplierId, LocalDate startDate, 
                                                        LocalDate endDate, String format) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("supplierId", supplierId);
        
        ReportRequest request = ReportRequest.builder()
            .reportName("Supplier Transaction Report")
            .reportType("SUPPLIER_TRANSACTION")
            .category("SUPPLIER")
            .format(format)
            .startDate(startDate)
            .endDate(endDate)
            .parameters(parameters)
            .build();
        
        return generateReport(request);
    }
    
    @Override
    public SavedReport generateSupplierOutstandingReport(String format) {
        ReportRequest request = ReportRequest.builder()
            .reportName("Supplier Outstanding Report")
            .reportType("SUPPLIER_OUTSTANDING")
            .category("SUPPLIER")
            .format(format)
            .build();
        
        return generateReport(request);
    }
    
    // ===================================================================
    // SAVED REPORT OPERATIONS
    // ===================================================================
    
    @Override
    @Transactional(readOnly = true)
    public SavedReport getSavedReportById(Long id) {
        return savedReportRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Saved report not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public SavedReport getSavedReportByCode(String reportCode) {
        return savedReportRepository.findByReportCode(reportCode)
            .orElseThrow(() -> new RuntimeException("Saved report not found with code: " + reportCode));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SavedReport> getAllSavedReports() {
        return savedReportRepository.findAll(Sort.by(Sort.Direction.DESC, "generatedTime"));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<SavedReport> getAllSavedReports(Pageable pageable) {
        return savedReportRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SavedReport> getSavedReportsByType(String reportType) {
        return savedReportRepository.findByReportType(reportType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<SavedReport> getSavedReportsByType(String reportType, Pageable pageable) {
        return savedReportRepository.findByReportType(reportType, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SavedReport> getSavedReportsByCategory(String category) {
        return savedReportRepository.findByCategory(category);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SavedReport> getSavedReportsByUser(Long userId) {
        return savedReportRepository.findByCreatedByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<SavedReport> getSavedReportsByUser(Long userId, Pageable pageable) {
        return savedReportRepository.findByCreatedByUserId(userId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SavedReport> getUserRecentReports(Long userId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return savedReportRepository.findUserRecentReports(userId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SavedReport> getUserFavoriteReports(Long userId) {
        return savedReportRepository.findUserFavoriteReports(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SavedReport> getPublicReports() {
        return savedReportRepository.findPublicReports();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<SavedReport> getPublicReports(Pageable pageable) {
        return savedReportRepository.findPublicReports(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<SavedReport> searchSavedReports(String keyword, Pageable pageable) {
        return savedReportRepository.searchReports(keyword, pageable);
    }
    
    @Override
    public void deleteSavedReport(Long id) {
        savedReportRepository.deleteById(id);
        log.info("Deleted saved report with id: {}", id);
    }
    
    @Override
    public SavedReport markAsFavorite(Long reportId) {
        SavedReport report = getSavedReportById(reportId);
//        report.setFavorite(true);
        return savedReportRepository.save(report);
    }
    
    @Override
    public SavedReport unmarkAsFavorite(Long reportId) {
        SavedReport report = getSavedReportById(reportId);
//        report.setFavorite(false);
        return savedReportRepository.save(report);
    }
    
    @Override
    public SavedReport makeReportPublic(Long reportId) {
        SavedReport report = getSavedReportById(reportId);
        report.setIsPublic(true);
        return savedReportRepository.save(report);
    }
    
    @Override
    public SavedReport makeReportPrivate(Long reportId) {
        SavedReport report = getSavedReportById(reportId);
        report.setIsPublic(false);
        return savedReportRepository.save(report);
    }
    
    // ===================================================================
    // SCHEDULED REPORTS
    // ===================================================================
    
    @Override
    public SavedReport scheduleReport(ReportRequest request, String frequency) {
        SavedReport report = SavedReport.builder()
            .reportCode(generateReportCode())
            .reportName(request.getReportName())
            .reportType(request.getReportType())
            .category(request.getCategory())
            .format(request.getFormat())
//            .scheduled(true)
//            .scheduleFrequency(frequency)
//            .nextScheduledRun(calculateNextScheduledRun(frequency))
            .status("SCHEDULED")
            .build();
        
        return savedReportRepository.save(report);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SavedReport> getScheduledReports() {
        return savedReportRepository.findByIsScheduled(true);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SavedReport> getScheduledReportsDue() {
        return savedReportRepository.findScheduledReportsDue(LocalDateTime.now());
    }
    
    @Override
    public int executeScheduledReports() {
        List<SavedReport> reports = getScheduledReportsDue();
        
        for (SavedReport report : reports) {
            try {
                regenerateReport(report.getId());
                
                // Update schedule
                //report// REMOVED setLastScheduledRun(LocalDateTime.now());
                //report// REMOVED setNextScheduledRun(calculateNextScheduledRun(report// REMOVED getScheduleFrequency()));
                savedReportRepository.save(report);
                
            } catch (Exception e) {
                log.error("Error executing scheduled report: {}", report.getId(), e);
            }
        }
        
        return reports.size();
    }
    
    @Override
    public SavedReport cancelScheduledReport(Long reportId) {
        SavedReport report = getSavedReportById(reportId);
//        report.setScheduled(false);
        //report// REMOVED setScheduleFrequency(null);
        //report// REMOVED setNextScheduledRun(null);
        return savedReportRepository.save(report);
    }
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getReportStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalReports", savedReportRepository.count());
        stats.put("completedReports", savedReportRepository.countByStatus("COMPLETED"));
        stats.put("failedReports", savedReportRepository.countByStatus("FAILED"));
        stats.put("scheduledReports", savedReportRepository.findByIsScheduled(true).size());
        stats.put("averageGenerationTime", savedReportRepository.getAverageGenerationTime());
        stats.put("averageFileSize", savedReportRepository.getAverageFileSize());
        stats.put("totalFileSize", savedReportRepository.getTotalFileSize());
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getReportStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        List<SavedReport> reports = savedReportRepository.findByGeneratedTimeBetween(startDate, endDate);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalReports", reports.size());
        stats.put("completedReports", reports.stream().filter(r -> "COMPLETED".equals(r.getStatus())).count());
        stats.put("failedReports", reports.stream().filter(r -> "FAILED".equals(r.getStatus())).count());
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getReportTypeDistribution() {
        List<Object[]> results = savedReportRepository.getReportTypeDistribution();
        return convertToMapList(results, "reportType", "reportCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCategoryDistribution() {
        List<Object[]> results = savedReportRepository.getCategoryDistribution();
        return convertToMapList(results, "category", "reportCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getFormatDistribution() {
        List<Object[]> results = savedReportRepository.getFormatDistribution();
        return convertToMapList(results, "format", "reportCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMostGeneratedReports() {
        List<Object[]> results = savedReportRepository.findMostGeneratedReports();
        return convertToMapList(results, "reportType", "reportCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMostActiveUsers() {
        List<Object[]> results = savedReportRepository.getMostActiveUsers();
        return convertToMapList(results, "username", "reportCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getDailyReportCount(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = LocalDateTime.from(startDate);
        LocalDateTime end = LocalDateTime.from(endDate);
        
        List<Object[]> results = savedReportRepository.getDailyReportCount(start, end);
        return convertToMapList(results, "date", "reportCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("statistics", getReportStatistics());
        dashboard.put("typeDistribution", getReportTypeDistribution());
        dashboard.put("categoryDistribution", getCategoryDistribution());
        dashboard.put("mostGenerated", getMostGeneratedReports().stream().limit(10).collect(Collectors.toList()));
        dashboard.put("mostActiveUsers", getMostActiveUsers().stream().limit(10).collect(Collectors.toList()));
        
        return dashboard;
    }
    
    // ===================================================================
    // EXPORT METHODS (Placeholder - implement with actual export logic)
    // ===================================================================
    
    @Override
    public byte[] exportToPdf(SavedReport report) {
        // TODO: Implement PDF export logic
        return new byte[0];
    }
    
    @Override
    public byte[] exportToExcel(SavedReport report) {
        // TODO: Implement Excel export logic
        return new byte[0];
    }
    
    @Override
    public byte[] exportToCsv(SavedReport report) {
        // TODO: Implement CSV export logic
        return new byte[0];
    }
    
    @Override
    public String exportToHtml(SavedReport report) {
        // TODO: Implement HTML export logic
        return "";
    }
    
    // ===================================================================
    // CLEANUP
    // ===================================================================
    
    @Override
    public int deleteOldReports(int daysToKeep) {
        LocalDateTime beforeDate = LocalDateTime.now().minusDays(daysToKeep);
        savedReportRepository.deleteOldReports(beforeDate);
        log.info("Deleted reports older than {} days", daysToKeep);
        return 0; // Return actual count if needed
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getTotalFileSize() {
        return savedReportRepository.getTotalFileSize();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getUserTotalFileSize(Long userId) {
        return savedReportRepository.getTotalFileSizeByUser(userId);
    }
    
    // ===================================================================
    // HELPER METHODS
    // ===================================================================
    
    private String generateReportCode() {
        return "RPT" + System.currentTimeMillis();
    }
    
    private byte[] generateReportData(ReportRequest request) {
        // TODO: Implement actual report generation logic based on report type
        // This is a placeholder
        return new byte[0];
    }
    
    private int calculateRecordCount(byte[] reportData) {
        // TODO: Implement actual record count calculation
        return 0;
    }
    
    private byte[] loadReportFile(String filePath) {
        // TODO: Implement actual file loading logic
        return new byte[0];
    }
    
    private LocalDateTime calculateNextScheduledRun(String frequency) {
        LocalDateTime now = LocalDateTime.now();
        
        switch (frequency.toUpperCase()) {
            case "DAILY":
                return now.plusDays(1);
            case "WEEKLY":
                return now.plusWeeks(1);
            case "MONTHLY":
                return now.plusMonths(1);
            case "QUARTERLY":
                return now.plusMonths(3);
            case "YEARLY":
                return now.plusYears(1);
            default:
                return now.plusDays(1);
        }
    }
    
    private List<Map<String, Object>> convertToMapList(List<Object[]> results, String key1, String key2) {
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put(key1, result[0]);
                map.put(key2, result[1]);
                return map;
            })
            .collect(Collectors.toList());
    }
}
