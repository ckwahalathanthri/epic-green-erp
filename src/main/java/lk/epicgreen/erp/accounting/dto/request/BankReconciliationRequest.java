package lk.epicgreen.erp.accounting.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for creating/updating Bank Reconciliation
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankReconciliationRequest {

    @NotBlank(message = "Reconciliation number is required")
    @Size(max = 30, message = "Reconciliation number must not exceed 30 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Reconciliation number must contain only uppercase letters, numbers, hyphens and underscores")
    private String reconciliationNumber;

    @NotNull(message = "Bank account ID is required")
    private Long bankAccountId;

    @NotNull(message = "Statement date is required")
    private LocalDate statementDate;

    @NotNull(message = "Statement balance is required")
    private BigDecimal statementBalance;

    @NotNull(message = "Book balance is required")
    private BigDecimal bookBalance;

    private BigDecimal reconciledBalance;

    @Pattern(regexp = "^(DRAFT|IN_PROGRESS|COMPLETED)$", 
             message = "Status must be one of: DRAFT, IN_PROGRESS, COMPLETED")
    private String status;

    @Size(max = 1000, message = "Remarks must not exceed 1000 characters")
    private String remarks;
}
