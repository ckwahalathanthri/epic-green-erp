package lk.epicgreen.erp.warehouse.entity;



import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_batches")
@Data
public class InventoryBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Long warehouseId;
    private String batchNumber;
    private LocalDate expiryDate;
    private LocalDate receivedDate;
    private BigDecimal quantity = BigDecimal.ZERO;
    private BigDecimal unitCost;
    private String batchStatus;
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}