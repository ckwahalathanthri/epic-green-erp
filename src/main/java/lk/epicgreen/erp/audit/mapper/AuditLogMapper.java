package lk.epicgreen.erp.audit.mapper;

import lk.epicgreen.erp.audit.dto.request.AuditLogRequest;
import lk.epicgreen.erp.audit.dto.response.AuditLogResponse;
import lk.epicgreen.erp.audit.entity.AuditLog;
import org.springframework.stereotype.Component;

/**
 * Mapper for AuditLog entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class AuditLogMapper {

    public AuditLog toEntity(AuditLogRequest request) {
        if (request == null) {
            return null;
        }

        return AuditLog.builder()
//            .userId(request.getUserId())
            .username(request.getUsername())
            .action(request.getAction())
            .entityType(request.getEntityType())
            .entityId(request.getEntityId())
            .entityName(request.getEntityName())
            .operationType(request.getOperationType())
            .oldValues(request.getOldValues().toString())
            .newValues(request.getNewValues().toString())
            .changedFields(request.getChangedFields().toString())
            .ipAddress(request.getIpAddress())
            .userAgent(request.getUserAgent())
            .sessionId(request.getSessionId())
            .status(request.getStatus() != null ? request.getStatus() : "SUCCESS")
            .errorMessage(request.getErrorMessage())
            .build();
    }

    public AuditLogResponse toResponse(AuditLog auditLog) {
        if (auditLog == null) {
            return null;
        }

        return AuditLogResponse.builder()
            .id(auditLog.getId())
//            .userId(auditLog.getUserId())
            .username(auditLog.getUsername())
            .action(auditLog.getAction())
            .entityType(auditLog.getEntityType())
            .entityId(auditLog.getEntityId())
            .entityName(auditLog.getEntityName())
            .operationType(auditLog.getOperationType())
//            .oldValues(auditLog.getOldValues().toString())
//            .newValues(auditLog.getNewValues())
//            .changedFields(auditLog.getChangedFields())
            .ipAddress(auditLog.getIpAddress())
            .userAgent(auditLog.getUserAgent())
            .sessionId(auditLog.getSessionId())
            .status(auditLog.getStatus())
            .errorMessage(auditLog.getErrorMessage())
            .createdAt(auditLog.getCreatedAt())
            .build();
    }
}
