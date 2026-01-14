package lk.epicgreen.erp.report.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for Saved Report response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedReportResponse {

    private Long id;
    private String reportCode;
    private String reportName;
    private String reportCategory;
    private String reportType;
    private String queryTemplate;
    private Map<String, Object> parameters;
    private String outputFormat;
    private Boolean isPublic;
    private Long createdBy;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String format;
}
