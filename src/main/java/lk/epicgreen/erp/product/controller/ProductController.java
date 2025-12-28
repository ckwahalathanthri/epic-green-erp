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
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestBody ProductRequest request) {
        log.info("Creating product: {}", request.getProductName());
        Product created = productService.createProduct(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Product created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
        @PathVariable Long id,
        @Valid @RequestBody ProductRequest request
    ) {
        log.info("Updating product: {}", id);
        Product updated = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Product updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        log.info("Deleting product: {}", id);
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
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<Product>>> getAllProductsList() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success(products, "Products list retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Page<Product>>> searchProducts(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<Product> products = productService.searchProducts(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(products, "Search results retrieved successfully"));
    }
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Product>> activateProduct(@PathVariable Long id) {
        log.info("Activating product: {}", id);
        Product activated = productService.activateProduct(id);
        return ResponseEntity.ok(ApiResponse.success(activated, "Product activated successfully"));
    }
    
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Product>> deactivateProduct(@PathVariable Long id) {
        log.info("Deactivating product: {}", id);
        Product deactivated = productService.deactivateProduct(id);
        return ResponseEntity.ok(ApiResponse.success(deactivated, "Product deactivated successfully"));
    }
    
    @PutMapping("/{id}/discontinue")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Product>> discontinueProduct(
        @PathVariable Long id,
        @RequestParam String reason
    ) {
        log.info("Discontinuing product: {}", id);
        Product discontinued = productService.discontinueProduct(id, reason);
        return ResponseEntity.ok(ApiResponse.success(discontinued, "Product discontinued successfully"));
    }
    
    @PutMapping("/{id}/feature")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Product>> featureProduct(@PathVariable Long id) {
        log.info("Featuring product: {}", id);
        Product featured = productService.featureProduct(id);
        return ResponseEntity.ok(ApiResponse.success(featured, "Product featured successfully"));
    }
    
    @PutMapping("/{id}/unfeature")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Product>> unfeatureProduct(@PathVariable Long id) {
        log.info("Unfeaturing product: {}", id);
        Product unfeatured = productService.unfeatureProduct(id);
        return ResponseEntity.ok(ApiResponse.success(unfeatured, "Product unfeatured successfully"));
    }
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Page<Product>>> getActiveProducts(Pageable pageable) {
        Page<Product> products = productService.getActiveProducts(pageable);
        return ResponseEntity.ok(ApiResponse.success(products, "Active products retrieved successfully"));
    }
    
    @GetMapping("/inactive")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getInactiveProducts() {
        List<Product> products = productService.getInactiveProducts();
        return ResponseEntity.ok(ApiResponse.success(products, "Inactive products retrieved successfully"));
    }
    
    @GetMapping("/discontinued")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getDiscontinuedProducts() {
        List<Product> products = productService.getDiscontinuedProducts();
        return ResponseEntity.ok(ApiResponse.success(products, "Discontinued products retrieved successfully"));
    }
    
    @GetMapping("/featured")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Page<Product>>> getFeaturedProducts(Pageable pageable) {
        Page<Product> products = productService.getFeaturedProducts(pageable);
        return ResponseEntity.ok(ApiResponse.success(products, "Featured products retrieved successfully"));
    }
    
    @GetMapping("/sellable")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<Product>>> getSellableProducts() {
        List<Product> products = productService.getSellableProducts();
        return ResponseEntity.ok(ApiResponse.success(products, "Sellable products retrieved successfully"));
    }
    
    @GetMapping("/purchasable")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getPurchasableProducts() {
        List<Product> products = productService.getPurchasableProducts();
        return ResponseEntity.ok(ApiResponse.success(products, "Purchasable products retrieved successfully"));
    }
    
    @GetMapping("/taxable")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Product>>> getTaxableProducts() {
        List<Product> products = productService.getTaxableProducts();
        return ResponseEntity.ok(ApiResponse.success(products, "Taxable products retrieved successfully"));
    }
    
    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(products, "Products by category retrieved successfully"));
    }
    
    @GetMapping("/type/{productType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByType(@PathVariable String productType) {
        List<Product> products = productService.getProductsByType(productType);
        return ResponseEntity.ok(ApiResponse.success(products, "Products by type retrieved successfully"));
    }
    
    @GetMapping("/type/raw-material")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getRawMaterialProducts() {
        List<Product> products = productService.getRawMaterialProducts();
        return ResponseEntity.ok(ApiResponse.success(products, "Raw material products retrieved successfully"));
    }
    
    @GetMapping("/type/finished-goods")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<Product>>> getFinishedGoodsProducts() {
        List<Product> products = productService.getFinishedGoodsProducts();
        return ResponseEntity.ok(ApiResponse.success(products, "Finished goods retrieved successfully"));
    }
    
    @GetMapping("/type/semi-finished")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getSemiFinishedProducts() {
        List<Product> products = productService.getSemiFinishedProducts();
        return ResponseEntity.ok(ApiResponse.success(products, "Semi-finished products retrieved successfully"));
    }
    
    @GetMapping("/brand/{brand}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByBrand(@PathVariable String brand) {
        List<Product> products = productService.getProductsByBrand(brand);
        return ResponseEntity.ok(ApiResponse.success(products, "Products by brand retrieved successfully"));
    }
    
    @GetMapping("/manufacturer/{manufacturer}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByManufacturer(@PathVariable String manufacturer) {
        List<Product> products = productService.getProductsByManufacturer(manufacturer);
        return ResponseEntity.ok(ApiResponse.success(products, "Products by manufacturer retrieved successfully"));
    }
    
    @GetMapping("/price-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByPriceRange(
        @RequestParam Double minPrice,
        @RequestParam Double maxPrice
    ) {
        List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(ApiResponse.success(products, "Products by price range retrieved successfully"));
    }
    
    @GetMapping("/price/below")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsBelowPrice(@RequestParam Double maxPrice) {
        List<Product> products = productService.getProductsBelowPrice(maxPrice);
        return ResponseEntity.ok(ApiResponse.success(products, "Products below price retrieved successfully"));
    }
    
    @GetMapping("/price/above")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsAbovePrice(@RequestParam Double minPrice) {
        List<Product> products = productService.getProductsAbovePrice(minPrice);
        return ResponseEntity.ok(ApiResponse.success(products, "Products above price retrieved successfully"));
    }
    
    @GetMapping("/cost-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByCostRange(
        @RequestParam Double minCost,
        @RequestParam Double maxCost
    ) {
        List<Product> products = productService.getProductsByCostRange(minCost, maxCost);
        return ResponseEntity.ok(ApiResponse.success(products, "Products by cost range retrieved successfully"));
    }
    
    @GetMapping("/margin/high")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getHighMarginProducts(@RequestParam Double marginPercentage) {
        List<Product> products = productService.getHighMarginProducts(marginPercentage);
        return ResponseEntity.ok(ApiResponse.success(products, "High margin products retrieved successfully"));
    }
    
    @GetMapping("/margin/low")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getLowMarginProducts(@RequestParam Double marginPercentage) {
        List<Product> products = productService.getLowMarginProducts(marginPercentage);
        return ResponseEntity.ok(ApiResponse.success(products, "Low margin products retrieved successfully"));
    }
    
    @GetMapping("/weight-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByWeightRange(
        @RequestParam Double minWeight,
        @RequestParam Double maxWeight
    ) {
        List<Product> products = productService.getProductsByWeightRange(minWeight, maxWeight);
        return ResponseEntity.ok(ApiResponse.success(products, "Products by weight range retrieved successfully"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getRecentProducts(@RequestParam(defaultValue = "10") int limit) {
        List<Product> products = productService.getRecentProducts(limit);
        return ResponseEntity.ok(ApiResponse.success(products, "Recent products retrieved successfully"));
    }
    
    @GetMapping("/recently-updated")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getRecentlyUpdatedProducts(@RequestParam(defaultValue = "10") int limit) {
        List<Product> products = productService.getRecentlyUpdatedProducts(limit);
        return ResponseEntity.ok(ApiResponse.success(products, "Recently updated products retrieved successfully"));
    }
    
    @GetMapping("/category-tree")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByCategoryTree(@RequestParam List<Long> categoryIds) {
        List<Product> products = productService.getProductsByCategoryTree(categoryIds);
        return ResponseEntity.ok(ApiResponse.success(products, "Products by category tree retrieved successfully"));
    }
    
    @GetMapping("/highest-priced")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getHighestPricedProducts(@RequestParam(defaultValue = "10") int limit) {
        List<Product> products = productService.getHighestPricedProducts(limit);
        return ResponseEntity.ok(ApiResponse.success(products, "Highest priced products retrieved successfully"));
    }
    
    @GetMapping("/lowest-priced")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> getLowestPricedProducts(@RequestParam(defaultValue = "10") int limit) {
        List<Product> products = productService.getLowestPricedProducts(limit);
        return ResponseEntity.ok(ApiResponse.success(products, "Lowest priced products retrieved successfully"));
    }
    
    // ===================================================================
    // PRICE OPERATIONS
    // ===================================================================
    
    @PutMapping("/{id}/selling-price")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> updateSellingPrice(
        @PathVariable Long id,
        @RequestParam Double newPrice
    ) {
        log.info("Updating selling price for product: {}", id);
        productService.updateSellingPrice(id, newPrice);
        return ResponseEntity.ok(ApiResponse.success(null, "Selling price updated successfully"));
    }
    
    @PutMapping("/{id}/cost-price")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> updateCostPrice(
        @PathVariable Long id,
        @RequestParam Double newCost
    ) {
        log.info("Updating cost price for product: {}", id);
        productService.updateCostPrice(id, newCost);
        return ResponseEntity.ok(ApiResponse.success(null, "Cost price updated successfully"));
    }
    
    @PutMapping("/{id}/prices")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> updatePrices(
        @PathVariable Long id,
        @RequestParam Double sellingPrice,
        @RequestParam Double costPrice
    ) {
        log.info("Updating prices for product: {}", id);
        productService.updatePrices(id, sellingPrice, costPrice);
        return ResponseEntity.ok(ApiResponse.success(null, "Prices updated successfully"));
    }
    
    @PutMapping("/{id}/discount-percentage")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> applyDiscountPercentage(
        @PathVariable Long id,
        @RequestParam Double discountPercentage
    ) {
        log.info("Applying discount percentage to product: {}", id);
        productService.applyDiscountPercentage(id, discountPercentage);
        return ResponseEntity.ok(ApiResponse.success(null, "Discount percentage applied successfully"));
    }
    
    @PutMapping("/{id}/discount-amount")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> applyDiscountAmount(
        @PathVariable Long id,
        @RequestParam Double discountAmount
    ) {
        log.info("Applying discount amount to product: {}", id);
        productService.applyDiscountAmount(id, discountAmount);
        return ResponseEntity.ok(ApiResponse.success(null, "Discount amount applied successfully"));
    }
    
    @DeleteMapping("/{id}/discount")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> removeDiscount(@PathVariable Long id) {
        log.info("Removing discount from product: {}", id);
        productService.removeDiscount(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Discount removed successfully"));
    }
    
    @PutMapping("/bulk/price-increase")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> applyBulkPriceIncrease(
        @RequestBody List<Long> productIds,
        @RequestParam Double percentage
    ) {
        log.info("Applying bulk price increase to {} products", productIds.size());
        productService.applyBulkPriceIncrease(productIds, percentage);
        return ResponseEntity.ok(ApiResponse.success(null, "Bulk price increase applied successfully"));
    }
    
    @PutMapping("/bulk/price-decrease")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> applyBulkPriceDecrease(
        @RequestBody List<Long> productIds,
        @RequestParam Double percentage
    ) {
        log.info("Applying bulk price decrease to {} products", productIds.size());
        productService.applyBulkPriceDecrease(productIds, percentage);
        return ResponseEntity.ok(ApiResponse.success(null, "Bulk price decrease applied successfully"));
    }
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    @GetMapping("/validate/code/{productCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> isProductCodeAvailable(@PathVariable String productCode) {
        boolean available = productService.isProductCodeAvailable(productCode);
        return ResponseEntity.ok(ApiResponse.success(available, "Product code availability checked"));
    }
    
    @GetMapping("/validate/sku/{sku}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> isSkuAvailable(@PathVariable String sku) {
        boolean available = productService.isSkuAvailable(sku);
        return ResponseEntity.ok(ApiResponse.success(available, "SKU availability checked"));
    }
    
    @GetMapping("/validate/barcode/{barcode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> isBarcodeAvailable(@PathVariable String barcode) {
        boolean available = productService.isBarcodeAvailable(barcode);
        return ResponseEntity.ok(ApiResponse.success(available, "Barcode availability checked"));
    }
    
    @GetMapping("/{id}/can-delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> canDeleteProduct(@PathVariable Long id) {
        boolean canDelete = productService.canDeleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(canDelete, "Delete check completed"));
    }
    
    @GetMapping("/{id}/can-sell")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Boolean>> canSellProduct(@PathVariable Long id) {
        boolean canSell = productService.canSellProduct(id);
        return ResponseEntity.ok(ApiResponse.success(canSell, "Sell check completed"));
    }
    
    @GetMapping("/{id}/can-purchase")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> canPurchaseProduct(@PathVariable Long id) {
        boolean canPurchase = productService.canPurchaseProduct(id);
        return ResponseEntity.ok(ApiResponse.success(canPurchase, "Purchase check completed"));
    }
    
    // ===================================================================
    // CALCULATIONS
    // ===================================================================
    
    @GetMapping("/{id}/margin")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> calculateMargin(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        Double margin = productService.calculateMargin(product);
        return ResponseEntity.ok(ApiResponse.success(margin, "Margin calculated successfully"));
    }
    
    @GetMapping("/{id}/margin-percentage")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> calculateMarginPercentage(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        Double marginPercentage = productService.calculateMarginPercentage(product);
        return ResponseEntity.ok(ApiResponse.success(marginPercentage, "Margin percentage calculated successfully"));
    }
    
    @GetMapping("/{id}/discounted-price")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Double>> calculateDiscountedPrice(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        Double discountedPrice = productService.calculateDiscountedPrice(product);
        return ResponseEntity.ok(ApiResponse.success(discountedPrice, "Discounted price calculated successfully"));
    }
    
    @GetMapping("/{id}/metrics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> calculateProductMetrics(@PathVariable Long id) {
        Map<String, Object> metrics = productService.calculateProductMetrics(id);
        return ResponseEntity.ok(ApiResponse.success(metrics, "Product metrics calculated successfully"));
    }
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    @PostMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<Product>>> createBulkProducts(@Valid @RequestBody List<ProductRequest> requests) {
        log.info("Creating {} products in bulk", requests.size());
        List<Product> products = productService.createBulkProducts(requests);
        return ResponseEntity.ok(ApiResponse.success(products, products.size() + " products created successfully"));
    }
    
    @PutMapping("/bulk/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> activateBulkProducts(@RequestBody List<Long> productIds) {
        log.info("Activating {} products in bulk", productIds.size());
        int count = productService.activateBulkProducts(productIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " products activated successfully"));
    }
    
    @PutMapping("/bulk/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> deactivateBulkProducts(@RequestBody List<Long> productIds) {
        log.info("Deactivating {} products in bulk", productIds.size());
        int count = productService.deactivateBulkProducts(productIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " products deactivated successfully"));
    }
    
    @DeleteMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> deleteBulkProducts(@RequestBody List<Long> productIds) {
        log.info("Deleting {} products in bulk", productIds.size());
        int count = productService.deleteBulkProducts(productIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " products deleted successfully"));
    }
    
    @PutMapping("/bulk/category")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> updateBulkCategory(
        @RequestBody List<Long> productIds,
        @RequestParam Long categoryId
    ) {
        log.info("Updating category for {} products in bulk", productIds.size());
        int count = productService.updateBulkCategory(productIds, categoryId);
        return ResponseEntity.ok(ApiResponse.success(count, count + " products updated successfully"));
    }
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProductStatistics() {
        Map<String, Object> statistics = productService.getProductStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Product statistics retrieved successfully"));
    }
    
    @GetMapping("/statistics/type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getProductTypeDistribution() {
        List<Map<String, Object>> distribution = productService.getProductTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Product type distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/status-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getStatusDistribution() {
        List<Map<String, Object>> distribution = productService.getStatusDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Status distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/by-category")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getProductsByCategory() {
        List<Map<String, Object>> stats = productService.getProductsByCategory();
        return ResponseEntity.ok(ApiResponse.success(stats, "Products by category retrieved successfully"));
    }
    
    @GetMapping("/statistics/by-brand")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getProductsByBrand() {
        List<Map<String, Object>> stats = productService.getProductsByBrand();
        return ResponseEntity.ok(ApiResponse.success(stats, "Products by brand retrieved successfully"));
    }
    
    @GetMapping("/statistics/average-selling-price")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getAverageSellingPrice() {
        Double avgPrice = productService.getAverageSellingPrice();
        return ResponseEntity.ok(ApiResponse.success(avgPrice, "Average selling price retrieved successfully"));
    }
    
    @GetMapping("/statistics/average-cost-price")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getAverageCostPrice() {
        Double avgCost = productService.getAverageCostPrice();
        return ResponseEntity.ok(ApiResponse.success(avgCost, "Average cost price retrieved successfully"));
    }
    
    @GetMapping("/statistics/average-margin")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getAverageMarginPercentage() {
        Double avgMargin = productService.getAverageMarginPercentage();
        return ResponseEntity.ok(ApiResponse.success(avgMargin, "Average margin percentage retrieved successfully"));
    }
    
    @GetMapping("/statistics/total-value")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getTotalProductValue() {
        Double totalValue = productService.getTotalProductValue();
        return ResponseEntity.ok(ApiResponse.success(totalValue, "Total product value retrieved successfully"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = productService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved successfully"));
    }
}
