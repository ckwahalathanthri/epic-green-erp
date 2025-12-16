package lk.epicgreen.erp.accounting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * General Ledger DTO
 * Data transfer object for GeneralLedger entity
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralLedgerDTO {
    
    private Long id;
    
    /**
     * Account information
     */
    private Long accountId;
    
    private String accountCode;
    
    private String accountName;
    
    private String accountType;
    
    /**
     * Financial period
     */
    private Long financialPeriodId;
    
    private String periodCode;
    
    private String periodName;
    
    /**
     * Journal entry reference
     */
    private Long journalEntryId;
    
    private String entryNumber;
    
    private Long journalEntryLineId;
    
    /**
     * Transaction details
     */
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate transactionDate;
    
    private String transactionType;
    
    private String referenceNumber;
    
    private Long referenceId;
    
    private String referenceType;
    
    /**
     * Amounts
     */
    private BigDecimal debitAmount;
    
    private BigDecimal creditAmount;
    
    private BigDecimal balance;
    
    private BigDecimal netAmount;
    
    /**
     * Currency
     */
    private String currency;
    
    private BigDecimal exchangeRate;
    
    private BigDecimal baseDebitAmount;
    
    private BigDecimal baseCreditAmount;
    
    private BigDecimal baseBalance;
    
    /**
     * Description
     */
    private String description;
    
    private String notes;
    
    /**
     * Reconciliation
     */
    private Boolean isReconciled;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate reconciliationDate;
    
    /**
     * Computed properties
     */
    private Boolean isDebitEntry;
    
    private Boolean isCreditEntry;
    
    /**
     * Audit fields
     */
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime createdAt;
    
    private String createdBy;
    
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime updatedAt;
    
    private String updatedBy;
}
