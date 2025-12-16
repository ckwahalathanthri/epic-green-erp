package lk.epicgreen.erp.payment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Payment DTO
 * Data transfer object for Payment entity
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
public class PaymentDTO {
    
    private Long id;
    
    private String paymentNumber;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate paymentDate;
    
    /**
     * Customer information
     */
    private Long customerId;
    
    private String customerCode;
    
    private String customerName;
    
    /**
     * Payment details
     */
    private String paymentMethod;
    
    private BigDecimal paymentAmount;
    
    private BigDecimal allocatedAmount;
    
    private BigDecimal unallocatedAmount;
    
    /**
     * Currency
     */
    private String currency;
    
    private BigDecimal exchangeRate;
    
    /**
     * Bank details
     */
    private String referenceNumber;
    
    private String bankName;
    
    private String bankAccount;
    
    private String bankBranch;
    
    /**
     * Cheque details (if payment method is CHEQUE)
     */
    private Long chequeId;
    
    private ChequeDTO cheque;
    
    /**
     * Status
     */
    private String status;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate clearedDate;
    
    /**
     * Posting
     */
    private Boolean isPosted;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate postedDate;
    
    private String postedBy;
    
    /**
     * Receipt details
     */
    private String receivedBy;
    
    private String receiptNumber;
    
    /**
     * Description and notes
     */
    private String description;
    
    private String notes;
    
    /**
     * Allocations
     */
    private List<PaymentAllocationDTO> allocations;
    
    private Integer totalAllocations;
    
    /**
     * Computed properties
     */
    private Boolean isFullyAllocated;
    
    private Boolean hasUnallocatedAmount;
    
    private Boolean isCleared;
    
    private Boolean isBounced;
    
    private Boolean canPost;
    
    private Boolean canEdit;
    
    private Boolean isChequePayment;
    
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
