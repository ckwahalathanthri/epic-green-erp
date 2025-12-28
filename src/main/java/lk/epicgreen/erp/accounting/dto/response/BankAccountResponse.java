package lk.epicgreen.erp.accounting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for Bank Account response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountResponse {

    private Long id;
    private String accountNumber;
    private String accountName;
    private String bankName;
    private String bankBranch;
    private String accountType;
    private String currencyCode;
    private Long glAccountId;
    private String glAccountCode;
    private String glAccountName;
    private BigDecimal openingBalance;
    private BigDecimal currentBalance;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private Long createdBy;
    private String createdByName;
    private LocalDateTime updatedAt;
    private Long updatedBy;
}
