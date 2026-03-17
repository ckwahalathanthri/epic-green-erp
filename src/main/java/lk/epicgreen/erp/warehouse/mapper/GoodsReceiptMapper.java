package lk.epicgreen.erp.warehouse.mapper;


import lk.epicgreen.erp.warehouse.dto.response.GoodsReceiptDTO;
import lk.epicgreen.erp.warehouse.entity.GoodsReceipt;
import org.springframework.stereotype.Component;

@Component
public class GoodsReceiptMapper {
    public GoodsReceiptDTO toDTO(GoodsReceipt entity) {
        GoodsReceiptDTO dto = new GoodsReceiptDTO();
        dto.setId(entity.getId());
        dto.setReceiptNumber(entity.getReceiptNumber());
        dto.setReceiptDate(entity.getReceiptDate());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setReceiptType(entity.getReceiptType());
        return dto;
    }
}
