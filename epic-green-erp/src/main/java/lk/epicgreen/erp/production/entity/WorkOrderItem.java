package lk.epicgreen.erp.production.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.product.entity.Product;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * WorkOrderItem entity
 * Represents products to be produced in a work order
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "work_order_items", indexes = {
    @Index(name = "idx_wo_item_wo", columnList = "work_order_id"),
    @Index(name = "idx_wo_item_product", columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkOrderItem extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Work order reference (header)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_wo_item_work_order"))
    private WorkOrder workOrder;
    
    /**
     * Product to produce (finished good)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_wo_item_product"))
    private Product product;
    
    /**
     * Item description (optional override)
     */
    @Column(name = "item_description", length = 500)
    private String itemDescription;
    
    /**
     * Planned quantity to produce
     */
    @Column(name = "planned_quantity", precision = 15, scale = 3, nullable = false)
    private BigDecimal plannedQuantity;
    
    /**
     * Produced quantity (actual produced)
     */
    @Column(name = "produced_quantity", precision = 15, scale = 3)
    private BigDecimal producedQuantity;
    
    /**
     * Good quantity (passed quality check)
     */
    @Column(name = "good_quantity", precision = 15, scale = 3)
    private BigDecimal goodQuantity;
    
    /**
     * Rejected quantity (failed quality check)
     */
    @Column(name = "rejected_quantity", precision = 15, scale = 3)
    private BigDecimal rejectedQuantity;
    
    /**
     * Unit of measurement
     */
    @Column(name = "unit", nullable = false, length = 10)
    private String unit;
    
    /**
     * Unit cost (cost per unit produced)
     */
    @Column(name = "unit_cost", precision = 15, scale = 2)
    private BigDecimal unitCost;
    
    /**
     * Total cost (produced quantity * unit cost)
     */
    @Column(name = "total_cost", precision = 15, scale = 2)
    private BigDecimal totalCost;
    
    /**
     * Currency
     */
    @Column(name = "currency", length = 10)
    private String currency;
    
    /**
     * Notes
     */
    @Column(name = "notes", length = 500)
    private String notes;
    
    /**
     * Calculates total cost
     */
    @Transient
    public void calculateTotalCost() {
        if (producedQuantity == null || unitCost == null) {
            totalCost = BigDecimal.ZERO;
            return;
        }
        
        totalCost = producedQuantity.multiply(unitCost);
    }
    
    /**
     * Gets completion percentage
     */
    @Transient
    public BigDecimal getCompletionPercentage() {
        if (plannedQuantity == null || plannedQuantity.compareTo(BigDecimal.ZERO) == 0 || producedQuantity == null) {
            return BigDecimal.ZERO;
        }
        return producedQuantity.divide(plannedQuantity, 4, RoundingMode.HALF_UP)
                              .multiply(new BigDecimal("100"));
    }
    
    /**
     * Gets quality pass percentage
     */
    @Transient
    public BigDecimal getQualityPassPercentage() {
        if (producedQuantity == null || producedQuantity.compareTo(BigDecimal.ZERO) == 0 || goodQuantity == null) {
            return BigDecimal.ZERO;
        }
        return goodQuantity.divide(producedQuantity, 4, RoundingMode.HALF_UP)
                          .multiply(new BigDecimal("100"));
    }
    
    /**
     * Gets pending quantity
     */
    @Transient
    public BigDecimal getPendingQuantity() {
        if (plannedQuantity == null || producedQuantity == null) {
            return plannedQuantity != null ? plannedQuantity : BigDecimal.ZERO;
        }
        BigDecimal pending = plannedQuantity.subtract(producedQuantity);
        return pending.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : pending;
    }
    
    /**
     * Checks if fully produced
     */
    @Transient
    public boolean isFullyProduced() {
        if (plannedQuantity == null || producedQuantity == null) {
            return false;
        }
        return producedQuantity.compareTo(plannedQuantity) >= 0;
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (producedQuantity == null) {
            producedQuantity = BigDecimal.ZERO;
        }
        if (goodQuantity == null) {
            goodQuantity = BigDecimal.ZERO;
        }
        if (rejectedQuantity == null) {
            rejectedQuantity = BigDecimal.ZERO;
        }
        if (currency == null) {
            currency = "LKR";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Recalculate total cost
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
