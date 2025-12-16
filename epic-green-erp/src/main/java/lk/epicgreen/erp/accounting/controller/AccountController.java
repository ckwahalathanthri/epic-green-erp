package lk.epicgreen.erp.accounting.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.accounting.entity.ChartOfAccounts;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Account Controller
 * REST controller for chart of accounts operations
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
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<ChartOfAccounts>> createAccount(
        @Valid @RequestBody ChartOfAccounts account
    ) {
        ChartOfAccounts created = accountingService.createAccount(account);
        return ResponseEntity.ok(ApiResponse.success(created, "Account created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<ChartOfAccounts>> updateAccount(
        @PathVariable Long id,
        @Valid @RequestBody ChartOfAccounts account
    ) {
        ChartOfAccounts updated = accountingService.updateAccount(id, account);
        return ResponseEntity.ok(ApiResponse.success(updated, "Account updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(@PathVariable Long id) {
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
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<ChartOfAccounts>>> searchAccounts(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<ChartOfAccounts> accounts = accountingService.searchAccounts(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(accounts, "Search results retrieved"));
    }
    
    // ===================================================================
    // ACCOUNT TYPE OPERATIONS
    // ===================================================================
    
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
        return ResponseEntity.ok(ApiResponse.success(accounts, "Active accounts retrieved"));
    }
    
    @GetMapping("/posting")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getPostingAccounts() {
        List<ChartOfAccounts> accounts = accountingService.getPostingAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Posting accounts retrieved"));
    }
    
    @GetMapping("/bank")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getBankAccounts() {
        List<ChartOfAccounts> accounts = accountingService.getBankAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Bank accounts retrieved"));
    }
    
    @GetMapping("/cash")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getCashAccounts() {
        List<ChartOfAccounts> accounts = accountingService.getCashAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Cash accounts retrieved"));
    }
    
    @GetMapping("/receivable")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getReceivableAccounts() {
        List<ChartOfAccounts> accounts = accountingService.getReceivableAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Receivable accounts retrieved"));
    }
    
    @GetMapping("/payable")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getPayableAccounts() {
        List<ChartOfAccounts> accounts = accountingService.getPayableAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Payable accounts retrieved"));
    }
    
    @GetMapping("/revenue")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getRevenueAccounts() {
        List<ChartOfAccounts> accounts = accountingService.getRevenueAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Revenue accounts retrieved"));
    }
    
    @GetMapping("/expense")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ChartOfAccounts>>> getExpenseAccounts() {
        List<ChartOfAccounts> accounts = accountingService.getExpenseAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts, "Expense accounts retrieved"));
    }
    
    // ===================================================================
    // BALANCE OPERATIONS
    // ===================================================================
    
    @GetMapping("/{id}/balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getAccountBalance(@PathVariable Long id) {
        Double balance = accountingService.getAccountBalance(id);
        return ResponseEntity.ok(ApiResponse.success(balance, "Balance retrieved"));
    }
    
    @GetMapping("/{id}/balance/period")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getAccountBalanceForPeriod(
        @PathVariable Long id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Double balance = accountingService.getAccountBalanceForPeriod(id, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(balance, "Period balance retrieved"));
    }
    
    @PostMapping("/{id}/recalculate-balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> updateAccountBalances(@PathVariable Long id) {
        accountingService.updateAccountBalances(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Balance recalculated"));
    }
    
    // ===================================================================
    // TRIAL BALANCE
    // ===================================================================
    
    @GetMapping("/trial-balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTrialBalance() {
        List<Map<String, Object>> trialBalance = accountingService.getTrialBalance();
        return ResponseEntity.ok(ApiResponse.success(trialBalance, "Trial balance retrieved"));
    }
    
    @GetMapping("/trial-balance/period")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTrialBalanceForPeriod(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<Map<String, Object>> trialBalance = accountingService.getTrialBalanceForPeriod(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(trialBalance, "Period trial balance retrieved"));
    }
    
    @GetMapping("/trial-balance/verify")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> verifyTrialBalance() {
        Map<String, Object> verification = accountingService.verifyTrialBalance();
        return ResponseEntity.ok(ApiResponse.success(verification, "Trial balance verified"));
    }
    
    // ===================================================================
    // FINANCIAL STATEMENTS
    // ===================================================================
    
    @GetMapping("/balance-sheet")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateBalanceSheet(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate
    ) {
        Map<String, Object> balanceSheet = accountingService.generateBalanceSheet(asOfDate);
        return ResponseEntity.ok(ApiResponse.success(balanceSheet, "Balance sheet generated"));
    }
    
    @GetMapping("/income-statement")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateIncomeStatement(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Map<String, Object> incomeStatement = accountingService.generateIncomeStatement(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(incomeStatement, "Income statement generated"));
    }
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = accountingService.getAccountingStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = accountingService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<ChartOfAccounts>> activateAccount(@PathVariable Long id) {
        ChartOfAccounts account = accountingService.activateAccount(id);
        return ResponseEntity.ok(ApiResponse.success(account, "Account activated"));
    }
    
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<ChartOfAccounts>> deactivateAccount(@PathVariable Long id) {
        ChartOfAccounts account = accountingService.deactivateAccount(id);
        return ResponseEntity.ok(ApiResponse.success(account, "Account deactivated"));
    }
}
