package lk.epicgreen.erp.production.repository;

import lk.epicgreen.erp.production.entity.WorkOrder;
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
 * WorkOrder Repository
 * Repository for work order data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find work order by work order number
     */
    Optional<WorkOrder> findByWorkOrderNumber(String workOrderNumber);
    
    /**
     * Check if work order exists by work order number
     */
    boolean existsByWorkOrderNumber(String workOrderNumber);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find work orders by product ID
     */
    List<WorkOrder> findByProductId(Long productId);
    
    /**
     * Find work orders by product ID with pagination
     */
    Page<WorkOrder> findByProductId(Long productId, Pageable pageable);
    
    /**
     * Find work orders by BOM ID
     */
    List<WorkOrder> findByBomId(Long bomId);
    
    /**
     * Find work orders by sales order ID
     */
    List<WorkOrder> findBySalesOrderId(Long salesOrderId);
    
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
     * Find work orders by work order type
     */
    List<WorkOrder> findByWorkOrderType(String workOrderType);
    
    /**
     * Find work orders by production line ID
     */
    List<WorkOrder> findByProductionLineId(Long productionLineId);
    
    /**
     * Find work orders by supervisor ID
     */
    List<WorkOrder> findBySupervisorId(Long supervisorId);
    
    /**
     * Find work orders by created by user
     */
    List<WorkOrder> findByCreatedByUserId(Long userId);
    
    /**
     * Find work orders by approved by user
     */
    List<WorkOrder> findByApprovedByUserId(Long userId);
    
    /**
     * Find work orders by is approved
     */
    List<WorkOrder> findByIsApproved(Boolean isApproved);
    
    /**
     * Find work orders by is completed
     */
    List<WorkOrder> findByIsCompleted(Boolean isCompleted);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find work orders by planned start date between dates
     */
    List<WorkOrder> findByPlannedStartDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find work orders by planned end date between dates
     */
    List<WorkOrder> findByPlannedEndDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find work orders by actual start date between dates
     */
    List<WorkOrder> findByActualStartDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find work orders by actual end date between dates
     */
    List<WorkOrder> findByActualEndDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find work orders by created at between dates
     */
    List<WorkOrder> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find work orders by product ID and status
     */
    List<WorkOrder> findByProductIdAndStatus(Long productId, String status);
    
    /**
     * Find work orders by status and priority
     */
    List<WorkOrder> findByStatusAndPriority(String status, String priority);
    
    /**
     * Find work orders by production line and status
     */
    List<WorkOrder> findByProductionLineIdAndStatus(Long productionLineId, String status);
    
    /**
     * Find work orders by supervisor and status
     */
    List<WorkOrder> findBySupervisorIdAndStatus(Long supervisorId, String status);
    
    /**
     * Find work orders by is approved and status
     */
    List<WorkOrder> findByIsApprovedAndStatus(Boolean isApproved, String status);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE " +
           "LOWER(wo.workOrderNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(wo.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(wo.notes) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<WorkOrder> searchWorkOrders(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find draft work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.status = 'DRAFT' " +
           "ORDER BY wo.createdAt DESC")
    List<WorkOrder> findDraftWorkOrders();
    
    /**
     * Find pending work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.status = 'PENDING' " +
           "ORDER BY wo.plannedStartDate ASC")
    List<WorkOrder> findPendingWorkOrders();
    
    /**
     * Find pending work orders with pagination
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.status = 'PENDING' " +
           "ORDER BY wo.plannedStartDate ASC")
    Page<WorkOrder> findPendingWorkOrders(Pageable pageable);
    
    /**
     * Find approved work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.status = 'APPROVED' AND wo.isApproved = true " +
           "ORDER BY wo.plannedStartDate ASC")
    List<WorkOrder> findApprovedWorkOrders();
    
    /**
     * Find in progress work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.status = 'IN_PROGRESS' " +
           "ORDER BY wo.actualStartDate ASC")
    List<WorkOrder> findInProgressWorkOrders();
    
    /**
     * Find completed work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.status = 'COMPLETED' AND wo.isCompleted = true " +
           "ORDER BY wo.actualEndDate DESC")
    List<WorkOrder> findCompletedWorkOrders();
    
    /**
     * Find cancelled work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.status = 'CANCELLED' " +
           "ORDER BY wo.createdAt DESC")
    List<WorkOrder> findCancelledWorkOrders();
    
    /**
     * Find work orders pending approval
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.isApproved = false " +
           "AND wo.status NOT IN ('DRAFT', 'CANCELLED') " +
           "ORDER BY wo.createdAt ASC")
    List<WorkOrder> findWorkOrdersPendingApproval();
    
    /**
     * Find overdue work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.plannedEndDate < :currentDate " +
           "AND wo.status IN ('APPROVED', 'IN_PROGRESS') " +
           "AND wo.isCompleted = false " +
           "ORDER BY wo.plannedEndDate ASC")
    List<WorkOrder> findOverdueWorkOrders(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Find high priority work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.priority = 'HIGH' " +
           "AND wo.status NOT IN ('COMPLETED', 'CANCELLED') " +
           "ORDER BY wo.plannedStartDate ASC")
    List<WorkOrder> findHighPriorityWorkOrders();
    
    /**
     * Find today's work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.plannedStartDate = :today " +
           "AND wo.status NOT IN ('CANCELLED', 'COMPLETED') " +
           "ORDER BY wo.priority DESC, wo.plannedStartDate ASC")
    List<WorkOrder> findTodaysWorkOrders(@Param("today") LocalDate today);
    
    /**
     * Find production line work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.productionLineId = :productionLineId " +
           "AND wo.status IN ('APPROVED', 'IN_PROGRESS') " +
           "ORDER BY wo.priority DESC, wo.plannedStartDate ASC")
    List<WorkOrder> findProductionLineWorkOrders(@Param("productionLineId") Long productionLineId);
    
    /**
     * Find supervisor work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.supervisorId = :supervisorId " +
           "AND wo.status IN ('APPROVED', 'IN_PROGRESS') " +
           "ORDER BY wo.priority DESC, wo.plannedStartDate ASC")
    List<WorkOrder> findSupervisorWorkOrders(@Param("supervisorId") Long supervisorId);
    
    /**
     * Find recent work orders
     */
    @Query("SELECT wo FROM WorkOrder wo ORDER BY wo.createdAt DESC")
    List<WorkOrder> findRecentWorkOrders(Pageable pageable);
    
    /**
     * Find product recent work orders
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.productId = :productId " +
           "ORDER BY wo.createdAt DESC")
    List<WorkOrder> findProductRecentWorkOrders(@Param("productId") Long productId, Pageable pageable);
    
    /**
     * Find work orders by date range and status
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.plannedStartDate BETWEEN :startDate AND :endDate " +
           "AND wo.status = :status ORDER BY wo.plannedStartDate ASC")
    List<WorkOrder> findByDateRangeAndStatus(@Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate,
                                             @Param("status") String status);
    
    /**
     * Find work orders requiring action
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE " +
           "(wo.isApproved = false AND wo.status = 'PENDING') OR " +
           "(wo.status = 'APPROVED' AND wo.plannedStartDate <= :currentDate) OR " +
           "(wo.plannedEndDate < :overdueDate AND wo.status = 'IN_PROGRESS') " +
           "ORDER BY wo.plannedStartDate ASC")
    List<WorkOrder> findWorkOrdersRequiringAction(@Param("currentDate") LocalDate currentDate,
                                                  @Param("overdueDate") LocalDate overdueDate);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count work orders by product
     */
    @Query("SELECT COUNT(wo) FROM WorkOrder wo WHERE wo.productId = :productId")
    Long countByProductId(@Param("productId") Long productId);
    
    /**
     * Count work orders by production line
     */
    @Query("SELECT COUNT(wo) FROM WorkOrder wo WHERE wo.productionLineId = :productionLineId")
    Long countByProductionLineId(@Param("productionLineId") Long productionLineId);
    
    /**
     * Count work orders by status
     */
    @Query("SELECT COUNT(wo) FROM WorkOrder wo WHERE wo.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count pending work orders
     */
    @Query("SELECT COUNT(wo) FROM WorkOrder wo WHERE wo.status = 'PENDING'")
    Long countPendingWorkOrders();
    
    /**
     * Count work orders pending approval
     */
    @Query("SELECT COUNT(wo) FROM WorkOrder wo WHERE wo.isApproved = false " +
           "AND wo.status NOT IN ('DRAFT', 'CANCELLED')")
    Long countWorkOrdersPendingApproval();
    
    /**
     * Count in progress work orders
     */
    @Query("SELECT COUNT(wo) FROM WorkOrder wo WHERE wo.status = 'IN_PROGRESS'")
    Long countInProgressWorkOrders();
    
    /**
     * Count overdue work orders
     */
    @Query("SELECT COUNT(wo) FROM WorkOrder wo WHERE wo.plannedEndDate < :currentDate " +
           "AND wo.status IN ('APPROVED', 'IN_PROGRESS')")
    Long countOverdueWorkOrders(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Get work order type distribution
     */
    @Query("SELECT wo.workOrderType, COUNT(wo) as workOrderCount FROM WorkOrder wo " +
           "GROUP BY wo.workOrderType ORDER BY workOrderCount DESC")
    List<Object[]> getWorkOrderTypeDistribution();
    
    /**
     * Get status distribution
     */
    @Query("SELECT wo.status, COUNT(wo) as workOrderCount FROM WorkOrder wo " +
           "GROUP BY wo.status ORDER BY workOrderCount DESC")
    List<Object[]> getStatusDistribution();
    
    /**
     * Get priority distribution
     */
    @Query("SELECT wo.priority, COUNT(wo) as workOrderCount FROM WorkOrder wo " +
           "GROUP BY wo.priority ORDER BY workOrderCount DESC")
    List<Object[]> getPriorityDistribution();
    
    /**
     * Get monthly work order count
     */
    @Query("SELECT YEAR(wo.plannedStartDate) as year, MONTH(wo.plannedStartDate) as month, " +
           "COUNT(wo) as workOrderCount FROM WorkOrder wo " +
           "WHERE wo.plannedStartDate BETWEEN :startDate AND :endDate " +
           "GROUP BY YEAR(wo.plannedStartDate), MONTH(wo.plannedStartDate) " +
           "ORDER BY year, month")
    List<Object[]> getMonthlyWorkOrderCount(@Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);
    
    /**
     * Get total planned quantity
     */
    @Query("SELECT SUM(wo.plannedQuantity) FROM WorkOrder wo " +
           "WHERE wo.status NOT IN ('DRAFT', 'CANCELLED')")
    Double getTotalPlannedQuantity();
    
    /**
     * Get total actual quantity
     */
    @Query("SELECT SUM(wo.actualQuantity) FROM WorkOrder wo WHERE wo.isCompleted = true")
    Double getTotalActualQuantity();
    
    /**
     * Get production efficiency
     */
    @Query("SELECT " +
           "(SELECT SUM(wo.actualQuantity) FROM WorkOrder wo WHERE wo.isCompleted = true) * 100.0 / " +
           "(SELECT SUM(wo.plannedQuantity) FROM WorkOrder wo WHERE wo.isCompleted = true) " +
           "FROM WorkOrder wo WHERE wo.isCompleted = true")
    Double getProductionEfficiency();
    
    /**
     * Get on-time completion rate
     */
    @Query("SELECT " +
           "(SELECT COUNT(wo) FROM WorkOrder wo WHERE wo.isCompleted = true " +
           "AND wo.actualEndDate <= wo.plannedEndDate) * 100.0 / " +
           "(SELECT COUNT(wo) FROM WorkOrder wo WHERE wo.isCompleted = true) " +
           "FROM WorkOrder wo WHERE wo.isCompleted = true")
    Double getOnTimeCompletionRate();
    
    /**
     * Get production line performance
     */
    @Query("SELECT wo.productionLineId, wo.productionLineName, COUNT(wo) as workOrderCount, " +
           "SUM(wo.actualQuantity) as totalProduced FROM WorkOrder wo " +
           "WHERE wo.isCompleted = true " +
           "GROUP BY wo.productionLineId, wo.productionLineName ORDER BY totalProduced DESC")
    List<Object[]> getProductionLinePerformance();
    
    /**
     * Find work orders by tags
     */
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.tags LIKE CONCAT('%', :tag, '%')")
    List<WorkOrder> findByTag(@Param("tag") String tag);
}
