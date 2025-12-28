package lk.epicgreen.erp.warehouse.mapper;

import lk.epicgreen.erp.warehouse.dto.request.WarehouseLocationRequest;
import lk.epicgreen.erp.warehouse.dto.response.WarehouseLocationResponse;
import lk.epicgreen.erp.warehouse.entity.WarehouseLocation;
import org.springframework.stereotype.Component;

/**
 * Mapper for WarehouseLocation entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class WarehouseLocationMapper {

    public WarehouseLocation toEntity(WarehouseLocationRequest request) {
        if (request == null) {
            return null;
        }

        return WarehouseLocation.builder()
            .locationCode(request.getLocationCode())
            .locationName(request.getLocationName())
            .aisle(request.getAisle())
            .rack(request.getRack())
            .shelf(request.getShelf())
            .bin(request.getBin())
            .isActive(request.getIsActive() != null ? request.getIsActive() : true)
            .build();
    }

    public void updateEntityFromRequest(WarehouseLocationRequest request, WarehouseLocation location) {
        if (request == null || location == null) {
            return;
        }

        location.setLocationCode(request.getLocationCode());
        location.setLocationName(request.getLocationName());
        location.setAisle(request.getAisle());
        location.setRack(request.getRack());
        location.setShelf(request.getShelf());
        location.setBin(request.getBin());
        
        if (request.getIsActive() != null) {
            location.setIsActive(request.getIsActive());
        }
    }

    public WarehouseLocationResponse toResponse(WarehouseLocation location) {
        if (location == null) {
            return null;
        }

        return WarehouseLocationResponse.builder()
            .id(location.getId())
            .warehouseId(location.getWarehouse() != null ? location.getWarehouse().getId() : null)
            .warehouseCode(location.getWarehouse() != null ? location.getWarehouse().getWarehouseCode() : null)
            .warehouseName(location.getWarehouse() != null ? location.getWarehouse().getWarehouseName() : null)
            .locationCode(location.getLocationCode())
            .locationName(location.getLocationName())
            .aisle(location.getAisle())
            .rack(location.getRack())
            .shelf(location.getShelf())
            .bin(location.getBin())
            .isActive(location.getIsActive())
            .createdAt(location.getCreatedAt())
            .build();
    }
}
