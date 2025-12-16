package lk.epicgreen.erp.warehouse.service;

import lk.epicgreen.erp.warehouse.dto.InventoryRequest;
import lk.epicgreen.erp.warehouse.entity.Inventory;
import lk.epicgreen.erp.warehouse.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Inventory Service Implementation
 * Implementation of inventory service operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryServiceImpl implements InventoryService {
    
    private final InventoryRepository inventoryRepository;
    
    @Override
    public Inventory createInventory(InventoryRequest request) {
        log.info("Creating inventory for product: {} in warehouse: {}", 
                 request.getProductId(), request.getWarehouseId());
        
        // Check if inventory already exists
        if (inventoryRepository.existsByProductIdAndWarehouseId(
                request.getProductId(), request.getWarehouseId())) {
            throw new RuntimeException("Inventory already exists for this product in this warehouse");
        }
        
        Inventory inventory = new Inventory();
        inventory.setProductId(request.getProductId());
        inventory.setProductCode(request.getProductCode());
        inventory.setProductName(request.getProductName());
        inventory.setWarehouseId(request.getWarehouseId());
        inventory.setWarehouseName(request.getWarehouseName());
        inventory.setQuantityOnHand(request.getQuantityOnHand() != null ? request.getQuantityOnHand() : 0.0);
        inventory.setQuantityReserved(0.0);
        inventory.setQuantityAllocated(0.0);
        inventory.setQuantityDamaged(0.0);
        inventory.setQuantityExpired(0.0);
        inventory.setQuantityAvailable(inventory.getQuantityOnHand());
        inventory.setReorderLevel(request.getReorderLevel());
        inventory.setReorderQuantity(request.getReorderQuantity());
        inventory.setMaxStockLevel(request.getMaxStockLevel());
        inventory.setMinStockLevel(request.getMinStockLevel());
        inventory.setUnitCost(request.getUnitCost());
        inventory.setAverageCost(request.getUnitCost());
        inventory.setLastCost(request.getUnitCost());
        inventory.setTotalValue(calculateTotalValue(inventory));
        inventory.setStatus("ACTIVE");
        inventory.setStockStatus(determineStockStatus(inventory));
        inventory.setLastUpdated(LocalDateTime.now());
        
        return inventoryRepository.save(inventory);
    }
    
    @Override
    public Inventory updateInventory(Long id, InventoryRequest request) {
        log.info("Updating inventory: {}", id);
        Inventory existing = getInventoryById(id);
        
        existing.setReorderLevel(request.getReorderLevel());
        existing.setReorderQuantity(request.getReorderQuantity());
        existing.setMaxStockLevel(request.getMaxStockLevel());
        existing.setMinStockLevel(request.getMinStockLevel());
        existing.setUnitCost(request.getUnitCost());
        existing.setLastUpdated(LocalDateTime.now());
        
        updateStockStatus(id);
        
        return inventoryRepository.save(existing);
    }
    
    @Override
    public void deleteInventory(Long id) {
        log.info("Deleting inventory: {}", id);
        Inventory inventory = getInventoryById(id);
        
        if (inventory.getQuantityOnHand() > 0) {
            throw new RuntimeException("Cannot delete inventory with stock on hand");
        }
        
        inventoryRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Inventory getInventoryByProductAndWarehouse(Long productId, Long warehouseId) {
        return inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId)
            .orElseThrow(() -> new RuntimeException(
                "Inventory not found for product: " + productId + " in warehouse: " + warehouseId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Inventory> getAllInventory(Pageable pageable) {
        return inventoryRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Inventory> searchInventory(String keyword, Pageable pageable) {
        return inventoryRepository.searchInventory(keyword, pageable);
    }
    
    @Override
    public void increaseStock(Long inventoryId, Double quantity) {
        Inventory inventory = getInventoryById(inventoryId);
        inventory.setQuantityOnHand(inventory.getQuantityOnHand() + quantity);
        updateAvailableQuantity(inventoryId);
        updateStockStatus(inventoryId);
        inventory.setLastMovementDate(LocalDateTime.now());
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepository.save(inventory);
    }
    
    @Override
    public void decreaseStock(Long inventoryId, Double quantity) {
        Inventory inventory = getInventoryById(inventoryId);
        
        if (inventory.getQuantityOnHand() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + inventory.getQuantityOnHand());
        }
        
        inventory.setQuantityOnHand(inventory.getQuantityOnHand() - quantity);
        updateAvailableQuantity(inventoryId);
        updateStockStatus(inventoryId);
        inventory.setLastMovementDate(LocalDateTime.now());
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepository.save(inventory);
    }
    
    @Override
    public void adjustStock(Long inventoryId, Double newQuantity, String reason) {
        log.info("Adjusting stock for inventory: {} to quantity: {}", inventoryId, newQuantity);
        Inventory inventory = getInventoryById(inventoryId);
        
        inventory.setQuantityOnHand(newQuantity);
        updateAvailableQuantity(inventoryId);
        updateStockStatus(inventoryId);
        inventory.setLastMovementDate(LocalDateTime.now());
        inventory.setLastUpdated(LocalDateTime.now());
        
        inventoryRepository.save(inventory);
    }
    
    @Override
    public void reserveStock(Long inventoryId, Double quantity) {
        Inventory inventory = getInventoryById(inventoryId);
        
        if (inventory.getQuantityAvailable() < quantity) {
            throw new RuntimeException("Insufficient available stock. Available: " + 
                                     inventory.getQuantityAvailable());
        }
        
        inventory.setQuantityReserved(inventory.getQuantityReserved() + quantity);
        updateAvailableQuantity(inventoryId);
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepository.save(inventory);
    }
    
    @Override
    public void releaseReservedStock(Long inventoryId, Double quantity) {
        Inventory inventory = getInventoryById(inventoryId);
        
        if (inventory.getQuantityReserved() < quantity) {
            throw new RuntimeException("Insufficient reserved stock. Reserved: " + 
                                     inventory.getQuantityReserved());
        }
        
        inventory.setQuantityReserved(inventory.getQuantityReserved() - quantity);
        updateAvailableQuantity(inventoryId);
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepository.save(inventory);
    }
    
    @Override
    public void allocateStock(Long inventoryId, Double quantity) {
        Inventory inventory = getInventoryById(inventoryId);
        
        if (inventory.getQuantityAvailable() < quantity) {
            throw new RuntimeException("Insufficient available stock. Available: " + 
                                     inventory.getQuantityAvailable());
        }
        
        inventory.setQuantityAllocated(inventory.getQuantityAllocated() + quantity);
        updateAvailableQuantity(inventoryId);
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepository.save(inventory);
    }
    
    @Override
    public void deallocateStock(Long inventoryId, Double quantity) {
        Inventory inventory = getInventoryById(inventoryId);
        
        if (inventory.getQuantityAllocated() < quantity) {
            throw new RuntimeException("Insufficient allocated stock. Allocated: " + 
                                     inventory.getQuantityAllocated());
        }
        
        inventory.setQuantityAllocated(inventory.getQuantityAllocated() - quantity);
        updateAvailableQuantity(inventoryId);
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepository.save(inventory);
    }
    
    @Override
    public void markDamaged(Long inventoryId, Double quantity, String reason) {
        Inventory inventory = getInventoryById(inventoryId);
        
        if (inventory.getQuantityOnHand() < quantity) {
            throw new RuntimeException("Insufficient stock to mark as damaged");
        }
        
        inventory.setQuantityDamaged(inventory.getQuantityDamaged() + quantity);
        inventory.setQuantityOnHand(inventory.getQuantityOnHand() - quantity);
        updateAvailableQuantity(inventoryId);
        updateStockStatus(inventoryId);
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepository.save(inventory);
    }
    
    @Override
    public void markExpired(Long inventoryId, Double quantity, String reason) {
        Inventory inventory = getInventoryById(inventoryId);
        
        if (inventory.getQuantityOnHand() < quantity) {
            throw new RuntimeException("Insufficient stock to mark as expired");
        }
        
        inventory.setQuantityExpired(inventory.getQuantityExpired() + quantity);
        inventory.setQuantityOnHand(inventory.getQuantityOnHand() - quantity);
        updateAvailableQuantity(inventoryId);
        updateStockStatus(inventoryId);
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepository.save(inventory);
    }
    
    @Override
    public void updateAvailableQuantity(Long inventoryId) {
        Inventory inventory = getInventoryById(inventoryId);
        Double available = calculateAvailableQuantity(inventory);
        inventory.setQuantityAvailable(available);
        inventoryRepository.save(inventory);
    }
    
    @Override
    public void recordStockCount(Long inventoryId, Double countedQuantity, String countedBy) {
        log.info("Recording stock count for inventory: {}", inventoryId);
        Inventory inventory = getInventoryById(inventoryId);
        
        inventory.setQuantityOnHand(countedQuantity);
        inventory.setLastStockCountDate(LocalDate.now());
        updateAvailableQuantity(inventoryId);
        updateStockStatus(inventoryId);
        inventory.setLastUpdated(LocalDateTime.now());
        
        inventoryRepository.save(inventory);
    }
    
    @Override
    public void updateLastStockCountDate(Long inventoryId, LocalDate countDate) {
        Inventory inventory = getInventoryById(inventoryId);
        inventory.setLastStockCountDate(countDate);
        inventoryRepository.save(inventory);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryRequiringStockCount(int daysThreshold) {
        LocalDate thresholdDate = LocalDate.now().minusDays(daysThreshold);
        return inventoryRepository.findInventoryRequiringStockCount(thresholdDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getActiveInventory() {
        return inventoryRepository.findActiveInventory();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryWithStock() {
        return inventoryRepository.findInventoryWithStock();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryWithoutStock() {
        return inventoryRepository.findInventoryWithoutStock();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getLowStockInventory() {
        return inventoryRepository.findLowStockInventory();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getOutOfStockInventory() {
        return inventoryRepository.findOutOfStockInventory();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getOverstockInventory() {
        return inventoryRepository.findOverstockInventory();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getOptimalStockInventory() {
        return inventoryRepository.findOptimalStockInventory();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryBelowReorderLevel() {
        return inventoryRepository.findInventoryBelowReorderLevel();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryAboveMaxStockLevel() {
        return inventoryRepository.findInventoryAboveMaxStockLevel();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryWithReservedStock() {
        return inventoryRepository.findInventoryWithReservedStock();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryWithAllocatedStock() {
        return inventoryRepository.findInventoryWithAllocatedStock();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryWithDamagedStock() {
        return inventoryRepository.findInventoryWithDamagedStock();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryWithExpiredStock() {
        return inventoryRepository.findInventoryWithExpiredStock();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryWithAvailableStock() {
        return inventoryRepository.findInventoryWithAvailableStock();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryByValueRange(Double minValue, Double maxValue) {
        return inventoryRepository.findByValueRange(minValue, maxValue);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getHighValueInventory(Double threshold) {
        return inventoryRepository.findHighValueInventory(threshold);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getSlowMovingInventory(int daysThreshold) {
        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(daysThreshold);
        return inventoryRepository.findSlowMovingInventory(thresholdDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getFastMovingInventory(int daysThreshold) {
        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(daysThreshold);
        return inventoryRepository.findFastMovingInventory(thresholdDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getWarehouseInventory(Long warehouseId) {
        return inventoryRepository.findWarehouseInventorySummary(warehouseId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getProductInventoryAcrossWarehouses(Long productId) {
        return inventoryRepository.findProductInventoryAcrossWarehouses(productId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getRecentInventoryUpdates(int limit) {
        return inventoryRepository.findRecentInventoryUpdates(PageRequest.of(0, limit));
    }
    
    @Override
    public void updateStockStatus(Long inventoryId) {
        Inventory inventory = getInventoryById(inventoryId);
        String newStatus = determineStockStatus(inventory);
        inventory.setStockStatus(newStatus);
        inventoryRepository.save(inventory);
    }
    
    @Override
    public void recalculateAllStockStatuses() {
        List<Inventory> allInventory = inventoryRepository.findAll();
        allInventory.forEach(inventory -> {
            inventory.setStockStatus(determineStockStatus(inventory));
            inventoryRepository.save(inventory);
        });
    }
    
    @Override
    public String determineStockStatus(Inventory inventory) {
        Double quantityOnHand = inventory.getQuantityOnHand();
        Double reorderLevel = inventory.getReorderLevel() != null ? inventory.getReorderLevel() : 0.0;
        Double maxStockLevel = inventory.getMaxStockLevel() != null ? inventory.getMaxStockLevel() : 0.0;
        
        if (quantityOnHand == 0) {
            return "OUT_OF_STOCK";
        } else if (reorderLevel > 0 && quantityOnHand <= reorderLevel) {
            return "LOW";
        } else if (maxStockLevel > 0 && quantityOnHand > maxStockLevel) {
            return "OVERSTOCK";
        } else {
            return "OPTIMAL";
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validateInventory(Inventory inventory) {
        return inventory.getProductId() != null &&
               inventory.getWarehouseId() != null &&
               inventory.getQuantityOnHand() != null &&
               inventory.getQuantityOnHand() >= 0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasAvailableStock(Long inventoryId, Double requiredQuantity) {
        Inventory inventory = getInventoryById(inventoryId);
        return inventory.getQuantityAvailable() >= requiredQuantity;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canReserveStock(Long inventoryId, Double quantity) {
        return hasAvailableStock(inventoryId, quantity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canAllocateStock(Long inventoryId, Double quantity) {
        return hasAvailableStock(inventoryId, quantity);
    }
    
    @Override
    public Double calculateAvailableQuantity(Inventory inventory) {
        return inventory.getQuantityOnHand() - 
               inventory.getQuantityReserved() - 
               inventory.getQuantityAllocated();
    }
    
    @Override
    public Double calculateTotalValue(Inventory inventory) {
        return inventory.getQuantityOnHand() * 
               (inventory.getAverageCost() != null ? inventory.getAverageCost() : 0.0);
    }
    
    @Override
    public void recalculateInventoryValues(Long inventoryId) {
        Inventory inventory = getInventoryById(inventoryId);
        inventory.setTotalValue(calculateTotalValue(inventory));
        inventoryRepository.save(inventory);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> calculateInventoryMetrics(Long inventoryId) {
        Inventory inventory = getInventoryById(inventoryId);
        Map<String, Object> metrics = new HashMap<>();
        
        metrics.put("quantityOnHand", inventory.getQuantityOnHand());
        metrics.put("quantityAvailable", inventory.getQuantityAvailable());
        metrics.put("quantityReserved", inventory.getQuantityReserved());
        metrics.put("quantityAllocated", inventory.getQuantityAllocated());
        metrics.put("totalValue", inventory.getTotalValue());
        metrics.put("stockStatus", inventory.getStockStatus());
        metrics.put("reorderLevel", inventory.getReorderLevel());
        metrics.put("maxStockLevel", inventory.getMaxStockLevel());
        
        return metrics;
    }
    
    @Override
    public List<Inventory> createBulkInventory(List<InventoryRequest> requests) {
        return requests.stream()
            .map(this::createInventory)
            .collect(Collectors.toList());
    }
    
    @Override
    public int adjustBulkStock(List<Long> inventoryIds, String reason) {
        int count = 0;
        for (Long id : inventoryIds) {
            try {
                updateStockStatus(id);
                count++;
            } catch (Exception e) {
                log.error("Error adjusting inventory: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int deleteInventoryBulk(List<Long> inventoryIds) {
        int count = 0;
        for (Long id : inventoryIds) {
            try {
                deleteInventory(id);
                count++;
            } catch (Exception e) {
                log.error("Error deleting inventory: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getInventoryStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalItems", inventoryRepository.count());
        stats.put("lowStockItems", inventoryRepository.countLowStockItems());
        stats.put("outOfStockItems", inventoryRepository.countOutOfStockItems());
        stats.put("overstockItems", inventoryRepository.countOverstockItems());
        stats.put("totalQuantityOnHand", getTotalQuantityOnHand());
        stats.put("totalAvailableQuantity", getTotalAvailableQuantity());
        stats.put("totalReservedQuantity", getTotalReservedQuantity());
        stats.put("totalAllocatedQuantity", getTotalAllocatedQuantity());
        stats.put("totalInventoryValue", getTotalInventoryValue());
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getStockStatusDistribution() {
        List<Object[]> results = inventoryRepository.getStockStatusDistribution();
        return convertToMapList(results, "stockStatus", "itemCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getInventoryByWarehouse() {
        List<Object[]> results = inventoryRepository.getInventoryByWarehouse();
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("warehouseId", result[0]);
                map.put("warehouseName", result[1]);
                map.put("itemCount", result[2]);
                map.put("totalQuantity", result[3]);
                map.put("totalValue", result[4]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getInventoryTurnoverData() {
        List<Object[]> results = inventoryRepository.getInventoryTurnoverData();
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("productId", result[0]);
                map.put("productName", result[1]);
                map.put("quantityOnHand", result[2]);
                map.put("averageCost", result[3]);
                map.put("lastMovementDate", result[4]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getTopValueInventoryItems(int limit) {
        return inventoryRepository.getTopValueInventoryItems(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getTopQuantityInventoryItems(int limit) {
        return inventoryRepository.getTopQuantityInventoryItems(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalQuantityOnHand() {
        Double total = inventoryRepository.getTotalQuantityOnHand();
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalAvailableQuantity() {
        Double total = inventoryRepository.getTotalAvailableQuantity();
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalReservedQuantity() {
        Double total = inventoryRepository.getTotalReservedQuantity();
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalAllocatedQuantity() {
        Double total = inventoryRepository.getTotalAllocatedQuantity();
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalInventoryValue() {
        Double total = inventoryRepository.getTotalInventoryValue();
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getInventoryValueByWarehouse(Long warehouseId) {
        Double total = inventoryRepository.getInventoryValueByWarehouse(warehouseId);
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("statistics", getInventoryStatistics());
        dashboard.put("stockStatusDistribution", getStockStatusDistribution());
        dashboard.put("inventoryByWarehouse", getInventoryByWarehouse());
        dashboard.put("topValueItems", getTopValueInventoryItems(10));
        
        return dashboard;
    }
    
    private List<Map<String, Object>> convertToMapList(List<Object[]> results, String key1, String key2) {
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put(key1, result[0]);
                map.put(key2, result[1]);
                return map;
            })
            .collect(Collectors.toList());
    }
}
