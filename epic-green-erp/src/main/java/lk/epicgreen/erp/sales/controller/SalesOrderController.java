package lk.epicgreen.erp.sales.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.sales.dto.SalesOrderRequest;
import lk.epicgreen.erp.sales.entity.SalesOrder;
import lk.epicgreen.erp.sales.service.SalesOrderService;
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
 * SalesOrder Controller
 * REST controller for sales order operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/sales/orders")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class SalesOrderController {
    
    private final SalesOrderService salesOrderService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<SalesOrder>> createSalesOrder(@Valid @RequestBody SalesOrderRequest request) {
        SalesOrder created = salesOrderService.createSalesOrder(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Sales order created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<SalesOrder>> updateSalesOrder(
        @PathVariable Long id,
        @Valid @RequestBody SalesOrderRequest request
    ) {
        SalesOrder updated = salesOrderService.updateSalesOrder(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Sales order updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteSalesOrder(@PathVariable Long id) {
        salesOrderService.deleteSalesOrder(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Sales order deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<SalesOrder>> getSalesOrderById(@PathVariable Long id) {
        SalesOrder order = salesOrderService.getSalesOrderById(id);
        return ResponseEntity.ok(ApiResponse.success(order, "Sales order retrieved successfully"));
    }
    
    @GetMapping("/number/{orderNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<SalesOrder>> getSalesOrderByNumber(@PathVariable String orderNumber) {
        SalesOrder order = salesOrderService.getSalesOrderByNumber(orderNumber);
        return ResponseEntity.ok(ApiResponse.success(order, "Sales order retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<SalesOrder>>> getAllSalesOrders(Pageable pageable) {
        Page<SalesOrder> orders = salesOrderService.getAllSalesOrders(pageable);
        return ResponseEntity.ok(ApiResponse.success(orders, "Sales orders retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<SalesOrder>>> searchSalesOrders(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<SalesOrder> orders = salesOrderService.searchSalesOrders(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(orders, "Search results retrieved"));
    }
    
    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<SalesOrder>> confirmSalesOrder(@PathVariable Long id) {
        SalesOrder confirmed = salesOrderService.confirmSalesOrder(id);
        return ResponseEntity.ok(ApiResponse.success(confirmed, "Sales order confirmed"));
    }
    
    @PostMapping("/{id}/process")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<SalesOrder>> processSalesOrder(@PathVariable Long id) {
        SalesOrder processing = salesOrderService.processSalesOrder(id);
        return ResponseEntity.ok(ApiResponse.success(processing, "Sales order processing started"));
    }
    
    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SalesOrder>> completeSalesOrder(@PathVariable Long id) {
        SalesOrder completed = salesOrderService.completeSalesOrder(id);
        return ResponseEntity.ok(ApiResponse.success(completed, "Sales order completed"));
    }
    
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<SalesOrder>> cancelSalesOrder(
        @PathVariable Long id,
        @RequestParam String cancellationReason
    ) {
        SalesOrder cancelled = salesOrderService.cancelSalesOrder(id, cancellationReason);
        return ResponseEntity.ok(ApiResponse.success(cancelled, "Sales order cancelled"));
    }
    
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SalesOrder>> approveSalesOrder(
        @PathVariable Long id,
        @RequestParam Long approvedByUserId,
        @RequestParam(required = false) String approvalNotes
    ) {
        SalesOrder approved = salesOrderService.approveSalesOrder(id, approvedByUserId, approvalNotes);
        return ResponseEntity.ok(ApiResponse.success(approved, "Sales order approved"));
    }
    
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SalesOrder>> rejectSalesOrder(
        @PathVariable Long id,
        @RequestParam String rejectionReason
    ) {
        SalesOrder rejected = salesOrderService.rejectSalesOrder(id, rejectionReason);
        return ResponseEntity.ok(ApiResponse.success(rejected, "Sales order rejected"));
    }
    
    @PostMapping("/{id}/mark-dispatched")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<SalesOrder>> markAsDispatched(
        @PathVariable Long id,
        @RequestParam Long dispatchNoteId
    ) {
        SalesOrder dispatched = salesOrderService.markAsDispatched(id, dispatchNoteId);
        return ResponseEntity.ok(ApiResponse.success(dispatched, "Sales order marked as dispatched"));
    }
    
    @PostMapping("/{id}/mark-invoiced")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<SalesOrder>> markAsInvoiced(
        @PathVariable Long id,
        @RequestParam Long invoiceId
    ) {
        SalesOrder invoiced = salesOrderService.markAsInvoiced(id, invoiceId);
        return ResponseEntity.ok(ApiResponse.success(invoiced, "Sales order marked as invoiced"));
    }
    
    @PostMapping("/{id}/mark-paid")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<SalesOrder>> markAsPaid(@PathVariable Long id) {
        SalesOrder paid = salesOrderService.markAsPaid(id);
        return ResponseEntity.ok(ApiResponse.success(paid, "Sales order marked as paid"));
    }
    
    @GetMapping("/draft")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getDraftOrders() {
        List<SalesOrder> orders = salesOrderService.getDraftOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Draft orders retrieved"));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getPendingOrders() {
        List<SalesOrder> orders = salesOrderService.getPendingOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Pending orders retrieved"));
    }
    
    @GetMapping("/confirmed")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getConfirmedOrders() {
        List<SalesOrder> orders = salesOrderService.getConfirmedOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Confirmed orders retrieved"));
    }
    
    @GetMapping("/processing")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getProcessingOrders() {
        List<SalesOrder> orders = salesOrderService.getProcessingOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Processing orders retrieved"));
    }
    
    @GetMapping("/completed")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getCompletedOrders() {
        List<SalesOrder> orders = salesOrderService.getCompletedOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Completed orders retrieved"));
    }
    
    @GetMapping("/pending-approval")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getOrdersPendingApproval() {
        List<SalesOrder> orders = salesOrderService.getOrdersPendingApproval();
        return ResponseEntity.ok(ApiResponse.success(orders, "Orders pending approval retrieved"));
    }
    
    @GetMapping("/pending-dispatch")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getOrdersPendingDispatch() {
        List<SalesOrder> orders = salesOrderService.getOrdersPendingDispatch();
        return ResponseEntity.ok(ApiResponse.success(orders, "Orders pending dispatch retrieved"));
    }
    
    @GetMapping("/pending-invoicing")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getOrdersPendingInvoicing() {
        List<SalesOrder> orders = salesOrderService.getOrdersPendingInvoicing();
        return ResponseEntity.ok(ApiResponse.success(orders, "Orders pending invoicing retrieved"));
    }
    
    @GetMapping("/unpaid")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getUnpaidOrders() {
        List<SalesOrder> orders = salesOrderService.getUnpaidOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Unpaid orders retrieved"));
    }
    
    @GetMapping("/overdue-deliveries")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getOverdueDeliveries() {
        List<SalesOrder> orders = salesOrderService.getOverdueDeliveries();
        return ResponseEntity.ok(ApiResponse.success(orders, "Overdue deliveries retrieved"));
    }
    
    @GetMapping("/high-priority")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getHighPriorityOrders() {
        List<SalesOrder> orders = salesOrderService.getHighPriorityOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "High priority orders retrieved"));
    }
    
    @GetMapping("/requiring-action")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getOrdersRequiringAction() {
        List<SalesOrder> orders = salesOrderService.getOrdersRequiringAction();
        return ResponseEntity.ok(ApiResponse.success(orders, "Orders requiring action retrieved"));
    }
    
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Page<SalesOrder>>> getOrdersByCustomer(
        @PathVariable Long customerId,
        Pageable pageable
    ) {
        Page<SalesOrder> orders = salesOrderService.getOrdersByCustomer(customerId, pageable);
        return ResponseEntity.ok(ApiResponse.success(orders, "Customer orders retrieved"));
    }
    
    @GetMapping("/sales-rep/{salesRepId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getOrdersBySalesRep(@PathVariable Long salesRepId) {
        List<SalesOrder> orders = salesOrderService.getOrdersBySalesRep(salesRepId);
        return ResponseEntity.ok(ApiResponse.success(orders, "Sales rep orders retrieved"));
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getOrdersByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<SalesOrder> orders = salesOrderService.getOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(orders, "Date range orders retrieved"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getRecentOrders(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<SalesOrder> orders = salesOrderService.getRecentOrders(limit);
        return ResponseEntity.ok(ApiResponse.success(orders, "Recent orders retrieved"));
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = salesOrderService.getSalesOrderStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = salesOrderService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
}
