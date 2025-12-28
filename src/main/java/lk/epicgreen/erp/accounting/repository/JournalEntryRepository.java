package lk.epicgreen.erp.accounting.repository;

import lk.epicgreen.erp.accounting.entity.JournalEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for JournalEntry entity
 * Based on ACTUAL database schema: journal_entries table
 * 
 * Fields: journal_number, journal_date, period_id (BIGINT),
 *         entry_type (ENUM: MANUAL, AUTOMATED, OPENING_BALANCE, CLOSING, ADJUSTMENT),
 *         source_type, source_id (BIGINT), source_reference, description,
 *         total_debit, total_credit,
 *         status (ENUM: DRAFT, POSTED, CANCELLED),
 *         posted_by (BIGINT), posted_at, approved_by (BIGINT), approved_at
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long>, JpaSpecificationExecutor<JournalEntry> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find journal entry by journal number
     */
    Optional<JournalEntry> findByJournalNumber(String journalNumber);
    
    /**
     * Find journal entries by period
     */
    List<JournalEntry> findByPeriodId(Long periodId);
    
    /**
     * Find journal entries by period with pagination
     */
    Page<JournalEntry> findByPeriodId(Long periodId, Pageable pageable);
    
    /**
     * Find journal entries by status
     */
    List<JournalEntry> findByStatus(String status);
    
    /**
     * Find journal entries by status with pagination
     */
    Page<JournalEntry> findByStatus(String status, Pageable pageable);
    
    /**
     * Find journal entries by entry type
     */
    List<JournalEntry> findByEntryType(String entryType);
    
    /**
     * Find journal entries by entry type with pagination
     */
    Page<JournalEntry> findByEntryType(String entryType, Pageable pageable);
    
    /**
     * Find journal entries by source
     */
    List<JournalEntry> findBySourceTypeAndSourceId(String sourceType, Long sourceId);
    
    /**
     * Find journal entries by journal date
     */
    List<JournalEntry> findByJournalDate(LocalDate journalDate);
    
    /**
     * Find journal entries by journal date range
     */
    List<JournalEntry> findByJournalDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find journal entries by journal date range with pagination
     */
    Page<JournalEntry> findByJournalDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find journal entries posted by user
     */
    List<JournalEntry> findByPostedBy(Long postedBy);
    
    /**
     * Find journal entries approved by user
     */
    List<JournalEntry> findByApprovedBy(Long approvedBy);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if journal number exists
     */
    boolean existsByJournalNumber(String journalNumber);
    
    /**
     * Check if journal number exists excluding specific journal ID
     */
    boolean existsByJournalNumberAndIdNot(String journalNumber, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search journal entries by journal number containing (case-insensitive)
     */
    Page<JournalEntry> findByJournalNumberContainingIgnoreCase(String journalNumber, Pageable pageable);
    
    /**
     * Search journal entries by source reference
     */
    Page<JournalEntry> findBySourceReferenceContainingIgnoreCase(String sourceReference, Pageable pageable);
    
    /**
     * Search journal entries by multiple criteria
     */
    @Query("SELECT je FROM JournalEntry je WHERE " +
           "(:journalNumber IS NULL OR LOWER(je.journalNumber) LIKE LOWER(CONCAT('%', :journalNumber, '%'))) AND " +
           "(:periodId IS NULL OR je.periodId = :periodId) AND " +
           "(:status IS NULL OR je.status = :status) AND " +
           "(:entryType IS NULL OR je.entryType = :entryType) AND " +
           "(:sourceType IS NULL OR je.sourceType = :sourceType) AND " +
           "(:startDate IS NULL OR je.journalDate >= :startDate) AND " +
           "(:endDate IS NULL OR je.journalDate <= :endDate)")
    Page<JournalEntry> searchJournalEntries(
            @Param("journalNumber") String journalNumber,
            @Param("periodId") Long periodId,
            @Param("status") String status,
            @Param("entryType") String entryType,
            @Param("sourceType") String sourceType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count journal entries by status
     */
    long countByStatus(String status);
    
    /**
     * Count journal entries by entry type
     */
    long countByEntryType(String entryType);
    
    /**
     * Count journal entries by period
     */
    long countByPeriodId(Long periodId);
    
    /**
     * Count journal entries in date range
     */
    long countByJournalDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find draft journal entries
     */
    @Query("SELECT je FROM JournalEntry je WHERE je.status = 'DRAFT' ORDER BY je.journalDate DESC")
    List<JournalEntry> findDraftJournalEntries();
    
    /**
     * Find posted journal entries
     */
    @Query("SELECT je FROM JournalEntry je WHERE je.status = 'POSTED' ORDER BY je.journalDate DESC")
    List<JournalEntry> findPostedJournalEntries();
    
    /**
     * Find cancelled journal entries
     */
    @Query("SELECT je FROM JournalEntry je WHERE je.status = 'CANCELLED' ORDER BY je.journalDate DESC")
    List<JournalEntry> findCancelledJournalEntries();
    
    /**
     * Find manual journal entries
     */
    @Query("SELECT je FROM JournalEntry je WHERE je.entryType = 'MANUAL' ORDER BY je.journalDate DESC")
    List<JournalEntry> findManualJournalEntries();
    
    /**
     * Find automated journal entries
     */
    @Query("SELECT je FROM JournalEntry je WHERE je.entryType = 'AUTOMATED' ORDER BY je.journalDate DESC")
    List<JournalEntry> findAutomatedJournalEntries();
    
    /**
     * Find opening balance entries
     */
    @Query("SELECT je FROM JournalEntry je WHERE je.entryType = 'OPENING_BALANCE' ORDER BY je.journalDate")
    List<JournalEntry> findOpeningBalanceEntries();
    
    /**
     * Find closing entries
     */
    @Query("SELECT je FROM JournalEntry je WHERE je.entryType = 'CLOSING' ORDER BY je.journalDate DESC")
    List<JournalEntry> findClosingEntries();
    
    /**
     * Find adjustment entries
     */
    @Query("SELECT je FROM JournalEntry je WHERE je.entryType = 'ADJUSTMENT' ORDER BY je.journalDate DESC")
    List<JournalEntry> findAdjustmentEntries();
    
    /**
     * Find unbalanced journal entries
     */
    @Query("SELECT je FROM JournalEntry je WHERE je.totalDebit <> je.totalCredit ORDER BY je.journalDate")
    List<JournalEntry> findUnbalancedJournalEntries();
    
    /**
     * Find journal entries by period and status
     */
    List<JournalEntry> findByPeriodIdAndStatus(Long periodId, String status);
    
    /**
     * Get total debit for period
     */
    @Query("SELECT SUM(je.totalDebit) FROM JournalEntry je WHERE je.periodId = :periodId " +
           "AND je.status = 'POSTED'")
    BigDecimal getTotalDebitByPeriod(@Param("periodId") Long periodId);
    
    /**
     * Get total credit for period
     */
    @Query("SELECT SUM(je.totalCredit) FROM JournalEntry je WHERE je.periodId = :periodId " +
           "AND je.status = 'POSTED'")
    BigDecimal getTotalCreditByPeriod(@Param("periodId") Long periodId);
    
    /**
     * Get journal entry statistics
     */
    @Query("SELECT " +
           "COUNT(je) as totalEntries, " +
           "SUM(CASE WHEN je.status = 'DRAFT' THEN 1 ELSE 0 END) as draftEntries, " +
           "SUM(CASE WHEN je.status = 'POSTED' THEN 1 ELSE 0 END) as postedEntries, " +
           "SUM(CASE WHEN je.status = 'CANCELLED' THEN 1 ELSE 0 END) as cancelledEntries, " +
           "SUM(je.totalDebit) as totalDebit, " +
           "SUM(je.totalCredit) as totalCredit " +
           "FROM JournalEntry je WHERE je.status = 'POSTED'")
    Object getJournalEntryStatistics();
    
    /**
     * Get journal entries grouped by status
     */
    @Query("SELECT je.status, COUNT(je) as entryCount, SUM(je.totalDebit) as totalDebit " +
           "FROM JournalEntry je GROUP BY je.status ORDER BY entryCount DESC")
    List<Object[]> getJournalEntriesByStatus();
    
    /**
     * Get journal entries grouped by entry type
     */
    @Query("SELECT je.entryType, COUNT(je) as entryCount, SUM(je.totalDebit) as totalDebit " +
           "FROM JournalEntry je GROUP BY je.entryType ORDER BY entryCount DESC")
    List<Object[]> getJournalEntriesByType();
    
    /**
     * Get daily journal entry summary
     */
    @Query("SELECT je.journalDate, COUNT(je) as entryCount, SUM(je.totalDebit) as totalDebit " +
           "FROM JournalEntry je WHERE je.journalDate BETWEEN :startDate AND :endDate " +
           "AND je.status = 'POSTED' GROUP BY je.journalDate ORDER BY je.journalDate DESC")
    List<Object[]> getDailyJournalEntrySummary(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find journal entries posted in time range
     */
    @Query("SELECT je FROM JournalEntry je WHERE je.postedAt BETWEEN :startTime AND :endTime " +
           "ORDER BY je.postedAt DESC")
    List<JournalEntry> findJournalEntriesPostedBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * Find today's journal entries
     */
    @Query("SELECT je FROM JournalEntry je WHERE je.journalDate = CURRENT_DATE ORDER BY je.createdAt DESC")
    List<JournalEntry> findTodayJournalEntries();
    
    /**
     * Find all journal entries ordered by date
     */
    List<JournalEntry> findAllByOrderByJournalDateDescCreatedAtDesc();
}
