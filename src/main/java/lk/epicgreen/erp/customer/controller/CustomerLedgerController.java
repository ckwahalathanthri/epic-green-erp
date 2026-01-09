package lk.epicgreen.erp.customer.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.customer.dto.request.CustomerLedgerRequest;
import lk.epicgreen.erp.customer.dto.response.CustomerLedgerResponse;
import lk.epicgreen.erp.customer.entity.CustomerLedger;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Customer Ledger Controller
 * REST controller for customer ledger operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/customers/ledger")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class CustomerLedgerController {
    
    private final CustomerService customerService;
    
    // ===================================================================
    // LEDGER OPERATIONS
    // ===================================================================
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<CustomerLedgerResponse>> createLedgerEntry(@Valid @RequestBody CustomerLedgerRequest request) {
        log.info("Creating ledger entry for customer: {}", request.getCustomerId());
        CustomerLedgerResponse created = customerService.createLedgerEntry(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Ledger entry created successfully"));
    }
    
    @PutMapping("/{id}/reverse")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<CustomerLedgerResponse>> reverseLedgerEntry(
        @PathVariable Long id,
        @RequestParam String reversalReason
    ) {
        log.info("Reversing ledger entry: {}", id);
        CustomerLedgerResponse reversed = customerService.reverseLedgerEntry(id, reversalReason);
        return ResponseEntity.ok(ApiResponse.success(reversed, "Ledger entry reversed successfully"));
    }
    
    @PutMapping("/{id}/reconcile")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> reconcileLedgerEntry(@PathVariable Long id) {
        log.info("Reconciling ledger entry: {}", id);
        customerService.reconcileLedgerEntry(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Ledger entry reconciled successfully"));
    }
    
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Page<CustomerLedgerResponse>>> getCustomerLedgerEntries(
        @PathVariable Long customerId,
        Pageable pageable
    ) {
        Page<CustomerLedgerResponse> entries = customerService.getCustomerLedgerEntries(customerId, pageable);
        return ResponseEntity.ok(ApiResponse.success(entries, "Ledger entries retrieved successfully"));
    }
    
    @GetMapping("/customer/{customerId}/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<CustomerLedgerResponse>>> getCustomerLedgerEntriesList(@PathVariable Long customerId) {
        List<CustomerLedgerResponse> entries = customerService.getCustomerLedgerEntries(customerId);
        return ResponseEntity.ok(ApiResponse.success(entries, "Ledger entries list retrieved successfully"));
    }
    
    @GetMapping("/customer/{customerId}/statement")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<CustomerLedgerResponse>>> getCustomerLedgerStatement(
        @PathVariable Long customerId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<CustomerLedgerResponse> statement = customerService.getCustomerLedgerStatement(customerId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(statement, "Ledger statement retrieved successfully"));
    }
    
    @GetMapping("/customer/{customerId}/balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP')")
    public ResponseEntity<ApiResponse<BigDecimal>> getCustomerBalance(@PathVariable Long customerId) {
        BigDecimal balance = customerService.getCustomerBalance(customerId);
        return ResponseEntity.ok(ApiResponse.success(balance, "Customer balance retrieved successfully"));
    }
    
    @GetMapping("/customer/{customerId}/opening-balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getCustomerOpeningBalance(
        @PathVariable Long customerId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
    ) {
        Double balance = customerService.getCustomerOpeningBalance(customerId, startDate);
        return ResponseEntity.ok(ApiResponse.success(balance, "Opening balance retrieved successfully"));
    }
}
