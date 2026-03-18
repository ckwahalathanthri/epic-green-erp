package lk.epicgreen.erp.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data

@NoArgsConstructor
@AllArgsConstructor
public class ProductListDTOResponse {
    private Long id;
    private String productCode;
    private String productName;
    private String categoryName;
    private BigDecimal sellingPrice;
    private Boolean isActive;
    private String productType;
}
