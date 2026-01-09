package lk.epicgreen.erp.accounting.repository;

import lk.epicgreen.erp.accounting.entity.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for BankAccount entity
 * Based on ACTUAL database schema: bank_accounts table
 * 
 * Fields: account_number, account_name, bank_name, bank_branch,
 *         account_type (ENUM: CURRENT, SAVINGS, OVERDRAFT, CASH),
 *         currency_code, gl_account_id (BIGINT),
 *         opening_balance, current_balance, is_active
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long>, JpaSpecificationExecutor<BankAccount> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find bank account by account number
     */
    Optional<BankAccount> findByAccountNumber(String accountNumber);
    
    /**
     * Find bank accounts by bank name
     */
    List<BankAccount> findByBankName(String bankName);
    List<BankAccount> findByIsActive(Boolean value);

    
    /**
     * Find bank accounts by bank name with pagination
     */
    Page<BankAccount> findByBankName(String bankName, Pageable pageable);
    
    /**
     * Find bank accounts by account type
     */
    List<BankAccount> findByAccountType(String accountType);
    
    /**
     * Find bank accounts by account type with pagination
     */
    Page<BankAccount> findByAccountType(String accountType, Pageable pageable);
    
    /**
     * Find bank accounts by currency
     */
    List<BankAccount> findByCurrencyCode(String currencyCode);
    
    /**
     * Find all active bank accounts
     */
    List<BankAccount> findByIsActiveTrue();
    
    /**
     * Find all active bank accounts with pagination
     */
    Page<BankAccount> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Find all inactive bank accounts
     */
    List<BankAccount> findByIsActiveFalse();
    
    /**
     * Find bank account by GL account
     */
    Optional<BankAccount> findByGlAccountId(Long glAccountId);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if account number exists
     */
    boolean existsByAccountNumber(String accountNumber);
    
    /**
     * Check if account number exists excluding specific bank account ID
     */
    boolean existsByAccountNumberAndIdNot(String accountNumber, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search bank accounts by account number containing (case-insensitive)
     */
    Page<BankAccount> findByAccountNumberContainingIgnoreCase(String accountNumber, Pageable pageable);
    
    /**
     * Search bank accounts by account name containing (case-insensitive)
     */
    Page<BankAccount> findByAccountNameContainingIgnoreCase(String accountName, Pageable pageable);
    
    /**
     * Search bank accounts by bank name containing (case-insensitive)
     */
    Page<BankAccount> findByBankNameContainingIgnoreCase(String bankName, Pageable pageable);
    
    /**
     * Search active bank accounts by keyword
     */
    @Query("SELECT ba FROM BankAccount ba WHERE ba.isActive = true AND " +
           "(LOWER(ba.accountNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(ba.accountName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(ba.bankName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<BankAccount> searchActiveBankAccounts(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Search bank accounts by multiple criteria
     */
    @Query("SELECT ba FROM BankAccount ba WHERE " +
           "(:accountNumber IS NULL OR LOWER(ba.accountNumber) LIKE LOWER(CONCAT('%', :accountNumber, '%'))) AND " +
           "(:accountName IS NULL OR LOWER(ba.accountName) LIKE LOWER(CONCAT('%', :accountName, '%'))) AND " +
           "(:bankName IS NULL OR LOWER(ba.bankName) LIKE LOWER(CONCAT('%', :bankName, '%'))) AND " +
           "(:accountType IS NULL OR ba.accountType = :accountType) AND " +
           "(:isActive IS NULL OR ba.isActive = :isActive)")
    Page<BankAccount> searchBankAccounts(
            @Param("accountNumber") String accountNumber,
            @Param("accountName") String accountName,
            @Param("bankName") String bankName,
            @Param("accountType") String accountType,
            @Param("isActive") Boolean isActive,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count active bank accounts
     */
    long countByIsActiveTrue();
    
    /**
     * Count bank accounts by account type
     */
    long countByAccountType(String accountType);
    
    /**
     * Count bank accounts by bank name
     */
    long countByBankName(String bankName);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find current accounts
     */
    @Query("SELECT ba FROM BankAccount ba WHERE ba.accountType = 'CURRENT' " +
           "AND ba.isActive = true ORDER BY ba.accountName")
    List<BankAccount> findCurrentAccounts();
    
    /**
     * Find savings accounts
     */
    @Query("SELECT ba FROM BankAccount ba WHERE ba.accountType = 'SAVINGS' " +
           "AND ba.isActive = true ORDER BY ba.accountName")
    List<BankAccount> findSavingsAccounts();
    
    /**
     * Find overdraft accounts
     */
    @Query("SELECT ba FROM BankAccount ba WHERE ba.accountType = 'OVERDRAFT' " +
           "AND ba.isActive = true ORDER BY ba.accountName")
    List<BankAccount> findOverdraftAccounts();
    
    /**
     * Find cash accounts
     */
    @Query("SELECT ba FROM BankAccount ba WHERE ba.accountType = 'CASH' " +
           "AND ba.isActive = true ORDER BY ba.accountName")
    List<BankAccount> findCashAccounts();
    
    /**
     * Get total current balance
     */
    @Query("SELECT SUM(ba.currentBalance) FROM BankAccount ba WHERE ba.isActive = true")
    BigDecimal getTotalCurrentBalance();
    
    /**
     * Get total current balance by account type
     */
    @Query("SELECT SUM(ba.currentBalance) FROM BankAccount ba WHERE ba.accountType = :accountType " +
           "AND ba.isActive = true")
    BigDecimal getTotalBalanceByAccountType(@Param("accountType") String accountType);
    
    /**
     * Get total current balance by bank
     */
    @Query("SELECT SUM(ba.currentBalance) FROM BankAccount ba WHERE ba.bankName = :bankName " +
           "AND ba.isActive = true")
    BigDecimal getTotalBalanceByBank(@Param("bankName") String bankName);
    
    /**
     * Get bank account statistics
     */
    @Query("SELECT " +
           "COUNT(ba) as totalAccounts, " +
           "SUM(CASE WHEN ba.isActive = true THEN 1 ELSE 0 END) as activeAccounts, " +
           "SUM(ba.currentBalance) as totalBalance, " +
           "COUNT(DISTINCT ba.bankName) as uniqueBanks " +
           "FROM BankAccount ba")
    Object getBankAccountStatistics();
    
    /**
     * Get bank accounts grouped by bank
     */
    @Query("SELECT ba.bankName, COUNT(ba) as accountCount, SUM(ba.currentBalance) as totalBalance " +
           "FROM BankAccount ba WHERE ba.isActive = true " +
           "GROUP BY ba.bankName ORDER BY totalBalance DESC")
    List<Object[]> getBankAccountsByBank();
    
    /**
     * Get bank accounts grouped by account type
     */
    @Query("SELECT ba.accountType, COUNT(ba) as accountCount, SUM(ba.currentBalance) as totalBalance " +
           "FROM BankAccount ba WHERE ba.isActive = true " +
           "GROUP BY ba.accountType ORDER BY totalBalance DESC")
    List<Object[]> getBankAccountsByType();
    
    /**
     * Find all bank accounts ordered by account number
     */
    List<BankAccount> findAllByOrderByAccountNumberAsc();
    
    /**
     * Find active bank accounts ordered by account name
     */
    List<BankAccount> findByIsActiveTrueOrderByAccountNameAsc();
    
    /**
     * Find bank accounts by bank and type
     */
    List<BankAccount> findByBankNameAndAccountType(String bankName, String accountType);
}
