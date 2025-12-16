package lk.epicgreen.erp.warehouse.service;

import lk.epicgreen.erp.warehouse.dto.InventoryRequest;
import lk.epicgreen.erp.warehouse.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Inventory Service Interface
 * Service for inventory operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface InventoryService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    Inventory createInventory(InventoryRequest request);
    Inventory updateInventory(Long id, InventoryRequest request);
    void deleteInventory(Long id);
    Inventory getInventoryById(Long id);
    Inventory getInventoryByProductAndWarehouse(Long productId, Long warehouseId);
    List<Inventory> getAllInventory();
    Page<Inventory> getAllInventory(Pageable pageable);
    Page<Inventory> searchInventory(String keyword, Pageable pageable);
    
    // ===================================================================
    // STOCK OPERATIONS
    // ===================================================================
    
    void increaseStock(Long inventoryId, Double quantity);
    void decreaseStock(Long inventoryId, Double quantity);
    void adjustStock(Long inventoryId, Double newQuantity, String reason);
    void reserveStock(Long inventoryId, Double quantity);
    void releaseReservedStock(Long inventoryId, Double quantity);
    void allocateStock(Long inventoryId, Double quantity);
    void deallocateStock(Long inventoryId, Double quantity);
    void markDamaged(Long inventoryId, Double quantity, String reason);
    void markExpired(Long inventoryId, Double quantity, String reason);
    void updateAvailableQuantity(Long inventoryId);
    
    // ===================================================================
    // STOCK COUNT OPERATIONS
    // ===================================================================
    
    void recordStockCount(Long inventoryId, Double countedQuantity, String countedBy);
    void updateLastStockCountDate(Long inventoryId, LocalDate countDate);
    List<Inventory> getInventoryRequiringStockCount(int daysThreshold);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
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
    List<Inventory> getInventoryWithAllocatedStock();
    List<Inventory> getInventoryWithDamagedStock();
    List<Inventory> getInventoryWithExpiredStock();
    List<Inventory> getInventoryWithAvailableStock();
    List<Inventory> getInventoryByValueRange(Double minValue, Double maxValue);
    List<Inventory> getHighValueInventory(Double threshold);
    List<Inventory> getSlowMovingInventory(int daysThreshold);
    List<Inventory> getFastMovingInventory(int daysThreshold);
    List<Inventory> getWarehouseInventory(Long warehouseId);
    List<Inventory> getProductInventoryAcrossWarehouses(Long productId);
    List<Inventory> getRecentInventoryUpdates(int limit);
    
    // ===================================================================
    // STOCK STATUS OPERATIONS
    // ===================================================================
    
    void updateStockStatus(Long inventoryId);
    void recalculateAllStockStatuses();
    String determineStockStatus(Inventory inventory);
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    boolean validateInventory(Inventory inventory);
    boolean hasAvailableStock(Long inventoryId, Double requiredQuantity);
    boolean canReserveStock(Long inventoryId, Double quantity);
    boolean canAllocateStock(Long inventoryId, Double quantity);
    
    // ===================================================================
    // CALCULATIONS
    // ===================================================================
    
    Double calculateAvailableQuantity(Inventory inventory);
    Double calculateTotalValue(Inventory inventory);
    void recalculateInventoryValues(Long inventoryId);
    Map<String, Object> calculateInventoryMetrics(Long inventoryId);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<Inventory> createBulkInventory(List<InventoryRequest> requests);
    int adjustBulkStock(List<Long> inventoryIds, String reason);
    int deleteInventoryBulk(List<Long> inventoryIds);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    Map<String, Object> getInventoryStatistics();
    List<Map<String, Object>> getStockStatusDistribution();
    List<Map<String, Object>> getInventoryByWarehouse();
    List<Map<String, Object>> getInventoryTurnoverData();
    List<Inventory> getTopValueInventoryItems(int limit);
    List<Inventory> getTopQuantityInventoryItems(int limit);
    Double getTotalQuantityOnHand();
    Double getTotalAvailableQuantity();
    Double getTotalReservedQuantity();
    Double getTotalAllocatedQuantity();
    Double getTotalInventoryValue();
    Double getInventoryValueByWarehouse(Long warehouseId);
    Map<String, Object> getDashboardStatistics();
}
