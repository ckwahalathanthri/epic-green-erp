package lk.epicgreen.erp.warehouse.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * StockAdjustment entity
 * Header for stock adjustment transactions (physical counts, corrections)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "stock_adjustments", indexes = {
    @Index(name = "idx_stock_adjustment_number", columnList = "adjustment_number"),
    @Index(name = "idx_stock_adjustment_date", columnList = "adjustment_date"),
    @Index(name = "idx_stock_adjustment_warehouse", columnList = "warehouse_id"),
    @Index(name = "idx_stock_adjustment_type", columnList = "adjustment_type"),
    @Index(name = "idx_stock_adjustment_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockAdjustment extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Adjustment number (unique identifier)
     */
    @Column(name = "adjustment_number", nullable = false, unique = true, length = 50)
    private String adjustmentNumber;
    
    /**
     * Adjustment date
     */
    @Column(name = "adjustment_date", nullable = false)
    private LocalDate adjustmentDate;
    
    /**
     * Adjustment type (PHYSICAL_COUNT, DAMAGE, LOSS, FOUND, CORRECTION, WRITE_OFF)
     */
    @Column(name = "adjustment_type", nullable = false, length = 30)
    private String adjustmentType;
    
    /**
     * Warehouse reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false, foreignKey = @ForeignKey(name = "fk_stock_adjustment_warehouse"))
    private Warehouse warehouse;
    
    /**
     * Reason for adjustment
     */
    @Column(name = "reason", length = 500)
    private String reason;
    
    /**
     * Description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * Status (DRAFT, PENDING_APPROVAL, APPROVED, COMPLETED, CANCELLED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Prepared by (user who prepared the adjustment)
     */
    @Column(name = "prepared_by", length = 50)
    private String preparedBy;
    
    /**
     * Approved by (user who approved)
     */
    @Column(name = "approved_by", length = 50)
    private String approvedBy;
    
    /**
     * Approval date
     */
    @Column(name = "approval_date")
    private LocalDate approvalDate;
    
    /**
     * Completed by (user who completed)
     */
    @Column(name = "completed_by", length = 50)
    private String completedBy;
    
    /**
     * Completion date
     */
    @Column(name = "completion_date")
    private LocalDate completionDate;
    
    /**
     * Total items count
     */
    @Column(name = "total_items")
    private Integer totalItems;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Adjustment items (details)
     */
    @OneToMany(mappedBy = "stockAdjustment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<StockAdjustmentItem> items = new HashSet<>();
    
    /**
     * Adds an adjustment item
     */
    public void addItem(StockAdjustmentItem item) {
        item.setStockAdjustment(this);
        items.add(item);
    }
    
    /**
     * Removes an adjustment item
     */
    public void removeItem(StockAdjustmentItem item) {
        items.remove(item);
        item.setStockAdjustment(null);
    }
    
    /**
     * Checks if adjustment is pending approval
     */
    @Transient
    public boolean isPendingApproval() {
        return "PENDING_APPROVAL".equals(status);
    }
    
    /**
     * Checks if adjustment is approved
     */
    @Transient
    public boolean isApproved() {
        return "APPROVED".equals(status);
    }
    
    /**
     * Checks if adjustment is completed
     */
    @Transient
    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }
    
    /**
     * Checks if adjustment can be edited
     */
    @Transient
    public boolean canEdit() {
        return "DRAFT".equals(status);
    }
    
    /**
     * Checks if adjustment can be approved
     */
    @Transient
    public boolean canApprove() {
        return "PENDING_APPROVAL".equals(status);
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "DRAFT";
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
        if (!(o instanceof StockAdjustment)) return false;
        StockAdjustment that = (StockAdjustment) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
