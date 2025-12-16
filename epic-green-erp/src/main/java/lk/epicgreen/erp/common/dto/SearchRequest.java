package lk.epicgreen.erp.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic search request with pagination, sorting, and filtering
 * Used for advanced search endpoints
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchRequest {
    
    /**
     * Search keyword/query
     */
    private String query;
    
    /**
     * Page number (0-indexed)
     */
    @Min(value = 0, message = "Page number cannot be negative")
    @Builder.Default
    private int page = 0;
    
    /**
     * Number of items per page
     */
    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size cannot exceed 100")
    @Builder.Default
    private int size = 20;
    
    /**
     * Field to sort by
     */
    @Builder.Default
    private String sortBy = "id";
    
    /**
     * Sort direction (ASC or DESC)
     */
    @Builder.Default
    private String sortDirection = "ASC";
    
    /**
     * List of filters to apply
     */
    @Builder.Default
    private List<FilterCriteria> filters = new ArrayList<>();
    
    /**
     * Whether to include deleted records
     */
    @Builder.Default
    private boolean includeDeleted = false;
    
    /**
     * Date range filter - from date
     */
    private String dateFrom;
    
    /**
     * Date range filter - to date
     */
    private String dateTo;
    
    // ==================== Helper Methods ====================
    
    /**
     * Converts this SearchRequest to Spring Pageable
     * 
     * @return Pageable object
     */
    public Pageable toPageable() {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }
    
    /**
     * Adds a filter criteria
     * 
     * @param field Field name
     * @param operation Filter operation (EQUALS, LIKE, etc.)
     * @param value Filter value
     * @return This SearchRequest instance
     */
    public SearchRequest addFilter(String field, FilterOperation operation, Object value) {
        if (filters == null) {
            filters = new ArrayList<>();
        }
        filters.add(FilterCriteria.builder()
                .field(field)
                .operation(operation)
                .value(value)
                .build());
        return this;
    }
    
    /**
     * Checks if search query is present
     * 
     * @return true if query is not empty
     */
    public boolean hasQuery() {
        return query != null && !query.trim().isEmpty();
    }
    
    /**
     * Checks if filters are present
     * 
     * @return true if filters list is not empty
     */
    public boolean hasFilters() {
        return filters != null && !filters.isEmpty();
    }
    
    /**
     * Checks if date range filter is present
     * 
     * @return true if both dateFrom and dateTo are set
     */
    public boolean hasDateRange() {
        return dateFrom != null && dateTo != null;
    }
    
    // ==================== Nested Classes ====================
    
    /**
     * Filter criteria for field-level filtering
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FilterCriteria {
        /**
         * Field name to filter on
         */
        private String field;
        
        /**
         * Filter operation
         */
        private FilterOperation operation;
        
        /**
         * Filter value
         */
        private Object value;
        
        /**
         * Logical operator to join with next filter (AND/OR)
         */
        @Builder.Default
        private String logicalOperator = "AND";
    }
    
    /**
     * Filter operations enum
     */
    public enum FilterOperation {
        EQUALS,           // field = value
        NOT_EQUALS,       // field != value
        GREATER_THAN,     // field > value
        LESS_THAN,        // field < value
        GREATER_OR_EQUAL, // field >= value
        LESS_OR_EQUAL,    // field <= value
        LIKE,             // field LIKE %value%
        STARTS_WITH,      // field LIKE value%
        ENDS_WITH,        // field LIKE %value
        IN,               // field IN (values)
        NOT_IN,           // field NOT IN (values)
        IS_NULL,          // field IS NULL
        IS_NOT_NULL,      // field IS NOT NULL
        BETWEEN           // field BETWEEN value1 AND value2
    }
}
