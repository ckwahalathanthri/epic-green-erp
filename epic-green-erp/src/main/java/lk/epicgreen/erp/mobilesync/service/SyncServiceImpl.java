package lk.epicgreen.erp.mobilesync.service;

import lk.epicgreen.erp.mobilesync.dto.SyncRequest;
import lk.epicgreen.erp.mobilesync.dto.SyncResponse;
import lk.epicgreen.erp.mobilesync.entity.SyncLog;
import lk.epicgreen.erp.mobilesync.entity.SyncQueue;
import lk.epicgreen.erp.mobilesync.repository.SyncLogRepository;
import lk.epicgreen.erp.mobilesync.repository.SyncQueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Sync Service Implementation
 * Implementation of mobile synchronization service operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SyncServiceImpl implements SyncService {
    
    private final SyncLogRepository syncLogRepository;
    private final SyncQueueRepository syncQueueRepository;
    
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final int STUCK_SYNC_THRESHOLD_MINUTES = 30;
    
    // ===================================================================
    // SYNC OPERATIONS
    // ===================================================================
    
    @Override
    public SyncResponse synchronize(SyncRequest request) {
        log.info("Starting synchronization for user: {}, device: {}", 
            request.getUserId(), request.getDeviceId());
        
        // Create sync log
        SyncLog syncLog = createSyncLog(request);
        syncLog.setStatus("IN_PROGRESS");
        syncLog = syncLogRepository.save(syncLog);
        
        try {
            // Determine sync direction
            if ("BIDIRECTIONAL".equals(request.getSyncDirection())) {
                return fullSync(request);
            } else if ("PUSH".equals(request.getSyncDirection())) {
                return pushData(request);
            } else if ("PULL".equals(request.getSyncDirection())) {
                return pullData(request);
            } else {
                throw new IllegalArgumentException("Invalid sync direction: " + request.getSyncDirection());
            }
            
        } catch (Exception e) {
            log.error("Synchronization failed", e);
            updateSyncLog(syncLog.getId(), "FAILED", e.getMessage());
            throw new RuntimeException("Synchronization failed: " + e.getMessage(), e);
        }
    }
    
    @Override
    public SyncResponse pushData(SyncRequest request) {
        log.info("Pushing data to server for user: {}", request.getUserId());
        
        SyncLog syncLog = createSyncLog(request);
        syncLog.setSyncDirection("PUSH");
        syncLog.setStatus("IN_PROGRESS");
        syncLog = syncLogRepository.save(syncLog);
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Validate data
            List<String> validationErrors = validateSyncData(request);
            if (!validationErrors.isEmpty()) {
                throw new RuntimeException("Validation failed: " + String.join(", ", validationErrors));
            }
            
            // Process push operation
            int recordsProcessed = processPushOperation(request);
            
            // Update sync log
            long duration = System.currentTimeMillis() - startTime;
            syncLog.setStatus("SUCCESS");
            syncLog.setRecordsSynced(recordsProcessed);
            syncLog.setDurationMillis(duration);
            syncLog.setCompletedAt(LocalDateTime.now());
            syncLogRepository.save(syncLog);
            
            // Build response
            return SyncResponse.builder()
                .success(true)
                .message("Data pushed successfully")
                .recordsSynced(recordsProcessed)
                .syncLogId(syncLog.getId())
                .syncLogCode(syncLog.getSyncLogCode())
                .durationMillis(duration)
                .build();
            
        } catch (Exception e) {
            log.error("Push data failed", e);
            updateSyncLog(syncLog.getId(), "FAILED", e.getMessage());
            
            return SyncResponse.builder()
                .success(false)
                .message("Push failed: " + e.getMessage())
                .syncLogId(syncLog.getId())
                .build();
        }
    }
    
    @Override
    public SyncResponse pullData(SyncRequest request) {
        log.info("Pulling data from server for user: {}", request.getUserId());
        
        SyncLog syncLog = createSyncLog(request);
        syncLog.setSyncDirection("PULL");
        syncLog.setStatus("IN_PROGRESS");
        syncLog = syncLogRepository.save(syncLog);
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Get last sync time
            LocalDateTime lastSyncTime = request.getLastSyncTime();
            if (lastSyncTime == null) {
                lastSyncTime = getLastSyncTime(request.getUserId(), request.getEntityType());
            }
            
            // Process pull operation
            Map<String, Object> data = processPullOperation(request, lastSyncTime);
            
            // Update sync log
            long duration = System.currentTimeMillis() - startTime;
            syncLog.setStatus("SUCCESS");
            syncLog.setRecordsSynced(data.size());
            syncLog.setDurationMillis(duration);
            syncLog.setCompletedAt(LocalDateTime.now());
            syncLogRepository.save(syncLog);
            
            // Build response
            return SyncResponse.builder()
                .success(true)
                .message("Data pulled successfully")
                .data(data)
                .recordsSynced(data.size())
                .syncLogId(syncLog.getId())
                .syncLogCode(syncLog.getSyncLogCode())
                .durationMillis(duration)
                .syncTimestamp(LocalDateTime.now())
                .build();
            
        } catch (Exception e) {
            log.error("Pull data failed", e);
            updateSyncLog(syncLog.getId(), "FAILED", e.getMessage());
            
            return SyncResponse.builder()
                .success(false)
                .message("Pull failed: " + e.getMessage())
                .syncLogId(syncLog.getId())
                .build();
        }
    }
    
    @Override
    public SyncResponse fullSync(SyncRequest request) {
        log.info("Starting full synchronization for user: {}", request.getUserId());
        
        // First push local changes
        SyncResponse pushResponse = pushData(request);
        
        // Then pull server changes
        SyncResponse pullResponse = pullData(request);
        
        // Combine results
        return SyncResponse.builder()
            .success(pushResponse.isSuccess() && pullResponse.isSuccess())
            .message("Full sync completed")
            .data(pullResponse.getData())
            .recordsSynced(pushResponse.getRecordsSynced() + pullResponse.getRecordsSynced())
            .durationMillis(pushResponse.getDurationMillis() + pullResponse.getDurationMillis())
            .syncTimestamp(LocalDateTime.now())
            .build();
    }
    
    @Override
    public SyncResponse incrementalSync(SyncRequest request) {
        log.info("Starting incremental sync for user: {}", request.getUserId());
        
        // Get last sync time
        LocalDateTime lastSyncTime = getLastSyncTime(request.getUserId(), request.getEntityType());
        request.setLastSyncTime(lastSyncTime);
        
        // Perform sync only for changes since last sync
        return synchronize(request);
    }
    
    @Override
    public SyncResponse forceSync(SyncRequest request) {
        log.info("Starting force sync for user: {}", request.getUserId());
        
        // Force sync ignores conflicts - server always wins
        request.setConflictResolution("SERVER_WINS");
        return synchronize(request);
    }
    
    // ===================================================================
    // ENTITY-SPECIFIC SYNC
    // ===================================================================
    
    @Override
    public SyncResponse syncCustomers(Long userId, String deviceId, LocalDateTime lastSyncTime) {
        SyncRequest request = SyncRequest.builder()
            .userId(userId)
            .deviceId(deviceId)
            .entityType("CUSTOMER")
            .syncType("FULL")
            .syncDirection("PULL")
            .lastSyncTime(lastSyncTime)
            .build();
        
        return synchronize(request);
    }
    
    @Override
    public SyncResponse syncProducts(Long userId, String deviceId, LocalDateTime lastSyncTime) {
        SyncRequest request = SyncRequest.builder()
            .userId(userId)
            .deviceId(deviceId)
            .entityType("PRODUCT")
            .syncType("FULL")
            .syncDirection("PULL")
            .lastSyncTime(lastSyncTime)
            .build();
        
        return synchronize(request);
    }
    
    @Override
    public SyncResponse syncSalesOrders(Long userId, String deviceId, LocalDateTime lastSyncTime) {
        SyncRequest request = SyncRequest.builder()
            .userId(userId)
            .deviceId(deviceId)
            .entityType("SALES_ORDER")
            .syncType("FULL")
            .syncDirection("BIDIRECTIONAL")
            .lastSyncTime(lastSyncTime)
            .build();
        
        return synchronize(request);
    }
    
    @Override
    public SyncResponse syncPayments(Long userId, String deviceId, LocalDateTime lastSyncTime) {
        SyncRequest request = SyncRequest.builder()
            .userId(userId)
            .deviceId(deviceId)
            .entityType("PAYMENT")
            .syncType("FULL")
            .syncDirection("BIDIRECTIONAL")
            .lastSyncTime(lastSyncTime)
            .build();
        
        return synchronize(request);
    }
    
    @Override
    public SyncResponse syncInventory(Long userId, String deviceId, LocalDateTime lastSyncTime) {
        SyncRequest request = SyncRequest.builder()
            .userId(userId)
            .deviceId(deviceId)
            .entityType("INVENTORY")
            .syncType("FULL")
            .syncDirection("PULL")
            .lastSyncTime(lastSyncTime)
            .build();
        
        return synchronize(request);
    }
    
    @Override
    public SyncResponse syncPriceLists(Long userId, String deviceId, LocalDateTime lastSyncTime) {
        SyncRequest request = SyncRequest.builder()
            .userId(userId)
            .deviceId(deviceId)
            .entityType("PRICE_LIST")
            .syncType("FULL")
            .syncDirection("PULL")
            .lastSyncTime(lastSyncTime)
            .build();
        
        return synchronize(request);
    }
    
    @Override
    public SyncResponse syncSalesReps(Long userId, String deviceId, LocalDateTime lastSyncTime) {
        SyncRequest request = SyncRequest.builder()
            .userId(userId)
            .deviceId(deviceId)
            .entityType("SALES_REP")
            .syncType("FULL")
            .syncDirection("PULL")
            .lastSyncTime(lastSyncTime)
            .build();
        
        return synchronize(request);
    }
    
    // ===================================================================
    // SYNC QUEUE OPERATIONS
    // ===================================================================
    
    @Override
    public SyncQueue addToSyncQueue(SyncRequest request) {
        SyncQueue queue = SyncQueue.builder()
            .queueCode(generateQueueCode())
            .syncDirection(request.getSyncDirection())
            .entityType(request.getEntityType())
            .entityId(request.getEntityId())
            .userId(request.getUserId())
            .deviceId(request.getDeviceId())
            .syncStatus("PENDING")
            .priority(request.getPriority() != null ? request.getPriority() : 5)
            .operationType("SYNC")
            .build();
        
        return syncQueueRepository.save(queue);
    }
    
    @Override
    @Async
    public int processSyncQueue() {
        return processPendingSyncQueues(100); // Process up to 100 items
    }
    
    @Override
    public int processPendingSyncQueues(int limit) {
        log.info("Processing pending sync queues, limit: {}", limit);
        
        Pageable pageable = PageRequest.of(0, limit);
        List<SyncQueue> queues = syncQueueRepository.findSyncQueuesReadyToProcess(
            LocalDateTime.now(), pageable);
        
        int processed = 0;
        for (SyncQueue queue : queues) {
            try {
                processSyncQueueItem(queue);
                processed++;
            } catch (Exception e) {
                log.error("Error processing sync queue item: {}", queue.getId(), e);
            }
        }
        
        log.info("Processed {} sync queue items", processed);
        return processed;
    }
    
    @Override
    public int processHighPriorityQueues() {
        log.info("Processing high priority sync queues");
        
        List<SyncQueue> queues = syncQueueRepository.findHighPriorityPendingQueues();
        
        int processed = 0;
        for (SyncQueue queue : queues) {
            try {
                processSyncQueueItem(queue);
                processed++;
            } catch (Exception e) {
                log.error("Error processing high priority queue item: {}", queue.getId(), e);
            }
        }
        
        return processed;
    }
    
    @Override
    public int retryFailedSyncQueues() {
        log.info("Retrying failed sync queues");
        
        List<SyncQueue> queues = syncQueueRepository.findFailedQueuesForRetry(
            MAX_RETRY_ATTEMPTS, LocalDateTime.now());
        
        int retried = 0;
        for (SyncQueue queue : queues) {
            try {
                retrySyncQueueItem(queue);
                retried++;
            } catch (Exception e) {
                log.error("Error retrying sync queue item: {}", queue.getId(), e);
            }
        }
        
        return retried;
    }
    
    @Override
    @Transactional(readOnly = true)
    public SyncQueue getSyncQueueById(Long id) {
        return syncQueueRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sync queue not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SyncQueue> getAllSyncQueues() {
        return syncQueueRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<SyncQueue> getAllSyncQueues(Pageable pageable) {
        return syncQueueRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SyncQueue> getPendingSyncQueues() {
        return syncQueueRepository.findPendingSyncQueues();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SyncQueue> getUserSyncQueues(Long userId) {
        return syncQueueRepository.findByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SyncQueue> getDeviceSyncQueues(String deviceId) {
        return syncQueueRepository.findByDeviceId(deviceId);
    }
    
    @Override
    public void cancelSyncQueue(Long queueId, String reason) {
        syncQueueRepository.cancelSyncQueue(queueId, LocalDateTime.now(), reason);
    }
    
    @Override
    public int cancelUserPendingQueues(Long userId, String reason) {
        syncQueueRepository.cancelUserPendingQueues(userId, LocalDateTime.now(), reason);
        return syncQueueRepository.findByUserIdAndStatus(userId, "CANCELLED").size();
    }
    
    @Override
    public int cancelDevicePendingQueues(String deviceId, String reason) {
        syncQueueRepository.cancelDevicePendingQueues(deviceId, LocalDateTime.now(), reason);
        return syncQueueRepository.findByDeviceIdAndStatus(deviceId, "CANCELLED").size();
    }
    
    // ===================================================================
    // SYNC LOG OPERATIONS
    // ===================================================================
    
    @Override
    public SyncLog createSyncLog(SyncRequest request) {
        SyncLog syncLog = SyncLog.builder()
            .syncLogCode(generateSyncLogCode())
            .syncType(request.getSyncType())
            .syncDirection(request.getSyncDirection())
            .entityType(request.getEntityType())
            .userId(request.getUserId())
            .deviceId(request.getDeviceId())
            .sessionId(request.getSessionId())
            .status("PENDING")
            .startedAt(LocalDateTime.now())
            .build();
        
        return syncLogRepository.save(syncLog);
    }
    
    @Override
    public SyncLog updateSyncLog(Long logId, String status, String message) {
        SyncLog syncLog = getSyncLogById(logId);
        syncLog.setStatus(status);
        syncLog.setErrorMessage(message);
        
        if ("SUCCESS".equals(status) || "FAILED".equals(status)) {
            syncLog.setCompletedAt(LocalDateTime.now());
            
            if (syncLog.getStartedAt() != null) {
                long duration = java.time.Duration.between(
                    syncLog.getStartedAt(), syncLog.getCompletedAt()).toMillis();
                syncLog.setDurationMillis(duration);
            }
        }
        
        return syncLogRepository.save(syncLog);
    }
    
    @Override
    @Transactional(readOnly = true)
    public SyncLog getSyncLogById(Long id) {
        return syncLogRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sync log not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SyncLog> getAllSyncLogs() {
        return syncLogRepository.findAll(Sort.by(Sort.Direction.DESC, "startedAt"));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<SyncLog> getAllSyncLogs(Pageable pageable) {
        return syncLogRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SyncLog> getSyncLogsByUser(Long userId) {
        return syncLogRepository.findByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<SyncLog> getSyncLogsByUser(Long userId, Pageable pageable) {
        return syncLogRepository.findByUserId(userId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SyncLog> getSyncLogsByDevice(String deviceId) {
        return syncLogRepository.findByDeviceId(deviceId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SyncLog> getSyncLogsByStatus(String status) {
        return syncLogRepository.findByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SyncLog> getSuccessfulSyncLogs() {
        return syncLogRepository.findSuccessfulSyncLogs();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SyncLog> getFailedSyncLogs() {
        return syncLogRepository.findFailedSyncLogs();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SyncLog> getSyncLogsWithConflicts() {
        return syncLogRepository.findSyncLogsWithConflicts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SyncLog> getUserRecentSyncLogs(Long userId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return syncLogRepository.findUserRecentSyncLogs(userId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SyncLog> getDeviceRecentSyncLogs(String deviceId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return syncLogRepository.findDeviceRecentSyncLogs(deviceId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<SyncLog> searchSyncLogs(String keyword, Pageable pageable) {
        return syncLogRepository.searchSyncLogs(keyword, pageable);
    }
    
    // ===================================================================
    // CONFLICT RESOLUTION
    // ===================================================================
    
    @Override
    public List<Map<String, Object>> detectConflicts(SyncRequest request) {
        // TODO: Implement conflict detection logic
        return new ArrayList<>();
    }
    
    @Override
    public SyncResponse resolveConflictServerWins(Long syncLogId) {
        SyncLog syncLog = getSyncLogById(syncLogId);
        syncLog.setConflictResolution("SERVER_WINS");
        syncLog.setHasConflicts(false);
        syncLogRepository.save(syncLog);
        
        return SyncResponse.builder()
            .success(true)
            .message("Conflict resolved - server wins")
            .syncLogId(syncLogId)
            .build();
    }
    
    @Override
    public SyncResponse resolveConflictClientWins(Long syncLogId) {
        SyncLog syncLog = getSyncLogById(syncLogId);
        syncLog.setConflictResolution("CLIENT_WINS");
        syncLog.setHasConflicts(false);
        syncLogRepository.save(syncLog);
        
        return SyncResponse.builder()
            .success(true)
            .message("Conflict resolved - client wins")
            .syncLogId(syncLogId)
            .build();
    }
    
    @Override
    public SyncResponse resolveConflictManualMerge(Long syncLogId, Map<String, Object> mergedData) {
        SyncLog syncLog = getSyncLogById(syncLogId);
        syncLog.setConflictResolution("MANUAL_MERGE");
        syncLog.setHasConflicts(false);
        syncLogRepository.save(syncLog);
        
        return SyncResponse.builder()
            .success(true)
            .message("Conflict resolved - manual merge")
            .data(mergedData)
            .syncLogId(syncLogId)
            .build();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SyncLog> getUnresolvedConflicts() {
        return syncLogRepository.findSyncLogsWithConflicts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SyncLog> getUserUnresolvedConflicts(Long userId) {
        return syncLogRepository.findByUserIdAndStatus(userId, "CONFLICT");
    }
    
    // ===================================================================
    // SYNC STATUS & MONITORING
    // ===================================================================
    
    @Override
    @Transactional(readOnly = true)
    public LocalDateTime getLastSyncTime(Long userId, String entityType) {
        Optional<SyncLog> lastSync = syncLogRepository.findLastSuccessfulSync(userId, entityType);
        return lastSync.map(SyncLog::getCompletedAt).orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public LocalDateTime getLastSyncTimeByDevice(String deviceId, String entityType) {
        Optional<SyncLog> lastSync = syncLogRepository.findLastSyncByDevice(deviceId, entityType);
        return lastSync.map(SyncLog::getStartedAt).orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isSyncInProgress(Long userId) {
        List<SyncLog> inProgressSyncs = syncLogRepository.findByUserIdAndStatus(userId, "IN_PROGRESS");
        return !inProgressSyncs.isEmpty();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isDeviceSyncInProgress(String deviceId) {
        List<SyncLog> inProgressSyncs = syncLogRepository.findByDeviceIdAndStatus(deviceId, "IN_PROGRESS");
        return !inProgressSyncs.isEmpty();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSyncStatus(Long userId) {
        Map<String, Object> status = new HashMap<>();
        
        status.put("syncInProgress", isSyncInProgress(userId));
        status.put("pendingQueues", syncQueueRepository.findByUserIdAndStatus(userId, "PENDING").size());
        status.put("failedSyncs", syncLogRepository.findByUserIdAndStatus(userId, "FAILED").size());
        status.put("unresolvedConflicts", getUserUnresolvedConflicts(userId).size());
        
        return status;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDeviceSyncStatus(String deviceId) {
        Map<String, Object> status = new HashMap<>();
        
        status.put("syncInProgress", isDeviceSyncInProgress(deviceId));
        status.put("pendingQueues", syncQueueRepository.findByDeviceIdAndStatus(deviceId, "PENDING").size());
        status.put("failedSyncs", syncLogRepository.findByDeviceIdAndStatus(deviceId, "FAILED").size());
        
        return status;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSyncProgress(String sessionId) {
        List<SyncLog> sessionLogs = syncLogRepository.findBySessionId(sessionId);
        
        long total = sessionLogs.size();
        long completed = sessionLogs.stream().filter(log -> "SUCCESS".equals(log.getStatus())).count();
        long failed = sessionLogs.stream().filter(log -> "FAILED".equals(log.getStatus())).count();
        long inProgress = sessionLogs.stream().filter(log -> "IN_PROGRESS".equals(log.getStatus())).count();
        
        Map<String, Object> progress = new HashMap<>();
        progress.put("total", total);
        progress.put("completed", completed);
        progress.put("failed", failed);
        progress.put("inProgress", inProgress);
        progress.put("percentage", total > 0 ? (completed * 100.0 / total) : 0);
        
        return progress;
    }
    
    // ===================================================================
    // DATA VALIDATION
    // ===================================================================
    
    @Override
    public List<String> validateSyncData(SyncRequest request) {
        List<String> errors = new ArrayList<>();
        
        if (request.getUserId() == null) {
            errors.add("User ID is required");
        }
        
        if (request.getDeviceId() == null || request.getDeviceId().isEmpty()) {
            errors.add("Device ID is required");
        }
        
        if (request.getEntityType() == null || request.getEntityType().isEmpty()) {
            errors.add("Entity type is required");
        }
        
        if (request.getSyncDirection() == null || request.getSyncDirection().isEmpty()) {
            errors.add("Sync direction is required");
        }
        
        return errors;
    }
    
    @Override
    public List<String> validateEntityData(String entityType, Map<String, Object> data) {
        // TODO: Implement entity-specific validation logic
        return new ArrayList<>();
    }
    
    @Override
    public boolean checkDataIntegrity(String entityType, Long entityId) {
        // TODO: Implement data integrity check logic
        return true;
    }
    
    // ===================================================================
    // BULK OPERATIONS
    // ===================================================================
    
    @Override
    public List<SyncResponse> bulkSynchronize(List<SyncRequest> requests) {
        return requests.stream()
            .map(this::synchronize)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<SyncResponse> bulkPushData(List<SyncRequest> requests) {
        return requests.stream()
            .map(this::pushData)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<SyncResponse> bulkPullData(List<SyncRequest> requests) {
        return requests.stream()
            .map(this::pullData)
            .collect(Collectors.toList());
    }
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSyncStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalSyncs", syncLogRepository.count());
        stats.put("successfulSyncs", syncLogRepository.countSuccessfulSyncs());
        stats.put("failedSyncs", syncLogRepository.countFailedSyncs());
        stats.put("syncsWithConflicts", syncLogRepository.countSyncsWithConflicts());
        stats.put("pendingQueues", syncQueueRepository.countPendingQueues());
        stats.put("processingQueues", syncQueueRepository.countProcessingQueues());
        stats.put("averageSyncDuration", syncLogRepository.getAverageSyncDuration());
        stats.put("totalRecordsSynced", syncLogRepository.getTotalRecordsSynced());
        stats.put("successRate", syncLogRepository.getSyncSuccessRate());
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSyncStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        List<SyncLog> logs = syncLogRepository.findByStartedAtBetween(startDate, endDate);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSyncs", logs.size());
        stats.put("successfulSyncs", logs.stream().filter(l -> "SUCCESS".equals(l.getStatus())).count());
        stats.put("failedSyncs", logs.stream().filter(l -> "FAILED".equals(l.getStatus())).count());
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getUserSyncStatistics(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalSyncs", syncLogRepository.countByUserId(userId));
        stats.put("pendingQueues", syncQueueRepository.findByUserIdAndStatus(userId, "PENDING").size());
        stats.put("failedSyncs", syncLogRepository.findByUserIdAndStatus(userId, "FAILED").size());
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDeviceSyncStatistics(String deviceId) {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalSyncs", syncLogRepository.findByDeviceId(deviceId).size());
        stats.put("pendingQueues", syncQueueRepository.findByDeviceIdAndStatus(deviceId, "PENDING").size());
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getSyncTypeDistribution() {
        List<Object[]> results = syncLogRepository.getSyncTypeDistribution();
        return convertToMapList(results, "syncType", "syncCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getSyncDirectionDistribution() {
        List<Object[]> results = syncLogRepository.getSyncDirectionDistribution();
        return convertToMapList(results, "syncDirection", "syncCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getEntityTypeDistribution() {
        List<Object[]> results = syncLogRepository.getEntityTypeDistribution();
        return convertToMapList(results, "entityType", "syncCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMostActiveUsers() {
        List<Object[]> results = syncLogRepository.getMostActiveUsers();
        return convertToMapList(results, "username", "syncCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMostActiveDevices() {
        List<Object[]> results = syncLogRepository.getMostActiveDevices();
        return convertToMapList(results, "deviceId", "syncCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getHourlySyncCount(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = syncLogRepository.getHourlySyncCount(startDate, endDate);
        return convertToMapList(results, "hour", "syncCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getDailySyncCount(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = syncLogRepository.getDailySyncCount(startDate, endDate);
        return convertToMapList(results, "date", "syncCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getSyncSuccessRate() {
        return syncLogRepository.getSyncSuccessRate();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("statistics", getSyncStatistics());
        dashboard.put("typeDistribution", getSyncTypeDistribution());
        dashboard.put("directionDistribution", getSyncDirectionDistribution());
        dashboard.put("entityDistribution", getEntityTypeDistribution());
        dashboard.put("mostActiveUsers", getMostActiveUsers().stream().limit(10).collect(Collectors.toList()));
        dashboard.put("mostActiveDevices", getMostActiveDevices().stream().limit(10).collect(Collectors.toList()));
        
        return dashboard;
    }
    
    // ===================================================================
    // CLEANUP OPERATIONS
    // ===================================================================
    
    @Override
    public int deleteOldSyncLogs(int daysToKeep) {
        LocalDateTime beforeDate = LocalDateTime.now().minusDays(daysToKeep);
        syncLogRepository.deleteOldSyncLogs(beforeDate);
        log.info("Deleted sync logs older than {} days", daysToKeep);
        return 0;
    }
    
    @Override
    public int deleteOldCompletedSyncQueues(int daysToKeep) {
        LocalDateTime beforeDate = LocalDateTime.now().minusDays(daysToKeep);
        syncQueueRepository.deleteCompletedSyncQueues(beforeDate);
        log.info("Deleted completed sync queues older than {} days", daysToKeep);
        return 0;
    }
    
    @Override
    public int deleteOldCancelledSyncQueues(int daysToKeep) {
        LocalDateTime beforeDate = LocalDateTime.now().minusDays(daysToKeep);
        syncQueueRepository.deleteCancelledSyncQueues(beforeDate);
        log.info("Deleted cancelled sync queues older than {} days", daysToKeep);
        return 0;
    }
    
    @Override
    public Map<String, Integer> cleanupSyncData(int daysToKeep) {
        Map<String, Integer> results = new HashMap<>();
        
        results.put("syncLogsDeleted", deleteOldSyncLogs(daysToKeep));
        results.put("completedQueuesDeleted", deleteOldCompletedSyncQueues(daysToKeep));
        results.put("cancelledQueuesDeleted", deleteOldCancelledSyncQueues(daysToKeep));
        
        return results;
    }
    
    // ===================================================================
    // OFFLINE SUPPORT
    // ===================================================================
    
    @Override
    public SyncQueue queueOfflineChange(String entityType, Long entityId, String operation,
                                       Map<String, Object> data, Long userId, String deviceId) {
        SyncRequest request = SyncRequest.builder()
            .entityType(entityType)
            .entityId(entityId)
            .userId(userId)
            .deviceId(deviceId)
            .syncType("OFFLINE_CHANGE")
            .syncDirection("PUSH")
            .priority(1) // HIGH priority
            .build();
        
        return addToSyncQueue(request);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SyncQueue> getOfflineChanges(Long userId, String deviceId) {
        return syncQueueRepository.findByUserIdAndStatus(userId, "PENDING");
    }
    
    @Override
    public SyncResponse syncOfflineChanges(Long userId, String deviceId) {
        List<SyncQueue> offlineChanges = getOfflineChanges(userId, deviceId);
        
        int synced = 0;
        for (SyncQueue queue : offlineChanges) {
            try {
                processSyncQueueItem(queue);
                synced++;
            } catch (Exception e) {
                log.error("Error syncing offline change: {}", queue.getId(), e);
            }
        }
        
        return SyncResponse.builder()
            .success(true)
            .message("Offline changes synced")
            .recordsSynced(synced)
            .build();
    }
    
    @Override
    public int clearOfflineChanges(Long userId, String deviceId) {
        return cancelUserPendingQueues(userId, "Cleared by user");
    }
    
    // ===================================================================
    // HELPER METHODS
    // ===================================================================
    
    private String generateSyncLogCode() {
        return "SYNC" + System.currentTimeMillis();
    }
    
    private String generateQueueCode() {
        return "QUEUE" + System.currentTimeMillis();
    }
    
    private int processPushOperation(SyncRequest request) {
        // TODO: Implement actual push operation logic
        return 0;
    }
    
    private Map<String, Object> processPullOperation(SyncRequest request, LocalDateTime lastSyncTime) {
        // TODO: Implement actual pull operation logic
        return new HashMap<>();
    }
    
    private void processSyncQueueItem(SyncQueue queue) {
        // Mark as processing
        syncQueueRepository.markAsProcessing(queue.getId(), LocalDateTime.now());
        
        try {
            // Process the queue item
            // TODO: Implement actual processing logic
            
            // Mark as completed
            syncQueueRepository.markAsCompleted(queue.getId(), LocalDateTime.now());
            
        } catch (Exception e) {
            // Mark as failed
            syncQueueRepository.markAsFailed(queue.getId(), e.getMessage(), LocalDateTime.now());
            throw e;
        }
    }
    
    private void retrySyncQueueItem(SyncQueue queue) {
        queue.setRetryCount(queue.getRetryCount() + 1);
        queue.setLastRetryAt(LocalDateTime.now());
        queue.setNextRetryAt(calculateNextRetryTime(queue.getRetryCount()));
        syncQueueRepository.save(queue);
        
        processSyncQueueItem(queue);
    }
    
    private LocalDateTime calculateNextRetryTime(int retryCount) {
        // Exponential backoff: 2^retryCount minutes
        int delayMinutes = (int) Math.pow(2, retryCount);
        return LocalDateTime.now().plusMinutes(delayMinutes);
    }
    
    private List<Map<String, Object>> convertToMapList(List<Object[]> results, String key1, String key2) {
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put(key1, result[0]);
                map.put(key2, result[1]);
                return map;
            })
            .collect(Collectors.toList());
    }
}

