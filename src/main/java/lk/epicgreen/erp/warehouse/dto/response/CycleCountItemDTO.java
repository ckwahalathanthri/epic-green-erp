package lk.epicgreen.erp.warehouse.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CycleCountItemDTO {
    private Long id;
    private Long productId;
    private String productCode;
    private String productName;
    private Integer systemQuantity;
    private Integer countedQuantity;
    private Integer varianceQuantity;
    private BigDecimal variancePercentage;
    private String unitOfMeasure;
}