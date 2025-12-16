package lk.epicgreen.erp.common.exception;

/**
 * Exception thrown when file storage operations fail
 * Results in HTTP 500 Internal Server Error response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public class FileStorageException extends RuntimeException {
    
    private String fileName;
    
    /**
     * Constructor with message only
     * 
     * @param message Error message
     */
    public FileStorageException(String message) {
        super(message);
    }
    
    /**
     * Constructor with message and cause
     * 
     * @param message Error message
     * @param cause Root cause
     */
    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructor with message and file name
     * 
     * @param message Error message
     * @param fileName Name of the file
     */
    public FileStorageException(String message, String fileName) {
        super(message);
        this.fileName = fileName;
    }
    
    /**
     * Constructor with message, file name, and cause
     * 
     * @param message Error message
     * @param fileName Name of the file
     * @param cause Root cause
     */
    public FileStorageException(String message, String fileName, Throwable cause) {
        super(message, cause);
        this.fileName = fileName;
    }
    
    /**
     * Gets the file name
     * 
     * @return File name
     */
    public String getFileName() {
        return fileName;
    }
}
