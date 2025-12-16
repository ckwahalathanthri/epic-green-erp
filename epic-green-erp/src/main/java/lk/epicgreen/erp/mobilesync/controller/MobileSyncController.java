package lk.epicgreen.erp.mobilesync.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.mobilesync.dto.SyncRequest;
import lk.epicgreen.erp.mobilesync.dto.SyncResponse;
import lk.epicgreen.erp.mobilesync.entity.SyncLog;
import lk.epicgreen.erp.mobilesync.entity.SyncQueue;
import lk.epicgreen.erp.mobilesync.service.SyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Mobile Sync Controller
 * REST controller for mobile synchronization operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mobile-sync")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class MobileSyncController {
    
    private final SyncService syncService;
    
    // ===================================================================
    // SYNC OPERATIONS
    // ===================================================================
    
    /**
     * Synchronize data (bidirectional)
     */
    @PostMapping("/synchronize")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<SyncResponse>> synchronize(
        @Valid @RequestBody SyncRequest request
    ) {
        log.info("Synchronizing data for user: {}, device: {}", request.getUserId(), request.getDeviceId());
        SyncResponse response = syncService.synchronize(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Synchronization completed"));
    }
    
    /**
     * Push data to server (upload)
     */
    @PostMapping("/push")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<SyncResponse>> pushData(
        @Valid @RequestBody SyncRequest request
    ) {
        log.info("Pushing data for user: {}", request.getUserId());
        SyncResponse response = syncService.pushData(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Data pushed successfully"));
    }
    
    /**
     * Pull data from server (download)
     */
    @PostMapping("/pull")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<SyncResponse>> pullData(
        @Valid @RequestBody SyncRequest request
    ) {
        log.info("Pulling data for user: {}", request.getUserId());
        SyncResponse response = syncService.pullData(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Data pulled successfully"));
    }
    
    /**
     * Full synchronization (both push and pull)
     */
    @PostMapping("/full-sync")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<SyncResponse>> fullSync(
        @Valid @RequestBody SyncRequest request
    ) {
        log.info("Full sync for user: {}", request.getUserId());
        SyncResponse response = syncService.fullSync(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Full sync completed"));
    }
    
    /**
     * Incremental synchronization
     */
    @PostMapping("/incremental-sync")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<SyncResponse>> incrementalSync(
        @Valid @RequestBody SyncRequest request
    ) {
        log.info("Incremental sync for user: {}", request.getUserId());
        SyncResponse response = syncService.incrementalSync(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Incremental sync completed"));
    }
    
    /**
     * Force synchronization
     */
    @PostMapping("/force-sync")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SyncResponse>> forceSync(
        @Valid @RequestBody SyncRequest request
    ) {
        log.info("Force sync for user: {}", request.getUserId());
        SyncResponse response = syncService.forceSync(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Force sync completed"));
    }
    
    // ===================================================================
    // ENTITY-SPECIFIC SYNC
    // ===================================================================
    
    @PostMapping("/sync/customers")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<SyncResponse>> syncCustomers(
        @RequestParam Long userId,
        @RequestParam String deviceId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastSyncTime
    ) {
        SyncResponse response = syncService.syncCustomers(userId, deviceId, lastSyncTime);
        return ResponseEntity.ok(ApiResponse.success(response, "Customers synced"));
    }
    
    @PostMapping("/sync/products")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<SyncResponse>> syncProducts(
        @RequestParam Long userId,
        @RequestParam String deviceId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastSyncTime
    ) {
        SyncResponse response = syncService.syncProducts(userId, deviceId, lastSyncTime);
        return ResponseEntity.ok(ApiResponse.success(response, "Products synced"));
    }
    
    @PostMapping("/sync/sales-orders")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<SyncResponse>> syncSalesOrders(
        @RequestParam Long userId,
        @RequestParam String deviceId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastSyncTime
    ) {
        SyncResponse response = syncService.syncSalesOrders(userId, deviceId, lastSyncTime);
        return ResponseEntity.ok(ApiResponse.success(response, "Sales orders synced"));
    }
    
    @PostMapping("/sync/payments")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<SyncResponse>> syncPayments(
        @RequestParam Long userId,
        @RequestParam String deviceId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastSyncTime
    ) {
        SyncResponse response = syncService.syncPayments(userId, deviceId, lastSyncTime);
        return ResponseEntity.ok(ApiResponse.success(response, "Payments synced"));
    }
    
    @PostMapping("/sync/inventory")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<SyncResponse>> syncInventory(
        @RequestParam Long userId,
        @RequestParam String deviceId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastSyncTime
    ) {
        SyncResponse response = syncService.syncInventory(userId, deviceId, lastSyncTime);
        return ResponseEntity.ok(ApiResponse.success(response, "Inventory synced"));
    }
    
    @PostMapping("/sync/price-lists")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<SyncResponse>> syncPriceLists(
        @RequestParam Long userId,
        @RequestParam String deviceId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastSyncTime
    ) {
        SyncResponse response = syncService.syncPriceLists(userId, deviceId, lastSyncTime);
        return ResponseEntity.ok(ApiResponse.success(response, "Price lists synced"));
    }
    
    // ===================================================================
    // SYNC QUEUE OPERATIONS
    // ===================================================================
    
    @PostMapping("/queue")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<SyncQueue>> addToSyncQueue(
        @Valid @RequestBody SyncRequest request
    ) {
        SyncQueue queue = syncService.addToSyncQueue(request);
        return ResponseEntity.ok(ApiResponse.success(queue, "Added to sync queue"));
    }
    
    @PostMapping("/queue/process")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> processSyncQueue() {
        int processed = syncService.processSyncQueue();
        return ResponseEntity.ok(ApiResponse.success(processed, "Processed " + processed + " items"));
    }
    
    @PostMapping("/queue/process-pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> processPendingSyncQueues(
        @RequestParam(defaultValue = "100") int limit
    ) {
        int processed = syncService.processPendingSyncQueues(limit);
        return ResponseEntity.ok(ApiResponse.success(processed, "Processed " + processed + " items"));
    }
    
    @PostMapping("/queue/process-high-priority")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> processHighPriorityQueues() {
        int processed = syncService.processHighPriorityQueues();
        return ResponseEntity.ok(ApiResponse.success(processed, "Processed " + processed + " high priority items"));
    }
    
    @PostMapping("/queue/retry-failed")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> retryFailedSyncQueues() {
        int retried = syncService.retryFailedSyncQueues();
        return ResponseEntity.ok(ApiResponse.success(retried, "Retried " + retried + " failed items"));
    }
    
    @GetMapping("/queue")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<SyncQueue>>> getAllSyncQueues(Pageable pageable) {
        Page<SyncQueue> queues = syncService.getAllSyncQueues(pageable);
        return ResponseEntity.ok(ApiResponse.success(queues, "Queues retrieved successfully"));
    }
    
    @GetMapping("/queue/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<SyncQueue>> getSyncQueueById(@PathVariable Long id) {
        SyncQueue queue = syncService.getSyncQueueById(id);
        return ResponseEntity.ok(ApiResponse.success(queue, "Queue retrieved successfully"));
    }
    
    @GetMapping("/queue/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SyncQueue>>> getPendingSyncQueues() {
        List<SyncQueue> queues = syncService.getPendingSyncQueues();
        return ResponseEntity.ok(ApiResponse.success(queues, "Pending queues retrieved"));
    }
    
    @GetMapping("/queue/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<List<SyncQueue>>> getUserSyncQueues(@PathVariable Long userId) {
        List<SyncQueue> queues = syncService.getUserSyncQueues(userId);
        return ResponseEntity.ok(ApiResponse.success(queues, "User queues retrieved"));
    }
    
    @GetMapping("/queue/device/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<List<SyncQueue>>> getDeviceSyncQueues(@PathVariable String deviceId) {
        List<SyncQueue> queues = syncService.getDeviceSyncQueues(deviceId);
        return ResponseEntity.ok(ApiResponse.success(queues, "Device queues retrieved"));
    }
    
    @DeleteMapping("/queue/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> cancelSyncQueue(
        @PathVariable Long id,
        @RequestParam String reason
    ) {
        syncService.cancelSyncQueue(id, reason);
        return ResponseEntity.ok(ApiResponse.success(null, "Queue cancelled"));
    }
    
    @DeleteMapping("/queue/user/{userId}/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> cancelUserPendingQueues(
        @PathVariable Long userId,
        @RequestParam String reason
    ) {
        int cancelled = syncService.cancelUserPendingQueues(userId, reason);
        return ResponseEntity.ok(ApiResponse.success(cancelled, "Cancelled " + cancelled + " queues"));
    }
    
    // ===================================================================
    // SYNC LOG OPERATIONS
    // ===================================================================
    
    @GetMapping("/logs")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<SyncLog>>> getAllSyncLogs(Pageable pageable) {
        Page<SyncLog> logs = syncService.getAllSyncLogs(pageable);
        return ResponseEntity.ok(ApiResponse.success(logs, "Logs retrieved successfully"));
    }
    
    @GetMapping("/logs/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<SyncLog>> getSyncLogById(@PathVariable Long id) {
        SyncLog log = syncService.getSyncLogById(id);
        return ResponseEntity.ok(ApiResponse.success(log, "Log retrieved successfully"));
    }
    
    @GetMapping("/logs/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<Page<SyncLog>>> getSyncLogsByUser(
        @PathVariable Long userId,
        Pageable pageable
    ) {
        Page<SyncLog> logs = syncService.getSyncLogsByUser(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(logs, "User logs retrieved"));
    }
    
    @GetMapping("/logs/device/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<List<SyncLog>>> getSyncLogsByDevice(@PathVariable String deviceId) {
        List<SyncLog> logs = syncService.getSyncLogsByDevice(deviceId);
        return ResponseEntity.ok(ApiResponse.success(logs, "Device logs retrieved"));
    }
    
    @GetMapping("/logs/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SyncLog>>> getSyncLogsByStatus(@PathVariable String status) {
        List<SyncLog> logs = syncService.getSyncLogsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(logs, "Logs retrieved by status"));
    }
    
    @GetMapping("/logs/successful")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SyncLog>>> getSuccessfulSyncLogs() {
        List<SyncLog> logs = syncService.getSuccessfulSyncLogs();
        return ResponseEntity.ok(ApiResponse.success(logs, "Successful logs retrieved"));
    }
    
    @GetMapping("/logs/failed")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SyncLog>>> getFailedSyncLogs() {
        List<SyncLog> logs = syncService.getFailedSyncLogs();
        return ResponseEntity.ok(ApiResponse.success(logs, "Failed logs retrieved"));
    }
    
    @GetMapping("/logs/conflicts")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SyncLog>>> getSyncLogsWithConflicts() {
        List<SyncLog> logs = syncService.getSyncLogsWithConflicts();
        return ResponseEntity.ok(ApiResponse.success(logs, "Logs with conflicts retrieved"));
    }
    
    @GetMapping("/logs/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<SyncLog>>> searchSyncLogs(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<SyncLog> logs = syncService.searchSyncLogs(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(logs, "Search results retrieved"));
    }
    
    // ===================================================================
    // CONFLICT RESOLUTION
    // ===================================================================
    
    @PostMapping("/conflicts/{logId}/resolve/server-wins")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SyncResponse>> resolveConflictServerWins(@PathVariable Long logId) {
        SyncResponse response = syncService.resolveConflictServerWins(logId);
        return ResponseEntity.ok(ApiResponse.success(response, "Conflict resolved - server wins"));
    }
    
    @PostMapping("/conflicts/{logId}/resolve/client-wins")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SyncResponse>> resolveConflictClientWins(@PathVariable Long logId) {
        SyncResponse response = syncService.resolveConflictClientWins(logId);
        return ResponseEntity.ok(ApiResponse.success(response, "Conflict resolved - client wins"));
    }
    
    @PostMapping("/conflicts/{logId}/resolve/manual")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SyncResponse>> resolveConflictManualMerge(
        @PathVariable Long logId,
        @RequestBody Map<String, Object> mergedData
    ) {
        SyncResponse response = syncService.resolveConflictManualMerge(logId, mergedData);
        return ResponseEntity.ok(ApiResponse.success(response, "Conflict resolved - manual merge"));
    }
    
    @GetMapping("/conflicts/unresolved")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SyncLog>>> getUnresolvedConflicts() {
        List<SyncLog> conflicts = syncService.getUnresolvedConflicts();
        return ResponseEntity.ok(ApiResponse.success(conflicts, "Unresolved conflicts retrieved"));
    }
    
    @GetMapping("/conflicts/user/{userId}/unresolved")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<SyncLog>>> getUserUnresolvedConflicts(@PathVariable Long userId) {
        List<SyncLog> conflicts = syncService.getUserUnresolvedConflicts(userId);
        return ResponseEntity.ok(ApiResponse.success(conflicts, "User unresolved conflicts retrieved"));
    }
    
    // ===================================================================
    // SYNC STATUS & MONITORING
    // ===================================================================
    
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
    
    @GetMapping("/last-sync-time")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<LocalDateTime>> getLastSyncTime(
        @RequestParam Long userId,
        @RequestParam String entityType
    ) {
        LocalDateTime lastSyncTime = syncService.getLastSyncTime(userId, entityType);
        return ResponseEntity.ok(ApiResponse.success(lastSyncTime, "Last sync time retrieved"));
    }
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSyncStatistics() {
        Map<String, Object> stats = syncService.getSyncStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserSyncStatistics(@PathVariable Long userId) {
        Map<String, Object> stats = syncService.getUserSyncStatistics(userId);
        return ResponseEntity.ok(ApiResponse.success(stats, "User statistics retrieved"));
    }
    
    @GetMapping("/statistics/device/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDeviceSyncStatistics(@PathVariable String deviceId) {
        Map<String, Object> stats = syncService.getDeviceSyncStatistics(deviceId);
        return ResponseEntity.ok(ApiResponse.success(stats, "Device statistics retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = syncService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
    
    // ===================================================================
    // OFFLINE SUPPORT
    // ===================================================================
    
    @PostMapping("/offline/queue")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<SyncQueue>> queueOfflineChange(
        @RequestParam String entityType,
        @RequestParam Long entityId,
        @RequestParam String operation,
        @RequestBody Map<String, Object> data,
        @RequestParam Long userId,
        @RequestParam String deviceId
    ) {
        SyncQueue queue = syncService.queueOfflineChange(entityType, entityId, operation, data, userId, deviceId);
        return ResponseEntity.ok(ApiResponse.success(queue, "Offline change queued"));
    }
    
    @GetMapping("/offline/changes")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<List<SyncQueue>>> getOfflineChanges(
        @RequestParam Long userId,
        @RequestParam String deviceId
    ) {
        List<SyncQueue> changes = syncService.getOfflineChanges(userId, deviceId);
        return ResponseEntity.ok(ApiResponse.success(changes, "Offline changes retrieved"));
    }
    
    @PostMapping("/offline/sync")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<SyncResponse>> syncOfflineChanges(
        @RequestParam Long userId,
        @RequestParam String deviceId
    ) {
        SyncResponse response = syncService.syncOfflineChanges(userId, deviceId);
        return ResponseEntity.ok(ApiResponse.success(response, "Offline changes synced"));
    }
    
    @DeleteMapping("/offline/clear")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<Integer>> clearOfflineChanges(
        @RequestParam Long userId,
        @RequestParam String deviceId
    ) {
        int cleared = syncService.clearOfflineChanges(userId, deviceId);
        return ResponseEntity.ok(ApiResponse.success(cleared, "Offline changes cleared"));
    }
    
    // ===================================================================
    // BULK OPERATIONS
    // ===================================================================
    
    @PostMapping("/bulk/synchronize")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SyncResponse>>> bulkSynchronize(
        @RequestBody List<SyncRequest> requests
    ) {
        List<SyncResponse> responses = syncService.bulkSynchronize(requests);
        return ResponseEntity.ok(ApiResponse.success(responses, "Bulk synchronization completed"));
    }
}
