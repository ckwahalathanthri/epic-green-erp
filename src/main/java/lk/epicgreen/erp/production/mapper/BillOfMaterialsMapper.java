package lk.epicgreen.erp.production.mapper;

import lk.epicgreen.erp.production.dto.request.BillOfMaterialsRequest;
import lk.epicgreen.erp.production.dto.response.BillOfMaterialsResponse;
import lk.epicgreen.erp.production.entity.BillOfMaterials;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

/**
 * Mapper for BillOfMaterials entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class BillOfMaterialsMapper {

    private final BomItemMapper bomItemMapper;

    public BillOfMaterialsMapper(BomItemMapper bomItemMapper) {
        this.bomItemMapper = bomItemMapper;
    }

    public BillOfMaterials toEntity(BillOfMaterialsRequest request) {
        if (request == null) {
            return null;
        }

        return BillOfMaterials.builder()
            .bomCode(request.getBomCode())
            .bomVersion(request.getBomVersion() != null ? request.getBomVersion() : "1.0")
            .outputQuantity(request.getOutputQuantity())
            .productionTimeMinutes(request.getProductionTimeMinutes())
            .laborCost(request.getLaborCost() != null ? request.getLaborCost() : BigDecimal.ZERO)
            .overheadCost(request.getOverheadCost() != null ? request.getOverheadCost() : BigDecimal.ZERO)
            .isActive(request.getIsActive() != null ? request.getIsActive() : true)
            .effectiveFrom(request.getEffectiveFrom())
            .effectiveTo(request.getEffectiveTo())
            .remarks(request.getRemarks())
            .build();
    }

    public void updateEntityFromRequest(BillOfMaterialsRequest request, BillOfMaterials bom) {
        if (request == null || bom == null) {
            return;
        }

        bom.setBomCode(request.getBomCode());
        bom.setBomVersion(request.getBomVersion());
        bom.setOutputQuantity(request.getOutputQuantity());
        bom.setProductionTimeMinutes(request.getProductionTimeMinutes());
        bom.setLaborCost(request.getLaborCost());
        bom.setOverheadCost(request.getOverheadCost());
        
        if (request.getIsActive() != null) {
            bom.setIsActive(request.getIsActive());
        }
        
        bom.setEffectiveFrom(request.getEffectiveFrom());
        bom.setEffectiveTo(request.getEffectiveTo());
        bom.setRemarks(request.getRemarks());
    }

    public BillOfMaterialsResponse toResponse(BillOfMaterials bom) {
        if (bom == null) {
            return null;
        }

        // Calculate total material cost from items
        BigDecimal totalMaterialCost = BigDecimal.ZERO;
        if (bom.getItems() != null && !bom.getItems().isEmpty()) {
            totalMaterialCost = bom.getItems().stream()
                .map(item -> {
                    if (item.getStandardCost() != null && item.getQuantityRequired() != null) {
                        return item.getStandardCost().multiply(item.getQuantityRequired());
                    }
                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        // Calculate total cost
        BigDecimal totalCost = totalMaterialCost
            .add(bom.getLaborCost() != null ? bom.getLaborCost() : BigDecimal.ZERO)
            .add(bom.getOverheadCost() != null ? bom.getOverheadCost() : BigDecimal.ZERO);

        return BillOfMaterialsResponse.builder()
            .id(bom.getId())
            .bomCode(bom.getBomCode())
            .finishedProductId(bom.getFinishedProduct() != null ? bom.getFinishedProduct().getId() : null)
            .finishedProductCode(bom.getFinishedProduct() != null ? bom.getFinishedProduct().getProductCode() : null)
            .finishedProductName(bom.getFinishedProduct() != null ? bom.getFinishedProduct().getProductName() : null)
            .bomVersion(bom.getBomVersion())
            .outputQuantity(bom.getOutputQuantity())
            .outputUomId(bom.getOutputUom() != null ? bom.getOutputUom().getId() : null)
            .outputUomCode(bom.getOutputUom() != null ? bom.getOutputUom().getUomCode() : null)
            .outputUomName(bom.getOutputUom() != null ? bom.getOutputUom().getUomName() : null)
            .productionTimeMinutes(bom.getProductionTimeMinutes())
            .laborCost(bom.getLaborCost())
            .overheadCost(bom.getOverheadCost())
            .totalMaterialCost(totalMaterialCost)
            .totalCost(totalCost)
            .isActive(bom.getIsActive())
            .effectiveFrom(bom.getEffectiveFrom())
            .effectiveTo(bom.getEffectiveTo())
            .remarks(bom.getRemarks())
            .createdAt(bom.getCreatedAt())
            .updatedAt(bom.getUpdatedAt())
            .items(bom.getItems() != null ? 
                bom.getItems().stream()
                    .map(bomItemMapper::toResponse)
                    .collect(Collectors.toList()) : null)
            .build();
    }
}
