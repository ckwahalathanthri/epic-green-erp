package lk.epicgreen.erp.common.exception;

/**
 * Exception thrown when a requested resource is not found
 * Results in HTTP 404 Not Found response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public class ResourceNotFoundException extends RuntimeException {
    
    private String resourceName;
    private String fieldName;
    private Object fieldValue;
    
    /**
     * Constructor with message only
     * 
     * @param message Error message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Constructor with message and cause
     * 
     * @param message Error message
     * @param cause Root cause
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructor with resource details
     * 
     * @param resourceName Name of the resource (e.g., "Product")
     * @param fieldName Name of the field (e.g., "id")
     * @param fieldValue Value that was not found
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    
    /**
     * Gets the resource name
     * 
     * @return Resource name
     */
    public String getResourceName() {
        return resourceName;
    }
    
    /**
     * Gets the field name
     * 
     * @return Field name
     */
    public String getFieldName() {
        return fieldName;
    }
    
    /**
     * Gets the field value
     * 
     * @return Field value
     */
    public Object getFieldValue() {
        return fieldValue;
    }
}
