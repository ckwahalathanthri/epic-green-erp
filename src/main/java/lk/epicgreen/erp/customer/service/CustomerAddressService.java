package lk.epicgreen.erp.customer.service;

import lk.epicgreen.erp.customer.dto.request.CustomerAddressRequest;
import lk.epicgreen.erp.customer.dto.response.CustomerAddressResponse;

import java.util.List;

/**
 * Service interface for CustomerAddress entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface CustomerAddressService {

    CustomerAddressResponse createCustomerAddress(CustomerAddressRequest request);
    CustomerAddressResponse updateCustomerAddress(Long id, CustomerAddressRequest request);
    void deleteCustomerAddress(Long id);
    void setDefaultAddress(Long id);
    CustomerAddressResponse getCustomerAddressById(Long id);
    List<CustomerAddressResponse> getAddressesByCustomer(Long customerId);
    List<CustomerAddressResponse> getAddressesByCustomerAndType(Long customerId, String addressType);
    CustomerAddressResponse getDefaultAddressByCustomer(Long customerId);
    List<CustomerAddressResponse> getAllCustomerAddresses();
}
