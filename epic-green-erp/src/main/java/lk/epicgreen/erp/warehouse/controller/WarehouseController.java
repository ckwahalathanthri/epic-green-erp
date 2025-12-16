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
 * REST controller for warehouse operations
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
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Warehouse>> createWarehouse(
        @Valid @RequestBody WarehouseRequest request
    ) {
        Warehouse created = warehouseService.createWarehouse(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Warehouse created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Warehouse>> updateWarehouse(
        @PathVariable Long id,
        @Valid @RequestBody WarehouseRequest request
    ) {
        Warehouse updated = warehouseService.updateWarehouse(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Warehouse updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteWarehouse(@PathVariable Long id) {
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
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<Warehouse>>> getAllWarehouses(Pageable pageable) {
        Page<Warehouse> warehouses = warehouseService.getAllWarehouses(pageable);
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Warehouses retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<Warehouse>>> searchWarehouses(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<Warehouse> warehouses = warehouseService.searchWarehouses(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Search results retrieved"));
    }
    
    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Warehouse>> activateWarehouse(@PathVariable Long id) {
        Warehouse activated = warehouseService.activateWarehouse(id);
        return ResponseEntity.ok(ApiResponse.success(activated, "Warehouse activated"));
    }
    
    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Warehouse>> deactivateWarehouse(@PathVariable Long id) {
        Warehouse deactivated = warehouseService.deactivateWarehouse(id);
        return ResponseEntity.ok(ApiResponse.success(deactivated, "Warehouse deactivated"));
    }
    
    @PostMapping("/{id}/set-default")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Warehouse>> setAsDefault(@PathVariable Long id) {
        Warehouse defaultWarehouse = warehouseService.setAsDefault(id);
        return ResponseEntity.ok(ApiResponse.success(defaultWarehouse, "Warehouse set as default"));
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getActiveWarehouses() {
        List<Warehouse> warehouses = warehouseService.getActiveWarehouses();
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Active warehouses retrieved"));
    }
    
    @GetMapping("/inactive")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getInactiveWarehouses() {
        List<Warehouse> warehouses = warehouseService.getInactiveWarehouses();
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Inactive warehouses retrieved"));
    }
    
    @GetMapping("/default")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Warehouse>> getDefaultWarehouse() {
        Warehouse warehouse = warehouseService.getDefaultWarehouse();
        return ResponseEntity.ok(ApiResponse.success(warehouse, "Default warehouse retrieved"));
    }
    
    @GetMapping("/type/main")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getMainWarehouses() {
        List<Warehouse> warehouses = warehouseService.getMainWarehouses();
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Main warehouses retrieved"));
    }
    
    @GetMapping("/type/branch")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getBranchWarehouses() {
        List<Warehouse> warehouses = warehouseService.getBranchWarehouses();
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Branch warehouses retrieved"));
    }
    
    @GetMapping("/type/transit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getTransitWarehouses() {
        List<Warehouse> warehouses = warehouseService.getTransitWarehouses();
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Transit warehouses retrieved"));
    }
    
    @GetMapping("/type/retail")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getRetailWarehouses() {
        List<Warehouse> warehouses = warehouseService.getRetailWarehouses();
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Retail warehouses retrieved"));
    }
    
    @GetMapping("/city/{city}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getWarehousesByCity(@PathVariable String city) {
        List<Warehouse> warehouses = warehouseService.getWarehousesByCity(city);
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Warehouses by city retrieved"));
    }
    
    @GetMapping("/low-utilization")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getWarehousesWithLowUtilization(
        @RequestParam(defaultValue = "0.3") Double threshold
    ) {
        List<Warehouse> warehouses = warehouseService.getWarehousesWithLowUtilization(threshold);
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Low utilization warehouses retrieved"));
    }
    
    @GetMapping("/high-utilization")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getWarehousesWithHighUtilization(
        @RequestParam(defaultValue = "0.9") Double threshold
    ) {
        List<Warehouse> warehouses = warehouseService.getWarehousesWithHighUtilization(threshold);
        return ResponseEntity.ok(ApiResponse.success(warehouses, "High utilization warehouses retrieved"));
    }
    
    @GetMapping("/near-capacity")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getWarehousesNearCapacity(
        @RequestParam(defaultValue = "0.95") Double threshold
    ) {
        List<Warehouse> warehouses = warehouseService.getWarehousesNearCapacity(threshold);
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Near capacity warehouses retrieved"));
    }
    
    @GetMapping("/{id}/capacity-utilization")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getCapacityUtilization(@PathVariable Long id) {
        Double utilization = warehouseService.getCapacityUtilization(id);
        return ResponseEntity.ok(ApiResponse.success(utilization, "Capacity utilization retrieved"));
    }
    
    @GetMapping("/{id}/available-capacity")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getAvailableCapacity(@PathVariable Long id) {
        Double available = warehouseService.getAvailableCapacity(id);
        return ResponseEntity.ok(ApiResponse.success(available, "Available capacity retrieved"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getRecentWarehouses(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<Warehouse> warehouses = warehouseService.getRecentWarehouses(limit);
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Recent warehouses retrieved"));
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = warehouseService.getWarehouseStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = warehouseService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
}
