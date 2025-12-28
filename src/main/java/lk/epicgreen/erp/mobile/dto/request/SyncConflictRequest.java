package lk.epicgreen.erp.mobile.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Map;

/**
 * DTO for creating/updating Sync Conflict
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncConflictRequest {

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

    @NotNull(message = "Server data is required")
    private Map<String, Object> serverData;

    @NotNull(message = "Client data is required")
    private Map<String, Object> clientData;

    @NotBlank(message = "Conflict type is required")
    @Pattern(regexp = "^(UPDATE_UPDATE|UPDATE_DELETE|VERSION_MISMATCH)$", 
             message = "Conflict type must be one of: UPDATE_UPDATE, UPDATE_DELETE, VERSION_MISMATCH")
    private String conflictType;

    @Pattern(regexp = "^(SERVER_WINS|CLIENT_WINS|MANUAL|MERGE)$", 
             message = "Resolution strategy must be one of: SERVER_WINS, CLIENT_WINS, MANUAL, MERGE")
    private String resolutionStrategy;

    @Pattern(regexp = "^(DETECTED|RESOLVED|IGNORED)$", 
             message = "Status must be one of: DETECTED, RESOLVED, IGNORED")
    private String status;

    private Map<String, Object> resolvedData;

    private Long resolvedBy;
}
