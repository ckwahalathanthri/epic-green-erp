package lk.epicgreen.erp.warehouse.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "adjustment_items")
@Data
public class AdjustmentItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adjustment_id")
    private StockAdjustment stockAdjustment;
    
    @Column(name = "product_id")
    private Long productId;
    
    @Column(name = "product_code", length = 50)
    private String productCode;
    
    @Column(name = "product_name", length = 200)
    private String productName;
    
    @Column(name = "system_quantity")
    private Integer systemQuantity;
    
    @Column(name = "actual_quantity")
    private Integer actualQuantity;
    
    @Column(name = "adjustment_quantity")
    private Integer adjustmentQuantity;
    
    @Column(name = "unit_of_measure", length = 20)
    private String unitOfMeasure;
    
    @Column(name = "unit_cost", precision = 15, scale = 2)
    private BigDecimal unitCost;
    
    @Column(name = "cost_impact", precision = 15, scale = 2)
    private BigDecimal costImpact;
    
    @PrePersist
    @PreUpdate
    public void calculateAdjustment() {
        this.adjustmentQuantity = this.actualQuantity - this.systemQuantity;
        if (this.unitCost != null && this.adjustmentQuantity != null) {
            this.costImpact = this.unitCost.multiply(BigDecimal.valueOf(this.adjustmentQuantity));
        }
    }
}