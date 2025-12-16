package lk.epicgreen.erp.accounting.repository;

import lk.epicgreen.erp.accounting.entity.JournalEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * JournalEntry Repository
 * Repository for journal entry data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find journal entry by entry number
     */
    Optional<JournalEntry> findByEntryNumber(String entryNumber);
    
    /**
     * Check if journal entry exists by entry number
     */
    boolean existsByEntryNumber(String entryNumber);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find journal entries by entry type
     */
    List<JournalEntry> findByEntryType(String entryType);
    
    /**
     * Find journal entries by entry type with pagination
     */
    Page<JournalEntry> findByEntryType(String entryType, Pageable pageable);
    
    /**
     * Find journal entries by status
     */
    List<JournalEntry> findByStatus(String status);
    
    /**
     * Find journal entries by status with pagination
     */
    Page<JournalEntry> findByStatus(String status, Pageable pageable);
    
    /**
     * Find journal entries by fiscal year
     */
    List<JournalEntry> findByFiscalYear(Integer fiscalYear);
    
    /**
     * Find journal entries by fiscal year with pagination
     */
    Page<JournalEntry> findByFiscalYear(Integer fiscalYear, Pageable pageable);
    
    /**
     * Find journal entries by fiscal period
     */
    List<JournalEntry> findByFiscalPeriod(Integer fiscalPeriod);
    
    /**
     * Find journal entries by created by user
     */
    List<JournalEntry> findByCreatedByUserId(Long userId);
    
    /**
     * Find journal entries by created by user with pagination
     */
    Page<JournalEntry> findByCreatedByUserId(Long userId, Pageable pageable);
    
    /**
     * Find journal entries by posted by user
     */
    List<JournalEntry> findByPostedByUserId(Long userId);
    
    /**
     * Find journal entries by is posted
     */
    List<JournalEntry> findByIsPosted(Boolean isPosted);
    
    /**
     * Find journal entries by is posted with pagination
     */
    Page<JournalEntry> findByIsPosted(Boolean isPosted, Pageable pageable);
    
    /**
     * Find journal entries by is reversed
     */
    List<JournalEntry> findByIsReversed(Boolean isReversed);
    
    /**
     * Find journal entries by is balanced
     */
    List<JournalEntry> findByIsBalanced(Boolean isBalanced);
    
    /**
     * Find journal entries by reference type
     */
    List<JournalEntry> findByReferenceType(String referenceType);
    
    /**
     * Find journal entries by reference ID
     */
    List<JournalEntry> findByReferenceId(Long referenceId);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find journal entries by entry date between dates
     */
    List<JournalEntry> findByEntryDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find journal entries by entry date between dates with pagination
     */
    Page<JournalEntry> findByEntryDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find journal entries by posting date between dates
     */
    List<JournalEntry> findByPostingDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find journal entries by created at between dates
     */
    List<JournalEntry> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find journal entries by entry type and status
     */
    List<JournalEntry> findByEntryTypeAndStatus(String entryType, String status);
    
    /**
     * Find journal entries by entry type and status with pagination
     */
    Page<JournalEntry> findByEntryTypeAndStatus(String entryType, String status, Pageable pageable);
    
    /**
     * Find journal entries by fiscal year and fiscal period
     */
    List<JournalEntry> findByFiscalYearAndFiscalPeriod(Integer fiscalYear, Integer fiscalPeriod);
    
    /**
     * Find journal entries by fiscal year and status
     */
    List<JournalEntry> findByFiscalYearAndStatus(Integer fiscalYear, String status);
    
    /**
     * Find journal entries by reference type and reference ID
     */
    List<JournalEntry> findByReferenceTypeAndReferenceId(String referenceType, Long referenceId);
    
    /**
     * Find journal entries by is posted and fiscal year
     */
    List<JournalEntry> findByIsPostedAndFiscalYear(Boolean isPosted, Integer fiscalYear);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Find journal entries by description pattern
     */
    @Query("SELECT j FROM JournalEntry j WHERE LOWER(j.description) LIKE LOWER(CONCAT('%', :description, '%'))")
    List<JournalEntry> findByDescriptionContaining(@Param("description") String description);
    
    /**
     * Search journal entries
     */
    @Query("SELECT j FROM JournalEntry j WHERE " +
           "LOWER(j.entryNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(j.notes) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<JournalEntry> searchJournalEntries(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find draft journal entries
     */
    @Query("SELECT j FROM JournalEntry j WHERE j.status = 'DRAFT' " +
           "ORDER BY j.entryDate DESC")
    List<JournalEntry> findDraftEntries();
    
    /**
     * Find draft journal entries with pagination
     */
    @Query("SELECT j FROM JournalEntry j WHERE j.status = 'DRAFT' " +
           "ORDER BY j.entryDate DESC")
    Page<JournalEntry> findDraftEntries(Pageable pageable);
    
    /**
     * Find posted journal entries
     */
    @Query("SELECT j FROM JournalEntry j WHERE j.status = 'POSTED' AND j.isPosted = true " +
           "ORDER BY j.postingDate DESC")
    List<JournalEntry> findPostedEntries();
    
    /**
     * Find posted journal entries with pagination
     */
    @Query("SELECT j FROM JournalEntry j WHERE j.status = 'POSTED' AND j.isPosted = true " +
           "ORDER BY j.postingDate DESC")
    Page<JournalEntry> findPostedEntries(Pageable pageable);
    
    /**
     * Find unposted journal entries
     */
    @Query("SELECT j FROM JournalEntry j WHERE j.isPosted = false " +
           "ORDER BY j.entryDate DESC")
    List<JournalEntry> findUnpostedEntries();
    
    /**
     * Find reversed journal entries
     */
    @Query("SELECT j FROM JournalEntry j WHERE j.isReversed = true " +
           "ORDER BY j.reversalDate DESC")
    List<JournalEntry> findReversedEntries();
    
    /**
     * Find unbalanced journal entries
     */
    @Query("SELECT j FROM JournalEntry j WHERE j.isBalanced = false")
    List<JournalEntry> findUnbalancedEntries();
    
    /**
     * Find recent journal entries
     */
    @Query("SELECT j FROM JournalEntry j ORDER BY j.entryDate DESC, j.createdAt DESC")
    List<JournalEntry> findRecentEntries(Pageable pageable);
    
    /**
     * Find user's recent journal entries
     */
    @Query("SELECT j FROM JournalEntry j WHERE j.createdByUserId = :userId " +
           "ORDER BY j.entryDate DESC, j.createdAt DESC")
    List<JournalEntry> findUserRecentEntries(@Param("userId") Long userId, Pageable pageable);
    
    /**
     * Find journal entries by fiscal year and period ordered
     */
    @Query("SELECT j FROM JournalEntry j WHERE j.fiscalYear = :year AND j.fiscalPeriod = :period " +
           "ORDER BY j.entryDate, j.entryNumber")
    List<JournalEntry> findByFiscalYearAndPeriodOrdered(@Param("year") Integer year, 
                                                        @Param("period") Integer period);
    
    /**
     * Find journal entries by date range and status
     */
    @Query("SELECT j FROM JournalEntry j WHERE j.entryDate BETWEEN :startDate AND :endDate " +
           "AND j.status = :status ORDER BY j.entryDate")
    List<JournalEntry> findByDateRangeAndStatus(@Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate,
                                                @Param("status") String status);
    
    /**
     * Find journal entries pending approval
     */
    @Query("SELECT j FROM JournalEntry j WHERE j.status = 'PENDING_APPROVAL' " +
           "ORDER BY j.createdAt ASC")
    List<JournalEntry> findEntriesPendingApproval();
    
    /**
     * Find journal entries requiring review
     */
    @Query("SELECT j FROM JournalEntry j WHERE j.requiresApproval = true " +
           "AND j.approvedByUserId IS NULL AND j.status <> 'REJECTED' " +
           "ORDER BY j.createdAt ASC")
    List<JournalEntry> findEntriesRequiringReview();
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count journal entries by entry type
     */
    @Query("SELECT COUNT(j) FROM JournalEntry j WHERE j.entryType = :entryType")
    Long countByEntryType(@Param("entryType") String entryType);
    
    /**
     * Count journal entries by status
     */
    @Query("SELECT COUNT(j) FROM JournalEntry j WHERE j.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count posted entries
     */
    @Query("SELECT COUNT(j) FROM JournalEntry j WHERE j.isPosted = true")
    Long countPostedEntries();
    
    /**
     * Count unposted entries
     */
    @Query("SELECT COUNT(j) FROM JournalEntry j WHERE j.isPosted = false")
    Long countUnpostedEntries();
    
    /**
     * Count reversed entries
     */
    @Query("SELECT COUNT(j) FROM JournalEntry j WHERE j.isReversed = true")
    Long countReversedEntries();
    
    /**
     * Count unbalanced entries
     */
    @Query("SELECT COUNT(j) FROM JournalEntry j WHERE j.isBalanced = false")
    Long countUnbalancedEntries();
    
    /**
     * Get entry type distribution
     */
    @Query("SELECT j.entryType, COUNT(j) as entryCount FROM JournalEntry j " +
           "GROUP BY j.entryType ORDER BY entryCount DESC")
    List<Object[]> getEntryTypeDistribution();
    
    /**
     * Get status distribution
     */
    @Query("SELECT j.status, COUNT(j) as entryCount FROM JournalEntry j " +
           "GROUP BY j.status ORDER BY entryCount DESC")
    List<Object[]> getStatusDistribution();
    
    /**
     * Get monthly entry count
     */
    @Query("SELECT YEAR(j.entryDate) as year, MONTH(j.entryDate) as month, COUNT(j) as entryCount " +
           "FROM JournalEntry j WHERE j.entryDate BETWEEN :startDate AND :endDate " +
           "GROUP BY YEAR(j.entryDate), MONTH(j.entryDate) " +
           "ORDER BY year, month")
    List<Object[]> getMonthlyEntryCount(@Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);
    
    /**
     * Get most active users
     */
    @Query("SELECT j.createdByUsername, COUNT(j) as entryCount FROM JournalEntry j " +
           "WHERE j.createdByUsername IS NOT NULL " +
           "GROUP BY j.createdByUsername ORDER BY entryCount DESC")
    List<Object[]> getMostActiveUsers();
    
    /**
     * Get total debit by fiscal year
     */
    @Query("SELECT SUM(j.totalDebit) FROM JournalEntry j WHERE j.fiscalYear = :year " +
           "AND j.isPosted = true")
    Double getTotalDebitByFiscalYear(@Param("year") Integer year);
    
    /**
     * Get total credit by fiscal year
     */
    @Query("SELECT SUM(j.totalCredit) FROM JournalEntry j WHERE j.fiscalYear = :year " +
           "AND j.isPosted = true")
    Double getTotalCreditByFiscalYear(@Param("year") Integer year);
    
    /**
     * Find entries by tags
     */
    @Query("SELECT j FROM JournalEntry j WHERE j.tags LIKE CONCAT('%', :tag, '%')")
    List<JournalEntry> findByTag(@Param("tag") String tag);
    
    /**
     * Find entries with attachments
     */
    @Query("SELECT j FROM JournalEntry j WHERE j.attachmentCount > 0")
    List<JournalEntry> findEntriesWithAttachments();
}
