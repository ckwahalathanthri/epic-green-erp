package lk.epicgreen.erp.production.entity;


import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_yields")
@Data
public class ProductionYield {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "production_order_id", nullable = false)
    private Long productionOrderId;
    
    @Column(name = "production_order_number", length = 50)
    private String productionOrderNumber;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_name", length = 200)
    private String productName;
    
    @Column(name = "recipe_id")
    private Long recipeId;
    
    @Column(name = "standard_yield", precision = 15, scale = 2)
    private BigDecimal standardYield;
    
    @Column(name = "actual_yield", precision = 15, scale = 2)
    private BigDecimal actualYield;
    
    @Column(name = "yield_variance", precision = 15, scale = 2)
    private BigDecimal yieldVariance;
    
    @Column(name = "yield_percentage", precision = 5, scale = 2)
    private BigDecimal yieldPercentage;
    
    @Column(name = "yield_category", length = 50)
    private String yieldCategory; // EXCELLENT, GOOD, AVERAGE, POOR
    
    @Column(name = "input_quantity", precision = 15, scale = 2)
    private BigDecimal inputQuantity;
    
    @Column(name = "output_quantity", precision = 15, scale = 2)
    private BigDecimal outputQuantity;
    
    @Column(name = "wastage_quantity", precision = 15, scale = 2)
    private BigDecimal wastageQuantity;
    
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
    public void calculateYield() {
        // Calculate yield variance
        if (this.actualYield != null && this.standardYield != null) {
            this.yieldVariance = this.actualYield.subtract(this.standardYield);
        }
        
        // Calculate yield percentage
        if (this.standardYield != null && this.standardYield.compareTo(BigDecimal.ZERO) > 0) {
            this.yieldPercentage = this.actualYield
                .multiply(BigDecimal.valueOf(100))
                .divide(this.standardYield, 2, BigDecimal.ROUND_HALF_UP);
        }
        
        // Categorize yield
        if (this.yieldPercentage != null) {
            if (this.yieldPercentage.compareTo(BigDecimal.valueOf(98)) >= 0) {
                this.yieldCategory = "EXCELLENT";
            } else if (this.yieldPercentage.compareTo(BigDecimal.valueOf(95)) >= 0) {
                this.yieldCategory = "GOOD";
            } else if (this.yieldPercentage.compareTo(BigDecimal.valueOf(90)) >= 0) {
                this.yieldCategory = "AVERAGE";
            } else {
                this.yieldCategory = "POOR";
            }
        }
        
        // Calculate wastage
        if (this.inputQuantity != null && this.outputQuantity != null) {
            this.wastageQuantity = this.inputQuantity.subtract(this.outputQuantity);
        }
    }
}