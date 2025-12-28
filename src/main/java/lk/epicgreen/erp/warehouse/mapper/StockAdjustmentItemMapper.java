package lk.epicgreen.erp.warehouse.mapper;

import lk.epicgreen.erp.warehouse.dto.request.StockAdjustmentItemRequest;
import lk.epicgreen.erp.warehouse.dto.response.StockAdjustmentItemResponse;
import lk.epicgreen.erp.warehouse.entity.StockAdjustmentItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapper for StockAdjustmentItem entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class StockAdjustmentItemMapper {

    public StockAdjustmentItem toEntity(StockAdjustmentItemRequest request) {
        if (request == null) {
            return null;
        }

        BigDecimal totalValue = null;
        if (request.getQuantityAdjusted() != null && request.getUnitCost() != null) {
            totalValue = request.getQuantityAdjusted().multiply(request.getUnitCost());
        }

        return StockAdjustmentItem.builder()
            .batchNumber(request.getBatchNumber())
            .quantityAdjusted(request.getQuantityAdjusted())
            .unitCost(request.getUnitCost())
            .totalValue(totalValue)
            .remarks(request.getRemarks())
            .build();
    }

    public StockAdjustmentItemResponse toResponse(StockAdjustmentItem item) {
        if (item == null) {
            return null;
        }

        return StockAdjustmentItemResponse.builder()
            .id(item.getId())
            .adjustmentId(item.getAdjustment() != null ? item.getAdjustment().getId() : null)
            .productId(item.getProduct() != null ? item.getProduct().getId() : null)
            .productCode(item.getProduct() != null ? item.getProduct().getProductCode() : null)
            .productName(item.getProduct() != null ? item.getProduct().getProductName() : null)
            .batchNumber(item.getBatchNumber())
            .locationId(item.getLocation() != null ? item.getLocation().getId() : null)
            .locationCode(item.getLocation() != null ? item.getLocation().getLocationCode() : null)
            .quantityAdjusted(item.getQuantityAdjusted())
            .unitCost(item.getUnitCost())
            .totalValue(item.getTotalValue())
            .remarks(item.getRemarks())
            .build();
    }
}
