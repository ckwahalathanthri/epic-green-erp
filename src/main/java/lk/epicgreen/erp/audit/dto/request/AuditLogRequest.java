package lk.epicgreen.erp.audit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Map;

/**
 * DTO for creating Audit Log
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogRequest {

    private Long userId;

    @Size(max = 50, message = "Username must not exceed 50 characters")
    private String username;

    @NotBlank(message = "Action is required")
    @Size(max = 50, message = "Action must not exceed 50 characters")
    private String action;

    @NotBlank(message = "Entity type is required")
    @Size(max = 50, message = "Entity type must not exceed 50 characters")
    private String entityType;

    private Long entityId;

    @Size(max = 200, message = "Entity name must not exceed 200 characters")
    private String entityName;

    @NotBlank(message = "Operation type is required")
    @Pattern(regexp = "^(CREATE|UPDATE|DELETE|VIEW|APPROVE|REJECT|LOGIN|LOGOUT)$", 
             message = "Operation type must be one of: CREATE, UPDATE, DELETE, VIEW, APPROVE, REJECT, LOGIN, LOGOUT")
    private String operationType;

    private Map<String, Object> oldValues;

    private Map<String, Object> newValues;

    private Map<String, Object> changedFields;

    @Size(max = 45, message = "IP address must not exceed 45 characters")
    private String ipAddress;

    @Size(max = 1000, message = "User agent must not exceed 1000 characters")
    private String userAgent;

    @Size(max = 100, message = "Session ID must not exceed 100 characters")
    private String sessionId;

    @Pattern(regexp = "^(SUCCESS|FAILED)$", 
             message = "Status must be either SUCCESS or FAILED")
    private String status;

    @Size(max = 1000, message = "Error message must not exceed 1000 characters")
    private String errorMessage;
}
