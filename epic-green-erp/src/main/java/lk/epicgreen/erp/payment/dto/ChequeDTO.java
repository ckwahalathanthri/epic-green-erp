package lk.epicgreen.erp.payment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Cheque DTO
 * Data transfer object for Cheque entity
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
public class ChequeDTO {
    
    private Long id;
    
    private String chequeNumber;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate chequeDate;
    
    /**
     * Customer information
     */
    private Long customerId;
    
    private String customerCode;
    
    private String customerName;
    
    /**
     * Bank details
     */
    private String bankName;
    
    private String bankBranch;
    
    private String bankAccount;
    
    /**
     * Amount
     */
    private BigDecimal chequeAmount;
    
    private String currency;
    
    private BigDecimal exchangeRate;
    
    /**
     * Cheque details
     */
    private String payeeName;
    
    private String chequeType;
    
    /**
     * Status
     */
    private String status;
    
    /**
     * Receipt details
     */
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate receivedDate;
    
    private String receivedBy;
    
    /**
     * Deposit details
     */
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate depositDate;
    
    private String depositedBy;
    
    private String depositBank;
    
    private String depositAccount;
    
    private String depositReference;
    
    /**
     * Clearing details
     */
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate clearedDate;
    
    /**
     * Bounce details
     */
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate bouncedDate;
    
    private String bounceReason;
    
    private BigDecimal bankCharges;
    
    /**
     * Post-dated cheque
     */
    private Boolean isPostDated;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate maturityDate;
    
    private String notes;
    
    /**
     * Computed properties
     */
    private Boolean isCleared;
    
    private Boolean isBounced;
    
    private Boolean isDeposited;
    
    private Boolean canDeposit;
    
    private Boolean isPendingMaturity;
    
    private Long daysToMaturity;
    
    private Long daysSinceDeposit;
    
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
