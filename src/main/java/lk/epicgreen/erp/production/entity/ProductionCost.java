package lk.epicgreen.erp.production.entity;



import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_costs")
@Data
public class ProductionCost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "recipe_id")
    private Long recipeId;
    
    @Column(name = "product_id")
    private Long productId;
    
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
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    @PreUpdate
    public void calculateTotalCost() {
        BigDecimal material = this.materialCost != null ? this.materialCost : BigDecimal.ZERO;
        BigDecimal labor = this.laborCost != null ? this.laborCost : BigDecimal.ZERO;
        BigDecimal overhead = this.overheadCost != null ? this.overheadCost : BigDecimal.ZERO;
        this.totalCost = material.add(labor).add(overhead);
    }
}