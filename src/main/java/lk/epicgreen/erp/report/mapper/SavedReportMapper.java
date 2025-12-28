package lk.epicgreen.erp.reports.mapper;

import lk.epicgreen.erp.reports.dto.request.SavedReportRequest;
import lk.epicgreen.erp.reports.dto.response.SavedReportResponse;
import lk.epicgreen.erp.reports.entity.SavedReport;
import org.springframework.stereotype.Component;

/**
 * Mapper for SavedReport entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class SavedReportMapper {

    public SavedReport toEntity(SavedReportRequest request) {
        if (request == null) {
            return null;
        }

        return SavedReport.builder()
            .reportCode(request.getReportCode())
            .reportName(request.getReportName())
            .reportCategory(request.getReportCategory())
            .reportType(request.getReportType())
            .queryTemplate(request.getQueryTemplate())
            .parameters(request.getParameters())
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
        report.setParameters(request.getParameters());
        report.setOutputFormat(request.getOutputFormat());
        report.setIsPublic(request.getIsPublic());
    }

    public SavedReportResponse toResponse(SavedReport report) {
        if (report == null) {
            return null;
        }

        return SavedReportResponse.builder()
            .id(report.getId())
            .reportCode(report.getReportCode())
            .reportName(report.getReportName())
            .reportCategory(report.getReportCategory())
            .reportType(report.getReportType())
            .queryTemplate(report.getQueryTemplate())
            .parameters(report.getParameters())
            .outputFormat(report.getOutputFormat())
            .isPublic(report.getIsPublic())
            .createdBy(report.getCreatedBy())
            .createdAt(report.getCreatedAt())
            .updatedAt(report.getUpdatedAt())
            .build();
    }
}
