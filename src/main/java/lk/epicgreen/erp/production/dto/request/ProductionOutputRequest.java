package lk.epicgreen.erp.production.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for creating/updating Production Output
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionOutputRequest {

    @NotNull(message = "Work order ID is required")
    private Long woId;

    @NotNull(message = "Output date is required")
    private LocalDate outputDate;

    @NotNull(message = "Finished product ID is required")
    private Long finishedProductId;

    @NotBlank(message = "Batch number is required")
    @Size(max = 50, message = "Batch number must not exceed 50 characters")
    private String batchNumber;

    @NotNull(message = "Work order ID is required")
    private Long workOrderId;

    @NotNull(message = "Quantity produced is required")
    @DecimalMin(value = "0.001", message = "Quantity produced must be > 0")
    private BigDecimal quantityProduced;

    @NotNull(message = "Quantity accepted is required")
    @DecimalMin(value = "0.0", message = "Quantity accepted must be >= 0")
    private BigDecimal quantityAccepted;

    @DecimalMin(value = "0.0", message = "Quantity rejected must be >= 0")
    private BigDecimal quantityRejected;

    @DecimalMin(value = "0.0", message = "Quantity rework must be >= 0")
    private BigDecimal quantityRework;

    @NotNull(message = "UOM ID is required")
    private Long uomId;

    private LocalDate manufacturingDate;

    private LocalDate expiryDate;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    private Long locationId;

    @DecimalMin(value = "0.0", message = "Unit cost must be >= 0")
    private BigDecimal unitCost;

    @Pattern(regexp = "^(PENDING|PASSED|FAILED)$", 
             message = "Quality status must be one of: PENDING, PASSED, FAILED")
    private String qualityStatus;

    private Long qualityCheckedBy;

    @Size(max = 1000, message = "Remarks must not exceed 1000 characters")
    private String remarks;
}
