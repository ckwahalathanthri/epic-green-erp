package lk.epicgreen.erp.warehouse.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Warehouse entity
 * Represents physical warehouse locations for storing inventory
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "warehouses", indexes = {
    @Index(name = "idx_warehouse_code", columnList = "warehouse_code"),
    @Index(name = "idx_warehouse_type", columnList = "warehouse_type"),
    @Index(name = "idx_is_active", columnList = "is_active")
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
    @NotBlank(message = "Warehouse code is required")
    @Size(max = 20)
    @Column(name = "warehouse_code", nullable = false, unique = true, length = 20)
    private String warehouseCode;
    
    /**
     * Warehouse name
     */
    @NotBlank(message = "Warehouse name is required")
    @Size(max = 100)
    @Column(name = "warehouse_name", nullable = false, length = 100)
    private String warehouseName;
    
    /**
     * Warehouse type (RAW_MATERIAL, FINISHED_GOODS, MIXED)
     */
    @NotBlank(message = "Warehouse type is required")
    @Column(name = "warehouse_type", nullable = false, length = 20)
    private String warehouseType;
    
    /**
     * Address line 1
     */
    @Size(max = 200)
    @Column(name = "address_line1", length = 200)
    private String addressLine1;
    
    /**
     * Address line 2
     */
    @Size(max = 200)
    @Column(name = "address_line2", length = 200)
    private String addressLine2;
    
    /**
     * City
     */
    @Size(max = 100)
    @Column(name = "city", length = 100)
    private String city;
    
    /**
     * State
     */
    @Size(max = 100)
    @Column(name = "state", length = 100)
    private String state;
    
    /**
     * Postal code
     */
    @Size(max = 20)
    @Column(name = "postal_code", length = 20)
    private String postalCode;
    
    /**
     * Warehouse manager (user reference)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", foreignKey = @ForeignKey(name = "fk_warehouse_manager"))
    private User manager;
    
    /**
     * Contact number
     */
    @Size(max = 20)
    @Column(name = "contact_number", length = 20)
    private String contactNumber;
    
    /**
     * Is active
     */
    @Column(name = "is_active")
    private Boolean isActive;
    
    /**
     * Warehouse locations (bins, racks, shelves)
     */
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    @Builder.Default
    private List<WarehouseLocation> locations = new ArrayList<>();
    
    /**
     * Inventory records
     */
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Inventory> inventories = new ArrayList<>();
    
    /**
     * Check if active
     */
    @Transient
    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }
    
    /**
     * Check if warehouse is for raw materials
     */
    @Transient
    public boolean isRawMaterialWarehouse() {
        return "RAW_MATERIAL".equals(warehouseType);
    }
    
    /**
     * Check if warehouse is for finished goods
     */
    @Transient
    public boolean isFinishedGoodsWarehouse() {
        return "FINISHED_GOODS".equals(warehouseType);
    }
    
    /**
     * Check if warehouse is mixed type
     */
    @Transient
    public boolean isMixedWarehouse() {
        return "MIXED".equals(warehouseType);
    }
    
    /**
     * Get full address
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
        return address.toString();
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (isActive == null) {
            isActive = true;
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
