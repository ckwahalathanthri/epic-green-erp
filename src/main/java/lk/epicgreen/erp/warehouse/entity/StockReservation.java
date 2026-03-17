package lk.epicgreen.erp.warehouse.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "reservation_number", unique = true, nullable = false, length = 50)
    private String reservationNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id", nullable = false)
    private Inventory inventoryItem;
    
    @Column(name = "product_id")
    private Long productId;
    
    @Column(name = "product_code", length = 50)
    private String productCode;
    
    @Column(name = "product_name", length = 200)
    private String productName;
    
    @Column(name = "reserved_quantity", precision = 15, scale = 3, nullable = false)
    private BigDecimal reservedQuantity;
    
    @Column(name = "fulfilled_quantity", precision = 15, scale = 3)
    private BigDecimal fulfilledQuantity = BigDecimal.ZERO;
    
    @Column(name = "remaining_quantity", precision = 15, scale = 3)
    private BigDecimal remainingQuantity;
    
    @Column(name = "unit_of_measure", length = 20)
    private String unitOfMeasure;
    
    @Column(name = "reservation_type", length = 50, nullable = false)
    private String reservationType; // SALES_ORDER, PRODUCTION, TRANSFER
    
    @Column(name = "reference_number", length = 100)
    private String referenceNumber;
    
    @Column(name = "reference_id")
    private Long referenceId;
    
    @Column(name = "reservation_date", nullable = false)
    private LocalDate reservationDate;
    
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    
    @Column(name = "reservation_status", length = 50)
    private String reservationStatus; // ACTIVE, PARTIALLY_FULFILLED, FULFILLED, EXPIRED, CANCELLED
    
    @Column(name = "priority", length = 20)
    private String priority;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bin_id")
    private StorageBin bin;
    
    @Column(name = "batch_number", length = 100)
    private String batchNumber;
    
    @Column(name = "reserved_by", length = 100)
    private String reservedBy;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    @PreUpdate
    public void calculateRemaining() {
        if (reservedQuantity != null && fulfilledQuantity != null) {
            remainingQuantity = reservedQuantity.subtract(fulfilledQuantity);
        }
    }
}
