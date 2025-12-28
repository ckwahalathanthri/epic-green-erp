package lk.epicgreen.erp.warehouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.product.entity.Product;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * StockMovement entity
 * Represents all inventory transactions (receipt, issue, transfer, etc.)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "stock_movements", indexes = {
    @Index(name = "idx_movement_date", columnList = "movement_date"),
    @Index(name = "idx_movement_type", columnList = "movement_type"),
    @Index(name = "idx_warehouse_product", columnList = "warehouse_id, product_id"),
    @Index(name = "idx_reference", columnList = "reference_type, reference_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Movement date
     */
    @NotNull(message = "Movement date is required")
    @Column(name = "movement_date", nullable = false)
    private LocalDate movementDate;
    
    /**
     * Movement type (RECEIPT, ISSUE, TRANSFER, ADJUSTMENT, PRODUCTION, SALES, RETURN)
     */
    @NotBlank(message = "Movement type is required")
    @Column(name = "movement_type", nullable = false, length = 20)
    private String movementType;
    
    /**
     * Warehouse reference
     */
    @NotNull(message = "Warehouse is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false, foreignKey = @ForeignKey(name = "fk_stock_movement_warehouse"))
    private Warehouse warehouse;
    
    /**
     * Product reference
     */
    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_stock_movement_product"))
    private Product product;
    
    /**
     * From location (for transfers)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_location_id", foreignKey = @ForeignKey(name = "fk_stock_movement_from_location"))
    private WarehouseLocation fromLocation;
    
    /**
     * To location (for transfers and receipts)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_location_id", foreignKey = @ForeignKey(name = "fk_stock_movement_to_location"))
    private WarehouseLocation toLocation;
    
    /**
     * Batch number
     */
    @Size(max = 50)
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    /**
     * Quantity moved
     */
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @Column(name = "quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal quantity;
    
    /**
     * Unit of measure
     */
    @NotNull(message = "UOM is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uom_id", nullable = false, foreignKey = @ForeignKey(name = "fk_stock_movement_uom"))
    private UnitOfMeasure uom;
    
    /**
     * Unit cost
     */
    @Column(name = "unit_cost", precision = 15, scale = 2)
    private BigDecimal unitCost;
    
    /**
     * Reference type (source document type, e.g., PURCHASE_ORDER, SALES_ORDER)
     */
    @Size(max = 50)
    @Column(name = "reference_type", length = 50)
    private String referenceType;
    
    /**
     * Reference ID (source document ID)
     */
    @Column(name = "reference_id")
    private Long referenceId;
    
    /**
     * Reference number (source document number)
     */
    @Size(max = 50)
    @Column(name = "reference_number", length = 50)
    private String referenceNumber;
    
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
     * Check if receipt movement
     */
    @Transient
    public boolean isReceipt() {
        return "RECEIPT".equals(movementType);
    }
    
    /**
     * Check if issue movement
     */
    @Transient
    public boolean isIssue() {
        return "ISSUE".equals(movementType);
    }
    
    /**
     * Check if transfer movement
     */
    @Transient
    public boolean isTransfer() {
        return "TRANSFER".equals(movementType);
    }
    
    /**
     * Check if adjustment movement
     */
    @Transient
    public boolean isAdjustment() {
        return "ADJUSTMENT".equals(movementType);
    }
    
    /**
     * Check if production movement
     */
    @Transient
    public boolean isProduction() {
        return "PRODUCTION".equals(movementType);
    }
    
    /**
     * Check if sales movement
     */
    @Transient
    public boolean isSales() {
        return "SALES".equals(movementType);
    }
    
    /**
     * Check if return movement
     */
    @Transient
    public boolean isReturn() {
        return "RETURN".equals(movementType);
    }
    
    /**
     * Get total value (quantity × unit cost)
     */
    @Transient
    public BigDecimal getTotalValue() {
        if (quantity == null || unitCost == null) {
            return BigDecimal.ZERO;
        }
        return quantity.multiply(unitCost);
    }
    
    /**
     * Get movement direction (+1 for incoming, -1 for outgoing)
     */
    @Transient
    public int getDirection() {
        if (isReceipt() || isReturn() || (isProduction() && "FINISHED_GOOD".equals(product.getProductType()))) {
            return 1; // Incoming
        } else if (isIssue() || isSales() || (isProduction() && "RAW_MATERIAL".equals(product.getProductType()))) {
            return -1; // Outgoing
        }
        return 0; // Neutral (transfer, adjustment)
    }
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (movementDate == null) {
            movementDate = LocalDate.now();
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockMovement)) return false;
        StockMovement that = (StockMovement) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
