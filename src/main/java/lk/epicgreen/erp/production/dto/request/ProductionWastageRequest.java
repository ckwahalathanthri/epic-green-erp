package lk.epicgreen.erp.production.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for creating/updating Production Wastage
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionWastageRequest {

    @NotNull(message = "Work order ID is required")
    private Long woId;

    @NotNull(message = "Wastage date is required")
    private LocalDate wastageDate;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Wastage type is required")
    @Pattern(regexp = "^(MATERIAL|PRODUCTION|QUALITY_REJECTION)$",
             message = "Wastage type must be one of: MATERIAL, PRODUCTION, QUALITY_REJECTION")
    private String wastageType;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.001", message = "Quantity must be > 0")
    private BigDecimal quantity;

    @NotNull(message = "UOM ID is required")
    private Long uomId;

    @DecimalMin(value = "0.0", message = "Unit cost must be >= 0")
    private BigDecimal unitCost;

    @Size(max = 1000, message = "Reason must not exceed 1000 characters")
    private String reason;
}
