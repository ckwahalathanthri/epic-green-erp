package lk.epicgreen.erp.mobile.service;

import lk.epicgreen.erp.mobile.dto.request.SyncLogRequest;
import lk.epicgreen.erp.mobile.dto.response.SyncLogResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for Sync Log entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface SyncLogService {

    SyncLogResponse createSyncLog(SyncLogRequest request);
    SyncLogResponse updateSyncLog(Long id, SyncLogRequest request);
    void deleteSyncLog(Long id);
    
    SyncLogResponse getSyncLogById(Long id);
    PageResponse<SyncLogResponse> getAllSyncLogs(Pageable pageable);
    
    List<SyncLogResponse> getSyncLogsByUser(Long userId);
    List<SyncLogResponse> getSyncLogsByDevice(String deviceId);
    List<SyncLogResponse> getSyncLogsByUserAndDevice(Long userId, String deviceId);
    PageResponse<SyncLogResponse> getSyncLogsByStatus(String syncStatus, Pageable pageable);
    List<SyncLogResponse> getSyncLogsByType(String syncType);
    List<SyncLogResponse> getSyncLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<SyncLogResponse> getActiveSyncs();
    List<SyncLogResponse> getFailedSyncs();
    
    void markAsCompleted(Long id, Integer recordsUploaded, Integer recordsDownloaded, 
                        Integer conflictsDetected, Integer conflictsResolved);
    void markAsFailed(Long id, String errorMessage);
    void updateProgress(Long id, Integer recordsUploaded, Integer recordsDownloaded);
    
    PageResponse<SyncLogResponse> searchSyncLogs(String keyword, Pageable pageable);
}
