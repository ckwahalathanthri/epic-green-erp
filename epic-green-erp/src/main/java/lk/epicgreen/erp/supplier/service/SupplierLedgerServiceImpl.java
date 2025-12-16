package lk.epicgreen.erp.supplier.service;

import lk.epicgreen.erp.supplier.dto.SupplierLedgerRequest;
import lk.epicgreen.erp.supplier.entity.Supplier;
import lk.epicgreen.erp.supplier.entity.SupplierLedger;
import lk.epicgreen.erp.supplier.repository.SupplierLedgerRepository;
import lk.epicgreen.erp.supplier.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SupplierLedger Service Implementation
 * Implementation of supplier ledger service operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SupplierLedgerServiceImpl implements SupplierLedgerService {
    
    private final SupplierLedgerRepository ledgerRepository;
    private final SupplierRepository supplierRepository;
    
    @Override
    public SupplierLedger createLedgerEntry(SupplierLedgerRequest request) {
        log.info("Creating ledger entry for supplier: {}", request.getSupplierId());
        
        // Validate reference number
        if (request.getReferenceNumber() != null && 
            ledgerRepository.existsByReferenceNumber(request.getReferenceNumber())) {
            throw new RuntimeException("Reference number already exists: " + request.getReferenceNumber());
        }
        
        // Get supplier
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
            .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + request.getSupplierId()));
        
        // Calculate balance before transaction
        Double currentBalance = getBalanceBySupplierId(request.getSupplierId());
        
        SupplierLedger ledger = new SupplierLedger();
        ledger.setSupplierId(request.getSupplierId());
        ledger.setSupplierName(supplier.getSupplierName());
        ledger.setTransactionType(request.getTransactionType());
        ledger.setTransactionDate(request.getTransactionDate() != null ? 
            request.getTransactionDate() : LocalDate.now());
        ledger.setReferenceNumber(request.getReferenceNumber());
        ledger.setReferenceType(request.getReferenceType());
        ledger.setReferenceId(request.getReferenceId());
        ledger.setDebitAmount(request.getDebitAmount() != null ? request.getDebitAmount() : 0.0);
        ledger.setCreditAmount(request.getCreditAmount() != null ? request.getCreditAmount() : 0.0);
        ledger.setPaymentMethod(request.getPaymentMethod());
        ledger.setChequeNumber(request.getChequeNumber());
        ledger.setChequeDate(request.getChequeDate());
        ledger.setBankName(request.getBankName());
        ledger.setDescription(request.getDescription());
        ledger.setNotes(request.getNotes());
        ledger.setRecordedByUserId(request.getRecordedByUserId());
        
        // Calculate balance after transaction
        Double balanceAfter = currentBalance + ledger.getDebitAmount() - ledger.getCreditAmount();
        ledger.setBalanceAfter(balanceAfter);
        
        SupplierLedger saved = ledgerRepository.save(ledger);
        
        // Update supplier balance
        supplier.setCurrentBalance(balanceAfter);
        supplier.setLastTransactionDate(LocalDateTime.now());
        supplierRepository.save(supplier);
        
        return saved;
    }
    
    @Override
    public SupplierLedger updateLedgerEntry(Long id, SupplierLedgerRequest request) {
        log.info("Updating ledger entry: {}", id);
        SupplierLedger existing = getLedgerEntryById(id);
        
        // Validate reference number if changed
        if (request.getReferenceNumber() != null && 
            !request.getReferenceNumber().equals(existing.getReferenceNumber()) &&
            ledgerRepository.existsByReferenceNumber(request.getReferenceNumber())) {
            throw new RuntimeException("Reference number already exists: " + request.getReferenceNumber());
        }
        
        existing.setTransactionType(request.getTransactionType());
        existing.setTransactionDate(request.getTransactionDate());
        existing.setReferenceNumber(request.getReferenceNumber());
        existing.setReferenceType(request.getReferenceType());
        existing.setReferenceId(request.getReferenceId());
        existing.setDebitAmount(request.getDebitAmount());
        existing.setCreditAmount(request.getCreditAmount());
        existing.setPaymentMethod(request.getPaymentMethod());
        existing.setChequeNumber(request.getChequeNumber());
        existing.setChequeDate(request.getChequeDate());
        existing.setBankName(request.getBankName());
        existing.setDescription(request.getDescription());
        existing.setNotes(request.getNotes());
        existing.setUpdatedAt(LocalDateTime.now());
        
        return ledgerRepository.save(existing);
    }
    
    @Override
    public void deleteLedgerEntry(Long id) {
        log.info("Deleting ledger entry: {}", id);
        SupplierLedger ledger = getLedgerEntryById(id);
        
        if (!canDeleteLedgerEntry(id)) {
            throw new RuntimeException("Cannot delete ledger entry");
        }
        
        ledgerRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public SupplierLedger getLedgerEntryById(Long id) {
        return ledgerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ledger entry not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public SupplierLedger getLedgerEntryByReferenceNumber(String referenceNumber) {
        return ledgerRepository.findByReferenceNumber(referenceNumber)
            .orElseThrow(() -> new RuntimeException("Ledger entry not found with reference: " + referenceNumber));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierLedger> getAllLedgerEntries() {
        return ledgerRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<SupplierLedger> getAllLedgerEntries(Pageable pageable) {
        return ledgerRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<SupplierLedger> searchLedgerEntries(String keyword, Pageable pageable) {
        return ledgerRepository.searchLedgerEntries(keyword, pageable);
    }
    
    @Override
    public SupplierLedger recordPurchase(Long supplierId, Double amount, String referenceType, 
                                        Long referenceId, String description, LocalDate transactionDate) {
        log.info("Recording purchase for supplier: {}, amount: {}", supplierId, amount);
        
        SupplierLedgerRequest request = new SupplierLedgerRequest();
        request.setSupplierId(supplierId);
        request.setTransactionType("PURCHASE");
        request.setTransactionDate(transactionDate);
        request.setReferenceType(referenceType);
        request.setReferenceId(referenceId);
        request.setDebitAmount(amount);
        request.setCreditAmount(0.0);
        request.setDescription(description);
        
        return createLedgerEntry(request);
    }
    
    @Override
    public SupplierLedger recordPayment(Long supplierId, Double amount, String paymentMethod, 
                                       String referenceNumber, String description, LocalDate transactionDate) {
        log.info("Recording payment for supplier: {}, amount: {}", supplierId, amount);
        
        SupplierLedgerRequest request = new SupplierLedgerRequest();
        request.setSupplierId(supplierId);
        request.setTransactionType("PAYMENT");
        request.setTransactionDate(transactionDate);
        request.setPaymentMethod(paymentMethod);
        request.setReferenceNumber(referenceNumber);
        request.setDebitAmount(0.0);
        request.setCreditAmount(amount);
        request.setDescription(description);
        
        return createLedgerEntry(request);
    }
    
    @Override
    public SupplierLedger recordReturn(Long supplierId, Double amount, String referenceType, 
                                      Long referenceId, String description, LocalDate transactionDate) {
        log.info("Recording return for supplier: {}, amount: {}", supplierId, amount);
        
        SupplierLedgerRequest request = new SupplierLedgerRequest();
        request.setSupplierId(supplierId);
        request.setTransactionType("RETURN");
        request.setTransactionDate(transactionDate);
        request.setReferenceType(referenceType);
        request.setReferenceId(referenceId);
        request.setDebitAmount(0.0);
        request.setCreditAmount(amount);
        request.setDescription(description);
        
        return createLedgerEntry(request);
    }
    
    @Override
    public SupplierLedger recordAdjustment(Long supplierId, Double debitAmount, Double creditAmount, 
                                          String description, String notes, LocalDate transactionDate) {
        log.info("Recording adjustment for supplier: {}", supplierId);
        
        SupplierLedgerRequest request = new SupplierLedgerRequest();
        request.setSupplierId(supplierId);
        request.setTransactionType("ADJUSTMENT");
        request.setTransactionDate(transactionDate);
        request.setDebitAmount(debitAmount);
        request.setCreditAmount(creditAmount);
        request.setDescription(description);
        request.setNotes(notes);
        
        return createLedgerEntry(request);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierLedger> getLedgerEntriesBySupplier(Long supplierId) {
        return ledgerRepository.findBySupplierId(supplierId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<SupplierLedger> getLedgerEntriesBySupplier(Long supplierId, Pageable pageable) {
        return ledgerRepository.findBySupplierId(supplierId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierLedger> getLedgerEntriesBySupplierAndDateRange(Long supplierId, LocalDate startDate, LocalDate endDate) {
        return ledgerRepository.findBySupplierIdAndTransactionDateBetween(supplierId, startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<SupplierLedger> getLedgerEntriesBySupplierAndDateRange(Long supplierId, LocalDate startDate, 
                                                                       LocalDate endDate, Pageable pageable) {
        return ledgerRepository.findBySupplierIdAndTransactionDateBetween(supplierId, startDate, endDate, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierLedger> getDebitEntriesBySupplier(Long supplierId) {
        return ledgerRepository.findDebitEntriesBySupplierId(supplierId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierLedger> getCreditEntriesBySupplier(Long supplierId) {
        return ledgerRepository.findCreditEntriesBySupplierId(supplierId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierLedger> getPurchaseEntries() {
        return ledgerRepository.findPurchaseEntries();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierLedger> getPaymentEntries() {
        return ledgerRepository.findPaymentEntries();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierLedger> getReturnEntries() {
        return ledgerRepository.findReturnEntries();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierLedger> getAdjustmentEntries() {
        return ledgerRepository.findAdjustmentEntries();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierLedger> getCashPayments() {
        return ledgerRepository.findCashPayments();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierLedger> getChequePayments() {
        return ledgerRepository.findChequePayments();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierLedger> getBankTransferPayments() {
        return ledgerRepository.findBankTransferPayments();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierLedger> getCreditPurchases() {
        return ledgerRepository.findCreditPurchases();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierLedger> getRecentEntriesBySupplier(Long supplierId, int limit) {
        return ledgerRepository.findRecentEntriesBySupplierId(supplierId, PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierLedger> getTodaysEntries() {
        return ledgerRepository.findTodaysEntries(LocalDate.now());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierLedger> getThisMonthEntries() {
        LocalDate now = LocalDate.now();
        return ledgerRepository.findThisMonthEntries(now.getYear(), now.getMonthValue());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierLedger> getLargeTransactions(Double threshold) {
        return ledgerRepository.findLargeTransactions(threshold);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierLedger> getSupplierStatement(Long supplierId, LocalDate startDate, LocalDate endDate) {
        return ledgerRepository.getSupplierStatement(supplierId, startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSupplierStatementSummary(Long supplierId, LocalDate startDate, LocalDate endDate) {
        Map<String, Object> summary = new HashMap<>();
        
        Double openingBalance = getOpeningBalance(supplierId, startDate);
        Double totalDebit = getTotalDebitBySupplierIdAndDateRange(supplierId, startDate, endDate);
        Double totalCredit = getTotalCreditBySupplierIdAndDateRange(supplierId, startDate, endDate);
        Double closingBalance = openingBalance + totalDebit - totalCredit;
        
        summary.put("supplierId", supplierId);
        summary.put("startDate", startDate);
        summary.put("endDate", endDate);
        summary.put("openingBalance", openingBalance);
        summary.put("totalDebit", totalDebit);
        summary.put("totalCredit", totalCredit);
        summary.put("closingBalance", closingBalance);
        summary.put("entries", getSupplierStatement(supplierId, startDate, endDate));
        
        return summary;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getOpeningBalance(Long supplierId, LocalDate startDate) {
        List<Double> results = ledgerRepository.getOpeningBalance(supplierId, startDate, PageRequest.of(0, 1));
        return results.isEmpty() ? 0.0 : results.get(0);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalDebitBySupplierId(Long supplierId) {
        Double total = ledgerRepository.getTotalDebitAmountBySupplierId(supplierId);
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalCreditBySupplierId(Long supplierId) {
        Double total = ledgerRepository.getTotalCreditAmountBySupplierId(supplierId);
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getBalanceBySupplierId(Long supplierId) {
        Double balance = ledgerRepository.getBalanceBySupplierId(supplierId);
        return balance != null ? balance : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalDebitBySupplierIdAndDateRange(Long supplierId, LocalDate startDate, LocalDate endDate) {
        Double total = ledgerRepository.getTotalDebitAmountBySupplierIdAndDateRange(supplierId, startDate, endDate);
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalCreditBySupplierIdAndDateRange(Long supplierId, LocalDate startDate, LocalDate endDate) {
        Double total = ledgerRepository.getTotalCreditAmountBySupplierIdAndDateRange(supplierId, startDate, endDate);
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalPurchasesBySupplierId(Long supplierId) {
        Double total = ledgerRepository.getTotalPurchasesBySupplierId(supplierId);
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalPaymentsBySupplierId(Long supplierId) {
        Double total = ledgerRepository.getTotalPaymentsBySupplierId(supplierId);
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalReturnsBySupplierId(Long supplierId) {
        Double total = ledgerRepository.getTotalReturnsBySupplierId(supplierId);
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validateLedgerEntry(SupplierLedger ledgerEntry) {
        return ledgerEntry.getSupplierId() != null &&
               ledgerEntry.getTransactionType() != null &&
               ledgerEntry.getTransactionDate() != null &&
               (ledgerEntry.getDebitAmount() > 0 || ledgerEntry.getCreditAmount() > 0);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isReferenceNumberAvailable(String referenceNumber) {
        return !ledgerRepository.existsByReferenceNumber(referenceNumber);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canDeleteLedgerEntry(Long ledgerEntryId) {
        // Can delete if not referenced by other transactions
        return true;
    }
    
    @Override
    public List<SupplierLedger> createBulkLedgerEntries(List<SupplierLedgerRequest> requests) {
        return requests.stream()
            .map(this::createLedgerEntry)
            .collect(Collectors.toList());
    }
    
    @Override
    public int deleteBulkLedgerEntries(List<Long> ledgerEntryIds) {
        int count = 0;
        for (Long id : ledgerEntryIds) {
            try {
                deleteLedgerEntry(id);
                count++;
            } catch (Exception e) {
                log.error("Error deleting ledger entry: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getLedgerStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalEntries", ledgerRepository.count());
        stats.put("totalDebitAmount", getTotalDebitAmount());
        stats.put("totalCreditAmount", getTotalCreditAmount());
        stats.put("averageTransactionAmount", getAverageTransactionAmount());
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTransactionTypeDistribution() {
        List<Object[]> results = ledgerRepository.getTransactionTypeDistribution();
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("transactionType", result[0]);
                map.put("entryCount", result[1]);
                map.put("totalDebit", result[2]);
                map.put("totalCredit", result[3]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPaymentMethodDistribution() {
        List<Object[]> results = ledgerRepository.getPaymentMethodDistribution();
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("paymentMethod", result[0]);
                map.put("entryCount", result[1]);
                map.put("totalAmount", result[2]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getDailyTransactionSummary(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = ledgerRepository.getDailyTransactionSummary(startDate, endDate);
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("transactionDate", result[0]);
                map.put("entryCount", result[1]);
                map.put("totalDebit", result[2]);
                map.put("totalCredit", result[3]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMonthlyTransactionSummary() {
        List<Object[]> results = ledgerRepository.getMonthlyTransactionSummary();
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("year", result[0]);
                map.put("month", result[1]);
                map.put("entryCount", result[2]);
                map.put("totalDebit", result[3]);
                map.put("totalCredit", result[4]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalDebitAmount() {
        Double total = ledgerRepository.getTotalDebitAmount();
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalCreditAmount() {
        Double total = ledgerRepository.getTotalCreditAmount();
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getAverageTransactionAmount() {
        Double average = ledgerRepository.getAverageTransactionAmount();
        return average != null ? average : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getSuppliersWithHighestDebit(int limit) {
        List<Object[]> results = ledgerRepository.getSuppliersWithHighestDebit(PageRequest.of(0, limit));
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("supplierId", result[0]);
                map.put("supplierName", result[1]);
                map.put("totalDebit", result[2]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getSuppliersWithHighestCredit(int limit) {
        List<Object[]> results = ledgerRepository.getSuppliersWithHighestCredit(PageRequest.of(0, limit));
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("supplierId", result[0]);
                map.put("supplierName", result[1]);
                map.put("totalCredit", result[2]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("statistics", getLedgerStatistics());
        dashboard.put("transactionTypeDistribution", getTransactionTypeDistribution());
        dashboard.put("paymentMethodDistribution", getPaymentMethodDistribution());
        dashboard.put("monthlyTransactionSummary", getMonthlyTransactionSummary());
        dashboard.put("suppliersWithHighestDebit", getSuppliersWithHighestDebit(10));
        dashboard.put("suppliersWithHighestCredit", getSuppliersWithHighestCredit(10));
        dashboard.put("todaysEntries", getTodaysEntries());
        
        return dashboard;
    }
}
