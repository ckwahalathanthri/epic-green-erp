package lk.epicgreen.erp.warehouse.repository;

import lk.epicgreen.erp.warehouse.entity.StockMovement;
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

/**
 * Repository interface for StockMovement entity
 * Based on ACTUAL database schema: stock_movements table
 * 
 * Fields: movement_date, movement_type (ENUM: RECEIPT, ISSUE, TRANSFER, ADJUSTMENT, PRODUCTION, SALES, RETURN),
 *         warehouse_id (BIGINT), product_id (BIGINT), from_location_id (BIGINT), to_location_id (BIGINT),
 *         batch_number, quantity, uom_id (BIGINT), unit_cost,
 *         reference_type, reference_id (BIGINT), reference_number, remarks
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long>, JpaSpecificationExecutor<StockMovement> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find all movements for a warehouse
     */
    List<StockMovement> findByWarehouseId(Long warehouseId);
    
    /**
     * Find all movements for a warehouse with pagination
     */
    Page<StockMovement> findByWarehouseId(Long warehouseId, Pageable pageable);
    
    /**
     * Find all movements for a product
     */
    List<StockMovement> findByProductId(Long productId);
    
    /**
     * Find all movements for a product with pagination
     */
    Page<StockMovement> findByProductId(Long productId, Pageable pageable);
    
    /**
     * Find movements for a product in a warehouse
     */
    List<StockMovement> findByWarehouseIdAndProductId(Long warehouseId, Long productId);
    
    /**
     * Find movements by movement type
     */
    List<StockMovement> findByMovementType(String movementType);
    
    /**
     * Find movements by movement type with pagination
     */
    Page<StockMovement> findByMovementType(String movementType, Pageable pageable);
    
    /**
     * Find movements by movement date
     */
    List<StockMovement> findByMovementDate(LocalDate movementDate);
    
    /**
     * Find movements by movement date range
     */
    List<StockMovement> findByMovementDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find movements by movement date range with pagination
     */
    Page<StockMovement> findByMovementDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find movements by batch number
     */
    List<StockMovement> findByBatchNumber(String batchNumber);
    
    /**
     * Find movements by reference
     */
    List<StockMovement> findByReferenceTypeAndReferenceId(String referenceType, Long referenceId);
    
    /**
     * Find movements by reference number
     */
    List<StockMovement> findByReferenceNumber(String referenceNumber);
    
    /**
     * Find movements from a specific location
     */
    List<StockMovement> findByFromLocationId(Long fromLocationId);
    
    /**
     * Find movements to a specific location
     */
    List<StockMovement> findByToLocationId(Long toLocationId);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search movements by multiple criteria
     */
    @Query("SELECT sm FROM StockMovement sm WHERE " +
           "(:warehouseId IS NULL OR sm.warehouse.id = :warehouseId) AND " +
           "(:productId IS NULL OR sm.product.id = :productId) AND " +
           "(:movementType IS NULL OR sm.movementType = :movementType) AND " +
           "(:startDate IS NULL OR sm.movementDate >= :startDate) AND " +
           "(:endDate IS NULL OR sm.movementDate <= :endDate) AND " +
           "(:batchNumber IS NULL OR sm.batchNumber = :batchNumber)")
    Page<StockMovement> searchMovements(
            @Param("warehouseId") Long warehouseId,
            @Param("productId") Long productId,
            @Param("movementType") String movementType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("batchNumber") String batchNumber,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count movements for a warehouse
     */
    long countByWarehouseId(Long warehouseId);
    
    /**
     * Count movements for a product
     */
    long countByProductId(Long productId);
    
    /**
     * Count movements by type
     */
    long countByMovementType(String movementType);
    
    /**
     * Count movements in date range
     */
    long countByMovementDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find receipt movements
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementType = 'RECEIPT' " +
           "ORDER BY sm.movementDate DESC, sm.createdAt DESC")
    List<StockMovement> findReceiptMovements();
    
    /**
     * Find issue movements
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementType = 'ISSUE' " +
           "ORDER BY sm.movementDate DESC, sm.createdAt DESC")
    List<StockMovement> findIssueMovements();
    
    /**
     * Find transfer movements
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementType = 'TRANSFER' " +
           "ORDER BY sm.movementDate DESC, sm.createdAt DESC")
    List<StockMovement> findTransferMovements();
    
    /**
     * Find adjustment movements
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementType = 'ADJUSTMENT' " +
           "ORDER BY sm.movementDate DESC, sm.createdAt DESC")
    List<StockMovement> findAdjustmentMovements();
    
    /**
     * Find production movements
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementType = 'PRODUCTION' " +
           "ORDER BY sm.movementDate DESC, sm.createdAt DESC")
    List<StockMovement> findProductionMovements();
    
    /**
     * Find sales movements
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementType = 'SALES' " +
           "ORDER BY sm.movementDate DESC, sm.createdAt DESC")
    List<StockMovement> findSalesMovements();
    
    /**
     * Find return movements
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementType = 'RETURN' " +
           "ORDER BY sm.movementDate DESC, sm.createdAt DESC")
    List<StockMovement> findReturnMovements();
    
    /**
     * Get total quantity moved for a product
     */
    @Query("SELECT SUM(sm.quantity) FROM StockMovement sm WHERE sm.product.id = :productId")
    BigDecimal getTotalQuantityMovedByProduct(@Param("productId") Long productId);
    
    /**
     * Get total quantity received for a product
     */
    @Query("SELECT SUM(sm.quantity) FROM StockMovement sm " +
           "WHERE sm.product.id = :productId AND sm.movementType = 'RECEIPT'")
    BigDecimal getTotalQuantityReceivedByProduct(@Param("productId") Long productId);
    
    /**
     * Get total quantity issued for a product
     */
    @Query("SELECT SUM(sm.quantity) FROM StockMovement sm " +
           "WHERE sm.product.id = :productId AND sm.movementType = 'ISSUE'")
    BigDecimal getTotalQuantityIssuedByProduct(@Param("productId") Long productId);
    
    /**
     * Get movement statistics by warehouse
     */
    @Query("SELECT sm.movementType, COUNT(sm) as movementCount, SUM(sm.quantity) as totalQuantity " +
           "FROM StockMovement sm WHERE sm.warehouse.id = :warehouseId " +
           "GROUP BY sm.movementType ORDER BY movementCount DESC")
    List<Object[]> getMovementStatisticsByWarehouse(@Param("warehouseId") Long warehouseId);
    
    /**
     * Get movement statistics by product
     */
    @Query("SELECT sm.movementType, COUNT(sm) as movementCount, SUM(sm.quantity) as totalQuantity " +
           "FROM StockMovement sm WHERE sm.product.id = :productId " +
           "GROUP BY sm.movementType ORDER BY movementCount DESC")
    List<Object[]> getMovementStatisticsByProduct(@Param("productId") Long productId);
    
    /**
     * Get daily movement summary
     */
    @Query("SELECT sm.movementDate, sm.movementType, COUNT(sm) as movementCount, SUM(sm.quantity) as totalQuantity " +
           "FROM StockMovement sm WHERE sm.movementDate BETWEEN :startDate AND :endDate " +
           "GROUP BY sm.movementDate, sm.movementType ORDER BY sm.movementDate DESC")
    List<Object[]> getDailyMovementSummary(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find movements by warehouse and type
     */
    List<StockMovement> findByWarehouseIdAndMovementType(Long warehouseId, String movementType);
    
    /**
     * Find movements by warehouse, type, and date range
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.warehouse.id = :warehouseId " +
           "AND sm.movementType = :movementType AND sm.movementDate BETWEEN :startDate AND :endDate " +
           "ORDER BY sm.movementDate DESC")
    List<StockMovement> findByWarehouseTypeAndDateRange(
            @Param("warehouseId") Long warehouseId,
            @Param("movementType") String movementType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find recent movements (last N days)
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementDate >= :startDate " +
           "ORDER BY sm.movementDate DESC, sm.createdAt DESC")
    List<StockMovement> findRecentMovements(@Param("startDate") LocalDate startDate, Pageable pageable);
    
    /**
     * Find movements for today
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementDate = CURRENT_DATE " +
           "ORDER BY sm.createdAt DESC")
    List<StockMovement> findTodayMovements();
    
    /**
     * Get movement value by warehouse and date range
     */
    @Query("SELECT SUM(sm.quantity * sm.unitCost) FROM StockMovement sm " +
           "WHERE sm.warehouse.id = :warehouseId AND sm.movementDate BETWEEN :startDate AND :endDate")
    BigDecimal getMovementValueByWarehouseAndDateRange(
            @Param("warehouseId") Long warehouseId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find movements by created by user
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.createdBy = :userId " +
           "ORDER BY sm.createdAt DESC")
    List<StockMovement> findMovementsByCreatedBy(@Param("userId") Long userId);
    
    /**
     * Find all movements ordered by date
     */
    List<StockMovement> findAllByOrderByMovementDateDescCreatedAtDesc();
}
