package lk.epicgreen.erp.report.mapper;

import lk.epicgreen.erp.report.dto.request.SavedReportRequest;
import lk.epicgreen.erp.report.dto.response.SavedReportResponse;
import lk.epicgreen.erp.report.entity.SavedReport;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/**
 * Mapper for SavedReport entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class SavedReportMapper {

    @Autowired
    private ObjectMapper objectMapper;

    public SavedReport toEntity(SavedReportRequest request) {
        if (request == null) {
            return null;
        }

        String parametersJson = "{}";
        if (request.getParameters() != null) {
            try {
                parametersJson = objectMapper.writeValueAsString(request.getParameters());
            } catch (JsonProcessingException e) {
                // Handle error or log it, defaulting to empty JSON object
                parametersJson = "{}";
            }
        }

        return SavedReport.builder()
            .reportCode(request.getReportCode())
            .reportName(request.getReportName())
            .reportCategory(request.getReportCategory())
            .reportType(request.getReportType())
            .queryTemplate(request.getQueryTemplate())
            .parameters(parametersJson)
            .outputFormat(request.getOutputFormat() != null ? request.getOutputFormat() : "PDF")
            .isPublic(request.getIsPublic() != null ? request.getIsPublic() : false)
            .build();
    }

    public void updateEntityFromRequest(SavedReportRequest request, SavedReport report) {
        if (request == null || report == null) {
            return;
        }

        report.setReportCode(request.getReportCode());
        report.setReportName(request.getReportName());
        report.setReportCategory(request.getReportCategory());
        report.setReportType(request.getReportType());
        report.setQueryTemplate(request.getQueryTemplate());
        
        if (request.getParameters() != null) {
            try {
                 report.setParameters(objectMapper.writeValueAsString(request.getParameters()));
            } catch (JsonProcessingException e) {
                 // Log error
            }
        }
        
        report.setOutputFormat(request.getOutputFormat());
        report.setIsPublic(request.getIsPublic());
    }

    public SavedReportResponse toResponse(SavedReport report) {
        if (report == null) {
            return null;
        }

        Map<String, Object> parametersMap = new HashMap<>();
        if (report.getParameters() != null) {
            try {
                parametersMap = objectMapper.readValue(report.getParameters(), Map.class);
            } catch (IOException e) {
                // Log error
            }
        }

        return SavedReportResponse.builder()
            .id(report.getId())
            .reportCode(report.getReportCode())
            .reportName(report.getReportName())
            .reportCategory(report.getReportCategory())
            .reportType(report.getReportType())
            .queryTemplate(report.getQueryTemplate())
            .parameters(parametersMap)
            .outputFormat(report.getOutputFormat())
            .isPublic(report.getIsPublic())
            .createdBy(report.getCreatedBy() != null ? report.getCreatedBy().getId() : null)
            .createdByName(report.getCreatedBy() != null ? report.getCreatedBy().getUsername() : null)
            .createdAt(report.getCreatedAt())
            .updatedAt(report.getUpdatedAt())
            .build();
    }
}
