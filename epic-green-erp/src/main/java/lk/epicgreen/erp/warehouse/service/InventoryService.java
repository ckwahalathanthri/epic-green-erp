package lk.epicgreen.erp.warehouse.service;

import lk.epicgreen.erp.warehouse.dto.InventoryRequest;
import lk.epicgreen.erp.warehouse.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Inventory Service Interface - Complete
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface InventoryService {
    
    // CRUD Operations
    Inventory createInventory(InventoryRequest request);
    Inventory updateInventory(Long id, InventoryRequest request);
    void deleteInventory(Long id);
    Inventory getInventoryById(Long id);
    Inventory getInventoryByProductAndWarehouse(Long productId, Long warehouseId);
    
    // List Operations
    List<Inventory> getAllInventory();
    Page<Inventory> getAllInventory(Pageable pageable);
    List<Inventory> getInventoryByProduct(Long productId);
    List<Inventory> getInventoryByWarehouse(Long warehouseId);
    
    // Stock Movement Operations
    void addStock(Long inventoryId, Double quantity, Double cost);
    void removeStock(Long inventoryId, Double quantity);
    void adjustStock(Long inventoryId, Double newQuantity, String reason);
    void transferStock(Long fromInventoryId, Long toInventoryId, Double quantity);
    
    // Reservation Operations
    void reserveStock(Long inventoryId, Double quantity);
    void releaseReservation(Long inventoryId, Double quantity);
    
    // Allocation Operations
    void allocateStock(Long inventoryId, Double quantity);
    void releaseAllocation(Long inventoryId, Double quantity);
    
    // Quality Control Operations
    void recordDamagedStock(Long inventoryId, Double quantity);
    void recordExpiredStock(Long inventoryId, Double quantity);
    
    // Stock Level Management
    void updateStockLevels(Long inventoryId, Integer reorderLevel, Integer maxLevel, Integer minLevel);
    
    // Query Operations
    Double getAvailableQuantity(Long productId, Long warehouseId);
    boolean isStockAvailable(Long productId, Long warehouseId, Double requiredQuantity);
    List<Inventory> getLowStockItems();
    List<Inventory> getOutOfStockItems();
    
    // Reporting
    Map<String, Object> getInventorySummary(Long inventoryId);
    Map<String, Object> getDashboardStatistics();
    Map<String, Object> getInventoryStatistics();
    Map<String, Object> calculateInventoryMetrics(Long inventoryId);
    
    // Search
    Page<Inventory> searchInventory(String searchTerm, Pageable pageable);
    
    // Convenience Methods (aliases)
    void increaseStock(Long inventoryId, Double quantity);
    void decreaseStock(Long inventoryId, Double quantity);
    void releaseReservedStock(Long inventoryId, Double quantity);
    void deallocateStock(Long inventoryId, Double quantity);
    void markDamaged(Long inventoryId, Double quantity, String reason);
    void markExpired(Long inventoryId, Double quantity, String reason);
    void recordStockCount(Long inventoryId, Double actualQuantity, String notes);
    
    // Query by Status
    List<Inventory> getActiveInventory();
    List<Inventory> getInventoryWithStock();
    List<Inventory> getInventoryWithoutStock();
    List<Inventory> getLowStockInventory();
    List<Inventory> getOutOfStockInventory();
    List<Inventory> getOverstockInventory();
    List<Inventory> getOptimalStockInventory();
    List<Inventory> getInventoryBelowReorderLevel();
    List<Inventory> getInventoryAboveMaxStockLevel();
    List<Inventory> getInventoryWithReservedStock();
    List<Inventory> getInventoryWithDamagedStock();
    List<Inventory> getInventoryWithExpiredStock();
    
    // Analytics
    List<Inventory> getSlowMovingInventory(int days);
    List<Inventory> getFastMovingInventory(int days);
    List<Inventory> getWarehouseInventory(Long warehouseId);
    List<Inventory> getProductInventoryAcrossWarehouses(Long productId);
    List<Inventory> getInventoryRequiringStockCount(int daysSinceLastCount);
    List<Inventory> getHighValueInventory(Double minValue);
    List<Inventory> getRecentInventoryUpdates(int days);
    List<Inventory> getTopValueInventoryItems(int limit);
    List<Inventory> getTopQuantityInventoryItems(int limit);
}
