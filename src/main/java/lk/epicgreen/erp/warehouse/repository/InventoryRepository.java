package lk.epicgreen.erp.warehouse.repository;

import lk.epicgreen.erp.warehouse.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Inventory entity
 * Based on ACTUAL database schema: inventory table
 * 
 * Fields: warehouse_id (BIGINT), product_id (BIGINT), location_id (BIGINT),
 *         batch_number, manufacturing_date, expiry_date,
 *         quantity_available, quantity_reserved, quantity_ordered,
 *         unit_cost, last_stock_date
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>, JpaSpecificationExecutor<Inventory> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find inventory by warehouse, product, batch, and location
     */
    Optional<Inventory> findByWarehouseIdAndProductIdAndBatchNumberAndLocationId(
            Long warehouseId, Long productId, String batchNumber, Long locationId);
    
    /**
     * Find all inventory for a warehouse
     */
    List<Inventory> findByWarehouseId(Long warehouseId);
    
    /**
     * Find all inventory for a warehouse with pagination
     */
    Page<Inventory> findByWarehouseId(Long warehouseId, Pageable pageable);
    
    /**
     * Find all inventory for a product
     */
    List<Inventory> findByProductId(Long productId);
    
    /**
     * Find all inventory for a product with pagination
     */
    Page<Inventory> findByProductId(Long productId, Pageable pageable);
    
    /**
     * Find inventory for a product in a specific warehouse
     */
    List<Inventory> findByWarehouseIdAndProductId(Long warehouseId, Long productId);
    
    /**
     * Find inventory by batch number
     */
    List<Inventory> findByBatchNumber(String batchNumber);
    
    /**
     * Find inventory by batch number in a warehouse
     */
    List<Inventory> findByWarehouseIdAndBatchNumber(Long warehouseId, String batchNumber);
    
    /**
     * Find inventory at a specific location
     */
    List<Inventory> findByLocationId(Long locationId);
    
    /**
     * Find inventory with available quantity greater than zero
     */
    @Query("SELECT i FROM Inventory i WHERE i.quantityAvailable > 0")
    List<Inventory> findInventoryWithStock();
    
    /**
     * Find inventory with available quantity greater than zero for a warehouse
     */
    @Query("SELECT i FROM Inventory i WHERE i.warehouseId = :warehouseId AND i.quantityAvailable > 0")
    List<Inventory> findInventoryWithStockByWarehouse(@Param("warehouseId") Long warehouseId);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if inventory exists for warehouse, product, batch, and location
     */
    boolean existsByWarehouseIdAndProductIdAndBatchNumberAndLocationId(
            Long warehouseId, Long productId, String batchNumber, Long locationId);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search inventory by batch number containing
     */
    Page<Inventory> findByBatchNumberContainingIgnoreCase(String batchNumber, Pageable pageable);
    
    /**
     * Search inventory by multiple criteria
     */
    @Query("SELECT i FROM Inventory i WHERE " +
           "(:warehouseId IS NULL OR i.warehouseId = :warehouseId) AND " +
           "(:productId IS NULL OR i.productId = :productId) AND " +
           "(:locationId IS NULL OR i.locationId = :locationId) AND " +
           "(:batchNumber IS NULL OR LOWER(i.batchNumber) LIKE LOWER(CONCAT('%', :batchNumber, '%')))")
    Page<Inventory> searchInventory(
            @Param("warehouseId") Long warehouseId,
            @Param("productId") Long productId,
            @Param("locationId") Long locationId,
            @Param("batchNumber") String batchNumber,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count inventory records for a warehouse
     */
    long countByWarehouseId(Long warehouseId);
    
    /**
     * Count inventory records for a product
     */
    long countByProductId(Long productId);
    
    /**
     * Count inventory records at a location
     */
    long countByLocationId(Long locationId);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Get total available quantity for a product across all warehouses
     */
    @Query("SELECT SUM(i.quantityAvailable) FROM Inventory i WHERE i.productId = :productId")
    BigDecimal getTotalAvailableQuantityByProduct(@Param("productId") Long productId);
    
    /**
     * Get total available quantity for a product in a specific warehouse
     */
    @Query("SELECT SUM(i.quantityAvailable) FROM Inventory i " +
           "WHERE i.warehouseId = :warehouseId AND i.productId = :productId")
    BigDecimal getTotalAvailableQuantityByWarehouseAndProduct(
            @Param("warehouseId") Long warehouseId,
            @Param("productId") Long productId);
    
    /**
     * Get total reserved quantity for a product
     */
    @Query("SELECT SUM(i.quantityReserved) FROM Inventory i WHERE i.productId = :productId")
    BigDecimal getTotalReservedQuantityByProduct(@Param("productId") Long productId);
    
    /**
     * Get total ordered quantity for a product
     */
    @Query("SELECT SUM(i.quantityOrdered) FROM Inventory i WHERE i.productId = :productId")
    BigDecimal getTotalOrderedQuantityByProduct(@Param("productId") Long productId);
    
    /**
     * Find inventory expiring before a specific date
     */
    @Query("SELECT i FROM Inventory i WHERE i.expiryDate < :date AND i.quantityAvailable > 0")
    List<Inventory> findInventoryExpiringBefore(@Param("date") LocalDate date);
    
    /**
     * Find inventory expiring in next X days
     */
    @Query("SELECT i FROM Inventory i WHERE i.expiryDate BETWEEN :today AND :futureDate " +
           "AND i.quantityAvailable > 0 ORDER BY i.expiryDate")
    List<Inventory> findInventoryExpiringInDays(
            @Param("today") LocalDate today,
            @Param("futureDate") LocalDate futureDate);
    
    /**
     * Find expired inventory
     */
    @Query("SELECT i FROM Inventory i WHERE i.expiryDate < CURRENT_DATE AND i.quantityAvailable > 0")
    List<Inventory> findExpiredInventory();
    
    /**
     * Find inventory by manufacturing date range
     */
    @Query("SELECT i FROM Inventory i WHERE i.manufacturingDate BETWEEN :startDate AND :endDate")
    List<Inventory> findByManufacturingDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find inventory with low stock (available < reserved + ordered)
     */
    @Query("SELECT i FROM Inventory i WHERE i.quantityAvailable < (i.quantityReserved + i.quantityOrdered)")
    List<Inventory> findLowStockInventory();
    
    /**
     * Find inventory with no available stock
     */
    @Query("SELECT i FROM Inventory i WHERE i.quantityAvailable = 0")
    List<Inventory> findOutOfStockInventory();
    
    /**
     * Find inventory with reserved quantity
     */
    @Query("SELECT i FROM Inventory i WHERE i.quantityReserved > 0")
    List<Inventory> findInventoryWithReservations();
    
    /**
     * Get inventory value by warehouse
     */
    @Query("SELECT SUM(i.quantityAvailable * i.unitCost) FROM Inventory i WHERE i.warehouseId = :warehouseId")
    BigDecimal getInventoryValueByWarehouse(@Param("warehouseId") Long warehouseId);
    
    /**
     * Get total inventory value
     */
    @Query("SELECT SUM(i.quantityAvailable * i.unitCost) FROM Inventory i")
    BigDecimal getTotalInventoryValue();
    
    /**
     * Get inventory statistics by warehouse
     */
    @Query("SELECT " +
           "COUNT(DISTINCT i.productId) as uniqueProducts, " +
           "SUM(i.quantityAvailable) as totalQuantity, " +
           "SUM(i.quantityReserved) as totalReserved, " +
           "SUM(i.quantityOrdered) as totalOrdered, " +
           "SUM(i.quantityAvailable * i.unitCost) as totalValue " +
           "FROM Inventory i WHERE i.warehouseId = :warehouseId")
    Object getInventoryStatisticsByWarehouse(@Param("warehouseId") Long warehouseId);
    
    /**
     * Get inventory statistics by product
     */
    @Query("SELECT " +
           "COUNT(DISTINCT i.warehouseId) as warehouseCount, " +
           "SUM(i.quantityAvailable) as totalAvailable, " +
           "SUM(i.quantityReserved) as totalReserved, " +
           "AVG(i.unitCost) as avgUnitCost " +
           "FROM Inventory i WHERE i.productId = :productId")
    Object getInventoryStatisticsByProduct(@Param("productId") Long productId);
    
    /**
     * Find inventory by warehouse and expiry date range
     */
    @Query("SELECT i FROM Inventory i WHERE i.warehouseId = :warehouseId " +
           "AND i.expiryDate BETWEEN :startDate AND :endDate ORDER BY i.expiryDate")
    List<Inventory> findByWarehouseAndExpiryDateBetween(
            @Param("warehouseId") Long warehouseId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Get products with inventory in multiple warehouses
     */
    @Query("SELECT i.productId, COUNT(DISTINCT i.warehouseId) as warehouseCount " +
           "FROM Inventory i WHERE i.quantityAvailable > 0 " +
           "GROUP BY i.productId HAVING COUNT(DISTINCT i.warehouseId) > 1")
    List<Object[]> findProductsInMultipleWarehouses();
    
    /**
     * Find inventory updated after specific date
     */
    @Query("SELECT i FROM Inventory i WHERE i.updatedAt >= :date")
    List<Inventory> findRecentlyUpdatedInventory(@Param("date") java.time.LocalDateTime date);
    
    /**
     * Find all inventory ordered by warehouse and product
     */
    @Query("SELECT i FROM Inventory i ORDER BY i.warehouseId, i.productId")
    List<Inventory> findAllOrderedByWarehouseAndProduct();
}
