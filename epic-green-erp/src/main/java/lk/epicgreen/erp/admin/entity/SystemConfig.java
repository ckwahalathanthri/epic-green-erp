package lk.epicgreen.erp.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * SystemConfig Entity
 * Entity for system configuration
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "system_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SystemConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "config_key", unique = true, nullable = false, length = 100)
    private String configKey;
    
    @Column(name = "config_value", columnDefinition = "TEXT")
    private String configValue;
    
    @Column(name = "config_group", length = 50)
    private String configGroup;
    
    @Column(name = "data_type", length = 20)
    private String dataType; // STRING, INTEGER, BOOLEAN, DECIMAL, JSON
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "is_encrypted")
    private Boolean isEncrypted;
    
    @Column(name = "is_system_config")
    private Boolean isSystemConfig;
    
    @Column(name = "is_editable")
    private Boolean isEditable;
    
    @Column(name = "display_order")
    private Integer displayOrder;
    
    @Column(name = "validation_regex", length = 500)
    private String validationRegex;
    
    @Column(name = "min_value")
    private Double minValue;
    
    @Column(name = "max_value")
    private Double maxValue;
    
    @Column(name = "allowed_values", columnDefinition = "TEXT")
    private String allowedValues; // JSON array of allowed values
    
    @Column(name = "created_by_user_id")
    private Long createdByUserId;
    
    @Column(name = "updated_by_user_id")
    private Long updatedByUserId;
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
