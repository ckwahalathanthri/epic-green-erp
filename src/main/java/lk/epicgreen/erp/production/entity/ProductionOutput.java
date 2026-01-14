package lk.epicgreen.erp.production.entity;


import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lk.epicgreen.erp.warehouse.entity.WarehouseLocation;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
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
@Table(name = "production_output", indexes = {
    @Index(name = "idx_wo_id", columnList = "wo_id"),
    @Index(name = "idx_output_date", columnList = "output_date"),
    @Index(name = "idx_batch_number", columnList = "batch_number"),
    @Index(name = "idx_finished_product_id", columnList = "finished_product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionOutput {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Work order reference
     */
    @NotNull(message = "Work order is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wo_id", nullable = false, foreignKey = @ForeignKey(name = "fk_production_output_wo"))
    private WorkOrder workOrder;
    
    /**
     * Output date
     */
    @NotNull(message = "Output date is required")
    @Column(name = "output_date", nullable = false)
    private LocalDate outputDate;
    
    /**
     * Finished product
     */
    @NotNull(message = "Finished product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finished_product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_production_output_finished_product"))
    private Product finishedProduct;
    
    /**
     * Batch number
     */
    @NotBlank(message = "Batch number is required")
    @Size(max = 50)
    @Column(name = "batch_number", nullable = false, length = 50)
    private String batchNumber;
    
    /**
     * Quantity produced
     */
    @NotNull(message = "Quantity produced is required")
    @Positive(message = "Quantity produced must be positive")
    @Column(name = "quantity_produced", nullable = false, precision = 15, scale = 3)
    private BigDecimal quantityProduced;
    
    /**
     * Quantity accepted (passed quality check)
     */
    @NotNull(message = "Quantity accepted is required")
    @PositiveOrZero(message = "Quantity accepted must be positive or zero")
    @Column(name = "quantity_accepted", nullable = false, precision = 15, scale = 3)
    private BigDecimal quantityAccepted;
    
    /**
     * Quantity rejected (failed quality check)
     */
    @PositiveOrZero(message = "Quantity rejected must be positive or zero")
    @Column(name = "quantity_rejected", precision = 15, scale = 3)
    private BigDecimal quantityRejected;
    
    /**
     * Quantity for rework
     */
    @PositiveOrZero(message = "Quantity rework must be positive or zero")
    @Column(name = "quantity_rework", precision = 15, scale = 3)
    private BigDecimal quantityRework;
    
    /**
     * Unit of measure
     */
    @NotNull(message = "UOM is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uom_id", nullable = false, foreignKey = @ForeignKey(name = "fk_production_output_uom"))
    private UnitOfMeasure uom;
    
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
     * Target warehouse
     */
    @NotNull(message = "Warehouse is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false, foreignKey = @ForeignKey(name = "fk_production_output_warehouse"))
    private Warehouse warehouse;
    
    /**
     * Storage location
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", foreignKey = @ForeignKey(name = "fk_production_output_location"))
    private WarehouseLocation location;
    
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
     * Quality status (PENDING, PASSED, FAILED)
     */
    @Column(name = "quality_status", nullable = false, length = 10)
    private String qualityStatus;
    
    /**
     * Quality checked by (user)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quality_checked_by", foreignKey = @ForeignKey(name = "fk_production_output_quality_checked_by"))
    private User qualityCheckedBy;
    
    /**
     * Quality check timestamp
     */
    @Column(name = "quality_checked_at")
    private LocalDateTime qualityCheckedAt;
    
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
     * Created by (user ID)
     */
    @Column(name = "created_by")
    private Long createdBy;
    
    /**
     * Check if quality pending
     */
    @Transient
    public boolean isQualityPending() {
        return "PENDING".equals(qualityStatus);
    }
    
    /**
     * Check if quality passed
     */
    @Transient
    public boolean isQualityPassed() {
        return "PASSED".equals(qualityStatus);
    }
    
    /**
     * Check if quality failed
     */
    @Transient
    public boolean isQualityFailed() {
        return "FAILED".equals(qualityStatus);
    }
    
    /**
     * Get acceptance rate percentage
     */
    @Transient
    public BigDecimal getAcceptanceRate() {
        if (quantityProduced == null || quantityProduced.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal accepted = quantityAccepted != null ? quantityAccepted : BigDecimal.ZERO;
        return accepted.divide(quantityProduced, 2, java.math.RoundingMode.HALF_UP)
                       .multiply(new BigDecimal("100"));
    }
    
    /**
     * Get rejection rate percentage
     */
    @Transient
    public BigDecimal getRejectionRate() {
        if (quantityProduced == null || quantityProduced.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal rejected = quantityRejected != null ? quantityRejected : BigDecimal.ZERO;
        return rejected.divide(quantityProduced, 2, java.math.RoundingMode.HALF_UP)
                       .multiply(new BigDecimal("100"));
    }
    
    /**
     * Get rework rate percentage
     */
    @Transient
    public BigDecimal getReworkRate() {
        if (quantityProduced == null || quantityProduced.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal rework = quantityRework != null ? quantityRework : BigDecimal.ZERO;
        return rework.divide(quantityProduced, 2, java.math.RoundingMode.HALF_UP)
                     .multiply(new BigDecimal("100"));
    }
    
    /**
     * Check if batch is expired
     */
    @Transient
    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDate.now());
    }
    
    /**
     * Check if batch is expiring soon (within days)
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
     * Calculate total cost
     */
    @Transient
    public void calculateTotalCost() {
        if (quantityProduced != null && unitCost != null) {
            totalCost = quantityProduced.multiply(unitCost).setScale(2, java.math.RoundingMode.HALF_UP);
        }
    }
    
    /**
     * Pass quality check
     */
    public void passQualityCheck(User checker) {
        this.qualityStatus = "PASSED";
        this.qualityCheckedBy = checker;
        this.qualityCheckedAt = LocalDateTime.now();
    }
    
    /**
     * Fail quality check
     */
    public void failQualityCheck(User checker) {
        this.qualityStatus = "FAILED";
        this.qualityCheckedBy = checker;
        this.qualityCheckedAt = LocalDateTime.now();
    }
    
    @PrePersist
    @PreUpdate
    protected void onSave() {
        if (qualityStatus == null) {
            qualityStatus = "PENDING";
        }
        if (quantityRejected == null) {
            quantityRejected = BigDecimal.ZERO;
        }
        if (quantityRework == null) {
            quantityRework = BigDecimal.ZERO;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (outputDate == null) {
            outputDate = LocalDate.now();
        }
        calculateTotalCost();
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

    public void setQualityCheckedBy(Long qualityCheckedBy2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setQualityCheckedBy'");
    }
}
