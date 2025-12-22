package lk.epicgreen.erp.supplier.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * SupplierLedger Request DTO
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierLedgerRequest {
    
    @NotNull(message = "Supplier ID is required")
    private Long supplierId;
    
    private String supplierName;
    
    @NotNull(message = "Transaction date is required")
    private LocalDate transactionDate;
    
    @NotBlank(message = "Transaction type is required")
    private String transactionType; // PURCHASE, PAYMENT, CREDIT_NOTE, DEBIT_NOTE, OPENING_BALANCE
    
    private String referenceType; // PURCHASE_ORDER, PAYMENT, ADJUSTMENT
    
    private Long referenceId;
    
    private String referenceNumber;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    @DecimalMin(value = "0.0", message = "Debit amount must be positive")
    private BigDecimal debitAmount;
    
    @DecimalMin(value = "0.0", message = "Credit amount must be positive")
    private BigDecimal creditAmount;
    
    // Payment details
    private String paymentType; // CASH, CHEQUE, BANK_TRANSFER, CREDIT
    
    @Size(max = 50, message = "Cheque number cannot exceed 50 characters")
    private String chequeNo;
    
    private LocalDate chequeDate;
    
    @Size(max = 200, message = "Bank name cannot exceed 200 characters")
    private String bank;
    
    @Size(max = 100, message = "Bank account cannot exceed 100 characters")
    private String bankAccount;
    
    @Size(max = 200, message = "Transaction reference cannot exceed 200 characters")
    private String transactionReference;
    
    private Long recordedByUserId;
    
    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;
}
