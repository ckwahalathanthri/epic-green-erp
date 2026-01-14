package lk.epicgreen.erp.mobile.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.mobile.dto.request.SyncLogRequest;
import lk.epicgreen.erp.mobile.entity.SyncLog;
import lk.epicgreen.erp.mobile.entity.SyncQueue;
import lk.epicgreen.erp.mobile.service.impl.SyncQueueServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

/**
 * Sync Queue Controller
 * REST controller for sync queue management
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mobile/sync/queue")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class SyncQueueController {
    
    private final SyncQueueServiceImpl syncService;
    
    // Queue Operations
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<SyncQueue>> addToSyncQueue(@Valid @RequestBody SyncLogRequest request) {
        log.info("Adding to sync queue for user: {}", request.getUserId());
        SyncQueue queue = syncService.addToSyncQueue(request);
        return ResponseEntity.ok(ApiResponse.success(queue, "Added to sync queue"));
    }
    
    @PostMapping("/process")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> processSyncQueue(@RequestParam Long userId, @RequestParam String deviceId) {
        log.info("Processing sync queue");
        syncService.processSyncQueue(userId, deviceId);
        return ResponseEntity.ok(ApiResponse.success( " items processed"));
    }
    
    @PostMapping("/process-pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> processPendingSyncQueues( Pageable limit) {
        log.info("Processing pending sync queues with limit: {}", limit);
        int processed = syncService.processPendingSyncQueues(limit);
        return ResponseEntity.ok(ApiResponse.success(processed, processed + " items processed"));
    }
    
    @PostMapping("/process-high-priority")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> processHighPriorityQueues() {
        log.info("Processing high priority queues");
        int processed = syncService.processHighPriorityQueues();
        return ResponseEntity.ok(ApiResponse.success(processed, processed + " high priority items processed"));
    }
    
    @PostMapping("/retry-failed")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> retryFailedSyncQueues() {
        log.info("Retrying failed sync queues");
        int retried = syncService.retryFailedSyncQueues();
        return ResponseEntity.ok(ApiResponse.success(retried, retried + " failed items retried"));
    }
    
    // Query Operations
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<SyncQueue>> getSyncQueueById(@PathVariable Long id) {
        SyncQueue queue = syncService.getSyncQueueById(id);
        return ResponseEntity.ok(ApiResponse.success(queue, "Sync queue retrieved"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<SyncQueue>>> getAllSyncQueues(Pageable pageable) {
        Page<SyncQueue> queues = syncService.getAllSyncQueues(pageable);
        return ResponseEntity.ok(ApiResponse.success(queues, "Sync queues retrieved"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SyncQueue>>> getAllSyncQueuesList() {
        List<SyncQueue> queues = syncService.getAllSyncQueues();
        return ResponseEntity.ok(ApiResponse.success(queues, "Sync queues list retrieved"));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SyncQueue>>> getPendingSyncQueues() {
        List<SyncQueue> queues = syncService.getPendingSyncQueues();
        return ResponseEntity.ok(ApiResponse.success(queues, "Pending sync queues retrieved"));
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<List<SyncQueue>>> getUserSyncQueues(@PathVariable Long userId) {
        List<SyncQueue> queues = syncService.getUserSyncQueues(userId);
        return ResponseEntity.ok(ApiResponse.success(queues, "User sync queues retrieved"));
    }
    
    @GetMapping("/device/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<List<SyncQueue>>> getDeviceSyncQueues(@PathVariable String deviceId) {
        List<SyncQueue> queues = syncService.getDeviceSyncQueues(deviceId);
        return ResponseEntity.ok(ApiResponse.success(queues, "Device sync queues retrieved"));
    }
    
    // Cancel Operations
    @PutMapping("/{queueId}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
    public ResponseEntity<ApiResponse<Void>> cancelSyncQueue(@PathVariable Long queueId, @RequestParam String reason) {
        log.info("Cancelling sync queue: {}", queueId);
        syncService.cancelSyncQueue(queueId, reason);
        return ResponseEntity.ok(ApiResponse.success(null, "Sync queue cancelled"));
    }
    
    @PutMapping("/user/{userId}/cancel-pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> cancelUserPendingQueues(@PathVariable Long userId, @RequestParam String reason) {
        log.info("Cancelling user pending queues: {}", userId);
        syncService.cancelUserPendingQueues(userId, reason);
        return ResponseEntity.ok(ApiResponse.success(" queues cancelled"));
    }
    
    @PutMapping("/device/{deviceId}/cancel-pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> cancelDevicePendingQueues(@PathVariable String deviceId, @RequestParam String reason) {
        log.info("Cancelling device pending queues: {}", deviceId);
        syncService.cancelDevicePendingQueues(deviceId, reason);
        return ResponseEntity.ok(ApiResponse.success(" queues cancelled"));
    }
}
