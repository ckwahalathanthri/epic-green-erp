package lk.epicgreen.erp.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Result DTO for file import operations (Excel, CSV, etc.)
 * Provides detailed information about import success/failures
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImportResultDTO {
    
    /**
     * Whether import was successful
     */
    private boolean success;
    
    /**
     * Summary message
     */
    private String message;
    
    /**
     * Filename that was imported
     */
    private String fileName;
    
    /**
     * Total number of rows in file
     */
    private int totalRows;
    
    /**
     * Number of rows successfully imported
     */
    private int successCount;
    
    /**
     * Number of rows that failed
     */
    private int failureCount;
    
    /**
     * Number of rows skipped (duplicates, invalid format, etc.)
     */
    private int skippedCount;
    
    /**
     * Import start time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    
    /**
     * Import end time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    
    /**
     * Duration in milliseconds
     */
    private Long durationMs;
    
    /**
     * List of error messages for failed rows
     */
    @Builder.Default
    private List<ImportError> errors = new ArrayList<>();
    
    /**
     * List of warning messages
     */
    @Builder.Default
    private List<String> warnings = new ArrayList<>();
    
    /**
     * IDs of successfully created records
     */
    private List<Long> createdIds;
    
    // ==================== Helper Methods ====================
    
    /**
     * Adds an import error
     * 
     * @param rowNumber Row number where error occurred
     * @param field Field name with error
     * @param message Error message
     */
    public void addError(int rowNumber, String field, String message) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(ImportError.builder()
                .rowNumber(rowNumber)
                .field(field)
                .message(message)
                .build());
        failureCount++;
    }
    
    /**
     * Adds a warning message
     * 
     * @param warning Warning message
     */
    public void addWarning(String warning) {
        if (warnings == null) {
            warnings = new ArrayList<>();
        }
        warnings.add(warning);
    }
    
    /**
     * Increments success count
     */
    public void incrementSuccess() {
        successCount++;
    }
    
    /**
     * Increments skip count
     */
    public void incrementSkipped() {
        skippedCount++;
    }
    
    /**
     * Calculates success percentage
     * 
     * @return Success percentage (0-100)
     */
    public double getSuccessPercentage() {
        if (totalRows == 0) {
            return 0.0;
        }
        return (successCount * 100.0) / totalRows;
    }
    
    /**
     * Gets summary text
     * Example: "Successfully imported 95 of 100 rows (5 failed)"
     * 
     * @return Summary text
     */
    public String getSummaryText() {
        return String.format("Successfully imported %d of %d rows (%d failed, %d skipped)",
                successCount, totalRows, failureCount, skippedCount);
    }
    
    /**
     * Checks if there are any errors
     * 
     * @return true if errors exist
     */
    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }
    
    /**
     * Checks if there are any warnings
     * 
     * @return true if warnings exist
     */
    public boolean hasWarnings() {
        return warnings != null && !warnings.isEmpty();
    }
    
    // ==================== Nested Classes ====================
    
    /**
     * Import error details
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImportError {
        /**
         * Row number where error occurred (1-based)
         */
        private int rowNumber;
        
        /**
         * Field/column name with error
         */
        private String field;
        
        /**
         * Error message
         */
        private String message;
        
        /**
         * Actual value that caused the error
         */
        private String value;
        
        /**
         * Error code for categorization
         */
        private String errorCode;
    }
}
