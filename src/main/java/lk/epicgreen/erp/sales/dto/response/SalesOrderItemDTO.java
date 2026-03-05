package lk.epicgreen.erp.sales.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SalesOrderItemDTO {
    private Long id;
    private Long orderId;
    private Integer lineNumber;
    private Long productId;
    private String productCode;
    private String productName;
    private String description;
    private BigDecimal quantity;
    private String unitOfMeasure;
    private BigDecimal unitPrice;
    private BigDecimal discountPercentage;
    private BigDecimal discountAmount;
    private BigDecimal lineTotal;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;
    private BigDecimal pickedQuantity;
    private BigDecimal packedQuantity;
    private BigDecimal dispatchedQuantity;
    private BigDecimal deliveredQuantity;
    private String notes;
}