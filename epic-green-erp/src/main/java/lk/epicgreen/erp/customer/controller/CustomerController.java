package lk.epicgreen.erp.customer.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.customer.dto.CustomerRequest;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
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
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Customer>> createCustomer(@Valid @RequestBody CustomerRequest request) {
        Customer created = customerService.createCustomer(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Customer created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Customer>> updateCustomer(
        @PathVariable Long id,
        @Valid @RequestBody CustomerRequest request
    ) {
        Customer updated = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Customer updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {
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
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Page<Customer>>> searchCustomers(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<Customer> customers = customerService.searchCustomers(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(customers, "Search results retrieved"));
    }
    
    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Customer>> activateCustomer(@PathVariable Long id) {
        Customer activated = customerService.activateCustomer(id);
        return ResponseEntity.ok(ApiResponse.success(activated, "Customer activated"));
    }
    
    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Customer>> deactivateCustomer(@PathVariable Long id) {
        Customer deactivated = customerService.deactivateCustomer(id);
        return ResponseEntity.ok(ApiResponse.success(deactivated, "Customer deactivated"));
    }
    
    @PostMapping("/{id}/suspend")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Customer>> suspendCustomer(
        @PathVariable Long id,
        @RequestParam String suspensionReason
    ) {
        Customer suspended = customerService.suspendCustomer(id, suspensionReason);
        return ResponseEntity.ok(ApiResponse.success(suspended, "Customer suspended"));
    }
    
    @PostMapping("/{id}/verify")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Customer>> verifyCustomer(
        @PathVariable Long id,
        @RequestParam Long verifiedByUserId
    ) {
        Customer verified = customerService.verifyCustomer(id, verifiedByUserId);
        return ResponseEntity.ok(ApiResponse.success(verified, "Customer verified"));
    }
    
    @PostMapping("/{id}/blacklist")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Customer>> blacklistCustomer(
        @PathVariable Long id,
        @RequestParam String blacklistReason
    ) {
        Customer blacklisted = customerService.blacklistCustomer(id, blacklistReason);
        return ResponseEntity.ok(ApiResponse.success(blacklisted, "Customer blacklisted"));
    }
    
    @PostMapping("/{id}/remove-blacklist")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Customer>> removeFromBlacklist(@PathVariable Long id) {
        Customer removed = customerService.removeFromBlacklist(id);
        return ResponseEntity.ok(ApiResponse.success(removed, "Customer removed from blacklist"));
    }
    
    @PostMapping("/{id}/enable-credit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Customer>> enableCreditFacility(
        @PathVariable Long id,
        @RequestParam Double creditLimit,
        @RequestParam String paymentTerms
    ) {
        Customer enabled = customerService.enableCreditFacility(id, creditLimit, paymentTerms);
        return ResponseEntity.ok(ApiResponse.success(enabled, "Credit facility enabled"));
    }
    
    @PostMapping("/{id}/update-credit-limit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Customer>> updateCreditLimit(
        @PathVariable Long id,
        @RequestParam Double newCreditLimit
    ) {
        Customer updated = customerService.updateCreditLimit(id, newCreditLimit);
        return ResponseEntity.ok(ApiResponse.success(updated, "Credit limit updated"));
    }
    
    @PostMapping("/{id}/disable-credit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Customer>> disableCreditFacility(@PathVariable Long id) {
        Customer disabled = customerService.disableCreditFacility(id);
        return ResponseEntity.ok(ApiResponse.success(disabled, "Credit facility disabled"));
    }
    
    @GetMapping("/{id}/balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getCustomerBalance(@PathVariable Long id) {
        Double balance = customerService.getCustomerBalance(id);
        return ResponseEntity.ok(ApiResponse.success(balance, "Customer balance retrieved"));
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<Customer>>> getActiveCustomers() {
        List<Customer> customers = customerService.getActiveCustomers();
        return ResponseEntity.ok(ApiResponse.success(customers, "Active customers retrieved"));
    }
    
    @GetMapping("/inactive")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Customer>>> getInactiveCustomers() {
        List<Customer> customers = customerService.getInactiveCustomers();
        return ResponseEntity.ok(ApiResponse.success(customers, "Inactive customers retrieved"));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Customer>>> getPendingCustomers() {
        List<Customer> customers = customerService.getPendingCustomers();
        return ResponseEntity.ok(ApiResponse.success(customers, "Pending customers retrieved"));
    }
    
    @GetMapping("/suspended")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Customer>>> getSuspendedCustomers() {
        List<Customer> customers = customerService.getSuspendedCustomers();
        return ResponseEntity.ok(ApiResponse.success(customers, "Suspended customers retrieved"));
    }
    
    @GetMapping("/blacklisted")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Customer>>> getBlacklistedCustomers() {
        List<Customer> customers = customerService.getBlacklistedCustomers();
        return ResponseEntity.ok(ApiResponse.success(customers, "Blacklisted customers retrieved"));
    }
    
    @GetMapping("/with-credit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Customer>>> getCustomersWithCreditFacility() {
        List<Customer> customers = customerService.getCustomersWithCreditFacility();
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers with credit facility retrieved"));
    }
    
    @GetMapping("/credit-exceeded")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Customer>>> getCustomersWithCreditExceeded() {
        List<Customer> customers = customerService.getCustomersWithCreditExceeded();
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers with credit exceeded retrieved"));
    }
    
    @GetMapping("/credit-warning")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Customer>>> getCustomersWithCreditWarning() {
        List<Customer> customers = customerService.getCustomersWithCreditWarning();
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers with credit warning retrieved"));
    }
    
    @GetMapping("/with-outstanding")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Customer>>> getCustomersWithOutstandingBalance() {
        List<Customer> customers = customerService.getCustomersWithOutstandingBalance();
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers with outstanding balance retrieved"));
    }
    
    @GetMapping("/with-overdue")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Customer>>> getCustomersWithOverdueBalance() {
        List<Customer> customers = customerService.getCustomersWithOverdueBalance();
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers with overdue balance retrieved"));
    }
    
    @GetMapping("/unverified")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Customer>>> getUnverifiedCustomers() {
        List<Customer> customers = customerService.getUnverifiedCustomers();
        return ResponseEntity.ok(ApiResponse.success(customers, "Unverified customers retrieved"));
    }
    
    @GetMapping("/without-recent-orders")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<Customer>>> getCustomersWithoutRecentOrders(
        @RequestParam(defaultValue = "90") int days
    ) {
        List<Customer> customers = customerService.getCustomersWithoutRecentOrders(days);
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers without recent orders retrieved"));
    }
    
    @GetMapping("/requiring-action")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Customer>>> getCustomersRequiringAction() {
        List<Customer> customers = customerService.getCustomersRequiringAction();
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers requiring action retrieved"));
    }
    
    @GetMapping("/by-type/{customerType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<Customer>>> getCustomersByType(@PathVariable String customerType) {
        List<Customer> customers = customerService.getCustomersByType(customerType);
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers by type retrieved"));
    }
    
    @GetMapping("/by-route/{route}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<Customer>>> getCustomersByRoute(@PathVariable String route) {
        List<Customer> customers = customerService.getCustomersByRoute(route);
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers by route retrieved"));
    }
    
    @GetMapping("/by-sales-rep/{salesRepId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Page<Customer>>> getCustomersBySalesRep(
        @PathVariable Long salesRepId,
        Pageable pageable
    ) {
        Page<Customer> customers = customerService.getCustomersBySalesRep(salesRepId, pageable);
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers by sales rep retrieved"));
    }
    
    @GetMapping("/top-customers")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Customer>>> getTopCustomers(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<Customer> customers = customerService.getTopCustomers(limit);
        return ResponseEntity.ok(ApiResponse.success(customers, "Top customers retrieved"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<Customer>>> getRecentCustomers(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<Customer> customers = customerService.getRecentCustomers(limit);
        return ResponseEntity.ok(ApiResponse.success(customers, "Recent customers retrieved"));
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = customerService.getCustomerStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = customerService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
}
