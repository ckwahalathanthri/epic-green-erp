package lk.epicgreen.erp.warehouse.dto.response;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class StockAdjustmentItemDTO {
    private Long id;
    private Long adjustmentId;
    private Integer lineNumber;
    private Long productId;
    private String productCode;
    private BigDecimal currentQuantity;
    private BigDecimal adjustmentQuantity;
    private BigDecimal newQuantity;
}
