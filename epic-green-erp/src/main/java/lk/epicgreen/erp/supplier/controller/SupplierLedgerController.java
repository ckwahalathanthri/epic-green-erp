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
 * SupplierLedger Controller
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
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<SupplierLedger>> createLedgerEntry(
        @Valid @RequestBody SupplierLedgerRequest request
    ) {
        SupplierLedger created = ledgerService.createLedgerEntry(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Ledger entry created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<SupplierLedger>> updateLedgerEntry(
        @PathVariable Long id,
        @Valid @RequestBody SupplierLedgerRequest request
    ) {
        SupplierLedger updated = ledgerService.updateLedgerEntry(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Ledger entry updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteLedgerEntry(@PathVariable Long id) {
        ledgerService.deleteLedgerEntry(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Ledger entry deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<SupplierLedger>> getLedgerEntryById(@PathVariable Long id) {
        SupplierLedger ledger = ledgerService.getLedgerEntryById(id);
        return ResponseEntity.ok(ApiResponse.success(ledger, "Ledger entry retrieved successfully"));
    }
    
    @GetMapping("/reference/{referenceNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<SupplierLedger>> getLedgerEntryByReferenceNumber(
        @PathVariable String referenceNumber
    ) {
        SupplierLedger ledger = ledgerService.getLedgerEntryByReferenceNumber(referenceNumber);
        return ResponseEntity.ok(ApiResponse.success(ledger, "Ledger entry retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Page<SupplierLedger>>> getAllLedgerEntries(Pageable pageable) {
        Page<SupplierLedger> entries = ledgerService.getAllLedgerEntries(pageable);
        return ResponseEntity.ok(ApiResponse.success(entries, "Ledger entries retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Page<SupplierLedger>>> searchLedgerEntries(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<SupplierLedger> entries = ledgerService.searchLedgerEntries(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(entries, "Search results retrieved"));
    }
    
    @PostMapping("/record-purchase")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<SupplierLedger>> recordPurchase(
        @RequestParam Long supplierId,
        @RequestParam Double amount,
        @RequestParam String referenceType,
        @RequestParam Long referenceId,
        @RequestParam String description,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transactionDate
    ) {
        SupplierLedger entry = ledgerService.recordPurchase(supplierId, amount, referenceType, 
                                                            referenceId, description, transactionDate);
        return ResponseEntity.ok(ApiResponse.success(entry, "Purchase recorded successfully"));
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
        SupplierLedger entry = ledgerService.recordPayment(supplierId, amount, paymentMethod, 
                                                          referenceNumber, description, transactionDate);
        return ResponseEntity.ok(ApiResponse.success(entry, "Payment recorded successfully"));
    }
    
    @PostMapping("/record-return")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<SupplierLedger>> recordReturn(
        @RequestParam Long supplierId,
        @RequestParam Double amount,
        @RequestParam String referenceType,
        @RequestParam Long referenceId,
        @RequestParam String description,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transactionDate
    ) {
        SupplierLedger entry = ledgerService.recordReturn(supplierId, amount, referenceType, 
                                                          referenceId, description, transactionDate);
        return ResponseEntity.ok(ApiResponse.success(entry, "Return recorded successfully"));
    }
    
    @PostMapping("/record-adjustment")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<SupplierLedger>> recordAdjustment(
        @RequestParam Long supplierId,
        @RequestParam Double debitAmount,
        @RequestParam Double creditAmount,
        @RequestParam String description,
        @RequestParam(required = false) String notes,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transactionDate
    ) {
        SupplierLedger entry = ledgerService.recordAdjustment(supplierId, debitAmount, creditAmount, 
                                                              description, notes, transactionDate);
        return ResponseEntity.ok(ApiResponse.success(entry, "Adjustment recorded successfully"));
    }
    
    @GetMapping("/supplier/{supplierId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<SupplierLedger>>> getLedgerEntriesBySupplier(
        @PathVariable Long supplierId,
        Pageable pageable
    ) {
        Page<SupplierLedger> entries = ledgerService.getLedgerEntriesBySupplier(supplierId, pageable);
        return ResponseEntity.ok(ApiResponse.success(entries, "Ledger entries by supplier retrieved"));
    }
    
    @GetMapping("/supplier/{supplierId}/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<SupplierLedger>>> getLedgerEntriesBySupplierAndDateRange(
        @PathVariable Long supplierId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        Pageable pageable
    ) {
        Page<SupplierLedger> entries = ledgerService.getLedgerEntriesBySupplierAndDateRange(
            supplierId, startDate, endDate, pageable);
        return ResponseEntity.ok(ApiResponse.success(entries, "Ledger entries retrieved"));
    }
    
    @GetMapping("/supplier/{supplierId}/debit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getDebitEntriesBySupplier(
        @PathVariable Long supplierId
    ) {
        List<SupplierLedger> entries = ledgerService.getDebitEntriesBySupplier(supplierId);
        return ResponseEntity.ok(ApiResponse.success(entries, "Debit entries retrieved"));
    }
    
    @GetMapping("/supplier/{supplierId}/credit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getCreditEntriesBySupplier(
        @PathVariable Long supplierId
    ) {
        List<SupplierLedger> entries = ledgerService.getCreditEntriesBySupplier(supplierId);
        return ResponseEntity.ok(ApiResponse.success(entries, "Credit entries retrieved"));
    }
    
    @GetMapping("/purchases")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getPurchaseEntries() {
        List<SupplierLedger> entries = ledgerService.getPurchaseEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Purchase entries retrieved"));
    }
    
    @GetMapping("/payments")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getPaymentEntries() {
        List<SupplierLedger> entries = ledgerService.getPaymentEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Payment entries retrieved"));
    }
    
    @GetMapping("/returns")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getReturnEntries() {
        List<SupplierLedger> entries = ledgerService.getReturnEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Return entries retrieved"));
    }
    
    @GetMapping("/adjustments")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getAdjustmentEntries() {
        List<SupplierLedger> entries = ledgerService.getAdjustmentEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Adjustment entries retrieved"));
    }
    
    @GetMapping("/payments/cash")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getCashPayments() {
        List<SupplierLedger> entries = ledgerService.getCashPayments();
        return ResponseEntity.ok(ApiResponse.success(entries, "Cash payments retrieved"));
    }
    
    @GetMapping("/payments/cheque")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getChequePayments() {
        List<SupplierLedger> entries = ledgerService.getChequePayments();
        return ResponseEntity.ok(ApiResponse.success(entries, "Cheque payments retrieved"));
    }
    
    @GetMapping("/payments/bank-transfer")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getBankTransferPayments() {
        List<SupplierLedger> entries = ledgerService.getBankTransferPayments();
        return ResponseEntity.ok(ApiResponse.success(entries, "Bank transfer payments retrieved"));
    }
    
    @GetMapping("/purchases/credit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getCreditPurchases() {
        List<SupplierLedger> entries = ledgerService.getCreditPurchases();
        return ResponseEntity.ok(ApiResponse.success(entries, "Credit purchases retrieved"));
    }
    
    @GetMapping("/supplier/{supplierId}/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getRecentEntriesBySupplier(
        @PathVariable Long supplierId,
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<SupplierLedger> entries = ledgerService.getRecentEntriesBySupplier(supplierId, limit);
        return ResponseEntity.ok(ApiResponse.success(entries, "Recent entries retrieved"));
    }
    
    @GetMapping("/today")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getTodaysEntries() {
        List<SupplierLedger> entries = ledgerService.getTodaysEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Today's entries retrieved"));
    }
    
    @GetMapping("/this-month")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getThisMonthEntries() {
        List<SupplierLedger> entries = ledgerService.getThisMonthEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "This month's entries retrieved"));
    }
    
    @GetMapping("/large-transactions")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getLargeTransactions(
        @RequestParam(defaultValue = "10000") Double threshold
    ) {
        List<SupplierLedger> entries = ledgerService.getLargeTransactions(threshold);
        return ResponseEntity.ok(ApiResponse.success(entries, "Large transactions retrieved"));
    }
    
    @GetMapping("/supplier/{supplierId}/statement")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SupplierLedger>>> getSupplierStatement(
        @PathVariable Long supplierId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<SupplierLedger> statement = ledgerService.getSupplierStatement(supplierId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(statement, "Supplier statement retrieved"));
    }
    
    @GetMapping("/supplier/{supplierId}/statement-summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSupplierStatementSummary(
        @PathVariable Long supplierId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Map<String, Object> summary = ledgerService.getSupplierStatementSummary(supplierId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(summary, "Supplier statement summary retrieved"));
    }
    
    @GetMapping("/supplier/{supplierId}/opening-balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Double>> getOpeningBalance(
        @PathVariable Long supplierId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
    ) {
        Double balance = ledgerService.getOpeningBalance(supplierId, startDate);
        return ResponseEntity.ok(ApiResponse.success(balance, "Opening balance retrieved"));
    }
    
    @GetMapping("/supplier/{supplierId}/total-debit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Double>> getTotalDebitBySupplierId(@PathVariable Long supplierId) {
        Double total = ledgerService.getTotalDebitBySupplierId(supplierId);
        return ResponseEntity.ok(ApiResponse.success(total, "Total debit retrieved"));
    }
    
    @GetMapping("/supplier/{supplierId}/total-credit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Double>> getTotalCreditBySupplierId(@PathVariable Long supplierId) {
        Double total = ledgerService.getTotalCreditBySupplierId(supplierId);
        return ResponseEntity.ok(ApiResponse.success(total, "Total credit retrieved"));
    }
    
    @GetMapping("/supplier/{supplierId}/balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Double>> getBalanceBySupplierId(@PathVariable Long supplierId) {
        Double balance = ledgerService.getBalanceBySupplierId(supplierId);
        return ResponseEntity.ok(ApiResponse.success(balance, "Balance retrieved"));
    }
    
    @GetMapping("/supplier/{supplierId}/total-purchases")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getTotalPurchasesBySupplierId(@PathVariable Long supplierId) {
        Double total = ledgerService.getTotalPurchasesBySupplierId(supplierId);
        return ResponseEntity.ok(ApiResponse.success(total, "Total purchases retrieved"));
    }
    
    @GetMapping("/supplier/{supplierId}/total-payments")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getTotalPaymentsBySupplierId(@PathVariable Long supplierId) {
        Double total = ledgerService.getTotalPaymentsBySupplierId(supplierId);
        return ResponseEntity.ok(ApiResponse.success(total, "Total payments retrieved"));
    }
    
    @GetMapping("/supplier/{supplierId}/total-returns")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getTotalReturnsBySupplierId(@PathVariable Long supplierId) {
        Double total = ledgerService.getTotalReturnsBySupplierId(supplierId);
        return ResponseEntity.ok(ApiResponse.success(total, "Total returns retrieved"));
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = ledgerService.getLedgerStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/transaction-type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTransactionTypeDistribution() {
        List<Map<String, Object>> distribution = ledgerService.getTransactionTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Transaction type distribution retrieved"));
    }
    
    @GetMapping("/statistics/payment-method-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getPaymentMethodDistribution() {
        List<Map<String, Object>> distribution = ledgerService.getPaymentMethodDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Payment method distribution retrieved"));
    }
    
    @GetMapping("/statistics/daily-summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getDailyTransactionSummary(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<Map<String, Object>> summary = ledgerService.getDailyTransactionSummary(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(summary, "Daily transaction summary retrieved"));
    }
    
    @GetMapping("/statistics/monthly-summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMonthlyTransactionSummary() {
        List<Map<String, Object>> summary = ledgerService.getMonthlyTransactionSummary();
        return ResponseEntity.ok(ApiResponse.success(summary, "Monthly transaction summary retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = ledgerService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
}
