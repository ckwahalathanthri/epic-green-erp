package lk.epicgreen.erp.warehouse.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferItemDTO {
    private Long id;
    private Long productId;
    private String productCode;
    private String productName;
    private Integer requestedQuantity;
    private Integer dispatchedQuantity;
    private String unitOfMeasure;
    private BigDecimal unitCost;
    private BigDecimal totalCost;
}