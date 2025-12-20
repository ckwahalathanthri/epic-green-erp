package lk.epicgreen.erp.warehouse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Inventory Entity - Complete version with all required fields
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "inventory", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"product_id", "warehouse_id"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Inventory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Product information (denormalized)
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_code", length = 50)
    private String productCode;
    
    @Column(name = "product_name", length = 200)
    private String productName;
    
    // Warehouse information (denormalized)
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;
    
    @Column(name = "warehouse_name", length = 200)
    private String warehouseName;
    
    // Quantity fields (Double for decimal quantities)
    @Column(name = "quantity_on_hand", precision = 15, scale = 3)
    private Double quantityOnHand;
    
    @Column(name = "quantity_available", precision = 15, scale = 3)
    private Double quantityAvailable;
    
    @Column(name = "quantity_reserved", precision = 15, scale = 3)
    private Double quantityReserved;
    
    @Column(name = "quantity_allocated", precision = 15, scale = 3)
    private Double quantityAllocated;
    
    @Column(name = "quantity_damaged", precision = 15, scale = 3)
    private Double quantityDamaged;
    
    @Column(name = "quantity_expired", precision = 15, scale = 3)
    private Double quantityExpired;
    
    // Stock level settings
    @Column(name = "reorder_level")
    private Integer reorderLevel;
    
    @Column(name = "reorder_quantity")
    private Integer reorderQuantity;
    
    @Column(name = "max_stock_level")
    private Integer maxStockLevel;
    
    @Column(name = "min_stock_level")
    private Integer minStockLevel;
    
    // Cost fields (Double for monetary values)
    @Column(name = "unit_cost", precision = 15, scale = 2)
    private Double unitCost;
    
    @Column(name = "average_cost", precision = 15, scale = 2)
    private Double averageCost;
    
    @Column(name = "last_cost", precision = 15, scale = 2)
    private Double lastCost;
    
    @Column(name = "total_value", precision = 15, scale = 2)
    private Double totalValue;
    
    // Status fields
    @Column(name = "status", length = 50)
    private String status; // ACTIVE, INACTIVE, DISCONTINUED
    
    @Column(name = "stock_status", length = 50)
    private String stockStatus; // IN_STOCK, LOW_STOCK, OUT_OF_STOCK, OVERSTOCK
    
    // Tracking fields
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    
    @Column(name = "last_movement_date")
    private LocalDateTime lastMovementDate;
    
    @Column(name = "last_stock_count_date")
    private LocalDateTime lastStockCountDate;
    
    @Column(name = "notes", length = 1000)
    private String notes;
    
    // Audit fields
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (lastUpdated == null) {
            lastUpdated = LocalDateTime.now();
        }
        if (quantityOnHand == null) {
            quantityOnHand = 0.0;
        }
        if (quantityAvailable == null) {
            quantityAvailable = 0.0;
        }
        if (quantityReserved == null) {
            quantityReserved = 0.0;
        }
        if (quantityAllocated == null) {
            quantityAllocated = 0.0;
        }
        if (quantityDamaged == null) {
            quantityDamaged = 0.0;
        }
        if (quantityExpired == null) {
            quantityExpired = 0.0;
        }
        if (status == null) {
            status = "ACTIVE";
        }
        if (stockStatus == null) {
            stockStatus = "IN_STOCK";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        lastUpdated = LocalDateTime.now();
    }
    
    /**
     * Calculate available quantity
     * Available = OnHand - Reserved - Allocated - Damaged - Expired
     */
    public Double calculateAvailableQuantity() {
        double onHand = quantityOnHand != null ? quantityOnHand : 0.0;
        double reserved = quantityReserved != null ? quantityReserved : 0.0;
        double allocated = quantityAllocated != null ? quantityAllocated : 0.0;
        double damaged = quantityDamaged != null ? quantityDamaged : 0.0;
        double expired = quantityExpired != null ? quantityExpired : 0.0;
        
        return onHand - reserved - allocated - damaged - expired;
    }
    
    /**
     * Update stock status based on quantity
     */
    public void updateStockStatus() {
        double available = calculateAvailableQuantity();
        Integer reorder = reorderLevel != null ? reorderLevel : 0;
        Integer max = maxStockLevel != null ? maxStockLevel : Integer.MAX_VALUE;
        
        if (available <= 0) {
            stockStatus = "OUT_OF_STOCK";
        } else if (available <= reorder) {
            stockStatus = "LOW_STOCK";
        } else if (available >= max) {
            stockStatus = "OVERSTOCK";
        } else {
            stockStatus = "IN_STOCK";
        }
    }
}
