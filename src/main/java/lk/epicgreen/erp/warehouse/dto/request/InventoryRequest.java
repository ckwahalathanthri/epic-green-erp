package lk.epicgreen.erp.warehouse.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for creating/updating inventory
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    private Long locationId;

    @Size(max = 50, message = "Batch number must not exceed 50 characters")
    private String batchNumber;

    private LocalDate manufacturingDate;

    private LocalDate expiryDate;

    @NotNull(message = "Quantity available is required")
    @DecimalMin(value = "0.0", message = "Quantity available must be >= 0")
    private BigDecimal quantityAvailable;

    @DecimalMin(value = "0.0", message = "Quantity reserved must be >= 0")
    private BigDecimal quantityReserved;

    @DecimalMin(value = "0.0", message = "Quantity ordered must be >= 0")
    private BigDecimal quantityOrdered;

    @DecimalMin(value = "0.0", message = "Unit cost must be >= 0")
    private BigDecimal unitCost;

    private LocalDate lastStockDate;
}
