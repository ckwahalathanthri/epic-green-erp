package lk.epicgreen.erp.sales.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dispatches", indexes = {
    @Index(name = "idx_dispatch_number", columnList = "dispatch_number"),
    @Index(name = "idx_order_id", columnList = "order_id"),
    @Index(name = "idx_dispatch_status", columnList = "dispatch_status"),
    @Index(name = "idx_dispatch_date", columnList = "dispatch_date")
})
@Data
public class Dispatch {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "dispatch_number", unique = true, nullable = false, length = 50)
    private String dispatchNumber; // DSP-2026-001
    
    @Column(name = "dispatch_date", nullable = false)
    private LocalDate dispatchDate;
    
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    @Column(name = "order_number", length = 50)
    private String orderNumber;
    
    @Column(name = "packing_slip_id")
    private Long packingSlipId;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    @Column(name = "customer_name", length = 200)
    private String customerName;
    
    @Column(name = "shipping_address", columnDefinition = "TEXT")
    private String shippingAddress;
    
    @Column(name = "dispatch_status", length = 20, nullable = false)
    private String dispatchStatus; // PLANNED, CONFIRMED, IN_TRANSIT, DELIVERED, CANCELLED
    
    @Column(name = "dispatch_type", length = 20)
    private String dispatchType; // FULL, PARTIAL
    
    @Column(name = "vehicle_id")
    private Long vehicleId;
    
    @Column(name = "vehicle_number", length = 50)
    private String vehicleNumber;
    
    @Column(name = "driver_name", length = 100)
    private String driverName;
    
    @Column(name = "driver_phone", length = 20)
    private String driverPhone;
    
    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;
    
    @Column(name = "actual_delivery_date")
    private LocalDate actualDeliveryDate;
    
    @Column(name = "total_packages")
    private Integer totalPackages = 0;
    
    @Column(name = "total_weight", precision = 15, scale = 3)
    private BigDecimal totalWeight;
    
    @Column(name = "freight_charge", precision = 15, scale = 2)
    private BigDecimal freightCharge = BigDecimal.ZERO;
    
    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;
    
    @Column(name = "route_details", columnDefinition = "TEXT")
    private String routeDetails;
    
    @Column(name = "gps_coordinates", length = 100)
    private String gpsCoordinates; // lat,lng
    
    @Column(name = "stock_deducted")
    private Boolean stockDeducted = false;
    
    @Column(name = "stock_deducted_at")
    private LocalDateTime stockDeductedAt;
    
    @Column(name = "dispatched_by", length = 100)
    private String dispatchedBy;
    
    @Column(name = "dispatched_at")
    private LocalDateTime dispatchedAt;
    
    @Column(name = "delivered_by", length = 100)
    private String deliveredBy;
    
    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "dispatch", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Invoice> Invoice = new ArrayList<>();
    
    @OneToMany(mappedBy = "dispatch", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DispatchItem> items = new ArrayList<>();

    @PrePersist
    @PreUpdate
    public void setTimestamps() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }
    
    public void confirm() {
        this.dispatchStatus = "CONFIRMED";
        this.stockDeducted = true;
        this.stockDeductedAt = LocalDateTime.now();
    }
    
    public void markInTransit() {
        this.dispatchStatus = "IN_TRANSIT";
        this.dispatchedAt = LocalDateTime.now();
    }
    
    public void markDelivered(String deliverer) {
        this.dispatchStatus = "DELIVERED";
        this.deliveredBy = deliverer;
        this.deliveredAt = LocalDateTime.now();
        this.actualDeliveryDate = LocalDate.now();
    }
}