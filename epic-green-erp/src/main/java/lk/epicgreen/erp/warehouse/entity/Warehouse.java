package lk.epicgreen.erp.warehouse.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Warehouse entity
 * Represents physical warehouse/storage facilities
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "warehouses", indexes = {
    @Index(name = "idx_warehouse_code", columnList = "warehouse_code"),
    @Index(name = "idx_warehouse_name", columnList = "warehouse_name"),
    @Index(name = "idx_warehouse_type", columnList = "warehouse_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Warehouse code (unique identifier)
     */
    @Column(name = "warehouse_code", nullable = false, unique = true, length = 20)
    private String warehouseCode;
    
    /**
     * Warehouse name
     */
    @Column(name = "warehouse_name", nullable = false, length = 100)
    private String warehouseName;
    
    /**
     * Warehouse type (MAIN, BRANCH, FACTORY, DISTRIBUTION, COLD_STORAGE)
     */
    @Column(name = "warehouse_type", nullable = false, length = 30)
    private String warehouseType;
    
    /**
     * Description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * Manager name
     */
    @Column(name = "manager_name", length = 100)
    private String managerName;
    
    /**
     * Contact person
     */
    @Column(name = "contact_person", length = 100)
    private String contactPerson;
    
    /**
     * Email
     */
    @Column(name = "email", length = 100)
    private String email;
    
    /**
     * Phone number
     */
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    /**
     * Mobile number
     */
    @Column(name = "mobile_number", length = 20)
    private String mobileNumber;
    
    /**
     * Address line 1
     */
    @Column(name = "address_line1", length = 255)
    private String addressLine1;
    
    /**
     * Address line 2
     */
    @Column(name = "address_line2", length = 255)
    private String addressLine2;
    
    /**
     * City
     */
    @Column(name = "city", length = 100)
    private String city;
    
    /**
     * State/Province
     */
    @Column(name = "state", length = 100)
    private String state;
    
    /**
     * Postal code
     */
    @Column(name = "postal_code", length = 20)
    private String postalCode;
    
    /**
     * Country
     */
    @Column(name = "country", length = 100)
    private String country;
    
    /**
     * Is main warehouse (primary/central warehouse)
     */
    @Column(name = "is_main_warehouse")
    private Boolean isMainWarehouse;
    
    /**
     * Is active
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    /**
     * Operating hours
     */
    @Column(name = "operating_hours", length = 255)
    private String operatingHours;
    
    /**
     * Capacity (in cubic meters or square meters)
     */
    @Column(name = "capacity")
    private Integer capacity;
    
    /**
     * Capacity unit (SQM, CBM)
     */
    @Column(name = "capacity_unit", length = 10)
    private String capacityUnit;
    
    /**
     * Facilities/Features (JSON or text)
     */
    @Column(name = "facilities", columnDefinition = "TEXT")
    private String facilities;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Warehouse locations
     */
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<WarehouseLocation> locations = new HashSet<>();
    
    /**
     * Inventory items in this warehouse
     */
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Inventory> inventoryItems = new HashSet<>();
    
    /**
     * Gets full address
     */
    @Transient
    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        if (addressLine1 != null) address.append(addressLine1);
        if (addressLine2 != null) {
            if (address.length() > 0) address.append(", ");
            address.append(addressLine2);
        }
        if (city != null) {
            if (address.length() > 0) address.append(", ");
            address.append(city);
        }
        if (state != null) {
            if (address.length() > 0) address.append(", ");
            address.append(state);
        }
        if (postalCode != null) {
            if (address.length() > 0) address.append(" ");
            address.append(postalCode);
        }
        if (country != null) {
            if (address.length() > 0) address.append(", ");
            address.append(country);
        }
        return address.toString();
    }
    
    /**
     * Adds a location to the warehouse
     */
    public void addLocation(WarehouseLocation location) {
        location.setWarehouse(this);
        locations.add(location);
    }
    
    /**
     * Removes a location from the warehouse
     */
    public void removeLocation(WarehouseLocation location) {
        locations.remove(location);
        location.setWarehouse(null);
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (isActive == null) {
            isActive = true;
        }
        if (isMainWarehouse == null) {
            isMainWarehouse = false;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Warehouse)) return false;
        Warehouse warehouse = (Warehouse) o;
        return id != null && id.equals(warehouse.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
