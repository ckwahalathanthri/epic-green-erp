package lk.epicgreen.erp.product.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProductPriceHistoryResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productCode;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private String priceType;
    private String changeReason;
    private Long changedBy;
    private LocalDateTime changedAt;
}
