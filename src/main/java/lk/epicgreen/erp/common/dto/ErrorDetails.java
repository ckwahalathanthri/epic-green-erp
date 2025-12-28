package lk.epicgreen.erp.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Error Details
 * Provides detailed error information
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDetails {
    
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private String exception;
    private String trace;
    
    /**
     * Create error details
     */
    public static ErrorDetails create(int status, String error, String message, String path) {
        return ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .build();
    }
    
    /**
     * Create error details with exception
     */
    public static ErrorDetails create(int status, String error, String message, String path, Exception exception) {
        return ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .exception(exception.getClass().getName())
                .build();
    }
}
