package lk.epicgreen.erp.product.service;

import lk.epicgreen.erp.product.dto.ProductRequest;
import lk.epicgreen.erp.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Product Service Interface
 * Service for product operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface ProductService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    Product createProduct(ProductRequest request);
    Product updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
    Product getProductById(Long id);
    Product getProductByCode(String productCode);
    Product getProductBySku(String sku);
    Product getProductByBarcode(String barcode);
    List<Product> getAllProducts();
    Page<Product> getAllProducts(Pageable pageable);
    Page<Product> searchProducts(String keyword, Pageable pageable);
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    Product activateProduct(Long id);
    Product deactivateProduct(Long id);
    Product discontinueProduct(Long id, String reason);
    Product featureProduct(Long id);
    Product unfeatureProduct(Long id);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    List<Product> getActiveProducts();
    Page<Product> getActiveProducts(Pageable pageable);
    List<Product> getInactiveProducts();
    List<Product> getDiscontinuedProducts();
    List<Product> getFeaturedProducts();
    Page<Product> getFeaturedProducts(Pageable pageable);
    List<Product> getSellableProducts();
    List<Product> getPurchasableProducts();
    List<Product> getTaxableProducts();
    List<Product> getProductsByCategory(Long categoryId);
    List<Product> getProductsByType(String productType);
    List<Product> getRawMaterialProducts();
    List<Product> getFinishedGoodsProducts();
    List<Product> getSemiFinishedProducts();
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByManufacturer(String manufacturer);
    List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice);
    List<Product> getProductsBelowPrice(Double maxPrice);
    List<Product> getProductsAbovePrice(Double minPrice);
    List<Product> getProductsByCostRange(Double minCost, Double maxCost);
    List<Product> getHighMarginProducts(Double marginPercentage);
    List<Product> getLowMarginProducts(Double marginPercentage);
    List<Product> getProductsByWeightRange(Double minWeight, Double maxWeight);
    List<Product> getRecentProducts(int limit);
    List<Product> getRecentlyUpdatedProducts(int limit);
    List<Product> getProductsByCategoryTree(List<Long> categoryIds);
    List<Product> getHighestPricedProducts(int limit);
    List<Product> getLowestPricedProducts(int limit);
    
    // ===================================================================
    // PRICE OPERATIONS
    // ===================================================================
    
    void updateSellingPrice(Long productId, Double newPrice);
    void updateCostPrice(Long productId, Double newCost);
    void updatePrices(Long productId, Double sellingPrice, Double costPrice);
    void applyDiscountPercentage(Long productId, Double discountPercentage);
    void applyDiscountAmount(Long productId, Double discountAmount);
    void removeDiscount(Long productId);
    void applyBulkPriceIncrease(List<Long> productIds, Double percentage);
    void applyBulkPriceDecrease(List<Long> productIds, Double percentage);
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    boolean validateProduct(Product product);
    boolean isProductCodeAvailable(String productCode);
    boolean isSkuAvailable(String sku);
    boolean isBarcodeAvailable(String barcode);
    boolean canDeleteProduct(Long productId);
    boolean canSellProduct(Long productId);
    boolean canPurchaseProduct(Long productId);
    
    // ===================================================================
    // CALCULATIONS
    // ===================================================================
    
    Double calculateMargin(Product product);
    Double calculateMarginPercentage(Product product);
    Double calculateDiscountedPrice(Product product);
    Map<String, Object> calculateProductMetrics(Long productId);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<Product> createBulkProducts(List<ProductRequest> requests);
    int activateBulkProducts(List<Long> productIds);
    int deactivateBulkProducts(List<Long> productIds);
    int deleteBulkProducts(List<Long> productIds);
    int updateBulkCategory(List<Long> productIds, Long categoryId);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    Map<String, Object> getProductStatistics();
    List<Map<String, Object>> getProductTypeDistribution();
    List<Map<String, Object>> getStatusDistribution();
    List<Map<String, Object>> getProductsByCategory();
    List<Map<String, Object>> getProductsByBrand();
    Double getAverageSellingPrice();
    Double getAverageCostPrice();
    Double getAverageMarginPercentage();
    Double getTotalProductValue();
    Map<String, Object> getDashboardStatistics();
}
