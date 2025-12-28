package lk.epicgreen.erp.reports.mapper;

import lk.epicgreen.erp.reports.dto.request.ReportExecutionHistoryRequest;
import lk.epicgreen.erp.reports.dto.response.ReportExecutionHistoryResponse;
import lk.epicgreen.erp.reports.entity.ReportExecutionHistory;
import org.springframework.stereotype.Component;

/**
 * Mapper for ReportExecutionHistory entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class ReportExecutionHistoryMapper {

    public ReportExecutionHistory toEntity(ReportExecutionHistoryRequest request) {
        if (request == null) {
            return null;
        }

        return ReportExecutionHistory.builder()
            .parametersUsed(request.getParametersUsed())
            .executionTimeMs(request.getExecutionTimeMs())
            .outputFormat(request.getOutputFormat())
            .outputFilePath(request.getOutputFilePath())
            .status(request.getStatus())
            .errorMessage(request.getErrorMessage())
            .build();
    }

    public void updateEntityFromRequest(ReportExecutionHistoryRequest request, ReportExecutionHistory history) {
        if (request == null || history == null) {
            return;
        }

        history.setParametersUsed(request.getParametersUsed());
        history.setExecutionTimeMs(request.getExecutionTimeMs());
        history.setOutputFormat(request.getOutputFormat());
        history.setOutputFilePath(request.getOutputFilePath());
        history.setStatus(request.getStatus());
        history.setErrorMessage(request.getErrorMessage());
    }

    public ReportExecutionHistoryResponse toResponse(ReportExecutionHistory history) {
        if (history == null) {
            return null;
        }

        return ReportExecutionHistoryResponse.builder()
            .id(history.getId())
            .reportId(history.getReport() != null ? history.getReport().getId() : null)
            .reportCode(history.getReport() != null ? history.getReport().getReportCode() : null)
            .reportName(history.getReport() != null ? history.getReport().getReportName() : null)
            .executedBy(history.getExecutedBy())
            .parametersUsed(history.getParametersUsed())
            .executionTimeMs(history.getExecutionTimeMs())
            .outputFormat(history.getOutputFormat())
            .outputFilePath(history.getOutputFilePath())
            .status(history.getStatus())
            .errorMessage(history.getErrorMessage())
            .executedAt(history.getExecutedAt())
            .build();
    }
}
