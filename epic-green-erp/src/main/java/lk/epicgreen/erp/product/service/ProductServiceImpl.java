package lk.epicgreen.erp.product.service;

import lk.epicgreen.erp.product.dto.ProductRequest;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Product Service Implementation
 * Implementation of product service operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    
    @Override
    public Product createProduct(ProductRequest request) {
        log.info("Creating product: {}", request.getProductName());
        
        // Validate unique fields
        if (productRepository.existsByProductCode(request.getProductCode())) {
            throw new RuntimeException("Product code already exists: " + request.getProductCode());
        }
        if (request.getSku() != null && productRepository.existsBySku(request.getSku())) {
            throw new RuntimeException("SKU already exists: " + request.getSku());
        }
        if (request.getBarcode() != null && productRepository.existsByBarcode(request.getBarcode())) {
            throw new RuntimeException("Barcode already exists: " + request.getBarcode());
        }
        
        Product product = new Product();
        product.setProductCode(request.getProductCode());
        product.setProductName(request.getProductName());
        product.setSku(request.getSku());
        product.setBarcode(request.getBarcode());
        product.setCategoryId(request.getCategoryId());
        product.setProductType(request.getProductType() != null ? request.getProductType() : "FINISHED_GOODS");
        product.setDescription(request.getDescription());
        product.setShortDescription(request.getShortDescription());
        product.setUnitOfMeasure(request.getUnitOfMeasure());
        product.setBrand(request.getBrand());
        product.setManufacturer(request.getManufacturer());
        product.setSellingPrice(request.getSellingPrice() != null ? BigDecimal.valueOf(request.getSellingPrice()) : BigDecimal.ZERO);
        product.setCostPrice(request.getCostPrice() != null ? BigDecimal.valueOf(request.getCostPrice()) : BigDecimal.ZERO);
        product.setMrp(request.getMrp() != null ? BigDecimal.valueOf(request.getMrp()) : null);
        product.setDiscountPercentage(request.getDiscountPercentage() != null ? BigDecimal.valueOf(request.getDiscountPercentage()) : BigDecimal.ZERO);
        product.setDiscountAmount(request.getDiscountAmount() != null ? BigDecimal.valueOf(request.getDiscountAmount()) : BigDecimal.ZERO);
        product.setTaxPercentage(request.getTaxPercentage() != null ? BigDecimal.valueOf(request.getTaxPercentage()) : BigDecimal.ZERO);
        product.setWeight(request.getWeight() != null ? BigDecimal.valueOf(request.getWeight()) : null);
        product.setWeightUnit(request.getWeightUnit());
        product.setDimensions(request.getDimensions());
        product.setIsActive(true);
        product.setStatus("ACTIVE");
        product.setIsFeatured(false);
        product.setIsTaxable(request.getIsTaxable() != null ? request.getIsTaxable() : true);
        product.setCanBeSold(request.getCanBeSold() != null ? request.getCanBeSold() : true);
        product.setCanBePurchased(request.getCanBePurchased() != null ? request.getCanBePurchased() : true);
        
        return productRepository.save(product);
    }
    
    @Override
    public Product updateProduct(Long id, ProductRequest request) {
        log.info("Updating product: {}", id);
        Product existing = getProductById(id);
        
        // Validate unique fields if changed
        if (!existing.getProductCode().equals(request.getProductCode()) &&
            productRepository.existsByProductCode(request.getProductCode())) {
            throw new RuntimeException("Product code already exists: " + request.getProductCode());
        }
        if (request.getSku() != null && !request.getSku().equals(existing.getSku()) &&
            productRepository.existsBySku(request.getSku())) {
            throw new RuntimeException("SKU already exists: " + request.getSku());
        }
        if (request.getBarcode() != null && !request.getBarcode().equals(existing.getBarcode()) &&
            productRepository.existsByBarcode(request.getBarcode())) {
            throw new RuntimeException("Barcode already exists: " + request.getBarcode());
        }
        
        existing.setProductCode(request.getProductCode());
        existing.setProductName(request.getProductName());
        existing.setSku(request.getSku());
        existing.setBarcode(request.getBarcode());
        existing.setCategoryId(request.getCategoryId());
        existing.setProductType(request.getProductType());
        existing.setDescription(request.getDescription());
        existing.setShortDescription(request.getShortDescription());
        existing.setUnitOfMeasure(request.getUnitOfMeasure());
        existing.setBrand(request.getBrand());
        existing.setManufacturer(request.getManufacturer());
        existing.setWeight(request.getWeight() != null ? BigDecimal.valueOf(request.getWeight()) : null);
        existing.setWeightUnit(request.getWeightUnit());
        existing.setDimensions(request.getDimensions());
        existing.setIsTaxable(request.getIsTaxable());
        existing.setCanBeSold(request.getCanBeSold());
        existing.setCanBePurchased(request.getCanBePurchased());
        existing.setUpdatedAt(LocalDateTime.now());
        
        return productRepository.save(existing);
    }
    
    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product: {}", id);
        Product product = getProductById(id);
        
        if (!canDeleteProduct(id)) {
            throw new RuntimeException("Cannot delete product with existing transactions");
        }
        
        productRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Product getProductByCode(String productCode) {
        return productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new RuntimeException("Product not found with code: " + productCode));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Product getProductBySku(String sku) {
        return productRepository.findBySku(sku)
            .orElseThrow(() -> new RuntimeException("Product not found with SKU: " + sku));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Product getProductByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode)
            .orElseThrow(() -> new RuntimeException("Product not found with barcode: " + barcode));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.searchProducts(keyword, pageable);
    }
    
    @Override
    public Product activateProduct(Long id) {
        log.info("Activating product: {}", id);
        Product product = getProductById(id);
        product.setIsActive(true);
        product.setStatus("ACTIVE");
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }
    
    @Override
    public Product deactivateProduct(Long id) {
        log.info("Deactivating product: {}", id);
        Product product = getProductById(id);
        product.setIsActive(false);
        product.setStatus("INACTIVE");
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }
    
    @Override
    public Product discontinueProduct(Long id, String reason) {
        log.info("Discontinuing product: {}", id);
        Product product = getProductById(id);
        product.setIsActive(false);
        product.setStatus("DISCONTINUED");
        product.setDiscontinuedReason(reason);
        product.setDiscontinuedDate(LocalDate.now());
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }
    
    @Override
    public Product featureProduct(Long id) {
        log.info("Featuring product: {}", id);
        Product product = getProductById(id);
        product.setIsFeatured(true);
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }
    
    @Override
    public Product unfeatureProduct(Long id) {
        log.info("Unfeaturing product: {}", id);
        Product product = getProductById(id);
        product.setIsFeatured(false);
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getActiveProducts() {
        return productRepository.findActiveProducts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Product> getActiveProducts(Pageable pageable) {
        return productRepository.findActiveProducts(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getInactiveProducts() {
        return productRepository.findInactiveProducts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getDiscontinuedProducts() {
        return productRepository.findDiscontinuedProducts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getFeaturedProducts() {
        return productRepository.findFeaturedProducts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Product> getFeaturedProducts(Pageable pageable) {
        return productRepository.findFeaturedProducts(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getSellableProducts() {
        return productRepository.findSellableProducts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getPurchasableProducts() {
        return productRepository.findPurchasableProducts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getTaxableProducts() {
        return productRepository.findTaxableProducts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByType(String productType) {
        return productRepository.findByProductType(productType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getRawMaterialProducts() {
        return productRepository.findRawMaterialProducts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getFinishedGoodsProducts() {
        return productRepository.findFinishedGoodsProducts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getSemiFinishedProducts() {
        return productRepository.findSemiFinishedProducts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByManufacturer(String manufacturer) {
        return productRepository.findByManufacturer(manufacturer);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceRange(minPrice, maxPrice);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsBelowPrice(Double maxPrice) {
        return productRepository.findBelowPrice(maxPrice);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsAbovePrice(Double minPrice) {
        return productRepository.findAbovePrice(minPrice);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByCostRange(Double minCost, Double maxCost) {
        return productRepository.findByCostRange(minCost, maxCost);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getHighMarginProducts(Double marginPercentage) {
        return productRepository.findHighMarginProducts(marginPercentage);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getLowMarginProducts(Double marginPercentage) {
        return productRepository.findLowMarginProducts(marginPercentage);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByWeightRange(Double minWeight, Double maxWeight) {
        return productRepository.findByWeightRange(minWeight, maxWeight);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getRecentProducts(int limit) {
        return productRepository.findRecentProducts(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getRecentlyUpdatedProducts(int limit) {
        return productRepository.findRecentlyUpdatedProducts(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategoryTree(List<Long> categoryIds) {
        return productRepository.findByCategoryTree(categoryIds);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getHighestPricedProducts(int limit) {
        return productRepository.getHighestPricedProducts(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getLowestPricedProducts(int limit) {
        return productRepository.getLowestPricedProducts(PageRequest.of(0, limit));
    }
    
    @Override
    public void updateSellingPrice(Long productId, Double newPrice) {
        Product product = getProductById(productId);
        product.setSellingPrice(newPrice != null ? BigDecimal.valueOf(newPrice) : BigDecimal.ZERO);
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }
    
    @Override
    public void updateCostPrice(Long productId, Double newCost) {
        Product product = getProductById(productId);
        product.setCostPrice(newCost != null ? BigDecimal.valueOf(newCost) : BigDecimal.ZERO);
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }
    
    @Override
    public void updatePrices(Long productId, Double sellingPrice, Double costPrice) {
        Product product = getProductById(productId);
        product.setSellingPrice(sellingPrice != null ? BigDecimal.valueOf(sellingPrice) : BigDecimal.ZERO);
        product.setCostPrice(costPrice != null ? BigDecimal.valueOf(costPrice) : BigDecimal.ZERO);
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }
    
    @Override
    public void applyDiscountPercentage(Long productId, Double discountPercentage) {
        Product product = getProductById(productId);
        BigDecimal discountPct = BigDecimal.valueOf(discountPercentage != null ? discountPercentage : 0.0);
        product.setDiscountPercentage(discountPct);
        
        BigDecimal sellingPrice = product.getSellingPrice();
        BigDecimal discountAmt = sellingPrice.multiply(discountPct).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        product.setDiscountAmount(discountAmt);
        
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }
    
    @Override
    public void applyDiscountAmount(Long productId, Double discountAmount) {
        Product product = getProductById(productId);
        BigDecimal discountAmt = BigDecimal.valueOf(discountAmount != null ? discountAmount : 0.0);
        product.setDiscountAmount(discountAmt);
        
        BigDecimal sellingPrice = product.getSellingPrice();
        BigDecimal discountPct = discountAmt.divide(sellingPrice, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        product.setDiscountPercentage(discountPct);
        
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }
    
    @Override
    public void removeDiscount(Long productId) {
        Product product = getProductById(productId);
        product.setDiscountPercentage(BigDecimal.ZERO);
        product.setDiscountAmount(BigDecimal.ZERO);
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }
    
    @Override
    public void applyBulkPriceIncrease(List<Long> productIds, Double percentage) {
        productIds.forEach(id -> {
            Product product = getProductById(id);
            BigDecimal sellingPrice = product.getSellingPrice();
            BigDecimal pct = BigDecimal.valueOf(percentage != null ? percentage : 0.0);
            BigDecimal multiplier = BigDecimal.ONE.add(pct.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
            BigDecimal newPrice = sellingPrice.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
            product.setSellingPrice(newPrice);
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
        });
    }
    
    @Override
    public void applyBulkPriceDecrease(List<Long> productIds, Double percentage) {
        productIds.forEach(id -> {
            Product product = getProductById(id);
            BigDecimal sellingPrice = product.getSellingPrice();
            BigDecimal pct = BigDecimal.valueOf(percentage != null ? percentage : 0.0);
            BigDecimal multiplier = BigDecimal.ONE.subtract(pct.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
            BigDecimal newPrice = sellingPrice.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
            product.setSellingPrice(newPrice);
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
        });
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validateProduct(Product product) {
        return product.getProductCode() != null &&
               product.getProductName() != null &&
               product.getCategoryId() != null &&
               product.getSellingPrice() != null &&
               product.getSellingPrice().compareTo(BigDecimal.ZERO) >= 0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isProductCodeAvailable(String productCode) {
        return !productRepository.existsByProductCode(productCode);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isSkuAvailable(String sku) {
        return !productRepository.existsBySku(sku);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isBarcodeAvailable(String barcode) {
        return !productRepository.existsByBarcode(barcode);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canDeleteProduct(Long productId) {
        // Check if product has no transactions (implement based on business rules)
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canSellProduct(Long productId) {
        Product product = getProductById(productId);
        return product.getIsActive() && product.getCanBeSold();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canPurchaseProduct(Long productId) {
        Product product = getProductById(productId);
        return product.getIsActive() && product.getCanBePurchased();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double calculateMargin(Product product) {
        if (product.getSellingPrice() != null && product.getCostPrice() != null) {
            BigDecimal margin = product.getSellingPrice().subtract(product.getCostPrice());
            return margin.doubleValue();
        }
        return 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double calculateMarginPercentage(Product product) {
        if (product.getSellingPrice() != null && product.getCostPrice() != null &&
            product.getSellingPrice().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal margin = product.getSellingPrice().subtract(product.getCostPrice());
            BigDecimal marginPct = margin.divide(product.getSellingPrice(), 4, RoundingMode.HALF_UP)
                                         .multiply(BigDecimal.valueOf(100));
            return marginPct.doubleValue();
        }
        return 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double calculateDiscountedPrice(Product product) {
        if (product.getSellingPrice() != null && product.getDiscountAmount() != null) {
            BigDecimal discountedPrice = product.getSellingPrice().subtract(product.getDiscountAmount());
            return discountedPrice.doubleValue();
        }
        return product.getSellingPrice() != null ? product.getSellingPrice().doubleValue() : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> calculateProductMetrics(Long productId) {
        Product product = getProductById(productId);
        Map<String, Object> metrics = new HashMap<>();
        
        metrics.put("sellingPrice", product.getSellingPrice());
        metrics.put("costPrice", product.getCostPrice());
        metrics.put("margin", calculateMargin(product));
        metrics.put("marginPercentage", calculateMarginPercentage(product));
        metrics.put("discountedPrice", calculateDiscountedPrice(product));
        
        BigDecimal sellingPrice = product.getSellingPrice();
        BigDecimal taxPct = product.getTaxPercentage();
        BigDecimal taxAmount = sellingPrice.multiply(taxPct).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        metrics.put("taxAmount", taxAmount.doubleValue());
        
        return metrics;
    }
    
    @Override
    public List<Product> createBulkProducts(List<ProductRequest> requests) {
        return requests.stream()
            .map(this::createProduct)
            .collect(Collectors.toList());
    }
    
    @Override
    public int activateBulkProducts(List<Long> productIds) {
        int count = 0;
        for (Long id : productIds) {
            try {
                activateProduct(id);
                count++;
            } catch (Exception e) {
                log.error("Error activating product: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int deactivateBulkProducts(List<Long> productIds) {
        int count = 0;
        for (Long id : productIds) {
            try {
                deactivateProduct(id);
                count++;
            } catch (Exception e) {
                log.error("Error deactivating product: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int deleteBulkProducts(List<Long> productIds) {
        int count = 0;
        for (Long id : productIds) {
            try {
                deleteProduct(id);
                count++;
            } catch (Exception e) {
                log.error("Error deleting product: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int updateBulkCategory(List<Long> productIds, Long categoryId) {
        int count = 0;
        for (Long id : productIds) {
            try {
                Product product = getProductById(id);
                product.setCategoryId(categoryId);
                product.setUpdatedAt(LocalDateTime.now());
                productRepository.save(product);
                count++;
            } catch (Exception e) {
                log.error("Error updating product category: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getProductStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalProducts", productRepository.count());
        stats.put("activeProducts", productRepository.countActiveProducts());
        stats.put("inactiveProducts", productRepository.countInactiveProducts());
        stats.put("featuredProducts", productRepository.countFeaturedProducts());
        stats.put("averageSellingPrice", getAverageSellingPrice());
        stats.put("averageCostPrice", getAverageCostPrice());
        stats.put("averageMarginPercentage", getAverageMarginPercentage());
        stats.put("totalProductValue", getTotalProductValue());
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getProductTypeDistribution() {
        List<Object[]> results = productRepository.getProductTypeDistribution();
        return convertToMapList(results, "productType", "productCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getStatusDistribution() {
        List<Object[]> results = productRepository.getStatusDistribution();
        return convertToMapList(results, "status", "productCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getProductsByCategory() {
        List<Object[]> results = productRepository.getProductsByCategory();
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("categoryId", result[0]);
                map.put("categoryName", result[1]);
                map.put("productCount", result[2]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getProductsByBrand() {
        List<Object[]> results = productRepository.getProductsByBrand();
        return convertToMapList(results, "brand", "productCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getAverageSellingPrice() {
        Double average = productRepository.getAverageSellingPrice();
        return average != null ? average : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getAverageCostPrice() {
        Double average = productRepository.getAverageCostPrice();
        return average != null ? average : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getAverageMarginPercentage() {
        Double average = productRepository.getAverageMarginPercentage();
        return average != null ? average : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalProductValue() {
        Double total = productRepository.getTotalProductValue();
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("statistics", getProductStatistics());
        dashboard.put("productTypeDistribution", getProductTypeDistribution());
        dashboard.put("statusDistribution", getStatusDistribution());
        dashboard.put("productsByCategory", getProductsByCategory());
        dashboard.put("productsByBrand", getProductsByBrand());
        dashboard.put("highestPricedProducts", getHighestPricedProducts(10));
        dashboard.put("lowestPricedProducts", getLowestPricedProducts(10));
        
        return dashboard;
    }
    
    private List<Map<String, Object>> convertToMapList(List<Object[]> results, String key1, String key2) {
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put(key1, result[0]);
                map.put(key2, result[1]);
                return map;
            })
            .collect(Collectors.toList());
    }
}
