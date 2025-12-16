package lk.epicgreen.erp.customer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Customer Ledger DTO
 * Data transfer object for CustomerLedger entity
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
public class CustomerLedgerDTO {
    
    private Long id;
    
    /**
     * Customer information
     */
    private Long customerId;
    
    private String customerCode;
    
    private String customerName;
    
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
    
    private String currency;
    
    /**
     * Due date and overdue
     */
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate dueDate;
    
    private Boolean isOverdue;
    
    private Long daysOverdue;
    
    /**
     * Payment details
     */
    private String paymentMethod;
    
    private String chequeNumber;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate chequeDate;
    
    private String bankName;
    
    private String transactionReference;
    
    /**
     * Description and status
     */
    private String description;
    
    private String status;
    
    private Boolean isReconciled;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate reconciliationDate;
    
    private String notes;
    
    /**
     * Computed properties
     */
    private Boolean isChequeBounced;
    
    private Boolean isSettled;
    
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
