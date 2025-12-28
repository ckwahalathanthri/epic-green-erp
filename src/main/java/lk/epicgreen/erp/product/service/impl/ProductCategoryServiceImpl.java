package lk.epicgreen.erp.product.service.impl;

import lk.epicgreen.erp.product.dto.request.ProductCategoryRequest;
import lk.epicgreen.erp.product.dto.response.ProductCategoryResponse;
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

import java.util.List;
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
}
