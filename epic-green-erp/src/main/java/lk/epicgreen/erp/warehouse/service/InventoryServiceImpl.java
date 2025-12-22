package lk.epicgreen.erp.warehouse.service;

import lk.epicgreen.erp.warehouse.dto.InventoryRequest;
import lk.epicgreen.erp.warehouse.entity.Inventory;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lk.epicgreen.erp.warehouse.repository.InventoryRepository;
import lk.epicgreen.erp.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Inventory Service Implementation - Complete
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
    private final WarehouseRepository warehouseRepository;

    @Override
    public Inventory createInventory(InventoryRequest request) {
        log.info("Creating inventory for product: {} in warehouse: {}",
                request.getProductId(), request.getWarehouseId());

        // Check if inventory already exists
        Optional<Inventory> existing = inventoryRepository
                .findByProductIdAndWarehouseId(request.getProductId(), request.getWarehouseId());

        if (existing.isPresent()) {
            throw new RuntimeException("Inventory already exists for this product in this warehouse");
        }

        Inventory inventory = new Inventory();
        inventory.setProductId(request.getProductId());
        inventory.setProductCode(request.getProductCode());
        inventory.setProductName(request.getProductName());
        // Set warehouse
        if (request.getWarehouseId() != null) {
            Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                    .orElseThrow(() -> new RuntimeException("Warehouse not found"));
            inventory.setWarehouseId(request.getWarehouseId());
            inventory.setWarehouse(warehouse);
            inventory.setWarehouseName(warehouse.getWarehouseName()); // Denormalized
        }

        inventory.setWarehouseName(request.getWarehouseName());
        inventory.setQuantityOnHand(request.getQuantityOnHand() != null ? request.getQuantityOnHand().doubleValue(): 0.0);
        inventory.setQuantityReserved(0.0);
        inventory.setQuantityAllocated(0.0);
        inventory.setQuantityDamaged(0.0);
        inventory.setQuantityExpired(0.0);
        inventory.setQuantityAvailable(inventory.getQuantityOnHand());
        inventory.setReorderLevel(request.getReorderLevel());
        inventory.setReorderQuantity(request.getReorderQuantity());
        inventory.setMaxStockLevel(request.getMaxStockLevel());
        inventory.setMinStockLevel(request.getMinStockLevel());
        inventory.setUnitCost(request.getUnitCost().doubleValue());
        inventory.setAverageCost(request.getAverageCost().doubleValue());
        inventory.setLastCost(request.getLastCost().doubleValue());
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
        existing.setUnitCost(request.getUnitCost().doubleValue());
        existing.setLastUpdated(LocalDateTime.now());

        return inventoryRepository.save(existing);
    }

    @Override
    public void deleteInventory(Long id) {
        log.info("Deleting inventory: {}", id);
        Inventory inventory = getInventoryById(id);

        // Check if there's any stock
        if (inventory.getQuantityOnHand() > 0) {
            throw new RuntimeException("Cannot delete inventory with existing stock");
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
    public List<Inventory> getInventoryByProduct(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryByWarehouse(Long warehouseId) {
        return inventoryRepository.findByWarehouseId(warehouseId);
    }

    @Override
    public void addStock(Long inventoryId, Double quantity, Double cost) {
        log.info("Adding {} units to inventory: {}", quantity, inventoryId);
        Inventory inventory = getInventoryById(inventoryId);

        Double currentQty = inventory.getQuantityOnHand();
        Double newQty = currentQty + quantity;
        inventory.setQuantityOnHand(newQty);

        // Update costs using weighted average
        updateAverageCost(inventory, quantity, cost);
        inventory.setLastCost(cost);
        inventory.setTotalValue(calculateTotalValue(inventory));
        inventory.setLastMovementDate(LocalDateTime.now());
        inventory.setLastUpdated(LocalDateTime.now());

        updateStockStatus(inventory);
        inventoryRepository.save(inventory);
    }

    @Override
    public void removeStock(Long inventoryId, Double quantity) {
        log.info("Removing {} units from inventory: {}", quantity, inventoryId);
        Inventory inventory = getInventoryById(inventoryId);

        Double currentQty = inventory.getQuantityOnHand();
        if (currentQty < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + currentQty + ", Requested: " + quantity);
        }

        Double newQty = currentQty - quantity;
        inventory.setQuantityOnHand(newQty);
        inventory.setTotalValue(calculateTotalValue(inventory));
        inventory.setLastMovementDate(LocalDateTime.now());
        inventory.setLastUpdated(LocalDateTime.now());

        updateStockStatus(inventory);
        inventoryRepository.save(inventory);
    }

    @Override
    public void adjustStock(Long inventoryId, Double newQuantity, String reason) {
        log.info("Adjusting inventory {} to quantity: {} - Reason: {}", inventoryId, newQuantity, reason);
        Inventory inventory = getInventoryById(inventoryId);

        inventory.setQuantityOnHand(newQuantity);
        inventory.setQuantityAvailable(newQuantity);
        inventory.setTotalValue(calculateTotalValue(inventory));
        inventory.setLastMovementDate(LocalDateTime.now());
        inventory.setLastUpdated(LocalDateTime.now());

        updateStockStatus(inventory);
        inventoryRepository.save(inventory);
    }

    @Override
    public void reserveStock(Long inventoryId, Double quantity) {
        log.info("Reserving {} units from inventory: {}", quantity, inventoryId);
        Inventory inventory = getInventoryById(inventoryId);

        Double available = inventory.getQuantityAvailable();
        if (available < quantity) {
            throw new RuntimeException("Insufficient available stock. Available: " + available + ", Requested: " + quantity);
        }

        Double currentReserved = inventory.getQuantityReserved();
        inventory.setQuantityReserved(currentReserved + quantity);
        inventory.setQuantityAvailable(inventory.calculateAvailableQuantity());
        inventory.setLastUpdated(LocalDateTime.now());

        updateStockStatus(inventory);
        inventoryRepository.save(inventory);
    }

    @Override
    public void releaseReservation(Long inventoryId, Double quantity) {
        log.info("Releasing {} reserved units from inventory: {}", quantity, inventoryId);
        Inventory inventory = getInventoryById(inventoryId);

        Double currentReserved = inventory.getQuantityReserved();
        if (currentReserved < quantity) {
            throw new RuntimeException("Cannot release more than reserved. Reserved: " + currentReserved);
        }

        inventory.setQuantityReserved(currentReserved - quantity);
        inventory.setQuantityAvailable(inventory.calculateAvailableQuantity());
        inventory.setLastUpdated(LocalDateTime.now());

        updateStockStatus(inventory);
        inventoryRepository.save(inventory);
    }

    @Override
    public void allocateStock(Long inventoryId, Double quantity) {
        log.info("Allocating {} units from inventory: {}", quantity, inventoryId);
        Inventory inventory = getInventoryById(inventoryId);

        Double available = inventory.getQuantityAvailable();
        if (available < quantity) {
            throw new RuntimeException("Insufficient available stock. Available: " + available);
        }

        Double currentAllocated = inventory.getQuantityAllocated();
        inventory.setQuantityAllocated(currentAllocated + quantity);
        inventory.setQuantityAvailable(inventory.calculateAvailableQuantity());
        inventory.setLastUpdated(LocalDateTime.now());

        updateStockStatus(inventory);
        inventoryRepository.save(inventory);
    }

    @Override
    public void releaseAllocation(Long inventoryId, Double quantity) {
        log.info("Releasing {} allocated units from inventory: {}", quantity, inventoryId);
        Inventory inventory = getInventoryById(inventoryId);

        Double currentAllocated = inventory.getQuantityAllocated();
        if (currentAllocated < quantity) {
            throw new RuntimeException("Cannot release more than allocated. Allocated: " + currentAllocated);
        }

        inventory.setQuantityAllocated(currentAllocated - quantity);
        inventory.setQuantityAvailable(inventory.calculateAvailableQuantity());
        inventory.setLastUpdated(LocalDateTime.now());

        updateStockStatus(inventory);
        inventoryRepository.save(inventory);
    }

    @Override
    public void recordDamagedStock(Long inventoryId, Double quantity) {
        log.info("Recording {} damaged units in inventory: {}", quantity, inventoryId);
        Inventory inventory = getInventoryById(inventoryId);

        Double currentQty = inventory.getQuantityOnHand();
        if (currentQty < quantity) {
            throw new RuntimeException("Cannot record more damaged than on hand");
        }

        Double currentDamaged = inventory.getQuantityDamaged();
        inventory.setQuantityDamaged(currentDamaged + quantity);
        inventory.setQuantityOnHand(currentQty - quantity);
        inventory.setQuantityAvailable(inventory.calculateAvailableQuantity());
        inventory.setLastUpdated(LocalDateTime.now());

        updateStockStatus(inventory);
        inventoryRepository.save(inventory);
    }

    @Override
    public void recordExpiredStock(Long inventoryId, Double quantity) {
        log.info("Recording {} expired units in inventory: {}", quantity, inventoryId);
        Inventory inventory = getInventoryById(inventoryId);

        Double currentQty = inventory.getQuantityOnHand();
        if (currentQty < quantity) {
            throw new RuntimeException("Cannot record more expired than on hand");
        }

        Double currentExpired = inventory.getQuantityExpired();
        inventory.setQuantityExpired(currentExpired + quantity);
        inventory.setQuantityOnHand(currentQty - quantity);
        inventory.setQuantityAvailable(inventory.calculateAvailableQuantity());
        inventory.setLastUpdated(LocalDateTime.now());

        updateStockStatus(inventory);
        inventoryRepository.save(inventory);
    }

    @Override
    public void updateStockLevels(Long inventoryId, Integer reorderLevel, Integer maxLevel, Integer minLevel) {
        Inventory inventory = getInventoryById(inventoryId);

        if (reorderLevel != null) inventory.setReorderLevel(reorderLevel);
        if (maxLevel != null) inventory.setMaxStockLevel(maxLevel);
        if (minLevel != null) inventory.setMinStockLevel(minLevel);

        updateStockStatus(inventory);
        inventoryRepository.save(inventory);
    }

    @Override
    public void transferStock(Long fromInventoryId, Long toInventoryId, Double quantity) {
        removeStock(fromInventoryId, quantity);

        Inventory fromInv = getInventoryById(fromInventoryId);
        addStock(toInventoryId, quantity, fromInv.getAverageCost());
    }

    // Query Operations

    @Override
    @Transactional(readOnly = true)
    public Double getAvailableQuantity(Long productId, Long warehouseId) {
        Inventory inventory = getInventoryByProductAndWarehouse(productId, warehouseId);
        return inventory.calculateAvailableQuantity();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isStockAvailable(Long productId, Long warehouseId, Double requiredQuantity) {
        try {
            Inventory inventory = getInventoryByProductAndWarehouse(productId, warehouseId);
            return inventory.getProductId() != null &&
                    inventory.getWarehouseId() != null &&
                    inventory.getQuantityOnHand() >= requiredQuantity;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getLowStockItems() {
        return inventoryRepository.findAll().stream()
                .filter(inv -> {
                    Double qty = inv.getQuantityAvailable() != null ? inv.getQuantityAvailable() : 0.0;
                    Integer reorder = inv.getReorderLevel() != null ? inv.getReorderLevel() : 0;
                    return qty <= reorder;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getOutOfStockItems() {
        return inventoryRepository.findAll().stream()
                .filter(inv -> {
                    Double qty = inv.getQuantityOnHand() != null ? inv.getQuantityOnHand() : 0.0;
                    return qty <= 0;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getInventorySummary(Long inventoryId) {
        Inventory inventory = getInventoryById(inventoryId);

        Map<String, Object> summary = new HashMap<>();
        summary.put("id", inventory.getId());
        summary.put("productId", inventory.getProductId());
        summary.put("warehouseId", inventory.getWarehouseId());
        summary.put("quantityOnHand", inventory.getQuantityOnHand());
        summary.put("quantityAvailable", inventory.getQuantityAvailable());
        summary.put("quantityReserved", inventory.getQuantityReserved());
        summary.put("quantityAllocated", inventory.getQuantityAllocated());
        summary.put("totalValue", inventory.getTotalValue());
        summary.put("stockStatus", inventory.getStockStatus());
        summary.put("reorderLevel", inventory.getReorderLevel());
        summary.put("maxStockLevel", inventory.getMaxStockLevel());

        return summary;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        List<Inventory> allInventory = inventoryRepository.findAll();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalItems", allInventory.size());
        stats.put("lowStockItems", getLowStockItems().size());
        stats.put("outOfStockItems", getOutOfStockItems().size());

        Double totalValue = allInventory.stream()
                .map(inv -> inv.getQuantityOnHand() * (inv.getAverageCost() != null ? inv.getAverageCost() : 0.0))
                .reduce(0.0, Double::sum);

        stats.put("totalInventoryValue", totalValue);

        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getInventoryStatistics() {
        return getDashboardStatistics();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> calculateInventoryMetrics(Long inventoryId) {
        return getInventorySummary(inventoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Inventory> searchInventory(String searchTerm, Pageable pageable) {
        List<Inventory> results = inventoryRepository.searchInventory(searchTerm);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), results.size());

        return new PageImpl<>(
                results.subList(start, end),
                pageable,
                results.size()
        );
    }

    // Convenience methods (aliases)

    @Override
    public void increaseStock(Long inventoryId, Double quantity) {
        Inventory inventory = getInventoryById(inventoryId);
        addStock(inventoryId, quantity, inventory.getAverageCost());
    }

    @Override
    public void decreaseStock(Long inventoryId, Double quantity) {
        removeStock(inventoryId, quantity);
    }

    @Override
    public void releaseReservedStock(Long inventoryId, Double quantity) {
        releaseReservation(inventoryId, quantity);
    }

    @Override
    public void deallocateStock(Long inventoryId, Double quantity) {
        releaseAllocation(inventoryId, quantity);
    }

    @Override
    public void markDamaged(Long inventoryId, Double quantity, String reason) {
        recordDamagedStock(inventoryId, quantity);
    }

    @Override
    public void markExpired(Long inventoryId, Double quantity, String reason) {
        recordExpiredStock(inventoryId, quantity);
    }

    @Override
    public void recordStockCount(Long inventoryId, Double actualQuantity, String notes) {
        adjustStock(inventoryId, actualQuantity, notes);
    }

    // Query by Status

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getActiveInventory() {
        return inventoryRepository.findAll().stream()
                .filter(inv -> "ACTIVE".equals(inv.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryWithStock() {
        return inventoryRepository.findAll().stream()
                .filter(inv -> {
                    Double qty = inv.getQuantityOnHand();
                    return qty != null && qty > 0;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryWithoutStock() {
        return inventoryRepository.findAll().stream()
                .filter(inv -> {
                    Double qty = inv.getQuantityOnHand();
                    return qty == null || qty <= 0;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getLowStockInventory() {
        return getLowStockItems();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getOutOfStockInventory() {
        return getOutOfStockItems();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getOverstockInventory() {
        return inventoryRepository.findAll().stream()
                .filter(inv -> {
                    Double qty = inv.getQuantityOnHand();
                    Integer max = inv.getMaxStockLevel();
                    return qty != null && max != null && qty >= max;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getOptimalStockInventory() {
        return inventoryRepository.findAll().stream()
                .filter(inv -> {
                    Double qty = inv.getQuantityOnHand();
                    Integer reorder = inv.getReorderLevel();
                    Integer max = inv.getMaxStockLevel();
                    return qty != null && reorder != null && max != null &&
                            qty > reorder && qty < max;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryBelowReorderLevel() {
        return getLowStockItems();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryAboveMaxStockLevel() {
        return getOverstockInventory();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryWithReservedStock() {
        return inventoryRepository.findAll().stream()
                .filter(inv -> {
                    Double reserved = inv.getQuantityReserved();
                    return reserved != null && reserved > 0;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryWithDamagedStock() {
        return inventoryRepository.findAll().stream()
                .filter(inv -> {
                    Double damaged = inv.getQuantityDamaged();
                    return damaged != null && damaged > 0;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryWithExpiredStock() {
        return inventoryRepository.findAll().stream()
                .filter(inv -> {
                    Double expired = inv.getQuantityExpired();
                    return expired != null && expired > 0;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getSlowMovingInventory(int days) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        return inventoryRepository.findAll().stream()
                .filter(inv -> {
                    LocalDateTime lastMovement = inv.getLastMovementDate();
                    return lastMovement == null || lastMovement.isBefore(cutoffDate);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getFastMovingInventory(int days) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        return inventoryRepository.findAll().stream()
                .filter(inv -> {
                    LocalDateTime lastMovement = inv.getLastMovementDate();
                    return lastMovement != null && lastMovement.isAfter(cutoffDate);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getWarehouseInventory(Long warehouseId) {
        return getInventoryByWarehouse(warehouseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getProductInventoryAcrossWarehouses(Long productId) {
        return getInventoryByProduct(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryRequiringStockCount(int daysSinceLastCount) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysSinceLastCount);
        return inventoryRepository.findAll().stream()
                .filter(inv -> {
                    LocalDateTime lastCount = inv.getLastStockCountDate();
                    return lastCount == null || lastCount.isBefore(cutoffDate);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getHighValueInventory(Double minValue) {
        return inventoryRepository.findAll().stream()
                .filter(inv -> {
                    Double value = inv.getTotalValue();
                    return value != null && value >= minValue;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getRecentInventoryUpdates(int days) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        return inventoryRepository.findAll().stream()
                .filter(inv -> {
                    LocalDateTime updated = inv.getLastUpdated();
                    return updated != null && updated.isAfter(cutoffDate);
                })
                .sorted((i1, i2) -> i2.getLastUpdated().compareTo(i1.getLastUpdated()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getTopValueInventoryItems(int limit) {
        return inventoryRepository.findAll().stream()
                .filter(inv -> inv.getTotalValue() != null)
                .sorted((i1, i2) -> Double.compare(
                        i2.getTotalValue() != null ? i2.getTotalValue() : 0.0,
                        i1.getTotalValue() != null ? i1.getTotalValue() : 0.0
                ))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getTopQuantityInventoryItems(int limit) {
        return inventoryRepository.findAll().stream()
                .filter(inv -> inv.getQuantityOnHand() != null)
                .sorted((i1, i2) -> Double.compare(
                        i2.getQuantityOnHand() != null ? i2.getQuantityOnHand() : 0.0,
                        i1.getQuantityOnHand() != null ? i1.getQuantityOnHand() : 0.0
                ))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Helper methods

    private void updateAverageCost(Inventory inventory, Double newQuantity, Double newCost) {
        if (newCost == null || newCost == 0.0) return;

        Double currentQty = inventory.getQuantityOnHand() != null ? inventory.getQuantityOnHand() : 0.0;
        Double currentAvgCost = inventory.getAverageCost() != null ? inventory.getAverageCost() : 0.0;

        Double totalCost = (currentQty * currentAvgCost) + (newQuantity * newCost);
        Double totalQty = currentQty + newQuantity;

        if (totalQty > 0) {
            Double newAvgCost = totalCost / totalQty;
            inventory.setAverageCost(newAvgCost);
        }
    }

    private Double calculateTotalValue(Inventory inventory) {
        Double qty = inventory.getQuantityOnHand() != null ? inventory.getQuantityOnHand() : 0.0;
        Double avgCost = inventory.getAverageCost() != null ? inventory.getAverageCost() : 0.0;
        return qty * avgCost;
    }

    private void updateStockStatus(Inventory inventory) {
        inventory.updateStockStatus();
        inventory.setTotalValue(calculateTotalValue(inventory));
    }

    private String determineStockStatus(Inventory inventory) {
        Double qty = inventory.getQuantityOnHand() != null ? inventory.getQuantityOnHand() : 0.0;
        Integer reorder = inventory.getReorderLevel() != null ? inventory.getReorderLevel() : 0;
        Integer max = inventory.getMaxStockLevel() != null ? inventory.getMaxStockLevel() : Integer.MAX_VALUE;

        if (qty <= 0) return "OUT_OF_STOCK";
        if (qty <= reorder) return "LOW_STOCK";
        if (qty >= max) return "OVERSTOCK";
        return "IN_STOCK";
    }
}
