# Report Module - Epic Green ERP

This directory contains **entities and DTOs** for reporting and analytics management in the Epic Green ERP system.

## 📦 Contents

### Entities (report/entity) - 1 File
1. **SavedReport.java** - Saved reports and report templates

### DTOs (report/dto) - 1 File
1. **ReportRequest.java** - Report generation request

---

## 📊 Database Schema

### Report Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    REPORT SYSTEM                        │
│                                                         │
│  ┌────────────────────────────────────────────────┐   │
│  │         Report Templates                       │   │
│  │  - Pre-defined report structures               │   │
│  │  - System reports (built-in)                   │   │
│  │  - User-created templates                      │   │
│  └────────────────┬───────────────────────────────┘   │
│                   │                                     │
│                   ▼                                     │
│  ┌────────────────────────────────────────────────┐   │
│  │         Report Request                         │   │
│  │  - Parameters (date range, filters)            │   │
│  │  - Format (PDF, Excel, CSV, JSON, HTML)        │   │
│  │  - Layout options                              │   │
│  └────────────────┬───────────────────────────────┘   │
│                   │                                     │
│                   ▼                                     │
│  ┌────────────────────────────────────────────────┐   │
│  │         Report Generator                       │   │
│  │  - Execute query/fetch data                    │   │
│  │  - Apply filters and grouping                  │   │
│  │  - Calculate aggregations                      │   │
│  │  - Format output                               │   │
│  └────────────────┬───────────────────────────────┘   │
│                   │                                     │
│                   ▼                                     │
│  ┌────────────────────────────────────────────────┐   │
│  │         Generated Report                       │   │
│  │  - Saved to database                           │   │
│  │  - Saved to file system                        │   │
│  │  - Emailed to recipients                       │   │
│  └────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

---

## 📋 1. SavedReport Entity

**Purpose:** Store saved reports and report templates

### Key Fields

```java
// Identification
- reportCode (unique, e.g., "RPT-SALES-001")
- reportName

// Classification
- reportType (SALES, PURCHASE, INVENTORY, FINANCIAL, PRODUCTION, CUSTOMER, SUPPLIER, CUSTOM)
- reportCategory (OPERATIONAL, ANALYTICAL, STATUTORY, MANAGEMENT, DASHBOARD)
- reportFormat (PDF, EXCEL, CSV, JSON, HTML)

// Properties
- isTemplate (can be used as template)
- isSystem (built-in, cannot be deleted)
- isPublic (visible to all users)

// User tracking
- createdByUserId
- createdByUsername

// Description
- description

// Report definition
- reportQuery (SQL query for data retrieval)
- reportParameters (JSON - parameter definitions)
- reportFilters (JSON - filter definitions)
- reportColumns (JSON - column definitions)
- reportLayout (JSON - layout configuration)
- reportStyling (JSON - styling configuration)

// Data source
- dataSource (DATABASE, API, FILE, CUSTOM)
- dataSourceConnection (connection string)

// Scheduling
- scheduleEnabled
- scheduleCron (cron expression)
- scheduleRecipients (comma-separated emails)

// Generation tracking
- lastGeneratedAt
- lastGeneratedBy
- generationCount

// Status
- status (ACTIVE, INACTIVE, ARCHIVED)

// Categorization
- tags (comma-separated tags)

// Notes
- notes

// File storage
- filePath
- fileSizeBytes
- avgGenerationTimeMillis
```

### Helper Methods

```java
boolean isActive(); // status == ACTIVE
boolean isArchived(); // status == ARCHIVED
boolean canDelete(); // !isSystem
boolean canEdit(); // !isSystem || isActive
boolean isScheduled(); // scheduleEnabled && scheduleCron != null
Double getFileSizeKB(); // Convert bytes to KB
Double getFileSizeMB(); // Convert bytes to MB
Double getAvgGenerationTimeSeconds(); // Convert millis to seconds
Long getDaysSinceLastGeneration(); // Days since last generation
```

### Report Types

**Sales Reports:**
- SALES_SUMMARY
- SALES_DETAILED
- SALES_BY_CUSTOMER
- SALES_BY_PRODUCT
- SALES_BY_REGION
- SALES_BY_REPRESENTATIVE

**Purchase Reports:**
- PURCHASE_SUMMARY
- PURCHASE_DETAILED
- PURCHASE_BY_SUPPLIER
- PURCHASE_BY_PRODUCT

