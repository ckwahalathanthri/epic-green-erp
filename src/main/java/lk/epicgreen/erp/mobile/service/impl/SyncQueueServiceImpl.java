package lk.epicgreen.erp.mobile.service.impl;

import lk.epicgreen.erp.mobile.dto.request.SyncQueueRequest;
import lk.epicgreen.erp.mobile.dto.response.SyncQueueResponse;
import lk.epicgreen.erp.mobile.entity.SyncQueue;
import lk.epicgreen.erp.mobile.mapper.SyncQueueMapper;
import lk.epicgreen.erp.mobile.repository.SyncQueueRepository;
import lk.epicgreen.erp.mobile.service.SyncQueueService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.InvalidOperationException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SyncQueueServiceImpl implements SyncQueueService {

    private final SyncQueueRepository syncQueueRepository;
    private final SyncQueueMapper syncQueueMapper;

    @Override
    @Transactional
    public SyncQueueResponse createSyncQueueItem(SyncQueueRequest request) {
        log.info("Creating sync queue item for user: {} entity: {}", 
            request.getUserId(), request.getEntityType());

        SyncQueue syncQueue = syncQueueMapper.toEntity(request);
        SyncQueue savedSyncQueue = syncQueueRepository.save(syncQueue);

        log.info("Sync queue item created successfully with ID: {}", savedSyncQueue.getId());
        return syncQueueMapper.toResponse(savedSyncQueue);
    }

    @Override
    @Transactional
    public SyncQueueResponse updateSyncQueueItem(Long id, SyncQueueRequest request) {
        log.info("Updating sync queue item: {}", id);

        SyncQueue syncQueue = findSyncQueueById(id);

        if (!canUpdate(id)) {
            throw new InvalidOperationException(
                "Cannot update sync queue item. Current status: " + syncQueue.getSyncStatus());
        }

        syncQueueMapper.updateEntityFromRequest(request, syncQueue);
        SyncQueue updatedSyncQueue = syncQueueRepository.save(syncQueue);

        log.info("Sync queue item updated successfully");
        return syncQueueMapper.toResponse(updatedSyncQueue);
    }

    @Override
    @Transactional
    public void deleteSyncQueueItem(Long id) {
        log.info("Deleting sync queue item: {}", id);

        if (!canDelete(id)) {
            throw new InvalidOperationException("Cannot delete synced item");
        }

        syncQueueRepository.deleteById(id);
        log.info("Sync queue item deleted successfully");
    }

    @Override
    public SyncQueueResponse getSyncQueueItemById(Long id) {
        SyncQueue syncQueue = findSyncQueueById(id);
        return syncQueueMapper.toResponse(syncQueue);
    }

    @Override
    public PageResponse<SyncQueueResponse> getAllSyncQueueItems(Pageable pageable) {
        Page<SyncQueue> syncQueuePage = syncQueueRepository.findAll(pageable);
        return createPageResponse(syncQueuePage);
    }

    @Override
    public List<SyncQueueResponse> getSyncQueueByUser(Long userId) {
        List<SyncQueue> syncQueues = syncQueueRepository.findByUserId(userId);
        return syncQueues.stream()
            .map(syncQueueMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SyncQueueResponse> getSyncQueueByDevice(String deviceId) {
        List<SyncQueue> syncQueues = syncQueueRepository.findByDeviceId(deviceId);
        return syncQueues.stream()
            .map(syncQueueMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SyncQueueResponse> getSyncQueueByUserAndDevice(Long userId, String deviceId) {
        List<SyncQueue> syncQueues = syncQueueRepository.findByUserIdAndDeviceId(userId, deviceId);
        return syncQueues.stream()
            .map(syncQueueMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<SyncQueueResponse> getSyncQueueByStatus(String syncStatus, Pageable pageable) {
        Page<SyncQueue> syncQueuePage = syncQueueRepository.findBySyncStatus(syncStatus, pageable);
        return createPageResponse(syncQueuePage);
    }

    @Override
    public List<SyncQueueResponse> getSyncQueueByEntity(String entityType, Long entityId) {
        List<SyncQueue> syncQueues = syncQueueRepository.findByEntityTypeAndEntityId(entityType, entityId);
        return syncQueues.stream()
            .map(syncQueueMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SyncQueueResponse> getPendingItems() {
        List<SyncQueue> syncQueues = syncQueueRepository.findBySyncStatus("PENDING");
        return syncQueues.stream()
            .map(syncQueueMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SyncQueueResponse> getFailedItems() {
        List<SyncQueue> syncQueues = syncQueueRepository.findBySyncStatus("FAILED");
        return syncQueues.stream()
            .map(syncQueueMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SyncQueueResponse> getConflictItems() {
        List<SyncQueue> syncQueues = syncQueueRepository.findBySyncStatus("CONFLICT");
        return syncQueues.stream()
            .map(syncQueueMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsInProgress(Long id) {
        log.info("Marking sync queue item as in progress: {}", id);

        SyncQueue syncQueue = findSyncQueueById(id);
        syncQueue.setSyncStatus("IN_PROGRESS");
        syncQueueRepository.save(syncQueue);

        log.info("Sync queue item marked as in progress");
    }

    @Override
    @Transactional
    public void markAsSynced(Long id) {
        log.info("Marking sync queue item as synced: {}", id);

        SyncQueue syncQueue = findSyncQueueById(id);
        syncQueue.setSyncStatus("SYNCED");
        syncQueue.setSyncedAt(LocalDateTime.now());
        syncQueueRepository.save(syncQueue);

        log.info("Sync queue item marked as synced");
    }

    @Override
    @Transactional
    public void markAsFailed(Long id, String errorMessage) {
        log.error("Marking sync queue item as failed: {} - Error: {}", id, errorMessage);

        SyncQueue syncQueue = findSyncQueueById(id);
        syncQueue.setSyncStatus("FAILED");
        syncQueue.setErrorMessage(errorMessage);
        syncQueue.setRetryCount(syncQueue.getRetryCount() + 1);
        syncQueueRepository.save(syncQueue);

        log.error("Sync queue item marked as failed");
    }

    @Override
    @Transactional
    public void markAsConflict(Long id) {
        log.warn("Marking sync queue item as conflict: {}", id);

        SyncQueue syncQueue = findSyncQueueById(id);
        syncQueue.setSyncStatus("CONFLICT");
        syncQueueRepository.save(syncQueue);

        log.warn("Sync queue item marked as conflict");
    }

    @Override
    @Transactional
    public void retrySyncItem(Long id) {
        log.info("Retrying sync queue item: {}", id);

        SyncQueue syncQueue = findSyncQueueById(id);

        if (!"FAILED".equals(syncQueue.getSyncStatus())) {
            throw new InvalidOperationException("Only FAILED items can be retried");
        }

        if (syncQueue.getRetryCount() >= syncQueue.getMaxRetries()) {
            throw new InvalidOperationException(
                "Maximum retry attempts reached (" + syncQueue.getMaxRetries() + ")");
        }

        syncQueue.setSyncStatus("PENDING");
        syncQueue.setErrorMessage(null);
        syncQueueRepository.save(syncQueue);

        log.info("Sync queue item queued for retry");
    }

    @Override
    @Transactional
    public void processSyncQueue(Long userId, String deviceId) {
        log.info("Processing sync queue for user: {} device: {}", userId, deviceId);

        List<SyncQueue> pendingItems = syncQueueRepository.findByUserIdAndDeviceIdAndSyncStatus(
            userId, deviceId, "PENDING");

        for (SyncQueue item : pendingItems) {
            try {
                markAsInProgress(item.getId());
                // TODO: Implement actual sync logic here
                markAsSynced(item.getId());
            } catch (Exception e) {
                log.error("Failed to sync item {}: {}", item.getId(), e.getMessage());
                markAsFailed(item.getId(), e.getMessage());
            }
        }

        log.info("Sync queue processing completed. Processed: {}", pendingItems.size());
    }

    @Override
    public PageResponse<SyncQueueResponse> searchSyncQueue(String keyword, Pageable pageable) {
        Page<SyncQueue> syncQueuePage = syncQueueRepository.searchSyncQueue(keyword, pageable);
        return createPageResponse(syncQueuePage);
    }

    @Override
    public boolean canUpdate(Long id) {
        SyncQueue syncQueue = findSyncQueueById(id);
        return "PENDING".equals(syncQueue.getSyncStatus()) || "FAILED".equals(syncQueue.getSyncStatus());
    }

    @Override
    public boolean canDelete(Long id) {
        SyncQueue syncQueue = findSyncQueueById(id);
        return !"SYNCED".equals(syncQueue.getSyncStatus());
    }

    private SyncQueue findSyncQueueById(Long id) {
        return syncQueueRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sync Queue item not found: " + id));
    }

    private PageResponse<SyncQueueResponse> createPageResponse(Page<SyncQueue> syncQueuePage) {
        List<SyncQueueResponse> content = syncQueuePage.getContent().stream()
            .map(syncQueueMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<SyncQueueResponse>builder()
            .content(content)
            .pageNumber(syncQueuePage.getNumber())
            .pageSize(syncQueuePage.getSize())
            .totalElements(syncQueuePage.getTotalElements())
            .totalPages(syncQueuePage.getTotalPages())
            .last(syncQueuePage.isLast())
            .first(syncQueuePage.isFirst())
            .empty(syncQueuePage.isEmpty())
            .build();
    }
}
