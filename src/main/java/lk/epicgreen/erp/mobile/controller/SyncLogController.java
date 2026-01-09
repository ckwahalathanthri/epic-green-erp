package lk.epicgreen.erp.mobile.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import lk.epicgreen.erp.mobile.dto.response.SyncLogResponse;
import lk.epicgreen.erp.mobile.entity.SyncLog;
import lk.epicgreen.erp.mobile.service.impl.SyncLogServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Sync Log Controller
 * REST controller for sync log and conflict resolution
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mobile/sync/logs")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class SyncLogController {
    
    private final SyncLogServiceImpl syncService;
    
    // Log Query Operations
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<SyncLogResponse>> getSyncLogById(@PathVariable Long id) {
        SyncLogResponse log = syncService.getSyncLogById(id);
        return ResponseEntity.ok(ApiResponse.success(log, "Sync log retrieved"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<PageResponse<SyncLogResponse>>> getAllSyncLogs(Pageable pageable) {
        PageResponse<SyncLogResponse> logs = syncService.getAllSyncLogs(pageable);
        return ResponseEntity.ok(ApiResponse.success(logs, "Sync logs retrieved"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SyncLogResponse>>> getAllSyncLogsList(Pageable pageable) {
        List<SyncLogResponse> logs = syncService.getAllSyncLogs(pageable).getContent();
        return ResponseEntity.ok(ApiResponse.success(logs, "Sync logs list retrieved"));
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<List<SyncLogResponse>>> getSyncLogsByUser(@PathVariable Long userId) {
        List<SyncLogResponse> logs = syncService.getSyncLogsByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(logs, "User sync logs retrieved"));
    }
    
    @GetMapping("/device/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<List<SyncLogResponse>>> getSyncLogsByDevice(@PathVariable String deviceId) {
        List<SyncLogResponse> logs = syncService.getSyncLogsByDevice(deviceId);
        return ResponseEntity.ok(ApiResponse.success(logs, "Device sync logs retrieved"));
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<PageResponse<SyncLogResponse>>> getSyncLogsByStatus(@PathVariable String status,Pageable pageable) {
        PageResponse<SyncLogResponse> logs = syncService.getSyncLogsByStatus(status,pageable);
        return ResponseEntity.ok(ApiResponse.success(logs, "Sync logs by status retrieved"));
    }
    
    @GetMapping("/successful")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SyncLog>>> getSuccessfulSyncLogs() {
        List<SyncLog> logs = syncService.getSuccessfulSyncLogs();
        return ResponseEntity.ok(ApiResponse.success(logs, "Successful sync logs retrieved"));
    }
    
    @GetMapping("/failed")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SyncLog>>> getFailedSyncLogs() {
        List<SyncLog> logs = syncService.getFailedSyncLogs();
        return ResponseEntity.ok(ApiResponse.success(logs, "Failed sync logs retrieved"));
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SyncLogResponse>>> getSyncLogsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        List<SyncLogResponse> logs = syncService.getSyncLogsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(logs, "Sync logs by date range retrieved"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<PageResponse<SyncLogResponse>>> searchSyncLogs(@RequestParam String keyword, Pageable pageable) {
        PageResponse<SyncLogResponse> logs = syncService.searchSyncLogs(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(logs, "Search results retrieved"));
    }
    
    // Conflict Operations
    @GetMapping("/conflicts")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SyncLog>>> getSyncLogsWithConflicts() {
        List<SyncLog> logs = syncService.getSyncLogsWithConflicts();
        return ResponseEntity.ok(ApiResponse.success(logs, "Sync logs with conflicts retrieved"));
    }
    
    @GetMapping("/conflicts/unresolved")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SyncLog>>> getUnresolvedConflicts() {
        List<SyncLog> logs = syncService.getUnresolvedConflicts();
        return ResponseEntity.ok(ApiResponse.success(logs, "Unresolved conflicts retrieved"));
    }
    
    @GetMapping("/user/{userId}/conflicts/unresolved")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<List<SyncLog>>> getUserUnresolvedConflicts(@PathVariable Long userId) {
        List<SyncLog> logs = syncService.getUserUnresolvedConflicts(userId);
        return ResponseEntity.ok(ApiResponse.success(logs, "User unresolved conflicts retrieved"));
    }
    
    @GetMapping("/conflicts/detect/{logId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> detectConflict(@PathVariable Long logId) {
        boolean hasConflict = syncService.detectConflict(logId);
        return ResponseEntity.ok(ApiResponse.success(hasConflict, "Conflict detection completed"));
    }
    
    // Conflict Resolution
    @PostMapping("/conflicts/{logId}/resolve/server-wins")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SyncLogResponse>> resolveConflictServerWins(@PathVariable Long logId) {
        log.info("Resolving conflict - server wins: {}", logId);
        SyncLogResponse response = syncService.resolveConflictServerWins(logId);
        return ResponseEntity.ok(ApiResponse.success(response, "Conflict resolved - server wins"));
    }

    @PostMapping("/conflicts/{logId}/resolve/client-wins")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SyncLogResponse>> resolveConflictClientWins(@PathVariable Long logId) {
        log.info("Resolving conflict - client wins: {}", logId);
        SyncLogResponse response = syncService.resolveConflictClientWins(logId);
        return ResponseEntity.ok(ApiResponse.success(response, "Conflict resolved - client wins"));
    }

    @PostMapping("/conflicts/{logId}/resolve/manual")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SyncLogResponse>> resolveConflictManualMerge(
        @PathVariable Long logId,
        @RequestBody Map<String, Object> mergedData
    ) {
        log.info("Resolving conflict - manual merge: {}", logId);
        SyncLogResponse response = syncService.resolveConflictManualMerge(logId, mergedData);
        return ResponseEntity.ok(ApiResponse.success(response, "Conflict resolved - manual merge"));
    }
}
