package lk.epicgreen.erp.warehouse.service;

import lk.epicgreen.erp.warehouse.dto.request.WarehouseRequest;
import lk.epicgreen.erp.warehouse.dto.response.WarehouseResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
}
