package lk.epicgreen.erp.common.audit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base audit entity with common audit fields
 * All entities should extend this class to get automatic audit tracking
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Creation timestamp
     * Automatically populated when entity is first persisted
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * User who created the record
     * Automatically populated from security context
     */
    @CreatedBy
    @Column(name = "created_by", length = 50, updatable = false)
    private String createdBy;
    
    /**
     * Last update timestamp
     * Automatically updated on every modification
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * User who last modified the record
     * Automatically populated from security context
     */
    @LastModifiedBy
    @Column(name = "updated_by", length = 50)
    private String updatedBy;
    
    /**
     * Soft delete timestamp
     * Set when record is soft deleted
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
    /**
     * User who deleted the record
     * Set from security context on soft delete
     */
    @Column(name = "deleted_by", length = 50)
    private String deletedBy;
    
    /**
     * Version number for optimistic locking
     * Automatically incremented on each update
     */
    @Version
    @Column(name = "version")
    private Long version;
    
    /**
     * Checks if entity is deleted (soft delete)
     * 
     * @return true if deleted
     */
    @Transient
    public boolean isDeleted() {
        return deletedAt != null;
    }
    
    /**
     * Checks if entity is new (not yet persisted)
     * 
     * @return true if new
     */
    @Transient
    public boolean isNew() {
        return createdAt == null;
    }
    
    /**
     * Marks entity as deleted (soft delete)
     * 
     * @param deletedBy User performing the delete
     */
    public void markAsDeleted(String deletedBy) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }
    
    /**
     * Restores soft deleted entity
     */
    public void restore() {
        this.deletedAt = null;
        this.deletedBy = null;
    }
    
    /**
     * Gets audit info as string
     * 
     * @return Audit info string
     */
    @Transient
    public String getAuditInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Created: ").append(createdAt).append(" by ").append(createdBy);
        if (updatedAt != null) {
            info.append(" | Updated: ").append(updatedAt).append(" by ").append(updatedBy);
        }
        if (deletedAt != null) {
            info.append(" | Deleted: ").append(deletedAt).append(" by ").append(deletedBy);
        }
        return info.toString();
    }
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
