package lk.epicgreen.erp.accounting.service;

import lk.epicgreen.erp.accounting.dto.response.GeneralLedgerResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for General Ledger entity (reporting)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface GeneralLedgerService {

    GeneralLedgerResponse getLedgerEntryById(Long id);
    PageResponse<GeneralLedgerResponse> getAllLedgerEntries(Pageable pageable);
    
    List<GeneralLedgerResponse> getLedgerEntriesByAccount(Long accountId);
    List<GeneralLedgerResponse> getLedgerEntriesByPeriod(Long periodId);
    List<GeneralLedgerResponse> getLedgerEntriesByDateRange(LocalDate startDate, LocalDate endDate);
    List<GeneralLedgerResponse> getLedgerEntriesByJournal(Long journalId);
    
    PageResponse<GeneralLedgerResponse> getAccountLedger(Long accountId, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
