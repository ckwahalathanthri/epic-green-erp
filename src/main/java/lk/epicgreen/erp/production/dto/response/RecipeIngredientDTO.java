package lk.epicgreen.erp.production.dto.response;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RecipeIngredientDTO {
    private Long id;
    private Long rawMaterialId;
    private String ingredientName;
    private BigDecimal standardQuantity;
    private String unit;
    private BigDecimal costPerUnit;
    private BigDecimal totalCost;
    private BigDecimal wastagePercentage;
}