package lk.epicgreen.erp.warehouse.dto.response;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueItemDTO {
    private Long id;
    private Long productId;
    private String productCode;
    private String productName;
    private BigDecimal requestedQuantity;
    private BigDecimal issuedQuantity;
    private String unitOfMeasure;
    private Long binId;
    private String batchNumber;
    private BigDecimal unitCost;
    private String notes;
}
