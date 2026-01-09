package lk.epicgreen.erp.audit.mapper;

import lk.epicgreen.erp.admin.repository.UserRepository;
import lk.epicgreen.erp.audit.dto.request.ActivityLogRequest;
import lk.epicgreen.erp.audit.dto.response.ActivityLogResponse;
import lk.epicgreen.erp.audit.entity.ActivityLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper for ActivityLog entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class ActivityLogMapper {
    @Autowired
    private UserRepository userRepository;

    public ActivityLog toEntity(ActivityLogRequest request) {
        if (request == null) {
            return null;
        }

        return ActivityLog.builder()
            .user(userRepository.findById(request.getUserId()).orElse(null))
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
            .userId(activityLog.getUser().getId())
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
