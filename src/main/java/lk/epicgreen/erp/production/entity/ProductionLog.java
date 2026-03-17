package lk.epicgreen.erp.production.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_logs")
@Data
public class ProductionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "log_number", unique = true, length = 50)
    private String logNumber;
    
    @Column(name = "production_order_id")
    private Long productionOrderId;
    
    @Column(name = "work_center_id")
    private Long workCenterId;
    
    @Column(name = "activity_type", length = 50)
    private String activityType;
    
    @Column(name = "activity_date")
    private LocalDateTime activityDate;
    
    @Column(name = "duration")
    private Integer duration;
    
    @Column(name = "operator_name", length = 100)
    private String operatorName;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}