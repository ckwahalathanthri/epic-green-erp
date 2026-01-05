package lk.epicgreen.erp.sales.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO for Dispatch Item
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispatchItemRequest {

    @NotNull(message = "Order item ID is required")
    private Long orderItemId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @Size(max = 50, message = "Batch number must not exceed 50 characters")
    private String batchNumber;

    @NotNull(message = "Quantity dispatched is required")
    @DecimalMin(value = "0.001", message = "Quantity dispatched must be > 0")
    private BigDecimal quantityDispatched;

    @NotNull(message = "UOM ID is required")
    private Long uomId;

    private Long locationId;

    @Size(max = 1000, message = "Remarks must not exceed 1000 characters")
    private String remarks;
}
