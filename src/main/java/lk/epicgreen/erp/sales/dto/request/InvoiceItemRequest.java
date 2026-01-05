package lk.epicgreen.erp.sales.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO for Invoice Item
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemRequest {

    private Long orderItemId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @Size(max = 50, message = "Batch number must not exceed 50 characters")
    private String batchNumber;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.001", message = "Quantity must be > 0")
    private BigDecimal quantity;

    @NotNull(message = "UOM ID is required")
    private Long uomId;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", message = "Unit price must be >= 0")
    private BigDecimal unitPrice;

    @DecimalMin(value = "0.0", message = "Discount percentage must be >= 0")
    @DecimalMax(value = "100.0", message = "Discount percentage must be <= 100")
    private BigDecimal discountPercentage;

    @DecimalMin(value = "0.0", message = "Discount amount must be >= 0")
    private BigDecimal discountAmount;

    private Long taxRateId;

    @DecimalMin(value = "0.0", message = "Tax amount must be >= 0")
    private BigDecimal taxAmount;

    @NotNull(message = "Line total is required")
    @DecimalMin(value = "0.0", message = "Line total must be >= 0")
    private BigDecimal lineTotal;
}
