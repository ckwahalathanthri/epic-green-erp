package lk.epicgreen.erp.warehouse.mapper;

import lk.epicgreen.erp.warehouse.dto.request.StockMovementRequest;
import lk.epicgreen.erp.warehouse.dto.response.StockMovementResponse;
import lk.epicgreen.erp.warehouse.entity.StockMovement;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapper for StockMovement entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class StockMovementMapper {

    public StockMovement toEntity(StockMovementRequest request) {
        if (request == null) {
            return null;
        }

        return StockMovement.builder()
            .movementDate(request.getMovementDate())
            .movementType(request.getMovementType())
            .batchNumber(request.getBatchNumber())
            .quantity(request.getQuantity())
            .unitCost(request.getUnitCost())
            .referenceType(request.getReferenceType())
            .referenceId(request.getReferenceId())
            .referenceNumber(request.getReferenceNumber())
            .remarks(request.getRemarks())
            .build();
    }

    public StockMovementResponse toResponse(StockMovement movement) {
        if (movement == null) {
            return null;
        }

        BigDecimal totalCost = null;
        if (movement.getQuantity() != null && movement.getUnitCost() != null) {
            totalCost = movement.getQuantity().multiply(movement.getUnitCost());
        }

        return StockMovementResponse.builder()
            .id(movement.getId())
            .movementDate(movement.getMovementDate())
            .movementType(movement.getMovementType())
            .warehouseId(movement.getWarehouse() != null ? movement.getWarehouse().getId() : null)
            .warehouseCode(movement.getWarehouse() != null ? movement.getWarehouse().getWarehouseCode() : null)
            .warehouseName(movement.getWarehouse() != null ? movement.getWarehouse().getWarehouseName() : null)
            .productId(movement.getProduct() != null ? movement.getProduct().getId() : null)
            .productCode(movement.getProduct() != null ? movement.getProduct().getProductCode() : null)
            .productName(movement.getProduct() != null ? movement.getProduct().getProductName() : null)
            .fromLocationId(movement.getFromLocation() != null ? movement.getFromLocation().getId() : null)
            .fromLocationCode(movement.getFromLocation() != null ? movement.getFromLocation().getLocationCode() : null)
            .toLocationId(movement.getToLocation() != null ? movement.getToLocation().getId() : null)
            .toLocationCode(movement.getToLocation() != null ? movement.getToLocation().getLocationCode() : null)
            .batchNumber(movement.getBatchNumber())
            .quantity(movement.getQuantity())
            .uomId(movement.getUom() != null ? movement.getUom().getId() : null)
            .uomCode(movement.getUom() != null ? movement.getUom().getUomCode() : null)
            .uomName(movement.getUom() != null ? movement.getUom().getUomName() : null)
            .unitCost(movement.getUnitCost())
            .totalCost(totalCost)
            .referenceType(movement.getReferenceType())
            .referenceId(movement.getReferenceId())
            .referenceNumber(movement.getReferenceNumber())
            .remarks(movement.getRemarks())
            .createdAt(movement.getCreatedAt())
            .createdBy(movement.getCreatedBy())
            .build();
    }
}
