package lk.epicgreen.erp.customer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for creating customer ledger entry (IMMUTABLE)
 * Note: Customer ledger is an immutable financial record
 * No update or delete operations allowed
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLedgerRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Transaction date is required")
    private LocalDate transactionDate;

    @NotBlank(message = "Transaction type is required")
    @Pattern(regexp = "^(SALE|PAYMENT|RETURN|CREDIT_NOTE|DEBIT_NOTE|ADJUSTMENT)$", 
             message = "Transaction type must be one of: SALE, PAYMENT, RETURN, CREDIT_NOTE, DEBIT_NOTE, ADJUSTMENT")
    private String transactionType;

    @Size(max = 50, message = "Reference type must not exceed 50 characters")
    private String referenceType;

    private Long referenceId;

    @Size(max = 50, message = "Reference number must not exceed 50 characters")
    private String referenceNumber;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Debit amount is required")
    @DecimalMin(value = "0.0", message = "Debit amount must be >= 0")
    private BigDecimal debitAmount;

    @NotNull(message = "Credit amount is required")
    @DecimalMin(value = "0.0", message = "Credit amount must be >= 0")
    private BigDecimal creditAmount;

    @AssertTrue(message = "Either debit or credit amount must be greater than zero, but not both")
    public boolean isValidAmounts() {
        if (debitAmount == null || creditAmount == null) {
            return true; // Let @NotNull handle null validation
        }
        
        boolean debitZero = debitAmount.compareTo(BigDecimal.ZERO) == 0;
        boolean creditZero = creditAmount.compareTo(BigDecimal.ZERO) == 0;
        
        // Either debit > 0 and credit = 0, OR debit = 0 and credit > 0
        return (debitAmount.compareTo(BigDecimal.ZERO) > 0 && creditZero) ||
               (creditAmount.compareTo(BigDecimal.ZERO) > 0 && debitZero);
    }
}
