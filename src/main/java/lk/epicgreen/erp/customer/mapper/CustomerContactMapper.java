package lk.epicgreen.erp.customer.mapper;

import lk.epicgreen.erp.customer.dto.request.CustomerContactRequest;
import lk.epicgreen.erp.customer.dto.response.CustomerContactResponse;
import lk.epicgreen.erp.customer.entity.CustomerContact;
import org.springframework.stereotype.Component;

/**
 * Mapper for CustomerContact entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class CustomerContactMapper {

    public CustomerContact toEntity(CustomerContactRequest request) {
        if (request == null) {
            return null;
        }

        return CustomerContact.builder()
            .contactName(request.getContactName())
            .designation(request.getDesignation())
            .email(request.getEmail())
            .phone(request.getPhone())
            .mobile(request.getMobile())
            .isPrimary(request.getIsPrimary() != null ? request.getIsPrimary() : false)
            .build();
    }

    public void updateEntityFromRequest(CustomerContactRequest request, CustomerContact contact) {
        if (request == null || contact == null) {
            return;
        }

        contact.setContactName(request.getContactName());
        contact.setDesignation(request.getDesignation());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setMobile(request.getMobile());
        
        if (request.getIsPrimary() != null) {
            contact.setIsPrimary(request.getIsPrimary());
        }
    }

    public CustomerContactResponse toResponse(CustomerContact contact) {
        if (contact == null) {
            return null;
        }

        return CustomerContactResponse.builder()
            .id(contact.getId())
            .customerId(contact.getCustomer() != null ? contact.getCustomer().getId() : null)
            .customerName(contact.getCustomer() != null ? contact.getCustomer().getCustomerName() : null)
            .contactName(contact.getContactName())
            .designation(contact.getDesignation())
            .email(contact.getEmail())
            .phone(contact.getPhone())
            .mobile(contact.getMobile())
            .isPrimary(contact.getIsPrimary())
            .createdAt(contact.getCreatedAt())
            .build();
    }
}
