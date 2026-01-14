package lk.epicgreen.erp.accounting.repository;

import lk.epicgreen.erp.accounting.entity.ChartOfAccounts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ChartOfAccounts entity
 * Based on ACTUAL database schema: chart_of_accounts table
 * 
 * Fields: account_code, account_name,
 *         account_type (ENUM: ASSET, LIABILITY, EQUITY, REVENUE, EXPENSE),
 *         account_category (ENUM: CURRENT_ASSET, FIXED_ASSET, CURRENT_LIABILITY, 
 *                                 LONG_TERM_LIABILITY, CAPITAL, DIRECT_INCOME, 
 *                                 INDIRECT_INCOME, DIRECT_EXPENSE, INDIRECT_EXPENSE, OTHER),
 *         parent_account_id (BIGINT), is_group_account, is_control_account,
 *         opening_balance, opening_balance_type (ENUM: DEBIT, CREDIT),
 *         current_balance, is_active
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface ChartOfAccountsRepository extends JpaRepository<ChartOfAccounts, Long>, JpaSpecificationExecutor<ChartOfAccounts> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find account by account code
     */
    Optional<ChartOfAccounts> findByAccountCode(String accountCode);
    
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
     * Find all active accounts
     */
    List<ChartOfAccounts> findByIsActiveTrue();
    
    /**
     * Find all active accounts with pagination
     */
    Page<ChartOfAccounts> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Find all inactive accounts
     */
    List<ChartOfAccounts> findByIsActiveFalse();
    
    /**
     * Find all group accounts
     */
    List<ChartOfAccounts> findByIsGroupAccountTrue();


    
    /**
     * Find all control accounts
     */
    List<ChartOfAccounts> findByIsControlAccountTrue();
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if account code exists
     */
    boolean existsByAccountCode(String accountCode);
    
    /**
     * Check if account code exists excluding specific account ID
     */
    boolean existsByAccountCodeAndIdNot(String accountCode, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search accounts by account code containing (case-insensitive)
     */
    Page<ChartOfAccounts> findByAccountCodeContainingIgnoreCase(String accountCode, Pageable pageable);
    
    /**
     * Search accounts by account name containing (case-insensitive)
     */
    Page<ChartOfAccounts> findByAccountNameContainingIgnoreCase(String accountName, Pageable pageable);
    
    /**
     * Search active accounts by keyword
     */
    @Query("SELECT coa FROM ChartOfAccounts coa WHERE coa.isActive = true AND " +
           "(LOWER(coa.accountCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(coa.accountName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<ChartOfAccounts> searchActiveAccounts(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(coa) FROM ChartOfAccounts coa WHERE coa.isActive=true")
    Integer countByIsActiveEmps();

    @Query("SELECT coa.accountType,COUNT(coa) FROM ChartOfAccounts coa GROUP BY coa.accountType")
    List<Object[]> getAccountTypeDistribution();

    @Query("SELECT coa.accountCategory,COUNT(coa) FROM ChartOfAccounts coa GROUP BY coa.accountCategory")
    List<Object[]> getAccountCategoryDistribution();

    @Query("SELECT coa.accountName FROM ChartOfAccounts coa ORDER BY coa.currentBalance DESC ")
    List<Object[]> getMostActiveAccounts();

    @Query("SELECT coa FROM ChartOfAccounts coa WHERE coa.currentBalance>0")
    List<ChartOfAccounts> AccountsWithBalance();

    @Query("SELECT SUM(coa.openingBalance) FROM ChartOfAccounts coa WHERE coa.id=:id AND coa.openingBalanceType='DEBIT'")
    Double getTotalDebitForAccount(@Param("id") Long id);

    @Query("SELECT SUM(coa.openingBalance) FROM ChartOfAccounts coa WHERE coa.id=:id AND coa.openingBalanceType='CREDIT'")

    Double getTotalCreditForAccount(@Param("id") Long id);

    @Query("SELECT SUM(coa.currentBalance) FROM ChartOfAccounts coa WHERE coa.id=:id AND coa.openDate BETWEEN :startDate AND :endDate" )
    Double getAccountBalance(@Param("startDate")LocalDate startDate,@Param("endDate")LocalDate endDate,@Param("id")Long id);

    @Query("SELECT SUM(coa.currentBalance) FROM ChartOfAccounts coa WHERE coa.id=:id AND coa.openDate=:date")
    Double getAccountBalanceUpToDate(@Param("id") Long id,@Param("date") LocalDate date);
    
    /**
     * Search accounts by multiple criteria
     */
    @Query("SELECT coa FROM ChartOfAccounts coa WHERE " +
           "(:accountCode IS NULL OR LOWER(coa.accountCode) LIKE LOWER(CONCAT('%', :accountCode, '%'))) AND " +
           "(:accountName IS NULL OR LOWER(coa.accountName) LIKE LOWER(CONCAT('%', :accountName, '%'))) AND " +
           "(:accountType IS NULL OR coa.accountType = :accountType) AND " +
           "(:accountCategory IS NULL OR coa.accountCategory = :accountCategory) AND " +
           "(:isActive IS NULL OR coa.isActive = :isActive)")
    Page<ChartOfAccounts> searchAccounts(
            @Param("accountCode") String accountCode,
            @Param("accountName") String accountName,
            @Param("accountType") String accountType,
            @Param("accountCategory") String accountCategory,
            @Param("isActive") Boolean isActive,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count active accounts
     */
    long countByIsActiveTrue();
    
    /**
     * Count accounts by account type
     */
    long countByAccountType(String accountType);
    
    /**
     * Count accounts by account category
     */
    long countByAccountCategory(String accountCategory);
    
    /**
     * Count child accounts
     */
    long countByParentAccountId(Long parentAccountId);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find asset accounts
     */
    @Query("SELECT coa FROM ChartOfAccounts coa WHERE coa.accountType = 'ASSET' " +
           "AND coa.isActive = true ORDER BY coa.accountCode")
    List<ChartOfAccounts> findAssetAccounts();
    
    /**
     * Find liability accounts
     */
    @Query("SELECT coa FROM ChartOfAccounts coa WHERE coa.accountType = 'LIABILITY' " +
           "AND coa.isActive = true ORDER BY coa.accountCode")
    List<ChartOfAccounts> findLiabilityAccounts();
    
    /**
     * Find equity accounts
     */
    @Query("SELECT coa FROM ChartOfAccounts coa WHERE coa.accountType = 'EQUITY' " +
           "AND coa.isActive = true ORDER BY coa.accountCode")
    List<ChartOfAccounts> findEquityAccounts();
    
    /**
     * Find revenue accounts
     */
    @Query("SELECT coa FROM ChartOfAccounts coa WHERE coa.accountType = 'REVENUE' " +
           "AND coa.isActive = true ORDER BY coa.accountCode")
    List<ChartOfAccounts> findRevenueAccounts();
    
    /**
     * Find expense accounts
     */
    @Query("SELECT coa FROM ChartOfAccounts coa WHERE coa.accountType = 'EXPENSE' " +
           "AND coa.isActive = true ORDER BY coa.accountCode")
    List<ChartOfAccounts> findExpenseAccounts();
    
    /**
     * Find root accounts (no parent)
     */
    @Query("SELECT coa FROM ChartOfAccounts coa WHERE coa.parentAccount.id IS NULL " +
           "AND coa.isActive = true ORDER BY coa.accountCode")
    List<ChartOfAccounts> findRootAccounts();
    
    /**
     * Find leaf accounts (not group accounts)
     */
    @Query("SELECT coa FROM ChartOfAccounts coa WHERE coa.isGroupAccount = false " +
           "AND coa.isActive = true ORDER BY coa.accountCode")
    List<ChartOfAccounts> findLeafAccounts();
    
    /**
     * Find accounts by type and category
     */
    List<ChartOfAccounts> findByAccountTypeAndAccountCategory(String accountType, String accountCategory);
    
    /**
     * Get account statistics
     */
    @Query("SELECT " +
           "COUNT(coa) as totalAccounts, " +
           "SUM(CASE WHEN coa.isActive = true THEN 1 ELSE 0 END) as activeAccounts, " +
           "SUM(CASE WHEN coa.isGroupAccount = true THEN 1 ELSE 0 END) as groupAccounts, " +
           "SUM(CASE WHEN coa.isControlAccount = true THEN 1 ELSE 0 END) as controlAccounts " +
           "FROM ChartOfAccounts coa")
    Object getAccountStatistics();
    
    /**
     * Get accounts grouped by type
     */
    @Query("SELECT coa.accountType, COUNT(coa) as accountCount, SUM(coa.currentBalance) as totalBalance " +
           "FROM ChartOfAccounts coa WHERE coa.isActive = true " +
           "GROUP BY coa.accountType ORDER BY accountCount DESC")
    List<Object[]> getAccountsByType();
    
    /**
     * Get accounts grouped by category
     */
    @Query("SELECT coa.accountCategory, COUNT(coa) as accountCount, SUM(coa.currentBalance) as totalBalance " +
           "FROM ChartOfAccounts coa WHERE coa.isActive = true " +
           "GROUP BY coa.accountCategory ORDER BY accountCount DESC")
    List<Object[]> getAccountsByCategory();
    
    /**
     * Find all accounts ordered by code
     */
    List<ChartOfAccounts> findAllByOrderByAccountCodeAsc();
    
    /**
     * Find active accounts ordered by code
     */
    List<ChartOfAccounts> findByIsActiveTrueOrderByAccountCodeAsc();
    
    /**
     * Find accounts by parent ordered by code
     */
    @Query("SELECT coa FROM ChartOfAccounts coa WHERE coa.parentAccount.id = :parentAccountId " +
           "ORDER BY coa.accountCode")
    List<ChartOfAccounts> findByParentOrderByCode(@Param("parentAccountId") Long parentAccountId);
    
    /**
     * Get account hierarchy (recursive)
     */
    @Query("SELECT coa FROM ChartOfAccounts coa WHERE coa.parentAccount.id = :parentAccountId " +
           "AND coa.isActive = true ORDER BY coa.accountCode")
    List<ChartOfAccounts> getAccountHierarchy(@Param("parentAccountId") Long parentAccountId);
@Query("SELECT coa.currentBalance FROM ChartOfAccounts coa WHERE coa.id=:accountId")
    BigDecimal getAccountBalance(@Param("accountId") Long accountId);

    List<ChartOfAccounts> findByParentAccountIsNull();

    List<ChartOfAccounts> findByIsActive(boolean b);
@Query("SELECT coa FROM ChartOfAccounts coa WHERE coa.isGroupAccount = false AND coa.isActive = true ORDER BY coa.accountCode")
    List<ChartOfAccounts> findPostingAccounts();

@Query("SELECT coa.accountType, SUM(coa.currentBalance) FROM ChartOfAccounts coa GROUP BY coa.accountType")
    List<Object[]> getTotalBalanceByAccountType();

@Query("SELECT coa.accountName, coa.currentBalance FROM ChartOfAccounts coa WHERE coa.currentBalance <> 0 ORDER BY coa.accountCode")
    List<Object[]> getTrialBalance();

@Query("SELECT coa.accountName, coa.currentBalance FROM ChartOfAccounts coa WHERE coa.currentBalance <> 0 AND coa.openDate BETWEEN :startDate AND :endDate ORDER BY coa.accountCode")
    List<Object[]> getTrialBalanceForPeriod(@Param("startDate")LocalDate startDate,@Param("endDate") LocalDate endDate);

@Query("SELECT coa.accountName, coa.currentBalance FROM ChartOfAccounts coa WHERE coa.currentBalance <> 0 AND FUNCTION('YEAR', coa.openDate) = :year ORDER BY coa.accountCode")
    List<Object[]> getTrialBalanceForFiscalYear(@Param("year") Integer year);
}

//@Query("SELECT coa FROM ChartOfAccounts coa WHERE coa.isReconsiled = false")
//    List<ChartOfAccounts> findByIsReconsiledFalse();
//}
