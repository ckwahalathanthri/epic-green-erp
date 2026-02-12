package lk.epicgreen.erp.supplier.dto.response;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LedgerEntryDTO {
    private Long id;
    private Long supplierId;
    private String supplierName;
    private String transactionType;
    private String referenceNumber;
    private LocalDate transactionDate;
    private LocalDate dueDate;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private BigDecimal balance;
    private String description;
    private Boolean isReconciled;
    private LocalDateTime reconciledDate;
    private String reconciledBy;
    private String notes;
    private String createdBy;
    private LocalDateTime createdAt;
}
