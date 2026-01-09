package lk.epicgreen.erp.warehouse.service.impl;

import lk.epicgreen.erp.warehouse.dto.request.InventoryRequest;
import lk.epicgreen.erp.warehouse.dto.response.InventoryResponse;
import lk.epicgreen.erp.warehouse.entity.Inventory;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lk.epicgreen.erp.warehouse.entity.WarehouseLocation;
import lk.epicgreen.erp.warehouse.mapper.InventoryMapper;
import lk.epicgreen.erp.warehouse.repository.InventoryRepository;
import lk.epicgreen.erp.warehouse.repository.WarehouseRepository;
import lk.epicgreen.erp.warehouse.repository.WarehouseLocationRepository;
import lk.epicgreen.erp.warehouse.service.InventoryService;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.repository.ProductRepository;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of InventoryService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final WarehouseLocationRepository warehouseLocationRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    @Transactional
    public InventoryResponse createInventory(InventoryRequest request) {
        log.info("Creating new inventory for warehouse: {}, product: {}", 
            request.getWarehouseId(), request.getProductId());

        // Verify warehouse exists
        Warehouse warehouse = findWarehouseById(request.getWarehouseId());

        // Verify product exists
        Product product = findProductById(request.getProductId());

        // Verify location if provided
        WarehouseLocation location = null;
        if (request.getLocationId() != null) {
            location = findWarehouseLocationById(request.getLocationId());
        }

        // Check for duplicate
        validateUniqueInventory(request.getWarehouseId(), request.getProductId(), 
            request.getBatchNumber(), request.getLocationId(), null);

        // Create inventory entity
        Inventory inventory = inventoryMapper.toEntity(request);
        inventory.setWarehouse(warehouse);
        inventory.setProduct(product);
        inventory.setLocation(location);

        Inventory savedInventory = inventoryRepository.save(inventory);
        log.info("Inventory created successfully: {}", savedInventory.getId());

        return inventoryMapper.toResponse(savedInventory);
    }

    @Override
    @Transactional
    public InventoryResponse updateInventory(Long id, InventoryRequest request) {
        log.info("Updating inventory: {}", id);

        Inventory inventory = findInventoryById(id);

        // Validate unique constraint if key fields changed
        if (!inventory.getWarehouse().getId().equals(request.getWarehouseId()) ||
            !inventory.getProduct().getId().equals(request.getProductId()) ||
            !String.valueOf(inventory.getBatchNumber()).equals(String.valueOf(request.getBatchNumber())) ||
            !String.valueOf(inventory.getLocation() != null ? inventory.getLocation().getId() : null)
                .equals(String.valueOf(request.getLocationId()))) {
            
            validateUniqueInventory(request.getWarehouseId(), request.getProductId(),
                request.getBatchNumber(), request.getLocationId(), id);
        }

        // Update fields
        inventoryMapper.updateEntityFromRequest(request, inventory);

        // Update relationships if changed
        if (!inventory.getWarehouse().getId().equals(request.getWarehouseId())) {
            Warehouse warehouse = findWarehouseById(request.getWarehouseId());
            inventory.setWarehouse(warehouse);
        }

        if (!inventory.getProduct().getId().equals(request.getProductId())) {
            Product product = findProductById(request.getProductId());
            inventory.setProduct(product);
        }

        if (request.getLocationId() != null) {
            WarehouseLocation location = findWarehouseLocationById(request.getLocationId());
            inventory.setLocation(location);
        } else {
            inventory.setLocation(null);
        }

        Inventory updatedInventory = inventoryRepository.save(inventory);
        log.info("Inventory updated successfully: {}", updatedInventory.getId());

        return inventoryMapper.toResponse(updatedInventory);
    }

    @Override
    @Transactional
    public void reserveQuantity(Long inventoryId, BigDecimal quantity) {
        log.info("Reserving quantity {} for inventory: {}", quantity, inventoryId);

        Inventory inventory = findInventoryById(inventoryId);

        // Check if enough quantity available
        if (inventory.getQuantityAvailable().compareTo(quantity) < 0) {
            throw new InvalidOperationException(
                "Insufficient quantity available. Available: " + inventory.getQuantityAvailable() + 
                ", Requested: " + quantity);
        }

        // Update quantities
        inventory.setQuantityAvailable(inventory.getQuantityAvailable().subtract(quantity));
        inventory.setQuantityReserved(inventory.getQuantityReserved().add(quantity));
        
        inventoryRepository.save(inventory);
        log.info("Quantity reserved successfully");
    }

    @Override
    @Transactional
    public void releaseReservation(Long inventoryId, BigDecimal quantity) {
        log.info("Releasing reservation {} for inventory: {}", quantity, inventoryId);

        Inventory inventory = findInventoryById(inventoryId);

        // Check if enough quantity reserved
        if (inventory.getQuantityReserved().compareTo(quantity) < 0) {
            throw new InvalidOperationException(
                "Insufficient quantity reserved. Reserved: " + inventory.getQuantityReserved() + 
                ", Requested to release: " + quantity);
        }

        // Update quantities
        inventory.setQuantityReserved(inventory.getQuantityReserved().subtract(quantity));
        inventory.setQuantityAvailable(inventory.getQuantityAvailable().add(quantity));
        
        inventoryRepository.save(inventory);
        log.info("Reservation released successfully");
    }

    @Override
    @Transactional
    public void updateQuantityAvailable(Long inventoryId, BigDecimal quantity) {
        log.info("Updating quantity available for inventory: {}", inventoryId);

        Inventory inventory = findInventoryById(inventoryId);
        inventory.setQuantityAvailable(quantity);
        inventory.setLastStockDate(LocalDate.now());
        
        inventoryRepository.save(inventory);
        log.info("Quantity available updated successfully");
    }

    @Override
    public InventoryResponse getInventoryById(Long id) {
        Inventory inventory = findInventoryById(id);
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    public InventoryResponse getInventoryByWarehouseAndProduct(Long warehouseId, Long productId, 
                                                               String batchNumber, Long locationId) {
        Inventory inventory = inventoryRepository
            .findByWarehouseIdAndProductIdAndBatchNumberAndLocationId(
                warehouseId, productId, batchNumber, locationId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Inventory not found for warehouse: " + warehouseId + ", product: " + productId));
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    public List<InventoryResponse> getInventoryByBatch(String batchNumber) {
        List<Inventory> inventories = inventoryRepository.findByBatchNumber(batchNumber);
        return inventories.stream()
            .map(inventoryMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<InventoryResponse> getAllInventory(Pageable pageable) {
        Page<Inventory> inventoryPage = inventoryRepository.findAll(pageable);
        return createPageResponse(inventoryPage);
    }

    @Override
    public List<InventoryResponse> getInventoryByWarehouse(Long warehouseId) {
        List<Inventory> inventories = inventoryRepository.findByWarehouseId(warehouseId);
        return inventories.stream()
            .map(inventoryMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<InventoryResponse> getInventoryByProduct(Long productId) {
        List<Inventory> inventories = inventoryRepository.findByProductId(productId);
        return inventories.stream()
            .map(inventoryMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<InventoryResponse> getLowStockItems() {
        List<Inventory> inventories = inventoryRepository.findLowStockInventory();
        return inventories.stream()
            .map(inventoryMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<InventoryResponse> getExpiredStock() {
        List<Inventory> inventories = inventoryRepository.findExpiredInventory();
        return inventories.stream()
            .map(inventoryMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<InventoryResponse> getExpiringStock(Integer daysBeforeExpiry) {
        LocalDate expiryDate = LocalDate.now().plusDays(daysBeforeExpiry);
        List<Inventory> inventories = inventoryRepository.findInventoryExpiringInDays(LocalDate.now(), expiryDate);
        return inventories.stream()
            .map(inventoryMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<InventoryResponse> getInventoryByLocation(Long locationId) {
        List<Inventory> inventories = inventoryRepository.findByLocationId(locationId);
        return inventories.stream()
            .map(inventoryMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalStockByProduct(Long productId) {
        BigDecimal total = inventoryRepository.getTotalAvailableQuantityByProduct(productId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getTotalStockValue(Long warehouseId) {
        BigDecimal total = inventoryRepository.getInventoryValueByWarehouse(warehouseId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public List<InventoryValuation> getInventoryValuation(Long warehouseId) {
        // This would typically involve complex calculations
        // For now, returning empty list - to be implemented based on business requirements
        return List.of();
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private Inventory findInventoryById(Long id) {
        return inventoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inventory not found: " + id));
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

    private void validateUniqueInventory(Long warehouseId, Long productId, String batchNumber, 
                                        Long locationId, Long excludeId) {
        if (excludeId == null) {
            if (inventoryRepository.existsByWarehouseIdAndProductIdAndBatchNumberAndLocationId(
                    warehouseId, productId, batchNumber, locationId)) {
                throw new DuplicateResourceException("Inventory already exists for this combination");
            }
        } else {
            if (inventoryRepository.existsByWarehouseIdAndProductIdAndBatchNumberAndLocationIdAndIdNot(
                    warehouseId, productId, batchNumber, locationId, excludeId)) {
                throw new DuplicateResourceException("Inventory already exists for this combination");
            }
        }
    }

    private PageResponse<InventoryResponse> createPageResponse(Page<Inventory> inventoryPage) {
        List<InventoryResponse> content = inventoryPage.getContent().stream()
            .map(inventoryMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<InventoryResponse>builder()
            .content(content)
            .pageNumber(inventoryPage.getNumber())
            .pageSize(inventoryPage.getSize())
            .totalElements(inventoryPage.getTotalElements())
            .totalPages(inventoryPage.getTotalPages())
            .last(inventoryPage.isLast())
            .first(inventoryPage.isFirst())
            .empty(inventoryPage.isEmpty())
            .build();
    }

    @Override
    public void deleteInventory(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteInventory'");
    }

    @Override
    public InventoryResponse getInventoryByProductAndWarehouse(Long productId, Long warehouseId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInventoryByProductAndWarehouse'");
    }

    @Override
    public List<Inventory> getAllInventory() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllInventory'");
    }

    @Override
    public PageResponse<InventoryResponse> searchInventory(String keyword, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchInventory'");
    }

    @Override
    public void addStock(Long id, Double quantity, Double cost) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addStock'");
    }

    @Override
    public void removeStock(Long id, Double quantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeStock'");
    }

    @Override
    public void adjustStock(Long id, Double newQuantity, String reason) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'adjustStock'");
    }

    @Override
    public void increaseStock(Long id, Double quantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'increaseStock'");
    }

    @Override
    public void decreaseStock(Long id, Double quantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'decreaseStock'");
    }

    @Override
    public void transferStock(Long fromInventoryId, Long toInventoryId, Double quantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'transferStock'");
    }

    @Override
    public void reserveStock(Long id, Double quantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reserveStock'");
    }

    @Override
    public void releaseReservation(Long id, Double quantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'releaseReservation'");
    }

    @Override
    public void releaseReservedStock(Long id, Double quantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'releaseReservedStock'");
    }

    @Override
    public void allocateStock(Long id, Double quantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'allocateStock'");
    }

    @Override
    public void releaseAllocation(Long id, Double quantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'releaseAllocation'");
    }

    @Override
    public void deallocateStock(Long id, Double quantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deallocateStock'");
    }

    @Override
    public void recordDamagedStock(Long id, Double quantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'recordDamagedStock'");
    }

    @Override
    public void recordExpiredStock(Long id, Double quantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'recordExpiredStock'");
    }

    @Override
    public void updateStockLevels(Long id, Integer reorderLevel, Integer maxLevel, Integer minLevel) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateStockLevels'");
    }

    @Override
    public Double getAvailableQuantity(Long productId, Long warehouseId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAvailableQuantity'");
    }

    @Override
    public boolean isStockAvailable(Long productId, Long warehouseId, Double requiredQuantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isStockAvailable'");
    }

    @Override
    public List<Inventory> getOutOfStockItems() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOutOfStockItems'");
    }

    @Override
    public Map<String, Object> getInventorySummary(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInventorySummary'");
    }

    @Override
    public Map<String, Object> calculateInventoryMetrics(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calculateInventoryMetrics'");
    }

    @Override
    public Map<String, Object> getInventoryStatistics() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInventoryStatistics'");
    }

    @Override
    public Map<String, Object> getDashboardStatistics() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDashboardStatistics'");
    }
}