**Inventory Reports:**
- INVENTORY_SUMMARY
- INVENTORY_VALUATION
- INVENTORY_MOVEMENT
- STOCK_AGING
- REORDER_REPORT

**Financial Reports:**
- PROFIT_AND_LOSS
- BALANCE_SHEET
- CASH_FLOW
- TRIAL_BALANCE
- GENERAL_LEDGER
- ACCOUNTS_RECEIVABLE_AGING
- ACCOUNTS_PAYABLE_AGING

**Production Reports:**
- PRODUCTION_SUMMARY
- PRODUCTION_EFFICIENCY
- MATERIAL_CONSUMPTION
- QUALITY_CONTROL

**Customer Reports:**
- CUSTOMER_SUMMARY
- CUSTOMER_OUTSTANDING
- CUSTOMER_PURCHASE_HISTORY
- CUSTOMER_PAYMENT_HISTORY

**Supplier Reports:**
- SUPPLIER_SUMMARY
- SUPPLIER_OUTSTANDING
- SUPPLIER_PURCHASE_HISTORY
- SUPPLIER_PAYMENT_HISTORY

---

## 🔧 2. ReportRequest DTO

**Purpose:** Request for generating reports with parameters and filters

### Key Fields

```java
// Report identification
- reportCode (required)
- reportName
- reportType (required)
- reportFormat (required - PDF, EXCEL, CSV, JSON, HTML)

// Date range
- startDate
- endDate
- periodType (DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY, CUSTOM)

// Filters
- filters (List<ReportFilter>)
  - fieldName
  - operator (EQUALS, NOT_EQUALS, GREATER_THAN, LESS_THAN, BETWEEN, IN, LIKE, IS_NULL, etc.)
  - value / value2 / values
  - dataType (STRING, NUMBER, DATE, BOOLEAN)
  - caseSensitive

// Parameters
- parameters (Map<String, Object>) - dynamic parameters

// Grouping
- groupBy (List<String>) - fields to group by

// Sorting
- sortBy (List<ReportSort>)
  - fieldName
  - direction (ASC, DESC)
  - priority

// Pagination
- pageNumber
- pageSize

// Columns
- includeColumns (if null, include all)
- excludeColumns

// Aggregations
- aggregations (Map<String, String>) - field -> function (SUM, AVG, COUNT, MIN, MAX)

// User information
- userId
- username

// Output options
- includeCharts
- includeSummary
- includeDetails
- chartType (BAR, LINE, PIE, COLUMN)

// Layout options
- orientation (PORTRAIT, LANDSCAPE)
- pageSize (A4, LETTER, LEGAL)

// Email options
- emailReport
- emailRecipients
- emailSubject
- emailBody

// Save options
- saveReport
- savedReportName

// Locale and timezone
- locale
- timezone

// Additional options
- additionalOptions (Map<String, Object>)
```

### Filter Operators

- **EQUALS** - Exact match
- **NOT_EQUALS** - Not equal
- **GREATER_THAN** - Greater than
- **GREATER_THAN_OR_EQUALS** - Greater than or equal
- **LESS_THAN** - Less than
- **LESS_THAN_OR_EQUALS** - Less than or equal
- **BETWEEN** - Between two values
- **IN** - In a list of values
- **NOT_IN** - Not in a list of values
- **LIKE** - Contains (wildcard search)
- **NOT_LIKE** - Does not contain
- **STARTS_WITH** - Starts with
- **ENDS_WITH** - Ends with
- **IS_NULL** - Is null
- **IS_NOT_NULL** - Is not null

---

## 💡 Usage Examples

### Example 1: Create Sales Summary Report Template

