package lk.epicgreen.erp.accounting.service.impl;

import lk.epicgreen.erp.accounting.dto.request.JournalEntryRequest;
import lk.epicgreen.erp.accounting.dto.request.JournalEntryLineRequest;
import lk.epicgreen.erp.accounting.dto.response.JournalEntryResponse;
import lk.epicgreen.erp.accounting.entity.*;
import lk.epicgreen.erp.accounting.mapper.JournalEntryMapper;
import lk.epicgreen.erp.accounting.mapper.JournalEntryLineMapper;
import lk.epicgreen.erp.accounting.repository.*;
import lk.epicgreen.erp.accounting.service.JournalEntryService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lk.epicgreen.erp.common.exception.InvalidOperationException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class JournalEntryServiceImpl implements JournalEntryService {

    private final JournalEntryRepository journalRepository;
    private final JournalEntryLineRepository journalLineRepository;
    private final FinancialPeriodRepository periodRepository;
    private final ChartOfAccountsRepository accountRepository;
    private final GeneralLedgerRepository generalLedgerRepository;
    private final JournalEntryMapper journalMapper;
    private final JournalEntryLineMapper lineMapper;

    @Override
    @Transactional
    public JournalEntryResponse createJournalEntry(JournalEntryRequest request) {
        log.info("Creating new Journal Entry: {}", request.getJournalNumber());

        validateUniqueJournalNumber(request.getJournalNumber(), null);
        validateBalancedEntry(request.getTotalDebit(), request.getTotalCredit());

        FinancialPeriod period = findPeriodById(request.getPeriodId());
        if (period.getIsClosed()) {
            throw new InvalidOperationException("Cannot create journal entry in closed period.");
        }

        JournalEntry journal = journalMapper.toEntity(request);
        journal.setPeriod(period);

        List<JournalEntryLine> lines = new ArrayList<>();
        for (JournalEntryLineRequest lineRequest : request.getLines()) {
            ChartOfAccounts account = findAccountById(lineRequest.getAccountId());
            JournalEntryLine line = lineMapper.toEntity(lineRequest);
            line.setJournal(journal);
            line.setAccount(account);
            lines.add(line);
        }
        journal.setLines(lines);

        JournalEntry savedJournal = journalRepository.save(journal);
        log.info("Journal Entry created successfully: {}", savedJournal.getJournalNumber());

        return journalMapper.toResponse(savedJournal);
    }

    @Override
    @Transactional
    public JournalEntryResponse updateJournalEntry(Long id, JournalEntryRequest request) {
        log.info("Updating Journal Entry: {}", id);

        JournalEntry journal = findJournalEntryById(id);

        if (!canUpdate(id)) {
            throw new InvalidOperationException(
                "Cannot update Journal Entry. Current status: " + journal.getStatus() + 
                ". Only DRAFT entries can be updated.");
        }

        if (!journal.getJournalNumber().equals(request.getJournalNumber())) {
            validateUniqueJournalNumber(request.getJournalNumber(), id);
        }

        validateBalancedEntry(request.getTotalDebit(), request.getTotalCredit());

        if (!journal.getPeriod().getId().equals(request.getPeriodId())) {
            FinancialPeriod period = findPeriodById(request.getPeriodId());
            if (period.getIsClosed()) {
                throw new InvalidOperationException("Cannot update to closed period.");
            }
            journal.setPeriod(period);
        }

        journalMapper.updateEntityFromRequest(request, journal);

        journalLineRepository.deleteAll(journal.getLines());
        journal.getLines().clear();

        List<JournalEntryLine> newLines = new ArrayList<>();
        for (JournalEntryLineRequest lineRequest : request.getLines()) {
            ChartOfAccounts account = findAccountById(lineRequest.getAccountId());
            JournalEntryLine line = lineMapper.toEntity(lineRequest);
            line.setJournal(journal);
            line.setAccount(account);
            newLines.add(line);
        }
        journal.setLines(newLines);

        JournalEntry updatedJournal = journalRepository.save(journal);
        log.info("Journal Entry updated successfully: {}", updatedJournal.getJournalNumber());

        return journalMapper.toResponse(updatedJournal);
    }

    @Override
    @Transactional
    public void postJournalEntry(Long id, Long postedBy) {
        log.info("Posting Journal Entry: {} by user: {}", id, postedBy);

        JournalEntry journal = findJournalEntryById(id);

        if (!"DRAFT".equals(journal.getStatus())) {
            throw new InvalidOperationException(
                "Cannot post Journal Entry. Current status: " + journal.getStatus() + 
                ". Only DRAFT entries can be posted.");
        }

        journal.setStatus("POSTED");
        journal.setPostedBy(postedBy);
        journal.setPostedAt(LocalDateTime.now());

        // Create General Ledger entries and update account balances
        for (JournalEntryLine line : journal.getLines()) {
            // Create GL entry
            GeneralLedger glEntry = GeneralLedger.builder()
                .transactionDate(journal.getJournalDate())
                .period(journal.getPeriod())
                .account(line.getAccount())
                .journal(journal)
                .journalLine(line)
                .description(line.getDescription())
                .debitAmount(line.getDebitAmount())
                .creditAmount(line.getCreditAmount())
                .balance(calculateNewBalance(line.getAccount(), line.getDebitAmount(), line.getCreditAmount()))
                .sourceType(journal.getSourceType())
                .sourceId(journal.getSourceId())
                .build();
            generalLedgerRepository.save(glEntry);

            // Update account current balance
            updateAccountBalance(line.getAccount(), line.getDebitAmount(), line.getCreditAmount());
        }

        journalRepository.save(journal);
        log.info("Journal Entry posted successfully: {}", id);
    }

    @Override
    @Transactional
    public void approveJournalEntry(Long id, Long approvedBy) {
        log.info("Approving Journal Entry: {} by user: {}", id, approvedBy);

        JournalEntry journal = findJournalEntryById(id);

        if (!"POSTED".equals(journal.getStatus())) {
            throw new InvalidOperationException(
                "Cannot approve Journal Entry. Current status: " + journal.getStatus() + 
                ". Only POSTED entries can be approved.");
        }

        journal.setApprovedBy(approvedBy);
        journal.setApprovedAt(LocalDateTime.now());
        journalRepository.save(journal);

        log.info("Journal Entry approved successfully: {}", id);
    }

    @Override
    @Transactional
    public void cancelJournalEntry(Long id, String reason) {
        log.info("Cancelling Journal Entry: {} with reason: {}", id, reason);

        JournalEntry journal = findJournalEntryById(id);

        if (!"DRAFT".equals(journal.getStatus())) {
            throw new InvalidOperationException(
                "Cannot cancel Journal Entry. Current status: " + journal.getStatus() + 
                ". Only DRAFT entries can be cancelled.");
        }

        journal.setStatus("CANCELLED");
        journalRepository.save(journal);

        log.info("Journal Entry cancelled successfully: {}", id);
    }

    @Override
    @Transactional
    public void deleteJournalEntry(Long id) {
        log.info("Deleting Journal Entry: {}", id);

        if (!canDelete(id)) {
            JournalEntry journal = findJournalEntryById(id);
            throw new InvalidOperationException(
                "Cannot delete Journal Entry. Current status: " + journal.getStatus() + 
                ". Only DRAFT entries can be deleted.");
        }

        journalRepository.deleteById(id);
        log.info("Journal Entry deleted successfully: {}", id);
    }

    @Override
    public JournalEntryResponse getJournalEntryById(Long id) {
        JournalEntry journal = findJournalEntryById(id);
        return journalMapper.toResponse(journal);
    }

    @Override
    public JournalEntryResponse getJournalEntryByNumber(String journalNumber) {
        JournalEntry journal = journalRepository.findByJournalNumber(journalNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Journal Entry not found: " + journalNumber));
        return journalMapper.toResponse(journal);
    }

    @Override
    public PageResponse<JournalEntryResponse> getAllJournalEntries(Pageable pageable) {
        Page<JournalEntry> journalPage = journalRepository.findAll(pageable);
        return createPageResponse(journalPage);
    }

    @Override
    public PageResponse<JournalEntryResponse> getJournalEntriesByStatus(String status, Pageable pageable) {
        Page<JournalEntry> journalPage = journalRepository.findByStatus(status, pageable);
        return createPageResponse(journalPage);
    }

    @Override
    public List<JournalEntryResponse> getJournalEntriesByPeriod(Long periodId) {
        List<JournalEntry> journals = journalRepository.findByPeriodId(periodId);
        return journals.stream()
            .map(journalMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<JournalEntryResponse> getJournalEntriesByDateRange(LocalDate startDate, LocalDate endDate) {
        List<JournalEntry> journals = journalRepository.findByJournalDateBetween(startDate, endDate);
        return journals.stream()
            .map(journalMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<JournalEntryResponse> getJournalEntriesByEntryType(String entryType) {
        List<JournalEntry> journals = journalRepository.findByEntryType(entryType);
        return journals.stream()
            .map(journalMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<JournalEntryResponse> searchJournalEntries(String keyword, Pageable pageable) {
        Page<JournalEntry> journalPage = journalRepository.searchJournals(keyword, pageable);
        return createPageResponse(journalPage);
    }

    @Override
    public boolean canDelete(Long id) {
        JournalEntry journal = findJournalEntryById(id);
        return "DRAFT".equals(journal.getStatus());
    }

    @Override
    public boolean canUpdate(Long id) {
        JournalEntry journal = findJournalEntryById(id);
        return "DRAFT".equals(journal.getStatus());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private void validateBalancedEntry(BigDecimal totalDebit, BigDecimal totalCredit) {
        if (totalDebit.compareTo(totalCredit) != 0) {
            throw new InvalidOperationException(
                "Journal Entry is not balanced. Total Debit: " + totalDebit + 
                ", Total Credit: " + totalCredit);
        }
    }

    private BigDecimal calculateNewBalance(ChartOfAccounts account, BigDecimal debit, BigDecimal credit) {
        BigDecimal currentBalance = account.getCurrentBalance() != null ? 
            account.getCurrentBalance() : BigDecimal.ZERO;
        
        return currentBalance.add(debit).subtract(credit);
    }

    private void updateAccountBalance(ChartOfAccounts account, BigDecimal debit, BigDecimal credit) {
        BigDecimal newBalance = calculateNewBalance(account, debit, credit);
        account.setCurrentBalance(newBalance);
        accountRepository.save(account);
    }

    private void validateUniqueJournalNumber(String journalNumber, Long excludeId) {
        boolean exists;
        if (excludeId != null) {
            exists = journalRepository.existsByJournalNumberAndIdNot(journalNumber, excludeId);
        } else {
            exists = journalRepository.existsByJournalNumber(journalNumber);
        }

        if (exists) {
            throw new DuplicateResourceException("Journal Entry with number '" + journalNumber + "' already exists");
        }
    }

    private JournalEntry findJournalEntryById(Long id) {
        return journalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Journal Entry not found: " + id));
    }

    private FinancialPeriod findPeriodById(Long id) {
        return periodRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Financial Period not found: " + id));
    }

    private ChartOfAccounts findAccountById(Long id) {
        return accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + id));
    }

    private PageResponse<JournalEntryResponse> createPageResponse(Page<JournalEntry> journalPage) {
        List<JournalEntryResponse> content = journalPage.getContent().stream()
            .map(journalMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<JournalEntryResponse>builder()
            .content(content)
            .pageNumber(journalPage.getNumber())
            .pageSize(journalPage.getSize())
            .totalElements(journalPage.getTotalElements())
            .totalPages(journalPage.getTotalPages())
            .last(journalPage.isLast())
            .first(journalPage.isFirst())
            .empty(journalPage.isEmpty())
            .build();
    }
}
