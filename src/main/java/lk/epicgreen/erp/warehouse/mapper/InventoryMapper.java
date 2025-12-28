package lk.epicgreen.erp.warehouse.mapper;

import lk.epicgreen.erp.warehouse.dto.request.InventoryRequest;
import lk.epicgreen.erp.warehouse.dto.response.InventoryResponse;
import lk.epicgreen.erp.warehouse.entity.Inventory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapper for Inventory entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class InventoryMapper {

    public Inventory toEntity(InventoryRequest request) {
        if (request == null) {
            return null;
        }

        return Inventory.builder()
            .batchNumber(request.getBatchNumber())
            .manufacturingDate(request.getManufacturingDate())
            .expiryDate(request.getExpiryDate())
            .quantityAvailable(request.getQuantityAvailable())
            .quantityReserved(request.getQuantityReserved() != null ? request.getQuantityReserved() : BigDecimal.ZERO)
            .quantityOrdered(request.getQuantityOrdered() != null ? request.getQuantityOrdered() : BigDecimal.ZERO)
            .unitCost(request.getUnitCost())
            .lastStockDate(request.getLastStockDate())
            .build();
    }

    public void updateEntityFromRequest(InventoryRequest request, Inventory inventory) {
        if (request == null || inventory == null) {
            return;
        }

        inventory.setBatchNumber(request.getBatchNumber());
        inventory.setManufacturingDate(request.getManufacturingDate());
        inventory.setExpiryDate(request.getExpiryDate());
        inventory.setQuantityAvailable(request.getQuantityAvailable());
        inventory.setQuantityReserved(request.getQuantityReserved());
        inventory.setQuantityOrdered(request.getQuantityOrdered());
        inventory.setUnitCost(request.getUnitCost());
        inventory.setLastStockDate(request.getLastStockDate());
    }

    public InventoryResponse toResponse(Inventory inventory) {
        if (inventory == null) {
            return null;
        }

        BigDecimal totalValue = null;
        if (inventory.getQuantityAvailable() != null && inventory.getUnitCost() != null) {
            totalValue = inventory.getQuantityAvailable().multiply(inventory.getUnitCost());
        }

        return InventoryResponse.builder()
            .id(inventory.getId())
            .warehouseId(inventory.getWarehouse() != null ? inventory.getWarehouse().getId() : null)
            .warehouseCode(inventory.getWarehouse() != null ? inventory.getWarehouse().getWarehouseCode() : null)
            .warehouseName(inventory.getWarehouse() != null ? inventory.getWarehouse().getWarehouseName() : null)
            .productId(inventory.getProduct() != null ? inventory.getProduct().getId() : null)
            .productCode(inventory.getProduct() != null ? inventory.getProduct().getProductCode() : null)
            .productName(inventory.getProduct() != null ? inventory.getProduct().getProductName() : null)
            .locationId(inventory.getLocation() != null ? inventory.getLocation().getId() : null)
            .locationCode(inventory.getLocation() != null ? inventory.getLocation().getLocationCode() : null)
            .batchNumber(inventory.getBatchNumber())
            .manufacturingDate(inventory.getManufacturingDate())
            .expiryDate(inventory.getExpiryDate())
            .quantityAvailable(inventory.getQuantityAvailable())
            .quantityReserved(inventory.getQuantityReserved())
            .quantityOrdered(inventory.getQuantityOrdered())
            .unitCost(inventory.getUnitCost())
            .totalValue(totalValue)
            .lastStockDate(inventory.getLastStockDate())
            .createdAt(inventory.getCreatedAt())
            .updatedAt(inventory.getUpdatedAt())
            .build();
    }
}
