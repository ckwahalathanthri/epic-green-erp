package lk.epicgreen.erp.sales.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for Invoice Item response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemResponse {

    private Long id;
    private Long invoiceId;
    private Long orderItemId;
    private Long productId;
    private String productCode;
    private String productName;
    private String batchNumber;
    private BigDecimal quantity;
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
}
