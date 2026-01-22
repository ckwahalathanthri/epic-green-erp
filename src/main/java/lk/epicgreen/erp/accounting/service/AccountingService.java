package lk.epicgreen.erp.accounting.service;

import lk.epicgreen.erp.accounting.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Unified Accounting Service Interface
 * Aggregates all accounting-related services
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface AccountingService {
    
    // ===================================================================
    // CHART OF ACCOUNTS
    // ===================================================================
    
    ChartOfAccounts createAccount(ChartOfAccounts account);
    ChartOfAccounts updateAccount(Long id, ChartOfAccounts account);
    void deleteAccount(Long id);
    ChartOfAccounts getAccountById(Long id);
    ChartOfAccounts getAccountByCode(String code);
    Page<ChartOfAccounts> getAllAccounts(Pageable pageable);
    List<ChartOfAccounts> getAllAccounts();
    Page<ChartOfAccounts> searchAccounts(String keyword, Pageable pageable);
    ChartOfAccounts activateAccount(Long id);
    ChartOfAccounts deactivateAccount(Long id);
    List<ChartOfAccounts> getAccountsByType(String type);
    List<ChartOfAccounts> getAccountsByCategory(String category);
    List<ChartOfAccounts> getActiveAccounts();
    List<ChartOfAccounts> getInactiveAccounts();
    List<ChartOfAccounts> getParentAccounts();
    List<ChartOfAccounts> getChildAccounts(Long parentId);
    BigDecimal getAccountBalance(Long accountId);

    Map<String, Object> getAccountSummary(Long accountId);

    BankReconciliation markAsReconciled(Long reconciliationId);
    Map<String, Object> getAccountReconciliationStatus(Long accountId);
    
    // ===================================================================
    // FINANCIAL PERIODS
    // ===================================================================
    
    FinancialPeriod createPeriod(FinancialPeriod period);
    FinancialPeriod updatePeriod(Long id, FinancialPeriod period);

    void deletePeriod(Long id);
    FinancialPeriod getPeriodById(Long id);
    FinancialPeriod getPeriodByCode(String code);
    Page<FinancialPeriod> getAllPeriods(Pageable pageable);
    List<FinancialPeriod> getAllPeriods();
    FinancialPeriod getCurrentPeriod();
    List<FinancialPeriod> getOpenPeriods();
    List<FinancialPeriod> getClosedPeriods();
    FinancialPeriod openPeriod(Long periodId);
    FinancialPeriod closePeriod(Long periodId);

    // ===================================================================
    // JOURNAL ENTRIES
    // ===================================================================

    JournalEntry createJournalEntry(JournalEntry journalEntry);
    JournalEntry updateJournalEntry(Long id, JournalEntry journalEntry);
    void deleteJournalEntry(Long id);
    JournalEntry getJournalEntryById(Long id);
    JournalEntry getJournalEntryByNumber(String number);
    Page<JournalEntry> getAllJournalEntries(Pageable pageable);
    List<JournalEntry> getAllJournalEntries();
    Page<JournalEntry> searchJournalEntries(String keyword, Pageable pageable);
    JournalEntry postJournalEntry(Long journalEntryId);

    JournalEntry reverseJournalEntry(Long journalEntryId);
    List<JournalEntry> getJournalEntriesByPeriod(Long periodId);
    List<JournalEntry> getPostedJournalEntries();
    List<JournalEntry> getUnpostedJournalEntries();
    
    // ===================================================================
    // GENERAL LEDGER
    // ===================================================================

    List<GeneralLedger> getAccountLedger(Long accountId);
    List<GeneralLedger> getAccountLedgerForPeriod(Long accountId, LocalDate startDate, LocalDate endDate);
    List<GeneralLedger> getLedgerEntriesByJournalEntry(Long journalEntryId);
    List<GeneralLedger> getPostedLedgerEntries();
    List<GeneralLedger> getUnpostedLedgerEntries();

    Page<GeneralLedger> searchLedgerEntries(String keyword, Pageable pageable);
    
    // ===================================================================
    // BANK ACCOUNTS
    // ===================================================================

    BankAccount createBankAccount(BankAccount bankAccount);
    BankAccount updateBankAccount(Long id,BankAccount bankAccount);
    void deleteBankAccount( Long id);
    BankAccount getBankAccountById(Long id);
    BankAccount getBankAccountByNumber(String accountNumber);
    Page<BankAccount> getAllBankAccounts(Pageable pageable);
    List<BankAccount> getAllBankAccounts();
    Page<BankAccount> searchBankAccounts(String keyword, Pageable pageable);
    BankAccount activateBankAccount(Long id);
    BankAccount deactivateBankAccount(Long id);

    List<BankAccount> getActiveBankAccounts();
    BigDecimal getBankAccountBalance(@Param("bankAccountId") Long bankAccountId);
    
    // ===================================================================
    // BANK RECONCILIATION
    // ===================================================================
    
    BankReconciliation createReconciliation(BankReconciliation reconciliation);
    BankReconciliation updateReconciliation(Long id, BankReconciliation reconciliation);
    void deleteReconciliation(Long id);
    BankReconciliation getReconciliationById(Long id);
    Page<BankReconciliation> getAllReconciliations(Pageable pageable);
    List<BankReconciliation> getAllReconciliations();
    List<BankReconciliation> getReconciliationsByBankAccount(Long bankAccountId);
    List<BankReconciliation> getReconciledReconciliations();
    List<BankReconciliation> getUnreconciledReconciliations();
//    BankReconciliation markAsReconciled(Long reconciliationId);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    Map<String, Object> getAccountingStatistics();
    List<Map<String, Object>> getAccountTypeDistribution();
    List<Map<String, Object>> getAccountCategoryDistribution();
    List<Map<String, Object>> getMostActiveAccounts();
    List<ChartOfAccounts> getAccountsWithBalance();
    Map<String, Object> getJournalEntryStatistics();
    Map<String, Object> getDashboardStatistics();

//    List<ChartOfAccounts> getPostingAccounts();

    List<ChartOfAccounts> getBankAccounts();

//    List<ChartOfAccounts> getCashAccounts();

//    List<ChartOfAccounts> getReceivableAccounts();

    List<ChartOfAccounts> getPayableAccounts();

    List<ChartOfAccounts> getRevenueAccounts();

    List<ChartOfAccounts> getExpenseAccounts();

    List<ChartOfAccounts> getAssetAccounts();

    List<ChartOfAccounts> getLiabilityAccounts();

    List<ChartOfAccounts> getEquityAccounts();

    Double getAccountBalanceForPeriod(Long id, LocalDate startDate, LocalDate endDate);

    Double getAccountBalanceUpToDate(Long id, LocalDate date);

    Double getTotalDebitForAccount(Long id);

    Double getTotalCreditForAccount(Long id);

    List<ChartOfAccounts> getPostingAccounts();

    List<ChartOfAccounts> getCashAccounts();

    List<ChartOfAccounts> getReceivableAccounts();

    boolean validateAccountHierarchy(Long id);

    boolean canDeleteAccount(Long id);

    List<Map<String, Object>> getTotalBalanceByAccountType();

    void markAccountAsReconciled(Long id, LocalDate reconciliationDate);

    void updateAccountBalances(Long id);

    List<Map<String, Object>> getTrialBalance();

    List<Map<String, Object>> getTrialBalanceForPeriod(LocalDate startDate, LocalDate endDate);

    List<Map<String, Object>> getTrialBalanceForFiscalYear(Integer year);

    boolean hasAccountTransactions(Long id);

    @NotBlank(message = "Period code is required") @Size(max = 20) String getCurrentFiscalPeriod();

    Integer getCurrentFiscalYear();

    Map<String, Object> generateBalanceSheet(LocalDate asOfDate);

    Map<String, Object> generateIncomeStatement(LocalDate startDate, LocalDate endDate);

    Map<String, Object> generateCashFlowStatement(LocalDate startDate, LocalDate endDate);

    Map<String, Object> generateEquityStatement(LocalDate startDate, LocalDate endDate);

//    void closeFiscalYear(Integer year);

//    void reopenFiscalPeriod(Integer year, Integer period);

//    List<ChartOfAccounts> getAccountsRequiringReconciliation();

//    boolean isAccountCodeAvailable(String code);

//    void updateAccountBalances(Long id);
//
//    void recalculateAllAccountBalances();

//    List<Map<String, Object>> getTrialBalance();

//    List<Map<String, Object>> getTrialBalanceForPeriod(LocalDate startDate, LocalDate endDate);
//
//    List<Map<String, Object>> getTrialBalanceForFiscalYear(Integer year);
//
//    Map<String, Object> verifyTrialBalance();
//
//    Map<String, Object> generateBalanceSheet(LocalDate asOfDate);
//
//    Map<String, Object> generateIncomeStatement(LocalDate startDate, LocalDate endDate);
//
//    Map<String, Object> generateCashFlowStatement(LocalDate startDate, LocalDate endDate);
//
//    Map<String, Object> generateEquityStatement(LocalDate startDate, LocalDate endDate);
//
//    Integer getCurrentFiscalYear();
//
//    Integer getCurrentFiscalPeriod();
//
//    void closeFiscalPeriod(Integer year, Integer period);
//
//    void closeFiscalYear(Integer year);
//
//    void reopenFiscalPeriod(Integer year, Integer period);
//
//    List<ChartOfAccounts> getAccountsRequiringReconciliation();
//
//    void markAccountAsReconciled(Long id, LocalDate reconciliationDate);
//
//    List<Map<String, Object>> getTotalBalanceByAccountType();
//
//    boolean isAccountCodeAvailable(String code);
//
//    boolean hasAccountTransactions(Long id);
//
//    boolean canDeleteAccount(Long id);
//
//    boolean validateAccountHierarchy(Long id);
}
