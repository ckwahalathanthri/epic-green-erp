package lk.epicgreen.erp.warehouse.repository;

import lk.epicgreen.erp.warehouse.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Inventory Repository
 * Repository for inventory data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find inventory by product and warehouse
     */
    Optional<Inventory> findByProductIdAndWarehouseId(Long productId, Long warehouseId);
    
    /**
     * Check if inventory exists by product and warehouse
     */
    boolean existsByProductIdAndWarehouseId(Long productId, Long warehouseId);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find inventory by product ID
     */
    List<Inventory> findByProductId(Long productId);
    
    /**
     * Find inventory by product ID with pagination
     */
    Page<Inventory> findByProductId(Long productId, Pageable pageable);
    
    /**
     * Find inventory by warehouse ID
     */
    List<Inventory> findByWarehouseId(Long warehouseId);
    
    /**
     * Find inventory by warehouse ID with pagination
     */
    Page<Inventory> findByWarehouseId(Long warehouseId, Pageable pageable);
    
    /**
     * Find inventory by status
     */
    List<Inventory> findByStatus(String status);
    
    /**
     * Find inventory by stock status
     */
    List<Inventory> findByStockStatus(String stockStatus);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find inventory by last updated between dates
     */
    List<Inventory> findByLastUpdatedBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find inventory by last stock count date between dates
     */
    List<Inventory> findByLastStockCountDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find inventory by warehouse and status
     */
    List<Inventory> findByWarehouseIdAndStatus(Long warehouseId, String status);
    
    /**
     * Find inventory by warehouse and stock status
     */
    List<Inventory> findByWarehouseIdAndStockStatus(Long warehouseId, String stockStatus);
    
    /**
     * Find inventory by status and stock status
     */
    List<Inventory> findByStatusAndStockStatus(String status, String stockStatus);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search inventory
     */
    @Query("SELECT i FROM Inventory i WHERE " +
           "LOWER(i.productCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.warehouseName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Inventory> searchInventory(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find active inventory
     */
    @Query("SELECT i FROM Inventory i WHERE i.status = 'ACTIVE' " +
           "ORDER BY i.productName ASC")
    List<Inventory> findActiveInventory();
    
    /**
     * Find inventory with stock
     */
    @Query("SELECT i FROM Inventory i WHERE i.quantityOnHand > 0 " +
           "ORDER BY i.productName ASC")
    List<Inventory> findInventoryWithStock();
    
    /**
     * Find inventory without stock
     */
    @Query("SELECT i FROM Inventory i WHERE i.quantityOnHand = 0 " +
           "ORDER BY i.productName ASC")
    List<Inventory> findInventoryWithoutStock();
    
    /**
     * Find low stock inventory
     */
    @Query("SELECT i FROM Inventory i WHERE i.stockStatus = 'LOW' " +
           "OR (i.reorderLevel > 0 AND i.quantityOnHand <= i.reorderLevel) " +
           "ORDER BY (i.quantityOnHand / NULLIF(i.reorderLevel, 0)) ASC")
    List<Inventory> findLowStockInventory();
    
    /**
     * Find out of stock inventory
     */
    @Query("SELECT i FROM Inventory i WHERE i.stockStatus = 'OUT_OF_STOCK' " +
           "OR i.quantityOnHand = 0 ORDER BY i.lastUpdated DESC")
    List<Inventory> findOutOfStockInventory();
    
    /**
     * Find overstock inventory
     */
    @Query("SELECT i FROM Inventory i WHERE i.stockStatus = 'OVERSTOCK' " +
           "OR (i.maxStockLevel > 0 AND i.quantityOnHand > i.maxStockLevel) " +
           "ORDER BY (i.quantityOnHand / NULLIF(i.maxStockLevel, 0)) DESC")
    List<Inventory> findOverstockInventory();
    
    /**
     * Find optimal stock inventory
     */
    @Query("SELECT i FROM Inventory i WHERE i.stockStatus = 'OPTIMAL' " +
           "AND i.quantityOnHand > i.reorderLevel " +
           "AND (i.maxStockLevel = 0 OR i.quantityOnHand <= i.maxStockLevel) " +
           "ORDER BY i.productName ASC")
    List<Inventory> findOptimalStockInventory();
    
    /**
     * Find inventory below reorder level
     */
    @Query("SELECT i FROM Inventory i WHERE i.reorderLevel > 0 " +
           "AND i.quantityOnHand <= i.reorderLevel " +
           "ORDER BY (i.quantityOnHand - i.reorderLevel) ASC")
    List<Inventory> findInventoryBelowReorderLevel();
    
    /**
     * Find inventory above max stock level
     */
    @Query("SELECT i FROM Inventory i WHERE i.maxStockLevel > 0 " +
           "AND i.quantityOnHand > i.maxStockLevel " +
           "ORDER BY (i.quantityOnHand - i.maxStockLevel) DESC")
    List<Inventory> findInventoryAboveMaxStockLevel();
    
    /**
     * Find inventory with reserved stock
     */
    @Query("SELECT i FROM Inventory i WHERE i.quantityReserved > 0 " +
           "ORDER BY i.quantityReserved DESC")
    List<Inventory> findInventoryWithReservedStock();
    
    /**
     * Find inventory with allocated stock
     */
    @Query("SELECT i FROM Inventory i WHERE i.quantityAllocated > 0 " +
           "ORDER BY i.quantityAllocated DESC")
    List<Inventory> findInventoryWithAllocatedStock();
    
    /**
     * Find inventory with damaged stock
     */
    @Query("SELECT i FROM Inventory i WHERE i.quantityDamaged > 0 " +
           "ORDER BY i.quantityDamaged DESC")
    List<Inventory> findInventoryWithDamagedStock();
    
    /**
     * Find inventory with expired stock
     */
    @Query("SELECT i FROM Inventory i WHERE i.quantityExpired > 0 " +
           "ORDER BY i.quantityExpired DESC")
    List<Inventory> findInventoryWithExpiredStock();
    
    /**
     * Find available stock
     */
    @Query("SELECT i FROM Inventory i WHERE i.quantityAvailable > 0 " +
           "ORDER BY i.quantityAvailable DESC")
    List<Inventory> findInventoryWithAvailableStock();
    
    /**
     * Find inventory by value range
     */
    @Query("SELECT i FROM Inventory i WHERE i.totalValue BETWEEN :minValue AND :maxValue " +
           "ORDER BY i.totalValue DESC")
    List<Inventory> findByValueRange(@Param("minValue") Double minValue,
                                     @Param("maxValue") Double maxValue);
    
    /**
     * Find high value inventory
     */
    @Query("SELECT i FROM Inventory i WHERE i.totalValue > :threshold " +
           "ORDER BY i.totalValue DESC")
    List<Inventory> findHighValueInventory(@Param("threshold") Double threshold);
    
    /**
     * Find inventory requiring stock count
     */
    @Query("SELECT i FROM Inventory i WHERE " +
           "i.lastStockCountDate IS NULL OR " +
           "i.lastStockCountDate < :thresholdDate " +
           "ORDER BY i.lastStockCountDate ASC NULLS FIRST")
    List<Inventory> findInventoryRequiringStockCount(@Param("thresholdDate") LocalDate thresholdDate);
    
    /**
     * Find slow moving inventory
     */
    @Query("SELECT i FROM Inventory i WHERE " +
           "i.lastMovementDate < :thresholdDate OR i.lastMovementDate IS NULL " +
           "ORDER BY i.lastMovementDate ASC NULLS FIRST")
    List<Inventory> findSlowMovingInventory(@Param("thresholdDate") LocalDateTime thresholdDate);
    
    /**
     * Find fast moving inventory
     */
    @Query("SELECT i FROM Inventory i WHERE " +
           "i.lastMovementDate >= :thresholdDate " +
           "ORDER BY i.lastMovementDate DESC")
    List<Inventory> findFastMovingInventory(@Param("thresholdDate") LocalDateTime thresholdDate);
    
    /**
     * Find recent inventory updates
     */
    @Query("SELECT i FROM Inventory i ORDER BY i.lastUpdated DESC")
    List<Inventory> findRecentInventoryUpdates(Pageable pageable);
    
    /**
     * Find warehouse inventory summary
     */
    @Query("SELECT i FROM Inventory i WHERE i.warehouseId = :warehouseId " +
           "ORDER BY i.totalValue DESC")
    List<Inventory> findWarehouseInventorySummary(@Param("warehouseId") Long warehouseId);
    
    /**
     * Find product inventory across warehouses
     */
    @Query("SELECT i FROM Inventory i WHERE i.productId = :productId " +
           "ORDER BY i.quantityOnHand DESC")
    List<Inventory> findProductInventoryAcrossWarehouses(@Param("productId") Long productId);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count inventory by warehouse
     */
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.warehouseId = :warehouseId")
    Long countByWarehouseId(@Param("warehouseId") Long warehouseId);
    
    /**
     * Count inventory by product
     */
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.productId = :productId")
    Long countByProductId(@Param("productId") Long productId);
    
    /**
     * Count inventory by stock status
     */
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.stockStatus = :stockStatus")
    Long countByStockStatus(@Param("stockStatus") String stockStatus);
    
    /**
     * Count low stock items
     */
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.stockStatus = 'LOW' " +
           "OR (i.reorderLevel > 0 AND i.quantityOnHand <= i.reorderLevel)")
    Long countLowStockItems();
    
    /**
     * Count out of stock items
     */
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.stockStatus = 'OUT_OF_STOCK' " +
           "OR i.quantityOnHand = 0")
    Long countOutOfStockItems();
    
    /**
     * Count overstock items
     */
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.stockStatus = 'OVERSTOCK' " +
           "OR (i.maxStockLevel > 0 AND i.quantityOnHand > i.maxStockLevel)")
    Long countOverstockItems();
    
    /**
     * Get stock status distribution
     */
    @Query("SELECT i.stockStatus, COUNT(i) as itemCount FROM Inventory i " +
           "GROUP BY i.stockStatus ORDER BY itemCount DESC")
    List<Object[]> getStockStatusDistribution();
    
    /**
     * Get inventory by warehouse
     */
    @Query("SELECT i.warehouseId, i.warehouseName, COUNT(i) as itemCount, " +
           "SUM(i.quantityOnHand) as totalQuantity, SUM(i.totalValue) as totalValue " +
           "FROM Inventory i GROUP BY i.warehouseId, i.warehouseName " +
           "ORDER BY totalValue DESC")
    List<Object[]> getInventoryByWarehouse();
    
    /**
     * Get total quantity on hand
     */
    @Query("SELECT SUM(i.quantityOnHand) FROM Inventory i")
    Double getTotalQuantityOnHand();
    
    /**
     * Get total available quantity
     */
    @Query("SELECT SUM(i.quantityAvailable) FROM Inventory i")
    Double getTotalAvailableQuantity();
    
    /**
     * Get total reserved quantity
     */
    @Query("SELECT SUM(i.quantityReserved) FROM Inventory i")
    Double getTotalReservedQuantity();
    
    /**
     * Get total allocated quantity
     */
    @Query("SELECT SUM(i.quantityAllocated) FROM Inventory i")
    Double getTotalAllocatedQuantity();
    
    /**
     * Get total inventory value
     */
    @Query("SELECT SUM(i.totalValue) FROM Inventory i")
    Double getTotalInventoryValue();
    
    /**
     * Get inventory value by warehouse
     */
    @Query("SELECT SUM(i.totalValue) FROM Inventory i WHERE i.warehouseId = :warehouseId")
    Double getInventoryValueByWarehouse(@Param("warehouseId") Long warehouseId);
    
    /**
     * Get inventory turnover data
     */
    @Query("SELECT i.productId, i.productName, i.quantityOnHand, i.averageCost, " +
           "i.lastMovementDate FROM Inventory i WHERE i.quantityOnHand > 0 " +
           "ORDER BY i.lastMovementDate ASC NULLS LAST")
    List<Object[]> getInventoryTurnoverData();
    
    /**
     * Get top value inventory items
     */
    @Query("SELECT i FROM Inventory i WHERE i.totalValue > 0 " +
           "ORDER BY i.totalValue DESC")
    List<Inventory> getTopValueInventoryItems(Pageable pageable);
    
    /**
     * Get top quantity inventory items
     */
    @Query("SELECT i FROM Inventory i WHERE i.quantityOnHand > 0 " +
           "ORDER BY i.quantityOnHand DESC")
    List<Inventory> getTopQuantityInventoryItems(Pageable pageable);
    
    /**
     * Find inventory by tags
     */
    @Query("SELECT i FROM Inventory i WHERE i.tags LIKE CONCAT('%', :tag, '%')")
    List<Inventory> findByTag(@Param("tag") String tag);
}
