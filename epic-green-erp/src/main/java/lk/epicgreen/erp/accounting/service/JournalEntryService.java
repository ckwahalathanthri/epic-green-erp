package lk.epicgreen.erp.accounting.service;

import lk.epicgreen.erp.accounting.dto.JournalEntryRequest;
import lk.epicgreen.erp.accounting.entity.JournalEntry;
import lk.epicgreen.erp.accounting.entity.JournalEntryLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * JournalEntry Service Interface
 * Service for journal entry operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface JournalEntryService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    JournalEntry createJournalEntry(JournalEntryRequest request);
    JournalEntry updateJournalEntry(Long id, JournalEntryRequest request);
    void deleteJournalEntry(Long id);
    JournalEntry getJournalEntryById(Long id);
    JournalEntry getJournalEntryByNumber(String entryNumber);
    List<JournalEntry> getAllJournalEntries();
    Page<JournalEntry> getAllJournalEntries(Pageable pageable);
    Page<JournalEntry> searchJournalEntries(String keyword, Pageable pageable);
    
    // ===================================================================
    // JOURNAL ENTRY STATUS OPERATIONS
    // ===================================================================
    
    JournalEntry postJournalEntry(Long id);
    JournalEntry unpostJournalEntry(Long id);
    JournalEntry reverseJournalEntry(Long id, String reason);
    JournalEntry approveJournalEntry(Long id, Long approvedBy);
    JournalEntry rejectJournalEntry(Long id, String reason);
    JournalEntry submitForApproval(Long id);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    List<JournalEntry> getDraftEntries();
    List<JournalEntry> getPostedEntries();
    List<JournalEntry> getUnpostedEntries();
    List<JournalEntry> getReversedEntries();
    List<JournalEntry> getUnbalancedEntries();
    List<JournalEntry> getEntriesPendingApproval();
    List<JournalEntry> getEntriesRequiringReview();
    List<JournalEntry> getEntriesByFiscalPeriod(Integer year, Integer period);
    List<JournalEntry> getEntriesByDateRange(LocalDate startDate, LocalDate endDate);
    
    // ===================================================================
    // JOURNAL ENTRY LINE OPERATIONS
    // ===================================================================
    
    JournalEntryLine addJournalEntryLine(Long entryId, JournalEntryLine line);
    JournalEntryLine updateJournalEntryLine(Long lineId, JournalEntryLine line);
    void deleteJournalEntryLine(Long lineId);
    List<JournalEntryLine> getJournalEntryLines(Long entryId);
    
    // ===================================================================
    // VALIDATION & BALANCE
    // ===================================================================
    
    boolean validateJournalEntry(JournalEntry entry);
    boolean isBalanced(JournalEntry entry);
    Map<String, Object> getJournalEntryBalance(Long entryId);
    void recalculateJournalEntryTotals(Long entryId);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<JournalEntry> createBulkJournalEntries(List<JournalEntryRequest> requests);
    int postBulkJournalEntries(List<Long> entryIds);
    int deleteBulkJournalEntries(List<Long> entryIds);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    Map<String, Object> getJournalEntryStatistics();
    List<Map<String, Object>> getEntryTypeDistribution();
    List<Map<String, Object>> getStatusDistribution();
    List<Map<String, Object>> getMostActiveUsers();
    Map<String, Object> getDashboardStatistics();
}
