package lk.epicgreen.erp.supplier.dto.response;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatementDTO {
    private Long id;
    private Long supplierId;
    private String supplierName;
    private String statementNumber;
    private LocalDate fromDate;
    private LocalDate toDate;
    private BigDecimal openingBalance;
    private BigDecimal totalDebits;
    private BigDecimal totalCredits;
    private BigDecimal closingBalance;
    private List<LedgerEntryDTO> transactions = new ArrayList<>();
    private String generatedBy;
    private LocalDateTime generatedAt;
}
