package lk.epicgreen.erp.supplier.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * SupplierLedger Request DTO
 * DTO for supplier ledger operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierLedgerRequest {
    
    @NotNull(message = "Supplier ID is required")
    private Long supplierId;
    
    @NotNull(message = "Transaction type is required")
    private String transactionType;
    
    @NotNull(message = "Transaction date is required")
    private LocalDate transactionDate;
    
    private String referenceNumber;
    
    private String referenceType;
    
    private Long referenceId;
    
    @NotNull(message = "Debit amount is required")
    private Double debitAmount;
    
    @NotNull(message = "Credit amount is required")
    private Double creditAmount;
    
    private String paymentMethod;
    
    private String chequeNumber;
    
    private LocalDate chequeDate;
    
    private String bankName;
    
    private String description;
    
    private String notes;
    
    private Long recordedByUserId;
}
