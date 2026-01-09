package lk.epicgreen.erp.accounting.service;

import lk.epicgreen.erp.accounting.dto.request.JournalEntryRequest;
import lk.epicgreen.erp.accounting.dto.response.JournalEntryResponse;
import lk.epicgreen.erp.accounting.entity.JournalEntry;
import lk.epicgreen.erp.accounting.entity.JournalEntryLine;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service interface for Journal Entry entity business logic
 * 
 * Journal Entry Status Workflow:
 * DRAFT → POSTED (creates GL entries, updates account balances)
 * Can be CANCELLED from DRAFT only
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface JournalEntryService {

    JournalEntryResponse createJournalEntry(JournalEntryRequest request);
    JournalEntryResponse updateJournalEntry(Long id, JournalEntryRequest request);
    JournalEntry postJournalEntry(Long id);
    JournalEntry approveJournalEntry(Long id);

    JournalEntry rejectJournalEntry(Long id,String reason);

    JournalEntry submitForApproval(Long id);
    void cancelJournalEntry(Long id, String reason);
    void deleteJournalEntry(Long id);

    List<JournalEntry> getDraftEntries();

    List<JournalEntry> getPostedEntries();

    List<JournalEntry> getUnpostedEntries();

    List<JournalEntry> getReversedEntries();

    List<JournalEntry> getUnbalancedEntries();
    List<JournalEntry> getEntriesPendingApproval();

    List<JournalEntry> getEntriesRequiringReview();
    List<JournalEntry> getEntriesByFiscalPeriod(Integer year,String periodCode);

    List<JournalEntry> getEntriesByDateRange(LocalDate startDate, LocalDate endDate);

    JournalEntryLine addJournalEntryLine(Long entryId,JournalEntryLine line);

    JournalEntry unpostJournalEntry(Long id);
    JournalEntryLine updateJournalEntryLine(Long entryId,Long lineId,JournalEntryLine line);
    void deleteJournalEntryLine(Long entryId,Long lineId);

    boolean validateJournalEntry(Long id);

    boolean isJournalEntryBalanced(Long id);

    List<JournalEntryLine> getJournalEntryLines(Long entryId);
    JournalEntry reverseJournalEntry(Long id,String reason);
    Map<String,Object> getJournalEntryBalance(Long id);

    void recalculateJournalEntryTotals(Long id);

    List<JournalEntry> createBulkJournalEntries(List<JournalEntryRequest> requests);

    int postBulkJournalEntries(List<Long> entryIds);

    int deleteBulkJournalEntries(List<Long> entryIds);

    Map<String,Object> getJournalEntryStatistics();
    List<Map<String,Object>> getEntryTypeDistribution();
    List<Map<String,Object>> getStatusDistribution();
    List<Map<String,Object>> getMostActiveUsers();
    Map<String,Object> getDashboardStatistics();
    
    JournalEntryResponse getJournalEntryById(Long id);
    JournalEntryResponse getJournalEntryByNumber(String journalNumber);
    PageResponse<JournalEntryResponse> getAllJournalEntries(Pageable pageable);
    
    PageResponse<JournalEntryResponse> getJournalEntriesByStatus(String status, Pageable pageable);
    List<JournalEntryResponse> getJournalEntriesByPeriod(Long periodId);
    List<JournalEntryResponse> getJournalEntriesByDateRange(LocalDate startDate, LocalDate endDate);
    List<JournalEntryResponse> getJournalEntriesByEntryType(String entryType);
    
    PageResponse<JournalEntryResponse> searchJournalEntries(String keyword, Pageable pageable);
    boolean canDelete(Long id);
    boolean canUpdate(Long id);
}
