package lk.epicgreen.erp.production.entity;



import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "work_centers")
@Data
public class WorkCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "work_center_code", unique = true, length = 50)
    private String workCenterCode;
    
    @Column(name = "work_center_name", length = 200)
    private String workCenterName;
    
    @Column(name = "work_center_type", length = 50)
    private String workCenterType;
    
    @Column(name = "capacity", precision = 15, scale = 2)
    private BigDecimal capacity;
    
    @Column(name = "capacity_unit", length = 20)
    private String capacityUnit;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "operator_required")
    private Integer operatorRequired = 1;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}