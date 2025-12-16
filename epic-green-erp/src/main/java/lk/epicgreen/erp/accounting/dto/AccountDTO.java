package lk.epicgreen.erp.accounting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Account DTO
 * Data transfer object for ChartOfAccounts entity
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
public class AccountDTO {
    
    private Long id;
    
    private String accountCode;
    
    private String accountName;
    
    /**
     * Account classification
     */
    private String accountType;
    
    private String accountCategory;
    
    private String accountSubcategory;
    
    /**
     * Hierarchy
     */
    private Long parentAccountId;
    
    private String parentAccountCode;
    
    private String parentAccountName;
    
    private Integer accountLevel;
    
    private String fullPath;
    
    /**
     * Properties
     */
    private Boolean isHeader;
    
    private Boolean isActive;
    
    private Boolean isSystem;
    
    private Boolean allowManualEntries;
    
    /**
     * Currency
     */
    private String currency;
    
    /**
     * Balances
     */
    private BigDecimal openingBalanceDebit;
    
    private BigDecimal openingBalanceCredit;
    
    private BigDecimal openingBalance;
    
    private BigDecimal currentBalanceDebit;
    
    private BigDecimal currentBalanceCredit;
    
    private BigDecimal balance;
    
    /**
     * Description
     */
    private String description;
    
    private String notes;
    
    /**
     * Computed properties
     */
    private String normalBalanceSide;
    
    private Boolean isDebitAccount;
    
    private Boolean isCreditAccount;
    
    private Boolean canAcceptEntries;
    
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