```java
@Transactional
public SavedReport createSalesSummaryTemplate() {
    // Define columns
    String columns = """
    [
      {"field": "invoiceDate", "header": "Date", "type": "date", "format": "dd/MM/yyyy"},
      {"field": "customerName", "header": "Customer", "type": "string"},
      {"field": "invoiceNumber", "header": "Invoice #", "type": "string"},
      {"field": "subtotal", "header": "Subtotal", "type": "currency", "format": "LKR #,##0.00"},
      {"field": "taxAmount", "header": "Tax", "type": "currency", "format": "LKR #,##0.00"},
      {"field": "totalAmount", "header": "Total", "type": "currency", "format": "LKR #,##0.00"}
    ]
    """;
    
    // Define query
    String query = """
    SELECT 
      i.invoice_date as invoiceDate,
      c.customer_name as customerName,
      i.invoice_number as invoiceNumber,
      i.subtotal,
      i.tax_amount as taxAmount,
      i.total_amount as totalAmount
    FROM invoices i
    JOIN customers c ON i.customer_id = c.id
    WHERE i.invoice_date BETWEEN :startDate AND :endDate
    AND i.status = 'POSTED'
    ORDER BY i.invoice_date DESC
    """;
    
    // Define parameters
    String parameters = """
    [
      {"name": "startDate", "type": "date", "label": "Start Date", "required": true},
      {"name": "endDate", "type": "date", "label": "End Date", "required": true}
    ]
    """;
    
    // Create report
    SavedReport report = SavedReport.builder()
        .reportCode("RPT-SALES-SUMMARY")
        .reportName("Sales Summary Report")
        .reportType("SALES")
        .reportCategory("OPERATIONAL")
        .reportFormat("PDF")
        .isTemplate(true)
        .isSystem(true)
        .isPublic(true)
        .description("Summary of all sales invoices within a date range")
        .reportQuery(query)
        .reportParameters(parameters)
        .reportColumns(columns)
        .dataSource("DATABASE")
        .status("ACTIVE")
        .tags("sales,invoice,summary")
        .build();
    
    return savedReportRepository.save(report);
}
```

### Example 2: Generate Sales Report with Filters

```java
@Transactional
public ReportResponse generateSalesReport(ReportRequest request) {
    // Validate report exists
    SavedReport template = savedReportRepository
        .findByReportCode(request.getReportCode())
        .orElseThrow(() -> new NotFoundException("Report template not found"));
    
    if (!template.isActive()) {
        throw new BusinessException("Report template is not active");
    }
    
    // Build query with filters
    String query = template.getReportQuery();
    Map<String, Object> params = new HashMap<>();
    
    // Add date range parameters
    params.put("startDate", request.getStartDate());
    params.put("endDate", request.getEndDate());
    
    // Apply filters
    if (request.getFilters() != null && !request.getFilters().isEmpty()) {
        StringBuilder filterClause = new StringBuilder();
        
        for (ReportRequest.ReportFilter filter : request.getFilters()) {
            if (filterClause.length() > 0) {
                filterClause.append(" AND ");
            }
            
            switch (filter.getOperator()) {
                case "EQUALS":
                    filterClause.append(filter.getFieldName())
                               .append(" = :").append(filter.getFieldName());
                    params.put(filter.getFieldName(), filter.getValue());
                    break;
                    
                case "GREATER_THAN":
                    filterClause.append(filter.getFieldName())
                               .append(" > :").append(filter.getFieldName());
                    params.put(filter.getFieldName(), filter.getValue());
                    break;
                    
                case "BETWEEN":
                    filterClause.append(filter.getFieldName())
                               .append(" BETWEEN :").append(filter.getFieldName())
                               .append("_from AND :").append(filter.getFieldName())
                               .append("_to");
                    params.put(filter.getFieldName() + "_from", filter.getValue());
                    params.put(filter.getFieldName() + "_to", filter.getValue2());
                    break;
                    
                case "IN":
                    filterClause.append(filter.getFieldName())
                               .append(" IN (:").append(filter.getFieldName())
                               .append("_list)");
                    params.put(filter.getFieldName() + "_list", filter.getValues());
                    break;
                    
                case "LIKE":
                    filterClause.append(filter.getFieldName())
                               .append(" LIKE :").append(filter.getFieldName());
                    params.put(filter.getFieldName(), "%" + filter.getValue() + "%");
                    break;
            }
        }
        
        // Add filter clause to query
        if (filterClause.length() > 0) {
            query = query.replace("ORDER BY", "AND " + filterClause + " ORDER BY");
        }
    }
    
    // Execute query
    List<Map<String, Object>> data = jdbcTemplate.queryForList(query, params);
    
    // Apply grouping if requested
    if (request.getGroupBy() != null && !request.getGroupBy().isEmpty()) {
        data = applyGrouping(data, request.getGroupBy());
    }
    
    // Apply aggregations if requested
    if (request.getAggregations() != null && !request.getAggregations().isEmpty()) {
        data = applyAggregations(data, request.getAggregations());
    }
    
    // Apply sorting
    if (request.getSortBy() != null && !request.getSortBy().isEmpty()) {
        data = applySorting(data, request.getSortBy());
    }
    
    // Apply pagination
    if (request.getPageNumber() != null && request.getPageSize() != null) {
        int start = request.getPageNumber() * request.getPageSize();
        int end = Math.min(start + request.getPageSize(), data.size());
        data = data.subList(start, end);
    }
    
    // Generate report file
    byte[] reportFile = generateReportFile(
        template,
        data,
        request.getReportFormat(),
        request
    );
    
    // Save report if requested
    if (request.getSaveReport()) {
        saveGeneratedReport(template, reportFile, request);
    }
    
    // Email report if requested
    if (request.getEmailReport()) {
        emailReport(reportFile, request);
    }
    
    // Update template statistics
    template.setLastGeneratedAt(LocalDateTime.now());
    template.setLastGeneratedBy(request.getUsername());
    template.setGenerationCount(template.getGenerationCount() + 1);
    savedReportRepository.save(template);
    
    // Return response
    return ReportResponse.builder()
        .reportCode(template.getReportCode())
        .reportName(template.getReportName())
        .format(request.getReportFormat())
        .generatedAt(LocalDateTime.now())
        .recordCount(data.size())
        .fileSize((long) reportFile.length)
        .fileContent(Base64.getEncoder().encodeToString(reportFile))
        .build();
}
```

