package lk.epicgreen.erp.payment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for creating/updating Cheque (PDC tracking)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChequeRequest {

    @NotNull(message = "Payment ID is required")
    private Long paymentId;

    @NotBlank(message = "Cheque number is required")
    @Size(max = 50, message = "Cheque number must not exceed 50 characters")
    private String chequeNumber;

    @NotNull(message = "Cheque date is required")
    private LocalDate chequeDate;

    @NotNull(message = "Cheque amount is required")
    @DecimalMin(value = "0.01", message = "Cheque amount must be > 0")
    private BigDecimal chequeAmount;

    @NotBlank(message = "Bank name is required")
    @Size(max = 100, message = "Bank name must not exceed 100 characters")
    private String bankName;

    @Size(max = 100, message = "Bank branch must not exceed 100 characters")
    private String bankBranch;

    @Size(max = 50, message = "Account number must not exceed 50 characters")
    private String accountNumber;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @Pattern(regexp = "^(RECEIVED|DEPOSITED|CLEARED|BOUNCED|RETURNED|CANCELLED)$", 
             message = "Status must be one of: RECEIVED, DEPOSITED, CLEARED, BOUNCED, RETURNED, CANCELLED")
    private String status;

    private LocalDate depositDate;

    private LocalDate clearanceDate;

    @Size(max = 1000, message = "Bounce reason must not exceed 1000 characters")
    private String bounceReason;

    @DecimalMin(value = "0.0", message = "Bounce charges must be >= 0")
    private BigDecimal bounceCharges;

    @Size(max = 1000, message = "Remarks must not exceed 1000 characters")
    private String remarks;
}
