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
 * ProductionOutput entity
 * Represents finished goods produced from work orders
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "production_outputs", indexes = {
    @Index(name = "idx_prod_output_wo", columnList = "work_order_id"),
    @Index(name = "idx_prod_output_product", columnList = "product_id"),
    @Index(name = "idx_prod_output_date", columnList = "output_date"),
    @Index(name = "idx_prod_output_batch", columnList = "batch_number")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionOutput extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Work order reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_prod_output_work_order"))
    private WorkOrder workOrder;
    
    /**
     * Work order item reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_item_id", foreignKey = @ForeignKey(name = "fk_prod_output_wo_item"))
    private WorkOrderItem workOrderItem;
    
    /**
     * Product produced (finished good)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_prod_output_product"))
    private Product product;
    
    /**
     * Warehouse where output is stored
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", foreignKey = @ForeignKey(name = "fk_prod_output_warehouse"))
    private Warehouse warehouse;
    
    /**
     * Warehouse location
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", foreignKey = @ForeignKey(name = "fk_prod_output_location"))
    private WarehouseLocation location;
    
    /**
     * Output date
     */
    @Column(name = "output_date", nullable = false)
    private LocalDate outputDate;
    
    /**
     * Output timestamp
     */
    @Column(name = "output_timestamp")
    private LocalDateTime outputTimestamp;
    
    /**
     * Batch number assigned to output
     */
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    /**
     * Serial number (for serialized items)
     */
    @Column(name = "serial_number", length = 50)
    private String serialNumber;
    
    /**
     * Quantity produced
     */
    @Column(name = "quantity_produced", precision = 15, scale = 3, nullable = false)
    private BigDecimal quantityProduced;
    
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
     * Rework quantity (needs rework)
     */
    @Column(name = "rework_quantity", precision = 15, scale = 3)
    private BigDecimal reworkQuantity;
    
    /**
     * Unit of measurement
     */
    @Column(name = "unit", nullable = false, length = 10)
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
     * Unit cost (cost to produce one unit)
     */
    @Column(name = "unit_cost", precision = 15, scale = 2)
    private BigDecimal unitCost;
    
    /**
     * Total cost (quantity * unit cost)
     */
    @Column(name = "total_cost", precision = 15, scale = 2)
    private BigDecimal totalCost;
    
    /**
     * Currency
     */
    @Column(name = "currency", length = 10)
    private String currency;
    
    /**
     * Quality status (PENDING, GOOD, ACCEPTABLE, POOR, REJECTED)
     */
    @Column(name = "quality_status", length = 20)
    private String qualityStatus;
    
    /**
     * Quality remarks
     */
    @Column(name = "quality_remarks", columnDefinition = "TEXT")
    private String qualityRemarks;
    
    /**
     * Quality inspector
     */
    @Column(name = "quality_inspector", length = 50)
    private String qualityInspector;
    
    /**
     * Quality check date
     */
    @Column(name = "quality_check_date")
    private LocalDate qualityCheckDate;
    
    /**
     * Moisture content (%) - for quality control
     */
    @Column(name = "moisture_content", precision = 5, scale = 2)
    private BigDecimal moistureContent;
    
    /**
     * Purity (%) - for quality control
     */
    @Column(name = "purity", precision = 5, scale = 2)
    private BigDecimal purity;
    
    /**
     * Produced by (operator/worker)
     */
    @Column(name = "produced_by", length = 50)
    private String producedBy;
    
    /**
     * Is posted to inventory
     */
    @Column(name = "is_posted")
    private Boolean isPosted;
    
    /**
     * Posted date
     */
    @Column(name = "posted_date")
    private LocalDate postedDate;
    
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
        if (quantityProduced == null || unitCost == null) {
            totalCost = BigDecimal.ZERO;
            return;
        }
        
        totalCost = quantityProduced.multiply(unitCost);
    }
    
    /**
     * Gets quality pass percentage
     */
    @Transient
    public BigDecimal getQualityPassPercentage() {
        if (quantityProduced == null || quantityProduced.compareTo(BigDecimal.ZERO) == 0 || goodQuantity == null) {
            return BigDecimal.ZERO;
        }
        return goodQuantity.divide(quantityProduced, 4, RoundingMode.HALF_UP)
                          .multiply(new BigDecimal("100"));
    }
    
    /**
     * Gets rejection percentage
     */
    @Transient
    public BigDecimal getRejectionPercentage() {
        if (quantityProduced == null || quantityProduced.compareTo(BigDecimal.ZERO) == 0 || rejectedQuantity == null) {
            return BigDecimal.ZERO;
        }
        return rejectedQuantity.divide(quantityProduced, 4, RoundingMode.HALF_UP)
                              .multiply(new BigDecimal("100"));
    }
    
    /**
     * Checks if quality is acceptable
     */
    @Transient
    public boolean isQualityAcceptable() {
        return "GOOD".equals(qualityStatus) || "ACCEPTABLE".equals(qualityStatus);
    }
    
    /**
     * Checks if there are rejected items
     */
    @Transient
    public boolean hasRejection() {
        return rejectedQuantity != null && rejectedQuantity.compareTo(BigDecimal.ZERO) > 0;
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (outputTimestamp == null) {
            outputTimestamp = LocalDateTime.now();
        }
        if (goodQuantity == null) {
            goodQuantity = quantityProduced;
        }
        if (rejectedQuantity == null) {
            rejectedQuantity = BigDecimal.ZERO;
        }
        if (reworkQuantity == null) {
            reworkQuantity = BigDecimal.ZERO;
        }
        if (qualityStatus == null) {
            qualityStatus = "PENDING";
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
        
        // Ensure good + rejected + rework = produced
        if (quantityProduced != null && goodQuantity != null && rejectedQuantity != null && reworkQuantity != null) {
            BigDecimal total = goodQuantity.add(rejectedQuantity).add(reworkQuantity);
            if (total.compareTo(quantityProduced) != 0) {
                // Adjust good quantity
                goodQuantity = quantityProduced.subtract(rejectedQuantity).subtract(reworkQuantity);
            }
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductionOutput)) return false;
        ProductionOutput that = (ProductionOutput) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
