package lk.epicgreen.erp.warehouse.service;

import lk.epicgreen.erp.warehouse.dto.WarehouseRequest;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Warehouse Service Interface
 * Service for warehouse operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface WarehouseService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    Warehouse createWarehouse(WarehouseRequest request);
    Warehouse updateWarehouse(Long id, WarehouseRequest request);
    void deleteWarehouse(Long id);
    Warehouse getWarehouseById(Long id);
    Warehouse getWarehouseByCode(String warehouseCode);
    Warehouse getWarehouseByName(String warehouseName);
    List<Warehouse> getAllWarehouses();
    Page<Warehouse> getAllWarehouses(Pageable pageable);
    Page<Warehouse> searchWarehouses(String keyword, Pageable pageable);
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    Warehouse activateWarehouse(Long id);
    Warehouse deactivateWarehouse(Long id);
    Warehouse setAsDefault(Long id);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    List<Warehouse> getActiveWarehouses();
    List<Warehouse> getInactiveWarehouses();
    Warehouse getDefaultWarehouse();
    List<Warehouse> getMainWarehouses();
    List<Warehouse> getBranchWarehouses();
    List<Warehouse> getTransitWarehouses();
    List<Warehouse> getRetailWarehouses();
    List<Warehouse> getWarehousesByType(String warehouseType);
    List<Warehouse> getWarehousesByCity(String city);
    List<Warehouse> getWarehousesByState(String stateProvince);
    List<Warehouse> getWarehousesByRegion(String region);
    List<Warehouse> getWarehousesByCapacityRange(Double minCapacity, Double maxCapacity);
    List<Warehouse> getWarehousesWithLowUtilization(Double threshold);
    List<Warehouse> getWarehousesWithHighUtilization(Double threshold);
    List<Warehouse> getWarehousesNearCapacity(Double threshold);
    List<Warehouse> getRecentWarehouses(int limit);
    
    // ===================================================================
    // CAPACITY OPERATIONS
    // ===================================================================
    
    void updateCurrentStock(Long warehouseId, Double quantity);
    void increaseStock(Long warehouseId, Double quantity);
    void decreaseStock(Long warehouseId, Double quantity);
    Double getCapacityUtilization(Long warehouseId);
    Double getAvailableCapacity(Long warehouseId);
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    boolean validateWarehouse(Warehouse warehouse);
    boolean isWarehouseCodeAvailable(String warehouseCode);
    boolean isWarehouseNameAvailable(String warehouseName);
    boolean canDeleteWarehouse(Long warehouseId);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<Warehouse> createBulkWarehouses(List<WarehouseRequest> requests);
    int activateBulkWarehouses(List<Long> warehouseIds);
    int deactivateBulkWarehouses(List<Long> warehouseIds);
    int deleteBulkWarehouses(List<Long> warehouseIds);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    Map<String, Object> getWarehouseStatistics();
    List<Map<String, Object>> getWarehouseTypeDistribution();
    List<Map<String, Object>> getStatusDistribution();
    List<Map<String, Object>> getWarehousesByCity();
    List<Map<String, Object>> getWarehousesByState();
    List<Map<String, Object>> getCapacityUtilizationByWarehouse();
    Double getTotalCapacity();
    Double getTotalCurrentStock();
    Double getAverageCapacityUtilization();
    Map<String, Object> getDashboardStatistics();
}