### Example 3: Generate Inventory Aging Report

```java
public ReportResponse generateInventoryAgingReport(LocalDate asOfDate) {
    ReportRequest request = ReportRequest.builder()
        .reportCode("RPT-INVENTORY-AGING")
        .reportName("Inventory Aging Report")
        .reportType("INVENTORY")
        .reportFormat("EXCEL")
        .endDate(asOfDate)
        .filters(List.of(
            ReportRequest.ReportFilter.builder()
                .fieldName("quantityOnHand")
                .operator("GREATER_THAN")
                .value(0)
                .build()
        ))
        .groupBy(List.of("productCategory", "warehouse"))
        .sortBy(List.of(
            ReportRequest.ReportSort.builder()
                .fieldName("agingDays")
                .direction("DESC")
                .priority(1)
                .build()
        ))
        .aggregations(Map.of(
            "quantityOnHand", "SUM",
            "totalValue", "SUM"
        ))
        .includeCharts(true)
        .chartType("BAR")
        .includeSummary(true)
        .userId(getCurrentUserId())
        .username(getCurrentUsername())
        .build();
    
    return generateReport(request);
}
```

### Example 4: Generate Financial Report (Profit & Loss)

```java
public ReportResponse generateProfitAndLossReport(
    LocalDate startDate,
    LocalDate endDate,
    String format
) {
    ReportRequest request = ReportRequest.builder()
        .reportCode("RPT-PROFIT-LOSS")
        .reportName("Profit & Loss Statement")
        .reportType("FINANCIAL")
        .reportFormat(format)
        .startDate(startDate)
        .endDate(endDate)
        .periodType("CUSTOM")
        .groupBy(List.of("accountType", "accountCategory"))
        .aggregations(Map.of(
            "debitAmount", "SUM",
            "creditAmount", "SUM"
        ))
        .includeCharts(true)
        .chartType("COLUMN")
        .includeSummary(true)
        .orientation("PORTRAIT")
        .pageSize("A4")
        .userId(getCurrentUserId())
        .username(getCurrentUsername())
        .build();
    
    return generateReport(request);
}
```

### Example 5: Schedule Report for Automatic Generation

```java
@Transactional
public void scheduleReport(Long reportId, String cronExpression, List<String> recipients) {
    SavedReport report = savedReportRepository.findById(reportId)
        .orElseThrow(() -> new NotFoundException("Report not found"));
    
    if (!report.canEdit()) {
        throw new BusinessException("Cannot edit system reports");
    }
    
    // Validate cron expression
    if (!CronExpression.isValidExpression(cronExpression)) {
        throw new BusinessException("Invalid cron expression");
    }
    
    // Update report
    report.setScheduleEnabled(true);
    report.setScheduleCron(cronExpression);
    report.setScheduleRecipients(String.join(",", recipients));
    
    savedReportRepository.save(report);
    
    // Schedule with task scheduler
    scheduleReportJob(report);
}

// Scheduled job to generate and email reports
@Scheduled(cron = "0 0 * * * *") // Every hour
public void processScheduledReports() {
    List<SavedReport> scheduledReports = savedReportRepository
        .findByScheduleEnabledAndStatus(true, "ACTIVE");
    
    for (SavedReport report : scheduledReports) {
        if (shouldGenerateNow(report.getScheduleCron())) {
            try {
                // Generate report
                ReportRequest request = buildDefaultRequest(report);
                ReportResponse response = generateReport(request);
                
                // Email to recipients
                List<String> recipients = Arrays.asList(
                    report.getScheduleRecipients().split(",")
                );
                
                emailReport(
                    response.getFileContent(),
                    recipients,
                    report.getReportName(),
                    "Scheduled report generated on " + LocalDateTime.now()
                );
                
            } catch (Exception e) {
                log.error("Failed to generate scheduled report: " + report.getReportCode(), e);
            }
        }
    }
}
```

