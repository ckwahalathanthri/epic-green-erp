package lk.epicgreen.erp.warehouse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for stock adjustment item response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAdjustmentItemResponse {

    private Long id;
    private Long adjustmentId;
    private Long productId;
    private String productCode;
    private String productName;
    private String batchNumber;
    private Long locationId;
    private String locationCode;
    private BigDecimal quantityAdjusted;
    private BigDecimal unitCost;
    private BigDecimal totalValue;
    private String remarks;
}
