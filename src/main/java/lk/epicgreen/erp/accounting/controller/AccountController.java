package lk.epicgreen.erp.accounting.controller;

import lk.epicgreen.erp.accounting.service.ChartOfAccountsService;
import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.accounting.entity.ChartOfAccounts;
import lk.epicgreen.erp.accounting.entity.GeneralLedger;
import lk.epicgreen.erp.accounting.service.AccountingService;
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
import java.util.Map;

/**
 * Account Controller
 * REST controller for chart of accounts and ledger operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/accounting/accounts")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class AccountController {
    
    private final AccountingService accountingService;

    private final ChartOfAccountsService chartOfAccountsService;

    // CRUD Operations
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<ChartOfAccounts>> createAccount(@Valid @RequestBody ChartOfAccounts account) {
        log.info("Creating account: {}", account.getAccountCode());
        ChartOfAccounts created = accountingService.createAccount(account);
        return ResponseEntity.ok(ApiResponse.success(created, "Account created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<ChartOfAccounts>> updateAccount(@PathVariable Long id, @Valid @RequestBody ChartOfAccounts account) {
        log.info("Updating account: {}", id);
        ChartOfAccounts updated = accountingService.updateAccount(id, account);
        return ResponseEntity.ok(ApiResponse.success(updated, "Account updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(@PathVariable Long id) {
        log.info("Deleting account: {}", id);
        accountingService.deleteAccount(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Account deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<ChartOfAccounts>> getAccountById(@PathVariable Long id) {
        ChartOfAccounts account = accountingService.getAccountById(id);
        return ResponseEntity.ok(ApiResponse.success(account, "Account retrieved successfully"));
    }
    
    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<ChartOfAccounts>> getAccountByCode(@PathVariable String code) {
        ChartOfAccounts account = accountingService.getAccountByCode(code);
        return ResponseEntity.ok(ApiResponse.success(account, "Account retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<ChartOfAccounts>>> getAllAccounts(Pageable pageable) {
        Page<ChartOfAccounts> accounts = accountingService.getAllAccounts(pageable);
        return ResponseEntity.ok(ApiResponse.success(accounts, "Accounts retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getAllAccountsList() {
        List<ChartOfAccounts> accounts = accountingService.getAllAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Accounts list retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<ChartOfAccounts>>> searchAccounts(@RequestParam String keyword, Pageable pageable) {
        Page<ChartOfAccounts> accounts = accountingService.searchAccounts(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(accounts, "Search results retrieved successfully"));
    }
    
    // Status Operations
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<ChartOfAccounts>> activateAccount(@PathVariable Long id) {
        log.info("Activating account: {}", id);
        ChartOfAccounts activated = accountingService.activateAccount(id);
        return ResponseEntity.ok(ApiResponse.success(activated, "Account activated successfully"));
    }
    
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<ChartOfAccounts>> deactivateAccount(@PathVariable Long id) {
        log.info("Deactivating account: {}", id);
        ChartOfAccounts deactivated = accountingService.deactivateAccount(id);
        return ResponseEntity.ok(ApiResponse.success(deactivated, "Account deactivated successfully"));
    }
    
    // Account Type Queries
    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getAccountsByType(@PathVariable String type) {
        List<ChartOfAccounts> accounts = accountingService.getAccountsByType(type);
        return ResponseEntity.ok(ApiResponse.success(accounts, "Accounts retrieved by type"));
    }
    
    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getAccountsByCategory(@PathVariable String category) {
        List<ChartOfAccounts> accounts = accountingService.getAccountsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(accounts, "Accounts retrieved by category"));
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getActiveAccounts() {
        List<ChartOfAccounts> accounts = accountingService.getActiveAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Active accounts retrieved successfully"));
    }
    
    @GetMapping("/posting")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getPostingAccounts() {
        List<ChartOfAccounts> accounts = accountingService.getPostingAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Posting accounts retrieved successfully"));
    }
    
    @GetMapping("/bank")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getBankAccounts() {
        List<ChartOfAccounts> accounts = accountingService.getBankAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Bank accounts retrieved successfully"));
    }
    
    @GetMapping("/cash")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getCashAccounts() {
        List<ChartOfAccounts> accounts = accountingService.getCashAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Cash accounts retrieved successfully"));
    }
    
    @GetMapping("/receivable")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getReceivableAccounts() {
        List<ChartOfAccounts> accounts = accountingService.getReceivableAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Receivable accounts retrieved successfully"));
    }
    
    @GetMapping("/payable")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getPayableAccounts() {
        List<ChartOfAccounts> accounts = accountingService.getPayableAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Payable accounts retrieved successfully"));
    }
    
    @GetMapping("/revenue")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getRevenueAccounts() {
        List<ChartOfAccounts> accounts = accountingService.getRevenueAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Revenue accounts retrieved successfully"));
    }
    
    @GetMapping("/expense")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getExpenseAccounts() {
        List<ChartOfAccounts> accounts = accountingService.getExpenseAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Expense accounts retrieved successfully"));
    }
    
    @GetMapping("/asset")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getAssetAccounts() {
        List<ChartOfAccounts> accounts = accountingService.getAssetAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Asset accounts retrieved successfully"));
    }
    
    @GetMapping("/liability")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getLiabilityAccounts() {
        List<ChartOfAccounts> accounts = accountingService.getLiabilityAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Liability accounts retrieved successfully"));
    }
    
    @GetMapping("/equity")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getEquityAccounts() {
        List<ChartOfAccounts> accounts = accountingService.getEquityAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Equity accounts retrieved successfully"));
    }
    
    // General Ledger Operations
    @GetMapping("/{id}/ledger")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<GeneralLedger>>> getAccountLedger(@PathVariable Long id) {
        List<GeneralLedger> ledger = accountingService.getAccountLedger(id);
        return ResponseEntity.ok(ApiResponse.success(ledger, "Account ledger retrieved successfully"));
    }
    
    @GetMapping("/{id}/ledger/period")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<GeneralLedger>>> getAccountLedgerForPeriod(
        @PathVariable Long id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<GeneralLedger> ledger = accountingService.getAccountLedgerForPeriod(id, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(ledger, "Period ledger retrieved successfully"));
    }
    
    @GetMapping("/ledger/posted")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<GeneralLedger>>> getPostedLedgerEntries() {
        List<GeneralLedger> entries = accountingService.getPostedLedgerEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Posted ledger entries retrieved successfully"));
    }
    
    @GetMapping("/ledger/unposted")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<GeneralLedger>>> getUnpostedLedgerEntries() {
        List<GeneralLedger> entries = accountingService.getUnpostedLedgerEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Unposted ledger entries retrieved successfully"));
    }
    
    @GetMapping("/ledger/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<GeneralLedger>>> searchLedgerEntries(@RequestParam String keyword, Pageable pageable) {
        Page<GeneralLedger> entries = accountingService.searchLedgerEntries(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(entries, "Ledger search results retrieved successfully"));
    }
    
    // Balance Operations
    @GetMapping("/{id}/balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getAccountBalance(@PathVariable Long id) {
        BigDecimal balance = accountingService.getAccountBalance(id);
        return ResponseEntity.ok(ApiResponse.success(balance.doubleValue(), "Account balance retrieved successfully"));
    }
    
    @GetMapping("/{id}/balance/period")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getAccountBalanceForPeriod(
        @PathVariable Long id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Double balance = accountingService.getAccountBalanceForPeriod(id, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(balance, "Period balance retrieved successfully"));
    }
    
    @GetMapping("/{id}/balance/up-to-date")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getAccountBalanceUpToDate(
        @PathVariable Long id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        Double balance = accountingService.getAccountBalanceUpToDate(id, date);
        return ResponseEntity.ok(ApiResponse.success(balance, "Balance up to date retrieved successfully"));
    }
    
    @GetMapping("/{id}/total-debit")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getTotalDebitForAccount(@PathVariable Long id) {
        Double debit = accountingService.getTotalDebitForAccount(id);
        return ResponseEntity.ok(ApiResponse.success(debit, "Total debit retrieved successfully"));
    }
    
    @GetMapping("/{id}/total-credit")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getTotalCreditForAccount(@PathVariable Long id) {
        Double credit = accountingService.getTotalCreditForAccount(id);
        return ResponseEntity.ok(ApiResponse.success(credit, "Total credit retrieved successfully"));
    }
    
    @PutMapping("/{id}/recalculate-balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> updateAccountBalances(@PathVariable Long id) {
        log.info("Recalculating balance for account: {}", id);
        accountingService.updateAccountBalances(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Account balance recalculated successfully"));
    }
    
    @PutMapping("/recalculate-all-balances")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> recalculateAllAccountBalances() {
        log.info("Recalculating all account balances");
        accountingService.recalculateAllAccountBalances();
        return ResponseEntity.ok(ApiResponse.success(null, "All account balances recalculated successfully"));
    }
    
    // Trial Balance
    @GetMapping("/trial-balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTrialBalance() {
        List<Map<String, Object>> trialBalance = accountingService.getTrialBalance();
        return ResponseEntity.ok(ApiResponse.success(trialBalance, "Trial balance retrieved successfully"));
    }
    
    @GetMapping("/trial-balance/period")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTrialBalanceForPeriod(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<Map<String, Object>> trialBalance = accountingService.getTrialBalanceForPeriod(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(trialBalance, "Period trial balance retrieved successfully"));
    }
    
    @GetMapping("/trial-balance/fiscal-year/{year}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTrialBalanceForFiscalYear(@PathVariable Integer year) {
        List<Map<String, Object>> trialBalance = accountingService.getTrialBalanceForFiscalYear(year);
        return ResponseEntity.ok(ApiResponse.success(trialBalance, "Fiscal year trial balance retrieved successfully"));
    }
    
    @GetMapping("/trial-balance/verify")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> verifyTrialBalance() {
        Map<String, Object> verification = accountingService.verifyTrialBalance();
        return ResponseEntity.ok(ApiResponse.success(verification, "Trial balance verified successfully"));
    }
    
    // Financial Statements
    @GetMapping("/balance-sheet")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateBalanceSheet(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
        Map<String, Object> balanceSheet = accountingService.generateBalanceSheet(asOfDate);
        return ResponseEntity.ok(ApiResponse.success(balanceSheet, "Balance sheet generated successfully"));
    }
    
    @GetMapping("/income-statement")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateIncomeStatement(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Map<String, Object> incomeStatement = accountingService.generateIncomeStatement(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(incomeStatement, "Income statement generated successfully"));
    }
    
    @GetMapping("/cash-flow-statement")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateCashFlowStatement(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Map<String, Object> cashFlowStatement = accountingService.generateCashFlowStatement(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(cashFlowStatement, "Cash flow statement generated successfully"));
    }
    
    @GetMapping("/equity-statement")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateEquityStatement(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Map<String, Object> equityStatement = accountingService.generateEquityStatement(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(equityStatement, "Equity statement generated successfully"));
    }
    
    // Fiscal Period Operations
    @GetMapping("/fiscal-year/current")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Integer>> getCurrentFiscalYear() {
        Integer year = accountingService.getCurrentFiscalYear();
        return ResponseEntity.ok(ApiResponse.success(year, "Current fiscal year retrieved successfully"));
    }
    
    @GetMapping("/fiscal-period/current")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Integer>> getCurrentFiscalPeriod() {
        Integer period = accountingService.getCurrentFiscalPeriod();
        return ResponseEntity.ok(ApiResponse.success(period, "Current fiscal period retrieved successfully"));
    }
    
    @PutMapping("/fiscal-period/close")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> closeFiscalPeriod(@RequestParam Integer year, @RequestParam Integer period) {
        log.info("Closing fiscal period: {}-{}", year, period);
        accountingService.closeFiscalPeriod(year, period);
        return ResponseEntity.ok(ApiResponse.success(null, "Fiscal period closed successfully"));
    }
    
    @PutMapping("/fiscal-year/close")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> closeFiscalYear(@RequestParam Integer year) {
        log.info("Closing fiscal year: {}", year);
        accountingService.closeFiscalYear(year);
        return ResponseEntity.ok(ApiResponse.success(null, "Fiscal year closed successfully"));
    }
    
    @PutMapping("/fiscal-period/reopen")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> reopenFiscalPeriod(@RequestParam Integer year, @RequestParam Integer period) {
        log.info("Reopening fiscal period: {}-{}", year, period);
        accountingService.reopenFiscalPeriod(year, period);
        return ResponseEntity.ok(ApiResponse.success(null, "Fiscal period reopened successfully"));
    }
    
    // Reconciliation
    @GetMapping("/reconciliation/requiring")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getAccountsRequiringReconciliation() {
        List<ChartOfAccounts> accounts = accountingService.getAccountsRequiringReconciliation();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Accounts requiring reconciliation retrieved successfully"));
    }
    
    @PutMapping("/{id}/reconciliation/mark")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> markAccountAsReconciled(
        @PathVariable Long id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reconciliationDate
    ) {
        log.info("Marking account {} as reconciled", id);
        accountingService.markAccountAsReconciled(id, reconciliationDate);
        return ResponseEntity.ok(ApiResponse.success(null, "Account marked as reconciled"));
    }
    
    @GetMapping("/{id}/reconciliation/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAccountReconciliationStatus(@PathVariable Long id) {
        Map<String, Object> status = accountingService.getAccountReconciliationStatus(id);
        return ResponseEntity.ok(ApiResponse.success(status, "Reconciliation status retrieved successfully"));
    }
    
    // Statistics
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAccountingStatistics() {
        Map<String, Object> statistics = accountingService.getAccountingStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Accounting statistics retrieved successfully"));
    }
    
    @GetMapping("/statistics/type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAccountTypeDistribution() {
        List<Map<String, Object>> distribution = accountingService.getAccountTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Account type distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/category-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAccountCategoryDistribution() {
        List<Map<String, Object>> distribution = accountingService.getAccountCategoryDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Account category distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/most-active")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMostActiveAccounts() {
        List<Map<String, Object>> accounts = accountingService.getMostActiveAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Most active accounts retrieved successfully"));
    }
    
    @GetMapping("/statistics/with-balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getAccountsWithBalance() {
        List<ChartOfAccounts> accounts = accountingService.getAccountsWithBalance();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Accounts with balance retrieved successfully"));
    }
    
    @GetMapping("/statistics/balance-by-type")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTotalBalanceByAccountType() {
        List<Map<String, Object>> balances = accountingService.getTotalBalanceByAccountType();
        return ResponseEntity.ok(ApiResponse.success(balances, "Total balance by type retrieved successfully"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = accountingService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved successfully"));
    }
    
    // Validation
    @GetMapping("/validate-code/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Boolean>> isAccountCodeAvailable(@PathVariable String code) {
        boolean available = accountingService.isAccountCodeAvailable(code);
        return ResponseEntity.ok(ApiResponse.success(available, "Account code availability checked"));
    }
    
    @GetMapping("/{id}/has-transactions")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Boolean>> hasAccountTransactions(@PathVariable Long id) {
        boolean hasTransactions = accountingService.hasAccountTransactions(id);
        return ResponseEntity.ok(ApiResponse.success(hasTransactions, "Transaction check completed"));
    }
    
    @GetMapping("/{id}/can-delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Boolean>> canDeleteAccount(@PathVariable Long id) {
        boolean canDelete = accountingService.canDeleteAccount(id);
        return ResponseEntity.ok(ApiResponse.success(canDelete, "Delete check completed"));
    }
    
    @GetMapping("/{id}/validate-hierarchy")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Boolean>> validateAccountHierarchy(@PathVariable Long id) {
        boolean valid = accountingService.validateAccountHierarchy(id);
        return ResponseEntity.ok(ApiResponse.success(valid, "Hierarchy validation completed"));
    }
}
