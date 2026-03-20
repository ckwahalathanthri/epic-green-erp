package lk.epicgreen.erp.warehouse.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lk.epicgreen.erp.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_levels",
       indexes = {
           @Index(name = "idx_inventory_item_id", columnList = "inventory_item_id"),
           @Index(name = "idx_warehouse_id", columnList = "warehouse_id"),
           @Index(name = "idx_batch_number", columnList = "batch_number"),
           @Index(name = "idx_expiry_date", columnList = "expiry_date")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Inventory item reference (bidirectional relationship)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id", nullable = false, foreignKey = @ForeignKey(name = "fk_stock_level_inventory"))
    @JsonBackReference
    private Inventory inventoryItem;

    /**
     * Warehouse reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false, foreignKey = @ForeignKey(name = "fk_stock_level_warehouse"))
    private Warehouse warehouse;
    
    /**
     * Product reference (direct reference for easier access)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_stock_level_product"))
    private Product product;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bin_id")
    private StorageBin bin;


    @Column(name = "batch_number", length = 100)
    private String batchNumber;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "quantity_available", precision = 15, scale = 3, nullable = false)
    private BigDecimal quantityAvailable = BigDecimal.ZERO;

    @Column(name = "quantity_reserved", precision = 15, scale = 3)
    private BigDecimal quantityReserved = BigDecimal.ZERO;

    @Column(name= "reorder_level", precision = 15, scale = 3)
    private BigDecimal reorderLevel = BigDecimal.ZERO;

    @Column(name = "quantity_in_transit", precision = 15, scale = 3)
    private BigDecimal quantityInTransit = BigDecimal.ZERO;
    @Column(name = "stock_status", length = 50)
    private String stockStatus;
    @Column(name = "manufacturing_date")
    private LocalDate manufacturingDate;
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    @Column(name = "unit_cost", precision = 15, scale = 2)
    private BigDecimal unitCost;
    @Column(name = "total_value", precision = 15, scale = 2)
    private BigDecimal totalValue;
    @Column(name = "last_counted_date")
    private LocalDateTime lastCountedDate;
    @Column(name = "last_movement_date")
    private LocalDateTime lastMovementDate;
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    @Column(name = "created_by", length = 100)
    private String createdBy;
    @Column(name = "updated_by", length = 100)
    private String updatedBy;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @PrePersist
    @PreUpdate
    public void calculateTotalValue() {
        if (unitCost != null && quantityAvailable != null) {
            totalValue = unitCost.multiply(quantityAvailable);
        }
    }
}
