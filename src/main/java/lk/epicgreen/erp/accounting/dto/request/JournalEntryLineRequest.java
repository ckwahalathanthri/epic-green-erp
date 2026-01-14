package lk.epicgreen.erp.accounting.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * DTO for Journal Entry Line
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntryLineRequest {

    @NotNull(message = "Line number is required")
    @Min(value = 1, message = "Line number must be >= 1")
    private Integer lineNumber;

    @NotNull(message = "Account ID is required")
    private Long accountId;

    @DecimalMin(value = "0.0", message = "Debit amount must be >= 0")
    private BigDecimal debitAmount;

    @DecimalMin(value = "0.0", message = "Credit amount must be >= 0")
    private BigDecimal creditAmount;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Size(max = 50, message = "Cost center must not exceed 50 characters")
    private String costCenter;

    @Size(max = 50, message = "Dimension1 must not exceed 50 characters")
    private String dimension1;

    @Size(max = 50, message = "Dimension2 must not exceed 50 characters")
    private String dimension2;
}
