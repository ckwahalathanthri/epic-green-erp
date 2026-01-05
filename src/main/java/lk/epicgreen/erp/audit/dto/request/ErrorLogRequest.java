package lk.epicgreen.erp.audit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

/**
 * DTO for creating Error Log
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorLogRequest {

    @NotBlank(message = "Error type is required")
    @Size(max = 50, message = "Error type must not exceed 50 characters")
    private String errorType;

    @Size(max = 20, message = "Error code must not exceed 20 characters")
    private String errorCode;

    @NotBlank(message = "Error message is required")
    @Size(max = 5000, message = "Error message must not exceed 5000 characters")
    private String errorMessage;

    @Size(max = 10000, message = "Stack trace must not exceed 10000 characters")
    private String stackTrace;

    @Size(max = 500, message = "Request URL must not exceed 500 characters")
    private String requestUrl;

    @Size(max = 10, message = "Request method must not exceed 10 characters")
    private String requestMethod;

    @Size(max = 5000, message = "Request body must not exceed 5000 characters")
    private String requestBody;

    private Long userId;

    @Size(max = 45, message = "IP address must not exceed 45 characters")
    private String ipAddress;

    @Size(max = 1000, message = "User agent must not exceed 1000 characters")
    private String userAgent;

    @Pattern(regexp = "^(LOW|MEDIUM|HIGH|CRITICAL)$", 
             message = "Severity must be one of: LOW, MEDIUM, HIGH, CRITICAL")
    private String severity;

    private Boolean isResolved;

    private Long resolvedBy;
}
