package lk.epicgreen.erp.audit.service.impl;

import lk.epicgreen.erp.audit.dto.request.ActivityLogRequest;
import lk.epicgreen.erp.audit.dto.response.ActivityLogResponse;
import lk.epicgreen.erp.audit.entity.ActivityLog;
import lk.epicgreen.erp.audit.mapper.ActivityLogMapper;
import lk.epicgreen.erp.audit.repository.ActivityLogRepository;
import lk.epicgreen.erp.audit.service.ActivityLogService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ActivityLogService interface
 * 
 * Activity logs are immutable - no UPDATE or DELETE operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository activityLogRepository;
    private final ActivityLogMapper activityLogMapper;

    @Override
    @Transactional
    public ActivityLogResponse createActivityLog(ActivityLogRequest request) {
        log.debug("Creating activity log for user: {} in module: {}", request.getUserId(), request.getModule());

        ActivityLog activityLog = activityLogMapper.toEntity(request);
        ActivityLog savedActivityLog = activityLogRepository.save(activityLog);

        log.debug("Activity log created successfully with ID: {}", savedActivityLog.getId());

        return activityLogMapper.toResponse(savedActivityLog);
    }

    @Override
    public ActivityLogResponse getActivityLogById(Long id) {
        ActivityLog activityLog = findActivityLogById(id);
        return activityLogMapper.toResponse(activityLog);
    }
    public List<ActivityLog> getLoginActivitiesByUserId(Long userId){
        return activityLogRepository.findByUserId(userId);
    }

    @Override
    public PageResponse<ActivityLogResponse> getAllActivityLogs(Pageable pageable) {
        Page<ActivityLog> activityLogPage = activityLogRepository.findAll(pageable);
        return createPageResponse(activityLogPage);
    }

    @Override
    public List<ActivityLogResponse> getActivityLogsByUser(Long userId) {
        List<ActivityLog> activityLogs = activityLogRepository.findByUserId(userId);
        return activityLogs.stream()
            .map(activityLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    public Page<ActivityLog> getActivityLogsByUserId(Long userId,Pageable pageable){
        return activityLogRepository.findByUserId(userId,pageable);
    }

    @Override
    public PageResponse<ActivityLogResponse> getActivityLogsByModule(String module, Pageable pageable) {
        Page<ActivityLog> activityLogPage = activityLogRepository.findByModule(module, pageable);
        return createPageResponse(activityLogPage);
    }

    @Override
    public List<ActivityLogResponse> getActivityLogsByActivityType(String activityType) {
        List<ActivityLog> activityLogs = activityLogRepository.findByActivityType(activityType);
        return activityLogs.stream()
            .map(activityLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<ActivityLogResponse> getActivityLogsByDeviceType(String deviceType, Pageable pageable) {
        Page<ActivityLog> activityLogPage = activityLogRepository.findByDeviceType(deviceType, pageable);
        return createPageResponse(activityLogPage);
    }

    @Override
    public List<ActivityLogResponse> getActivityLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<ActivityLog> activityLogs = activityLogRepository.findByCreatedAtBetween(startDate, endDate);
        return activityLogs.stream()
            .map(activityLogMapper::toResponse)
            .collect(Collectors.toList());
    }

//    @Override
//    public List<ActivityLogResponse> getActivityLogsByReference(String referenceType, Long referenceId) {
//        List<ActivityLog> activityLogs = activityLogRepository.findByReferenceTypeAndReferenceId(referenceType, referenceId);
//        return activityLogs.stream()
//            .map(activityLogMapper::toResponse)
//            .collect(Collectors.toList());
//    }

//    @Override
//    public List<ActivityLogResponse> getUserActivitySummary(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
//        List<ActivityLog> activityLogs = activityLogRepository.findByUserIdAndCreatedAtBetween(userId, startDate, endDate);
//        return activityLogs.stream()
//            .map(activityLogMapper::toResponse)
//            .collect(Collectors.toList());
//    }

    public Page<ActivityLog> getActivityLogsByType(String activityType,Pageable pageable){
        return activityLogRepository.findByActivityType(activityType,pageable);
    }

//    @Override
//    public PageResponse<ActivityLogResponse> searchActivityLogs(String keyword, Pageable pageable) {
//        Page<ActivityLog> activityLogPage = activityLogRepository.searchActivityLogs(Long.valueOf(keyword),null,null,null,null,null,pageable);
//        return createPageResponse(activityLogPage);
//    }

    public List<ActivityLog> getRecentActivityLogs(Pageable limit){
        return activityLogRepository.findLatest(limit);
    }

    public void deleteOldActivityLogs(int daysToKeep){
        LocalDateTime cutoffDate=LocalDateTime.now().minusDays(daysToKeep);
        activityLogRepository.deleteActivityLogsByCreatedAtBefore(cutoffDate);
        log.info("Deleted successfully");
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private ActivityLog findActivityLogById(Long id) {
        return activityLogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Activity Log not found: " + id));
    }

    private PageResponse<ActivityLogResponse> createPageResponse(Page<ActivityLog> activityLogPage) {
        List<ActivityLogResponse> content = activityLogPage.getContent().stream()
            .map(activityLogMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<ActivityLogResponse>builder()
            .content(content)
            .pageNumber(activityLogPage.getNumber())
            .pageSize(activityLogPage.getSize())
            .totalElements(activityLogPage.getTotalElements())
            .totalPages(activityLogPage.getTotalPages())
            .last(activityLogPage.isLast())
            .first(activityLogPage.isFirst())
            .empty(activityLogPage.isEmpty())
            .build();
    }
}
