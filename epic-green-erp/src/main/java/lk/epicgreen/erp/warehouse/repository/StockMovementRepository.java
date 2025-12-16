package lk.epicgreen.erp.warehouse.repository;

import lk.epicgreen.erp.warehouse.entity.StockMovement;
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
 * StockMovement Repository
 * Repository for stock movement data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find stock movement by movement number
     */
    Optional<StockMovement> findByMovementNumber(String movementNumber);
    
    /**
     * Check if stock movement exists by movement number
     */
    boolean existsByMovementNumber(String movementNumber);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find stock movements by product ID
     */
    List<StockMovement> findByProductId(Long productId);
    
    /**
     * Find stock movements by product ID with pagination
     */
    Page<StockMovement> findByProductId(Long productId, Pageable pageable);
    
    /**
     * Find stock movements by warehouse ID
     */
    List<StockMovement> findByWarehouseId(Long warehouseId);
    
    /**
     * Find stock movements by warehouse ID with pagination
     */
    Page<StockMovement> findByWarehouseId(Long warehouseId, Pageable pageable);
    
    /**
     * Find stock movements by from warehouse
     */
    List<StockMovement> findByFromWarehouseId(Long fromWarehouseId);
    
    /**
     * Find stock movements by to warehouse
     */
    List<StockMovement> findByToWarehouseId(Long toWarehouseId);
    
    /**
     * Find stock movements by movement type
     */
    List<StockMovement> findByMovementType(String movementType);
    
    /**
     * Find stock movements by movement type with pagination
     */
    Page<StockMovement> findByMovementType(String movementType, Pageable pageable);
    
    /**
     * Find stock movements by status
     */
    List<StockMovement> findByStatus(String status);
    
    /**
     * Find stock movements by transaction type
     */
    List<StockMovement> findByTransactionType(String transactionType);
    
    /**
     * Find stock movements by reference ID
     */
    List<StockMovement> findByReferenceId(Long referenceId);
    
    /**
     * Find stock movements by reference type
     */
    List<StockMovement> findByReferenceType(String referenceType);
    
    /**
     * Find stock movements by created by user
     */
    List<StockMovement> findByCreatedByUserId(Long userId);
    
    /**
     * Find stock movements by approved by user
     */
    List<StockMovement> findByApprovedByUserId(Long userId);
    
    /**
     * Find stock movements by is approved
     */
    List<StockMovement> findByIsApproved(Boolean isApproved);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find stock movements by movement date between dates
     */
    List<StockMovement> findByMovementDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find stock movements by movement date between dates with pagination
     */
    Page<StockMovement> findByMovementDateBetween(LocalDate startDate, LocalDate endDate, 
                                                   Pageable pageable);
    
    /**
     * Find stock movements by created at between dates
     */
    List<StockMovement> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find stock movements by product and warehouse
     */
    List<StockMovement> findByProductIdAndWarehouseId(Long productId, Long warehouseId);
    
    /**
     * Find stock movements by product and movement type
     */
    List<StockMovement> findByProductIdAndMovementType(Long productId, String movementType);
    
    /**
     * Find stock movements by warehouse and movement type
     */
    List<StockMovement> findByWarehouseIdAndMovementType(Long warehouseId, String movementType);
    
    /**
     * Find stock movements by movement type and status
     */
    List<StockMovement> findByMovementTypeAndStatus(String movementType, String status);
    
    /**
     * Find stock movements by reference type and reference ID
     */
    List<StockMovement> findByReferenceTypeAndReferenceId(String referenceType, Long referenceId);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search stock movements
     */
    @Query("SELECT sm FROM StockMovement sm WHERE " +
           "LOWER(sm.movementNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(sm.productCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(sm.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(sm.notes) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<StockMovement> searchStockMovements(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find stock in movements
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementType = 'IN' " +
           "ORDER BY sm.movementDate DESC")
    List<StockMovement> findStockInMovements();
    
    /**
     * Find stock out movements
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementType = 'OUT' " +
           "ORDER BY sm.movementDate DESC")
    List<StockMovement> findStockOutMovements();
    
    /**
     * Find transfer movements
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementType = 'TRANSFER' " +
           "ORDER BY sm.movementDate DESC")
    List<StockMovement> findTransferMovements();
    
    /**
     * Find adjustment movements
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementType = 'ADJUSTMENT' " +
           "ORDER BY sm.movementDate DESC")
    List<StockMovement> findAdjustmentMovements();
    
    /**
     * Find return movements
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementType = 'RETURN' " +
           "ORDER BY sm.movementDate DESC")
    List<StockMovement> findReturnMovements();
    
    /**
     * Find damage movements
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementType = 'DAMAGE' " +
           "ORDER BY sm.movementDate DESC")
    List<StockMovement> findDamageMovements();
    
    /**
     * Find pending movements
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.status = 'PENDING' " +
           "ORDER BY sm.createdAt ASC")
    List<StockMovement> findPendingMovements();
    
    /**
     * Find approved movements
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.status = 'APPROVED' " +
           "AND sm.isApproved = true ORDER BY sm.approvedDate DESC")
    List<StockMovement> findApprovedMovements();
    
    /**
     * Find completed movements
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.status = 'COMPLETED' " +
           "ORDER BY sm.movementDate DESC")
    List<StockMovement> findCompletedMovements();
    
    /**
     * Find movements pending approval
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.isApproved = false " +
           "AND sm.status NOT IN ('CANCELLED') ORDER BY sm.createdAt ASC")
    List<StockMovement> findMovementsPendingApproval();
    
    /**
     * Find today's movements
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementDate = :today " +
           "ORDER BY sm.createdAt DESC")
    List<StockMovement> findTodaysMovements(@Param("today") LocalDate today);
    
    /**
     * Find recent movements
     */
    @Query("SELECT sm FROM StockMovement sm ORDER BY sm.movementDate DESC, sm.createdAt DESC")
    List<StockMovement> findRecentMovements(Pageable pageable);
    
    /**
     * Find product recent movements
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.productId = :productId " +
           "ORDER BY sm.movementDate DESC, sm.createdAt DESC")
    List<StockMovement> findProductRecentMovements(@Param("productId") Long productId,
                                                    Pageable pageable);
    
    /**
     * Find warehouse recent movements
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.warehouseId = :warehouseId " +
           "ORDER BY sm.movementDate DESC, sm.createdAt DESC")
    List<StockMovement> findWarehouseRecentMovements(@Param("warehouseId") Long warehouseId,
                                                      Pageable pageable);
    
    /**
     * Find transfers between warehouses
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementType = 'TRANSFER' " +
           "AND sm.fromWarehouseId = :fromWarehouseId " +
           "AND sm.toWarehouseId = :toWarehouseId " +
           "ORDER BY sm.movementDate DESC")
    List<StockMovement> findTransfersBetweenWarehouses(@Param("fromWarehouseId") Long fromWarehouseId,
                                                        @Param("toWarehouseId") Long toWarehouseId);
    
    /**
     * Get product movement history
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.productId = :productId " +
           "AND sm.movementDate BETWEEN :startDate AND :endDate " +
           "ORDER BY sm.movementDate DESC")
    List<StockMovement> getProductMovementHistory(@Param("productId") Long productId,
                                                   @Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);
    
    /**
     * Get warehouse movement history
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.warehouseId = :warehouseId " +
           "AND sm.movementDate BETWEEN :startDate AND :endDate " +
           "ORDER BY sm.movementDate DESC")
    List<StockMovement> getWarehouseMovementHistory(@Param("warehouseId") Long warehouseId,
                                                     @Param("startDate") LocalDate startDate,
                                                     @Param("endDate") LocalDate endDate);
    
    /**
     * Get total quantity in by product
     */
    @Query("SELECT SUM(sm.quantity) FROM StockMovement sm " +
           "WHERE sm.productId = :productId AND sm.movementType = 'IN' " +
           "AND sm.status = 'COMPLETED'")
    Double getTotalQuantityInByProduct(@Param("productId") Long productId);
    
    /**
     * Get total quantity out by product
     */
    @Query("SELECT SUM(sm.quantity) FROM StockMovement sm " +
           "WHERE sm.productId = :productId AND sm.movementType = 'OUT' " +
           "AND sm.status = 'COMPLETED'")
    Double getTotalQuantityOutByProduct(@Param("productId") Long productId);
    
    /**
     * Get total value in by warehouse
     */
    @Query("SELECT SUM(sm.totalValue) FROM StockMovement sm " +
           "WHERE sm.warehouseId = :warehouseId AND sm.movementType = 'IN' " +
           "AND sm.status = 'COMPLETED'")
    Double getTotalValueInByWarehouse(@Param("warehouseId") Long warehouseId);
    
    /**
     * Get total value out by warehouse
     */
    @Query("SELECT SUM(sm.totalValue) FROM StockMovement sm " +
           "WHERE sm.warehouseId = :warehouseId AND sm.movementType = 'OUT' " +
           "AND sm.status = 'COMPLETED'")
    Double getTotalValueOutByWarehouse(@Param("warehouseId") Long warehouseId);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count movements by product
     */
    @Query("SELECT COUNT(sm) FROM StockMovement sm WHERE sm.productId = :productId")
    Long countByProductId(@Param("productId") Long productId);
    
    /**
     * Count movements by warehouse
     */
    @Query("SELECT COUNT(sm) FROM StockMovement sm WHERE sm.warehouseId = :warehouseId")
    Long countByWarehouseId(@Param("warehouseId") Long warehouseId);
    
    /**
     * Count movements by movement type
     */
    @Query("SELECT COUNT(sm) FROM StockMovement sm WHERE sm.movementType = :movementType")
    Long countByMovementType(@Param("movementType") String movementType);
    
    /**
     * Count movements by status
     */
    @Query("SELECT COUNT(sm) FROM StockMovement sm WHERE sm.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count pending movements
     */
    @Query("SELECT COUNT(sm) FROM StockMovement sm WHERE sm.status = 'PENDING'")
    Long countPendingMovements();
    
    /**
     * Count movements pending approval
     */
    @Query("SELECT COUNT(sm) FROM StockMovement sm WHERE sm.isApproved = false " +
           "AND sm.status NOT IN ('CANCELLED')")
    Long countMovementsPendingApproval();
    
    /**
     * Get movement type distribution
     */
    @Query("SELECT sm.movementType, COUNT(sm) as movementCount FROM StockMovement sm " +
           "GROUP BY sm.movementType ORDER BY movementCount DESC")
    List<Object[]> getMovementTypeDistribution();
    
    /**
     * Get status distribution
     */
    @Query("SELECT sm.status, COUNT(sm) as movementCount FROM StockMovement sm " +
           "GROUP BY sm.status ORDER BY movementCount DESC")
    List<Object[]> getStatusDistribution();
    
    /**
     * Get transaction type distribution
     */
    @Query("SELECT sm.transactionType, COUNT(sm) as movementCount FROM StockMovement sm " +
           "GROUP BY sm.transactionType ORDER BY movementCount DESC")
    List<Object[]> getTransactionTypeDistribution();
    
    /**
     * Get monthly movement count
     */
    @Query("SELECT YEAR(sm.movementDate) as year, MONTH(sm.movementDate) as month, " +
           "COUNT(sm) as movementCount, SUM(sm.quantity) as totalQuantity " +
           "FROM StockMovement sm " +
           "WHERE sm.movementDate BETWEEN :startDate AND :endDate " +
           "GROUP BY YEAR(sm.movementDate), MONTH(sm.movementDate) " +
           "ORDER BY year, month")
    List<Object[]> getMonthlyMovementCount(@Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);
    
    /**
     * Get total quantity moved
     */
    @Query("SELECT SUM(sm.quantity) FROM StockMovement sm WHERE sm.status = 'COMPLETED'")
    Double getTotalQuantityMoved();
    
    /**
     * Get total value moved
     */
    @Query("SELECT SUM(sm.totalValue) FROM StockMovement sm WHERE sm.status = 'COMPLETED'")
    Double getTotalValueMoved();
    
    /**
     * Get movements by warehouse
     */
    @Query("SELECT sm.warehouseId, sm.warehouseName, COUNT(sm) as movementCount, " +
           "SUM(sm.quantity) as totalQuantity, SUM(sm.totalValue) as totalValue " +
           "FROM StockMovement sm WHERE sm.status = 'COMPLETED' " +
           "GROUP BY sm.warehouseId, sm.warehouseName ORDER BY totalValue DESC")
    List<Object[]> getMovementsByWarehouse();
    
    /**
     * Get movements by product
     */
    @Query("SELECT sm.productId, sm.productName, COUNT(sm) as movementCount, " +
           "SUM(sm.quantity) as totalQuantity FROM StockMovement sm " +
           "WHERE sm.status = 'COMPLETED' " +
           "GROUP BY sm.productId, sm.productName ORDER BY totalQuantity DESC")
    List<Object[]> getMovementsByProduct();
    
    /**
     * Find movements by tags
     */
    @Query("SELECT sm FROM StockMovement sm WHERE sm.tags LIKE CONCAT('%', :tag, '%')")
    List<StockMovement> findByTag(@Param("tag") String tag);
}
