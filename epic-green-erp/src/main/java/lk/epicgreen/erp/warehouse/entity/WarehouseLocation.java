package lk.epicgreen.erp.warehouse.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * WarehouseLocation entity
 * Represents specific storage locations within a warehouse
 * (e.g., Zone A, Rack 5, Shelf 3, Bin 12)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "warehouse_locations", 
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_warehouse_location", 
            columnNames = {"warehouse_id", "location_code"})
    },
    indexes = {
        @Index(name = "idx_warehouse_location_warehouse", columnList = "warehouse_id"),
        @Index(name = "idx_warehouse_location_code", columnList = "location_code"),
        @Index(name = "idx_warehouse_location_type", columnList = "location_type")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseLocation extends AuditEntity {
    
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
     * Location code (unique within warehouse)
     */
    @Column(name = "location_code", nullable = false, length = 50)
    private String locationCode;
    
    /**
     * Location name
     */
    @Column(name = "location_name", nullable = false, length = 100)
    private String locationName;
    
    /**
     * Location type (ZONE, AISLE, RACK, SHELF, BIN, PALLET)
     */
    @Column(name = "location_type", nullable = false, length = 20)
    private String locationType;
    
    /**
     * Parent location (for hierarchical structure)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "fk_warehouse_location_parent"))
    private WarehouseLocation parent;
    
    /**
     * Child locations
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<WarehouseLocation> children = new HashSet<>();
    
    /**
     * Zone (e.g., A, B, C)
     */
    @Column(name = "zone", length = 20)
    private String zone;
    
    /**
     * Aisle number
     */
    @Column(name = "aisle", length = 20)
    private String aisle;
    
    /**
     * Rack number
     */
    @Column(name = "rack", length = 20)
    private String rack;
    
    /**
     * Shelf/Level number
     */
    @Column(name = "shelf", length = 20)
    private String shelf;
    
    /**
     * Bin number
     */
    @Column(name = "bin", length = 20)
    private String bin;
    
    /**
     * Capacity (in units suitable for location type)
     */
    @Column(name = "capacity")
    private Integer capacity;
    
    /**
     * Capacity unit (PALLETS, BOXES, KG, CBM)
     */
    @Column(name = "capacity_unit", length = 10)
    private String capacityUnit;
    
    /**
     * Is active
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    /**
     * Is available (not blocked/reserved)
     */
    @Column(name = "is_available")
    private Boolean isAvailable;
    
    /**
     * Special storage conditions (COOL, DRY, REFRIGERATED)
     */
    @Column(name = "storage_conditions", length = 255)
    private String storageConditions;
    
    /**
     * Temperature range (e.g., "15-25°C")
     */
    @Column(name = "temperature_range", length = 50)
    private String temperatureRange;
    
    /**
     * Humidity range (e.g., "40-60%")
     */
    @Column(name = "humidity_range", length = 50)
    private String humidityRange;
    
    /**
     * Notes
     */
    @Column(name = "notes", length = 500)
    private String notes;
    
    /**
     * Inventory items at this location
     */
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Inventory> inventoryItems = new HashSet<>();
    
    /**
     * Gets full location path
     */
    @Transient
    public String getFullLocationPath() {
        StringBuilder path = new StringBuilder();
        
        if (parent != null) {
            path.append(parent.getFullLocationPath()).append(" > ");
        }
        
        path.append(locationName);
        
        // Add detailed location info if available
        if (zone != null || aisle != null || rack != null || shelf != null || bin != null) {
            path.append(" (");
            if (zone != null) path.append("Zone:").append(zone).append(" ");
            if (aisle != null) path.append("Aisle:").append(aisle).append(" ");
            if (rack != null) path.append("Rack:").append(rack).append(" ");
            if (shelf != null) path.append("Shelf:").append(shelf).append(" ");
            if (bin != null) path.append("Bin:").append(bin);
            path.append(")");
        }
        
        return path.toString().trim();
    }
    
    /**
     * Gets simple location code
     */
    @Transient
    public String getSimpleLocationCode() {
        StringBuilder code = new StringBuilder();
        if (zone != null) code.append(zone).append("-");
        if (aisle != null) code.append("A").append(aisle).append("-");
        if (rack != null) code.append("R").append(rack).append("-");
        if (shelf != null) code.append("S").append(shelf).append("-");
        if (bin != null) code.append("B").append(bin);
        
        String result = code.toString();
        return result.endsWith("-") ? result.substring(0, result.length() - 1) : result;
    }
    
    /**
     * Checks if this is a leaf location (can store inventory)
     */
    @Transient
    public boolean isLeafLocation() {
        return children == null || children.isEmpty();
    }
    
    /**
     * Adds a child location
     */
    public void addChild(WarehouseLocation child) {
        child.setParent(this);
        children.add(child);
    }
    
    /**
     * Removes a child location
     */
    public void removeChild(WarehouseLocation child) {
        children.remove(child);
        child.setParent(null);
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (isActive == null) {
            isActive = true;
        }
        if (isAvailable == null) {
            isAvailable = true;
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
