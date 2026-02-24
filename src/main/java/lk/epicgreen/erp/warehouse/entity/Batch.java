package lk.epicgreen.erp.warehouse.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "batches")
@Data
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "batch_number", unique = true, nullable = false, length = 50)
    private String batchNumber;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_code", length = 50)
    private String productCode;
    
    @Column(name = "product_name", length = 200)
    private String productName;
    
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;
    
    @Column(name = "manufacturing_date")
    private LocalDate manufacturingDate;
    
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    
    @Column(name = "initial_quantity", nullable = false)
    private Integer initialQuantity;
    
    @Column(name = "current_quantity", nullable = false)
    private Integer currentQuantity;
    
    @Column(name = "reserved_quantity")
    private Integer reservedQuantity = 0;
    
    @Column(name = "available_quantity")
    private Integer availableQuantity;
    
    @Column(name = "unit_of_measure", length = 20)
    private String unitOfMeasure;
    
    @Column(name = "unit_cost", precision = 15, scale = 2)
    private BigDecimal unitCost;
    
    @Column(name = "total_value", precision = 15, scale = 2)
    private BigDecimal totalValue;
    
    @Column(name = "batch_status", length = 50)
    private String batchStatus;
    
    @Column(name = "grn_number", length = 50)
    private String grnNumber;
    
    @Column(name = "storage_location", length = 100)
    private String storageLocation;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    @PreUpdate
    public void calculateValues() {
        this.availableQuantity = this.currentQuantity - (this.reservedQuantity != null ? this.reservedQuantity : 0);
        if (this.unitCost != null && this.currentQuantity != null) {
            this.totalValue = this.unitCost.multiply(BigDecimal.valueOf(this.currentQuantity));
        }
    }
}