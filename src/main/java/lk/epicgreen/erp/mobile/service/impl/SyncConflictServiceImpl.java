package lk.epicgreen.erp.mobile.service.impl;

import lk.epicgreen.erp.mobile.dto.request.SyncConflictRequest;
import lk.epicgreen.erp.mobile.dto.response.SyncConflictResponse;
import lk.epicgreen.erp.mobile.entity.SyncConflict;
import lk.epicgreen.erp.mobile.mapper.SyncConflictMapper;
import lk.epicgreen.erp.mobile.repository.SyncConflictRepository;
import lk.epicgreen.erp.mobile.service.SyncConflictService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.InvalidOperationException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SyncConflictServiceImpl implements SyncConflictService {

    private final SyncConflictRepository syncConflictRepository;
    private final SyncConflictMapper syncConflictMapper;

    @Override
    @Transactional
    public SyncConflictResponse createSyncConflict(SyncConflictRequest request) {
        log.warn("Creating sync conflict for user: {} entity: {}", 
            request.getUserId(), request.getEntityType());

        SyncConflict syncConflict = syncConflictMapper.toEntity(request);
        SyncConflict savedSyncConflict = syncConflictRepository.save(syncConflict);

        log.warn("Sync conflict created successfully with ID: {}", savedSyncConflict.getId());
        return syncConflictMapper.toResponse(savedSyncConflict);
    }

    @Override
    @Transactional
    public SyncConflictResponse updateSyncConflict(Long id, SyncConflictRequest request) {
        log.info("Updating sync conflict: {}", id);

        SyncConflict syncConflict = findSyncConflictById(id);
        syncConflictMapper.updateEntityFromRequest(request, syncConflict);

        SyncConflict updatedSyncConflict = syncConflictRepository.save(syncConflict);
        log.info("Sync conflict updated successfully");

        return syncConflictMapper.toResponse(updatedSyncConflict);
    }

    @Override
    @Transactional
    public void deleteSyncConflict(Long id) {
        log.info("Deleting sync conflict: {}", id);
        syncConflictRepository.deleteById(id);
        log.info("Sync conflict deleted successfully");
    }

    @Override
    public SyncConflictResponse getSyncConflictById(Long id) {
        SyncConflict syncConflict = findSyncConflictById(id);
        return syncConflictMapper.toResponse(syncConflict);
    }

    @Override
    public PageResponse<SyncConflictResponse> getAllSyncConflicts(Pageable pageable) {
        Page<SyncConflict> syncConflictPage = syncConflictRepository.findAll(pageable);
        return createPageResponse(syncConflictPage);
    }

    @Override
    public List<SyncConflictResponse> getSyncConflictsByUser(Long userId) {
        List<SyncConflict> syncConflicts = syncConflictRepository.findByUserId(userId);
        return syncConflicts.stream()
            .map(syncConflictMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SyncConflictResponse> getSyncConflictsByDevice(String deviceId) {
        List<SyncConflict> syncConflicts = syncConflictRepository.findByDeviceId(deviceId);
        return syncConflicts.stream()
            .map(syncConflictMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SyncConflictResponse> getSyncConflictsByUserAndDevice(Long userId, String deviceId) {
        List<SyncConflict> syncConflicts = syncConflictRepository.findByUserIdAndDeviceId(userId, deviceId);
        return syncConflicts.stream()
            .map(syncConflictMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<SyncConflictResponse> getSyncConflictsByStatus(String status, Pageable pageable) {
        Page<SyncConflict> syncConflictPage = syncConflictRepository.findByStatus(status, pageable);
        return createPageResponse(syncConflictPage);
    }

    @Override
    public List<SyncConflictResponse> getSyncConflictsByEntity(String entityType, Long entityId) {
        List<SyncConflict> syncConflicts = syncConflictRepository.findByEntityTypeAndEntityId(entityType, entityId);
        return syncConflicts.stream()
            .map(syncConflictMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SyncConflictResponse> getSyncConflictsByType(String conflictType) {
        List<SyncConflict> syncConflicts = syncConflictRepository.findByConflictType(conflictType);
        return syncConflicts.stream()
            .map(syncConflictMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SyncConflictResponse> getUnresolvedConflicts() {
        List<SyncConflict> syncConflicts = syncConflictRepository.findByStatus("DETECTED");
        return syncConflicts.stream()
            .map(syncConflictMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SyncConflictResponse> getResolvedConflicts() {
        List<SyncConflict> syncConflicts = syncConflictRepository.findByStatus("RESOLVED");
        return syncConflicts.stream()
            .map(syncConflictMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void resolveConflict(Long id, String resolutionStrategy, Map<String, Object> resolvedData, Long resolvedBy) {
        log.info("Resolving sync conflict: {} with strategy: {}", id, resolutionStrategy);

        SyncConflict syncConflict = findSyncConflictById(id);

        if ("RESOLVED".equals(syncConflict.getStatus())) {
            throw new InvalidOperationException("Conflict is already resolved");
        }

        syncConflict.setResolutionStrategy(resolutionStrategy);
        syncConflict.setResolvedData(resolvedData.toString());
        syncConflict.setStatus("RESOLVED");
//        syncConflict.setResolvedBy(resolvedBy);
        syncConflict.setResolvedAt(LocalDateTime.now());

        syncConflictRepository.save(syncConflict);
        log.info("Sync conflict resolved successfully");
    }

    @Override
    @Transactional
    public void ignoreConflict(Long id) {
        log.info("Ignoring sync conflict: {}", id);

        SyncConflict syncConflict = findSyncConflictById(id);
        syncConflict.setStatus("IGNORED");
        syncConflictRepository.save(syncConflict);

        log.info("Sync conflict ignored");
    }

    @Override
    @Transactional
    public void autoResolveConflict(Long id, String resolutionStrategy) {
        log.info("Auto-resolving sync conflict: {} with strategy: {}", id, resolutionStrategy);

        SyncConflict syncConflict = findSyncConflictById(id);

        @NotBlank(message = "Server data is required") String resolvedData;
        if ("SERVER_WINS".equals(resolutionStrategy)) {
            resolvedData = syncConflict.getServerData();
        } else if ("CLIENT_WINS".equals(resolutionStrategy)) {
            resolvedData = syncConflict.getClientData();
        } else {
            throw new InvalidOperationException(
                "Auto-resolve only supports SERVER_WINS or CLIENT_WINS strategies");
        }

        syncConflict.setResolutionStrategy(resolutionStrategy);
        syncConflict.setResolvedData(resolvedData);
        syncConflict.setStatus("RESOLVED");
        syncConflict.setResolvedAt(LocalDateTime.now());

        syncConflictRepository.save(syncConflict);
        log.info("Sync conflict auto-resolved successfully");
    }

    @Override
    public PageResponse<SyncConflictResponse> searchSyncConflicts(String keyword, Pageable pageable) {
        Page<SyncConflict> syncConflictPage = syncConflictRepository.searchSyncConflicts(keyword, pageable);
        return createPageResponse(syncConflictPage);
    }

    private SyncConflict findSyncConflictById(Long id) {
        return syncConflictRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sync Conflict not found: " + id));
    }

    private PageResponse<SyncConflictResponse> createPageResponse(Page<SyncConflict> syncConflictPage) {
        List<SyncConflictResponse> content = syncConflictPage.getContent().stream()
            .map(syncConflictMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<SyncConflictResponse>builder()
            .content(content)
            .pageNumber(syncConflictPage.getNumber())
            .pageSize(syncConflictPage.getSize())
            .totalElements(syncConflictPage.getTotalElements())
            .totalPages(syncConflictPage.getTotalPages())
            .last(syncConflictPage.isLast())
            .first(syncConflictPage.isFirst())
            .empty(syncConflictPage.isEmpty())
            .build();
    }
}
