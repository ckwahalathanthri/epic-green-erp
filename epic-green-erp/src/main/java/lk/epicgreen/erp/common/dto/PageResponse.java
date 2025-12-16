package lk.epicgreen.erp.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Generic pagination response wrapper
 * Used for all paginated list endpoints
 * 
 * @param <T> Type of content in the page
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> {
    
    /**
     * List of items in current page
     */
    private List<T> content;
    
    /**
     * Current page number (0-indexed)
     */
    private int pageNumber;
    
    /**
     * Number of items per page
     */
    private int pageSize;
    
    /**
     * Total number of items across all pages
     */
    private long totalElements;
    
    /**
     * Total number of pages
     */
    private int totalPages;
    
    /**
     * Whether this is the last page
     */
    private boolean last;
    
    /**
     * Whether this is the first page
     */
    private boolean first;
    
    /**
     * Whether the page is empty
     */
    private boolean empty;
    
    /**
     * Number of items in current page
     */
    private int numberOfElements;
    
    /**
     * Sort information (optional)
     */
    private String sort;
    
    // ==================== Factory Methods ====================
    
    /**
     * Creates PageResponse from Spring Data Page object
     * 
     * @param page Spring Data Page
     * @param <T> Type of content
     * @return PageResponse
     */
    public static <T> PageResponse<T> from(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .first(page.isFirst())
                .empty(page.isEmpty())
                .numberOfElements(page.getNumberOfElements())
                .sort(page.getSort().toString())
                .build();
    }
    
    /**
     * Creates PageResponse with custom content (for manual pagination)
     * 
     * @param content List of items
     * @param pageNumber Current page number
     * @param pageSize Items per page
     * @param totalElements Total items
     * @param <T> Type of content
     * @return PageResponse
     */
    public static <T> PageResponse<T> of(List<T> content, int pageNumber, int pageSize, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        return PageResponse.<T>builder()
                .content(content)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .last(pageNumber >= totalPages - 1)
                .first(pageNumber == 0)
                .empty(content.isEmpty())
                .numberOfElements(content.size())
                .build();
    }
    
    /**
     * Creates an empty PageResponse
     * 
     * @param pageNumber Current page number
     * @param pageSize Items per page
     * @param <T> Type of content
     * @return Empty PageResponse
     */
    public static <T> PageResponse<T> empty(int pageNumber, int pageSize) {
        return PageResponse.<T>builder()
                .content(List.of())
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(0)
                .totalPages(0)
                .last(true)
                .first(true)
                .empty(true)
                .numberOfElements(0)
                .build();
    }
    
    // ==================== Helper Methods ====================
    
    /**
     * Checks if there is a next page
     * 
     * @return true if not last page
     */
    public boolean hasNext() {
        return !last;
    }
    
    /**
     * Checks if there is a previous page
     * 
     * @return true if not first page
     */
    public boolean hasPrevious() {
        return !first;
    }
    
    /**
     * Checks if the page has content
     * 
     * @return true if content is not empty
     */
    public boolean hasContent() {
        return !empty && content != null && !content.isEmpty();
    }
    
    /**
     * Gets the next page number
     * 
     * @return next page number, or current if last page
     */
    public int getNextPageNumber() {
        return hasNext() ? pageNumber + 1 : pageNumber;
    }
    
    /**
     * Gets the previous page number
     * 
     * @return previous page number, or 0 if first page
     */
    public int getPreviousPageNumber() {
        return hasPrevious() ? pageNumber - 1 : 0;
    }
    
    /**
     * Calculates the starting index for pagination display
     * Example: "Showing 21-40 of 100 items"
     * 
     * @return start index (1-based)
     */
    public int getStartIndex() {
        return empty ? 0 : (pageNumber * pageSize) + 1;
    }
    
    /**
     * Calculates the ending index for pagination display
     * Example: "Showing 21-40 of 100 items"
     * 
     * @return end index (1-based)
     */
    public int getEndIndex() {
        return empty ? 0 : getStartIndex() + numberOfElements - 1;
    }
    
    /**
     * Gets display text for pagination
     * Example: "Showing 21-40 of 100 items"
     * 
     * @param itemName Name of items (e.g., "products", "orders")
     * @return Display text
     */
    public String getDisplayText(String itemName) {
        if (empty) {
            return "No " + itemName + " found";
        }
        return String.format("Showing %d-%d of %d %s", 
                getStartIndex(), getEndIndex(), totalElements, itemName);
    }
}
