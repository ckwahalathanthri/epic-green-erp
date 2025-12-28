package lk.epicgreen.erp.warehouse.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.warehouse.dto.WarehouseRequest;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lk.epicgreen.erp.warehouse.service.WarehouseService;
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
 * Warehouse Controller
 * REST controller for warehouse location operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/warehouse/warehouses")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class WarehouseController {
    
    private final WarehouseService warehouseService;
    
    // CRUD Operations
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Warehouse>> createWarehouse(@Valid @RequestBody WarehouseRequest request) {
        log.info("Creating warehouse: {}", request.getWarehouseName());
        Warehouse created = warehouseService.createWarehouse(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Warehouse created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Warehouse>> updateWarehouse(@PathVariable Long id, @Valid @RequestBody WarehouseRequest request) {
        log.info("Updating warehouse: {}", id);
        Warehouse updated = warehouseService.updateWarehouse(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Warehouse updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteWarehouse(@PathVariable Long id) {
        log.info("Deleting warehouse: {}", id);
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Warehouse deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Warehouse>> getWarehouseById(@PathVariable Long id) {
        Warehouse warehouse = warehouseService.getWarehouseById(id);
        return ResponseEntity.ok(ApiResponse.success(warehouse, "Warehouse retrieved successfully"));
    }
    
    @GetMapping("/code/{warehouseCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Warehouse>> getWarehouseByCode(@PathVariable String warehouseCode) {
        Warehouse warehouse = warehouseService.getWarehouseByCode(warehouseCode);
        return ResponseEntity.ok(ApiResponse.success(warehouse, "Warehouse retrieved successfully"));
    }
    
    @GetMapping("/name/{warehouseName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Warehouse>> getWarehouseByName(@PathVariable String warehouseName) {
        Warehouse warehouse = warehouseService.getWarehouseByName(warehouseName);
        return ResponseEntity.ok(ApiResponse.success(warehouse, "Warehouse retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<Warehouse>>> getAllWarehouses(Pageable pageable) {
        Page<Warehouse> warehouses = warehouseService.getAllWarehouses(pageable);
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Warehouses retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getAllWarehousesList() {
        List<Warehouse> warehouses = warehouseService.getAllWarehouses();
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Warehouses list retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<Warehouse>>> searchWarehouses(@RequestParam String keyword, Pageable pageable) {
        Page<Warehouse> warehouses = warehouseService.searchWarehouses(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Search results retrieved successfully"));
    }
    
    // Status Operations
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Warehouse>> activateWarehouse(@PathVariable Long id) {
        log.info("Activating warehouse: {}", id);
        Warehouse activated = warehouseService.activateWarehouse(id);
        return ResponseEntity.ok(ApiResponse.success(activated, "Warehouse activated successfully"));
    }
    
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Warehouse>> deactivateWarehouse(@PathVariable Long id) {
        log.info("Deactivating warehouse: {}", id);
        Warehouse deactivated = warehouseService.deactivateWarehouse(id);
        return ResponseEntity.ok(ApiResponse.success(deactivated, "Warehouse deactivated successfully"));
    }
    
    @PutMapping("/{id}/set-default")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Warehouse>> setAsDefault(@PathVariable Long id) {
        log.info("Setting warehouse as default: {}", id);
        Warehouse warehouse = warehouseService.setAsDefault(id);
        return ResponseEntity.ok(ApiResponse.success(warehouse, "Warehouse set as default successfully"));
    }
    
    // Query Operations
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getActiveWarehouses() {
        List<Warehouse> warehouses = warehouseService.getActiveWarehouses();
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Active warehouses retrieved successfully"));
    }
    
    @GetMapping("/inactive")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getInactiveWarehouses() {
        List<Warehouse> warehouses = warehouseService.getInactiveWarehouses();
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Inactive warehouses retrieved successfully"));
    }
    
    @GetMapping("/default")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Warehouse>> getDefaultWarehouse() {
        Warehouse warehouse = warehouseService.getDefaultWarehouse();
        return ResponseEntity.ok(ApiResponse.success(warehouse, "Default warehouse retrieved successfully"));
    }
    
    @GetMapping("/type/main")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getMainWarehouses() {
        List<Warehouse> warehouses = warehouseService.getMainWarehouses();
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Main warehouses retrieved successfully"));
    }
    
    @GetMapping("/type/branch")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getBranchWarehouses() {
        List<Warehouse> warehouses = warehouseService.getBranchWarehouses();
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Branch warehouses retrieved successfully"));
    }
    
    @GetMapping("/type/transit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getTransitWarehouses() {
        List<Warehouse> warehouses = warehouseService.getTransitWarehouses();
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Transit warehouses retrieved successfully"));
    }
    
    @GetMapping("/type/retail")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getRetailWarehouses() {
        List<Warehouse> warehouses = warehouseService.getRetailWarehouses();
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Retail warehouses retrieved successfully"));
    }
    
    @GetMapping("/type/{warehouseType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getWarehousesByType(@PathVariable String warehouseType) {
        List<Warehouse> warehouses = warehouseService.getWarehousesByType(warehouseType);
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Warehouses by type retrieved successfully"));
    }
    
    @GetMapping("/city/{city}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getWarehousesByCity(@PathVariable String city) {
        List<Warehouse> warehouses = warehouseService.getWarehousesByCity(city);
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Warehouses by city retrieved successfully"));
    }
    
    @GetMapping("/state/{stateProvince}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getWarehousesByState(@PathVariable String stateProvince) {
        List<Warehouse> warehouses = warehouseService.getWarehousesByState(stateProvince);
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Warehouses by state retrieved successfully"));
    }
    
    @GetMapping("/region/{region}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getWarehousesByRegion(@PathVariable String region) {
        List<Warehouse> warehouses = warehouseService.getWarehousesByRegion(region);
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Warehouses by region retrieved successfully"));
    }
    
    @GetMapping("/capacity-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getWarehousesByCapacityRange(@RequestParam Double minCapacity, @RequestParam Double maxCapacity) {
        List<Warehouse> warehouses = warehouseService.getWarehousesByCapacityRange(minCapacity, maxCapacity);
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Warehouses by capacity range retrieved successfully"));
    }
    
    @GetMapping("/low-utilization")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getWarehousesWithLowUtilization(@RequestParam(defaultValue = "0.3") Double threshold) {
        List<Warehouse> warehouses = warehouseService.getWarehousesWithLowUtilization(threshold);
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Low utilization warehouses retrieved successfully"));
    }
    
    @GetMapping("/high-utilization")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getWarehousesWithHighUtilization(@RequestParam(defaultValue = "0.9") Double threshold) {
        List<Warehouse> warehouses = warehouseService.getWarehousesWithHighUtilization(threshold);
        return ResponseEntity.ok(ApiResponse.success(warehouses, "High utilization warehouses retrieved successfully"));
    }
    
    @GetMapping("/near-capacity")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getWarehousesNearCapacity(@RequestParam(defaultValue = "0.95") Double threshold) {
        List<Warehouse> warehouses = warehouseService.getWarehousesNearCapacity(threshold);
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Warehouses near capacity retrieved successfully"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getRecentWarehouses(@RequestParam(defaultValue = "10") int limit) {
        List<Warehouse> warehouses = warehouseService.getRecentWarehouses(limit);
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Recent warehouses retrieved successfully"));
    }
    
    // Capacity Operations
    @PutMapping("/{id}/stock/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> updateCurrentStock(@PathVariable Long id, @RequestParam Double quantity) {
        log.info("Updating current stock for warehouse: {}", id);
        warehouseService.updateCurrentStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Current stock updated successfully"));
    }
    
    @PutMapping("/{id}/stock/increase")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> increaseStock(@PathVariable Long id, @RequestParam Double quantity) {
        log.info("Increasing stock for warehouse: {}", id);
        warehouseService.increaseStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock increased successfully"));
    }
    
    @PutMapping("/{id}/stock/decrease")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> decreaseStock(@PathVariable Long id, @RequestParam Double quantity) {
        log.info("Decreasing stock for warehouse: {}", id);
        warehouseService.decreaseStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock decreased successfully"));
    }
    
    @GetMapping("/{id}/capacity/utilization")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Double>> getCapacityUtilization(@PathVariable Long id) {
        Double utilization = warehouseService.getCapacityUtilization(id);
        return ResponseEntity.ok(ApiResponse.success(utilization, "Capacity utilization retrieved successfully"));
    }
    
    @GetMapping("/{id}/capacity/available")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Double>> getAvailableCapacity(@PathVariable Long id) {
        Double available = warehouseService.getAvailableCapacity(id);
        return ResponseEntity.ok(ApiResponse.success(available, "Available capacity retrieved successfully"));
    }
    
    // Validation
    @GetMapping("/validate/code/{warehouseCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> isWarehouseCodeAvailable(@PathVariable String warehouseCode) {
        boolean available = warehouseService.isWarehouseCodeAvailable(warehouseCode);
        return ResponseEntity.ok(ApiResponse.success(available, "Warehouse code availability checked"));
    }
    
    @GetMapping("/validate/name/{warehouseName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> isWarehouseNameAvailable(@PathVariable String warehouseName) {
        boolean available = warehouseService.isWarehouseNameAvailable(warehouseName);
        return ResponseEntity.ok(ApiResponse.success(available, "Warehouse name availability checked"));
    }
    
    @GetMapping("/{id}/can-delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> canDeleteWarehouse(@PathVariable Long id) {
        boolean canDelete = warehouseService.canDeleteWarehouse(id);
        return ResponseEntity.ok(ApiResponse.success(canDelete, "Delete check completed"));
    }
    
    // Batch Operations
    @PostMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> createBulkWarehouses(@Valid @RequestBody List<WarehouseRequest> requests) {
        log.info("Creating {} warehouses in bulk", requests.size());
        List<Warehouse> warehouses = warehouseService.createBulkWarehouses(requests);
        return ResponseEntity.ok(ApiResponse.success(warehouses, warehouses.size() + " warehouses created successfully"));
    }
    
    @PutMapping("/bulk/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> activateBulkWarehouses(@RequestBody List<Long> warehouseIds) {
        log.info("Activating {} warehouses in bulk", warehouseIds.size());
        int count = warehouseService.activateBulkWarehouses(warehouseIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " warehouses activated successfully"));
    }
    
    @PutMapping("/bulk/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> deactivateBulkWarehouses(@RequestBody List<Long> warehouseIds) {
        log.info("Deactivating {} warehouses in bulk", warehouseIds.size());
        int count = warehouseService.deactivateBulkWarehouses(warehouseIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " warehouses deactivated successfully"));
    }
    
    @DeleteMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> deleteBulkWarehouses(@RequestBody List<Long> warehouseIds) {
        log.info("Deleting {} warehouses in bulk", warehouseIds.size());
        int count = warehouseService.deleteBulkWarehouses(warehouseIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " warehouses deleted successfully"));
    }
    
    // Statistics
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getWarehouseStatistics() {
        Map<String, Object> statistics = warehouseService.getWarehouseStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Warehouse statistics retrieved successfully"));
    }
    
    @GetMapping("/statistics/type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTypeDistribution() {
        List<Map<String, Object>> distribution = warehouseService.getTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Type distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/by-region")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getWarehousesByRegionStats() {
        List<Map<String, Object>> stats = warehouseService.getWarehousesByRegionStats();
        return ResponseEntity.ok(ApiResponse.success(stats, "Warehouses by region retrieved successfully"));
    }
    
    @GetMapping("/statistics/capacity-utilization")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getCapacityUtilizationStats() {
        List<Map<String, Object>> stats = warehouseService.getCapacityUtilizationStats();
        return ResponseEntity.ok(ApiResponse.success(stats, "Capacity utilization stats retrieved successfully"));
    }
    
    @GetMapping("/statistics/total-capacity")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getTotalCapacity() {
        Double total = warehouseService.getTotalCapacity();
        return ResponseEntity.ok(ApiResponse.success(total, "Total capacity retrieved successfully"));
    }
    
    @GetMapping("/statistics/total-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getTotalCurrentStock() {
        Double total = warehouseService.getTotalCurrentStock();
        return ResponseEntity.ok(ApiResponse.success(total, "Total current stock retrieved successfully"));
    }
    
    @GetMapping("/statistics/average-utilization")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getAverageUtilization() {
        Double avg = warehouseService.getAverageUtilization();
        return ResponseEntity.ok(ApiResponse.success(avg, "Average utilization retrieved successfully"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = warehouseService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved successfully"));
    }
}
