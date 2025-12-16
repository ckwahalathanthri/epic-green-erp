package lk.epicgreen.erp.mobilesync.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Sync Response DTO
 * Response from server to mobile device after sync
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
public class SyncResponse {
    
    /**
     * Success flag
     */
    private boolean success;
    
    /**
     * Sync log ID
     */
    private Long syncLogId;
    
    /**
     * Sync log code
     */
    private String syncLogCode;
    
    /**
     * Sync status
     */
    private String syncStatus; // COMPLETED, PARTIAL, FAILED
    
    /**
     * Sync timestamp
     */
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime syncTimestamp;
    
    /**
     * New sync token (for next incremental sync)
     */
    private String newSyncToken;
    
    /**
     * Server timestamp
     */
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime serverTimestamp;
    
    /**
     * Server version
     */
    private String serverVersion;
    
    /**
     * Summary
     */
    private SyncSummary summary;
    
    /**
     * Upload results
     */
    private List<SyncResult> uploadResults;
    
    /**
     * Download data
     */
    private List<SyncDataItem> downloadData;
    
    /**
     * Conflicts
     */
    private List<SyncConflictInfo> conflicts;
    
    /**
     * Errors
     */
    private List<SyncError> errors;
    
    /**
     * Message
     */
    private String message;
    
    /**
     * Data (generic data payload)
     */
    private Object data;
    
    /**
     * Records synced count
     */
    private Integer recordsSynced;
    
    /**
     * Duration in milliseconds
     */
    private Long durationMillis;
    
    /**
     * Sync Summary
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SyncSummary {
        
        private Integer totalRecords;
        
        private Integer uploadedRecords;
        
        private Integer downloadedRecords;
        
        private Integer successfulRecords;
        
        private Integer failedRecords;
        
        private Integer conflictedRecords;
        
        private Long durationMillis;
        
        private Long dataSizeBytes;
    }
    
    /**
     * Sync Result (for uploaded data)
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SyncResult {
        
        private String entityType;
        
        private Long entityId;
        
        private String operationType;
        
        private String status; // SUCCESS, FAILED, CONFLICT
        
        private Long newEntityId; // For CREATE operations
        
        private Long newVersion; // New version after sync
        
        @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
        private LocalDateTime lastModifiedAt;
        
        private String errorMessage;
        
        private Long conflictId; // If conflict detected
    }
    
    /**
     * Sync Data Item (for download)
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SyncDataItem {
        
        private String entityType;
        
        private Long entityId;
        
        private String operationType; // CREATE, UPDATE, DELETE
        
        private Long entityVersion;
        
        @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
        private LocalDateTime lastModifiedAt;
        
        private String entityData; // JSON string
        
        private Boolean isDeleted;
    }
    
    /**
     * Sync Conflict Info
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SyncConflictInfo {
        
        private Long conflictId;
        
        private String entityType;
        
        private Long entityId;
        
        private String conflictType; // VERSION_MISMATCH, CONCURRENT_UPDATE, DELETE_CONFLICT
        
        private Long clientVersion;
        
        private Long serverVersion;
        
        private String clientData; // JSON string
        
        private String serverData; // JSON string
        
        private String conflictDetails;
        
        private String suggestedResolution; // CLIENT_WINS, SERVER_WINS, MERGE
        
        private Boolean requiresManualResolution;
    }
    
    /**
     * Sync Error
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SyncError {
        
        private String entityType;
        
        private Long entityId;
        
        private String operationType;
        
        private String errorCode;
        
        private String errorMessage;
        
        private String errorDetails;
    }
}
