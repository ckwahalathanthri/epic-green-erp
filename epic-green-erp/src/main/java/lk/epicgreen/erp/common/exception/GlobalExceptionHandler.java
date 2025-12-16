package lk.epicgreen.erp.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lk.epicgreen.erp.common.constants.ErrorMessages;
import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Global exception handler for the entire application
 * Catches all exceptions and returns standardized error responses
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    // ==================== Custom Application Exceptions ====================
    
    /**
     * Handles ResourceNotFoundException (404)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {
        
        log.error("Resource not found: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), errorResponse));
    }
    
    /**
     * Handles BusinessException (400)
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request) {
        
        log.error("Business exception: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        
        if (ex.getErrorCode() != null) {
            errorResponse.withErrorCode(ex.getErrorCode());
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), errorResponse));
    }
    
    /**
     * Handles ValidationException (400)
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleValidationException(
            ValidationException ex,
            HttpServletRequest request) {
        
        log.error("Validation exception: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.ofValidation(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                ex.getFieldErrors()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), errorResponse));
    }
    
    /**
     * Handles UnauthorizedException (401)
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleUnauthorizedException(
            UnauthorizedException ex,
            HttpServletRequest request) {
        
        log.error("Unauthorized: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        
        if (ex.getErrorCode() != null) {
            errorResponse.withErrorCode(ex.getErrorCode());
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(ex.getMessage(), errorResponse));
    }
    
    /**
     * Handles ForbiddenException (403)
     */
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleForbiddenException(
            ForbiddenException ex,
            HttpServletRequest request) {
        
        log.error("Forbidden: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(ex.getMessage(), errorResponse));
    }
    
    /**
     * Handles DuplicateResourceException (409)
     */
    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleDuplicateResourceException(
            DuplicateResourceException ex,
            HttpServletRequest request) {
        
        log.error("Duplicate resource: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage(), errorResponse));
    }
    
    /**
     * Handles FileStorageException (500)
     */
    @ExceptionHandler(FileStorageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleFileStorageException(
            FileStorageException ex,
            HttpServletRequest request) {
        
        log.error("File storage exception: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ex.getMessage(), errorResponse));
    }
    
    // ==================== Spring Validation Exceptions ====================
    
    /**
     * Handles MethodArgumentNotValidException (400)
     * Thrown when @Valid fails on request body
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        
        log.error("Validation failed: {}", ex.getMessage());
        
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        
        ErrorResponse errorResponse = ErrorResponse.ofValidation(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ErrorMessages.ERROR_VALIDATION_FAILED,
                request.getRequestURI(),
                fieldErrors
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ErrorMessages.ERROR_VALIDATION_FAILED, errorResponse));
    }
    
    /**
     * Handles ConstraintViolationException (400)
     * Thrown when @Validated fails on method parameters
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {
        
        log.error("Constraint violation: {}", ex.getMessage());
        
        Map<String, String> fieldErrors = new HashMap<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        
        violations.forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        
        ErrorResponse errorResponse = ErrorResponse.ofValidation(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ErrorMessages.ERROR_VALIDATION_FAILED,
                request.getRequestURI(),
                fieldErrors
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ErrorMessages.ERROR_VALIDATION_FAILED, errorResponse));
    }
    
    // ==================== Spring Security Exceptions ====================
    
    /**
     * Handles BadCredentialsException (401)
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request) {
        
        log.error("Bad credentials: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ErrorMessages.ERROR_INVALID_CREDENTIALS,
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(ErrorMessages.ERROR_INVALID_CREDENTIALS, errorResponse));
    }
    
    /**
     * Handles LockedException (401)
     */
    @ExceptionHandler(LockedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleLockedException(
            LockedException ex,
            HttpServletRequest request) {
        
        log.error("Account locked: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ErrorMessages.ERROR_ACCOUNT_LOCKED,
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(ErrorMessages.ERROR_ACCOUNT_LOCKED, errorResponse));
    }
    
    /**
     * Handles AuthenticationException (401)
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleAuthenticationException(
            AuthenticationException ex,
            HttpServletRequest request) {
        
        log.error("Authentication failed: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ErrorMessages.ERROR_UNAUTHORIZED,
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(ErrorMessages.ERROR_UNAUTHORIZED, errorResponse));
    }
    
    /**
     * Handles AccessDeniedException (403)
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request) {
        
        log.error("Access denied: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                ErrorMessages.ERROR_INSUFFICIENT_PERMISSIONS,
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(ErrorMessages.ERROR_INSUFFICIENT_PERMISSIONS, errorResponse));
    }
    
    // ==================== Database Exceptions ====================
    
    /**
     * Handles DataIntegrityViolationException (409)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {
        
        log.error("Data integrity violation: {}", ex.getMessage());
        
        String message = ErrorMessages.ERROR_CONSTRAINT_VIOLATION;
        
        // Determine specific constraint violation
        String exceptionMessage = ex.getMostSpecificCause().getMessage();
        if (exceptionMessage != null) {
            if (exceptionMessage.contains("foreign key")) {
                message = ErrorMessages.ERROR_FOREIGN_KEY_VIOLATION;
            } else if (exceptionMessage.contains("unique") || exceptionMessage.contains("duplicate")) {
                message = ErrorMessages.ERROR_UNIQUE_VIOLATION;
            }
        }
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(message, errorResponse));
    }
    
    // ==================== HTTP Exceptions ====================
    
    /**
     * Handles HttpMessageNotReadableException (400)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        
        log.error("Message not readable: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ErrorMessages.ERROR_INVALID_INPUT,
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ErrorMessages.ERROR_INVALID_INPUT, errorResponse));
    }
    
    /**
     * Handles HttpRequestMethodNotSupportedException (405)
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {
        
        log.error("Method not supported: {}", ex.getMessage());
        
        String message = String.format("Method '%s' is not supported for this endpoint", ex.getMethod());
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.error(message, errorResponse));
    }
    
    /**
     * Handles NoHandlerFoundException (404)
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleNoHandlerFound(
            NoHandlerFoundException ex,
            HttpServletRequest request) {
        
        log.error("No handler found: {}", ex.getMessage());
        
        String message = String.format("Endpoint '%s' not found", ex.getRequestURL());
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(message, errorResponse));
    }
    
    /**
     * Handles MissingServletRequestParameterException (400)
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleMissingParameter(
            MissingServletRequestParameterException ex,
            HttpServletRequest request) {
        
        log.error("Missing parameter: {}", ex.getMessage());
        
        String message = String.format("Required parameter '%s' is missing", ex.getParameterName());
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message, errorResponse));
    }
    
    /**
     * Handles MethodArgumentTypeMismatchException (400)
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        
        log.error("Type mismatch: {}", ex.getMessage());
        
        String message = String.format("Parameter '%s' should be of type '%s'", 
                ex.getName(), ex.getRequiredType().getSimpleName());
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message, errorResponse));
    }
    
    /**
     * Handles MaxUploadSizeExceededException (413)
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleMaxUploadSizeExceeded(
            MaxUploadSizeExceededException ex,
            HttpServletRequest request) {
        
        log.error("File too large: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.PAYLOAD_TOO_LARGE.value(),
                HttpStatus.PAYLOAD_TOO_LARGE.getReasonPhrase(),
                ErrorMessages.ERROR_FILE_TOO_LARGE,
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(ApiResponse.error(ErrorMessages.ERROR_FILE_TOO_LARGE, errorResponse));
    }
    
    // ==================== Generic Exception Handler ====================
    
    /**
     * Handles all other exceptions (500)
     * This is the catch-all handler
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleGenericException(
            Exception ex,
            HttpServletRequest request) {
        
        log.error("Internal server error: ", ex);
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ErrorMessages.ERROR_INTERNAL_SERVER,
                request.getRequestURI(),
                ex.getClass().getName()
        );
        
        // Add stack trace in development mode only
        // if (isDevelopmentMode()) {
        //     errorResponse.withTrace(getStackTrace(ex));
        // }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ErrorMessages.ERROR_INTERNAL_SERVER, errorResponse));
    }
}
