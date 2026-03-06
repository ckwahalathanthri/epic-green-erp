package lk.epicgreen.erp.warehouse.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity

@Table(name = "inventory_items")
@Data
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Long warehouseId;
    private BigDecimal quantityOnHand = BigDecimal.ZERO;
    private BigDecimal quantityReserved = BigDecimal.ZERO;
    private BigDecimal quantityAvailable = BigDecimal.ZERO;
    private BigDecimal averageCost = BigDecimal.ZERO;
    private BigDecimal totalValue = BigDecimal.ZERO;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        quantityAvailable = quantityOnHand.subtract(quantityReserved);
        totalValue = quantityOnHand.multiply(averageCost);
    }
}