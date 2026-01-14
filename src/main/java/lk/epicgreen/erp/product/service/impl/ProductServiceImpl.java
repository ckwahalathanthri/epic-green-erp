package lk.epicgreen.erp.product.service.impl;

import lk.epicgreen.erp.product.dto.request.ProductRequest;
import lk.epicgreen.erp.product.dto.response.ProductResponse;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.entity.ProductCategory;
import lk.epicgreen.erp.product.mapper.ProductMapper;
import lk.epicgreen.erp.product.repository.ProductRepository;
import lk.epicgreen.erp.product.repository.ProductCategoryRepository;
import lk.epicgreen.erp.product.service.ProductService;
import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.admin.repository.UnitOfMeasureRepository;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of ProductService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating new product: {}", request.getProductCode());

        // Validate unique constraints
        validateUniqueProductCode(request.getProductCode(), null);
        
        if (request.getBarcode() != null) {
            validateUniqueBarcode(request.getBarcode(), null);
        }
        
        if (request.getSku() != null) {
            validateUniqueSku(request.getSku(), null);
        }

        // Create product entity
        Product product = productMapper.toEntity(request);

        // Set category if provided
        if (request.getCategoryId() != null) {
            ProductCategory category = productCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + request.getCategoryId()));
            product.setCategory(category);
        }

        // Set base UOM
        UnitOfMeasure baseUom = unitOfMeasureRepository.findById(request.getBaseUomId())
            .orElseThrow(() -> new ResourceNotFoundException("Unit of measure not found: " + request.getBaseUomId()));
        product.setBaseUom(baseUom);

        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully: {}", savedProduct.getProductCode());

        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        log.info("Updating product: {}", id);

        Product product = findProductById(id);

        // Validate unique constraints
        validateUniqueProductCode(request.getProductCode(), id);
        
        if (request.getBarcode() != null) {
            validateUniqueBarcode(request.getBarcode(), id);
        }
        
        if (request.getSku() != null) {
            validateUniqueSku(request.getSku(), id);
        }

        // Update fields
        productMapper.updateEntityFromRequest(request, product);

        // Update category if provided
        if (request.getCategoryId() != null) {
            ProductCategory category = productCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + request.getCategoryId()));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        // Update base UOM
        UnitOfMeasure baseUom = unitOfMeasureRepository.findById(request.getBaseUomId())
            .orElseThrow(() -> new ResourceNotFoundException("Unit of measure not found: " + request.getBaseUomId()));
        product.setBaseUom(baseUom);

        Product updatedProduct = productRepository.save(product);
        log.info("Product updated successfully: {}", updatedProduct.getProductCode());

        return productMapper.toResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void activateProduct(Long id) {
        log.info("Activating product: {}", id);

        Product product = findProductById(id);
        product.setIsActive(true);
        productRepository.save(product);

        log.info("Product activated successfully: {}", id);
    }

    @Override
    @Transactional
    public void deactivateProduct(Long id) {
        log.info("Deactivating product: {}", id);

        Product product = findProductById(id);
        product.setIsActive(false);
        productRepository.save(product);

        log.info("Product deactivated successfully: {}", id);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        log.info("Deleting product (soft delete): {}", id);

        Product product = findProductById(id);
        product.setDeletedAt(LocalDateTime.now());
        product.setIsActive(false);
        productRepository.save(product);

        log.info("Product deleted successfully: {}", id);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = findProductById(id);
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse getProductByCode(String productCode) {
        Product product = productRepository.findByProductCodeAndDeletedAtIsNull(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse getProductByBarcode(String barcode) {
        Product product = productRepository.findByBarcodeAndDeletedAtIsNull(barcode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with barcode: " + barcode));
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse getProductBySku(String sku) {
        Product product = productRepository.findBySkuAndDeletedAtIsNull(sku)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU: " + sku));
        return productMapper.toResponse(product);
    }

    @Override
    public PageResponse<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findByDeletedAtIsNull(pageable);
        return createPageResponse(productPage);
    }

    @Override
    public List<ProductResponse> getAllActiveProducts() {
        List<Product> products = productRepository.findByIsActiveTrueAndDeletedAtIsNull();
        return products.stream()
            .map(productMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<ProductResponse> getProductsByType(String productType, Pageable pageable) {
        Page<Product> productPage = productRepository.findByProductTypeAndDeletedAtIsNull(productType, pageable);
        return createPageResponse(productPage);
    }

    @Override
    public PageResponse<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        Page<Product> productPage = productRepository.findByCategoryIdAndDeletedAtIsNull(categoryId, pageable);
        return createPageResponse(productPage);
    }

    @Override
    public List<ProductResponse> getRawMaterials() {
        List<Product> products = productRepository.findByProductTypeAndIsActiveTrueAndDeletedAtIsNull("RAW_MATERIAL");
        return products.stream()
            .map(productMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getFinishedGoods() {
        List<Product> products = productRepository.findByProductTypeAndIsActiveTrueAndDeletedAtIsNull("FINISHED_GOOD");
        return products.stream()
            .map(productMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getSemiFinishedProducts() {
        List<Product> products = productRepository.findByProductTypeAndIsActiveTrueAndDeletedAtIsNull("SEMI_FINISHED");
        return products.stream()
            .map(productMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getPackagingMaterials() {
        List<Product> products = productRepository.findByProductTypeAndIsActiveTrueAndDeletedAtIsNull("PACKAGING");
        return products.stream()
            .map(productMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        Page<Product> productPage = productRepository.searchProducts(keyword, pageable);
        return createPageResponse(productPage);
    }

    @Override
    public List<ProductResponse> getProductsBelowReorderLevel() {
        List<Product> products = productRepository.findProductsBelowReorderLevel();
        return products.stream()
            .map(productMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsBelowMinimumStock() {
        List<Product> products = productRepository.findProductsBelowMinimumStock();
        return products.stream()
            .map(productMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public ProductResponse unfeatureProduct(Long id) {
        log.info("Unfeaturing product: {}", id);

        Product product = findProductById(id);
        product.setIsFeatured(false);
        Product updatedProduct = productRepository.save(product);

        log.info("Product unfeatured successfully: {}", id);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public BigDecimal calculateDiscountedPrice(ProductResponse product){
        BigDecimal discountAmount = product.getDiscountAmount() != null ? BigDecimal.valueOf(product.getDiscountAmount()) : BigDecimal.ZERO;
        return product.getSellingPrice().subtract(discountAmount);
    }

    @Override
    public Double getAverageMarginPercentage(){
        Double averageMargin = productRepository.findAverageMarginPercentage();
        return averageMargin != null ? averageMargin : 0.0;
    }

    @Override
    public List<ProductResponse> getProductsByBrand(String brand){
        List<Product> products = productRepository.findByBrandAndIsActiveTrueAndDeletedAtIsNull(brand);
        return products.stream()
            .map(productMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getProductsByBrand(){
        return productRepository.findProductCountsByBrand();
    }

    @Override
    public int activateBulkProducts(List<Long> productIds){
        log.info("Activating bulk products: {}", productIds);
        int updatedCount = productRepository.activateProductsByIds(productIds);
        log.info("Bulk products activated successfully. Count: {}", updatedCount);
        return updatedCount;
    }
    
    @Override
    public int updateBulkCategory(List<Long> productIds, Long categoryId){
        log.info("Updating category for bulk products: {}", productIds);
        ProductCategory category = productCategoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));
        int updatedCount = productRepository.updateCategoryForProducts(productIds, category);
        log.info("Bulk product categories updated successfully. Count: {}", updatedCount);
        return updatedCount;
    }
    

    // ==================== PRIVATE HELPER METHODS ====================

    private Product findProductById(Long id) {
        return productRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    private void validateUniqueProductCode(String productCode, Long excludeId) {
        if (excludeId == null) {
            if (productRepository.existsByProductCode(productCode)) {
                throw new DuplicateResourceException("Product code already exists: " + productCode);
            }
        } else {
            if (productRepository.existsByProductCodeAndIdNot(productCode, excludeId)) {
                throw new DuplicateResourceException("Product code already exists: " + productCode);
            }
        }
    }

    private void validateUniqueBarcode(String barcode, Long excludeId) {
        if (excludeId == null) {
            if (productRepository.existsByBarcode(barcode)) {
                throw new DuplicateResourceException("Barcode already exists: " + barcode);
            }
        } else {
            if (productRepository.existsByBarcodeAndIdNot(barcode, excludeId)) {
                throw new DuplicateResourceException("Barcode already exists: " + barcode);
            }
        }
    }

    private void validateUniqueSku(String sku, Long excludeId) {
        if (excludeId == null) {
            if (productRepository.existsBySku(sku)) {
                throw new DuplicateResourceException("SKU already exists: " + sku);
            }
        } else {
            if (productRepository.existsBySkuAndIdNot(sku, excludeId)) {
                throw new DuplicateResourceException("SKU already exists: " + sku);
            }
        }
    }

    private PageResponse<ProductResponse> createPageResponse(Page<Product> productPage) {
        List<ProductResponse> content = productPage.getContent().stream()
            .map(productMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<ProductResponse>builder()
            .content(content)
            .pageNumber(productPage.getNumber())
            .pageSize(productPage.getSize())
            .totalElements(productPage.getTotalElements())
            .totalPages(productPage.getTotalPages())
            .last(productPage.isLast())
            .first(productPage.isFirst())
            .empty(productPage.isEmpty())
            .build();
    }

	@Override
	public List<ProductResponse> getAllProducts() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getAllProducts'");
	}

	@Override
	public ProductResponse discontinueProduct(Long id, String reason) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'discontinueProduct'");
	}

	@Override
	public ProductResponse featureProduct(Long id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'featureProduct'");
	}

	@Override
	public PageResponse<ProductResponse> getActiveProducts(Pageable pageable) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getActiveProducts'");
	}

	@Override
	public List<ProductResponse> getInactiveProducts() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getInactiveProducts'");
	}

	@Override
	public List<ProductResponse> getDiscontinuedProducts() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getDiscontinuedProducts'");
	}

	@Override
	public PageResponse<ProductResponse> getFeaturedProducts(Pageable pageable) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getFeaturedProducts'");
	}

	@Override
	public List<ProductResponse> getSellableProducts() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getSellableProducts'");
	}

	@Override
	public List<ProductResponse> getPurchasableProducts() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getPurchasableProducts'");
	}

	@Override
	public List<ProductResponse> getTaxableProducts() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getTaxableProducts'");
	}

	@Override
	public List<ProductResponse> getProductsByCategory(Long categoryId) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getProductsByCategory'");
	}

	@Override
	public List<ProductResponse> getProductsByType(String productType) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getProductsByType'");
	}

	@Override
	public List<ProductResponse> getRawMaterialProducts() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getRawMaterialProducts'");
	}

	@Override
	public List<ProductResponse> getFinishedGoodsProducts() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getFinishedGoodsProducts'");
	}

	@Override
	public List<ProductResponse> getProductsByManufacturer(String manufacturer) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getProductsByManufacturer'");
	}

	@Override
	public List<ProductResponse> getProductsByPriceRange(Double minPrice, Double maxPrice) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getProductsByPriceRange'");
	}

	@Override
	public List<ProductResponse> getProductsBelowPrice(Double maxPrice) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getProductsBelowPrice'");
	}

	@Override
	public List<ProductResponse> getProductsAbovePrice(Double minPrice) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getProductsAbovePrice'");
	}

	@Override
	public List<ProductResponse> getProductsByCostRange(Double minCost, Double maxCost) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getProductsByCostRange'");
	}

	@Override
	public List<ProductResponse> getHighMarginProducts(Double marginPercentage) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getHighMarginProducts'");
	}

	@Override
	public List<ProductResponse> getLowMarginProducts(Double marginPercentage) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getLowMarginProducts'");
	}

	@Override
	public List<ProductResponse> getProductsByWeightRange(Double minWeight, Double maxWeight) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getProductsByWeightRange'");
	}

	@Override
	public List<ProductResponse> getRecentProducts(int limit) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getRecentProducts'");
	}

	@Override
	public List<ProductResponse> getRecentlyUpdatedProducts(int limit) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getRecentlyUpdatedProducts'");
	}

	@Override
	public List<ProductResponse> getProductsByCategoryTree(List<Long> categoryIds) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getProductsByCategoryTree'");
	}

	@Override
	public List<ProductResponse> getHighestPricedProducts(int limit) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getHighestPricedProducts'");
	}

	@Override
	public List<ProductResponse> getLowestPricedProducts(int limit) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getLowestPricedProducts'");
	}

	@Override
	public void updateSellingPrice(Long id, Double newPrice) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'updateSellingPrice'");
	}

	@Override
	public void updateCostPrice(Long id, Double newCost) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'updateCostPrice'");
	}

	@Override
	public void updatePrices(Long id, Double sellingPrice, Double costPrice) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'updatePrices'");
	}

	@Override
	public void applyDiscountPercentage(Long id, Double discountPercentage) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'applyDiscountPercentage'");
	}

	@Override
	public void applyDiscountAmount(Long id, Double discountAmount) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'applyDiscountAmount'");
	}

	@Override
	public void removeDiscount(Long id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'removeDiscount'");
	}

	@Override
	public void applyBulkPriceIncrease(List<Long> productIds, Double percentage) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'applyBulkPriceIncrease'");
	}

	@Override
	public void applyBulkPriceDecrease(List<Long> productIds, Double percentage) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'applyBulkPriceDecrease'");
	}

	@Override
	public boolean isProductCodeAvailable(String productCode) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'isProductCodeAvailable'");
	}

	@Override
	public boolean isSkuAvailable(String sku) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'isSkuAvailable'");
	}

	@Override
	public boolean isBarcodeAvailable(String barcode) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'isBarcodeAvailable'");
	}

	@Override
	public boolean canDeleteProduct(Long id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'canDeleteProduct'");
	}

	@Override
	public boolean canSellProduct(Long id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'canSellProduct'");
	}

	@Override
	public boolean canPurchaseProduct(Long id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'canPurchaseProduct'");
	}

	@Override
	public Double calculateMargin(ProductResponse product) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'calculateMargin'");
	}

	@Override
	public Double calculateMarginPercentage(ProductResponse product) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'calculateMarginPercentage'");
	}

	@Override
	public Map<String, Object> calculateProductMetrics(Long id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'calculateProductMetrics'");
	}

	@Override
	public List<ProductResponse> createBulkProducts(List<ProductRequest> requests) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'createBulkProducts'");
	}

	@Override
	public int deactivateBulkProducts(List<Long> productIds) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'deactivateBulkProducts'");
	}

	@Override
	public int deleteBulkProducts(List<Long> productIds) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'deleteBulkProducts'");
	}

	@Override
	public Map<String, Object> getProductStatistics() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getProductStatistics'");
	}

	@Override
	public List<Map<String, Object>> getProductTypeDistribution() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getProductTypeDistribution'");
	}

	@Override
	public List<Map<String, Object>> getStatusDistribution() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getStatusDistribution'");
	}

	@Override
	public List<Map<String, Object>> getProductsByCategory() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getProductsByCategory'");
	}

	@Override
	public Double getAverageSellingPrice() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getAverageSellingPrice'");
	}

	@Override
	public Double getAverageCostPrice() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getAverageCostPrice'");
	}

	@Override
	public Double getTotalProductValue() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getTotalProductValue'");
	}

	@Override
	public Map<String, Object> getDashboardStatistics() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getDashboardStatistics'");
	}
}
