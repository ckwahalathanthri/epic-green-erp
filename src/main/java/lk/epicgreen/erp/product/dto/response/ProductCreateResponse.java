package lk.epicgreen.erp.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data

@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateResponse {
    @NotBlank
    private String productCode;
    
    @NotBlank
    private String productName;
    
    private String description;
    private Long categoryId;
    private String sku;
    private String barcode;
    
    @DecimalMin("0.00")
    private BigDecimal costPrice;
    
    @DecimalMin("0.00")
    private BigDecimal sellingPrice;
    
    private BigDecimal minStockLevel;
    private BigDecimal reorderLevel;
    private Boolean isActive = true;
    private Boolean isTaxable = true;
    private String imageUrl;
    
    @NotBlank
    private String productType; // RAW_MATERIAL or FINISHED_PRODUCT
}
