package lk.epicgreen.erp.customer.service.impl;

import lk.epicgreen.erp.customer.dto.request.CustomerRequest;
import lk.epicgreen.erp.customer.dto.response.CustomerResponse;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.mapper.CustomerMapper;
import lk.epicgreen.erp.customer.repository.CustomerRepository;
import lk.epicgreen.erp.customer.service.CustomerService;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.admin.repository.UserRepository;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of CustomerService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        log.info("Creating new customer: {}", request.getCustomerCode());

        // Validate unique constraint
        validateUniqueCustomerCode(request.getCustomerCode(), null);

        // Create customer entity
        Customer customer = customerMapper.toEntity(request);

        // Set sales representative if provided
        if (request.getAssignedSalesRepId() != null) {
            User salesRep = userRepository.findById(request.getAssignedSalesRepId())
                .orElseThrow(() -> new ResourceNotFoundException("Sales representative not found: " + request.getAssignedSalesRepId()));
            customer.setAssignedSalesRep(salesRep);
        }

        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer created successfully: {}", savedCustomer.getCustomerCode());

        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        log.info("Updating customer: {}", id);

        Customer customer = findCustomerById(id);

        // Validate unique constraint
        validateUniqueCustomerCode(request.getCustomerCode(), id);

        // Update fields
        customerMapper.updateEntityFromRequest(request, customer);

        // Update sales representative if provided
        if (request.getAssignedSalesRepId() != null) {
            User salesRep = userRepository.findById(request.getAssignedSalesRepId())
                .orElseThrow(() -> new ResourceNotFoundException("Sales representative not found: " + request.getAssignedSalesRepId()));
            customer.setAssignedSalesRep(salesRep);
        } else {
            customer.setAssignedSalesRep(null);
        }

        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Customer updated successfully: {}", updatedCustomer.getCustomerCode());

        return customerMapper.toResponse(updatedCustomer);
    }

    @Override
    @Transactional
    public void activateCustomer(Long id) {
        log.info("Activating customer: {}", id);

        Customer customer = findCustomerById(id);
        customer.setIsActive(true);
        customerRepository.save(customer);

        log.info("Customer activated successfully: {}", id);
    }

    @Override
    @Transactional
    public void deactivateCustomer(Long id) {
        log.info("Deactivating customer: {}", id);

        Customer customer = findCustomerById(id);
        customer.setIsActive(false);
        customerRepository.save(customer);

        log.info("Customer deactivated successfully: {}", id);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        log.info("Deleting customer (soft delete): {}", id);

        Customer customer = findCustomerById(id);
        customer.setDeletedAt(LocalDateTime.now());
        customer.setIsActive(false);
        customerRepository.save(customer);

        log.info("Customer deleted successfully: {}", id);
    }

    @Override
    @Transactional
    public void assignSalesRepresentative(Long customerId, Long salesRepId) {
        log.info("Assigning sales representative {} to customer {}", salesRepId, customerId);

        Customer customer = findCustomerById(customerId);
        User salesRep = userRepository.findById(salesRepId)
            .orElseThrow(() -> new ResourceNotFoundException("Sales representative not found: " + salesRepId));

        customer.setAssignedSalesRep(salesRep);
        customerRepository.save(customer);

        log.info("Sales representative assigned successfully");
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = findCustomerById(id);
        return customerMapper.toResponse(customer);
    }

    @Override
    public CustomerResponse getCustomerByCode(String customerCode) {
        Customer customer = customerRepository.findByCustomerCodeAndDeletedAtIsNull(customerCode)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerCode));
        return customerMapper.toResponse(customer);
    }

    @Override
    public PageResponse<CustomerResponse> getAllCustomers(Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findByDeletedAtIsNull(pageable);
        return createPageResponse(customerPage);
    }

    @Override
    public List<CustomerResponse> getAllActiveCustomers() {
        List<Customer> customers = customerRepository.findByIsActiveTrueAndDeletedAtIsNull();
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<CustomerResponse> getCustomersByType(String customerType, Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findByCustomerTypeAndDeletedAtIsNull(customerType, pageable);
        return createPageResponse(customerPage);
    }

    @Override
    public List<CustomerResponse> getWholesaleCustomers() {
        List<Customer> customers = customerRepository.findByCustomerTypeAndIsActiveTrueAndDeletedAtIsNull("WHOLESALE");
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> getRetailCustomers() {
        List<Customer> customers = customerRepository.findByCustomerTypeAndIsActiveTrueAndDeletedAtIsNull("RETAIL");
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> getDistributorCustomers() {
        List<Customer> customers = customerRepository.findByCustomerTypeAndIsActiveTrueAndDeletedAtIsNull("DISTRIBUTOR");
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> getCustomersBySalesRep(Long salesRepId) {
        List<Customer> customers = customerRepository.findByAssignedSalesRepIdAndDeletedAtIsNull(salesRepId);
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> getCustomersByRegion(String region) {
        List<Customer> customers = customerRepository.findByRegionAndDeletedAtIsNull(region);
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> getCustomersByRoute(String routeCode) {
        List<Customer> customers = customerRepository.findByRouteCodeAndDeletedAtIsNull(routeCode);
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<CustomerResponse> searchCustomers(String keyword, Pageable pageable) {
        Page<Customer> customerPage = customerRepository.searchCustomers(keyword, pageable);
        return createPageResponse(customerPage);
    }

    @Override
    public List<CustomerResponse> getCustomersWithOutstandingBalance() {
        List<Customer> customers = customerRepository.findCustomersWithOutstandingBalance();
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> getCustomersExceedingCreditLimit() {
        List<Customer> customers = customerRepository.findCustomersExceedingCreditLimit();
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getCustomerBalance(Long id) {
        Customer customer = findCustomerById(id);
        return customer.getCurrentBalance() != null ? customer.getCurrentBalance() : BigDecimal.ZERO;
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private Customer findCustomerById(Long id) {
        return customerRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
    }

    private void validateUniqueCustomerCode(String customerCode, Long excludeId) {
        if (excludeId == null) {
            if (customerRepository.existsByCustomerCode(customerCode)) {
                throw new DuplicateResourceException("Customer code already exists: " + customerCode);
            }
        } else {
            if (customerRepository.existsByCustomerCodeAndIdNot(customerCode, excludeId)) {
                throw new DuplicateResourceException("Customer code already exists: " + customerCode);
            }
        }
    }

    private PageResponse<CustomerResponse> createPageResponse(Page<Customer> customerPage) {
        List<CustomerResponse> content = customerPage.getContent().stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<CustomerResponse>builder()
            .content(content)
            .pageNumber(customerPage.getNumber())
            .pageSize(customerPage.getSize())
            .totalElements(customerPage.getTotalElements())
            .totalPages(customerPage.getTotalPages())
            .last(customerPage.isLast())
            .first(customerPage.isFirst())
            .empty(customerPage.isEmpty())
            .build();
    }
}
