package lk.epicgreen.erp.sales.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.entity.CustomerAddress;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DispatchNote entity
 * Represents delivery/dispatch notes for sales orders
 * Tracks delivery process from warehouse to customer
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "dispatch_notes", indexes = {
    @Index(name = "idx_dispatch_number", columnList = "dispatch_number"),
    @Index(name = "idx_dispatch_date", columnList = "dispatch_date"),
    @Index(name = "idx_order_id", columnList = "order_id"),
    @Index(name = "idx_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DispatchNote extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Dispatch number (unique identifier)
     */
    @NotBlank(message = "Dispatch number is required")
    @Size(max = 30)
    @Column(name = "dispatch_number", nullable = false, unique = true, length = 30)
    private String dispatchNumber;
    
    /**
     * Dispatch date
     */
    @NotNull(message = "Dispatch date is required")
    @Column(name = "dispatch_date", nullable = false)
    private LocalDate dispatchDate;
    
    /**
     * Sales order reference
     */
    @NotNull(message = "Sales order is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dispatch_note_order"))
    private SalesOrder order;
    
    /**
     * Customer reference
     */
    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dispatch_note_customer"))
    private Customer customer;
    
    /**
     * Warehouse (dispatch from)
     */
    @NotNull(message = "Warehouse is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dispatch_note_warehouse"))
    private Warehouse warehouse;
    
    /**
     * Vehicle number
     */
    @Size(max = 20)
    @Column(name = "vehicle_number", length = 20)
    private String vehicleNumber;

    @Column
    private String dispatchNote;

    /**
     * Driver name
     */
    @Size(max = 100)
    @Column(name = "driver_name", length = 100)
    private String driverName;
    
    /**
     * Driver mobile number
     */
    @Size(max = 20)
    @Column(name = "driver_mobile", length = 20)
    private String driverMobile;
    
    /**
     * Delivery address
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id", foreignKey = @ForeignKey(name = "fk_dispatch_note_delivery_address"))
    private CustomerAddress deliveryAddress;
    
    /**
     * Route code
     */
    @Size(max = 20)
    @Column(name = "route_code", length = 20)
    private String routeCode;
    
    /**
     * Status (PENDING, LOADING, DISPATCHED, IN_TRANSIT, DELIVERED, RETURNED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Dispatch time (when vehicle left warehouse)
     */
    @Column(name = "dispatch_time")
    private LocalDateTime dispatchTime;
    
    /**
     * Delivery time (when delivered to customer)
     */
    @Column(name = "delivery_time")
    private LocalDateTime deliveryTime;
    
    /**
     * Delivered by (user/driver)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivered_by", foreignKey = @ForeignKey(name = "fk_dispatch_note_delivered_by"))
    private User deliveredBy;
    
    /**
     * Received by (customer representative name)
     */
    @Size(max = 100)
    @Column(name = "received_by_name", length = 100)
    private String receivedByName;
    
    /**
     * Received by signature (base64 or path)
     */
    @Column(name = "received_by_signature", columnDefinition = "TEXT")
    private String receivedBySignature;
    
    /**
     * Delivery photo URL
     */
    @Size(max = 500)
    @Column(name = "delivery_photo_url", length = 500)
    private String deliveryPhotoUrl;
    
    /**
     * GPS latitude at delivery
     */
    @Column(name = "gps_latitude", precision = 10, scale = 8)
    private BigDecimal gpsLatitude;
    
    /**
     * GPS longitude at delivery
     */
    @Column(name = "gps_longitude", precision = 11, scale = 8)
    private BigDecimal gpsLongitude;
    
    /**
     * Remarks
     */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    /**
     * Dispatch items
     */
    @OneToMany(mappedBy = "dispatch", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DispatchItem> items = new ArrayList<>();
    
    /**
     * Add dispatch item
     */
    public void addItem(DispatchItem item) {
        item.setDispatch(this);
        items.add(item);
    }
    
    /**
     * Remove dispatch item
     */
    public void removeItem(DispatchItem item) {
        items.remove(item);
        item.setDispatch(null);
    }
    
    /**
     * Status checks
     */
    @Transient
    public boolean isPending() {
        return "PENDING".equals(status);
    }
    
    @Transient
    public boolean isLoading() {
        return "LOADING".equals(status);
    }
    
    @Transient
    public boolean isDispatched() {
        return "DISPATCHED".equals(status);
    }
    
    @Transient
    public boolean isInTransit() {
        return "IN_TRANSIT".equals(status);
    }
    
    @Transient
    public boolean isDelivered() {
        return "DELIVERED".equals(status);
    }
    
    @Transient
    public boolean isReturned() {
        return "RETURNED".equals(status);
    }
    
    /**
     * Check if has GPS coordinates
     */
    @Transient
    public boolean hasGpsCoordinates() {
        return gpsLatitude != null && gpsLongitude != null;
    }
    
    /**
     * Check if has delivery proof
     */
    @Transient
    public boolean hasDeliveryProof() {
        return receivedBySignature != null || deliveryPhotoUrl != null;
    }
    
    /**
     * Get GPS coordinates formatted
     */
    @Transient
    public String getGpsCoordinatesFormatted() {
        if (!hasGpsCoordinates()) {
            return null;
        }
        return gpsLatitude.toPlainString() + ", " + gpsLongitude.toPlainString();
    }
    
    /**
     * Workflow methods
     */
    public void startLoading() {
        if (!isPending()) {
            throw new IllegalStateException("Only pending dispatch notes can start loading");
        }
        this.status = "LOADING";
    }
    
    public void dispatch() {
        if (!isLoading()) {
            throw new IllegalStateException("Only loading dispatch notes can be dispatched");
        }
        this.status = "DISPATCHED";
        this.dispatchTime = LocalDateTime.now();
    }
    
    public void markInTransit() {
        if (!isDispatched()) {
            throw new IllegalStateException("Only dispatched notes can be marked in transit");
        }
        this.status = "IN_TRANSIT";
    }
    
    public void markAsDelivered(String receivedBy, User deliverer) {
        if (!isInTransit() && !isDispatched()) {
            throw new IllegalStateException("Only in-transit or dispatched notes can be delivered");
        }
        this.status = "DELIVERED";
        this.deliveryTime = LocalDateTime.now();
        this.receivedByName = receivedBy;
        this.deliveredBy = deliverer;
    }
    
    public void markAsReturned() {
        if (isDelivered()) {
            throw new IllegalStateException("Delivered dispatch notes cannot be returned");
        }
        this.status = "RETURNED";
    }
    

    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "PENDING";
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DispatchNote)) return false;
        DispatchNote that = (DispatchNote) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
