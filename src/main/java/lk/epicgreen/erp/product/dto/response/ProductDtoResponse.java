package lk.epicgreen.erp.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data

@NoArgsConstructor
@AllArgsConstructor
public class ProductDtoResponse {
    private Long id;
    private String productCode;
    private String productName;
    private String description;
    private Long categoryId;
    private String categoryName;
    private String sku;
    private String barcode;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private BigDecimal minStockLevel;
    private BigDecimal reorderLevel;
    private Boolean isActive;
    private Boolean isTaxable;
    private String imageUrl;
    private String productType;
}
