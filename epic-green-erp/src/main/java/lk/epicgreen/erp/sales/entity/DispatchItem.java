package lk.epicgreen.erp.sales.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.warehouse.entity.WarehouseLocation;
import lombok.*;

import java.math.BigDecimal;

/**
 * DispatchItem entity
 * Represents line items in dispatch notes
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "dispatch_items", indexes = {
    @Index(name = "idx_dispatch_item_dispatch", columnList = "dispatch_note_id"),
    @Index(name = "idx_dispatch_item_order_item", columnList = "order_item_id"),
    @Index(name = "idx_dispatch_item_product", columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DispatchItem extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Dispatch note reference (header)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispatch_note_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dispatch_item_dispatch"))
    private DispatchNote dispatchNote;
    
    /**
     * Sales order item reference (optional)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", foreignKey = @ForeignKey(name = "fk_dispatch_item_order_item"))
    private SalesOrderItem orderItem;
    
    /**
     * Product reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dispatch_item_product"))
    private Product product;
    
    /**
     * Warehouse location (from where picked)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", foreignKey = @ForeignKey(name = "fk_dispatch_item_location"))
    private WarehouseLocation location;
    
    /**
     * Item description (optional override)
     */
    @Column(name = "item_description", length = 500)
    private String itemDescription;
    
    /**
     * Batch number
     */
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    /**
     * Serial number
     */
    @Column(name = "serial_number", length = 50)
    private String serialNumber;
    
    /**
     * Ordered quantity (from sales order)
     */
    @Column(name = "ordered_quantity", precision = 15, scale = 3)
    private BigDecimal orderedQuantity;
    
    /**
     * Dispatched quantity (actual dispatched)
     */
    @Column(name = "dispatched_quantity", precision = 15, scale = 3, nullable = false)
    private BigDecimal dispatchedQuantity;
    
    /**
     * Unit of measurement
     */
    @Column(name = "unit", nullable = false, length = 10)
    private String unit;
    
    /**
     * Unit price (for reference)
     */
    @Column(name = "unit_price", precision = 15, scale = 2)
    private BigDecimal unitPrice;
    
    /**
     * Total value (dispatched quantity * unit price)
     */
    @Column(name = "total_value", precision = 15, scale = 2)
    private BigDecimal totalValue;
    
    /**
     * Notes
     */
    @Column(name = "notes", length = 500)
    private String notes;
    
    /**
     * Calculates total value
     */
    @Transient
    public void calculateTotalValue() {
        if (dispatchedQuantity == null || unitPrice == null) {
            totalValue = BigDecimal.ZERO;
            return;
        }
        
        totalValue = dispatchedQuantity.multiply(unitPrice);
    }
    
    /**
     * Gets variance quantity (dispatched - ordered)
     */
    @Transient
    public BigDecimal getVarianceQuantity() {
        if (dispatchedQuantity == null || orderedQuantity == null) {
            return BigDecimal.ZERO;
        }
        return dispatchedQuantity.subtract(orderedQuantity);
    }
    
    /**
     * Checks if fully dispatched (compared to order)
     */
    @Transient
    public boolean isFullyDispatched() {
        if (orderedQuantity == null || dispatchedQuantity == null) {
            return false;
        }
        return dispatchedQuantity.compareTo(orderedQuantity) >= 0;
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        
        // Calculate total value
        calculateTotalValue();
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Recalculate total value
        calculateTotalValue();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DispatchItem)) return false;
        DispatchItem that = (DispatchItem) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
