package lk.epicgreen.erp.report.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Report Request DTO
 * Request for generating reports with parameters and filters
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportRequest {
    
    /**
     * Report identification
     */
    @NotBlank(message = "Report code is required")
    private String reportCode;
    
    private String reportName;
    
    /**
     * Report type (SALES, PURCHASE, INVENTORY, FINANCIAL, PRODUCTION, CUSTOMER, SUPPLIER, CUSTOM)
     */
    @NotBlank(message = "Report type is required")
    private String reportType;
    
    /**
     * Report format (PDF, EXCEL, CSV, JSON, HTML)
     */
    @NotBlank(message = "Report format is required")
    private String reportFormat;
    
    /**
     * Date range
     */
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate startDate;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate endDate;
    
    /**
     * Period type (DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY, CUSTOM)
     */
    private String periodType;
    
    /**
     * Filters
     */
    @Valid
    private List<ReportFilter> filters;
    
    /**
     * Parameters (dynamic report parameters)
     */
    private Map<String, Object> parameters;
    
    /**
     * Grouping
     */
    private List<String> groupBy;
    
    /**
     * Sorting
     */
    @Valid
    private List<ReportSort> sortBy;
    
    /**
     * Pagination
     */
    private Integer pageNumber;
    
    /**
     * Columns to include (if null, include all)
     */
    private List<String> includeColumns;
    
    /**
     * Columns to exclude
     */
    private List<String> excludeColumns;
    
    /**
     * Aggregations (SUM, AVG, COUNT, MIN, MAX)
     */
    private Map<String, String> aggregations;
    
    /**
     * User information
     */
    private Long userId;
    
    private String username;
    
    /**
     * Output options
     */
    private Boolean includeCharts;
    
    private Boolean includeSummary;
    
    private Boolean includeDetails;
    
    private String chartType; // BAR, LINE, PIE, COLUMN
    
    /**
     * Layout options
     */
    private String orientation; // PORTRAIT, LANDSCAPE
    
    private String pageSize; // A4, LETTER, LEGAL
    
    /**
     * Email options (if report should be emailed)
     */
    private Boolean emailReport;
    
    private List<String> emailRecipients;
    
    private String emailSubject;
    
    private String emailBody;
    
    /**
     * Save options
     */
    private Boolean saveReport;
    
    private String savedReportName;
    
    /**
     * Locale and timezone
     */
    private String locale;
    
    private String timezone;
    
    /**
     * Additional options
     */
    private Map<String, Object> additionalOptions;

    // ==================== NEW FIELDS FOR SERVICE LAYER ====================
    
    /**
     * Category (report category)
     */
    private String category;
    
    /**
     * Format (alias for reportFormat)
     */
    private String format;
    
    /**
     * Parameters as JSON string
     */
    private String parametersAsJson;

    // ==================== HELPER METHODS ====================
    
    /**
     * Gets category
     */
    public String getCategory() {
        return category;
    }
    
    /**
     * Gets format (format or reportFormat)
     */
    public String getFormat() {
        return format != null ? format : reportFormat;
    }
    
    /**
     * Gets parameters as JSON
     */
    public String getParametersAsJson() {
        return parametersAsJson;
    }
    
    /**
     * Report Filter
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReportFilter {
        
        @NotBlank(message = "Field name is required")
        private String fieldName;
        
        @NotBlank(message = "Operator is required")
        private String operator; // EQUALS, NOT_EQUALS, GREATER_THAN, LESS_THAN, BETWEEN, IN, NOT_IN, LIKE, IS_NULL, IS_NOT_NULL
        
        private Object value;
        
        private Object value2; // For BETWEEN operator
        
        private List<Object> values; // For IN, NOT_IN operators
        
        private String dataType; // STRING, NUMBER, DATE, BOOLEAN
        
        private Boolean caseSensitive;
    }
    
    /**
     * Report Sort
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReportSort {
        
        @NotBlank(message = "Field name is required")
        private String fieldName;
        
        @NotBlank(message = "Direction is required")
        private String direction; // ASC, DESC
        
        private Integer priority; // For multiple sorts
    }
    
    /**
     * Standard Report Types
     */
    public static class ReportTypes {
        // Sales Reports
        public static final String SALES_SUMMARY = "SALES_SUMMARY";
        public static final String SALES_DETAILED = "SALES_DETAILED";
        public static final String SALES_BY_CUSTOMER = "SALES_BY_CUSTOMER";
        public static final String SALES_BY_PRODUCT = "SALES_BY_PRODUCT";
        public static final String SALES_BY_REGION = "SALES_BY_REGION";
        public static final String SALES_BY_REPRESENTATIVE = "SALES_BY_REPRESENTATIVE";
        
        // Purchase Reports
        public static final String PURCHASE_SUMMARY = "PURCHASE_SUMMARY";
        public static final String PURCHASE_DETAILED = "PURCHASE_DETAILED";
        public static final String PURCHASE_BY_SUPPLIER = "PURCHASE_BY_SUPPLIER";
        public static final String PURCHASE_BY_PRODUCT = "PURCHASE_BY_PRODUCT";
        
        // Inventory Reports
        public static final String INVENTORY_SUMMARY = "INVENTORY_SUMMARY";
        public static final String INVENTORY_VALUATION = "INVENTORY_VALUATION";
        public static final String INVENTORY_MOVEMENT = "INVENTORY_MOVEMENT";
        public static final String STOCK_AGING = "STOCK_AGING";
        public static final String REORDER_REPORT = "REORDER_REPORT";
        
        // Financial Reports
        public static final String PROFIT_AND_LOSS = "PROFIT_AND_LOSS";
        public static final String BALANCE_SHEET = "BALANCE_SHEET";
        public static final String CASH_FLOW = "CASH_FLOW";
        public static final String TRIAL_BALANCE = "TRIAL_BALANCE";
        public static final String GENERAL_LEDGER = "GENERAL_LEDGER";
        public static final String ACCOUNTS_RECEIVABLE_AGING = "ACCOUNTS_RECEIVABLE_AGING";
        public static final String ACCOUNTS_PAYABLE_AGING = "ACCOUNTS_PAYABLE_AGING";
        
        // Production Reports
        public static final String PRODUCTION_SUMMARY = "PRODUCTION_SUMMARY";
        public static final String PRODUCTION_EFFICIENCY = "PRODUCTION_EFFICIENCY";
        public static final String MATERIAL_CONSUMPTION = "MATERIAL_CONSUMPTION";
        public static final String QUALITY_CONTROL = "QUALITY_CONTROL";
        
        // Customer Reports
        public static final String CUSTOMER_SUMMARY = "CUSTOMER_SUMMARY";
        public static final String CUSTOMER_OUTSTANDING = "CUSTOMER_OUTSTANDING";
        public static final String CUSTOMER_PURCHASE_HISTORY = "CUSTOMER_PURCHASE_HISTORY";
        public static final String CUSTOMER_PAYMENT_HISTORY = "CUSTOMER_PAYMENT_HISTORY";
        
        // Supplier Reports
        public static final String SUPPLIER_SUMMARY = "SUPPLIER_SUMMARY";
        public static final String SUPPLIER_OUTSTANDING = "SUPPLIER_OUTSTANDING";
        public static final String SUPPLIER_PURCHASE_HISTORY = "SUPPLIER_PURCHASE_HISTORY";
        public static final String SUPPLIER_PAYMENT_HISTORY = "SUPPLIER_PAYMENT_HISTORY";
    }
    
    /**
     * Filter Operators
     */
    public static class FilterOperators {
        public static final String EQUALS = "EQUALS";
        public static final String NOT_EQUALS = "NOT_EQUALS";
        public static final String GREATER_THAN = "GREATER_THAN";
        public static final String GREATER_THAN_OR_EQUALS = "GREATER_THAN_OR_EQUALS";
        public static final String LESS_THAN = "LESS_THAN";
        public static final String LESS_THAN_OR_EQUALS = "LESS_THAN_OR_EQUALS";
        public static final String BETWEEN = "BETWEEN";
        public static final String IN = "IN";
        public static final String NOT_IN = "NOT_IN";
        public static final String LIKE = "LIKE";
        public static final String NOT_LIKE = "NOT_LIKE";
        public static final String STARTS_WITH = "STARTS_WITH";
        public static final String ENDS_WITH = "ENDS_WITH";
        public static final String IS_NULL = "IS_NULL";
        public static final String IS_NOT_NULL = "IS_NOT_NULL";
    }
    
    /**
     * Builder class extensions for new fields
     */
    public static class ReportRequestBuilder {
        
        /**
         * Sets category
         */
        public ReportRequestBuilder category(String category) {
            this.category = category;
            return this;
        }
        
        /**
         * Sets format
         */
        public ReportRequestBuilder format(String format) {
            this.format = format;
            return this;
        }
        
        /**
         * Sets parameters as JSON
         */
        public ReportRequestBuilder parametersAsJson(String parametersAsJson) {
            this.parametersAsJson = parametersAsJson;
            return this;
        }
    }
}
