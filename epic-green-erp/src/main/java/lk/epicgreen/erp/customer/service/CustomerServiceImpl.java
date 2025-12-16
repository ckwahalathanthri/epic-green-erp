package lk.epicgreen.erp.customer.service;

import lk.epicgreen.erp.customer.dto.CustomerRequest;
import lk.epicgreen.erp.customer.dto.CustomerLedgerRequest;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.entity.CustomerLedger;
import lk.epicgreen.erp.customer.repository.CustomerRepository;
import lk.epicgreen.erp.customer.repository.CustomerLedgerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Customer Service Implementation
 * Implementation of customer service operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerServiceImpl implements CustomerService {
    
    private final CustomerRepository customerRepository;
    private final CustomerLedgerRepository ledgerRepository;
    
    @Override
    public Customer createCustomer(CustomerRequest request) {
        log.info("Creating customer: {}", request.getCustomerName());
        
        // Validate unique fields
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }
        if (customerRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone already exists: " + request.getPhone());
        }
        
        Customer customer = new Customer();
        customer.setCustomerCode(generateCustomerCode());
        customer.setCustomerName(request.getCustomerName());
        customer.setBusinessName(request.getBusinessName());
        customer.setCustomerType(request.getCustomerType() != null ? request.getCustomerType() : "RETAIL");
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setMobile(request.getMobile());
        customer.setTaxNumber(request.getTaxNumber());
        customer.setRegistrationDate(LocalDate.now());
        customer.setStatus("PENDING");
        customer.setIsActive(true);
        customer.setIsVerified(false);
        customer.setIsBlacklisted(false);
        customer.setHasCreditFacility(false);
        customer.setCreditLimit(BigDecimal.ZERO);
        customer.setCurrentOutstanding(0.0);
        customer.setAvailableCredit(0.0);
        customer.setOverdueAmount(0.0);
        customer.setTotalSalesAmount(0.0);
        customer.setTotalOrderCount(0);
        customer.setAverageOrderValue(0.0);
        
        // Address
        customer.setAddressLine1(request.getAddressLine1());
        customer.setAddressLine2(request.getAddressLine2());
        customer.setCity(request.getCity());
        customer.setProvince(request.getProvince());
        customer.setPostalCode(request.getPostalCode());
        customer.setCountry(request.getCountry());
        
        // Business details
        customer.setPaymentTerms(request.getPaymentTerms() != null ? request.getPaymentTerms() : "CASH");
        customer.setSalesRepId(request.getSalesRepId());
        customer.setSalesRepName(request.getSalesRepName());
        customer.setRoute(request.getRoute());
        customer.setNotes(request.getNotes());
        
        return customerRepository.save(customer);
    }
    
    @Override
    public Customer updateCustomer(Long id, CustomerRequest request) {
        log.info("Updating customer: {}", id);
        Customer existing = getCustomerById(id);
        
        // Validate unique fields if changed
        if (!existing.getEmail().equals(request.getEmail()) && 
            customerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }
        if (!existing.getPhone().equals(request.getPhone()) && 
            customerRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone already exists: " + request.getPhone());
        }
        
        existing.setCustomerName(request.getCustomerName());
        existing.setBusinessName(request.getBusinessName());
        existing.setCustomerType(request.getCustomerType());
        existing.setEmail(request.getEmail());
        existing.setPhone(request.getPhone());
        existing.setMobile(request.getMobile());
        existing.setTaxNumber(request.getTaxNumber());
        existing.setAddressLine1(request.getAddressLine1());
        existing.setAddressLine2(request.getAddressLine2());
        existing.setCity(request.getCity());
        existing.setProvince(request.getProvince());
        existing.setPostalCode(request.getPostalCode());
        existing.setCountry(request.getCountry());
        existing.setPaymentTerms(request.getPaymentTerms());
        existing.setRoute(request.getRoute());
        existing.setNotes(request.getNotes());
        
        return customerRepository.save(existing);
    }
    
    @Override
    public void deleteCustomer(Long id) {
        log.info("Deleting customer: {}", id);
        Customer customer = getCustomerById(id);
        
        if (customer.getCurrentOutstanding() > 0) {
            throw new RuntimeException("Cannot delete customer with outstanding balance");
        }
        
        customerRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerByCode(String customerCode) {
        return customerRepository.findByCustomerCode(customerCode)
            .orElseThrow(() -> new RuntimeException("Customer not found with code: " + customerCode));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Customer not found with email: " + email));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerByPhone(String phone) {
        return customerRepository.findByPhone(phone)
            .orElseThrow(() -> new RuntimeException("Customer not found with phone: " + phone));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Customer> searchCustomers(String keyword, Pageable pageable) {
        return customerRepository.searchCustomers(keyword, pageable);
    }
    
    @Override
    public Customer activateCustomer(Long id) {
        log.info("Activating customer: {}", id);
        Customer customer = getCustomerById(id);
        
        customer.setIsActive(true);
        customer.setStatus("ACTIVE");
        
        return customerRepository.save(customer);
    }
    
    @Override
    public Customer deactivateCustomer(Long id) {
        log.info("Deactivating customer: {}", id);
        Customer customer = getCustomerById(id);
        
        customer.setIsActive(false);
        customer.setStatus("INACTIVE");
        
        return customerRepository.save(customer);
    }
    
    @Override
    public Customer suspendCustomer(Long id, String suspensionReason) {
        log.info("Suspending customer: {}", id);
        Customer customer = getCustomerById(id);
        
        customer.setStatus("SUSPENDED");
        customer.setSuspensionReason(suspensionReason);
        customer.setSuspendedDate(LocalDate.now());
        
        return customerRepository.save(customer);
    }
    
    @Override
    public Customer verifyCustomer(Long id, Long verifiedByUserId) {
        log.info("Verifying customer: {}", id);
        Customer customer = getCustomerById(id);
        
        customer.setIsVerified(true);
        customer.setVerifiedDate(LocalDate.now());
        customer.setVerifiedByUserId(verifiedByUserId);
        customer.setStatus("ACTIVE");
        
        return customerRepository.save(customer);
    }
    
    @Override
    public Customer blacklistCustomer(Long id, String blacklistReason) {
        log.info("Blacklisting customer: {}", id);
        Customer customer = getCustomerById(id);
        
        customer.setIsBlacklisted(true);
        customer.setBlacklistReason(blacklistReason);
        customer.setBlacklistedDate(LocalDate.now());
        customer.setStatus("SUSPENDED");
        
        return customerRepository.save(customer);
    }
    
    @Override
    public Customer removeFromBlacklist(Long id) {
        log.info("Removing customer from blacklist: {}", id);
        Customer customer = getCustomerById(id);
        
        customer.setIsBlacklisted(false);
        customer.setBlacklistReason(null);
        customer.setBlacklistedDate(null);
        customer.setStatus("ACTIVE");
        
        return customerRepository.save(customer);
    }
    
    @Override
    public Customer enableCreditFacility(Long id, Double creditLimit, String paymentTerms) {
        log.info("Enabling credit facility for customer: {}", id);
        Customer customer = getCustomerById(id);
        
        customer.setHasCreditFacility(true);
        customer.setCreditLimit(creditLimit != null ? BigDecimal.valueOf(creditLimit) : BigDecimal.ZERO);
        customer.setPaymentTerms(paymentTerms);
        customer.setCreditStatus("GOOD");
        calculateAvailableCredit(id);
        
        return customerRepository.save(customer);
    }
    
    @Override
    public Customer updateCreditLimit(Long id, Double newCreditLimit) {
        log.info("Updating credit limit for customer: {}", id);
        Customer customer = getCustomerById(id);
        
        if (!customer.getHasCreditFacility()) {
            throw new RuntimeException("Customer does not have credit facility");
        }
        
        customer.setCreditLimit(newCreditLimit != null ? BigDecimal.valueOf(newCreditLimit) : BigDecimal.ZERO);
        calculateAvailableCredit(id);
        updateCreditStatus(id);
        
        return customerRepository.save(customer);
    }
    
    @Override
    public Customer disableCreditFacility(Long id) {
        log.info("Disabling credit facility for customer: {}", id);
        Customer customer = getCustomerById(id);
        
        if (customer.getCurrentOutstanding() > 0) {
            throw new RuntimeException("Cannot disable credit facility with outstanding balance");
        }
        
        customer.setHasCreditFacility(false);
        customer.setCreditLimit(BigDecimal.ZERO);
        customer.setAvailableCredit(0.0);
        customer.setCreditStatus(null);
        customer.setPaymentTerms("CASH");
        
        return customerRepository.save(customer);
    }
    
    @Override
    public Customer updateCreditStatus(Long id) {
        Customer customer = getCustomerById(id);
        
        if (!customer.getHasCreditFacility()) {
            return customer;
        }
        
        Double outstanding = customer.getCurrentOutstanding();
        Double creditLimit = customer.getCreditLimit() != null ? customer.getCreditLimit().doubleValue() : 0.0;
        
        if (outstanding > creditLimit) {
            customer.setCreditStatus("EXCEEDED");
        } else if (outstanding >= creditLimit * 0.8) {
            customer.setCreditStatus("WARNING");
        } else if (outstanding > 0) {
            customer.setCreditStatus("UTILIZED");
        } else {
            customer.setCreditStatus("GOOD");
        }
        
        return customerRepository.save(customer);
    }
    
    @Override
    public void updateCustomerOutstanding(Long customerId) {
        Customer customer = getCustomerById(customerId);
        Double balance = getCustomerBalance(customerId);
        
        customer.setCurrentOutstanding(balance != null && balance > 0 ? balance : 0.0);
        calculateAvailableCredit(customerId);
        updateCreditStatus(customerId);
        
        customerRepository.save(customer);
    }
    
    @Override
    public CustomerLedger createLedgerEntry(CustomerLedgerRequest request) {
        log.info("Creating ledger entry for customer: {}", request.getCustomerId());
        
        CustomerLedger ledger = new CustomerLedger();
        ledger.setTransactionReference(generateTransactionReference());
        ledger.setCustomerId(request.getCustomerId());
        ledger.setCustomerName(request.getCustomerName());
        ledger.setTransactionDate(request.getTransactionDate() != null ? request.getTransactionDate() : LocalDate.now());
        ledger.setTransactionType(request.getTransactionType());
        ledger.setEntryType(request.getEntryType());
        ledger.setDebitAmount(request.getDebitAmount() != null ? BigDecimal.valueOf(request.getDebitAmount()) : BigDecimal.ZERO);
        ledger.setCreditAmount(request.getCreditAmount() != null ? BigDecimal.valueOf(request.getCreditAmount()) : BigDecimal.ZERO);
        ledger.setDescription(request.getDescription());
        ledger.setReferenceType(request.getReferenceType());
        ledger.setReferenceId(request.getReferenceId());
        ledger.setInvoiceId(request.getInvoiceId());
        ledger.setPaymentId(request.getPaymentId());
        ledger.setSalesOrderId(request.getSalesOrderId());
        ledger.setDueDate(request.getDueDate());
        ledger.setIsReconciled(false);
        ledger.setIsReversed(false);
        
        CustomerLedger saved = ledgerRepository.save(ledger);
        
        // Update customer outstanding
        updateCustomerOutstanding(request.getCustomerId());
        
        return saved;
    }
    
    @Override
    public CustomerLedger reverseLedgerEntry(Long ledgerEntryId, String reversalReason) {
        log.info("Reversing ledger entry: {}", ledgerEntryId);
        CustomerLedger ledger = ledgerRepository.findById(ledgerEntryId)
            .orElseThrow(() -> new RuntimeException("Ledger entry not found"));
        
        if (ledger.getIsReversed()) {
            throw new RuntimeException("Ledger entry already reversed");
        }
        
        ledger.setIsReversed(true);
        ledger.setReversalReason(reversalReason);
        ledger.setReversedDate(LocalDate.now());
        
        CustomerLedger reversed = ledgerRepository.save(ledger);
        
        // Update customer outstanding
        updateCustomerOutstanding(ledger.getCustomerId());
        
        return reversed;
    }
    
    @Override
    public void reconcileLedgerEntry(Long ledgerEntryId) {
        log.info("Reconciling ledger entry: {}", ledgerEntryId);
        CustomerLedger ledger = ledgerRepository.findById(ledgerEntryId)
            .orElseThrow(() -> new RuntimeException("Ledger entry not found"));
        
        ledger.setIsReconciled(true);
        ledger.setReconciledDate(LocalDate.now());
        
        ledgerRepository.save(ledger);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CustomerLedger> getCustomerLedgerEntries(Long customerId) {
        return ledgerRepository.findByCustomerId(customerId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<CustomerLedger> getCustomerLedgerEntries(Long customerId, Pageable pageable) {
        return ledgerRepository.findByCustomerId(customerId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CustomerLedger> getCustomerLedgerStatement(Long customerId, LocalDate startDate, LocalDate endDate) {
        return ledgerRepository.getCustomerLedgerStatement(customerId, startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getCustomerBalance(Long customerId) {
        Double balance = ledgerRepository.calculateCustomerBalance(customerId);
        return balance != null ? balance : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getCustomerOpeningBalance(Long customerId, LocalDate startDate) {
        Double balance = ledgerRepository.getOpeningBalance(customerId, startDate);
        return balance != null ? balance : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Customer> getActiveCustomers() {
        return customerRepository.findActiveCustomers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Customer> getInactiveCustomers() {
        return customerRepository.findInactiveCustomers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Customer> getPendingCustomers() {
        return customerRepository.findPendingCustomers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Customer> getSuspendedCustomers() {
        return customerRepository.findSuspendedCustomers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Customer> getBlacklistedCustomers() {
        return customerRepository.findBlacklistedCustomers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Customer> getCustomersWithCreditFacility() {
        return customerRepository.findCustomersWithCreditFacility();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Customer> getCustomersWithCreditExceeded() {
        return customerRepository.findCustomersWithCreditExceeded();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Customer> getCustomersWithCreditWarning() {
        return customerRepository.findCustomersWithCreditWarning();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Customer> getCustomersWithOutstandingBalance() {
        return customerRepository.findCustomersWithOutstandingBalance();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Customer> getCustomersWithOverdueBalance() {
        return customerRepository.findCustomersWithOverdueBalance();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Customer> getUnverifiedCustomers() {
        return customerRepository.findUnverifiedCustomers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Customer> getCustomersWithoutRecentOrders(int days) {
        LocalDate thresholdDate = LocalDate.now().minusDays(days);
        return customerRepository.findCustomersWithoutRecentOrders(thresholdDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Customer> getCustomersRequiringAction() {
        return customerRepository.findCustomersRequiringAction();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Customer> getCustomersByType(String customerType) {
        return customerRepository.findByCustomerType(customerType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Customer> getCustomersByRoute(String route) {
        return customerRepository.findActiveCustomersByRoute(route);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Customer> getCustomersBySalesRep(Long salesRepId) {
        return customerRepository.findActiveCustomersBySalesRep(salesRepId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Customer> getCustomersBySalesRep(Long salesRepId, Pageable pageable) {
        return customerRepository.findBySalesRepId(salesRepId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Customer> getTopCustomers(int limit) {
        return customerRepository.findTopCustomersByTotalSales(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Customer> getRecentCustomers(int limit) {
        return customerRepository.findRecentCustomers(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validateCustomer(Customer customer) {
        return customer.getCustomerName() != null &&
               customer.getEmail() != null &&
               customer.getPhone() != null;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canActivateCustomer(Long customerId) {
        Customer customer = getCustomerById(customerId);
        return !customer.getIsActive() && !customer.getIsBlacklisted();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canSuspendCustomer(Long customerId) {
        Customer customer = getCustomerById(customerId);
        return customer.getIsActive() && !"SUSPENDED".equals(customer.getStatus());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canEnableCreditFacility(Long customerId) {
        Customer customer = getCustomerById(customerId);
        return !customer.getHasCreditFacility() && 
               customer.getIsVerified() && 
               !customer.getIsBlacklisted();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        return !customerRepository.existsByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isPhoneAvailable(String phone) {
        return !customerRepository.existsByPhone(phone);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isCustomerCodeAvailable(String customerCode) {
        return !customerRepository.existsByCustomerCode(customerCode);
    }
    
    @Override
    public void calculateCustomerMetrics(Long customerId) {
        // This would be implemented to calculate various customer metrics
        updateCustomerOutstanding(customerId);
    }
    
    @Override
    public void updateTotalSalesAmount(Long customerId, Double amount) {
        Customer customer = getCustomerById(customerId);
        customer.setTotalSalesAmount(customer.getTotalSalesAmount() + amount);
        customer.setTotalOrderCount(customer.getTotalOrderCount() + 1);
        updateAverageOrderValue(customerId);
        customerRepository.save(customer);
    }
    
    @Override
    public void updateLastOrderDate(Long customerId, LocalDate orderDate) {
        Customer customer = getCustomerById(customerId);
        customer.setLastOrderDate(orderDate);
        customerRepository.save(customer);
    }
    
    @Override
    public void updateAverageOrderValue(Long customerId) {
        Customer customer = getCustomerById(customerId);
        if (customer.getTotalOrderCount() > 0) {
            customer.setAverageOrderValue(customer.getTotalSalesAmount() / customer.getTotalOrderCount());
        }
        customerRepository.save(customer);
    }
    
    @Override
    public void calculateAvailableCredit(Long customerId) {
        Customer customer = getCustomerById(customerId);
        if (customer.getHasCreditFacility()) {
            BigDecimal creditLimit = customer.getCreditLimit() != null ? customer.getCreditLimit() : BigDecimal.ZERO;
            Double currentOutstanding = customer.getCurrentOutstanding() != null ? customer.getCurrentOutstanding() : 0.0;
            BigDecimal currentOutstandingBD = BigDecimal.valueOf(currentOutstanding);
            BigDecimal availableBD = creditLimit.subtract(currentOutstandingBD);
            Double available = availableBD.doubleValue();
            customer.setAvailableCredit(available > 0 ? available : 0.0);
            customerRepository.save(customer);
        }
    }
    
    @Override
    public List<Customer> createBulkCustomers(List<CustomerRequest> requests) {
        return requests.stream()
            .map(this::createCustomer)
            .collect(Collectors.toList());
    }
    
    @Override
    public int activateBulkCustomers(List<Long> customerIds) {
        int count = 0;
        for (Long id : customerIds) {
            try {
                activateCustomer(id);
                count++;
            } catch (Exception e) {
                log.error("Error activating customer: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int deactivateBulkCustomers(List<Long> customerIds) {
        int count = 0;
        for (Long id : customerIds) {
            try {
                deactivateCustomer(id);
                count++;
            } catch (Exception e) {
                log.error("Error deactivating customer: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int deleteBulkCustomers(List<Long> customerIds) {
        int count = 0;
        for (Long id : customerIds) {
            try {
                deleteCustomer(id);
                count++;
            } catch (Exception e) {
                log.error("Error deleting customer: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getCustomerStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalCustomers", customerRepository.count());
        stats.put("activeCustomers", customerRepository.countActiveCustomers());
        stats.put("customersWithCredit", customerRepository.countCustomersWithCreditFacility());
        stats.put("creditExceeded", customerRepository.countCustomersWithCreditExceeded());
        stats.put("outstandingCustomers", customerRepository.countCustomersWithOutstandingBalance());
        stats.put("unverifiedCustomers", customerRepository.countUnverifiedCustomers());
        stats.put("totalSalesAmount", getTotalSalesAmount());
        stats.put("totalOutstanding", getTotalOutstandingAmount());
        stats.put("totalOverdue", getTotalOverdueAmount());
        stats.put("creditUtilization", getCreditUtilizationRate());
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCustomerTypeDistribution() {
        List<Object[]> results = customerRepository.getCustomerTypeDistribution();
        return convertToMapList(results, "customerType", "customerCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getStatusDistribution() {
        List<Object[]> results = customerRepository.getStatusDistribution();
        return convertToMapList(results, "status", "customerCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCreditStatusDistribution() {
        List<Object[]> results = customerRepository.getCreditStatusDistribution();
        return convertToMapList(results, "creditStatus", "customerCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPaymentTermsDistribution() {
        List<Object[]> results = customerRepository.getPaymentTermsDistribution();
        return convertToMapList(results, "paymentTerms", "customerCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getRouteDistribution() {
        List<Object[]> results = customerRepository.getRouteDistribution();
        return convertToMapList(results, "route", "customerCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCityDistribution() {
        List<Object[]> results = customerRepository.getCityDistribution();
        return convertToMapList(results, "city", "customerCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getProvinceDistribution() {
        List<Object[]> results = customerRepository.getProvinceDistribution();
        return convertToMapList(results, "province", "customerCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMonthlyRegistrationCount(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = customerRepository.getMonthlyRegistrationCount(startDate, endDate);
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("year", result[0]);
                map.put("month", result[1]);
                map.put("customerCount", result[2]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCustomersBySalesRepWithTotals() {
        List<Object[]> results = customerRepository.getCustomersBySalesRepWithTotals();
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("salesRepId", result[0]);
                map.put("salesRepName", result[1]);
                map.put("customerCount", result[2]);
                map.put("totalSales", result[3]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalSalesAmount() {
        Double total = customerRepository.getTotalSalesAmount();
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalOutstandingAmount() {
        Double total = customerRepository.getTotalOutstandingAmount();
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalOverdueAmount() {
        Double total = customerRepository.getTotalOverdueAmount();
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalCreditLimit() {
        Double total = customerRepository.getTotalCreditLimit();
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalAvailableCredit() {
        Double total = customerRepository.getTotalAvailableCredit();
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getAverageOrderValue() {
        Double average = customerRepository.getAverageOrderValue();
        return average != null ? average : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getCreditUtilizationRate() {
        Double rate = customerRepository.getCreditUtilizationRate();
        return rate != null ? rate : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("statistics", getCustomerStatistics());
        dashboard.put("typeDistribution", getCustomerTypeDistribution());
        dashboard.put("statusDistribution", getStatusDistribution());
        dashboard.put("creditStatusDistribution", getCreditStatusDistribution());
        
        return dashboard;
    }
    
    private String generateCustomerCode() {
        return "CUS-" + System.currentTimeMillis();
    }
    
    private String generateTransactionReference() {
        return "TXN-" + System.currentTimeMillis();
    }
    
    private List<Map<String, Object>> convertToMapList(List<Object[]> results, String key1, String key2) {
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put(key1, result[0]);
                map.put(key2, result[1]);
                return map;
            })
            .collect(Collectors.toList());
    }
}
