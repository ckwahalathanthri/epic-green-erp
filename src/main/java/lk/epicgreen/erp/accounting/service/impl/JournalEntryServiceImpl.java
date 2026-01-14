package lk.epicgreen.erp.accounting.service.impl;

import lk.epicgreen.erp.accounting.dto.request.JournalEntryRequest;
import lk.epicgreen.erp.accounting.dto.request.JournalEntryLineRequest;
import lk.epicgreen.erp.accounting.dto.response.JournalEntryResponse;
import lk.epicgreen.erp.accounting.entity.*;
import lk.epicgreen.erp.accounting.mapper.JournalEntryMapper;
import lk.epicgreen.erp.accounting.mapper.JournalEntryLineMapper;
import lk.epicgreen.erp.accounting.repository.*;
import lk.epicgreen.erp.accounting.service.JournalEntryService;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lk.epicgreen.erp.common.exception.InvalidOperationException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.net.ContentHandler;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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
    public JournalEntry postJournalEntry(Long id) {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Posting Journal Entry: {} by user: {}", id, user.getFirstName());

        JournalEntry journal = findJournalEntryById(id);

        if (!"DRAFT".equals(journal.getStatus())) {
            throw new InvalidOperationException(
                "Cannot post Journal Entry. Current status: " + journal.getStatus() + 
                ". Only DRAFT entries can be posted.");
        }

        journal.setStatus("POSTED");
        journal.setPostedBy(user);
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
        return journal;
    }

    public JournalEntry unpostJournalEntry(Long id){
        JournalEntry journal=findJournalEntryById(id);
        if(!journal.isPosted()){
            throw new IllegalStateException("Journal Entry is not posted, canot unpost");
        }

        journal.setStatus("DRAFT");
        journal.setPostedBy(null);
        journal.setPostedAt(null);

        journalRepository.save(journal);

        return journal;
    }

    public JournalEntry reverseJournalEntry(Long id, String reason){
        JournalEntry currentEntry=findJournalEntryById(id);

        if(!currentEntry.isPosted()){
            throw  new IllegalStateException("There are no such journel posted");
        }
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        JournalEntry reversal=JournalEntry.builder()
                .journalNumber(currentEntry.getJournalNumber())
                .journalDate(LocalDate.now())
                .period(currentEntry.getPeriod())
                .entryType("REVERSAL")
                .description("Reversal Of "+currentEntry.getJournalNumber()+" Reason: "+reason)
                .totalDebit(currentEntry.getTotalCredit())
                .totalCredit(currentEntry.getTotalDebit())
                .status("POSTED")
                .postedBy(user)
                .postedAt(LocalDateTime.now())
                .build();
        for(JournalEntryLine line:currentEntry.getLines()){
            JournalEntryLine reverseLine=JournalEntryLine.builder()
                    .journal(reversal)
                    .lineNumber(line.getLineNumber())
                    .account(line.getAccount())
                    .debitAmount(line.getCreditAmount())
                    .creditAmount(line.getDebitAmount())
                    .description("Reversal of Line "+line.getLineNumber())
                    .build();
            reversal.addLine(reverseLine);
        }
        journalRepository.save(reversal);

        currentEntry.setDescription(currentEntry.getDescription()+ "[Reversed By ] "+reversal.getJournalNumber());
        journalRepository.save(currentEntry);
        return reversal;
    }

    @Override
    @Transactional
    public JournalEntry approveJournalEntry(Long id) {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Approving Journal Entry: {} by user: {}", id, user.getFirstName());

        JournalEntry journal = findJournalEntryById(id);

        if (!"POSTED".equals(journal.getStatus())) {
            throw new InvalidOperationException(
                "Cannot approve Journal Entry. Current status: " + journal.getStatus() + 
                ". Only POSTED entries can be approved.");
        }

        journal.setApprovedBy(user);
        journal.setApprovedAt(LocalDateTime.now());
        journalRepository.save(journal);

        log.info("Journal Entry approved successfully: {}", id);
        return journal;
    }

    public JournalEntry rejectJournalEntry(Long id,String reason){
        JournalEntry  journal=findJournalEntryById(id);
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(journal.isCancelled()){
            throw new IllegalStateException("Cannot reject a cancelled journal entry");
        }
        journal.setStatus("REJECTED");
        journal.setDescription(journal.getDescription()+ "|"+"Rejected By "+user.getFirstName());
        journal.setApprovedBy(user);

        return journalRepository.save(journal);
    }

    public JournalEntry submitForApproval(Long id){
        JournalEntry journal =findJournalEntryById(id);


        if(!"DRAFT".equals(journal.getStatus())){
            throw new IllegalStateException("Only journals in the draft status could be sent for approval ");
        }

        if(journal.getTotalDebit() ==null || journal.getTotalCredit()==null || journal.getTotalDebit().compareTo(journal.getTotalCredit()) !=0){
            throw new IllegalStateException(
                    "Journal entry must be balanced before submittion"
            );
        }
        if(journal.getLines()==null || journal.getLines().isEmpty()){
            throw new IllegalStateException(
              "Journal entry should atleast have one line before submission"
            );
        }
        journal.setStatus("Submitted");
        journal.setUpdatedAt(LocalDateTime.now());
        journal.setDescription(journal.getDescription()+"|"+ "Submitted for approval");
        return journalRepository.save(journal);

    }

    public List<JournalEntry> getPostedEntries(){
        return journalRepository.findByStatus("POSTED");
    }

    public List<JournalEntry> getUnpostedEntries(){
        return journalRepository.findByStatusNotIn(
                Arrays.asList("POSTED","CANCELLED")
        );
    }

    public List<JournalEntry>getUnbalancedEntries(){
        return journalRepository.findByStatus("UNBALANCED");
    }

    public List<JournalEntry> getEntriesPendingApproval(){
        return  journalRepository.findByStatus("PENDING");
    }

    @Transactional
    public List<JournalEntry> getEntriesByFiscalPeriod(Integer year,String periodCode){
        FinancialPeriod financialPeriod=periodRepository.findByYearAndPeriod(year,periodCode)
                .orElseThrow(()-> new EntityNotFoundException(
                        "Financial period not found for the year "+year+" and period "+periodCode
                ));
         return  journalRepository.findByPeriod(financialPeriod);

    }

    public List<JournalEntry> getEntriesByDateRange(LocalDate startDate, LocalDate endDate){
        return journalRepository.getEntriesByDateRange(startDate,endDate);
    }

    public JournalEntryLine addJournalEntryLine(Long entryId,JournalEntryLine line){
        JournalEntry journal=journalRepository.findById(entryId)
                .orElseThrow(()-> new EntityNotFoundException(
                        "Journal enry was not found with id "+entryId));
        line.setJournal(journal);
        journal.getLines().add(line);
        journalRepository.save(journal);
        return line;
    }

    public JournalEntryLine updateJournalEntryLine(Long entryId,Long lineId,JournalEntryLine line){
        JournalEntry journal=journalRepository.findById(entryId)
                .orElseThrow(()-> new EntityNotFoundException(
                        "Journal entry was not found with id "+entryId
                ));
        if(!journal.getStatus().equals("DRAFT")){
            throw new IllegalStateException("Canot update a jounal thats not a draft");
        }


        JournalEntryLine journalLine=journalLineRepository.findByIdAndJournal_Id(lineId,entryId).orElseThrow(
                ()->
                        new EntityNotFoundException(" Line "+lineId+" Does not belong to journal "+entryId)
        );

        journalLine.setLineNumber(line.getLineNumber());
        journalLine.setCreditAmount(line.getCreditAmount());
        journalLine.setDebitAmount(line.getDebitAmount());
        journalLine.setDescription(line.getDescription());
        journalLine.setAccount(line.getAccount());
        journalLine.setCostCenter(line.getCostCenter());
        journalLine.setJournal(journal);
        journalLine.setDimension1(line.getDimension1());
        journalLine.setDimension2(line.getDimension2());


        journalLineRepository.save(journalLine);

        return  journalLine;
    }

    public void recalculateJournalEntryTotals(Long id){
        JournalEntry journal=journalRepository.findById(id)
                .orElseThrow(()->
                        new EntityNotFoundException(
                                "There is no journal entry with id "+id
                        ));

        journal.calculateTotals();
        journalRepository.save(journal);

    }

    public List<JournalEntry> createBulkJournalEntries(List<JournalEntryRequest> requests){
        if(requests ==null || requests.isEmpty()){
            return Collections.emptyList();
        }
        List<JournalEntry> journals=requests.stream()
                .map(journalMapper::toEntity)
                .collect(Collectors.toList());
        return journalRepository.saveAll(journals);
    }

    public int deleteBulkJournalEntries(List<Long> entryIds){
        List<JournalEntry> entries=journalRepository.findAllById(entryIds);
        int deletedCount=entries.size();
        journalRepository.deleteAllById(entryIds);
        return deletedCount;




    }

    public int postBulkJournalEntries(List<Long> entryIds){
        User currentUser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<JournalEntry> entries=journalRepository.findAllById(entryIds);
        int postedCount=0;

        for(JournalEntry entry:entries){
            if(entry.canPost()){

                entry.post(currentUser);
                postedCount++;
            }
        }

        journalRepository.saveAll(entries);
        return postedCount;
    }

    public Map<String,Object> getJournalEntryBalance(Long id){
        JournalEntry journal=journalRepository.findById(id)
                .orElseThrow(()->
                        new EntityNotFoundException("There is no journal entry with id "+id));
        Map<String,Object> map=new HashMap<>();
        map.put("totalDebit",journal.getTotalDebit());
        map.put("totalCredit",journal.getTotalCredit());
        map.put("Balance",journal.getDifference());
        map.put("isBalanced",journal.isBalanced());

        return map;
    }

    public Map<String,Object> getJournalEntryStatistics(){
        Map<String,Object> map=new HashMap<>();
        map.put("statistics",journalRepository.getJournalEntryStatistics());
        return map;
    }
    public List<Map<String,Object>> getEntryTypeDistribution(){
        List<Object[]> entries=journalRepository.countByEntryType();
        List<Map<String,Object>> distribution=new ArrayList<>();

        for(Object[] row:entries){
            Map<String,Object> map=new HashMap<>();
            map.put("entryType",row[0]);
            map.put("count",row[1]);
            distribution.add(map);
        }
        return distribution;
    }
    public List<Map<String,Object>> getStatusDistribution(){
        List<Object[]> entries=journalRepository.countByStatusDistribution();
        List<Map<String,Object>> distribution=new ArrayList<>();

        for(Object[] row:entries){
            Map<String,Object> map=new HashMap<>();
            map.put("Status",row[0]);
            map.put("count",row[1]);
            distribution.add(map);
        }
        return distribution;
    }
    public List<Map<String,Object>> getMostActiveUsers(){
        List<Object[]> entries=journalRepository.mostActiveUsers();
        List<Map<String,Object>> distribution=new ArrayList<>();

        for(Object[] row:entries){
            Map<String,Object> map=new HashMap<>();
            map.put("User",row[0]);
            map.put("Times",row[1]);
            distribution.add(map);
        }
        return distribution;
    }

    public Map<String,Object>  getDashboardStatistics(){
        Map<String,Object> map=new HashMap<>();
        map.put("totalJournal",journalRepository.countAll());
        map.put("totalDebit",journalRepository.sumTotalDebit());
        map.put("totalCredit",journalRepository.sumTotalCredit());
        return map;
    }

    public List<JournalEntryLine> getJournalEntryLines(Long entryId){
        JournalEntry journal=journalRepository.findById(entryId)
                .orElseThrow(()->
                        new EntityNotFoundException(
                                "There is no journal entry with id "+entryId
                        ));
        return journal.getLines();
    }

    public void deleteJournalEntryLine(Long entryId,Long lineId){
        JournalEntry journal=journalRepository.findById(entryId)
                .orElseThrow(()-> new EntityNotFoundException(
                        "There is no journal entry with id "+entryId
                ));
        JournalEntryLine line=journalLineRepository.findByIdAndJournal_Id(lineId,entryId)
                .orElseThrow(()-> new EntityNotFoundException(
                        "Line"+lineId+" Does not belong to"
                ));

        journal.getLines().remove(line);
        journalLineRepository.delete(line);
        journalRepository.save(journal);

    }



    public boolean isJournalEntryBalanced(Long id){
        JournalEntry journal=journalRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(
                        "No journal found with the id "+id
                ));

        if(journal.isBalanced()){
            return true;
        }else{
            return false;
        }
    }

    public List<JournalEntry> getReversedEntries(){
        return journalRepository.findByStatus("REVERSED");
    }

    public boolean validateJournalEntry(Long id){
        JournalEntry journal=journalRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "No journal found eith the id "+id
                ));
        try {
            journal.validate();
            return true;
        }catch (IllegalStateException err){
            return false;
        }


    }
    public List<JournalEntry> getDraftEntries(){
        return journalRepository.findByStatus("DRAFT");

    }

    public List<JournalEntry> getEntriesRequiringReview(){
        return journalRepository.findByStatus("Submitted");
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
