package lk.epicgreen.erp.product.service;

import lk.epicgreen.erp.product.dto.ProductCategoryRequest;
import lk.epicgreen.erp.product.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * ProductCategory Service Interface
 * Service for product category operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface ProductCategoryService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    ProductCategory createCategory(ProductCategoryRequest request);
    ProductCategory updateCategory(Long id, ProductCategoryRequest request);
    void deleteCategory(Long id);
    ProductCategory getCategoryById(Long id);
    ProductCategory getCategoryByCode(String categoryCode);
    ProductCategory getCategoryByName(String categoryName);
    List<ProductCategory> getAllCategories();
    Page<ProductCategory> getAllCategories(Pageable pageable);
    Page<ProductCategory> searchCategories(String keyword, Pageable pageable);
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    ProductCategory activateCategory(Long id);
    ProductCategory deactivateCategory(Long id);
    
    // ===================================================================
    // HIERARCHY OPERATIONS
    // ===================================================================
    
    ProductCategory addSubcategory(Long parentId, ProductCategoryRequest request);
    ProductCategory moveCategory(Long categoryId, Long newParentId);
    List<ProductCategory> getCategoryHierarchy(Long categoryId);
    List<ProductCategory> getCategoryTree();
    List<ProductCategory> getCategoryTreeByParent(Long parentId);
    void updateCategoryPath(Long categoryId);
    void recalculateCategoryLevels(Long parentId);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    List<ProductCategory> getActiveCategories();
    Page<ProductCategory> getActiveCategories(Pageable pageable);
    List<ProductCategory> getInactiveCategories();
    List<ProductCategory> getRootCategories();
    List<ProductCategory> getLeafCategories();
    List<ProductCategory> getCategoriesWithChildren();
    List<ProductCategory> getSubcategories(Long parentId);
    List<ProductCategory> getAllSubcategories(Long parentId);
    List<ProductCategory> getSiblingCategories(Long categoryId);
    List<ProductCategory> getCategoriesByLevel(Integer level);
    List<ProductCategory> getCategoriesByMaxDepth(Integer maxLevel);
    List<ProductCategory> getCategoriesWithProducts();
    List<ProductCategory> getEmptyCategories();
    List<ProductCategory> getRecentCategories(int limit);
    Integer getMaxDepth();
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    boolean validateCategory(ProductCategory category);
    boolean isCategoryCodeAvailable(String categoryCode);
    boolean isCategoryNameAvailable(String categoryName);
    boolean canDeleteCategory(Long categoryId);
    boolean hasProducts(Long categoryId);
    boolean hasSubcategories(Long categoryId);
    boolean isDescendantOf(Long categoryId, Long ancestorId);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<ProductCategory> createBulkCategories(List<ProductCategoryRequest> requests);
    int activateBulkCategories(List<Long> categoryIds);
    int deactivateBulkCategories(List<Long> categoryIds);
    int deleteBulkCategories(List<Long> categoryIds);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    Map<String, Object> getCategoryStatistics();
    List<Map<String, Object>> getLevelDistribution();
    List<Map<String, Object>> getCategoriesByProductCount();
    Long countProductsInCategory(Long categoryId);
    Long countSubcategories(Long categoryId);
    Map<String, Object> getDashboardStatistics();
}
