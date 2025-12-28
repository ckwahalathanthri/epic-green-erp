package lk.epicgreen.erp.mobile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Sync Log response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncLogResponse {

    private Long id;
    private Long userId;
    private String username;
    private String deviceId;
    private String deviceType;
    private String appVersion;
    private String syncType;
    private String syncDirection;
    private String syncStatus;
    private Integer recordsUploaded;
    private Integer recordsDownloaded;
    private Integer conflictsDetected;
    private Integer conflictsResolved;
    private String errorMessage;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Integer durationSeconds;
}
