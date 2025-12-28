package lk.epicgreen.erp.customer.service;

import lk.epicgreen.erp.customer.dto.request.CustomerContactRequest;
import lk.epicgreen.erp.customer.dto.response.CustomerContactResponse;

import java.util.List;

/**
 * Service interface for CustomerContact entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface CustomerContactService {

    CustomerContactResponse createCustomerContact(CustomerContactRequest request);
    CustomerContactResponse updateCustomerContact(Long id, CustomerContactRequest request);
    void deleteCustomerContact(Long id);
    void setPrimaryContact(Long id);
    CustomerContactResponse getCustomerContactById(Long id);
    List<CustomerContactResponse> getContactsByCustomer(Long customerId);
    CustomerContactResponse getPrimaryContactByCustomer(Long customerId);
    List<CustomerContactResponse> getAllCustomerContacts();
}
