package lk.epicgreen.erp.admin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * SystemConfig entity
 * Represents system-wide configuration settings
 * 
 * Note: Does NOT extend AuditEntity (different update pattern)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "system_config", indexes = {
    @Index(name = "idx_config_group", columnList = "config_group")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Configuration key (unique)
     */
    @NotBlank(message = "Config key is required")
    @Size(max = 100)
    @Column(name = "config_key", nullable = false, unique = true, length = 100)
    private String configKey;
    
    /**
     * Configuration value (stored as text)
     */
    @NotBlank(message = "Config value is required")
    @Column(name = "config_value", nullable = false, columnDefinition = "TEXT")
    private String configValue;
    
    /**
     * Configuration group (for organizing settings)
     */
    @NotBlank(message = "Config group is required")
    @Size(max = 50)
    @Column(name = "config_group", nullable = false, length = 50)
    private String configGroup;
    
    /**
     * Description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * Data type (STRING, INTEGER, DECIMAL, BOOLEAN, JSON)
     */
    @Column(name = "data_type", length = 20)
    private String dataType;
    
    /**
     * Is encrypted (for sensitive data like API keys)
     */
    @Column(name = "is_encrypted")
    private Boolean isEncrypted;
    
    /**
     * Created timestamp
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Last updated timestamp
     */
    @Column(name = "updated_at", nullable = false, updatable = true)
    private LocalDateTime updatedAt;
    
    
    /**
     * Updated by (user ID)
     */
    @Column(name = "updated_by")
    private Long updatedBy;
    
    /**
     * Check if encrypted
     */
    @Transient
    public boolean isEncrypted() {
        return Boolean.TRUE.equals(isEncrypted);
    }
    
    /**
     * Get value as String
     */
    @Transient
    public String getAsString() {
        return configValue;
    }
    
    /**
     * Get value as Integer
     */
    @Transient
    public Integer getAsInteger() {
        try {
            return Integer.parseInt(configValue);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Get value as Long
     */
    @Transient
    public Long getAsLong() {
        try {
            return Long.parseLong(configValue);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Get value as Boolean
     */
    @Transient
    public Boolean getAsBoolean() {
        return Boolean.parseBoolean(configValue);
    }
    
    /**
     * Get value as Double
     */
    @Transient
    public Double getAsDouble() {
        try {
            return Double.parseDouble(configValue);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    @PrePersist
    protected void onCreate() {
        if (dataType == null) {
            dataType = "STRING";
        }
        if (isEncrypted == null) {
            isEncrypted = false;
        }
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SystemConfig)) return false;
        SystemConfig that = (SystemConfig) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
