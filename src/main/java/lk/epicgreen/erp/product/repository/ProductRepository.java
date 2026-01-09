package lk.epicgreen.erp.product.repository;

import lk.epicgreen.erp.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Product entity
 * Based on ACTUAL database schema: products table
 * 
 * Fields: product_code, product_name, product_type (ENUM), category_id, base_uom_id,
 *         barcode, sku, hsn_code, reorder_level, minimum_stock_level, maximum_stock_level,
 *         standard_cost, selling_price, shelf_life_days, is_active, image_url, deleted_at
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find product by product code
     */
    Optional<Product> findByProductCode(String productCode);
    
    /**
     * Find product by product name
     */
    Optional<Product> findByProductName(String productName);
    
    /**
     * Find product by barcode
     */
    Optional<Product> findByBarcode(String barcode);
    
    /**
     * Find product by SKU
     */
    Optional<Product> findBySku(String sku);
    
    /**
     * Find all active products
     */
    List<Product> findByIsActiveTrue();
    
    /**
     * Find all active products with pagination
     */
    Page<Product> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Find all inactive products
     */
    List<Product> findByIsActiveFalse();
    
    /**
     * Find products by product type
     */
    List<Product> findByProductType(String productType);
    
    /**
     * Find products by product type with pagination
     */
    Page<Product> findByProductType(String productType, Pageable pageable);
    
    /**
     * Find active products by type
     */
    List<Product> findByProductTypeAndIsActiveTrue(String productType);
    
    /**
     * Find products by category ID
     */
    List<Product> findByCategoryId(Long categoryId);
    
    /**
     * Find products by category ID with pagination
     */
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    
    /**
     * Find active products by category
     */
    List<Product> findByCategoryIdAndIsActiveTrue(Long categoryId);
    
    /**
     * Find products by base UOM ID
     */
    List<Product> findByBaseUomId(Long baseUomId);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if product code exists
     */
    boolean existsByProductCode(String productCode);
    
    /**
     * Check if product name exists
     */
    boolean existsByProductName(String productName);
    
    /**
     * Check if barcode exists
     */
    boolean existsByBarcode(String barcode);
    
    /**
     * Check if SKU exists
     */
    boolean existsBySku(String sku);
    
    /**
     * Check if product code exists excluding specific product ID
     */
    boolean existsByProductCodeAndIdNot(String productCode, Long id);
    
    /**
     * Check if barcode exists excluding specific product ID
     */
    boolean existsByBarcodeAndIdNot(String barcode, Long id);
    
    /**
     * Check if SKU exists excluding specific product ID
     */
    boolean existsBySkuAndIdNot(String sku, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search products by product code containing (case-insensitive)
     */
    Page<Product> findByProductCodeContainingIgnoreCase(String productCode, Pageable pageable);
    
    /**
     * Search products by product name containing (case-insensitive)
     */
    Page<Product> findByProductNameContainingIgnoreCase(String productName, Pageable pageable);
    
    /**
     * Search active products by keyword
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND " +
           "(LOWER(p.productCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.barcode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.sku) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> searchActiveProducts(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Search products by multiple criteria
     */
    @Query("SELECT p FROM Product p WHERE " +
           "(:productCode IS NULL OR LOWER(p.productCode) LIKE LOWER(CONCAT('%', :productCode, '%'))) AND " +
           "(:productName IS NULL OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :productName, '%'))) AND " +
           "(:productType IS NULL OR p.productType = :productType) AND " +
           "(:categoryId IS NULL OR p.categoryId = :categoryId) AND " +
           "(:isActive IS NULL OR p.isActive = :isActive)")
    Page<Product> searchProducts(
            @Param("productCode") String productCode,
            @Param("productName") String productName,
            @Param("productType") String productType,
            @Param("categoryId") Long categoryId,
            @Param("isActive") Boolean isActive,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count active products
     */
    long countByIsActiveTrue();
    
    /**
     * Count inactive products
     */
    long countByIsActiveFalse();
    
    /**
     * Count products by type
     */
    long countByProductType(String productType);
    
    /**
     * Count products by category
     */
    long countByCategoryId(Long categoryId);
    
    /**
     * Count active products by type
     */
    long countByProductTypeAndIsActiveTrue(String productType);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find products by HSN code
     */
    List<Product> findByHsnCode(String hsnCode);
    
    /**
     * Find products with selling price in range
     */
    @Query("SELECT p FROM Product p WHERE p.sellingPrice BETWEEN :minPrice AND :maxPrice AND p.isActive = true")
    List<Product> findBySellingPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    
    /**
     * Find products with standard cost in range
     */
    @Query("SELECT p FROM Product p WHERE p.standardCost BETWEEN :minCost AND :maxCost AND p.isActive = true")
    List<Product> findByStandardCostRange(@Param("minCost") BigDecimal minCost, @Param("maxCost") BigDecimal maxCost);
    
    /**
     * Find products below reorder level
     * Note: This query joins with inventory to check actual stock
     */
    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN Inventory i ON i.productId = p.id " +
           "WHERE i.quantity <= p.reorderLevel AND p.isActive = true " +
           "GROUP BY p.id HAVING SUM(i.quantity) <= p.reorderLevel")
    List<Product> findProductsBelowReorderLevel();
    
    /**
     * Find products with shelf life
     */
    @Query("SELECT p FROM Product p WHERE p.shelfLifeDays IS NOT NULL AND p.isActive = true")
    List<Product> findProductsWithShelfLife();
    
    /**
     * Find products with shelf life less than X days
     */
    @Query("SELECT p FROM Product p WHERE p.shelfLifeDays < :days AND p.isActive = true")
    List<Product> findProductsWithShortShelfLife(@Param("days") int days);
    
    /**
     * Get product statistics
     */
    @Query("SELECT " +
           "COUNT(p) as totalProducts, " +
           "SUM(CASE WHEN p.isActive = true THEN 1 ELSE 0 END) as activeProducts, " +
           "SUM(CASE WHEN p.productType = 'RAW_MATERIAL' THEN 1 ELSE 0 END) as rawMaterials, " +
           "SUM(CASE WHEN p.productType = 'FINISHED_GOOD' THEN 1 ELSE 0 END) as finishedGoods, " +
           "SUM(CASE WHEN p.productType = 'SEMI_FINISHED' THEN 1 ELSE 0 END) as semiFinished, " +
           "SUM(CASE WHEN p.productType = 'PACKAGING' THEN 1 ELSE 0 END) as packaging " +
           "FROM Product p")
    Object getProductStatistics();
    
    /**
     * Get products grouped by type
     */
    @Query("SELECT p.productType, COUNT(p) as productCount, AVG(p.sellingPrice) as avgPrice " +
           "FROM Product p WHERE p.isActive = true GROUP BY p.productType")
    List<Object[]> getProductsByType();
    
    /**
     * Get products grouped by category
     */
    @Query("SELECT pc.categoryName, COUNT(p) as productCount " +
           "FROM Product p JOIN ProductCategory pc ON p.categoryId = pc.id " +
           "WHERE p.isActive = true GROUP BY pc.id, pc.categoryName ORDER BY productCount DESC")
    List<Object[]> getProductsByCategory();
    
    /**
     * Find products without category
     */
    @Query("SELECT p FROM Product p WHERE p.categoryId IS NULL")
    List<Product> findProductsWithoutCategory();
    
    /**
     * Find products with image
     */
    @Query("SELECT p FROM Product p WHERE p.imageUrl IS NOT NULL AND p.isActive = true")
    List<Product> findProductsWithImage();
    
    /**
     * Find products without image
     */
    @Query("SELECT p FROM Product p WHERE p.imageUrl IS NULL AND p.isActive = true")
    List<Product> findProductsWithoutImage();
    
    /**
     * Find raw materials
     */
    @Query("SELECT p FROM Product p WHERE p.productType = 'RAW_MATERIAL' AND p.isActive = true")
    List<Product> findRawMaterials();
    
    /**
     * Find finished goods
     */
    @Query("SELECT p FROM Product p WHERE p.productType = 'FINISHED_GOOD' AND p.isActive = true")
    List<Product> findFinishedGoods();
    
    /**
     * Find semi-finished products
     */
    @Query("SELECT p FROM Product p WHERE p.productType = 'SEMI_FINISHED' AND p.isActive = true")
    List<Product> findSemiFinishedProducts();
    
    /**
     * Find packaging materials
     */
    @Query("SELECT p FROM Product p WHERE p.productType = 'PACKAGING' AND p.isActive = true")
    List<Product> findPackagingMaterials();
    
    /**
     * Find all products ordered by code
     */
    List<Product> findAllByOrderByProductCodeAsc();
    
    /**
     * Find active products ordered by name
     */
    List<Product> findByIsActiveTrueOrderByProductNameAsc();
    
    /**
     * Find products by type and category with pagination
     */
    Page<Product> findByProductTypeAndCategoryIdAndIsActive(
            String productType, 
            Long categoryId, 
            boolean isActive, 
            Pageable pageable);
    
    /**
     * Find products by price range with pagination
     */
    @Query("SELECT p FROM Product p WHERE p.sellingPrice BETWEEN :minPrice AND :maxPrice " +
           "AND p.isActive = true ORDER BY p.sellingPrice")
    Page<Product> findByPriceRange(
            @Param("minPrice") BigDecimal minPrice, 
            @Param("maxPrice") BigDecimal maxPrice, 
            Pageable pageable);
    
    /**
     * Find top selling products (most expensive)
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.sellingPrice DESC")
    List<Product> findTopSellingProducts(Pageable pageable);
    
    /**
     * Find products updated after specific date
     */
    @Query("SELECT p FROM Product p WHERE p.updatedAt >= :date")
    List<Product> findRecentlyUpdatedProducts(@Param("date") java.time.LocalDateTime date);

    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.deletedAt IS NULL")
    Product findByIdAndDeletedAtIsNull(Long id);
}
