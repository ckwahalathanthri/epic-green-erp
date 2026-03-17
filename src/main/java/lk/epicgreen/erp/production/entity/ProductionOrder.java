package lk.epicgreen.erp.production.entity;


import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "production_orders")
@Data
public class ProductionOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "production_order_number", unique = true, nullable = false, length = 50)
    private String productionOrderNumber;
    
    @Column(name = "recipe_id", nullable = false)
    private Long recipeId;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_name", length = 200)
    private String productName;
    
    @Column(name = "planned_quantity", nullable = false)
    private Integer plannedQuantity;
    
    @Column(name = "produced_quantity")
    private Integer producedQuantity = 0;
    
    @Column(name = "rejected_quantity")
    private Integer rejectedQuantity = 0;
    
    @Column(name = "unit_of_measure", length = 20)
    private String unitOfMeasure;
    
    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;
    
    @Column(name = "scheduled_start_date")
    private LocalDate scheduledStartDate;
    
    @Column(name = "actual_start_date")
    private LocalDate actualStartDate;
    
    @Column(name = "scheduled_end_date")
    private LocalDate scheduledEndDate;
    
    @Column(name = "actual_end_date")
    private LocalDate actualEndDate;
    
    @Column(name = "work_center_id")
    private Long workCenterId;
    
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    @Column(name = "order_status", nullable = false, length = 50)
    private String orderStatus; // PLANNED, RELEASED, IN_PROGRESS, COMPLETED, CANCELLED
    
    @Column(name = "priority_level", length = 20)
    private String priorityLevel;
    
    @Column(name = "standard_cost", precision = 15, scale = 2)
    private BigDecimal standardCost;
    
    @Column(name = "actual_cost", precision = 15, scale = 2)
    private BigDecimal actualCost;
    
    @Column(name = "cost_variance", precision = 15, scale = 2)
    private BigDecimal costVariance;
    
    @OneToMany(mappedBy = "productionOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MaterialConsumption> materialConsumptions = new ArrayList<>();


    @Column(name = "created_by", length = 100)
    private String createdBy;

    @OneToMany(mappedBy = "productionOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductionOrderItem> productionOrderItems = new ArrayList<>();

    @OneToMany(mappedBy = "productionOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductionOutput> productionOutputs = new ArrayList<>();


    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    @PreUpdate
    public void calculateVariances() {
        if (this.actualCost != null && this.standardCost != null) {
            this.costVariance = this.actualCost.subtract(this.standardCost);
        }
    }
    
    public void start(String startedBy) {
        this.orderStatus = "IN_PROGRESS";
        this.actualStartDate = LocalDate.now();
        this.createdBy = startedBy;
    }
    
    public void complete() {
        this.orderStatus = "COMPLETED";
        this.actualEndDate = LocalDate.now();
    }
}
