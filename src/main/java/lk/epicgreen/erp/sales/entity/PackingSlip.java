package lk.epicgreen.erp.sales.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "packing_slips", indexes = {
    @Index(name = "idx_packing_number", columnList = "packing_number"),
    @Index(name = "idx_order_id", columnList = "order_id"),
    @Index(name = "idx_picking_list_id", columnList = "picking_list_id")
})
@Data
public class PackingSlip {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "packing_number", unique = true, nullable = false, length = 50)
    private String packingNumber; // PKS-2026-001
    
    @Column(name = "packing_date", nullable = false)
    private LocalDate packingDate;
    
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    @Column(name = "order_number", length = 50)
    private String orderNumber;
    
    @Column(name = "picking_list_id")
    private Long pickingListId;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    @Column(name = "customer_name", length = 200)
    private String customerName;
    
    @Column(name = "shipping_address", columnDefinition = "TEXT")
    private String shippingAddress;
    
    @Column(name = "packing_status", length = 20, nullable = false)
    private String packingStatus; // PENDING, IN_PROGRESS, COMPLETED, CANCELLED
    
    @Column(name = "packed_by", length = 100)
    private String packedBy;
    
    @Column(name = "packing_started_at")
    private LocalDateTime packingStartedAt;
    
    @Column(name = "packing_completed_at")
    private LocalDateTime packingCompletedAt;
    
    @Column(name = "total_items")
    private Integer totalItems = 0;
    
    @Column(name = "packed_items")
    private Integer packedItems = 0;
    
    @Column(name = "total_packages")
    private Integer totalPackages = 0;
    
    @Column(name = "total_weight", precision = 15, scale = 3)
    private java.math.BigDecimal totalWeight;
    
    @Column(name = "weight_unit", length = 10)
    private String weightUnit = "kg";
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "packingSlip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PackingSlipItem> items = new ArrayList<>();
    
    @PrePersist
    @PreUpdate
    public void calculateTotals() {
        if (items != null && !items.isEmpty()) {
            totalItems = items.size();
            packedItems = (int) items.stream()
                .filter(item -> item.getPackedQuantity() != null && 
                               item.getPackedQuantity().compareTo(item.getQuantityToPack()) >= 0)
                .count();
        }
        
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }
    
    public void completePacking() {
        this.packingStatus = "COMPLETED";
        this.packingCompletedAt = LocalDateTime.now();
    }
}