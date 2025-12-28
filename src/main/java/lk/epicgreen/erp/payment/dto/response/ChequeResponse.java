package lk.epicgreen.erp.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for Cheque response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChequeResponse {

    private Long id;
    private Long paymentId;
    private String paymentNumber;
    private String chequeNumber;
    private LocalDate chequeDate;
    private BigDecimal chequeAmount;
    private String bankName;
    private String bankBranch;
    private String accountNumber;
    private Long customerId;
    private String customerCode;
    private String customerName;
    private String status;
    private LocalDate depositDate;
    private LocalDate clearanceDate;
    private String bounceReason;
    private BigDecimal bounceCharges;
    private String remarks;
    private LocalDateTime createdAt;
    private Long createdBy;
    private String createdByName;
    private LocalDateTime updatedAt;
    private Long updatedBy;
}
