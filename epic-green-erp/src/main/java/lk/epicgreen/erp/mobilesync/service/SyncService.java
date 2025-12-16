package lk.epicgreen.erp.mobilesync.service;

import lk.epicgreen.erp.mobilesync.dto.SyncRequest;
import lk.epicgreen.erp.mobilesync.dto.SyncResponse;
import lk.epicgreen.erp.mobilesync.entity.SyncLog;
import lk.epicgreen.erp.mobilesync.entity.SyncQueue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Sync Service Interface
 * Service for mobile synchronization operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface SyncService {
    
    // ===================================================================
    // SYNC OPERATIONS
    // ===================================================================
    
    /**
     * Synchronize data (bidirectional)
     */
    SyncResponse synchronize(SyncRequest request);
    
    /**
     * Push data to server (upload)
     */
    SyncResponse pushData(SyncRequest request);
    
    /**
     * Pull data from server (download)
     */
    SyncResponse pullData(SyncRequest request);
    
    /**
     * Full synchronization (both push and pull)
     */
    SyncResponse fullSync(SyncRequest request);
    
    /**
     * Incremental synchronization (only changes since last sync)
     */
    SyncResponse incrementalSync(SyncRequest request);
    
    /**
     * Force synchronization (ignore conflicts)
     */
    SyncResponse forceSync(SyncRequest request);
    
    // ===================================================================
    // ENTITY-SPECIFIC SYNC
    // ===================================================================
    
    /**
     * Sync customers
     */
    SyncResponse syncCustomers(Long userId, String deviceId, LocalDateTime lastSyncTime);
    
    /**
     * Sync products
     */
    SyncResponse syncProducts(Long userId, String deviceId, LocalDateTime lastSyncTime);
    
    /**
     * Sync sales orders
     */
    SyncResponse syncSalesOrders(Long userId, String deviceId, LocalDateTime lastSyncTime);
    
    /**
     * Sync payments
     */
    SyncResponse syncPayments(Long userId, String deviceId, LocalDateTime lastSyncTime);
    
    /**
     * Sync inventory
     */
    SyncResponse syncInventory(Long userId, String deviceId, LocalDateTime lastSyncTime);
    
    /**
     * Sync price lists
     */
    SyncResponse syncPriceLists(Long userId, String deviceId, LocalDateTime lastSyncTime);
    
    /**
     * Sync sales representatives
     */
    SyncResponse syncSalesReps(Long userId, String deviceId, LocalDateTime lastSyncTime);
    
    // ===================================================================
    // SYNC QUEUE OPERATIONS
    // ===================================================================
    
    /**
     * Add to sync queue
     */
    SyncQueue addToSyncQueue(SyncRequest request);
    
    /**
     * Process sync queue
     */
    int processSyncQueue();
    
    /**
     * Process pending sync queues
     */
    int processPendingSyncQueues(int limit);
    
    /**
     * Process high priority queues
     */
    int processHighPriorityQueues();
    
    /**
     * Retry failed sync queues
     */
    int retryFailedSyncQueues();
    
    /**
     * Get sync queue by ID
     */
    SyncQueue getSyncQueueById(Long id);
    
    /**
     * Get all sync queues
     */
    List<SyncQueue> getAllSyncQueues();
    
    /**
     * Get all sync queues with pagination
     */
    Page<SyncQueue> getAllSyncQueues(Pageable pageable);
    
    /**
     * Get pending sync queues
     */
    List<SyncQueue> getPendingSyncQueues();
    
    /**
     * Get user's sync queues
     */
    List<SyncQueue> getUserSyncQueues(Long userId);
    
    /**
     * Get device's sync queues
     */
    List<SyncQueue> getDeviceSyncQueues(String deviceId);
    
    /**
     * Cancel sync queue
     */
    void cancelSyncQueue(Long queueId, String reason);
    
    /**
     * Cancel user's pending queues
     */
    int cancelUserPendingQueues(Long userId, String reason);
    
    /**
     * Cancel device's pending queues
     */
    int cancelDevicePendingQueues(String deviceId, String reason);
    
    // ===================================================================
    // SYNC LOG OPERATIONS
    // ===================================================================
    
    /**
     * Create sync log
     */
    SyncLog createSyncLog(SyncRequest request);
    
    /**
     * Update sync log
     */
    SyncLog updateSyncLog(Long logId, String status, String message);
    
    /**
     * Get sync log by ID
     */
    SyncLog getSyncLogById(Long id);
    
    /**
     * Get all sync logs
     */
    List<SyncLog> getAllSyncLogs();
    
    /**
     * Get all sync logs with pagination
     */
    Page<SyncLog> getAllSyncLogs(Pageable pageable);
    
    /**
     * Get sync logs by user
     */
    List<SyncLog> getSyncLogsByUser(Long userId);
    
    /**
     * Get sync logs by user with pagination
     */
    Page<SyncLog> getSyncLogsByUser(Long userId, Pageable pageable);
    
    /**
     * Get sync logs by device
     */
    List<SyncLog> getSyncLogsByDevice(String deviceId);
    
    /**
     * Get sync logs by status
     */
    List<SyncLog> getSyncLogsByStatus(String status);
    
    /**
     * Get successful sync logs
     */
    List<SyncLog> getSuccessfulSyncLogs();
    
    /**
     * Get failed sync logs
     */
    List<SyncLog> getFailedSyncLogs();
    
    /**
     * Get sync logs with conflicts
     */
    List<SyncLog> getSyncLogsWithConflicts();
    
    /**
     * Get user's recent sync logs
     */
    List<SyncLog> getUserRecentSyncLogs(Long userId, int limit);
    
    /**
     * Get device's recent sync logs
     */
    List<SyncLog> getDeviceRecentSyncLogs(String deviceId, int limit);
    
    /**
     * Search sync logs
     */
    Page<SyncLog> searchSyncLogs(String keyword, Pageable pageable);
    
    // ===================================================================
    // CONFLICT RESOLUTION
    // ===================================================================
    
    /**
     * Detect conflicts
     */
    List<Map<String, Object>> detectConflicts(SyncRequest request);
    
    /**
     * Resolve conflict (server wins)
     */
    SyncResponse resolveConflictServerWins(Long syncLogId);
    
    /**
     * Resolve conflict (client wins)
     */
    SyncResponse resolveConflictClientWins(Long syncLogId);
    
    /**
     * Resolve conflict (manual merge)
     */
    SyncResponse resolveConflictManualMerge(Long syncLogId, Map<String, Object> mergedData);
    
    /**
     * Get unresolved conflicts
     */
    List<SyncLog> getUnresolvedConflicts();
    
    /**
     * Get user's unresolved conflicts
     */
    List<SyncLog> getUserUnresolvedConflicts(Long userId);
    
    // ===================================================================
    // SYNC STATUS & MONITORING
    // ===================================================================
    
    /**
     * Get last sync time by user and entity type
     */
    LocalDateTime getLastSyncTime(Long userId, String entityType);
    
    /**
     * Get last sync time by device and entity type
     */
    LocalDateTime getLastSyncTimeByDevice(String deviceId, String entityType);
    
    /**
     * Check if sync is in progress
     */
    boolean isSyncInProgress(Long userId);
    
    /**
     * Check if device sync is in progress
     */
    boolean isDeviceSyncInProgress(String deviceId);
    
    /**
     * Get sync status
     */
    Map<String, Object> getSyncStatus(Long userId);
    
    /**
     * Get device sync status
     */
    Map<String, Object> getDeviceSyncStatus(String deviceId);
    
    /**
     * Get sync progress
     */
    Map<String, Object> getSyncProgress(String sessionId);
    
    // ===================================================================
    // DATA VALIDATION
    // ===================================================================
    
    /**
     * Validate sync data
     */
    List<String> validateSyncData(SyncRequest request);
    
    /**
     * Validate entity data
     */
    List<String> validateEntityData(String entityType, Map<String, Object> data);
    
    /**
     * Check data integrity
     */
    boolean checkDataIntegrity(String entityType, Long entityId);
    
    // ===================================================================
    // BULK OPERATIONS
    // ===================================================================
    
    /**
     * Bulk synchronize
     */
    List<SyncResponse> bulkSynchronize(List<SyncRequest> requests);
    
    /**
     * Bulk push data
     */
    List<SyncResponse> bulkPushData(List<SyncRequest> requests);
    
    /**
     * Bulk pull data
     */
    List<SyncResponse> bulkPullData(List<SyncRequest> requests);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Get sync statistics
     */
    Map<String, Object> getSyncStatistics();
    
    /**
     * Get sync statistics by date range
     */
    Map<String, Object> getSyncStatistics(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get user sync statistics
     */
    Map<String, Object> getUserSyncStatistics(Long userId);
    
    /**
     * Get device sync statistics
     */
    Map<String, Object> getDeviceSyncStatistics(String deviceId);
    
    /**
     * Get sync type distribution
     */
    List<Map<String, Object>> getSyncTypeDistribution();
    
    /**
     * Get sync direction distribution
     */
    List<Map<String, Object>> getSyncDirectionDistribution();
    
    /**
     * Get entity type distribution
     */
    List<Map<String, Object>> getEntityTypeDistribution();
    
    /**
     * Get most active users
     */
    List<Map<String, Object>> getMostActiveUsers();
    
    /**
     * Get most active devices
     */
    List<Map<String, Object>> getMostActiveDevices();
    
    /**
     * Get hourly sync count
     */
    List<Map<String, Object>> getHourlySyncCount(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get daily sync count
     */
    List<Map<String, Object>> getDailySyncCount(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get sync success rate
     */
    Double getSyncSuccessRate();
    
    /**
     * Get dashboard statistics
     */
    Map<String, Object> getDashboardStatistics();
    
    // ===================================================================
    // CLEANUP OPERATIONS
    // ===================================================================
    
    /**
     * Delete old sync logs
     */
    int deleteOldSyncLogs(int daysToKeep);
    
    /**
     * Delete old completed sync queues
     */
    int deleteOldCompletedSyncQueues(int daysToKeep);
    
    /**
     * Delete old cancelled sync queues
     */
    int deleteOldCancelledSyncQueues(int daysToKeep);
    
    /**
     * Cleanup sync data
     */
    Map<String, Integer> cleanupSyncData(int daysToKeep);
    
    // ===================================================================
    // OFFLINE SUPPORT
    // ===================================================================
    
    /**
     * Queue offline changes
     */
    SyncQueue queueOfflineChange(String entityType, Long entityId, String operation, 
                                 Map<String, Object> data, Long userId, String deviceId);
    
    /**
     * Get offline changes
     */
    List<SyncQueue> getOfflineChanges(Long userId, String deviceId);
    
    /**
     * Sync offline changes
     */
    SyncResponse syncOfflineChanges(Long userId, String deviceId);
    
    /**
     * Clear offline changes
     */
    int clearOfflineChanges(Long userId, String deviceId);
}
