package lk.epicgreen.erp.payment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Payment Allocation DTO
 * Data transfer object for PaymentAllocation entity
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
public class PaymentAllocationDTO {
    
    private Long id;
    
    /**
     * Payment information
     */
    private Long paymentId;
    
    private String paymentNumber;
    
    /**
     * Invoice information
     */
    private Long invoiceId;
    
    private String invoiceNumber;
    
    /**
     * Allocation amounts
     */
    private BigDecimal invoiceBalanceBefore;
    
    private BigDecimal allocatedAmount;
    
    private BigDecimal invoiceBalanceAfter;
    
    /**
     * Currency
     */
    private String currency;
    
    private BigDecimal exchangeRate;
    
    private String notes;
    
    /**
     * Computed properties
     */
    private Boolean isInvoiceFullyPaid;
    
    private BigDecimal allocationPercentage;
    
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
