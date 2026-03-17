package lk.epicgreen.erp.warehouse.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AdjustmentItemDTO {
    private Long id;
    private Long productId;
    private String productCode;
    private String productName;
    private Integer systemQuantity;
    private Integer actualQuantity;
    private Integer adjustmentQuantity;
    private String unitOfMeasure;
    private BigDecimal unitCost;
    private BigDecimal costImpact;
}