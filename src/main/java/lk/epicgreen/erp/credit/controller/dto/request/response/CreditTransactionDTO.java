package lk.epicgreen.erp.credit.controller.dto.request.response;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditTransactionDTO {
    private Long id;
    private Long creditLimitId;
    private String transactionType;
    private BigDecimal amount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private String referenceType;
    private Long referenceId;
    private LocalDateTime transactionDate;
    private String description;
    private String createdBy;
    private LocalDateTime createdAt;
}
