package lk.epicgreen.erp.production.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * WorkOrderItem entity
 * Represents raw materials to be consumed in a work order
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "work_order_items", indexes = {
    @Index(name = "idx_wo_id", columnList = "wo_id"),
    @Index(name = "idx_raw_material_id", columnList = "raw_material_id"),
    @Index(name = "idx_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkOrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Work order reference (header)
     */
    @NotNull(message = "Work order is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wo_id", nullable = false, foreignKey = @ForeignKey(name = "fk_work_order_item_wo"))
    private WorkOrder workOrder;
    
    /**
     * Raw material
     */
    @NotNull(message = "Raw material is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raw_material_id", nullable = false, foreignKey = @ForeignKey(name = "fk_work_order_item_raw_material"))
    private Product rawMaterial;
    
    /**
     * Planned quantity
     */
    @NotNull(message = "Planned quantity is required")
    @Positive(message = "Planned quantity must be positive")
    @Column(name = "planned_quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal plannedQuantity;
    
    /**
     * Consumed quantity
     */
    @PositiveOrZero(message = "Consumed quantity must be positive or zero")
    @Column(name = "consumed_quantity", precision = 15, scale = 3)
    private BigDecimal consumedQuantity;
    
    /**
     * Unit of measure
     */
    @NotNull(message = "UOM is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uom_id", nullable = false, foreignKey = @ForeignKey(name = "fk_work_order_item_uom"))
    private UnitOfMeasure uom;
    
    /**
     * Unit cost
     */
    @PositiveOrZero(message = "Unit cost must be positive or zero")
    @Column(name = "unit_cost", precision = 15, scale = 2)
    private BigDecimal unitCost;
    
    /**
     * Total cost
     */
    @PositiveOrZero(message = "Total cost must be positive or zero")
    @Column(name = "total_cost", precision = 15, scale = 2)
    private BigDecimal totalCost;
    
    /**
     * Status (PENDING, ISSUED, CONSUMED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Issued from warehouse
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issued_from_warehouse_id", foreignKey = @ForeignKey(name = "fk_work_order_item_warehouse"))
    private Warehouse issuedFromWarehouse;
    
    /**
     * Issued timestamp
     */
    @Column(name = "issued_at")
    private LocalDateTime issuedAt;
    
    /**
     * Issued by (user)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issued_by", foreignKey = @ForeignKey(name = "fk_work_order_item_issued_by"))
    private User issuedBy;
    
    /**
     * Check if pending
     */
    @Transient
    public boolean isPending() {
        return "PENDING".equals(status);
    }
    
    /**
     * Check if issued
     */
    @Transient
    public boolean isIssued() {
        return "ISSUED".equals(status);
    }
    
    /**
     * Check if consumed
     */
    @Transient
    public boolean isConsumed() {
        return "CONSUMED".equals(status);
    }
    
    /**
     * Get remaining quantity to consume
     */
    @Transient
    public BigDecimal getRemainingQuantity() {
        BigDecimal planned = plannedQuantity != null ? plannedQuantity : BigDecimal.ZERO;
        BigDecimal consumed = consumedQuantity != null ? consumedQuantity : BigDecimal.ZERO;
        return planned.subtract(consumed);
    }
    
    /**
     * Get consumption percentage
     */
    @Transient
    public BigDecimal getConsumptionPercentage() {
        if (plannedQuantity == null || plannedQuantity.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal consumed = consumedQuantity != null ? consumedQuantity : BigDecimal.ZERO;
        return consumed.divide(plannedQuantity, 2, java.math.RoundingMode.HALF_UP)
                       .multiply(new BigDecimal("100"));
    }
    
    /**
     * Check if fully consumed
     */
    @Transient
    public boolean isFullyConsumed() {
        return getRemainingQuantity().compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * Check if over-consumed
     */
    @Transient
    public boolean isOverConsumed() {
        return getRemainingQuantity().compareTo(BigDecimal.ZERO) < 0;
    }
    
    /**
     * Calculate total cost
     */
    @Transient
    public void calculateTotalCost() {
        if (plannedQuantity != null && unitCost != null) {
            totalCost = plannedQuantity.multiply(unitCost).setScale(2, java.math.RoundingMode.HALF_UP);
        }
    }
    
    /**
     * Issue material
     */
    public void issue(Warehouse warehouse, User user) {
        if (!isPending()) {
            throw new IllegalStateException("Only pending items can be issued");
        }
        this.status = "ISSUED";
        this.issuedFromWarehouse = warehouse;
        this.issuedBy = user;
        this.issuedAt = LocalDateTime.now();
    }
    
    /**
     * Mark as consumed
     */
    public void markAsConsumed() {
        if (!isIssued()) {
            throw new IllegalStateException("Only issued items can be consumed");
        }
        this.status = "CONSUMED";
    }
    
    @PrePersist
    @PreUpdate
    protected void onSave() {
        if (status == null) {
            status = "PENDING";
        }
        if (consumedQuantity == null) {
            consumedQuantity = BigDecimal.ZERO;
        }
        calculateTotalCost();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkOrderItem)) return false;
        WorkOrderItem that = (WorkOrderItem) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
