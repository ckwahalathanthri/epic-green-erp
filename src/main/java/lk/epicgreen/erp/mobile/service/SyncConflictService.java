package lk.epicgreen.erp.mobile.service;

import lk.epicgreen.erp.mobile.dto.request.SyncConflictRequest;
import lk.epicgreen.erp.mobile.dto.response.SyncConflictResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Service interface for Sync Conflict entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface SyncConflictService {

    SyncConflictResponse createSyncConflict(SyncConflictRequest request);
    SyncConflictResponse updateSyncConflict(Long id, SyncConflictRequest request);
    void deleteSyncConflict(Long id);
    
    SyncConflictResponse getSyncConflictById(Long id);
    PageResponse<SyncConflictResponse> getAllSyncConflicts(Pageable pageable);
    
    List<SyncConflictResponse> getSyncConflictsByUser(Long userId);
    List<SyncConflictResponse> getSyncConflictsByDevice(String deviceId);
    List<SyncConflictResponse> getSyncConflictsByUserAndDevice(Long userId, String deviceId);
    PageResponse<SyncConflictResponse> getSyncConflictsByStatus(String status, Pageable pageable);
    List<SyncConflictResponse> getSyncConflictsByEntity(String entityType, Long entityId);
    List<SyncConflictResponse> getSyncConflictsByType(String conflictType);
    
    List<SyncConflictResponse> getUnresolvedConflicts();
    List<SyncConflictResponse> getResolvedConflicts();
    
    void resolveConflict(Long id, String resolutionStrategy, Map<String, Object> resolvedData, Long resolvedBy);
    void ignoreConflict(Long id);
    void autoResolveConflict(Long id, String resolutionStrategy);
    
    PageResponse<SyncConflictResponse> searchSyncConflicts(String keyword, Pageable pageable);
}
