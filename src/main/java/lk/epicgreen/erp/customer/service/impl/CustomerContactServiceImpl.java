package lk.epicgreen.erp.customer.service.impl;

import lk.epicgreen.erp.customer.dto.request.CustomerContactRequest;
import lk.epicgreen.erp.customer.dto.response.CustomerContactResponse;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.entity.CustomerContact;
import lk.epicgreen.erp.customer.mapper.CustomerContactMapper;
import lk.epicgreen.erp.customer.repository.CustomerRepository;
import lk.epicgreen.erp.customer.repository.CustomerContactRepository;
import lk.epicgreen.erp.customer.service.CustomerContactService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of CustomerContactService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CustomerContactServiceImpl implements CustomerContactService {

    private final CustomerContactRepository customerContactRepository;
    private final CustomerRepository customerRepository;
    private final CustomerContactMapper customerContactMapper;

    @Override
    @Transactional
    public CustomerContactResponse createCustomerContact(CustomerContactRequest request) {
        log.info("Creating new customer contact for customer: {}", request.getCustomerId());

        // Verify customer exists
        Customer customer = findCustomerById(request.getCustomerId());

        // Create contact entity
        CustomerContact contact = customerContactMapper.toEntity(request);
        contact.setCustomer(customer);

        // If this is set as primary, unset any existing primary contacts
        if (Boolean.TRUE.equals(request.getIsPrimary())) {
            unsetPrimaryContacts(request.getCustomerId());
        }

        CustomerContact savedContact = customerContactRepository.save(contact);
        log.info("Customer contact created successfully: {}", savedContact.getId());

        return customerContactMapper.toResponse(savedContact);
    }

    @Override
    @Transactional
    public CustomerContactResponse updateCustomerContact(Long id, CustomerContactRequest request) {
        log.info("Updating customer contact: {}", id);

        CustomerContact contact = findCustomerContactById(id);

        // Update fields
        customerContactMapper.updateEntityFromRequest(request, contact);

        // If setting as primary, unset other primary contacts
        if (Boolean.TRUE.equals(request.getIsPrimary())) {
            unsetPrimaryContacts(contact.getCustomer().getId(), id);
        }

        CustomerContact updatedContact = customerContactRepository.save(contact);
        log.info("Customer contact updated successfully: {}", updatedContact.getId());

        return customerContactMapper.toResponse(updatedContact);
    }

    @Override
    @Transactional
    public void deleteCustomerContact(Long id) {
        log.info("Deleting customer contact: {}", id);

        CustomerContact contact = findCustomerContactById(id);
        customerContactRepository.delete(contact);

        log.info("Customer contact deleted successfully: {}", id);
    }

    @Override
    @Transactional
    public void setPrimaryContact(Long id) {
        log.info("Setting primary contact: {}", id);

        CustomerContact contact = findCustomerContactById(id);
        
        // Unset all primary contacts for this customer
        unsetPrimaryContacts(contact.getCustomer().getId(), id);
        
        // Set this contact as primary
        contact.setIsPrimary(true);
        customerContactRepository.save(contact);

        log.info("Primary contact set successfully: {}", id);
    }

    @Override
    public CustomerContactResponse getCustomerContactById(Long id) {
        CustomerContact contact = findCustomerContactById(id);
        return customerContactMapper.toResponse(contact);
    }

    @Override
    public List<CustomerContactResponse> getContactsByCustomer(Long customerId) {
        List<CustomerContact> contacts = customerContactRepository.findByCustomerId(customerId);
        return contacts.stream()
            .map(customerContactMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public CustomerContactResponse getPrimaryContactByCustomer(Long customerId) {
        CustomerContact contact = customerContactRepository.findByCustomerIdAndIsPrimaryTrue(customerId)
            .orElse(null);
        return contact != null ? customerContactMapper.toResponse(contact) : null;
    }

    @Override
    public List<CustomerContactResponse> getAllCustomerContacts() {
        List<CustomerContact> contacts = customerContactRepository.findAll();
        return contacts.stream()
            .map(customerContactMapper::toResponse)
            .collect(Collectors.toList());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private CustomerContact findCustomerContactById(Long id) {
        return customerContactRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer contact not found: " + id));
    }

    private Customer findCustomerById(Long id) {
        return customerRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
    }

    private void unsetPrimaryContacts(Long customerId) {
        unsetPrimaryContacts(customerId, null);
    }

    private void unsetPrimaryContacts(Long customerId, Long excludeContactId) {
        List<CustomerContact> primaryContacts = customerContactRepository.findByCustomerIdAndIsPrimaryTrue(customerId);
        
        for (CustomerContact contact : primaryContacts) {
            if (excludeContactId == null || !contact.getId().equals(excludeContactId)) {
                contact.setIsPrimary(false);
                customerContactRepository.save(contact);
            }
        }
    }
}
