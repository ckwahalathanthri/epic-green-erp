package lk.epicgreen.erp.warehouse.mapper;

import lk.epicgreen.erp.warehouse.dto.response.PhysicalInventoryDTO;
import lk.epicgreen.erp.warehouse.entity.PhysicalInventory;
import org.springframework.stereotype.Component;

@Component
public class PhysicalInventoryMapper {
    public PhysicalInventoryDTO toDTO(PhysicalInventory entity) {
        PhysicalInventoryDTO dto = new PhysicalInventoryDTO();
        dto.setId(entity.getId());
        dto.setCountNumber(entity.getCountNumber());
        dto.setCountDate(entity.getCountDate());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setCountType(entity.getCountType());
        dto.setCountStatus(entity.getCountStatus());
        dto.setTotalItemsCounted(entity.getTotalItemsCounted());
        dto.setTotalDiscrepancies(entity.getTotalDiscrepancies());
        return dto;
    }
}
