# Report Module - Repository, Service & Controller

This directory contains **repository, service, and controller layers** for the report system in Epic Green ERP.

## 📦 Contents

### Repository (report/repository) - 1 File
1. **SavedReportRepository.java** - Saved report data access

### Service (report/service) - 2 Files
1. **ReportService.java** - Report service interface
2. **ReportServiceImpl.java** - Report service implementation

### Controller (report/controller) - 1 File
1. **ReportController.java** - REST controller for report endpoints

---

## 📊 1. Repository

### SavedReportRepository

**Purpose:** Data access for saved reports

**Key Methods:**
- **Find by code:** `findByReportCode()`, `existsByReportCode()`
- **Find by type:** `findByReportType()`
- **Find by category:** `findByCategory()`
- **Find by status:** `findByStatus()`
- **Find by format:** `findByFormat()`
- **Find by user:** `findByCreatedByUserId()`, `findByGeneratedByUserId()`
- **Find public:** `findByIsPublic()`, `findPublicReports()`
- **Find scheduled:** `findByIsScheduled()`, `findScheduledReportsDue()`
- **Find favorite:** `findByIsFavorite()`, `findUserFavoriteReports()`
- **Find recent:** `findRecentReports()`, `findUserRecentReports()`
- **Find completed:** `findCompletedReports()`
- **Search:** `searchReports(keyword)`
- **Statistics:** `countByReportType()`, `getReportTypeDistribution()`, `getMostGeneratedReports()`
- **Metrics:** `getAverageGenerationTime()`, `getAverageFileSize()`, `getTotalFileSize()`
- **Cleanup:** `deleteOldReports()`

**Total:** 60+ query methods

---

## 🔧 2. Service Layer

### ReportService Interface

**Purpose:** Define report service contract

**Method Categories:**
1. **Report Generation** (6 methods)
2. **Sales Reports** (7 methods)
3. **Inventory Reports** (6 methods)
4. **Purchase Reports** (4 methods)
5. **Production Reports** (5 methods)
6. **Financial Reports** (6 methods)
7. **Customer Reports** (4 methods)
8. **Supplier Reports** (3 methods)
9. **Saved Report Operations** (20 methods)
10. **Scheduled Reports** (5 methods)
11. **Statistics** (9 methods)
12. **Export** (4 methods)
13. **Cleanup** (3 methods)

**Total:** 82 methods

### ReportServiceImpl Implementation

**Purpose:** Implement report service business logic

**Report Types Supported:**

**Sales Reports:**
- Sales Summary
- Sales by Customer
- Sales by Product
- Sales by Sales Representative
- Daily Sales
- Monthly Sales
- Sales Trend

**Inventory Reports:**
- Stock Summary
- Stock by Warehouse
- Low Stock
- Stock Movement
- Stock Valuation
- Expiring Stock

**Purchase Reports:**
- Purchase Summary
- Purchase by Supplier
- Purchase by Product
- Monthly Purchase

**Production Reports:**
- Production Summary
- Production by Product
- Production Efficiency
- Raw Material Consumption
- Daily Production

**Financial Reports:**
- Payment Summary
- Outstanding Payments
- Payment by Customer
- Profit & Loss
- Balance Sheet
- Cash Flow

**Customer Reports:**
- Customer Summary
- Customer Transaction
- Customer Outstanding
- Top Customers

**Supplier Reports:**
- Supplier Summary
- Supplier Transaction
- Supplier Outstanding

---

## 🌐 3. Controller Layer

### ReportController

**Purpose:** REST API endpoints for report operations

**Endpoint Categories:**

**Report Generation:**
- `POST /api/reports/generate` - Generate report
- `POST /api/reports/generate/async` - Generate async
- `POST /api/reports/{id}/regenerate` - Regenerate
- `GET /api/reports/{id}/download` - Download report
- `GET /api/reports/code/{code}/download` - Download by code

**Sales Reports:**
- `POST /api/reports/sales/summary` - Sales summary
- `POST /api/reports/sales/by-customer` - By customer
- `POST /api/reports/sales/by-product` - By product
- `POST /api/reports/sales/daily` - Daily sales
- `POST /api/reports/sales/monthly` - Monthly sales

**Inventory Reports:**
- `POST /api/reports/inventory/stock-summary` - Stock summary
- `POST /api/reports/inventory/low-stock` - Low stock
- `POST /api/reports/inventory/stock-movement` - Movement
- `POST /api/reports/inventory/stock-valuation` - Valuation

