package lk.epicgreen.erp.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * CustomerLedger Request DTO
 * DTO for customer ledger operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLedgerRequest {
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    private String customerName;
    
    @NotNull(message = "Transaction type is required")
    private String transactionType;
    
    private String entryType; // Alias for transactionType
    
    @NotNull(message = "Transaction date is required")
    private LocalDate transactionDate;
    
    private String referenceNumber;
    
    private String referenceType;
    
    private Long referenceId;
    
    private Long invoiceId;
    
    private Long paymentId;
    
    private Long salesOrderId;
    
    private LocalDate dueDate;
    
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
