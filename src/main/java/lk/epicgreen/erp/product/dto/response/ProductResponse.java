package lk.epicgreen.erp.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * DTO for product response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String productCode;
    private String productName;
    private String productType;
    private Long categoryId;
    private String categoryName;
    private String description;
    private Long baseUomId;
    private String baseUomCode;
    private String baseUomName;
    private String barcode;
    private String sku;
    private String hsnCode;
    private BigDecimal reorderLevel;
    private BigDecimal minimumStockLevel;
    private BigDecimal maximumStockLevel;
    private BigDecimal standardCost;
    private BigDecimal sellingPrice;
    private Integer shelfLifeDays;
    private Boolean isActive;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double discountAmount;
}
