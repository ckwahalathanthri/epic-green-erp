package lk.epicgreen.erp.warehouse.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.product.entity.Product;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Inventory entity
 * Represents product stock at specific warehouse locations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "inventory",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_inventory_product_warehouse_location",
            columnNames = {"product_id", "warehouse_id", "location_id", "batch_number"})
    },
    indexes = {
        @Index(name = "idx_inventory_product", columnList = "product_id"),
        @Index(name = "idx_inventory_warehouse", columnList = "warehouse_id"),
        @Index(name = "idx_inventory_location", columnList = "location_id"),
        @Index(name = "idx_inventory_batch", columnList = "batch_number")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Product reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_inventory_product"))
    private Product product;
    
    /**
     * Warehouse reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false, foreignKey = @ForeignKey(name = "fk_inventory_warehouse"))
    private Warehouse warehouse;
    
    /**
     * Warehouse location reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", foreignKey = @ForeignKey(name = "fk_inventory_location"))
    private WarehouseLocation location;
    
    /**
     * Batch number (for batch tracked items)
     */
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    /**
     * Serial number (for serial tracked items)
     */
    @Column(name = "serial_number", length = 50)
    private String serialNumber;
    
    /**
     * Available quantity (on hand)
     */
    @Column(name = "available_quantity", precision = 15, scale = 3, nullable = false)
    private BigDecimal availableQuantity;
    
    /**
     * Allocated quantity (reserved for orders)
     */
    @Column(name = "allocated_quantity", precision = 15, scale = 3)
    private BigDecimal allocatedQuantity;
    
    /**
     * In transit quantity (being moved)
     */
    @Column(name = "in_transit_quantity", precision = 15, scale = 3)
    private BigDecimal inTransitQuantity;
    
    /**
     * Damaged quantity
     */
    @Column(name = "damaged_quantity", precision = 15, scale = 3)
    private BigDecimal damagedQuantity;
    
    /**
     * Unit of measurement
     */
    @Column(name = "unit", length = 10)
    private String unit;
    
    /**
     * Manufacturing date
     */
    @Column(name = "manufacturing_date")
    private LocalDate manufacturingDate;
    
    /**
     * Expiry date
     */
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    
    /**
     * Last stock count date
     */
    @Column(name = "last_stock_count_date")
    private LocalDate lastStockCountDate;
    
    /**
     * Cost per unit (for this batch/lot)
     */
    @Column(name = "cost_per_unit", precision = 15, scale = 2)
    private BigDecimal costPerUnit;
    
    /**
     * Currency
     */
    @Column(name = "currency", length = 10)
    private String currency;
    
    /**
     * Quality status (GOOD, DAMAGED, QUARANTINE, EXPIRED)
     */
    @Column(name = "quality_status", length = 20)
    private String qualityStatus;
    
    /**
     * Notes
     */
    @Column(name = "notes", length = 500)
    private String notes;
    
    /**
     * Gets total quantity (available + allocated + in transit)
     */
    @Transient
    public BigDecimal getTotalQuantity() {
        BigDecimal total = availableQuantity != null ? availableQuantity : BigDecimal.ZERO;
        if (allocatedQuantity != null) {
            total = total.add(allocatedQuantity);
        }
        if (inTransitQuantity != null) {
            total = total.add(inTransitQuantity);
        }
        return total;
    }
    
    /**
     * Gets unallocated quantity (available - allocated)
     */
    @Transient
    public BigDecimal getUnallocatedQuantity() {
        BigDecimal available = availableQuantity != null ? availableQuantity : BigDecimal.ZERO;
        BigDecimal allocated = allocatedQuantity != null ? allocatedQuantity : BigDecimal.ZERO;
        BigDecimal unallocated = available.subtract(allocated);
        return unallocated.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : unallocated;
    }
    
    /**
     * Gets total stock value
     */
    @Transient
    public BigDecimal getStockValue() {
        if (costPerUnit == null) {
            return BigDecimal.ZERO;
        }
        return getTotalQuantity().multiply(costPerUnit);
    }
    
    /**
     * Checks if inventory is expired
     */
    @Transient
    public boolean isExpired() {
        return expiryDate != null && LocalDate.now().isAfter(expiryDate);
    }
    
    /**
     * Checks if inventory is near expiry (within 30 days)
     */
    @Transient
    public boolean isNearExpiry() {
        if (expiryDate == null) {
            return false;
        }
        LocalDate thirtyDaysFromNow = LocalDate.now().plusDays(30);
        return !expiryDate.isBefore(LocalDate.now()) && expiryDate.isBefore(thirtyDaysFromNow);
    }
    
    /**
     * Gets days until expiry
     */
    @Transient
    public Long getDaysUntilExpiry() {
        if (expiryDate == null) {
            return null;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }
    
    /**
     * Checks if stock is available
     */
    @Transient
    public boolean isAvailable() {
        return availableQuantity != null && 
               availableQuantity.compareTo(BigDecimal.ZERO) > 0 &&
               "GOOD".equals(qualityStatus) &&
               !isExpired();
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (availableQuantity == null) {
            availableQuantity = BigDecimal.ZERO;
        }
        if (allocatedQuantity == null) {
            allocatedQuantity = BigDecimal.ZERO;
        }
        if (inTransitQuantity == null) {
            inTransitQuantity = BigDecimal.ZERO;
        }
        if (damagedQuantity == null) {
            damagedQuantity = BigDecimal.ZERO;
        }
        if (qualityStatus == null) {
            qualityStatus = "GOOD";
        }
        if (currency == null) {
            currency = "LKR";
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inventory)) return false;
        Inventory inventory = (Inventory) o;
        return id != null && id.equals(inventory.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
