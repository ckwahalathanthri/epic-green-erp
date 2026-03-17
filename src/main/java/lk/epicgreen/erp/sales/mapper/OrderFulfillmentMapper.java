package lk.epicgreen.erp.sales.mapper;

import lk.epicgreen.erp.sales.dto.response.OrderFulfillmentDTO;
import lk.epicgreen.erp.sales.entity.OrderFulfillment;
import org.springframework.stereotype.Component;

@Component
public class OrderFulfillmentMapper {
    
    public OrderFulfillmentDTO toDTO(OrderFulfillment entity) {
        if (entity == null) return null;
        
        OrderFulfillmentDTO dto = new OrderFulfillmentDTO();
        dto.setId(entity.getId());
        dto.setOrderId(entity.getOrderId());
        dto.setFulfillmentStatus(entity.getFulfillmentStatus());
        dto.setPickingProgress(entity.getPickingProgress());
        dto.setPackingProgress(entity.getPackingProgress());
        dto.setDispatchProgress(entity.getDispatchProgress());
        return dto;
    }
    
    public void updateEntityFromDTO(OrderFulfillmentDTO dto, OrderFulfillment entity) {
        if (dto == null || entity == null) return;
        entity.setOrderId(dto.getOrderId());
        entity.setFulfillmentStatus(dto.getFulfillmentStatus());
    }
}