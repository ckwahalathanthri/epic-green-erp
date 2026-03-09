package lk.epicgreen.erp.warehouse.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_transfers", indexes = {
    @Index(name = "idx_transfer_number", columnList = "transfer_number"),
    @Index(name = "idx_from_warehouse", columnList = "from_warehouse_id"),
    @Index(name = "idx_to_warehouse", columnList = "to_warehouse_id"),
    @Index(name = "idx_transfer_status", columnList = "transfer_status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockTransfer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "transfer_number", unique = true, nullable = false, length = 50)
    private String transferNumber;
    
    @Column(name = "transfer_date", nullable = false)
    private LocalDate transferDate;
    
    @Column(name = "from_warehouse_id", nullable = false)
    private Long fromWarehouseId;
    
    @Column(name = "to_warehouse_id", nullable = false)
    private Long toWarehouseId;
    
    @Column(name = "transfer_status", length = 20, nullable = false)
    private String transferStatus; // DRAFT, CONFIRMED, IN_TRANSIT, RECEIVED, CANCELLED
    
    @Column(name = "priority", length = 20)
    private String priority; // LOW, MEDIUM, HIGH, URGENT
    
    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;
    
    @Column(name = "actual_delivery_date")
    private LocalDate actualDeliveryDate;
    
    @Column(name = "vehicle_number", length = 50)
    private String vehicleNumber;
    
    @Column(name = "driver_name", length = 100)
    private String driverName;
    
    @Column(name = "shipped_by", length = 100)
    private String shippedBy;
    
    @Column(name = "shipped_at")
    private LocalDateTime shippedAt;
    
    @Column(name = "received_by", length = 100)
    private String receivedBy;
    
    @Column(name = "received_at")
    private LocalDateTime receivedAt;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_by", length = 100)
    private String createdBy;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (transferStatus == null) {
            transferStatus = "DRAFT";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public void confirm() {
        this.transferStatus = "CONFIRMED";
    }
    
    public void ship() {
        this.transferStatus = "IN_TRANSIT";
        this.shippedAt = LocalDateTime.now();
    }
    
    public void receive() {
        this.transferStatus = "RECEIVED";
        this.receivedAt = LocalDateTime.now();
        this.actualDeliveryDate = LocalDate.now();
    }
    
    public void cancel() {
        this.transferStatus = "CANCELLED";
    }
}
