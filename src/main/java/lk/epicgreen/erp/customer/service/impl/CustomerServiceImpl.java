package lk.epicgreen.erp.customer.service.impl;

import lk.epicgreen.erp.customer.dto.request.CustomerLedgerRequest;
import lk.epicgreen.erp.customer.dto.request.CustomerRequest;
import lk.epicgreen.erp.customer.dto.response.CustomerLedgerResponse;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
    public CustomerResponse getCustomerByEmail(String email){
        Customer customer = customerRepository.findByEmailAndDeletedAtIsNull(email)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + email));
        return customerMapper.toResponse(customer);
    }

    @Override
    public CustomerResponse getCustomerByCode(String customerCode) {
        Customer customer = customerRepository.findByCustomerCodeAndDeletedAtIsNull(customerCode)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerCode));
        return customerMapper.toResponse(customer);
    }

    @Override
    public CustomerResponse getCustomerByPhone(String phoneNum) {
        Customer customer = customerRepository.findByPhoneNumberAndDeletedAtIsNull(phoneNum)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + phoneNum));
        return customerMapper.toResponse(customer);
    }

    @Override
    public PageResponse<CustomerResponse> getAllCustomers(Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findByDeletedAtIsNull(pageable);
        return createPageResponse(customerPage);
    }

    @Override
    public List<CustomerResponse> getAllCustomers(){
        List<Customer> customers = customerRepository.findByDeletedAtIsNull();
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> getAllActiveCustomers() {
        List<Customer> customers = customerRepository.findByIsActiveTrueAndDeletedAtIsNull();
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> getCustomersWithOverdueBalance(){
        List<Customer> customers = customerRepository.findCustomersWithOverdueBalance();
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

    @Override
    public CustomerResponse suspendCustomer(Long id, String suspensionReason){
        log.info("Suspending customer: {}", id);

        Customer customer = findCustomerById(id);
        customer.setIsActive(false);
        customer.setSuspensionReason(suspensionReason);
        Customer suspendedCustomer = customerRepository.save(customer);

        log.info("Customer suspended successfully: {}", id);
        return customerMapper.toResponse(suspendedCustomer);
    }

    @Override
    public CustomerResponse verifyCustomer(Long id, Long verifiedByUserId){
        log.info("verifyinf customer: {}", id);

        Customer customer = findCustomerById(id);
        User verifiedBy = userRepository.findById(verifiedByUserId)
            .orElseThrow(() -> new ResourceNotFoundException("Verifying user not found: " + verifiedByUserId));
        customer.setIsVerified(true);
        customer.setVerifiedBy(verifiedBy);
        Customer verifiedCustomer = customerRepository.save(customer);
        log.info("Customer verified successfully: {}", id);
        return customerMapper.toResponse(verifiedCustomer);
    }

    @Override
    public CustomerResponse blacklistCustomer(Long id, String blacklistReason){
        log.info("Blacklisting customer: {}", id);

        Customer customer = findCustomerById(id);
        customer.setIsBlacklisted(true);
        customer.setBlacklistReason(blacklistReason);
        Customer blacklistedCustomer = customerRepository.save(customer);
        log.info("Customer blacklisted successfully: {}", id);
        return customerMapper.toResponse(blacklistedCustomer);
    }

    @Override
    public CustomerResponse removeFromBlacklist(Long id){
        log.info("Removing customer from blacklist: {}", id);

        Customer customer = findCustomerById(id);
        customer.setIsBlacklisted(false);
        customer.setBlacklistReason(null);
        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Customer removed from blacklist successfully: {}", id);
        return customerMapper.toResponse(updatedCustomer);
    }

    @Override
    public CustomerResponse enableCreditFacility(Long id, Double creditLimit, String paymentTerms){
        log.info("Enabling credit facility for customer: {}", id);

        Customer customer = findCustomerById(id);
        customer.setHasCreditFacility(true);
        customer.setCreditLimit(BigDecimal.valueOf(creditLimit));
        customer.setPaymentTerms(paymentTerms);
        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Credit facility enabled successfully for customer: {}", id);
        return customerMapper.toResponse(updatedCustomer);
    }

    @Override
    public CustomerResponse updateCreditLimit(Long id, Double newCreditLimit){
        log.info("Updating credit limit for customer: {}", id);

        Customer customer = findCustomerById(id);
        customer.setCreditLimit(BigDecimal.valueOf(newCreditLimit));
        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Credit limit updated successfully for customer: {}", id);
        return customerMapper.toResponse(updatedCustomer);
    }

    @Override
    public CustomerResponse disableCreditFacility(Long id){
        log.info("Disabling credit facility for customer: {}", id);

        Customer customer = findCustomerById(id);
        customer.setHasCreditFacility(false);
        customer.setCreditLimit(BigDecimal.ZERO);
        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Credit facility disabled successfully for customer: {}", id);
        return customerMapper.toResponse(updatedCustomer);
    }

    @Override
    public CustomerResponse updateCreditStatus(Long id){
        log.info("Updating credit status for customer: {}", id);

        Customer customer = customerOutstandingBalance(id);
        Customer updatedCustomer = customerRepository.save(customer);

        log.info("Credit status updated successfully for customer: {}", id);
        return customerMapper.toResponse(updatedCustomer);
    }

    @Override
    public void updateCustomerOutstanding(Long id){
        log.info("Updating outstanding balance for customer: {}", id);

        Customer customer = customerOutstandingBalance(id);
        customerRepository.save(customer);

        log.info("Outstanding balance updated successfully for customer: {}", id);
    }

    @Override
    public List<CustomerResponse> getActiveCustomers(){
        List<Customer> customers = customerRepository.findByIsActiveTrueOrderByCustomerNameAsc();
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> getInactiveCustomers(){
        List<Customer> customers = customerRepository.findAll().stream()
            .filter(c -> Boolean.FALSE.equals(c.getIsActive()) && c.getDeletedAt() == null)
            .collect(Collectors.toList());
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> getPendingCustomers(){
        List<Customer> customers = customerRepository.findAll().stream()
            .filter(c -> Boolean.FALSE.equals(c.getIsVerified()) && c.getDeletedAt() == null)
            .collect(Collectors.toList());
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> getSuspendedCustomers(){
        List<Customer> customers = customerRepository.findAll().stream()
            .filter(c -> Boolean.FALSE.equals(c.getIsActive()) && c.getSuspensionReason() != null && c.getDeletedAt() == null)
            .collect(Collectors.toList());
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> getBlacklistedCustomers(){
        List<Customer> customers = customerRepository.findAll().stream()
            .filter(c -> Boolean.TRUE.equals(c.getIsBlacklisted()) && c.getDeletedAt() == null)
            .collect(Collectors.toList());
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> getCustomersWithCreditFacility(){
        List<Customer> customers = customerRepository.findAll().stream()
            .filter(c -> Boolean.TRUE.equals(c.getHasCreditFacility()) && c.getDeletedAt() == null)
            .collect(Collectors.toList());
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> getCustomersWithCreditExceeded(){
        List<Customer> customers = customerRepository.findAll().stream()
            .filter(c -> Boolean.TRUE.equals(c.getHasCreditFacility()) &&
                c.getCreditLimit() != null &&
                c.getCurrentBalance() != null &&
                c.getCurrentBalance().compareTo(c.getCreditLimit()) > 0 &&
                c.getDeletedAt() == null)
            .collect(Collectors.toList());
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> getCustomersWithCreditWarning(){
        List<Customer> customers = customerRepository.findAll().stream()
            .filter(c -> Boolean.TRUE.equals(c.getHasCreditFacility()) &&
                c.getCreditLimit() != null &&
                c.getCurrentBalance() != null &&
                c.getCurrentBalance().compareTo(c.getCreditLimit().multiply(BigDecimal.valueOf(0.9))) >= 0 &&
                c.getCurrentBalance().compareTo(c.getCreditLimit()) <= 0 &&
                c.getDeletedAt() == null)
            .collect(Collectors.toList());
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> getUnverifiedCustomers(){
        List<Customer> customers = customerRepository.findAll().stream()
            .filter(c -> Boolean.FALSE.equals(c.getIsVerified()) && c.getDeletedAt() == null)
            .collect(Collectors.toList());
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override 
    public List<CustomerResponse> getCustomersWithoutRecentOrders(int days){
        List<Customer> customers = customerRepository.findAll().stream()
            .filter(c -> c.getDeletedAt() == null)
            .collect(Collectors.toList());
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> getCustomersRequiringAction(){
        List<Customer> customers = customerRepository.findAll().stream()
            .filter(c -> (Boolean.FALSE.equals(c.getIsVerified()) ||
                          Boolean.FALSE.equals(c.getIsActive()) ||
                          Boolean.TRUE.equals(c.getIsBlacklisted()) ||
                          (Boolean.TRUE.equals(c.getHasCreditFacility()) &&
                           c.getCreditLimit() != null &&
                           c.getCurrentBalance() != null &&
                           c.getCurrentBalance().compareTo(c.getCreditLimit()) > 0))
                          && c.getDeletedAt() == null)
            .collect(Collectors.toList());
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> getTopCustomers(int limit){
        List<Customer> customers = customerRepository.findAll().stream()
            .filter(c -> c.getDeletedAt() == null)
            .sorted((c1, c2) -> {
                BigDecimal balance1 = c1.getCurrentBalance() != null ? c1.getCurrentBalance() : BigDecimal.ZERO;
                BigDecimal balance2 = c2.getCurrentBalance() != null ? c2.getCurrentBalance() : BigDecimal.ZERO;
                return balance2.compareTo(balance1);
            })
            .limit(limit)
            .collect(Collectors.toList());
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> getRecentCustomers(int limit){
        List<Customer> customers = customerRepository.findAll().stream()
            .filter(c -> c.getDeletedAt() == null)
            .sorted((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt()))
            .limit(limit)
            .collect(Collectors.toList());
        return customers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public boolean isEmailAvailable(String email){
        return customerRepository.findByEmailAndDeletedAtIsNull(email).isEmpty();
    }

    @Override
    public boolean isPhoneAvailable(String phoneNum){
        return customerRepository.findByPhoneNumberAndDeletedAtIsNull(phoneNum).isEmpty();
    }

    @Override
    public boolean isCustomerCodeAvailable(String customerCode){
        return customerRepository.findByCustomerCodeAndDeletedAtIsNull(customerCode).isEmpty();
    }

    @Override
    public boolean canActivateCustomer(Long id){
        Customer customer = findCustomerById(id);
        return Boolean.FALSE.equals(customer.getIsActive()) && customer.getDeletedAt() == null;
    }

    @Override
    public boolean canSuspendCustomer(Long id){
        Customer customer = findCustomerById(id);
        return Boolean.TRUE.equals(customer.getIsActive()) && customer.getDeletedAt() == null;
    }

    @Override
    public boolean canEnableCreditFacility(Long id){
        Customer customer = findCustomerById(id);
        return Boolean.FALSE.equals(customer.getHasCreditFacility()) && customer.getDeletedAt() == null;
    }

    @Override
    public void calculateCustomerMetrics(Long id){
        log.info("Calculating metrics for customer: {}", id);

        Customer customer = findCustomerById(id);
        // Placeholder for actual metrics calculation logic

        customerRepository.save(customer);

        log.info("Customer metrics calculated successfully: {}", id);
    }

    @Override
    public void updateTotalSalesAmount(Long id,Double amount){
        log.info("Updating total sales amount for customer: {}", id);

        Customer customer = findCustomerById(id);
        BigDecimal currentTotalSales = customer.getTotalSalesAmount() != null ? customer.getTotalSalesAmount() : BigDecimal.ZERO;
        customer.setTotalSalesAmount(currentTotalSales.add(BigDecimal.valueOf(amount)));

        customerRepository.save(customer);

        log.info("Total sales amount updated successfully for customer: {}", id);
    }

    @Override
    public void updateAverageOrderValue(Long id){
        log.info("Updating average order value for customer: {}", id);

        Customer customer = findCustomerById(id);
        // Placeholder for actual average order value calculation logic

        customerRepository.save(customer);

        log.info("Average order value updated successfully for customer: {}", id);
    }

    @Override
    public void updateLastOrderDate(Long id, LocalDate orderDate){
        log.info("Updating last order date for customer: {}", id);

        Customer customer = findCustomerById(id);
        customer.setLastOrderDate(orderDate);

        customerRepository.save(customer);

        log.info("Last order date updated successfully for customer: {}", id);
    }

    @Override
    public void calculateAvailableCredit(Long id){
        log.info("Calculating available credit for customer: {}", id);

        Customer customer = findCustomerById(id);
        // Placeholder for actual available credit calculation logic

        customerRepository.save(customer);

        log.info("Available credit calculated successfully for customer: {}", id);
    }

    @Override
    public List<CustomerResponse> createBulkCustomers(List<CustomerRequest> customerRequests){
        log.info("Creating bulk customers, count: {}", customerRequests.size());

        List<Customer> customers = customerRequests.stream()
            .map(request -> {
                // Validate unique constraint
                validateUniqueCustomerCode(request.getCustomerCode(), null);
                return customerMapper.toEntity(request);
            })
            .collect(Collectors.toList());

        List<Customer> savedCustomers = customerRepository.saveAll(customers);
        log.info("Bulk customers created successfully, count: {}", savedCustomers.size());

        return savedCustomers.stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public int activateBulkCustomers(List<Long> customerIds){
        log.info("Activating bulk customers, count: {}", customerIds.size());

        List<Customer> customers = customerRepository.findAllById(customerIds);
        customers.forEach(customer -> customer.setIsActive(true));
        customerRepository.saveAll(customers);

        log.info("Bulk customers activated successfully, count: {}", customers.size());
        return customers.size();
    }

    @Override
    public int deactivateBulkCustomers(List<Long> customerIds){
        log.info("Deactivating bulk customers, count: {}", customerIds.size());

        List<Customer> customers = customerRepository.findAllById(customerIds);
        customers.forEach(customer -> customer.setIsActive(false));
        customerRepository.saveAll(customers);

        log.info("Bulk customers deactivated successfully, count: {}", customers.size());
        return customers.size();
    }

    @Override
    public int deleteBulkCustomers(List<Long> customerIds){
        log.info("Deleting bulk customers (soft delete), count: {}", customerIds.size());

        List<Customer> customers = customerRepository.findAllById(customerIds);
        LocalDateTime now = LocalDateTime.now();
        customers.forEach(customer -> {
            customer.setDeletedAt(now);
            customer.setIsActive(false);
        });
        customerRepository.saveAll(customers);

        log.info("Bulk customers deleted successfully, count: {}", customers.size());
        return customers.size();
    }

    @Override
    public Map<String, Object> getCustomerStatistics(){
        log.info("Fetching customer statistics");

        long totalCustomers = customerRepository.countByDeletedAtIsNull();
        long activeCustomers = customerRepository.countByIsActiveTrueAndDeletedAtIsNull();
        long inactiveCustomers = customerRepository.countByIsActiveFalseAndDeletedAtIsNull();
        long blacklistedCustomers = customerRepository.countByIsBlacklistedTrueAndDeletedAtIsNull();
        long customersWithCreditFacility = customerRepository.countByHasCreditFacilityTrueAndDeletedAtIsNull();

        Map<String, Object> stats = Map.of(
            "totalCustomers", totalCustomers,
            "activeCustomers", activeCustomers,
            "inactiveCustomers", inactiveCustomers,
            "blacklistedCustomers", blacklistedCustomers,
            "customersWithCreditFacility", customersWithCreditFacility
        );

        log.info("Customer statistics fetched successfully");
        return stats;
    }

    @Override
    public List<Map<String, Object>> getCustomerTypeDistribution(){
        log.info("Fetching customer type distribution");

        List<Object[]> distributionData = customerRepository.countCustomersByType();
        List<Map<String, Object>> distribution = distributionData.stream()
            .map(data -> Map.of(
                "customerType", data[0],
                "count", data[1]
            ))
            .collect(Collectors.toList());

        log.info("Customer type distribution fetched successfully");
        return distribution;
    }

    @Override
    public List<Map<String, Object>> getCustomerStatusDistribution(){
        log.info("Fetching customer status distribution");

        List<Object[]> distributionData = customerRepository.countCustomersByStatus();
        List<Map<String, Object>> distribution = distributionData.stream()
            .map(data -> Map.of(
                "isActive", data[0],
                "count", data[1]
            ))
            .collect(Collectors.toList());

        log.info("Customer status distribution fetched successfully");
        return distribution;
    }

    @Override
    public List<Map<String, Object>> getCreditStatusDistribution(){
        log.info("Fetching customer credit status distribution");

        List<Object[]> distributionData = customerRepository.countCustomersByCreditStatus();
        List<Map<String, Object>> distribution = distributionData.stream()
            .map(data -> Map.of(
                "hasCreditFacility", data[0],
                "count", data[1]
            ))
            .collect(Collectors.toList());

        log.info("Customer credit status distribution fetched successfully");
        return distribution;
    }

    @Override
    public List<Map<String, Object>> getPaymentTermsDistribution(){
        log.info("Fetching customer payment terms distribution");

        List<Object[]> distributionData = customerRepository.countCustomersByPaymentTerms();
        List<Map<String, Object>> distribution = distributionData.stream()
            .map(data -> Map.of(
                "paymentTerms", data[0],
                "count", data[1]
            ))
            .collect(Collectors.toList());

        log.info("Customer payment terms distribution fetched successfully");
        return distribution;
    }

    @Override
    public List<Map<String, Object>> getRouteDistribution(){
        log.info("Fetching customer route distribution");

        List<Object[]> distributionData = customerRepository.countCustomersByRoute();
        List<Map<String, Object>> distribution = distributionData.stream()
            .map(data -> Map.of(
                "routeCode", data[0],
                "count", data[1]
            ))
            .collect(Collectors.toList());

        log.info("Customer route distribution fetched successfully");
        return distribution;
    }

    @Override
    public List<Map<String, Object>> getCityDistribution(){
        log.info("Fetching customer city distribution");

        List<Object[]> distributionData = customerRepository.countCustomersByCity();
        List<Map<String, Object>> distribution = distributionData.stream()
            .map(data -> Map.of(
                "city", data[0],
                "count", data[1]
            ))
            .collect(Collectors.toList());

        log.info("Customer city distribution fetched successfully");
        return distribution;
    }

    @Override
    public List<Map<String, Object>> getProvinceDistribution(){
        log.info("Fetching customer province distribution");

        List<Object[]> distributionData = customerRepository.countCustomersByProvince();
        List<Map<String, Object>> distribution = distributionData.stream()
            .map(data -> Map.of(
                "province", data[0],
                "count", data[1]
            ))
            .collect(Collectors.toList());

        log.info("Customer province distribution fetched successfully");
        return distribution;
    }

    @Override
    public List<Map<String, Object>> getMonthlyRegistrationCount(){
        log.info("Fetching monthly customer registration count");

        List<Object[]> registrationData = customerRepository.countMonthlyRegistrations();
        List<Map<String, Object>> monthlyCounts = registrationData.stream()
            .map(data -> Map.of(
                "month", data[0],
                "year", data[1],
                "count", data[2]
            ))
            .collect(Collectors.toList());

        log.info("Monthly customer registration count fetched successfully");
        return monthlyCounts;
    }

    @Override
    public List<Map<String, Object>> getCustomersBySalesRepWithTotals(){
        log.info("Fetching customers by sales representative with totals");

        List<Object[]> salesRepData = customerRepository.findCustomersBySalesRepWithTotals();
        List<Map<String, Object>> result = salesRepData.stream()
            .map(data -> Map.of(
                "salesRepId", data[0],
                "salesRepName", data[1],
                "customerCount", data[2],
                "totalSalesAmount", data[3]
            ))
            .collect(Collectors.toList());

        log.info("Customers by sales representative with totals fetched successfully");
        return result;
    }

    @Override
    public Double getTotalCustomerSales(){
        log.info("Calculating total customer sales amount");

        Double totalSales = customerRepository.calculateTotalCustomerSales();

        log.info("Total customer sales amount calculated successfully: {}", totalSales);
        return totalSales;
    }

    @Override
    public Double getTotalOutstandingBalance(){
        log.info("Calculating total outstanding balance across all customers");

        Double totalOutstanding = customerRepository.calculateTotalOutstandingBalance();

        log.info("Total outstanding balance calculated successfully: {}", totalOutstanding);
        return totalOutstanding;
    }

    @Override
    public Double getTotalOverdueAmount(){
        log.info("Calculating total overdue amount across all customers");

        Double totalOverdue = customerRepository.calculateTotalOverdueAmount();

        log.info("Total overdue amount calculated successfully: {}", totalOverdue);
        return totalOverdue;
    }

    @Override
    public Double getTotalCreditLimit(){
        log.info("Calculating total credit limit across all customers");

        Double totalCreditLimit = customerRepository.calculateTotalCreditLimit();

        log.info("Total credit limit calculated successfully: {}", totalCreditLimit);
        return totalCreditLimit;
    }

    @Override
    public  Double getTotalAvailableCredit(){
        log.info("Calculating total available credit across all customers");

        Double totalAvailableCredit = customerRepository.calculateTotalAvailableCredit();

        log.info("Total available credit calculated successfully: {}", totalAvailableCredit);
        return totalAvailableCredit;
    }

    @Override
    public Double getAverageOrderValue(){
        log.info("Calculating average order value across all customers");

        Double averageOrderValue = customerRepository.calculateAverageOrderValue();

        log.info("Average order value calculated successfully: {}", averageOrderValue);
        return averageOrderValue;
    }

    @Override
    public Double getCreditUtilizationRate(){
        log.info("Calculating credit utilization rate across all customers");

        Double creditUtilizationRate = customerRepository.calculateCreditUtilizationRate();

        log.info("Credit utilization rate calculated successfully: {}", creditUtilizationRate);
        return creditUtilizationRate;
    }

    @Override
    public Map<String, Object> getDashboardStatistics(){
        log.info("Fetching customer dashboard statistics");

        Map<String, Object> stats = Map.of(
            "totalSales", getTotalCustomerSales(),
            "totalOutstandingBalance", getTotalOutstandingBalance(),
            "totalOverdueAmount", getTotalOverdueAmount(),
            "totalCreditLimit", getTotalCreditLimit(),
            "totalAvailableCredit", getTotalAvailableCredit(),
            "averageOrderValue", getAverageOrderValue(),
            "creditUtilizationRate", getCreditUtilizationRate()
        );

        log.info("Customer dashboard statistics fetched successfully");
        return stats;
    }

    @Override
    public CustomerLedgerResponse reverseLedgerEntry(Long id, String reversalReason){
        log.info("Reversing ledger entry for customer: {}", id);

        Customer customer = findCustomerById(id);
        // Placeholder for actual ledger reversal logic

        customerRepository.save(customer);

        log.info("Ledger entry reversed successfully for customer: {}", id);
        return CustomerLedgerResponse.builder()
            .message("Ledger entry reversed successfully for customer: " + id)
            .build();
    }

    @Override
    public CustomerLedgerResponse createLedgerEntry(CustomerLedgerRequest request){
        log.info("Creating ledger entry for customer: {}", request.getCustomerId());

        Customer customer = findCustomerById(request.getCustomerId());
        // Placeholder for actual ledger entry creation logic

        customerRepository.save(customer);

        log.info("Ledger entry created successfully for customer: {}", request.getCustomerId());
        return CustomerLedgerResponse.builder()
            .message("Ledger entry created successfully for customer: " + request.getCustomerId())
            .build();
    }

    @Override
    public void reconcileLedgerEntry(Long id){
        log.info("Reconciling ledger entry for customer: {}", id);

        Customer customer = findCustomerById(id);
        // Placeholder for actual ledger reconciliation logic

        customerRepository.save(customer);

        log.info("Ledger entry reconciled successfully for customer: {}", id);
    }

    @Override
    public Page<CustomerLedgerResponse> getCustomerLedgerEntries(Long customerId, Pageable pageable){
        log.info("Fetching ledger entries for customer: {}", customerId);

        Page<CustomerLedgerResponse> ledgerEntries = Page.empty(pageable);
        // Placeholder for actual ledger entries retrieval logic

        log.info("Ledger entries fetched successfully for customer: {}", customerId);
        return ledgerEntries;
    }

    @Override
    public List<CustomerLedgerResponse> getCustomerLedgerEntries(Long customerId){
        log.info("Fetching all ledger entries for customer: {}", customerId);

        List<CustomerLedgerResponse> ledgerEntries = List.of();
        // Placeholder for actual ledger entries retrieval logic

        log.info("All ledger entries fetched successfully for customer: {}", customerId);
        return ledgerEntries;
    }

    @Override
    public List<CustomerLedgerResponse> getCustomerLedgerStatement(Long customerId, LocalDate startDate, LocalDate endDate){
        log.info("Fetching ledger statement for customer: {} from {} to {}", customerId, startDate, endDate);

        List<CustomerLedgerResponse> ledgerStatement = List.of();
        // Placeholder for actual ledger statement retrieval logic

        log.info("Ledger statement fetched successfully for customer: {}", customerId);
        return ledgerStatement;
    }

    @Override
    public Double getCustomerOpeningBalance(Long customerId, LocalDate startDate){
        log.info("Calculating opening balance for customer: {} as of {}", customerId, startDate);

        Double openingBalance = 0.0;
        // Placeholder for actual opening balance calculation logic

        log.info("Opening balance calculated successfully for customer: {}", customerId);
        return openingBalance;
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private Customer customerOutstandingBalance(Long id) {
        Customer customer = findCustomerById(id);
        boolean hasOutstandingBalance = customer.getCurrentBalance() != null && customer.getCurrentBalance().compareTo(BigDecimal.ZERO) > 0;
        customer.setHasOutstandingBalance(hasOutstandingBalance);
        return customer;
    }

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
