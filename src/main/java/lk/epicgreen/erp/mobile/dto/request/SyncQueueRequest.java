package lk.epicgreen.erp.mobile.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for creating/updating Sync Queue
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncQueueRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Device ID is required")
    @Size(max = 100, message = "Device ID must not exceed 100 characters")
    private String deviceId;

    @NotBlank(message = "Entity type is required")
    @Size(max = 50, message = "Entity type must not exceed 50 characters")
    private String entityType;

    @NotNull(message = "Entity ID is required")
    private Long entityId;

    @NotBlank(message = "Operation type is required")
    @Pattern(regexp = "^(INSERT|UPDATE|DELETE)$", 
             message = "Operation type must be one of: INSERT, UPDATE, DELETE")
    private String operationType;

    private Map<String, Object> dataSnapshot;

    @Pattern(regexp = "^(PENDING|IN_PROGRESS|SYNCED|FAILED|CONFLICT)$", 
             message = "Sync status must be one of: PENDING, IN_PROGRESS, SYNCED, FAILED, CONFLICT")
    private String syncStatus;

    @Min(value = 1, message = "Priority must be >= 1")
    @Max(value = 10, message = "Priority must be <= 10")
    private Integer priority;

    @Min(value = 0, message = "Retry count must be >= 0")
    private Integer retryCount;

    @Min(value = 1, message = "Max retries must be >= 1")
    @Max(value = 10, message = "Max retries must be <= 10")
    private Integer maxRetries;

    @Size(max = 1000, message = "Error message must not exceed 1000 characters")
    private String errorMessage;

    private LocalDateTime syncedAt;
}
