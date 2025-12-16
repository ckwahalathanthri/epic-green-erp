package lk.epicgreen.erp.accounting.repository;

import lk.epicgreen.erp.accounting.entity.GeneralLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * GeneralLedger Repository
 * Repository for general ledger data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface GeneralLedgerRepository extends JpaRepository<GeneralLedger, Long> {
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find ledger entries by account ID
     */
    List<GeneralLedger> findByAccountId(Long accountId);
    
    /**
     * Find ledger entries by account ID with pagination
     */
    Page<GeneralLedger> findByAccountId(Long accountId, Pageable pageable);
    
    /**
     * Find ledger entries by journal entry ID
     */
    List<GeneralLedger> findByJournalEntryId(Long journalEntryId);
    
    /**
     * Find ledger entries by entry type
     */
    List<GeneralLedger> findByEntryType(String entryType);
    
    /**
     * Find ledger entries by entry type with pagination
     */
    Page<GeneralLedger> findByEntryType(String entryType, Pageable pageable);
    
    /**
     * Find ledger entries by fiscal year
     */
    List<GeneralLedger> findByFiscalYear(Integer fiscalYear);
    
    /**
     * Find ledger entries by fiscal year with pagination
     */
    Page<GeneralLedger> findByFiscalYear(Integer fiscalYear, Pageable pageable);
    
    /**
     * Find ledger entries by fiscal period
     */
    List<GeneralLedger> findByFiscalPeriod(Integer fiscalPeriod);
    
    /**
     * Find ledger entries by is debit
     */
    List<GeneralLedger> findByIsDebit(Boolean isDebit);
    
    /**
     * Find ledger entries by is posted
     */
    List<GeneralLedger> findByIsPosted(Boolean isPosted);
    
    /**
     * Find ledger entries by is reversed
     */
    List<GeneralLedger> findByIsReversed(Boolean isReversed);
    
    /**
     * Find ledger entries by reference type
     */
    List<GeneralLedger> findByReferenceType(String referenceType);
    
    /**
     * Find ledger entries by reference ID
     */
    List<GeneralLedger> findByReferenceId(Long referenceId);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find ledger entries by transaction date between dates
     */
    List<GeneralLedger> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find ledger entries by transaction date between dates with pagination
     */
    Page<GeneralLedger> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find ledger entries by posting date between dates
     */
    List<GeneralLedger> findByPostingDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find ledger entries by created at between dates
     */
    List<GeneralLedger> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find ledger entries by account ID and fiscal year
     */
    List<GeneralLedger> findByAccountIdAndFiscalYear(Long accountId, Integer fiscalYear);
    
    /**
     * Find ledger entries by account ID and fiscal year with pagination
     */
    Page<GeneralLedger> findByAccountIdAndFiscalYear(Long accountId, Integer fiscalYear, Pageable pageable);
    
    /**
     * Find ledger entries by account ID and fiscal year and period
     */
    List<GeneralLedger> findByAccountIdAndFiscalYearAndFiscalPeriod(Long accountId, Integer fiscalYear, Integer fiscalPeriod);
    
    /**
     * Find ledger entries by account ID and date range
     */
    @Query("SELECT g FROM GeneralLedger g WHERE g.accountId = :accountId " +
           "AND g.transactionDate BETWEEN :startDate AND :endDate " +
           "ORDER BY g.transactionDate, g.createdAt")
    List<GeneralLedger> findByAccountIdAndDateRange(@Param("accountId") Long accountId,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);
    
    /**
     * Find ledger entries by reference type and reference ID
     */
    List<GeneralLedger> findByReferenceTypeAndReferenceId(String referenceType, Long referenceId);
    
    /**
     * Find ledger entries by fiscal year and fiscal period
     */
    List<GeneralLedger> findByFiscalYearAndFiscalPeriod(Integer fiscalYear, Integer fiscalPeriod);
    
    /**
     * Find ledger entries by is posted and fiscal year
     */
    List<GeneralLedger> findByIsPostedAndFiscalYear(Boolean isPosted, Integer fiscalYear);
    
    // ===================================================================
    // CUSTOM QUERIES - ACCOUNT LEDGER
    // ===================================================================
    
    /**
     * Get account ledger (ordered by date)
     */
    @Query("SELECT g FROM GeneralLedger g WHERE g.accountId = :accountId " +
           "ORDER BY g.transactionDate, g.createdAt")
    List<GeneralLedger> getAccountLedger(@Param("accountId") Long accountId);
    
    /**
     * Get account ledger with pagination
     */
    @Query("SELECT g FROM GeneralLedger g WHERE g.accountId = :accountId " +
           "ORDER BY g.transactionDate DESC, g.createdAt DESC")
    Page<GeneralLedger> getAccountLedger(@Param("accountId") Long accountId, Pageable pageable);
    
    /**
     * Get account ledger for date range
     */
    @Query("SELECT g FROM GeneralLedger g WHERE g.accountId = :accountId " +
           "AND g.transactionDate BETWEEN :startDate AND :endDate " +
           "AND g.isPosted = true " +
           "ORDER BY g.transactionDate, g.createdAt")
    List<GeneralLedger> getAccountLedgerForPeriod(@Param("accountId") Long accountId,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);
    
    /**
     * Get posted ledger entries for account
     */
    @Query("SELECT g FROM GeneralLedger g WHERE g.accountId = :accountId " +
           "AND g.isPosted = true " +
           "ORDER BY g.transactionDate, g.createdAt")
    List<GeneralLedger> getPostedAccountLedger(@Param("accountId") Long accountId);
    
    // ===================================================================
    // CUSTOM QUERIES - BALANCE CALCULATIONS
    // ===================================================================
    
    /**
     * Get account balance
     */
    @Query("SELECT " +
           "COALESCE(SUM(CASE WHEN g.isDebit = true THEN g.amount ELSE 0 END), 0) - " +
           "COALESCE(SUM(CASE WHEN g.isDebit = false THEN g.amount ELSE 0 END), 0) " +
           "FROM GeneralLedger g WHERE g.accountId = :accountId AND g.isPosted = true")
    Double getAccountBalance(@Param("accountId") Long accountId);
    
    /**
     * Get account balance for date range
     */
    @Query("SELECT " +
           "COALESCE(SUM(CASE WHEN g.isDebit = true THEN g.amount ELSE 0 END), 0) - " +
           "COALESCE(SUM(CASE WHEN g.isDebit = false THEN g.amount ELSE 0 END), 0) " +
           "FROM GeneralLedger g WHERE g.accountId = :accountId " +
           "AND g.transactionDate BETWEEN :startDate AND :endDate " +
           "AND g.isPosted = true")
    Double getAccountBalanceForPeriod(@Param("accountId") Long accountId,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);
    
    /**
     * Get account balance up to date
     */
    @Query("SELECT " +
           "COALESCE(SUM(CASE WHEN g.isDebit = true THEN g.amount ELSE 0 END), 0) - " +
           "COALESCE(SUM(CASE WHEN g.isDebit = false THEN g.amount ELSE 0 END), 0) " +
           "FROM GeneralLedger g WHERE g.accountId = :accountId " +
           "AND g.transactionDate <= :date " +
           "AND g.isPosted = true")
    Double getAccountBalanceUpToDate(@Param("accountId") Long accountId,
                                     @Param("date") LocalDate date);
    
    /**
     * Get total debit for account
     */
    @Query("SELECT COALESCE(SUM(g.amount), 0) FROM GeneralLedger g " +
           "WHERE g.accountId = :accountId AND g.isDebit = true AND g.isPosted = true")
    Double getTotalDebitForAccount(@Param("accountId") Long accountId);
    
    /**
     * Get total credit for account
     */
    @Query("SELECT COALESCE(SUM(g.amount), 0) FROM GeneralLedger g " +
           "WHERE g.accountId = :accountId AND g.isDebit = false AND g.isPosted = true")
    Double getTotalCreditForAccount(@Param("accountId") Long accountId);
    
    /**
     * Get total debit for account in period
     */
    @Query("SELECT COALESCE(SUM(g.amount), 0) FROM GeneralLedger g " +
           "WHERE g.accountId = :accountId AND g.isDebit = true " +
           "AND g.transactionDate BETWEEN :startDate AND :endDate " +
           "AND g.isPosted = true")
    Double getTotalDebitForPeriod(@Param("accountId") Long accountId,
                                  @Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate);
    
    /**
     * Get total credit for account in period
     */
    @Query("SELECT COALESCE(SUM(g.amount), 0) FROM GeneralLedger g " +
           "WHERE g.accountId = :accountId AND g.isDebit = false " +
           "AND g.transactionDate BETWEEN :startDate AND :endDate " +
           "AND g.isPosted = true")
    Double getTotalCreditForPeriod(@Param("accountId") Long accountId,
                                   @Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate);
    
    // ===================================================================
    // CUSTOM QUERIES - SEARCH & FILTER
    // ===================================================================
    
    /**
     * Search ledger entries
     */
    @Query("SELECT g FROM GeneralLedger g WHERE " +
           "LOWER(g.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(g.notes) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(g.referenceNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<GeneralLedger> searchLedgerEntries(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find recent ledger entries
     */
    @Query("SELECT g FROM GeneralLedger g ORDER BY g.transactionDate DESC, g.createdAt DESC")
    List<GeneralLedger> findRecentEntries(Pageable pageable);
    
    /**
     * Find posted ledger entries
     */
    @Query("SELECT g FROM GeneralLedger g WHERE g.isPosted = true " +
           "ORDER BY g.postingDate DESC")
    List<GeneralLedger> findPostedEntries();
    
    /**
     * Find posted ledger entries with pagination
     */
    @Query("SELECT g FROM GeneralLedger g WHERE g.isPosted = true " +
           "ORDER BY g.postingDate DESC")
    Page<GeneralLedger> findPostedEntries(Pageable pageable);
    
    /**
     * Find unposted ledger entries
     */
    @Query("SELECT g FROM GeneralLedger g WHERE g.isPosted = false " +
           "ORDER BY g.transactionDate DESC")
    List<GeneralLedger> findUnpostedEntries();
    
    /**
     * Find reversed ledger entries
     */
    @Query("SELECT g FROM GeneralLedger g WHERE g.isReversed = true " +
           "ORDER BY g.reversalDate DESC")
    List<GeneralLedger> findReversedEntries();
    
    /**
     * Find ledger entries by fiscal year and period ordered
     */
    @Query("SELECT g FROM GeneralLedger g WHERE g.fiscalYear = :year AND g.fiscalPeriod = :period " +
           "ORDER BY g.transactionDate, g.accountId")
    List<GeneralLedger> findByFiscalYearAndPeriodOrdered(@Param("year") Integer year, 
                                                         @Param("period") Integer period);
    
    // ===================================================================
    // CUSTOM QUERIES - TRIAL BALANCE
    // ===================================================================
    
    /**
     * Get trial balance
     */
    @Query("SELECT g.accountId, " +
           "SUM(CASE WHEN g.isDebit = true THEN g.amount ELSE 0 END) as totalDebit, " +
           "SUM(CASE WHEN g.isDebit = false THEN g.amount ELSE 0 END) as totalCredit " +
           "FROM GeneralLedger g WHERE g.isPosted = true " +
           "GROUP BY g.accountId")
    List<Object[]> getTrialBalance();
    
    /**
     * Get trial balance for date range
     */
    @Query("SELECT g.accountId, " +
           "SUM(CASE WHEN g.isDebit = true THEN g.amount ELSE 0 END) as totalDebit, " +
           "SUM(CASE WHEN g.isDebit = false THEN g.amount ELSE 0 END) as totalCredit " +
           "FROM GeneralLedger g WHERE g.isPosted = true " +
           "AND g.transactionDate BETWEEN :startDate AND :endDate " +
           "GROUP BY g.accountId")
    List<Object[]> getTrialBalanceForPeriod(@Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);
    
    /**
     * Get trial balance for fiscal year
     */
    @Query("SELECT g.accountId, " +
           "SUM(CASE WHEN g.isDebit = true THEN g.amount ELSE 0 END) as totalDebit, " +
           "SUM(CASE WHEN g.isDebit = false THEN g.amount ELSE 0 END) as totalCredit " +
           "FROM GeneralLedger g WHERE g.isPosted = true " +
           "AND g.fiscalYear = :year " +
           "GROUP BY g.accountId")
    List<Object[]> getTrialBalanceForFiscalYear(@Param("year") Integer year);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count ledger entries by account
     */
    @Query("SELECT COUNT(g) FROM GeneralLedger g WHERE g.accountId = :accountId")
    Long countByAccountId(@Param("accountId") Long accountId);
    
    /**
     * Count posted entries
     */
    @Query("SELECT COUNT(g) FROM GeneralLedger g WHERE g.isPosted = true")
    Long countPostedEntries();
    
    /**
     * Count unposted entries
     */
    @Query("SELECT COUNT(g) FROM GeneralLedger g WHERE g.isPosted = false")
    Long countUnpostedEntries();
    
    /**
     * Count reversed entries
     */
    @Query("SELECT COUNT(g) FROM GeneralLedger g WHERE g.isReversed = true")
    Long countReversedEntries();
    
    /**
     * Get entry type distribution
     */
    @Query("SELECT g.entryType, COUNT(g) as entryCount FROM GeneralLedger g " +
           "GROUP BY g.entryType ORDER BY entryCount DESC")
    List<Object[]> getEntryTypeDistribution();
    
    /**
     * Get most active accounts
     */
    @Query("SELECT g.accountId, COUNT(g) as entryCount FROM GeneralLedger g " +
           "GROUP BY g.accountId ORDER BY entryCount DESC")
    List<Object[]> getMostActiveAccounts();
    
    /**
     * Get total posted amount
     */
    @Query("SELECT SUM(g.amount) FROM GeneralLedger g WHERE g.isPosted = true")
    Double getTotalPostedAmount();
    
    /**
     * Find ledger entries by tags
     */
    @Query("SELECT g FROM GeneralLedger g WHERE g.tags LIKE CONCAT('%', :tag, '%')")
    List<GeneralLedger> findByTag(@Param("tag") String tag);
}
