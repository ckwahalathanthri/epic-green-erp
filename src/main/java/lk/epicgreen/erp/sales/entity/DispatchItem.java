package lk.epicgreen.erp.sales.entity;

import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.warehouse.entity.WarehouseLocation;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * DispatchItem entity
 * Represents individual items dispatched in a delivery
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "dispatch_items", indexes = {
    @Index(name = "idx_dispatch_id", columnList = "dispatch_id"),
    @Index(name = "idx_order_item_id", columnList = "order_item_id"),
    @Index(name = "idx_product_id", columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DispatchItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Dispatch note reference (header)
     */
    @NotNull(message = "Dispatch note is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispatch_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dispatch_item_dispatch"))
    private DispatchNote dispatch;
    
    /**
     * Sales order item reference
     */
    @NotNull(message = "Order item is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dispatch_item_order_item"))
    private SalesOrderItem orderItem;
    
    /**
     * Product reference
     */
    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dispatch_item_product"))
    private Product product;
    
    /**
     * Batch number
     */
    @Size(max = 50)
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    /**
     * Quantity dispatched
     */
    @NotNull(message = "Quantity dispatched is required")
    @Positive(message = "Quantity dispatched must be positive")
    @Column(name = "quantity_dispatched", nullable = false, precision = 15, scale = 3)
    private BigDecimal quantityDispatched;
    
    /**
     * Unit of measure
     */
    @NotNull(message = "UOM is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uom_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dispatch_item_uom"))
    private UnitOfMeasure uom;
    
    /**
     * Warehouse location (picked from)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", foreignKey = @ForeignKey(name = "fk_dispatch_item_location"))
    private WarehouseLocation location;
    
    /**
     * Remarks
     */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    /**
     * Get dispatch summary
     */
    @Transient
    public String getDispatchSummary() {
        StringBuilder summary = new StringBuilder();
        if (product != null) {
            summary.append(product.getProductName());
        }
        summary.append(" - ").append(quantityDispatched);
        if (uom != null) {
            summary.append(" ").append(uom.getUomCode());
        }
        if (batchNumber != null) {
            summary.append(" (Batch: ").append(batchNumber).append(")");
        }
        return summary.toString();
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

    public void setDispatchNote(DispatchNote dispatch) {
        this.dispatch=dispatch;
    }
}
