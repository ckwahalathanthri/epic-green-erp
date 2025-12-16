package lk.epicgreen.erp.common.exception;

/**
 * Exception thrown when user doesn't have permission to access a resource
 * Results in HTTP 403 Forbidden response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public class ForbiddenException extends RuntimeException {
    
    private String requiredPermission;
    private String requiredRole;
    
    /**
     * Constructor with message only
     * 
     * @param message Error message
     */
    public ForbiddenException(String message) {
        super(message);
    }
    
    /**
     * Constructor with message and cause
     * 
     * @param message Error message
     * @param cause Root cause
     */
    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructor with message and required permission
     * 
     * @param message Error message
     * @param requiredPermission Required permission
     */
    public ForbiddenException(String message, String requiredPermission) {
        super(message);
        this.requiredPermission = requiredPermission;
    }
    
    /**
     * Gets the required permission
     * 
     * @return Required permission
     */
    public String getRequiredPermission() {
        return requiredPermission;
    }
    
    /**
     * Gets the required role
     * 
     * @return Required role
     */
    public String getRequiredRole() {
        return requiredRole;
    }
    
    /**
     * Sets the required role
     * 
     * @param requiredRole Required role
     */
    public void setRequiredRole(String requiredRole) {
        this.requiredRole = requiredRole;
    }
}
