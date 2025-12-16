package lk.epicgreen.erp.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ProductCategory DTO
 * Data transfer object for ProductCategory entity
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductCategoryDTO {
    
    private Long id;
    
    private String categoryCode;
    
    private String categoryName;
    
    private String description;
    
    /**
     * Parent category
     */
    private Long parentId;
    
    private String parentCategoryCode;
    
    private String parentCategoryName;
    
    /**
     * Child categories
     */
    private List<ProductCategoryDTO> children;
    
    /**
     * Category hierarchy
     */
    private Integer categoryLevel;
    
    private String fullPath;
    
    /**
     * Display
     */
    private Integer displayOrder;
    
    private String imageUrl;
    
    /**
     * Status
     */
    private Boolean isActive;
    
    /**
     * Notes
     */
    private String notes;
    
    /**
     * Statistics
     */
    private Long productCount;
    
    private Long activeProductCount;
    
    /**
     * Computed properties
     */
    private Boolean isRootCategory;
    
    private Boolean isLeafCategory;
    
    /**
     * Audit fields
     */
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime createdAt;
    
    private String createdBy;
    
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime updatedAt;
    
    private String updatedBy;
}
