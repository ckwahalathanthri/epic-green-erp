package lk.epicgreen.erp.accounting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for Chart of Accounts response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartOfAccountsResponse {

    private Long id;
    private String accountCode;
    private String accountName;
    private String accountType;
    private String accountCategory;
    private Long parentAccountId;
    private String parentAccountCode;
    private String parentAccountName;
    private Boolean isGroupAccount;
    private Boolean isControlAccount;
    private BigDecimal openingBalance;
    private String openingBalanceType;
    private BigDecimal currentBalance;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private Long createdBy;
    private String createdByName;
    private LocalDateTime updatedAt;
    private Long updatedBy;
}
