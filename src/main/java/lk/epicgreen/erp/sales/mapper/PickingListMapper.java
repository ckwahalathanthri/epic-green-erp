package lk.epicgreen.erp.sales.mapper;


import lk.epicgreen.erp.sales.dto.response.PickingListDTO;
import lk.epicgreen.erp.sales.entity.PickingList;
import org.springframework.stereotype.Component;

@Component
public class PickingListMapper {
    
    public PickingListDTO toDTO(PickingList entity) {
        if (entity == null) return null;
        
        PickingListDTO dto = new PickingListDTO();
        dto.setId(entity.getId());
        dto.setPickingNumber(entity.getPickingNumber());
        dto.setPickingDate(entity.getPickingDate());
        dto.setOrderId(entity.getOrderId());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setPickingStatus(entity.getPickingStatus());
        dto.setTotalItems(entity.getTotalItems());
        dto.setPickedItems(entity.getPickedItems());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
    
    public PickingList toEntity(PickingListDTO dto) {
        if (dto == null) return null;
        
        PickingList entity = new PickingList();
        entity.setPickingNumber(dto.getPickingNumber());
        entity.setPickingDate(dto.getPickingDate());
        entity.setOrderId(dto.getOrderId());
        entity.setPickingStatus(dto.getPickingStatus());
        return entity;
    }
    
    public void updateEntityFromDTO(PickingListDTO dto, PickingList entity) {
        if (dto == null || entity == null) return;
        entity.setPickingStatus(dto.getPickingStatus());
        entity.setAssignedTo(dto.getAssignedTo());
    }
}