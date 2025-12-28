package lk.epicgreen.erp.accounting.service;

import lk.epicgreen.erp.accounting.dto.request.JournalEntryRequest;
import lk.epicgreen.erp.accounting.dto.response.JournalEntryResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

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
    void postJournalEntry(Long id, Long postedBy);
    void approveJournalEntry(Long id, Long approvedBy);
    void cancelJournalEntry(Long id, String reason);
    void deleteJournalEntry(Long id);
    
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
