package lk.epicgreen.erp.sales.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * DispatchNote entity
 * Represents goods dispatch/delivery notes
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "dispatch_notes", indexes = {
    @Index(name = "idx_dispatch_note_number", columnList = "dispatch_number"),
    @Index(name = "idx_dispatch_date", columnList = "dispatch_date"),
    @Index(name = "idx_dispatch_order", columnList = "sales_order_id"),
    @Index(name = "idx_dispatch_customer", columnList = "customer_id"),
    @Index(name = "idx_dispatch_status", columnList = "status")
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
    @Column(name = "dispatch_number", nullable = false, unique = true, length = 50)
    private String dispatchNumber;
    
    /**
     * Dispatch date
     */
    @Column(name = "dispatch_date", nullable = false)
    private LocalDate dispatchDate;
    
    /**
     * Dispatch timestamp
     */
    @Column(name = "dispatch_timestamp")
    private LocalDateTime dispatchTimestamp;
    
    /**
     * Sales order reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_order_id", foreignKey = @ForeignKey(name = "fk_dispatch_note_order"))
    private SalesOrder salesOrder;
    
    /**
     * Customer reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dispatch_note_customer"))
    private Customer customer;
    
    /**
     * Warehouse (from where goods dispatched)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dispatch_note_warehouse"))
    private Warehouse warehouse;
    
    /**
     * Delivery address (reference to customer address)
     */
    @Column(name = "delivery_address_id")
    private Long deliveryAddressId;
    
    /**
     * Delivery address text (copy of address at time of dispatch)
     */
    @Column(name = "delivery_address_text", columnDefinition = "TEXT")
    private String deliveryAddressText;
    
    /**
     * Vehicle number
     */
    @Column(name = "vehicle_number", length = 50)
    private String vehicleNumber;
    
    /**
     * Driver name
     */
    @Column(name = "driver_name", length = 100)
    private String driverName;
    
    /**
     * Driver contact
     */
    @Column(name = "driver_contact", length = 20)
    private String driverContact;
    
    /**
     * Transporter name
     */
    @Column(name = "transporter_name", length = 200)
    private String transporterName;
    
    /**
     * Tracking number
     */
    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;
    
    /**
     * Dispatched by (person who prepared dispatch)
     */
    @Column(name = "dispatched_by", length = 50)
    private String dispatchedBy;
    
    /**
     * Status (DRAFT, PREPARED, DISPATCHED, IN_TRANSIT, DELIVERED, CANCELLED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Delivery status (PENDING, DELIVERED, PARTIALLY_DELIVERED, FAILED, RETURNED)
     */
    @Column(name = "delivery_status", length = 30)
    private String deliveryStatus;
    
    /**
     * Delivered date
     */
    @Column(name = "delivered_date")
    private LocalDate deliveredDate;
    
    /**
     * Received by (person who received goods)
     */
    @Column(name = "received_by", length = 100)
    private String receivedBy;
    
    /**
     * Received date
     */
    @Column(name = "received_date")
    private LocalDate receivedDate;
    
    /**
     * Is posted to inventory (inventory deducted)
     */
    @Column(name = "is_posted")
    private Boolean isPosted;
    
    /**
     * Posted date
     */
    @Column(name = "posted_date")
    private LocalDate postedDate;
    
    /**
     * Posted by
     */
    @Column(name = "posted_by", length = 50)
    private String postedBy;
    
    /**
     * Delivery instructions
     */
    @Column(name = "delivery_instructions", columnDefinition = "TEXT")
    private String deliveryInstructions;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Total items count
     */
    @Column(name = "total_items")
    private Integer totalItems;
    
    /**
     * Dispatch items
     */
    @OneToMany(mappedBy = "dispatchNote", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    @Builder.Default
    private Set<DispatchItem> items = new HashSet<>();
    
    /**
     * Adds a dispatch item
     */
    public void addItem(DispatchItem item) {
        item.setDispatchNote(this);
        items.add(item);
    }
    
    /**
     * Removes a dispatch item
     */
    public void removeItem(DispatchItem item) {
        items.remove(item);
        item.setDispatchNote(null);
    }
    
    /**
     * Gets total dispatched quantity
     */
    @Transient
    public BigDecimal getTotalDispatchedQuantity() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
            .map(DispatchItem::getDispatchedQuantity)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Checks if can be posted to inventory
     */
    @Transient
    public boolean canPost() {
        return "DISPATCHED".equals(status) && !isPosted;
    }
    
    /**
     * Checks if can be edited
     */
    @Transient
    public boolean canEdit() {
        return "DRAFT".equals(status) || "PREPARED".equals(status);
    }
    
    /**
     * Checks if is delivered
     */
    @Transient
    public boolean isDelivered() {
        return "DELIVERED".equals(deliveryStatus);
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "DRAFT";
        }
        if (deliveryStatus == null) {
            deliveryStatus = "PENDING";
        }
        if (dispatchTimestamp == null) {
            dispatchTimestamp = LocalDateTime.now();
        }
        if (isPosted == null) {
            isPosted = false;
        }
        if (totalItems == null) {
            totalItems = 0;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Update total items count
        if (items != null) {
            totalItems = items.size();
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
