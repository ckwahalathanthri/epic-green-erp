package lk.epicgreen.erp.mobilesync.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Sync Request DTO
 * Request from mobile device to sync data with server
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SyncRequest {
    
    /**
     * Device information
     */
    @NotBlank(message = "Device ID is required")
    private String deviceId;
    
    private String deviceName;
    
    private String deviceType; // ANDROID, IOS, WEB
    
    /**
     * User information
     */
    @NotNull(message = "User ID is required")
    private Long userId;
    
    /**
     * Sync details
     */
    @NotBlank(message = "Sync type is required")
    private String syncType; // FULL, INCREMENTAL, UPLOAD, DOWNLOAD
    
    private String syncDirection; // UPLOAD, DOWNLOAD, BIDIRECTIONAL
    
    /**
     * Entity type (for single entity sync)
     */
    private String entityType;
    
    /**
     * Entity ID (for single entity sync)
     */
    private Long entityId;
    
    /**
     * Session ID
     */
    private String sessionId;
    
    /**
     * Priority (1 = highest, 10 = lowest)
     */
    private Integer priority;
    
    /**
     * Last sync token (for incremental sync)
     */
    private String lastSyncToken;
    
    /**
     * Last sync time
     */
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime lastSyncTime;
    
    /**
     * Client timestamp
     */
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime clientTimestamp;
    
    /**
     * Client version
     */
    private String clientVersion;
    
    /**
     * Network type
     */
    private String networkType; // WIFI, MOBILE_DATA
    
    /**
     * Entity filters (which entities to sync)
     */
    private List<String> entityTypes;
    
    /**
     * Data to upload
     */
    @Valid
    private List<SyncDataItem> dataToUpload;
    
    /**
     * Conflict resolution preference
     */
    private String conflictResolutionPreference; // CLIENT_WINS, SERVER_WINS, MANUAL
    
    /**
     * Conflict resolution (alias for compatibility)
     */
    private String conflictResolution;
    
    /**
     * Sync Data Item
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SyncDataItem {
        
        @NotBlank(message = "Entity type is required")
        private String entityType;
        
        @NotNull(message = "Entity ID is required")
        private Long entityId;
        
        @NotBlank(message = "Operation type is required")
        private String operationType; // CREATE, UPDATE, DELETE
        
        private Long entityVersion;
        
        @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
        private LocalDateTime lastModifiedAt;
        
        private String entityData; // JSON string
        
        private Boolean forceUpload; // Force upload even if conflict
    }
}
