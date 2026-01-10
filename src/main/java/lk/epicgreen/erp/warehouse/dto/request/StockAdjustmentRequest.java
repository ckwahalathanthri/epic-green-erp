package lk.epicgreen.erp.warehouse.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for creating/updating stock adjustment
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAdjustmentRequest {

    @NotBlank(message = "Adjustment number is required")
    @Size(max = 30, message = "Adjustment number must not exceed 30 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Adjustment number must contain only uppercase letters, numbers, hyphens and underscores")
    private String adjustmentNumber;

    @NotNull(message = "Adjustment date is required")
    private LocalDate adjustmentDate;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    @NotBlank(message = "Adjustment type is required")
    @Pattern(regexp = "^(DAMAGE|EXPIRY|PILFERAGE|SURPLUS|DEFICIT|OTHER)$", 
             message = "Adjustment type must be one of: DAMAGE, EXPIRY, PILFERAGE, SURPLUS, DEFICIT, OTHER")
    private String adjustmentType;

    @Size(max = 1000, message = "Remarks must not exceed 1000 characters")
    private String remarks;

    @NotEmpty(message = "Adjustment items are required")
    private List<StockAdjustmentItemRequest> items;
}
