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
}
