package lk.epicgreen.erp.warehouse.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class StockMovementDTO {
    private Long id;
    private String movementNumber;
    private LocalDate movementDate;
    private String movementType;
    private String transactionType;
    private Long productId;
    private String productCode;
    private Long warehouseId;
    private BigDecimal quantity;
    private BigDecimal unitCost;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private String documentType;
    private String documentNumber;
}
