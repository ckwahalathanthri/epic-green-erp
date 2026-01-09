package lk.epicgreen.erp.product.service;

import lk.epicgreen.erp.product.dto.request.ProductCategoryRequest;
import lk.epicgreen.erp.product.dto.response.ProductCategoryResponse;
import lk.epicgreen.erp.product.entity.ProductCategory;
import lk.epicgreen.erp.common.dto.PageResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

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

    ProductCategory createCategory(ProductCategoryRequest request);

    ProductCategory updateCategory(Long id, ProductCategoryRequest request);

    void deleteCategory(Long id);

    ProductCategory getCategoryById(Long id);

    ProductCategory getCategoryByCode(String categoryCode);

    ProductCategory getCategoryByName(String categoryName);

    Page<ProductCategory> getAllCategories(Pageable pageable);

    List<ProductCategory> getAllCategories();

    Page<ProductCategory> searchCategories(String keyword, Pageable pageable);

    ProductCategory activateCategory(Long id);

    ProductCategory deactivateCategory(Long id);

    void setParentCategory(Long categoryId, Long parentId);

    void removeParentCategory(Long categoryId);

    List<ProductCategory> getLeafCategories();

    List<ProductCategory> getCategoryPath(Long categoryId);

    List<ProductCategory> getAllDescendants(Long categoryId);

    List<ProductCategory> getCategoryTree();

    int getCategoryDepth(Long categoryId);

    int getMaxDepth();

    List<ProductCategory> getActiveCategories();

    List<ProductCategory> getInactiveCategories();

    List<ProductCategory> getCategoriesByLevel(Integer level);

    List<ProductCategory> getCategoriesWithProducts();

    List<ProductCategory> getCategoriesWithoutProducts();

    List<ProductCategory> getRecentCategories(int limit);

    boolean isCategoryCodeAvailable(String categoryCode);

    boolean isCategoryNameAvailable(String categoryName);

    boolean canDeleteCategory(Long id);

    boolean hasProducts(Long categoryId);

    boolean hasChildren(Long categoryId);

    Map<String, Object> getCategoryStatistics();

	List<Map<String, Object>> getLevelDistribution();

	List<Map<String, Object>> getCategoriesByProductCount();

	Long countProductsInCategory(Long categoryId);

	List<ProductCategory> createBulkCategories(List<ProductCategoryRequest> requests);

	int activateBulkCategories(List<Long> categoryIds);

	int deactivateBulkCategories(List<Long> categoryIds);

	int deleteBulkCategories(List<Long> categoryIds);
}
