package lk.epicgreen.erp.production.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * WorkOrder Request DTO
 * DTO for work order operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderRequest {
    
    @NotBlank(message = "Work order number is required")
    private String workOrderNumber;
    
    @NotNull(message = "Work order date is required")
    private LocalDate workOrderDate;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    private String productCode;
    
    private String productName;
    
    @NotNull(message = "Quantity to produce is required")
    private Double quantityToProduce;
    
    private Double plannedQuantity;  // Alias for quantityToProduce
    
    private Double quantityProduced;
    
    private String unit;
    
    @NotNull(message = "BOM ID is required")
    private Long bomId;
    
    private String bomCode;
    
    private String bomName;
    
    private String workOrderType; // STANDARD, REWORK, SAMPLE
    
    private String priority; // LOW, MEDIUM, HIGH, URGENT
    
    @NotNull(message = "Planned start date is required")
    private LocalDate plannedStartDate;
    
    @NotNull(message = "Planned end date is required")
    private LocalDate plannedEndDate;
    
    private LocalDate actualStartDate;
    
    private LocalDate actualEndDate;
    
    private Long warehouseId;
    
    private String warehouseName;
    
    private String productionLineId;
    
    private String productionLineName;
    
    private String shift;
    
    private String supervisorId;
    
    private String supervisorName;
    
    private Long salesOrderId;
    
    private String salesOrderNumber;
    
    private Long customerId;
    
    private String customerName;
    
    private String description;
    
    private String notes;
    
    private String status; // DRAFT, RELEASED, IN_PROGRESS, PAUSED, COMPLETED, CANCELLED
    
    private String completionStatus; // ON_TIME, DELAYED, EARLY
    
    private Double completionPercentage;
    
    private Long createdByUserId;
    
    private Long releasedByUserId;
    
    private LocalDate releasedDate;
    
    private String releaseNotes;
    
    /**
     * Gets planned quantity (alias for quantityToProduce)
     */
    public Double getPlannedQuantity() {
        return plannedQuantity != null ? plannedQuantity : quantityToProduce;
    }
}
