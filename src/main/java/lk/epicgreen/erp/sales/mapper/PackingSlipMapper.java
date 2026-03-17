package lk.epicgreen.erp.sales.mapper;


import lk.epicgreen.erp.sales.dto.response.PackingSlipDTO;
import lk.epicgreen.erp.sales.entity.PackingSlip;
import org.springframework.stereotype.Component;

@Component
public class PackingSlipMapper {
    
    public PackingSlipDTO toDTO(PackingSlip entity) {
        if (entity == null) return null;
        
        PackingSlipDTO dto = new PackingSlipDTO();
        dto.setId(entity.getId());
        dto.setPackingNumber(entity.getPackingNumber());
        dto.setPackingDate(entity.getPackingDate());
        dto.setOrderId(entity.getOrderId());
        dto.setPackingStatus(entity.getPackingStatus());
        dto.setTotalItems(entity.getTotalItems());
        dto.setPackedItems(entity.getPackedItems());
        return dto;
    }
    
    public PackingSlip toEntity(PackingSlipDTO dto) {
        if (dto == null) return null;
        
        PackingSlip entity = new PackingSlip();
        entity.setPackingNumber(dto.getPackingNumber());
        entity.setPackingDate(dto.getPackingDate());
        entity.setOrderId(dto.getOrderId());
        return entity;
    }
    
    public void updateEntityFromDTO(PackingSlipDTO dto, PackingSlip entity) {
        if (dto == null || entity == null) return;
        entity.setPackingStatus(dto.getPackingStatus());
    }
}