package lk.epicgreen.erp.payment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for Payment Allocation (Bill-to-Bill settlement)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAllocationRequest {

    @NotNull(message = "Invoice ID is required")
    private Long invoiceId;

    @NotNull(message = "Allocated amount is required")
    @DecimalMin(value = "0.01", message = "Allocated amount must be > 0")
    private BigDecimal allocatedAmount;

    @NotNull(message = "Allocation date is required")
    private LocalDate allocationDate;

    @Size(max = 1000, message = "Remarks must not exceed 1000 characters")
    private String remarks;
}
