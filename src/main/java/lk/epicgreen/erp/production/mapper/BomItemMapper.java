package lk.epicgreen.erp.production.mapper;

import lk.epicgreen.erp.production.dto.request.BomItemRequest;
import lk.epicgreen.erp.production.dto.response.BomItemResponse;
import lk.epicgreen.erp.production.entity.BomItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapper for BomItem entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class BomItemMapper {

    public BomItem toEntity(BomItemRequest request) {
        if (request == null) {
            return null;
        }

        BigDecimal totalCost = null;
        if (request.getStandardCost() != null && request.getQuantityRequired() != null) {
            totalCost = request.getStandardCost().multiply(request.getQuantityRequired());
        }

        return BomItem.builder()
            .quantityRequired(request.getQuantityRequired())
            .wastagePercentage(request.getWastagePercentage() != null ? request.getWastagePercentage() : BigDecimal.ZERO)
            .standardCost(request.getStandardCost())
            .sequenceNumber(request.getSequenceNumber())
            .isCritical(request.getIsCritical() != null ? request.getIsCritical() : false)
            .remarks(request.getRemarks())
            .build();
    }

    public BomItemResponse toResponse(BomItem item) {
        if (item == null) {
            return null;
        }

        BigDecimal totalCost = null;
        if (item.getStandardCost() != null && item.getQuantityRequired() != null) {
            totalCost = item.getStandardCost().multiply(item.getQuantityRequired());
        }

        return BomItemResponse.builder()
            .id(item.getId())
            .bomId(item.getBom() != null ? item.getBom().getId() : null)
            .rawMaterialId(item.getRawMaterial() != null ? item.getRawMaterial().getId() : null)
            .rawMaterialCode(item.getRawMaterial() != null ? item.getRawMaterial().getProductCode() : null)
            .rawMaterialName(item.getRawMaterial() != null ? item.getRawMaterial().getProductName() : null)
            .quantityRequired(item.getQuantityRequired())
            .uomId(item.getUom() != null ? item.getUom().getId() : null)
            .uomCode(item.getUom() != null ? item.getUom().getUomCode() : null)
            .uomName(item.getUom() != null ? item.getUom().getUomName() : null)
            .wastagePercentage(item.getWastagePercentage())
            .standardCost(item.getStandardCost())
            .totalCost(totalCost)
            .sequenceNumber(item.getSequenceNumber())
            .isCritical(item.getIsCritical())
            .remarks(item.getRemarks())
            .build();
    }
}
