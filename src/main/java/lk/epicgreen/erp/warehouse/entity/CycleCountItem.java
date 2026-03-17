package lk.epicgreen.erp.warehouse.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cycle_count_items")
@Data
public class CycleCountItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycle_count_id")
    private CycleCount cycleCount;
    
    @Column(name = "product_id")
    private Long productId;
    
    @Column(name = "product_code", length = 50)
    private String productCode;
    
    @Column(name = "product_name", length = 200)
    private String productName;
    
    @Column(name = "system_quantity")
    private Integer systemQuantity;
    
    @Column(name = "counted_quantity")
    private Integer countedQuantity;
    
    @Column(name = "variance_quantity")
    private Integer varianceQuantity;
    
    @Column(name = "variance_percentage", precision = 5, scale = 2)
    private BigDecimal variancePercentage;
    
    @Column(name = "unit_of_measure", length = 20)
    private String unitOfMeasure;
    
    @PrePersist
    @PreUpdate
    public void calculateVariance() {
        if (this.countedQuantity != null && this.systemQuantity != null) {
            this.varianceQuantity = this.countedQuantity - this.systemQuantity;
            if (this.systemQuantity > 0) {
                this.variancePercentage = BigDecimal.valueOf(this.varianceQuantity)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(this.systemQuantity), 2, BigDecimal.ROUND_HALF_UP);
            }
        }
    }
}