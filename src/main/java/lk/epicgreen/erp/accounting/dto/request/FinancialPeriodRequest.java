package lk.epicgreen.erp.accounting.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * DTO for creating/updating Financial Period
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialPeriodRequest {

    @NotBlank(message = "Period code is required")
    @Size(max = 20, message = "Period code must not exceed 20 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Period code must contain only uppercase letters, numbers, hyphens and underscores")
    private String periodCode;

    @NotBlank(message = "Period name is required")
    @Size(max = 100, message = "Period name must not exceed 100 characters")
    private String periodName;

    @NotBlank(message = "Period type is required")
    @Pattern(regexp = "^(MONTH|QUARTER|YEAR)$", 
             message = "Period type must be one of: MONTH, QUARTER, YEAR")
    private String periodType;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Fiscal year is required")
    @Min(value = 2000, message = "Fiscal year must be >= 2000")
    @Max(value = 2100, message = "Fiscal year must be <= 2100")
    private Integer fiscalYear;

    private Boolean isClosed;
}
