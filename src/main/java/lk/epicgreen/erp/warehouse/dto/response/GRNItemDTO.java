package lk.epicgreen.erp.warehouse.dto.response;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GRNItemDTO {
    private Long id;
    private Long productId;
    private String productCode;
    private String productName;
    private BigDecimal orderedQuantity;
    private BigDecimal receivedQuantity;
    private BigDecimal rejectedQuantity;
    private BigDecimal acceptedQuantity;
    private String unitOfMeasure;
    private String batchNumber;
    private LocalDate expiryDate;
    private Long binId;
    private BigDecimal unitPrice;
    private String qualityStatus;
    private String notes;
}
