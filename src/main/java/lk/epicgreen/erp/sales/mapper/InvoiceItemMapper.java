package lk.epicgreen.erp.sales.mapper;

import lk.epicgreen.erp.sales.dto.request.InvoiceItemRequest;
import lk.epicgreen.erp.sales.dto.response.InvoiceItemResponse;
import lk.epicgreen.erp.sales.entity.InvoiceItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapper for InvoiceItem entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class InvoiceItemMapper {

    public InvoiceItem toEntity(InvoiceItemRequest request) {
        if (request == null) {
            return null;
        }

        return InvoiceItem.builder()
            .batchNumber(request.getBatchNumber())
            .quantity(request.getQuantity())
            .unitPrice(request.getUnitPrice())
            .discountPercentage(request.getDiscountPercentage() != null ? request.getDiscountPercentage() : BigDecimal.ZERO)
            .discountAmount(request.getDiscountAmount() != null ? request.getDiscountAmount() : BigDecimal.ZERO)
            .taxAmount(request.getTaxAmount() != null ? request.getTaxAmount() : BigDecimal.ZERO)
            .lineTotal(request.getLineTotal())
            .build();
    }

    public InvoiceItemResponse toResponse(InvoiceItem item) {
        if (item == null) {
            return null;
        }

        return InvoiceItemResponse.builder()
            .id(item.getId())
            .invoiceId(item.getInvoice() != null ? item.getInvoice().getId() : null)
            .orderItemId(item.getOrderItem() != null ? item.getOrderItem().getId() : null)
            .productId(item.getProduct() != null ? item.getProduct().getId() : null)
            .productCode(item.getProduct() != null ? item.getProduct().getProductCode() : null)
            .productName(item.getProduct() != null ? item.getProduct().getProductName() : null)
            .batchNumber(item.getBatchNumber())
            .quantity(item.getQuantity())
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
            .build();
    }
}
