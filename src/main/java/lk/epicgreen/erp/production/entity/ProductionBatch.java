package lk.epicgreen.erp.production.entity;


import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_batches")
@Data
public class ProductionBatch {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "batch_number", unique = true, nullable = false, length = 50)
    private String batchNumber;
    
    @Column(name = "production_order_id")
    private Long productionOrderId;
    
    @Column(name = "production_order_number", length = 50)
    private String productionOrderNumber;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_code", length = 50)
    private String productCode;
    
    @Column(name = "product_name", length = 200)
    private String productName;
    
    @Column(name = "batch_quantity", precision = 15, scale = 2)
    private BigDecimal batchQuantity;
    
    @Column(name = "unit_of_measure", length = 20)
    private String unitOfMeasure;
    
    @Column(name = "production_date", nullable = false)
    private LocalDate productionDate;
    
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    
    @Column(name = "batch_status", length = 50)
    private String batchStatus; // ACTIVE, QUARANTINE, APPROVED, REJECTED, RELEASED
    
    @Column(name = "quality_grade", length = 50)
    private String qualityGrade; // A, B, C, REJECT
    
    @Column(name = "warehouse_id")
    private Long warehouseId;
    
    @Column(name = "warehouse_name", length = 200)
    private String warehouseName;
    
    @Column(name = "storage_location", length = 100)
    private String storageLocation;
    
    @Column(name = "manufacturing_notes", columnDefinition = "TEXT")
    private String manufacturingNotes;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}