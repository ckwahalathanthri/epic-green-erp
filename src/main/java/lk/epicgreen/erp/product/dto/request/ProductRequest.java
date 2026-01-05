package lk.epicgreen.erp.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO for creating/updating product
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Product code is required")
    @Size(max = 30, message = "Product code must not exceed 30 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Product code must contain only uppercase letters, numbers, hyphens and underscores")
    private String productCode;

    @NotBlank(message = "Product name is required")
    @Size(max = 200, message = "Product name must not exceed 200 characters")
    private String productName;

    @NotBlank(message = "Product type is required")
    @Pattern(regexp = "^(RAW_MATERIAL|FINISHED_GOOD|SEMI_FINISHED|PACKAGING)$", 
             message = "Product type must be one of: RAW_MATERIAL, FINISHED_GOOD, SEMI_FINISHED, PACKAGING")
    private String productType;

    private Long categoryId;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Base UOM is required")
    private Long baseUomId;

    @Size(max = 50, message = "Barcode must not exceed 50 characters")
    private String barcode;

    @Size(max = 50, message = "SKU must not exceed 50 characters")
    private String sku;

    @Size(max = 20, message = "HSN code must not exceed 20 characters")
    private String hsnCode;

    @DecimalMin(value = "0.0", message = "Reorder level must be >= 0")
    private BigDecimal reorderLevel;

    @DecimalMin(value = "0.0", message = "Minimum stock level must be >= 0")
    private BigDecimal minimumStockLevel;

    @DecimalMin(value = "0.0", message = "Maximum stock level must be >= 0")
    private BigDecimal maximumStockLevel;

    @DecimalMin(value = "0.0", message = "Standard cost must be >= 0")
    private BigDecimal standardCost;

    @DecimalMin(value = "0.0", message = "Selling price must be >= 0")
    private BigDecimal sellingPrice;

    @Min(value = 0, message = "Shelf life days must be >= 0")
    private Integer shelfLifeDays;

    private Boolean isActive;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;
}
