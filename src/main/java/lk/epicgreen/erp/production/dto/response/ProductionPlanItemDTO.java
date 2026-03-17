package lk.epicgreen.erp.production.dto.response;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ProductionPlanItemDTO {
    private Long id;
    private Long recipeId;
    private Long productId;
    private String productName;
    private Integer plannedQuantity;
    private Integer actualQuantity;
    private LocalDate scheduledDate;
    private String priority;
    private String productionStatus;
}