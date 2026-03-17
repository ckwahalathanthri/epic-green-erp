package lk.epicgreen.erp.production.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "production_recipes")
@Data
public class ProductionRecipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "recipe_code", unique = true, nullable = false, length = 50)
    private String recipeCode;
    
    @Column(name = "recipe_name", nullable = false, length = 200)
    private String recipeName;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_name", length = 200)
    private String productName;
    
    @Column(name = "recipe_version", length = 20)
    private String recipeVersion = "1.0";
    
    @Column(name = "recipe_status", length = 50)
    private String recipeStatus; // DRAFT, ACTIVE, INACTIVE, ARCHIVED
    
    @Column(name = "standard_yield", precision = 15, scale = 2)
    private BigDecimal standardYield;
    
    @Column(name = "yield_unit", length = 20)
    private String yieldUnit;
    
    @Column(name = "standard_batch_size", precision = 15, scale = 2)
    private BigDecimal standardBatchSize;
    
    @Column(name = "preparation_time")
    private Integer preparationTime;
    
    @Column(name = "production_time")
    private Integer productionTime;
    
    @Column(name = "total_time")
    private Integer totalTime;
    
    @Column(name = "material_cost", precision = 15, scale = 2)
    private BigDecimal materialCost;
    
    @Column(name = "labor_cost", precision = 15, scale = 2)
    private BigDecimal laborCost;
    
    @Column(name = "overhead_cost", precision = 15, scale = 2)
    private BigDecimal overheadCost;
    
    @Column(name = "total_cost", precision = 15, scale = 2)
    private BigDecimal totalCost;
    
    @Column(name = "cost_per_unit", precision = 15, scale = 2)
    private BigDecimal costPerUnit;
    
    @OneToMany(mappedBy = "productionRecipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> ingredients = new ArrayList<>();
    
    @Column(name = "approved_by", length = 100)
    private String approvedBy;
    
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    @PreUpdate
    public void calculateCosts() {
        if (this.preparationTime != null && this.productionTime != null) {
            this.totalTime = this.preparationTime + this.productionTime;
        }
        if (ingredients != null && !ingredients.isEmpty()) {
            this.materialCost = ingredients.stream()
                .map(RecipeIngredient::getTotalCost)
                .filter(cost -> cost != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        BigDecimal labor = this.laborCost != null ? this.laborCost : BigDecimal.ZERO;
        BigDecimal overhead = this.overheadCost != null ? this.overheadCost : BigDecimal.ZERO;
        BigDecimal material = this.materialCost != null ? this.materialCost : BigDecimal.ZERO;
        this.totalCost = material.add(labor).add(overhead);
        if (this.standardYield != null && this.standardYield.compareTo(BigDecimal.ZERO) > 0) {
            this.costPerUnit = this.totalCost.divide(this.standardYield, 2, BigDecimal.ROUND_HALF_UP);
        }
    }
}