package lk.epicgreen.erp.warehouse.service.impl;

import lk.epicgreen.erp.warehouse.dto.request.StockAdjustmentRequest;
import lk.epicgreen.erp.warehouse.dto.request.StockAdjustmentItemRequest;
import lk.epicgreen.erp.warehouse.dto.request.StockMovementRequest;
import lk.epicgreen.erp.warehouse.dto.response.StockAdjustmentResponse;
import lk.epicgreen.erp.warehouse.entity.StockAdjustment;
import lk.epicgreen.erp.warehouse.entity.StockAdjustmentItem;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lk.epicgreen.erp.warehouse.entity.WarehouseLocation;
import lk.epicgreen.erp.warehouse.mapper.StockAdjustmentMapper;
import lk.epicgreen.erp.warehouse.mapper.StockAdjustmentItemMapper;
import lk.epicgreen.erp.warehouse.repository.StockAdjustmentRepository;
import lk.epicgreen.erp.warehouse.repository.WarehouseRepository;
import lk.epicgreen.erp.warehouse.repository.WarehouseLocationRepository;
import lk.epicgreen.erp.warehouse.service.StockAdjustmentService;
import lk.epicgreen.erp.warehouse.service.StockMovementService;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.repository.ProductRepository;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.admin.repository.UserRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of StockAdjustmentService interface
 * 
 * Stock Adjustment Workflow:
 * 1. DRAFT - Initial creation, can be edited/deleted
 * 2. PENDING_APPROVAL - Submitted, cannot be edited/deleted
 * 3. APPROVED - Approved, stock movements created, cannot be edited/deleted
 * 4. REJECTED - Rejected, cannot be edited/deleted
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StockAdjustmentServiceImpl implements StockAdjustmentService {

    private final StockAdjustmentRepository stockAdjustmentRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final WarehouseLocationRepository warehouseLocationRepository;
    private final UserRepository userRepository;
    private final StockMovementService stockMovementService;
    private final StockAdjustmentMapper stockAdjustmentMapper;
    private final StockAdjustmentItemMapper stockAdjustmentItemMapper;

    @Override
    @Transactional
    public StockAdjustmentResponse createStockAdjustment(StockAdjustmentRequest request) {
        log.info("Creating new stock adjustment: {} for warehouse: {}", 
            request.getAdjustmentNumber(), request.getWarehouseId());

        // Validate unique constraint
        validateUniqueAdjustmentNumber(request.getAdjustmentNumber(), null);

        // Verify warehouse exists
        Warehouse warehouse = findWarehouseById(request.getWarehouseId());

        // Create adjustment entity
        StockAdjustment adjustment = stockAdjustmentMapper.toEntity(request);
        adjustment.setWarehouse(warehouse);
        adjustment.setStatus("DRAFT");

        // Create adjustment items
        List<StockAdjustmentItem> items = new ArrayList<>();
        for (StockAdjustmentItemRequest itemRequest : request.getItems()) {
            StockAdjustmentItem item = createAdjustmentItem(itemRequest);
            item.setAdjustment(adjustment);
            items.add(item);
        }
        adjustment.setItems(items);

        StockAdjustment savedAdjustment = stockAdjustmentRepository.save(adjustment);
        log.info("Stock adjustment created successfully: {}", savedAdjustment.getId());

        return stockAdjustmentMapper.toResponse(savedAdjustment);
    }

    @Override
    @Transactional
    public StockAdjustmentResponse updateStockAdjustment(Long id, StockAdjustmentRequest request) {
        log.info("Updating stock adjustment: {}", id);

        StockAdjustment adjustment = findStockAdjustmentById(id);

        // Can only update DRAFT adjustments
        if (!"DRAFT".equals(adjustment.getStatus())) {
            throw new InvalidOperationException(
                "Cannot update stock adjustment. Current status: " + adjustment.getStatus() + 
                ". Only DRAFT adjustments can be updated.");
        }

        // Validate unique constraint
        validateUniqueAdjustmentNumber(request.getAdjustmentNumber(), id);

        // Update fields
        stockAdjustmentMapper.updateEntityFromRequest(request, adjustment);

        // Update warehouse if changed
        if (!adjustment.getWarehouse().getId().equals(request.getWarehouseId())) {
            Warehouse warehouse = findWarehouseById(request.getWarehouseId());
            adjustment.setWarehouse(warehouse);
        }

        // Clear existing items and create new ones
        adjustment.getItems().clear();
        for (StockAdjustmentItemRequest itemRequest : request.getItems()) {
            StockAdjustmentItem item = createAdjustmentItem(itemRequest);
            item.setAdjustment(adjustment);
            adjustment.getItems().add(item);
        }

        StockAdjustment updatedAdjustment = stockAdjustmentRepository.save(adjustment);
        log.info("Stock adjustment updated successfully: {}", updatedAdjustment.getId());

        return stockAdjustmentMapper.toResponse(updatedAdjustment);
    }

    @Override
    @Transactional
    public void submitForApproval(Long id) {
        log.info("Submitting stock adjustment for approval: {}", id);

        StockAdjustment adjustment = findStockAdjustmentById(id);

        // Can only submit DRAFT adjustments
        if (!"DRAFT".equals(adjustment.getStatus())) {
            throw new InvalidOperationException(
                "Cannot submit stock adjustment. Current status: " + adjustment.getStatus() + 
                ". Only DRAFT adjustments can be submitted.");
        }

        adjustment.setStatus("PENDING_APPROVAL");
        stockAdjustmentRepository.save(adjustment);

        log.info("Stock adjustment submitted for approval successfully: {}", id);
    }

    @Override
    @Transactional
    public void approve(Long id, Long approvedBy) {
        log.info("Approving stock adjustment: {} by user: {}", id, approvedBy);

        StockAdjustment adjustment = findStockAdjustmentById(id);

        // Can only approve PENDING_APPROVAL adjustments
        if (!"PENDING_APPROVAL".equals(adjustment.getStatus())) {
            throw new InvalidOperationException(
                "Cannot approve stock adjustment. Current status: " + adjustment.getStatus() + 
                ". Only PENDING_APPROVAL adjustments can be approved.");
        }

        User approver = findUserById(approvedBy);

        // Update status
        adjustment.setStatus("APPROVED");
        adjustment.setApprovedBy(approver);
        adjustment.setApprovedAt(LocalDateTime.now());

        stockAdjustmentRepository.save(adjustment);

        // Create stock movements for each adjustment item
        createStockMovementsForAdjustment(adjustment);

        log.info("Stock adjustment approved successfully: {}", id);
    }

    @Override
    @Transactional
    public void reject(Long id, Long rejectedBy, String reason) {
        log.info("Rejecting stock adjustment: {} by user: {}", id, rejectedBy);

        StockAdjustment adjustment = findStockAdjustmentById(id);

        // Can only reject PENDING_APPROVAL adjustments
        if (!"PENDING_APPROVAL".equals(adjustment.getStatus())) {
            throw new InvalidOperationException(
                "Cannot reject stock adjustment. Current status: " + adjustment.getStatus() + 
                ". Only PENDING_APPROVAL adjustments can be rejected.");
        }

        // Update status
        adjustment.setStatus("REJECTED");
        adjustment.setRemarks(adjustment.getRemarks() != null ? 
            adjustment.getRemarks() + "\nRejection reason: " + reason : 
            "Rejection reason: " + reason);

        stockAdjustmentRepository.save(adjustment);

        log.info("Stock adjustment rejected successfully: {}", id);
    }

    @Override
    @Transactional
    public void deleteStockAdjustment(Long id) {
        log.info("Deleting stock adjustment: {}", id);

        StockAdjustment adjustment = findStockAdjustmentById(id);

        // Can only delete DRAFT adjustments
        if (!"DRAFT".equals(adjustment.getStatus())) {
            throw new InvalidOperationException(
                "Cannot delete stock adjustment. Current status: " + adjustment.getStatus() + 
                ". Only DRAFT adjustments can be deleted.");
        }

        stockAdjustmentRepository.delete(adjustment);

        log.info("Stock adjustment deleted successfully: {}", id);
    }

    @Override
    public StockAdjustmentResponse getStockAdjustmentById(Long id) {
        StockAdjustment adjustment = findStockAdjustmentById(id);
        return stockAdjustmentMapper.toResponse(adjustment);
    }

    @Override
    public PageResponse<StockAdjustmentResponse> getAllStockAdjustments(Pageable pageable) {
        Page<StockAdjustment> adjustmentPage = stockAdjustmentRepository.findAll(pageable);
        return createPageResponse(adjustmentPage);
    }

    @Override
    public List<StockAdjustmentResponse> getAdjustmentsByWarehouse(Long warehouseId) {
        List<StockAdjustment> adjustments = stockAdjustmentRepository.findByWarehouseId(warehouseId);
        return adjustments.stream()
            .map(stockAdjustmentMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<StockAdjustmentResponse> getAdjustmentsByType(String adjustmentType, Pageable pageable) {
        Page<StockAdjustment> adjustmentPage = stockAdjustmentRepository
            .findByAdjustmentType(adjustmentType, pageable);
        return createPageResponse(adjustmentPage);
    }

    @Override
    public List<StockAdjustmentResponse> getAdjustmentsByStatus(String status) {
        List<StockAdjustment> adjustments = stockAdjustmentRepository.findByStatus(status);
        return adjustments.stream()
            .map(stockAdjustmentMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<StockAdjustmentResponse> getPendingApprovals() {
        List<StockAdjustment> adjustments = stockAdjustmentRepository.findByStatus("PENDING_APPROVAL");
        return adjustments.stream()
            .map(stockAdjustmentMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<StockAdjustmentResponse> getApprovedAdjustments(Long warehouseId) {
        List<StockAdjustment> adjustments = stockAdjustmentRepository
            .findByWarehouseIdAndStatus(warehouseId, "APPROVED");
        return adjustments.stream()
            .map(stockAdjustmentMapper::toResponse)
            .collect(Collectors.toList());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private StockAdjustment findStockAdjustmentById(Long id) {
        return stockAdjustmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Stock adjustment not found: " + id));
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

    private User findUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    private void validateUniqueAdjustmentNumber(String adjustmentNumber, Long excludeId) {
        if (excludeId == null) {
            if (stockAdjustmentRepository.existsByAdjustmentNumber(adjustmentNumber)) {
                throw new DuplicateResourceException("Adjustment number already exists: " + adjustmentNumber);
            }
        } else {
            if (stockAdjustmentRepository.existsByAdjustmentNumberAndIdNot(adjustmentNumber, excludeId)) {
                throw new DuplicateResourceException("Adjustment number already exists: " + adjustmentNumber);
            }
        }
    }

    private StockAdjustmentItem createAdjustmentItem(StockAdjustmentItemRequest request) {
        Product product = findProductById(request.getProductId());
        
        WarehouseLocation location = null;
        if (request.getLocationId() != null) {
            location = findWarehouseLocationById(request.getLocationId());
        }

        StockAdjustmentItem item = stockAdjustmentItemMapper.toEntity(request);
        item.setProduct(product);
        item.setLocation(location);

        return item;
    }

    private void createStockMovementsForAdjustment(StockAdjustment adjustment) {
        for (StockAdjustmentItem item : adjustment.getItems()) {
            StockMovementRequest movementRequest = StockMovementRequest.builder()
                .movementDate(adjustment.getAdjustmentDate())
                .movementType("ADJUSTMENT")
                .warehouseId(adjustment.getWarehouse().getId())
                .productId(item.getProduct().getId())
                .toLocationId(item.getLocation() != null ? item.getLocation().getId() : null)
                .batchNumber(item.getBatchNumber())
                .quantity(item.getQuantityAdjusted().abs())
                .uomId(item.getProduct().getBaseUom().getId())
                .unitCost(item.getUnitCost())
                .referenceType("STOCK_ADJUSTMENT")
                .referenceId(adjustment.getId())
                .referenceNumber(adjustment.getAdjustmentNumber())
                .remarks(adjustment.getRemarks())
                .build();

            stockMovementService.createStockMovement(movementRequest);
        }
    }

    private PageResponse<StockAdjustmentResponse> createPageResponse(Page<StockAdjustment> adjustmentPage) {
        List<StockAdjustmentResponse> content = adjustmentPage.getContent().stream()
            .map(stockAdjustmentMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<StockAdjustmentResponse>builder()
            .content(content)
            .pageNumber(adjustmentPage.getNumber())
            .pageSize(adjustmentPage.getSize())
            .totalElements(adjustmentPage.getTotalElements())
            .totalPages(adjustmentPage.getTotalPages())
            .last(adjustmentPage.isLast())
            .first(adjustmentPage.isFirst())
            .empty(adjustmentPage.isEmpty())
            .build();
    }
}
