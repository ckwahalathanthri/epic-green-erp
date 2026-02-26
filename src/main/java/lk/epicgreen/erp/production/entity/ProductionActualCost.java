package lk.epicgreen.erp.production.entity;


import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_actual_costs")
@Data
public class ProductionActualCost {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "production_order_id", nullable = false)
    private Long productionOrderId;
    
    @Column(name = "production_order_number", length = 50)
    private String productionOrderNumber;
    
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_name", length = 200)
    private String productName;
    
    @Column(name = "actual_material_cost", precision = 15, scale = 2)
    private BigDecimal actualMaterialCost;
    
    @Column(name = "actual_labor_cost", precision = 15, scale = 2)
    private BigDecimal actualLaborCost;
    
    @Column(name = "actual_overhead_cost", precision = 15, scale = 2)
    private BigDecimal actualOverheadCost;
    
    @Column(name = "total_actual_cost", precision = 15, scale = 2)
    private BigDecimal totalActualCost;
    
    @Column(name = "standard_cost", precision = 15, scale = 2)
    private BigDecimal standardCost;
    
    @Column(name = "cost_variance", precision = 15, scale = 2)
    private BigDecimal costVariance;
    
    @Column(name = "variance_percentage", precision = 5, scale = 2)
    private BigDecimal variancePercentage;
    
    @Column(name = "quantity_produced", precision = 15, scale = 2)
    private BigDecimal quantityProduced;
    
    @Column(name = "cost_per_unit", precision = 15, scale = 2)
    private BigDecimal costPerUnit;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    @PreUpdate
    public void calculateCosts() {
        // Calculate total actual cost
        BigDecimal material = this.actualMaterialCost != null ? this.actualMaterialCost : BigDecimal.ZERO;
        BigDecimal labor = this.actualLaborCost != null ? this.actualLaborCost : BigDecimal.ZERO;
        BigDecimal overhead = this.actualOverheadCost != null ? this.actualOverheadCost : BigDecimal.ZERO;
        this.totalActualCost = material.add(labor).add(overhead);
        
        // Calculate cost variance
        if (this.standardCost != null) {
            this.costVariance = this.totalActualCost.subtract(this.standardCost);
            
            // Calculate variance percentage
            if (this.standardCost.compareTo(BigDecimal.ZERO) > 0) {
                this.variancePercentage = this.costVariance
                    .multiply(BigDecimal.valueOf(100))
                    .divide(this.standardCost, 2, BigDecimal.ROUND_HALF_UP);
            }
        }
        
        // Calculate cost per unit
        if (this.quantityProduced != null && this.quantityProduced.compareTo(BigDecimal.ZERO) > 0) {
            this.costPerUnit = this.totalActualCost.divide(this.quantityProduced, 2, BigDecimal.ROUND_HALF_UP);
        }
    }
}