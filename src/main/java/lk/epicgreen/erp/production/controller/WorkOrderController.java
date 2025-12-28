package lk.epicgreen.erp.production.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.production.dto.WorkOrderRequest;
import lk.epicgreen.erp.production.entity.WorkOrder;
import lk.epicgreen.erp.production.service.WorkOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Work Order Controller
 * REST controller for work order operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/production/work-orders")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class WorkOrderController {
    
    private final WorkOrderService workOrderService;
    
    // CRUD Operations
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<WorkOrder>> createWorkOrder(@Valid @RequestBody WorkOrderRequest request) {
        log.info("Creating work order for product: {}", request.getProductId());
        WorkOrder created = workOrderService.createWorkOrder(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Work order created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<WorkOrder>> updateWorkOrder(@PathVariable Long id, @Valid @RequestBody WorkOrderRequest request) {
        log.info("Updating work order: {}", id);
        WorkOrder updated = workOrderService.updateWorkOrder(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Work order updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteWorkOrder(@PathVariable Long id) {
        log.info("Deleting work order: {}", id);
        workOrderService.deleteWorkOrder(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Work order deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<WorkOrder>> getWorkOrderById(@PathVariable Long id) {
        WorkOrder workOrder = workOrderService.getWorkOrderById(id);
        return ResponseEntity.ok(ApiResponse.success(workOrder, "Work order retrieved successfully"));
    }
    
    @GetMapping("/number/{workOrderNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<WorkOrder>> getWorkOrderByNumber(@PathVariable String workOrderNumber) {
        WorkOrder workOrder = workOrderService.getWorkOrderByNumber(workOrderNumber);
        return ResponseEntity.ok(ApiResponse.success(workOrder, "Work order retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<WorkOrder>>> getAllWorkOrders(Pageable pageable) {
        Page<WorkOrder> workOrders = workOrderService.getAllWorkOrders(pageable);
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Work orders retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getAllWorkOrdersList() {
        List<WorkOrder> workOrders = workOrderService.getAllWorkOrders();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Work orders list retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<Page<WorkOrder>>> searchWorkOrders(@RequestParam String keyword, Pageable pageable) {
        Page<WorkOrder> workOrders = workOrderService.searchWorkOrders(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Search results retrieved successfully"));
    }
    
    // Status Operations
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<WorkOrder>> approveWorkOrder(
        @PathVariable Long id,
        @RequestParam Long approvedByUserId,
        @RequestParam(required = false) String approvalNotes
    ) {
        log.info("Approving work order: {}", id);
        WorkOrder approved = workOrderService.approveWorkOrder(id, approvedByUserId, approvalNotes);
        return ResponseEntity.ok(ApiResponse.success(approved, "Work order approved successfully"));
    }
    
    @PutMapping("/{id}/start")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<WorkOrder>> startWorkOrder(
        @PathVariable Long id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate actualStartDate
    ) {
        log.info("Starting work order: {}", id);
        WorkOrder started = workOrderService.startWorkOrder(id, actualStartDate);
        return ResponseEntity.ok(ApiResponse.success(started, "Work order started successfully"));
    }
    
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<WorkOrder>> completeWorkOrder(
        @PathVariable Long id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate actualEndDate,
        @RequestParam Double actualQuantity
    ) {
        log.info("Completing work order: {}", id);
        WorkOrder completed = workOrderService.completeWorkOrder(id, actualEndDate, actualQuantity);
        return ResponseEntity.ok(ApiResponse.success(completed, "Work order completed successfully"));
    }
    
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<WorkOrder>> cancelWorkOrder(@PathVariable Long id, @RequestParam String cancellationReason) {
        log.info("Cancelling work order: {}", id);
        WorkOrder cancelled = workOrderService.cancelWorkOrder(id, cancellationReason);
        return ResponseEntity.ok(ApiResponse.success(cancelled, "Work order cancelled successfully"));
    }
    
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<WorkOrder>> rejectWorkOrder(@PathVariable Long id, @RequestParam String rejectionReason) {
        log.info("Rejecting work order: {}", id);
        WorkOrder rejected = workOrderService.rejectWorkOrder(id, rejectionReason);
        return ResponseEntity.ok(ApiResponse.success(rejected, "Work order rejected successfully"));
    }
    
    // Production Operations
    @PutMapping("/{workOrderId}/update-quantity")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<Void>> updateActualQuantity(@PathVariable Long workOrderId, @RequestParam Double quantity) {
        log.info("Updating actual quantity for work order: {}", workOrderId);
        workOrderService.updateActualQuantity(workOrderId, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Actual quantity updated successfully"));
    }
    
    @PostMapping("/{workOrderId}/record-material")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<Void>> recordMaterialConsumption(
        @PathVariable Long workOrderId,
        @RequestParam Long materialId,
        @RequestParam Double quantity
    ) {
        log.info("Recording material consumption for work order: {}", workOrderId);
        workOrderService.recordMaterialConsumption(workOrderId, materialId, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Material consumption recorded successfully"));
    }
    
    @PostMapping("/{workOrderId}/record-labour")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<Void>> recordLabourHours(@PathVariable Long workOrderId, @RequestParam Double hours) {
        log.info("Recording labour hours for work order: {}", workOrderId);
        workOrderService.recordLabourHours(workOrderId, hours);
        return ResponseEntity.ok(ApiResponse.success(null, "Labour hours recorded successfully"));
    }
    
    // Query Operations
    @GetMapping("/draft")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getDraftWorkOrders() {
        List<WorkOrder> workOrders = workOrderService.getDraftWorkOrders();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Draft work orders retrieved successfully"));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getPendingWorkOrders() {
        List<WorkOrder> workOrders = workOrderService.getPendingWorkOrders();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Pending work orders retrieved successfully"));
    }
    
    @GetMapping("/approved")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getApprovedWorkOrders() {
        List<WorkOrder> workOrders = workOrderService.getApprovedWorkOrders();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Approved work orders retrieved successfully"));
    }
    
    @GetMapping("/in-progress")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getInProgressWorkOrders() {
        List<WorkOrder> workOrders = workOrderService.getInProgressWorkOrders();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "In-progress work orders retrieved successfully"));
    }
    
    @GetMapping("/completed")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getCompletedWorkOrders() {
        List<WorkOrder> workOrders = workOrderService.getCompletedWorkOrders();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Completed work orders retrieved successfully"));
    }
    
    @GetMapping("/cancelled")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getCancelledWorkOrders() {
        List<WorkOrder> workOrders = workOrderService.getCancelledWorkOrders();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Cancelled work orders retrieved successfully"));
    }
    
    @GetMapping("/pending-approval")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getWorkOrdersPendingApproval() {
        List<WorkOrder> workOrders = workOrderService.getWorkOrdersPendingApproval();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Work orders pending approval retrieved successfully"));
    }
    
    @GetMapping("/overdue")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getOverdueWorkOrders() {
        List<WorkOrder> workOrders = workOrderService.getOverdueWorkOrders();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Overdue work orders retrieved successfully"));
    }
    
    @GetMapping("/high-priority")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getHighPriorityWorkOrders() {
        List<WorkOrder> workOrders = workOrderService.getHighPriorityWorkOrders();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "High priority work orders retrieved successfully"));
    }
    
    @GetMapping("/todays")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getTodaysWorkOrders() {
        List<WorkOrder> workOrders = workOrderService.getTodaysWorkOrders();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Today's work orders retrieved successfully"));
    }
    
    @GetMapping("/requiring-action")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getWorkOrdersRequiringAction() {
        List<WorkOrder> workOrders = workOrderService.getWorkOrdersRequiringAction();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Work orders requiring action retrieved successfully"));
    }
    
    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getWorkOrdersByProduct(@PathVariable Long productId) {
        List<WorkOrder> workOrders = workOrderService.getWorkOrdersByProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Work orders by product retrieved successfully"));
    }
    
    @GetMapping("/production-line/{productionLineId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getWorkOrdersByProductionLine(@PathVariable Long productionLineId) {
        List<WorkOrder> workOrders = workOrderService.getWorkOrdersByProductionLine(productionLineId);
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Work orders by production line retrieved successfully"));
    }
    
    @GetMapping("/supervisor/{supervisorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getWorkOrdersBySupervisor(@PathVariable Long supervisorId) {
        List<WorkOrder> workOrders = workOrderService.getWorkOrdersBySupervisor(supervisorId);
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Work orders by supervisor retrieved successfully"));
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getWorkOrdersByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<WorkOrder> workOrders = workOrderService.getWorkOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Work orders by date range retrieved successfully"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getRecentWorkOrders(@RequestParam(defaultValue = "10") int limit) {
        List<WorkOrder> workOrders = workOrderService.getRecentWorkOrders(limit);
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Recent work orders retrieved successfully"));
    }
    
    // Validation
    @GetMapping("/{id}/can-approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> canApproveWorkOrder(@PathVariable Long id) {
        boolean canApprove = workOrderService.canApproveWorkOrder(id);
        return ResponseEntity.ok(ApiResponse.success(canApprove, "Approval check completed"));
    }
    
    @GetMapping("/{id}/can-start")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<Boolean>> canStartWorkOrder(@PathVariable Long id) {
        boolean canStart = workOrderService.canStartWorkOrder(id);
        return ResponseEntity.ok(ApiResponse.success(canStart, "Start check completed"));
    }
    
    @GetMapping("/{id}/can-complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<Boolean>> canCompleteWorkOrder(@PathVariable Long id) {
        boolean canComplete = workOrderService.canCompleteWorkOrder(id);
        return ResponseEntity.ok(ApiResponse.success(canComplete, "Complete check completed"));
    }
    
    @GetMapping("/{id}/can-cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> canCancelWorkOrder(@PathVariable Long id) {
        boolean canCancel = workOrderService.canCancelWorkOrder(id);
        return ResponseEntity.ok(ApiResponse.success(canCancel, "Cancel check completed"));
    }
    
    // Calculations
    @GetMapping("/{id}/completion-percentage")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<Double>> calculateCompletionPercentage(@PathVariable Long id) {
        Double percentage = workOrderService.calculateCompletionPercentage(id);
        return ResponseEntity.ok(ApiResponse.success(percentage, "Completion percentage calculated successfully"));
    }
    
    @GetMapping("/{id}/variance")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<Double>> calculateVariance(@PathVariable Long id) {
        Double variance = workOrderService.calculateVariance(id);
        return ResponseEntity.ok(ApiResponse.success(variance, "Variance calculated successfully"));
    }
    
    @GetMapping("/{id}/metrics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> calculateWorkOrderMetrics(@PathVariable Long id) {
        Map<String, Object> metrics = workOrderService.calculateWorkOrderMetrics(id);
        return ResponseEntity.ok(ApiResponse.success(metrics, "Work order metrics calculated successfully"));
    }
    
    // Batch Operations
    @PostMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> createBulkWorkOrders(@Valid @RequestBody List<WorkOrderRequest> requests) {
        log.info("Creating {} work orders in bulk", requests.size());
        List<WorkOrder> workOrders = workOrderService.createBulkWorkOrders(requests);
        return ResponseEntity.ok(ApiResponse.success(workOrders, workOrders.size() + " work orders created successfully"));
    }
    
    @PutMapping("/bulk/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> approveBulkWorkOrders(@RequestBody List<Long> workOrderIds, @RequestParam Long approvedByUserId) {
        log.info("Approving {} work orders in bulk", workOrderIds.size());
        int count = workOrderService.approveBulkWorkOrders(workOrderIds, approvedByUserId);
        return ResponseEntity.ok(ApiResponse.success(count, count + " work orders approved successfully"));
    }
    
    @DeleteMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> deleteBulkWorkOrders(@RequestBody List<Long> workOrderIds) {
        log.info("Deleting {} work orders in bulk", workOrderIds.size());
        int count = workOrderService.deleteBulkWorkOrders(workOrderIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " work orders deleted successfully"));
    }
    
    // Statistics
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getWorkOrderStatistics() {
        Map<String, Object> statistics = workOrderService.getWorkOrderStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Work order statistics retrieved successfully"));
    }
    
    @GetMapping("/statistics/type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getWorkOrderTypeDistribution() {
        List<Map<String, Object>> distribution = workOrderService.getWorkOrderTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Work order type distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/status-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getStatusDistribution() {
        List<Map<String, Object>> distribution = workOrderService.getStatusDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Status distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/priority-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getPriorityDistribution() {
        List<Map<String, Object>> distribution = workOrderService.getPriorityDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Priority distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/monthly")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMonthlyWorkOrderCount(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<Map<String, Object>> count = workOrderService.getMonthlyWorkOrderCount(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(count, "Monthly work order count retrieved successfully"));
    }
    
    @GetMapping("/statistics/production-line-performance")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getProductionLinePerformance() {
        List<Map<String, Object>> performance = workOrderService.getProductionLinePerformance();
        return ResponseEntity.ok(ApiResponse.success(performance, "Production line performance retrieved successfully"));
    }
    
    @GetMapping("/statistics/production-efficiency")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getProductionEfficiency() {
        Double efficiency = workOrderService.getProductionEfficiency();
        return ResponseEntity.ok(ApiResponse.success(efficiency, "Production efficiency retrieved successfully"));
    }
    
    @GetMapping("/statistics/on-time-completion-rate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getOnTimeCompletionRate() {
        Double rate = workOrderService.getOnTimeCompletionRate();
        return ResponseEntity.ok(ApiResponse.success(rate, "On-time completion rate retrieved successfully"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = workOrderService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved successfully"));
    }
}
