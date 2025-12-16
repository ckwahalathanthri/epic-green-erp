package lk.epicgreen.erp.accounting.repository;

import lk.epicgreen.erp.accounting.entity.ChartOfAccounts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ChartOfAccounts Repository
 * Repository for chart of accounts data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface ChartOfAccountsRepository extends JpaRepository<ChartOfAccounts, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find account by account code
     */
    Optional<ChartOfAccounts> findByAccountCode(String accountCode);
    
    /**
     * Check if account exists by account code
     */
    boolean existsByAccountCode(String accountCode);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find accounts by account type
     */
    List<ChartOfAccounts> findByAccountType(String accountType);
    
    /**
     * Find accounts by account type with pagination
     */
    Page<ChartOfAccounts> findByAccountType(String accountType, Pageable pageable);
    
    /**
     * Find accounts by account category
     */
    List<ChartOfAccounts> findByAccountCategory(String accountCategory);
    
    /**
     * Find accounts by account category with pagination
     */
    Page<ChartOfAccounts> findByAccountCategory(String accountCategory, Pageable pageable);
    
    /**
     * Find accounts by parent account
     */
    List<ChartOfAccounts> findByParentAccountId(Long parentAccountId);
    
    /**
     * Find accounts by status
     */
    List<ChartOfAccounts> findByStatus(String status);
    
    /**
     * Find accounts by status with pagination
     */
    Page<ChartOfAccounts> findByStatus(String status, Pageable pageable);
    
    /**
     * Find active accounts
     */
    List<ChartOfAccounts> findByIsActive(Boolean isActive);
    
    /**
     * Find active accounts with pagination
     */
    Page<ChartOfAccounts> findByIsActive(Boolean isActive, Pageable pageable);
    
    /**
     * Find system accounts
     */
    List<ChartOfAccounts> findByIsSystemAccount(Boolean isSystemAccount);
    
    /**
     * Find accounts by is header
     */
    List<ChartOfAccounts> findByIsHeader(Boolean isHeader);
    
    /**
     * Find accounts by can post
     */
    List<ChartOfAccounts> findByCanPost(Boolean canPost);
    
    /**
     * Find accounts by requires reconciliation
     */
    List<ChartOfAccounts> findByRequiresReconciliation(Boolean requiresReconciliation);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find accounts by account type and status
     */
    List<ChartOfAccounts> findByAccountTypeAndStatus(String accountType, String status);
    
    /**
     * Find accounts by account type and status with pagination
     */
    Page<ChartOfAccounts> findByAccountTypeAndStatus(String accountType, String status, Pageable pageable);
    
    /**
     * Find accounts by account category and status
     */
    List<ChartOfAccounts> findByAccountCategoryAndStatus(String accountCategory, String status);
    
    /**
     * Find accounts by account type and is active
     */
    List<ChartOfAccounts> findByAccountTypeAndIsActive(String accountType, Boolean isActive);
    
    /**
     * Find accounts by can post and is active
     */
    List<ChartOfAccounts> findByCanPostAndIsActive(Boolean canPost, Boolean isActive);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Find accounts by account name pattern
     */
    @Query("SELECT c FROM ChartOfAccounts c WHERE LOWER(c.accountName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ChartOfAccounts> findByAccountNameContaining(@Param("name") String name);
    
    /**
     * Search accounts
     */
    @Query("SELECT c FROM ChartOfAccounts c WHERE " +
           "LOWER(c.accountCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.accountName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ChartOfAccounts> searchAccounts(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find root accounts (no parent)
     */
    @Query("SELECT c FROM ChartOfAccounts c WHERE c.parentAccountId IS NULL ORDER BY c.accountCode")
    List<ChartOfAccounts> findRootAccounts();
    
    /**
     * Find child accounts
     */
    @Query("SELECT c FROM ChartOfAccounts c WHERE c.parentAccountId = :parentId ORDER BY c.accountCode")
    List<ChartOfAccounts> findChildAccounts(@Param("parentId") Long parentId);
    
    /**
     * Find accounts by account type ordered
     */
    @Query("SELECT c FROM ChartOfAccounts c WHERE c.accountType = :accountType " +
           "AND c.isActive = true ORDER BY c.accountCode")
    List<ChartOfAccounts> findActiveAccountsByType(@Param("accountType") String accountType);
    
    /**
     * Find posting accounts
     */
    @Query("SELECT c FROM ChartOfAccounts c WHERE c.canPost = true " +
           "AND c.isActive = true ORDER BY c.accountCode")
    List<ChartOfAccounts> findPostingAccounts();
    
    /**
     * Find header accounts
     */
    @Query("SELECT c FROM ChartOfAccounts c WHERE c.isHeader = true " +
           "ORDER BY c.accountCode")
    List<ChartOfAccounts> findHeaderAccounts();
    
    /**
     * Find accounts requiring reconciliation
     */
    @Query("SELECT c FROM ChartOfAccounts c WHERE c.requiresReconciliation = true " +
           "AND c.isActive = true ORDER BY c.accountCode")
    List<ChartOfAccounts> findAccountsRequiringReconciliation();
    
    /**
     * Find bank accounts
     */
    @Query("SELECT c FROM ChartOfAccounts c WHERE c.accountCategory = 'BANK' " +
           "AND c.isActive = true ORDER BY c.accountCode")
    List<ChartOfAccounts> findBankAccounts();
    
    /**
     * Find cash accounts
     */
    @Query("SELECT c FROM ChartOfAccounts c WHERE c.accountCategory = 'CASH' " +
           "AND c.isActive = true ORDER BY c.accountCode")
    List<ChartOfAccounts> findCashAccounts();
    
    /**
     * Find receivable accounts
     */
    @Query("SELECT c FROM ChartOfAccounts c WHERE c.accountCategory = 'RECEIVABLE' " +
           "AND c.isActive = true ORDER BY c.accountCode")
    List<ChartOfAccounts> findReceivableAccounts();
    
    /**
     * Find payable accounts
     */
    @Query("SELECT c FROM ChartOfAccounts c WHERE c.accountCategory = 'PAYABLE' " +
           "AND c.isActive = true ORDER BY c.accountCode")
    List<ChartOfAccounts> findPayableAccounts();
    
    /**
     * Find revenue accounts
     */
    @Query("SELECT c FROM ChartOfAccounts c WHERE c.accountType = 'REVENUE' " +
           "AND c.isActive = true ORDER BY c.accountCode")
    List<ChartOfAccounts> findRevenueAccounts();
    
    /**
     * Find expense accounts
     */
    @Query("SELECT c FROM ChartOfAccounts c WHERE c.accountType = 'EXPENSE' " +
           "AND c.isActive = true ORDER BY c.accountCode")
    List<ChartOfAccounts> findExpenseAccounts();
    
    /**
     * Find asset accounts
     */
    @Query("SELECT c FROM ChartOfAccounts c WHERE c.accountType = 'ASSET' " +
           "AND c.isActive = true ORDER BY c.accountCode")
    List<ChartOfAccounts> findAssetAccounts();
    
    /**
     * Find liability accounts
     */
    @Query("SELECT c FROM ChartOfAccounts c WHERE c.accountType = 'LIABILITY' " +
           "AND c.isActive = true ORDER BY c.accountCode")
    List<ChartOfAccounts> findLiabilityAccounts();
    
    /**
     * Find equity accounts
     */
    @Query("SELECT c FROM ChartOfAccounts c WHERE c.accountType = 'EQUITY' " +
           "AND c.isActive = true ORDER BY c.accountCode")
    List<ChartOfAccounts> findEquityAccounts();
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count accounts by account type
     */
    @Query("SELECT COUNT(c) FROM ChartOfAccounts c WHERE c.accountType = :accountType")
    Long countByAccountType(@Param("accountType") String accountType);
    
    /**
     * Count accounts by account category
     */
    @Query("SELECT COUNT(c) FROM ChartOfAccounts c WHERE c.accountCategory = :accountCategory")
    Long countByAccountCategory(@Param("accountCategory") String accountCategory);
    
    /**
     * Count active accounts
     */
    @Query("SELECT COUNT(c) FROM ChartOfAccounts c WHERE c.isActive = true")
    Long countActiveAccounts();
    
    /**
     * Count posting accounts
     */
    @Query("SELECT COUNT(c) FROM ChartOfAccounts c WHERE c.canPost = true AND c.isActive = true")
    Long countPostingAccounts();
    
    /**
     * Count header accounts
     */
    @Query("SELECT COUNT(c) FROM ChartOfAccounts c WHERE c.isHeader = true")
    Long countHeaderAccounts();
    
    /**
     * Get account type distribution
     */
    @Query("SELECT c.accountType, COUNT(c) as accountCount FROM ChartOfAccounts c " +
           "WHERE c.isActive = true " +
           "GROUP BY c.accountType ORDER BY accountCount DESC")
    List<Object[]> getAccountTypeDistribution();
    
    /**
     * Get account category distribution
     */
    @Query("SELECT c.accountCategory, COUNT(c) as accountCount FROM ChartOfAccounts c " +
           "WHERE c.isActive = true AND c.accountCategory IS NOT NULL " +
           "GROUP BY c.accountCategory ORDER BY accountCount DESC")
    List<Object[]> getAccountCategoryDistribution();
    
    /**
     * Find accounts with balance
     */
    @Query("SELECT c FROM ChartOfAccounts c WHERE " +
           "(c.currentBalance <> 0 OR c.debitBalance <> 0 OR c.creditBalance <> 0) " +
           "AND c.isActive = true ORDER BY c.accountCode")
    List<ChartOfAccounts> findAccountsWithBalance();
    
    /**
     * Find accounts by balance range
     */
    @Query("SELECT c FROM ChartOfAccounts c WHERE c.currentBalance BETWEEN :minBalance AND :maxBalance " +
           "AND c.isActive = true ORDER BY c.currentBalance DESC")
    List<ChartOfAccounts> findAccountsByBalanceRange(@Param("minBalance") Double minBalance,
                                                     @Param("maxBalance") Double maxBalance);
    
    /**
     * Get total balance by account type
     */
    @Query("SELECT c.accountType, SUM(c.currentBalance) as totalBalance FROM ChartOfAccounts c " +
           "WHERE c.isActive = true " +
           "GROUP BY c.accountType")
    List<Object[]> getTotalBalanceByAccountType();
    
    /**
     * Find accounts by tags
     */
    @Query("SELECT c FROM ChartOfAccounts c WHERE c.tags LIKE CONCAT('%', :tag, '%')")
    List<ChartOfAccounts> findByTag(@Param("tag") String tag);
}
