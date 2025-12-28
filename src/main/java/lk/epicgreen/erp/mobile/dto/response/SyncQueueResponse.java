package lk.epicgreen.erp.mobile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for Sync Queue response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncQueueResponse {

    private Long id;
    private Long userId;
    private String username;
    private String deviceId;
    private String entityType;
    private Long entityId;
    private String operationType;
    private Map<String, Object> dataSnapshot;
    private String syncStatus;
    private Integer priority;
    private Integer retryCount;
    private Integer maxRetries;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime syncedAt;
}
