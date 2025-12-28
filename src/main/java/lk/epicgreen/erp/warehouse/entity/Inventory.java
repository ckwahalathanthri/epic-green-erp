package lk.epicgreen.erp.warehouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lk.epicgreen.erp.product.entity.Product;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Inventory entity
 * Represents real-time stock levels for products in warehouses
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "inventory",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_inventory", 
                           columnNames = {"warehouse_id", "product_id", "batch_number", "location_id"})
       },
       indexes = {
           @Index(name = "idx_warehouse_product", columnList = "warehouse_id, product_id"),
           @Index(name = "idx_batch_number", columnList = "batch_number"),
           @Index(name = "idx_expiry_date", columnList = "expiry_date")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Warehouse reference
     */
    @NotNull(message = "Warehouse is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false, foreignKey = @ForeignKey(name = "fk_inventory_warehouse"))
    private Warehouse warehouse;
    
    /**
     * Product reference
     */
    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_inventory_product"))
    private Product product;
    
    /**
     * Specific location within warehouse
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", foreignKey = @ForeignKey(name = "fk_inventory_location"))
    private WarehouseLocation location;
    
    /**
     * Batch number (for lot tracking)
     */
    @Size(max = 50)
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
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
     * Quantity available (free stock)
     */
    @PositiveOrZero(message = "Available quantity must be positive or zero")
    @Column(name = "quantity_available", precision = 15, scale = 3)
    private BigDecimal quantityAvailable;
    
    /**
     * Quantity reserved (allocated for orders but not yet issued)
     */
    @PositiveOrZero(message = "Reserved quantity must be positive or zero")
    @Column(name = "quantity_reserved", precision = 15, scale = 3)
    private BigDecimal quantityReserved;
    
    /**
     * Quantity ordered (on purchase orders, not yet received)
     */
    @PositiveOrZero(message = "Ordered quantity must be positive or zero")
    @Column(name = "quantity_ordered", precision = 15, scale = 3)
    private BigDecimal quantityOrdered;
    
    /**
     * Unit cost (average cost)
     */
    @PositiveOrZero(message = "Unit cost must be positive or zero")
    @Column(name = "unit_cost", precision = 15, scale = 2)
    private BigDecimal unitCost;
    
    /**
     * Last stock update date
     */
    @Column(name = "last_stock_date")
    private LocalDate lastStockDate;
    
    /**
     * Created timestamp
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /**
     * Updated timestamp
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * Get total quantity (available + reserved)
     */
    @Transient
    public BigDecimal getTotalQuantity() {
        BigDecimal available = quantityAvailable != null ? quantityAvailable : BigDecimal.ZERO;
        BigDecimal reserved = quantityReserved != null ? quantityReserved : BigDecimal.ZERO;
        return available.add(reserved);
    }
    
    /**
     * Get free quantity (available - reserved)
     */
    @Transient
    public BigDecimal getFreeQuantity() {
        BigDecimal available = quantityAvailable != null ? quantityAvailable : BigDecimal.ZERO;
        BigDecimal reserved = quantityReserved != null ? quantityReserved : BigDecimal.ZERO;
        return available.subtract(reserved);
    }
    
    /**
     * Get total inventory value
     */
    @Transient
    public BigDecimal getInventoryValue() {
        BigDecimal total = getTotalQuantity();
        BigDecimal cost = unitCost != null ? unitCost : BigDecimal.ZERO;
        return total.multiply(cost);
    }
    
    /**
     * Check if stock is expired
     */
    @Transient
    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDate.now());
    }
    
    /**
     * Check if stock is expiring soon (within days)
     */
    @Transient
    public boolean isExpiringSoon(int days) {
        if (expiryDate == null) {
            return false;
        }
        LocalDate warningDate = LocalDate.now().plusDays(days);
        return expiryDate.isBefore(warningDate) && !isExpired();
    }
    
    /**
     * Check if stock is available for allocation
     */
    @Transient
    public boolean hasAvailableStock() {
        return getFreeQuantity().compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Check if can allocate quantity
     */
    @Transient
    public boolean canAllocate(BigDecimal quantity) {
        if (quantity == null) {
            return false;
        }
        return getFreeQuantity().compareTo(quantity) >= 0;
    }
    
    /**
     * Reserve quantity
     */
    public void reserve(BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (!canAllocate(quantity)) {
            throw new IllegalStateException("Insufficient available quantity");
        }
        quantityReserved = (quantityReserved != null ? quantityReserved : BigDecimal.ZERO).add(quantity);
    }
    
    /**
     * Release reserved quantity
     */
    public void releaseReservation(BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        BigDecimal currentReserved = quantityReserved != null ? quantityReserved : BigDecimal.ZERO;
        if (currentReserved.compareTo(quantity) < 0) {
            throw new IllegalStateException("Cannot release more than reserved");
        }
        quantityReserved = currentReserved.subtract(quantity);
    }
    
    /**
     * Issue stock (reduce available quantity)
     */
    public void issue(BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        BigDecimal currentAvailable = quantityAvailable != null ? quantityAvailable : BigDecimal.ZERO;
        if (currentAvailable.compareTo(quantity) < 0) {
            throw new IllegalStateException("Insufficient available quantity");
        }
        quantityAvailable = currentAvailable.subtract(quantity);
        lastStockDate = LocalDate.now();
    }
    
    /**
     * Receive stock (increase available quantity)
     */
    public void receive(BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        quantityAvailable = (quantityAvailable != null ? quantityAvailable : BigDecimal.ZERO).add(quantity);
        lastStockDate = LocalDate.now();
    }
    
    @PrePersist
    protected void onCreate() {
        if (quantityAvailable == null) {
            quantityAvailable = BigDecimal.ZERO;
        }
        if (quantityReserved == null) {
            quantityReserved = BigDecimal.ZERO;
        }
        if (quantityOrdered == null) {
            quantityOrdered = BigDecimal.ZERO;
        }
        if (unitCost == null) {
            unitCost = BigDecimal.ZERO;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
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
