package lk.epicgreen.erp.accounting.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO for creating/updating Bank Account
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountRequest {

    @NotBlank(message = "Account number is required")
    @Size(max = 50, message = "Account number must not exceed 50 characters")
    private String accountNumber;

    @NotBlank(message = "Account name is required")
    @Size(max = 200, message = "Account name must not exceed 200 characters")
    private String accountName;

    @NotBlank(message = "Bank name is required")
    @Size(max = 100, message = "Bank name must not exceed 100 characters")
    private String bankName;

    @Size(max = 100, message = "Bank branch must not exceed 100 characters")
    private String bankBranch;

    @NotBlank(message = "Account type is required")
    @Pattern(regexp = "^(CURRENT|SAVINGS|OVERDRAFT|CASH)$", 
             message = "Account type must be one of: CURRENT, SAVINGS, OVERDRAFT, CASH")
    private String accountType;

    @Size(max = 3, message = "Currency code must not exceed 3 characters")
    private String currencyCode;

    @NotNull(message = "GL account ID is required")
    private Long glAccountId;

    @DecimalMin(value = "0.0", message = "Opening balance must be >= 0")
    private BigDecimal openingBalance;

    @DecimalMin(value = "0.0", message = "Current balance must be >= 0")
    private BigDecimal currentBalance;

    private Boolean isActive;
}
