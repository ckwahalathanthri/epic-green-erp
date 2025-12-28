package lk.epicgreen.erp.warehouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * StockAdjustment entity
 * Represents stock adjustments (damage, expiry, pilferage, surplus, deficit)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "stock_adjustments", indexes = {
    @Index(name = "idx_adjustment_number", columnList = "adjustment_number"),
    @Index(name = "idx_adjustment_date", columnList = "adjustment_date"),
    @Index(name = "idx_warehouse_id", columnList = "warehouse_id"),
    @Index(name = "idx_status", columnList = "status")
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
    @NotBlank(message = "Adjustment number is required")
    @Size(max = 30)
    @Column(name = "adjustment_number", nullable = false, unique = true, length = 30)
    private String adjustmentNumber;
    
    /**
     * Adjustment date
     */
    @NotNull(message = "Adjustment date is required")
    @Column(name = "adjustment_date", nullable = false)
    private LocalDate adjustmentDate;
    
    /**
     * Warehouse reference
     */
    @NotNull(message = "Warehouse is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false, foreignKey = @ForeignKey(name = "fk_stock_adjustment_warehouse"))
    private Warehouse warehouse;
    
    /**
     * Adjustment type (DAMAGE, EXPIRY, PILFERAGE, SURPLUS, DEFICIT, OTHER)
     */
    @NotBlank(message = "Adjustment type is required")
    @Column(name = "adjustment_type", nullable = false, length = 20)
    private String adjustmentType;
    
    /**
     * Status (DRAFT, PENDING_APPROVAL, APPROVED, REJECTED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Approved by (user reference)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by", foreignKey = @ForeignKey(name = "fk_stock_adjustment_approved_by"))
    private User approvedBy;
    
    /**
     * Approval timestamp
     */
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    
    /**
     * Remarks
     */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    /**
     * Adjustment items (lines)
     */
    @OneToMany(mappedBy = "adjustment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<StockAdjustmentItem> items = new ArrayList<>();
    
    /**
     * Add adjustment item
     */
    public void addItem(StockAdjustmentItem item) {
        item.setAdjustment(this);
        items.add(item);
    }
    
    /**
     * Remove adjustment item
     */
    public void removeItem(StockAdjustmentItem item) {
        items.remove(item);
        item.setAdjustment(null);
    }
    
    /**
     * Check if draft
     */
    @Transient
    public boolean isDraft() {
        return "DRAFT".equals(status);
    }
    
    /**
     * Check if pending approval
     */
    @Transient
    public boolean isPendingApproval() {
        return "PENDING_APPROVAL".equals(status);
    }
    
    /**
     * Check if approved
     */
    @Transient
    public boolean isApproved() {
        return "APPROVED".equals(status);
    }
    
    /**
     * Check if rejected
     */
    @Transient
    public boolean isRejected() {
        return "REJECTED".equals(status);
    }
    
    /**
     * Check if can be edited
     */
    @Transient
    public boolean canEdit() {
        return isDraft();
    }
    
    /**
     * Check if can be approved
     */
    @Transient
    public boolean canApprove() {
        return isPendingApproval();
    }
    
    /**
     * Check if is damage adjustment
     */
    @Transient
    public boolean isDamage() {
        return "DAMAGE".equals(adjustmentType);
    }
    
    /**
     * Check if is expiry adjustment
     */
    @Transient
    public boolean isExpiry() {
        return "EXPIRY".equals(adjustmentType);
    }
    
    /**
     * Check if is pilferage adjustment
     */
    @Transient
    public boolean isPilferage() {
        return "PILFERAGE".equals(adjustmentType);
    }
    
    /**
     * Check if is surplus adjustment
     */
    @Transient
    public boolean isSurplus() {
        return "SURPLUS".equals(adjustmentType);
    }
    
    /**
     * Check if is deficit adjustment
     */
    @Transient
    public boolean isDeficit() {
        return "DEFICIT".equals(adjustmentType);
    }
    
    /**
     * Submit for approval
     */
    public void submitForApproval() {
        if (!isDraft()) {
            throw new IllegalStateException("Only draft adjustments can be submitted");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalStateException("Cannot submit adjustment with no items");
        }
        this.status = "PENDING_APPROVAL";
    }
    
    /**
     * Approve adjustment
     */
    public void approve(User approver) {
        if (!isPendingApproval()) {
            throw new IllegalStateException("Only pending adjustments can be approved");
        }
        this.status = "APPROVED";
        this.approvedBy = approver;
        this.approvedAt = LocalDateTime.now();
    }
    
    /**
     * Reject adjustment
     */
    public void reject() {
        if (!isPendingApproval()) {
            throw new IllegalStateException("Only pending adjustments can be rejected");
        }
        this.status = "REJECTED";
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "DRAFT";
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
