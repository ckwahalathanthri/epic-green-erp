package lk.epicgreen.erp.common.audit;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * AuditEntity base class
 * Provides auditing fields for tracking creation and modification
 * All entities that need audit tracking should extend this class
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class AuditEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Created timestamp
     * Automatically set when entity is first persisted
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * Created by (user ID)
     * Automatically set from security context when entity is first persisted
     */
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdBy;
    
    /**
     * Last updated timestamp
     * Automatically updated when entity is modified
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * Last updated by (user ID)
     * Automatically set from security context when entity is modified
     */
    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;
    
    /**
     * Called before entity is persisted
     */
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }
    
    /**
     * Called before entity is updated
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
