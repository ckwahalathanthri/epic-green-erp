package lk.epicgreen.erp.accounting.service.impl;

import lk.epicgreen.erp.accounting.dto.response.GeneralLedgerResponse;
import lk.epicgreen.erp.accounting.entity.GeneralLedger;
import lk.epicgreen.erp.accounting.mapper.GeneralLedgerMapper;
import lk.epicgreen.erp.accounting.repository.GeneralLedgerRepository;
import lk.epicgreen.erp.accounting.service.GeneralLedgerService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class GeneralLedgerServiceImpl implements GeneralLedgerService {

    private final GeneralLedgerRepository ledgerRepository;
    private final GeneralLedgerMapper ledgerMapper;

    @Override
    public GeneralLedgerResponse getLedgerEntryById(Long id) {
        GeneralLedger ledger = ledgerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("General Ledger entry not found: " + id));
        return ledgerMapper.toResponse(ledger);
    }

    @Override
    public PageResponse<GeneralLedgerResponse> getAllLedgerEntries(Pageable pageable) {
        Page<GeneralLedger> ledgerPage = ledgerRepository.findAll(pageable);
        return createPageResponse(ledgerPage);
    }

    @Override
    public List<GeneralLedgerResponse> getLedgerEntriesByAccount(Long accountId) {
        List<GeneralLedger> entries = ledgerRepository.findByAccountId(accountId);
        return entries.stream()
            .map(ledgerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<GeneralLedgerResponse> getLedgerEntriesByPeriod(Long periodId) {
        List<GeneralLedger> entries = ledgerRepository.findByPeriodId(periodId);
        return entries.stream()
            .map(ledgerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<GeneralLedgerResponse> getLedgerEntriesByDateRange(LocalDate startDate, LocalDate endDate) {
        List<GeneralLedger> entries = ledgerRepository.findByTransactionDateBetween(startDate, endDate);
        return entries.stream()
            .map(ledgerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<GeneralLedgerResponse> getLedgerEntriesByJournal(Long journalId) {
        List<GeneralLedger> entries = ledgerRepository.findByJournalId(journalId);
        return entries.stream()
            .map(ledgerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<GeneralLedgerResponse> getAccountLedger(Long accountId, LocalDate startDate, 
                                                                 LocalDate endDate, Pageable pageable) {
        Page<GeneralLedger> ledgerPage = ledgerRepository.findByAccountIdAndTransactionDateBetween(
            accountId, startDate, endDate, pageable);
        return createPageResponse(ledgerPage);
    }

    private PageResponse<GeneralLedgerResponse> createPageResponse(Page<GeneralLedger> ledgerPage) {
        List<GeneralLedgerResponse> content = ledgerPage.getContent().stream()
            .map(ledgerMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<GeneralLedgerResponse>builder()
            .content(content)
            .pageNumber(ledgerPage.getNumber())
            .pageSize(ledgerPage.getSize())
            .totalElements(ledgerPage.getTotalElements())
            .totalPages(ledgerPage.getTotalPages())
            .last(ledgerPage.isLast())
            .first(ledgerPage.isFirst())
            .empty(ledgerPage.isEmpty())
            .build();
    }
}
