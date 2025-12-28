package lk.epicgreen.erp.customer.mapper;

import lk.epicgreen.erp.customer.dto.request.CustomerAddressRequest;
import lk.epicgreen.erp.customer.dto.response.CustomerAddressResponse;
import lk.epicgreen.erp.customer.entity.CustomerAddress;
import org.springframework.stereotype.Component;

/**
 * Mapper for CustomerAddress entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class CustomerAddressMapper {

    public CustomerAddress toEntity(CustomerAddressRequest request) {
        if (request == null) {
            return null;
        }

        return CustomerAddress.builder()
            .addressType(request.getAddressType())
            .addressName(request.getAddressName())
            .addressLine1(request.getAddressLine1())
            .addressLine2(request.getAddressLine2())
            .city(request.getCity())
            .state(request.getState())
            .country(request.getCountry() != null ? request.getCountry() : "Sri Lanka")
            .postalCode(request.getPostalCode())
            .contactPerson(request.getContactPerson())
            .contactPhone(request.getContactPhone())
            .isDefault(request.getIsDefault() != null ? request.getIsDefault() : false)
            .build();
    }

    public void updateEntityFromRequest(CustomerAddressRequest request, CustomerAddress address) {
        if (request == null || address == null) {
            return;
        }

        address.setAddressType(request.getAddressType());
        address.setAddressName(request.getAddressName());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setContactPerson(request.getContactPerson());
        address.setContactPhone(request.getContactPhone());
        
        if (request.getIsDefault() != null) {
            address.setIsDefault(request.getIsDefault());
        }
    }

    public CustomerAddressResponse toResponse(CustomerAddress address) {
        if (address == null) {
            return null;
        }

        return CustomerAddressResponse.builder()
            .id(address.getId())
            .customerId(address.getCustomer() != null ? address.getCustomer().getId() : null)
            .customerName(address.getCustomer() != null ? address.getCustomer().getCustomerName() : null)
            .addressType(address.getAddressType())
            .addressName(address.getAddressName())
            .addressLine1(address.getAddressLine1())
            .addressLine2(address.getAddressLine2())
            .city(address.getCity())
            .state(address.getState())
            .country(address.getCountry())
            .postalCode(address.getPostalCode())
            .contactPerson(address.getContactPerson())
            .contactPhone(address.getContactPhone())
            .isDefault(address.getIsDefault())
            .createdAt(address.getCreatedAt())
            .build();
    }
}
