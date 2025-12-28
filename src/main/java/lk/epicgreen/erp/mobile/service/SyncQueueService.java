package lk.epicgreen.erp.mobile.service;

import lk.epicgreen.erp.mobile.dto.request.SyncQueueRequest;
import lk.epicgreen.erp.mobile.dto.response.SyncQueueResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for Sync Queue entity business logic
 * 
 * Sync Queue Status Workflow:
 * PENDING → IN_PROGRESS → SYNCED
 * PENDING → FAILED (can retry)
 * PENDING/FAILED → CONFLICT (needs resolution)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface SyncQueueService {

    SyncQueueResponse createSyncQueueItem(SyncQueueRequest request);
    SyncQueueResponse updateSyncQueueItem(Long id, SyncQueueRequest request);
    void deleteSyncQueueItem(Long id);
    
    SyncQueueResponse getSyncQueueItemById(Long id);
    PageResponse<SyncQueueResponse> getAllSyncQueueItems(Pageable pageable);
    
    List<SyncQueueResponse> getSyncQueueByUser(Long userId);
    List<SyncQueueResponse> getSyncQueueByDevice(String deviceId);
    List<SyncQueueResponse> getSyncQueueByUserAndDevice(Long userId, String deviceId);
    PageResponse<SyncQueueResponse> getSyncQueueByStatus(String syncStatus, Pageable pageable);
    List<SyncQueueResponse> getSyncQueueByEntity(String entityType, Long entityId);
    
    List<SyncQueueResponse> getPendingItems();
    List<SyncQueueResponse> getFailedItems();
    List<SyncQueueResponse> getConflictItems();
    
    void markAsInProgress(Long id);
    void markAsSynced(Long id);
    void markAsFailed(Long id, String errorMessage);
    void markAsConflict(Long id);
    void retrySyncItem(Long id);
    
    void processSyncQueue(Long userId, String deviceId);
    
    PageResponse<SyncQueueResponse> searchSyncQueue(String keyword, Pageable pageable);
    
    boolean canUpdate(Long id);
    boolean canDelete(Long id);
}
