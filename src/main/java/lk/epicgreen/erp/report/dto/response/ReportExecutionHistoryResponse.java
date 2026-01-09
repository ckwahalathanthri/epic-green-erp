package lk.epicgreen.erp.report.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for Report Execution History response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportExecutionHistoryResponse {

    private Long id;
    private Long reportId;
    private String reportCode;
    private String reportName;
    private Long executedBy;
    private String executedByName;
    private Map<String, Object> parametersUsed;
    private Integer executionTimeMs;
    private String outputFormat;
    private String outputFilePath;
    private String status;
    private String errorMessage;
    private LocalDateTime executedAt;
}
