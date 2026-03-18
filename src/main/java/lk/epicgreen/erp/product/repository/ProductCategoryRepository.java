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
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.categoryCode = :categoryCode")
    Optional<ProductCategory> findByCategoryCode(@Param("categoryCode") String categoryCode);

    /**
     * Find category by category name
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.categoryName = :categoryName")
    Optional<ProductCategory> findByCategoryName(@Param("categoryName") String categoryName);

    List<ProductCategory> findByParentCategoryIsNull();
    /**
     * Find all active categories
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.isActive = true")
    List<ProductCategory> findByIsActiveTrue();

    /**
     * Find all active categories with pagination
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.isActive = true")
    Page<ProductCategory> findByIsActiveTrue(Pageable pageable);

    /**
     * Find all inactive categories
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.isActive = false")
    List<ProductCategory> findByIsActiveFalse();

    /**
     * Find root categories (parent_category_id IS NULL)
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.parentCategory.id IS NULL")
    List<ProductCategory> findRootCategories();

    /**
     * Find active root categories
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.parentCategory.id IS NULL AND pc.isActive = true")
    List<ProductCategory> findActiveRootCategories();

    /**
     * Find child categories by parent ID
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.parentCategory.id = :parentCategoryId")
    List<ProductCategory> findByParentCategoryId(@Param("parentCategoryId") Long parentCategoryId);

    /**
     * Find active child categories by parent ID
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.parentCategory.id = :parentId AND pc.isActive = true")
    List<ProductCategory> findActiveChildCategories(@Param("parentId") Long parentId);

    /**
     * Find all subcategories (categories with parent)
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.parentCategory.id IS NOT NULL")
    List<ProductCategory> findAllSubcategories();

    // ==================== EXISTENCE CHECKS ====================

    /**
     * Check if category code exists
     */
    @Query("SELECT CASE WHEN COUNT(pc) > 0 THEN true ELSE false END FROM ProductCategory pc WHERE pc.categoryCode = :categoryCode")
    boolean existsByCategoryCode(@Param("categoryCode") String categoryCode);

    /**
     * Check if category name exists
     */
    @Query("SELECT CASE WHEN COUNT(pc) > 0 THEN true ELSE false END FROM ProductCategory pc WHERE pc.categoryName = :categoryName")
    boolean existsByCategoryName(@Param("categoryName") String categoryName);

    /**
     * Check if category code exists excluding specific category ID
     */
    @Query("SELECT CASE WHEN COUNT(pc) > 0 THEN true ELSE false END FROM ProductCategory pc WHERE pc.categoryCode = :categoryCode AND pc.id <> :id")
    boolean existsByCategoryCodeAndIdNot(@Param("categoryCode") String categoryCode, @Param("id") Long id);

    /**
     * Check if category name exists excluding specific category ID
     */
    @Query("SELECT CASE WHEN COUNT(pc) > 0 THEN true ELSE false END FROM ProductCategory pc WHERE pc.categoryName = :categoryName AND pc.id <> :id")
    boolean existsByCategoryNameAndIdNot(@Param("categoryName") String categoryName, @Param("id") Long id);

    /**
     * Check if category has children
     */
    @Query("SELECT COUNT(pc) > 0 FROM ProductCategory pc WHERE pc.parentCategory.id = :categoryId")
    boolean hasChildren(@Param("categoryId") Long categoryId);

    // ==================== SEARCH METHODS ====================

    /**
     * Search categories by category code containing (case-insensitive)
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE LOWER(pc.categoryCode) LIKE LOWER(CONCAT('%', :categoryCode, '%'))")
    Page<ProductCategory> findByCategoryCodeContainingIgnoreCase(@Param("categoryCode") String categoryCode, Pageable pageable);

    /**
     * Search categories by category name containing (case-insensitive)
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE LOWER(pc.categoryName) LIKE LOWER(CONCAT('%', :categoryName, '%'))")
    Page<ProductCategory> findByCategoryNameContainingIgnoreCase(@Param("categoryName") String categoryName, Pageable pageable);

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
            "(:parentCategoryId IS NULL OR pc.parentCategory.id = :parentCategoryId) AND " +
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
    @Query("SELECT COUNT(pc) FROM ProductCategory pc WHERE pc.isActive = true")
    long countByIsActiveTrue();

    /**
     * Count inactive categories
     */
    @Query("SELECT COUNT(pc) FROM ProductCategory pc WHERE pc.isActive = false")
    long countByIsActiveFalse();

    /**
     * Count root categories
     */
    @Query("SELECT COUNT(pc) FROM ProductCategory pc WHERE pc.parentCategory.id IS NULL")
    long countRootCategories();

    /**
     * Count child categories for a parent
     */
    @Query("SELECT COUNT(pc) FROM ProductCategory pc WHERE pc.parentCategory.id = :parentCategoryId")
    long countByParentCategoryId(@Param("parentCategoryId") Long parentCategoryId);

    // ==================== CUSTOM QUERIES ====================

    /**
     * Get category hierarchy (all levels)
     */
    @Query("SELECT pc FROM ProductCategory pc ORDER BY pc.parentCategory.id NULLS FIRST, pc.categoryName")
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
            "SUM(CASE WHEN pc.parentCategory.id IS NULL THEN 1 ELSE 0 END) as rootCategories " +
            "FROM ProductCategory pc")
    Object getCategoryStatistics();

    /**
     * Find categories with product count
     */
    @Query("SELECT pc.id, pc.categoryName, COUNT(p) as productCount " +
            "FROM ProductCategory pc LEFT JOIN Product p ON p.category.id = pc.id " +
            "GROUP BY pc.id, pc.categoryName ORDER BY productCount DESC")
    List<Object[]> findCategoriesWithProductCount();

    /**
     * Find empty categories (no products)
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE NOT EXISTS " +
            "(SELECT 1 FROM Product p WHERE p.category.id = pc.id)")
    List<ProductCategory> findEmptyCategories();

    /**
     * Find leaf categories (no children)
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE NOT EXISTS " +
            "(SELECT 1 FROM ProductCategory child WHERE child.parentCategory.id = pc.id)")
    List<ProductCategory> findLeafCategories();

    /**
     * Find all categories ordered by code
     */
    @Query("SELECT pc FROM ProductCategory pc ORDER BY pc.categoryCode ASC")
    List<ProductCategory> findAllByOrderByCategoryCodeAsc();

    /**
     * Find all active categories ordered by name
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.isActive = true ORDER BY pc.categoryName ASC")
    List<ProductCategory> findByIsActiveTrueOrderByCategoryNameAsc();

    /**
     * Find categories with parent and active status
     */
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.parentCategory.id = :parentCategoryId AND pc.isActive = :isActive")
    Page<ProductCategory> findByParentCategoryIdAndIsActive(@Param("parentCategoryId") Long parentCategoryId, @Param("isActive") boolean isActive, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.id = :id")
    long countProductsByCategoryId(@Param("id") Long id);

    @Query("SELECT pc FROM ProductCategory pc WHERE " +
            "LOWER(pc.categoryCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(pc.categoryName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ProductCategory> searchCategories(@Param("keyword") String keyword, Pageable pageable);
}