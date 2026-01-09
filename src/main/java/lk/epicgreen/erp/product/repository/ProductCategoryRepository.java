package lk.epicgreen.erp.product.repository;

import lk.epicgreen.erp.product.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ProductCategory entity
 * Based on ACTUAL database schema: product_categories table
 * 
 * Fields: category_code, category_name, parent_category_id, description, is_active
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>, JpaSpecificationExecutor<ProductCategory> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find category by category code
     */
    Optional<ProductCategory> findByCategoryCode(String categoryCode);
    
    /**
     * Find category by category name
     */
    Optional<ProductCategory> findByCategoryName(String categoryName);
    
    /**
     * Find all active categories
     */
    List<ProductCategory> findByIsActiveTrue();
    
    /**
     * Find all active categories with pagination
     */
    Page<ProductCategory> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Find all inactive categories
     */
    List<ProductCategory> findByIsActiveFalse();
    
    /**
     * Find root categories (parent_category_id IS NULL)
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.parentCategoryId IS NULL")
    List<ProductCategory> findRootCategories();
    
    /**
     * Find active root categories
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.parentCategoryId IS NULL AND pc.isActive = true")
    List<ProductCategory> findActiveRootCategories();
    
    /**
     * Find child categories by parent ID
     */
    List<ProductCategory> findByParentCategoryId(Long parentCategoryId);
    
    /**
     * Find active child categories by parent ID
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.parentCategoryId = :parentId AND pc.isActive = true")
    List<ProductCategory> findActiveChildCategories(@Param("parentId") Long parentId);
    
    /**
     * Find all subcategories (categories with parent)
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.parentCategoryId IS NOT NULL")
    List<ProductCategory> findAllSubcategories();
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if category code exists
     */
    boolean existsByCategoryCode(String categoryCode);
    
    /**
     * Check if category name exists
     */
    boolean existsByCategoryName(String categoryName);
    
    /**
     * Check if category code exists excluding specific category ID
     */
    boolean existsByCategoryCodeAndIdNot(String categoryCode, Long id);
    
    /**
     * Check if category name exists excluding specific category ID
     */
    boolean existsByCategoryNameAndIdNot(String categoryName, Long id);
    
    /**
     * Check if category has children
     */
    @Query("SELECT COUNT(pc) > 0 FROM ProductCategory pc WHERE pc.parentCategoryId = :categoryId")
    boolean hasChildren(@Param("categoryId") Long categoryId);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search categories by category code containing (case-insensitive)
     */
    Page<ProductCategory> findByCategoryCodeContainingIgnoreCase(String categoryCode, Pageable pageable);
    
    /**
     * Search categories by category name containing (case-insensitive)
     */
    Page<ProductCategory> findByCategoryNameContainingIgnoreCase(String categoryName, Pageable pageable);
    
    /**
     * Search active categories by keyword
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.isActive = true AND " +
           "(LOWER(pc.categoryCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(pc.categoryName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<ProductCategory> searchActiveCategories(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Search categories by multiple criteria
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE " +
           "(:categoryCode IS NULL OR LOWER(pc.categoryCode) LIKE LOWER(CONCAT('%', :categoryCode, '%'))) AND " +
           "(:categoryName IS NULL OR LOWER(pc.categoryName) LIKE LOWER(CONCAT('%', :categoryName, '%'))) AND " +
           "(:parentCategoryId IS NULL OR pc.parentCategoryId = :parentCategoryId) AND " +
           "(:isActive IS NULL OR pc.isActive = :isActive)")
    Page<ProductCategory> searchCategories(
            @Param("categoryCode") String categoryCode,
            @Param("categoryName") String categoryName,
            @Param("parentCategoryId") Long parentCategoryId,
            @Param("isActive") Boolean isActive,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count active categories
     */
    long countByIsActiveTrue();
    
    /**
     * Count inactive categories
     */
    long countByIsActiveFalse();
    
    /**
     * Count root categories
     */
    @Query("SELECT COUNT(pc) FROM ProductCategory pc WHERE pc.parentCategoryId IS NULL")
    long countRootCategories();
    
    /**
     * Count child categories for a parent
     */
    long countByParentCategoryId(Long parentCategoryId);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Get category hierarchy (all levels)
     */
    @Query("SELECT pc FROM ProductCategory pc ORDER BY pc.parentCategoryId NULLS FIRST, pc.categoryName")
    List<ProductCategory> findAllInHierarchicalOrder();
    
    /**
     * Find categories by depth level
     * Level 0 = root categories (no parent)
     * Level 1 = direct children of root
     * etc.
     */
    @Query(value = "WITH RECURSIVE category_hierarchy AS ( " +
           "  SELECT id, category_code, category_name, parent_category_id, 0 as level " +
           "  FROM product_categories WHERE parent_category_id IS NULL " +
           "  UNION ALL " +
           "  SELECT c.id, c.category_code, c.category_name, c.parent_category_id, ch.level + 1 " +
           "  FROM product_categories c " +
           "  INNER JOIN category_hierarchy ch ON c.parent_category_id = ch.id " +
           ") " +
           "SELECT id FROM category_hierarchy WHERE level = :level", 
           nativeQuery = true)
    List<Long> findCategoryIdsByLevel(@Param("level") int level);
    
    /**
     * Get category statistics
     */
    @Query("SELECT " +
           "COUNT(pc) as totalCategories, " +
           "SUM(CASE WHEN pc.isActive = true THEN 1 ELSE 0 END) as activeCategories, " +
           "SUM(CASE WHEN pc.parentCategoryId IS NULL THEN 1 ELSE 0 END) as rootCategories " +
           "FROM ProductCategory pc")
    Object getCategoryStatistics();
    
    /**
     * Find categories with product count
     */
    @Query("SELECT pc.id, pc.categoryName, COUNT(p) as productCount " +
           "FROM ProductCategory pc LEFT JOIN Product p ON p.categoryId = pc.id " +
           "GROUP BY pc.id, pc.categoryName ORDER BY productCount DESC")
    List<Object[]> findCategoriesWithProductCount();
    
    /**
     * Find empty categories (no products)
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE NOT EXISTS " +
           "(SELECT 1 FROM Product p WHERE p.categoryId = pc.id)")
    List<ProductCategory> findEmptyCategories();
    
    /**
     * Find leaf categories (no children)
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE NOT EXISTS " +
           "(SELECT 1 FROM ProductCategory child WHERE child.parentCategoryId = pc.id)")
    List<ProductCategory> findLeafCategories();
    
    /**
     * Find all categories ordered by code
     */
    List<ProductCategory> findAllByOrderByCategoryCodeAsc();
    
    /**
     * Find all active categories ordered by name
     */
    List<ProductCategory> findByIsActiveTrueOrderByCategoryNameAsc();
    
    /**
     * Find categories with parent and active status
     */
    Page<ProductCategory> findByParentCategoryIdAndIsActive(Long parentCategoryId, boolean isActive, Pageable pageable);

    long countProductsByCategoryId(Long id);

    Page<ProductCategory> searchCategories(String keyword, Pageable pageable);
}
