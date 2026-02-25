package lk.epicgreen.erp.production.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MaterialConsumptionDTO {
    private Long id;
    private String consumptionNumber;
    private Long productionOrderId;
    private Long materialId;
    private String materialName;
    private String batchNumber;
    private Long warehouseId;
    private BigDecimal plannedQuantity;
    private BigDecimal actualQuantity;
    private BigDecimal varianceQuantity;
    private String unitOfMeasure;
    private LocalDate consumptionDate;
    private BigDecimal costPerUnit;
    private BigDecimal totalCost;
}