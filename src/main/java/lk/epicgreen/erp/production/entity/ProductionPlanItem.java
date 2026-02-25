package lk.epicgreen.erp.production.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "production_plan_items")
@Data
public class ProductionPlanItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private ProductionPlan productionPlan;
    
    @Column(name = "recipe_id")
    private Long recipeId;
    
    @Column(name = "product_id")
    private Long productId;
    
    @Column(name = "product_name", length = 200)
    private String productName;
    
    @Column(name = "planned_quantity")
    private Integer plannedQuantity;
    
    @Column(name = "actual_quantity")
    private Integer actualQuantity = 0;
    
    @Column(name = "scheduled_date")
    private LocalDate scheduledDate;
    
    @Column(name = "priority", length = 20)
    private String priority;
    
    @Column(name = "production_status", length = 50)
    private String productionStatus;
}