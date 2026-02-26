package lk.epicgreen.erp.production.entity;


import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quality_inspections")
@Data
public class QualityInspection {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "inspection_number", unique = true, nullable = false, length = 50)
    private String inspectionNumber;
    
    @Column(name = "production_order_id")
    private Long productionOrderId;
    
    @Column(name = "production_order_number", length = 50)
    private String productionOrderNumber;
    
    @Column(name = "output_batch_id")
    private Long outputBatchId;
    
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_name", length = 200)
    private String productName;
    
    @Column(name = "inspection_type", nullable = false, length = 50)
    private String inspectionType; // IN_PROCESS, FINAL, RANDOM
    
    @Column(name = "inspection_date", nullable = false)
    private LocalDate inspectionDate;
    
    @Column(name = "inspector_name", length = 100)
    private String inspectorName;
    
    @Column(name = "inspection_status", nullable = false, length = 50)
    private String inspectionStatus; // SCHEDULED, IN_PROGRESS, COMPLETED
    
    @Column(name = "overall_result", length = 50)
    private String overallResult; // PASSED, FAILED, CONDITIONAL
    
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    @Column(name = "corrective_actions", columnDefinition = "TEXT")
    private String correctiveActions;
    
    @OneToMany(mappedBy = "qualityInspection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QualityInspectionTest> tests = new ArrayList<>();
    
    @Column(name = "approved_by", length = 100)
    private String approvedBy;
    
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public void approve(String approvedBy) {
        this.inspectionStatus = "COMPLETED";
        this.approvedBy = approvedBy;
        this.approvedAt = LocalDateTime.now();
    }
}