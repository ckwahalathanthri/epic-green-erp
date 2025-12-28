package lk.epicgreen.erp.customer.service;

import lk.epicgreen.erp.customer.dto.request.CustomerLedgerRequest;
import lk.epicgreen.erp.customer.dto.response.CustomerLedgerResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for CustomerLedger entity business logic
 * 
 * IMPORTANT: CustomerLedger is IMMUTABLE
 * - CREATE: Allowed (add new ledger entries)
 * - READ: Allowed (query ledger entries)
 * - UPDATE: NOT ALLOWED (financial audit requirement)
 * - DELETE: NOT ALLOWED (financial audit requirement)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface CustomerLedgerService {

    /**
     * Create new ledger entry (IMMUTABLE - cannot be updated or deleted)
     */
    CustomerLedgerResponse createLedgerEntry(CustomerLedgerRequest request);

    CustomerLedgerResponse getLedgerEntryById(Long id);
    PageResponse<CustomerLedgerResponse> getLedgerEntriesByCustomer(Long customerId, Pageable pageable);
    List<CustomerLedgerResponse> getLedgerEntriesByCustomerAndDateRange(Long customerId, LocalDate startDate, LocalDate endDate);
    PageResponse<CustomerLedgerResponse> getLedgerEntriesByType(String transactionType, Pageable pageable);
    List<CustomerLedgerResponse> getLedgerEntriesByReference(String referenceType, Long referenceId);
    BigDecimal getCustomerBalance(Long customerId);
    BigDecimal getCustomerBalanceAsOfDate(Long customerId, LocalDate asOfDate);
    PageResponse<CustomerLedgerResponse> getAllLedgerEntries(Pageable pageable);
    List<CustomerLedgerResponse> getSaleEntriesByCustomer(Long customerId);
    List<CustomerLedgerResponse> getPaymentEntriesByCustomer(Long customerId);
    CustomerLedgerSummary getLedgerSummary(Long customerId);
    CustomerLedgerSummary getLedgerSummaryForDateRange(Long customerId, LocalDate startDate, LocalDate endDate);

    /**
     * DTO for ledger summary
     */
    class CustomerLedgerSummary {
        private Long customerId;
        private String customerName;
        private BigDecimal totalSales;
        private BigDecimal totalPayments;
        private BigDecimal totalDebits;
        private BigDecimal totalCredits;
        private BigDecimal currentBalance;
        private Integer transactionCount;

        public CustomerLedgerSummary(Long customerId, String customerName, 
                                    BigDecimal totalSales, BigDecimal totalPayments,
                                    BigDecimal totalDebits, BigDecimal totalCredits,
                                    BigDecimal currentBalance, Integer transactionCount) {
            this.customerId = customerId;
            this.customerName = customerName;
            this.totalSales = totalSales;
            this.totalPayments = totalPayments;
            this.totalDebits = totalDebits;
            this.totalCredits = totalCredits;
            this.currentBalance = currentBalance;
            this.transactionCount = transactionCount;
        }

        public Long getCustomerId() { return customerId; }
        public String getCustomerName() { return customerName; }
        public BigDecimal getTotalSales() { return totalSales; }
        public BigDecimal getTotalPayments() { return totalPayments; }
        public BigDecimal getTotalDebits() { return totalDebits; }
        public BigDecimal getTotalCredits() { return totalCredits; }
        public BigDecimal getCurrentBalance() { return currentBalance; }
        public Integer getTransactionCount() { return transactionCount; }
    }
}
