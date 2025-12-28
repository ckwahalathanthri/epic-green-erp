package lk.epicgreen.erp.audit.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Audit Statistics Controller
 * REST controller for audit statistics and reporting
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/audit/statistics")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuditStatisticsController {
    
    private final AuditService auditService;
    
    // Audit Statistics
    @GetMapping("/audit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAuditStatistics() {
        Map<String, Object> statistics = auditService.getAuditStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Audit statistics retrieved successfully"));
    }
    
    @GetMapping("/audit/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAuditStatisticsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        Map<String, Object> statistics = auditService.getAuditStatistics(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(statistics, "Audit statistics by date range retrieved successfully"));
    }
    
    // Activity Statistics
    @GetMapping("/activity")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getActivityStatistics() {
        Map<String, Object> statistics = auditService.getActivityStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Activity statistics retrieved successfully"));
    }
    
    @GetMapping("/activity/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getActivityStatisticsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        Map<String, Object> statistics = auditService.getActivityStatistics(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(statistics, "Activity statistics by date range retrieved successfully"));
    }
    
    // User Statistics
    @GetMapping("/user/{userId}/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR', 'USER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserActivitySummary(@PathVariable Long userId) {
        Map<String, Object> summary = auditService.getUserActivitySummary(userId);
        return ResponseEntity.ok(ApiResponse.success(summary, "User activity summary retrieved successfully"));
    }
    
    @GetMapping("/user/{userId}/summary/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR', 'USER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserActivitySummaryByDateRange(
        @PathVariable Long userId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        Map<String, Object> summary = auditService.getUserActivitySummary(userId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(summary, "User activity summary by date range retrieved successfully"));
    }
    
    @GetMapping("/most-active-users")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMostActiveUsers() {
        List<Map<String, Object>> users = auditService.getMostActiveUsers();
        return ResponseEntity.ok(ApiResponse.success(users, "Most active users retrieved successfully"));
    }
    
    @GetMapping("/most-active-users/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMostActiveUsersByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        List<Map<String, Object>> users = auditService.getMostActiveUsers(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(users, "Most active users by date range retrieved successfully"));
    }
    
    // Module Statistics
    @GetMapping("/most-used-modules")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMostUsedModules() {
        List<Map<String, Object>> modules = auditService.getMostUsedModules();
        return ResponseEntity.ok(ApiResponse.success(modules, "Most used modules retrieved successfully"));
    }
    
    // Distribution Statistics
    @GetMapping("/action-type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getActionTypeDistribution() {
        List<Map<String, Object>> distribution = auditService.getActionTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Action type distribution retrieved successfully"));
    }
    
    @GetMapping("/activity-type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getActivityTypeDistribution() {
        List<Map<String, Object>> distribution = auditService.getActivityTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Activity type distribution retrieved successfully"));
    }
    
    @GetMapping("/device-type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getDeviceTypeDistribution() {
        List<Map<String, Object>> distribution = auditService.getDeviceTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Device type distribution retrieved successfully"));
    }
    
    // Page View Statistics
    @GetMapping("/most-viewed-pages")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMostViewedPages() {
        List<Map<String, Object>> pages = auditService.getMostViewedPages();
        return ResponseEntity.ok(ApiResponse.success(pages, "Most viewed pages retrieved successfully"));
    }
    
    @GetMapping("/most-viewed-pages/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMostViewedPagesByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        List<Map<String, Object>> pages = auditService.getMostViewedPages(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(pages, "Most viewed pages by date range retrieved successfully"));
    }
    
    // Time-Based Activity
    @GetMapping("/hourly-activity/{date}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getHourlyActivity(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Map<String, Object>> activity = auditService.getHourlyActivity(date);
        return ResponseEntity.ok(ApiResponse.success(activity, "Hourly activity retrieved successfully"));
    }
    
    @GetMapping("/daily-activity")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getDailyActivity(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<Map<String, Object>> activity = auditService.getDailyActivity(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(activity, "Daily activity retrieved successfully"));
    }
    
    @GetMapping("/weekly-activity")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getWeeklyActivity(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<Map<String, Object>> activity = auditService.getWeeklyActivity(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(activity, "Weekly activity retrieved successfully"));
    }
    
    @GetMapping("/monthly-activity/{year}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMonthlyActivity(@PathVariable int year) {
        List<Map<String, Object>> activity = auditService.getMonthlyActivity(year);
        return ResponseEntity.ok(ApiResponse.success(activity, "Monthly activity retrieved successfully"));
    }
}
