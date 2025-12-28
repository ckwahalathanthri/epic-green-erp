package lk.epicgreen.erp.mobile.mapper;

import lk.epicgreen.erp.mobile.dto.request.SyncLogRequest;
import lk.epicgreen.erp.mobile.dto.response.SyncLogResponse;
import lk.epicgreen.erp.mobile.entity.SyncLog;
import org.springframework.stereotype.Component;

/**
 * Mapper for SyncLog entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class SyncLogMapper {

    public SyncLog toEntity(SyncLogRequest request) {
        if (request == null) {
            return null;
        }

        return SyncLog.builder()
            .userId(request.getUserId())
            .deviceId(request.getDeviceId())
            .deviceType(request.getDeviceType())
            .appVersion(request.getAppVersion())
            .syncType(request.getSyncType())
            .syncDirection(request.getSyncDirection())
            .syncStatus(request.getSyncStatus() != null ? request.getSyncStatus() : "INITIATED")
            .recordsUploaded(request.getRecordsUploaded() != null ? request.getRecordsUploaded() : 0)
            .recordsDownloaded(request.getRecordsDownloaded() != null ? request.getRecordsDownloaded() : 0)
            .conflictsDetected(request.getConflictsDetected() != null ? request.getConflictsDetected() : 0)
            .conflictsResolved(request.getConflictsResolved() != null ? request.getConflictsResolved() : 0)
            .errorMessage(request.getErrorMessage())
            .startedAt(request.getStartedAt())
            .completedAt(request.getCompletedAt())
            .durationSeconds(request.getDurationSeconds())
            .build();
    }

    public void updateEntityFromRequest(SyncLogRequest request, SyncLog syncLog) {
        if (request == null || syncLog == null) {
            return;
        }

        syncLog.setUserId(request.getUserId());
        syncLog.setDeviceId(request.getDeviceId());
        syncLog.setDeviceType(request.getDeviceType());
        syncLog.setAppVersion(request.getAppVersion());
        syncLog.setSyncType(request.getSyncType());
        syncLog.setSyncDirection(request.getSyncDirection());
        syncLog.setSyncStatus(request.getSyncStatus());
        syncLog.setRecordsUploaded(request.getRecordsUploaded());
        syncLog.setRecordsDownloaded(request.getRecordsDownloaded());
        syncLog.setConflictsDetected(request.getConflictsDetected());
        syncLog.setConflictsResolved(request.getConflictsResolved());
        syncLog.setErrorMessage(request.getErrorMessage());
        syncLog.setStartedAt(request.getStartedAt());
        syncLog.setCompletedAt(request.getCompletedAt());
        syncLog.setDurationSeconds(request.getDurationSeconds());
    }

    public SyncLogResponse toResponse(SyncLog syncLog) {
        if (syncLog == null) {
            return null;
        }

        return SyncLogResponse.builder()
            .id(syncLog.getId())
            .userId(syncLog.getUserId())
            .deviceId(syncLog.getDeviceId())
            .deviceType(syncLog.getDeviceType())
            .appVersion(syncLog.getAppVersion())
            .syncType(syncLog.getSyncType())
            .syncDirection(syncLog.getSyncDirection())
            .syncStatus(syncLog.getSyncStatus())
            .recordsUploaded(syncLog.getRecordsUploaded())
            .recordsDownloaded(syncLog.getRecordsDownloaded())
            .conflictsDetected(syncLog.getConflictsDetected())
            .conflictsResolved(syncLog.getConflictsResolved())
            .errorMessage(syncLog.getErrorMessage())
            .startedAt(syncLog.getStartedAt())
            .completedAt(syncLog.getCompletedAt())
            .durationSeconds(syncLog.getDurationSeconds())
            .build();
    }
}
