package lk.epicgreen.erp.warehouse.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.product.entity.Product;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * StockMovement entity
 * Tracks all stock movements (in, out, transfer, adjustment)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "stock_movements", indexes = {
    @Index(name = "idx_stock_movement_product", columnList = "product_id"),
    @Index(name = "idx_stock_movement_warehouse", columnList = "warehouse_id"),
    @Index(name = "idx_stock_movement_date", columnList = "movement_date"),
    @Index(name = "idx_stock_movement_type", columnList = "movement_type"),
    @Index(name = "idx_stock_movement_reference", columnList = "reference_number")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovement extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Movement number (unique identifier)
     */
    @Column(name = "movement_number", unique = true, length = 50)
    private String movementNumber;
    
    /**
     * Movement date
     */
    @Column(name = "movement_date", nullable = false)
    private LocalDate movementDate;
    
    /**
     * Movement timestamp
     */
    @Column(name = "movement_timestamp", nullable = false)
    private LocalDateTime movementTimestamp;
    
    /**
     * Movement type (IN, OUT, TRANSFER, ADJUSTMENT, PRODUCTION, DAMAGE, RETURN)
     */
    @Column(name = "movement_type", nullable = false, length = 20)
    private String movementType;
    
    /**
     * Product reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_stock_movement_product"))
    private Product product;
    
    /**
     * From warehouse (for OUT and TRANSFER)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_warehouse_id", foreignKey = @ForeignKey(name = "fk_stock_movement_from_warehouse"))
    private Warehouse fromWarehouse;
    
    /**
     * From location
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_location_id", foreignKey = @ForeignKey(name = "fk_stock_movement_from_location"))
    private WarehouseLocation fromLocation;
    
    /**
     * To warehouse (for IN and TRANSFER)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_warehouse_id", foreignKey = @ForeignKey(name = "fk_stock_movement_to_warehouse"))
    private Warehouse toWarehouse;
    
    /**
     * To location
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_location_id", foreignKey = @ForeignKey(name = "fk_stock_movement_to_location"))
    private WarehouseLocation toLocation;
    
    /**
     * Warehouse (shorthand for single warehouse operations)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", foreignKey = @ForeignKey(name = "fk_stock_movement_warehouse"))
    private Warehouse warehouse;
    
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
     * Quantity moved
     */
    @Column(name = "quantity", precision = 15, scale = 3, nullable = false)
    private BigDecimal quantity;
    
    /**
     * Unit of measurement
     */
    @Column(name = "unit", length = 10)
    private String unit;
    
    /**
     * Cost per unit (at time of movement)
     */
    @Column(name = "cost_per_unit", precision = 15, scale = 2)
    private BigDecimal costPerUnit;
    
    /**
     * Total value (quantity * cost per unit)
     */
    @Column(name = "total_value", precision = 15, scale = 2)
    private BigDecimal totalValue;
    
    /**
     * Currency
     */
    @Column(name = "currency", length = 10)
    private String currency;
    
    /**
     * Reference number (PO number, SO number, GRN number, etc.)
     */
    @Column(name = "reference_number", length = 50)
    private String referenceNumber;
    
    /**
     * Reference ID (ID of related entity)
     */
    @Column(name = "reference_id")
    private Long referenceId;
    
    /**
     * Reference type (PURCHASE_ORDER, SALES_ORDER, GRN, PRODUCTION, etc.)
     */
    @Column(name = "reference_type", length = 30)
    private String referenceType;
    
    /**
     * Reason for movement
     */
    @Column(name = "reason", length = 255)
    private String reason;
    
    /**
     * Performed by (user who performed the movement)
     */
    @Column(name = "performed_by", length = 50)
    private String performedBy;
    
    /**
     * Approved by (user who approved the movement)
     */
    @Column(name = "approved_by", length = 50)
    private String approvedBy;
    
    /**
     * Status (PENDING, APPROVED, COMPLETED, CANCELLED)
     */
    @Column(name = "status", length = 20)
    private String status;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Gets movement direction description
     */
    @Transient
    public String getMovementDirection() {
        if ("IN".equals(movementType)) {
            return "Stock In";
        } else if ("OUT".equals(movementType)) {
            return "Stock Out";
        } else if ("TRANSFER".equals(movementType)) {
            return "Transfer";
        } else if ("ADJUSTMENT".equals(movementType)) {
            return "Adjustment";
        }
        return movementType;
    }
    
    /**
     * Checks if movement is inbound
     */
    @Transient
    public boolean isInbound() {
        return "IN".equals(movementType) || "PRODUCTION".equals(movementType) || "RETURN".equals(movementType);
    }
    
    /**
     * Checks if movement is outbound
     */
    @Transient
    public boolean isOutbound() {
        return "OUT".equals(movementType) || "DAMAGE".equals(movementType);
    }
    
    /**
     * Checks if movement is transfer
     */
    @Transient
    public boolean isTransfer() {
        return "TRANSFER".equals(movementType);
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (movementTimestamp == null) {
            movementTimestamp = LocalDateTime.now();
        }
        if (status == null) {
            status = "COMPLETED";
        }
        if (currency == null) {
            currency = "LKR";
        }
        
        // Calculate total value if not set
        if (totalValue == null && quantity != null && costPerUnit != null) {
            totalValue = quantity.multiply(costPerUnit);
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
