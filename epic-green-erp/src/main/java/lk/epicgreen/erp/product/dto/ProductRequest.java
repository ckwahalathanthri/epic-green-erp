package lk.epicgreen.erp.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Product Request DTO
 * DTO for product operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    
    @NotBlank(message = "Product code is required")
    private String productCode;
    
    @NotBlank(message = "Product name is required")
    private String productName;
    
    private String sku;
    
    private String barcode;
    
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    
    private String categoryName;
    
    private String productType;
    
    private String description;
    
    private String shortDescription;
    
    @NotNull(message = "Selling price is required")
    private Double sellingPrice;
    
    private Double costPrice;
    
    private Double mrp;
    
    private String brand;
    
    private String manufacturer;
    
    private String unitOfMeasure;
    
    private Double weight;
    
    private String weightUnit;
    
    private String dimensions;
    
    private Boolean isTaxable;
    
    private Double taxPercentage;
    
    private Boolean canBeSold;
    
    private Boolean canBePurchased;
    
    private Boolean isFeatured;
    
    private Integer displayOrder;
    
    private Double discountPercentage;
    
    private Double discountAmount;
    
    private Integer reorderLevel;
    
    private Integer reorderQuantity;
    
    private String imageUrl;
    
    private String notes;
}
