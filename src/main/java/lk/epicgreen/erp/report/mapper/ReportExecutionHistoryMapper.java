package lk.epicgreen.erp.report.mapper;

import lk.epicgreen.erp.report.dto.request.ReportExecutionHistoryRequest;
import lk.epicgreen.erp.report.dto.response.ReportExecutionHistoryResponse;
import lk.epicgreen.erp.report.entity.ReportExecutionHistory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Collections;

/**
 * Mapper for ReportExecutionHistory entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ReportExecutionHistoryMapper {

    private final ObjectMapper objectMapper;

    public ReportExecutionHistory toEntity(ReportExecutionHistoryRequest request) {
        if (request == null) {
            return null;
        }

        String parametersJson = null;
        if (request.getParametersUsed() != null) {
            try {
                parametersJson = objectMapper.writeValueAsString(request.getParametersUsed());
            } catch (JsonProcessingException e) {
                log.error("Error converting parameters to JSON", e);
            }
        }

        return ReportExecutionHistory.builder()
            .parametersUsed(parametersJson)
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

        if (request.getParametersUsed() != null) {
            try {
                history.setParametersUsed(objectMapper.writeValueAsString(request.getParametersUsed()));
            } catch (JsonProcessingException e) {
                log.error("Error converting parameters to JSON", e);
            }
        }
        
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

        Map<String, Object> parametersMap = Collections.emptyMap();
        if (history.getParametersUsed() != null) {
            try {
                parametersMap = objectMapper.readValue(history.getParametersUsed(), new TypeReference<Map<String, Object>>(){});
            } catch (JsonProcessingException e) {
                log.error("Error converting JSON parameters to Map", e);
            }
        }

        return ReportExecutionHistoryResponse.builder()
            .id(history.getId())
            .reportId(history.getReport() != null ? history.getReport().getId() : null)
            .reportCode(history.getReport() != null ? history.getReport().getReportCode() : null)
            .reportName(history.getReport() != null ? history.getReport().getReportName() : null)
            .executedBy(history.getExecutedBy() != null ? history.getExecutedBy().getId() : null)
            .parametersUsed(parametersMap)
            .executionTimeMs(history.getExecutionTimeMs())
            .outputFormat(history.getOutputFormat())
            .outputFilePath(history.getOutputFilePath())
            .status(history.getStatus())
            .errorMessage(history.getErrorMessage())
            .executedAt(history.getExecutedAt())
            .build();
    }
}
