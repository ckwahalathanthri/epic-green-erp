package lk.epicgreen.erp.audit.service;

import lk.epicgreen.erp.audit.dto.request.ActivityLogRequest;
import lk.epicgreen.erp.audit.dto.response.ActivityLogResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for Activity Log entity business logic
 * 
 * Activity logs are immutable - no UPDATE or DELETE operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface ActivityLogService {

    /**
     * Create activity log entry
     */
    ActivityLogResponse createActivityLog(ActivityLogRequest request);

    /**
     * Get Activity Log by ID
     */
    ActivityLogResponse getActivityLogById(Long id);

    /**
     * Get all Activity Logs (paginated)
     */
    PageResponse<ActivityLogResponse> getAllActivityLogs(Pageable pageable);

    /**
     * Get Activity Logs by user
     */
    List<ActivityLogResponse> getActivityLogsByUser(Long userId);

    /**
     * Get Activity Logs by module
     */
    PageResponse<ActivityLogResponse> getActivityLogsByModule(String module, Pageable pageable);

    /**
     * Get Activity Logs by activity type
     */
    List<ActivityLogResponse> getActivityLogsByActivityType(String activityType);

    /**
     * Get Activity Logs by device type
     */
    PageResponse<ActivityLogResponse> getActivityLogsByDeviceType(String deviceType, Pageable pageable);

    /**
     * Get Activity Logs by date range
     */
    List<ActivityLogResponse> getActivityLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get Activity Logs by reference
     */
    List<ActivityLogResponse> getActivityLogsByReference(String referenceType, Long referenceId);

    /**
     * Get user activity summary
     */
    List<ActivityLogResponse> getUserActivitySummary(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Search Activity Logs
     */
    PageResponse<ActivityLogResponse> searchActivityLogs(String keyword, Pageable pageable);
}
