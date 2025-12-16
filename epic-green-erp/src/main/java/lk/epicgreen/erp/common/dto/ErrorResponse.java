package lk.epicgreen.erp.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Standardized error response structure
 * Used by GlobalExceptionHandler for all error responses
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    /**
     * Timestamp when error occurred
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    /**
     * HTTP status code
     */
    private int status;
    
    /**
     * HTTP status reason phrase (e.g., "Bad Request", "Not Found")
     */
    private String error;
    
    /**
     * Detailed error message
     */
    private String message;
    
    /**
     * Request path that caused the error
     */
    private String path;
    
    /**
     * Exception class name (for debugging, optional)
     */
    private String exception;
    
    /**
     * Unique error code for tracking (optional)
     */
    private String errorCode;
    
    /**
     * Field-level validation errors (for validation exceptions)
     * Key: field name, Value: error message
     */
    private Map<String, String> fieldErrors;
    
    /**
     * Stack trace (only in development mode)
     */
    private String trace;
    
    /**
     * Additional error details
     */
    private Object details;
    
    /**
     * List of sub-errors (for complex validation)
     */
    private List<SubError> subErrors;
    
    // ==================== Factory Methods ====================
    
    /**
     * Creates a basic error response
     * 
     * @param status HTTP status code
     * @param error HTTP status reason
     * @param message Error message
     * @param path Request path
     * @return ErrorResponse
     */
    public static ErrorResponse of(int status, String error, String message, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .build();
    }
    
    /**
     * Creates error response with exception details
     * 
     * @param status HTTP status code
     * @param error HTTP status reason
     * @param message Error message
     * @param path Request path
     * @param exception Exception class name
     * @return ErrorResponse
     */
    public static ErrorResponse of(int status, String error, String message, String path, String exception) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .exception(exception)
                .build();
    }
    
    /**
     * Creates error response with field validation errors
     * 
     * @param status HTTP status code
     * @param error HTTP status reason
     * @param message Error message
     * @param path Request path
     * @param fieldErrors Field-level errors
     * @return ErrorResponse
     */
    public static ErrorResponse ofValidation(int status, String error, String message, 
                                             String path, Map<String, String> fieldErrors) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .fieldErrors(fieldErrors)
                .build();
    }
    
    /**
     * Creates error response with error code
     * 
     * @param status HTTP status code
     * @param error HTTP status reason
     * @param message Error message
     * @param path Request path
     * @param errorCode Unique error code
     * @return ErrorResponse
     */
    public static ErrorResponse withCode(int status, String error, String message, 
                                         String path, String errorCode) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .errorCode(errorCode)
                .build();
    }
    
    // ==================== Builder Methods ====================
    
    /**
     * Adds field errors to this response
     * 
     * @param fieldErrors Field-level errors
     * @return This ErrorResponse instance
     */
    public ErrorResponse withFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
        return this;
    }
    
    /**
     * Adds stack trace to this response (development only)
     * 
     * @param trace Stack trace
     * @return This ErrorResponse instance
     */
    public ErrorResponse withTrace(String trace) {
        this.trace = trace;
        return this;
    }
    
    /**
     * Adds error code to this response
     * 
     * @param errorCode Unique error code
     * @return This ErrorResponse instance
     */
    public ErrorResponse withErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }
    
    /**
     * Adds additional details to this response
     * 
     * @param details Additional error details
     * @return This ErrorResponse instance
     */
    public ErrorResponse withDetails(Object details) {
        this.details = details;
        return this;
    }
    
    /**
     * Adds sub-errors to this response
     * 
     * @param subErrors List of sub-errors
     * @return This ErrorResponse instance
     */
    public ErrorResponse withSubErrors(List<SubError> subErrors) {
        this.subErrors = subErrors;
        return this;
    }
    
    // ==================== Nested Classes ====================
    
    /**
     * Represents a sub-error for detailed error reporting
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubError {
        /**
         * Object/Entity name where error occurred
         */
        private String object;
        
        /**
         * Field name where error occurred
         */
        private String field;
        
        /**
         * Rejected value
         */
        private Object rejectedValue;
        
        /**
         * Error message
         */
        private String message;
        
        /**
         * Error code
         */
        private String code;
    }
}
