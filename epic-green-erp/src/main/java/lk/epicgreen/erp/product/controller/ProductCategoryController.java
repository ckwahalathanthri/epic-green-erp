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
 * ProductCategory Controller
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
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<ProductCategory>> createCategory(
        @Valid @RequestBody ProductCategoryRequest request
    ) {
        ProductCategory created = categoryService.createCategory(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Category created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<ProductCategory>> updateCategory(
        @PathVariable Long id,
        @Valid @RequestBody ProductCategoryRequest request
    ) {
        ProductCategory updated = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Category updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
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
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Page<ProductCategory>>> searchCategories(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<ProductCategory> categories = categoryService.searchCategories(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(categories, "Search results retrieved"));
    }
    
    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<ProductCategory>> activateCategory(@PathVariable Long id) {
        ProductCategory activated = categoryService.activateCategory(id);
        return ResponseEntity.ok(ApiResponse.success(activated, "Category activated"));
    }
    
    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<ProductCategory>> deactivateCategory(@PathVariable Long id) {
        ProductCategory deactivated = categoryService.deactivateCategory(id);
        return ResponseEntity.ok(ApiResponse.success(deactivated, "Category deactivated"));
    }
    
    @PostMapping("/{parentId}/subcategory")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<ProductCategory>> addSubcategory(
        @PathVariable Long parentId,
        @Valid @RequestBody ProductCategoryRequest request
    ) {
        ProductCategory subcategory = categoryService.addSubcategory(parentId, request);
        return ResponseEntity.ok(ApiResponse.success(subcategory, "Subcategory added successfully"));
    }
    
    @PostMapping("/{categoryId}/move")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<ProductCategory>> moveCategory(
        @PathVariable Long categoryId,
        @RequestParam(required = false) Long newParentId
    ) {
        ProductCategory moved = categoryService.moveCategory(categoryId, newParentId);
        return ResponseEntity.ok(ApiResponse.success(moved, "Category moved successfully"));
    }
    
    @GetMapping("/{id}/hierarchy")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getCategoryHierarchy(@PathVariable Long id) {
        List<ProductCategory> hierarchy = categoryService.getCategoryHierarchy(id);
        return ResponseEntity.ok(ApiResponse.success(hierarchy, "Category hierarchy retrieved"));
    }
    
    @GetMapping("/tree")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getCategoryTree() {
        List<ProductCategory> tree = categoryService.getCategoryTree();
        return ResponseEntity.ok(ApiResponse.success(tree, "Category tree retrieved"));
    }
    
    @GetMapping("/{parentId}/tree")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getCategoryTreeByParent(@PathVariable Long parentId) {
        List<ProductCategory> tree = categoryService.getCategoryTreeByParent(parentId);
        return ResponseEntity.ok(ApiResponse.success(tree, "Category tree by parent retrieved"));
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Page<ProductCategory>>> getActiveCategories(Pageable pageable) {
        Page<ProductCategory> categories = categoryService.getActiveCategories(pageable);
        return ResponseEntity.ok(ApiResponse.success(categories, "Active categories retrieved"));
    }
    
    @GetMapping("/inactive")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getInactiveCategories() {
        List<ProductCategory> categories = categoryService.getInactiveCategories();
        return ResponseEntity.ok(ApiResponse.success(categories, "Inactive categories retrieved"));
    }
    
    @GetMapping("/root")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getRootCategories() {
        List<ProductCategory> categories = categoryService.getRootCategories();
        return ResponseEntity.ok(ApiResponse.success(categories, "Root categories retrieved"));
    }
    
    @GetMapping("/leaf")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getLeafCategories() {
        List<ProductCategory> categories = categoryService.getLeafCategories();
        return ResponseEntity.ok(ApiResponse.success(categories, "Leaf categories retrieved"));
    }
    
    @GetMapping("/with-children")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getCategoriesWithChildren() {
        List<ProductCategory> categories = categoryService.getCategoriesWithChildren();
        return ResponseEntity.ok(ApiResponse.success(categories, "Categories with children retrieved"));
    }
    
    @GetMapping("/{parentId}/subcategories")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getSubcategories(@PathVariable Long parentId) {
        List<ProductCategory> subcategories = categoryService.getSubcategories(parentId);
        return ResponseEntity.ok(ApiResponse.success(subcategories, "Subcategories retrieved"));
    }
    
    @GetMapping("/{parentId}/all-subcategories")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getAllSubcategories(@PathVariable Long parentId) {
        List<ProductCategory> subcategories = categoryService.getAllSubcategories(parentId);
        return ResponseEntity.ok(ApiResponse.success(subcategories, "All subcategories retrieved"));
    }
    
    @GetMapping("/{id}/siblings")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getSiblingCategories(@PathVariable Long id) {
        List<ProductCategory> siblings = categoryService.getSiblingCategories(id);
        return ResponseEntity.ok(ApiResponse.success(siblings, "Sibling categories retrieved"));
    }
    
    @GetMapping("/level/{level}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getCategoriesByLevel(@PathVariable Integer level) {
        List<ProductCategory> categories = categoryService.getCategoriesByLevel(level);
        return ResponseEntity.ok(ApiResponse.success(categories, "Categories by level retrieved"));
    }
    
    @GetMapping("/max-depth/{maxLevel}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getCategoriesByMaxDepth(@PathVariable Integer maxLevel) {
        List<ProductCategory> categories = categoryService.getCategoriesByMaxDepth(maxLevel);
        return ResponseEntity.ok(ApiResponse.success(categories, "Categories by max depth retrieved"));
    }
    
    @GetMapping("/with-products")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getCategoriesWithProducts() {
        List<ProductCategory> categories = categoryService.getCategoriesWithProducts();
        return ResponseEntity.ok(ApiResponse.success(categories, "Categories with products retrieved"));
    }
    
    @GetMapping("/empty")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getEmptyCategories() {
        List<ProductCategory> categories = categoryService.getEmptyCategories();
        return ResponseEntity.ok(ApiResponse.success(categories, "Empty categories retrieved"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getRecentCategories(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<ProductCategory> categories = categoryService.getRecentCategories(limit);
        return ResponseEntity.ok(ApiResponse.success(categories, "Recent categories retrieved"));
    }
    
    @GetMapping("/max-depth")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Integer>> getMaxDepth() {
        Integer maxDepth = categoryService.getMaxDepth();
        return ResponseEntity.ok(ApiResponse.success(maxDepth, "Max depth retrieved"));
    }
    
    @GetMapping("/{id}/product-count")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Long>> countProductsInCategory(@PathVariable Long id) {
        Long count = categoryService.countProductsInCategory(id);
        return ResponseEntity.ok(ApiResponse.success(count, "Product count retrieved"));
    }
    
    @GetMapping("/{id}/subcategory-count")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Long>> countSubcategories(@PathVariable Long id) {
        Long count = categoryService.countSubcategories(id);
        return ResponseEntity.ok(ApiResponse.success(count, "Subcategory count retrieved"));
    }
    
    @GetMapping("/{id}/has-products")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Boolean>> hasProducts(@PathVariable Long id) {
        Boolean hasProducts = categoryService.hasProducts(id);
        return ResponseEntity.ok(ApiResponse.success(hasProducts, "Has products check completed"));
    }
    
    @GetMapping("/{id}/has-subcategories")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Boolean>> hasSubcategories(@PathVariable Long id) {
        Boolean hasSubcategories = categoryService.hasSubcategories(id);
        return ResponseEntity.ok(ApiResponse.success(hasSubcategories, "Has subcategories check completed"));
    }
    
    @GetMapping("/{categoryId}/is-descendant-of/{ancestorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Boolean>> isDescendantOf(
        @PathVariable Long categoryId,
        @PathVariable Long ancestorId
    ) {
        Boolean isDescendant = categoryService.isDescendantOf(categoryId, ancestorId);
        return ResponseEntity.ok(ApiResponse.success(isDescendant, "Descendant check completed"));
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = categoryService.getCategoryStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCT_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = categoryService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
}
