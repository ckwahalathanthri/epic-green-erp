package lk.epicgreen.erp.accounting.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO for creating/updating Chart of Accounts
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartOfAccountsRequest {

    @NotBlank(message = "Account code is required")
    @Size(max = 20, message = "Account code must not exceed 20 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Account code must contain only uppercase letters, numbers, hyphens and underscores")
    private String accountCode;

    @NotBlank(message = "Account name is required")
    @Size(max = 200, message = "Account name must not exceed 200 characters")
    private String accountName;

    @NotBlank(message = "Account type is required")
    @Pattern(regexp = "^(ASSET|LIABILITY|EQUITY|REVENUE|EXPENSE)$", 
             message = "Account type must be one of: ASSET, LIABILITY, EQUITY, REVENUE, EXPENSE")
    private String accountType;

    @NotBlank(message = "Account category is required")
    @Pattern(regexp = "^(CURRENT_ASSET|FIXED_ASSET|CURRENT_LIABILITY|LONG_TERM_LIABILITY|CAPITAL|DIRECT_INCOME|INDIRECT_INCOME|DIRECT_EXPENSE|INDIRECT_EXPENSE|OTHER)$",
             message = "Account category must be valid")
    private String accountCategory;

    private Long parentAccountId;

    private Boolean isGroupAccount;

    private Boolean isControlAccount;

    @DecimalMin(value = "0.0", message = "Opening balance must be >= 0")
    private BigDecimal openingBalance;

    @Pattern(regexp = "^(DEBIT|CREDIT)$", 
             message = "Opening balance type must be either DEBIT or CREDIT")
    private String openingBalanceType;

    @DecimalMin(value = "0.0", message = "Current balance must be >= 0")
    private BigDecimal currentBalance;

    private Boolean isActive;
}
