package lk.epicgreen.erp.warehouse.service.impl;

import lk.epicgreen.erp.warehouse.dto.request.StockMovementRequest;
import lk.epicgreen.erp.warehouse.dto.response.StockMovementResponse;
import lk.epicgreen.erp.warehouse.entity.StockMovement;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lk.epicgreen.erp.warehouse.entity.WarehouseLocation;
import lk.epicgreen.erp.warehouse.mapper.StockMovementMapper;
import lk.epicgreen.erp.warehouse.repository.StockMovementRepository;
import lk.epicgreen.erp.warehouse.repository.WarehouseRepository;
import lk.epicgreen.erp.warehouse.repository.WarehouseLocationRepository;
import lk.epicgreen.erp.warehouse.service.StockMovementService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of StockMovementService interface
 * 
 * CRITICAL: StockMovement is IMMUTABLE
 * - CREATE: Allowed (add new movements)
 * - READ: Allowed (query movements)
 * - UPDATE: NOT IMPLEMENTED (audit trail requirement)
 * - DELETE: NOT IMPLEMENTED (audit trail requirement)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StockMovementServiceImpl implements StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final WarehouseLocationRepository warehouseLocationRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final StockMovementMapper stockMovementMapper;

    @Override
    @Transactional
    public StockMovementResponse createStockMovement(StockMovementRequest request) {
        log.info("Creating new stock movement: {} for warehouse: {}, product: {}", 
            request.getMovementType(), request.getWarehouseId(), request.getProductId());

        // Verify warehouse exists
        Warehouse warehouse = findWarehouseById(request.getWarehouseId());

        // Verify product exists
        Product product = findProductById(request.getProductId());

        // Verify UOM exists
        UnitOfMeasure uom = findUnitOfMeasureById(request.getUomId());

        // Verify locations if provided
        WarehouseLocation fromLocation = null;
        if (request.getFromLocationId() != null) {
            fromLocation = findWarehouseLocationById(request.getFromLocationId());
        }

        WarehouseLocation toLocation = null;
        if (request.getToLocationId() != null) {
            toLocation = findWarehouseLocationById(request.getToLocationId());
        }

        // Create stock movement entity
        StockMovement movement = stockMovementMapper.toEntity(request);
        movement.setWarehouse(warehouse);
        movement.setProduct(product);
        movement.setUom(uom);
        movement.setFromLocation(fromLocation);
        movement.setToLocation(toLocation);

        StockMovement savedMovement = stockMovementRepository.save(movement);
        log.info("Stock movement created successfully: {}", savedMovement.getId());

        return stockMovementMapper.toResponse(savedMovement);
    }

    @Override
    public StockMovementResponse getStockMovementById(Long id) {
        StockMovement movement = findStockMovementById(id);
        return stockMovementMapper.toResponse(movement);
    }

    @Override
    public PageResponse<StockMovementResponse> getAllStockMovements(Pageable pageable) {
        Page<StockMovement> movementPage = stockMovementRepository.findAll(pageable);
        return createPageResponse(movementPage);
    }

    @Override
    public List<StockMovementResponse> getMovementsByWarehouse(Long warehouseId) {
        List<StockMovement> movements = stockMovementRepository.findByWarehouseId(warehouseId);
        return movements.stream()
            .map(stockMovementMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<StockMovementResponse> getMovementsByProduct(Long productId) {
        List<StockMovement> movements = stockMovementRepository.findByProductId(productId);
        return movements.stream()
            .map(stockMovementMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<StockMovementResponse> getMovementsByType(String movementType, Pageable pageable) {
        Page<StockMovement> movementPage = stockMovementRepository.findByMovementType(movementType, pageable);
        return createPageResponse(movementPage);
    }

    @Override
    public List<StockMovementResponse> getMovementsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<StockMovement> movements = stockMovementRepository
            .findByMovementDateBetween(startDate, endDate);
        return movements.stream()
            .map(stockMovementMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<StockMovementResponse> getMovementsByReference(String referenceType, Long referenceId) {
        List<StockMovement> movements = stockMovementRepository
            .findByReferenceTypeAndReferenceId(referenceType, referenceId);
        return movements.stream()
            .map(stockMovementMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<StockMovementResponse> getReceiptMovements(Long warehouseId) {
        List<StockMovement> movements = stockMovementRepository
            .findByWarehouseIdAndMovementType(warehouseId, "RECEIPT");
        return movements.stream()
            .map(stockMovementMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<StockMovementResponse> getIssueMovements(Long warehouseId) {
        List<StockMovement> movements = stockMovementRepository
            .findByWarehouseIdAndMovementType(warehouseId, "ISSUE");
        return movements.stream()
            .map(stockMovementMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<StockMovementResponse> getTransferMovements(Long warehouseId) {
        List<StockMovement> movements = stockMovementRepository
            .findByWarehouseIdAndMovementType(warehouseId, "TRANSFER");
        return movements.stream()
            .map(stockMovementMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<StockMovementResponse> getSalesMovements(Long warehouseId) {
        List<StockMovement> movements = stockMovementRepository
            .findByWarehouseIdAndMovementType(warehouseId, "SALES");
        return movements.stream()
            .map(stockMovementMapper::toResponse)
            .collect(Collectors.toList());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private StockMovement findStockMovementById(Long id) {
        return stockMovementRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Stock movement not found: " + id));
    }

    private Warehouse findWarehouseById(Long id) {
        return warehouseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found: " + id));
    }

    private Product findProductById(Long id) {
        return productRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    private WarehouseLocation findWarehouseLocationById(Long id) {
        return warehouseLocationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Warehouse location not found: " + id));
    }

    private UnitOfMeasure findUnitOfMeasureById(Long id) {
        return unitOfMeasureRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Unit of measure not found: " + id));
    }

    private PageResponse<StockMovementResponse> createPageResponse(Page<StockMovement> movementPage) {
        List<StockMovementResponse> content = movementPage.getContent().stream()
            .map(stockMovementMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<StockMovementResponse>builder()
            .content(content)
            .pageNumber(movementPage.getNumber())
            .pageSize(movementPage.getSize())
            .totalElements(movementPage.getTotalElements())
            .totalPages(movementPage.getTotalPages())
            .last(movementPage.isLast())
            .first(movementPage.isFirst())
            .empty(movementPage.isEmpty())
            .build();
    }

    @Override
    public StockMovementResponse updateStockMovement(Long id, StockMovementRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateStockMovement'");
    }

    @Override
    public void deleteStockMovement(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteStockMovement'");
    }

    @Override
    public StockMovementResponse getStockMovementByNumber(String movementNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStockMovementByNumber'");
    }

    @Override
    public List<StockMovementResponse> getAllStockMovements() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllStockMovements'");
    }

    @Override
    public PageResponse<StockMovementResponse> searchStockMovements(String keyword, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchStockMovements'");
    }

    @Override
    public StockMovementResponse approveStockMovement(Long id, Long approvedByUserId, String approvalNotes) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'approveStockMovement'");
    }

    @Override
    public StockMovementResponse completeStockMovement(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'completeStockMovement'");
    }

    @Override
    public StockMovementResponse cancelStockMovement(Long id, String cancellationReason) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cancelStockMovement'");
    }

    @Override
    public StockMovementResponse recordStockIn(StockMovementRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'recordStockIn'");
    }

    @Override
    public StockMovementResponse recordStockOut(StockMovementRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'recordStockOut'");
    }

    @Override
    public StockMovementResponse recordTransfer(StockMovementRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'recordTransfer'");
    }

    @Override
    public StockMovementResponse recordAdjustment(StockMovementRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'recordAdjustment'");
    }

    @Override
    public StockMovementResponse recordReturn(StockMovementRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'recordReturn'");
    }

    @Override
    public StockMovementResponse recordDamage(StockMovementRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'recordDamage'");
    }

    @Override
    public List<StockMovementResponse> getStockInMovements() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStockInMovements'");
    }

    @Override
    public List<StockMovementResponse> getStockOutMovements() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStockOutMovements'");
    }

    @Override
    public List<StockMovementResponse> getTransferMovements() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTransferMovements'");
    }

    @Override
    public List<StockMovementResponse> getAdjustmentMovements() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAdjustmentMovements'");
    }

    @Override
    public List<StockMovementResponse> getReturnMovements() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReturnMovements'");
    }

    @Override
    public List<StockMovementResponse> getDamageMovements() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDamageMovements'");
    }

    @Override
    public List<StockMovementResponse> getPendingMovements() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPendingMovements'");
    }

    @Override
    public List<StockMovementResponse> getApprovedMovements() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getApprovedMovements'");
    }

    @Override
    public List<StockMovementResponse> getCompletedMovements() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCompletedMovements'");
    }

    @Override
    public List<StockMovementResponse> getMovementsPendingApproval() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMovementsPendingApproval'");
    }

    @Override
    public List<StockMovementResponse> getTodaysMovements() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTodaysMovements'");
    }

    @Override
    public Double getTotalQuantityInByProduct(Long productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalQuantityInByProduct'");
    }

    @Override
    public Double getTotalQuantityOutByProduct(Long productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalQuantityOutByProduct'");
    }

    @Override
    public Double getTotalValueInByWarehouse(Long warehouseId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalValueInByWarehouse'");
    }

    @Override
    public Double getTotalValueOutByWarehouse(Long warehouseId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalValueOutByWarehouse'");
    }

    @Override
    public boolean canApproveStockMovement(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canApproveStockMovement'");
    }

    @Override
    public boolean canCancelStockMovement(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canCancelStockMovement'");
    }

    @Override
    public List<StockMovement> createBulkStockMovements(List<StockMovementRequest> requests) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createBulkStockMovements'");
    }

    @Override
    public int approveBulkStockMovements(List<Long> movementIds, Long approvedByUserId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'approveBulkStockMovements'");
    }

    @Override
    public int deleteBulkStockMovements(List<Long> movementIds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteBulkStockMovements'");
    }

    @Override
    public Map<String, Object> getStockMovementStatistics() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStockMovementStatistics'");
    }

    @Override
    public List<Map<String, Object>> getMovementTypeDistribution() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMovementTypeDistribution'");
    }

    @Override
    public List<Map<String, Object>> getStatusDistribution() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStatusDistribution'");
    }

    @Override
    public List<Map<String, Object>> getTransactionTypeDistribution() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTransactionTypeDistribution'");
    }

    @Override
    public List<Map<String, Object>> getMonthlyMovementCount(LocalDate startDate, LocalDate endDate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMonthlyMovementCount'");
    }

    @Override
    public List<Map<String, Object>> getMovementsByWarehouse() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMovementsByWarehouse'");
    }

    @Override
    public List<Map<String, Object>> getMovementsByProduct() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMovementsByProduct'");
    }

    @Override
    public Double getTotalQuantityMoved() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalQuantityMoved'");
    }

    @Override
    public Double getTotalValueMoved() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalValueMoved'");
    }

    @Override
    public Map<String, Object> getDashboardStatistics() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDashboardStatistics'");
    }

    @Override
    public List<StockMovementResponse> getTransfersBetweenWarehouses(Long fromWarehouseId, Long toWarehouseId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTransfersBetweenWarehouses'");
    }

    @Override
    public List<StockMovementResponse> getProductMovementHistory(Long productId, LocalDate startDate,
            LocalDate endDate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductMovementHistory'");
    }

    @Override
    public List<StockMovementResponse> getWarehouseMovementHistory(Long warehouseId, LocalDate startDate,
            LocalDate endDate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWarehouseMovementHistory'");
    }

    @Override
    public List<StockMovementResponse> getRecentMovements(int limit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRecentMovements'");
    }
}
