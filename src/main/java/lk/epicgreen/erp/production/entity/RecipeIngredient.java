package lk.epicgreen.erp.production.entity;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "recipe_ingredients")
@Data
public class RecipeIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private ProductionRecipe productionRecipe;
    
    @Column(name = "raw_material_id")
    private Long rawMaterialId;
    
    @Column(name = "ingredient_name", length = 200)
    private String ingredientName;
    
    @Column(name = "standard_quantity", precision = 15, scale = 4)
    private BigDecimal standardQuantity;
    
    @Column(name = "unit", length = 20)
    private String unit;
    
    @Column(name = "cost_per_unit", precision = 15, scale = 2)
    private BigDecimal costPerUnit;
    
    @Column(name = "total_cost", precision = 15, scale = 2)
    private BigDecimal totalCost;
    
    @Column(name = "wastage_percentage", precision = 5, scale = 2)
    private BigDecimal wastagePercentage;
    
    @PrePersist
    @PreUpdate
    public void calculateCost() {
        if (this.standardQuantity != null && this.costPerUnit != null) {
            this.totalCost = this.standardQuantity.multiply(this.costPerUnit);
            if (this.wastagePercentage != null && this.wastagePercentage.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal wastage = this.totalCost.multiply(this.wastagePercentage)
                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
                this.totalCost = this.totalCost.add(wastage);
            }
        }
    }
}