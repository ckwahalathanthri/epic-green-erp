package lk.epicgreen.erp.warehouse.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for creating stock movement
 * Note: Stock movements are typically created automatically by the system
 * but can also be created manually for adjustments
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementRequest {

    @NotNull(message = "Movement date is required")
    private LocalDate movementDate;

    @NotBlank(message = "Movement type is required")
    @Pattern(regexp = "^(RECEIPT|ISSUE|TRANSFER|ADJUSTMENT|PRODUCTION|SALES|RETURN)$", 
             message = "Movement type must be one of: RECEIPT, ISSUE, TRANSFER, ADJUSTMENT, PRODUCTION, SALES, RETURN")
    private String movementType;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    private Long fromLocationId;

    private Long toLocationId;

    @Size(max = 50, message = "Batch number must not exceed 50 characters")
    private String batchNumber;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.001", message = "Quantity must be > 0")
    private BigDecimal quantity;

    @NotNull(message = "UOM ID is required")
    private Long uomId;

    @DecimalMin(value = "0.0", message = "Unit cost must be >= 0")
    private BigDecimal unitCost;

    @Size(max = 50, message = "Reference type must not exceed 50 characters")
    private String referenceType;

    private Long referenceId;

    @Size(max = 50, message = "Reference number must not exceed 50 characters")
    private String referenceNumber;

    @Size(max = 1000, message = "Remarks must not exceed 1000 characters")
    private String remarks;
}