**Production Reports:**
- `POST /api/reports/production/summary` - Production summary
- `POST /api/reports/production/daily` - Daily production

**Financial Reports:**
- `POST /api/reports/finance/payment-summary` - Payment summary
- `POST /api/reports/finance/outstanding-payments` - Outstanding
- `POST /api/reports/finance/profit-loss` - Profit & Loss

**Saved Report Operations:**
- `GET /api/reports` - Get all (paginated)
- `GET /api/reports/{id}` - Get by ID
- `GET /api/reports/code/{code}` - Get by code
- `GET /api/reports/type/{type}` - Get by type
- `GET /api/reports/category/{category}` - Get by category
- `GET /api/reports/my-reports` - User's reports
- `GET /api/reports/my-favorites` - Favorites
- `GET /api/reports/public` - Public reports
- `GET /api/reports/search` - Search reports
- `DELETE /api/reports/{id}` - Delete report
- `PUT /api/reports/{id}/favorite` - Mark favorite
- `DELETE /api/reports/{id}/favorite` - Unmark favorite
- `PUT /api/reports/{id}/public` - Make public
- `PUT /api/reports/{id}/private` - Make private

**Scheduled Reports:**
- `POST /api/reports/schedule` - Schedule report
- `GET /api/reports/scheduled` - Get scheduled
- `DELETE /api/reports/{id}/schedule` - Cancel schedule

**Statistics:**
- `GET /api/reports/statistics` - Get statistics
- `GET /api/reports/statistics/type-distribution` - Type distribution
- `GET /api/reports/statistics/dashboard` - Dashboard stats

**Total:** 35+ REST endpoints

---

## 💡 Usage Examples

### Example 1: Generate Sales Summary Report

```java
@RestController
@RequiredArgsConstructor
public class SalesController {
    
    private final ReportService reportService;
    
    @GetMapping("/sales/reports/summary")
    public ResponseEntity<?> generateSalesSummary(
        @RequestParam LocalDate startDate,
        @RequestParam LocalDate endDate
    ) {
        SavedReport report = reportService.generateSalesSummaryReport(
            startDate, 
            endDate, 
            "PDF"
        );
        
        return ResponseEntity.ok(report);
    }
}
```

### Example 2: Generate Custom Report

```java
ReportRequest request = ReportRequest.builder()
    .reportName("Custom Sales Report")
    .reportType("SALES_SUMMARY")
    .category("SALES")
    .format("EXCEL")
    .startDate(LocalDate.now().minusMonths(1).atStartOfDay())
    .endDate(LocalDate.now().atTime(LocalTime.MAX))
    .build();

SavedReport report = reportService.generateReport(request);
```

### Example 3: Download Report

```java
@GetMapping("/reports/{id}/download")
public ResponseEntity<byte[]> downloadReport(@PathVariable Long id) {
    SavedReport report = reportService.getSavedReportById(id);
    byte[] data = reportService.downloadReport(id);
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("attachment", 
        report.getReportCode() + ".pdf");
    
    return new ResponseEntity<>(data, headers, HttpStatus.OK);
}
```

### Example 4: Schedule Monthly Report

```java
ReportRequest request = ReportRequest.builder()
    .reportName("Monthly Sales Report")
    .reportType("MONTHLY_SALES")
    .category("SALES")
    .format("PDF")
    .build();

SavedReport scheduledReport = reportService.scheduleReport(request, "MONTHLY");
```

### Example 5: Execute Scheduled Reports

```java
@Component
@RequiredArgsConstructor
public class ReportScheduler {
    
    private final ReportService reportService;
    
    @Scheduled(cron = "0 0 * * * ?") // Every hour
    public void executeScheduledReports() {
        int executed = reportService.executeScheduledReports();
        log.info("Executed {} scheduled reports", executed);
    }
}
```

### Example 6: Get User's Reports

```java
@GetMapping("/my-reports")
public ResponseEntity<Page<SavedReport>> getMyReports(
    @RequestParam Long userId,
    Pageable pageable
) {
    Page<SavedReport> reports = reportService.getSavedReportsByUser(userId, pageable);
    return ResponseEntity.ok(reports);
}
```

### Example 7: Search Reports

```java
@GetMapping("/reports/search")
public ResponseEntity<Page<SavedReport>> searchReports(
    @RequestParam String keyword,
    Pageable pageable
) {
    Page<SavedReport> reports = reportService.searchSavedReports(keyword, pageable);
    return ResponseEntity.ok(reports);
}
```

