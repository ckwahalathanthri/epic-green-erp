package lk.epicgreen.erp.warehouse.mapper;


import lk.epicgreen.erp.warehouse.dto.response.StockReservationDTO;
import lk.epicgreen.erp.warehouse.entity.StockReservation;
import org.springframework.stereotype.Component;

@Component
public class StockReservationMapper {
    public StockReservationDTO toDTO(StockReservation entity) {
        StockReservationDTO dto = new StockReservationDTO();
        dto.setId(entity.getId());
        dto.setReservationNumber(entity.getReservationNumber());
        dto.setProductId(entity.getProductId());
        dto.setWarehouseId(entity.getWarehouse().getId());
        dto.setReservedQuantity(entity.getReservedQuantity());
        dto.setReservationStatus(entity.getReservationStatus());
        return dto;
    }
}
