package lk.epicgreen.erp.report.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.util.Map;

/**
 * DTO for creating Report Execution History
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportExecutionHistoryRequest {

    @NotNull(message = "Report ID is required")
    private Long reportId;

    @NotNull(message = "Executed by user ID is required")
    private Long executedBy;

    private Map<String, Object> parametersUsed;

    @Min(value = 0, message = "Execution time must be >= 0")
    private Integer executionTimeMs;

    @Size(max = 20, message = "Output format must not exceed 20 characters")
    private String outputFormat;

    @Size(max = 500, message = "Output file path must not exceed 500 characters")
    private String outputFilePath;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(RUNNING|COMPLETED|FAILED)$", 
             message = "Status must be one of: RUNNING, COMPLETED, FAILED")
    private String status;

    @Size(max = 5000, message = "Error message must not exceed 5000 characters")
    private String errorMessage;
}
