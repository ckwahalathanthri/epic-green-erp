package lk.epicgreen.erp.sales.mapper;

import lk.epicgreen.erp.sales.dto.request.SalesOrderItemRequest;
import lk.epicgreen.erp.sales.dto.response.SalesOrderItemResponse;
import lk.epicgreen.erp.sales.entity.SalesOrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapper for SalesOrderItem entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class SalesOrderItemMapper {

    public SalesOrderItem toEntity(SalesOrderItemRequest request) {
        if (request == null) {
            return null;
        }

        return SalesOrderItem.builder()
            .batchNumber(request.getBatchNumber())
            .quantityOrdered(request.getQuantityOrdered())
            .quantityDelivered(request.getQuantityDelivered() != null ? request.getQuantityDelivered() : BigDecimal.ZERO)
            .unitPrice(request.getUnitPrice())
            .discountPercentage(request.getDiscountPercentage() != null ? request.getDiscountPercentage() : BigDecimal.ZERO)
            .discountAmount(request.getDiscountAmount() != null ? request.getDiscountAmount() : BigDecimal.ZERO)
            .taxAmount(request.getTaxAmount() != null ? request.getTaxAmount() : BigDecimal.ZERO)
            .lineTotal(request.getLineTotal())
            .remarks(request.getRemarks())
            .build();
    }

    public SalesOrderItemResponse toResponse(SalesOrderItem item) {
        if (item == null) {
            return null;
        }

        return SalesOrderItemResponse.builder()
            .id(item.getId())
            .orderId(item.getOrder() != null ? item.getOrder().getId() : null)
            .productId(item.getProduct() != null ? item.getProduct().getId() : null)
            .productCode(item.getProduct() != null ? item.getProduct().getProductCode() : null)
            .productName(item.getProduct() != null ? item.getProduct().getProductName() : null)
            .batchNumber(item.getBatchNumber())
            .quantityOrdered(item.getQuantityOrdered())
            .quantityDelivered(item.getQuantityDelivered())
            .quantityPending(item.getQuantityPending())
            .uomId(item.getUom() != null ? item.getUom().getId() : null)
            .uomCode(item.getUom() != null ? item.getUom().getUomCode() : null)
            .uomName(item.getUom() != null ? item.getUom().getUomName() : null)
            .unitPrice(item.getUnitPrice())
            .discountPercentage(item.getDiscountPercentage())
            .discountAmount(item.getDiscountAmount())
            .taxRateId(item.getTaxRate() != null ? item.getTaxRate().getId() : null)
            .taxRateName(item.getTaxRate() != null ? item.getTaxRate().getTaxName() : null)
            .taxAmount(item.getTaxAmount())
            .lineTotal(item.getLineTotal())
            .remarks(item.getRemarks())
            .build();
    }
}
