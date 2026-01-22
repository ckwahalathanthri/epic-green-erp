package lk.epicgreen.erp.product.repository;

import lk.epicgreen.erp.customer.entity.CustomerPriceList;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.entity.ProductCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
    @Query("SELECT p FROM Product p WHERE p.productCode = :productCode")
    Optional<Product> findByProductCode(@Param("productCode") String productCode);

    /**
     * Find product by product name
     */
    @Query("SELECT p FROM Product p WHERE p.productName = :productName")
    Optional<Product> findByProductName(@Param("productName") String productName);

    /**
     * Find product by barcode
     */
    @Query("SELECT p FROM Product p WHERE p.barcode = :barcode")
    Optional<Product> findByBarcode(@Param("barcode") String barcode);

    /**
     * Find product by SKU
     */
    @Query("SELECT p FROM Product p WHERE p.sku = :sku")
    Optional<Product> findBySku(@Param("sku") String sku);

    /**
     * Find all active products
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = true")
    List<Product> findByIsActiveTrue();

    /**
     * Find all active products with pagination
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = true")
    Page<Product> findByIsActiveTrue(Pageable pageable);

    /**
     * Find all inactive products
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = false")
    List<Product> findByIsActiveFalse();

    /**
     * Find products by product type
     */
    @Query("SELECT p FROM Product p WHERE p.productType = :productType")
    List<Product> findByProductType(@Param("productType") String productType);

    /**
     * Find products by product type with pagination
     */
    @Query("SELECT p FROM Product p WHERE p.productType = :productType")
    Page<Product> findByProductType(@Param("productType") String productType, Pageable pageable);

    /**
     * Find active products by type
     */
    @Query("SELECT p FROM Product p WHERE p.productType = :productType AND p.isActive = true")
    List<Product> findByProductTypeAndIsActiveTrue(@Param("productType") String productType);

    /**
     * Find products by category ID
     */
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * Find products by category ID with pagination
     */
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    Page<Product> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    /**
     * Find active products by category
     */
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.isActive = true")
    List<Product> findByCategoryIdAndIsActiveTrue(@Param("categoryId") Long categoryId);

    /**
     * Find products by base UOM ID
     */
    @Query("SELECT p FROM Product p WHERE p.baseUom.id = :baseUomId")
    List<Product> findByBaseUomId(@Param("baseUomId") Long baseUomId);

    // ==================== EXISTENCE CHECKS ====================

    /**
     * Check if product code exists
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.productCode = :productCode")
    boolean existsByProductCode(@Param("productCode") String productCode);

    /**
     * Check if product name exists
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.productName = :productName")
    boolean existsByProductName(@Param("productName") String productName);

    /**
     * Check if barcode exists
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.barcode = :barcode")
    boolean existsByBarcode(@Param("barcode") String barcode);

    /**
     * Check if SKU exists
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.sku = :sku")
    boolean existsBySku(@Param("sku") String sku);

    /**
     * Check if product code exists excluding specific product ID
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.productCode = :productCode AND p.id != :id")
    boolean existsByProductCodeAndIdNot(@Param("productCode") String productCode, @Param("id") Long id);

    /**
     * Check if barcode exists excluding specific product ID
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.barcode = :barcode AND p.id != :id")
    boolean existsByBarcodeAndIdNot(@Param("barcode") String barcode, @Param("id") Long id);

    /**
     * Check if SKU exists excluding specific product ID
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.sku = :sku AND p.id != :id")
    boolean existsBySkuAndIdNot(@Param("sku") String sku, @Param("id") Long id);

    // ==================== SEARCH METHODS ====================

    /**
     * Search products by product code containing (case-insensitive)
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.productCode) LIKE LOWER(CONCAT('%', :productCode, '%'))")
    Page<Product> findByProductCodeContainingIgnoreCase(@Param("productCode") String productCode, Pageable pageable);

    /**
     * Search products by product name containing (case-insensitive)
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :productName, '%'))")
    Page<Product> findByProductNameContainingIgnoreCase(@Param("productName") String productName, Pageable pageable);

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
            "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
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
    @Query("SELECT COUNT(p) FROM Product p WHERE p.isActive = true")
    long countByIsActiveTrue();

    /**
     * Count inactive products
     */
    @Query("SELECT COUNT(p) FROM Product p WHERE p.isActive = false")
    long countByIsActiveFalse();

    /**
     * Count products by type
     */
    @Query("SELECT COUNT(p) FROM Product p WHERE p.productType = :productType")
    long countByProductType(@Param("productType") String productType);

    /**
     * Count products by category
     */
    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.id = :categoryId")
    long countByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * Count active products by type
     */
    @Query("SELECT COUNT(p) FROM Product p WHERE p.productType = :productType AND p.isActive = true")
    long countByProductTypeAndIsActiveTrue(@Param("productType") String productType);

    // ==================== CUSTOM QUERIES ====================

    /**
     * Find products by HSN code
     */
    @Query("SELECT p FROM Product p WHERE p.hsnCode = :hsnCode")
    List<Product> findByHsnCode(@Param("hsnCode") String hsnCode);

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
            "JOIN Inventory i ON i.product.id = p.id " +
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
            "FROM Product p JOIN ProductCategory pc ON p.category.id = pc.id " +
            "WHERE p.isActive = true GROUP BY pc.id, pc.categoryName ORDER BY productCount ")
    List<Object[]> getProductsByCategory();

    /**
     * Find products without category
     */
    @Query("SELECT p FROM Product p WHERE p.category.id IS NULL")
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
    @Query("SELECT p FROM Product p ORDER BY p.productCode ASC")
    List<Product> findAllByOrderByProductCodeAsc();

    /**
     * Find active products ordered by name
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.productName ASC")
    List<Product> findByIsActiveTrueOrderByProductNameAsc();

    /**
     * Find products by type and category with pagination
     */
    @Query("SELECT p FROM Product p WHERE p.productType = :productType AND p.category.id = :categoryId AND p.isActive = :isActive")
    Page<Product> findByProductTypeAndCategoryIdAndIsActive(
            @Param("productType") String productType,
            @Param("categoryId") Long categoryId,
            @Param("isActive") boolean isActive,
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
    @Query("SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.sellingPrice ")
    List<Product> findTopSellingProducts(Pageable pageable);

    /**
     * Find products updated after specific date
     */
    @Query("SELECT p FROM Product p WHERE p.updatedAt >= :date")
    List<Product> findRecentlyUpdatedProducts(@Param("date") java.time.LocalDateTime date);

    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.deletedAt IS NULL")
    Optional<Product> findByIdAndDeletedAtIsNull(@Param("id") Long id);

    @Query("SELECT p FROM Product p WHERE p.productCode = :productCode AND p.deletedAt IS NULL")
    Optional<Product> findByProductCodeAndDeletedAtIsNull(@Param("productCode") String productCode);

    @Query("SELECT p FROM Product p WHERE p.barcode = :barcode AND p.deletedAt IS NULL")
    Optional<Product> findByBarcodeAndDeletedAtIsNull(@Param("barcode") String barcode);

    @Query("SELECT p FROM Product p WHERE p.sku = :sku AND p.deletedAt IS NULL")
    Optional<Product> findBySkuAndDeletedAtIsNull(@Param("sku") String sku);

    @Query("SELECT p FROM Product p WHERE p.deletedAt IS NULL")
    Page<Product> findByDeletedAtIsNull(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.deletedAt IS NULL")
    List<Product> findByIsActiveTrueAndDeletedAtIsNull();

    @Query("SELECT p FROM Product p WHERE p.productType = :productType AND p.deletedAt IS NULL")
    Page<Product> findByProductTypeAndDeletedAtIsNull(@Param("productType") String productType, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.deletedAt IS NULL")
    Page<Product> findByCategoryIdAndDeletedAtIsNull(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.productType = :productType AND p.isActive = true AND p.deletedAt IS NULL")
    List<Product> findByProductTypeAndIsActiveTrueAndDeletedAtIsNull(@Param("productType") String string);

    @Query("SELECT p FROM Product p WHERE p.deletedAt IS NULL AND " +
            "(LOWER(p.productCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.barcode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.sku) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN Inventory i ON i.product.id = p.id " +
            "WHERE i.quantity <= p.minimumStockLevel AND p.isActive = true AND p.deletedAt IS NULL " +
            "GROUP BY p.id HAVING SUM(i.quantity) <= p.minimumStockLevel")
    List<Product> findProductsBelowMinimumStock();

    @Query("SELECT AVG((p.sellingPrice - p.standardCost) / p.sellingPrice * 100) " +
            "FROM Product p WHERE p.sellingPrice > 0 AND p.isActive = true AND p.deletedAt IS NULL")
    Double findAverageMarginPercentage();

    @Query("SELECT p FROM Product p WHERE p.brand = :brand AND p.isActive = true AND p.deletedAt IS NULL")
    List<Product> findByBrandAndIsActiveTrueAndDeletedAtIsNull(@Param("brand") String brand);

    @Modifying
    @Query("UPDATE Product p SET p.isActive = true WHERE p.id IN :productIds")
    int activateProductsByIds(@Param("productIds") List<Long> productIds);

    @Query("SELECT p.brand as brand, COUNT(p) as count " +
            "FROM Product p WHERE p.deletedAt IS NULL GROUP BY p.brand ORDER BY COUNT(p) DESC")
    List<Map<String, Object>> findProductCountsByBrand();

    @Modifying
    @Query("UPDATE Product p SET p.category.id = :categoryId WHERE p.id IN :productIds")
    int updateCategoryForProducts(@Param("productIds") List<Long> productIds, @Param("categoryId") ProductCategory category);
}