package lk.epicgreen.erp.warehouse.service;

import lk.epicgreen.erp.warehouse.dto.request.WarehouseLocationRequest;
import lk.epicgreen.erp.warehouse.dto.response.WarehouseLocationResponse;

import java.util.List;

/**
 * Service interface for WarehouseLocation entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface WarehouseLocationService {

    WarehouseLocationResponse createWarehouseLocation(WarehouseLocationRequest request);
    WarehouseLocationResponse updateWarehouseLocation(Long id, WarehouseLocationRequest request);
    void activateWarehouseLocation(Long id);
    void deactivateWarehouseLocation(Long id);
    void deleteWarehouseLocation(Long id);
    WarehouseLocationResponse getWarehouseLocationById(Long id);
    List<WarehouseLocationResponse> getLocationsByWarehouse(Long warehouseId);
    List<WarehouseLocationResponse> getActiveLocationsByWarehouse(Long warehouseId);
    WarehouseLocationResponse getLocationByWarehouseAndCode(Long warehouseId, String locationCode);
    List<WarehouseLocationResponse> searchLocations(String keyword);
}
