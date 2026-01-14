package lk.epicgreen.erp.warehouse.dto.request;

import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;

/**
 * DTO for stock adjustment item
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAdjustmentItemRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @Size(max = 50, message = "Batch number must not exceed 50 characters")
    private String batchNumber;

    private Long locationId;

    @NotNull(message = "Quantity adjusted is required")
    private BigDecimal quantityAdjusted;

    @DecimalMin(value = "0.0", message = "Unit cost must be >= 0")
    private BigDecimal unitCost;

    @Size(max = 1000, message = "Remarks must not exceed 1000 characters")
    private String remarks;
}
