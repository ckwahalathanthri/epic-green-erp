package lk.epicgreen.erp.product.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.product.dto.ProductCategoryRequest;
import lk.epicgreen.erp.product.entity.ProductCategory;
import lk.epicgreen.erp.product.service.ProductCategoryService;
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
 * Product Category Controller
 * REST controller for product category operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/products/categories")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductCategoryController {
    
    private final ProductCategoryService categoryService;
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<ProductCategory>> createCategory(@Valid @RequestBody ProductCategoryRequest request) {
        log.info("Creating product category: {}", request.getCategoryName());
        ProductCategory created = categoryService.createCategory(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Category created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<ProductCategory>> updateCategory(
        @PathVariable Long id,
        @Valid @RequestBody ProductCategoryRequest request
    ) {
        log.info("Updating product category: {}", id);
        ProductCategory updated = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Category updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        log.info("Deleting product category: {}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Category deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<ProductCategory>> getCategoryById(@PathVariable Long id) {
        ProductCategory category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success(category, "Category retrieved successfully"));
    }
    
    @GetMapping("/code/{categoryCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<ProductCategory>> getCategoryByCode(@PathVariable String categoryCode) {
        ProductCategory category = categoryService.getCategoryByCode(categoryCode);
        return ResponseEntity.ok(ApiResponse.success(category, "Category retrieved successfully"));
    }
    
    @GetMapping("/name/{categoryName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<ProductCategory>> getCategoryByName(@PathVariable String categoryName) {
        ProductCategory category = categoryService.getCategoryByName(categoryName);
        return ResponseEntity.ok(ApiResponse.success(category, "Category retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Page<ProductCategory>>> getAllCategories(Pageable pageable) {
        Page<ProductCategory> categories = categoryService.getAllCategories(pageable);
        return ResponseEntity.ok(ApiResponse.success(categories, "Categories retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getAllCategoriesList() {
        List<ProductCategory> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(categories, "Categories list retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Page<ProductCategory>>> searchCategories(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<ProductCategory> categories = categoryService.searchCategories(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(categories, "Search results retrieved successfully"));
    }
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<ProductCategory>> activateCategory(@PathVariable Long id) {
        log.info("Activating category: {}", id);
        ProductCategory activated = categoryService.activateCategory(id);
        return ResponseEntity.ok(ApiResponse.success(activated, "Category activated successfully"));
    }
    
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<ProductCategory>> deactivateCategory(@PathVariable Long id) {
        log.info("Deactivating category: {}", id);
        ProductCategory deactivated = categoryService.deactivateCategory(id);
        return ResponseEntity.ok(ApiResponse.success(deactivated, "Category deactivated successfully"));
    }
    
    // ===================================================================
    // HIERARCHY OPERATIONS
    // ===================================================================
    
    @PutMapping("/{categoryId}/parent/{parentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> setParentCategory(
        @PathVariable Long categoryId,
        @PathVariable Long parentId
    ) {
        log.info("Setting parent category {} for category {}", parentId, categoryId);
        categoryService.setParentCategory(categoryId, parentId);
        return ResponseEntity.ok(ApiResponse.success(null, "Parent category set successfully"));
    }
    
    @DeleteMapping("/{categoryId}/parent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> removeParentCategory(@PathVariable Long categoryId) {
        log.info("Removing parent category for category: {}", categoryId);
        categoryService.removeParentCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(null, "Parent category removed successfully"));
    }
    
    @GetMapping("/{parentId}/children")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getChildCategories(@PathVariable Long parentId) {
        List<ProductCategory> children = categoryService.getChildCategories(parentId);
        return ResponseEntity.ok(ApiResponse.success(children, "Child categories retrieved successfully"));
    }
    
    @GetMapping("/root")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getRootCategories() {
        List<ProductCategory> rootCategories = categoryService.getRootCategories();
        return ResponseEntity.ok(ApiResponse.success(rootCategories, "Root categories retrieved successfully"));
    }
    
    @GetMapping("/leaf")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getLeafCategories() {
        List<ProductCategory> leafCategories = categoryService.getLeafCategories();
        return ResponseEntity.ok(ApiResponse.success(leafCategories, "Leaf categories retrieved successfully"));
    }
    
    @GetMapping("/{categoryId}/path")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getCategoryPath(@PathVariable Long categoryId) {
        List<ProductCategory> path = categoryService.getCategoryPath(categoryId);
        return ResponseEntity.ok(ApiResponse.success(path, "Category path retrieved successfully"));
    }
    
    @GetMapping("/{categoryId}/descendants")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getAllDescendants(@PathVariable Long categoryId) {
        List<ProductCategory> descendants = categoryService.getAllDescendants(categoryId);
        return ResponseEntity.ok(ApiResponse.success(descendants, "Descendant categories retrieved successfully"));
    }
    
    @GetMapping("/tree")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getCategoryTree() {
        List<ProductCategory> tree = categoryService.getCategoryTree();
        return ResponseEntity.ok(ApiResponse.success(tree, "Category tree retrieved successfully"));
    }
    
    @GetMapping("/{categoryId}/depth")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> getCategoryDepth(@PathVariable Long categoryId) {
        int depth = categoryService.getCategoryDepth(categoryId);
        return ResponseEntity.ok(ApiResponse.success(depth, "Category depth retrieved successfully"));
    }
    
    @GetMapping("/max-depth")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> getMaxDepth() {
        int maxDepth = categoryService.getMaxDepth();
        return ResponseEntity.ok(ApiResponse.success(maxDepth, "Maximum depth retrieved successfully"));
    }
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getActiveCategories() {
        List<ProductCategory> categories = categoryService.getActiveCategories();
        return ResponseEntity.ok(ApiResponse.success(categories, "Active categories retrieved successfully"));
    }
    
    @GetMapping("/inactive")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getInactiveCategories() {
        List<ProductCategory> categories = categoryService.getInactiveCategories();
        return ResponseEntity.ok(ApiResponse.success(categories, "Inactive categories retrieved successfully"));
    }
    
    @GetMapping("/level/{level}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getCategoriesByLevel(@PathVariable Integer level) {
        List<ProductCategory> categories = categoryService.getCategoriesByLevel(level);
        return ResponseEntity.ok(ApiResponse.success(categories, "Categories by level retrieved successfully"));
    }
    
    @GetMapping("/with-products")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getCategoriesWithProducts() {
        List<ProductCategory> categories = categoryService.getCategoriesWithProducts();
        return ResponseEntity.ok(ApiResponse.success(categories, "Categories with products retrieved successfully"));
    }
    
    @GetMapping("/without-products")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getCategoriesWithoutProducts() {
        List<ProductCategory> categories = categoryService.getCategoriesWithoutProducts();
        return ResponseEntity.ok(ApiResponse.success(categories, "Categories without products retrieved successfully"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getRecentCategories(@RequestParam(defaultValue = "10") int limit) {
        List<ProductCategory> categories = categoryService.getRecentCategories(limit);
        return ResponseEntity.ok(ApiResponse.success(categories, "Recent categories retrieved successfully"));
    }
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    @GetMapping("/validate/code/{categoryCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> isCategoryCodeAvailable(@PathVariable String categoryCode) {
        boolean available = categoryService.isCategoryCodeAvailable(categoryCode);
        return ResponseEntity.ok(ApiResponse.success(available, "Category code availability checked"));
    }
    
    @GetMapping("/validate/name/{categoryName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> isCategoryNameAvailable(@PathVariable String categoryName) {
        boolean available = categoryService.isCategoryNameAvailable(categoryName);
        return ResponseEntity.ok(ApiResponse.success(available, "Category name availability checked"));
    }
    
    @GetMapping("/{id}/can-delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> canDeleteCategory(@PathVariable Long id) {
        boolean canDelete = categoryService.canDeleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(canDelete, "Delete check completed"));
    }
    
    @GetMapping("/{categoryId}/has-products")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> hasProducts(@PathVariable Long categoryId) {
        boolean hasProducts = categoryService.hasProducts(categoryId);
        return ResponseEntity.ok(ApiResponse.success(hasProducts, "Product check completed"));
    }
    
    @GetMapping("/{categoryId}/has-children")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> hasChildren(@PathVariable Long categoryId) {
        boolean hasChildren = categoryService.hasChildren(categoryId);
        return ResponseEntity.ok(ApiResponse.success(hasChildren, "Children check completed"));
    }
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCategoryStatistics() {
        Map<String, Object> statistics = categoryService.getCategoryStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Category statistics retrieved successfully"));
    }
    
    @GetMapping("/statistics/level-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getLevelDistribution() {
        List<Map<String, Object>> distribution = categoryService.getLevelDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Level distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/by-product-count")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getCategoriesByProductCount() {
        List<Map<String, Object>> stats = categoryService.getCategoriesByProductCount();
        return ResponseEntity.ok(ApiResponse.success(stats, "Categories by product count retrieved successfully"));
    }
    
    @GetMapping("/{categoryId}/product-count")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Long>> countProductsInCategory(@PathVariable Long categoryId) {
        Long count = categoryService.countProductsInCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(count, "Product count retrieved successfully"));
    }
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    @PostMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> createBulkCategories(
        @Valid @RequestBody List<ProductCategoryRequest> requests
    ) {
        log.info("Creating {} categories in bulk", requests.size());
        List<ProductCategory> categories = categoryService.createBulkCategories(requests);
        return ResponseEntity.ok(ApiResponse.success(categories, categories.size() + " categories created successfully"));
    }
    
    @PutMapping("/bulk/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> activateBulkCategories(@RequestBody List<Long> categoryIds) {
        log.info("Activating {} categories in bulk", categoryIds.size());
        int count = categoryService.activateBulkCategories(categoryIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " categories activated successfully"));
    }
    
    @PutMapping("/bulk/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> deactivateBulkCategories(@RequestBody List<Long> categoryIds) {
        log.info("Deactivating {} categories in bulk", categoryIds.size());
        int count = categoryService.deactivateBulkCategories(categoryIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " categories deactivated successfully"));
    }
    
    @DeleteMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> deleteBulkCategories(@RequestBody List<Long> categoryIds) {
        log.info("Deleting {} categories in bulk", categoryIds.size());
        int count = categoryService.deleteBulkCategories(categoryIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " categories deleted successfully"));
    }
}
