package lk.epicgreen.erp.production.service;

import lk.epicgreen.erp.production.dto.WorkOrderRequest;
import lk.epicgreen.erp.production.entity.WorkOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * WorkOrder Service Interface
 * Service for work order operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface WorkOrderService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    WorkOrder createWorkOrder(WorkOrderRequest request);
    WorkOrder updateWorkOrder(Long id, WorkOrderRequest request);
    void deleteWorkOrder(Long id);
    WorkOrder getWorkOrderById(Long id);
    WorkOrder getWorkOrderByNumber(String workOrderNumber);
    List<WorkOrder> getAllWorkOrders();
    Page<WorkOrder> getAllWorkOrders(Pageable pageable);
    Page<WorkOrder> searchWorkOrders(String keyword, Pageable pageable);
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    WorkOrder approveWorkOrder(Long id, Long approvedByUserId, String approvalNotes);
    WorkOrder startWorkOrder(Long id, LocalDate actualStartDate);
    WorkOrder completeWorkOrder(Long id, LocalDate actualEndDate, Double actualQuantity);
    WorkOrder cancelWorkOrder(Long id, String cancellationReason);
    WorkOrder rejectWorkOrder(Long id, String rejectionReason);
    
    // ===================================================================
    // PRODUCTION OPERATIONS
    // ===================================================================
    
    void updateActualQuantity(Long workOrderId, Double quantity);
    void recordMaterialConsumption(Long workOrderId, Long materialId, Double quantity);
    void recordLabourHours(Long workOrderId, Double hours);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    List<WorkOrder> getDraftWorkOrders();
    List<WorkOrder> getPendingWorkOrders();
    List<WorkOrder> getApprovedWorkOrders();
    List<WorkOrder> getInProgressWorkOrders();
    List<WorkOrder> getCompletedWorkOrders();
    List<WorkOrder> getCancelledWorkOrders();
    List<WorkOrder> getWorkOrdersPendingApproval();
    List<WorkOrder> getOverdueWorkOrders();
    List<WorkOrder> getHighPriorityWorkOrders();
    List<WorkOrder> getTodaysWorkOrders();
    List<WorkOrder> getWorkOrdersRequiringAction();
    List<WorkOrder> getWorkOrdersByProduct(Long productId);
    List<WorkOrder> getWorkOrdersByProductionLine(Long productionLineId);
    List<WorkOrder> getWorkOrdersBySupervisor(Long supervisorId);
    List<WorkOrder> getWorkOrdersByDateRange(LocalDate startDate, LocalDate endDate);
    List<WorkOrder> getRecentWorkOrders(int limit);
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    boolean validateWorkOrder(WorkOrder workOrder);
    boolean canApproveWorkOrder(Long workOrderId);
    boolean canStartWorkOrder(Long workOrderId);
    boolean canCompleteWorkOrder(Long workOrderId);
    boolean canCancelWorkOrder(Long workOrderId);
    
    // ===================================================================
    // CALCULATIONS
    // ===================================================================
    
    Double calculateCompletionPercentage(Long workOrderId);
    Double calculateVariance(Long workOrderId);
    Map<String, Object> calculateWorkOrderMetrics(Long workOrderId);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<WorkOrder> createBulkWorkOrders(List<WorkOrderRequest> requests);
    int approveBulkWorkOrders(List<Long> workOrderIds, Long approvedByUserId);
    int deleteBulkWorkOrders(List<Long> workOrderIds);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    Map<String, Object> getWorkOrderStatistics();
    List<Map<String, Object>> getWorkOrderTypeDistribution();
    List<Map<String, Object>> getStatusDistribution();
    List<Map<String, Object>> getPriorityDistribution();
    List<Map<String, Object>> getMonthlyWorkOrderCount(LocalDate startDate, LocalDate endDate);
    List<Map<String, Object>> getProductionLinePerformance();
    Double getProductionEfficiency();
    Double getOnTimeCompletionRate();
    Map<String, Object> getDashboardStatistics();
}
