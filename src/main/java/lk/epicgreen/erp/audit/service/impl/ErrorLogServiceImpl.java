package lk.epicgreen.erp.audit.service.impl;

import lk.epicgreen.erp.audit.dto.request.ErrorLogRequest;
import lk.epicgreen.erp.audit.dto.response.ErrorLogResponse;
import lk.epicgreen.erp.audit.entity.ErrorLog;
import lk.epicgreen.erp.audit.mapper.ErrorLogMapper;
import lk.epicgreen.erp.audit.repository.ErrorLogRepository;
import lk.epicgreen.erp.audit.service.ErrorLogService;
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
 * Implementation of ErrorLogService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ErrorLogServiceImpl implements ErrorLogService {

    private final ErrorLogRepository errorLogRepository;
    private final ErrorLogMapper errorLogMapper;

    @Override
    @Transactional
    public ErrorLogResponse createErrorLog(ErrorLogRequest request) {
        log.error("Creating error log - Type: {}, Message: {}", request.getErrorType(), request.getErrorMessage());

        ErrorLog errorLog = errorLogMapper.toEntity(request);
        ErrorLog savedErrorLog = errorLogRepository.save(errorLog);

        log.debug("Error log created successfully with ID: {}", savedErrorLog.getId());

        return errorLogMapper.toResponse(savedErrorLog);
    }

    @Override
    @Transactional
    public ErrorLogResponse updateErrorLog(Long id, ErrorLogRequest request) {
        log.info("Updating Error Log: {}", id);

        ErrorLog errorLog = findErrorLogById(id);
        errorLogMapper.updateEntityFromRequest(request, errorLog);

        ErrorLog updatedErrorLog = errorLogRepository.save(errorLog);
        log.info("Error Log updated successfully: {}", updatedErrorLog.getId());

        return errorLogMapper.toResponse(updatedErrorLog);
    }

    @Override
    @Transactional
    public void resolveError(Long id, Long resolvedBy) {
        log.info("Resolving Error Log: {} by user: {}", id, resolvedBy);

        ErrorLog errorLog = findErrorLogById(id);

        if (errorLog.getIsResolved()) {
            log.warn("Error Log {} is already resolved", id);
            return;
        }

        errorLog.setIsResolved(true);
        errorLog.setResolvedBy(resolvedBy);
        errorLog.setResolvedAt(LocalDateTime.now());
        errorLogRepository.save(errorLog);

        log.info("Error Log resolved successfully: {}", id);
    }

    @Override
    @Transactional
    public void deleteErrorLog(Long id) {
        log.info("Deleting Error Log: {}", id);

        errorLogRepository.deleteById(id);
        log.info("Error Log deleted successfully: {}", id);
    }

    @Override
    public ErrorLogResponse getErrorLogById(Long id) {
        ErrorLog errorLog = findErrorLogById(id);
        return errorLogMapper.toResponse(errorLog);
    }

    @Override
    public PageResponse<ErrorLogResponse> getAllErrorLogs(Pageable pageable) {
        Page<ErrorLog> errorLogPage = errorLogRepository.findAll(pageable);
        return createPageResponse(errorLogPage);
    }

    @Override
    public PageResponse<ErrorLogResponse> getErrorLogsBySeverity(String severity, Pageable pageable) {
        Page<ErrorLog> errorLogPage = errorLogRepository.findBySeverity(severity, pageable);
        return createPageResponse(errorLogPage);
    }

    @Override
    public List<ErrorLogResponse> getErrorLogsByErrorType(String errorType) {
        List<ErrorLog> errorLogs = errorLogRepository.findByErrorType(errorType);
        return errorLogs.stream()
            .map(errorLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ErrorLogResponse> getUnresolvedErrors() {
        List<ErrorLog> errorLogs = errorLogRepository.findByIsResolvedFalse();
        return errorLogs.stream()
            .map(errorLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<ErrorLogResponse> getResolvedErrors(Pageable pageable) {
        Page<ErrorLog> errorLogPage = errorLogRepository.findByIsResolvedTrue(pageable);
        return createPageResponse(errorLogPage);
    }

    @Override
    public List<ErrorLogResponse> getCriticalErrors() {
        List<ErrorLog> errorLogs = errorLogRepository.findBySeverityAndIsResolvedFalse("CRITICAL");
        return errorLogs.stream()
            .map(errorLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ErrorLogResponse> getErrorLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<ErrorLog> errorLogs = errorLogRepository.findByCreatedAtBetween(startDate, endDate);
        return errorLogs.stream()
            .map(errorLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ErrorLogResponse> getErrorLogsByUser(Long userId) {
        List<ErrorLog> errorLogs = errorLogRepository.findByUserId(userId);
        return errorLogs.stream()
            .map(errorLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<ErrorLogResponse> searchErrorLogs(String keyword, Pageable pageable) {
        Page<ErrorLog> errorLogPage = errorLogRepository.searchErrorLogs(keyword, pageable);
        return createPageResponse(errorLogPage);
    }

    @Override
    public Long getErrorCountByType(String errorType) {
        return errorLogRepository.countByErrorType(errorType);
    }

    @Override
    public Long getErrorCountBySeverity(String severity) {
        return errorLogRepository.countBySeverity(severity);
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private ErrorLog findErrorLogById(Long id) {
        return errorLogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Error Log not found: " + id));
    }

    private PageResponse<ErrorLogResponse> createPageResponse(Page<ErrorLog> errorLogPage) {
        List<ErrorLogResponse> content = errorLogPage.getContent().stream()
            .map(errorLogMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<ErrorLogResponse>builder()
            .content(content)
            .pageNumber(errorLogPage.getNumber())
            .pageSize(errorLogPage.getSize())
            .totalElements(errorLogPage.getTotalElements())
            .totalPages(errorLogPage.getTotalPages())
            .last(errorLogPage.isLast())
            .first(errorLogPage.isFirst())
            .empty(errorLogPage.isEmpty())
            .build();
    }
}
