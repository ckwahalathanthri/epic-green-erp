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
@Table(name = "production_plans")
@Data
public class ProductionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "plan_number", unique = true, length = 50)
    private String planNumber;
    
    @Column(name = "plan_name", length = 200)
    private String planName;
    
    @Column(name = "plan_status", length = 50)
    private String planStatus; // DRAFT, APPROVED, IN_PROGRESS, COMPLETED
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "approved_by", length = 100)
    private String approvedBy;
    
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    
    @OneToMany(mappedBy = "productionPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductionPlanItem> items = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}