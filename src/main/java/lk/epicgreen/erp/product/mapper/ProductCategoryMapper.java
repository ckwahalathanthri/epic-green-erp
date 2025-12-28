package lk.epicgreen.erp.product.mapper;

import lk.epicgreen.erp.product.dto.request.ProductCategoryRequest;
import lk.epicgreen.erp.product.dto.response.ProductCategoryResponse;
import lk.epicgreen.erp.product.entity.ProductCategory;
import org.springframework.stereotype.Component;

/**
 * Mapper for ProductCategory entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class ProductCategoryMapper {

    public ProductCategory toEntity(ProductCategoryRequest request) {
        if (request == null) {
            return null;
        }

        return ProductCategory.builder()
            .categoryCode(request.getCategoryCode())
            .categoryName(request.getCategoryName())
            .description(request.getDescription())
            .isActive(request.getIsActive() != null ? request.getIsActive() : true)
            .build();
    }

    public void updateEntityFromRequest(ProductCategoryRequest request, ProductCategory category) {
        if (request == null || category == null) {
            return;
        }

        category.setCategoryCode(request.getCategoryCode());
        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());
        
        if (request.getIsActive() != null) {
            category.setIsActive(request.getIsActive());
        }
    }

    public ProductCategoryResponse toResponse(ProductCategory category) {
        if (category == null) {
            return null;
        }

        return ProductCategoryResponse.builder()
            .id(category.getId())
            .categoryCode(category.getCategoryCode())
            .categoryName(category.getCategoryName())
            .parentCategoryId(category.getParentCategory() != null ? category.getParentCategory().getId() : null)
            .parentCategoryName(category.getParentCategory() != null ? category.getParentCategory().getCategoryName() : null)
            .description(category.getDescription())
            .isActive(category.getIsActive())
            .createdAt(category.getCreatedAt())
            .updatedAt(category.getUpdatedAt())
            .build();
    }
}
