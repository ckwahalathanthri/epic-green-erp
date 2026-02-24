package lk.epicgreen.erp.warehouse.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transfer_items")
@Data
public class TransferItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id")
    private WarehouseTransfer warehouseTransfer;
    
    @Column(name = "product_id")
    private Long productId;
    
    @Column(name = "product_code", length = 50)
    private String productCode;
    
    @Column(name = "product_name", length = 200)
    private String productName;
    
    @Column(name = "requested_quantity")
    private Integer requestedQuantity;
    
    @Column(name = "dispatched_quantity")
    private Integer dispatchedQuantity;
    
    @Column(name = "unit_of_measure", length = 20)
    private String unitOfMeasure;
    
    @Column(name = "unit_cost", precision = 15, scale = 2)
    private BigDecimal unitCost;
    
    @Column(name = "total_cost", precision = 15, scale = 2)
    private BigDecimal totalCost;
    
    @PrePersist
    @PreUpdate
    public void calculateCost() {
        if (this.unitCost != null && this.dispatchedQuantity != null) {
            this.totalCost = this.unitCost.multiply(BigDecimal.valueOf(this.dispatchedQuantity));
        }
    }
}