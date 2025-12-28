package lk.epicgreen.erp.supplier.mapper;

import lk.epicgreen.erp.supplier.dto.request.SupplierContactRequest;
import lk.epicgreen.erp.supplier.dto.response.SupplierContactResponse;
import lk.epicgreen.erp.supplier.entity.SupplierContact;
import org.springframework.stereotype.Component;

/**
 * Mapper for SupplierContact entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class SupplierContactMapper {

    public SupplierContact toEntity(SupplierContactRequest request) {
        if (request == null) {
            return null;
        }

        return SupplierContact.builder()
            .contactName(request.getContactName())
            .designation(request.getDesignation())
            .email(request.getEmail())
            .phone(request.getPhone())
            .mobile(request.getMobile())
            .isPrimary(request.getIsPrimary() != null ? request.getIsPrimary() : false)
            .build();
    }

    public void updateEntityFromRequest(SupplierContactRequest request, SupplierContact contact) {
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

    public SupplierContactResponse toResponse(SupplierContact contact) {
        if (contact == null) {
            return null;
        }

        return SupplierContactResponse.builder()
            .id(contact.getId())
            .supplierId(contact.getSupplier() != null ? contact.getSupplier().getId() : null)
            .supplierName(contact.getSupplier() != null ? contact.getSupplier().getSupplierName() : null)
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
