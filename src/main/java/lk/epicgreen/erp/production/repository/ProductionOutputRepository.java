package lk.epicgreen.erp.production.repository;

import lk.epicgreen.erp.production.entity.ProductionOutput;
import lk.epicgreen.erp.production.dto.response.ProductionOutputResponse; // Kept if custom queries return this, but repo extends Entity
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
import java.util.Optional;

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
    @Query("SELECT po FROM ProductionOutput po WHERE po.workOrder.id = :woId")
    List<ProductionOutput> findByWorkOrderId(@Param("woId") Long woId);

    /**
     * Find all outputs for a work order with pagination
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.workOrder.id = :woId")
    Page<ProductionOutput> findByWorkOrderId(@Param("woId") Long woId, Pageable pageable);

    /**
     * Find all outputs for a finished product
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.finishedProduct.id = :finishedProductId")
    List<ProductionOutput> findByFinishedProductId(@Param("finishedProductId") Long finishedProductId);

    /**
     * Find all outputs for a finished product with pagination
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.finishedProduct.id = :finishedProductId")
    Page<ProductionOutput> findByFinishedProductId(@Param("finishedProductId") Long finishedProductId, Pageable pageable);

    /**
     * Find outputs by batch number
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.batchNumber = :batchNumber")
    List<ProductionOutput> findByBatchNumber(@Param("batchNumber") String batchNumber);

    /**
     * Find outputs by quality status
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.qualityStatus = :qualityStatus")
    List<ProductionOutput> findByQualityStatus(@Param("qualityStatus") String qualityStatus);

    /**
     * Find outputs by quality status with pagination
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.qualityStatus = :qualityStatus")
    Page<ProductionOutput> findByQualityStatus(@Param("qualityStatus") String qualityStatus, Pageable pageable);

    /**
     * Find outputs by warehouse
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.warehouse.id = :warehouseId")
    List<ProductionOutput> findByWarehouseId(@Param("warehouseId") Long warehouseId);

    /**
     * Find outputs by location
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.location.id = :locationId")
    List<ProductionOutput> findByLocationId(@Param("locationId") Long locationId);

    /**
     * Find outputs by quality checked by user
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.qualityCheckedBy.id = :qualityCheckedBy")
    List<ProductionOutput> findByQualityCheckedById(@Param("qualityCheckedBy") Long qualityCheckedBy);

    /**
     * Find outputs by output date
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.outputDate = :outputDate")
    List<ProductionOutput> findByOutputDate(@Param("outputDate") LocalDate outputDate);

    /**
     * Find outputs by output date range
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.outputDate BETWEEN :startDate AND :endDate")
    List<ProductionOutput> findByOutputDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Find outputs by output date range with pagination
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.outputDate BETWEEN :startDate AND :endDate")
    Page<ProductionOutput> findByOutputDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);

    /**
     * Find outputs by manufacturing date range
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.manufacturingDate BETWEEN :startDate AND :endDate")
    List<ProductionOutput> findByManufacturingDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Find outputs by expiry date range
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.expiryDate BETWEEN :startDate AND :endDate")
    List<ProductionOutput> findByExpiryDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // ==================== COUNT METHODS ====================

    /**
     * Count outputs for a work order
     */
    @Query("SELECT COUNT(po) FROM ProductionOutput po WHERE po.workOrder.id = :woId")
    long countByWorkOrderId(@Param("woId") Long woId);

    /**
     * Count outputs for a finished product
     */
    @Query("SELECT COUNT(po) FROM ProductionOutput po WHERE po.finishedProduct.id = :finishedProductId")
    long countByFinishedProductId(@Param("finishedProductId") Long finishedProductId);

    /**
     * Count outputs by quality status
     */
    @Query("SELECT COUNT(po) FROM ProductionOutput po WHERE po.qualityStatus = :qualityStatus")
    long countByQualityStatus(@Param("qualityStatus") String qualityStatus);

    /**
     * Count outputs in date range
     */
    @Query("SELECT COUNT(po) FROM ProductionOutput po WHERE po.outputDate BETWEEN :startDate AND :endDate")
    long countByOutputDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

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
    @Query("SELECT SUM(po.quantityProduced) FROM ProductionOutput po WHERE po.finishedProduct.id = :finishedProductId")
    BigDecimal getTotalQuantityProducedByProduct(@Param("finishedProductId") Long finishedProductId);

    /**
     * Get total quantity accepted for a finished product
     */
    @Query("SELECT SUM(po.quantityAccepted) FROM ProductionOutput po WHERE po.finishedProduct.id = :finishedProductId")
    BigDecimal getTotalQuantityAcceptedByProduct(@Param("finishedProductId") Long finishedProductId);

    /**
     * Get total quantity rejected for a finished product
     */
    @Query("SELECT SUM(po.quantityRejected) FROM ProductionOutput po WHERE po.finishedProduct.id = :finishedProductId")
    BigDecimal getTotalQuantityRejectedByProduct(@Param("finishedProductId") Long finishedProductId);

    /**
     * Get total quantity for a work order
     */
    @Query("SELECT SUM(po.quantityProduced) FROM ProductionOutput po WHERE po.id = :woId")
    BigDecimal getTotalQuantityByWorkOrder(@Param("woId") Long woId);

    /**
     * Get total cost for a work order
     */
    @Query("SELECT SUM(po.totalCost) FROM ProductionOutput po WHERE po.id = :woId")
    BigDecimal getTotalCostByWorkOrder(@Param("woId") Long woId);

    /**
     * Get production output statistics by finished product
     */
    @Query("SELECT po.finishedProduct.id, COUNT(po) as outputCount, " +
            "SUM(po.quantityProduced) as totalProduced, SUM(po.quantityAccepted) as totalAccepted, " +
            "SUM(po.quantityRejected) as totalRejected, SUM(po.quantityRework) as totalRework " +
            "FROM ProductionOutput po GROUP BY po.finishedProduct.id ORDER BY totalProduced DESC")
    List<Object[]> getOutputStatisticsByProduct();

    /**
     * Get production output statistics by warehouse
     */
    @Query("SELECT po.warehouse.id, COUNT(po) as outputCount, " +
            "SUM(po.quantityProduced) as totalProduced, SUM(po.quantityAccepted) as totalAccepted " +
            "FROM ProductionOutput po GROUP BY po.warehouse.id ORDER BY totalProduced DESC")
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
    List<ProductionOutputResponse> findTodayOutputs();

    /**
     * Get top produced products
     */
    @Query("SELECT po.finishedProduct.id, SUM(po.quantityProduced) as totalProduced " +
            "FROM ProductionOutput po GROUP BY po.finishedProduct.id ORDER BY totalProduced DESC")
    List<Object[]> getTopProducedProducts(Pageable pageable);

    /**
     * Get quality yield percentage by product
     */
    @Query("SELECT po.finishedProduct.id, " +
            "SUM(po.quantityProduced) as totalProduced, " +
            "SUM(po.quantityAccepted) as totalAccepted, " +
            "(SUM(po.quantityAccepted) / SUM(po.quantityProduced) * 100) as yieldPercentage " +
            "FROM ProductionOutput po WHERE po.quantityProduced > 0 " +
            "GROUP BY po.finishedProduct.id ")
    List<Object[]> getQualityYieldByProduct();

    /**
     * Find outputs quality checked in time range
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.qualityCheckedAt BETWEEN :startTime AND :endTime " +
            "ORDER BY po.qualityCheckedAt DESC")
    List<ProductionOutputResponse> findOutputsQualityCheckedBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * Find all outputs ordered by work order
     */
    @Query("SELECT po FROM ProductionOutput po ORDER BY po.id, po.outputDate DESC")
    List<ProductionOutputResponse> findAllOrderedByWorkOrder();

    /**
     * Find outputs by batch and product
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.batchNumber = :batchNumber AND po.finishedProduct.id = :finishedProductId")
    List<ProductionOutputResponse> findByBatchNumberAndFinishedProductId(@Param("batchNumber") String batchNumber, @Param("finishedProductId") Long finishedProductId);

    @Query("SELECT po FROM ProductionOutput po WHERE po.id = :woId")
    List<ProductionOutput> findByWoId(@Param("woId") Long woId);

    @Query("SELECT po FROM ProductionOutput po WHERE po.expiryDate < :now ORDER BY po.expiryDate")
    List<ProductionOutput> findExpiredOutput(@Param("now") LocalDate now);

    @Query("SELECT po FROM ProductionOutput po WHERE po.expiryDate BETWEEN :now AND :expiryDate ORDER BY po.expiryDate")
    List<ProductionOutput> findOutputExpiringSoon(@Param("now") LocalDate now, @Param("expiryDate") LocalDate expiryDate);

    @Query("SELECT SUM(po.quantityProduced) FROM ProductionOutput po WHERE po.workOrder.id = :woId")
    Optional<BigDecimal> sumQuantityProducedByWo(@Param("woId") Long woId);

    @Query("SELECT SUM(po.quantityAccepted) FROM ProductionOutput po WHERE po.workOrder.id = :woId")
    Optional<BigDecimal> sumQuantityAcceptedByWo(@Param("woId") Long woId);

    @Query("SELECT po FROM ProductionOutput po WHERE " +
            "LOWER(po.batchNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ProductionOutput> searchOutputs(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(po) FROM ProductionOutput po WHERE po.id = :id")
    Integer countByWoId(@Param("id") Long id);
}