package lk.epicgreen.erp.customer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for creating/updating customer price list
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPriceListRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Special price is required")
    @DecimalMin(value = "0.0", message = "Special price must be >= 0")
    private BigDecimal specialPrice;

    @DecimalMin(value = "0.0", message = "Discount percentage must be >= 0")
    @DecimalMax(value = "100.0", message = "Discount percentage must be <= 100")
    private BigDecimal discountPercentage;

    @DecimalMin(value = "0.001", message = "Minimum quantity must be > 0")
    private BigDecimal minQuantity;

    @NotNull(message = "Valid from date is required")
    private LocalDate validFrom;

    private LocalDate validTo;

    private Boolean isActive;
}
