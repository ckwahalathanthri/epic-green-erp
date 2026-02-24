package lk.epicgreen.erp.warehouse.entity;

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
@Table(name = "cycle_counts")
@Data
public class CycleCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "count_number", unique = true, length = 50)
    private String countNumber;
    
    @Column(name = "warehouse_id")
    private Long warehouseId;
    
    @Column(name = "count_date")
    private LocalDate countDate;
    
    @Column(name = "count_type", length = 50)
    private String countType;
    
    @Column(name = "count_status", length = 50)
    private String countStatus;
    
    @Column(name = "count_method", length = 50)
    private String countMethod;
    
    @Column(name = "counter_name", length = 100)
    private String counterName;
    
    @Column(name = "approved_by", length = 100)
    private String approvedBy;
    
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    
    @Column(name = "total_items_counted")
    private Integer totalItemsCounted = 0;
    
    @Column(name = "items_with_variance")
    private Integer itemsWithVariance = 0;
    
    @Column(name = "accuracy_percentage", precision = 5, scale = 2)
    private BigDecimal accuracyPercentage;
    
    @OneToMany(mappedBy = "cycleCount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CycleCountItem> items = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}