package lk.epicgreen.erp.supplier.service;

import lk.epicgreen.erp.supplier.dto.request.SupplierLedgerRequest;
import lk.epicgreen.erp.supplier.dto.response.SupplierLedgerResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for SupplierLedger entity business logic
 * 
 * IMPORTANT: SupplierLedger is IMMUTABLE
 * - CREATE: Allowed (add new ledger entries)
 * - READ: Allowed (query ledger entries)
 * - UPDATE: NOT ALLOWED (financial audit requirement)
 * - DELETE: NOT ALLOWED (financial audit requirement)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface SupplierLedgerService {

    /**
     * Create new ledger entry (IMMUTABLE - cannot be updated or deleted)
     * Automatically calculates running balance
     */
    SupplierLedgerResponse createLedgerEntry(SupplierLedgerRequest request);

    /**
     * Get ledger entry by ID (READ ONLY)
     */
    SupplierLedgerResponse getLedgerEntryById(Long id);

    /**
     * Get all ledger entries for a supplier (READ ONLY)
     */
    PageResponse<SupplierLedgerResponse> getLedgerEntriesBySupplier(Long supplierId, Pageable pageable);

    /**
     * Get ledger entries by supplier and date range (READ ONLY)
     */
    List<SupplierLedgerResponse> getLedgerEntriesBySupplierAndDateRange(
        Long supplierId, LocalDate startDate, LocalDate endDate);

    /**
     * Get ledger entries by transaction type (READ ONLY)
     */
    PageResponse<SupplierLedgerResponse> getLedgerEntriesByType(
        String transactionType, Pageable pageable);

    /**
     * Get ledger entries by reference (READ ONLY)
     */
    List<SupplierLedgerResponse> getLedgerEntriesByReference(
        String referenceType, Long referenceId);

    /**
     * Get supplier current balance (READ ONLY)
     */
    BigDecimal getSupplierBalance(Long supplierId);

    /**
     * Get supplier balance as of date (READ ONLY)
     */
    BigDecimal getSupplierBalanceAsOfDate(Long supplierId, LocalDate asOfDate);

    /**
     * Get all ledger entries with pagination (READ ONLY)
     */
    PageResponse<SupplierLedgerResponse> getAllLedgerEntries(Pageable pageable);

    /**
     * Get purchase entries by supplier (READ ONLY)
     */
    List<SupplierLedgerResponse> getPurchaseEntriesBySupplier(Long supplierId);

    /**
     * Get payment entries by supplier (READ ONLY)
     */
    List<SupplierLedgerResponse> getPaymentEntriesBySupplier(Long supplierId);

    /**
     * Get ledger summary for supplier (READ ONLY)
     */
    SupplierLedgerSummary getLedgerSummary(Long supplierId);

    /**
     * Get ledger summary for date range (READ ONLY)
     */
    SupplierLedgerSummary getLedgerSummaryForDateRange(
        Long supplierId, LocalDate startDate, LocalDate endDate);

    /**
     * DTO for ledger summary
     */
    class SupplierLedgerSummary {
        private Long supplierId;
        private String supplierName;
        private BigDecimal totalPurchases;
        private BigDecimal totalPayments;
        private BigDecimal totalDebits;
        private BigDecimal totalCredits;
        private BigDecimal currentBalance;
        private Integer transactionCount;

        // Constructor
        public SupplierLedgerSummary(Long supplierId, String supplierName, 
                                    BigDecimal totalPurchases, BigDecimal totalPayments,
                                    BigDecimal totalDebits, BigDecimal totalCredits,
                                    BigDecimal currentBalance, Integer transactionCount) {
            this.supplierId = supplierId;
            this.supplierName = supplierName;
            this.totalPurchases = totalPurchases;
            this.totalPayments = totalPayments;
            this.totalDebits = totalDebits;
            this.totalCredits = totalCredits;
            this.currentBalance = currentBalance;
            this.transactionCount = transactionCount;
        }

        // Getters
        public Long getSupplierId() { return supplierId; }
        public String getSupplierName() { return supplierName; }
        public BigDecimal getTotalPurchases() { return totalPurchases; }
        public BigDecimal getTotalPayments() { return totalPayments; }
        public BigDecimal getTotalDebits() { return totalDebits; }
        public BigDecimal getTotalCredits() { return totalCredits; }
        public BigDecimal getCurrentBalance() { return currentBalance; }
        public Integer getTransactionCount() { return transactionCount; }
    }
}
