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
 * Result DTO for bulk operations (delete, update, activate, etc.)
 * Provides detailed information about bulk operation success/failures
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BulkOperationResultDTO {
    
    /**
     * Operation type (DELETE, UPDATE, ACTIVATE, etc.)
     */
    private String operation;
    
    /**
     * Whether operation was successful for all items
     */
    private boolean success;
    
    /**
     * Summary message
     */
    private String message;
    
    /**
     * Total number of items in request
     */
    private int totalItems;
    
    /**
     * Number of items successfully processed
     */
    private int successCount;
    
    /**
     * Number of items that failed
     */
    private int failureCount;
    
    /**
     * List of successfully processed item IDs
     */
    private List<Long> successIds;
    
    /**
     * List of failed items with reasons
     */
    @Builder.Default
    private List<FailedItem> failedItems = new ArrayList<>();
    
    /**
     * Operation start time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    
    /**
     * Operation end time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    
    /**
     * Duration in milliseconds
     */
    private Long durationMs;
    
    // ==================== Factory Methods ====================
    
    /**
     * Creates a successful bulk operation result
     * 
     * @param operation Operation type
     * @param successIds List of successful IDs
     * @return BulkOperationResultDTO
     */
    public static BulkOperationResultDTO success(String operation, List<Long> successIds) {
        return BulkOperationResultDTO.builder()
                .operation(operation)
                .success(true)
                .totalItems(successIds.size())
                .successCount(successIds.size())
                .failureCount(0)
                .successIds(successIds)
                .message(String.format("Successfully processed %d items", successIds.size()))
                .build();
    }
    
    /**
     * Creates a partial success bulk operation result
     * 
     * @param operation Operation type
     * @param successIds List of successful IDs
     * @param failedItems List of failed items
     * @return BulkOperationResultDTO
     */
    public static BulkOperationResultDTO partial(String operation, List<Long> successIds, 
                                                 List<FailedItem> failedItems) {
        int total = successIds.size() + failedItems.size();
        return BulkOperationResultDTO.builder()
                .operation(operation)
                .success(false)
                .totalItems(total)
                .successCount(successIds.size())
                .failureCount(failedItems.size())
                .successIds(successIds)
                .failedItems(failedItems)
                .message(String.format("Processed %d of %d items successfully", 
                        successIds.size(), total))
                .build();
    }
    
    /**
     * Creates a failed bulk operation result
     * 
     * @param operation Operation type
     * @param failedItems List of failed items
     * @return BulkOperationResultDTO
     */
    public static BulkOperationResultDTO failure(String operation, List<FailedItem> failedItems) {
        return BulkOperationResultDTO.builder()
                .operation(operation)
                .success(false)
                .totalItems(failedItems.size())
                .successCount(0)
                .failureCount(failedItems.size())
                .failedItems(failedItems)
                .message("Bulk operation failed for all items")
                .build();
    }
    
    // ==================== Helper Methods ====================
    
    /**
     * Adds a failed item
     * 
     * @param id Item ID
     * @param reason Failure reason
     */
    public void addFailure(Long id, String reason) {
        if (failedItems == null) {
            failedItems = new ArrayList<>();
        }
        failedItems.add(FailedItem.builder()
                .id(id)
                .reason(reason)
                .build());
        failureCount++;
    }
    
    /**
     * Calculates success percentage
     * 
     * @return Success percentage (0-100)
     */
    public double getSuccessPercentage() {
        if (totalItems == 0) {
            return 0.0;
        }
        return (successCount * 100.0) / totalItems;
    }
    
    /**
     * Gets summary text
     * Example: "Successfully deleted 95 of 100 items (5 failed)"
     * 
     * @return Summary text
     */
    public String getSummaryText() {
        return String.format("Successfully %s %d of %d items (%d failed)",
                operation.toLowerCase(), successCount, totalItems, failureCount);
    }
    
    /**
     * Checks if all items were processed successfully
     * 
     * @return true if all successful
     */
    public boolean isFullSuccess() {
        return success && failureCount == 0;
    }
    
    /**
     * Checks if operation was partial success
     * 
     * @return true if some succeeded and some failed
     */
    public boolean isPartialSuccess() {
        return successCount > 0 && failureCount > 0;
    }
    
    /**
     * Checks if all items failed
     * 
     * @return true if all failed
     */
    public boolean isFullFailure() {
        return successCount == 0 && failureCount > 0;
    }
    
    // ==================== Nested Classes ====================
    
    /**
     * Failed item details
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FailedItem {
        /**
         * Item ID that failed
         */
        private Long id;
        
        /**
         * Item identifier (code, name, etc.)
         */
        private String identifier;
        
        /**
         * Failure reason
         */
        private String reason;
        
        /**
         * Error code for categorization
         */
        private String errorCode;
        
        /**
         * Additional error details
         */
        private String details;
    }
}
