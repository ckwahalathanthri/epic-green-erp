package lk.epicgreen.erp.production.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * DTO for Work Order Item
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderItemRequest {

    @NotNull(message = "Raw material ID is required")
    private Long rawMaterialId;

    @NotNull(message = "Planned quantity is required")
    @DecimalMin(value = "0.001", message = "Planned quantity must be > 0")
    private BigDecimal plannedQuantity;

    @DecimalMin(value = "0.0", message = "Consumed quantity must be >= 0")
    private BigDecimal consumedQuantity;

    @NotNull(message = "UOM ID is required")
    private Long uomId;

    @DecimalMin(value = "0.0", message = "Unit cost must be >= 0")
    private BigDecimal unitCost;

    @Pattern(regexp = "^(PENDING|ISSUED|CONSUMED)$",
             message = "Status must be one of: PENDING, ISSUED, CONSUMED")
    private String status;

    private Long issuedFromWarehouseId;
}
