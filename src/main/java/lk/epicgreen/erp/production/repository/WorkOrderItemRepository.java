package lk.epicgreen.erp.production.repository;

import lk.epicgreen.erp.production.entity.WorkOrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for WorkOrderItem entity
 * Based on ACTUAL database schema: work_order_items table
 * 
 * Fields: wo_id (BIGINT), raw_material_id (BIGINT), planned_quantity, consumed_quantity,
 *         uom_id (BIGINT), unit_cost, total_cost,
 *         status (ENUM: PENDING, ISSUED, CONSUMED),
 *         issued_from_warehouse_id (BIGINT), issued_at, issued_by (BIGINT)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface WorkOrderItemRepository extends JpaRepository<WorkOrderItem, Long>, JpaSpecificationExecutor<WorkOrderItem> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find all items for a work order
     */
    List<WorkOrderItem> findByWoId(Long woId);
    
    /**
     * Find all items for a work order with pagination
     */
    Page<WorkOrderItem> findByWoId(Long woId, Pageable pageable);
    
    /**
     * Find all items for a raw material
     */
    List<WorkOrderItem> findByRawMaterialId(Long rawMaterialId);
    
    /**
     * Find all items for a raw material with pagination
     */
    Page<WorkOrderItem> findByRawMaterialId(Long rawMaterialId, Pageable pageable);
    
    /**
     * Find items by work order and raw material
     */
    List<WorkOrderItem> findByWoIdAndRawMaterialId(Long woId, Long rawMaterialId);
    
    /**
     * Find items by status
     */
    List<WorkOrderItem> findByStatus(String status);
    
    /**
     * Find items by work order and status
     */
    List<WorkOrderItem> findByWoIdAndStatus(Long woId, String status);
    
    /**
     * Find items issued by user
     */
    List<WorkOrderItem> findByIssuedBy(Long issuedBy);
    
    /**
     * Find items issued from warehouse
     */
    List<WorkOrderItem> findByIssuedFromWarehouseId(Long warehouseId);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count items for a work order
     */
    long countByWoId(Long woId);
    
    /**
     * Count items for a raw material
     */
    long countByRawMaterialId(Long rawMaterialId);
    
    /**
     * Count items by status
     */
    long countByStatus(String status);
    
    // ==================== DELETE METHODS ====================
    
    /**
     * Delete all items for a work order
     */
    @Modifying
    @Query("DELETE FROM WorkOrderItem woi WHERE woi.woId = :woId")
    void deleteAllByWoId(@Param("woId") Long woId);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find pending items
     */
    @Query("SELECT woi FROM WorkOrderItem woi WHERE woi.status = 'PENDING' ORDER BY woi.woId")
    List<WorkOrderItem> findPendingItems();
    
    /**
     * Find issued items
     */
    @Query("SELECT woi FROM WorkOrderItem woi WHERE woi.status = 'ISSUED' ORDER BY woi.issuedAt DESC")
    List<WorkOrderItem> findIssuedItems();
    
    /**
     * Find consumed items
     */
    @Query("SELECT woi FROM WorkOrderItem woi WHERE woi.status = 'CONSUMED' ORDER BY woi.woId")
    List<WorkOrderItem> findConsumedItems();
    
    /**
     * Find pending items for a work order
     */
    @Query("SELECT woi FROM WorkOrderItem woi WHERE woi.woId = :woId AND woi.status = 'PENDING'")
    List<WorkOrderItem> findPendingItemsByWorkOrder(@Param("woId") Long woId);
    
    /**
     * Find items with consumption variance
     */
    @Query("SELECT woi FROM WorkOrderItem woi WHERE woi.consumedQuantity <> woi.plannedQuantity " +
           "AND woi.status = 'CONSUMED'")
    List<WorkOrderItem> findItemsWithConsumptionVariance();
    
    /**
     * Get total planned quantity for a raw material
     */
    @Query("SELECT SUM(woi.plannedQuantity) FROM WorkOrderItem woi WHERE woi.rawMaterialId = :rawMaterialId")
    BigDecimal getTotalPlannedQuantityByRawMaterial(@Param("rawMaterialId") Long rawMaterialId);
    
    /**
     * Get total consumed quantity for a raw material
     */
    @Query("SELECT SUM(woi.consumedQuantity) FROM WorkOrderItem woi WHERE woi.rawMaterialId = :rawMaterialId")
    BigDecimal getTotalConsumedQuantityByRawMaterial(@Param("rawMaterialId") Long rawMaterialId);
    
    /**
     * Get total cost for a work order
     */
    @Query("SELECT SUM(woi.totalCost) FROM WorkOrderItem woi WHERE woi.woId = :woId")
    BigDecimal getTotalCostByWorkOrder(@Param("woId") Long woId);
    
    /**
     * Get total planned quantity for a work order
     */
    @Query("SELECT SUM(woi.plannedQuantity) FROM WorkOrderItem woi WHERE woi.woId = :woId")
    BigDecimal getTotalPlannedQuantityByWorkOrder(@Param("woId") Long woId);
    
    /**
     * Get total consumed quantity for a work order
     */
    @Query("SELECT SUM(woi.consumedQuantity) FROM WorkOrderItem woi WHERE woi.woId = :woId")
    BigDecimal getTotalConsumedQuantityByWorkOrder(@Param("woId") Long woId);
    
    /**
     * Get work order item statistics by raw material
     */
    @Query("SELECT woi.rawMaterialId, COUNT(woi) as orderCount, " +
           "SUM(woi.plannedQuantity) as totalPlanned, SUM(woi.consumedQuantity) as totalConsumed " +
           "FROM WorkOrderItem woi GROUP BY woi.rawMaterialId ORDER BY orderCount DESC")
    List<Object[]> getWorkOrderItemStatisticsByRawMaterial();
    
    /**
     * Get most consumed raw materials
     */
    @Query("SELECT woi.rawMaterialId, SUM(woi.consumedQuantity) as totalConsumed " +
           "FROM WorkOrderItem woi WHERE woi.status = 'CONSUMED' " +
           "GROUP BY woi.rawMaterialId ORDER BY totalConsumed DESC")
    List<Object[]> getMostConsumedRawMaterials(Pageable pageable);
    
    /**
     * Find items issued in time range
     */
    @Query("SELECT woi FROM WorkOrderItem woi WHERE woi.issuedAt BETWEEN :startTime AND :endTime " +
           "ORDER BY woi.issuedAt DESC")
    List<WorkOrderItem> findItemsIssuedBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * Find all items ordered by work order
     */
    @Query("SELECT woi FROM WorkOrderItem woi ORDER BY woi.woId, woi.rawMaterialId")
    List<WorkOrderItem> findAllOrderedByWorkOrder();
    
    /**
     * Get material consumption efficiency
     */
    @Query("SELECT woi.rawMaterialId, " +
           "SUM(woi.plannedQuantity) as totalPlanned, " +
           "SUM(woi.consumedQuantity) as totalConsumed, " +
           "((SUM(woi.plannedQuantity) - SUM(woi.consumedQuantity)) / SUM(woi.plannedQuantity) * 100) as savingsPercentage " +
           "FROM WorkOrderItem woi WHERE woi.status = 'CONSUMED' AND woi.plannedQuantity > 0 " +
           "GROUP BY woi.rawMaterialId")
    List<Object[]> getMaterialConsumptionEfficiency();
}
