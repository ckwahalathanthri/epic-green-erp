package lk.epicgreen.erp.sales.dto.response;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class DispatchItemDTO {
    private Long id;
    private Long dispatchId;
    private Integer lineNumber;
    private Long productId;
    private String productCode;
    private String productName;
    private BigDecimal dispatchedQuantity;
    private String unitOfMeasure;
    private String batchNumber;
    private Long warehouseId;
    private String warehouseLocation;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
    private String notes;
}