package lk.epicgreen.erp.product.repository;

import lk.epicgreen.erp.product.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * ProductCategory Repository
 * Repository for product category data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find category by code
     */
    Optional<ProductCategory> findByCategoryCode(String categoryCode);
    
    /**
     * Check if category exists by code
     */
    boolean existsByCategoryCode(String categoryCode);
    
    /**
     * Find category by name
     */
    Optional<ProductCategory> findByCategoryName(String categoryName);
    
    /**
     * Check if category exists by name
     */
    boolean existsByCategoryName(String categoryName);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find categories by parent ID
     */
    List<ProductCategory> findByParentCategoryId(Long parentCategoryId);
    
    /**
     * Find categories by is active
     */
    List<ProductCategory> findByIsActive(Boolean isActive);
    
    /**
     * Find categories by is leaf
     */
    List<ProductCategory> findByIsLeaf(Boolean isLeaf);
    
    /**
     * Find categories by level
     */
    List<ProductCategory> findByLevel(Integer level);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find categories by created at between dates
     */
    List<ProductCategory> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find categories by parent ID and is active
     */
    List<ProductCategory> findByParentCategoryIdAndIsActive(Long parentCategoryId, Boolean isActive);
    
    /**
     * Find categories by level and is active
     */
    List<ProductCategory> findByLevelAndIsActive(Integer level, Boolean isActive);
    
    /**
     * Find categories by is leaf and is active
     */
    List<ProductCategory> findByIsLeafAndIsActive(Boolean isLeaf, Boolean isActive);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search categories
     */
    @Query("SELECT c FROM ProductCategory c WHERE " +
           "LOWER(c.categoryCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ProductCategory> searchCategories(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find active categories
     */
    @Query("SELECT c FROM ProductCategory c WHERE c.isActive = true " +
           "ORDER BY c.displayOrder ASC, c.categoryName ASC")
    List<ProductCategory> findActiveCategories();
    
    /**
     * Find active categories with pagination
     */
    @Query("SELECT c FROM ProductCategory c WHERE c.isActive = true " +
           "ORDER BY c.displayOrder ASC, c.categoryName ASC")
    Page<ProductCategory> findActiveCategories(Pageable pageable);
    
    /**
     * Find inactive categories
     */
    @Query("SELECT c FROM ProductCategory c WHERE c.isActive = false " +
           "ORDER BY c.categoryName ASC")
    List<ProductCategory> findInactiveCategories();
    
    /**
     * Find root categories (no parent)
     */
    @Query("SELECT c FROM ProductCategory c WHERE c.parentCategoryId IS NULL " +
           "AND c.isActive = true ORDER BY c.displayOrder ASC, c.categoryName ASC")
    List<ProductCategory> findRootCategories();
    
    /**
     * Find leaf categories (no children)
     */
    @Query("SELECT c FROM ProductCategory c WHERE c.isLeaf = true " +
           "AND c.isActive = true ORDER BY c.categoryName ASC")
    List<ProductCategory> findLeafCategories();
    
    /**
     * Find categories with children
     */
    @Query("SELECT c FROM ProductCategory c WHERE c.isLeaf = false " +
           "AND c.isActive = true ORDER BY c.categoryName ASC")
    List<ProductCategory> findCategoriesWithChildren();
    
    /**
     * Find subcategories by parent ID
     */
    @Query("SELECT c FROM ProductCategory c WHERE c.parentCategoryId = :parentId " +
           "AND c.isActive = true ORDER BY c.displayOrder ASC, c.categoryName ASC")
    List<ProductCategory> findSubcategoriesByParent(@Param("parentId") Long parentId);
    
    /**
     * Find all subcategories by parent ID (recursive)
     */
    @Query("SELECT c FROM ProductCategory c WHERE c.categoryPath LIKE CONCAT(:parentPath, '%') " +
           "AND c.categoryId != :parentId AND c.isActive = true " +
           "ORDER BY c.level ASC, c.displayOrder ASC")
    List<ProductCategory> findAllSubcategoriesByParent(@Param("parentId") Long parentId,
                                                        @Param("parentPath") String parentPath);
    
    /**
     * Find category hierarchy (parent to root)
     */
    @Query("SELECT c FROM ProductCategory c WHERE c.categoryId IN :categoryIds " +
           "ORDER BY c.level ASC")
    List<ProductCategory> findCategoryHierarchy(@Param("categoryIds") List<Long> categoryIds);
    
    /**
     * Find categories by level
     */
    @Query("SELECT c FROM ProductCategory c WHERE c.level = :level " +
           "AND c.isActive = true ORDER BY c.displayOrder ASC, c.categoryName ASC")
    List<ProductCategory> findCategoriesByLevel(@Param("level") Integer level);
    
    /**
     * Find categories by max depth
     */
    @Query("SELECT c FROM ProductCategory c WHERE c.level <= :maxLevel " +
           "AND c.isActive = true ORDER BY c.level ASC, c.displayOrder ASC")
    List<ProductCategory> findCategoriesByMaxDepth(@Param("maxLevel") Integer maxLevel);
    
    /**
     * Find recent categories
     */
    @Query("SELECT c FROM ProductCategory c ORDER BY c.createdAt DESC")
    List<ProductCategory> findRecentCategories(Pageable pageable);
    
    /**
     * Find sibling categories
     */
    @Query("SELECT c FROM ProductCategory c WHERE c.parentCategoryId = :parentId " +
           "AND c.categoryId != :categoryId AND c.isActive = true " +
           "ORDER BY c.displayOrder ASC, c.categoryName ASC")
    List<ProductCategory> findSiblingCategories(@Param("categoryId") Long categoryId,
                                                @Param("parentId") Long parentId);
    
    /**
     * Find categories with products
     */
    @Query("SELECT c FROM ProductCategory c WHERE " +
           "EXISTS (SELECT 1 FROM Product p WHERE p.categoryId = c.categoryId) " +
           "AND c.isActive = true ORDER BY c.categoryName ASC")
    List<ProductCategory> findCategoriesWithProducts();
    
    /**
     * Find empty categories (no products)
     */
    @Query("SELECT c FROM ProductCategory c WHERE " +
           "NOT EXISTS (SELECT 1 FROM Product p WHERE p.categoryId = c.categoryId) " +
           "AND c.isActive = true ORDER BY c.categoryName ASC")
    List<ProductCategory> findEmptyCategories();
    
    /**
     * Get category tree starting from root
     */
    @Query("SELECT c FROM ProductCategory c WHERE c.isActive = true " +
           "ORDER BY c.level ASC, c.parentCategoryId ASC NULLS FIRST, " +
           "c.displayOrder ASC, c.categoryName ASC")
    List<ProductCategory> getCategoryTree();
    
    /**
     * Get category tree by parent
     */
    @Query("SELECT c FROM ProductCategory c WHERE " +
           "(c.categoryId = :parentId OR c.categoryPath LIKE CONCAT(:parentPath, '%')) " +
           "AND c.isActive = true " +
           "ORDER BY c.level ASC, c.displayOrder ASC, c.categoryName ASC")
    List<ProductCategory> getCategoryTreeByParent(@Param("parentId") Long parentId,
                                                   @Param("parentPath") String parentPath);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count categories by parent
     */
    @Query("SELECT COUNT(c) FROM ProductCategory c WHERE c.parentCategoryId = :parentId")
    Long countByParentCategoryId(@Param("parentId") Long parentId);
    
    /**
     * Count categories by level
     */
    @Query("SELECT COUNT(c) FROM ProductCategory c WHERE c.level = :level")
    Long countByLevel(@Param("level") Integer level);
    
    /**
     * Count active categories
     */
    @Query("SELECT COUNT(c) FROM ProductCategory c WHERE c.isActive = true")
    Long countActiveCategories();
    
    /**
     * Count inactive categories
     */
    @Query("SELECT COUNT(c) FROM ProductCategory c WHERE c.isActive = false")
    Long countInactiveCategories();
    
    /**
     * Count root categories
     */
    @Query("SELECT COUNT(c) FROM ProductCategory c WHERE c.parentCategoryId IS NULL")
    Long countRootCategories();
    
    /**
     * Count leaf categories
     */
    @Query("SELECT COUNT(c) FROM ProductCategory c WHERE c.isLeaf = true")
    Long countLeafCategories();
    
    /**
     * Get level distribution
     */
    @Query("SELECT c.level, COUNT(c) as categoryCount FROM ProductCategory c " +
           "WHERE c.isActive = true GROUP BY c.level ORDER BY c.level ASC")
    List<Object[]> getLevelDistribution();
    
    /**
     * Get categories by product count
     */
    @Query("SELECT c.categoryId, c.categoryName, COUNT(p) as productCount " +
           "FROM ProductCategory c LEFT JOIN Product p ON p.categoryId = c.categoryId " +
           "WHERE c.isActive = true " +
           "GROUP BY c.categoryId, c.categoryName ORDER BY productCount DESC")
    List<Object[]> getCategoriesByProductCount();
    
    /**
     * Get max depth
     */
    @Query("SELECT MAX(c.level) FROM ProductCategory c WHERE c.isActive = true")
    Integer getMaxDepth();
    
    /**
     * Get category with most products
     */
    @Query("SELECT c.categoryId, c.categoryName, COUNT(p) as productCount " +
           "FROM ProductCategory c LEFT JOIN Product p ON p.categoryId = c.categoryId " +
           "WHERE c.isActive = true " +
           "GROUP BY c.categoryId, c.categoryName ORDER BY productCount DESC")
    List<Object[]> getCategoryWithMostProducts(Pageable pageable);
    
    /**
     * Find categories by tags
     */
    @Query("SELECT c FROM ProductCategory c WHERE c.tags LIKE CONCAT('%', :tag, '%')")
    List<ProductCategory> findByTag(@Param("tag") String tag);
}
