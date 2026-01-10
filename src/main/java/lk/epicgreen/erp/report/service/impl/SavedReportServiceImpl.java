package lk.epicgreen.erp.report.service.impl;

import lk.epicgreen.erp.report.dto.request.SavedReportRequest;
import lk.epicgreen.erp.report.dto.response.ReportExecutionHistoryResponse;
import lk.epicgreen.erp.report.dto.response.SavedReportResponse;
import lk.epicgreen.erp.report.entity.SavedReport;
import lk.epicgreen.erp.report.mapper.SavedReportMapper;
import lk.epicgreen.erp.report.repository.SavedReportRepository;
import lk.epicgreen.erp.report.service.SavedReportService;
import lk.epicgreen.erp.report.service.ReportExecutionHistoryService;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.admin.repository.UserRepository;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of SavedReportService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SavedReportServiceImpl implements SavedReportService {

    private final SavedReportRepository reportRepository;
    private final SavedReportMapper reportMapper;
    private final ReportExecutionHistoryService executionHistoryService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SavedReportResponse createReport(SavedReportRequest request, Long createdBy) {
        log.info("Creating saved report: {}", request.getReportCode());

        validateUniqueReportCode(request.getReportCode(), null);

        User user = userRepository.findById(createdBy)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + createdBy));

        SavedReport report = reportMapper.toEntity(request);
        report.setCreatedBy(user);

        SavedReport savedReport = reportRepository.save(report);
        log.info("Saved report created successfully: {}", savedReport.getReportCode());

        return reportMapper.toResponse(savedReport);
    }

    @Override
    @Transactional
    public SavedReportResponse updateReport(Long id, SavedReportRequest request) {
        log.info("Updating saved report: {}", id);

        SavedReport report = findReportById(id);

        if (!report.getReportCode().equals(request.getReportCode())) {
            validateUniqueReportCode(request.getReportCode(), id);
        }

        reportMapper.updateEntityFromRequest(request, report);
        SavedReport updatedReport = reportRepository.save(report);

        log.info("Saved report updated successfully: {}", updatedReport.getReportCode());
        return reportMapper.toResponse(updatedReport);
    }

    @Override
    @Transactional
    public void deleteReport(Long id) {
        log.info("Deleting saved report: {}", id);

        if (!canDelete(id)) {
            throw new ResourceNotFoundException("Cannot delete report. It may have execution history.");
        }

        reportRepository.deleteById(id);
        log.info("Saved report deleted successfully: {}", id);
    }

    @Override
    public SavedReportResponse getReportById(Long id) {
        SavedReport report = findReportById(id);
        return reportMapper.toResponse(report);
    }

    @Override
    public SavedReportResponse getReportByCode(String reportCode) {
        SavedReport report = reportRepository.findByReportCode(reportCode)
            .orElseThrow(() -> new ResourceNotFoundException("Report not found: " + reportCode));
        return reportMapper.toResponse(report);
    }

    @Override
    public PageResponse<SavedReportResponse> getAllReports(Pageable pageable) {
        Page<SavedReport> reportPage = reportRepository.findAll(pageable);
        return createPageResponse(reportPage);
    }

    @Override
    public List<SavedReportResponse> getReportsByType(String reportType) {
        List<SavedReport> reports = reportRepository.findByReportType(reportType);
        return reports.stream()
            .map(reportMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SavedReportResponse> getReportsByCategory(String reportCategory) {
        List<SavedReport> reports = reportRepository.findByReportCategory(reportCategory);
        return reports.stream()
            .map(reportMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SavedReportResponse> getReportsByCreator(Long createdBy) {
        List<SavedReport> reports = reportRepository.findByCreatedById(createdBy);
        return reports.stream()
            .map(reportMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SavedReportResponse> getPublicReports() {
        List<SavedReport> reports = reportRepository.findByIsPublicTrue();
        return reports.stream()
            .map(reportMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SavedReportResponse> getUserAccessibleReports(Long userId) {
        List<SavedReport> reports = reportRepository.findByCreatedByOrIsPublicTrue(userId);
        return reports.stream()
            .map(reportMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public byte[] executeReport(Long reportId, Map<String, Object> parameters, Long executedBy) {
        log.info("Executing report: {} by user: {}", reportId, executedBy);

        SavedReport report = findReportById(reportId);

        // Start execution tracking
        ReportExecutionHistoryResponse executionHistory = executionHistoryService.startExecution(reportId, executedBy, parameters);

        try {
            long startTime = System.currentTimeMillis();

            // TODO: Implement actual report generation logic here
            // This is a placeholder for the actual report generation implementation
            // The implementation would vary based on the report type and query template

            byte[] reportData = generateReportData(report, parameters);

            long executionTime = System.currentTimeMillis() - startTime;

            // TODO: Save report file and get file path
            String outputFilePath = "/reports/" + report.getReportCode() + "_" + System.currentTimeMillis() + 
                                  "." + report.getOutputFormat().toLowerCase();

            executionHistoryService.markAsCompleted(
                executionHistory.getId(), 
                outputFilePath, 
                (int) executionTime
            );

            log.info("Report executed successfully: {} in {} ms", reportId, executionTime);
            return reportData;

        } catch (Exception e) {
            log.error("Failed to execute report {}: {}", reportId, e.getMessage(), e);
            
            long executionTime = System.currentTimeMillis();
            executionHistoryService.markAsFailed(
                executionHistory.getId(), 
                e.getMessage(), 
                (int) executionTime
            );

            throw new RuntimeException("Failed to execute report: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void scheduleReport(Long reportId, String cronExpression) {
        log.info("Scheduling report: {} with cron: {}", reportId, cronExpression);

        SavedReport report = findReportById(reportId);
        
        if (!"SCHEDULED".equals(report.getReportType())) {
            throw new RuntimeException("Only SCHEDULED type reports can be scheduled");
        }

        // TODO: Implement actual scheduling logic using Spring's @Scheduled or Quartz
        // This would typically involve creating a scheduled task with the given cron expression

        log.info("Report scheduled successfully: {}", reportId);
    }

    @Override
    public PageResponse<SavedReportResponse> searchReports(String keyword, Pageable pageable) {
        Page<SavedReport> reportPage = reportRepository.findByReportNameContainingIgnoreCase(keyword, pageable);
        return createPageResponse(reportPage);
    }

    @Override
    public boolean canDelete(Long id) {
        // Check if report has execution history
        // In a real implementation, you would check the execution history table
        return true;
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private byte[] generateReportData(SavedReport report, Map<String, Object> parameters) {
        // TODO: Implement actual report generation based on:
        // - report.getQueryTemplate() - SQL query or template
        // - report.getOutputFormat() - PDF, EXCEL, CSV, HTML
        // - parameters - dynamic report parameters
        
        // This is a placeholder implementation
        String sampleData = "Report: " + report.getReportName() + "\nGenerated at: " + 
                           java.time.LocalDateTime.now();
        return sampleData.getBytes();
    }

    private void validateUniqueReportCode(String reportCode, Long excludeId) {
        boolean exists;
        if (excludeId != null) {
            exists = reportRepository.existsByReportCodeAndIdNot(reportCode, excludeId);
        } else {
            exists = reportRepository.existsByReportCode(reportCode);
        }

        if (exists) {
            throw new DuplicateResourceException("Report with code '" + reportCode + "' already exists");
        }
    }

    private SavedReport findReportById(Long id) {
        return reportRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Saved Report not found: " + id));
    }

    private PageResponse<SavedReportResponse> createPageResponse(Page<SavedReport> reportPage) {
        List<SavedReportResponse> content = reportPage.getContent().stream()
            .map(reportMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<SavedReportResponse>builder()
            .content(content)
            .pageNumber(reportPage.getNumber())
            .pageSize(reportPage.getSize())
            .totalElements(reportPage.getTotalElements())
            .totalPages(reportPage.getTotalPages())
            .last(reportPage.isLast())
            .first(reportPage.isFirst())
            .empty(reportPage.isEmpty())
            .build();
    }
}
