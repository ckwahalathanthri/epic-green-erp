package lk.epicgreen.erp.supplier.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.supplier.dto.SupplierLedgerRequest;
import lk.epicgreen.erp.supplier.entity.SupplierLedger;
import lk.epicgreen.erp.supplier.service.SupplierLedgerService;
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
 * Supplier Ledger Controller
 * REST controller for supplier ledger operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/suppliers/ledger")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class SupplierLedgerController {
    
    private final SupplierLedgerService ledgerService;
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<SupplierLedger>> createLedgerEntry(@Valid @RequestBody SupplierLedgerRequest request) {
        log.info("Creating supplier ledger entry for supplier: {}", request.getSupplierId());
        SupplierLedger created = ledgerService.createLedgerEntry(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Ledger entry created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<SupplierLedger>> updateLedgerEntry(
        @PathVariable Long id,
        @Valid @RequestBody SupplierLedgerRequest request
    ) {
        log.info("Updating ledger entry: {}", id);
        SupplierLedger updated = ledgerService.updateLedgerEntry(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Ledger entry updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteLedgerEntry(@PathVariable Long id) {
        log.info("Deleting ledger entry: {}", id);
        ledgerService.deleteLedgerEntry(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Ledger entry deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<SupplierLedger>> getLedgerEntryById(@PathVariable Long id) {
        SupplierLedger ledger = ledgerService.getLedgerEntryById(id);
        return ResponseEntity.ok(ApiResponse.success(ledger, "Ledger entry retrieved successfully"));
    }
    
    @GetMapping("/reference/{referenceNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<SupplierLedger>> getLedgerEntryByReferenceNumber(@PathVariable String referenceNumber) {
        SupplierLedger ledger = ledgerService.getLedgerEntryByReferenceNumber(referenceNumber);
        return ResponseEntity.ok(ApiResponse.success(ledger, "Ledger entry retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Page<SupplierLedger>>> getAllLedgerEntries(Pageable pageable) {
        Page<SupplierLedger> entries = ledgerService.getAllLedgerEntries(pageable);
        return ResponseEntity.ok(ApiResponse.success(entries, "Ledger entries retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getAllLedgerEntriesList() {
        List<SupplierLedger> entries = ledgerService.getAllLedgerEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Ledger entries list retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Page<SupplierLedger>>> searchLedgerEntries(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<SupplierLedger> entries = ledgerService.searchLedgerEntries(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(entries, "Search results retrieved successfully"));
    }
    
    // ===================================================================
    // TRANSACTION OPERATIONS
    // ===================================================================
    
    @PostMapping("/record-purchase")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<SupplierLedger>> recordPurchase(
        @RequestParam Long supplierId,
        @RequestParam Double amount,
        @RequestParam String referenceType,
        @RequestParam Long referenceId,
        @RequestParam String description,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transactionDate
    ) {
        log.info("Recording purchase for supplier: {}", supplierId);
        SupplierLedger ledger = ledgerService.recordPurchase(supplierId, amount, referenceType, referenceId, description, transactionDate);
        return ResponseEntity.ok(ApiResponse.success(ledger, "Purchase recorded successfully"));
    }
    
    @PostMapping("/record-payment")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<SupplierLedger>> recordPayment(
        @RequestParam Long supplierId,
        @RequestParam Double amount,
        @RequestParam String paymentMethod,
        @RequestParam String referenceNumber,
        @RequestParam String description,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transactionDate
    ) {
        log.info("Recording payment for supplier: {}", supplierId);
        SupplierLedger ledger = ledgerService.recordPayment(supplierId, amount, paymentMethod, referenceNumber, description, transactionDate);
        return ResponseEntity.ok(ApiResponse.success(ledger, "Payment recorded successfully"));
    }
    
    @PostMapping("/record-return")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<SupplierLedger>> recordReturn(
        @RequestParam Long supplierId,
        @RequestParam Double amount,
        @RequestParam String referenceType,
        @RequestParam Long referenceId,
        @RequestParam String description,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transactionDate
    ) {
        log.info("Recording return for supplier: {}", supplierId);
        SupplierLedger ledger = ledgerService.recordReturn(supplierId, amount, referenceType, referenceId, description, transactionDate);
        return ResponseEntity.ok(ApiResponse.success(ledger, "Return recorded successfully"));
    }
    
    @PostMapping("/record-adjustment")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<SupplierLedger>> recordAdjustment(
        @RequestParam Long supplierId,
        @RequestParam Double debitAmount,
        @RequestParam Double creditAmount,
        @RequestParam String description,
        @RequestParam String notes,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transactionDate
    ) {
        log.info("Recording adjustment for supplier: {}", supplierId);
        SupplierLedger ledger = ledgerService.recordAdjustment(supplierId, debitAmount, creditAmount, description, notes, transactionDate);
        return ResponseEntity.ok(ApiResponse.success(ledger, "Adjustment recorded successfully"));
    }
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    @GetMapping("/supplier/{supplierId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Page<SupplierLedger>>> getLedgerEntriesBySupplier(
        @PathVariable Long supplierId,
        Pageable pageable
    ) {
        Page<SupplierLedger> entries = ledgerService.getLedgerEntriesBySupplier(supplierId, pageable);
        return ResponseEntity.ok(ApiResponse.success(entries, "Ledger entries retrieved successfully"));
    }
    
    @GetMapping("/supplier/{supplierId}/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getLedgerEntriesBySupplierList(@PathVariable Long supplierId) {
        List<SupplierLedger> entries = ledgerService.getLedgerEntriesBySupplier(supplierId);
        return ResponseEntity.ok(ApiResponse.success(entries, "Ledger entries list retrieved successfully"));
    }
    
    @GetMapping("/supplier/{supplierId}/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Page<SupplierLedger>>> getLedgerEntriesBySupplierAndDateRange(
        @PathVariable Long supplierId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        Pageable pageable
    ) {
        Page<SupplierLedger> entries = ledgerService.getLedgerEntriesBySupplierAndDateRange(supplierId, startDate, endDate, pageable);
        return ResponseEntity.ok(ApiResponse.success(entries, "Ledger entries retrieved successfully"));
    }
    
    @GetMapping("/supplier/{supplierId}/date-range/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getLedgerEntriesBySupplierAndDateRangeList(
        @PathVariable Long supplierId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<SupplierLedger> entries = ledgerService.getLedgerEntriesBySupplierAndDateRange(supplierId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(entries, "Ledger entries list retrieved successfully"));
    }
    
    @GetMapping("/supplier/{supplierId}/debits")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getDebitEntriesBySupplier(@PathVariable Long supplierId) {
        List<SupplierLedger> entries = ledgerService.getDebitEntriesBySupplier(supplierId);
        return ResponseEntity.ok(ApiResponse.success(entries, "Debit entries retrieved successfully"));
    }
    
    @GetMapping("/supplier/{supplierId}/credits")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getCreditEntriesBySupplier(@PathVariable Long supplierId) {
        List<SupplierLedger> entries = ledgerService.getCreditEntriesBySupplier(supplierId);
        return ResponseEntity.ok(ApiResponse.success(entries, "Credit entries retrieved successfully"));
    }
    
    @GetMapping("/purchases")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getPurchaseEntries() {
        List<SupplierLedger> entries = ledgerService.getPurchaseEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Purchase entries retrieved successfully"));
    }
    
    @GetMapping("/payments")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getPaymentEntries() {
        List<SupplierLedger> entries = ledgerService.getPaymentEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Payment entries retrieved successfully"));
    }
    
    @GetMapping("/returns")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getReturnEntries() {
        List<SupplierLedger> entries = ledgerService.getReturnEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Return entries retrieved successfully"));
    }
    
    @GetMapping("/adjustments")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getAdjustmentEntries() {
        List<SupplierLedger> entries = ledgerService.getAdjustmentEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Adjustment entries retrieved successfully"));
    }
    
    @GetMapping("/cash-payments")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getCashPayments() {
        List<SupplierLedger> entries = ledgerService.getCashPayments();
        return ResponseEntity.ok(ApiResponse.success(entries, "Cash payment entries retrieved successfully"));
    }
    
    // ===================================================================
    // BALANCE OPERATIONS
    // ===================================================================
    
    @GetMapping("/supplier/{supplierId}/balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSupplierBalance(@PathVariable Long supplierId) {
        Map<String, Object> balance = ledgerService.getSupplierBalance(supplierId);
        return ResponseEntity.ok(ApiResponse.success(balance, "Supplier balance retrieved successfully"));
    }
    
    @GetMapping("/supplier/{supplierId}/statement")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSupplierStatement(
        @PathVariable Long supplierId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Map<String, Object> statement = ledgerService.getSupplierStatement(supplierId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(statement, "Supplier statement retrieved successfully"));
    }
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getLedgerStatistics() {
        Map<String, Object> statistics = ledgerService.getLedgerStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Ledger statistics retrieved successfully"));
    }
    
    @GetMapping("/statistics/transaction-type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTransactionTypeDistribution() {
        List<Map<String, Object>> distribution = ledgerService.getTransactionTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Transaction type distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/payment-method-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getPaymentMethodDistribution() {
        List<Map<String, Object>> distribution = ledgerService.getPaymentMethodDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Payment method distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/monthly-summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMonthlySummary() {
        List<Map<String, Object>> summary = ledgerService.getMonthlySummary();
        return ResponseEntity.ok(ApiResponse.success(summary, "Monthly summary retrieved successfully"));
    }
    
    @GetMapping("/statistics/total-purchases")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getTotalPurchases() {
        Double totalPurchases = ledgerService.getTotalPurchases();
        return ResponseEntity.ok(ApiResponse.success(totalPurchases, "Total purchases retrieved successfully"));
    }
    
    @GetMapping("/statistics/total-payments")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getTotalPayments() {
        Double totalPayments = ledgerService.getTotalPayments();
        return ResponseEntity.ok(ApiResponse.success(totalPayments, "Total payments retrieved successfully"));
    }
    
    @GetMapping("/statistics/total-outstanding")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getTotalOutstanding() {
        Double totalOutstanding = ledgerService.getTotalOutstanding();
        return ResponseEntity.ok(ApiResponse.success(totalOutstanding, "Total outstanding retrieved successfully"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = ledgerService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved successfully"));
    }
}
