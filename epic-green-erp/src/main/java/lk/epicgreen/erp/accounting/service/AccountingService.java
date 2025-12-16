package lk.epicgreen.erp.accounting.service;

import lk.epicgreen.erp.accounting.entity.ChartOfAccounts;
import lk.epicgreen.erp.accounting.entity.GeneralLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Accounting Service Interface
 * Service for accounting operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface AccountingService {
    
    // ===================================================================
    // CHART OF ACCOUNTS OPERATIONS
    // ===================================================================
    
    /**
     * Create account
     */
    ChartOfAccounts createAccount(ChartOfAccounts account);
    
    /**
     * Update account
     */
    ChartOfAccounts updateAccount(Long id, ChartOfAccounts account);
    
    /**
     * Delete account
     */
    void deleteAccount(Long id);
    
    /**
     * Get account by ID
     */
    ChartOfAccounts getAccountById(Long id);
    
    /**
     * Get account by code
     */
    ChartOfAccounts getAccountByCode(String accountCode);
    
    /**
     * Get all accounts
     */
    List<ChartOfAccounts> getAllAccounts();
    
    /**
     * Get all accounts with pagination
     */
    Page<ChartOfAccounts> getAllAccounts(Pageable pageable);
    
    /**
     * Get accounts by type
     */
    List<ChartOfAccounts> getAccountsByType(String accountType);
    
    /**
     * Get accounts by category
     */
    List<ChartOfAccounts> getAccountsByCategory(String accountCategory);
    
    /**
     * Get active accounts
     */
    List<ChartOfAccounts> getActiveAccounts();
    
    /**
     * Get posting accounts
     */
    List<ChartOfAccounts> getPostingAccounts();
    
    /**
     * Get root accounts
     */
    List<ChartOfAccounts> getRootAccounts();
    
    /**
     * Get child accounts
     */
    List<ChartOfAccounts> getChildAccounts(Long parentId);
    
    /**
     * Search accounts
     */
    Page<ChartOfAccounts> searchAccounts(String keyword, Pageable pageable);
    
    /**
     * Activate account
     */
    ChartOfAccounts activateAccount(Long id);
    
    /**
     * Deactivate account
     */
    ChartOfAccounts deactivateAccount(Long id);
    
    // ===================================================================
    // ACCOUNT TYPE SPECIFIC OPERATIONS
    // ===================================================================
    
    /**
     * Get bank accounts
     */
    List<ChartOfAccounts> getBankAccounts();
    
    /**
     * Get cash accounts
     */
    List<ChartOfAccounts> getCashAccounts();
    
    /**
     * Get receivable accounts
     */
    List<ChartOfAccounts> getReceivableAccounts();
    
    /**
     * Get payable accounts
     */
    List<ChartOfAccounts> getPayableAccounts();
    
    /**
     * Get revenue accounts
     */
    List<ChartOfAccounts> getRevenueAccounts();
    
    /**
     * Get expense accounts
     */
    List<ChartOfAccounts> getExpenseAccounts();
    
    /**
     * Get asset accounts
     */
    List<ChartOfAccounts> getAssetAccounts();
    
    /**
     * Get liability accounts
     */
    List<ChartOfAccounts> getLiabilityAccounts();
    
    /**
     * Get equity accounts
     */
    List<ChartOfAccounts> getEquityAccounts();
    
    // ===================================================================
    // GENERAL LEDGER OPERATIONS
    // ===================================================================
    
    /**
     * Get account ledger
     */
    List<GeneralLedger> getAccountLedger(Long accountId);
    
    /**
     * Get account ledger with pagination
     */
    Page<GeneralLedger> getAccountLedger(Long accountId, Pageable pageable);
    
    /**
     * Get account ledger for period
     */
    List<GeneralLedger> getAccountLedgerForPeriod(Long accountId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get ledger entries by journal entry
     */
    List<GeneralLedger> getLedgerEntriesByJournalEntry(Long journalEntryId);
    
    /**
     * Get posted ledger entries
     */
    List<GeneralLedger> getPostedLedgerEntries();
    
    /**
     * Get unposted ledger entries
     */
    List<GeneralLedger> getUnpostedLedgerEntries();
    
    /**
     * Search ledger entries
     */
    Page<GeneralLedger> searchLedgerEntries(String keyword, Pageable pageable);
    
    // ===================================================================
    // BALANCE OPERATIONS
    // ===================================================================
    
    /**
     * Get account balance
     */
    Double getAccountBalance(Long accountId);
    
    /**
     * Get account balance for period
     */
    Double getAccountBalanceForPeriod(Long accountId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get account balance up to date
     */
    Double getAccountBalanceUpToDate(Long accountId, LocalDate date);
    
    /**
     * Get total debit for account
     */
    Double getTotalDebitForAccount(Long accountId);
    
    /**
     * Get total credit for account
     */
    Double getTotalCreditForAccount(Long accountId);
    
    /**
     * Update account balances
     */
    void updateAccountBalances(Long accountId);
    
    /**
     * Recalculate all account balances
     */
    void recalculateAllAccountBalances();
    
    // ===================================================================
    // TRIAL BALANCE
    // ===================================================================
    
    /**
     * Get trial balance
     */
    List<Map<String, Object>> getTrialBalance();
    
    /**
     * Get trial balance for period
     */
    List<Map<String, Object>> getTrialBalanceForPeriod(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get trial balance for fiscal year
     */
    List<Map<String, Object>> getTrialBalanceForFiscalYear(Integer year);
    
    /**
     * Verify trial balance
     */
    Map<String, Object> verifyTrialBalance();
    
    // ===================================================================
    // FINANCIAL STATEMENTS
    // ===================================================================
    
    /**
     * Generate balance sheet
     */
    Map<String, Object> generateBalanceSheet(LocalDate asOfDate);
    
    /**
     * Generate income statement
     */
    Map<String, Object> generateIncomeStatement(LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate cash flow statement
     */
    Map<String, Object> generateCashFlowStatement(LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate statement of changes in equity
     */
    Map<String, Object> generateEquityStatement(LocalDate startDate, LocalDate endDate);
    
    // ===================================================================
    // FISCAL PERIOD OPERATIONS
    // ===================================================================
    
    /**
     * Get current fiscal year
     */
    Integer getCurrentFiscalYear();
    
    /**
     * Get current fiscal period
     */
    Integer getCurrentFiscalPeriod();
    
    /**
     * Close fiscal period
     */
    void closeFiscalPeriod(Integer year, Integer period);
    
    /**
     * Close fiscal year
     */
    void closeFiscalYear(Integer year);
    
    /**
     * Reopen fiscal period
     */
    void reopenFiscalPeriod(Integer year, Integer period);
    
    // ===================================================================
    // RECONCILIATION
    // ===================================================================
    
    /**
     * Get accounts requiring reconciliation
     */
    List<ChartOfAccounts> getAccountsRequiringReconciliation();
    
    /**
     * Mark account as reconciled
     */
    void markAccountAsReconciled(Long accountId, LocalDate reconciliationDate);
    
    /**
     * Get account reconciliation status
     */
    Map<String, Object> getAccountReconciliationStatus(Long accountId);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Get accounting statistics
     */
    Map<String, Object> getAccountingStatistics();
    
    /**
     * Get account type distribution
     */
    List<Map<String, Object>> getAccountTypeDistribution();
    
    /**
     * Get account category distribution
     */
    List<Map<String, Object>> getAccountCategoryDistribution();
    
    /**
     * Get most active accounts
     */
    List<Map<String, Object>> getMostActiveAccounts();
    
    /**
     * Get accounts with balance
     */
    List<ChartOfAccounts> getAccountsWithBalance();
    
    /**
     * Get total balance by account type
     */
    List<Map<String, Object>> getTotalBalanceByAccountType();
    
    /**
     * Get dashboard statistics
     */
    Map<String, Object> getDashboardStatistics();
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    /**
     * Validate account code
     */
    boolean validateAccountCode(String accountCode);
    
    /**
     * Check if account has transactions
     */
    boolean hasTransactions(Long accountId);
    
    /**
     * Check if account can be deleted
     */
    boolean canDeleteAccount(Long accountId);
    
    /**
     * Validate account hierarchy
     */
    boolean validateAccountHierarchy(Long parentId, Long childId);
}
