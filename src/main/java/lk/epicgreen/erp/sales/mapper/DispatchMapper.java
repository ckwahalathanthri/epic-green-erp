package lk.epicgreen.erp.sales.mapper;


import lk.epicgreen.erp.sales.dto.response.DispatchDTO;
import lk.epicgreen.erp.sales.entity.Dispatch;
import org.springframework.stereotype.Component;

@Component
public class DispatchMapper {
    
    public DispatchDTO toDTO(Dispatch entity) {
        if (entity == null) return null;
        
        DispatchDTO dto = new DispatchDTO();
        dto.setId(entity.getId());
        dto.setDispatchNumber(entity.getDispatchNumber());
        dto.setDispatchDate(entity.getDispatchDate());
        dto.setOrderId(entity.getOrderId());
        dto.setDispatchStatus(entity.getDispatchStatus());
        dto.setVehicleNumber(entity.getVehicleNumber());
        dto.setDriverName(entity.getDriverName());
        dto.setStockDeducted(entity.getStockDeducted());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
    
    public Dispatch toEntity(DispatchDTO dto) {
        if (dto == null) return null;
        
        Dispatch entity = new Dispatch();
        entity.setDispatchNumber(dto.getDispatchNumber());
        entity.setDispatchDate(dto.getDispatchDate());
        entity.setOrderId(dto.getOrderId());
        entity.setDispatchStatus(dto.getDispatchStatus());
        return entity;
    }
    
    public void updateEntityFromDTO(DispatchDTO dto, Dispatch entity) {
        if (dto == null || entity == null) return;
        entity.setDispatchStatus(dto.getDispatchStatus());
        entity.setVehicleNumber(dto.getVehicleNumber());
    }
}