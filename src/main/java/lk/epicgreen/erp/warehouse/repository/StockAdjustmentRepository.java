package lk.epicgreen.erp.warehouse.repository;

import lk.epicgreen.erp.warehouse.entity.StockAdjustment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for StockAdjustment entity
 * Based on ACTUAL database schema: stock_adjustments table
 * 
 * Fields: adjustment_number, adjustment_date, warehouse_id (BIGINT),
 *         adjustment_type (ENUM: DAMAGE, EXPIRY, PILFERAGE, SURPLUS, DEFICIT, OTHER),
 *         status (ENUM: DRAFT, PENDING_APPROVAL, APPROVED, REJECTED),
 *         approved_by (BIGINT), approved_at, remarks
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface StockAdjustmentRepository extends JpaRepository<StockAdjustment, Long>, JpaSpecificationExecutor<StockAdjustment> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find adjustment by adjustment number
     */
    Optional<StockAdjustment> findByAdjustmentNumber(String adjustmentNumber);
    
    /**
     * Find all adjustments for a warehouse
     */
    List<StockAdjustment> findByWarehouseId(Long warehouseId);
    
    /**
     * Find all adjustments for a warehouse with pagination
     */
    Page<StockAdjustment> findByWarehouseId(Long warehouseId, Pageable pageable);
    
    /**
     * Find adjustments by status
     */
    List<StockAdjustment> findByStatus(String status);
    
    /**
     * Find adjustments by status with pagination
     */
    Page<StockAdjustment> findByStatus(String status, Pageable pageable);
    
    /**
     * Find adjustments by adjustment type
     */
    List<StockAdjustment> findByAdjustmentType(String adjustmentType);
    
    /**
     * Find adjustments by adjustment type with pagination
     */
    Page<StockAdjustment> findByAdjustmentType(String adjustmentType, Pageable pageable);
    
    /**
     * Find adjustments by warehouse and status
     */
    List<StockAdjustment> findByWarehouseIdAndStatus(Long warehouseId, String status);
    
    /**
     * Find adjustments by adjustment date
     */
    List<StockAdjustment> findByAdjustmentDate(LocalDate adjustmentDate);
    
    /**
     * Find adjustments by adjustment date range
     */
    List<StockAdjustment> findByAdjustmentDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find adjustments by adjustment date range with pagination
     */
    Page<StockAdjustment> findByAdjustmentDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find adjustments approved by a user
     */
    List<StockAdjustment> findByApprovedBy(Long approvedBy);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if adjustment number exists
     */
    boolean existsByAdjustmentNumber(String adjustmentNumber);
    
    /**
     * Check if adjustment number exists excluding specific adjustment ID
     */
    boolean existsByAdjustmentNumberAndIdNot(String adjustmentNumber, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search adjustments by adjustment number containing (case-insensitive)
     */
    Page<StockAdjustment> findByAdjustmentNumberContainingIgnoreCase(String adjustmentNumber, Pageable pageable);
    
    /**
     * Search adjustments by multiple criteria
     */
    @Query("SELECT sa FROM StockAdjustment sa WHERE " +
           "(:adjustmentNumber IS NULL OR LOWER(sa.adjustmentNumber) LIKE LOWER(CONCAT('%', :adjustmentNumber, '%'))) AND " +
           "(:warehouseId IS NULL OR sa.warehouse.id = :warehouseId) AND " +
           "(:adjustmentType IS NULL OR sa.adjustmentType = :adjustmentType) AND " +
           "(:status IS NULL OR sa.status = :status) AND " +
           "(:startDate IS NULL OR sa.adjustmentDate >= :startDate) AND " +
           "(:endDate IS NULL OR sa.adjustmentDate <= :endDate)")
    Page<StockAdjustment> searchAdjustments(
            @Param("adjustmentNumber") String adjustmentNumber,
            @Param("warehouseId") Long warehouseId,
            @Param("adjustmentType") String adjustmentType,
            @Param("status") String status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count adjustments for a warehouse
     */
    long countByWarehouseId(Long warehouseId);
    
    /**
     * Count adjustments by status
     */
    long countByStatus(String status);
    
    /**
     * Count adjustments by type
     */
    long countByAdjustmentType(String adjustmentType);
    
    /**
     * Count adjustments in date range
     */
    long countByAdjustmentDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Count adjustments by warehouse and status
     */
    long countByWarehouseIdAndStatus(Long warehouseId, String status);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find draft adjustments
     */
    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.status = 'DRAFT' " +
           "ORDER BY sa.adjustmentDate DESC, sa.createdAt DESC")
    List<StockAdjustment> findDraftAdjustments();
    
    /**
     * Find pending approval adjustments
     */
    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.status = 'PENDING_APPROVAL' " +
           "ORDER BY sa.adjustmentDate DESC, sa.createdAt DESC")
    List<StockAdjustment> findPendingApprovalAdjustments();
    
    /**
     * Find approved adjustments
     */
    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.status = 'APPROVED' " +
           "ORDER BY sa.approvedAt DESC")
    List<StockAdjustment> findApprovedAdjustments();
    
    /**
     * Find rejected adjustments
     */
    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.status = 'REJECTED' " +
           "ORDER BY sa.adjustmentDate DESC")
    List<StockAdjustment> findRejectedAdjustments();
    
    /**
     * Find adjustments by type - DAMAGE
     */
    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.adjustmentType = 'DAMAGE' " +
           "ORDER BY sa.adjustmentDate DESC")
    List<StockAdjustment> findDamageAdjustments();
    
    /**
     * Find adjustments by type - EXPIRY
     */
    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.adjustmentType = 'EXPIRY' " +
           "ORDER BY sa.adjustmentDate DESC")
    List<StockAdjustment> findExpiryAdjustments();
    
    /**
     * Find adjustments by type - PILFERAGE
     */
    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.adjustmentType = 'PILFERAGE' " +
           "ORDER BY sa.adjustmentDate DESC")
    List<StockAdjustment> findPilferageAdjustments();
    
    /**
     * Find adjustments by type - SURPLUS
     */
    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.adjustmentType = 'SURPLUS' " +
           "ORDER BY sa.adjustmentDate DESC")
    List<StockAdjustment> findSurplusAdjustments();
    
    /**
     * Find adjustments by type - DEFICIT
     */
    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.adjustmentType = 'DEFICIT' " +
           "ORDER BY sa.adjustmentDate DESC")
    List<StockAdjustment> findDeficitAdjustments();
    
    /**
     * Get adjustment statistics by warehouse
     */
    @Query("SELECT sa.adjustmentType, sa.status, COUNT(sa) as adjustmentCount " +
           "FROM StockAdjustment sa WHERE sa.warehouse.id = :warehouseId " +
           "GROUP BY sa.adjustmentType, sa.status ORDER BY adjustmentCount DESC")
    List<Object[]> getAdjustmentStatisticsByWarehouse(@Param("warehouseId") Long warehouseId);
    
    /**
     * Get adjustment statistics by type
     */
    @Query("SELECT sa.adjustmentType, COUNT(sa) as adjustmentCount " +
           "FROM StockAdjustment sa GROUP BY sa.adjustmentType ORDER BY adjustmentCount DESC")
    List<Object[]> getAdjustmentStatisticsByType();
    
    /**
     * Get adjustment statistics by status
     */
    @Query("SELECT sa.status, COUNT(sa) as adjustmentCount " +
           "FROM StockAdjustment sa GROUP BY sa.status ORDER BY adjustmentCount DESC")
    List<Object[]> getAdjustmentStatisticsByStatus();
    
    /**
     * Find adjustments awaiting approval for a warehouse
     */
    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.warehouse.id = :warehouseId " +
           "AND sa.status = 'PENDING_APPROVAL' ORDER BY sa.adjustmentDate")
    List<StockAdjustment> findAwaitingApprovalByWarehouse(@Param("warehouseId") Long warehouseId);
    
    /**
     * Find adjustments approved in date range
     */
    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.approvedAt BETWEEN :startDate AND :endDate " +
           "ORDER BY sa.approvedAt DESC")
    List<StockAdjustment> findAdjustmentsApprovedBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find adjustments created by a user
     */
    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.createdBy = :userId " +
           "ORDER BY sa.createdAt DESC")
    List<StockAdjustment> findAdjustmentsByCreatedBy(@Param("userId") Long userId);
    
    /**
     * Find recent adjustments (last N days)
     */
    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.adjustmentDate >= :startDate " +
           "ORDER BY sa.adjustmentDate DESC, sa.createdAt DESC")
    List<StockAdjustment> findRecentAdjustments(@Param("startDate") LocalDate startDate, Pageable pageable);
    
    /**
     * Find all adjustments ordered by date
     */
    List<StockAdjustment> findAllByOrderByAdjustmentDateDescCreatedAtDesc();
    
    /**
     * Find adjustments by warehouse and type
     */
    List<StockAdjustment> findByWarehouseIdAndAdjustmentType(Long warehouseId, String adjustmentType);
}
