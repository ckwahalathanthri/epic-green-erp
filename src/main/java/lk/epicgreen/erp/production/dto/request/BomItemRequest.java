package lk.epicgreen.erp.production.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO for BOM item
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BomItemRequest {

    @NotNull(message = "Raw material ID is required")
    private Long rawMaterialId;

    @NotNull(message = "Quantity required is required")
    @DecimalMin(value = "0.001", message = "Quantity required must be > 0")
    private BigDecimal quantityRequired;

    @NotNull(message = "UOM ID is required")
    private Long uomId;

    @DecimalMin(value = "0.0", message = "Wastage percentage must be >= 0")
    @DecimalMax(value = "100.0", message = "Wastage percentage must be <= 100")
    private BigDecimal wastagePercentage;

    @DecimalMin(value = "0.0", message = "Standard cost must be >= 0")
    private BigDecimal standardCost;

    @Min(value = 1, message = "Sequence number must be >= 1")
    private Integer sequenceNumber;

    private Boolean isCritical;

    @Size(max = 1000, message = "Remarks must not exceed 1000 characters")
    private String remarks;
}
