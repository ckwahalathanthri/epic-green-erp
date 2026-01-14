package lk.epicgreen.erp.mobile.mapper;

import lk.epicgreen.erp.mobile.dto.request.SyncConflictRequest;
import lk.epicgreen.erp.mobile.dto.response.SyncConflictResponse;
import lk.epicgreen.erp.mobile.entity.SyncConflict;
import org.springframework.stereotype.Component;

/**
 * Mapper for SyncConflict entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class SyncConflictMapper {

    public SyncConflict toEntity(SyncConflictRequest request) {
        if (request == null) {
            return null;
        }

        return SyncConflict.builder()
//            .userId(request.getUserId())
            .deviceId(request.getDeviceId())
            .entityType(request.getEntityType())
            .entityId(request.getEntityId())
            .serverData(request.getServerData().toString())
            .clientData(request.getClientData().toString())
            .conflictType(request.getConflictType())
            .resolutionStrategy(request.getResolutionStrategy() != null ? request.getResolutionStrategy() : "MANUAL")
            .status(request.getStatus() != null ? request.getStatus() : "DETECTED")
            .resolvedData(request.getResolvedData().toString())
//            .resolvedBy(request.getResolvedBy())
            .build();
    }

    public void updateEntityFromRequest(SyncConflictRequest request, SyncConflict syncConflict) {
        if (request == null || syncConflict == null) {
            return;
        }

//        syncConflict.setUserId(request.getUserId());
        syncConflict.setDeviceId(request.getDeviceId());
        syncConflict.setEntityType(request.getEntityType());
        syncConflict.setEntityId(request.getEntityId());
        syncConflict.setServerData(request.getServerData().toString());
        syncConflict.setClientData(request.getClientData().toString());
        syncConflict.setConflictType(request.getConflictType());
        syncConflict.setResolutionStrategy(request.getResolutionStrategy());
        syncConflict.setStatus(request.getStatus());
        syncConflict.setResolvedData(request.getResolvedData().toString());
//        syncConflict.setResolvedBy(request.getResolvedBy());
    }

    public SyncConflictResponse toResponse(SyncConflict syncConflict) {
        if (syncConflict == null) {
            return null;
        }

        return SyncConflictResponse.builder()
            .id(syncConflict.getId())
//            .userId(syncConflict.getUserId())
            .deviceId(syncConflict.getDeviceId())
            .entityType(syncConflict.getEntityType())
            .entityId(syncConflict.getEntityId())
//            .serverData(syncConflict.getServerData())
//            .clientData(syncConflict.getClientData())
            .conflictType(syncConflict.getConflictType())
            .resolutionStrategy(syncConflict.getResolutionStrategy())
            .status(syncConflict.getStatus())
//            .resolvedData(syncConflict.getResolvedData())
//            .resolvedBy(syncConflict.getResolvedBy())
            .resolvedAt(syncConflict.getResolvedAt())
            .detectedAt(syncConflict.getDetectedAt())
            .build();
    }
}
