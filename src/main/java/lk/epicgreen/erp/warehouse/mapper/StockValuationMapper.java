package lk.epicgreen.erp.warehouse.mapper;


import lk.epicgreen.erp.warehouse.dto.response.StockValuationDTO;
import lk.epicgreen.erp.warehouse.entity.StockValuation;
import org.springframework.stereotype.Component;

@Component
public class StockValuationMapper {
    public StockValuationDTO toDTO(StockValuation entity) {
        StockValuationDTO dto = new StockValuationDTO();
        dto.setId(entity.getId());
        dto.setValuationDate(entity.getValuationDate());
        dto.setTotalValue(entity.getTotalValue());
        return dto;
    }
}