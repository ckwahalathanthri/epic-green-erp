package lk.epicgreen.erp.customer.service;

import lk.epicgreen.erp.customer.dto.CustomerRequest;
import lk.epicgreen.erp.customer.dto.CustomerLedgerRequest;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.entity.CustomerLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Customer Service Interface
 * Service for customer operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface CustomerService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    Customer createCustomer(CustomerRequest request);
    Customer updateCustomer(Long id, CustomerRequest request);
    void deleteCustomer(Long id);
    Customer getCustomerById(Long id);
    Customer getCustomerByCode(String customerCode);
    Customer getCustomerByEmail(String email);
    Customer getCustomerByPhone(String phone);
    List<Customer> getAllCustomers();
    Page<Customer> getAllCustomers(Pageable pageable);
    Page<Customer> searchCustomers(String keyword, Pageable pageable);
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    Customer activateCustomer(Long id);
    Customer deactivateCustomer(Long id);
    Customer suspendCustomer(Long id, String suspensionReason);
    Customer verifyCustomer(Long id, Long verifiedByUserId);
    Customer blacklistCustomer(Long id, String blacklistReason);
    Customer removeFromBlacklist(Long id);
    
    // ===================================================================
    // CREDIT OPERATIONS
    // ===================================================================
    
    Customer enableCreditFacility(Long id, Double creditLimit, String paymentTerms);
    Customer updateCreditLimit(Long id, Double newCreditLimit);
    Customer disableCreditFacility(Long id);
    Customer updateCreditStatus(Long id);
    void updateCustomerOutstanding(Long customerId);
    
    // ===================================================================
    // LEDGER OPERATIONS
    // ===================================================================
    
    CustomerLedger createLedgerEntry(CustomerLedgerRequest request);
    CustomerLedger reverseLedgerEntry(Long ledgerEntryId, String reversalReason);
    void reconcileLedgerEntry(Long ledgerEntryId);
    List<CustomerLedger> getCustomerLedgerEntries(Long customerId);
    Page<CustomerLedger> getCustomerLedgerEntries(Long customerId, Pageable pageable);
    List<CustomerLedger> getCustomerLedgerStatement(Long customerId, LocalDate startDate, LocalDate endDate);
    Double getCustomerBalance(Long customerId);
    Double getCustomerOpeningBalance(Long customerId, LocalDate startDate);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    List<Customer> getActiveCustomers();
    List<Customer> getInactiveCustomers();
    List<Customer> getPendingCustomers();
    List<Customer> getSuspendedCustomers();
    List<Customer> getBlacklistedCustomers();
    List<Customer> getCustomersWithCreditFacility();
    List<Customer> getCustomersWithCreditExceeded();
    List<Customer> getCustomersWithCreditWarning();
    List<Customer> getCustomersWithOutstandingBalance();
    List<Customer> getCustomersWithOverdueBalance();
    List<Customer> getUnverifiedCustomers();
    List<Customer> getCustomersWithoutRecentOrders(int days);
    List<Customer> getCustomersRequiringAction();
    List<Customer> getCustomersByType(String customerType);
    List<Customer> getCustomersByRoute(String route);
    List<Customer> getCustomersBySalesRep(Long salesRepId);
    Page<Customer> getCustomersBySalesRep(Long salesRepId, Pageable pageable);
    List<Customer> getTopCustomers(int limit);
    List<Customer> getRecentCustomers(int limit);
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    boolean validateCustomer(Customer customer);
    boolean canActivateCustomer(Long customerId);
    boolean canSuspendCustomer(Long customerId);
    boolean canEnableCreditFacility(Long customerId);
    boolean isEmailAvailable(String email);
    boolean isPhoneAvailable(String phone);
    boolean isCustomerCodeAvailable(String customerCode);
    
    // ===================================================================
    // CALCULATIONS
    // ===================================================================
    
    void calculateCustomerMetrics(Long customerId);
    void updateTotalSalesAmount(Long customerId, Double amount);
    void updateLastOrderDate(Long customerId, LocalDate orderDate);
    void updateAverageOrderValue(Long customerId);
    void calculateAvailableCredit(Long customerId);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<Customer> createBulkCustomers(List<CustomerRequest> requests);
    int activateBulkCustomers(List<Long> customerIds);
    int deactivateBulkCustomers(List<Long> customerIds);
    int deleteBulkCustomers(List<Long> customerIds);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    Map<String, Object> getCustomerStatistics();
    List<Map<String, Object>> getCustomerTypeDistribution();
    List<Map<String, Object>> getStatusDistribution();
    List<Map<String, Object>> getCreditStatusDistribution();
    List<Map<String, Object>> getPaymentTermsDistribution();
    List<Map<String, Object>> getRouteDistribution();
    List<Map<String, Object>> getCityDistribution();
    List<Map<String, Object>> getProvinceDistribution();
    List<Map<String, Object>> getMonthlyRegistrationCount(LocalDate startDate, LocalDate endDate);
    List<Map<String, Object>> getCustomersBySalesRepWithTotals();
    Double getTotalSalesAmount();
    Double getTotalOutstandingAmount();
    Double getTotalOverdueAmount();
    Double getTotalCreditLimit();
    Double getTotalAvailableCredit();
    Double getAverageOrderValue();
    Double getCreditUtilizationRate();
    Map<String, Object> getDashboardStatistics();
}
