package lk.epicgreen.erp.warehouse.mapper;

import lk.epicgreen.erp.warehouse.dto.response.InventoryItemDTO;
import lk.epicgreen.erp.warehouse.entity.InventoryItem;
import org.springframework.stereotype.Component;

@Component
public class InventoryItemMapper {
    public InventoryItemDTO toDTO(InventoryItem entity) {
        InventoryItemDTO dto = new InventoryItemDTO();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProductId());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setQuantityOnHand(entity.getQuantityOnHand());
        dto.setQuantityAvailable(entity.getQuantityAvailable());
        return dto;
    }
}