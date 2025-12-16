package lk.epicgreen.erp.common.exception;

/**
 * Exception thrown when business rules are violated
 * Results in HTTP 400 Bad Request response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public class BusinessException extends RuntimeException {
    
    private String errorCode;
    private Object[] params;
    
    /**
     * Constructor with message only
     * 
     * @param message Error message
     */
    public BusinessException(String message) {
        super(message);
    }
    
    /**
     * Constructor with message and cause
     * 
     * @param message Error message
     * @param cause Root cause
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructor with message and error code
     * 
     * @param message Error message
     * @param errorCode Error code for categorization
     */
    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    /**
     * Constructor with message, error code, and parameters
     * 
     * @param message Error message
     * @param errorCode Error code
     * @param params Additional parameters
     */
    public BusinessException(String message, String errorCode, Object... params) {
        super(message);
        this.errorCode = errorCode;
        this.params = params;
    }
    
    /**
     * Gets the error code
     * 
     * @return Error code
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Gets the parameters
     * 
     * @return Parameters array
     */
    public Object[] getParams() {
        return params;
    }
}
