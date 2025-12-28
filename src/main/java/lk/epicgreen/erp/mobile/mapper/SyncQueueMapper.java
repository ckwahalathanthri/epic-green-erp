package lk.epicgreen.erp.mobile.mapper;

import lk.epicgreen.erp.mobile.dto.request.SyncQueueRequest;
import lk.epicgreen.erp.mobile.dto.response.SyncQueueResponse;
import lk.epicgreen.erp.mobile.entity.SyncQueue;
import org.springframework.stereotype.Component;

/**
 * Mapper for SyncQueue entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class SyncQueueMapper {

    public SyncQueue toEntity(SyncQueueRequest request) {
        if (request == null) {
            return null;
        }

        return SyncQueue.builder()
            .userId(request.getUserId())
            .deviceId(request.getDeviceId())
            .entityType(request.getEntityType())
            .entityId(request.getEntityId())
            .operationType(request.getOperationType())
            .dataSnapshot(request.getDataSnapshot())
            .syncStatus(request.getSyncStatus() != null ? request.getSyncStatus() : "PENDING")
            .priority(request.getPriority() != null ? request.getPriority() : 5)
            .retryCount(request.getRetryCount() != null ? request.getRetryCount() : 0)
            .maxRetries(request.getMaxRetries() != null ? request.getMaxRetries() : 3)
            .errorMessage(request.getErrorMessage())
            .syncedAt(request.getSyncedAt())
            .build();
    }

    public void updateEntityFromRequest(SyncQueueRequest request, SyncQueue syncQueue) {
        if (request == null || syncQueue == null) {
            return;
        }

        syncQueue.setUserId(request.getUserId());
        syncQueue.setDeviceId(request.getDeviceId());
        syncQueue.setEntityType(request.getEntityType());
        syncQueue.setEntityId(request.getEntityId());
        syncQueue.setOperationType(request.getOperationType());
        syncQueue.setDataSnapshot(request.getDataSnapshot());
        syncQueue.setSyncStatus(request.getSyncStatus());
        syncQueue.setPriority(request.getPriority());
        syncQueue.setRetryCount(request.getRetryCount());
        syncQueue.setMaxRetries(request.getMaxRetries());
        syncQueue.setErrorMessage(request.getErrorMessage());
        syncQueue.setSyncedAt(request.getSyncedAt());
    }

    public SyncQueueResponse toResponse(SyncQueue syncQueue) {
        if (syncQueue == null) {
            return null;
        }

        return SyncQueueResponse.builder()
            .id(syncQueue.getId())
            .userId(syncQueue.getUserId())
            .deviceId(syncQueue.getDeviceId())
            .entityType(syncQueue.getEntityType())
            .entityId(syncQueue.getEntityId())
            .operationType(syncQueue.getOperationType())
            .dataSnapshot(syncQueue.getDataSnapshot())
            .syncStatus(syncQueue.getSyncStatus())
            .priority(syncQueue.getPriority())
            .retryCount(syncQueue.getRetryCount())
            .maxRetries(syncQueue.getMaxRetries())
            .errorMessage(syncQueue.getErrorMessage())
            .createdAt(syncQueue.getCreatedAt())
            .syncedAt(syncQueue.getSyncedAt())
            .build();
    }
}
