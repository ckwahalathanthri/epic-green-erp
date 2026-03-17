package lk.epicgreen.erp.production.dto.response;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductionActualCostDTO {
    private Long id;
    private Long productionOrderId;
    private String batchNumber;
    private Long productId;
    private String productName;
    private BigDecimal actualMaterialCost;
    private BigDecimal actualLaborCost;
    private BigDecimal actualOverheadCost;
    private BigDecimal totalActualCost;
    private BigDecimal standardCost;
    private BigDecimal costVariance;
    private BigDecimal variancePercentage;
    private BigDecimal costPerUnit;
}