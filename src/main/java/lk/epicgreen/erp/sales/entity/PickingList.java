package lk.epicgreen.erp.sales.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Picking List Entity
 * Warehouse picking instructions generated from sales orders
 */
@Entity
@Table(name = "picking_lists", indexes = {
    @Index(name = "idx_picking_number", columnList = "picking_number"),
    @Index(name = "idx_order_id", columnList = "order_id"),
    @Index(name = "idx_picking_status", columnList = "picking_status"),
    @Index(name = "idx_picking_date", columnList = "picking_date")
})
@Data
public class PickingList {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "picking_number", unique = true, nullable = false, length = 50)
    private String pickingNumber; // PKL-2026-001
    
    @Column(name = "picking_date", nullable = false)
    private LocalDate pickingDate;
    
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    @Column(name = "order_number", length = 50)
    private String orderNumber;
    
    @Column(name = "customer_id")
    private Long customerId;
    
    @Column(name = "customer_name", length = 200)
    private String customerName;
    
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;
    
    @Column(name = "warehouse_name", length = 200)
    private String warehouseName;
    
    @Column(name = "picking_status", length = 20, nullable = false)
    private String pickingStatus; // PENDING, IN_PROGRESS, COMPLETED, CANCELLED
    
    @Column(name = "priority", length = 20)
    private String priority; // LOW, NORMAL, HIGH, URGENT
    
    @Column(name = "assigned_to", length = 100)
    private String assignedTo; // Picker name
    
    @Column(name = "picking_started_at")
    private LocalDateTime pickingStartedAt;
    
    @Column(name = "picking_completed_at")
    private LocalDateTime pickingCompletedAt;
    
    @Column(name = "total_items", nullable = false)
    private Integer totalItems = 0;
    
    @Column(name = "picked_items")
    private Integer pickedItems = 0;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "picking_notes", columnDefinition = "TEXT")
    private String pickingNotes; // Notes from warehouse picker
    
    @Column(name = "created_by", length = 100)
    private String createdBy;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "pickingList", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PickingListItem> items = new ArrayList<>();
    
    @PrePersist
    @PreUpdate
    public void calculateTotals() {
        if (items != null && !items.isEmpty()) {
            totalItems = items.size();
            pickedItems = (int) items.stream()
                .filter(item -> item.getPickedQuantity() != null && 
                               item.getPickedQuantity().compareTo(item.getQuantityToPick()) >= 0)
                .count();
        } else {
            totalItems = 0;
            pickedItems = 0;
        }
        
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * Start picking process
     */
    public void startPicking(String picker) {
        this.pickingStatus = "IN_PROGRESS";
        this.assignedTo = picker;
        this.pickingStartedAt = LocalDateTime.now();
    }
    
    /**
     * Complete picking process
     */
    public void completePicking() {
        this.pickingStatus = "COMPLETED";
        this.pickingCompletedAt = LocalDateTime.now();
    }
    
    /**
     * Cancel picking list
     */
    public void cancel() {
        this.pickingStatus = "CANCELLED";
    }
    
    /**
     * Check if picking is complete
     */
    public boolean isPickingComplete() {
        if (items == null || items.isEmpty()) {
            return false;
        }
        return items.stream().allMatch(item -> 
            item.getPickedQuantity() != null && 
            item.getPickedQuantity().compareTo(item.getQuantityToPick()) >= 0
        );
    }
    
    /**
     * Get picking progress percentage
     */
    public double getPickingProgress() {
        if (totalItems == 0) {
            return 0.0;
        }
        return (pickedItems * 100.0) / totalItems;
    }
}
