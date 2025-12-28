package lk.epicgreen.erp.mobile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for Sync Conflict response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncConflictResponse {

    private Long id;
    private Long userId;
    private String username;
    private String deviceId;
    private String entityType;
    private Long entityId;
    private Map<String, Object> serverData;
    private Map<String, Object> clientData;
    private String conflictType;
    private String resolutionStrategy;
    private String status;
    private Map<String, Object> resolvedData;
    private Long resolvedBy;
    private String resolvedByName;
    private LocalDateTime resolvedAt;
    private LocalDateTime detectedAt;
}
