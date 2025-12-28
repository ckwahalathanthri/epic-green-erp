package lk.epicgreen.erp.audit.mapper;

import lk.epicgreen.erp.audit.dto.request.ActivityLogRequest;
import lk.epicgreen.erp.audit.dto.response.ActivityLogResponse;
import lk.epicgreen.erp.audit.entity.ActivityLog;
import org.springframework.stereotype.Component;

/**
 * Mapper for ActivityLog entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class ActivityLogMapper {

    public ActivityLog toEntity(ActivityLogRequest request) {
        if (request == null) {
            return null;
        }

        return ActivityLog.builder()
            .userId(request.getUserId())
            .activityType(request.getActivityType())
            .module(request.getModule())
            .activityDescription(request.getActivityDescription())
            .referenceType(request.getReferenceType())
            .referenceId(request.getReferenceId())
            .ipAddress(request.getIpAddress())
            .deviceType(request.getDeviceType())
            .build();
    }

    public ActivityLogResponse toResponse(ActivityLog activityLog) {
        if (activityLog == null) {
            return null;
        }

        return ActivityLogResponse.builder()
            .id(activityLog.getId())
            .userId(activityLog.getUserId())
            .activityType(activityLog.getActivityType())
            .module(activityLog.getModule())
            .activityDescription(activityLog.getActivityDescription())
            .referenceType(activityLog.getReferenceType())
            .referenceId(activityLog.getReferenceId())
            .ipAddress(activityLog.getIpAddress())
            .deviceType(activityLog.getDeviceType())
            .createdAt(activityLog.getCreatedAt())
            .build();
    }
}
