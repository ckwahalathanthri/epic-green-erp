package lk.epicgreen.erp.production.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "production_order_items")
@Data
public class ProductionOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_order_id")
    private ProductionOrder productionOrder;
    
    @Column(name = "product_id")
    private Long productId;
    
    @Column(name = "product_name", length = 200)
    private String productName;
    
    @Column(name = "planned_quantity")
    private Integer plannedQuantity;
    
    @Column(name = "produced_quantity")
    private Integer producedQuantity = 0;
    
    @Column(name = "unit_of_measure", length = 20)
    private String unitOfMeasure;
    
    @Column(name = "standard_cost", precision = 15, scale = 2)
    private BigDecimal standardCost;
    
    @Column(name = "actual_cost", precision = 15, scale = 2)
    private BigDecimal actualCost;
}