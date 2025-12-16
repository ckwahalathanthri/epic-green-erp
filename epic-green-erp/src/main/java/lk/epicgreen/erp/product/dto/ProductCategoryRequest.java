package lk.epicgreen.erp.product.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ProductCategory Request DTO
 * DTO for product category operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryRequest {
    
    @NotBlank(message = "Category code is required")
    private String categoryCode;
    
    @NotBlank(message = "Category name is required")
    private String categoryName;
    
    private String description;
    
    private Long parentCategoryId;
    
    private Integer displayOrder;
    
    private String imageUrl;
    
    private String notes;
}
