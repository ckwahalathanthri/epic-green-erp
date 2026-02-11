package lk.epicgreen.erp.accounting.dto.response;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LedgerEntryDTO {
    private Long id;
    private Long customerId;
    private String customerName;
    private LocalDate transactionDate;
    private String transactionType;
    private String referenceType;
    private String referenceNumber;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private BigDecimal balance;
    private String description;
    private LocalDate dueDate;
    private Boolean isReconciled;
    private String createdBy;
    private LocalDateTime createdAt;
}
