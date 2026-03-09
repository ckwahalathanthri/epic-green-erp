package lk.epicgreen.erp.warehouse.mapper;

import lk.epicgreen.erp.warehouse.dto.response.StockTransferDTO;
import lk.epicgreen.erp.warehouse.entity.StockTransfer;
import org.springframework.stereotype.Component;

@Component
public class StockTransferMapper {
    public StockTransferDTO toDTO(StockTransfer entity) {
        StockTransferDTO dto = new StockTransferDTO();
        dto.setId(entity.getId());
        dto.setTransferNumber(entity.getTransferNumber());
        dto.setTransferDate(entity.getTransferDate());
        dto.setFromWarehouseId(entity.getFromWarehouseId());
        dto.setToWarehouseId(entity.getToWarehouseId());
        dto.setTransferStatus(entity.getTransferStatus());
        return dto;
    }
}
