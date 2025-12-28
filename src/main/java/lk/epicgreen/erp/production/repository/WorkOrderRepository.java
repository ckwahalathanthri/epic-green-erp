package lk.epicgreen.erp.production.repository;

import lk.epicgreen.erp.production.entity.WorkOrder;
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
 * Repository interface for WorkOrder entity
 * Based on ACTUAL database schema: work_orders table
 * 
 * Fields: wo_number, wo_date, bom_id (BIGINT), finished_product_id (BIGINT),
 *         warehouse_id (BIGINT), planned_quantity, actual_quantity, uom_id (BIGINT),
 *         batch_number, manufacturing_date, expected_completion_date, actual_completion_date,
 *         status (ENUM: DRAFT, RELEASED, IN_PROGRESS, COMPLETED, CANCELLED),
 *         priority (ENUM: LOW, MEDIUM, HIGH, URGENT),
 *         supervisor_id (BIGINT), material_cost, labor_cost, overhead_cost, total_cost, remarks
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long>, JpaSpecificationExecutor<WorkOrder> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find work order by WO number
     */
    Optional<WorkOrder> findByWoNumber(String woNumber);
    
    /**
     * Find work orders by status
     */
    List<WorkOrder> findByStatus(String status);
    
    /**
     * Find work orders by status with pagination
     */
    Page<WorkOrder> findByStatus(String status, Pageable pageable);
    
    /**
     * Find work orders by priority
     */
    List<WorkOrder> findByPriority(String priority);
    
    /**
     * Find work orders by BOM
     */
    List<WorkOrder> findByBomId(Long bomId);
    
    /**
     * Find work orders by finished product
     */
    List<WorkOrder> findByFinishedProductId(Long finishedProductId);
    
    /**
     * Find work orders by finished product with pagination
     */
    Page<WorkOrder> findByFinishedProductId(Long finishedProductId, Pageable pageable);
    
    /**
     * Find work orders by warehouse
     */
    List<WorkOrder> findByWarehouseId(Long warehouseId);
    
    /**
     * Find work orders by supervisor
     */
    List<WorkOrder> findBySupervisorId(Long supervisorId);
    
    /**
     * Find work orders by batch number
     */
    List<WorkOrder> findByBatchNumber(String batchNumber);
    
    /**
     * Find work orders by WO date
     */
    List<WorkOrder> findByWoDate(LocalDate woDate);
    
    /**
     * Find work orders by WO date range
     */
    List<WorkOrder> findByWoDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find work orders by WO date range with pagination
     */
    Page<WorkOrder> findByWoDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find work orders by expected completion date
     */
    List<WorkOrder> findByExpectedCompletionDate(LocalDate expectedCompletionDate);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if WO number exists
     */
    boolean existsByWoNumber(String woNumber);
    
    /**
     * Check if WO number exists excluding specific WO ID
     */
    boolean existsByWoNumberAndIdNot(String woNumber, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search work orders by WO number containing (case-insensitive)
     */
    Page<WorkOrder> findByWoNumberContainingIgnoreCase(String woNumber, Pageable pageable);
    
    /**
     * Search work orders by batch number containing (case-insensitive)
     */
    Page<WorkOrder> findByBatchNumberContainingIgnoreCase(String batchNumber, Pageable pageable);
    
    /**
     * Search work orders by multiple criteria
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE " +
           "(:woNumber IS NULL OR LOWER(wo.woNumber) LIKE LOWER(CONCAT('%', :woNumber, '%'))) AND " +
           "(:finishedProductId IS NULL OR wo.finishedProductId = :finishedProductId) AND " +
           "(:status IS NULL OR wo.status = :status) AND " +
           "(:priority IS NULL OR wo.priority = :priority) AND " +
           "(:supervisorId IS NULL OR wo.supervisorId = :supervisorId) AND " +
           "(:startDate IS NULL OR wo.woDate >= :startDate) AND " +
           "(:endDate IS NULL OR wo.woDate <= :endDate)")
    Page<WorkOrder> searchWorkOrders(
            @Param("woNumber") String woNumber,
            @Param("finishedProductId") Long finishedProductId,
            @Param("status") String status,
            @Param("priority") String priority,
            @Param("supervisorId") Long supervisorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count work orders by status
     */
    long countByStatus(String status);
    
    /**
     * Count work orders by priority
     */
    long countByPriority(String priority);
    
    /**
     * Count work orders by supervisor
     */
    long countBySupervisorId(Long supervisorId);
    
    /**
     * Count work orders in date range
     */
    long countByWoDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find draft work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.status = 'DRAFT' ORDER BY wo.woDate DESC")
    List<WorkOrder> findDraftWorkOrders();
    
    /**
     * Find released work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.status = 'RELEASED' ORDER BY wo.woDate")
    List<WorkOrder> findReleasedWorkOrders();
    
    /**
     * Find in-progress work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.status = 'IN_PROGRESS' ORDER BY wo.expectedCompletionDate")
    List<WorkOrder> findInProgressWorkOrders();
    
    /**
     * Find completed work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.status = 'COMPLETED' ORDER BY wo.actualCompletionDate DESC")
    List<WorkOrder> findCompletedWorkOrders();
    
    /**
     * Find cancelled work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.status = 'CANCELLED' ORDER BY wo.woDate DESC")
    List<WorkOrder> findCancelledWorkOrders();
    
    /**
     * Find urgent work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.priority = 'URGENT' " +
           "AND wo.status NOT IN ('COMPLETED', 'CANCELLED') ORDER BY wo.woDate")
    List<WorkOrder> findUrgentWorkOrders();
    
    /**
     * Find high priority work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.priority = 'HIGH' " +
           "AND wo.status NOT IN ('COMPLETED', 'CANCELLED') ORDER BY wo.woDate")
    List<WorkOrder> findHighPriorityWorkOrders();
    
    /**
     * Find work orders due for completion
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.expectedCompletionDate <= :date " +
           "AND wo.status NOT IN ('COMPLETED', 'CANCELLED') ORDER BY wo.expectedCompletionDate")
    List<WorkOrder> findWorkOrdersDueForCompletion(@Param("date") LocalDate date);
    
    /**
     * Find overdue work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.expectedCompletionDate < CURRENT_DATE " +
           "AND wo.status NOT IN ('COMPLETED', 'CANCELLED') ORDER BY wo.expectedCompletionDate")
    List<WorkOrder> findOverdueWorkOrders();
    
    /**
     * Find work orders by status and priority
     */
    List<WorkOrder> findByStatusAndPriority(String status, String priority);
    
    /**
     * Get work order statistics
     */
    @Query("SELECT " +
           "COUNT(wo) as totalWorkOrders, " +
           "SUM(CASE WHEN wo.status = 'DRAFT' THEN 1 ELSE 0 END) as draftOrders, " +
           "SUM(CASE WHEN wo.status = 'RELEASED' THEN 1 ELSE 0 END) as releasedOrders, " +
           "SUM(CASE WHEN wo.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) as inProgressOrders, " +
           "SUM(CASE WHEN wo.status = 'COMPLETED' THEN 1 ELSE 0 END) as completedOrders, " +
           "SUM(wo.plannedQuantity) as totalPlannedQuantity, " +
           "SUM(wo.actualQuantity) as totalActualQuantity " +
           "FROM WorkOrder wo")
    Object getWorkOrderStatistics();
    
    /**
     * Get work orders grouped by status
     */
    @Query("SELECT wo.status, COUNT(wo) as orderCount, SUM(wo.plannedQuantity) as totalQuantity " +
           "FROM WorkOrder wo GROUP BY wo.status ORDER BY orderCount DESC")
    List<Object[]> getWorkOrdersByStatus();
    
    /**
     * Get work orders grouped by priority
     */
    @Query("SELECT wo.priority, COUNT(wo) as orderCount, SUM(wo.plannedQuantity) as totalQuantity " +
           "FROM WorkOrder wo WHERE wo.status NOT IN ('COMPLETED', 'CANCELLED') " +
           "GROUP BY wo.priority ORDER BY orderCount DESC")
    List<Object[]> getWorkOrdersByPriority();
    
    /**
     * Get work orders grouped by finished product
     */
    @Query("SELECT wo.finishedProductId, COUNT(wo) as orderCount, " +
           "SUM(wo.plannedQuantity) as totalPlanned, SUM(wo.actualQuantity) as totalActual " +
           "FROM WorkOrder wo GROUP BY wo.finishedProductId ORDER BY orderCount DESC")
    List<Object[]> getWorkOrdersByProduct();
    
    /**
     * Get work orders grouped by supervisor
     */
    @Query("SELECT wo.supervisorId, COUNT(wo) as orderCount, " +
           "SUM(wo.plannedQuantity) as totalPlanned " +
           "FROM WorkOrder wo WHERE wo.supervisorId IS NOT NULL " +
           "GROUP BY wo.supervisorId ORDER BY orderCount DESC")
    List<Object[]> getWorkOrdersBySupervisor();
    
    /**
     * Get daily production summary
     */
    @Query("SELECT wo.woDate, COUNT(wo) as orderCount, " +
           "SUM(wo.plannedQuantity) as totalPlanned, SUM(wo.actualQuantity) as totalActual " +
           "FROM WorkOrder wo WHERE wo.woDate BETWEEN :startDate AND :endDate " +
           "GROUP BY wo.woDate ORDER BY wo.woDate DESC")
    List<Object[]> getDailyProductionSummary(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find today's work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.woDate = CURRENT_DATE ORDER BY wo.createdAt DESC")
    List<WorkOrder> findTodayWorkOrders();
    
    /**
     * Find work orders by manufacturing date range
     */
    List<WorkOrder> findByManufacturingDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get total production cost by product
     */
    @Query("SELECT wo.finishedProductId, SUM(wo.totalCost) as totalCost " +
           "FROM WorkOrder wo WHERE wo.status = 'COMPLETED' " +
           "GROUP BY wo.finishedProductId ORDER BY totalCost DESC")
    List<Object[]> getTotalCostByProduct();
    
    /**
     * Find all work orders ordered by date
     */
    List<WorkOrder> findAllByOrderByWoDateDescCreatedAtDesc();
    
    /**
     * Find work orders with variance (actual != planned)
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.actualQuantity <> wo.plannedQuantity " +
           "AND wo.status = 'COMPLETED'")
    List<WorkOrder> findWorkOrdersWithVariance();
    
    /**
     * Get production efficiency by product
     */
    @Query("SELECT wo.finishedProductId, " +
           "SUM(wo.plannedQuantity) as totalPlanned, " +
           "SUM(wo.actualQuantity) as totalActual, " +
           "(SUM(wo.actualQuantity) / SUM(wo.plannedQuantity) * 100) as efficiency " +
           "FROM WorkOrder wo WHERE wo.status = 'COMPLETED' AND wo.plannedQuantity > 0 " +
           "GROUP BY wo.finishedProductId")
    List<Object[]> getProductionEfficiencyByProduct();
}
