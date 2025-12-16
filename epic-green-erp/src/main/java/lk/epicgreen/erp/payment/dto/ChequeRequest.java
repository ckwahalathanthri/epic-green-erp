package lk.epicgreen.erp.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Cheque Request DTO
 * DTO for cheque operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChequeRequest {
    
    @NotBlank(message = "Cheque number is required")
    private String chequeNumber;
    
    @NotNull(message = "Cheque date is required")
    private LocalDate chequeDate;
    
    @NotNull(message = "Amount is required")
    private Double amount;
    
    @NotBlank(message = "Bank name is required")
    private String bankName;
    
    private String bankBranch;
    
    private String accountNumber;
    
    @NotBlank(message = "Payee name is required")
    private String payeeName;
    
    private String payerName;
    
    @NotBlank(message = "Cheque type is required")
    private String chequeType; // RECEIVED, ISSUED
    
    private String referenceType; // PAYMENT, RECEIPT, PURCHASE, SALE
    
    private Long referenceId;
    
    private String referenceNumber;
    
    private Long customerId;
    
    private Long supplierId;
    
    private LocalDate depositDate;
    
    private LocalDate clearanceDate;
    
    private String notes;
    
    private Long createdByUserId;
}
