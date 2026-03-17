package lk.epicgreen.erp.accounting.dto.response;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsDTO {
    private Long customerId;
    private String customerName;
    private BigDecimal totalSales;
    private BigDecimal totalPayments;
    private BigDecimal totalOutstanding;
    private LocalDateTime lastPurchaseDate;
    private LocalDateTime lastPaymentDate;
    private Integer averageDaysToPay;
    private Double creditUtilizationPercentage;
    private Integer totalInvoices;
    private Integer totalPaymentsCount;
}
