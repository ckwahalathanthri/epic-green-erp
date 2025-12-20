package lk.epicgreen.erp.supplier.service;

import lk.epicgreen.erp.supplier.dto.SupplierLedgerRequest;
import lk.epicgreen.erp.supplier.entity.Supplier;
import lk.epicgreen.erp.supplier.entity.SupplierLedger;
import lk.epicgreen.erp.supplier.repository.SupplierLedgerRepository;
import lk.epicgreen.erp.supplier.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SupplierLedger Service Implementation
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public abstract class SupplierLedgerServiceImpl implements SupplierLedgerService {
    
    private final SupplierLedgerRepository supplierLedgerRepository;
    private final SupplierRepository supplierRepository;
    
    @Override
    public SupplierLedger createLedgerEntry(SupplierLedgerRequest request) {
        log.info("Creating supplier ledger entry for supplier: {}", request.getSupplierId());
        
        // Get supplier to update balance
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
            .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + request.getSupplierId()));
        
        SupplierLedger ledger = new SupplierLedger();
        ledger.setSupplierId(request.getSupplierId());
        ledger.setSupplierName(request.getSupplierName() != null ? 
            request.getSupplierName() : supplier.getName());
        
        ledger.setTransactionDate(request.getTransactionDate());
        ledger.setTransactionType(request.getTransactionType());
        ledger.setReferenceType(request.getReferenceType());
        ledger.setReferenceId(request.getReferenceId());
        ledger.setReferenceNumber(request.getReferenceNumber());
        ledger.setDescription(request.getDescription());
        
        // Convert amounts to BigDecimal
        BigDecimal debitAmount = request.getDebitAmount() != null ? 
            BigDecimal.valueOf(request.getDebitAmount()) : BigDecimal.ZERO;
        BigDecimal creditAmount = request.getCreditAmount() != null ? 
            BigDecimal.valueOf(request.getCreditAmount()) : BigDecimal.ZERO;
        
        ledger.setDebitAmount(debitAmount);
        ledger.setCreditAmount(creditAmount);
        
        // Payment details
        ledger.setPaymentType(request.getPaymentType());
        ledger.setChequeNo(request.getChequeNo());
        ledger.setChequeDate(request.getChequeDate());
        ledger.setBank(request.getBank());
        ledger.setBankAccount(request.getBankAccount());
        ledger.setTransactionReference(request.getTransactionReference());
        ledger.setRecordedBy(request.getRecordedByUserId() != null ? 
            String.valueOf(request.getRecordedByUserId()) : null);
        ledger.setNotes(request.getNotes());
        
        // Calculate new balance
        BigDecimal currentBalance = supplier.getOutstandingBalance() != null ? 
            supplier.getOutstandingBalance() : BigDecimal.ZERO;
        BigDecimal newBalance = currentBalance.add(debitAmount).subtract(creditAmount);
        ledger.setBalanceAmount(newBalance);
        
        // Update supplier balance
        supplier.setOutstandingBalance(newBalance);
        supplier.setLastTransactionAt(LocalDateTime.now());
        supplierRepository.save(supplier);
        
        return supplierLedgerRepository.save(ledger);
    }
    
    @Override
    public SupplierLedger updateLedgerEntry(Long id, SupplierLedgerRequest request) {
        log.info("Updating supplier ledger entry: {}", id);
        SupplierLedger existing = getLedgerEntryById(id);
        
        // Store old amounts for balance recalculation
        BigDecimal oldDebit = existing.getDebitAmount() != null ? 
            existing.getDebitAmount() : BigDecimal.ZERO;
        BigDecimal oldCredit = existing.getCreditAmount() != null ? 
            existing.getCreditAmount() : BigDecimal.ZERO;
        
        existing.setTransactionDate(request.getTransactionDate());
        existing.setTransactionType(request.getTransactionType());
        existing.setReferenceType(request.getReferenceType());
        existing.setReferenceId(request.getReferenceId());
        existing.setReferenceNumber(request.getReferenceNumber());
        existing.setDescription(request.getDescription());
        
        // Convert new amounts to BigDecimal
        BigDecimal newDebit = request.getDebitAmount() != null ? 
            BigDecimal.valueOf(request.getDebitAmount()) : BigDecimal.ZERO;
        BigDecimal newCredit = request.getCreditAmount() != null ? 
            BigDecimal.valueOf(request.getCreditAmount()) : BigDecimal.ZERO;
        
        existing.setDebitAmount(newDebit);
        existing.setCreditAmount(newCredit);
        existing.setPaymentType(request.getPaymentType());
        existing.setChequeNo(request.getChequeNo());
        existing.setChequeDate(request.getChequeDate());
        existing.setBank(request.getBank());
        existing.setBankAccount(request.getBankAccount());
        existing.setTransactionReference(request.getTransactionReference());
        existing.setNotes(request.getNotes());
        
        // Recalculate balance
        Supplier supplier = supplierRepository.findById(existing.getSupplierId())
            .orElseThrow(() -> new RuntimeException("Supplier not found"));
        
        BigDecimal currentBalance = supplier.getOutstandingBalance() != null ? 
            supplier.getOutstandingBalance() : BigDecimal.ZERO;
        
        // Remove old transaction effect and add new transaction effect
        BigDecimal adjustedBalance = currentBalance
            .subtract(oldDebit)
            .add(oldCredit)
            .add(newDebit)
            .subtract(newCredit);
        
        existing.setBalanceAmount(adjustedBalance);
        supplier.setOutstandingBalance(adjustedBalance);
        supplierRepository.save(supplier);
        
        return supplierLedgerRepository.save(existing);
    }
    
    @Override
    public void deleteLedgerEntry(Long id) {
        log.info("Deleting supplier ledger entry: {}", id);
        SupplierLedger ledger = getLedgerEntryById(id);
        
        // Update supplier balance by reversing this transaction
        Supplier supplier = supplierRepository.findById(ledger.getSupplierId())
            .orElseThrow(() -> new RuntimeException("Supplier not found"));
        
        BigDecimal currentBalance = supplier.getOutstandingBalance() != null ? 
            supplier.getOutstandingBalance() : BigDecimal.ZERO;
        BigDecimal debit = ledger.getDebitAmount() != null ? 
            ledger.getDebitAmount() : BigDecimal.ZERO;
        BigDecimal credit = ledger.getCreditAmount() != null ? 
            ledger.getCreditAmount() : BigDecimal.ZERO;
        
        BigDecimal newBalance = currentBalance.subtract(debit).add(credit);
        supplier.setOutstandingBalance(newBalance);
        supplierRepository.save(supplier);
        
        supplierLedgerRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public SupplierLedger getLedgerEntryById(Long id) {
        return supplierLedgerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Supplier ledger entry not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierLedger> getAllLedgerEntries() {
        return supplierLedgerRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<SupplierLedger> getAllLedgerEntries(Pageable pageable) {
        return supplierLedgerRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SupplierLedger> getLedgerEntriesBySupplier(Long supplierId) {
        return supplierLedgerRepository.findBySupplierId(supplierId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<SupplierLedger> getLedgerEntriesBySupplier(Long supplierId, Pageable pageable) {
        return supplierLedgerRepository.findBySupplierId(supplierId, pageable);
    }
    
    @Transactional(readOnly = true)
    public List<SupplierLedger> getLedgerEntriesByDateRange(Long supplierId, LocalDate startDate, LocalDate endDate) {
        if (supplierId != null) {
            return supplierLedgerRepository.findBySupplierIdAndTransactionDateBetween(
                supplierId, startDate, endDate);
        } else {
            return supplierLedgerRepository.findByTransactionDateBetween(startDate, endDate);
        }
    }
    
    @Transactional(readOnly = true)
    public List<SupplierLedger> getLedgerEntriesByType(Long supplierId, String transactionType) {
        if (supplierId != null) {
            return supplierLedgerRepository.findBySupplierIdAndTransactionType(supplierId, transactionType);
        } else {
            return supplierLedgerRepository.findByTransactionType(transactionType);
        }
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getTotalDebits(Long supplierId, LocalDate startDate, LocalDate endDate) {
        List<SupplierLedger> entries = getLedgerEntriesByDateRange(supplierId, startDate, endDate);
        return entries.stream()
            .map(SupplierLedger::getDebitAmount)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getTotalCredits(Long supplierId, LocalDate startDate, LocalDate endDate) {
        List<SupplierLedger> entries = getLedgerEntriesByDateRange(supplierId, startDate, endDate);
        return entries.stream()
            .map(SupplierLedger::getCreditAmount)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getSupplierBalance(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
            .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + supplierId));
        return supplier.getOutstandingBalance() != null ? 
            supplier.getOutstandingBalance() : BigDecimal.ZERO;
    }
    
    @Transactional(readOnly = true)
    public Map<String, Object> getSupplierLedgerSummary(Long supplierId, LocalDate startDate, LocalDate endDate) {
        Map<String, Object> summary = new HashMap<>();
        
        BigDecimal totalDebits = getTotalDebits(supplierId, startDate, endDate);
        BigDecimal totalCredits = getTotalCredits(supplierId, startDate, endDate);
        BigDecimal currentBalance = getSupplierBalance(supplierId);
        
        summary.put("supplierId", supplierId);
        summary.put("startDate", startDate);
        summary.put("endDate", endDate);
        summary.put("totalDebits", totalDebits.doubleValue());
        summary.put("totalCredits", totalCredits.doubleValue());
        summary.put("currentBalance", currentBalance.doubleValue());
        summary.put("netChange", totalDebits.subtract(totalCredits).doubleValue());
        
        return summary;
    }
    
    @Transactional(readOnly = true)
    public List<SupplierLedger> getUnreconciledEntries(Long supplierId) {
        if (supplierId != null) {
            return supplierLedgerRepository.findAll().stream()
                .filter(entry -> entry.getSupplierId().equals(supplierId))
                .filter(entry -> !Boolean.TRUE.equals(entry.getIsReconciled()))
                .collect(Collectors.toList());
        } else {
            return supplierLedgerRepository.findAll().stream()
                .filter(entry -> !Boolean.TRUE.equals(entry.getIsReconciled()))
                .collect(Collectors.toList());
        }
    }
    
    @Transactional
    public void reconcileLedgerEntry(Long id) {
        log.info("Reconciling ledger entry: {}", id);
        SupplierLedger ledger = getLedgerEntryById(id);
        ledger.setIsReconciled(true);
        ledger.setReconciledDate(LocalDate.now());
        supplierLedgerRepository.save(ledger);
    }
    
    @Transactional
    public void unreconcileLedgerEntry(Long id) {
        log.info("Unreconciling ledger entry: {}", id);
        SupplierLedger ledger = getLedgerEntryById(id);
        ledger.setIsReconciled(false);
        ledger.setReconciledDate(null);
        supplierLedgerRepository.save(ledger);
    }
    
    @Transactional
    public SupplierLedger recordPurchase(Long supplierId, Long purchaseOrderId, String poNumber, 
                                        Double amount, String description) {
        SupplierLedgerRequest request = SupplierLedgerRequest.builder()
            .supplierId(supplierId)
            .transactionDate(LocalDate.now())
            .transactionType("PURCHASE")
            .referenceType("PURCHASE_ORDER")
            .referenceId(purchaseOrderId)
            .referenceNumber(poNumber)
            .description(description != null ? description : "Purchase from supplier")
            .debitAmount(amount)
            .creditAmount(0.0)
            .build();
        
        return createLedgerEntry(request);
    }
    
    @Transactional
    public SupplierLedger recordPayment(Long supplierId, String paymentType, Double amount, 
                                       String chequeNo, LocalDate chequeDate, String bank, 
                                       String description) {
        SupplierLedgerRequest request = SupplierLedgerRequest.builder()
            .supplierId(supplierId)
            .transactionDate(LocalDate.now())
            .transactionType("PAYMENT")
            .referenceType("PAYMENT")
            .description(description != null ? description : "Payment to supplier")
            .debitAmount(0.0)
            .creditAmount(amount)
            .paymentType(paymentType)
            .chequeNo(chequeNo)
            .chequeDate(chequeDate)
            .bank(bank)
            .build();
        
        return createLedgerEntry(request);
    }
    
    @Transactional
    public SupplierLedger recordCreditNote(Long supplierId, String creditNoteNumber, 
                                          Double amount, String description) {
        SupplierLedgerRequest request = SupplierLedgerRequest.builder()
            .supplierId(supplierId)
            .transactionDate(LocalDate.now())
            .transactionType("CREDIT_NOTE")
            .referenceType("ADJUSTMENT")
            .referenceNumber(creditNoteNumber)
            .description(description != null ? description : "Credit note from supplier")
            .debitAmount(0.0)
            .creditAmount(amount)
            .build();
        
        return createLedgerEntry(request);
    }
    
    @Transactional
    public SupplierLedger recordDebitNote(Long supplierId, String debitNoteNumber, 
                                         Double amount, String description) {
        SupplierLedgerRequest request = SupplierLedgerRequest.builder()
            .supplierId(supplierId)
            .transactionDate(LocalDate.now())
            .transactionType("DEBIT_NOTE")
            .referenceType("ADJUSTMENT")
            .referenceNumber(debitNoteNumber)
            .description(description != null ? description : "Debit note to supplier")
            .debitAmount(amount)
            .creditAmount(0.0)
            .build();
        
        return createLedgerEntry(request);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAgingSummary() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return suppliers.stream()
            .filter(supplier -> {
                BigDecimal balance = supplier.getOutstandingBalance();
                return balance != null && balance.compareTo(BigDecimal.ZERO) > 0;
            })
            .map(supplier -> {
                Map<String, Object> aging = new HashMap<>();
                aging.put("supplierId", supplier.getId());
                aging.put("supplierName", supplier.getName());
                
                BigDecimal balance = supplier.getOutstandingBalance();
                aging.put("totalOutstanding", balance != null ? balance.doubleValue() : 0.0);
                
                // Calculate aging buckets
                List<SupplierLedger> unpaidEntries = supplierLedgerRepository.findBySupplierId(supplier.getId())
                    .stream()
                    .filter(ledgerEntry -> {
                        BigDecimal debit = ledgerEntry.getDebitAmount();
                        BigDecimal credit = ledgerEntry.getCreditAmount();
                        return debit != null && credit != null && 
                               debit.compareTo(BigDecimal.ZERO) > 0 && 
                               credit.compareTo(BigDecimal.ZERO) > 0;
                    })
                    .collect(Collectors.toList());
                
                LocalDate today = LocalDate.now();
                BigDecimal current = BigDecimal.ZERO;
                BigDecimal days30 = BigDecimal.ZERO;
                BigDecimal days60 = BigDecimal.ZERO;
                BigDecimal days90 = BigDecimal.ZERO;
                BigDecimal over90 = BigDecimal.ZERO;
                
                for (SupplierLedger entry : unpaidEntries) {
                    long daysPast = java.time.temporal.ChronoUnit.DAYS.between(
                        entry.getTransactionDate(), today);
                    BigDecimal entryBalance = entry.getDebitAmount()
                        .subtract(entry.getCreditAmount());
                    
                    if (daysPast <= 30) {
                        current = current.add(entryBalance);
                    } else if (daysPast <= 60) {
                        days30 = days30.add(entryBalance);
                    } else if (daysPast <= 90) {
                        days60 = days60.add(entryBalance);
                    } else if (daysPast <= 120) {
                        days90 = days90.add(entryBalance);
                    } else {
                        over90 = over90.add(entryBalance);
                    }
                }
                
                aging.put("current", current.doubleValue());
                aging.put("days30", days30.doubleValue());
                aging.put("days60", days60.doubleValue());
                aging.put("days90", days90.doubleValue());
                aging.put("over90", over90.doubleValue());
                
                return aging;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getSuppliersWithHighestCredit(int limit) {
        List<Supplier> allSuppliers = supplierRepository.findAll();
        
        return allSuppliers.stream()
            .filter(s -> {
                BigDecimal balance = s.getOutstandingBalance();
                return balance != null && balance.compareTo(BigDecimal.ZERO) > 0;
            })
            .sorted((s1, s2) -> {
                BigDecimal balance1 = s1.getOutstandingBalance();
                BigDecimal balance2 = s2.getOutstandingBalance();
                return balance2.compareTo(balance1); // Descending order
            })
            .limit(limit)
            .map(supplier -> {
                Map<String, Object> summary = new HashMap<>();
                summary.put("supplierId", supplier.getId());
                summary.put("supplierName", supplier.getName());
                summary.put("outstandingBalance", supplier.getOutstandingBalance().doubleValue());
                summary.put("creditLimit", supplier.getCreditLimit() != null ? 
                    supplier.getCreditLimit().doubleValue() : 0.0);
                summary.put("creditDays", supplier.getCreditDays());
                return summary;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<Supplier> allSuppliers = supplierRepository.findAll();
        
        // Total outstanding
        BigDecimal totalOutstanding = allSuppliers.stream()
            .map(Supplier::getOutstandingBalance)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        stats.put("totalOutstanding", totalOutstanding.doubleValue());
        
        // Suppliers with outstanding balance
        long suppliersWithBalance = allSuppliers.stream()
            .filter(s -> {
                BigDecimal balance = s.getOutstandingBalance();
                return balance != null && balance.compareTo(BigDecimal.ZERO) > 0;
            })
            .count();
        
        stats.put("suppliersWithOutstanding", suppliersWithBalance);
        
        // Current month statistics
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now();
        
        BigDecimal monthlyPurchases = getTotalDebits(null, startOfMonth, endOfMonth);
        BigDecimal monthlyPayments = getTotalCredits(null, startOfMonth, endOfMonth);
        
        stats.put("monthlyPurchases", monthlyPurchases.doubleValue());
        stats.put("monthlyPayments", monthlyPayments.doubleValue());
        stats.put("monthlyNet", monthlyPurchases.subtract(monthlyPayments).doubleValue());
        
        return stats;
    }
}
