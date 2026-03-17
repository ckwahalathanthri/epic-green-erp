package lk.epicgreen.erp.supplier.service.impl;

import lk.epicgreen.erp.supplier.dto.request.SupplierLedgerRequest;
import lk.epicgreen.erp.supplier.dto.response.LedgerEntryDTO;
import lk.epicgreen.erp.supplier.dto.response.SupplierLedgerResponse;
import lk.epicgreen.erp.supplier.entity.Supplier;
import lk.epicgreen.erp.supplier.entity.SupplierLedger;
import lk.epicgreen.erp.supplier.mapper.SupplierLedgerMapper;
import lk.epicgreen.erp.supplier.repository.SupplierRepository;
import lk.epicgreen.erp.supplier.repository.SupplierLedgerRepository;
import lk.epicgreen.erp.supplier.service.SupplierLedgerService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of SupplierLedgerService interface
 * 
 * CRITICAL: SupplierLedger is IMMUTABLE
 * - CREATE: Allowed with automatic balance calculation
 * - READ: Allowed for all query operations
 * - UPDATE: NOT IMPLEMENTED (financial audit requirement)
 * - DELETE: NOT IMPLEMENTED (financial audit requirement)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SupplierLedgerServiceImpl implements SupplierLedgerService {

    private final SupplierLedgerRepository supplierLedgerRepository;
    private final SupplierRepository supplierRepository;
    private final SupplierLedgerMapper supplierLedgerMapper;

    @Override
    @Transactional
    public SupplierLedgerResponse createLedgerEntry(SupplierLedgerRequest request) {
        log.info("Creating new supplier ledger entry for supplier: {}", request.getSupplierId());

        // Verify supplier exists
        Supplier supplier = findSupplierById(request.getSupplierId());

        // Create ledger entry
        SupplierLedger ledgerEntry = supplierLedgerMapper.toEntity(request);
        ledgerEntry.setSupplier(supplier);

        // Calculate running balance
        BigDecimal currentBalance = getSupplierBalance(request.getSupplierId());
        BigDecimal newBalance = currentBalance
            .add(request.getDebitAmount())
            .subtract(request.getCreditAmount());
        
        ledgerEntry.setBalance(newBalance);

        // Save ledger entry
        SupplierLedger savedEntry = supplierLedgerRepository.save(ledgerEntry);

        // Update supplier current balance
        supplier.setCurrentBalance(newBalance);
        supplierRepository.save(supplier);

        log.info("Supplier ledger entry created successfully. New balance: {}", newBalance);

        return supplierLedgerMapper.toResponse(savedEntry);
    }

    @Transactional(readOnly = true)
    public BigDecimal getBalance(Long supplierId) {
        return supplierLedgerRepository.calculateBalance(supplierId);
    }

    @Transactional(readOnly = true)
    public List<LedgerEntryDTO> getBySupplier(Long supplierId) {
        return supplierLedgerRepository.findBySupplierId(supplierId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }


    @Transactional
    public Page<SupplierLedger> searchLedgerEntries(String keyword, Pageable pageable){
        return supplierLedgerRepository.searchLedgerEntries(keyword, pageable);
    }

    @Transactional
    public SupplierLedger recordPurchase(Long supplierId, Double amount, String referenceType, Long referenceId, String description, LocalDate transactionDate){
        Supplier supplier = findSupplierById(supplierId);

        BigDecimal debitAmount = BigDecimal.valueOf(amount);
        BigDecimal creditAmount = BigDecimal.ZERO;

        BigDecimal currentBalance = getSupplierBalance(supplierId);
        BigDecimal newBalance = currentBalance.add(debitAmount).subtract(creditAmount);

        SupplierLedger ledgerEntry = SupplierLedger.builder()
            .supplier(supplier)
            .transactionDate(transactionDate)
            .transactionType("PURCHASE")
            .debitAmount(debitAmount)
            .creditAmount(creditAmount)
            .balance(newBalance)
            .referenceType(referenceType)
            .referenceId(referenceId)
            .description(description)
            .build();

        SupplierLedger savedEntry = supplierLedgerRepository.save(ledgerEntry);

        supplier.setCurrentBalance(newBalance);
        supplierRepository.save(supplier);

        return savedEntry;
    }


    @Override
    public SupplierLedgerResponse getLedgerEntryById(Long id) {
        SupplierLedger ledgerEntry = findLedgerEntryById(id);
        return supplierLedgerMapper.toResponse(ledgerEntry);
    }

    @Override
    public PageResponse<SupplierLedgerResponse> getLedgerEntriesBySupplier(Long supplierId, Pageable pageable) {
        Page<SupplierLedger> ledgerPage = supplierLedgerRepository.findBySupplierId(supplierId, pageable);
        return createPageResponse(ledgerPage);
    }

    @Override
    public List<SupplierLedgerResponse> getLedgerEntriesBySupplierAndDateRange(
            Long supplierId, LocalDate startDate, LocalDate endDate) {
        List<SupplierLedger> entries = supplierLedgerRepository
            .findBySupplierIdAndTransactionDateBetween(supplierId, startDate, endDate);
        return entries.stream()
            .map(supplierLedgerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public SupplierLedger getLedgerEntryByReferenceNumber(String referenceNumber){
        return (SupplierLedger) supplierLedgerRepository.findByReferenceNumber(referenceNumber);
    }

    @Transactional
    public     SupplierLedger recordPayment(Long supplierId, Double amount, String paymentMethod, String referenceNumber, String description, LocalDate transactionDate){
        Supplier supplier = findSupplierById(supplierId);

        BigDecimal debitAmount = BigDecimal.ZERO;
        BigDecimal creditAmount = BigDecimal.valueOf(amount);

        BigDecimal currentBalance = getSupplierBalance(supplierId);
        BigDecimal newBalance = currentBalance.add(debitAmount).subtract(creditAmount);

        SupplierLedger ledgerEntry = SupplierLedger.builder()
            .supplier(supplier)
            .transactionDate(transactionDate)
            .transactionType("PAYMENT")
            .debitAmount(debitAmount)
            .creditAmount(creditAmount)
            .balance(newBalance)
            .referenceType(paymentMethod)
            .referenceNumber(referenceNumber)
            .description(description)
            .build();

        SupplierLedger savedEntry = supplierLedgerRepository.save(ledgerEntry);

        supplier.setCurrentBalance(newBalance);
        supplierRepository.save(supplier);

        return savedEntry;
    }

    @Override
    public PageResponse<SupplierLedgerResponse> getLedgerEntriesByType(
            String transactionType, Pageable pageable) {
        List<SupplierLedger> ledgerPage = supplierLedgerRepository
            .findByTransactionType(transactionType);
        return createPageResponse((Page<SupplierLedger>) ledgerPage);
    }

    @Override
    public List<SupplierLedgerResponse> getLedgerEntriesByReference(
            String referenceType, Long referenceId) {
        List<SupplierLedger> entries = supplierLedgerRepository
            .findByReferenceTypeAndReferenceId(referenceType, referenceId);
        return entries.stream()
            .map(supplierLedgerMapper::toResponse)
            .collect(Collectors.toList());
    }

//    public SupplierLedgerResponse getLedgerEntryById(Long id){
//        SupplierLedger ledgerEntry = findLedgerEntryById(id);
//        return supplierLedgerMapper.toResponse(ledgerEntry);
//    }



@Transactional
public void deleteLedgerEntry(Long id){
    SupplierLedger ledgerEntry = findLedgerEntryById(id);
    supplierLedgerRepository.delete(ledgerEntry);
}

    /**
     * @param id
     * @param request
     * @return
     */
    @Override
    public SupplierLedger updateLedgerEntry(Long id, SupplierLedgerRequest request) {
        return null;
    }

    @Override
    public BigDecimal getSupplierBalance(Long supplierId) {
        return supplierLedgerRepository.getSupplierBalance(supplierId)
            .orElse(BigDecimal.ZERO);
    }

    @Transactional(readOnly = true)
    public List<LedgerEntryDTO> getBySupplierAndDateRange(Long supplierId, LocalDate fromDate, LocalDate toDate) {
        return supplierLedgerRepository.findBySupplierIdAndDateRange(supplierId, fromDate, toDate).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public SupplierLedger recordReturn(Long supplierId, Double amount, String referenceType, Long referenceId, String description, LocalDate transactionDate){
        Supplier supplier = findSupplierById(supplierId);

        BigDecimal debitAmount = BigDecimal.ZERO;
        BigDecimal creditAmount = BigDecimal.valueOf(amount);

        BigDecimal currentBalance = getSupplierBalance(supplierId);
        BigDecimal newBalance = currentBalance.add(debitAmount).subtract(creditAmount);

        SupplierLedger ledgerEntry = SupplierLedger.builder()
            .supplier(supplier)
            .transactionDate(transactionDate)
            .transactionType("RETURN")
            .debitAmount(debitAmount)
            .creditAmount(creditAmount)
            .balance(newBalance)
            .referenceType(referenceType)
            .referenceId(referenceId)
            .description(description)
            .build();

        SupplierLedger savedEntry = supplierLedgerRepository.save(ledgerEntry);

        supplier.setCurrentBalance(newBalance);
        supplierRepository.save(supplier);

        return savedEntry;
    }


    @Override
    public BigDecimal getSupplierBalanceAsOfDate(Long supplierId, LocalDate asOfDate) {
        return supplierLedgerRepository.getSupplierBalanceAsOfDate(supplierId, asOfDate)
            .orElse(BigDecimal.ZERO);
    }

    @Transactional
    public  SupplierLedger recordAdjustment(Long supplierId, Double debitAmount, Double creditAmount, String description, String notes, LocalDate transactionDate){
        Supplier supplier = findSupplierById(supplierId);

        BigDecimal debitAmt = BigDecimal.valueOf(debitAmount);
        BigDecimal creditAmt = BigDecimal.valueOf(creditAmount);

        BigDecimal currentBalance = getSupplierBalance(supplierId);
        BigDecimal newBalance = currentBalance.add(debitAmt).subtract(creditAmt);

        SupplierLedger ledgerEntry = SupplierLedger.builder()
            .supplier(supplier)
            .transactionDate(transactionDate)
            .transactionType("ADJUSTMENT")
            .debitAmount(debitAmt)
            .creditAmount(creditAmt)
            .balance(newBalance)
            .description(description)
            .build();

        SupplierLedger savedEntry = supplierLedgerRepository.save(ledgerEntry);

        supplier.setCurrentBalance(newBalance);
        supplierRepository.save(supplier);

        return savedEntry;
    }

    @Transactional
    public List<SupplierLedger> getDebitEntriesBySupplier(Long supplierId){
        return supplierLedgerRepository.findBySupplierIdAndDebitAmountGreaterThan(supplierId, BigDecimal.ZERO);
    }

    @Transactional
    public List<SupplierLedger> getPaymentEntries(){
        return supplierLedgerRepository.findByTransactionType("PAYMENT");
    }

    @Transactional
    public List<SupplierLedger> getPurchaseEntries(){
        return supplierLedgerRepository.findByTransactionType("PURCHASE");
    }

    @Transactional
    public List<SupplierLedger> getCreditEntriesBySupplier(Long supplierId){
        return supplierLedgerRepository.findBySupplierIdAndCreditAmountGreaterThan(supplierId, BigDecimal.ZERO);
    }
    @Override
    public PageResponse<SupplierLedgerResponse> getAllLedgerEntries(Pageable pageable) {
        Page<SupplierLedger> ledgerPage = supplierLedgerRepository.findAll(pageable);
        return createPageResponse(ledgerPage);
    }

    @Override
    public List<SupplierLedgerResponse> getPurchaseEntriesBySupplier(Long supplierId) {
        List<SupplierLedger> entries = supplierLedgerRepository
            .findBySupplierIdAndTransactionType(supplierId, "PURCHASE");
        return entries.stream()
            .map(supplierLedgerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SupplierLedgerResponse> getPaymentEntriesBySupplier(Long supplierId) {
        List<SupplierLedger> entries = supplierLedgerRepository
            .findBySupplierIdAndTransactionType(supplierId, "PAYMENT");
        return entries.stream()
            .map(supplierLedgerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public List<SupplierLedger> getReturnEntries(){
        return supplierLedgerRepository.findByTransactionType("RETURN");
    }

    @Transactional
    public  List<SupplierLedger> getAdjustmentEntries(){
        return supplierLedgerRepository.findByTransactionType("ADJUSTMENT");
    }

    @Transactional
    public List<SupplierLedger> getCashPayments(){
        return supplierLedgerRepository.findByTransactionTypeAndReferenceType("PAYMENT", "CASH");
    }

    @Transactional
    public Map<String, Object> getSupplierStatement(Long supplierId, LocalDate startDate, LocalDate endDate){
        Supplier supplier = findSupplierById(supplierId);

        BigDecimal openingBalance = getSupplierBalanceAsOfDate(supplierId, startDate.minusDays(1));
        BigDecimal closingBalance = getSupplierBalanceAsOfDate(supplierId, endDate);

        List<SupplierLedgerResponse> transactions = getLedgerEntriesBySupplierAndDateRange(supplierId, startDate, endDate);



        Map<String,Object> map=new HashMap<>();
        map.put("supplierId",supplier.getId());
        map.put("supplierName",supplier.getSupplierName());
        map.put("openingBalance",openingBalance);
        map.put("closingBalance",closingBalance);
        map.put("transactions",transactions);

        return map;
    }

    @Transactional
    public List<Map<String, Object>> getTransactionTypeDistribution(){
        return supplierLedgerRepository.getTransactionTypeDistribution();
    }

    @Transactional
    public  Double getTotalPurchases(){
        return supplierLedgerRepository.getTotalPurchases()
            .orElse(BigDecimal.ZERO)
            .doubleValue();
    }

    @Transactional
    public Double getTotalPayments(){
        return supplierLedgerRepository.getTotalPayments()
            .orElse(BigDecimal.ZERO)
            .doubleValue();
    }

    @Transactional
    public Double getTotalOutstanding(){
        return supplierLedgerRepository.getTotalOutstanding()
            .orElse(BigDecimal.ZERO)
            .doubleValue();
    }

    @Transactional
    public Map<String, Object> getDashboardStatistics(){
        Long totalSuppliers = supplierRepository.countByDeletedAtIsNull();
        Double totalPurchases = getTotalPurchases();
        Double totalPayments = getTotalPayments();
        Double totalOutstanding = getTotalOutstanding();

        Map<String,Object> map=new HashMap<>();
        map.put("totalSuppliers",totalSuppliers);
        map.put("totalPurchases",totalPurchases);
        map.put("totalPayments",totalPayments);
        map.put("totalOutstanding",totalOutstanding);

        return map;
    }
    @Transactional
    public List<Map<String, Object>> getPaymentMethodDistribution(){
        return supplierLedgerRepository.getPaymentMethodDistribution();
    }
    @Transactional
    public Map<String, Object> getLedgerStatistics(){
        Long totalEntries = supplierLedgerRepository.count();
        BigDecimal totalDebits = supplierLedgerRepository.getTotalDebits()
            .orElse(BigDecimal.ZERO);
        BigDecimal totalCredits = supplierLedgerRepository.getTotalCredits()
            .orElse(BigDecimal.ZERO);

        Map<String,Object> map=new HashMap<>();
        map.put("totalEntries",totalEntries);
        map.put("totalDebits",totalDebits);
        map.put("totalCredits",totalCredits);

        return map;
    }

    @Override
    public SupplierLedgerSummary getLedgerSummary(Long supplierId) {
        Supplier supplier = findSupplierById(supplierId);
        
        BigDecimal totalPurchases = supplierLedgerRepository
            .getTotalBySupplierAndType(supplierId, "PURCHASE")
            .orElse(BigDecimal.ZERO);
        
        BigDecimal totalPayments = supplierLedgerRepository
            .getTotalBySupplierAndType(supplierId, "PAYMENT")
            .orElse(BigDecimal.ZERO);
        
        BigDecimal totalDebits = supplierLedgerRepository
            .getTotalDebitsBySupplier(supplierId)
            .orElse(BigDecimal.ZERO);
        
        BigDecimal totalCredits = supplierLedgerRepository
            .getTotalCreditsBySupplier(supplierId)
            .orElse(BigDecimal.ZERO);
        
        BigDecimal currentBalance = getSupplierBalance(supplierId);
        
        Integer transactionCount = Math.toIntExact(supplierLedgerRepository.countBySupplierId(supplierId));

        return new SupplierLedgerSummary(
            supplier.getId(),
            supplier.getSupplierName(),
            totalPurchases,
            totalPayments,
            totalDebits,
            totalCredits,
            currentBalance,
            transactionCount
        );
    }

    @Override
    public SupplierLedgerSummary getLedgerSummaryForDateRange(
            Long supplierId, LocalDate startDate, LocalDate endDate) {
        Supplier supplier = findSupplierById(supplierId);
        
        BigDecimal totalPurchases = supplierLedgerRepository
            .getTotalBySupplierAndTypeAndDateRange(supplierId, "PURCHASE", startDate, endDate)
            .orElse(BigDecimal.ZERO);
        
        BigDecimal totalPayments = supplierLedgerRepository
            .getTotalBySupplierAndTypeAndDateRange(supplierId, "PAYMENT", startDate, endDate)
            .orElse(BigDecimal.ZERO);
        
        BigDecimal totalDebits = supplierLedgerRepository
            .getTotalDebitsBySupplierAndDateRange(supplierId, startDate, endDate)
            .orElse(BigDecimal.ZERO);
        
        BigDecimal totalCredits = supplierLedgerRepository
            .getTotalCreditsBySupplierAndDateRange(supplierId, startDate, endDate)
            .orElse(BigDecimal.ZERO);
        
        BigDecimal balanceAsOfEnd = getSupplierBalanceAsOfDate(supplierId, endDate);
        
        Integer transactionCount = supplierLedgerRepository
            .countBySupplierIdAndTransactionDateBetween(supplierId, startDate, endDate);

        return new SupplierLedgerSummary(
            supplier.getId(),
            supplier.getSupplierName(),
            totalPurchases,
            totalPayments,
            totalDebits,
            totalCredits,
            balanceAsOfEnd,
            transactionCount
        );
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private SupplierLedger findLedgerEntryById(Long id) {
        return supplierLedgerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier ledger entry not found: " + id));
    }

    private Supplier findSupplierById(Long id) {
        return supplierRepository.findByIdAndDeletedAtIsNull(id);
    }

    private PageResponse<SupplierLedgerResponse> createPageResponse(Page<SupplierLedger> ledgerPage) {
        List<SupplierLedgerResponse> content = ledgerPage.getContent().stream()
            .map(supplierLedgerMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<SupplierLedgerResponse>builder()
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

    private LedgerEntryDTO toDTO(SupplierLedger entity) {
        LedgerEntryDTO dto = new LedgerEntryDTO();
        dto.setId(entity.getId());
        dto.setSupplierId(entity.getSupplier().getId());
        dto.setSupplierName(entity.getSupplier().getSupplierName());
        dto.setTransactionType(entity.getTransactionType());
        dto.setReferenceNumber(entity.getReferenceNumber());
        dto.setTransactionDate(entity.getTransactionDate());
        dto.setDueDate(entity.getDueDate());
        dto.setDebitAmount(entity.getDebitAmount());
        dto.setCreditAmount(entity.getCreditAmount());
        dto.setBalance(entity.getBalance());
        dto.setDescription(entity.getDescription());
        dto.setIsReconciled(entity.getIsReconciled());
//        dto.setReconciledDate(entity.getReconciledDate());
//        dto.setReconciledBy(entity.getReconciledBy());
//        dto.setNotes(entity.getNotes());
        dto.setCreatedBy(String.valueOf(entity.getCreatedBy()));
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}
