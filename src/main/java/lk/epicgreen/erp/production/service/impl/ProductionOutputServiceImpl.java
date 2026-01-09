package lk.epicgreen.erp.production.service.impl;

import lk.epicgreen.erp.production.dto.request.ProductionOutputRequest;
import lk.epicgreen.erp.production.dto.response.ProductionOutputResponse;
import lk.epicgreen.erp.production.dto.response.WorkOrderResponse; // Keep this if used for response mapping, but usually service uses entities
import lk.epicgreen.erp.production.entity.ProductionOutput;
import lk.epicgreen.erp.production.entity.WorkOrder;
import lk.epicgreen.erp.production.mapper.ProductionOutputMapper;
import lk.epicgreen.erp.production.repository.ProductionOutputRepository;
import lk.epicgreen.erp.production.repository.WorkOrderRepository;
import lk.epicgreen.erp.production.service.ProductionOutputService;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.repository.ProductRepository;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lk.epicgreen.erp.warehouse.entity.WarehouseLocation;
import lk.epicgreen.erp.warehouse.repository.WarehouseRepository;
import lk.epicgreen.erp.warehouse.repository.WarehouseLocationRepository;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of ProductionOutputService interface
 * 
 * Quality Status Workflow:
 * PENDING → PASSED/FAILED
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductionOutputServiceImpl implements ProductionOutputService {

    private final ProductionOutputRepository productionOutputRepository;
    private final WorkOrderRepository workOrderRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseLocationRepository warehouseLocationRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final ProductionOutputMapper productionOutputMapper;

    @Override
    @Transactional
    public ProductionOutputResponse recordOutput(ProductionOutputRequest request) {
        log.info("Recording production output for work order: {}", request.getWoId());

        // Verify work order exists
        WorkOrder workOrder = findWorkOrderById(request.getWoId());

        // Verify finished product exists
        Product finishedProduct = findProductById(request.getFinishedProductId());

        // Verify warehouse exists
        Warehouse warehouse = findWarehouseById(request.getWarehouseId());

        // Verify UOM exists
        UnitOfMeasure uom = findUnitOfMeasureById(request.getUomId());

        // Create production output entity
        ProductionOutput output = productionOutputMapper.toEntity(request);
        output.setWorkOrder(workOrder);
        output.setFinishedProduct(finishedProduct);
        output.setWarehouse(warehouse);
        output.setUom(uom);

        // Set location if provided
        if (request.getLocationId() != null) {
            WarehouseLocation location = findWarehouseLocationById(request.getLocationId());
            output.setLocation(location);
        }

        // Update work order actual quantity
        BigDecimal currentActual = workOrder.getActualQuantity() != null ? 
            workOrder.getActualQuantity() : BigDecimal.ZERO;
        workOrder.setActualQuantity(currentActual.add(request.getQuantityAccepted()));
        workOrderRepository.save(workOrder);

        ProductionOutput savedOutput = productionOutputRepository.save(output);
        log.info("Production output recorded successfully: {}", savedOutput.getId());

        return productionOutputMapper.toResponse(savedOutput);
    }

    @Override
    @Transactional
    public ProductionOutputResponse updateOutput(Long id, ProductionOutputRequest request) {
        log.info("Updating production output: {}", id);

        ProductionOutput output = findProductionOutputById(id);

        // Store old accepted quantity
        BigDecimal oldAcceptedQuantity = output.getQuantityAccepted();

        // Update fields
        productionOutputMapper.updateEntityFromRequest(request, output);

        // Update relationships if changed
        if (!output.getFinishedProduct().getId().equals(request.getFinishedProductId())) {
            Product finishedProduct = findProductById(request.getFinishedProductId());
            output.setFinishedProduct(finishedProduct);
        }

        if (!output.getWarehouse().getId().equals(request.getWarehouseId())) {
            Warehouse warehouse = findWarehouseById(request.getWarehouseId());
            output.setWarehouse(warehouse);
        }

        if (!output.getUom().getId().equals(request.getUomId())) {
            UnitOfMeasure uom = findUnitOfMeasureById(request.getUomId());
            output.setUom(uom);
        }

        // Update location
        if (request.getLocationId() != null) {
            WarehouseLocation location = findWarehouseLocationById(request.getLocationId());
            output.setLocation(location);
        } else {
            output.setLocation(null);
        }

        // Update work order actual quantity if accepted quantity changed
        if (request.getQuantityAccepted().compareTo(oldAcceptedQuantity) != 0) {
            WorkOrder workOrder = output.getWorkOrder();
            BigDecimal difference = request.getQuantityAccepted().subtract(oldAcceptedQuantity);
            BigDecimal currentActual = workOrder.getActualQuantity() != null ? 
                workOrder.getActualQuantity() : BigDecimal.ZERO;
            workOrder.setActualQuantity(currentActual.add(difference));
            workOrderRepository.save(workOrder);
        }

        ProductionOutput updatedOutput = productionOutputRepository.save(output);
        log.info("Production output updated successfully: {}", updatedOutput.getId());

        return productionOutputMapper.toResponse(updatedOutput);
    }

    @Override
    @Transactional
    public void passQualityCheck(Long id, Long qualityCheckedBy) {
        log.info("Passing quality check for production output: {} by user: {}", id, qualityCheckedBy);

        ProductionOutput output = findProductionOutputById(id);

        output.setQualityStatus("PASSED");
        output.setQualityCheckedBy(qualityCheckedBy);
        output.setQualityCheckedAt(LocalDateTime.now());

        productionOutputRepository.save(output);

        log.info("Quality check passed successfully: {}", id);
    }

    @Override
    @Transactional
    public void failQualityCheck(Long id, Long qualityCheckedBy, String remarks) {
        log.info("Failing quality check for production output: {} by user: {}", id, qualityCheckedBy);

        ProductionOutput output = findProductionOutputById(id);

        output.setQualityStatus("FAILED");
        output.setQualityCheckedBy(qualityCheckedBy);
        output.setQualityCheckedAt(LocalDateTime.now());
        output.setRemarks(output.getRemarks() != null ? 
            output.getRemarks() + "\nQuality Check Failed: " + remarks : 
            "Quality Check Failed: " + remarks);

        productionOutputRepository.save(output);

        log.info("Quality check failed: {}", id);
    }

    @Override
    @Transactional
    public void deleteOutput(Long id) {
        log.info("Deleting production output: {}", id);

        ProductionOutput output = findProductionOutputById(id);

        // Subtract from work order actual quantity
        WorkOrder workOrder = output.getWorkOrder();
        BigDecimal currentActual = workOrder.getActualQuantity() != null ? 
            workOrder.getActualQuantity() : BigDecimal.ZERO;
        workOrder.setActualQuantity(currentActual.subtract(output.getQuantityAccepted()));
        workOrderRepository.save(workOrder);

        productionOutputRepository.delete(output);

        log.info("Production output deleted successfully: {}", id);
    }

    @Override
    public ProductionOutputResponse getProductionOutputById(Long id) {
        ProductionOutput output = findProductionOutputById(id);
        return productionOutputMapper.toResponse(output);
    }

    @Override
    public PageResponse<ProductionOutputResponse> getAllProductionOutput(Pageable pageable) {
        Page<ProductionOutput> outputPage = productionOutputRepository.findAll(pageable);
        return createPageResponse(outputPage);
    }

    @Override
    public List<ProductionOutputResponse> getOutputByWorkOrder(Long woId) {
        List<ProductionOutput> outputs = productionOutputRepository.findByWoId(woId);
        return outputs.stream()
            .map(productionOutputMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOutputResponse> getOutputByFinishedProduct(Long finishedProductId) {
        List<ProductionOutput> outputs = productionOutputRepository
            .findByFinishedProductId(finishedProductId);
        return outputs.stream()
            .map(productionOutputMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOutputResponse> getOutputByWarehouse(Long warehouseId) {
        List<ProductionOutput> outputs = productionOutputRepository.findByWarehouseId(warehouseId);
        return outputs.stream()
            .map(productionOutputMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOutputResponse> getOutputByBatchNumber(String batchNumber) {
        List<ProductionOutput> outputs = productionOutputRepository.findByBatchNumber(batchNumber);
        return outputs.stream()
            .map(productionOutputMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<ProductionOutputResponse> getOutputByQualityStatus(String qualityStatus, Pageable pageable) {
        Page<ProductionOutput> outputPage = productionOutputRepository
            .findByQualityStatus(qualityStatus, pageable);
        return createPageResponse(outputPage);
    }

    @Override
    public List<ProductionOutputResponse> getOutputByDateRange(LocalDate startDate, LocalDate endDate) {
        List<ProductionOutput> outputs = productionOutputRepository
            .findByOutputDateBetween(startDate, endDate);
        return outputs.stream()
            .map(productionOutputMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOutputResponse> getExpiredOutput() {
        List<ProductionOutput> outputs = productionOutputRepository.findExpiredOutput(LocalDate.now());
        return outputs.stream()
            .map(productionOutputMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOutputResponse> getOutputExpiringSoon(Integer daysBeforeExpiry) {
        LocalDate expiryDate = LocalDate.now().plusDays(daysBeforeExpiry);
        List<ProductionOutput> outputs = productionOutputRepository
            .findOutputExpiringSoon(LocalDate.now(), expiryDate);
        return outputs.stream()
            .map(productionOutputMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOutputResponse> getPendingQualityCheck() {
        List<ProductionOutput> outputs = productionOutputRepository.findByQualityStatus("PENDING");
        return outputs.stream()
            .map(productionOutputMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalOutputByWorkOrder(Long woId) {
        return productionOutputRepository
            .sumQuantityProducedByWo(woId)
            .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getAcceptedOutputByWorkOrder(Long woId) {
        return productionOutputRepository
            .sumQuantityAcceptedByWo(woId)
            .orElse(BigDecimal.ZERO);
    }

    @Override
    public Double calculateQualityPassRate(Long woId) {
        BigDecimal totalProduced = getTotalOutputByWorkOrder(woId);
        BigDecimal totalAccepted = getAcceptedOutputByWorkOrder(woId);

        if (totalProduced.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }

        return (totalAccepted.doubleValue() / totalProduced.doubleValue()) * 100.0;
    }

    @Override
    public PageResponse<ProductionOutputResponse> searchProductionOutput(String keyword, Pageable pageable) {
        Page<ProductionOutput> outputPage = productionOutputRepository
            .searchOutputs(keyword, pageable);
        return createPageResponse(outputPage);
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private ProductionOutput findProductionOutputById(Long id) {
        return productionOutputRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Production output not found: " + id));
    }

    private WorkOrder findWorkOrderById(Long id) {
        return workOrderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Work Order not found: " + id));
    }

    private Product findProductById(Long id) {
        return productRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    private Warehouse findWarehouseById(Long id) {
        return warehouseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found: " + id));
    }

    private WarehouseLocation findWarehouseLocationById(Long id) {
        return warehouseLocationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Warehouse location not found: " + id));
    }

    private UnitOfMeasure findUnitOfMeasureById(Long id) {
        return unitOfMeasureRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Unit of Measure not found: " + id));
    }

    private PageResponse<ProductionOutputResponse> createPageResponse(Page<ProductionOutput> outputPage) {
        List<ProductionOutputResponse> content = outputPage.getContent().stream()
            .map(productionOutputMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<ProductionOutputResponse>builder()
            .content(content)
            .pageNumber(outputPage.getNumber())
            .pageSize(outputPage.getSize())
            .totalElements(outputPage.getTotalElements())
            .totalPages(outputPage.getTotalPages())
            .last(outputPage.isLast())
            .first(outputPage.isFirst())
            .empty(outputPage.isEmpty())
            .build();
    }

    @Override
    public ProductionOutputResponse createProductionOutput(ProductionOutputRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createProductionOutput'");
    }

    @Override
    public ProductionOutputResponse updateProductionOutput(Long id, ProductionOutputRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProductionOutput'");
    }

    @Override
    public void deleteProductionOutput(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteProductionOutput'");
    }

    @Override
    public ProductionOutputResponse getProductionOutputByNumber(String outputNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductionOutputByNumber'");
    }

    @Override
    public Page<ProductionOutputResponse> getAllProductionOutputs(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllProductionOutputs'");
    }

    @Override
    public List<ProductionOutputResponse> getAllProductionOutputs() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllProductionOutputs'");
    }

    @Override
    public Page<ProductionOutputResponse> searchProductionOutputs(String keyword, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchProductionOutputs'");
    }

    @Override
    public ProductionOutputResponse verifyProductionOutput(Long id, Long verifiedByUserId, String verificationNotes) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyProductionOutput'");
    }

    @Override
    public ProductionOutputResponse postToInventory(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'postToInventory'");
    }

    @Override
    public ProductionOutputResponse rejectProductionOutput(Long id, String rejectionReason) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rejectProductionOutput'");
    }

    @Override
    public void recordQualityCheck(Long id, String qualityStatus) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'recordQualityCheck'");
    }

    @Override
    public void updateOutputQuantities(Long id, Double goodQuantity, Double rejectedQuantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateOutputQuantities'");
    }

    @Override
    public List<ProductionOutputResponse> getPendingProductionOutputs() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPendingProductionOutputs'");
    }

    @Override
    public List<ProductionOutputResponse> getVerifiedProductionOutputs() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVerifiedProductionOutputs'");
    }

    @Override
    public List<ProductionOutputResponse> getPostedProductionOutputs() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPostedProductionOutputs'");
    }

    @Override
    public List<ProductionOutputResponse> getRejectedProductionOutputs() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRejectedProductionOutputs'");
    }

    @Override
    public List<ProductionOutputResponse> getUnverifiedProductionOutputs() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUnverifiedProductionOutputs'");
    }

    @Override
    public List<ProductionOutputResponse> getUnpostedProductionOutputs() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUnpostedProductionOutputs'");
    }

    @Override
    public List<ProductionOutputResponse> getTodaysProductionOutputs() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTodaysProductionOutputs'");
    }

    @Override
    public List<ProductionOutputResponse> getProductionOutputsByWorkOrder(Long workOrderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductionOutputsByWorkOrder'");
    }

    @Override
    public List<ProductionOutputResponse> getProductionOutputsByProduct(Long productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductionOutputsByProduct'");
    }

    @Override
    public List<ProductionOutputResponse> getProductionOutputsByProductionLine(Long productionLineId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductionOutputsByProductionLine'");
    }

    @Override
    public List<ProductionOutputResponse> getProductionOutputsBySupervisor(Long supervisorId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductionOutputsBySupervisor'");
    }

    @Override
    public List<ProductionOutputResponse> getProductionOutputsByDateRange(LocalDate startDate, LocalDate endDate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductionOutputsByDateRange'");
    }

    @Override
    public List<ProductionOutputResponse> getRecentProductionOutputs(int limit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRecentProductionOutputs'");
    }

    @Override
    public Double getTotalGoodQuantityByWorkOrder(Long workOrderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalGoodQuantityByWorkOrder'");
    }

    @Override
    public Double getTotalRejectedQuantityByWorkOrder(Long workOrderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalRejectedQuantityByWorkOrder'");
    }

    @Override
    public boolean canVerifyProductionOutput(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canVerifyProductionOutput'");
    }

    @Override
    public boolean canPostToInventory(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canPostToInventory'");
    }

    @Override
    public boolean canRejectProductionOutput(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canRejectProductionOutput'");
    }

    @Override
    public Double calculateQualityRate(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calculateQualityRate'");
    }

    @Override
    public Double calculateRejectionRate(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calculateRejectionRate'");
    }

    @Override
    public Map<String, Object> calculateProductionMetrics(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calculateProductionMetrics'");
    }

    @Override
    public List<ProductionOutputResponse> createBulkProductionOutputs(List<ProductionOutputRequest> requests) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createBulkProductionOutputs'");
    }

    @Override
    public int verifyBulkProductionOutputs(List<Long> outputIds, Long verifiedByUserId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyBulkProductionOutputs'");
    }

    @Override
    public int postBulkToInventory(List<Long> outputIds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'postBulkToInventory'");
    }

    @Override
    public int deleteBulkProductionOutputs(List<Long> outputIds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteBulkProductionOutputs'");
    }

    @Override
    public Map<String, Object> getProductionOutputStatistics() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductionOutputStatistics'");
    }

    @Override
    public List<Map<String, Object>> getOutputTypeDistribution() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOutputTypeDistribution'");
    }

    @Override
    public List<Map<String, Object>> getStatusDistribution() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStatusDistribution'");
    }

    @Override
    public List<Map<String, Object>> getMonthlyProductionOutput(LocalDate startDate, LocalDate endDate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMonthlyProductionOutput'");
    }

    @Override
    public List<Map<String, Object>> getProductionByProduct() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductionByProduct'");
    }

    @Override
    public List<Map<String, Object>> getProductionByProductionLine() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductionByProductionLine'");
    }

    @Override
    public Double getQualityRate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getQualityRate'");
    }

    @Override
    public Double getRejectionRate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRejectionRate'");
    }

    @Override
    public Map<String, Object> getDashboardStatistics() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDashboardStatistics'");
    }
}
