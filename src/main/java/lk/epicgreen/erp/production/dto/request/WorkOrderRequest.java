package lk.epicgreen.erp.production.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for creating/updating Work Order
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderRequest {

    @NotBlank(message = "Work order number is required")
    @Size(max = 30, message = "Work order number must not exceed 30 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Work order number must contain only uppercase letters, numbers, hyphens and underscores")
    private String woNumber;

    @NotNull(message = "Work order date is required")
    private LocalDate woDate;

    @NotNull(message = "BOM ID is required")
    private Long bomId;

    @NotNull(message = "Finished product ID is required")
    private Long finishedProductId;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    @NotNull(message = "Planned quantity is required")
    @DecimalMin(value = "0.001", message = "Planned quantity must be > 0")
    private BigDecimal plannedQuantity;

    @DecimalMin(value = "0.0", message = "Actual quantity must be >= 0")
    private BigDecimal actualQuantity;

    @NotNull(message = "UOM ID is required")
    private Long uomId;

    @Size(max = 50, message = "Batch number must not exceed 50 characters")
    private String batchNumber;

    private LocalDate manufacturingDate;

    private LocalDate expectedCompletionDate;

    private LocalDate actualCompletionDate;

    @Pattern(regexp = "^(DRAFT|RELEASED|IN_PROGRESS|COMPLETED|CANCELLED)$", 
             message = "Status must be one of: DRAFT, RELEASED, IN_PROGRESS, COMPLETED, CANCELLED")
    private String status;

    @Pattern(regexp = "^(LOW|MEDIUM|HIGH|URGENT)$", 
             message = "Priority must be one of: LOW, MEDIUM, HIGH, URGENT")
    private String priority;

    private Long supervisorId;

    @DecimalMin(value = "0.0", message = "Material cost must be >= 0")
    private BigDecimal materialCost;

    @DecimalMin(value = "0.0", message = "Labor cost must be >= 0")
    private BigDecimal laborCost;

    @DecimalMin(value = "0.0", message = "Overhead cost must be >= 0")
    private BigDecimal overheadCost;

    @DecimalMin(value = "0.0", message = "Total cost must be >= 0")
    private BigDecimal totalCost;

    @Size(max = 1000, message = "Remarks must not exceed 1000 characters")
    private String remarks;

    private List<WorkOrderItemRequest> items;
}
