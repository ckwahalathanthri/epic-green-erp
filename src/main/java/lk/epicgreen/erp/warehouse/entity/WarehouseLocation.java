package lk.epicgreen.erp.warehouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * WarehouseLocation entity
 * Represents specific locations within a warehouse (aisle, rack, shelf, bin)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "warehouse_locations",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_warehouse_location", columnNames = {"warehouse_id", "location_code"})
       },
       indexes = {
           @Index(name = "idx_warehouse_id", columnList = "warehouse_id"),
           @Index(name = "idx_location_code", columnList = "location_code")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseLocation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Warehouse reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false, foreignKey = @ForeignKey(name = "fk_warehouse_location_warehouse"))
    private Warehouse warehouse;
    
    /**
     * Location code (unique within warehouse, e.g., "A-01-02-05")
     */
    @NotBlank(message = "Location code is required")
    @Size(max = 30)
    @Column(name = "location_code", nullable = false, length = 30)
    private String locationCode;
    
    /**
     * Location name (optional display name)
     */
    @Size(max = 100)
    @Column(name = "location_name", length = 100)
    private String locationName;
    
    /**
     * Aisle identifier (e.g., "A", "B", "C")
     */
    @Size(max = 10)
    @Column(name = "aisle", length = 10)
    private String aisle;
    
    /**
     * Rack identifier (e.g., "01", "02")
     */
    @Size(max = 10)
    @Column(name = "rack", length = 10)
    private String rack;
    
    /**
     * Shelf identifier (e.g., "01", "02", "03")
     */
    @Size(max = 10)
    @Column(name = "shelf", length = 10)
    private String shelf;
    
    /**
     * Bin identifier (e.g., "01", "02")
     */
    @Size(max = 10)
    @Column(name = "bin", length = 10)
    private String bin;
    
    /**
     * Is active
     */
    @Column(name = "is_active")
    private Boolean isActive;
    
    /**
     * Created timestamp
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /**
     * Created by (user ID)
     */
    @Column(name = "created_by")
    private Long createdBy;
    
    /**
     * Check if active
     */
    @Transient
    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }
    
    /**
     * Get full location path (e.g., "Aisle A > Rack 01 > Shelf 02 > Bin 05")
     */
    @Transient
    public String getFullPath() {
        StringBuilder path = new StringBuilder();
        if (aisle != null) path.append("Aisle ").append(aisle);
        if (rack != null) {
            if (path.length() > 0) path.append(" > ");
            path.append("Rack ").append(rack);
        }
        if (shelf != null) {
            if (path.length() > 0) path.append(" > ");
            path.append("Shelf ").append(shelf);
        }
        if (bin != null) {
            if (path.length() > 0) path.append(" > ");
            path.append("Bin ").append(bin);
        }
        return path.toString();
    }
    
    /**
     * Get compact code representation (e.g., "A-01-02-05")
     */
    @Transient
    public String getCompactCode() {
        StringBuilder code = new StringBuilder();
        if (aisle != null) code.append(aisle);
        if (rack != null) {
            if (code.length() > 0) code.append("-");
            code.append(rack);
        }
        if (shelf != null) {
            if (code.length() > 0) code.append("-");
            code.append(shelf);
        }
        if (bin != null) {
            if (code.length() > 0) code.append("-");
            code.append(bin);
        }
        return code.toString();
    }
    
    @PrePersist
    protected void onCreate() {
        if (isActive == null) {
            isActive = true;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WarehouseLocation)) return false;
        WarehouseLocation that = (WarehouseLocation) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
