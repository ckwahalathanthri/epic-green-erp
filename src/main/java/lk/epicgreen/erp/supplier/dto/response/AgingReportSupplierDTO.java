package lk.epicgreen.erp.supplier.dto.response;
import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgingReportSupplierDTO {
    private Long supplierId;
    private String supplierName;
    private String supplierCode;
    private BigDecimal currentAmount = BigDecimal.ZERO;
    private BigDecimal days30Amount = BigDecimal.ZERO;
    private BigDecimal days60Amount = BigDecimal.ZERO;
    private BigDecimal days90Amount = BigDecimal.ZERO;
    private BigDecimal days90PlusAmount = BigDecimal.ZERO;
    private BigDecimal totalOutstanding = BigDecimal.ZERO;
    private List<LedgerEntryDTO> overdueTransactions = new ArrayList<>();
}
