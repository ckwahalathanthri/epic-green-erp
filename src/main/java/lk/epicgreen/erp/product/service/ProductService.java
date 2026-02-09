package lk.epicgreen.erp.product.service;

import lk.epicgreen.erp.product.dto.request.ProductRequest;
import lk.epicgreen.erp.product.dto.response.ProductResponse;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.common.dto.PageResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Service interface for Product entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface ProductService {

    /**
     * Create new product
     */
    ProductResponse createProduct(ProductRequest request);
   /**
     * Update existing product
     */
    ProductResponse updateProduct(Long id, ProductRequest request);

    /**
     * Activate product
     */
    void activateProduct(Long id);

    /**
     * Deactivate product
     */
    void deactivateProduct(Long id);

    /**
     * Delete product (soft delete)
     */
    void deleteProduct(Long id);

    /**
     * Get product by ID
     */
    ProductResponse getProductById(Long id);

    /**
     * Get product by code
     */
    ProductResponse getProductByCode(String productCode);

    /**
     * Get product by barcode
     */
    ProductResponse getProductByBarcode(String barcode);

    /**
     * Get product by SKU
     */
    ProductResponse getProductBySku(String sku);

    /**
     * Get all products
     */
    PageResponse<ProductResponse> getAllProducts(Pageable pageable);



    /**
     * Get all active products
     */
    List<ProductResponse> getAllActiveProducts();

    /**
     * Get products by type
     */
    PageResponse<ProductResponse> getProductsByType(String productType, Pageable pageable);

    /**
     * Get products by category
     */
    PageResponse<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable);

    /**
     * Get raw materials
     */
    List<ProductResponse> getRawMaterials();

    /**
     * Get finished goods
     */
    List<ProductResponse> getFinishedGoods();

    /**
     * Get semi-finished products
     */
    List<ProductResponse> getSemiFinishedProducts();

    /**
     * Get packaging materials
     */
    List<ProductResponse> getPackagingMaterials();

    /**
     * Search products
     */
    PageResponse<ProductResponse> searchProducts(String keyword, Pageable pageable);

    /**
     * Get products below reorder level
     */
    List<ProductResponse> getProductsBelowReorderLevel();

    /**
     * Get products below minimum stock level
     */
    List<ProductResponse> getProductsBelowMinimumStock();

    List<ProductResponse> getAllProducts();

    ProductResponse discontinueProduct(Long id, String reason);

    ProductResponse featureProduct(Long id);

    ProductResponse unfeatureProduct(Long id);

    PageResponse<ProductResponse> getActiveProducts(Pageable pageable);

    List<ProductResponse> getInactiveProducts();

    List<ProductResponse> getDiscontinuedProducts();

    PageResponse<ProductResponse> getFeaturedProducts(Pageable pageable);

    List<ProductResponse> getSellableProducts();

    List<ProductResponse> getPurchasableProducts();

    List<ProductResponse> getTaxableProducts();

    List<ProductResponse> getProductsByCategory(Long categoryId);

    List<ProductResponse> getProductsByType(String productType);

    List<ProductResponse> getRawMaterialProducts();

	List<ProductResponse> getFinishedGoodsProducts();

    List<ProductResponse> getProductsByBrand(String brand);

    List<ProductResponse> getProductsByManufacturer(String manufacturer);

    List<ProductResponse> getProductsByPriceRange(Double minPrice, Double maxPrice);

    List<ProductResponse> getProductsBelowPrice(Double maxPrice);

    List<ProductResponse> getProductsAbovePrice(Double minPrice);

	List<ProductResponse> getProductsByCostRange(Double minCost, Double maxCost);

    List<ProductResponse> getHighMarginProducts(Double marginPercentage);

    List<ProductResponse> getLowMarginProducts(Double marginPercentage);

    List<ProductResponse> getProductsByWeightRange(Double minWeight, Double maxWeight);

    List<ProductResponse> getRecentProducts(int limit);

    List<ProductResponse> getRecentlyUpdatedProducts(int limit);

    List<ProductResponse> getProductsByCategoryTree(List<Long> categoryIds);

    List<ProductResponse> getHighestPricedProducts(int limit);

    List<ProductResponse> getLowestPricedProducts(int limit);

    void updateSellingPrice(Long id, Double newPrice);

	void updateCostPrice(Long id, Double newCost);

	void updatePrices(Long id, Double sellingPrice, Double costPrice);

	void applyDiscountPercentage(Long id, Double discountPercentage);

	void applyDiscountAmount(Long id, Double discountAmount);

	void removeDiscount(Long id);

	void applyBulkPriceIncrease(List<Long> productIds, Double percentage);

	void applyBulkPriceDecrease(List<Long> productIds, Double percentage);

	boolean isProductCodeAvailable(String productCode);

	boolean isSkuAvailable(String sku);

	boolean isBarcodeAvailable(String barcode);

	boolean canDeleteProduct(Long id);

	boolean canSellProduct(Long id);

	boolean canPurchaseProduct(Long id);

    Double calculateMargin(ProductResponse product);

	Double calculateMarginPercentage(ProductResponse product);

	BigDecimal calculateDiscountedPrice(ProductResponse product);

	Map<String, Object> calculateProductMetrics(Long id);

	List<ProductResponse> createBulkProducts(List<ProductRequest> requests);

    int activateBulkProducts(List<Long> productIds);

	int deactivateBulkProducts(List<Long> productIds);

	int deleteBulkProducts(List<Long> productIds);

	int updateBulkCategory(List<Long> productIds, Long categoryId);

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
