package lk.epicgreen.erp.audit.controller;

import lk.epicgreen.erp.audit.dto.response.ActivityLogResponse;
import lk.epicgreen.erp.audit.mapper.ActivityLogMapper;
import lk.epicgreen.erp.audit.service.impl.ActivityLogServiceImpl;
import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.audit.entity.ActivityLog;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Activity Log Controller
 * REST controller for activity log operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/audit/activities")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ActivityLogController {

    @Autowired
    private final ActivityLogServiceImpl auditService;

    @Autowired
    private final ActivityLogMapper activityLogMapper;
    
    // Query Operations
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<ActivityLogResponse>> getActivityLogById(@PathVariable Long id) {
        ActivityLogResponse activityLog = auditService.getActivityLogById(id);
        return ResponseEntity.ok(ApiResponse.success(activityLog, "Activity log retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<PageResponse<ActivityLogResponse>>> getAllActivityLogs(Pageable pageable) {
        PageResponse<ActivityLogResponse> activityLogs = auditService.getAllActivityLogs(pageable);
        return ResponseEntity.ok(ApiResponse.success(activityLogs, "Activity logs retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<PageResponse<ActivityLogResponse>>> getAllActivityLogsList(@PathVariable Pageable pageable) {
        PageResponse<ActivityLogResponse> activityLogs = auditService.getAllActivityLogs(pageable);
        return ResponseEntity.ok(ApiResponse.success(activityLogs, "Activity logs list retrieved successfully"));
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR', 'USER')")
    public ResponseEntity<ApiResponse<Page<ActivityLog>>> getActivityLogsByUserId(@PathVariable Long userId, Pageable pageable) {
        Page<ActivityLog> activityLogs = auditService.getActivityLogsByUserId(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(activityLogs, "Activity logs by user retrieved successfully"));
    }
    
    @GetMapping("/type/{activityType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<Page<ActivityLog>>> getActivityLogsByType(@PathVariable String activityType, Pageable pageable) {
        Page<ActivityLog> activityLogs = auditService.getActivityLogsByType(activityType, pageable);
        return ResponseEntity.ok(ApiResponse.success(activityLogs, "Activity logs by type retrieved successfully"));
    }
    
//    @GetMapping("/session/{sessionId}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
//    public ResponseEntity<ApiResponse<List<ActivityLog>>> getActivityLogsBySessionId(@PathVariable String sessionId) {
//        List<ActivityLog> activityLogs = auditService.getActivityLogsBySessionId(sessionId);
//        return ResponseEntity.ok(ApiResponse.success(activityLogs, "Activity logs by session retrieved successfully"));
//    }
    
    @GetMapping("/user/{userId}/logins")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR', 'USER')")
    public ResponseEntity<ApiResponse<List<ActivityLog>>> getLoginActivitiesByUserId(@PathVariable Long userId) {
        List<ActivityLog> logins = auditService.getLoginActivitiesByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(logins, "Login activities retrieved successfully"));
    }
    
//    @GetMapping("/user/{userId}/last-login")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR', 'USER')")
//    public ResponseEntity<ApiResponse<ActivityLog>> getLastLoginByUserId(@PathVariable Long userId) {
//        ActivityLog lastLogin = auditService.getLastLoginByUserId(userId);
//        return ResponseEntity.ok(ApiResponse.success(lastLogin, "Last login retrieved successfully"));
//    }
    
//    @GetMapping("/failed-logins")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
//    public ResponseEntity<ApiResponse<Page<ActivityLog>>> getFailedLoginAttempts(Pageable pageable) {
//        Page<ActivityLog> failedLogins = auditService.getFailedLoginAttempts(pageable);
//        return ResponseEntity.ok(ApiResponse.success(failedLogins, "Failed login attempts retrieved successfully"));
//    }
    
//    @GetMapping("/user/{userId}/failed-logins")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
//    public ResponseEntity<ApiResponse<List<ActivityLog>>> getFailedLoginAttemptsByUserId(@PathVariable Long userId) {
//        List<ActivityLog> failedLogins = auditService.getFailedLoginAttemptsByUserId(userId);
//        return ResponseEntity.ok(ApiResponse.success(failedLogins, "Failed login attempts by user retrieved successfully"));
//    }
    
//    @GetMapping("/user/{userId}/page-views")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR', 'USER')")
//    public ResponseEntity<ApiResponse<Page<ActivityLog>>> getPageViewsByUserId(@PathVariable Long userId, Pageable pageable) {
//        Page<ActivityLog> pageViews = auditService.getPageViewsByUserId(userId, pageable);
//        return ResponseEntity.ok(ApiResponse.success(pageViews, "Page views by user retrieved successfully"));
//    }
    
//    @GetMapping("/search")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
//    public ResponseEntity<ApiResponse<PageResponse<ActivityLogResponse>>> searchActivityLogs(@RequestParam String keyword, Pageable pageable) {
//        PageResponse<ActivityLogResponse> activityLogs = auditService.searchActivityLogs(keyword, pageable);
//        return ResponseEntity.ok(ApiResponse.success(activityLogs, "Search results retrieved successfully"));
//    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<List<ActivityLog>>> getRecentActivityLogs(@RequestParam() Pageable limit) {
        List<ActivityLog> activityLogs = auditService.getRecentActivityLogs(limit);
        return ResponseEntity.ok(ApiResponse.success(activityLogs, "Recent activity logs retrieved successfully"));
    }
    
//    @GetMapping("/sessions/current")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
//    public ResponseEntity<ApiResponse<List<ActivityLog>>> getCurrentUserSessions() {
//        List<ActivityLog> sessions = auditService.getCurrentUserSessions();
//        return ResponseEntity.ok(ApiResponse.success(sessions, "Current user sessions retrieved successfully"));
//    }
    
    // Cleanup Operations
    @DeleteMapping("/cleanup")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteOldActivityLogs(@RequestParam(defaultValue = "180") int daysToKeep) {
        log.info("Deleting activity logs older than {} days", daysToKeep);
        auditService.deleteOldActivityLogs(daysToKeep);
        return ResponseEntity.ok(ApiResponse.success(null, "Old activity logs deleted successfully"));
    }
}
