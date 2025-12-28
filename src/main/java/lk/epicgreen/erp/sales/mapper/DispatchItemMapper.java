package lk.epicgreen.erp.sales.mapper;

import lk.epicgreen.erp.sales.dto.request.DispatchItemRequest;
import lk.epicgreen.erp.sales.dto.response.DispatchItemResponse;
import lk.epicgreen.erp.sales.entity.DispatchItem;
import org.springframework.stereotype.Component;

/**
 * Mapper for DispatchItem entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class DispatchItemMapper {

    public DispatchItem toEntity(DispatchItemRequest request) {
        if (request == null) {
            return null;
        }

        return DispatchItem.builder()
            .batchNumber(request.getBatchNumber())
            .quantityDispatched(request.getQuantityDispatched())
            .remarks(request.getRemarks())
            .build();
    }

    public DispatchItemResponse toResponse(DispatchItem item) {
        if (item == null) {
            return null;
        }

        return DispatchItemResponse.builder()
            .id(item.getId())
            .dispatchId(item.getDispatchNote() != null ? item.getDispatchNote().getId() : null)
            .orderItemId(item.getOrderItem() != null ? item.getOrderItem().getId() : null)
            .productId(item.getProduct() != null ? item.getProduct().getId() : null)
            .productCode(item.getProduct() != null ? item.getProduct().getProductCode() : null)
            .productName(item.getProduct() != null ? item.getProduct().getProductName() : null)
            .batchNumber(item.getBatchNumber())
            .quantityDispatched(item.getQuantityDispatched())
            .uomId(item.getUom() != null ? item.getUom().getId() : null)
            .uomCode(item.getUom() != null ? item.getUom().getUomCode() : null)
            .uomName(item.getUom() != null ? item.getUom().getUomName() : null)
            .locationId(item.getLocation() != null ? item.getLocation().getId() : null)
            .locationCode(item.getLocation() != null ? item.getLocation().getLocationCode() : null)
            .remarks(item.getRemarks())
            .build();
    }
}