### Example 8: Mark as Favorite

```java
@PutMapping("/reports/{id}/favorite")
public ResponseEntity<SavedReport> markAsFavorite(@PathVariable Long id) {
    SavedReport report = reportService.markAsFavorite(id);
    return ResponseEntity.ok(report);
}
```

### Example 9: Get Report Statistics

```java
@GetMapping("/reports/statistics")
public ResponseEntity<Map<String, Object>> getStatistics() {
    Map<String, Object> stats = reportService.getReportStatistics();
    
    // Stats include:
    // - totalReports
    // - completedReports
    // - failedReports
    // - scheduledReports
    // - averageGenerationTime
    // - averageFileSize
    // - totalFileSize
    
    return ResponseEntity.ok(stats);
}
```

### Example 10: Cleanup Old Reports

```java
@Component
@RequiredArgsConstructor
public class ReportCleanupScheduler {
    
    private final ReportService reportService;
    
    @Scheduled(cron = "0 0 2 * * ?") // Run at 2 AM daily
    public void cleanupOldReports() {
        int deleted = reportService.deleteOldReports(90); // Keep 90 days
        log.info("Deleted {} old reports", deleted);
    }
}
```

---

## 📋 Report Formats Supported

- **PDF** - Portable Document Format
- **EXCEL** - Microsoft Excel (.xlsx)
- **CSV** - Comma Separated Values
- **HTML** - HTML Document

---

## 📋 Report Categories

1. **SALES** - Sales reports
2. **INVENTORY** - Inventory reports
3. **PURCHASE** - Purchase reports
4. **PRODUCTION** - Production reports
5. **FINANCE** - Financial reports
6. **CUSTOMER** - Customer reports
7. **SUPPLIER** - Supplier reports

---

## 📋 Report Status

- **PENDING** - Report queued for generation
- **PROCESSING** - Report being generated
- **COMPLETED** - Report successfully generated
- **FAILED** - Report generation failed
- **SCHEDULED** - Scheduled for future generation

---

## 📋 Schedule Frequencies

- **DAILY** - Every day
- **WEEKLY** - Every week
- **MONTHLY** - Every month
- **QUARTERLY** - Every 3 months
- **YEARLY** - Every year

---

## 🔒 Security

**Role-based Access Control:**

- **ADMIN** - Full access to all reports
- **MANAGER** - Generate and view reports
- **REPORT_GENERATOR** - Generate reports
- **USER** - View reports only

**Endpoint Security:**
- All endpoints require authentication
- `@PreAuthorize` annotations on all endpoints
- Role-based authorization

---

## 📁 Directory Structure

```
report/
├── repository/
│   ├── SavedReportRepository.java (60+ methods)
│   └── README.md
├── service/
│   ├── ReportService.java (82 methods)
│   ├── ReportServiceImpl.java
│   └── README.md
└── controller/
    ├── ReportController.java (35+ endpoints)
    └── README.md
```

---

## ✅ Summary

✅ **1 Repository** - 60+ query methods  
✅ **2 Service files** - 82 business logic methods  
✅ **1 Controller** - 35+ REST endpoints  
✅ **25+ Report types** - Comprehensive report coverage  
✅ **Sales reports** - 7 types  
✅ **Inventory reports** - 6 types  
✅ **Purchase reports** - 4 types  
✅ **Production reports** - 5 types  
✅ **Financial reports** - 6 types  
✅ **Customer reports** - 4 types  
✅ **Supplier reports** - 3 types  
✅ **Multiple formats** - PDF, Excel, CSV, HTML  
✅ **Scheduled reports** - Auto-generate on schedule  
✅ **Report management** - Save, search, favorite, public/private  
✅ **Statistics** - Comprehensive analytics  
✅ **Download** - Direct file download  
✅ **Async generation** - Non-blocking report generation  
✅ **User reports** - Personal report management  
✅ **Favorite system** - Mark favorite reports  
✅ **Public/Private** - Share reports publicly  
✅ **Search** - Full-text search  
✅ **Pagination** - Page-based results  
✅ **Role-based security** - Secure access control  
✅ **Production-ready** - Enterprise-grade implementation  

**Everything you need for complete report management with 25+ report types covering sales, inventory, purchase, production, finance, customer and supplier operations, multiple format support (PDF, Excel, CSV, HTML), scheduled report generation, report favorites, public/private sharing, comprehensive statistics, and secure role-based access control in a spice production ERP!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.report