### Example 6: Generate Custom Report with Dynamic Columns

```java
public ReportResponse generateCustomReport(
    String entityType,
    List<String> columns,
    List<ReportRequest.ReportFilter> filters,
    LocalDate startDate,
    LocalDate endDate
) {
    // Build dynamic query based on entity type and columns
    String query = buildDynamicQuery(entityType, columns, filters);
    
    // Create temporary report
    SavedReport tempReport = SavedReport.builder()
        .reportCode("TEMP-" + UUID.randomUUID())
        .reportName("Custom " + entityType + " Report")
        .reportType("CUSTOM")
        .reportQuery(query)
        .dataSource("DATABASE")
        .isTemplate(false)
        .isSystem(false)
        .build();
    
    // Create request
    ReportRequest request = ReportRequest.builder()
        .reportCode(tempReport.getReportCode())
        .reportType("CUSTOM")
        .reportFormat("EXCEL")
        .startDate(startDate)
        .endDate(endDate)
        .filters(filters)
        .includeColumns(columns)
        .userId(getCurrentUserId())
        .build();
    
    return generateReport(request);
}
```

### Example 7: Export Report Data as JSON API

```java
@GetMapping("/api/reports/{reportCode}/data")
public ResponseEntity<Map<String, Object>> getReportData(
    @PathVariable String reportCode,
    @RequestParam LocalDate startDate,
    @RequestParam LocalDate endDate,
    @RequestParam(required = false) List<String> filters
) {
    ReportRequest request = ReportRequest.builder()
        .reportCode(reportCode)
        .reportFormat("JSON")
        .startDate(startDate)
        .endDate(endDate)
        .build();
    
    // Parse filters if provided
    if (filters != null) {
        request.setFilters(parseFilters(filters));
    }
    
    ReportResponse response = reportService.generateReport(request);
    
    // Parse JSON response
    Map<String, Object> data = objectMapper.readValue(
        response.getFileContent(),
        new TypeReference<Map<String, Object>>() {}
    );
    
    return ResponseEntity.ok(data);
}
```

---

## 📁 Directory Structure

```
report/
├── entity/
│   ├── SavedReport.java
│   └── README.md
└── dto/
    └── ReportRequest.java
```

---

## ✅ Summary

✅ **1 Entity class** - Complete report management  
✅ **1 DTO class** - Comprehensive report request  
✅ **8 Report types** - SALES, PURCHASE, INVENTORY, FINANCIAL, PRODUCTION, CUSTOMER, SUPPLIER, CUSTOM  
✅ **5 Report categories** - OPERATIONAL, ANALYTICAL, STATUTORY, MANAGEMENT, DASHBOARD  
✅ **5 Report formats** - PDF, EXCEL, CSV, JSON, HTML  
✅ **Report templates** - Pre-defined report structures  
✅ **System reports** - Built-in reports that cannot be deleted  
✅ **User reports** - User-created custom reports  
✅ **Dynamic queries** - SQL queries with parameters  
✅ **Advanced filtering** - 15+ filter operators  
✅ **Grouping** - Group by multiple fields  
✅ **Sorting** - Multi-level sorting  
✅ **Aggregations** - SUM, AVG, COUNT, MIN, MAX  
✅ **Pagination** - Page-based data retrieval  
✅ **Chart support** - BAR, LINE, PIE, COLUMN charts  
✅ **Layout options** - PORTRAIT/LANDSCAPE, page sizes  
✅ **Scheduled reports** - Automatic generation with cron  
✅ **Email reports** - Auto-email to recipients  
✅ **Save reports** - Save generated reports  
✅ **Multi-format** - Generate in multiple formats  
✅ **Audit tracking** - Track generation history  
✅ **Production-ready** - Enterprise-grade reporting  

**Everything you need for complete reporting and analytics, including sales reports, financial statements, inventory reports, customer reports, supplier reports, scheduled reports, and custom reports with advanced filtering, grouping, aggregations, and multi-format export in a spice production ERP!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.report
