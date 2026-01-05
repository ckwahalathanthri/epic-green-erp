package lk.epicgreen.erp.payment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for creating/updating Payment
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotBlank(message = "Payment number is required")
    @Size(max = 30, message = "Payment number must not exceed 30 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Payment number must contain only uppercase letters, numbers, hyphens and underscores")
    private String paymentNumber;

    @NotNull(message = "Payment date is required")
    private LocalDate paymentDate;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Payment mode is required")
    @Pattern(regexp = "^(CASH|CHEQUE|BANK_TRANSFER|CREDIT_CARD|ONLINE)$", 
             message = "Payment mode must be one of: CASH, CHEQUE, BANK_TRANSFER, CREDIT_CARD, ONLINE")
    private String paymentMode;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.01", message = "Total amount must be > 0")
    private BigDecimal totalAmount;

    @DecimalMin(value = "0.0", message = "Allocated amount must be >= 0")
    private BigDecimal allocatedAmount;

    @Pattern(regexp = "^(DRAFT|PENDING|CLEARED|BOUNCED|CANCELLED)$", 
             message = "Status must be one of: DRAFT, PENDING, CLEARED, BOUNCED, CANCELLED")
    private String status;

    // Bank/Cheque specific fields
    @Size(max = 100, message = "Bank name must not exceed 100 characters")
    private String bankName;

    @Size(max = 100, message = "Bank branch must not exceed 100 characters")
    private String bankBranch;

    @Size(max = 50, message = "Cheque number must not exceed 50 characters")
    private String chequeNumber;

    private LocalDate chequeDate;

    private LocalDate chequeClearanceDate;

    @Size(max = 50, message = "Bank reference number must not exceed 50 characters")
    private String bankReferenceNumber;

    // Collection details
    private Long collectedBy;

    private LocalDateTime collectedAt;

    @Size(max = 1000, message = "Remarks must not exceed 1000 characters")
    private String remarks;

    // Payment allocations (bill-to-bill settlement)
    @Valid
    private List<PaymentAllocationRequest> allocations;
}
