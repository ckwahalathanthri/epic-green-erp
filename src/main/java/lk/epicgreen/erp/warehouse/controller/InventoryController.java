package lk.epicgreen.erp.warehouse.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import lk.epicgreen.erp.warehouse.dto.request.InventoryRequest;
import lk.epicgreen.erp.warehouse.dto.response.InventoryResponse;
import lk.epicgreen.erp.warehouse.entity.Inventory;
import lk.epicgreen.erp.warehouse.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Inventory Controller
 * REST controller for inventory/stock management operations
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
    
    // CRUD Operations
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<InventoryResponse>> createInventory(@Valid @RequestBody InventoryRequest request) {
        log.info("Creating inventory for product: {}, warehouse: {}", request.getProductId(), request.getWarehouseId());
        InventoryResponse created = inventoryService.createInventory(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Inventory created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<InventoryResponse>> updateInventory(@PathVariable Long id, @Valid @RequestBody InventoryRequest request) {
        log.info("Updating inventory: {}", id);
        InventoryResponse updated = inventoryService.updateInventory(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Inventory updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteInventory(@PathVariable Long id) {
        log.info("Deleting inventory: {}", id);
        inventoryService.deleteInventory(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Inventory deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'SALES_REP', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<InventoryResponse>> getInventoryById(@PathVariable Long id) {
        InventoryResponse inventory = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(ApiResponse.success(inventory, "Inventory retrieved successfully"));
    }
    
    @GetMapping("/product/{productId}/warehouse/{warehouseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'SALES_REP', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<InventoryResponse>> getInventoryByProductAndWarehouse(@PathVariable Long productId, @PathVariable Long warehouseId) {
        InventoryResponse inventory = inventoryService.getInventoryByProductAndWarehouse(productId, warehouseId);
        return ResponseEntity.ok(ApiResponse.success(inventory, "Inventory retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'SALES_REP', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<InventoryResponse>>> getAllInventory(Pageable pageable) {
        PageResponse<InventoryResponse> inventory = inventoryService.getAllInventory(pageable);
        return ResponseEntity.ok(ApiResponse.success(inventory, "Inventory retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'SALES_REP', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getAllInventoryList() {
        List<Inventory> inventory = inventoryService.getAllInventory();
        return ResponseEntity.ok(ApiResponse.success(inventory, "Inventory list retrieved successfully"));
    }
    
    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'SALES_REP', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getInventoryByProduct(@PathVariable Long productId) {
        List<InventoryResponse> inventory = inventoryService.getInventoryByProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(inventory, "Inventory by product retrieved successfully"));
    }
    
    @GetMapping("/warehouse/{warehouseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getInventoryByWarehouse(@PathVariable Long warehouseId) {
        List<InventoryResponse> inventory = inventoryService.getInventoryByWarehouse(warehouseId);
        return ResponseEntity.ok(ApiResponse.success(inventory, "Inventory by warehouse retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'SALES_REP', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<InventoryResponse>>> searchInventory(@RequestParam String keyword, Pageable pageable) {
        PageResponse<InventoryResponse> inventory = inventoryService.searchInventory(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(inventory, "Search results retrieved successfully"));
    }
    
    // Stock Operations
    @PutMapping("/{id}/add-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> addStock(@PathVariable Long id, @RequestParam Double quantity, @RequestParam Double cost) {
        log.info("Adding {} units to inventory: {}", quantity, id);
        inventoryService.addStock(id, quantity, cost);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock added successfully"));
    }
    
    @PutMapping("/{id}/remove-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> removeStock(@PathVariable Long id, @RequestParam Double quantity) {
        log.info("Removing {} units from inventory: {}", quantity, id);
        inventoryService.removeStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock removed successfully"));
    }
    
    @PutMapping("/{id}/adjust-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> adjustStock(@PathVariable Long id, @RequestParam Double newQuantity, @RequestParam String reason) {
        log.info("Adjusting inventory {} to quantity: {}", id, newQuantity);
        inventoryService.adjustStock(id, newQuantity, reason);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock adjusted successfully"));
    }
    
    @PutMapping("/{id}/increase-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> increaseStock(@PathVariable Long id, @RequestParam Double quantity) {
        log.info("Increasing stock for inventory: {}", id);
        inventoryService.increaseStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock increased successfully"));
    }
    
    @PutMapping("/{id}/decrease-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> decreaseStock(@PathVariable Long id, @RequestParam Double quantity) {
        log.info("Decreasing stock for inventory: {}", id);
        inventoryService.decreaseStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock decreased successfully"));
    }
    
    @PutMapping("/transfer")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> transferStock(@RequestParam Long fromInventoryId, @RequestParam Long toInventoryId, @RequestParam Double quantity) {
        log.info("Transferring {} units from inventory {} to {}", quantity, fromInventoryId, toInventoryId);
        inventoryService.transferStock(fromInventoryId, toInventoryId, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock transferred successfully"));
    }
    
    // Reservation Operations
    @PutMapping("/{id}/reserve-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Void>> reserveStock(@PathVariable Long id, @RequestParam Double quantity) {
        log.info("Reserving {} units from inventory: {}", quantity, id);
        inventoryService.reserveStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock reserved successfully"));
    }
    
    @PutMapping("/{id}/release-reservation")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> releaseReservation(@PathVariable Long id, @RequestParam Double quantity) {
        log.info("Releasing {} reserved units from inventory: {}", quantity, id);
        inventoryService.releaseReservation(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Reservation released successfully"));
    }
    
    @PutMapping("/{id}/release-reserved-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> releaseReservedStock(@PathVariable Long id, @RequestParam Double quantity) {
        log.info("Releasing reserved stock from inventory: {}", id);
        inventoryService.releaseReservedStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Reserved stock released successfully"));
    }
    
    // Allocation Operations
    @PutMapping("/{id}/allocate-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> allocateStock(@PathVariable Long id, @RequestParam Double quantity) {
        log.info("Allocating {} units from inventory: {}", quantity, id);
        inventoryService.allocateStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock allocated successfully"));
    }
    
    @PutMapping("/{id}/release-allocation")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> releaseAllocation(@PathVariable Long id, @RequestParam Double quantity) {
        log.info("Releasing {} allocated units from inventory: {}", quantity, id);
        inventoryService.releaseAllocation(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Allocation released successfully"));
    }
    
    @PutMapping("/{id}/deallocate-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deallocateStock(@PathVariable Long id, @RequestParam Double quantity) {
        log.info("Deallocating stock from inventory: {}", id);
        inventoryService.deallocateStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock deallocated successfully"));
    }
    
    // Quality Control Operations
    @PutMapping("/{id}/record-damaged")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> recordDamagedStock(@PathVariable Long id, @RequestParam Double quantity) {
        log.info("Recording {} damaged units in inventory: {}", quantity, id);
        inventoryService.recordDamagedStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Damaged stock recorded successfully"));
    }
    
    @PutMapping("/{id}/record-expired")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> recordExpiredStock(@PathVariable Long id, @RequestParam Double quantity) {
        log.info("Recording {} expired units in inventory: {}", quantity, id);
        inventoryService.recordExpiredStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Expired stock recorded successfully"));
    }
    
    // Stock Level Management
    @PutMapping("/{id}/stock-levels")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> updateStockLevels(
        @PathVariable Long id,
        @RequestParam Integer reorderLevel,
        @RequestParam Integer maxLevel,
        @RequestParam Integer minLevel
    ) {
        log.info("Updating stock levels for inventory: {}", id);
        inventoryService.updateStockLevels(id, reorderLevel, maxLevel, minLevel);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock levels updated successfully"));
    }
    
    // Query Operations
    @GetMapping("/product/{productId}/warehouse/{warehouseId}/available")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'SALES_REP', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Double>> getAvailableQuantity(@PathVariable Long productId, @PathVariable Long warehouseId) {
        Double available = inventoryService.getAvailableQuantity(productId, warehouseId);
        return ResponseEntity.ok(ApiResponse.success(available, "Available quantity retrieved successfully"));
    }
    
    @GetMapping("/product/{productId}/warehouse/{warehouseId}/check")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'SALES_REP', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> isStockAvailable(
        @PathVariable Long productId,
        @PathVariable Long warehouseId,
        @RequestParam Double requiredQuantity
    ) {
        boolean available = inventoryService.isStockAvailable(productId, warehouseId, requiredQuantity);
        return ResponseEntity.ok(ApiResponse.success(available, "Stock availability checked"));
    }
    
    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getLowStockItems() {
        List<InventoryResponse> inventory = inventoryService.getLowStockItems();
        return ResponseEntity.ok(ApiResponse.success(inventory, "Low stock items retrieved successfully"));
    }
    
    @GetMapping("/out-of-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Inventory>>> getOutOfStockItems() {
        List<Inventory> inventory = inventoryService.getOutOfStockItems();
        return ResponseEntity.ok(ApiResponse.success(inventory, "Out of stock items retrieved successfully"));
    }
    
    // Summary & Metrics
    @GetMapping("/{id}/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getInventorySummary(@PathVariable Long id) {
        Map<String, Object> summary = inventoryService.getInventorySummary(id);
        return ResponseEntity.ok(ApiResponse.success(summary, "Inventory summary retrieved successfully"));
    }
    
    @GetMapping("/{id}/metrics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> calculateInventoryMetrics(@PathVariable Long id) {
        Map<String, Object> metrics = inventoryService.calculateInventoryMetrics(id);
        return ResponseEntity.ok(ApiResponse.success(metrics, "Inventory metrics calculated successfully"));
    }
    
    // Statistics
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getInventoryStatistics() {
        Map<String, Object> statistics = inventoryService.getInventoryStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Inventory statistics retrieved successfully"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = inventoryService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved successfully"));
    }
}
