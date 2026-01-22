package lk.epicgreen.erp.customer.service.impl;

import lk.epicgreen.erp.customer.dto.request.CustomerAddressRequest;
import lk.epicgreen.erp.customer.dto.response.CustomerAddressResponse;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.entity.CustomerAddress;
import lk.epicgreen.erp.customer.mapper.CustomerAddressMapper;
import lk.epicgreen.erp.customer.repository.CustomerRepository;
import lk.epicgreen.erp.customer.repository.CustomerAddressRepository;
import lk.epicgreen.erp.customer.service.CustomerAddressService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of CustomerAddressService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CustomerAddressServiceImpl implements CustomerAddressService {

    private final CustomerAddressRepository customerAddressRepository;
    private final CustomerRepository customerRepository;
    private final CustomerAddressMapper customerAddressMapper;

    @Override
    @Transactional
    public CustomerAddressResponse createCustomerAddress(CustomerAddressRequest request) {
        log.info("Creating new customer address for customer: {}", request.getCustomerId());

        // Verify customer exists
        Customer customer = findCustomerById(request.getCustomerId());

        // Create address entity
        CustomerAddress address = customerAddressMapper.toEntity(request);
        address.setCustomer(customer);

        // If this is set as default, unset any existing default addresses
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            unsetDefaultAddresses(request.getCustomerId());
        }

        CustomerAddress savedAddress = customerAddressRepository.save(address);
        log.info("Customer address created successfully: {}", savedAddress.getId());

        return customerAddressMapper.toResponse(savedAddress);
    }

    @Override
    @Transactional
    public CustomerAddressResponse updateCustomerAddress(Long id, CustomerAddressRequest request) {
        log.info("Updating customer address: {}", id);

        CustomerAddress address = findCustomerAddressById(id);

        // Update fields
        customerAddressMapper.updateEntityFromRequest(request, address);

        // If setting as default, unset other default addresses
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            unsetDefaultAddresses(address.getCustomer().getId(), id);
        }

        CustomerAddress updatedAddress = customerAddressRepository.save(address);
        log.info("Customer address updated successfully: {}", updatedAddress.getId());

        return customerAddressMapper.toResponse(updatedAddress);
    }

    @Override
    @Transactional
    public void deleteCustomerAddress(Long id) {
        log.info("Deleting customer address: {}", id);

        CustomerAddress address = findCustomerAddressById(id);
        customerAddressRepository.delete(address);

        log.info("Customer address deleted successfully: {}", id);
    }

    @Override
    @Transactional
    public void setDefaultAddress(Long id) {
        log.info("Setting default address: {}", id);

        CustomerAddress address = findCustomerAddressById(id);
        
        // Unset all default addresses for this customer
        unsetDefaultAddresses(address.getCustomer().getId(), id);
        
        // Set this address as default
        address.setIsDefault(true);
        customerAddressRepository.save(address);

        log.info("Default address set successfully: {}", id);
    }

    @Override
    public CustomerAddressResponse getCustomerAddressById(Long id) {
        CustomerAddress address = findCustomerAddressById(id);
        return customerAddressMapper.toResponse(address);
    }

    @Override
    public List<CustomerAddressResponse> getAddressesByCustomer(Long customerId) {
        List<CustomerAddress> addresses = customerAddressRepository.findByCustomerId(customerId);
        return addresses.stream()
            .map(customerAddressMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerAddressResponse> getAddressesByCustomerAndType(Long customerId, String addressType) {
        List<CustomerAddress> addresses = customerAddressRepository.findByCustomerIdAndAddressType(customerId, addressType);
        return addresses.stream()
            .map(customerAddressMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public CustomerAddressResponse getDefaultAddressByCustomer(Long customerId) {
        List<CustomerAddress> addresses = customerAddressRepository.findByCustomerIdAndIsDefaultTrue(customerId);
        if (addresses.isEmpty()) {
            return null;
        }
        return customerAddressMapper.toResponse(addresses.get(0));
    }

    @Override
    public List<CustomerAddressResponse> getAllCustomerAddresses() {
        List<CustomerAddress> addresses = customerAddressRepository.findAll();
        return addresses.stream()
            .map(customerAddressMapper::toResponse)
            .collect(Collectors.toList());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private CustomerAddress findCustomerAddressById(Long id) {
        return customerAddressRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer address not found: " + id));
    }

    private Customer findCustomerById(Long id) {
        return customerRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
    }

    private void unsetDefaultAddresses(Long customerId) {
        unsetDefaultAddresses(customerId, null);
    }

    private void unsetDefaultAddresses(Long customerId, Long excludeAddressId) {
        List<CustomerAddress> defaultAddresses = customerAddressRepository.findByCustomerIdAndIsDefaultTrue(customerId);
        
        for (CustomerAddress address : defaultAddresses) {
            if (excludeAddressId == null || !address.getId().equals(excludeAddressId)) {
                address.setIsDefault(false);
                customerAddressRepository.save(address);
            }
        }
    }
}
