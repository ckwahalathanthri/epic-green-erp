package lk.epicgreen.erp.production.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lk.epicgreen.erp.warehouse.entity.WarehouseLocation;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * MaterialConsumption entity
 * Tracks raw materials consumed during production
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "material_consumptions", indexes = {
    @Index(name = "idx_mat_cons_wo", columnList = "work_order_id"),
    @Index(name = "idx_mat_cons_product", columnList = "product_id"),
    @Index(name = "idx_mat_cons_date", columnList = "consumption_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialConsumption extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Work order reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_mat_cons_work_order"))
    private WorkOrder workOrder;
    
    /**
     * Product (raw material) consumed
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_mat_cons_product"))
    private Product product;
    
    /**
     * Warehouse from which material was taken
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", foreignKey = @ForeignKey(name = "fk_mat_cons_warehouse"))
    private Warehouse warehouse;
    
    /**
     * Warehouse location
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", foreignKey = @ForeignKey(name = "fk_mat_cons_location"))
    private WarehouseLocation location;
    
    /**
     * Consumption date
     */
    @Column(name = "consumption_date", nullable = false)
    private LocalDate consumptionDate;
    
    /**
     * Consumption timestamp
     */
    @Column(name = "consumption_timestamp")
    private LocalDateTime consumptionTimestamp;
    
    /**
     * Batch number of material consumed
     */
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    /**
     * Serial number (for serialized items)
     */
    @Column(name = "serial_number", length = 50)
    private String serialNumber;
    
    /**
     * Planned quantity (from BOM)
     */
    @Column(name = "planned_quantity", precision = 15, scale = 3)
    private BigDecimal plannedQuantity;
    
    /**
     * Consumed quantity (actual used)
     */
    @Column(name = "consumed_quantity", precision = 15, scale = 3, nullable = false)
    private BigDecimal consumedQuantity;
    
    /**
     * Wastage quantity
     */
    @Column(name = "wastage_quantity", precision = 15, scale = 3)
    private BigDecimal wastageQuantity;
    
    /**
     * Unit of measurement
     */
    @Column(name = "unit", nullable = false, length = 10)
    private String unit;
    
    /**
     * Cost per unit
     */
    @Column(name = "cost_per_unit", precision = 15, scale = 2)
    private BigDecimal costPerUnit;
    
    /**
     * Total cost (consumed quantity * cost per unit)
     */
    @Column(name = "total_cost", precision = 15, scale = 2)
    private BigDecimal totalCost;
    
    /**
     * Currency
     */
    @Column(name = "currency", length = 10)
    private String currency;
    
    /**
     * Consumed by (operator/worker)
     */
    @Column(name = "consumed_by", length = 50)
    private String consumedBy;
    
    /**
     * Is posted to inventory
     */
    @Column(name = "is_posted")
    private Boolean isPosted;
    
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
        if (consumedQuantity == null || costPerUnit == null) {
            totalCost = BigDecimal.ZERO;
            return;
        }
        
        totalCost = consumedQuantity.multiply(costPerUnit);
    }
    
    /**
     * Gets variance quantity (consumed - planned)
     */
    @Transient
    public BigDecimal getVarianceQuantity() {
        if (consumedQuantity == null || plannedQuantity == null) {
            return BigDecimal.ZERO;
        }
        return consumedQuantity.subtract(plannedQuantity);
    }
    
    /**
     * Gets variance percentage
     */
    @Transient
    public BigDecimal getVariancePercentage() {
        if (plannedQuantity == null || plannedQuantity.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal variance = getVarianceQuantity();
        return variance.divide(plannedQuantity, 4, RoundingMode.HALF_UP)
                      .multiply(new BigDecimal("100"));
    }
    
    /**
     * Checks if consumption is over planned
     */
    @Transient
    public boolean isOverConsumption() {
        return getVarianceQuantity().compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Checks if consumption is under planned
     */
    @Transient
    public boolean isUnderConsumption() {
        return getVarianceQuantity().compareTo(BigDecimal.ZERO) < 0;
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (consumptionTimestamp == null) {
            consumptionTimestamp = LocalDateTime.now();
        }
        if (wastageQuantity == null) {
            wastageQuantity = BigDecimal.ZERO;
        }
        if (isPosted == null) {
            isPosted = false;
        }
        if (currency == null) {
            currency = "LKR";
        }
        
        // Calculate total cost
        calculateTotalCost();
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Recalculate total cost
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
