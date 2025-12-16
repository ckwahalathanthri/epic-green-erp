package lk.epicgreen.erp.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Generic API Response wrapper for all REST endpoints
 * Provides consistent response structure across the application
 * 
 * @param <T> Type of data being returned
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    /**
     * Indicates if the request was successful
     */
    private boolean success;
    
    /**
     * Human-readable message about the operation
     */
    private String message;
    
    /**
     * The actual data payload
     */
    private T data;
    
    /**
     * Timestamp when the response was generated
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    /**
     * Request path that generated this response
     */
    private String path;
    
    /**
     * HTTP status code
     */
    private Integer status;
    
    /**
     * Additional metadata (optional)
     */
    private Object metadata;
    
    // ==================== Factory Methods ====================
    
    /**
     * Creates a successful response with data
     * 
     * @param data The data to return
     * @param <T> Type of data
     * @return ApiResponse with success=true
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates a successful response with data and message
     * 
     * @param data The data to return
     * @param message Success message
     * @param <T> Type of data
     * @return ApiResponse with success=true
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates a successful response with message only (no data)
     * 
     * @param message Success message
     * @param <T> Type of data
     * @return ApiResponse with success=true
     */
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates a successful response with data, message, and metadata
     * 
     * @param data The data to return
     * @param message Success message
     * @param metadata Additional metadata
     * @param <T> Type of data
     * @return ApiResponse with success=true
     */
    public static <T> ApiResponse<T> success(T data, String message, Object metadata) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .metadata(metadata)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates an error response with message
     * 
     * @param message Error message
     * @param <T> Type of data
     * @return ApiResponse with success=false
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates an error response with message and error details
     * 
     * @param message Error message
     * @param data Error details (can be validation errors map, etc.)
     * @param <T> Type of error details
     * @return ApiResponse with success=false
     */
    public static <T> ApiResponse<T> error(String message, T data) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates an error response with message, status, and path
     * 
     * @param message Error message
     * @param status HTTP status code
     * @param path Request path
     * @param <T> Type of data
     * @return ApiResponse with success=false
     */
    public static <T> ApiResponse<T> error(String message, Integer status, String path) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .status(status)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    // ==================== Builder Methods ====================
    
    /**
     * Sets the path for this response
     * 
     * @param path Request path
     * @return This ApiResponse instance
     */
    public ApiResponse<T> withPath(String path) {
        this.path = path;
        return this;
    }
    
    /**
     * Sets the status code for this response
     * 
     * @param status HTTP status code
     * @return This ApiResponse instance
     */
    public ApiResponse<T> withStatus(Integer status) {
        this.status = status;
        return this;
    }
    
    /**
     * Sets metadata for this response
     * 
     * @param metadata Additional metadata
     * @return This ApiResponse instance
     */
    public ApiResponse<T> withMetadata(Object metadata) {
        this.metadata = metadata;
        return this;
    }
}
