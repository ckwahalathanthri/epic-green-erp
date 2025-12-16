package lk.epicgreen.erp.purchase.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.purchase.dto.PurchaseOrderRequest;
import lk.epicgreen.erp.purchase.entity.PurchaseOrder;
import lk.epicgreen.erp.purchase.service.PurchaseOrderService;
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
 * PurchaseOrder Controller
 * REST controller for purchase order operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/purchase/orders")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class PurchaseOrderController {
    
    private final PurchaseOrderService purchaseOrderService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<PurchaseOrder>> createPurchaseOrder(
        @Valid @RequestBody PurchaseOrderRequest request
    ) {
        PurchaseOrder created = purchaseOrderService.createPurchaseOrder(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Purchase order created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<PurchaseOrder>> updatePurchaseOrder(
        @PathVariable Long id,
        @Valid @RequestBody PurchaseOrderRequest request
    ) {
        PurchaseOrder updated = purchaseOrderService.updatePurchaseOrder(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Purchase order updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deletePurchaseOrder(@PathVariable Long id) {
        purchaseOrderService.deletePurchaseOrder(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Purchase order deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<PurchaseOrder>> getPurchaseOrderById(@PathVariable Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrderById(id);
        return ResponseEntity.ok(ApiResponse.success(purchaseOrder, "Purchase order retrieved successfully"));
    }
    
    @GetMapping("/number/{orderNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<PurchaseOrder>> getPurchaseOrderByNumber(
        @PathVariable String orderNumber
    ) {
        PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrderByNumber(orderNumber);
        return ResponseEntity.ok(ApiResponse.success(purchaseOrder, "Purchase order retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<PurchaseOrder>>> getAllPurchaseOrders(Pageable pageable) {
        Page<PurchaseOrder> purchaseOrders = purchaseOrderService.getAllPurchaseOrders(pageable);
        return ResponseEntity.ok(ApiResponse.success(purchaseOrders, "Purchase orders retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<PurchaseOrder>>> searchPurchaseOrders(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<PurchaseOrder> purchaseOrders = purchaseOrderService.searchPurchaseOrders(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(purchaseOrders, "Search results retrieved"));
    }
    
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<PurchaseOrder>> approvePurchaseOrder(
        @PathVariable Long id,
        @RequestParam Long approvedByUserId,
        @RequestParam(required = false) String approvalNotes
    ) {
        PurchaseOrder approved = purchaseOrderService.approvePurchaseOrder(id, approvedByUserId, approvalNotes);
        return ResponseEntity.ok(ApiResponse.success(approved, "Purchase order approved"));
    }
    
    @PostMapping("/{id}/mark-ordered")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<PurchaseOrder>> markAsOrdered(@PathVariable Long id) {
        PurchaseOrder ordered = purchaseOrderService.markAsOrdered(id);
        return ResponseEntity.ok(ApiResponse.success(ordered, "Purchase order marked as ordered"));
    }
    
    @PostMapping("/{id}/mark-received")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<PurchaseOrder>> markAsReceived(
        @PathVariable Long id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate receivedDate
    ) {
        PurchaseOrder received = purchaseOrderService.markAsReceived(id, receivedDate);
        return ResponseEntity.ok(ApiResponse.success(received, "Purchase order marked as received"));
    }
    
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<PurchaseOrder>> cancelPurchaseOrder(
        @PathVariable Long id,
        @RequestParam String cancellationReason
    ) {
        PurchaseOrder cancelled = purchaseOrderService.cancelPurchaseOrder(id, cancellationReason);
        return ResponseEntity.ok(ApiResponse.success(cancelled, "Purchase order cancelled"));
    }
    
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<PurchaseOrder>> rejectPurchaseOrder(
        @PathVariable Long id,
        @RequestParam String rejectionReason
    ) {
        PurchaseOrder rejected = purchaseOrderService.rejectPurchaseOrder(id, rejectionReason);
        return ResponseEntity.ok(ApiResponse.success(rejected, "Purchase order rejected"));
    }
    
    @PostMapping("/{id}/record-payment")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> recordPayment(
        @PathVariable Long id,
        @RequestParam Double paidAmount
    ) {
        purchaseOrderService.recordPayment(id, paidAmount);
        return ResponseEntity.ok(ApiResponse.success(null, "Payment recorded successfully"));
    }
    
    @PostMapping("/{id}/mark-paid")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> markAsPaid(@PathVariable Long id) {
        purchaseOrderService.markAsPaid(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Purchase order marked as paid"));
    }
    
    @GetMapping("/draft")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<PurchaseOrder>>> getDraftPurchaseOrders() {
        List<PurchaseOrder> orders = purchaseOrderService.getDraftPurchaseOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Draft purchase orders retrieved"));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<PurchaseOrder>>> getPendingPurchaseOrders() {
        List<PurchaseOrder> orders = purchaseOrderService.getPendingPurchaseOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Pending purchase orders retrieved"));
    }
    
    @GetMapping("/approved")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<PurchaseOrder>>> getApprovedPurchaseOrders() {
        List<PurchaseOrder> orders = purchaseOrderService.getApprovedPurchaseOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Approved purchase orders retrieved"));
    }
    
    @GetMapping("/ordered")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<PurchaseOrder>>> getOrderedPurchaseOrders() {
        List<PurchaseOrder> orders = purchaseOrderService.getOrderedPurchaseOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Ordered purchase orders retrieved"));
    }
    
    @GetMapping("/received")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<PurchaseOrder>>> getReceivedPurchaseOrders() {
        List<PurchaseOrder> orders = purchaseOrderService.getReceivedPurchaseOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Received purchase orders retrieved"));
    }
    
    @GetMapping("/pending-approval")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<PurchaseOrder>>> getPurchaseOrdersPendingApproval() {
        List<PurchaseOrder> orders = purchaseOrderService.getPurchaseOrdersPendingApproval();
        return ResponseEntity.ok(ApiResponse.success(orders, "Purchase orders pending approval retrieved"));
    }
    
    @GetMapping("/overdue-deliveries")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<PurchaseOrder>>> getOverdueDeliveries() {
        List<PurchaseOrder> orders = purchaseOrderService.getOverdueDeliveries();
        return ResponseEntity.ok(ApiResponse.success(orders, "Overdue deliveries retrieved"));
    }
    
    @GetMapping("/high-priority")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<PurchaseOrder>>> getHighPriorityPurchaseOrders() {
        List<PurchaseOrder> orders = purchaseOrderService.getHighPriorityPurchaseOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "High priority purchase orders retrieved"));
    }
    
    @GetMapping("/unpaid")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<PurchaseOrder>>> getUnpaidPurchaseOrders() {
        List<PurchaseOrder> orders = purchaseOrderService.getUnpaidPurchaseOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Unpaid purchase orders retrieved"));
    }
    
    @GetMapping("/unreceived")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<PurchaseOrder>>> getUnreceivedPurchaseOrders() {
        List<PurchaseOrder> orders = purchaseOrderService.getUnreceivedPurchaseOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Unreceived purchase orders retrieved"));
    }
    
    @GetMapping("/todays-deliveries")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<PurchaseOrder>>> getTodaysExpectedDeliveries() {
        List<PurchaseOrder> orders = purchaseOrderService.getTodaysExpectedDeliveries();
        return ResponseEntity.ok(ApiResponse.success(orders, "Today's expected deliveries retrieved"));
    }
    
    @GetMapping("/requiring-action")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<PurchaseOrder>>> getPurchaseOrdersRequiringAction() {
        List<PurchaseOrder> orders = purchaseOrderService.getPurchaseOrdersRequiringAction();
        return ResponseEntity.ok(ApiResponse.success(orders, "Purchase orders requiring action retrieved"));
    }
    
    @GetMapping("/supplier/{supplierId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<PurchaseOrder>>> getPurchaseOrdersBySupplier(
        @PathVariable Long supplierId
    ) {
        List<PurchaseOrder> orders = purchaseOrderService.getPurchaseOrdersBySupplier(supplierId);
        return ResponseEntity.ok(ApiResponse.success(orders, "Supplier purchase orders retrieved"));
    }
    
    @GetMapping("/warehouse/{warehouseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<PurchaseOrder>>> getPurchaseOrdersByWarehouse(
        @PathVariable Long warehouseId
    ) {
        List<PurchaseOrder> orders = purchaseOrderService.getPurchaseOrdersByWarehouse(warehouseId);
        return ResponseEntity.ok(ApiResponse.success(orders, "Warehouse purchase orders retrieved"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<PurchaseOrder>>> getRecentPurchaseOrders(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<PurchaseOrder> orders = purchaseOrderService.getRecentPurchaseOrders(limit);
        return ResponseEntity.ok(ApiResponse.success(orders, "Recent purchase orders retrieved"));
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = purchaseOrderService.getPurchaseOrderStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = purchaseOrderService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
}
