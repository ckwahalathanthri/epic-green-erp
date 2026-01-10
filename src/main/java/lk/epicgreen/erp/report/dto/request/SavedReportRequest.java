package lk.epicgreen.erp.report.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Map;

/**
 * DTO for creating/updating Saved Report
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedReportRequest {

    @NotBlank(message = "Report code is required")
    @Size(max = 50, message = "Report code must not exceed 50 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Report code must contain only uppercase letters, numbers, hyphens and underscores")
    private String reportCode;

    @NotBlank(message = "Report name is required")
    @Size(max = 200, message = "Report name must not exceed 200 characters")
    private String reportName;

    @NotBlank(message = "Report category is required")
    @Size(max = 50, message = "Report category must not exceed 50 characters")
    private String reportCategory;

    @NotBlank(message = "Report type is required")
    @Pattern(regexp = "^(STANDARD|CUSTOM|SCHEDULED)$", 
             message = "Report type must be one of: STANDARD, CUSTOM, SCHEDULED")
    private String reportType;

    @Size(max = 50000, message = "Query template must not exceed 50000 characters")
    private String queryTemplate;

    private Map<String, Object> parameters;

    @Pattern(regexp = "^(PDF|EXCEL|CSV|HTML)$", 
             message = "Output format must be one of: PDF, EXCEL, CSV, HTML")
    private String outputFormat;

    private Boolean isPublic;
}
