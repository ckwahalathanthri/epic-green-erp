package lk.epicgreen.erp.customer.service;

import lk.epicgreen.erp.customer.dto.request.CustomerLedgerRequest;
import lk.epicgreen.erp.customer.dto.request.CustomerRequest;
import lk.epicgreen.erp.customer.dto.response.CustomerLedgerResponse;
import lk.epicgreen.erp.customer.dto.response.CustomerResponse;
import lk.epicgreen.erp.common.dto.PageResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    /*
    Get customer by email
     */
    CustomerResponse getCustomerByEmail(String email);

    /**
     * Get customer by code
     */
    CustomerResponse getCustomerByCode(String customerCode);

    /*
    * Get customer by phone
     */
    CustomerResponse getCustomerByPhone(String phone);

    /**
     * Get all customers with pagination
     */
    PageResponse<CustomerResponse> getAllCustomers(Pageable pageable);

    /**
     * Get all customers
     */
    List<CustomerResponse> getAllCustomers();

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
     * Get customers by sales representative (list)
     */
    //Page<CustomerResponse> getCustomersBySalesRepList(Long salesRepId, Pageable pageable);

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

    /**
     * Suspend customer
     */
    CustomerResponse suspendCustomer(Long id, String suspensionReason);

    /**
     * Verify customer
     */
    CustomerResponse verifyCustomer(Long id, Long verifiedByUserId);

    /**
     * Blacklist customer
     */
    CustomerResponse blacklistCustomer(Long id, String blacklistReason);  
    
    /**
     * Remove customer from blacklist
     */
    CustomerResponse removeFromBlacklist(Long id);

    /**
     * Enable credit facility for customer
     */
    CustomerResponse enableCreditFacility(Long id, Double creditLimit, String paymentTerms);

    /**
     * Update credit limit for customer
     */
    CustomerResponse updateCreditLimit(Long id, Double newCreditLimit);

    /**
     * Disable credit facility for customer
     */
    CustomerResponse disableCreditFacility(Long id);

    /**
     * Update credit status for customer
     */
    CustomerResponse updateCreditStatus(Long id);

    /**
     * Update customer outstanding balance
     */
    void updateCustomerOutstanding(Long id);

    /**
     * Get active customers
     */
    List<CustomerResponse> getActiveCustomers();

    /**
     * Get inactive customers
     */
    List<CustomerResponse> getInactiveCustomers();

    /**
     * Get pending customers
     */
    List<CustomerResponse> getPendingCustomers();

    /**
     * Get suspended customers
     */
    List<CustomerResponse> getSuspendedCustomers();

    /**
     * Get blacklisted customers
     */
    List<CustomerResponse> getBlacklistedCustomers();

    /**
     * Get customers with credit facility
     */
    List<CustomerResponse> getCustomersWithCreditFacility();

    /**
     * Get customers who have exceeded their credit limit
     */
    List<CustomerResponse> getCustomersWithCreditExceeded();

    /**
     * Get customers who need credit warning
     */    
    List<CustomerResponse> getCustomersWithCreditWarning();

    /**
     * Get customers with overdue balance
     */
    List<CustomerResponse> getCustomersWithOverdueBalance();

    /**
     * Get unverified customers
     */
    List<CustomerResponse> getUnverifiedCustomers();

    /**
     * Get customers without recent orders
     */
    List<CustomerResponse> getCustomersWithoutRecentOrders(int days);

    /**
     * Get customers requiring action
     */
    List<CustomerResponse> getCustomersRequiringAction();

    /**
     * Get top customers by sales volume
     */
    List<CustomerResponse> getTopCustomers(int limit);

    /**
     * Get recent customers
     */
    List<CustomerResponse> getRecentCustomers(int limit);

    /**
     * Check if email is available
     */
    boolean isEmailAvailable(String email);

    /**
     * Check if phone is available
     */
    boolean isPhoneAvailable(String phone);

    /**
     * Check if customer code is available
     */
    boolean isCustomerCodeAvailable(String customerCode);

    /**
     * Check if customer can be activated
     */
    boolean canActivateCustomer(Long id);

    /**
     * Check if customer can be suspended
     */
    boolean canSuspendCustomer(Long id);

    /**
     * Check if credit facility can be enabled for customer
     */
    boolean canEnableCreditFacility(Long id);

    /*
    calculate customer metrics
     */
    void calculateCustomerMetrics(Long id);

    /**
     * Update total sales amount for customer
     */
    void updateTotalSalesAmount(Long id,Double amount);


    /**
     * Update average order value for customer
     */
    void updateAverageOrderValue(Long id);

    /**
     * Calculate available credit for customer
     */
    void calculateAvailableCredit(Long id);

    /**
     * Create bulk customers
     */
    List<CustomerResponse> createBulkCustomers(List<CustomerRequest> customerRequests);

    /**
     * Activate bulk customers
     */
    int activateBulkCustomers(List<Long> customerIds);

    /**
     * Deactivate bulk customers
     */
    int deactivateBulkCustomers(List<Long> customerIds);

    /**
     * Delete bulk customers
     */
    int deleteBulkCustomers(List<Long> customerIds);

    /**
     * Get customer statistics
     */
    Map<String, Object> getCustomerStatistics();

    /**
     * Get customer type distribution
     */
    List<Map<String, Object>> getCustomerTypeDistribution();

    /**
     * Get customer status distribution
     */
    List<Map<String, Object>> getCustomerStatusDistribution();

    /**
     * Get customer credit status distribution
     */
    List<Map<String, Object>> getCreditStatusDistribution();

    /**
     * Get payment terms distribution
     */
    List<Map<String, Object>> getPaymentTermsDistribution();

    /**
     * Get route distribution
     */
    List<Map<String, Object>> getRouteDistribution();

    /**
     * Get city distribution
     */
    List<Map<String, Object>> getCityDistribution();

    /**
     * Get province distribution
     */
    List<Map<String, Object>> getProvinceDistribution();

    /**
     * Get monthly customer registration count for the past year
     */
    List<Map<String, Object>> getMonthlyRegistrationCount();

    /**
     * Get customers by sales representative with total sales and outstanding balances
     */
    List<Map<String, Object>> getCustomersBySalesRepWithTotals();

    /**
     * Get total customer sales amount
     */
    Double getTotalCustomerSales();

    /**
     * Get total outstanding balance across all customers
     */
    Double getTotalOutstandingBalance();

    /**
     * Get total overdue amount across all customers
     */
    Double getTotalOverdueAmount();

    /**
     * Get total credit limit across all customers
     */
    Double getTotalCreditLimit();

    /**
     * Get total available credit across all customers
     */
    Double getTotalAvailableCredit();

    /**
     * Get average order value across all customers
     */
    Double getAverageOrderValue();

    /**
     * Get credit utilization rate across all customers
     */
    Double getCreditUtilizationRate();

    /**
     * Get dashboard statistics
     */
    Map<String, Object> getDashboardStatistics();

    /**
     * Reverse a customer ledger entry
     */
    CustomerLedgerResponse reverseLedgerEntry(Long id, String reversalReason);

    /**
     * Reconcile a customer ledger entry
     */
    CustomerLedgerResponse createLedgerEntry(CustomerLedgerRequest request);

    /**
     * Get customer ledger entries with pagination
     */
    void reconcileLedgerEntry(Long id);

    /**
     * Get customer ledger entries by customer ID
     */
    Page<CustomerLedgerResponse> getCustomerLedgerEntries(Long customerId, Pageable pageable);

    /**
     * Get customer ledger entries list by customer ID
     */
    List<CustomerLedgerResponse> getCustomerLedgerEntries(Long customerId);

    /**
     * Get customer ledger statement within date range
     */
    List<CustomerLedgerResponse> getCustomerLedgerStatement(Long customerId, LocalDate startDate, LocalDate endDate);

    /**
     * Get customer opening balance as of a specific date
     */
    Double getCustomerOpeningBalance(Long customerId, LocalDate startDate);

    void updateLastOrderDate(Long id, LocalDate orderDate);
}