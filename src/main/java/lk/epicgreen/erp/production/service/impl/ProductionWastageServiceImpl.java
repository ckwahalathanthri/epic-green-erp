package lk.epicgreen.erp.production.service.impl;

import lk.epicgreen.erp.production.dto.request.ProductionWastageRequest;
import lk.epicgreen.erp.production.dto.response.ProductionWastageResponse;
import lk.epicgreen.erp.production.entity.ProductionWastage;
import lk.epicgreen.erp.production.entity.WorkOrder;
import lk.epicgreen.erp.production.mapper.ProductionWastageMapper;
import lk.epicgreen.erp.production.repository.ProductionWastageRepository;
import lk.epicgreen.erp.production.repository.WorkOrderRepository;
import lk.epicgreen.erp.production.service.ProductionWastageService;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.repository.ProductRepository;
import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.admin.repository.UnitOfMeasureRepository;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ProductionWastageService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductionWastageServiceImpl implements ProductionWastageService {

    private final ProductionWastageRepository productionWastageRepository;
    private final WorkOrderRepository workOrderRepository;
    private final ProductRepository productRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final ProductionWastageMapper productionWastageMapper;

    @Override
    @Transactional
    public ProductionWastageResponse recordWastage(ProductionWastageRequest request) {
        log.info("Recording production wastage for work order: {}, type: {}", 
            request.getWoId(), request.getWastageType());

        // Verify work order exists
        WorkOrder workOrder = findWorkOrderById(request.getWoId());

        // Verify product exists
        Product product = findProductById(request.getProductId());

        // Verify UOM exists
        UnitOfMeasure uom = findUnitOfMeasureById(request.getUomId());

        // Create production wastage entity
        ProductionWastage wastage = productionWastageMapper.toEntity(request);
        wastage.setWorkOrder(workOrder);
        wastage.setProduct(product);
        wastage.setUom(uom);

        ProductionWastage savedWastage = productionWastageRepository.save(wastage);
        log.info("Production wastage recorded successfully: {}", savedWastage.getId());

        return productionWastageMapper.toResponse(savedWastage);
    }

    @Override
    public ProductionWastageResponse getProductionWastageById(Long id) {
        ProductionWastage wastage = findProductionWastageById(id);
        return productionWastageMapper.toResponse(wastage);
    }

    @Override
    public PageResponse<ProductionWastageResponse> getAllProductionWastage(Pageable pageable) {
        Page<ProductionWastage> wastagePage = productionWastageRepository.findAll(pageable);
        return createPageResponse(wastagePage);
    }

    @Override
    public List<ProductionWastageResponse> getWastageByWorkOrder(Long woId) {
        List<ProductionWastage> wastages = productionWastageRepository.findByWoId(woId);
        return wastages.stream()
            .map(productionWastageMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductionWastageResponse> getWastageByProduct(Long productId) {
        List<ProductionWastage> wastages = productionWastageRepository.findByProductId(productId);
        return wastages.stream()
            .map(productionWastageMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<ProductionWastageResponse> getWastageByType(String wastageType, Pageable pageable) {
        Page<ProductionWastage> wastagePage = productionWastageRepository
            .findByWastageType(wastageType, pageable);
        return createPageResponse(wastagePage);
    }

    @Override
    public List<ProductionWastageResponse> getWastageByDateRange(LocalDate startDate, LocalDate endDate) {
        List<ProductionWastage> wastages = productionWastageRepository
            .findByWastageDateBetween(startDate, endDate);
        return wastages.stream()
            .map(productionWastageMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalWastageQuantityByWorkOrder(Long woId) {
        return productionWastageRepository
            .sumQuantityByWo(woId)
            .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getTotalWastageValueByWorkOrder(Long woId) {
        return productionWastageRepository
            .sumTotalValueByWo(woId)
            .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getWastageQuantityByWoAndType(Long woId, String wastageType) {
        return productionWastageRepository
            .sumQuantityByWoAndType(woId, wastageType)
            .orElse(BigDecimal.ZERO);
    }

    @Override
    public PageResponse<ProductionWastageResponse> searchProductionWastage(String keyword, Pageable pageable) {
        Page<ProductionWastage> wastagePage = productionWastageRepository
            .searchWastages(keyword, pageable);
        return createPageResponse(wastagePage);
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private ProductionWastage findProductionWastageById(Long id) {
        return productionWastageRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Production wastage not found: " + id));
    }

    private WorkOrder findWorkOrderById(Long id) {
        return workOrderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Work Order not found: " + id));
    }

    private Product findProductById(Long id) {
        return productRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    private UnitOfMeasure findUnitOfMeasureById(Long id) {
        return unitOfMeasureRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Unit of Measure not found: " + id));
    }

    private PageResponse<ProductionWastageResponse> createPageResponse(Page<ProductionWastage> wastagePage) {
        List<ProductionWastageResponse> content = wastagePage.getContent().stream()
            .map(productionWastageMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<ProductionWastageResponse>builder()
            .content(content)
            .pageNumber(wastagePage.getNumber())
            .pageSize(wastagePage.getSize())
            .totalElements(wastagePage.getTotalElements())
            .totalPages(wastagePage.getTotalPages())
            .last(wastagePage.isLast())
            .first(wastagePage.isFirst())
            .empty(wastagePage.isEmpty())
            .build();
    }
}
