package lk.epicgreen.erp.audit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for Audit Log response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponse {

    private Long id;
    private Long userId;
    private String username;
    private String action;
    private String entityType;
    private Long entityId;
    private String entityName;
    private String operationType;
    private Map<String, Object> oldValues;
    private Map<String, Object> newValues;
    private Map<String, Object> changedFields;
    private String ipAddress;
    private String userAgent;
    private String sessionId;
    private String status;
    private String errorMessage;
    private LocalDateTime createdAt;
}
