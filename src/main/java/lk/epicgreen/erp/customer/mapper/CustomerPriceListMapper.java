package lk.epicgreen.erp.customer.mapper;

import lk.epicgreen.erp.customer.dto.request.CustomerPriceListRequest;
import lk.epicgreen.erp.customer.dto.response.CustomerPriceListResponse;
import lk.epicgreen.erp.customer.entity.CustomerPriceList;
import org.springframework.stereotype.Component;

/**
 * Mapper for CustomerPriceList entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class CustomerPriceListMapper {

    public CustomerPriceList toEntity(CustomerPriceListRequest request) {
        if (request == null) {
            return null;
        }

        return CustomerPriceList.builder()
            .specialPrice(request.getSpecialPrice())
            .discountPercentage(request.getDiscountPercentage())
            .minQuantity(request.getMinQuantity())
            .validFrom(request.getValidFrom())
            .validTo(request.getValidTo())
            .isActive(request.getIsActive() != null ? request.getIsActive() : true)
            .build();
    }

    public void updateEntityFromRequest(CustomerPriceListRequest request, CustomerPriceList priceList) {
        if (request == null || priceList == null) {
            return;
        }

        priceList.setSpecialPrice(request.getSpecialPrice());
        priceList.setDiscountPercentage(request.getDiscountPercentage());
        priceList.setMinQuantity(request.getMinQuantity());
        priceList.setValidFrom(request.getValidFrom());
        priceList.setValidTo(request.getValidTo());
        
        if (request.getIsActive() != null) {
            priceList.setIsActive(request.getIsActive());
        }
    }

    public CustomerPriceListResponse toResponse(CustomerPriceList priceList) {
        if (priceList == null) {
            return null;
        }

        return CustomerPriceListResponse.builder()
            .id(priceList.getId())
            .customerId(priceList.getCustomer() != null ? priceList.getCustomer().getId() : null)
            .customerCode(priceList.getCustomer() != null ? priceList.getCustomer().getCustomerCode() : null)
            .customerName(priceList.getCustomer() != null ? priceList.getCustomer().getCustomerName() : null)
            .productId(priceList.getProduct() != null ? priceList.getProduct().getId() : null)
            .productCode(priceList.getProduct() != null ? priceList.getProduct().getProductCode() : null)
            .productName(priceList.getProduct() != null ? priceList.getProduct().getProductName() : null)
            .specialPrice(priceList.getSpecialPrice())
            .discountPercentage(priceList.getDiscountPercentage())
            .minQuantity(priceList.getMinQuantity())
            .validFrom(priceList.getValidFrom())
            .validTo(priceList.getValidTo())
            .isActive(priceList.getIsActive())
            .createdAt(priceList.getCreatedAt())
            .build();
    }
}
