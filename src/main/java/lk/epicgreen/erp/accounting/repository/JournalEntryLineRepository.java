package lk.epicgreen.erp.accounting.repository;

import lk.epicgreen.erp.accounting.entity.JournalEntryLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for JournalEntryLine entity
 * Based on ACTUAL database schema: journal_entry_lines table
 * 
 * Fields: journal_id (BIGINT), line_number, account_id (BIGINT),
 *         debit_amount, credit_amount, description,
 *         cost_center, dimension1, dimension2
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface JournalEntryLineRepository extends JpaRepository<JournalEntryLine, Long>, JpaSpecificationExecutor<JournalEntryLine> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find all lines for a journal entry
     */
    List<JournalEntryLine> findByJournalId(Long journalId);
    
    /**
     * Find all lines for a journal entry ordered by line number
     */
    List<JournalEntryLine> findByJournalIdOrderByLineNumberAsc(Long journalId);
    
    /**
     * Find all lines for an account
     */
    List<JournalEntryLine> findByAccountId(Long accountId);
    
    /**
     * Find all lines for an account with pagination
     */
    Page<JournalEntryLine> findByAccountId(Long accountId, Pageable pageable);
    
    /**
     * Find lines by journal and account
     */
    List<JournalEntryLine> findByJournalIdAndAccountId(Long journalId, Long accountId);
    
    /**
     * Find lines by cost center
     */
    List<JournalEntryLine> findByCostCenter(String costCenter);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count lines for a journal entry
     */
    long countByJournalId(Long journalId);
    
    /**
     * Count lines for an account
     */
    long countByAccountId(Long accountId);
    
    // ==================== DELETE METHODS ====================
    
    /**
     * Delete all lines for a journal entry
     */
    @Modifying
    @Query("DELETE FROM JournalEntryLine jel WHERE jel.journalId = :journalId")
    void deleteAllByJournalId(@Param("journalId") Long journalId);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Get total debit for a journal entry
     */
    @Query("SELECT SUM(jel.debitAmount) FROM JournalEntryLine jel WHERE jel.journalId = :journalId")
    BigDecimal getTotalDebitByJournal(@Param("journalId") Long journalId);
    
    /**
     * Get total credit for a journal entry
     */
    @Query("SELECT SUM(jel.creditAmount) FROM JournalEntryLine jel WHERE jel.journalId = :journalId")
    BigDecimal getTotalCreditByJournal(@Param("journalId") Long journalId);
    
    /**
     * Get total debit for an account
     */
    @Query("SELECT SUM(jel.debitAmount) FROM JournalEntryLine jel WHERE jel.accountId = :accountId")
    BigDecimal getTotalDebitByAccount(@Param("accountId") Long accountId);
    
    /**
     * Get total credit for an account
     */
    @Query("SELECT SUM(jel.creditAmount) FROM JournalEntryLine jel WHERE jel.accountId = :accountId")
    BigDecimal getTotalCreditByAccount(@Param("accountId") Long accountId);
    
    /**
     * Find debit lines for a journal
     */
    @Query("SELECT jel FROM JournalEntryLine jel WHERE jel.journalId = :journalId " +
           "AND jel.debitAmount > 0 ORDER BY jel.lineNumber")
    List<JournalEntryLine> findDebitLinesByJournal(@Param("journalId") Long journalId);
    
    /**
     * Find credit lines for a journal
     */
    @Query("SELECT jel FROM JournalEntryLine jel WHERE jel.journalId = :journalId " +
           "AND jel.creditAmount > 0 ORDER BY jel.lineNumber")
    List<JournalEntryLine> findCreditLinesByJournal(@Param("journalId") Long journalId);
    
    /**
     * Get journal entry line statistics by account
     */
    @Query("SELECT jel.accountId, COUNT(jel) as lineCount, " +
           "SUM(jel.debitAmount) as totalDebit, SUM(jel.creditAmount) as totalCredit " +
           "FROM JournalEntryLine jel GROUP BY jel.accountId ORDER BY lineCount DESC")
    List<Object[]> getJournalEntryLineStatisticsByAccount();
    
    /**
     * Find all lines ordered by journal
     */
    @Query("SELECT jel FROM JournalEntryLine jel ORDER BY jel.journalId, jel.lineNumber")
    List<JournalEntryLine> findAllOrderedByJournal();
}
