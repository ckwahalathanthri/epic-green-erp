package lk.epicgreen.erp.accounting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Journal Entry DTO
 * Data transfer object for JournalEntry entity
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
public class JournalEntryDTO {
    
    private Long id;
    
    private String entryNumber;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate entryDate;
    
    /**
     * Financial period
     */
    private Long financialPeriodId;
    
    private String periodCode;
    
    private String periodName;
    
    /**
     * Entry details
     */
    private String entryType;
    
    private String referenceNumber;
    
    private Long referenceId;
    
    private String referenceType;
    
    /**
     * Currency
     */
    private String currency;
    
    private BigDecimal exchangeRate;
    
    /**
     * Totals
     */
    private BigDecimal totalDebit;
    
    private BigDecimal totalCredit;
    
    private BigDecimal difference;
    
    /**
     * Status
     */
    private String status;
    
    /**
     * Posting
     */
    private Boolean isPosted;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate postedDate;
    
    private String postedBy;
    
    /**
     * Approval
     */
    private String approvedBy;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate approvalDate;
    
    /**
     * Description
     */
    private String description;
    
    private String notes;
    
    private Integer totalLines;
    
    /**
     * Journal entry lines
     */
    private List<JournalEntryLineDTO> lines;
    
    /**
     * Computed properties
     */
    private Boolean isBalanced;
    
    private Boolean canPost;
    
    private Boolean canEdit;
    
    private Boolean requiresApproval;
    
    /**
     * Audit fields
     */
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime createdAt;
    
    private String createdBy;
    
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime updatedAt;
    
    private String updatedBy;
    
    /**
     * Journal Entry Line DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class JournalEntryLineDTO {
        
        private Long id;
        
        private Integer lineNumber;
        
        /**
         * Account information
         */
        private Long accountId;
        
        private String accountCode;
        
        private String accountName;
        
        /**
         * Amounts
         */
        private BigDecimal debitAmount;
        
        private BigDecimal creditAmount;
        
        private BigDecimal netAmount;
        
        /**
         * Currency
         */
        private String currency;
        
        private BigDecimal exchangeRate;
        
        private BigDecimal baseDebitAmount;
        
        private BigDecimal baseCreditAmount;
        
        /**
         * Description
         */
        private String description;
        
        private String notes;
        
        /**
         * Computed properties
         */
        private Boolean isDebitEntry;
        
        private Boolean isCreditEntry;
    }
}
