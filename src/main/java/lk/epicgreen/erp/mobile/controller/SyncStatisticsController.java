package lk.epicgreen.erp.mobile.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.mobile.service.SyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Sync Statistics Controller
 * REST controller for sync statistics and monitoring
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mobile/sync/statistics")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class SyncStatisticsController {
    
    private final SyncService syncService;
    
    // Statistics Operations
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSyncStatistics() {
        Map<String, Object> statistics = syncService.getSyncStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Sync statistics retrieved"));
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSyncStatisticsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        Map<String, Object> statistics = syncService.getSyncStatistics(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(statistics, "Sync statistics by date range retrieved"));
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserSyncStatistics(@PathVariable Long userId) {
        Map<String, Object> statistics = syncService.getUserSyncStatistics(userId);
        return ResponseEntity.ok(ApiResponse.success(statistics, "User sync statistics retrieved"));
    }
    
    @GetMapping("/device/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDeviceSyncStatistics(@PathVariable String deviceId) {
        Map<String, Object> statistics = syncService.getDeviceSyncStatistics(deviceId);
        return ResponseEntity.ok(ApiResponse.success(statistics, "Device sync statistics retrieved"));
    }
    
    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = syncService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
    
    // Distribution Statistics
    @GetMapping("/sync-type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getSyncTypeDistribution() {
        List<Map<String, Object>> distribution = syncService.getSyncTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Sync type distribution retrieved"));
    }
    
    @GetMapping("/sync-direction-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getSyncDirectionDistribution() {
        List<Map<String, Object>> distribution = syncService.getSyncDirectionDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Sync direction distribution retrieved"));
    }
    
    @GetMapping("/entity-type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getEntityTypeDistribution() {
        List<Map<String, Object>> distribution = syncService.getEntityTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Entity type distribution retrieved"));
    }
    
    // Activity Statistics
    @GetMapping("/most-active-users")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMostActiveUsers() {
        List<Map<String, Object>> users = syncService.getMostActiveUsers();
        return ResponseEntity.ok(ApiResponse.success(users, "Most active users retrieved"));
    }
    
    @GetMapping("/most-active-devices")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMostActiveDevices() {
        List<Map<String, Object>> devices = syncService.getMostActiveDevices();
        return ResponseEntity.ok(ApiResponse.success(devices, "Most active devices retrieved"));
    }
    
    // Time-Based Statistics
    @GetMapping("/hourly")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getHourlySyncCount(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        List<Map<String, Object>> count = syncService.getHourlySyncCount(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(count, "Hourly sync count retrieved"));
    }
    
    @GetMapping("/daily")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getDailySyncCount(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        List<Map<String, Object>> count = syncService.getDailySyncCount(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(count, "Daily sync count retrieved"));
    }
    
    // Success Rate
    @GetMapping("/success-rate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getSyncSuccessRate() {
        Double rate = syncService.getSyncSuccessRate();
        return ResponseEntity.ok(ApiResponse.success(rate, "Sync success rate retrieved"));
    }
    
    // Status and Monitoring
    @GetMapping("/status/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSyncStatus(@PathVariable Long userId) {
        Map<String, Object> status = syncService.getSyncStatus(userId);
        return ResponseEntity.ok(ApiResponse.success(status, "Sync status retrieved"));
    }
    
    @GetMapping("/status/device/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDeviceSyncStatus(@PathVariable String deviceId) {
        Map<String, Object> status = syncService.getDeviceSyncStatus(deviceId);
        return ResponseEntity.ok(ApiResponse.success(status, "Device sync status retrieved"));
    }
    
    @GetMapping("/progress/{sessionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSyncProgress(@PathVariable String sessionId) {
        Map<String, Object> progress = syncService.getSyncProgress(sessionId);
        return ResponseEntity.ok(ApiResponse.success(progress, "Sync progress retrieved"));
    }
    
    @GetMapping("/in-progress/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<Boolean>> isSyncInProgress(@PathVariable Long userId) {
        boolean inProgress = syncService.isSyncInProgress(userId);
        return ResponseEntity.ok(ApiResponse.success(inProgress, "Sync in progress status retrieved"));
    }
    
    @GetMapping("/in-progress/device/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<Boolean>> isDeviceSyncInProgress(@PathVariable String deviceId) {
        boolean inProgress = syncService.isDeviceSyncInProgress(deviceId);
        return ResponseEntity.ok(ApiResponse.success(inProgress, "Device sync in progress status retrieved"));
    }
    
    @GetMapping("/last-sync-time/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<LocalDateTime>> getLastSyncTime(
        @PathVariable Long userId,
        @RequestParam String entityType
    ) {
        LocalDateTime lastSyncTime = syncService.getLastSyncTime(userId, entityType);
        return ResponseEntity.ok(ApiResponse.success(lastSyncTime, "Last sync time retrieved"));
    }
    
    @GetMapping("/last-sync-time/device/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<LocalDateTime>> getLastSyncTimeByDevice(
        @PathVariable String deviceId,
        @RequestParam String entityType
    ) {
        LocalDateTime lastSyncTime = syncService.getLastSyncTimeByDevice(deviceId, entityType);
        return ResponseEntity.ok(ApiResponse.success(lastSyncTime, "Last sync time by device retrieved"));
    }
    
    // Cleanup Operations
    @DeleteMapping("/cleanup/logs")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Integer>> deleteOldSyncLogs(@RequestParam(defaultValue = "90") int daysToKeep) {
        log.info("Deleting sync logs older than {} days", daysToKeep);
        int deleted = syncService.deleteOldSyncLogs(daysToKeep);
        return ResponseEntity.ok(ApiResponse.success(deleted, deleted + " sync logs deleted"));
    }
    
    @DeleteMapping("/cleanup/queues/completed")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Integer>> deleteOldCompletedSyncQueues(@RequestParam(defaultValue = "30") int daysToKeep) {
        log.info("Deleting completed sync queues older than {} days", daysToKeep);
        int deleted = syncService.deleteOldCompletedSyncQueues(daysToKeep);
        return ResponseEntity.ok(ApiResponse.success(deleted, deleted + " completed queues deleted"));
    }
    
    @DeleteMapping("/cleanup/queues/cancelled")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Integer>> deleteOldCancelledSyncQueues(@RequestParam(defaultValue = "30") int daysToKeep) {
        log.info("Deleting cancelled sync queues older than {} days", daysToKeep);
        int deleted = syncService.deleteOldCancelledSyncQueues(daysToKeep);
        return ResponseEntity.ok(ApiResponse.success(deleted, deleted + " cancelled queues deleted"));
    }
    
    @DeleteMapping("/cleanup")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> cleanupSyncData(@RequestParam(defaultValue = "90") int daysToKeep) {
        log.info("Cleaning up sync data older than {} days", daysToKeep);
        Map<String, Integer> results = syncService.cleanupSyncData(daysToKeep);
        return ResponseEntity.ok(ApiResponse.success(results, "Cleanup completed"));
    }
}
