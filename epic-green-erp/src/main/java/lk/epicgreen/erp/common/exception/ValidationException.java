package lk.epicgreen.erp.common.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception thrown when validation fails
 * Results in HTTP 400 Bad Request response with field errors
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public class ValidationException extends RuntimeException {
    
    private Map<String, String> fieldErrors;
    
    /**
     * Constructor with message only
     * 
     * @param message Error message
     */
    public ValidationException(String message) {
        super(message);
        this.fieldErrors = new HashMap<>();
    }
    
    /**
     * Constructor with message and field errors
     * 
     * @param message Error message
     * @param fieldErrors Map of field names to error messages
     */
    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors;
    }
    
    /**
     * Constructor with single field error
     * 
     * @param message Error message
     * @param fieldName Field name
     * @param fieldError Field error message
     */
    public ValidationException(String message, String fieldName, String fieldError) {
        super(message);
        this.fieldErrors = new HashMap<>();
        this.fieldErrors.put(fieldName, fieldError);
    }
    
    /**
     * Adds a field error
     * 
     * @param fieldName Field name
     * @param errorMessage Error message
     */
    public void addFieldError(String fieldName, String errorMessage) {
        if (this.fieldErrors == null) {
            this.fieldErrors = new HashMap<>();
        }
        this.fieldErrors.put(fieldName, errorMessage);
    }
    
    /**
     * Gets all field errors
     * 
     * @return Map of field names to error messages
     */
    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
    
    /**
     * Checks if there are any field errors
     * 
     * @return true if field errors exist
     */
    public boolean hasFieldErrors() {
        return fieldErrors != null && !fieldErrors.isEmpty();
    }
}
