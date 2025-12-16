package lk.epicgreen.erp.returns.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Credit Note DTO
 * Data transfer object for CreditNote entity
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
public class CreditNoteDTO {
    
    private Long id;
    
    private String creditNoteNumber;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate creditNoteDate;
    
    /**
     * Customer information
     */
    private Long customerId;
    
    private String customerCode;
    
    private String customerName;
    
    /**
     * References
     */
    private Long invoiceId;
    
    private String invoiceNumber;
    
    private Long salesReturnId;
    
    private String returnNumber;
    
    /**
     * Credit note details
     */
    private String creditNoteType;
    
    private String reason;
    
    /**
     * Financial
     */
    private String currency;
    
    private BigDecimal exchangeRate;
    
    private BigDecimal creditAmount;
    
    private BigDecimal appliedAmount;
    
    private BigDecimal unappliedAmount;
    
    /**
     * Status
     */
    private String status;
    
    /**
     * Approval
     */
    private String approvedBy;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate approvalDate;
    
    /**
     * Posting
     */
    private Boolean isPosted;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate postedDate;
    
    private String postedBy;
    
    /**
     * Terms and notes
     */
    private String termsAndConditions;
    
    private String notes;
    
    private String internalNotes;
    
    /**
     * Computed properties
     */
    private Boolean isFullyApplied;
    
    private Boolean hasUnappliedAmount;
    
    private BigDecimal applicationPercentage;
    
    private Boolean canPost;
    
    private Boolean canEdit;
    
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
