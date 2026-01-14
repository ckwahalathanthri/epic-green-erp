package lk.epicgreen.erp.production.repository;

import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.production.entity.MaterialConsumption;
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
 * Repository interface for MaterialConsumption entity
 * Based on ACTUAL database schema: material_consumption table
 *
 * Fields: wo_id (BIGINT), wo_item_id (BIGINT), raw_material_id (BIGINT),
 *         consumption_date, batch_number, quantity_consumed, uom_id (BIGINT),
 *         unit_cost, total_cost, warehouse_id (BIGINT), consumed_by (BIGINT), remarks
 *
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface MaterialConsumptionRepository extends JpaRepository<MaterialConsumption, Long>, JpaSpecificationExecutor<MaterialConsumption> {

    // ==================== FINDER METHODS ====================

    /**
     * Find all consumptions for a work order
     */
    @Query("SELECT mc FROM MaterialConsumption mc WHERE mc.id = :woId")
    List<MaterialConsumption> findByWoId(@Param("woId") Long woId);

    /**
     * Find all consumptions for a work order with pagination
     */
    @Query("SELECT mc FROM MaterialConsumption mc WHERE mc.id = :woId")
    Page<MaterialConsumption> findByWoId(@Param("woId") Long woId, Pageable pageable);

    /**
     * Find all consumptions for a work order item
     */
    @Query("SELECT mc FROM MaterialConsumption mc WHERE mc.id = :woItemId")
    List<MaterialConsumption> findByWoItemId(@Param("woItemId") Long woItemId);

    /**
     * Find all consumptions for a raw material
     */
    @Query("SELECT mc FROM MaterialConsumption mc WHERE mc.rawMaterial.id = :rawMaterialId")
    List<MaterialConsumption> findByRawMaterialId(@Param("rawMaterialId") Long rawMaterialId);

    /**
     * Find all consumptions for a raw material with pagination
     */
    @Query("SELECT mc FROM MaterialConsumption mc WHERE mc.rawMaterial.id = :rawMaterialId")
    Page<MaterialConsumption> findByRawMaterialId(@Param("rawMaterialId") Long rawMaterialId, Pageable pageable);

    /**
     * Find consumptions by batch number
     */
    @Query("SELECT mc FROM MaterialConsumption mc WHERE mc.batchNumber = :batchNumber")
    List<MaterialConsumption> findByBatchNumber(@Param("batchNumber") String batchNumber);

    /**
     * Find consumptions by warehouse
     */
    @Query("SELECT mc FROM MaterialConsumption mc WHERE mc.warehouse.id = :warehouseId")
    List<MaterialConsumption> findByWarehouseId(@Param("warehouseId") Long warehouseId);

    /**
     * Find consumptions by consumed by user
     */
    @Query("SELECT mc FROM MaterialConsumption mc WHERE mc.consumedBy = :consumedBy")
    List<MaterialConsumption> findByConsumedBy(@Param("consumedBy") Long consumedBy);

    /**
     * Find consumptions by consumption date
     */
    @Query("SELECT mc FROM MaterialConsumption mc WHERE mc.consumptionDate = :consumptionDate")
    List<MaterialConsumption> findByConsumptionDate(@Param("consumptionDate") LocalDate consumptionDate);

    /**
     * Find consumptions by consumption date range
     */
    @Query("SELECT mc FROM MaterialConsumption mc WHERE mc.consumptionDate BETWEEN :startDate AND :endDate")
    List<MaterialConsumption> findByConsumptionDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Find consumptions by consumption date range with pagination
     */
    @Query("SELECT mc FROM MaterialConsumption mc WHERE mc.consumptionDate BETWEEN :startDate AND :endDate")
    Page<MaterialConsumption> findByConsumptionDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);

    // ==================== COUNT METHODS ====================

    /**
     * Count consumptions for a work order
     */
    @Query("SELECT COUNT(mc) FROM MaterialConsumption mc WHERE mc.id = :woId")
    long countByWoId(@Param("woId") Long woId);

    /**
     * Count consumptions for a raw material
     */
    @Query("SELECT COUNT(mc) FROM MaterialConsumption mc WHERE mc.rawMaterial.id= :rawMaterialId")
    long countByRawMaterialId(@Param("rawMaterialId") Long rawMaterialId);

    /**
     * Count consumptions in date range
     */
    @Query("SELECT COUNT(mc) FROM MaterialConsumption mc WHERE mc.consumptionDate BETWEEN :startDate AND :endDate")
    long countByConsumptionDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // ==================== CUSTOM QUERIES ====================

    /**
     * Get total quantity consumed for a raw material
     */
    @Query("SELECT SUM(mc.quantityConsumed) FROM MaterialConsumption mc WHERE mc.rawMaterial.id = :rawMaterialId")
    BigDecimal getTotalQuantityConsumedByRawMaterial(@Param("rawMaterialId") Long rawMaterialId);

    /**
     * Get total quantity consumed for a work order
     */
    @Query("SELECT SUM(mc.quantityConsumed) FROM MaterialConsumption mc WHERE mc.id = :woId")
    BigDecimal getTotalQuantityConsumedByWorkOrder(@Param("woId") Long woId);

    /**
     * Get total cost for a work order
     */
    @Query("SELECT SUM(mc.totalCost) FROM MaterialConsumption mc WHERE mc.id = :woId")
    BigDecimal getTotalCostByWorkOrder(@Param("woId") Long woId);

    /**
     * Get total value consumed by raw material
     */
    @Query("SELECT SUM(mc.totalCost) FROM MaterialConsumption mc WHERE mc.rawMaterial.id = :rawMaterialId")
    BigDecimal getTotalValueByRawMaterial(@Param("rawMaterialId") Long rawMaterialId);

    /**
     * Get consumption statistics by raw material
     */
    @Query("SELECT mc.rawMaterial.id, COUNT(mc) as consumptionCount, " +
            "SUM(mc.quantityConsumed) as totalQuantity, SUM(mc.totalCost) as totalCost " +
            "FROM MaterialConsumption mc GROUP BY mc.rawMaterial.id ORDER BY totalQuantity DESC")
    List<Object[]> getConsumptionStatisticsByRawMaterial();

    /**
     * Get consumption statistics by warehouse
     */
    @Query("SELECT mc.warehouse.id, COUNT(mc) as consumptionCount, " +
            "SUM(mc.quantityConsumed) as totalQuantity, SUM(mc.totalCost) as totalCost " +
            "FROM MaterialConsumption mc GROUP BY mc.warehouse.id ORDER BY totalQuantity DESC")
    List<Object[]> getConsumptionStatisticsByWarehouse();

    /**
     * Get daily consumption summary
     */
    @Query("SELECT mc.consumptionDate, COUNT(mc) as consumptionCount, " +
            "SUM(mc.quantityConsumed) as totalQuantity, SUM(mc.totalCost) as totalCost " +
            "FROM MaterialConsumption mc WHERE mc.consumptionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY mc.consumptionDate ORDER BY mc.consumptionDate DESC")
    List<Object[]> getDailyConsumptionSummary(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * Find today's consumptions
     */
    @Query("SELECT mc FROM MaterialConsumption mc WHERE mc.consumptionDate = CURRENT_DATE " +
            "ORDER BY mc.createdAt DESC")
    List<MaterialConsumption> findTodayConsumptions();

    /**
     * Get top consumed raw materials
     */
    @Query("SELECT mc.rawMaterial.id, SUM(mc.quantityConsumed) as totalQuantity " +
            "FROM MaterialConsumption mc GROUP BY mc.rawMaterial.id ORDER BY totalQuantity DESC")
    List<Object[]> getTopConsumedRawMaterials(Pageable pageable);

    /**
     * Get consumption by work order and raw material
     */
    @Query("SELECT SUM(mc.quantityConsumed) FROM MaterialConsumption mc " +
            "WHERE mc.id = :woId AND mc.rawMaterial.id = :rawMaterialId")
    BigDecimal getConsumptionByWorkOrderAndRawMaterial(
            @Param("woId") Long woId,
            @Param("rawMaterialId") Long rawMaterialId);

    /**
     * Find consumptions by work order and batch
     */
    @Query("SELECT mc FROM MaterialConsumption mc WHERE mc.id = :woId AND mc.batchNumber = :batchNumber")
    List<MaterialConsumption> findByWoIdAndBatchNumber(@Param("woId") Long woId, @Param("batchNumber") String batchNumber);

    /**
     * Find consumptions by raw material and batch
     */
    @Query("SELECT mc FROM MaterialConsumption mc WHERE mc.rawMaterial.id = :rawMaterialId AND mc.batchNumber = :batchNumber")
    List<MaterialConsumption> findByRawMaterialIdAndBatchNumber(@Param("rawMaterialId") Long rawMaterialId, @Param("batchNumber") String batchNumber);

    /**
     * Find all consumptions ordered by work order
     */
    @Query("SELECT mc FROM MaterialConsumption mc ORDER BY mc.id, mc.consumptionDate DESC")
    List<MaterialConsumption> findAllOrderedByWorkOrder();

    /**
     * Get consumption trend by month
     */
    @Query("SELECT YEAR(mc.consumptionDate), MONTH(mc.consumptionDate), " +
            "SUM(mc.quantityConsumed) as totalQuantity, SUM(mc.totalCost) as totalCost " +
            "FROM MaterialConsumption mc " +
            "WHERE mc.consumptionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY YEAR(mc.consumptionDate), MONTH(mc.consumptionDate) " +
            "ORDER BY YEAR(mc.consumptionDate) DESC, MONTH(mc.consumptionDate) DESC")
    List<Object[]> getMonthlyConsumptionTrend(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT mc FROM MaterialConsumption mc WHERE mc.rawMaterial.productCode LIKE %:keyword% OR mc.batchNumber LIKE %:keyword%")
    Page<MaterialConsumption> searchConsumptions(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT SUM(mc.quantityConsumed) FROM MaterialConsumption mc WHERE mc.workOrder.id = :woId AND mc.rawMaterial.id = :rawMaterialId")
    Optional<BigDecimal> sumQuantityByWoAndMaterial(@Param("woId") Long woId, @Param("rawMaterialId") Long rawMaterialId);

    @Query("SELECT SUM(mc.totalCost) FROM MaterialConsumption mc WHERE mc.workOrder.id = :woId")
    Optional<BigDecimal> sumTotalCostByWo(@Param("woId") Long woId);
}