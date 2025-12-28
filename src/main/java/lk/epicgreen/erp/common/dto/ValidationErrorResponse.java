package lk.epicgreen.erp.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Validation Error Response
 * Provides structured validation error information
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationErrorResponse {
    
    private boolean success;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    
    @Builder.Default
    private List<FieldError> errors = new ArrayList<>();
    
    /**
     * Field-level validation error
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FieldError {
        private String field;
        private String message;
        private Object rejectedValue;
    }
    
    /**
     * Add field error
     */
    public void addError(String field, String message, Object rejectedValue) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(FieldError.builder()
                .field(field)
                .message(message)
                .rejectedValue(rejectedValue)
                .build());
    }
    
    /**
     * Create validation error response
     */
    public static ValidationErrorResponse create(String message, String path) {
        return ValidationErrorResponse.builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .path(path)
                .errors(new ArrayList<>())
                .build();
    }
}
