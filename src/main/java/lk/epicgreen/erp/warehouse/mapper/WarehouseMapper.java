package lk.epicgreen.erp.warehouse.mapper;

import lk.epicgreen.erp.warehouse.dto.request.WarehouseRequest;
import lk.epicgreen.erp.warehouse.dto.response.WarehouseResponse;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import org.springframework.stereotype.Component;

/**
 * Mapper for Warehouse entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class WarehouseMapper {

    public Warehouse toEntity(WarehouseRequest request) {
        if (request == null) {
            return null;
        }

        return Warehouse.builder()
            .warehouseCode(request.getWarehouseCode())
            .warehouseName(request.getWarehouseName())
            .warehouseType(request.getWarehouseType())
            .addressLine1(request.getAddressLine1())
            .addressLine2(request.getAddressLine2())
            .city(request.getCity())
            .state(request.getState())
            .postalCode(request.getPostalCode())
            .contactNumber(request.getContactNumber())
            .isActive(request.getIsActive() != null ? request.getIsActive() : true)
            .build();
    }

    public void updateEntityFromRequest(WarehouseRequest request, Warehouse warehouse) {
        if (request == null || warehouse == null) {
            return;
        }

        warehouse.setWarehouseCode(request.getWarehouseCode());
        warehouse.setWarehouseName(request.getWarehouseName());
        warehouse.setWarehouseType(request.getWarehouseType());
        warehouse.setAddressLine1(request.getAddressLine1());
        warehouse.setAddressLine2(request.getAddressLine2());
        warehouse.setCity(request.getCity());
        warehouse.setState(request.getState());
        warehouse.setPostalCode(request.getPostalCode());
        warehouse.setContactNumber(request.getContactNumber());
        
        if (request.getIsActive() != null) {
            warehouse.setIsActive(request.getIsActive());
        }
    }

    public WarehouseResponse toResponse(Warehouse warehouse) {
        if (warehouse == null) {
            return null;
        }

        return WarehouseResponse.builder()
            .id(warehouse.getId())
            .warehouseCode(warehouse.getWarehouseCode())
            .warehouseName(warehouse.getWarehouseName())
            .warehouseType(warehouse.getWarehouseType())
            .addressLine1(warehouse.getAddressLine1())
            .addressLine2(warehouse.getAddressLine2())
            .city(warehouse.getCity())
            .state(warehouse.getState())
            .postalCode(warehouse.getPostalCode())
            .managerId(warehouse.getManager() != null ? warehouse.getManager().getId() : null)
            .managerName(warehouse.getManager() != null ? 
                warehouse.getManager().getFirstName() + " " + warehouse.getManager().getLastName() : null)
            .contactNumber(warehouse.getContactNumber())
            .isActive(warehouse.getIsActive())
            .createdAt(warehouse.getCreatedAt())
            .updatedAt(warehouse.getUpdatedAt())
            .build();
    }
}
