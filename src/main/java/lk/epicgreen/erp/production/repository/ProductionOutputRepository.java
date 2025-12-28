package lk.epicgreen.erp.production.repository;

import lk.epicgreen.erp.production.entity.ProductionOutput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for ProductionOutput entity
 * Based on ACTUAL database schema: production_output table
 * 
 * Fields: wo_id (BIGINT), output_date, finished_product_id (BIGINT), batch_number,
 *         quantity_produced, quantity_accepted, quantity_rejected, quantity_rework,
 *         uom_id (BIGINT), manufacturing_date, expiry_date, warehouse_id (BIGINT),
 *         location_id (BIGINT), unit_cost, total_cost,
 *         quality_status (ENUM: PENDING, PASSED, FAILED),
 *         quality_checked_by (BIGINT), quality_checked_at, remarks
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface ProductionOutputRepository extends JpaRepository<ProductionOutput, Long>, JpaSpecificationExecutor<ProductionOutput> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find all outputs for a work order
     */
    List<ProductionOutput> findByWoId(Long woId);
    
    /**
     * Find all outputs for a work order with pagination
     */
    Page<ProductionOutput> findByWoId(Long woId, Pageable pageable);
    
    /**
     * Find all outputs for a finished product
     */
    List<ProductionOutput> findByFinishedProductId(Long finishedProductId);
    
    /**
     * Find all outputs for a finished product with pagination
     */
    Page<ProductionOutput> findByFinishedProductId(Long finishedProductId, Pageable pageable);
    
    /**
     * Find outputs by batch number
     */
    List<ProductionOutput> findByBatchNumber(String batchNumber);
    
    /**
     * Find outputs by quality status
     */
    List<ProductionOutput> findByQualityStatus(String qualityStatus);
    
    /**
     * Find outputs by quality status with pagination
     */
    Page<ProductionOutput> findByQualityStatus(String qualityStatus, Pageable pageable);
    
    /**
     * Find outputs by warehouse
     */
    List<ProductionOutput> findByWarehouseId(Long warehouseId);
    
    /**
     * Find outputs by location
     */
    List<ProductionOutput> findByLocationId(Long locationId);
    
    /**
     * Find outputs by quality checked by user
     */
    List<ProductionOutput> findByQualityCheckedBy(Long qualityCheckedBy);
    
    /**
     * Find outputs by output date
     */
    List<ProductionOutput> findByOutputDate(LocalDate outputDate);
    
    /**
     * Find outputs by output date range
     */
    List<ProductionOutput> findByOutputDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find outputs by output date range with pagination
     */
    Page<ProductionOutput> findByOutputDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find outputs by manufacturing date range
     */
    List<ProductionOutput> findByManufacturingDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find outputs by expiry date range
     */
    List<ProductionOutput> findByExpiryDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count outputs for a work order
     */
    long countByWoId(Long woId);
    
    /**
     * Count outputs for a finished product
     */
    long countByFinishedProductId(Long finishedProductId);
    
    /**
     * Count outputs by quality status
     */
    long countByQualityStatus(String qualityStatus);
    
    /**
     * Count outputs in date range
     */
    long countByOutputDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find pending quality check outputs
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.qualityStatus = 'PENDING' " +
           "ORDER BY po.outputDate")
    List<ProductionOutput> findPendingQualityCheckOutputs();
    
    /**
     * Find passed quality outputs
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.qualityStatus = 'PASSED' " +
           "ORDER BY po.outputDate DESC")
    List<ProductionOutput> findPassedQualityOutputs();
    
    /**
     * Find failed quality outputs
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.qualityStatus = 'FAILED' " +
           "ORDER BY po.outputDate DESC")
    List<ProductionOutput> findFailedQualityOutputs();
    
    /**
     * Find outputs with rejections
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.quantityRejected > 0 " +
           "ORDER BY po.outputDate DESC")
    List<ProductionOutput> findOutputsWithRejections();
    
    /**
     * Find outputs with rework
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.quantityRework > 0 " +
           "ORDER BY po.outputDate DESC")
    List<ProductionOutput> findOutputsWithRework();
    
    /**
     * Find outputs expiring soon
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.expiryDate BETWEEN CURRENT_DATE AND :futureDate " +
           "ORDER BY po.expiryDate")
    List<ProductionOutput> findOutputsExpiringSoon(@Param("futureDate") LocalDate futureDate);
    
    /**
     * Find expired outputs
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.expiryDate < CURRENT_DATE " +
           "ORDER BY po.expiryDate")
    List<ProductionOutput> findExpiredOutputs();
    
    /**
     * Get total quantity produced for a finished product
     */
    @Query("SELECT SUM(po.quantityProduced) FROM ProductionOutput po WHERE po.finishedProductId = :finishedProductId")
    BigDecimal getTotalQuantityProducedByProduct(@Param("finishedProductId") Long finishedProductId);
    
    /**
     * Get total quantity accepted for a finished product
     */
    @Query("SELECT SUM(po.quantityAccepted) FROM ProductionOutput po WHERE po.finishedProductId = :finishedProductId")
    BigDecimal getTotalQuantityAcceptedByProduct(@Param("finishedProductId") Long finishedProductId);
    
    /**
     * Get total quantity rejected for a finished product
     */
    @Query("SELECT SUM(po.quantityRejected) FROM ProductionOutput po WHERE po.finishedProductId = :finishedProductId")
    BigDecimal getTotalQuantityRejectedByProduct(@Param("finishedProductId") Long finishedProductId);
    
    /**
     * Get total quantity for a work order
     */
    @Query("SELECT SUM(po.quantityProduced) FROM ProductionOutput po WHERE po.woId = :woId")
    BigDecimal getTotalQuantityByWorkOrder(@Param("woId") Long woId);
    
    /**
     * Get total cost for a work order
     */
    @Query("SELECT SUM(po.totalCost) FROM ProductionOutput po WHERE po.woId = :woId")
    BigDecimal getTotalCostByWorkOrder(@Param("woId") Long woId);
    
    /**
     * Get production output statistics by finished product
     */
    @Query("SELECT po.finishedProductId, COUNT(po) as outputCount, " +
           "SUM(po.quantityProduced) as totalProduced, SUM(po.quantityAccepted) as totalAccepted, " +
           "SUM(po.quantityRejected) as totalRejected, SUM(po.quantityRework) as totalRework " +
           "FROM ProductionOutput po GROUP BY po.finishedProductId ORDER BY totalProduced DESC")
    List<Object[]> getOutputStatisticsByProduct();
    
    /**
     * Get production output statistics by warehouse
     */
    @Query("SELECT po.warehouseId, COUNT(po) as outputCount, " +
           "SUM(po.quantityProduced) as totalProduced, SUM(po.quantityAccepted) as totalAccepted " +
           "FROM ProductionOutput po GROUP BY po.warehouseId ORDER BY totalProduced DESC")
    List<Object[]> getOutputStatisticsByWarehouse();
    
    /**
     * Get daily production summary
     */
    @Query("SELECT po.outputDate, COUNT(po) as outputCount, " +
           "SUM(po.quantityProduced) as totalProduced, SUM(po.quantityAccepted) as totalAccepted, " +
           "SUM(po.quantityRejected) as totalRejected " +
           "FROM ProductionOutput po WHERE po.outputDate BETWEEN :startDate AND :endDate " +
           "GROUP BY po.outputDate ORDER BY po.outputDate DESC")
    List<Object[]> getDailyProductionSummary(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Get quality statistics
     */
    @Query("SELECT po.qualityStatus, COUNT(po) as outputCount, " +
           "SUM(po.quantityProduced) as totalProduced " +
           "FROM ProductionOutput po GROUP BY po.qualityStatus ORDER BY outputCount DESC")
    List<Object[]> getQualityStatistics();
    
    /**
     * Find today's outputs
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.outputDate = CURRENT_DATE " +
           "ORDER BY po.createdAt DESC")
    List<ProductionOutput> findTodayOutputs();
    
    /**
     * Get top produced products
     */
    @Query("SELECT po.finishedProductId, SUM(po.quantityProduced) as totalProduced " +
           "FROM ProductionOutput po GROUP BY po.finishedProductId ORDER BY totalProduced DESC")
    List<Object[]> getTopProducedProducts(Pageable pageable);
    
    /**
     * Get quality yield percentage by product
     */
    @Query("SELECT po.finishedProductId, " +
           "SUM(po.quantityProduced) as totalProduced, " +
           "SUM(po.quantityAccepted) as totalAccepted, " +
           "(SUM(po.quantityAccepted) / SUM(po.quantityProduced) * 100) as yieldPercentage " +
           "FROM ProductionOutput po WHERE po.quantityProduced > 0 " +
           "GROUP BY po.finishedProductId ORDER BY yieldPercentage DESC")
    List<Object[]> getQualityYieldByProduct();
    
    /**
     * Find outputs quality checked in time range
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.qualityCheckedAt BETWEEN :startTime AND :endTime " +
           "ORDER BY po.qualityCheckedAt DESC")
    List<ProductionOutput> findOutputsQualityCheckedBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * Find all outputs ordered by work order
     */
    @Query("SELECT po FROM ProductionOutput po ORDER BY po.woId, po.outputDate DESC")
    List<ProductionOutput> findAllOrderedByWorkOrder();
    
    /**
     * Find outputs by batch and product
     */
    List<ProductionOutput> findByBatchNumberAndFinishedProductId(String batchNumber, Long finishedProductId);
}
