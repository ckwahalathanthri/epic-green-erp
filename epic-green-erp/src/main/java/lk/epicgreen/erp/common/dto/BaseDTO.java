package lk.epicgreen.erp.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base DTO class containing common audit fields
 * All DTOs should extend this class to include audit information
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Entity ID (primary key)
     */
    private Long id;
    
    /**
     * When this record was created
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    /**
     * Who created this record
     */
    private String createdBy;
    
    /**
     * When this record was last updated
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    /**
     * Who last updated this record
     */
    private String updatedBy;
    
    /**
     * When this record was soft deleted (null if not deleted)
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deletedAt;
    
    /**
     * Who soft deleted this record
     */
    private String deletedBy;
    
    /**
     * Version for optimistic locking
     */
    private Integer version;
    
    /**
     * Record status (Active, Inactive, etc.)
     */
    private String status;
    
    /**
     * Additional notes or remarks
     */
    private String remarks;
    
    // ==================== Helper Methods ====================
    
    /**
     * Checks if this record is soft deleted
     * 
     * @return true if deleted
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }
    
    /**
     * Checks if this is a new record (not yet saved)
     * 
     * @return true if id is null
     */
    public boolean isNew() {
        return id == null;
    }
    
    /**
     * Checks if this record has been modified
     * 
     * @return true if updatedAt is after createdAt
     */
    public boolean isModified() {
        return updatedAt != null && createdAt != null && updatedAt.isAfter(createdAt);
    }
    
    /**
     * Gets display text for audit information
     * Example: "Created by John Doe on 2024-12-10 10:30:00"
     * 
     * @return Audit display text
     */
    public String getAuditDisplayText() {
        if (createdBy != null && createdAt != null) {
            return String.format("Created by %s on %s", createdBy, createdAt);
        }
        return "No audit information available";
    }
    
    /**
     * Gets display text for last modification
     * Example: "Last modified by Jane Smith on 2024-12-10 15:45:00"
     * 
     * @return Modification display text
     */
    public String getModificationDisplayText() {
        if (updatedBy != null && updatedAt != null) {
            return String.format("Last modified by %s on %s", updatedBy, updatedAt);
        }
        return "Never modified";
    }
}
