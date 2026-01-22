package lk.epicgreen.erp.mobile.service.impl;

import lk.epicgreen.erp.mobile.dto.request.SyncLogRequest;
import lk.epicgreen.erp.mobile.dto.response.SyncLogResponse;
import lk.epicgreen.erp.mobile.entity.SyncLog;
import lk.epicgreen.erp.mobile.mapper.SyncLogMapper;
import lk.epicgreen.erp.mobile.repository.SyncLogRepository;
import lk.epicgreen.erp.mobile.service.SyncLogService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SyncLogServiceImpl implements SyncLogService {

    private final SyncLogRepository syncLogRepository;
    private final SyncLogMapper syncLogMapper;

    @Override
    @Transactional
    public SyncLogResponse createSyncLog(SyncLogRequest request) {
        log.info("Creating sync log for user: {} device: {}", request.getUserId(), request.getDeviceId());

        SyncLog syncLog = syncLogMapper.toEntity(request);
        SyncLog savedSyncLog = syncLogRepository.save(syncLog);

        log.info("Sync log created successfully with ID: {}", savedSyncLog.getId());
        return syncLogMapper.toResponse(savedSyncLog);
    }

    @Override
    @Transactional
    public SyncLogResponse updateSyncLog(Long id, SyncLogRequest request) {
        log.info("Updating sync log: {}", id);

        SyncLog syncLog = findSyncLogById(id);
        syncLogMapper.updateEntityFromRequest(request, syncLog);

        SyncLog updatedSyncLog = syncLogRepository.save(syncLog);
        log.info("Sync log updated successfully");

        return syncLogMapper.toResponse(updatedSyncLog);
    }

    @Transactional
    public List<SyncLog> getSuccessfulSyncLogs(){
        log.info("Fetching successful sync logs");
        List<SyncLog> syncLogs = syncLogRepository.findBySyncStatus("COMPLETED");
        log.info("Fetched {} successful sync logs", syncLogs.size());
        return syncLogs;
    }
    @Transactional
    public List<SyncLog> getFailedSyncLogs(){
        log.info("Fetching failed sync logs");
        List<SyncLog> syncLogs = syncLogRepository.findBySyncStatus("FAILED");
        log.info("Fetched {} failed sync logs", syncLogs.size());
        return syncLogs;
    }

    @Transactional
    public List<SyncLog> getSyncLogsWithConflicts(){
        log.info("Fetching sync logs with conflicts");
        List<SyncLog> syncLogs = syncLogRepository.findByConflictsDetectedGreaterThan(0);
        log.info("Fetched {} sync logs with conflicts", syncLogs.size());
        return syncLogs;
    }
    public List<SyncLog> getUnresolvedConflicts(){
        log.info("Fetching sync logs with unresolved conflicts");
        List<SyncLog> syncLogs = syncLogRepository.findByConflictsResolvedLessThanConflictsDetected();
        log.info("Fetched {} sync logs with unresolved conflicts", syncLogs.size());
        return syncLogs;
    }

    @Override
    @Transactional
    public void deleteSyncLog(Long id) {

        log.info("Deleting sync log: {}", id);
        syncLogRepository.deleteById(id);
        log.info("Sync log deleted successfully");

    }

    @Transactional
    public List<SyncLog> getUserUnresolvedConflicts(Long userId){
        log.info("Fetching unresolved conflicts for user: {}", userId);
        List<SyncLog> syncLogs = syncLogRepository.findByUserIdAndConflictsResolvedLessThanConflictsDetected(userId);
        log.info("Fetched {} unresolved conflicts for user: {}", syncLogs.size(), userId);
        return syncLogs;
    }

    @Transactional
    public boolean detectConflict(Long logId){
        log.info("Detecting conflicts for sync log: {}", logId);
        SyncLog syncLog = findSyncLogById(logId);
        boolean hasConflict = syncLog.getConflictsDetected() != null && syncLog.getConflictsDetected() > 0;
        log.info("Conflict detection result for sync log {}: {}", logId, hasConflict);
        return hasConflict;
    }

    @Transactional
    public SyncLogResponse resolveConflictServerWins(Long logId){
        log.info("Resolving conflicts (server wins) for sync log: {}", logId);
        SyncLog syncLog = findSyncLogById(logId);
        syncLog.setConflictsResolved(syncLog.getConflictsDetected());
        SyncLog updatedSyncLog = syncLogRepository.save(syncLog);
        log.info("Conflicts resolved for sync log: {}", logId);
        return syncLogMapper.toResponse(updatedSyncLog);
    }

    @Transactional
    public SyncLogResponse resolveConflictClientWins(Long logId){
        log.info("Resolving conflicts (client wins) for sync log: {}", logId);
        SyncLog syncLog = findSyncLogById(logId);
        syncLog.setConflictsResolved(syncLog.getConflictsDetected());
        SyncLog updatedSyncLog = syncLogRepository.save(syncLog);
        log.info("Conflicts resolved for sync log: {}", logId);
        return syncLogMapper.toResponse(updatedSyncLog);
    }

    @Transactional
    public SyncLogResponse resolveConflictManualMerge(Long logId, Map<String,Object> mergedData){
        log.info("Resolving conflicts (manual merge) for sync log: {}", logId);
        SyncLog syncLog = findSyncLogById(logId);
        syncLog.setConflictsResolved(syncLog.getConflictsDetected());
        SyncLog updatedSyncLog = syncLogRepository.save(syncLog);
        log.info("Conflicts resolved for sync log: {}", logId);
        return syncLogMapper.toResponse(updatedSyncLog);
    }

    @Override
    public SyncLogResponse getSyncLogById(Long id) {
        SyncLog syncLog = findSyncLogById(id);
        return syncLogMapper.toResponse(syncLog);
    }

    @Override
    public PageResponse<SyncLogResponse> getAllSyncLogs(Pageable pageable) {
        Page<SyncLog> syncLogPage = syncLogRepository.findAll(pageable);
        return createPageResponse(syncLogPage);
    }

    @Override
    public List<SyncLogResponse> getSyncLogsByUser(Long userId) {
        List<SyncLog> syncLogs = syncLogRepository.findByUserId(userId);
        return syncLogs.stream()
            .map(syncLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SyncLogResponse> getSyncLogsByDevice(String deviceId) {
        List<SyncLog> syncLogs = syncLogRepository.findByDeviceId(deviceId);
        return syncLogs.stream()
            .map(syncLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SyncLogResponse> getSyncLogsByUserAndDevice(Long userId, String deviceId) {
        List<SyncLog> syncLogs = syncLogRepository.findByUserIdAndDeviceId(userId, deviceId);
        return syncLogs.stream()
            .map(syncLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<SyncLogResponse> getSyncLogsByStatus(String syncStatus, Pageable pageable) {
        Page<SyncLog> syncLogPage = syncLogRepository.findBySyncStatus(syncStatus, pageable);
        return createPageResponse(syncLogPage);
    }

    @Override
    public List<SyncLogResponse> getSyncLogsByType(String syncType) {
        List<SyncLog> syncLogs = syncLogRepository.findBySyncType(syncType);
        return syncLogs.stream()
            .map(syncLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SyncLogResponse> getSyncLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<SyncLog> syncLogs = syncLogRepository.findByStartedAtBetween(startDate, endDate);
        return syncLogs.stream()
            .map(syncLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SyncLogResponse> getActiveSyncs() {
        List<SyncLog> syncLogs = syncLogRepository.findBySyncStatusIn(Arrays.asList("INITIATED", "IN_PROGRESS"));
        return syncLogs.stream()
            .map(syncLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SyncLogResponse> getFailedSyncs() {
        List<SyncLog> syncLogs = syncLogRepository.findBySyncStatus("FAILED");
        return syncLogs.stream()
            .map(syncLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsCompleted(Long id, Integer recordsUploaded, Integer recordsDownloaded,
                               Integer conflictsDetected, Integer conflictsResolved) {
        log.info("Marking sync log as completed: {}", id);

        SyncLog syncLog = findSyncLogById(id);
        syncLog.setSyncStatus("COMPLETED");
        syncLog.setRecordsUploaded(recordsUploaded);
        syncLog.setRecordsDownloaded(recordsDownloaded);
        syncLog.setConflictsDetected(conflictsDetected);
        syncLog.setConflictsResolved(conflictsResolved);
        syncLog.setCompletedAt(LocalDateTime.now());

        if (syncLog.getStartedAt() != null) {
            Duration duration = Duration.between(syncLog.getStartedAt(), syncLog.getCompletedAt());
            syncLog.setDurationSeconds((int) duration.getSeconds());
        }

        syncLogRepository.save(syncLog);
        log.info("Sync log marked as completed");
    }

    @Override
    @Transactional
    public void markAsFailed(Long id, String errorMessage) {
        log.error("Marking sync log as failed: {} - Error: {}", id, errorMessage);

        SyncLog syncLog = findSyncLogById(id);
        syncLog.setSyncStatus("FAILED");
        syncLog.setErrorMessage(errorMessage);
        syncLog.setCompletedAt(LocalDateTime.now());

        if (syncLog.getStartedAt() != null) {
            Duration duration = Duration.between(syncLog.getStartedAt(), syncLog.getCompletedAt());
            syncLog.setDurationSeconds((int) duration.getSeconds());
        }

        syncLogRepository.save(syncLog);
        log.error("Sync log marked as failed");
    }

    @Override
    @Transactional
    public void updateProgress(Long id, Integer recordsUploaded, Integer recordsDownloaded) {
        log.debug("Updating sync progress: {}", id);

        SyncLog syncLog = findSyncLogById(id);
        syncLog.setSyncStatus("IN_PROGRESS");
        syncLog.setRecordsUploaded(recordsUploaded);
        syncLog.setRecordsDownloaded(recordsDownloaded);
        syncLogRepository.save(syncLog);

        log.debug("Sync progress updated");
    }

    @Override
    public PageResponse<SyncLogResponse> searchSyncLogs(String keyword, Pageable pageable) {
        Page<SyncLog> syncLogPage = syncLogRepository.searchSyncLogs(null,null,keyword,null,null,null,null,pageable);
        return createPageResponse(syncLogPage);
    }

    private SyncLog findSyncLogById(Long id) {
        return syncLogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sync Log not found: " + id));
    }

    private PageResponse<SyncLogResponse> createPageResponse(Page<SyncLog> syncLogPage) {
        List<SyncLogResponse> content = syncLogPage.getContent().stream()
            .map(syncLogMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<SyncLogResponse>builder()
            .content(content)
            .pageNumber(syncLogPage.getNumber())
            .pageSize(syncLogPage.getSize())
            .totalElements(syncLogPage.getTotalElements())
            .totalPages(syncLogPage.getTotalPages())
            .last(syncLogPage.isLast())
            .first(syncLogPage.isFirst())
            .empty(syncLogPage.isEmpty())
            .build();
    }
}
