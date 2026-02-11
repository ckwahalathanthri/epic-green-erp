package lk.epicgreen.erp.accounting.dto.response;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatementDTO {
    private Long id;
    private Long customerId;
    private String customerName;
    private String statementNumber;
    private LocalDate statementDate;
    private LocalDate fromDate;
    private LocalDate toDate;
    private BigDecimal openingBalance;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private BigDecimal closingBalance;
    private List<LedgerEntryDTO> transactions;
}
