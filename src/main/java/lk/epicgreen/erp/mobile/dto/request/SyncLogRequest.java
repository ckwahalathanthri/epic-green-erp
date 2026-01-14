package lk.epicgreen.erp.mobile.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.*;

import java.time.LocalDateTime;

/**
 * DTO for creating Sync Log
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncLogRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Device ID is required")
    @Size(max = 100, message = "Device ID must not exceed 100 characters")
    private String deviceId;

    @NotBlank(message = "Device type is required")
    @Pattern(regexp = "^(ANDROID|IOS)$", 
             message = "Device type must be either ANDROID or IOS")
    private String deviceType;

    @Size(max = 20, message = "App version must not exceed 20 characters")
    private String appVersion;

    @NotBlank(message = "Sync type is required")
    @Pattern(regexp = "^(FULL|INCREMENTAL|PUSH|PULL)$", 
             message = "Sync type must be one of: FULL, INCREMENTAL, PUSH, PULL")
    private String syncType;

    @NotBlank(message = "Sync direction is required")
    @Pattern(regexp = "^(UPLOAD|DOWNLOAD|BIDIRECTIONAL)$", 
             message = "Sync direction must be one of: UPLOAD, DOWNLOAD, BIDIRECTIONAL")
    private String syncDirection;

    @Pattern(regexp = "^(INITIATED|IN_PROGRESS|COMPLETED|FAILED|CANCELLED)$", 
             message = "Sync status must be one of: INITIATED, IN_PROGRESS, COMPLETED, FAILED, CANCELLED")
    private String syncStatus;

    @Min(value = 0, message = "Records uploaded must be >= 0")
    private Integer recordsUploaded;

    @Min(value = 0, message = "Records downloaded must be >= 0")
    private Integer recordsDownloaded;

    @Min(value = 0, message = "Conflicts detected must be >= 0")
    private Integer conflictsDetected;

    @Min(value = 0, message = "Conflicts resolved must be >= 0")
    private Integer conflictsResolved;

    @Size(max = 5000, message = "Error message must not exceed 5000 characters")
    private String errorMessage;

    @NotNull(message = "Started at is required")
    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    @Min(value = 0, message = "Duration seconds must be >= 0")
    private Integer durationSeconds;
}
