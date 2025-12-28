package lk.epicgreen.erp.accounting.repository;

import lk.epicgreen.erp.accounting.entity.GeneralLedger;
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

/**
 * Repository interface for GeneralLedger entity
 * Based on ACTUAL database schema: general_ledger table
 * 
 * Fields: transaction_date, period_id (BIGINT), account_id (BIGINT),
 *         journal_id (BIGINT), journal_line_id (BIGINT), description,
 *         debit_amount, credit_amount, balance,
 *         source_type, source_id (BIGINT)
 * 
 * NOTE: This is typically an IMMUTABLE ledger - no update/delete methods
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface GeneralLedgerRepository extends JpaRepository<GeneralLedger, Long>, JpaSpecificationExecutor<GeneralLedger> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find all entries for an account
     */
    List<GeneralLedger> findByAccountId(Long accountId);
    
    /**
     * Find all entries for an account with pagination
     */
    Page<GeneralLedger> findByAccountId(Long accountId, Pageable pageable);
    
    /**
     * Find all entries for an account ordered by transaction date
     */
    List<GeneralLedger> findByAccountIdOrderByTransactionDateAscCreatedAtAsc(Long accountId);
    
    /**
     * Find all entries for a period
     */
    List<GeneralLedger> findByPeriodId(Long periodId);
    
    /**
     * Find all entries for a period with pagination
     */
    Page<GeneralLedger> findByPeriodId(Long periodId, Pageable pageable);
    
    /**
     * Find all entries for a journal
     */
    List<GeneralLedger> findByJournalId(Long journalId);
    
    /**
     * Find entries by transaction date
     */
    List<GeneralLedger> findByTransactionDate(LocalDate transactionDate);
    
    /**
     * Find entries by transaction date range
     */
    List<GeneralLedger> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find entries by transaction date range with pagination
     */
    Page<GeneralLedger> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find entries by source
     */
    List<GeneralLedger> findBySourceTypeAndSourceId(String sourceType, Long sourceId);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count entries for an account
     */
    long countByAccountId(Long accountId);
    
    /**
     * Count entries for a period
     */
    long countByPeriodId(Long periodId);
    
    /**
     * Count entries in date range
     */
    long countByTransactionDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Get current balance for an account
     */
    @Query("SELECT gl.balance FROM GeneralLedger gl WHERE gl.accountId = :accountId " +
           "ORDER BY gl.transactionDate DESC, gl.createdAt DESC LIMIT 1")
    BigDecimal getCurrentBalanceByAccount(@Param("accountId") Long accountId);
    
    /**
     * Get total debit for an account
     */
    @Query("SELECT SUM(gl.debitAmount) FROM GeneralLedger gl WHERE gl.accountId = :accountId")
    BigDecimal getTotalDebitByAccount(@Param("accountId") Long accountId);
    
    /**
     * Get total credit for an account
     */
    @Query("SELECT SUM(gl.creditAmount) FROM GeneralLedger gl WHERE gl.accountId = :accountId")
    BigDecimal getTotalCreditByAccount(@Param("accountId") Long accountId);
    
    /**
     * Get total debit for an account in period
     */
    @Query("SELECT SUM(gl.debitAmount) FROM GeneralLedger gl WHERE gl.accountId = :accountId " +
           "AND gl.periodId = :periodId")
    BigDecimal getTotalDebitByAccountAndPeriod(@Param("accountId") Long accountId, @Param("periodId") Long periodId);
    
    /**
     * Get total credit for an account in period
     */
    @Query("SELECT SUM(gl.creditAmount) FROM GeneralLedger gl WHERE gl.accountId = :accountId " +
           "AND gl.periodId = :periodId")
    BigDecimal getTotalCreditByAccountAndPeriod(@Param("accountId") Long accountId, @Param("periodId") Long periodId);
    
    /**
     * Get account statement
     */
    @Query("SELECT gl FROM GeneralLedger gl WHERE gl.accountId = :accountId " +
           "AND gl.transactionDate BETWEEN :startDate AND :endDate " +
           "ORDER BY gl.transactionDate ASC, gl.createdAt ASC")
    List<GeneralLedger> getAccountStatement(
            @Param("accountId") Long accountId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find entries by account and period
     */
    List<GeneralLedger> findByAccountIdAndPeriodId(Long accountId, Long periodId);
    
    /**
     * Get general ledger statistics
     */
    @Query("SELECT " +
           "COUNT(gl) as totalEntries, " +
           "SUM(gl.debitAmount) as totalDebit, " +
           "SUM(gl.creditAmount) as totalCredit, " +
           "COUNT(DISTINCT gl.accountId) as uniqueAccounts " +
           "FROM GeneralLedger gl")
    Object getGeneralLedgerStatistics();
    
    /**
     * Get entries grouped by account
     */
    @Query("SELECT gl.accountId, COUNT(gl) as entryCount, " +
           "SUM(gl.debitAmount) as totalDebit, SUM(gl.creditAmount) as totalCredit " +
           "FROM GeneralLedger gl GROUP BY gl.accountId ORDER BY entryCount DESC")
    List<Object[]> getEntriesByAccount();
    
    /**
     * Find today's entries
     */
    @Query("SELECT gl FROM GeneralLedger gl WHERE gl.transactionDate = CURRENT_DATE " +
           "ORDER BY gl.createdAt DESC")
    List<GeneralLedger> findTodayEntries();
}
