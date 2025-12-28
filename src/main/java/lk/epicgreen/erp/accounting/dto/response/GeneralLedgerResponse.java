package lk.epicgreen.erp.accounting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for General Ledger response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralLedgerResponse {

    private Long id;
    private LocalDate transactionDate;
    private Long periodId;
    private String periodCode;
    private Long accountId;
    private String accountCode;
    private String accountName;
    private Long journalId;
    private String journalNumber;
    private Long journalLineId;
    private String description;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private BigDecimal balance;
    private String sourceType;
    private Long sourceId;
    private LocalDateTime createdAt;
}
