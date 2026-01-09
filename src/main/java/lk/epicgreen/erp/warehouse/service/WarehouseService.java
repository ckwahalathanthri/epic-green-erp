package lk.epicgreen.erp.warehouse.service;

import lk.epicgreen.erp.warehouse.dto.request.WarehouseRequest;
import lk.epicgreen.erp.warehouse.dto.response.WarehouseResponse;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

/**
 * Service interface for Warehouse entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface WarehouseService {

    WarehouseResponse createWarehouse(WarehouseRequest request);
    WarehouseResponse updateWarehouse(Long id, WarehouseRequest request);
    void activateWarehouse(Long id);
    void deactivateWarehouse(Long id);
    void assignManager(Long warehouseId, Long managerId);
    WarehouseResponse getWarehouseById(Long id);
    WarehouseResponse getWarehouseByCode(String warehouseCode);
    PageResponse<WarehouseResponse> getAllWarehouses(Pageable pageable);
    List<WarehouseResponse> getAllActiveWarehouses();
    PageResponse<WarehouseResponse> getWarehousesByType(String warehouseType, Pageable pageable);
    List<WarehouseResponse> getRawMaterialWarehouses();
    List<WarehouseResponse> getFinishedGoodsWarehouses();
    List<WarehouseResponse> getMixedWarehouses();
    List<WarehouseResponse> getWarehousesByManager(Long managerId);
    PageResponse<WarehouseResponse> searchWarehouses(String keyword, Pageable pageable);
    void deleteWarehouse(Long id);
    WarehouseResponse getWarehouseByName(String warehouseName);
    WarehouseResponse setAsDefault(Long id);
    List<WarehouseResponse> getActiveWarehouses();
    List<WarehouseResponse> getInactiveWarehouses();
    WarehouseResponse getDefaultWarehouse();
    List<WarehouseResponse> getMainWarehouses();
    List<WarehouseResponse> getBranchWarehouses();
    List<WarehouseResponse> getTransitWarehouses();
    List<WarehouseResponse> getRetailWarehouses();
    List<WarehouseResponse> getWarehousesByType(String warehouseType);
    List<WarehouseResponse> getWarehousesByCity(String city);
    List<WarehouseResponse> getWarehousesByState(String stateProvince);
    List<WarehouseResponse> getWarehousesByRegion(String region);
    List<WarehouseResponse> getWarehousesByCapacityRange(Double minCapacity, Double maxCapacity);
    List<WarehouseResponse> getWarehousesWithLowUtilization(Double threshold);
    List<WarehouseResponse> getWarehousesWithHighUtilization(Double threshold);
    List<WarehouseResponse> getWarehousesNearCapacity(Double threshold);
    List<WarehouseResponse> getRecentWarehouses(int limit);
    void updateCurrentStock(Long id, Double quantity);
    void increaseStock(Long id, Double quantity);
    void decreaseStock(Long id, Double quantity);
    Double getCapacityUtilization(Long id);
    Double getAvailableCapacity(Long id);
    boolean isWarehouseCodeAvailable(String warehouseCode);
    boolean isWarehouseNameAvailable(String warehouseName);
    boolean canDeleteWarehouse(Long id);
    List<Warehouse> createBulkWarehouses(List<WarehouseRequest> requests);
	int activateBulkWarehouses(List<Long> warehouseIds);
	int deactivateBulkWarehouses(List<Long> warehouseIds);
	int deleteBulkWarehouses(List<Long> warehouseIds);
	Map<String, Object> getWarehouseStatistics();
	List<WarehouseResponse> getAllWarehouses();
	List<Map<String, Object>> getTypeDistribution();
	List<Map<String, Object>> getWarehousesByRegionStats();
	List<Map<String, Object>> getCapacityUtilizationStats();
	Double getTotalCapacity();
	Double getTotalCurrentStock();
	Double getAverageUtilization();
	Map<String, Object> getDashboardStatistics();
}
