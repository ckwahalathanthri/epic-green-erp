package lk.epicgreen.erp.warehouse.dto.response;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PhysicalInventoryLineDTO {
    private Long id;
    private Long physicalInventoryId;
    private Integer lineNumber;
    private Long productId;
    private String productCode;
    private String locationCode;
    private BigDecimal systemQuantity;
    private BigDecimal countedQuantity;
    private BigDecimal varianceQuantity;
    private String countStatus;
}
