package lk.epicgreen.erp.accounting.service.impl;

import lk.epicgreen.erp.accounting.entity.*;
import lk.epicgreen.erp.accounting.repository.*;
import lk.epicgreen.erp.accounting.service.AccountingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Unified Accounting Service Implementation
 * Delegates to individual repositories
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountingServiceImpl implements AccountingService {
    
    private final ChartOfAccountsRepository accountRepository;
    private final FinancialPeriodRepository periodRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final GeneralLedgerRepository ledgerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final BankReconciliationRepository reconciliationRepository;
    
    // ===================================================================
    // CHART OF ACCOUNTS
    // ===================================================================
    
    @Override
    public ChartOfAccounts createAccount(ChartOfAccounts account) {
        log.info("Creating account: {}", account.getAccountCode());
        return accountRepository.save(account);
    }
    
    @Override
    public ChartOfAccounts updateAccount(Long id, ChartOfAccounts account) {
        log.info("Updating account: {}", id);
        ChartOfAccounts existing = getAccountById(id);
        account.setId(id);
        return accountRepository.save(account);
    }
    
    @Override
    public void deleteAccount(Long id) {
        log.info("Deleting account: {}", id);
        accountRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ChartOfAccounts getAccountById(Long id) {
        return accountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public ChartOfAccounts getAccountByCode(String code) {
        return accountRepository.findByAccountCode(code)
            .orElseThrow(() -> new RuntimeException("Account not found with code: " + code));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ChartOfAccounts> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getAllAccounts() {
        return accountRepository.findAll(Sort.by(Sort.Direction.ASC, "accountCode"));
    }

    @Transactional
    public List<ChartOfAccounts> getEquityAccounts(){
        return  accountRepository.findEquityAccounts();
    }

    @Transactional
    public List<ChartOfAccounts> getLiabilityAccounts(){
        return accountRepository.findLiabilityAccounts();
    }

    @Transactional
    public List<ChartOfAccounts> getAssetAccounts(){
        return accountRepository.findAssetAccounts();
    }
    @Transactional
    public List<ChartOfAccounts> getPayableAccounts(){
        return accountRepository.findLiabilityAccounts();
    }

    @Transactional
    public List<ChartOfAccounts> getExpenseAccounts(){
        return  accountRepository.findExpenseAccounts();
    }

    @Transactional
    public List<ChartOfAccounts> getRevenueAccounts(){
        return accountRepository.findRevenueAccounts();
    }
//    @Transactional
//    public List<ChartOfAccounts> getPostingAccounts(){
//
//    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ChartOfAccounts> searchAccounts(String keyword, Pageable pageable) {
        return accountRepository.searchAccounts(keyword,null,null,null,null,pageable);
    }
    
    @Override
    public ChartOfAccounts activateAccount(Long id) {
        ChartOfAccounts account = getAccountById(id);
        account.setIsActive(true);
        return accountRepository.save(account);
    }
    
    @Override
    public ChartOfAccounts deactivateAccount(Long id) {
        ChartOfAccounts account = getAccountById(id);
        account.setIsActive(false);
        return accountRepository.save(account);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getAccountsByType(String type) {
        return accountRepository.findByAccountType(type);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getAccountsByCategory(String category) {
        return accountRepository.findByAccountCategory(category);
    }

    @Transactional
    public Double getAccountBalanceUpToDate(Long id, LocalDate date){
        return accountRepository.getAccountBalanceUpToDate(id,date);
    }


    @Transactional
    public Double getAccountBalanceForPeriod(Long id, LocalDate startDate, LocalDate endDate){
        return accountRepository.getAccountBalance(startDate,endDate,id);
    }
    @Transactional
    public List<ChartOfAccounts> getBankAccounts(){
        return accountRepository.findAll();
    }

    @Transactional
    public Double getTotalDebitForAccount(Long id){
        return accountRepository.getTotalDebitForAccount(id);
    }

    public Double getTotalCreditForAccount(Long id){
        return accountRepository.getTotalCreditForAccount(id);
    }
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getActiveAccounts() {
        return accountRepository.findByIsActive(true);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getInactiveAccounts() {
        return accountRepository.findByIsActive(false);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getParentAccounts() {
        return accountRepository.findByParentAccountIsNull();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getChildAccounts(Long parentId) {
        return accountRepository.findByParentAccountId(parentId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAccountBalance(Long accountId) {
        return accountRepository.getAccountBalance(accountId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAccountSummary(Long accountId) {
        ChartOfAccounts account = getAccountById(accountId);
        Map<String, Object> summary = new HashMap<>();
        summary.put("account", account);
        summary.put("balance", getAccountBalance(accountId));
        summary.put("ledgerEntries", ledgerRepository.findByAccountId(accountId));
        return summary;
    }
    
//    @Override
//    public ChartOfAccounts markAsReconciled(Long accountId) {
//        ChartOfAccounts account = getAccountById(accountId);
//        account.setIsReconciled(true);
//        return accountRepository.save(account);
//    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAccountReconciliationStatus(Long accountId) {
        ChartOfAccounts account = getAccountById(accountId);
        Map<String, Object> status = new HashMap<>();
        status.put("isReconciled", account.getIsReconsiled());
        status.put("lastReconciledAt", account.getLastReconciledAt());
        return status;
    }
    
    // ===================================================================
    // FINANCIAL PERIODS
    // ===================================================================
    
    @Override
    public FinancialPeriod createPeriod(FinancialPeriod period) {
        log.info("Creating period: {}", period.getPeriodCode());
        return periodRepository.save(period);
    }
    
    @Override
    public FinancialPeriod updatePeriod(Long id, FinancialPeriod period) {
        log.info("Updating period: {}", id);
        period.setId(id);
        return periodRepository.save(period);
    }
    
    @Override
    public void deletePeriod(Long id) {
        log.info("Deleting period: {}", id);
        periodRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public FinancialPeriod getPeriodById(Long id) {
        return periodRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Period not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public FinancialPeriod getPeriodByCode(String code) {
        return periodRepository.findByPeriodCode(code)
            .orElseThrow(() -> new RuntimeException("Period not found with code: " + code));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<FinancialPeriod> getAllPeriods(Pageable pageable) {
        return periodRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FinancialPeriod> getAllPeriods() {
        return periodRepository.findAll(Sort.by(Sort.Direction.DESC, "startDate"));
    }
    
    @Override
    @Transactional(readOnly = true)
    public FinancialPeriod getCurrentPeriod() {
//        LocalDate today = LocalDate.now();
        return periodRepository.findCurrentPeriod()
            .orElseThrow(() -> new RuntimeException("No current period found"));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FinancialPeriod> getOpenPeriods() {
        return periodRepository.findByIsClosedFalse();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FinancialPeriod> getClosedPeriods() {
        return periodRepository.findByIsClosedTrue();
    }
    
    @Override
    public FinancialPeriod openPeriod(Long periodId) {
        FinancialPeriod period = getPeriodById(periodId);
        period.setIsClosed(false);
        return periodRepository.save(period);
    }
    
    @Override
    public FinancialPeriod closePeriod(Long periodId) {
        FinancialPeriod period = getPeriodById(periodId);
        period.setIsClosed(true);
        return periodRepository.save(period);
    }
    
    // ===================================================================
    // JOURNAL ENTRIES
    // ===================================================================
    
    @Override
    public JournalEntry createJournalEntry(JournalEntry journalEntry) {
        log.info("Creating journal entry: {}", journalEntry.getJournalNumber());
        return journalEntryRepository.save(journalEntry);
    }
    
    @Override
    public JournalEntry updateJournalEntry(Long id, JournalEntry journalEntry) {
        log.info("Updating journal entry: {}", id);
        journalEntry.setId(id);
        return journalEntryRepository.save(journalEntry);
    }
    
    @Override
    public void deleteJournalEntry(Long id) {
        log.info("Deleting journal entry: {}", id);
        journalEntryRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public JournalEntry getJournalEntryById(Long id) {
        return journalEntryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Journal entry not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public JournalEntry getJournalEntryByNumber(String number) {
        return journalEntryRepository.findByJournalNumber(number)
            .orElseThrow(() -> new RuntimeException("Journal entry not found with number: " + number));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<JournalEntry> getAllJournalEntries(Pageable pageable) {
        return journalEntryRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<JournalEntry> getAllJournalEntries() {
        return journalEntryRepository.findAll(Sort.by(Sort.Direction.DESC, "journalDate"));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<JournalEntry> searchJournalEntries(String keyword, Pageable pageable) {
        return journalEntryRepository.searchJournalEntries(keyword, pageable);
    }
    
    @Override
    public JournalEntry postJournalEntry(Long journalEntryId) {
        JournalEntry entry = getJournalEntryById(journalEntryId);
        entry.setIsPosted(true);
        return journalEntryRepository.save(entry);
    }
    
    @Override
    public JournalEntry reverseJournalEntry(Long journalEntryId) {
        JournalEntry entry = getJournalEntryById(journalEntryId);
        entry.setIsReversed(true);
        return journalEntryRepository.save(entry);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<JournalEntry> getJournalEntriesByPeriod(Long periodId) {
        return journalEntryRepository.findByPeriodId(periodId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<JournalEntry> getPostedJournalEntries() {
        return journalEntryRepository.findByIsPosted(true);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<JournalEntry> getUnpostedJournalEntries() {
        return journalEntryRepository.findByIsPosted(false);
    }
    
    // ===================================================================
    // GENERAL LEDGER
    // ===================================================================
    
    @Override
    @Transactional(readOnly = true)
    public List<GeneralLedger> getAccountLedger(Long accountId) {
        return ledgerRepository.findByAccountId(accountId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GeneralLedger> getAccountLedgerForPeriod(Long accountId, LocalDate startDate, LocalDate endDate) {
        return ledgerRepository.findByAccountIdAndTransactionDateBetween(accountId, startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GeneralLedger> getLedgerEntriesByJournalEntry(Long journalEntryId) {
        return ledgerRepository.findByJournalEntryId(journalEntryId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GeneralLedger> getPostedLedgerEntries() {
        return ledgerRepository.findByIsPosted(true);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GeneralLedger> getUnpostedLedgerEntries() {
        return ledgerRepository.findByIsPosted(false);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<GeneralLedger> searchLedgerEntries(String keyword, Pageable pageable) {
        return ledgerRepository.searchLedgerEntries(keyword, pageable);
    }
    
    // ===================================================================
    // BANK ACCOUNTS
    // ===================================================================
    
    @Override
    public BankAccount createBankAccount(BankAccount bankAccount) {
        log.info("Creating bank account: {}", bankAccount.getAccountNumber());
        return bankAccountRepository.save(bankAccount);
    }
    
    @Override
    public BankAccount updateBankAccount(Long id, BankAccount bankAccount) {
        log.info("Updating bank account: {}", id);
        bankAccount.setId(id);
        return bankAccountRepository.save(bankAccount);
    }
    
    @Override
    public void deleteBankAccount(Long id) {
        log.info("Deleting bank account: {}", id);
        bankAccountRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BankAccount getBankAccountById(Long id) {
        return bankAccountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Bank account not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public BankAccount getBankAccountByNumber(String accountNumber) {
        return bankAccountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new RuntimeException("Bank account not found with number: " + accountNumber));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<BankAccount> getAllBankAccounts(Pageable pageable) {
        return bankAccountRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.findAll(Sort.by(Sort.Direction.ASC, "accountNumber"));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<BankAccount> searchBankAccounts(String keyword, Pageable pageable) {
        // for now only filters through account number
        return bankAccountRepository.searchBankAccounts(keyword,null,null,null,null,pageable);
    }
    
    @Override
    public BankAccount activateBankAccount(Long id) {
        BankAccount account = getBankAccountById(id);
        account.setIsActive(true);
        return bankAccountRepository.save(account);
    }
    
    @Override
    public BankAccount deactivateBankAccount(Long id) {
        BankAccount account = getBankAccountById(id);
        account.setIsActive(false);
        return bankAccountRepository.save(account);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BankAccount> getActiveBankAccounts() {
        return bankAccountRepository.findByIsActive(true);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBankAccountBalance(Long bankAccountId) {
        BankAccount account = getBankAccountById(bankAccountId);
        return account.getCurrentBalance();
    }
    
    // ===================================================================
    // BANK RECONCILIATION
    // ===================================================================
    
    @Override
    public BankReconciliation createReconciliation(BankReconciliation reconciliation) {
        log.info("Creating reconciliation");
        return reconciliationRepository.save(reconciliation);
    }
    
    @Override
    public BankReconciliation updateReconciliation(Long id, BankReconciliation reconciliation) {
        log.info("Updating reconciliation: {}", id);
        reconciliation.setId(id);
        return reconciliationRepository.save(reconciliation);
    }
    
    @Override
    public void deleteReconciliation(Long id) {
        log.info("Deleting reconciliation: {}", id);
        reconciliationRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BankReconciliation getReconciliationById(Long id) {
        return reconciliationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reconciliation not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<BankReconciliation> getAllReconciliations(Pageable pageable) {
        return reconciliationRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BankReconciliation> getAllReconciliations() {
        return reconciliationRepository.findAll(Sort.by(Sort.Direction.DESC, "reconciliationDate"));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BankReconciliation> getReconciliationsByBankAccount(Long bankAccountId) {
        return reconciliationRepository.findByBankAccountId(bankAccountId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BankReconciliation> getReconciledReconciliations() {
        return reconciliationRepository.findByIsReconciled(true);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BankReconciliation> getUnreconciledReconciliations() {
        return reconciliationRepository.findByIsReconciled(false);
    }
    
    @Override
    public BankReconciliation markAsReconciled(Long reconciliationId) {
        BankReconciliation reconciliation = getReconciliationById(reconciliationId);
        reconciliation.setIsReconciled(true);
        return reconciliationRepository.save(reconciliation);
    }
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAccountingStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAccounts", accountRepository.count());
        stats.put("activeAccounts", accountRepository.countByIsActiveEmps());
        stats.put("totalJournalEntries", journalEntryRepository.count());
        stats.put("postedJournalEntries", journalEntryRepository.countByIsPostedTrue());
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAccountTypeDistribution() {
        List<Object[]> results = accountRepository.getAccountTypeDistribution();
        return convertToMapList(results);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAccountCategoryDistribution() {
        List<Object[]> results = accountRepository.getAccountCategoryDistribution();
        return convertToMapList(results);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMostActiveAccounts() {
        List<Object[]> results = accountRepository.getMostActiveAccounts();
        return convertToMapList(results);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getAccountsWithBalance() {
        return accountRepository.AccountsWithBalance();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getJournalEntryStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEntries", journalEntryRepository.count());
        stats.put("postedEntries", journalEntryRepository.countByIsPostedTrue());
        stats.put("unpostedEntries", journalEntryRepository.countByIsPostedFalse());
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("accounting", getAccountingStatistics());
        dashboard.put("journalEntries", getJournalEntryStatistics());
        dashboard.put("typeDistribution", getAccountTypeDistribution());
        return dashboard;
    }
    
    // Helper method
    private List<Map<String, Object>> convertToMapList(List<Object[]> results) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("key", result[0]);
            map.put("value", result[1]);
            list.add(map);
        }
        return list;
    }
}
