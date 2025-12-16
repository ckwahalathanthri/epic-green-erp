package lk.epicgreen.erp.product.service;

import lk.epicgreen.erp.product.dto.ProductCategoryRequest;
import lk.epicgreen.erp.product.entity.ProductCategory;
import lk.epicgreen.erp.product.repository.ProductCategoryRepository;
import lk.epicgreen.erp.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ProductCategory Service Implementation
 * Implementation of product category service operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {
    
    private final ProductCategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    
    @Override
    public ProductCategory createCategory(ProductCategoryRequest request) {
        log.info("Creating category: {}", request.getCategoryName());
        
        // Validate unique fields
        if (categoryRepository.existsByCategoryCode(request.getCategoryCode())) {
            throw new RuntimeException("Category code already exists: " + request.getCategoryCode());
        }
        if (categoryRepository.existsByCategoryName(request.getCategoryName())) {
            throw new RuntimeException("Category name already exists: " + request.getCategoryName());
        }
        
        ProductCategory category = new ProductCategory();
        category.setCategoryCode(request.getCategoryCode());
        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());
        category.setParentCategoryId(request.getParentCategoryId());
        category.setIsActive(true);
        category.setIsLeaf(true); // Initially leaf until subcategories are added
        category.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        
        // Calculate level and path
        if (request.getParentCategoryId() != null) {
            ProductCategory parent = getCategoryById(request.getParentCategoryId());
            category.setLevel(parent.getLevel() + 1);
            category.setCategoryPath(parent.getCategoryPath() + category.getCategoryId() + "/");
            
            // Parent is no longer a leaf
            parent.setIsLeaf(false);
            categoryRepository.save(parent);
        } else {
            category.setLevel(1);
        }
        
        ProductCategory saved = categoryRepository.save(category);
        
        // Update path after save (now we have ID)
        saved.setCategoryPath(buildCategoryPath(saved));
        return categoryRepository.save(saved);
    }
    
    @Override
    public ProductCategory updateCategory(Long id, ProductCategoryRequest request) {
        log.info("Updating category: {}", id);
        ProductCategory existing = getCategoryById(id);
        
        // Validate unique fields if changed
        if (!existing.getCategoryCode().equals(request.getCategoryCode()) &&
            categoryRepository.existsByCategoryCode(request.getCategoryCode())) {
            throw new RuntimeException("Category code already exists: " + request.getCategoryCode());
        }
        if (!existing.getCategoryName().equals(request.getCategoryName()) &&
            categoryRepository.existsByCategoryName(request.getCategoryName())) {
            throw new RuntimeException("Category name already exists: " + request.getCategoryName());
        }
        
        existing.setCategoryCode(request.getCategoryCode());
        existing.setCategoryName(request.getCategoryName());
        existing.setDescription(request.getDescription());
        existing.setDisplayOrder(request.getDisplayOrder());
        existing.setUpdatedAt(LocalDateTime.now());
        
        return categoryRepository.save(existing);
    }
    
    @Override
    public void deleteCategory(Long id) {
        log.info("Deleting category: {}", id);
        ProductCategory category = getCategoryById(id);
        
        if (!canDeleteCategory(id)) {
            throw new RuntimeException("Cannot delete category with products or subcategories");
        }
        
        // Update parent if exists
        if (category.getParentCategoryId() != null) {
            Long parentId = category.getParentCategoryId();
            Long siblingCount = categoryRepository.countByParentCategoryId(parentId);
            if (siblingCount == 1) { // This is the last child
                ProductCategory parent = getCategoryById(parentId);
                parent.setIsLeaf(true);
                categoryRepository.save(parent);
            }
        }
        
        categoryRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProductCategory getCategoryById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProductCategory getCategoryByCode(String categoryCode) {
        return categoryRepository.findByCategoryCode(categoryCode)
            .orElseThrow(() -> new RuntimeException("Category not found with code: " + categoryCode));
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProductCategory getCategoryByName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName)
            .orElseThrow(() -> new RuntimeException("Category not found with name: " + categoryName));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductCategory> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductCategory> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductCategory> searchCategories(String keyword, Pageable pageable) {
        return categoryRepository.searchCategories(keyword, pageable);
    }
    
    @Override
    public ProductCategory activateCategory(Long id) {
        log.info("Activating category: {}", id);
        ProductCategory category = getCategoryById(id);
        category.setIsActive(true);
        category.setUpdatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }
    
    @Override
    public ProductCategory deactivateCategory(Long id) {
        log.info("Deactivating category: {}", id);
        ProductCategory category = getCategoryById(id);
        category.setIsActive(false);
        category.setUpdatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }
    
    @Override
    public ProductCategory addSubcategory(Long parentId, ProductCategoryRequest request) {
        log.info("Adding subcategory to parent: {}", parentId);
        ProductCategory parent = getCategoryById(parentId);
        
        request.setParentCategoryId(parentId);
        ProductCategory subcategory = createCategory(request);
        
        // Parent is no longer a leaf
        if (parent.getIsLeaf()) {
            parent.setIsLeaf(false);
            categoryRepository.save(parent);
        }
        
        return subcategory;
    }
    
    @Override
    public ProductCategory moveCategory(Long categoryId, Long newParentId) {
        log.info("Moving category {} to new parent: {}", categoryId, newParentId);
        ProductCategory category = getCategoryById(categoryId);
        
        // Validate not moving to a descendant
        if (newParentId != null && isDescendantOf(newParentId, categoryId)) {
            throw new RuntimeException("Cannot move category to its own descendant");
        }
        
        Long oldParentId = category.getParentCategoryId();
        
        // Update category
        category.setParentCategoryId(newParentId);
        
        if (newParentId != null) {
            ProductCategory newParent = getCategoryById(newParentId);
            category.setLevel(newParent.getLevel() + 1);
            newParent.setIsLeaf(false);
            categoryRepository.save(newParent);
        } else {
            category.setLevel(1);
        }
        
        category.setCategoryPath(buildCategoryPath(category));
        ProductCategory saved = categoryRepository.save(category);
        
        // Update old parent if no more children
        if (oldParentId != null) {
            Long siblingCount = categoryRepository.countByParentCategoryId(oldParentId);
            if (siblingCount == 0) {
                ProductCategory oldParent = getCategoryById(oldParentId);
                oldParent.setIsLeaf(true);
                categoryRepository.save(oldParent);
            }
        }
        
        // Recalculate levels for all descendants
        recalculateCategoryLevels(categoryId);
        
        return saved;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductCategory> getCategoryHierarchy(Long categoryId) {
        ProductCategory category = getCategoryById(categoryId);
        List<Long> hierarchyIds = parseCategoryPath(category.getCategoryPath());
        return categoryRepository.findCategoryHierarchy(hierarchyIds);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductCategory> getCategoryTree() {
        return categoryRepository.getCategoryTree();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductCategory> getCategoryTreeByParent(Long parentId) {
        ProductCategory parent = getCategoryById(parentId);
        return categoryRepository.getCategoryTreeByParent(parentId, parent.getCategoryPath());
    }
    
    @Override
    public void updateCategoryPath(Long categoryId) {
        ProductCategory category = getCategoryById(categoryId);
        category.setCategoryPath(buildCategoryPath(category));
        categoryRepository.save(category);
    }
    
    @Override
    public void recalculateCategoryLevels(Long parentId) {
        ProductCategory parent = getCategoryById(parentId);
        List<ProductCategory> descendants = categoryRepository
            .findAllSubcategoriesByParent(parentId, parent.getCategoryPath());
        
        for (ProductCategory descendant : descendants) {
            // Calculate level based on parent path
            int newLevel = parent.getLevel() + 1;
            if (descendant.getParentCategoryId() != null) {
                ProductCategory immediateParent = getCategoryById(descendant.getParentCategoryId());
                newLevel = immediateParent.getLevel() + 1;
            }
            descendant.setLevel(newLevel);
            descendant.setCategoryPath(buildCategoryPath(descendant));
            categoryRepository.save(descendant);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductCategory> getActiveCategories() {
        return categoryRepository.findActiveCategories();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductCategory> getActiveCategories(Pageable pageable) {
        return categoryRepository.findActiveCategories(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductCategory> getInactiveCategories() {
        return categoryRepository.findInactiveCategories();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductCategory> getRootCategories() {
        return categoryRepository.findRootCategories();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductCategory> getLeafCategories() {
        return categoryRepository.findLeafCategories();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductCategory> getCategoriesWithChildren() {
        return categoryRepository.findCategoriesWithChildren();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductCategory> getSubcategories(Long parentId) {
        return categoryRepository.findSubcategoriesByParent(parentId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductCategory> getAllSubcategories(Long parentId) {
        ProductCategory parent = getCategoryById(parentId);
        return categoryRepository.findAllSubcategoriesByParent(parentId, parent.getCategoryPath());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductCategory> getSiblingCategories(Long categoryId) {
        ProductCategory category = getCategoryById(categoryId);
        if (category.getParentCategoryId() == null) {
            return Collections.emptyList();
        }
        return categoryRepository.findSiblingCategories(categoryId, category.getParentCategoryId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductCategory> getCategoriesByLevel(Integer level) {
        return categoryRepository.findCategoriesByLevel(level);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductCategory> getCategoriesByMaxDepth(Integer maxLevel) {
        return categoryRepository.findCategoriesByMaxDepth(maxLevel);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductCategory> getCategoriesWithProducts() {
        return categoryRepository.findCategoriesWithProducts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductCategory> getEmptyCategories() {
        return categoryRepository.findEmptyCategories();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductCategory> getRecentCategories(int limit) {
        return categoryRepository.findRecentCategories(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Integer getMaxDepth() {
        Integer maxDepth = categoryRepository.getMaxDepth();
        return maxDepth != null ? maxDepth : 0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validateCategory(ProductCategory category) {
        return category.getCategoryCode() != null &&
               category.getCategoryName() != null;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isCategoryCodeAvailable(String categoryCode) {
        return !categoryRepository.existsByCategoryCode(categoryCode);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isCategoryNameAvailable(String categoryName) {
        return !categoryRepository.existsByCategoryName(categoryName);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canDeleteCategory(Long categoryId) {
        return !hasProducts(categoryId) && !hasSubcategories(categoryId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasProducts(Long categoryId) {
        return productRepository.countByCategoryId(categoryId) > 0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasSubcategories(Long categoryId) {
        return categoryRepository.countByParentCategoryId(categoryId) > 0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isDescendantOf(Long categoryId, Long ancestorId) {
        ProductCategory category = getCategoryById(categoryId);
        ProductCategory ancestor = getCategoryById(ancestorId);
        return category.getCategoryPath() != null &&
               category.getCategoryPath().contains("/" + ancestorId + "/");
    }
    
    @Override
    public List<ProductCategory> createBulkCategories(List<ProductCategoryRequest> requests) {
        return requests.stream()
            .map(this::createCategory)
            .collect(Collectors.toList());
    }
    
    @Override
    public int activateBulkCategories(List<Long> categoryIds) {
        int count = 0;
        for (Long id : categoryIds) {
            try {
                activateCategory(id);
                count++;
            } catch (Exception e) {
                log.error("Error activating category: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int deactivateBulkCategories(List<Long> categoryIds) {
        int count = 0;
        for (Long id : categoryIds) {
            try {
                deactivateCategory(id);
                count++;
            } catch (Exception e) {
                log.error("Error deactivating category: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int deleteBulkCategories(List<Long> categoryIds) {
        int count = 0;
        for (Long id : categoryIds) {
            try {
                deleteCategory(id);
                count++;
            } catch (Exception e) {
                log.error("Error deleting category: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getCategoryStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalCategories", categoryRepository.count());
        stats.put("activeCategories", categoryRepository.countActiveCategories());
        stats.put("inactiveCategories", categoryRepository.countInactiveCategories());
        stats.put("rootCategories", categoryRepository.countRootCategories());
        stats.put("leafCategories", categoryRepository.countLeafCategories());
        stats.put("maxDepth", getMaxDepth());
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getLevelDistribution() {
        List<Object[]> results = categoryRepository.getLevelDistribution();
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("level", result[0]);
                map.put("categoryCount", result[1]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCategoriesByProductCount() {
        List<Object[]> results = categoryRepository.getCategoriesByProductCount();
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
    public Long countProductsInCategory(Long categoryId) {
        return productRepository.countByCategoryId(categoryId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long countSubcategories(Long categoryId) {
        return categoryRepository.countByParentCategoryId(categoryId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("statistics", getCategoryStatistics());
        dashboard.put("levelDistribution", getLevelDistribution());
        dashboard.put("categoriesByProductCount", getCategoriesByProductCount());
        dashboard.put("rootCategories", getRootCategories());
        dashboard.put("recentCategories", getRecentCategories(10));
        
        return dashboard;
    }
    
    // ===================================================================
    // HELPER METHODS
    // ===================================================================
    
    private String buildCategoryPath(ProductCategory category) {
        if (category.getParentCategoryId() == null) {
            return "/" + category.getCategoryId() + "/";
        }
        
        ProductCategory parent = getCategoryById(category.getParentCategoryId());
        return parent.getCategoryPath() + category.getCategoryId() + "/";
    }
    
    private List<Long> parseCategoryPath(String path) {
        if (path == null || path.isEmpty()) {
            return Collections.emptyList();
        }
        
        return Arrays.stream(path.split("/"))
            .filter(s -> !s.isEmpty())
            .map(Long::parseLong)
            .collect(Collectors.toList());
    }
}
