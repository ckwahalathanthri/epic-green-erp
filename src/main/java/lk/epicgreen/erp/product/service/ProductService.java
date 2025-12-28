package lk.epicgreen.erp.product.service;

import lk.epicgreen.erp.product.dto.request.ProductRequest;
import lk.epicgreen.erp.product.dto.response.ProductResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
}
