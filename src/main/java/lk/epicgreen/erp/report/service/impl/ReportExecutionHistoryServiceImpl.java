package lk.epicgreen.erp.report.service.impl;

import lk.epicgreen.erp.report.dto.request.ReportExecutionHistoryRequest;
import lk.epicgreen.erp.report.dto.response.ReportExecutionHistoryResponse;
import lk.epicgreen.erp.report.entity.ReportExecutionHistory;
import lk.epicgreen.erp.report.entity.SavedReport;
import lk.epicgreen.erp.report.mapper.ReportExecutionHistoryMapper;
import lk.epicgreen.erp.report.repository.ReportExecutionHistoryRepository;
import lk.epicgreen.erp.report.repository.SavedReportRepository;
import lk.epicgreen.erp.report.service.ReportExecutionHistoryService;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.admin.repository.UserRepository;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.dto.PageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of ReportExecutionHistoryService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReportExecutionHistoryServiceImpl implements ReportExecutionHistoryService {

    private final ReportExecutionHistoryRepository executionHistoryRepository;
    private final SavedReportRepository reportRepository;
    private final ReportExecutionHistoryMapper executionHistoryMapper;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public ReportExecutionHistoryResponse createExecutionHistory(ReportExecutionHistoryRequest request) {
        log.info("Creating execution history for report: {}", request.getReportId());

        SavedReport report = reportRepository.findById(request.getReportId())
            .orElseThrow(() -> new ResourceNotFoundException("Report not found: " + request.getReportId()));

        User user = userRepository.findById(request.getExecutedBy())
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getExecutedBy()));

        ReportExecutionHistory executionHistory = executionHistoryMapper.toEntity(request);
        executionHistory.setReport(report);
        executionHistory.setExecutedBy(user);

        ReportExecutionHistory savedHistory = executionHistoryRepository.save(executionHistory);
        log.info("Execution history created successfully with ID: {}", savedHistory.getId());

        return executionHistoryMapper.toResponse(savedHistory);
    }

    @Override
    @Transactional
    public ReportExecutionHistoryResponse startExecution(Long reportId, Long executedBy, Map<String, Object> parameters) {
        log.info("Starting report execution - Report: {}, User: {}", reportId, executedBy);

        SavedReport report = reportRepository.findById(reportId)
            .orElseThrow(() -> new ResourceNotFoundException("Report not found: " + reportId));

        User user = userRepository.findById(executedBy)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + executedBy));

        String parametersJson;
        try {
            parametersJson = objectMapper.writeValueAsString(parameters);
        } catch (JsonProcessingException e) {
            log.error("Error converting parameters to JSON", e);
            parametersJson = "{}";
        }

        ReportExecutionHistory executionHistory = ReportExecutionHistory.builder()
            .report(report)
            .executedBy(user)
            .parametersUsed(parametersJson)
            .status("RUNNING")
            .build();

        ReportExecutionHistory savedHistory = executionHistoryRepository.save(executionHistory);
        log.info("Report execution started with history ID: {}", savedHistory.getId());

        return executionHistoryMapper.toResponse(savedHistory);
    }

    @Override
    @Transactional
    public void markAsCompleted(Long id, String outputFilePath, Integer executionTimeMs) {
        log.info("Marking execution as completed: {}", id);

        ReportExecutionHistory executionHistory = findExecutionHistoryById(id);
        executionHistory.setStatus("COMPLETED");
        executionHistory.setOutputFilePath(outputFilePath);
        executionHistory.setExecutionTimeMs(executionTimeMs);
        executionHistoryRepository.save(executionHistory);

        log.info("Execution marked as completed: {} in {} ms", id, executionTimeMs);
    }

    @Override
    @Transactional
    public void markAsFailed(Long id, String errorMessage, Integer executionTimeMs) {
        log.error("Marking execution as failed: {} - Error: {}", id, errorMessage);

        ReportExecutionHistory executionHistory = findExecutionHistoryById(id);
        executionHistory.setStatus("FAILED");
        executionHistory.setErrorMessage(errorMessage);
        executionHistory.setExecutionTimeMs(executionTimeMs);
        executionHistoryRepository.save(executionHistory);

        log.error("Execution marked as failed: {}", id);
    }

    @Override
    @Transactional
    public ReportExecutionHistoryResponse updateExecutionHistory(Long id, ReportExecutionHistoryRequest request) {
        log.info("Updating execution history: {}", id);

        ReportExecutionHistory executionHistory = findExecutionHistoryById(id);
        executionHistoryMapper.updateEntityFromRequest(request, executionHistory);

        ReportExecutionHistory updatedHistory = executionHistoryRepository.save(executionHistory);
        log.info("Execution history updated successfully");

        return executionHistoryMapper.toResponse(updatedHistory);
    }

    @Override
    @Transactional
    public void deleteExecutionHistory(Long id) {
        log.info("Deleting execution history: {}", id);
        executionHistoryRepository.deleteById(id);
        log.info("Execution history deleted successfully: {}", id);
    }

    @Override
    public ReportExecutionHistoryResponse getExecutionHistoryById(Long id) {
        ReportExecutionHistory executionHistory = findExecutionHistoryById(id);
        return executionHistoryMapper.toResponse(executionHistory);
    }

    @Override
    public PageResponse<ReportExecutionHistoryResponse> getAllExecutionHistories(Pageable pageable) {
        Page<ReportExecutionHistory> executionHistoryPage = executionHistoryRepository.findAll(pageable);
        return createPageResponse(executionHistoryPage);
    }

    @Override
    public List<ReportExecutionHistoryResponse> getExecutionHistoriesByReport(Long reportId) {
        List<ReportExecutionHistory> histories = executionHistoryRepository.findByReportId(reportId);
        return histories.stream()
            .map(executionHistoryMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ReportExecutionHistoryResponse> getExecutionHistoriesByUser(Long executedBy) {
        List<ReportExecutionHistory> histories = executionHistoryRepository.findByExecutedBy(executedBy);
        return histories.stream()
            .map(executionHistoryMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<ReportExecutionHistoryResponse> getExecutionHistoriesByStatus(String status, Pageable pageable) {
        Page<ReportExecutionHistory> executionHistoryPage = executionHistoryRepository.findByStatus(status, pageable);
        return createPageResponse(executionHistoryPage);
    }

    @Override
    public List<ReportExecutionHistoryResponse> getExecutionHistoriesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<ReportExecutionHistory> histories = executionHistoryRepository.findByExecutedAtBetween(startDate, endDate);
        return histories.stream()
            .map(executionHistoryMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ReportExecutionHistoryResponse> getRunningExecutions() {
        List<ReportExecutionHistory> histories = executionHistoryRepository.findByStatus("RUNNING");
        return histories.stream()
            .map(executionHistoryMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ReportExecutionHistoryResponse> getFailedExecutions() {
        List<ReportExecutionHistory> histories = executionHistoryRepository.findByStatus("FAILED");
        return histories.stream()
            .map(executionHistoryMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ReportExecutionHistoryResponse> getRecentExecutionsForReport(Long reportId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        Page<ReportExecutionHistory> histories = executionHistoryRepository.findByReportIdOrderByExecutedAtDesc(reportId, pageable);
        return histories.getContent().stream()
            .map(executionHistoryMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getExecutionStatistics(Long reportId) {
        log.debug("Getting execution statistics for report: {}", reportId);

        List<ReportExecutionHistory> histories = executionHistoryRepository.findByReportId(reportId);

        Map<String, Object> statistics = new HashMap<>();
        
        // Total executions
        statistics.put("totalExecutions", histories.size());

        // Count by status
        long completedCount = histories.stream().filter(h -> "COMPLETED".equals(h.getStatus())).count();
        long failedCount = histories.stream().filter(h -> "FAILED".equals(h.getStatus())).count();
        long runningCount = histories.stream().filter(h -> "RUNNING".equals(h.getStatus())).count();

        statistics.put("completedExecutions", completedCount);
        statistics.put("failedExecutions", failedCount);
        statistics.put("runningExecutions", runningCount);

        // Average execution time
        if (completedCount > 0) {
            double avgExecutionTime = histories.stream()
                .filter(h -> "COMPLETED".equals(h.getStatus()) && h.getExecutionTimeMs() != null)
                .mapToInt(ReportExecutionHistory::getExecutionTimeMs)
                .average()
                .orElse(0.0);
            statistics.put("averageExecutionTimeMs", avgExecutionTime);
        }

        // Last execution
        if (!histories.isEmpty()) {
            ReportExecutionHistory lastExecution = histories.stream()
                .max((h1, h2) -> h1.getExecutedAt().compareTo(h2.getExecutedAt()))
                .orElse(null);
            
            if (lastExecution != null) {
                statistics.put("lastExecutedAt", lastExecution.getExecutedAt());
                statistics.put("lastExecutionStatus", lastExecution.getStatus());
            }
        }

        log.debug("Execution statistics generated: {}", statistics);
        return statistics;
    }

    @Override
    public PageResponse<ReportExecutionHistoryResponse> searchExecutionHistories(String keyword, Pageable pageable) {
        Page<ReportExecutionHistory> executionHistoryPage = executionHistoryRepository.searchExecutionHistories(keyword, pageable);
        return createPageResponse(executionHistoryPage);
    }

    @Override
    @Transactional
    public void cleanupOldHistories(int daysToKeep) {
        log.info("Cleaning up execution histories older than {} days", daysToKeep);

        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
        int deletedCount = executionHistoryRepository.deleteByExecutedAtBefore(cutoffDate);

        log.info("Cleaned up {} old execution histories", deletedCount);
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private ReportExecutionHistory findExecutionHistoryById(Long id) {
        return executionHistoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Report Execution History not found: " + id));
    }

    private PageResponse<ReportExecutionHistoryResponse> createPageResponse(Page<ReportExecutionHistory> executionHistoryPage) {
        List<ReportExecutionHistoryResponse> content = executionHistoryPage.getContent().stream()
            .map(executionHistoryMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<ReportExecutionHistoryResponse>builder()
            .content(content)
            .pageNumber(executionHistoryPage.getNumber())
            .pageSize(executionHistoryPage.getSize())
            .totalElements(executionHistoryPage.getTotalElements())
            .totalPages(executionHistoryPage.getTotalPages())
            .last(executionHistoryPage.isLast())
            .first(executionHistoryPage.isFirst())
            .empty(executionHistoryPage.isEmpty())
            .build();
    }
}
