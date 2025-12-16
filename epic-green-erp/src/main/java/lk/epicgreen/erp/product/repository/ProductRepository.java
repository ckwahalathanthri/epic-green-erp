package lk.epicgreen.erp.product.repository;

import lk.epicgreen.erp.product.entity.Product;
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
 * Product Repository
 * Repository for product data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find product by code
     */
    Optional<Product> findByProductCode(String productCode);
    
    /**
     * Check if product exists by code
     */
    boolean existsByProductCode(String productCode);
    
    /**
     * Find product by SKU
     */
    Optional<Product> findBySku(String sku);
    
    /**
     * Check if product exists by SKU
     */
    boolean existsBySku(String sku);
    
    /**
     * Find product by barcode
     */
    Optional<Product> findByBarcode(String barcode);
    
    /**
     * Check if product exists by barcode
     */
    boolean existsByBarcode(String barcode);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find products by category ID
     */
    List<Product> findByCategoryId(Long categoryId);
    
    /**
     * Find products by category ID with pagination
     */
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    
    /**
     * Find products by product type
     */
    List<Product> findByProductType(String productType);
    
    /**
     * Find products by status
     */
    List<Product> findByStatus(String status);
    
    /**
     * Find products by status with pagination
     */
    Page<Product> findByStatus(String status, Pageable pageable);
    
    /**
     * Find products by is active
     */
    List<Product> findByIsActive(Boolean isActive);
    
    /**
     * Find products by is featured
     */
    List<Product> findByIsFeatured(Boolean isFeatured);
    
    /**
     * Find products by is taxable
     */
    List<Product> findByIsTaxable(Boolean isTaxable);
    
    /**
     * Find products by can be sold
     */
    List<Product> findByCanBeSold(Boolean canBeSold);
    
    /**
     * Find products by can be purchased
     */
    List<Product> findByCanBePurchased(Boolean canBePurchased);
    
    /**
     * Find products by brand
     */
    List<Product> findByBrand(String brand);
    
    /**
     * Find products by manufacturer
     */
    List<Product> findByManufacturer(String manufacturer);
    
    /**
     * Find products by unit of measure
     */
    List<Product> findByUnitOfMeasure(String unitOfMeasure);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find products by created at between dates
     */
    List<Product> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find products by updated at between dates
     */
    List<Product> findByUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find products by category and status
     */
    List<Product> findByCategoryIdAndStatus(Long categoryId, String status);
    
    /**
     * Find products by category and is active
     */
    List<Product> findByCategoryIdAndIsActive(Long categoryId, Boolean isActive);
    
    /**
     * Find products by product type and status
     */
    List<Product> findByProductTypeAndStatus(String productType, String status);
    
    /**
     * Find products by status and is active
     */
    List<Product> findByStatusAndIsActive(String status, Boolean isActive);
    
    /**
     * Find products by brand and is active
     */
    List<Product> findByBrandAndIsActive(String brand, Boolean isActive);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search products
     */
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.productCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.sku) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.barcode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find active products
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.status = 'ACTIVE' " +
           "ORDER BY p.productName ASC")
    List<Product> findActiveProducts();
    
    /**
     * Find active products with pagination
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.status = 'ACTIVE' " +
           "ORDER BY p.productName ASC")
    Page<Product> findActiveProducts(Pageable pageable);
    
    /**
     * Find inactive products
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = false OR p.status = 'INACTIVE' " +
           "ORDER BY p.productName ASC")
    List<Product> findInactiveProducts();
    
    /**
     * Find discontinued products
     */
    @Query("SELECT p FROM Product p WHERE p.status = 'DISCONTINUED' " +
           "ORDER BY p.updatedAt DESC")
    List<Product> findDiscontinuedProducts();
    
    /**
     * Find featured products
     */
    @Query("SELECT p FROM Product p WHERE p.isFeatured = true AND p.isActive = true " +
           "ORDER BY p.displayOrder ASC, p.productName ASC")
    List<Product> findFeaturedProducts();
    
    /**
     * Find featured products with pagination
     */
    @Query("SELECT p FROM Product p WHERE p.isFeatured = true AND p.isActive = true " +
           "ORDER BY p.displayOrder ASC, p.productName ASC")
    Page<Product> findFeaturedProducts(Pageable pageable);
    
    /**
     * Find sellable products
     */
    @Query("SELECT p FROM Product p WHERE p.canBeSold = true AND p.isActive = true " +
           "ORDER BY p.productName ASC")
    List<Product> findSellableProducts();
    
    /**
     * Find purchasable products
     */
    @Query("SELECT p FROM Product p WHERE p.canBePurchased = true AND p.isActive = true " +
           "ORDER BY p.productName ASC")
    List<Product> findPurchasableProducts();
    
    /**
     * Find taxable products
     */
    @Query("SELECT p FROM Product p WHERE p.isTaxable = true AND p.isActive = true " +
           "ORDER BY p.productName ASC")
    List<Product> findTaxableProducts();
    
    /**
     * Find raw material products
     */
    @Query("SELECT p FROM Product p WHERE p.productType = 'RAW_MATERIAL' " +
           "AND p.isActive = true ORDER BY p.productName ASC")
    List<Product> findRawMaterialProducts();
    
    /**
     * Find finished goods products
     */
    @Query("SELECT p FROM Product p WHERE p.productType = 'FINISHED_GOODS' " +
           "AND p.isActive = true ORDER BY p.productName ASC")
    List<Product> findFinishedGoodsProducts();
    
    /**
     * Find semi-finished products
     */
    @Query("SELECT p FROM Product p WHERE p.productType = 'SEMI_FINISHED' " +
           "AND p.isActive = true ORDER BY p.productName ASC")
    List<Product> findSemiFinishedProducts();
    
    /**
     * Find products by price range
     */
    @Query("SELECT p FROM Product p WHERE p.sellingPrice BETWEEN :minPrice AND :maxPrice " +
           "AND p.isActive = true ORDER BY p.sellingPrice ASC")
    List<Product> findByPriceRange(@Param("minPrice") Double minPrice,
                                   @Param("maxPrice") Double maxPrice);
    
    /**
     * Find products below price
     */
    @Query("SELECT p FROM Product p WHERE p.sellingPrice < :maxPrice " +
           "AND p.isActive = true ORDER BY p.sellingPrice ASC")
    List<Product> findBelowPrice(@Param("maxPrice") Double maxPrice);
    
    /**
     * Find products above price
     */
    @Query("SELECT p FROM Product p WHERE p.sellingPrice > :minPrice " +
           "AND p.isActive = true ORDER BY p.sellingPrice DESC")
    List<Product> findAbovePrice(@Param("minPrice") Double minPrice);
    
    /**
     * Find products by cost range
     */
    @Query("SELECT p FROM Product p WHERE p.costPrice BETWEEN :minCost AND :maxCost " +
           "AND p.isActive = true ORDER BY p.costPrice ASC")
    List<Product> findByCostRange(@Param("minCost") Double minCost,
                                  @Param("maxCost") Double maxCost);
    
    /**
     * Find high margin products
     */
    @Query("SELECT p FROM Product p WHERE " +
           "((p.sellingPrice - p.costPrice) / p.sellingPrice) * 100 > :marginPercentage " +
           "AND p.isActive = true ORDER BY " +
           "((p.sellingPrice - p.costPrice) / p.sellingPrice) * 100 DESC")
    List<Product> findHighMarginProducts(@Param("marginPercentage") Double marginPercentage);
    
    /**
     * Find low margin products
     */
    @Query("SELECT p FROM Product p WHERE " +
           "((p.sellingPrice - p.costPrice) / p.sellingPrice) * 100 < :marginPercentage " +
           "AND p.isActive = true ORDER BY " +
           "((p.sellingPrice - p.costPrice) / p.sellingPrice) * 100 ASC")
    List<Product> findLowMarginProducts(@Param("marginPercentage") Double marginPercentage);
    
    /**
     * Find products with weight range
     */
    @Query("SELECT p FROM Product p WHERE p.weight BETWEEN :minWeight AND :maxWeight " +
           "AND p.isActive = true ORDER BY p.weight ASC")
    List<Product> findByWeightRange(@Param("minWeight") Double minWeight,
                                    @Param("maxWeight") Double maxWeight);
    
    /**
     * Find recent products
     */
    @Query("SELECT p FROM Product p ORDER BY p.createdAt DESC")
    List<Product> findRecentProducts(Pageable pageable);
    
    /**
     * Find recently updated products
     */
    @Query("SELECT p FROM Product p ORDER BY p.updatedAt DESC")
    List<Product> findRecentlyUpdatedProducts(Pageable pageable);
    
    /**
     * Find products by category tree
     */
    @Query("SELECT p FROM Product p WHERE p.categoryId IN :categoryIds " +
           "AND p.isActive = true ORDER BY p.productName ASC")
    List<Product> findByCategoryTree(@Param("categoryIds") List<Long> categoryIds);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count products by category
     */
    @Query("SELECT COUNT(p) FROM Product p WHERE p.categoryId = :categoryId")
    Long countByCategoryId(@Param("categoryId") Long categoryId);
    
    /**
     * Count products by product type
     */
    @Query("SELECT COUNT(p) FROM Product p WHERE p.productType = :productType")
    Long countByProductType(@Param("productType") String productType);
    
    /**
     * Count products by status
     */
    @Query("SELECT COUNT(p) FROM Product p WHERE p.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count active products
     */
    @Query("SELECT COUNT(p) FROM Product p WHERE p.isActive = true")
    Long countActiveProducts();
    
    /**
     * Count inactive products
     */
    @Query("SELECT COUNT(p) FROM Product p WHERE p.isActive = false")
    Long countInactiveProducts();
    
    /**
     * Count featured products
     */
    @Query("SELECT COUNT(p) FROM Product p WHERE p.isFeatured = true")
    Long countFeaturedProducts();
    
    /**
     * Get product type distribution
     */
    @Query("SELECT p.productType, COUNT(p) as productCount FROM Product p " +
           "GROUP BY p.productType ORDER BY productCount DESC")
    List<Object[]> getProductTypeDistribution();
    
    /**
     * Get status distribution
     */
    @Query("SELECT p.status, COUNT(p) as productCount FROM Product p " +
           "GROUP BY p.status ORDER BY productCount DESC")
    List<Object[]> getStatusDistribution();
    
    /**
     * Get products by category
     */
    @Query("SELECT p.categoryId, p.categoryName, COUNT(p) as productCount FROM Product p " +
           "WHERE p.isActive = true GROUP BY p.categoryId, p.categoryName " +
           "ORDER BY productCount DESC")
    List<Object[]> getProductsByCategory();
    
    /**
     * Get products by brand
     */
    @Query("SELECT p.brand, COUNT(p) as productCount FROM Product p " +
           "WHERE p.isActive = true AND p.brand IS NOT NULL " +
           "GROUP BY p.brand ORDER BY productCount DESC")
    List<Object[]> getProductsByBrand();
    
    /**
     * Get average selling price
     */
    @Query("SELECT AVG(p.sellingPrice) FROM Product p WHERE p.isActive = true")
    Double getAverageSellingPrice();
    
    /**
     * Get average cost price
     */
    @Query("SELECT AVG(p.costPrice) FROM Product p WHERE p.isActive = true")
    Double getAverageCostPrice();
    
    /**
     * Get average margin percentage
     */
    @Query("SELECT AVG(((p.sellingPrice - p.costPrice) / p.sellingPrice) * 100) " +
           "FROM Product p WHERE p.isActive = true AND p.sellingPrice > 0")
    Double getAverageMarginPercentage();
    
    /**
     * Get total product value
     */
    @Query("SELECT SUM(p.sellingPrice) FROM Product p WHERE p.isActive = true")
    Double getTotalProductValue();
    
    /**
     * Get highest priced products
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = true " +
           "ORDER BY p.sellingPrice DESC")
    List<Product> getHighestPricedProducts(Pageable pageable);
    
    /**
     * Get lowest priced products
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = true " +
           "ORDER BY p.sellingPrice ASC")
    List<Product> getLowestPricedProducts(Pageable pageable);
    
    /**
     * Find products by tags
     */
    @Query("SELECT p FROM Product p WHERE p.tags LIKE CONCAT('%', :tag, '%')")
    List<Product> findByTag(@Param("tag") String tag);
}
