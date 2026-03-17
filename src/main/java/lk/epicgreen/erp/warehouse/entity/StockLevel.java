package lk.epicgreen.erp.warehouse.entity;


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
@Table(name = "stock_levels")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id", nullable = false)
    private Inventory inventoryItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;


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
