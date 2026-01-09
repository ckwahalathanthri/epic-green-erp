package lk.epicgreen.erp.warehouse.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import lk.epicgreen.erp.warehouse.dto.request.StockMovementRequest;
import lk.epicgreen.erp.warehouse.dto.response.StockMovementResponse;
import lk.epicgreen.erp.warehouse.entity.StockMovement;
import lk.epicgreen.erp.warehouse.service.StockMovementService;
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
 * Stock Movement Controller
 * REST controller for stock movement/transfer operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/warehouse/stock-movements")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class StockMovementController {
    
    private final StockMovementService stockMovementService;
    
    // CRUD Operations
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<StockMovementResponse>> createStockMovement(@Valid @RequestBody StockMovementRequest request) {
        log.info("Creating stock movement for product: {}", request.getProductId());
        StockMovementResponse created = stockMovementService.createStockMovement(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Stock movement created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<StockMovementResponse>> updateStockMovement(@PathVariable Long id, @Valid @RequestBody StockMovementRequest request) {
        log.info("Updating stock movement: {}", id);
        StockMovementResponse updated = stockMovementService.updateStockMovement(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Stock movement updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteStockMovement(@PathVariable Long id) {
        log.info("Deleting stock movement: {}", id);
        stockMovementService.deleteStockMovement(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock movement deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<StockMovementResponse>> getStockMovementById(@PathVariable Long id) {
        StockMovementResponse movement = stockMovementService.getStockMovementById(id);
        return ResponseEntity.ok(ApiResponse.success(movement, "Stock movement retrieved successfully"));
    }
    
    @GetMapping("/number/{movementNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<StockMovementResponse>> getStockMovementByNumber(@PathVariable String movementNumber) {
        StockMovementResponse movement = stockMovementService.getStockMovementByNumber(movementNumber);
        return ResponseEntity.ok(ApiResponse.success(movement, "Stock movement retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<StockMovementResponse>>> getAllStockMovements(Pageable pageable) {
        PageResponse<StockMovementResponse> movements = stockMovementService.getAllStockMovements(pageable);
        return ResponseEntity.ok(ApiResponse.success(movements, "Stock movements retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getAllStockMovementsList() {
        List<StockMovementResponse> movements = stockMovementService.getAllStockMovements();
        return ResponseEntity.ok(ApiResponse.success(movements, "Stock movements list retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<StockMovementResponse>>> searchStockMovements(@RequestParam String keyword, Pageable pageable) {
        PageResponse<StockMovementResponse> movements = stockMovementService.searchStockMovements(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(movements, "Search results retrieved successfully"));
    }
    
    // Status Operations
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<StockMovementResponse>> approveStockMovement(
        @PathVariable Long id,
        @RequestParam Long approvedByUserId,
        @RequestParam(required = false) String approvalNotes
    ) {
        log.info("Approving stock movement: {}", id);
        StockMovementResponse approved = stockMovementService.approveStockMovement(id, approvedByUserId, approvalNotes);
        return ResponseEntity.ok(ApiResponse.success(approved, "Stock movement approved successfully"));
    }
    
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<StockMovementResponse>> completeStockMovement(@PathVariable Long id) {
        log.info("Completing stock movement: {}", id);
        StockMovementResponse completed = stockMovementService.completeStockMovement(id);
        return ResponseEntity.ok(ApiResponse.success(completed, "Stock movement completed successfully"));
    }
    
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<StockMovementResponse>> cancelStockMovement(@PathVariable Long id, @RequestParam String cancellationReason) {
        log.info("Cancelling stock movement: {}", id);
        StockMovementResponse cancelled = stockMovementService.cancelStockMovement(id, cancellationReason);
        return ResponseEntity.ok(ApiResponse.success(cancelled, "Stock movement cancelled successfully"));
    }
    
    // Movement Operations
    @PostMapping("/record-stock-in")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<StockMovementResponse>> recordStockIn(@Valid @RequestBody StockMovementRequest request) {
        log.info("Recording stock in for product: {}", request.getProductId());
        StockMovementResponse movement = stockMovementService.recordStockIn(request);
        return ResponseEntity.ok(ApiResponse.success(movement, "Stock in recorded successfully"));
    }
    
    @PostMapping("/record-stock-out")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<StockMovementResponse>> recordStockOut(@Valid @RequestBody StockMovementRequest request) {
        log.info("Recording stock out for product: {}", request.getProductId());
        StockMovementResponse movement = stockMovementService.recordStockOut(request);
        return ResponseEntity.ok(ApiResponse.success(movement, "Stock out recorded successfully"));
    }
    
    @PostMapping("/record-transfer")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<StockMovementResponse>> recordTransfer(@Valid @RequestBody StockMovementRequest request) {
        log.info("Recording transfer for product: {}", request.getProductId());
        StockMovementResponse movement = stockMovementService.recordTransfer(request);
        return ResponseEntity.ok(ApiResponse.success(movement, "Transfer recorded successfully"));
    }
    
    @PostMapping("/record-adjustment")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<StockMovementResponse>> recordAdjustment(@Valid @RequestBody StockMovementRequest request) {
        log.info("Recording adjustment for product: {}", request.getProductId());
        StockMovementResponse movement = stockMovementService.recordAdjustment(request);
        return ResponseEntity.ok(ApiResponse.success(movement, "Adjustment recorded successfully"));
    }
    
    @PostMapping("/record-return")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<StockMovementResponse>> recordReturn(@Valid @RequestBody StockMovementRequest request) {
        log.info("Recording return for product: {}", request.getProductId());
        StockMovementResponse movement = stockMovementService.recordReturn(request);
        return ResponseEntity.ok(ApiResponse.success(movement, "Return recorded successfully"));
    }
    
    @PostMapping("/record-damage")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<StockMovementResponse>> recordDamage(@Valid @RequestBody StockMovementRequest request) {
        log.info("Recording damage for product: {}", request.getProductId());
        StockMovementResponse movement = stockMovementService.recordDamage(request);
        return ResponseEntity.ok(ApiResponse.success(movement, "Damage recorded successfully"));
    }
    
    // Query Operations
    @GetMapping("/stock-in")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getStockInMovements() {
        List<StockMovementResponse> movements = stockMovementService.getStockInMovements();
        return ResponseEntity.ok(ApiResponse.success(movements, "Stock in movements retrieved successfully"));
    }
    
    @GetMapping("/stock-out")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getStockOutMovements() {
        List<StockMovementResponse> movements = stockMovementService.getStockOutMovements();
        return ResponseEntity.ok(ApiResponse.success(movements, "Stock out movements retrieved successfully"));
    }
    
    @GetMapping("/transfers")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getTransferMovements() {
        List<StockMovementResponse> movements = stockMovementService.getTransferMovements();
        return ResponseEntity.ok(ApiResponse.success(movements, "Transfer movements retrieved successfully"));
    }
    
    @GetMapping("/adjustments")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getAdjustmentMovements() {
        List<StockMovementResponse> movements = stockMovementService.getAdjustmentMovements();
        return ResponseEntity.ok(ApiResponse.success(movements, "Adjustment movements retrieved successfully"));
    }
    
    @GetMapping("/returns")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getReturnMovements() {
        List<StockMovementResponse> movements = stockMovementService.getReturnMovements();
        return ResponseEntity.ok(ApiResponse.success(movements, "Return movements retrieved successfully"));
    }
    
    @GetMapping("/damages")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getDamageMovements() {
        List<StockMovementResponse> movements = stockMovementService.getDamageMovements();
        return ResponseEntity.ok(ApiResponse.success(movements, "Damage movements retrieved successfully"));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getPendingMovements() {
        List<StockMovementResponse> movements = stockMovementService.getPendingMovements();
        return ResponseEntity.ok(ApiResponse.success(movements, "Pending movements retrieved successfully"));
    }
    
    @GetMapping("/approved")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getApprovedMovements() {
        List<StockMovementResponse> movements = stockMovementService.getApprovedMovements();
        return ResponseEntity.ok(ApiResponse.success(movements, "Approved movements retrieved successfully"));
    }
    
    @GetMapping("/completed")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getCompletedMovements() {
        List<StockMovementResponse> movements = stockMovementService.getCompletedMovements();
        return ResponseEntity.ok(ApiResponse.success(movements, "Completed movements retrieved successfully"));
    }
    
    @GetMapping("/pending-approval")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getMovementsPendingApproval() {
        List<StockMovementResponse> movements = stockMovementService.getMovementsPendingApproval();
        return ResponseEntity.ok(ApiResponse.success(movements, "Movements pending approval retrieved successfully"));
    }
    
    @GetMapping("/today")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getTodaysMovements() {
        List<StockMovementResponse> movements = stockMovementService.getTodaysMovements();
        return ResponseEntity.ok(ApiResponse.success(movements, "Today's movements retrieved successfully"));
    }
    
    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getMovementsByProduct(@PathVariable Long productId) {
        List<StockMovementResponse> movements = stockMovementService.getMovementsByProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(movements, "Movements by product retrieved successfully"));
    }
    
    @GetMapping("/warehouse/{warehouseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getMovementsByWarehouse(@PathVariable Long warehouseId) {
        List<StockMovementResponse> movements = stockMovementService.getMovementsByWarehouse(warehouseId);
        return ResponseEntity.ok(ApiResponse.success(movements, "Movements by warehouse retrieved successfully"));
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getMovementsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<StockMovementResponse> movements = stockMovementService.getMovementsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(movements, "Movements by date range retrieved successfully"));
    }
    
    @GetMapping("/transfer/{fromWarehouseId}/{toWarehouseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getTransfersBetweenWarehouses(
        @PathVariable Long fromWarehouseId,
        @PathVariable Long toWarehouseId
    ) {
        List<StockMovementResponse> movements = stockMovementService.getTransfersBetweenWarehouses(fromWarehouseId, toWarehouseId);
        return ResponseEntity.ok(ApiResponse.success(movements, "Transfers between warehouses retrieved successfully"));
    }
    
    @GetMapping("/product/{productId}/history")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getProductMovementHistory(
        @PathVariable Long productId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<StockMovementResponse> movements = stockMovementService.getProductMovementHistory(productId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(movements, "Product movement history retrieved successfully"));
    }
    
    @GetMapping("/warehouse/{warehouseId}/history")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getWarehouseMovementHistory(
        @PathVariable Long warehouseId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<StockMovementResponse> movements = stockMovementService.getWarehouseMovementHistory(warehouseId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(movements, "Warehouse movement history retrieved successfully"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getRecentMovements(@RequestParam(defaultValue = "10") int limit) {
        List<StockMovementResponse> movements = stockMovementService.getRecentMovements(limit);
        return ResponseEntity.ok(ApiResponse.success(movements, "Recent movements retrieved successfully"));
    }
    
    // Calculation Operations
    @GetMapping("/product/{productId}/total-in")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getTotalQuantityInByProduct(@PathVariable Long productId) {
        Double total = stockMovementService.getTotalQuantityInByProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(total, "Total quantity in retrieved successfully"));
    }
    
    @GetMapping("/product/{productId}/total-out")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getTotalQuantityOutByProduct(@PathVariable Long productId) {
        Double total = stockMovementService.getTotalQuantityOutByProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(total, "Total quantity out retrieved successfully"));
    }
    
    @GetMapping("/warehouse/{warehouseId}/total-value-in")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getTotalValueInByWarehouse(@PathVariable Long warehouseId) {
        Double total = stockMovementService.getTotalValueInByWarehouse(warehouseId);
        return ResponseEntity.ok(ApiResponse.success(total, "Total value in retrieved successfully"));
    }
    
    @GetMapping("/warehouse/{warehouseId}/total-value-out")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getTotalValueOutByWarehouse(@PathVariable Long warehouseId) {
        Double total = stockMovementService.getTotalValueOutByWarehouse(warehouseId);
        return ResponseEntity.ok(ApiResponse.success(total, "Total value out retrieved successfully"));
    }
    
    // Validation
    @GetMapping("/{id}/can-approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> canApproveStockMovement(@PathVariable Long id) {
        boolean canApprove = stockMovementService.canApproveStockMovement(id);
        return ResponseEntity.ok(ApiResponse.success(canApprove, "Approval check completed"));
    }
    
    @GetMapping("/{id}/can-cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> canCancelStockMovement(@PathVariable Long id) {
        boolean canCancel = stockMovementService.canCancelStockMovement(id);
        return ResponseEntity.ok(ApiResponse.success(canCancel, "Cancel check completed"));
    }
    
    // Batch Operations
    @PostMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<StockMovement>>> createBulkStockMovements(@Valid @RequestBody List<StockMovementRequest> requests) {
        log.info("Creating {} stock movements in bulk", requests.size());
        List<StockMovement> movements = stockMovementService.createBulkStockMovements(requests);
        return ResponseEntity.ok(ApiResponse.success(movements, movements.size() + " stock movements created successfully"));
    }
    
    @PutMapping("/bulk/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> approveBulkStockMovements(@RequestBody List<Long> movementIds, @RequestParam Long approvedByUserId) {
        log.info("Approving {} stock movements in bulk", movementIds.size());
        int count = stockMovementService.approveBulkStockMovements(movementIds, approvedByUserId);
        return ResponseEntity.ok(ApiResponse.success(count, count + " stock movements approved successfully"));
    }
    
    @DeleteMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> deleteBulkStockMovements(@RequestBody List<Long> movementIds) {
        log.info("Deleting {} stock movements in bulk", movementIds.size());
        int count = stockMovementService.deleteBulkStockMovements(movementIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " stock movements deleted successfully"));
    }
    
    // Statistics
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStockMovementStatistics() {
        Map<String, Object> statistics = stockMovementService.getStockMovementStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Stock movement statistics retrieved successfully"));
    }
    
    @GetMapping("/statistics/movement-type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMovementTypeDistribution() {
        List<Map<String, Object>> distribution = stockMovementService.getMovementTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Movement type distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/status-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getStatusDistribution() {
        List<Map<String, Object>> distribution = stockMovementService.getStatusDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Status distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/transaction-type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTransactionTypeDistribution() {
        List<Map<String, Object>> distribution = stockMovementService.getTransactionTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Transaction type distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/monthly")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMonthlyMovementCount(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<Map<String, Object>> count = stockMovementService.getMonthlyMovementCount(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(count, "Monthly movement count retrieved successfully"));
    }
    
    @GetMapping("/statistics/by-warehouse")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMovementsByWarehouse() {
        List<Map<String, Object>> stats = stockMovementService.getMovementsByWarehouse();
        return ResponseEntity.ok(ApiResponse.success(stats, "Movements by warehouse retrieved successfully"));
    }
    
    @GetMapping("/statistics/by-product")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMovementsByProduct() {
        List<Map<String, Object>> stats = stockMovementService.getMovementsByProduct();
        return ResponseEntity.ok(ApiResponse.success(stats, "Movements by product retrieved successfully"));
    }
    
    @GetMapping("/statistics/total-quantity-moved")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getTotalQuantityMoved() {
        Double total = stockMovementService.getTotalQuantityMoved();
        return ResponseEntity.ok(ApiResponse.success(total, "Total quantity moved retrieved successfully"));
    }
    
    @GetMapping("/statistics/total-value-moved")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getTotalValueMoved() {
        Double total = stockMovementService.getTotalValueMoved();
        return ResponseEntity.ok(ApiResponse.success(total, "Total value moved retrieved successfully"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = stockMovementService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved successfully"));
    }
}
