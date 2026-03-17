package lk.epicgreen.erp.customer.mapper;

import lk.epicgreen.erp.customer.dto.request.CustomerRequest;
import lk.epicgreen.erp.customer.dto.response.CustomerCategoryDTO;
import lk.epicgreen.erp.customer.dto.response.CustomerContactDTO;
import lk.epicgreen.erp.customer.dto.response.CustomerResponse;
import lk.epicgreen.erp.customer.dto.response.CustomerTypeDTO;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.entity.CustomerCategory;
import lk.epicgreen.erp.customer.entity.CustomerContact;
import lk.epicgreen.erp.customer.entity.CustomerType;
import org.springframework.stereotype.Component;

/**
 * Mapper for Customer entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequest request) {
        if (request == null) {
            return null;
        }

        return Customer.builder()
            .customerCode(request.getCustomerCode())
            .customerName(request.getCustomerName())
            .customerType(request.getCustomerType())
            .contactPerson(request.getContactPerson())
            .email(request.getEmail())
            .phone(request.getPhone())
            .mobile(request.getMobile())
            .taxId(request.getTaxId())
            .paymentTerms(request.getPaymentTerms())
            .creditLimit(request.getCreditLimit())
            .creditDays(request.getCreditDays())
            .billingAddressLine1(request.getBillingAddressLine1())
            .billingAddressLine2(request.getBillingAddressLine2())
            .billingCity(request.getBillingCity())
            .billingState(request.getBillingState())
            .billingCountry(request.getBillingCountry() != null ? request.getBillingCountry() : "Sri Lanka")
            .billingPostalCode(request.getBillingPostalCode())
            .shippingAddressLine1(request.getShippingAddressLine1())
            .shippingAddressLine2(request.getShippingAddressLine2())
            .shippingCity(request.getShippingCity())
            .shippingState(request.getShippingState())
            .shippingCountry(request.getShippingCountry() != null ? request.getShippingCountry() : "Sri Lanka")
            .shippingPostalCode(request.getShippingPostalCode())
            .region(request.getRegion())
            .routeCode(request.getRouteCode())
            .isActive(request.getIsActive() != null ? request.getIsActive() : true)
            .build();
    }

    public void updateEntityFromRequest(CustomerRequest request, Customer customer) {
        if (request == null || customer == null) {
            return;
        }

        customer.setCustomerCode(request.getCustomerCode());
        customer.setCustomerName(request.getCustomerName());
        customer.setCustomerType(request.getCustomerType());
        customer.setContactPerson(request.getContactPerson());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setMobile(request.getMobile());
        customer.setTaxId(request.getTaxId());
        customer.setPaymentTerms(request.getPaymentTerms());
        customer.setCreditLimit(request.getCreditLimit());
        customer.setCreditDays(request.getCreditDays());
        customer.setBillingAddressLine1(request.getBillingAddressLine1());
        customer.setBillingAddressLine2(request.getBillingAddressLine2());
        customer.setBillingCity(request.getBillingCity());
        customer.setBillingState(request.getBillingState());
        customer.setBillingCountry(request.getBillingCountry());
        customer.setBillingPostalCode(request.getBillingPostalCode());
        customer.setShippingAddressLine1(request.getShippingAddressLine1());
        customer.setShippingAddressLine2(request.getShippingAddressLine2());
        customer.setShippingCity(request.getShippingCity());
        customer.setShippingState(request.getShippingState());
        customer.setShippingCountry(request.getShippingCountry());
        customer.setShippingPostalCode(request.getShippingPostalCode());
        customer.setRegion(request.getRegion());
        customer.setRouteCode(request.getRouteCode());
        
        if (request.getIsActive() != null) {
            customer.setIsActive(request.getIsActive());
        }
    }

    public CustomerCategory toEntity(CustomerCategoryDTO dto) {
        if (dto == null) return null;
        CustomerCategory category = new CustomerCategory();
        category.setId(dto.getId());
        category.setCategoryCode(dto.getCategoryCode());
        category.setCategoryName(dto.getCategoryName());
        category.setDescription(dto.getDescription());
        category.setIsActive(dto.getIsActive());
        return category;
    }

    public CustomerType toEntity(CustomerTypeDTO dto) {
        if (dto == null) return null;
        CustomerType type = new CustomerType();
        type.setId(dto.getId());
        type.setTypeCode(dto.getTypeCode());
        type.setTypeName(dto.getTypeName());
        type.setDescription(dto.getDescription());
        type.setIsActive(dto.getIsActive());
        return type;
    }

    public CustomerTypeDTO toDTO(CustomerType type) {
        if (type == null) return null;
        CustomerTypeDTO dto = new CustomerTypeDTO();
        dto.setId(type.getId());
        dto.setTypeCode(type.getTypeCode());
        dto.setTypeName(type.getTypeName());
        dto.setDescription(type.getDescription());
        dto.setIsActive(type.getIsActive());
        dto.setCreatedBy(type.getCreatedBy());
        dto.setUpdatedBy(type.getUpdatedBy());
        dto.setCreatedAt(type.getCreatedAt());
        dto.setUpdatedAt(type.getUpdatedAt());
        return dto;
    }

    public CustomerCategoryDTO toDTO(CustomerCategory category) {
        if (category == null) return null;
        CustomerCategoryDTO dto = new CustomerCategoryDTO();
        dto.setId(category.getId());
        dto.setCategoryCode(category.getCategoryCode());
        dto.setCategoryName(category.getCategoryName());
        dto.setDescription(category.getDescription());
        dto.setIsActive(category.getIsActive());
        dto.setCreatedBy(category.getCreatedBy());
        dto.setUpdatedBy(category.getUpdatedBy());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }

    public CustomerResponse toResponse(Customer customer) {
        if (customer == null) {
            return null;
        }

        return CustomerResponse.builder()
            .id(customer.getId())
            .customerCode(customer.getCustomerCode())
            .customerName(customer.getCustomerName())
            .customerType(customer.getCustomerType())
            .contactPerson(customer.getContactPerson())
            .email(customer.getEmail())
            .phone(customer.getPhone())
            .mobile(customer.getMobile())
            .taxId(customer.getTaxId())
            .paymentTerms(customer.getPaymentTerms())
            .creditLimit(customer.getCreditLimit())
            .creditDays(customer.getCreditDays())
            .currentBalance(customer.getCurrentBalance())
            .billingAddressLine1(customer.getBillingAddressLine1())
            .billingAddressLine2(customer.getBillingAddressLine2())
            .billingCity(customer.getBillingCity())
            .billingState(customer.getBillingState())
            .billingCountry(customer.getBillingCountry())
            .billingPostalCode(customer.getBillingPostalCode())
            .shippingAddressLine1(customer.getShippingAddressLine1())
            .shippingAddressLine2(customer.getShippingAddressLine2())
            .shippingCity(customer.getShippingCity())
            .shippingState(customer.getShippingState())
            .shippingCountry(customer.getShippingCountry())
            .shippingPostalCode(customer.getShippingPostalCode())
            .assignedSalesRepId(customer.getAssignedSalesRep() != null ? customer.getAssignedSalesRep().getId() : null)
            .assignedSalesRepName(customer.getAssignedSalesRep() != null ? 
                customer.getAssignedSalesRep().getFirstName() + " " + customer.getAssignedSalesRep().getLastName() : null)
            .region(customer.getRegion())
            .routeCode(customer.getRouteCode())
            .isActive(customer.getIsActive())
            .createdAt(customer.getCreatedAt())
            .updatedAt(customer.getUpdatedAt())
            .build();
    }
}
