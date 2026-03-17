package lk.epicgreen.erp.warehouse.mapper;

import lk.epicgreen.erp.warehouse.dto.response.InventoryAlertDTO;
import lk.epicgreen.erp.warehouse.entity.InventoryAlert;
import org.springframework.stereotype.Component;

@Component
public class InventoryAlertMapper {
    public InventoryAlertDTO toDTO(InventoryAlert entity) {
        InventoryAlertDTO dto = new InventoryAlertDTO();
        dto.setId(entity.getId());
        dto.setAlertType(entity.getAlertType());
        dto.setAlertSeverity(entity.getAlertSeverity());
        dto.setProductId(entity.getProductId());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setAlertStatus(entity.getAlertStatus());
        return dto;
    }
}
