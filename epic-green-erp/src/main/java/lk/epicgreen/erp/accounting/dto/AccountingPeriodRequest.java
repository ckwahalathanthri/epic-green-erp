package lk.epicgreen.erp.accounting.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * AccountingPeriod Request DTO
 * DTO for accounting period operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountingPeriodRequest {
    
    @NotBlank(message = "Period code is required")
    private String periodCode;
    
    @NotBlank(message = "Period name is required")
    private String periodName;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    
    @NotNull(message = "Fiscal year is required")
    private Integer fiscalYear;
    
    private String periodType;
    
    private Integer periodNumber;
    
    private String description;
}
