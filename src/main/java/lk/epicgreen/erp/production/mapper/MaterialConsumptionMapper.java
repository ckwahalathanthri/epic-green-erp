package lk.epicgreen.erp.production.mapper;

import lk.epicgreen.erp.production.dto.request.MaterialConsumptionRequest;
import lk.epicgreen.erp.production.dto.response.MaterialConsumptionResponse;
import lk.epicgreen.erp.production.entity.MaterialConsumption;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapper for MaterialConsumption entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class MaterialConsumptionMapper {

    public MaterialConsumption toEntity(MaterialConsumptionRequest request) {
        if (request == null) {
            return null;
        }

        BigDecimal totalCost = null;
        if (request.getUnitCost() != null && request.getQuantityConsumed() != null) {
            totalCost = request.getUnitCost().multiply(request.getQuantityConsumed());
        }

        return MaterialConsumption.builder()
            .consumptionDate(request.getConsumptionDate())
            .batchNumber(request.getBatchNumber())
            .quantityConsumed(request.getQuantityConsumed())
            .unitCost(request.getUnitCost())
            .totalCost(totalCost)
            .remarks(request.getRemarks())
            .build();
    }

    public MaterialConsumptionResponse toResponse(MaterialConsumption consumption) {
        if (consumption == null) {
            return null;
        }

        BigDecimal totalCost = null;
        if (consumption.getUnitCost() != null && consumption.getQuantityConsumed() != null) {
            totalCost = consumption.getUnitCost().multiply(consumption.getQuantityConsumed());
        }

        return MaterialConsumptionResponse.builder()
            .id(consumption.getId())
            .woId(consumption.getWorkOrder() != null ? consumption.getWorkOrder().getId() : null)
            .woNumber(consumption.getWorkOrder() != null ? consumption.getWorkOrder().getWoNumber() : null)
            .woItemId(consumption.getWorkOrderItem() != null ? consumption.getWorkOrderItem().getId() : null)
            .rawMaterialId(consumption.getRawMaterial() != null ? consumption.getRawMaterial().getId() : null)
            .rawMaterialCode(consumption.getRawMaterial() != null ? consumption.getRawMaterial().getProductCode() : null)
            .rawMaterialName(consumption.getRawMaterial() != null ? consumption.getRawMaterial().getProductName() : null)
            .consumptionDate(consumption.getConsumptionDate())
            .batchNumber(consumption.getBatchNumber())
            .quantityConsumed(consumption.getQuantityConsumed())
            .uomId(consumption.getUom() != null ? consumption.getUom().getId() : null)
            .uomCode(consumption.getUom() != null ? consumption.getUom().getUomCode() : null)
            .uomName(consumption.getUom() != null ? consumption.getUom().getUomName() : null)
            .unitCost(consumption.getUnitCost())
            .totalCost(totalCost)
            .warehouseId(consumption.getWarehouse() != null ? consumption.getWarehouse().getId() : null)
            .warehouseCode(consumption.getWarehouse() != null ? consumption.getWarehouse().getWarehouseCode() : null)
            .warehouseName(consumption.getWarehouse() != null ? consumption.getWarehouse().getWarehouseName() : null)
            .consumedBy(consumption.getConsumedBy() != null ? consumption.getConsumedBy().getId() : null)
            .remarks(consumption.getRemarks())
            .createdAt(consumption.getCreatedAt())
            .build();
    }
}
