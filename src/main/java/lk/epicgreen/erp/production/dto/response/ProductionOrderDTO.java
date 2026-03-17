package lk.epicgreen.erp.production.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductionOrderDTO {
    private Long id;
    private String productionOrderNumber;
    private Long recipeId;
    private Long productId;
    private String productName;
    private Integer plannedQuantity;
    private Integer producedQuantity;
    private Integer rejectedQuantity;
    private String unitOfMeasure;
    private LocalDate orderDate;
    private LocalDate scheduledStartDate;
    private LocalDate actualStartDate;
    private String orderStatus;
    private String priorityLevel;
    private BigDecimal standardCost;
    private BigDecimal actualCost;
    private BigDecimal costVariance;
}