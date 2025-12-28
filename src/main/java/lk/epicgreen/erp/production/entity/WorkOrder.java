package lk.epicgreen.erp.production.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * WorkOrder entity
 * Represents production orders for manufacturing finished goods
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "work_orders", indexes = {
    @Index(name = "idx_wo_number", columnList = "wo_number"),
    @Index(name = "idx_wo_date", columnList = "wo_date"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_batch_number", columnList = "batch_number")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkOrder extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Work order number (unique identifier)
     */
    @NotBlank(message = "WO number is required")
    @Size(max = 30)
    @Column(name = "wo_number", nullable = false, unique = true, length = 30)
    private String woNumber;
    
    /**
     * Work order date
     */
    @NotNull(message = "WO date is required")
    @Column(name = "wo_date", nullable = false)
    private LocalDate woDate;
    
    /**
     * BOM reference
     */
    @NotNull(message = "BOM is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bom_id", nullable = false, foreignKey = @ForeignKey(name = "fk_work_order_bom"))
    private BillOfMaterials bom;
    
    /**
     * Finished product
     */
    @NotNull(message = "Finished product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finished_product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_work_order_finished_product"))
    private Product finishedProduct;
    
    /**
     * Target warehouse
     */
    @NotNull(message = "Warehouse is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false, foreignKey = @ForeignKey(name = "fk_work_order_warehouse"))
    private Warehouse warehouse;
    
    /**
     * Planned quantity to produce
     */
    @NotNull(message = "Planned quantity is required")
    @Positive(message = "Planned quantity must be positive")
    @Column(name = "planned_quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal plannedQuantity;
    
    /**
     * Actual quantity produced
     */
    @PositiveOrZero(message = "Actual quantity must be positive or zero")
    @Column(name = "actual_quantity", precision = 15, scale = 3)
    private BigDecimal actualQuantity;
    
    /**
     * Unit of measure
     */
    @NotNull(message = "UOM is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uom_id", nullable = false, foreignKey = @ForeignKey(name = "fk_work_order_uom"))
    private UnitOfMeasure uom;
    
    /**
     * Batch number
     */
    @Size(max = 50)
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    /**
     * Manufacturing date
     */
    @Column(name = "manufacturing_date")
    private LocalDate manufacturingDate;
    
    /**
     * Expected completion date
     */
    @Column(name = "expected_completion_date")
    private LocalDate expectedCompletionDate;
    
    /**
     * Actual completion date
     */
    @Column(name = "actual_completion_date")
    private LocalDate actualCompletionDate;
    
    /**
     * Status (DRAFT, RELEASED, IN_PROGRESS, COMPLETED, CANCELLED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Priority (LOW, MEDIUM, HIGH, URGENT)
     */
    @Column(name = "priority", length = 10)
    private String priority;
    
    /**
     * Production supervisor
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id", foreignKey = @ForeignKey(name = "fk_work_order_supervisor"))
    private User supervisor;
    
    /**
     * Material cost
     */
    @PositiveOrZero(message = "Material cost must be positive or zero")
    @Column(name = "material_cost", precision = 15, scale = 2)
    private BigDecimal materialCost;
    
    /**
     * Labor cost
     */
    @PositiveOrZero(message = "Labor cost must be positive or zero")
    @Column(name = "labor_cost", precision = 15, scale = 2)
    private BigDecimal laborCost;
    
    /**
     * Overhead cost
     */
    @PositiveOrZero(message = "Overhead cost must be positive or zero")
    @Column(name = "overhead_cost", precision = 15, scale = 2)
    private BigDecimal overheadCost;
    
    /**
     * Total cost
     */
    @PositiveOrZero(message = "Total cost must be positive or zero")
    @Column(name = "total_cost", precision = 15, scale = 2)
    private BigDecimal totalCost;
    
    /**
     * Remarks
     */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    /**
     * Work order items (materials)
     */
    @OneToMany(mappedBy = "workOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<WorkOrderItem> items = new ArrayList<>();
    
    /**
     * Add work order item
     */
    public void addItem(WorkOrderItem item) {
        item.setWorkOrder(this);
        items.add(item);
    }
    
    /**
     * Remove work order item
     */
    public void removeItem(WorkOrderItem item) {
        items.remove(item);
        item.setWorkOrder(null);
    }
    
    /**
     * Check if draft
     */
    @Transient
    public boolean isDraft() {
        return "DRAFT".equals(status);
    }
    
    /**
     * Check if released
     */
    @Transient
    public boolean isReleased() {
        return "RELEASED".equals(status);
    }
    
    /**
     * Check if in progress
     */
    @Transient
    public boolean isInProgress() {
        return "IN_PROGRESS".equals(status);
    }
    
    /**
     * Check if completed
     */
    @Transient
    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }
    
    /**
     * Check if cancelled
     */
    @Transient
    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }
    
    /**
     * Check if can be edited
     */
    @Transient
    public boolean canEdit() {
        return isDraft();
    }
    
    /**
     * Check if can be started
     */
    @Transient
    public boolean canStart() {
        return isReleased();
    }
    
    /**
     * Check if can be completed
     */
    @Transient
    public boolean canComplete() {
        return isInProgress();
    }
    
    /**
     * Check if is urgent priority
     */
    @Transient
    public boolean isUrgent() {
        return "URGENT".equals(priority);
    }
    
    /**
     * Check if is high priority
     */
    @Transient
    public boolean isHighPriority() {
        return "HIGH".equals(priority);
    }
    
    /**
     * Check if overdue
     */
    @Transient
    public boolean isOverdue() {
        if (isCompleted() || isCancelled()) {
            return false;
        }
        return expectedCompletionDate != null && LocalDate.now().isAfter(expectedCompletionDate);
    }
    
    /**
     * Get completion percentage
     */
    @Transient
    public BigDecimal getCompletionPercentage() {
        if (plannedQuantity == null || plannedQuantity.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal actual = actualQuantity != null ? actualQuantity : BigDecimal.ZERO;
        return actual.divide(plannedQuantity, 2, java.math.RoundingMode.HALF_UP)
                     .multiply(new BigDecimal("100"));
    }
    
    /**
     * Calculate total cost
     */
    @Transient
    public void calculateTotalCost() {
        BigDecimal material = materialCost != null ? materialCost : BigDecimal.ZERO;
        BigDecimal labor = laborCost != null ? laborCost : BigDecimal.ZERO;
        BigDecimal overhead = overheadCost != null ? overheadCost : BigDecimal.ZERO;
        totalCost = material.add(labor).add(overhead);
    }
    
    /**
     * Release work order
     */
    public void release() {
        if (!isDraft()) {
            throw new IllegalStateException("Only draft work orders can be released");
        }
        this.status = "RELEASED";
    }
    
    /**
     * Start work order
     */
    public void start() {
        if (!isReleased()) {
            throw new IllegalStateException("Only released work orders can be started");
        }
        this.status = "IN_PROGRESS";
        if (manufacturingDate == null) {
            manufacturingDate = LocalDate.now();
        }
    }
    
    /**
     * Complete work order
     */
    public void complete() {
        if (!isInProgress()) {
            throw new IllegalStateException("Only in-progress work orders can be completed");
        }
        this.status = "COMPLETED";
        this.actualCompletionDate = LocalDate.now();
    }
    
    /**
     * Cancel work order
     */
    public void cancel() {
        if (isCompleted()) {
            throw new IllegalStateException("Cannot cancel completed work orders");
        }
        this.status = "CANCELLED";
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "DRAFT";
        }
        if (priority == null) {
            priority = "MEDIUM";
        }
        if (actualQuantity == null) {
            actualQuantity = BigDecimal.ZERO;
        }
        if (materialCost == null) {
            materialCost = BigDecimal.ZERO;
        }
        if (laborCost == null) {
            laborCost = BigDecimal.ZERO;
        }
        if (overheadCost == null) {
            overheadCost = BigDecimal.ZERO;
        }
        if (totalCost == null) {
            totalCost = BigDecimal.ZERO;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkOrder)) return false;
        WorkOrder workOrder = (WorkOrder) o;
        return id != null && id.equals(workOrder.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
