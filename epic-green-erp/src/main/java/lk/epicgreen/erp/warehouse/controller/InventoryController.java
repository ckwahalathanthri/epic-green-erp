package lk.epicgreen.erp.warehouse.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.warehouse.dto.InventoryRequest;
import lk.epicgreen.erp.warehouse.entity.Inventory;
import lk.epicgreen.erp.warehouse.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Inventory Controller
 * REST controller for inventory operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/warehouse/inventory")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class InventoryController {
    
    private final InventoryService inventoryService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Inventory>> createInventory(
        @Valid @RequestBody InventoryRequest request
    ) {
        Inventory created = inventoryService.createInventory(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Inventory created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Inventory>> updateInventory(
        @PathVariable Long id,
        @Valid @RequestBody InventoryRequest request
    ) {
        Inventory updated = inventoryService.updateInventory(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Inventory updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Inventory deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Inventory>> getInventoryById(@PathVariable Long id) {
        Inventory inventory = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(ApiResponse.success(inventory, "Inventory retrieved successfully"));
    }
    
    @GetMapping("/product/{productId}/warehouse/{warehouseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Inventory>> getInventoryByProductAndWarehouse(
        @PathVariable Long productId,
        @PathVariable Long warehouseId
    ) {
        Inventory inventory = inventoryService.getInventoryByProductAndWarehouse(productId, warehouseId);
        return ResponseEntity.ok(ApiResponse.success(inventory, "Inventory retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<Inventory>>> getAllInventory(Pageable pageable) {
        Page<Inventory> inventory = inventoryService.getAllInventory(pageable);
        return ResponseEntity.ok(ApiResponse.success(inventory, "Inventory retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<Inventory>>> searchInventory(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<Inventory> inventory = inventoryService.searchInventory(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(inventory, "Search results retrieved"));
    }
    
    @PostMapping("/{id}/increase-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> increaseStock(
        @PathVariable Long id,
        @RequestParam Double quantity
    ) {
        inventoryService.increaseStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock increased successfully"));
    }
    
    @PostMapping("/{id}/decrease-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> decreaseStock(
        @PathVariable Long id,
        @RequestParam Double quantity
    ) {
        inventoryService.decreaseStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock decreased successfully"));
    }
    
    @PostMapping("/{id}/adjust-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> adjustStock(
        @PathVariable Long id,
        @RequestParam Double newQuantity,
        @RequestParam String reason
    ) {
        inventoryService.adjustStock(id, newQuantity, reason);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock adjusted successfully"));
    }
    
    @PostMapping("/{id}/reserve-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Void>> reserveStock(
        @PathVariable Long id,
        @RequestParam Double quantity
    ) {
        inventoryService.reserveStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock reserved successfully"));
    }
    
    @PostMapping("/{id}/release-reserved-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> releaseReservedStock(
        @PathVariable Long id,
        @RequestParam Double quantity
    ) {
        inventoryService.releaseReservedStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Reserved stock released successfully"));
    }
    
    @PostMapping("/{id}/allocate-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> allocateStock(
        @PathVariable Long id,
        @RequestParam Double quantity
    ) {
        inventoryService.allocateStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock allocated successfully"));
    }
    
    @PostMapping("/{id}/deallocate-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deallocateStock(
        @PathVariable Long id,
        @RequestParam Double quantity
    ) {
        inventoryService.deallocateStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock deallocated successfully"));
    }
    
    @PostMapping("/{id}/mark-damaged")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> markDamaged(
        @PathVariable Long id,
        @RequestParam Double quantity,
        @RequestParam String reason
    ) {
        inventoryService.markDamaged(id, quantity, reason);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock marked as damaged"));
    }
    
    @PostMapping("/{id}/mark-expired")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> markExpired(
        @PathVariable Long id,
        @RequestParam Double quantity,
        @RequestParam String reason
    ) {
        inventoryService.markExpired(id, quantity, reason);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock marked as expired"));
    }
    
    @PostMapping("/{id}/record-stock-count")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> recordStockCount(
        @PathVariable Long id,
        @RequestParam Double countedQuantity,
        @RequestParam String countedBy
    ) {
        inventoryService.recordStockCount(id, countedQuantity, countedBy);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock count recorded"));
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getActiveInventory() {
        List<Inventory> inventory = inventoryService.getActiveInventory();
        return ResponseEntity.ok(ApiResponse.success(inventory, "Active inventory retrieved"));
    }
    
    @GetMapping("/with-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getInventoryWithStock() {
        List<Inventory> inventory = inventoryService.getInventoryWithStock();
        return ResponseEntity.ok(ApiResponse.success(inventory, "Inventory with stock retrieved"));
    }
    
    @GetMapping("/without-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getInventoryWithoutStock() {
        List<Inventory> inventory = inventoryService.getInventoryWithoutStock();
        return ResponseEntity.ok(ApiResponse.success(inventory, "Inventory without stock retrieved"));
    }
    
    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getLowStockInventory() {
        List<Inventory> inventory = inventoryService.getLowStockInventory();
        return ResponseEntity.ok(ApiResponse.success(inventory, "Low stock inventory retrieved"));
    }
    
    @GetMapping("/out-of-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getOutOfStockInventory() {
        List<Inventory> inventory = inventoryService.getOutOfStockInventory();
        return ResponseEntity.ok(ApiResponse.success(inventory, "Out of stock inventory retrieved"));
    }
    
    @GetMapping("/overstock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getOverstockInventory() {
        List<Inventory> inventory = inventoryService.getOverstockInventory();
        return ResponseEntity.ok(ApiResponse.success(inventory, "Overstock inventory retrieved"));
    }
    
    @GetMapping("/optimal-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getOptimalStockInventory() {
        List<Inventory> inventory = inventoryService.getOptimalStockInventory();
        return ResponseEntity.ok(ApiResponse.success(inventory, "Optimal stock inventory retrieved"));
    }
    
    @GetMapping("/below-reorder-level")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getInventoryBelowReorderLevel() {
        List<Inventory> inventory = inventoryService.getInventoryBelowReorderLevel();
        return ResponseEntity.ok(ApiResponse.success(inventory, "Inventory below reorder level retrieved"));
    }
    
    @GetMapping("/above-max-stock-level")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getInventoryAboveMaxStockLevel() {
        List<Inventory> inventory = inventoryService.getInventoryAboveMaxStockLevel();
        return ResponseEntity.ok(ApiResponse.success(inventory, "Inventory above max stock level retrieved"));
    }
    
    @GetMapping("/with-reserved-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getInventoryWithReservedStock() {
        List<Inventory> inventory = inventoryService.getInventoryWithReservedStock();
        return ResponseEntity.ok(ApiResponse.success(inventory, "Inventory with reserved stock retrieved"));
    }
    
    @GetMapping("/with-damaged-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getInventoryWithDamagedStock() {
        List<Inventory> inventory = inventoryService.getInventoryWithDamagedStock();
        return ResponseEntity.ok(ApiResponse.success(inventory, "Inventory with damaged stock retrieved"));
    }
    
    @GetMapping("/with-expired-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getInventoryWithExpiredStock() {
        List<Inventory> inventory = inventoryService.getInventoryWithExpiredStock();
        return ResponseEntity.ok(ApiResponse.success(inventory, "Inventory with expired stock retrieved"));
    }
    
    @GetMapping("/slow-moving")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getSlowMovingInventory(
        @RequestParam(defaultValue = "90") int daysThreshold
    ) {
        List<Inventory> inventory = inventoryService.getSlowMovingInventory(daysThreshold);
        return ResponseEntity.ok(ApiResponse.success(inventory, "Slow moving inventory retrieved"));
    }
    
    @GetMapping("/fast-moving")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getFastMovingInventory(
        @RequestParam(defaultValue = "30") int daysThreshold
    ) {
        List<Inventory> inventory = inventoryService.getFastMovingInventory(daysThreshold);
        return ResponseEntity.ok(ApiResponse.success(inventory, "Fast moving inventory retrieved"));
    }
    
    @GetMapping("/warehouse/{warehouseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getWarehouseInventory(
        @PathVariable Long warehouseId
    ) {
        List<Inventory> inventory = inventoryService.getWarehouseInventory(warehouseId);
        return ResponseEntity.ok(ApiResponse.success(inventory, "Warehouse inventory retrieved"));
    }
    
    @GetMapping("/product/{productId}/across-warehouses")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getProductInventoryAcrossWarehouses(
        @PathVariable Long productId
    ) {
        List<Inventory> inventory = inventoryService.getProductInventoryAcrossWarehouses(productId);
        return ResponseEntity.ok(ApiResponse.success(inventory, "Product inventory across warehouses retrieved"));
    }
    
    @GetMapping("/requiring-stock-count")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getInventoryRequiringStockCount(
        @RequestParam(defaultValue = "90") int daysThreshold
    ) {
        List<Inventory> inventory = inventoryService.getInventoryRequiringStockCount(daysThreshold);
        return ResponseEntity.ok(ApiResponse.success(inventory, "Inventory requiring stock count retrieved"));
    }
    
    @GetMapping("/high-value")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getHighValueInventory(
        @RequestParam Double threshold
    ) {
        List<Inventory> inventory = inventoryService.getHighValueInventory(threshold);
        return ResponseEntity.ok(ApiResponse.success(inventory, "High value inventory retrieved"));
    }
    
    @GetMapping("/recent-updates")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getRecentInventoryUpdates(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<Inventory> inventory = inventoryService.getRecentInventoryUpdates(limit);
        return ResponseEntity.ok(ApiResponse.success(inventory, "Recent inventory updates retrieved"));
    }
    
    @GetMapping("/top-value")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getTopValueInventoryItems(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<Inventory> inventory = inventoryService.getTopValueInventoryItems(limit);
        return ResponseEntity.ok(ApiResponse.success(inventory, "Top value inventory items retrieved"));
    }
    
    @GetMapping("/top-quantity")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getTopQuantityInventoryItems(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<Inventory> inventory = inventoryService.getTopQuantityInventoryItems(limit);
        return ResponseEntity.ok(ApiResponse.success(inventory, "Top quantity inventory items retrieved"));
    }
    
    @GetMapping("/{id}/metrics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> calculateInventoryMetrics(
        @PathVariable Long id
    ) {
        Map<String, Object> metrics = inventoryService.calculateInventoryMetrics(id);
        return ResponseEntity.ok(ApiResponse.success(metrics, "Inventory metrics calculated"));
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = inventoryService.getInventoryStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = inventoryService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
}
