package lk.epicgreen.erp.production.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for creating/updating Material Consumption
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialConsumptionRequest {

    @NotNull(message = "Work order ID is required")
    private Long woId;

    private Long woItemId;

    @NotNull(message = "Raw material ID is required")
    private Long rawMaterialId;

    @NotNull(message = "Consumption date is required")
    private LocalDate consumptionDate;

    @Size(max = 50, message = "Batch number must not exceed 50 characters")
    private String batchNumber;

    @NotNull(message = "Quantity consumed is required")
    @DecimalMin(value = "0.001", message = "Quantity consumed must be > 0")
    private BigDecimal quantityConsumed;

    @NotNull(message = "UOM ID is required")
    private Long uomId;

    @DecimalMin(value = "0.0", message = "Unit cost must be >= 0")
    private BigDecimal unitCost;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    private Long consumedBy;

    @Size(max = 1000, message = "Remarks must not exceed 1000 characters")
    private String remarks;
}
