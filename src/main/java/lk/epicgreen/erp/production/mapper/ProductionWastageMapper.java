package lk.epicgreen.erp.production.mapper;

import lk.epicgreen.erp.production.dto.request.ProductionWastageRequest;
import lk.epicgreen.erp.production.dto.response.ProductionWastageResponse;
import lk.epicgreen.erp.production.entity.ProductionWastage;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapper for ProductionWastage entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class ProductionWastageMapper {

    public ProductionWastage toEntity(ProductionWastageRequest request) {
        if (request == null) {
            return null;
        }

        BigDecimal totalValue = null;
        if (request.getUnitCost() != null && request.getQuantity() != null) {
            totalValue = request.getUnitCost().multiply(request.getQuantity());
        }

        return ProductionWastage.builder()
            .wastageDate(request.getWastageDate())
            .wastageType(request.getWastageType())
            .quantity(request.getQuantity())
            .unitCost(request.getUnitCost())
            .totalValue(totalValue)
            .reason(request.getReason())
            .build();
    }

    public ProductionWastageResponse toResponse(ProductionWastage wastage) {
        if (wastage == null) {
            return null;
        }

        BigDecimal totalValue = null;
        if (wastage.getUnitCost() != null && wastage.getQuantity() != null) {
            totalValue = wastage.getUnitCost().multiply(wastage.getQuantity());
        }

        return ProductionWastageResponse.builder()
            .id(wastage.getId())
            .woId(wastage.getWorkOrder() != null ? wastage.getWorkOrder().getId() : null)
            .woNumber(wastage.getWorkOrder() != null ? wastage.getWorkOrder().getWoNumber() : null)
            .wastageDate(wastage.getWastageDate())
            .productId(wastage.getProduct() != null ? wastage.getProduct().getId() : null)
            .productCode(wastage.getProduct() != null ? wastage.getProduct().getProductCode() : null)
            .productName(wastage.getProduct() != null ? wastage.getProduct().getProductName() : null)
            .wastageType(wastage.getWastageType())
            .quantity(wastage.getQuantity())
            .uomId(wastage.getUom() != null ? wastage.getUom().getId() : null)
            .uomCode(wastage.getUom() != null ? wastage.getUom().getUomCode() : null)
            .uomName(wastage.getUom() != null ? wastage.getUom().getUomName() : null)
            .unitCost(wastage.getUnitCost())
            .totalValue(totalValue)
            .reason(wastage.getReason())
            .createdAt(wastage.getCreatedAt())
            .createdBy(wastage.getCreatedBy())
            .build();
    }
}
