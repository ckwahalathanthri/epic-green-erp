package lk.epicgreen.erp.warehouse.dto.response;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class StockTransferItemDTO {
    private Long id;
    private Long transferId;
    private Integer lineNumber;
    private Long productId;
    private String productCode;
    private String batchNumber;
    private BigDecimal quantityRequested;
    private BigDecimal quantityShipped;
    private BigDecimal quantityReceived;
}
