package lk.epicgreen.erp.accounting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for Financial Period response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialPeriodResponse {

    private Long id;
    private String periodCode;
    private String periodName;
    private String periodType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer fiscalYear;
    private Boolean isClosed;
    private Long closedBy;
    private String closedByName;
    private LocalDateTime closedAt;
    private LocalDateTime createdAt;
    private Long createdBy;
    private String createdByName;
}
