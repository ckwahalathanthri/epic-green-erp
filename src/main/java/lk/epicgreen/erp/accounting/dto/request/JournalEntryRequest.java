package lk.epicgreen.erp.accounting.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for creating/updating Journal Entry
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntryRequest {

    @NotBlank(message = "Journal number is required")
    @Size(max = 30, message = "Journal number must not exceed 30 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Journal number must contain only uppercase letters, numbers, hyphens and underscores")
    private String journalNumber;

    @NotNull(message = "Journal date is required")
    private LocalDate journalDate;

    @NotNull(message = "Period ID is required")
    private Long periodId;

    @Pattern(regexp = "^(MANUAL|AUTOMATED|OPENING_BALANCE|CLOSING|ADJUSTMENT)$", 
             message = "Entry type must be one of: MANUAL, AUTOMATED, OPENING_BALANCE, CLOSING, ADJUSTMENT")
    private String entryType;

    @Size(max = 50, message = "Source type must not exceed 50 characters")
    private String sourceType;

    private Long sourceId;

    @Size(max = 50, message = "Source reference must not exceed 50 characters")
    private String sourceReference;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Total debit is required")
    @DecimalMin(value = "0.0", message = "Total debit must be >= 0")
    private BigDecimal totalDebit;

    @NotNull(message = "Total credit is required")
    @DecimalMin(value = "0.0", message = "Total credit must be >= 0")
    private BigDecimal totalCredit;

    @Pattern(regexp = "^(DRAFT|POSTED|CANCELLED)$", 
             message = "Status must be one of: DRAFT, POSTED, CANCELLED")
    private String status;

    @NotEmpty(message = "At least one journal entry line is required")
    @Valid
    private List<JournalEntryLineRequest> lines;
}
