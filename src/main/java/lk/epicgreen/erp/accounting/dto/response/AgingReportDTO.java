package lk.epicgreen.erp.accounting.dto.response;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgingReportDTO {
    private Long customerId;
    private String customerCode;
    private String customerName;
    private BigDecimal currentAmount;
    private BigDecimal days30;
    private BigDecimal days60;
    private BigDecimal days90;
    private BigDecimal days90Plus;
    private BigDecimal totalOutstanding;
}
