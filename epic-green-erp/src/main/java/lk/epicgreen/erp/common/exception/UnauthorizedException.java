package lk.epicgreen.erp.common.exception;

/**
 * Exception thrown when authentication fails
 * Results in HTTP 401 Unauthorized response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public class UnauthorizedException extends RuntimeException {
    
    private String errorCode;
    
    /**
     * Constructor with message only
     * 
     * @param message Error message
     */
    public UnauthorizedException(String message) {
        super(message);
    }
    
    /**
     * Constructor with message and cause
     * 
     * @param message Error message
     * @param cause Root cause
     */
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructor with message and error code
     * 
     * @param message Error message
     * @param errorCode Error code for categorization
     */
    public UnauthorizedException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    /**
     * Gets the error code
     * 
     * @return Error code
     */
    public String getErrorCode() {
        return errorCode;
    }
}
