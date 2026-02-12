package lk.epicgreen.erp.product.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Specification Template Entity
 * Reusable templates for product specifications
 */
@Entity
@Table(name = "specification_templates")
@Data
@NoArgsConstructor
public class SpecificationTemplate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "template_name", nullable = false)
    private String templateName;
    
    @Column(name = "spec_name", nullable = false)
    private String specName;
    
    @Column(name = "spec_unit")
    private String specUnit;
    
    @Column(name = "data_type")
    private String dataType; // TEXT, NUMBER, BOOLEAN, DATE
    
    @Column(name = "is_required")
    private Boolean isRequired = false;
    
    @Column(name = "default_value")
    private String defaultValue;
    
    @Column(name = "display_order")
    private Integer displayOrder;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
