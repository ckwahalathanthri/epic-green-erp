package lk.epicgreen.erp.customer.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.customer.dto.CustomerLedgerRequest;
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
import java.time.LocalDate;
import java.util.List;

/**
 * CustomerLedger Controller
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
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<CustomerLedger>> createLedgerEntry(@Valid @RequestBody CustomerLedgerRequest request) {
        CustomerLedger created = customerService.createLedgerEntry(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Ledger entry created successfully"));
    }
    
    @PostMapping("/{id}/reverse")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<CustomerLedger>> reverseLedgerEntry(
        @PathVariable Long id,
        @RequestParam String reversalReason
    ) {
        CustomerLedger reversed = customerService.reverseLedgerEntry(id, reversalReason);
        return ResponseEntity.ok(ApiResponse.success(reversed, "Ledger entry reversed"));
    }
    
    @PostMapping("/{id}/reconcile")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> reconcileLedgerEntry(@PathVariable Long id) {
        customerService.reconcileLedgerEntry(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Ledger entry reconciled"));
    }
    
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Page<CustomerLedger>>> getCustomerLedgerEntries(
        @PathVariable Long customerId,
        Pageable pageable
    ) {
        Page<CustomerLedger> entries = customerService.getCustomerLedgerEntries(customerId, pageable);
        return ResponseEntity.ok(ApiResponse.success(entries, "Ledger entries retrieved"));
    }
    
    @GetMapping("/customer/{customerId}/statement")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<CustomerLedger>>> getCustomerLedgerStatement(
        @PathVariable Long customerId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<CustomerLedger> statement = customerService.getCustomerLedgerStatement(customerId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(statement, "Ledger statement retrieved"));
    }
    
    @GetMapping("/customer/{customerId}/balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Double>> getCustomerBalance(@PathVariable Long customerId) {
        Double balance = customerService.getCustomerBalance(customerId);
        return ResponseEntity.ok(ApiResponse.success(balance, "Customer balance retrieved"));
    }
    
    @GetMapping("/customer/{customerId}/opening-balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getCustomerOpeningBalance(
        @PathVariable Long customerId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
    ) {
        Double balance = customerService.getCustomerOpeningBalance(customerId, startDate);
        return ResponseEntity.ok(ApiResponse.success(balance, "Opening balance retrieved"));
    }
}
