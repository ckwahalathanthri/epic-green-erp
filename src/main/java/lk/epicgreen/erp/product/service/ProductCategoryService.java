package lk.epicgreen.erp.product.service;

import lk.epicgreen.erp.product.dto.request.ProductCategoryRequest;
import lk.epicgreen.erp.product.dto.response.ProductCategoryResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for ProductCategory entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface ProductCategoryService {

    /**
     * Create new product category
     */
    ProductCategoryResponse createProductCategory(ProductCategoryRequest request);

    /**
     * Update existing product category
     */
    ProductCategoryResponse updateProductCategory(Long id, ProductCategoryRequest request);

    /**
     * Activate product category
     */
    void activateProductCategory(Long id);

    /**
     * Deactivate product category
     */
    void deactivateProductCategory(Long id);

    /**
     * Delete product category
     */
    void deleteProductCategory(Long id);

    /**
     * Get product category by ID
     */
    ProductCategoryResponse getProductCategoryById(Long id);

    /**
     * Get product category by code
     */
    ProductCategoryResponse getProductCategoryByCode(String categoryCode);

    /**
     * Get all product categories
     */
    List<ProductCategoryResponse> getAllProductCategories();

    /**
     * Get all product categories with pagination
     */
    PageResponse<ProductCategoryResponse> getAllProductCategories(Pageable pageable);

    /**
     * Get all active product categories
     */
    List<ProductCategoryResponse> getAllActiveProductCategories();

    /**
     * Get root categories (no parent)
     */
    List<ProductCategoryResponse> getRootCategories();

    /**
     * Get child categories by parent ID
     */
    List<ProductCategoryResponse> getChildCategories(Long parentId);

    /**
     * Search product categories
     */
    PageResponse<ProductCategoryResponse> searchProductCategories(String keyword, Pageable pageable);
}
