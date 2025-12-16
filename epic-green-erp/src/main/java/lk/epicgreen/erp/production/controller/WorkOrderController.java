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
 * WorkOrder Controller
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
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<WorkOrder>> createWorkOrder(@Valid @RequestBody WorkOrderRequest request) {
        WorkOrder created = workOrderService.createWorkOrder(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Work order created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<WorkOrder>> updateWorkOrder(
        @PathVariable Long id,
        @Valid @RequestBody WorkOrderRequest request
    ) {
        WorkOrder updated = workOrderService.updateWorkOrder(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Work order updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteWorkOrder(@PathVariable Long id) {
        workOrderService.deleteWorkOrder(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Work order deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<WorkOrder>> getWorkOrderById(@PathVariable Long id) {
        WorkOrder workOrder = workOrderService.getWorkOrderById(id);
        return ResponseEntity.ok(ApiResponse.success(workOrder, "Work order retrieved successfully"));
    }
    
    @GetMapping("/number/{workOrderNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<WorkOrder>> getWorkOrderByNumber(@PathVariable String workOrderNumber) {
        WorkOrder workOrder = workOrderService.getWorkOrderByNumber(workOrderNumber);
        return ResponseEntity.ok(ApiResponse.success(workOrder, "Work order retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<Page<WorkOrder>>> getAllWorkOrders(Pageable pageable) {
        Page<WorkOrder> workOrders = workOrderService.getAllWorkOrders(pageable);
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Work orders retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<Page<WorkOrder>>> searchWorkOrders(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<WorkOrder> workOrders = workOrderService.searchWorkOrders(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Search results retrieved"));
    }
    
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<WorkOrder>> approveWorkOrder(
        @PathVariable Long id,
        @RequestParam Long approvedByUserId,
        @RequestParam(required = false) String approvalNotes
    ) {
        WorkOrder approved = workOrderService.approveWorkOrder(id, approvedByUserId, approvalNotes);
        return ResponseEntity.ok(ApiResponse.success(approved, "Work order approved"));
    }
    
    @PostMapping("/{id}/start")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<WorkOrder>> startWorkOrder(
        @PathVariable Long id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate actualStartDate
    ) {
        WorkOrder started = workOrderService.startWorkOrder(id, actualStartDate);
        return ResponseEntity.ok(ApiResponse.success(started, "Work order started"));
    }
    
    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<WorkOrder>> completeWorkOrder(
        @PathVariable Long id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate actualEndDate,
        @RequestParam Double actualQuantity
    ) {
        WorkOrder completed = workOrderService.completeWorkOrder(id, actualEndDate, actualQuantity);
        return ResponseEntity.ok(ApiResponse.success(completed, "Work order completed"));
    }
    
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<WorkOrder>> cancelWorkOrder(
        @PathVariable Long id,
        @RequestParam String cancellationReason
    ) {
        WorkOrder cancelled = workOrderService.cancelWorkOrder(id, cancellationReason);
        return ResponseEntity.ok(ApiResponse.success(cancelled, "Work order cancelled"));
    }
    
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<WorkOrder>> rejectWorkOrder(
        @PathVariable Long id,
        @RequestParam String rejectionReason
    ) {
        WorkOrder rejected = workOrderService.rejectWorkOrder(id, rejectionReason);
        return ResponseEntity.ok(ApiResponse.success(rejected, "Work order rejected"));
    }
    
    @GetMapping("/draft")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getDraftWorkOrders() {
        List<WorkOrder> workOrders = workOrderService.getDraftWorkOrders();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Draft work orders retrieved"));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getPendingWorkOrders() {
        List<WorkOrder> workOrders = workOrderService.getPendingWorkOrders();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Pending work orders retrieved"));
    }
    
    @GetMapping("/approved")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getApprovedWorkOrders() {
        List<WorkOrder> workOrders = workOrderService.getApprovedWorkOrders();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Approved work orders retrieved"));
    }
    
    @GetMapping("/in-progress")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getInProgressWorkOrders() {
        List<WorkOrder> workOrders = workOrderService.getInProgressWorkOrders();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "In-progress work orders retrieved"));
    }
    
    @GetMapping("/completed")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getCompletedWorkOrders() {
        List<WorkOrder> workOrders = workOrderService.getCompletedWorkOrders();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Completed work orders retrieved"));
    }
    
    @GetMapping("/pending-approval")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getWorkOrdersPendingApproval() {
        List<WorkOrder> workOrders = workOrderService.getWorkOrdersPendingApproval();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Work orders pending approval retrieved"));
    }
    
    @GetMapping("/overdue")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getOverdueWorkOrders() {
        List<WorkOrder> workOrders = workOrderService.getOverdueWorkOrders();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Overdue work orders retrieved"));
    }
    
    @GetMapping("/high-priority")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getHighPriorityWorkOrders() {
        List<WorkOrder> workOrders = workOrderService.getHighPriorityWorkOrders();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "High priority work orders retrieved"));
    }
    
    @GetMapping("/today")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getTodaysWorkOrders() {
        List<WorkOrder> workOrders = workOrderService.getTodaysWorkOrders();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Today's work orders retrieved"));
    }
    
    @GetMapping("/requiring-action")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getWorkOrdersRequiringAction() {
        List<WorkOrder> workOrders = workOrderService.getWorkOrdersRequiringAction();
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Work orders requiring action retrieved"));
    }
    
    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getWorkOrdersByProduct(@PathVariable Long productId) {
        List<WorkOrder> workOrders = workOrderService.getWorkOrdersByProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Product work orders retrieved"));
    }
    
    @GetMapping("/production-line/{productionLineId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getWorkOrdersByProductionLine(@PathVariable Long productionLineId) {
        List<WorkOrder> workOrders = workOrderService.getWorkOrdersByProductionLine(productionLineId);
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Production line work orders retrieved"));
    }
    
    @GetMapping("/supervisor/{supervisorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getWorkOrdersBySupervisor(@PathVariable Long supervisorId) {
        List<WorkOrder> workOrders = workOrderService.getWorkOrdersBySupervisor(supervisorId);
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Supervisor work orders retrieved"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getRecentWorkOrders(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<WorkOrder> workOrders = workOrderService.getRecentWorkOrders(limit);
        return ResponseEntity.ok(ApiResponse.success(workOrders, "Recent work orders retrieved"));
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = workOrderService.getWorkOrderStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = workOrderService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
}
