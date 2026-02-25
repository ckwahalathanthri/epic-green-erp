package lk.epicgreen.erp.production.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductionRecipeDTO {
    private Long id;
    private String recipeCode;
    private String recipeName;
    private Long productId;
    private String productName;
    private String recipeVersion;
    private String recipeStatus;
    private BigDecimal standardYield;
    private String yieldUnit;
    private BigDecimal standardBatchSize;
    private Integer preparationTime;
    private Integer productionTime;
    private Integer totalTime;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal overheadCost;
    private BigDecimal totalCost;
    private BigDecimal costPerUnit;
    private List<RecipeIngredientDTO> ingredients;
}