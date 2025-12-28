package lk.epicgreen.erp.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for Payment Allocation response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAllocationResponse {

    private Long id;
    private Long paymentId;
    private String paymentNumber;
    private Long invoiceId;
    private String invoiceNumber;
    private BigDecimal allocatedAmount;
    private LocalDate allocationDate;
    private String remarks;
    private LocalDateTime createdAt;
    private Long createdBy;
    private String createdByName;
}
