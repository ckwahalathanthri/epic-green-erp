package lk.epicgreen.erp.production.repository;

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
    List<MaterialConsumption> findByWoId(Long woId);
    
    /**
     * Find all consumptions for a work order with pagination
     */
    Page<MaterialConsumption> findByWoId(Long woId, Pageable pageable);
    
    /**
     * Find all consumptions for a work order item
     */
    List<MaterialConsumption> findByWoItemId(Long woItemId);
    
    /**
     * Find all consumptions for a raw material
     */
    List<MaterialConsumption> findByRawMaterialId(Long rawMaterialId);
    
    /**
     * Find all consumptions for a raw material with pagination
     */
    Page<MaterialConsumption> findByRawMaterialId(Long rawMaterialId, Pageable pageable);
    
    /**
     * Find consumptions by batch number
     */
    List<MaterialConsumption> findByBatchNumber(String batchNumber);
    
    /**
     * Find consumptions by warehouse
     */
    List<MaterialConsumption> findByWarehouseId(Long warehouseId);
    
    /**
     * Find consumptions by consumed by user
     */
    List<MaterialConsumption> findByConsumedBy(Long consumedBy);
    
    /**
     * Find consumptions by consumption date
     */
    List<MaterialConsumption> findByConsumptionDate(LocalDate consumptionDate);
    
    /**
     * Find consumptions by consumption date range
     */
    List<MaterialConsumption> findByConsumptionDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find consumptions by consumption date range with pagination
     */
    Page<MaterialConsumption> findByConsumptionDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count consumptions for a work order
     */
    long countByWoId(Long woId);
    
    /**
     * Count consumptions for a raw material
     */
    long countByRawMaterialId(Long rawMaterialId);
    
    /**
     * Count consumptions in date range
     */
    long countByConsumptionDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Get total quantity consumed for a raw material
     */
    @Query("SELECT SUM(mc.quantityConsumed) FROM MaterialConsumption mc WHERE mc.rawMaterialId = :rawMaterialId")
    BigDecimal getTotalQuantityConsumedByRawMaterial(@Param("rawMaterialId") Long rawMaterialId);
    
    /**
     * Get total quantity consumed for a work order
     */
    @Query("SELECT SUM(mc.quantityConsumed) FROM MaterialConsumption mc WHERE mc.woId = :woId")
    BigDecimal getTotalQuantityConsumedByWorkOrder(@Param("woId") Long woId);
    
    /**
     * Get total cost for a work order
     */
    @Query("SELECT SUM(mc.totalCost) FROM MaterialConsumption mc WHERE mc.woId = :woId")
    BigDecimal getTotalCostByWorkOrder(@Param("woId") Long woId);
    
    /**
     * Get total value consumed by raw material
     */
    @Query("SELECT SUM(mc.totalCost) FROM MaterialConsumption mc WHERE mc.rawMaterialId = :rawMaterialId")
    BigDecimal getTotalValueByRawMaterial(@Param("rawMaterialId") Long rawMaterialId);
    
    /**
     * Get consumption statistics by raw material
     */
    @Query("SELECT mc.rawMaterialId, COUNT(mc) as consumptionCount, " +
           "SUM(mc.quantityConsumed) as totalQuantity, SUM(mc.totalCost) as totalCost " +
           "FROM MaterialConsumption mc GROUP BY mc.rawMaterialId ORDER BY totalQuantity DESC")
    List<Object[]> getConsumptionStatisticsByRawMaterial();
    
    /**
     * Get consumption statistics by warehouse
     */
    @Query("SELECT mc.warehouseId, COUNT(mc) as consumptionCount, " +
           "SUM(mc.quantityConsumed) as totalQuantity, SUM(mc.totalCost) as totalCost " +
           "FROM MaterialConsumption mc GROUP BY mc.warehouseId ORDER BY totalQuantity DESC")
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
    @Query("SELECT mc.rawMaterialId, SUM(mc.quantityConsumed) as totalQuantity " +
           "FROM MaterialConsumption mc GROUP BY mc.rawMaterialId ORDER BY totalQuantity DESC")
    List<Object[]> getTopConsumedRawMaterials(Pageable pageable);
    
    /**
     * Get consumption by work order and raw material
     */
    @Query("SELECT SUM(mc.quantityConsumed) FROM MaterialConsumption mc " +
           "WHERE mc.woId = :woId AND mc.rawMaterialId = :rawMaterialId")
    BigDecimal getConsumptionByWorkOrderAndRawMaterial(
            @Param("woId") Long woId,
            @Param("rawMaterialId") Long rawMaterialId);
    
    /**
     * Find consumptions by work order and batch
     */
    List<MaterialConsumption> findByWoIdAndBatchNumber(Long woId, String batchNumber);
    
    /**
     * Find consumptions by raw material and batch
     */
    List<MaterialConsumption> findByRawMaterialIdAndBatchNumber(Long rawMaterialId, String batchNumber);
    
    /**
     * Find all consumptions ordered by work order
     */
    @Query("SELECT mc FROM MaterialConsumption mc ORDER BY mc.woId, mc.consumptionDate DESC")
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
}
