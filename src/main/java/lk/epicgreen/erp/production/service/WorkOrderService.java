package lk.epicgreen.erp.production.service;

import lk.epicgreen.erp.production.dto.request.WorkOrderRequest;
import lk.epicgreen.erp.production.dto.response.WorkOrderResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service interface for WorkOrder entity business logic
 * 
 * WorkOrder Status Workflow:
 * DRAFT → RELEASED → IN_PROGRESS → COMPLETED
 * Can be CANCELLED from any status
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface WorkOrderService {

    /**
     * Create a new Work Order
     */
    WorkOrderResponse createWorkOrder(WorkOrderRequest request);

    /**
     * Update an existing Work Order (only in DRAFT status)
     */
    WorkOrderResponse updateWorkOrder(Long id, WorkOrderRequest request);

    /**
     * Release Work Order (DRAFT → RELEASED)
     * Validates material availability and creates work order items from BOM
     */
    void releaseWorkOrder(Long id);

    /**
     * Start Production (RELEASED → IN_PROGRESS)
     */
    void startProduction(Long id);

    /**
     * Complete Work Order (IN_PROGRESS → COMPLETED)
     * Validates production output exists
     */
    void completeWorkOrder(Long id);

    /**
     * Cancel Work Order (any status → CANCELLED)
     */
    void cancelWorkOrder(Long id, String reason);

    /**
     * Delete Work Order (only in DRAFT status)
     */
    void deleteWorkOrder(Long id);

    /**
     * Assign supervisor to Work Order
     */
    void assignSupervisor(Long workOrderId, Long supervisorId);

    /**
     * Update priority
     */
    void updatePriority(Long id, String priority);

    /**
     * Get Work Order by ID
     */
    WorkOrderResponse getWorkOrderById(Long id);

    /**
     * Get Work Order by number
     */
    WorkOrderResponse getWorkOrderByNumber(String woNumber);

    /**
     * Get all Work Orders (paginated)
     */
    PageResponse<WorkOrderResponse> getAllWorkOrders(Pageable pageable);

    /**
     * Get Work Orders by status
     */
    PageResponse<WorkOrderResponse> getWorkOrdersByStatus(String status, Pageable pageable);

    /**
     * Get Work Orders by priority
     */
    List<WorkOrderResponse> getWorkOrdersByPriority(String priority);

    /**
     * Get Work Orders by finished product
     */
    List<WorkOrderResponse> getWorkOrdersByFinishedProduct(Long finishedProductId);

    /**
     * Get Work Orders by warehouse
     */
    List<WorkOrderResponse> getWorkOrdersByWarehouse(Long warehouseId);

    /**
     * Get Work Orders by supervisor
     */
    List<WorkOrderResponse> getWorkOrdersBySupervisor(Long supervisorId);

    /**
     * Get Work Orders by date range
     */
    List<WorkOrderResponse> getWorkOrdersByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Get overdue Work Orders (expected_completion_date < today and status not COMPLETED/CANCELLED)
     */
    List<WorkOrderResponse> getOverdueWorkOrders();

    /**
     * Get Work Orders due soon (expected_completion_date within specified days)
     */
    List<WorkOrderResponse> getWorkOrdersDueSoon(Integer daysAhead);

    /**
     * Get in-progress Work Orders
     */
    List<WorkOrderResponse> getInProgressWorkOrders();

    /**
     * Get Work Orders by BOM
     */
    List<WorkOrderResponse> getWorkOrdersByBom(Long bomId);

    /**
     * Search Work Orders by keyword
     */
    PageResponse<WorkOrderResponse> searchWorkOrders(String keyword, Pageable pageable);

    /**
     * Check if Work Order can be deleted
     */
    boolean canDelete(Long id);

    /**
     * Check if Work Order can be updated
     */
    boolean canUpdate(Long id);

    /**
     * Calculate production efficiency (actual_quantity / planned_quantity * 100)
     */
    Double calculateProductionEfficiency(Long id);

    List<WorkOrderResponse> getAllWorkOrders();

    WorkOrderResponse approveWorkOrder(Long id, Long approvedByUserId, String approvalNotes);

    WorkOrderResponse startWorkOrder(Long id, LocalDate actualStartDate);

    WorkOrderResponse completeWorkOrder(Long id, LocalDate actualEndDate, Double actualQuantity);

    WorkOrderResponse rejectWorkOrder(Long id, String rejectionReason);

    void updateActualQuantity(Long workOrderId, Double quantity);

    void recordMaterialConsumption(Long workOrderId, Long materialId, Double quantity);

    void recordLabourHours(Long workOrderId, Double hours);

    List<WorkOrderResponse> getDraftWorkOrders();

    List<WorkOrderResponse> getPendingWorkOrders();

    List<WorkOrderResponse> getApprovedWorkOrders();

    List<WorkOrderResponse> getCompletedWorkOrders();

    List<WorkOrderResponse> getCancelledWorkOrders();

    List<WorkOrderResponse> getWorkOrdersPendingApproval();

    List<WorkOrderResponse> getHighPriorityWorkOrders();

    List<WorkOrderResponse> getTodaysWorkOrders();

    List<WorkOrderResponse> getWorkOrdersRequiringAction();

    List<WorkOrderResponse> getWorkOrdersByProduct(Long productId);

    List<WorkOrderResponse> getWorkOrdersByProductionLine(Long productionLineId);

    List<WorkOrderResponse> getRecentWorkOrders(int limit);

    boolean canApproveWorkOrder(Long id);

    boolean canStartWorkOrder(Long id);

    boolean canCompleteWorkOrder(Long id);

    boolean canCancelWorkOrder(Long id);

    Double calculateCompletionPercentage(Long id);

    Double calculateVariance(Long id);

    Map<String, Object> calculateWorkOrderMetrics(Long id);

	List<WorkOrderResponse> createBulkWorkOrders(List<WorkOrderRequest> requests);

	int approveBulkWorkOrders(List<Long> workOrderIds, Long approvedByUserId);

	int deleteBulkWorkOrders(List<Long> workOrderIds);

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
