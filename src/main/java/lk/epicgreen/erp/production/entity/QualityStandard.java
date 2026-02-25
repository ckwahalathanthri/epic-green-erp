package lk.epicgreen.erp.production.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "quality_standards")
@Data
public class QualityStandard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "standard_code", unique = true, length = 50)
    private String standardCode;
    
    @Column(name = "product_id")
    private Long productId;
    
    @Column(name = "product_name", length = 200)
    private String productName;
    
    @Column(name = "parameter_name", length = 200)
    private String parameterName;
    
    @Column(name = "min_value", precision = 15, scale = 4)
    private BigDecimal minValue;
    
    @Column(name = "max_value", precision = 15, scale = 4)
    private BigDecimal maxValue;
    
    @Column(name = "target_value", precision = 15, scale = 4)
    private BigDecimal targetValue;
    
    @Column(name = "unit", length = 50)
    private String unit;
    
    @Column(name = "is_critical")
    private Boolean isCritical = false;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}