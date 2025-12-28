package lk.epicgreen.erp.customer.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.customer.dto.CustomerRequest;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Customer Controller
 * REST controller for customer operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class CustomerController {
    
    private final CustomerService customerService;
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Customer>> createCustomer(@Valid @RequestBody CustomerRequest request) {
        log.info("Creating customer: {}", request.getCustomerName());
        Customer created = customerService.createCustomer(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Customer created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Customer>> updateCustomer(
        @PathVariable Long id,
        @Valid @RequestBody CustomerRequest request
    ) {
        log.info("Updating customer: {}", id);
        Customer updated = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Customer updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {
        log.info("Deleting customer: {}", id);
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Customer deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Customer>> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success(customer, "Customer retrieved successfully"));
    }
    
    @GetMapping("/code/{customerCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Customer>> getCustomerByCode(@PathVariable String customerCode) {
        Customer customer = customerService.getCustomerByCode(customerCode);
        return ResponseEntity.ok(ApiResponse.success(customer, "Customer retrieved successfully"));
    }
    
    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Customer>> getCustomerByEmail(@PathVariable String email) {
        Customer customer = customerService.getCustomerByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(customer, "Customer retrieved successfully"));
    }
    
    @GetMapping("/phone/{phone}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Customer>> getCustomerByPhone(@PathVariable String phone) {
        Customer customer = customerService.getCustomerByPhone(phone);
        return ResponseEntity.ok(ApiResponse.success(customer, "Customer retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Page<Customer>>> getAllCustomers(Pageable pageable) {
        Page<Customer> customers = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<List<Customer>>> getAllCustomersList() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers list retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Page<Customer>>> searchCustomers(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<Customer> customers = customerService.searchCustomers(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(customers, "Search results retrieved successfully"));
    }
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Customer>> activateCustomer(@PathVariable Long id) {
        log.info("Activating customer: {}", id);
        Customer activated = customerService.activateCustomer(id);
        return ResponseEntity.ok(ApiResponse.success(activated, "Customer activated successfully"));
    }
    
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Customer>> deactivateCustomer(@PathVariable Long id) {
        log.info("Deactivating customer: {}", id);
        Customer deactivated = customerService.deactivateCustomer(id);
        return ResponseEntity.ok(ApiResponse.success(deactivated, "Customer deactivated successfully"));
    }
    
    @PutMapping("/{id}/suspend")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Customer>> suspendCustomer(
        @PathVariable Long id,
        @RequestParam String suspensionReason
    ) {
        log.info("Suspending customer: {}", id);
        Customer suspended = customerService.suspendCustomer(id, suspensionReason);
        return ResponseEntity.ok(ApiResponse.success(suspended, "Customer suspended successfully"));
    }
    
    @PutMapping("/{id}/verify")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Customer>> verifyCustomer(
        @PathVariable Long id,
        @RequestParam Long verifiedByUserId
    ) {
        log.info("Verifying customer: {}", id);
        Customer verified = customerService.verifyCustomer(id, verifiedByUserId);
        return ResponseEntity.ok(ApiResponse.success(verified, "Customer verified successfully"));
    }
    
    @PutMapping("/{id}/blacklist")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Customer>> blacklistCustomer(
        @PathVariable Long id,
        @RequestParam String blacklistReason
    ) {
        log.info("Blacklisting customer: {}", id);
        Customer blacklisted = customerService.blacklistCustomer(id, blacklistReason);
        return ResponseEntity.ok(ApiResponse.success(blacklisted, "Customer blacklisted successfully"));
    }
    
    @PutMapping("/{id}/remove-blacklist")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Customer>> removeFromBlacklist(@PathVariable Long id) {
        log.info("Removing customer from blacklist: {}", id);
        Customer removed = customerService.removeFromBlacklist(id);
        return ResponseEntity.ok(ApiResponse.success(removed, "Customer removed from blacklist successfully"));
    }
    
    // ===================================================================
    // CREDIT OPERATIONS
    // ===================================================================
    
    @PutMapping("/{id}/credit/enable")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Customer>> enableCreditFacility(
        @PathVariable Long id,
        @RequestParam Double creditLimit,
        @RequestParam String paymentTerms
    ) {
        log.info("Enabling credit facility for customer: {}", id);
        Customer enabled = customerService.enableCreditFacility(id, creditLimit, paymentTerms);
        return ResponseEntity.ok(ApiResponse.success(enabled, "Credit facility enabled successfully"));
    }
    
    @PutMapping("/{id}/credit/limit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Customer>> updateCreditLimit(
        @PathVariable Long id,
        @RequestParam Double newCreditLimit
    ) {
        log.info("Updating credit limit for customer: {}", id);
        Customer updated = customerService.updateCreditLimit(id, newCreditLimit);
        return ResponseEntity.ok(ApiResponse.success(updated, "Credit limit updated successfully"));
    }
    
    @PutMapping("/{id}/credit/disable")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Customer>> disableCreditFacility(@PathVariable Long id) {
        log.info("Disabling credit facility for customer: {}", id);
        Customer disabled = customerService.disableCreditFacility(id);
        return ResponseEntity.ok(ApiResponse.success(disabled, "Credit facility disabled successfully"));
    }
    
    @PutMapping("/{id}/credit/update-status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Customer>> updateCreditStatus(@PathVariable Long id) {
        log.info("Updating credit status for customer: {}", id);
        Customer updated = customerService.updateCreditStatus(id);
        return ResponseEntity.ok(ApiResponse.success(updated, "Credit status updated successfully"));
    }
    
    @PutMapping("/{id}/credit/update-outstanding")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> updateCustomerOutstanding(@PathVariable Long id) {
        log.info("Updating outstanding for customer: {}", id);
        customerService.updateCustomerOutstanding(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Customer outstanding updated successfully"));
    }
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<List<Customer>>> getActiveCustomers() {
        List<Customer> customers = customerService.getActiveCustomers();
        return ResponseEntity.ok(ApiResponse.success(customers, "Active customers retrieved successfully"));
    }
    
    @GetMapping("/inactive")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<Customer>>> getInactiveCustomers() {
        List<Customer> customers = customerService.getInactiveCustomers();
        return ResponseEntity.ok(ApiResponse.success(customers, "Inactive customers retrieved successfully"));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Customer>>> getPendingCustomers() {
        List<Customer> customers = customerService.getPendingCustomers();
        return ResponseEntity.ok(ApiResponse.success(customers, "Pending customers retrieved successfully"));
    }
    
    @GetMapping("/suspended")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Customer>>> getSuspendedCustomers() {
        List<Customer> customers = customerService.getSuspendedCustomers();
        return ResponseEntity.ok(ApiResponse.success(customers, "Suspended customers retrieved successfully"));
    }
    
    @GetMapping("/blacklisted")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Customer>>> getBlacklistedCustomers() {
        List<Customer> customers = customerService.getBlacklistedCustomers();
        return ResponseEntity.ok(ApiResponse.success(customers, "Blacklisted customers retrieved successfully"));
    }
    
    @GetMapping("/with-credit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<Customer>>> getCustomersWithCreditFacility() {
        List<Customer> customers = customerService.getCustomersWithCreditFacility();
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers with credit facility retrieved successfully"));
    }
    
    @GetMapping("/credit-exceeded")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Customer>>> getCustomersWithCreditExceeded() {
        List<Customer> customers = customerService.getCustomersWithCreditExceeded();
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers with credit exceeded retrieved successfully"));
    }
    
    @GetMapping("/credit-warning")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Customer>>> getCustomersWithCreditWarning() {
        List<Customer> customers = customerService.getCustomersWithCreditWarning();
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers with credit warning retrieved successfully"));
    }
    
    @GetMapping("/with-outstanding")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Customer>>> getCustomersWithOutstandingBalance() {
        List<Customer> customers = customerService.getCustomersWithOutstandingBalance();
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers with outstanding balance retrieved successfully"));
    }
    
    @GetMapping("/with-overdue")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Customer>>> getCustomersWithOverdueBalance() {
        List<Customer> customers = customerService.getCustomersWithOverdueBalance();
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers with overdue balance retrieved successfully"));
    }
    
    @GetMapping("/unverified")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Customer>>> getUnverifiedCustomers() {
        List<Customer> customers = customerService.getUnverifiedCustomers();
        return ResponseEntity.ok(ApiResponse.success(customers, "Unverified customers retrieved successfully"));
    }
    
    @GetMapping("/without-recent-orders")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<Customer>>> getCustomersWithoutRecentOrders(@RequestParam(defaultValue = "90") int days) {
        List<Customer> customers = customerService.getCustomersWithoutRecentOrders(days);
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers without recent orders retrieved successfully"));
    }
    
    @GetMapping("/requiring-action")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Customer>>> getCustomersRequiringAction() {
        List<Customer> customers = customerService.getCustomersRequiringAction();
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers requiring action retrieved successfully"));
    }
    
    @GetMapping("/type/{customerType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Customer>>> getCustomersByType(@PathVariable String customerType) {
        List<Customer> customers = customerService.getCustomersByType(customerType);
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers by type retrieved successfully"));
    }
    
    @GetMapping("/route/{route}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<Customer>>> getCustomersByRoute(@PathVariable String route) {
        List<Customer> customers = customerService.getCustomersByRoute(route);
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers by route retrieved successfully"));
    }
    
    @GetMapping("/sales-rep/{salesRepId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Page<Customer>>> getCustomersBySalesRep(
        @PathVariable Long salesRepId,
        Pageable pageable
    ) {
        Page<Customer> customers = customerService.getCustomersBySalesRep(salesRepId, pageable);
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers by sales rep retrieved successfully"));
    }
    
    @GetMapping("/sales-rep/{salesRepId}/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<Customer>>> getCustomersBySalesRepList(@PathVariable Long salesRepId) {
        List<Customer> customers = customerService.getCustomersBySalesRep(salesRepId);
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers by sales rep list retrieved successfully"));
    }
    
    @GetMapping("/top")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<Customer>>> getTopCustomers(@RequestParam(defaultValue = "10") int limit) {
        List<Customer> customers = customerService.getTopCustomers(limit);
        return ResponseEntity.ok(ApiResponse.success(customers, "Top customers retrieved successfully"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<Customer>>> getRecentCustomers(@RequestParam(defaultValue = "10") int limit) {
        List<Customer> customers = customerService.getRecentCustomers(limit);
        return ResponseEntity.ok(ApiResponse.success(customers, "Recent customers retrieved successfully"));
    }
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    @GetMapping("/validate/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Boolean>> isEmailAvailable(@PathVariable String email) {
        boolean available = customerService.isEmailAvailable(email);
        return ResponseEntity.ok(ApiResponse.success(available, "Email availability checked"));
    }
    
    @GetMapping("/validate/phone/{phone}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Boolean>> isPhoneAvailable(@PathVariable String phone) {
        boolean available = customerService.isPhoneAvailable(phone);
        return ResponseEntity.ok(ApiResponse.success(available, "Phone availability checked"));
    }
    
    @GetMapping("/validate/code/{customerCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Boolean>> isCustomerCodeAvailable(@PathVariable String customerCode) {
        boolean available = customerService.isCustomerCodeAvailable(customerCode);
        return ResponseEntity.ok(ApiResponse.success(available, "Customer code availability checked"));
    }
    
    @GetMapping("/{id}/can-activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> canActivateCustomer(@PathVariable Long id) {
        boolean canActivate = customerService.canActivateCustomer(id);
        return ResponseEntity.ok(ApiResponse.success(canActivate, "Activation check completed"));
    }
    
    @GetMapping("/{id}/can-suspend")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> canSuspendCustomer(@PathVariable Long id) {
        boolean canSuspend = customerService.canSuspendCustomer(id);
        return ResponseEntity.ok(ApiResponse.success(canSuspend, "Suspension check completed"));
    }
    
    @GetMapping("/{id}/can-enable-credit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> canEnableCreditFacility(@PathVariable Long id) {
        boolean canEnable = customerService.canEnableCreditFacility(id);
        return ResponseEntity.ok(ApiResponse.success(canEnable, "Credit enablement check completed"));
    }
    
    // ===================================================================
    // CALCULATIONS
    // ===================================================================
    
    @PutMapping("/{id}/calculate-metrics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> calculateCustomerMetrics(@PathVariable Long id) {
        log.info("Calculating metrics for customer: {}", id);
        customerService.calculateCustomerMetrics(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Customer metrics calculated successfully"));
    }
    
    @PutMapping("/{id}/update-sales")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> updateTotalSalesAmount(
        @PathVariable Long id,
        @RequestParam Double amount
    ) {
        log.info("Updating total sales for customer: {}", id);
        customerService.updateTotalSalesAmount(id, amount);
        return ResponseEntity.ok(ApiResponse.success(null, "Total sales updated successfully"));
    }
    
    @PutMapping("/{id}/update-last-order")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> updateLastOrderDate(
        @PathVariable Long id,
        @RequestParam LocalDate orderDate
    ) {
        log.info("Updating last order date for customer: {}", id);
        customerService.updateLastOrderDate(id, orderDate);
        return ResponseEntity.ok(ApiResponse.success(null, "Last order date updated successfully"));
    }
    
    @PutMapping("/{id}/update-average-order")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> updateAverageOrderValue(@PathVariable Long id) {
        log.info("Updating average order value for customer: {}", id);
        customerService.updateAverageOrderValue(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Average order value updated successfully"));
    }
    
    @PutMapping("/{id}/calculate-available-credit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> calculateAvailableCredit(@PathVariable Long id) {
        log.info("Calculating available credit for customer: {}", id);
        customerService.calculateAvailableCredit(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Available credit calculated successfully"));
    }
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    @PostMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<Customer>>> createBulkCustomers(@Valid @RequestBody List<CustomerRequest> requests) {
        log.info("Creating {} customers in bulk", requests.size());
        List<Customer> customers = customerService.createBulkCustomers(requests);
        return ResponseEntity.ok(ApiResponse.success(customers, customers.size() + " customers created successfully"));
    }
    
    @PutMapping("/bulk/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> activateBulkCustomers(@RequestBody List<Long> customerIds) {
        log.info("Activating {} customers in bulk", customerIds.size());
        int count = customerService.activateBulkCustomers(customerIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " customers activated successfully"));
    }
    
    @PutMapping("/bulk/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> deactivateBulkCustomers(@RequestBody List<Long> customerIds) {
        log.info("Deactivating {} customers in bulk", customerIds.size());
        int count = customerService.deactivateBulkCustomers(customerIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " customers deactivated successfully"));
    }
    
    @DeleteMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> deleteBulkCustomers(@RequestBody List<Long> customerIds) {
        log.info("Deleting {} customers in bulk", customerIds.size());
        int count = customerService.deleteBulkCustomers(customerIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " customers deleted successfully"));
    }
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCustomerStatistics() {
        Map<String, Object> statistics = customerService.getCustomerStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Customer statistics retrieved successfully"));
    }
    
    @GetMapping("/statistics/type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getCustomerTypeDistribution() {
        List<Map<String, Object>> distribution = customerService.getCustomerTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Customer type distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/status-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getCustomerStatusDistribution() {
        List<Map<String, Object>> distribution = customerService.getCustomerStatusDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Customer status distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/credit-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getCreditStatusDistribution() {
        List<Map<String, Object>> distribution = customerService.getCreditStatusDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Credit status distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/payment-terms-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getPaymentTermsDistribution() {
        List<Map<String, Object>> distribution = customerService.getPaymentTermsDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Payment terms distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/route-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getRouteDistribution() {
        List<Map<String, Object>> distribution = customerService.getRouteDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Route distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/city-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getCityDistribution() {
        List<Map<String, Object>> distribution = customerService.getCityDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "City distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/province-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getProvinceDistribution() {
        List<Map<String, Object>> distribution = customerService.getProvinceDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Province distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/monthly-registration")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMonthlyRegistrationCount() {
        List<Map<String, Object>> count = customerService.getMonthlyRegistrationCount();
        return ResponseEntity.ok(ApiResponse.success(count, "Monthly registration count retrieved successfully"));
    }
    
    @GetMapping("/statistics/by-sales-rep")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getCustomersBySalesRepWithTotals() {
        List<Map<String, Object>> stats = customerService.getCustomersBySalesRepWithTotals();
        return ResponseEntity.ok(ApiResponse.success(stats, "Customers by sales rep with totals retrieved successfully"));
    }
    
    @GetMapping("/statistics/total-sales")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getTotalCustomerSales() {
        Double totalSales = customerService.getTotalCustomerSales();
        return ResponseEntity.ok(ApiResponse.success(totalSales, "Total customer sales retrieved successfully"));
    }
    
    @GetMapping("/statistics/total-outstanding")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getTotalOutstandingBalance() {
        Double totalOutstanding = customerService.getTotalOutstandingBalance();
        return ResponseEntity.ok(ApiResponse.success(totalOutstanding, "Total outstanding balance retrieved successfully"));
    }
    
    @GetMapping("/statistics/total-overdue")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getTotalOverdueAmount() {
        Double totalOverdue = customerService.getTotalOverdueAmount();
        return ResponseEntity.ok(ApiResponse.success(totalOverdue, "Total overdue amount retrieved successfully"));
    }
    
    @GetMapping("/statistics/total-credit-limit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getTotalCreditLimit() {
        Double totalCredit = customerService.getTotalCreditLimit();
        return ResponseEntity.ok(ApiResponse.success(totalCredit, "Total credit limit retrieved successfully"));
    }
    
    @GetMapping("/statistics/total-available-credit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getTotalAvailableCredit() {
        Double totalAvailable = customerService.getTotalAvailableCredit();
        return ResponseEntity.ok(ApiResponse.success(totalAvailable, "Total available credit retrieved successfully"));
    }
    
    @GetMapping("/statistics/average-order-value")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getAverageOrderValue() {
        Double avgOrderValue = customerService.getAverageOrderValue();
        return ResponseEntity.ok(ApiResponse.success(avgOrderValue, "Average order value retrieved successfully"));
    }
    
    @GetMapping("/statistics/credit-utilization-rate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getCreditUtilizationRate() {
        Double utilizationRate = customerService.getCreditUtilizationRate();
        return ResponseEntity.ok(ApiResponse.success(utilizationRate, "Credit utilization rate retrieved successfully"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = customerService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved successfully"));
    }
}
