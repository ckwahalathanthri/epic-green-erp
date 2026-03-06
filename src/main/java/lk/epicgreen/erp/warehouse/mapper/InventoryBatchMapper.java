package lk.epicgreen.erp.warehouse.mapper;


import lk.epicgreen.erp.warehouse.dto.response.InventoryBatchDTO;
import lk.epicgreen.erp.warehouse.entity.InventoryBatch;
import org.springframework.stereotype.Component;

@Component
public class InventoryBatchMapper {
    public InventoryBatchDTO toDTO(InventoryBatch entity) {
        InventoryBatchDTO dto = new InventoryBatchDTO();
        dto.setId(entity.getId());
        dto.setBatchNumber(entity.getBatchNumber());
        dto.setQuantity(entity.getQuantity());
        return dto;
    }
}