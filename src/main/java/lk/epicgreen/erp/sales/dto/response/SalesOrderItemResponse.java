package lk.epicgreen.erp.sales.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for Sales Order Item response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderItemResponse {

    private Long id;
    private Long orderId;
    private Long productId;
    private String productCode;
    private String productName;
    private String batchNumber;
    private BigDecimal quantityOrdered;
    private BigDecimal quantityDelivered;
    private BigDecimal quantityPending;
    private Long uomId;
    private String uomCode;
    private String uomName;
    private BigDecimal unitPrice;
    private BigDecimal discountPercentage;
    private BigDecimal discountAmount;
    private Long taxRateId;
    private String taxRateName;
    private BigDecimal taxAmount;
    private BigDecimal lineTotal;
    private String remarks;
}
