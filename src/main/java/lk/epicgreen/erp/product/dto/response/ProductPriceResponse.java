package lk.epicgreen.erp.product.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
    
@Data
public class ProductPriceResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productCode;
    private String priceType;
    private BigDecimal price;
    private BigDecimal minQuantity;
    private BigDecimal maxQuantity;
    private String currency;
    private Boolean isDefault;
    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;
    private Boolean isActive;
}
