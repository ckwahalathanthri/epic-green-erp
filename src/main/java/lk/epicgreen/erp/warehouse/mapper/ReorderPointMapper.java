package lk.epicgreen.erp.warehouse.mapper;


import lk.epicgreen.erp.warehouse.dto.response.ReorderPointDTO;
import lk.epicgreen.erp.warehouse.entity.ReorderPoint;
import org.springframework.stereotype.Component;

@Component
public class ReorderPointMapper {
    public ReorderPointDTO toDTO(ReorderPoint entity) {
        ReorderPointDTO dto = new ReorderPointDTO();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProductId());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setMinStockLevel(entity.getMinStockLevel());
        dto.setReorderPoint(entity.getReorderPoint());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
