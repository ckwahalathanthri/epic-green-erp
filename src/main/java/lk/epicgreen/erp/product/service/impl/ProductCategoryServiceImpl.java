package lk.epicgreen.erp.product.service.impl;

import lk.epicgreen.erp.product.dto.request.ProductCategoryRequest;
import lk.epicgreen.erp.product.dto.response.ProductCategoryResponse;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.entity.ProductCategory;
import lk.epicgreen.erp.product.mapper.ProductCategoryMapper;
import lk.epicgreen.erp.product.repository.ProductCategoryRepository;
import lk.epicgreen.erp.product.service.ProductCategoryService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lk.epicgreen.erp.common.exception.InvalidOperationException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of ProductCategoryService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;

    @Override
    @Transactional
    public ProductCategoryResponse createProductCategory(ProductCategoryRequest request) {
        log.info("Creating new product category: {}", request.getCategoryCode());

        // Validate unique constraints
        validateUniqueCategoryCode(request.getCategoryCode(), null);

        // Create category entity
        ProductCategory category = productCategoryMapper.toEntity(request);

        // Set parent category if provided
        if (request.getParentCategoryId() != null) {
            ProductCategory parentCategory = findProductCategoryById(request.getParentCategoryId());
            category.setParentCategory(parentCategory);
        }

        ProductCategory savedCategory = productCategoryRepository.save(category);
        log.info("Product category created successfully: {}", savedCategory.getCategoryCode());

        return productCategoryMapper.toResponse(savedCategory);
    }

    @Override
    @Transactional
    public ProductCategoryResponse updateProductCategory(Long id, ProductCategoryRequest request) {
        log.info("Updating product category: {}", id);

        ProductCategory category = findProductCategoryById(id);

        // Validate unique constraints
        validateUniqueCategoryCode(request.getCategoryCode(), id);

        // Update fields
        productCategoryMapper.updateEntityFromRequest(request, category);

        // Update parent category if provided
        if (request.getParentCategoryId() != null) {
            // Prevent circular reference
            if (id.equals(request.getParentCategoryId())) {
                throw new InvalidOperationException("Category cannot be its own parent");
            }
            ProductCategory parentCategory = findProductCategoryById(request.getParentCategoryId());
            category.setParentCategory(parentCategory);
        } else {
            category.setParentCategory(null);
        }

        ProductCategory updatedCategory = productCategoryRepository.save(category);
        log.info("Product category updated successfully: {}", updatedCategory.getCategoryCode());

        return productCategoryMapper.toResponse(updatedCategory);
    }

    @Override
    @Transactional
    public void activateProductCategory(Long id) {
        log.info("Activating product category: {}", id);

        ProductCategory category = findProductCategoryById(id);
        category.setIsActive(true);
        productCategoryRepository.save(category);

        log.info("Product category activated successfully: {}", id);
    }

    @Override
    @Transactional
    public void deactivateProductCategory(Long id) {
        log.info("Deactivating product category: {}", id);

        ProductCategory category = findProductCategoryById(id);
        category.setIsActive(false);
        productCategoryRepository.save(category);

        log.info("Product category deactivated successfully: {}", id);
    }

    @Override
    @Transactional
    public void deleteProductCategory(Long id) {
        log.info("Deleting product category: {}", id);

        ProductCategory category = findProductCategoryById(id);

        // Check if category has child categories
        long childCount = productCategoryRepository.countByParentCategoryId(id);
        if (childCount > 0) {
            throw new InvalidOperationException(
                "Cannot delete category. It has " + childCount + " child categories");
        }

        // Check if category has products
        long productCount = productCategoryRepository.countProductsByCategoryId(id);
        if (productCount > 0) {
            throw new InvalidOperationException(
                "Cannot delete category. It has " + productCount + " products");
        }

        productCategoryRepository.delete(category);
        log.info("Product category deleted successfully: {}", id);
    }

    @Override
    public ProductCategoryResponse getProductCategoryById(Long id) {
        ProductCategory category = findProductCategoryById(id);
        return productCategoryMapper.toResponse(category);
    }

    @Override
    public ProductCategoryResponse getProductCategoryByCode(String categoryCode) {
        ProductCategory category = productCategoryRepository.findByCategoryCode(categoryCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product category not found: " + categoryCode));
        return productCategoryMapper.toResponse(category);
    }

    @Override
    public List<ProductCategoryResponse> getAllProductCategories() {
        List<ProductCategory> categories = productCategoryRepository.findAll();
        return categories.stream()
            .map(productCategoryMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<ProductCategoryResponse> getAllProductCategories(Pageable pageable) {
        Page<ProductCategory> categoryPage = productCategoryRepository.findAll(pageable);
        return createPageResponse(categoryPage);
    }

    @Override
    public List<ProductCategoryResponse> getAllActiveProductCategories() {
        List<ProductCategory> categories = productCategoryRepository.findByIsActiveTrue();
        return categories.stream()
            .map(productCategoryMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductCategoryResponse> getRootCategories() {
        List<ProductCategory> categories = productCategoryRepository.findRootCategories();
        return categories.stream()
            .map(productCategoryMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductCategoryResponse> getChildCategories(Long parentId) {
        List<ProductCategory> categories = productCategoryRepository.findByParentCategoryId(parentId);
        return categories.stream()
            .map(productCategoryMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<ProductCategoryResponse> searchProductCategories(String keyword, Pageable pageable) {
        Page<ProductCategory> categoryPage = productCategoryRepository.searchCategories(keyword, pageable);
        return createPageResponse(categoryPage);
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private ProductCategory findProductCategoryById(Long id) {
        return productCategoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product category not found: " + id));
    }

    private void validateUniqueCategoryCode(String categoryCode, Long excludeId) {
        if (excludeId == null) {
            if (productCategoryRepository.existsByCategoryCode(categoryCode)) {
                throw new DuplicateResourceException("Category code already exists: " + categoryCode);
            }
        } else {
            if (productCategoryRepository.existsByCategoryCodeAndIdNot(categoryCode, excludeId)) {
                throw new DuplicateResourceException("Category code already exists: " + categoryCode);
            }
        }
    }

    private PageResponse<ProductCategoryResponse> createPageResponse(Page<ProductCategory> categoryPage) {
        List<ProductCategoryResponse> content = categoryPage.getContent().stream()
            .map(productCategoryMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<ProductCategoryResponse>builder()
            .content(content)
            .pageNumber(categoryPage.getNumber())
            .pageSize(categoryPage.getSize())
            .totalElements(categoryPage.getTotalElements())
            .totalPages(categoryPage.getTotalPages())
            .last(categoryPage.isLast())
            .first(categoryPage.isFirst())
            .empty(categoryPage.isEmpty())
            .build();
    }

    @Override
    public ProductCategory createCategory(ProductCategoryRequest request) {
        String categoryCode = request.getCategoryCode();
        log.info("Creating new product category: {}", categoryCode);
        validateUniqueCategoryCode(categoryCode, null);
        ProductCategory category = productCategoryMapper.toEntity(request);
        if (request.getParentCategoryId() != null) {
            ProductCategory parentCategory = findProductCategoryById(request.getParentCategoryId());
            category.setParentCategory(parentCategory);
        }
        ProductCategory savedCategory = productCategoryRepository.save(category);
        log.info("Product category created successfully: {}", savedCategory.getCategoryCode());
        return savedCategory;
    }

    @Override
    public ProductCategory updateCategory(Long id, ProductCategoryRequest request) {
        log.info("Updating product category: {}", id);
        ProductCategory category = findProductCategoryById(id);
        validateUniqueCategoryCode(request.getCategoryCode(), id);
        productCategoryMapper.updateEntityFromRequest(request, category);
        if (request.getParentCategoryId() != null) {
            if (id.equals(request.getParentCategoryId())) {
                throw new InvalidOperationException("Category cannot be its own parent");
            }
            ProductCategory parentCategory = findProductCategoryById(request.getParentCategoryId());
            category.setParentCategory(parentCategory);
        } else {
            category.setParentCategory(null);
        }
        ProductCategory updatedCategory = productCategoryRepository.save(category);
        log.info("Product category updated successfully: {}", updatedCategory.getCategoryCode());
        return updatedCategory;
    }

    @Override
    public void deleteCategory(Long id) {
        log.info("Deleting product category: {}", id);
        ProductCategory category = findProductCategoryById(id);
        long childCount = productCategoryRepository.countByParentCategoryId(id);
        if (childCount > 0) {
            throw new InvalidOperationException(
                "Cannot delete category. It has " + childCount + " child categories");
        }
        long productCount = productCategoryRepository.countProductsByCategoryId(id);
        if (productCount > 0) {
            throw new InvalidOperationException(
                "Cannot delete category. It has " + productCount + " products");
        }
        productCategoryRepository.delete(category);
        log.info("Product category deleted successfully: {}", id);
    }

    @Override
    public ProductCategory getCategoryById(Long id) {
        return findProductCategoryById(id);
    }

    @Override
    public ProductCategory getCategoryByCode(String categoryCode) {
        return findProductCategoryByCode(categoryCode);
    }

    private ProductCategory findProductCategoryByCode(String categoryCode) {
        return productCategoryRepository.findByCategoryCode(categoryCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product category not found: " + categoryCode));
    }

    @Override
    public ProductCategory getCategoryByName(String categoryName) {
        return findProductCategoryByName(categoryName);
    }

    private ProductCategory findProductCategoryByName(String categoryName) {
        return productCategoryRepository.findByCategoryName(categoryName)
            .orElseThrow(() -> new ResourceNotFoundException("Product category not found: " + categoryName));
    }

    @Override
    public Page<ProductCategory> getAllCategories(Pageable pageable) {
        return productCategoryRepository.findAll(pageable);
    }

    @Override
    public List<ProductCategory> getAllCategories() {
        return productCategoryRepository.findAll();
    }

    @Override
    public Page<ProductCategory> searchCategories(String keyword, Pageable pageable) {
        return productCategoryRepository.searchCategories(keyword, pageable);
    }

    @Override
    public ProductCategory activateCategory(Long id) {
        ProductCategory category = findProductCategoryById(id);
        category.setIsActive(true);
        return productCategoryRepository.save(category);
    }

    @Override
    public ProductCategory deactivateCategory(Long id) {
        ProductCategory category = findProductCategoryById(id);
        category.setIsActive(false);
        return productCategoryRepository.save(category);
    }

    @Override
    public void setParentCategory(Long categoryId, Long parentId) {
        ProductCategory category = findProductCategoryById(categoryId);
        ProductCategory parentCategory = findProductCategoryById(parentId);
        category.setParentCategory(parentCategory);
        productCategoryRepository.save(category);
    }

    @Override
    public void removeParentCategory(Long categoryId) {
        ProductCategory category = findProductCategoryById(categoryId);
        category.setParentCategory(null);
        productCategoryRepository.save(category);
    }

    @Override
    public List<ProductCategory> getLeafCategories() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLeafCategories'");
    }

    @Override
    public List<ProductCategory> getCategoryPath(Long categoryId) {
      ProductCategory category = findProductCategoryById(categoryId);
        List<ProductCategory> path = new ArrayList<>();
        while (category != null) {
            path.add(0, category); // Add to the beginning to maintain order from root to leaf
            category = category.getParentCategory();
        }
        return path;
    }

    @Override
    public List<ProductCategory> getAllDescendants(Long categoryId) {
        ProductCategory category = findProductCategoryById(categoryId);
        List<ProductCategory> descendants = new ArrayList<>();
        fetchDescendants(category, descendants);
        return descendants;
    }

    private void fetchDescendants(ProductCategory category, List<ProductCategory> descendants) {
        List<ProductCategory> children = productCategoryRepository.findByParentCategoryId(category.getId());
        for (ProductCategory child : children) {
            descendants.add(child);
            fetchDescendants(child, descendants);
        }
    }

    @Override
    public List<ProductCategory> getCategoryTree() {
        List<ProductCategory> roots = productCategoryRepository.findRootCategories();
        for (ProductCategory root : roots) {
            populateChildren(root);
        }
        return roots;
    }

    private void populateChildren(ProductCategory root) {
        List<ProductCategory> children = productCategoryRepository.findByParentCategoryId(root.getId());
        root.setChildren(children);
        for (ProductCategory child : children) {
            populateChildren(child);
        }
    }

    @Override
    public int getCategoryDepth(Long categoryId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCategoryDepth'");
    }

    @Override
    public int getMaxDepth() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMaxDepth'");
    }

    @Override
    public List<ProductCategoryResponse> getActiveCategories() {
        return productCategoryRepository.findByIsActiveTrue()
            .stream()
            .map(productCategoryMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductCategory> getInactiveCategories() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInactiveCategories'");
    }

    @Override
    public List<ProductCategory> getCategoriesByLevel(Integer level) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCategoriesByLevel'");
    }

    @Override
    public List<ProductCategory> getCategoriesWithProducts() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCategoriesWithProducts'");
    }

    @Override
    public List<ProductCategory> getCategoriesWithoutProducts() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCategoriesWithoutProducts'");
    }

    @Override
    public List<ProductCategory> getRecentCategories(int limit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRecentCategories'");
    }

    @Override
    public boolean isCategoryCodeAvailable(String categoryCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isCategoryCodeAvailable'");
    }

    @Override
    public boolean isCategoryNameAvailable(String categoryName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isCategoryNameAvailable'");
    }

    @Override
    public boolean canDeleteCategory(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canDeleteCategory'");
    }

    @Override
    public boolean hasProducts(Long categoryId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasProducts'");
    }

    @Override
    public boolean hasChildren(Long categoryId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasChildren'");
    }

    @Override
    public Map<String, Object> getCategoryStatistics() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCategoryStatistics'");
    }

    @Override
    public List<Map<String, Object>> getLevelDistribution() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLevelDistribution'");
    }

    @Override
    public List<Map<String, Object>> getCategoriesByProductCount() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCategoriesByProductCount'");
    }

    @Override
    public Long countProductsInCategory(Long categoryId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'countProductsInCategory'");
    }

    @Override
    public List<ProductCategory> createBulkCategories(List<ProductCategoryRequest> requests) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createBulkCategories'");
    }

    @Override
    public int activateBulkCategories(List<Long> categoryIds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'activateBulkCategories'");
    }

    @Override
    public int deactivateBulkCategories(List<Long> categoryIds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deactivateBulkCategories'");
    }

    @Override
    public int deleteBulkCategories(List<Long> categoryIds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteBulkCategories'");
    }
}
