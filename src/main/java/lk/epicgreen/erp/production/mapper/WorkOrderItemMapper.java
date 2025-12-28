package lk.epicgreen.erp.production.mapper;

import lk.epicgreen.erp.production.dto.request.WorkOrderItemRequest;
import lk.epicgreen.erp.production.dto.response.WorkOrderItemResponse;
import lk.epicgreen.erp.production.entity.WorkOrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapper for WorkOrderItem entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class WorkOrderItemMapper {

    public WorkOrderItem toEntity(WorkOrderItemRequest request) {
        if (request == null) {
            return null;
        }

        BigDecimal totalCost = null;
        if (request.getUnitCost() != null && request.getPlannedQuantity() != null) {
            totalCost = request.getUnitCost().multiply(request.getPlannedQuantity());
        }

        return WorkOrderItem.builder()
            .plannedQuantity(request.getPlannedQuantity())
            .consumedQuantity(request.getConsumedQuantity() != null ? request.getConsumedQuantity() : BigDecimal.ZERO)
            .unitCost(request.getUnitCost())
            .totalCost(totalCost)
            .status(request.getStatus() != null ? request.getStatus() : "PENDING")
            .build();
    }

    public WorkOrderItemResponse toResponse(WorkOrderItem item) {
        if (item == null) {
            return null;
        }

        BigDecimal totalCost = null;
        if (item.getUnitCost() != null && item.getPlannedQuantity() != null) {
            totalCost = item.getUnitCost().multiply(item.getPlannedQuantity());
        }

        return WorkOrderItemResponse.builder()
            .id(item.getId())
            .woId(item.getWorkOrder() != null ? item.getWorkOrder().getId() : null)
            .rawMaterialId(item.getRawMaterial() != null ? item.getRawMaterial().getId() : null)
            .rawMaterialCode(item.getRawMaterial() != null ? item.getRawMaterial().getProductCode() : null)
            .rawMaterialName(item.getRawMaterial() != null ? item.getRawMaterial().getProductName() : null)
            .plannedQuantity(item.getPlannedQuantity())
            .consumedQuantity(item.getConsumedQuantity())
            .uomId(item.getUom() != null ? item.getUom().getId() : null)
            .uomCode(item.getUom() != null ? item.getUom().getUomCode() : null)
            .uomName(item.getUom() != null ? item.getUom().getUomName() : null)
            .unitCost(item.getUnitCost())
            .totalCost(totalCost)
            .status(item.getStatus())
            .issuedFromWarehouseId(item.getIssuedFromWarehouse() != null ? item.getIssuedFromWarehouse().getId() : null)
            .issuedFromWarehouseCode(item.getIssuedFromWarehouse() != null ? item.getIssuedFromWarehouse().getWarehouseCode() : null)
            .issuedAt(item.getIssuedAt())
            .issuedBy(item.getIssuedBy())
            .build();
    }
}
