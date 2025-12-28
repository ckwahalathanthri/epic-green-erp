package lk.epicgreen.erp.supplier.mapper;

import lk.epicgreen.erp.supplier.dto.request.SupplierRequest;
import lk.epicgreen.erp.supplier.dto.response.SupplierResponse;
import lk.epicgreen.erp.supplier.entity.Supplier;
import org.springframework.stereotype.Component;

/**
 * Mapper for Supplier entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class SupplierMapper {

    public Supplier toEntity(SupplierRequest request) {
        if (request == null) {
            return null;
        }

        return Supplier.builder()
            .supplierCode(request.getSupplierCode())
            .supplierName(request.getSupplierName())
            .supplierType(request.getSupplierType())
            .contactPerson(request.getContactPerson())
            .email(request.getEmail())
            .phone(request.getPhone())
            .mobile(request.getMobile())
            .taxId(request.getTaxId())
            .paymentTerms(request.getPaymentTerms())
            .creditLimit(request.getCreditLimit())
            .creditDays(request.getCreditDays())
            .addressLine1(request.getAddressLine1())
            .addressLine2(request.getAddressLine2())
            .city(request.getCity())
            .state(request.getState())
            .country(request.getCountry() != null ? request.getCountry() : "Sri Lanka")
            .postalCode(request.getPostalCode())
            .bankName(request.getBankName())
            .bankAccountNumber(request.getBankAccountNumber())
            .bankBranch(request.getBankBranch())
            .rating(request.getRating())
            .isActive(request.getIsActive() != null ? request.getIsActive() : true)
            .build();
    }

    public void updateEntityFromRequest(SupplierRequest request, Supplier supplier) {
        if (request == null || supplier == null) {
            return;
        }

        supplier.setSupplierCode(request.getSupplierCode());
        supplier.setSupplierName(request.getSupplierName());
        supplier.setSupplierType(request.getSupplierType());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());
        supplier.setMobile(request.getMobile());
        supplier.setTaxId(request.getTaxId());
        supplier.setPaymentTerms(request.getPaymentTerms());
        supplier.setCreditLimit(request.getCreditLimit());
        supplier.setCreditDays(request.getCreditDays());
        supplier.setAddressLine1(request.getAddressLine1());
        supplier.setAddressLine2(request.getAddressLine2());
        supplier.setCity(request.getCity());
        supplier.setState(request.getState());
        supplier.setCountry(request.getCountry());
        supplier.setPostalCode(request.getPostalCode());
        supplier.setBankName(request.getBankName());
        supplier.setBankAccountNumber(request.getBankAccountNumber());
        supplier.setBankBranch(request.getBankBranch());
        supplier.setRating(request.getRating());
        
        if (request.getIsActive() != null) {
            supplier.setIsActive(request.getIsActive());
        }
    }

    public SupplierResponse toResponse(Supplier supplier) {
        if (supplier == null) {
            return null;
        }

        return SupplierResponse.builder()
            .id(supplier.getId())
            .supplierCode(supplier.getSupplierCode())
            .supplierName(supplier.getSupplierName())
            .supplierType(supplier.getSupplierType())
            .contactPerson(supplier.getContactPerson())
            .email(supplier.getEmail())
            .phone(supplier.getPhone())
            .mobile(supplier.getMobile())
            .taxId(supplier.getTaxId())
            .paymentTerms(supplier.getPaymentTerms())
            .creditLimit(supplier.getCreditLimit())
            .creditDays(supplier.getCreditDays())
            .addressLine1(supplier.getAddressLine1())
            .addressLine2(supplier.getAddressLine2())
            .city(supplier.getCity())
            .state(supplier.getState())
            .country(supplier.getCountry())
            .postalCode(supplier.getPostalCode())
            .bankName(supplier.getBankName())
            .bankAccountNumber(supplier.getBankAccountNumber())
            .bankBranch(supplier.getBankBranch())
            .rating(supplier.getRating())
            .isActive(supplier.getIsActive())
            .currentBalance(supplier.getCurrentBalance())
            .createdAt(supplier.getCreatedAt())
            .updatedAt(supplier.getUpdatedAt())
            .build();
    }
}
