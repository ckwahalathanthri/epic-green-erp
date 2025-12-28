package lk.epicgreen.erp.production.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * MaterialConsumption entity
 * Tracks actual material usage in production
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "material_consumption", indexes = {
    @Index(name = "idx_wo_id", columnList = "wo_id"),
    @Index(name = "idx_consumption_date", columnList = "consumption_date"),
    @Index(name = "idx_raw_material_id", columnList = "raw_material_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialConsumption {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Work order reference
     */
    @NotNull(message = "Work order is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wo_id", nullable = false, foreignKey = @ForeignKey(name = "fk_material_consumption_wo"))
    private WorkOrder workOrder;
    
    /**
     * Work order item reference (optional)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wo_item_id", foreignKey = @ForeignKey(name = "fk_material_consumption_wo_item"))
    private WorkOrderItem workOrderItem;
    
    /**
     * Raw material consumed
     */
    @NotNull(message = "Raw material is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raw_material_id", nullable = false, foreignKey = @ForeignKey(name = "fk_material_consumption_raw_material"))
    private Product rawMaterial;
    
    /**
     * Consumption date
     */
    @NotNull(message = "Consumption date is required")
    @Column(name = "consumption_date", nullable = false)
    private LocalDate consumptionDate;
    
    /**
     * Batch number
     */
    @Size(max = 50)
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    /**
     * Quantity consumed
     */
    @NotNull(message = "Quantity consumed is required")
    @Positive(message = "Quantity consumed must be positive")
    @Column(name = "quantity_consumed", nullable = false, precision = 15, scale = 3)
    private BigDecimal quantityConsumed;
    
    /**
     * Unit of measure
     */
    @NotNull(message = "UOM is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uom_id", nullable = false, foreignKey = @ForeignKey(name = "fk_material_consumption_uom"))
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
     * Warehouse (where material was taken from)
     */
    @NotNull(message = "Warehouse is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false, foreignKey = @ForeignKey(name = "fk_material_consumption_warehouse"))
    private Warehouse warehouse;
    
    /**
     * Consumed by (user)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumed_by", foreignKey = @ForeignKey(name = "fk_material_consumption_consumed_by"))
    private User consumedBy;
    
    /**
     * Remarks
     */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    /**
     * Created timestamp
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /**
     * Calculate total cost
     */
    @Transient
    public void calculateTotalCost() {
        if (quantityConsumed != null && unitCost != null) {
            totalCost = quantityConsumed.multiply(unitCost).setScale(2, java.math.RoundingMode.HALF_UP);
        }
    }
    
    /**
     * Get consumption summary
     */
    @Transient
    public String getConsumptionSummary() {
        StringBuilder summary = new StringBuilder();
        if (rawMaterial != null) {
            summary.append(rawMaterial.getProductName());
        }
        summary.append(" - ").append(quantityConsumed);
        if (uom != null) {
            summary.append(" ").append(uom.getUomCode());
        }
        if (batchNumber != null) {
            summary.append(" (Batch: ").append(batchNumber).append(")");
        }
        return summary.toString();
    }
    
    @PrePersist
    @PreUpdate
    protected void onSave() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (consumptionDate == null) {
            consumptionDate = LocalDate.now();
        }
        calculateTotalCost();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MaterialConsumption)) return false;
        MaterialConsumption that = (MaterialConsumption) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
