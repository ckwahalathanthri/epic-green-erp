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

import java.time.LocalDateTime;
import java.util.List;
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
}
