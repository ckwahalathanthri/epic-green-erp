package lk.epicgreen.erp.sales.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;

import lk.epicgreen.erp.common.dto.PageResponse;
import lk.epicgreen.erp.sales.dto.request.SalesOrderRequest;
import lk.epicgreen.erp.sales.dto.response.SalesOrderResponse;
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


import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Sales Order Controller
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
    
    // CRUD Operations
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<SalesOrderResponse>> createSalesOrder(@Valid @RequestBody SalesOrderRequest request) {
        log.info("Creating sales order for customer: {}", request.getCustomerId());
        SalesOrderResponse created = salesOrderService.createSalesOrder(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Sales order created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<SalesOrderResponse>> updateSalesOrder(@PathVariable Long id, @Valid @RequestBody SalesOrderRequest request) {
        log.info("Updating sales order: {}", id);
        SalesOrderResponse updated = salesOrderService.updateSalesOrder(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Sales order updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteSalesOrder(@PathVariable Long id) {
        log.info("Deleting sales order: {}", id);
        salesOrderService.deleteSalesOrder(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Sales order deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'WAREHOUSE_MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<SalesOrderResponse>> getSalesOrderById(@PathVariable Long id) {
        SalesOrderResponse order = salesOrderService.getSalesOrderById(id);
        return ResponseEntity.ok(ApiResponse.success(order, "Sales order retrieved successfully"));
    }
    
    @GetMapping("/number/{orderNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'WAREHOUSE_MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<SalesOrderResponse>> getSalesOrderByNumber(@PathVariable String orderNumber) {
        SalesOrderResponse order = salesOrderService.getSalesOrderByNumber(orderNumber);
        return ResponseEntity.ok(ApiResponse.success(order, "Sales order retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'WAREHOUSE_MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<SalesOrderResponse>>> getAllSalesOrders(Pageable pageable) {
        PageResponse<SalesOrderResponse> orders = salesOrderService.getAllSalesOrders(pageable);
        return ResponseEntity.ok(ApiResponse.success(orders, "Sales orders retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'WAREHOUSE_MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<SalesOrderResponse>>> getAllSalesOrdersList(Pageable pageable) {
        PageResponse<SalesOrderResponse> orders = salesOrderService.getAllSalesOrders(pageable);
        return ResponseEntity.ok(ApiResponse.success(orders, "Sales orders list retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'WAREHOUSE_MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<SalesOrderResponse>>> searchSalesOrders(@RequestParam String keyword, Pageable pageable) {
        PageResponse<SalesOrderResponse> orders = salesOrderService.searchSalesOrders(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(orders, "Search results retrieved successfully"));
    }
    
    // Status Operations
    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<SalesOrder>> confirmSalesOrder(@PathVariable Long id) {
        log.info("Confirming sales order: {}", id);
        SalesOrder confirmed = salesOrderService.confirmSalesOrder(id);
        return ResponseEntity.ok(ApiResponse.success(confirmed, "Sales order confirmed successfully"));
    }
    
    @PutMapping("/{id}/process")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<SalesOrder>> processSalesOrder(@PathVariable Long id) {
        log.info("Processing sales order: {}", id);
        SalesOrder processed = salesOrderService.processSalesOrder(id);
        return ResponseEntity.ok(ApiResponse.success(processed, "Sales order processing started"));
    }
    
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<SalesOrder>> completeSalesOrder(@PathVariable Long id) {
        log.info("Completing sales order: {}", id);
        SalesOrder completed = salesOrderService.completeSalesOrder(id);
        return ResponseEntity.ok(ApiResponse.success(completed, "Sales order completed successfully"));
    }
    
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<SalesOrder>> cancelSalesOrder(@PathVariable Long id, @RequestParam String cancellationReason) {
        log.info("Cancelling sales order: {}", id);
        SalesOrder cancelled = salesOrderService.cancelSalesOrder(id, cancellationReason);
        return ResponseEntity.ok(ApiResponse.success(cancelled, "Sales order cancelled successfully"));
    }
    
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SalesOrder>> approveSalesOrder(
        @PathVariable Long id,
        @RequestParam Long approvedByUserId,
        @RequestParam(required = false) String approvalNotes
    ) {
        log.info("Approving sales order: {}", id);
        SalesOrder approved = salesOrderService.approveSalesOrder(id, approvedByUserId,approvalNotes);
        return ResponseEntity.ok(ApiResponse.success(approved, "Sales order approved successfully"));
    }
    
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SalesOrder>> rejectSalesOrder(@PathVariable Long id, @RequestParam String rejectionReason) {
        log.info("Rejecting sales order: {}", id);
        SalesOrder rejected = salesOrderService.rejectSalesOrder(id, rejectionReason);
        return ResponseEntity.ok(ApiResponse.success(rejected, "Sales order rejected successfully"));
    }
    
    // Delivery Operations
    @PutMapping("/{id}/dispatch")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<SalesOrder>> markAsDispatched(@PathVariable Long id, @RequestParam Long dispatchNoteId) {
        log.info("Marking sales order as dispatched: {}", id);
        SalesOrder dispatched = salesOrderService.markAsDispatched(id);
        return ResponseEntity.ok(ApiResponse.success(dispatched, "Sales order marked as dispatched"));
    }
    
    @PutMapping("/{id}/delivery-status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<SalesOrder>> updateDeliveryStatus(@PathVariable Long id, @RequestParam String deliveryStatus) {
        log.info("Updating delivery status for sales order: {}", id);
        SalesOrder updated = salesOrderService.updateDeliveryStatus(id, deliveryStatus);
        return ResponseEntity.ok(ApiResponse.success(updated, "Delivery status updated successfully"));
    }
    
    // Payment Operations
    @PutMapping("/{id}/invoice")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<SalesOrder>> markAsInvoiced(@PathVariable Long id, @RequestParam Long invoiceId) {
        log.info("Marking sales order as invoiced: {}", id);
        SalesOrder invoiced = salesOrderService.markAsInvoiced(id, invoiceId);
        return ResponseEntity.ok(ApiResponse.success(invoiced, "Sales order marked as invoiced"));
    }
    
    @PutMapping("/{id}/payment-status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<SalesOrder>> updatePaymentStatus(@PathVariable Long id, @RequestParam String paymentStatus) {
        log.info("Updating payment status for sales order: {}", id);
        SalesOrder updated = salesOrderService.updatePaymentStatus(id, paymentStatus);
        return ResponseEntity.ok(ApiResponse.success(updated, "Payment status updated successfully"));
    }
    
    @PutMapping("/{id}/mark-paid")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<SalesOrder>> markAsPaid(@PathVariable Long id) {
        log.info("Marking sales order as paid: {}", id);
        SalesOrder paid = salesOrderService.markAsPaid(id);
        return ResponseEntity.ok(ApiResponse.success(paid, "Sales order marked as paid"));
    }
    
    // Query Operations
    @GetMapping("/draft")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getDraftOrders() {
        List<SalesOrder> orders = salesOrderService.getDraftOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Draft orders retrieved successfully"));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getPendingOrders() {
        List<SalesOrder> orders = salesOrderService.getPendingOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Pending orders retrieved successfully"));
    }
    
    @GetMapping("/confirmed")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getConfirmedOrders() {
        List<SalesOrder> orders = salesOrderService.getConfirmedOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Confirmed orders retrieved successfully"));
    }
    
    @GetMapping("/processing")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getProcessingOrders() {
        List<SalesOrder> orders = salesOrderService.getProcessingOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Processing orders retrieved successfully"));
    }
    
    @GetMapping("/completed")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getCompletedOrders() {
        List<SalesOrder> orders = salesOrderService.getCompletedOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Completed orders retrieved successfully"));
    }
    
    @GetMapping("/cancelled")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getCancelledOrders() {
        List<SalesOrder> orders = salesOrderService.getCancelledOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Cancelled orders retrieved successfully"));
    }
    
    @GetMapping("/pending-approval")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getOrdersPendingApproval() {
        List<SalesOrder> orders = salesOrderService.getOrdersPendingApproval();
        return ResponseEntity.ok(ApiResponse.success(orders, "Orders pending approval retrieved successfully"));
    }
    
    @GetMapping("/pending-dispatch")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getOrdersPendingDispatch() {
        List<SalesOrder> orders = salesOrderService.getOrdersPendingDispatch();
        return ResponseEntity.ok(ApiResponse.success(orders, "Orders pending dispatch retrieved successfully"));
    }
    
    @GetMapping("/pending-invoicing")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getOrdersPendingInvoicing() {
        List<SalesOrder> orders = salesOrderService.getOrdersPendingInvoicing();
        return ResponseEntity.ok(ApiResponse.success(orders, "Orders pending invoicing retrieved successfully"));
    }
    
    @GetMapping("/unpaid")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getUnpaidOrders() {
        List<SalesOrder> orders = salesOrderService.getUnpaidOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Unpaid orders retrieved successfully"));
    }
    
    @GetMapping("/overdue-deliveries")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getOverdueDeliveries() {
        List<SalesOrder> orders = salesOrderService.getOverdueDeliveries();
        return ResponseEntity.ok(ApiResponse.success(orders, "Overdue deliveries retrieved successfully"));
    }
    
    @GetMapping("/high-priority")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getHighPriorityOrders() {
        List<SalesOrder> orders = salesOrderService.getHighPriorityOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "High priority orders retrieved successfully"));
    }
    
    @GetMapping("/requiring-action")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getOrdersRequiringAction() {
        List<SalesOrder> orders = salesOrderService.getOrdersRequiringAction();
        return ResponseEntity.ok(ApiResponse.success(orders, "Orders requiring action retrieved successfully"));
    }
    
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Page<SalesOrder>>> getOrdersByCustomer(@PathVariable Long customerId, Pageable pageable) {
        Page<SalesOrder> orders = salesOrderService.getOrdersByCustomer(customerId, pageable);
        return ResponseEntity.ok(ApiResponse.success(orders, "Orders by customer retrieved successfully"));
    }
    
    @GetMapping("/customer/{customerId}/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getOrdersByCustomerList(@PathVariable Long customerId) {
        List<SalesOrder> orders = salesOrderService.getOrdersByCustomer(customerId);
        return ResponseEntity.ok(ApiResponse.success(orders, "Orders by customer list retrieved successfully"));
    }
    
    @GetMapping("/sales-rep/{salesRepId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getOrdersBySalesRep(@PathVariable Long salesRepId) {
        List<SalesOrder> orders = salesOrderService.getOrdersBySalesRep(salesRepId);
        return ResponseEntity.ok(ApiResponse.success(orders, "Orders by sales rep retrieved successfully"));
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getOrdersByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<SalesOrder> orders = salesOrderService.getOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(orders, "Orders by date range retrieved successfully"));
    }
    
//    @GetMapping("/recent")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'WAREHOUSE_MANAGER', 'ACCOUNTANT', 'USER')")
//    public ResponseEntity<ApiResponse<List<SalesOrder>>> getRecentOrders(@RequestParam(defaultValue = "10") int limit) {
//        List<SalesOrder> orders = salesOrderService.getRecentOrders(limit);
//        return ResponseEntity.ok(ApiResponse.success(orders, "Recent orders retrieved successfully"));
//    }
    
    @GetMapping("/customer/{customerId}/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getCustomerRecentOrders(@PathVariable Long customerId, Pageable limit) {
        List<SalesOrder> orders = salesOrderService.getCustomerRecentOrders(customerId, limit);
        return ResponseEntity.ok(ApiResponse.success(orders, "Customer recent orders retrieved successfully"));
    }
    
    // Validation
    @GetMapping("/{id}/can-confirm")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Boolean>> canConfirmOrder(@PathVariable Long id) {
        boolean canConfirm = salesOrderService.canConfirmOrder(id);
        return ResponseEntity.ok(ApiResponse.success(canConfirm, "Confirm check completed"));
    }
    
    @GetMapping("/{id}/can-cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Boolean>> canCancelOrder(@PathVariable Long id) {
        boolean canCancel = salesOrderService.canCancelOrder(id);
        return ResponseEntity.ok(ApiResponse.success(canCancel, "Cancel check completed"));
    }
    
    @GetMapping("/{id}/can-approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> canApproveOrder(@PathVariable Long id) {
        boolean canApprove = salesOrderService.canApproveOrder(id);
        return ResponseEntity.ok(ApiResponse.success(canApprove, "Approval check completed"));
    }
    
    // Calculations
//    @PutMapping("/{id}/calculate-totals")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT')")
//    public ResponseEntity<ApiResponse<Void>> calculateOrderTotals(@PathVariable Long id) {
//        log.info("Calculating totals for sales order: {}", id);
//        salesOrderService.calculateOrderTotals(id);
//        return ResponseEntity.ok(ApiResponse.success(null, "Order totals calculated successfully"));
//    }
    
    @GetMapping("/{id}/subtotal")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Double>> calculateSubtotal(@PathVariable Long id) {
        Double subtotal = salesOrderService.calculateSubtotal(id);
        return ResponseEntity.ok(ApiResponse.success(subtotal, "Subtotal calculated successfully"));
    }
    
    @GetMapping("/{id}/total-tax")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Double>> calculateTotalTax(@PathVariable Long id) {
        Double tax = salesOrderService.calculateTotalTax(id);
        return ResponseEntity.ok(ApiResponse.success(tax, "Total tax calculated successfully"));
    }
    
    @GetMapping("/{id}/total-discount")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Double>> calculateTotalDiscount(@PathVariable Long id) {
        Double discount = salesOrderService.calculateTotalDiscount(id);
        return ResponseEntity.ok(ApiResponse.success(discount, "Total discount calculated successfully"));
    }
    
    // Batch Operations
//    @PostMapping("/bulk")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
//    public ResponseEntity<ApiResponse<List<SalesOrder>>> createBulkSalesOrders(@Valid @RequestBody List<SalesOrderRequest> requests) {
//        log.info("Creating {} sales orders in bulk", requests.size());
//        List<SalesOrder> orders = salesOrderService.createBulkSalesOrders(requests);
//        return ResponseEntity.ok(ApiResponse.success(orders, orders.size() + " sales orders created successfully"));
//    }
    
//    @PutMapping("/bulk/approve")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public ResponseEntity<ApiResponse<Integer>> approveBulkSalesOrders(@RequestBody List<Long> orderIds, @RequestParam Long approvedByUserId) {
//        log.info("Approving {} sales orders in bulk", orderIds.size());
//        int count = salesOrderService.approveBulkSalesOrders(orderIds, approvedByUserId);
//        return ResponseEntity.ok(ApiResponse.success(count, count + " sales orders approved successfully"));
//    }
    
//    @DeleteMapping("/bulk")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public ResponseEntity<ApiResponse<Integer>> deleteBulkSalesOrders(@RequestBody List<Long> orderIds) {
//        log.info("Deleting {} sales orders in bulk", orderIds.size());
//        int count = salesOrderService.deleteBulkSalesOrders(orderIds);
//        return ResponseEntity.ok(ApiResponse.success(count, count + " sales orders deleted successfully"));
//    }
    
    // Statistics
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSalesOrderStatistics() {
        Map<String, Object> statistics = salesOrderService.getSalesOrderStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Sales order statistics retrieved successfully"));
    }
    
    @GetMapping("/statistics/type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getOrderTypeDistribution() {
        Map<String, Object> distribution = salesOrderService.getOrderTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Order type distribution retrieved successfully"));
    }
    
//    @GetMapping("/statistics/status-distribution")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
//    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getStatusDistribution() {
//        List<Map<String, Object>> distribution = salesOrderService.getStatusDistribution();
//        return ResponseEntity.ok(ApiResponse.success(distribution, "Status distribution retrieved successfully"));
//    }
//
//    @GetMapping("/statistics/priority-distribution")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
//    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getPriorityDistribution() {
//        List<Map<String, Object>> distribution = salesOrderService.getPriorityDistribution();
//        return ResponseEntity.ok(ApiResponse.success(distribution, "Priority distribution retrieved successfully"));
//    }
    
    @GetMapping("/statistics/delivery-status-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getDeliveryStatusDistribution() {
        List<Map<String, Object>> distribution = salesOrderService.getDeliveryStatusDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Delivery status distribution retrieved successfully"));
    }
//
//    @GetMapping("/statistics/payment-status-distribution")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
//    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getPaymentStatusDistribution() {
//        List<Map<String, Object>> distribution = salesOrderService.getPaymentStatusDistribution();
//        return ResponseEntity.ok(ApiResponse.success(distribution, "Payment status distribution retrieved successfully"));
//    }
//
//    @GetMapping("/statistics/monthly")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT')")
//    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMonthlyOrderCount(
//        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
//    ) {
//        List<Map<String, Object>> count = salesOrderService.getMonthlyOrderCount(startDate, endDate);
//        return ResponseEntity.ok(ApiResponse.success(count, "Monthly order count retrieved successfully"));
//    }
//
//    @GetMapping("/statistics/by-customer")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT')")
//    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTotalOrderValueByCustomer() {
//        List<Map<String, Object>> stats = salesOrderService.getTotalOrderValueByCustomer();
//        return ResponseEntity.ok(ApiResponse.success(stats, "Order value by customer retrieved successfully"));
//    }
//
//    @GetMapping("/statistics/by-sales-rep")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTotalOrderValueBySalesRep() {
//        List<Map<String, Object>> stats = salesOrderService.getTotalOrderValueBySalesRep();
//        return ResponseEntity.ok(ApiResponse.success(stats, "Order value by sales rep retrieved successfully"));
//    }
//
//    @GetMapping("/statistics/top-customers")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT')")
//    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTopCustomers(@RequestParam(defaultValue = "10") int limit) {
//        List<Map<String, Object>> customers = salesOrderService.getTopCustomers(limit);
//        return ResponseEntity.ok(ApiResponse.success(customers, "Top customers retrieved successfully"));
//    }
    
    @GetMapping("/statistics/total-value")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getTotalOrderValue() {
        Double total = salesOrderService.getTotalOrderValue();
        return ResponseEntity.ok(ApiResponse.success(total, "Total order value retrieved successfully"));
    }
    
    @GetMapping("/statistics/average-value")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getAverageOrderValue() {
        Double average = salesOrderService.getAverageOrderValue();
        return ResponseEntity.ok(ApiResponse.success(average, "Average order value retrieved successfully"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = salesOrderService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved successfully"));
    }
}
