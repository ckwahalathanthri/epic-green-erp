package lk.epicgreen.erp.supplier.dto.response;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceDTO {
    private Long id;
    private Long supplierId;
    private String supplierName;
    private Integer totalPurchaseOrders;
    private BigDecimal totalPurchaseAmount;
    private BigDecimal totalPaymentsMade;
    private BigDecimal outstandingBalance;
    private Integer averagePaymentDays;
    private Double onTimePaymentPercentage;
    private LocalDateTime lastPurchaseDate;
    private LocalDateTime lastPaymentDate;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
