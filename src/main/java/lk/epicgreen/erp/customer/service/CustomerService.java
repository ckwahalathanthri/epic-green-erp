package lk.epicgreen.erp.customer.service;

import lk.epicgreen.erp.customer.dto.request.CustomerRequest;
import lk.epicgreen.erp.customer.dto.response.CustomerResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for Customer entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface CustomerService {

    /**
     * Create new customer
     */
    CustomerResponse createCustomer(CustomerRequest request);

    /**
     * Update existing customer
     */
    CustomerResponse updateCustomer(Long id, CustomerRequest request);

    /**
     * Activate customer
     */
    void activateCustomer(Long id);

    /**
     * Deactivate customer
     */
    void deactivateCustomer(Long id);

    /**
     * Delete customer (soft delete)
     */
    void deleteCustomer(Long id);

    /**
     * Assign sales representative to customer
     */
    void assignSalesRepresentative(Long customerId, Long salesRepId);

    /**
     * Get customer by ID
     */
    CustomerResponse getCustomerById(Long id);

    /**
     * Get customer by code
     */
    CustomerResponse getCustomerByCode(String customerCode);

    /**
     * Get all customers with pagination
     */
    PageResponse<CustomerResponse> getAllCustomers(Pageable pageable);

    /**
     * Get all active customers
     */
    List<CustomerResponse> getAllActiveCustomers();

    /**
     * Get customers by type
     */
    PageResponse<CustomerResponse> getCustomersByType(String customerType, Pageable pageable);

    /**
     * Get wholesale customers
     */
    List<CustomerResponse> getWholesaleCustomers();

    /**
     * Get retail customers
     */
    List<CustomerResponse> getRetailCustomers();

    /**
     * Get distributor customers
     */
    List<CustomerResponse> getDistributorCustomers();

    /**
     * Get customers by sales representative
     */
    List<CustomerResponse> getCustomersBySalesRep(Long salesRepId);

    /**
     * Get customers by region
     */
    List<CustomerResponse> getCustomersByRegion(String region);

    /**
     * Get customers by route
     */
    List<CustomerResponse> getCustomersByRoute(String routeCode);

    /**
     * Search customers
     */
    PageResponse<CustomerResponse> searchCustomers(String keyword, Pageable pageable);

    /**
     * Get customers with outstanding balance
     */
    List<CustomerResponse> getCustomersWithOutstandingBalance();

    /**
     * Get customers exceeding credit limit
     */
    List<CustomerResponse> getCustomersExceedingCreditLimit();

    /**
     * Get customer current balance
     */
    BigDecimal getCustomerBalance(Long id);
}
