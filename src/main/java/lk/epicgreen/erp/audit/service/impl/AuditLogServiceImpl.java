package lk.epicgreen.erp.audit.service.impl;

import lk.epicgreen.erp.audit.dto.request.AuditLogRequest;
import lk.epicgreen.erp.audit.dto.response.AuditLogResponse;
import lk.epicgreen.erp.audit.entity.AuditLog;
import lk.epicgreen.erp.audit.mapper.AuditLogMapper;
import lk.epicgreen.erp.audit.repository.AuditLogRepository;
import lk.epicgreen.erp.audit.service.AuditLogService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
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

/**
 * Implementation of AuditLogService interface
 * 
 * Audit logs are immutable - no UPDATE or DELETE operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @Override
    @Transactional
    public AuditLogResponse createAuditLog(AuditLogRequest request) {
        log.debug("Creating audit log for action: {} on entity: {}", request.getAction(), request.getEntityType());

        AuditLog auditLog = auditLogMapper.toEntity(request);
        AuditLog savedAuditLog = auditLogRepository.save(auditLog);

        log.debug("Audit log created successfully with ID: {}", savedAuditLog.getId());

        return auditLogMapper.toResponse(savedAuditLog);
    }

    @Override
    public AuditLogResponse getAuditLogById(Long id) {
        AuditLog auditLog = findAuditLogById(id);
        return auditLogMapper.toResponse(auditLog);
    }

    @Override
    public PageResponse<AuditLogResponse> getAllAuditLogs(Pageable pageable) {
        Page<AuditLog> auditLogPage = auditLogRepository.findAll(pageable);
        return createPageResponse(auditLogPage);
    }

    @Override
    public List<AuditLogResponse> getAuditLogsByUser(Long userId) {
        List<AuditLog> auditLogs = auditLogRepository.findByUserId(userId);
        return auditLogs.stream()
            .map(auditLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponse> getAuditLogsByEntity(String entityType, Long entityId) {
        List<AuditLog> auditLogs = auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId);
        return auditLogs.stream()
            .map(auditLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<AuditLogResponse> getAuditLogsByOperationType(String operationType, Pageable pageable) {
        Page<AuditLog> auditLogPage = auditLogRepository.findByOperationType(operationType, pageable);
        return createPageResponse(auditLogPage);
    }

    @Override
    public PageResponse<AuditLogResponse> getAuditLogsByStatus(String status, Pageable pageable) {
        Page<AuditLog> auditLogPage = auditLogRepository.findByStatus(status, pageable);
        return createPageResponse(auditLogPage);
    }

    @Override
    public List<AuditLogResponse> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<AuditLog> auditLogs = auditLogRepository.findByCreatedAtBetween(startDate, endDate);
        return auditLogs.stream()
            .map(auditLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponse> getAuditLogsByAction(String action) {
        List<AuditLog> auditLogs = auditLogRepository.findByAction(action);
        return auditLogs.stream()
            .map(auditLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponse> getFailedOperations() {
        List<AuditLog> auditLogs = auditLogRepository.findByStatus("FAILED");
        return auditLogs.stream()
            .map(auditLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<AuditLogResponse> searchAuditLogs(String keyword, Pageable pageable) {
        Page<AuditLog> auditLogPage = auditLogRepository.searchAuditLogs(keyword, pageable);
        return createPageResponse(auditLogPage);
    }

    @Override
    public List<AuditLogResponse> getAuditLogsBySession(String sessionId) {
        List<AuditLog> auditLogs = auditLogRepository.findBySessionId(sessionId);
        return auditLogs.stream()
            .map(auditLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponse> getAuditLogsByIpAddress(String ipAddress) {
        List<AuditLog> auditLogs = auditLogRepository.findByIpAddress(ipAddress);
        return auditLogs.stream()
            .map(auditLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private AuditLog findAuditLogById(Long id) {
        return auditLogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Audit Log not found: " + id));
    }

    private PageResponse<AuditLogResponse> createPageResponse(Page<AuditLog> auditLogPage) {
        List<AuditLogResponse> content = auditLogPage.getContent().stream()
            .map(auditLogMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<AuditLogResponse>builder()
            .content(content)
            .pageNumber(auditLogPage.getNumber())
            .pageSize(auditLogPage.getSize())
            .totalElements(auditLogPage.getTotalElements())
            .totalPages(auditLogPage.getTotalPages())
            .last(auditLogPage.isLast())
            .first(auditLogPage.isFirst())
            .empty(auditLogPage.isEmpty())
            .build();
    }
}
