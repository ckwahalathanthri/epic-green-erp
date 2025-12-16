package lk.epicgreen.erp.supplier.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * SupplierLedger DTO
 * Data transfer object for SupplierLedger entity
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
public class SupplierLedgerDTO {
    
    private Long id;
    
    private Long supplierId;
    
    private String supplierCode;
    
    private String supplierName;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate transactionDate;
    
    private String transactionType;
    
    private String referenceNumber;
    
    private Long referenceId;
    
    private String referenceType;
    
    private String description;
    
    private BigDecimal debitAmount;
    
    private BigDecimal creditAmount;
    
    private BigDecimal balance;
    
    private String currency;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate dueDate;
    
    private String paymentStatus;
    
    private Boolean isReconciled;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate reconciledDate;
    
    private String reconciledBy;
    
    private String notes;
    
    /**
     * Computed properties
     */
    private BigDecimal transactionAmount;
    
    private Boolean isOverdue;
    
    private Long daysOverdue;
    
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
