package lk.epicgreen.erp.warehouse.mapper;

import lk.epicgreen.erp.warehouse.dto.response.GoodsIssueDTO;
import lk.epicgreen.erp.warehouse.entity.GoodsIssue;
import org.springframework.stereotype.Component;

@Component
public class GoodsIssueMapper {
    public GoodsIssueDTO toDTO(GoodsIssue entity) {
        GoodsIssueDTO dto = new GoodsIssueDTO();
        dto.setId(entity.getId());
        dto.setIssueNumber(entity.getIssueNumber());
        dto.setIssueDate(entity.getIssueDate());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setIssueType(entity.getIssueType());
        return dto;
    }
}
