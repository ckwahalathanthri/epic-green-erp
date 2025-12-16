package lk.epicgreen.erp.production.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * WorkOrder entity
 * Represents production orders/jobs for manufacturing
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "work_orders", indexes = {
    @Index(name = "idx_wo_number", columnList = "wo_number"),
    @Index(name = "idx_wo_date", columnList = "wo_date"),
    @Index(name = "idx_wo_bom", columnList = "bom_id"),
    @Index(name = "idx_wo_status", columnList = "status"),
    @Index(name = "idx_wo_start_date", columnList = "planned_start_date")
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
    @Column(name = "wo_number", nullable = false, unique = true, length = 50)
    private String woNumber;
    
    /**
     * Work order date
     */
    @Column(name = "wo_date", nullable = false)
    private LocalDate woDate;
    
    /**
     * BOM reference (recipe to follow)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bom_id", nullable = false, foreignKey = @ForeignKey(name = "fk_wo_bom"))
    private BillOfMaterials billOfMaterials;
    
    /**
     * Warehouse for output (where finished goods go)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "output_warehouse_id", foreignKey = @ForeignKey(name = "fk_wo_output_warehouse"))
    private Warehouse outputWarehouse;
    
    /**
     * Warehouse for material consumption (where raw materials come from)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_warehouse_id", foreignKey = @ForeignKey(name = "fk_wo_material_warehouse"))
    private Warehouse materialWarehouse;
    
    /**
     * Planned quantity to produce
     */
    @Column(name = "planned_quantity", precision = 15, scale = 3, nullable = false)
    private BigDecimal plannedQuantity;
    
    /**
     * Actual quantity produced
     */
    @Column(name = "produced_quantity", precision = 15, scale = 3)
    private BigDecimal producedQuantity;
    
    /**
     * Unit of measurement
     */
    @Column(name = "unit", nullable = false, length = 10)
    private String unit;
    
    /**
     * Planned start date
     */
    @Column(name = "planned_start_date")
    private LocalDate plannedStartDate;
    
    /**
     * Planned end date
     */
    @Column(name = "planned_end_date")
    private LocalDate plannedEndDate;
    
    /**
     * Actual start date
     */
    @Column(name = "actual_start_date")
    private LocalDate actualStartDate;
    
    /**
     * Actual end date
     */
    @Column(name = "actual_end_date")
    private LocalDate actualEndDate;
    
    /**
     * Priority (LOW, MEDIUM, HIGH, URGENT)
     */
    @Column(name = "priority", length = 20)
    private String priority;
    
    /**
     * Status (DRAFT, PLANNED, RELEASED, IN_PROGRESS, COMPLETED, CLOSED, CANCELLED, ON_HOLD)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Production supervisor
     */
    @Column(name = "supervisor", length = 50)
    private String supervisor;
    
    /**
     * Shift (MORNING, AFTERNOON, NIGHT)
     */
    @Column(name = "shift", length = 20)
    private String shift;
    
    /**
     * Batch number for production
     */
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    /**
     * Planned material cost
     */
    @Column(name = "planned_material_cost", precision = 15, scale = 2)
    private BigDecimal plannedMaterialCost;
    
    /**
     * Actual material cost
     */
    @Column(name = "actual_material_cost", precision = 15, scale = 2)
    private BigDecimal actualMaterialCost;
    
    /**
     * Planned labor cost
     */
    @Column(name = "planned_labor_cost", precision = 15, scale = 2)
    private BigDecimal plannedLaborCost;
    
    /**
     * Actual labor cost
     */
    @Column(name = "actual_labor_cost", precision = 15, scale = 2)
    private BigDecimal actualLaborCost;
    
    /**
     * Planned overhead cost
     */
    @Column(name = "planned_overhead_cost", precision = 15, scale = 2)
    private BigDecimal plannedOverheadCost;
    
    /**
     * Actual overhead cost
     */
    @Column(name = "actual_overhead_cost", precision = 15, scale = 2)
    private BigDecimal actualOverheadCost;
    
    /**
     * Total planned cost
     */
    @Column(name = "total_planned_cost", precision = 15, scale = 2)
    private BigDecimal totalPlannedCost;
    
    /**
     * Total actual cost
     */
    @Column(name = "total_actual_cost", precision = 15, scale = 2)
    private BigDecimal totalActualCost;
    
    /**
     * Currency
     */
    @Column(name = "currency", length = 10)
    private String currency;
    
    /**
     * Production instructions
     */
    @Column(name = "production_instructions", columnDefinition = "TEXT")
    private String productionInstructions;
    
    /**
     * Quality check required
     */
    @Column(name = "quality_check_required")
    private Boolean qualityCheckRequired;
    
    /**
     * Quality check status (PENDING, PASSED, FAILED)
     */
    @Column(name = "quality_check_status", length = 20)
    private String qualityCheckStatus;
    
    /**
     * Quality remarks
     */
    @Column(name = "quality_remarks", columnDefinition = "TEXT")
    private String qualityRemarks;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Work order number (denormalized for quick access)
     */
    @Column(name = "work_order_number", length = 50)
    private String workOrderNumber;
    
    /**
     * Product ID (denormalized)
     */
    @Column(name = "product_id_direct")
    private Long productId;
    
    /**
     * Product name (denormalized)
     */
    @Column(name = "product_name", length = 200)
    private String productName;
    
    /**
     * BOM ID (denormalized)
     */
    @Column(name = "bom_id_direct")
    private Long bomId;
    
    /**
     * Sales order ID reference
     */
    @Column(name = "sales_order_id")
    private Long salesOrderId;
    
    /**
     * Work order type (STANDARD, CUSTOM, REWORK, URGENT)
     */
    @Column(name = "work_order_type", length = 20)
    private String workOrderType;
    
    /**
     * Production line ID
     */
    @Column(name = "production_line_id", length = 50)
    private String productionLineId;
    
    /**
     * Supervisor ID
     */
    @Column(name = "supervisor_id", length = 50)
    private String supervisorId;
    
    /**
     * Supervisor name (denormalized)
     */
    @Column(name = "supervisor_name", length = 100)
    private String supervisorName;
    
    /**
     * Is approved flag
     */
    @Column(name = "is_approved")
    private Boolean isApproved;
    
    /**
     * Is completed flag
     */
    @Column(name = "is_completed")
    private Boolean isCompleted;
    
    /**
     * Approved date
     */
    @Column(name = "approved_date")
    private LocalDate approvedDate;
    
    /**
     * Approved by user ID
     */
    @Column(name = "approved_by_user_id")
    private Long approvedByUserId;
    
    /**
     * Approval notes
     */
    @Column(name = "approval_notes", length = 500)
    private String approvalNotes;
    
    /**
     * Completed date
     */
    @Column(name = "completed_date")
    private LocalDate completedDate;
    
    /**
     * Actual quantity produced (alias for producedQuantity)
     */
    @Transient
    private BigDecimal actualQuantity;
    
    /**
     * Actual labour hours spent
     */
    @Column(name = "actual_labour_hours", precision = 10, scale = 2)
    private BigDecimal actualLabourHours;
    
    /**
     * Cancellation reason
     */
    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;
    
    /**
     * Cancelled date
     */
    @Column(name = "cancelled_date")
    private LocalDate cancelledDate;
    
    /**
     * Rejection reason
     */
    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;
    
    /**
     * Rejected date
     */
    @Column(name = "rejected_date")
    private LocalDate rejectedDate;
    
    /**
     * Gets actual quantity (alias for producedQuantity)
     */
    public BigDecimal getActualQuantity() {
        return producedQuantity != null ? producedQuantity : actualQuantity;
    }
    
    /**
     * Sets actual quantity (updates producedQuantity)
     */
    public void setActualQuantity(BigDecimal actualQuantity) {
        this.producedQuantity = actualQuantity;
        this.actualQuantity = actualQuantity;
    }
    
    /**
     * Sets actual quantity from Double
     */
    public void setActualQuantity(Double actualQuantity) {
        BigDecimal actualQty = actualQuantity != null ? BigDecimal.valueOf(actualQuantity) : BigDecimal.ZERO;
        this.producedQuantity = actualQty;
        this.actualQuantity = actualQty;
    }
    
    /**
     * Gets product ID
     */
    @Transient
    public Long getProductId() {
        if (billOfMaterials != null && billOfMaterials.getProduct() != null) {
            return billOfMaterials.getProduct().getId();
        }
        return productId;
    }
    
    /**
     * Gets is approved status
     */
    public Boolean getIsApproved() {
        return isApproved != null ? isApproved : false;
    }
    
    /**
     * Gets is completed status
     */
    public Boolean getIsCompleted() {
        return isCompleted != null ? isCompleted : false;
    }
    
    /**
     * Work order items (products to produce)
     */
    @OneToMany(mappedBy = "workOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<WorkOrderItem> items = new HashSet<>();
    
    /**
     * Material consumptions
     */
    @OneToMany(mappedBy = "workOrder", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<MaterialConsumption> materialConsumptions = new HashSet<>();
    
    /**
     * Production outputs
     */
    @OneToMany(mappedBy = "workOrder", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<ProductionOutput> productionOutputs = new HashSet<>();
    
    /**
     * Adds a work order item
     */
    public void addItem(WorkOrderItem item) {
        item.setWorkOrder(this);
        items.add(item);
    }
    
    /**
     * Removes a work order item
     */
    public void removeItem(WorkOrderItem item) {
        items.remove(item);
        item.setWorkOrder(null);
    }
    
    /**
     * Calculates planned costs based on BOM and quantity
     */
    @Transient
    public void calculatePlannedCosts() {
        if (billOfMaterials == null || plannedQuantity == null) {
            return;
        }
        
        // Calculate based on BOM output quantity ratio
        BigDecimal bomOutputQty = billOfMaterials.getOutputQuantity();
        if (bomOutputQty == null || bomOutputQty.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        
        BigDecimal ratio = plannedQuantity.divide(bomOutputQty, 6, RoundingMode.HALF_UP);
        
        // Material cost
        if (billOfMaterials.getTotalMaterialCost() != null) {
            plannedMaterialCost = billOfMaterials.getTotalMaterialCost().multiply(ratio);
        }
        
        // Labor cost
        if (billOfMaterials.getLaborCost() != null) {
            plannedLaborCost = billOfMaterials.getLaborCost().multiply(ratio);
        }
        
        // Overhead cost
        if (billOfMaterials.getOverheadCost() != null) {
            plannedOverheadCost = billOfMaterials.getOverheadCost().multiply(ratio);
        }
        
        // Total planned cost
        totalPlannedCost = BigDecimal.ZERO;
        if (plannedMaterialCost != null) totalPlannedCost = totalPlannedCost.add(plannedMaterialCost);
        if (plannedLaborCost != null) totalPlannedCost = totalPlannedCost.add(plannedLaborCost);
        if (plannedOverheadCost != null) totalPlannedCost = totalPlannedCost.add(plannedOverheadCost);
    }
    
    /**
     * Calculates actual costs from consumptions
     */
    @Transient
    public void calculateActualCosts() {
        if (materialConsumptions != null && !materialConsumptions.isEmpty()) {
            actualMaterialCost = materialConsumptions.stream()
                .map(MaterialConsumption::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        
        // Total actual cost
        totalActualCost = BigDecimal.ZERO;
        if (actualMaterialCost != null) totalActualCost = totalActualCost.add(actualMaterialCost);
        if (actualLaborCost != null) totalActualCost = totalActualCost.add(actualLaborCost);
        if (actualOverheadCost != null) totalActualCost = totalActualCost.add(actualOverheadCost);
    }
    
    /**
     * Gets completion percentage
     */
    @Transient
    public BigDecimal getCompletionPercentage() {
        if (plannedQuantity == null || plannedQuantity.compareTo(BigDecimal.ZERO) == 0 || producedQuantity == null) {
            return BigDecimal.ZERO;
        }
        return producedQuantity.divide(plannedQuantity, 4, RoundingMode.HALF_UP)
                              .multiply(new BigDecimal("100"));
    }
    
    /**
     * Checks if work order is completed
     */
    @Transient
    public boolean isCompleted() {
        return "COMPLETED".equals(status) || "CLOSED".equals(status);
    }
    
    /**
     * Checks if work order can be started
     */
    @Transient
    public boolean canStart() {
        return "RELEASED".equals(status);
    }
    
    /**
     * Checks if work order can be edited
     */
    @Transient
    public boolean canEdit() {
        return "DRAFT".equals(status) || "PLANNED".equals(status);
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
        if (producedQuantity == null) {
            producedQuantity = BigDecimal.ZERO;
        }
        if (currency == null) {
            currency = "LKR";
        }
        if (qualityCheckRequired == null) {
            qualityCheckRequired = true;
        }
        if (qualityCheckStatus == null) {
            qualityCheckStatus = "PENDING";
        }
        
        // Initialize new fields
        if (isApproved == null) {
            isApproved = false;
        }
        if (isCompleted == null) {
            isCompleted = false;
        }
        if (workOrderType == null) {
            workOrderType = "STANDARD";
        }
        if (actualLabourHours == null) {
            actualLabourHours = BigDecimal.ZERO;
        }
        
        // Sync work order number
        if (workOrderNumber == null && woNumber != null) {
            workOrderNumber = woNumber;
        }
        
        // Sync BOM ID
        if (billOfMaterials != null && bomId == null) {
            bomId = billOfMaterials.getId();
        }
        
        // Sync product info from BOM
        if (billOfMaterials != null && billOfMaterials.getProduct() != null) {
            if (productId == null) {
                productId = billOfMaterials.getProduct().getId();
            }
            if (productName == null) {
                productName = billOfMaterials.getProduct().getProductName();
            }
        }
        
        // Sync supervisor name
        if (supervisorName == null && supervisor != null) {
            supervisorName = supervisor;
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
