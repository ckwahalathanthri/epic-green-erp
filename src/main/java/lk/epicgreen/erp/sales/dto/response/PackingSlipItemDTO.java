package lk.epicgreen.erp.sales.dto.response;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PackingSlipItemDTO {
    private Long id;
    private Long packingSlipId;
    private Integer lineNumber;
    private Long productId;
    private String productCode;
    private String productName;
    private BigDecimal quantityToPack;
    private BigDecimal packedQuantity;
    private String unitOfMeasure;
    private String batchNumber;
    private String packageNumber;
    private String packageType;
    private BigDecimal weight;
    private Boolean isPacked;
    private String notes;
}