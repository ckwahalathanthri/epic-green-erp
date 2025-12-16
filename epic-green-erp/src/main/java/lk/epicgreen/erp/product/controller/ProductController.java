package lk.epicgreen.erp.product.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.product.dto.ProductRequest;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Product Controller
 * REST controller for product operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {
    
    private final ProductService productService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Product>> createProduct(
        @Valid @RequestBody ProductRequest request
    ) {
        Product created = productService.createProduct(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Product created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
        @PathVariable Long id,
        @Valid @RequestBody ProductRequest request
    ) {
        Product updated = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Product updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Product deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product, "Product retrieved successfully"));
    }
    
    @GetMapping("/code/{productCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Product>> getProductByCode(@PathVariable String productCode) {
        Product product = productService.getProductByCode(productCode);
        return ResponseEntity.ok(ApiResponse.success(product, "Product retrieved successfully"));
    }
    
    @GetMapping("/sku/{sku}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Product>> getProductBySku(@PathVariable String sku) {
        Product product = productService.getProductBySku(sku);
        return ResponseEntity.ok(ApiResponse.success(product, "Product retrieved successfully"));
    }
    
    @GetMapping("/barcode/{barcode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Product>> getProductByBarcode(@PathVariable String barcode) {
        Product product = productService.getProductByBarcode(barcode);
        return ResponseEntity.ok(ApiResponse.success(product, "Product retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Page<Product>>> getAllProducts(Pageable pageable) {
        Page<Product> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(ApiResponse.success(products, "Products retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Page<Product>>> searchProducts(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<Product> products = productService.searchProducts(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(products, "Search results retrieved"));
    }
    
    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Product>> activateProduct(@PathVariable Long id) {
        Product activated = productService.activateProduct(id);
        return ResponseEntity.ok(ApiResponse.success(activated, "Product activated"));
    }
    
    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Product>> deactivateProduct(@PathVariable Long id) {
        Product deactivated = productService.deactivateProduct(id);
        return ResponseEntity.ok(ApiResponse.success(deactivated, "Product deactivated"));
    }
    
    @PostMapping("/{id}/discontinue")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Product>> discontinueProduct(
        @PathVariable Long id,
        @RequestParam String reason
    ) {
        Product discontinued = productService.discontinueProduct(id, reason);
        return ResponseEntity.ok(ApiResponse.success(discontinued, "Product discontinued"));
    }
    
    @PostMapping("/{id}/feature")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Product>> featureProduct(@PathVariable Long id) {
        Product featured = productService.featureProduct(id);
        return ResponseEntity.ok(ApiResponse.success(featured, "Product featured"));
    }
    
    @PostMapping("/{id}/unfeature")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Product>> unfeatureProduct(@PathVariable Long id) {
        Product unfeatured = productService.unfeatureProduct(id);
        return ResponseEntity.ok(ApiResponse.success(unfeatured, "Product unfeatured"));
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Page<Product>>> getActiveProducts(Pageable pageable) {
        Page<Product> products = productService.getActiveProducts(pageable);
        return ResponseEntity.ok(ApiResponse.success(products, "Active products retrieved"));
    }
    
    @GetMapping("/inactive")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getInactiveProducts() {
        List<Product> products = productService.getInactiveProducts();
        return ResponseEntity.ok(ApiResponse.success(products, "Inactive products retrieved"));
    }
    
    @GetMapping("/discontinued")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getDiscontinuedProducts() {
        List<Product> products = productService.getDiscontinuedProducts();
        return ResponseEntity.ok(ApiResponse.success(products, "Discontinued products retrieved"));
    }
    
    @GetMapping("/featured")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Page<Product>>> getFeaturedProducts(Pageable pageable) {
        Page<Product> products = productService.getFeaturedProducts(pageable);
        return ResponseEntity.ok(ApiResponse.success(products, "Featured products retrieved"));
    }
    
    @GetMapping("/sellable")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<Product>>> getSellableProducts() {
        List<Product> products = productService.getSellableProducts();
        return ResponseEntity.ok(ApiResponse.success(products, "Sellable products retrieved"));
    }
    
    @GetMapping("/purchasable")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getPurchasableProducts() {
        List<Product> products = productService.getPurchasableProducts();
        return ResponseEntity.ok(ApiResponse.success(products, "Purchasable products retrieved"));
    }
    
    @GetMapping("/taxable")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Product>>> getTaxableProducts() {
        List<Product> products = productService.getTaxableProducts();
        return ResponseEntity.ok(ApiResponse.success(products, "Taxable products retrieved"));
    }
    
    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(products, "Products by category retrieved"));
    }
    
    @GetMapping("/type/{productType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByType(@PathVariable String productType) {
        List<Product> products = productService.getProductsByType(productType);
        return ResponseEntity.ok(ApiResponse.success(products, "Products by type retrieved"));
    }
    
    @GetMapping("/type/raw-material")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getRawMaterialProducts() {
        List<Product> products = productService.getRawMaterialProducts();
        return ResponseEntity.ok(ApiResponse.success(products, "Raw material products retrieved"));
    }
    
    @GetMapping("/type/finished-goods")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<Product>>> getFinishedGoodsProducts() {
        List<Product> products = productService.getFinishedGoodsProducts();
        return ResponseEntity.ok(ApiResponse.success(products, "Finished goods products retrieved"));
    }
    
    @GetMapping("/type/semi-finished")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getSemiFinishedProducts() {
        List<Product> products = productService.getSemiFinishedProducts();
        return ResponseEntity.ok(ApiResponse.success(products, "Semi-finished products retrieved"));
    }
    
    @GetMapping("/brand/{brand}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByBrand(@PathVariable String brand) {
        List<Product> products = productService.getProductsByBrand(brand);
        return ResponseEntity.ok(ApiResponse.success(products, "Products by brand retrieved"));
    }
    
    @GetMapping("/manufacturer/{manufacturer}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByManufacturer(
        @PathVariable String manufacturer
    ) {
        List<Product> products = productService.getProductsByManufacturer(manufacturer);
        return ResponseEntity.ok(ApiResponse.success(products, "Products by manufacturer retrieved"));
    }
    
    @GetMapping("/price-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByPriceRange(
        @RequestParam Double minPrice,
        @RequestParam Double maxPrice
    ) {
        List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(ApiResponse.success(products, "Products in price range retrieved"));
    }
    
    @GetMapping("/below-price/{maxPrice}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsBelowPrice(@PathVariable Double maxPrice) {
        List<Product> products = productService.getProductsBelowPrice(maxPrice);
        return ResponseEntity.ok(ApiResponse.success(products, "Products below price retrieved"));
    }
    
    @GetMapping("/above-price/{minPrice}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsAbovePrice(@PathVariable Double minPrice) {
        List<Product> products = productService.getProductsAbovePrice(minPrice);
        return ResponseEntity.ok(ApiResponse.success(products, "Products above price retrieved"));
    }
    
    @GetMapping("/high-margin")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getHighMarginProducts(
        @RequestParam(defaultValue = "30") Double marginPercentage
    ) {
        List<Product> products = productService.getHighMarginProducts(marginPercentage);
        return ResponseEntity.ok(ApiResponse.success(products, "High margin products retrieved"));
    }
    
    @GetMapping("/low-margin")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getLowMarginProducts(
        @RequestParam(defaultValue = "10") Double marginPercentage
    ) {
        List<Product> products = productService.getLowMarginProducts(marginPercentage);
        return ResponseEntity.ok(ApiResponse.success(products, "Low margin products retrieved"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Product>>> getRecentProducts(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<Product> products = productService.getRecentProducts(limit);
        return ResponseEntity.ok(ApiResponse.success(products, "Recent products retrieved"));
    }
    
    @GetMapping("/recently-updated")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getRecentlyUpdatedProducts(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<Product> products = productService.getRecentlyUpdatedProducts(limit);
        return ResponseEntity.ok(ApiResponse.success(products, "Recently updated products retrieved"));
    }
    
    @GetMapping("/highest-priced")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Product>>> getHighestPricedProducts(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<Product> products = productService.getHighestPricedProducts(limit);
        return ResponseEntity.ok(ApiResponse.success(products, "Highest priced products retrieved"));
    }
    
    @GetMapping("/lowest-priced")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Product>>> getLowestPricedProducts(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<Product> products = productService.getLowestPricedProducts(limit);
        return ResponseEntity.ok(ApiResponse.success(products, "Lowest priced products retrieved"));
    }
    
    @PostMapping("/{id}/update-selling-price")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> updateSellingPrice(
        @PathVariable Long id,
        @RequestParam Double newPrice
    ) {
        productService.updateSellingPrice(id, newPrice);
        return ResponseEntity.ok(ApiResponse.success(null, "Selling price updated"));
    }
    
    @PostMapping("/{id}/update-cost-price")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> updateCostPrice(
        @PathVariable Long id,
        @RequestParam Double newCost
    ) {
        productService.updateCostPrice(id, newCost);
        return ResponseEntity.ok(ApiResponse.success(null, "Cost price updated"));
    }
    
    @PostMapping("/{id}/apply-discount-percentage")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> applyDiscountPercentage(
        @PathVariable Long id,
        @RequestParam Double discountPercentage
    ) {
        productService.applyDiscountPercentage(id, discountPercentage);
        return ResponseEntity.ok(ApiResponse.success(null, "Discount percentage applied"));
    }
    
    @PostMapping("/{id}/apply-discount-amount")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> applyDiscountAmount(
        @PathVariable Long id,
        @RequestParam Double discountAmount
    ) {
        productService.applyDiscountAmount(id, discountAmount);
        return ResponseEntity.ok(ApiResponse.success(null, "Discount amount applied"));
    }
    
    @PostMapping("/{id}/remove-discount")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> removeDiscount(@PathVariable Long id) {
        productService.removeDiscount(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Discount removed"));
    }
    
    @GetMapping("/{id}/metrics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> calculateProductMetrics(@PathVariable Long id) {
        Map<String, Object> metrics = productService.calculateProductMetrics(id);
        return ResponseEntity.ok(ApiResponse.success(metrics, "Product metrics calculated"));
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = productService.getProductStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = productService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
}
