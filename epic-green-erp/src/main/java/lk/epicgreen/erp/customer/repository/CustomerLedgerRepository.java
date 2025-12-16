package lk.epicgreen.erp.customer.repository;

import lk.epicgreen.erp.customer.entity.CustomerLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * CustomerLedger Repository
 * Repository for customer ledger data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface CustomerLedgerRepository extends JpaRepository<CustomerLedger, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find ledger entry by transaction reference
     */
    Optional<CustomerLedger> findByTransactionReference(String transactionReference);
    
    /**
     * Check if ledger entry exists by transaction reference
     */
    boolean existsByTransactionReference(String transactionReference);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find ledger entries by customer ID
     */
    List<CustomerLedger> findByCustomerId(Long customerId);
    
    /**
     * Find ledger entries by customer ID with pagination
     */
    Page<CustomerLedger> findByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Find ledger entries by transaction type
     */
    List<CustomerLedger> findByTransactionType(String transactionType);
    
    /**
     * Find ledger entries by transaction type with pagination
     */
    Page<CustomerLedger> findByTransactionType(String transactionType, Pageable pageable);
    
    /**
     * Find ledger entries by entry type
     */
    List<CustomerLedger> findByEntryType(String entryType);
    
    /**
     * Find ledger entries by reference type
     */
    List<CustomerLedger> findByReferenceType(String referenceType);
    
    /**
     * Find ledger entries by reference ID
     */
    List<CustomerLedger> findByReferenceId(Long referenceId);
    
    /**
     * Find ledger entries by invoice ID
     */
    List<CustomerLedger> findByInvoiceId(Long invoiceId);
    
    /**
     * Find ledger entries by payment ID
     */
    List<CustomerLedger> findByPaymentId(Long paymentId);
    
    /**
     * Find ledger entries by sales order ID
     */
    List<CustomerLedger> findBySalesOrderId(Long salesOrderId);
    
    /**
     * Find ledger entries by created by user
     */
    List<CustomerLedger> findByCreatedByUserId(Long userId);
    
    /**
     * Find ledger entries by is reconciled
     */
    List<CustomerLedger> findByIsReconciled(Boolean isReconciled);
    
    /**
     * Find ledger entries by is reversed
     */
    List<CustomerLedger> findByIsReversed(Boolean isReversed);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find ledger entries by transaction date between dates
     */
    List<CustomerLedger> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find ledger entries by transaction date between dates with pagination
     */
    Page<CustomerLedger> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find ledger entries by due date between dates
     */
    List<CustomerLedger> findByDueDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find ledger entries by created at between dates
     */
    List<CustomerLedger> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find ledger entries by customer ID and transaction type
     */
    List<CustomerLedger> findByCustomerIdAndTransactionType(Long customerId, String transactionType);
    
    /**
     * Find ledger entries by customer ID and transaction type with pagination
     */
    Page<CustomerLedger> findByCustomerIdAndTransactionType(Long customerId, String transactionType, Pageable pageable);
    
    /**
     * Find ledger entries by customer ID and entry type
     */
    List<CustomerLedger> findByCustomerIdAndEntryType(Long customerId, String entryType);
    
    /**
     * Find ledger entries by customer ID and is reconciled
     */
    List<CustomerLedger> findByCustomerIdAndIsReconciled(Long customerId, Boolean isReconciled);
    
    /**
     * Find ledger entries by reference type and reference ID
     */
    List<CustomerLedger> findByReferenceTypeAndReferenceId(String referenceType, Long referenceId);
    
    /**
     * Find ledger entries by transaction type and entry type
     */
    List<CustomerLedger> findByTransactionTypeAndEntryType(String transactionType, String entryType);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search ledger entries
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE " +
           "LOWER(cl.transactionReference) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(cl.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(cl.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<CustomerLedger> searchLedgerEntries(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find debit entries
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.entryType = 'DEBIT' " +
           "ORDER BY cl.transactionDate DESC")
    List<CustomerLedger> findDebitEntries();
    
    /**
     * Find credit entries
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.entryType = 'CREDIT' " +
           "ORDER BY cl.transactionDate DESC")
    List<CustomerLedger> findCreditEntries();
    
    /**
     * Find customer debit entries
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.customerId = :customerId " +
           "AND cl.entryType = 'DEBIT' " +
           "ORDER BY cl.transactionDate DESC")
    List<CustomerLedger> findCustomerDebitEntries(@Param("customerId") Long customerId);
    
    /**
     * Find customer credit entries
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.customerId = :customerId " +
           "AND cl.entryType = 'CREDIT' " +
           "ORDER BY cl.transactionDate DESC")
    List<CustomerLedger> findCustomerCreditEntries(@Param("customerId") Long customerId);
    
    /**
     * Find unreconciled entries
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.isReconciled = false " +
           "ORDER BY cl.transactionDate ASC")
    List<CustomerLedger> findUnreconciledEntries();
    
    /**
     * Find customer unreconciled entries
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.customerId = :customerId " +
           "AND cl.isReconciled = false " +
           "ORDER BY cl.transactionDate ASC")
    List<CustomerLedger> findCustomerUnreconciledEntries(@Param("customerId") Long customerId);
    
    /**
     * Find reversed entries
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.isReversed = true " +
           "ORDER BY cl.transactionDate DESC")
    List<CustomerLedger> findReversedEntries();
    
    /**
     * Find invoice entries
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.transactionType = 'INVOICE' " +
           "ORDER BY cl.transactionDate DESC")
    List<CustomerLedger> findInvoiceEntries();
    
    /**
     * Find payment entries
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.transactionType = 'PAYMENT' " +
           "ORDER BY cl.transactionDate DESC")
    List<CustomerLedger> findPaymentEntries();
    
    /**
     * Find credit note entries
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.transactionType = 'CREDIT_NOTE' " +
           "ORDER BY cl.transactionDate DESC")
    List<CustomerLedger> findCreditNoteEntries();
    
    /**
     * Find debit note entries
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.transactionType = 'DEBIT_NOTE' " +
           "ORDER BY cl.transactionDate DESC")
    List<CustomerLedger> findDebitNoteEntries();
    
    /**
     * Find overdue entries
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.dueDate < :currentDate " +
           "AND cl.isReconciled = false " +
           "AND cl.entryType = 'DEBIT' " +
           "ORDER BY cl.dueDate ASC")
    List<CustomerLedger> findOverdueEntries(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Find customer overdue entries
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.customerId = :customerId " +
           "AND cl.dueDate < :currentDate " +
           "AND cl.isReconciled = false " +
           "AND cl.entryType = 'DEBIT' " +
           "ORDER BY cl.dueDate ASC")
    List<CustomerLedger> findCustomerOverdueEntries(@Param("customerId") Long customerId,
                                                    @Param("currentDate") LocalDate currentDate);
    
    /**
     * Find recent ledger entries
     */
    @Query("SELECT cl FROM CustomerLedger cl ORDER BY cl.transactionDate DESC, cl.createdAt DESC")
    List<CustomerLedger> findRecentLedgerEntries(Pageable pageable);
    
    /**
     * Find customer recent ledger entries
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.customerId = :customerId " +
           "ORDER BY cl.transactionDate DESC, cl.createdAt DESC")
    List<CustomerLedger> findCustomerRecentLedgerEntries(@Param("customerId") Long customerId, Pageable pageable);
    
    /**
     * Calculate customer balance
     */
    @Query("SELECT " +
           "(SELECT COALESCE(SUM(cl.debitAmount), 0) FROM CustomerLedger cl " +
           "WHERE cl.customerId = :customerId AND cl.isReversed = false) - " +
           "(SELECT COALESCE(SUM(cl.creditAmount), 0) FROM CustomerLedger cl " +
           "WHERE cl.customerId = :customerId AND cl.isReversed = false) " +
           "FROM CustomerLedger cl WHERE cl.customerId = :customerId")
    Double calculateCustomerBalance(@Param("customerId") Long customerId);
    
    /**
     * Get customer ledger statement
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.customerId = :customerId " +
           "AND cl.transactionDate BETWEEN :startDate AND :endDate " +
           "ORDER BY cl.transactionDate ASC, cl.createdAt ASC")
    List<CustomerLedger> getCustomerLedgerStatement(@Param("customerId") Long customerId,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);
    
    /**
     * Get opening balance for customer
     */
    @Query("SELECT " +
           "(SELECT COALESCE(SUM(cl.debitAmount), 0) FROM CustomerLedger cl " +
           "WHERE cl.customerId = :customerId AND cl.transactionDate < :startDate AND cl.isReversed = false) - " +
           "(SELECT COALESCE(SUM(cl.creditAmount), 0) FROM CustomerLedger cl " +
           "WHERE cl.customerId = :customerId AND cl.transactionDate < :startDate AND cl.isReversed = false) " +
           "FROM CustomerLedger cl WHERE cl.customerId = :customerId")
    Double getOpeningBalance(@Param("customerId") Long customerId,
                            @Param("startDate") LocalDate startDate);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count ledger entries by customer
     */
    @Query("SELECT COUNT(cl) FROM CustomerLedger cl WHERE cl.customerId = :customerId")
    Long countByCustomerId(@Param("customerId") Long customerId);
    
    /**
     * Count ledger entries by transaction type
     */
    @Query("SELECT COUNT(cl) FROM CustomerLedger cl WHERE cl.transactionType = :transactionType")
    Long countByTransactionType(@Param("transactionType") String transactionType);
    
    /**
     * Count unreconciled entries
     */
    @Query("SELECT COUNT(cl) FROM CustomerLedger cl WHERE cl.isReconciled = false")
    Long countUnreconciledEntries();
    
    /**
     * Count customer unreconciled entries
     */
    @Query("SELECT COUNT(cl) FROM CustomerLedger cl WHERE cl.customerId = :customerId " +
           "AND cl.isReconciled = false")
    Long countCustomerUnreconciledEntries(@Param("customerId") Long customerId);
    
    /**
     * Get transaction type distribution
     */
    @Query("SELECT cl.transactionType, COUNT(cl) as entryCount FROM CustomerLedger cl " +
           "GROUP BY cl.transactionType ORDER BY entryCount DESC")
    List<Object[]> getTransactionTypeDistribution();
    
    /**
     * Get entry type distribution
     */
    @Query("SELECT cl.entryType, COUNT(cl) as entryCount FROM CustomerLedger cl " +
           "GROUP BY cl.entryType ORDER BY entryCount DESC")
    List<Object[]> getEntryTypeDistribution();
    
    /**
     * Get monthly ledger activity
     */
    @Query("SELECT YEAR(cl.transactionDate) as year, MONTH(cl.transactionDate) as month, " +
           "COUNT(cl) as entryCount, SUM(cl.debitAmount) as totalDebit, " +
           "SUM(cl.creditAmount) as totalCredit FROM CustomerLedger cl " +
           "WHERE cl.transactionDate BETWEEN :startDate AND :endDate " +
           "GROUP BY YEAR(cl.transactionDate), MONTH(cl.transactionDate) " +
           "ORDER BY year, month")
    List<Object[]> getMonthlyLedgerActivity(@Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);
    
    /**
     * Get total debit amount
     */
    @Query("SELECT SUM(cl.debitAmount) FROM CustomerLedger cl WHERE cl.isReversed = false")
    Double getTotalDebitAmount();
    
    /**
     * Get total credit amount
     */
    @Query("SELECT SUM(cl.creditAmount) FROM CustomerLedger cl WHERE cl.isReversed = false")
    Double getTotalCreditAmount();
    
    /**
     * Get customer total debit amount
     */
    @Query("SELECT SUM(cl.debitAmount) FROM CustomerLedger cl " +
           "WHERE cl.customerId = :customerId AND cl.isReversed = false")
    Double getCustomerTotalDebitAmount(@Param("customerId") Long customerId);
    
    /**
     * Get customer total credit amount
     */
    @Query("SELECT SUM(cl.creditAmount) FROM CustomerLedger cl " +
           "WHERE cl.customerId = :customerId AND cl.isReversed = false")
    Double getCustomerTotalCreditAmount(@Param("customerId") Long customerId);
    
    /**
     * Get total outstanding amount
     */
    @Query("SELECT " +
           "(SELECT COALESCE(SUM(cl.debitAmount), 0) FROM CustomerLedger cl " +
           "WHERE cl.isReconciled = false AND cl.isReversed = false) - " +
           "(SELECT COALESCE(SUM(cl.creditAmount), 0) FROM CustomerLedger cl " +
           "WHERE cl.isReconciled = false AND cl.isReversed = false) " +
           "FROM CustomerLedger cl WHERE cl.isReconciled = false")
    Double getTotalOutstandingAmount();
    
    /**
     * Get customer ledger summary
     */
    @Query("SELECT cl.customerId, cl.customerName, " +
           "SUM(cl.debitAmount) as totalDebit, SUM(cl.creditAmount) as totalCredit, " +
           "(SUM(cl.debitAmount) - SUM(cl.creditAmount)) as balance " +
           "FROM CustomerLedger cl WHERE cl.isReversed = false " +
           "GROUP BY cl.customerId, cl.customerName " +
           "ORDER BY balance DESC")
    List<Object[]> getCustomerLedgerSummary();
    
    /**
     * Find ledger entries by tags
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.tags LIKE CONCAT('%', :tag, '%')")
    List<CustomerLedger> findByTag(@Param("tag") String tag);
}
