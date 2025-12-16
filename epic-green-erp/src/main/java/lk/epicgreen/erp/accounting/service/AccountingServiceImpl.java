package lk.epicgreen.erp.accounting.service;

import lk.epicgreen.erp.accounting.entity.ChartOfAccounts;
import lk.epicgreen.erp.accounting.entity.GeneralLedger;
import lk.epicgreen.erp.accounting.repository.ChartOfAccountsRepository;
import lk.epicgreen.erp.accounting.repository.GeneralLedgerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Accounting Service Implementation
 * Implementation of accounting service operations
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
    private final GeneralLedgerRepository ledgerRepository;
    
    // ===================================================================
    // CHART OF ACCOUNTS OPERATIONS
    // ===================================================================
    
    @Override
    public ChartOfAccounts createAccount(ChartOfAccounts account) {
        log.info("Creating account: {}", account.getAccountCode());
        
        // Validate account code
        if (accountRepository.existsByAccountCode(account.getAccountCode())) {
            throw new RuntimeException("Account code already exists: " + account.getAccountCode());
        }
        
        // Set defaults
        if (account.getIsActive() == null) {
            account.setIsActive(true);
        }
        if (account.getCanPost() == null) {
            account.setCanPost(!account.getIsHeader());
        }
        
        return accountRepository.save(account);
    }
    
    @Override
    public ChartOfAccounts updateAccount(Long id, ChartOfAccounts account) {
        log.info("Updating account: {}", id);
        
        ChartOfAccounts existing = getAccountById(id);
        
        // Update fields
        existing.setAccountName(account.getAccountName());
        existing.setDescription(account.getDescription());
        existing.setAccountType(account.getAccountType());
        existing.setAccountCategory(account.getAccountCategory());
        existing.setParentAccountId(account.getParentAccountId());
        existing.setIsHeader(account.getIsHeader());
        existing.setCanPost(account.getCanPost());
        existing.setRequiresReconciliation(account.getRequiresReconciliation());
        existing.setNotes(account.getNotes());
        existing.setTags(account.getTags());
        
        return accountRepository.save(existing);
    }
    
    @Override
    public void deleteAccount(Long id) {
        log.info("Deleting account: {}", id);
        
        if (!canDeleteAccount(id)) {
            throw new RuntimeException("Cannot delete account with transactions");
        }
        
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
    public ChartOfAccounts getAccountByCode(String accountCode) {
        return accountRepository.findByAccountCode(accountCode)
            .orElseThrow(() -> new RuntimeException("Account not found with code: " + accountCode));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getAllAccounts() {
        return accountRepository.findAll(Sort.by(Sort.Direction.ASC, "accountCode"));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ChartOfAccounts> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getAccountsByType(String accountType) {
        return accountRepository.findByAccountType(accountType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getAccountsByCategory(String accountCategory) {
        return accountRepository.findByAccountCategory(accountCategory);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getActiveAccounts() {
        return accountRepository.findByIsActive(true);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getPostingAccounts() {
        return accountRepository.findPostingAccounts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getRootAccounts() {
        return accountRepository.findRootAccounts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getChildAccounts(Long parentId) {
        return accountRepository.findChildAccounts(parentId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ChartOfAccounts> searchAccounts(String keyword, Pageable pageable) {
        return accountRepository.searchAccounts(keyword, pageable);
    }
    
    @Override
    public ChartOfAccounts activateAccount(Long id) {
        ChartOfAccounts account = getAccountById(id);
        account.setIsActive(true);
        account.setStatus("ACTIVE");
        return accountRepository.save(account);
    }
    
    @Override
    public ChartOfAccounts deactivateAccount(Long id) {
        ChartOfAccounts account = getAccountById(id);
        account.setIsActive(false);
        account.setStatus("INACTIVE");
        return accountRepository.save(account);
    }
    
    // ===================================================================
    // ACCOUNT TYPE SPECIFIC OPERATIONS
    // ===================================================================
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getBankAccounts() {
        return accountRepository.findBankAccounts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getCashAccounts() {
        return accountRepository.findCashAccounts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getReceivableAccounts() {
        return accountRepository.findReceivableAccounts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getPayableAccounts() {
        return accountRepository.findPayableAccounts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getRevenueAccounts() {
        return accountRepository.findRevenueAccounts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getExpenseAccounts() {
        return accountRepository.findExpenseAccounts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getAssetAccounts() {
        return accountRepository.findAssetAccounts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getLiabilityAccounts() {
        return accountRepository.findLiabilityAccounts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getEquityAccounts() {
        return accountRepository.findEquityAccounts();
    }
    
    // ===================================================================
    // GENERAL LEDGER OPERATIONS
    // ===================================================================
    
    @Override
    @Transactional(readOnly = true)
    public List<GeneralLedger> getAccountLedger(Long accountId) {
        return ledgerRepository.getAccountLedger(accountId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<GeneralLedger> getAccountLedger(Long accountId, Pageable pageable) {
        return ledgerRepository.getAccountLedger(accountId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GeneralLedger> getAccountLedgerForPeriod(Long accountId, LocalDate startDate, LocalDate endDate) {
        return ledgerRepository.getAccountLedgerForPeriod(accountId, startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GeneralLedger> getLedgerEntriesByJournalEntry(Long journalEntryId) {
        return ledgerRepository.findByJournalEntryId(journalEntryId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GeneralLedger> getPostedLedgerEntries() {
        return ledgerRepository.findPostedEntries();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GeneralLedger> getUnpostedLedgerEntries() {
        return ledgerRepository.findUnpostedEntries();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<GeneralLedger> searchLedgerEntries(String keyword, Pageable pageable) {
        return ledgerRepository.searchLedgerEntries(keyword, pageable);
    }
    
    // ===================================================================
    // BALANCE OPERATIONS
    // ===================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Double getAccountBalance(Long accountId) {
        Double balance = ledgerRepository.getAccountBalance(accountId);
        return balance != null ? balance : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getAccountBalanceForPeriod(Long accountId, LocalDate startDate, LocalDate endDate) {
        Double balance = ledgerRepository.getAccountBalanceForPeriod(accountId, startDate, endDate);
        return balance != null ? balance : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getAccountBalanceUpToDate(Long accountId, LocalDate date) {
        Double balance = ledgerRepository.getAccountBalanceUpToDate(accountId, date);
        return balance != null ? balance : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalDebitForAccount(Long accountId) {
        Double debit = ledgerRepository.getTotalDebitForAccount(accountId);
        return debit != null ? debit : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalCreditForAccount(Long accountId) {
        Double credit = ledgerRepository.getTotalCreditForAccount(accountId);
        return credit != null ? credit : 0.0;
    }
    
    @Override
    public void updateAccountBalances(Long accountId) {
        ChartOfAccounts account = getAccountById(accountId);
        
        Double debit = getTotalDebitForAccount(accountId);
        Double credit = getTotalCreditForAccount(accountId);
        Double balance = debit - credit;
        
        account.setDebitBalance(debit);
        account.setCreditBalance(credit);
        account.setCurrentBalance(balance);
        account.setLastBalanceUpdate(LocalDate.now());
        
        accountRepository.save(account);
    }
    
    @Override
    public void recalculateAllAccountBalances() {
        List<ChartOfAccounts> accounts = getAllAccounts();
        
        for (ChartOfAccounts account : accounts) {
            try {
                updateAccountBalances(account.getId());
            } catch (Exception e) {
                log.error("Error updating balance for account: {}", account.getAccountCode(), e);
            }
        }
    }
    
    // ===================================================================
    // TRIAL BALANCE
    // ===================================================================
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTrialBalance() {
        List<Object[]> results = ledgerRepository.getTrialBalance();
        return convertTrialBalanceResults(results);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTrialBalanceForPeriod(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = ledgerRepository.getTrialBalanceForPeriod(startDate, endDate);
        return convertTrialBalanceResults(results);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTrialBalanceForFiscalYear(Integer year) {
        List<Object[]> results = ledgerRepository.getTrialBalanceForFiscalYear(year);
        return convertTrialBalanceResults(results);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> verifyTrialBalance() {
        List<Map<String, Object>> trialBalance = getTrialBalance();
        
        double totalDebit = trialBalance.stream()
            .mapToDouble(tb -> (Double) tb.get("totalDebit"))
            .sum();
        
        double totalCredit = trialBalance.stream()
            .mapToDouble(tb -> (Double) tb.get("totalCredit"))
            .sum();
        
        Map<String, Object> verification = new HashMap<>();
        verification.put("totalDebit", totalDebit);
        verification.put("totalCredit", totalCredit);
        verification.put("difference", Math.abs(totalDebit - totalCredit));
        verification.put("isBalanced", Math.abs(totalDebit - totalCredit) < 0.01);
        
        return verification;
    }
    
    // ===================================================================
    // FINANCIAL STATEMENTS (Placeholder implementations)
    // ===================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> generateBalanceSheet(LocalDate asOfDate) {
        // TODO: Implement balance sheet generation
        return new HashMap<>();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> generateIncomeStatement(LocalDate startDate, LocalDate endDate) {
        // TODO: Implement income statement generation
        return new HashMap<>();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> generateCashFlowStatement(LocalDate startDate, LocalDate endDate) {
        // TODO: Implement cash flow statement generation
        return new HashMap<>();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> generateEquityStatement(LocalDate startDate, LocalDate endDate) {
        // TODO: Implement equity statement generation
        return new HashMap<>();
    }
    
    // ===================================================================
    // FISCAL PERIOD OPERATIONS
    // ===================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Integer getCurrentFiscalYear() {
        return LocalDate.now().getYear();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Integer getCurrentFiscalPeriod() {
        return LocalDate.now().getMonthValue();
    }
    
    @Override
    public void closeFiscalPeriod(Integer year, Integer period) {
        // TODO: Implement fiscal period closing logic
        log.info("Closing fiscal period: {}-{}", year, period);
    }
    
    @Override
    public void closeFiscalYear(Integer year) {
        // TODO: Implement fiscal year closing logic
        log.info("Closing fiscal year: {}", year);
    }
    
    @Override
    public void reopenFiscalPeriod(Integer year, Integer period) {
        // TODO: Implement fiscal period reopening logic
        log.info("Reopening fiscal period: {}-{}", year, period);
    }
    
    // ===================================================================
    // RECONCILIATION
    // ===================================================================
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getAccountsRequiringReconciliation() {
        return accountRepository.findAccountsRequiringReconciliation();
    }
    
    @Override
    public void markAccountAsReconciled(Long accountId, LocalDate reconciliationDate) {
        ChartOfAccounts account = getAccountById(accountId);
        account.setLastReconciliationDate(reconciliationDate);
        accountRepository.save(account);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAccountReconciliationStatus(Long accountId) {
        ChartOfAccounts account = getAccountById(accountId);
        
        Map<String, Object> status = new HashMap<>();
        status.put("accountId", account.getId());
        status.put("accountCode", account.getAccountCode());
        status.put("accountName", account.getAccountName());
        status.put("requiresReconciliation", account.getRequiresReconciliation());
        status.put("lastReconciliationDate", account.getLastReconciliationDate());
        
        return status;
    }
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAccountingStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalAccounts", accountRepository.count());
        stats.put("activeAccounts", accountRepository.countActiveAccounts());
        stats.put("postingAccounts", accountRepository.countPostingAccounts());
        stats.put("headerAccounts", accountRepository.countHeaderAccounts());
        stats.put("totalLedgerEntries", ledgerRepository.count());
        stats.put("postedEntries", ledgerRepository.countPostedEntries());
        stats.put("unpostedEntries", ledgerRepository.countUnpostedEntries());
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAccountTypeDistribution() {
        List<Object[]> results = accountRepository.getAccountTypeDistribution();
        return convertToMapList(results, "accountType", "accountCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAccountCategoryDistribution() {
        List<Object[]> results = accountRepository.getAccountCategoryDistribution();
        return convertToMapList(results, "accountCategory", "accountCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMostActiveAccounts() {
        List<Object[]> results = ledgerRepository.getMostActiveAccounts();
        return convertToMapList(results, "accountId", "entryCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getAccountsWithBalance() {
        return accountRepository.findAccountsWithBalance();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTotalBalanceByAccountType() {
        List<Object[]> results = accountRepository.getTotalBalanceByAccountType();
        return convertToMapList(results, "accountType", "totalBalance");
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("statistics", getAccountingStatistics());
        dashboard.put("typeDistribution", getAccountTypeDistribution());
        dashboard.put("categoryDistribution", getAccountCategoryDistribution());
        dashboard.put("mostActiveAccounts", getMostActiveAccounts().stream().limit(10).collect(Collectors.toList()));
        
        return dashboard;
    }
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    @Override
    @Transactional(readOnly = true)
    public boolean validateAccountCode(String accountCode) {
        return !accountRepository.existsByAccountCode(accountCode);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasTransactions(Long accountId) {
        return ledgerRepository.countByAccountId(accountId) > 0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canDeleteAccount(Long accountId) {
        // Cannot delete if has transactions or child accounts
        return !hasTransactions(accountId) && getChildAccounts(accountId).isEmpty();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validateAccountHierarchy(Long parentId, Long childId) {
        // TODO: Implement hierarchy validation to prevent circular references
        return true;
    }
    
    // ===================================================================
    // HELPER METHODS
    // ===================================================================
    
    private List<Map<String, Object>> convertTrialBalanceResults(List<Object[]> results) {
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("accountId", result[0]);
                map.put("totalDebit", result[1]);
                map.put("totalCredit", result[2]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    private List<Map<String, Object>> convertToMapList(List<Object[]> results, String key1, String key2) {
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put(key1, result[0]);
                map.put(key2, result[1]);
                return map;
            })
            .collect(Collectors.toList());
    }
}

