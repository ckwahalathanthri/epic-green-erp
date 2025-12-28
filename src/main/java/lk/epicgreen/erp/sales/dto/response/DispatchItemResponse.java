package lk.epicgreen.erp.sales.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for Dispatch Item response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispatchItemResponse {

    private Long id;
    private Long dispatchId;
    private Long orderItemId;
    private Long productId;
    private String productCode;
    private String productName;
    private String batchNumber;
    private BigDecimal quantityDispatched;
    private Long uomId;
    private String uomCode;
    private String uomName;
    private Long locationId;
    private String locationCode;
    private String remarks;
}
