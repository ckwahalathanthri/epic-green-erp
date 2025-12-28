package lk.epicgreen.erp.accounting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for Bank Reconciliation response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankReconciliationResponse {

    private Long id;
    private String reconciliationNumber;
    private Long bankAccountId;
    private String accountNumber;
    private String accountName;
    private String bankName;
    private LocalDate statementDate;
    private BigDecimal statementBalance;
    private BigDecimal bookBalance;
    private BigDecimal reconciledBalance;
    private BigDecimal difference;
    private String status;
    private Long reconciledBy;
    private String reconciledByName;
    private LocalDateTime reconciledAt;
    private String remarks;
    private LocalDateTime createdAt;
    private Long createdBy;
    private String createdByName;
}
