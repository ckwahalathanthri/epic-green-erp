package lk.epicgreen.erp.production.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductionBatchDTO {
    private Long id;
    private String batchNumber;
    private Long productionOrderId;
    private Long productId;
    private String productName;
    private BigDecimal batchQuantity;
    private String unitOfMeasure;
    private LocalDate productionDate;
    private LocalDate expiryDate;
    private String batchStatus;
    private String qualityGrade;
    private Long warehouseId;
}