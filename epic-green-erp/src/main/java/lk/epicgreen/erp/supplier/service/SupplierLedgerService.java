package lk.epicgreen.erp.supplier.service;

import lk.epicgreen.erp.supplier.dto.SupplierLedgerRequest;
import lk.epicgreen.erp.supplier.entity.SupplierLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * SupplierLedger Service Interface
 * Service for supplier ledger operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface SupplierLedgerService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    SupplierLedger createLedgerEntry(SupplierLedgerRequest request);
    SupplierLedger updateLedgerEntry(Long id, SupplierLedgerRequest request);
    void deleteLedgerEntry(Long id);
    SupplierLedger getLedgerEntryById(Long id);
    SupplierLedger getLedgerEntryByReferenceNumber(String referenceNumber);
    List<SupplierLedger> getAllLedgerEntries();
    Page<SupplierLedger> getAllLedgerEntries(Pageable pageable);
    Page<SupplierLedger> searchLedgerEntries(String keyword, Pageable pageable);
    
    // ===================================================================
    // TRANSACTION OPERATIONS
    // ===================================================================
    
    SupplierLedger recordPurchase(Long supplierId, Double amount, String referenceType, 
                                  Long referenceId, String description, LocalDate transactionDate);
    SupplierLedger recordPayment(Long supplierId, Double amount, String paymentMethod, 
                                 String referenceNumber, String description, LocalDate transactionDate);
    SupplierLedger recordReturn(Long supplierId, Double amount, String referenceType, 
                                Long referenceId, String description, LocalDate transactionDate);
    SupplierLedger recordAdjustment(Long supplierId, Double debitAmount, Double creditAmount, 
                                    String description, String notes, LocalDate transactionDate);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    List<SupplierLedger> getLedgerEntriesBySupplier(Long supplierId);
    Page<SupplierLedger> getLedgerEntriesBySupplier(Long supplierId, Pageable pageable);
    List<SupplierLedger> getLedgerEntriesBySupplierAndDateRange(Long supplierId, LocalDate startDate, LocalDate endDate);
    Page<SupplierLedger> getLedgerEntriesBySupplierAndDateRange(Long supplierId, LocalDate startDate, LocalDate endDate, Pageable pageable);
    List<SupplierLedger> getDebitEntriesBySupplier(Long supplierId);
    List<SupplierLedger> getCreditEntriesBySupplier(Long supplierId);
    List<SupplierLedger> getPurchaseEntries();
    List<SupplierLedger> getPaymentEntries();
    List<SupplierLedger> getReturnEntries();
    List<SupplierLedger> getAdjustmentEntries();
    List<SupplierLedger> getCashPayments();
    List<SupplierLedger> getChequePayments();
    List<SupplierLedger> getBankTransferPayments();
    List<SupplierLedger> getCreditPurchases();
    List<SupplierLedger> getRecentEntriesBySupplier(Long supplierId, int limit);
    List<SupplierLedger> getTodaysEntries();
    List<SupplierLedger> getThisMonthEntries();
    List<SupplierLedger> getLargeTransactions(Double threshold);
    
    // ===================================================================
    // STATEMENT OPERATIONS
    // ===================================================================
    
    List<SupplierLedger> getSupplierStatement(Long supplierId, LocalDate startDate, LocalDate endDate);
    Map<String, Object> getSupplierStatementSummary(Long supplierId, LocalDate startDate, LocalDate endDate);
    Double getOpeningBalance(Long supplierId, LocalDate startDate);
    
    // ===================================================================
    // AGGREGATE OPERATIONS
    // ===================================================================
    
    Double getTotalDebitBySupplierId(Long supplierId);
    Double getTotalCreditBySupplierId(Long supplierId);
    Double getBalanceBySupplierId(Long supplierId);
    Double getTotalDebitBySupplierIdAndDateRange(Long supplierId, LocalDate startDate, LocalDate endDate);
    Double getTotalCreditBySupplierIdAndDateRange(Long supplierId, LocalDate startDate, LocalDate endDate);
    Double getTotalPurchasesBySupplierId(Long supplierId);
    Double getTotalPaymentsBySupplierId(Long supplierId);
    Double getTotalReturnsBySupplierId(Long supplierId);
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    boolean validateLedgerEntry(SupplierLedger ledgerEntry);
    boolean isReferenceNumberAvailable(String referenceNumber);
    boolean canDeleteLedgerEntry(Long ledgerEntryId);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<SupplierLedger> createBulkLedgerEntries(List<SupplierLedgerRequest> requests);
    int deleteBulkLedgerEntries(List<Long> ledgerEntryIds);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    Map<String, Object> getLedgerStatistics();
    List<Map<String, Object>> getTransactionTypeDistribution();
    List<Map<String, Object>> getPaymentMethodDistribution();
    List<Map<String, Object>> getDailyTransactionSummary(LocalDate startDate, LocalDate endDate);
    List<Map<String, Object>> getMonthlyTransactionSummary();
    Double getTotalDebitAmount();
    Double getTotalCreditAmount();
    Double getAverageTransactionAmount();
    List<Map<String, Object>> getSuppliersWithHighestDebit(int limit);

    @Transactional(readOnly = true)
    List<Map<String, Object>> getAgingSummary();

    List<Map<String, Object>> getSuppliersWithHighestCredit(int limit);
    Map<String, Object> getDashboardStatistics();
}
