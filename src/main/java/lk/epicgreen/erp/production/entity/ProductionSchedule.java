package lk.epicgreen.erp.production.entity;



import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_schedules")
@Data
public class ProductionSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "schedule_number", unique = true, length = 50)
    private String scheduleNumber;
    
    @Column(name = "work_center_id")
    private Long workCenterId;
    
    @Column(name = "recipe_id")
    private Long recipeId;
    
    @Column(name = "product_id")
    private Long productId;
    
    @Column(name = "scheduled_start_time")
    private LocalDateTime scheduledStartTime;
    
    @Column(name = "scheduled_end_time")
    private LocalDateTime scheduledEndTime;
    
    @Column(name = "assigned_operator", length = 100)
    private String assignedOperator;
    
    @Column(name = "schedule_status", length = 50)
    private String scheduleStatus;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}