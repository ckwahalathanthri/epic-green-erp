package lk.epicgreen.erp.supplier.repository;

import lk.epicgreen.erp.supplier.entity.SupplierLedger;
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
 * SupplierLedger Repository
 * Repository for supplier ledger data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface SupplierLedgerRepository extends JpaRepository<SupplierLedger, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find ledger entry by reference number
     */
    Optional<SupplierLedger> findByReferenceNumber(String referenceNumber);
    
    /**
     * Check if ledger entry exists by reference number
     */
    boolean existsByReferenceNumber(String referenceNumber);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find ledger entries by supplier ID
     */
    List<SupplierLedger> findBySupplierId(Long supplierId);
    
    /**
     * Find ledger entries by supplier ID with pagination
     */
    Page<SupplierLedger> findBySupplierId(Long supplierId, Pageable pageable);
    
    /**
     * Find ledger entries by transaction type
     */
    List<SupplierLedger> findByTransactionType(String transactionType);
    
    /**
     * Find ledger entries by payment method
     */
    List<SupplierLedger> findByPaymentMethod(String paymentMethod);
    
    /**
     * Find ledger entries by reference type
     */
    List<SupplierLedger> findByReferenceType(String referenceType);
    
    /**
     * Find ledger entries by reference ID
     */
    List<SupplierLedger> findByReferenceId(Long referenceId);
    
    /**
     * Find ledger entries by recorded by user ID
     */
    List<SupplierLedger> findByRecordedByUserId(Long recordedByUserId);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find ledger entries by transaction date between dates
     */
    List<SupplierLedger> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find ledger entries by transaction date between dates with pagination
     */
    Page<SupplierLedger> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find ledger entries by created at between dates
     */
    List<SupplierLedger> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find ledger entries by supplier and transaction type
     */
    List<SupplierLedger> findBySupplierIdAndTransactionType(Long supplierId, String transactionType);
    
    /**
     * Find ledger entries by supplier and payment method
     */
    List<SupplierLedger> findBySupplierIdAndPaymentMethod(Long supplierId, String paymentMethod);
    
    /**
     * Find ledger entries by supplier and reference type
     */
    List<SupplierLedger> findBySupplierIdAndReferenceType(Long supplierId, String referenceType);
    
    /**
     * Find ledger entries by reference type and reference ID
     */
    List<SupplierLedger> findByReferenceTypeAndReferenceId(String referenceType, Long referenceId);
    
    /**
     * Find ledger entries by supplier and date range
     */
    List<SupplierLedger> findBySupplierIdAndTransactionDateBetween(
        Long supplierId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Find ledger entries by supplier and date range with pagination
     */
    Page<SupplierLedger> findBySupplierIdAndTransactionDateBetween(
        Long supplierId, LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find ledger entries by transaction type and date range
     */
    List<SupplierLedger> findByTransactionTypeAndTransactionDateBetween(
        String transactionType, LocalDate startDate, LocalDate endDate);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search ledger entries
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE " +
           "LOWER(sl.referenceNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(sl.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(sl.notes) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<SupplierLedger> searchLedgerEntries(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find debit entries by supplier
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.supplierId = :supplierId " +
           "AND sl.debitAmount > 0 ORDER BY sl.transactionDate DESC, sl.createdAt DESC")
    List<SupplierLedger> findDebitEntriesBySupplierId(@Param("supplierId") Long supplierId);
    
    /**
     * Find credit entries by supplier
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.supplierId = :supplierId " +
           "AND sl.creditAmount > 0 ORDER BY sl.transactionDate DESC, sl.createdAt DESC")
    List<SupplierLedger> findCreditEntriesBySupplierId(@Param("supplierId") Long supplierId);
    
    /**
     * Find purchase entries
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.transactionType = 'PURCHASE' " +
           "ORDER BY sl.transactionDate DESC")
    List<SupplierLedger> findPurchaseEntries();
    
    /**
     * Find payment entries
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.transactionType = 'PAYMENT' " +
           "ORDER BY sl.transactionDate DESC")
    List<SupplierLedger> findPaymentEntries();
    
    /**
     * Find return entries
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.transactionType = 'RETURN' " +
           "ORDER BY sl.transactionDate DESC")
    List<SupplierLedger> findReturnEntries();
    
    /**
     * Find adjustment entries
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.transactionType = 'ADJUSTMENT' " +
           "ORDER BY sl.transactionDate DESC")
    List<SupplierLedger> findAdjustmentEntries();
    
    /**
     * Find entries by payment method (cash)
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.paymentMethod = 'CASH' " +
           "AND sl.transactionType = 'PAYMENT' ORDER BY sl.transactionDate DESC")
    List<SupplierLedger> findCashPayments();
    
    /**
     * Find entries by payment method (cheque)
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.paymentMethod = 'CHEQUE' " +
           "AND sl.transactionType = 'PAYMENT' ORDER BY sl.transactionDate DESC")
    List<SupplierLedger> findChequePayments();
    
    /**
     * Find entries by payment method (bank transfer)
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.paymentMethod = 'BANK_TRANSFER' " +
           "AND sl.transactionType = 'PAYMENT' ORDER BY sl.transactionDate DESC")
    List<SupplierLedger> findBankTransferPayments();
    
    /**
     * Find entries by payment method (credit)
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.paymentMethod = 'CREDIT' " +
           "AND sl.transactionType = 'PURCHASE' ORDER BY sl.transactionDate DESC")
    List<SupplierLedger> findCreditPurchases();
    
    /**
     * Find recent entries by supplier
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.supplierId = :supplierId " +
           "ORDER BY sl.transactionDate DESC, sl.createdAt DESC")
    List<SupplierLedger> findRecentEntriesBySupplierId(@Param("supplierId") Long supplierId, Pageable pageable);
    
    /**
     * Find entries by amount range
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE " +
           "(sl.debitAmount BETWEEN :minAmount AND :maxAmount) OR " +
           "(sl.creditAmount BETWEEN :minAmount AND :maxAmount) " +
           "ORDER BY sl.transactionDate DESC")
    List<SupplierLedger> findByAmountRange(@Param("minAmount") Double minAmount,
                                           @Param("maxAmount") Double maxAmount);
    
    /**
     * Find large transactions
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE " +
           "sl.debitAmount > :threshold OR sl.creditAmount > :threshold " +
           "ORDER BY GREATEST(sl.debitAmount, sl.creditAmount) DESC")
    List<SupplierLedger> findLargeTransactions(@Param("threshold") Double threshold);
    
    /**
     * Get supplier statement (ledger entries for period)
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.supplierId = :supplierId " +
           "AND sl.transactionDate BETWEEN :startDate AND :endDate " +
           "ORDER BY sl.transactionDate ASC, sl.createdAt ASC")
    List<SupplierLedger> getSupplierStatement(@Param("supplierId") Long supplierId,
                                              @Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);
    
    /**
     * Find today's entries
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.transactionDate = :today " +
           "ORDER BY sl.createdAt DESC")
    List<SupplierLedger> findTodaysEntries(@Param("today") LocalDate today);
    
    /**
     * Find this month's entries
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE " +
           "YEAR(sl.transactionDate) = :year AND MONTH(sl.transactionDate) = :month " +
           "ORDER BY sl.transactionDate DESC")
    List<SupplierLedger> findThisMonthEntries(@Param("year") int year, @Param("month") int month);
    
    /**
     * Find entries by user
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.recordedByUserId = :userId " +
           "ORDER BY sl.transactionDate DESC, sl.createdAt DESC")
    List<SupplierLedger> findEntriesByUser(@Param("userId") Long userId);
    
    // ===================================================================
    // AGGREGATE QUERIES
    // ===================================================================
    
    /**
     * Get total debit amount by supplier
     */
    @Query("SELECT SUM(sl.debitAmount) FROM SupplierLedger sl WHERE sl.supplierId = :supplierId")
    Double getTotalDebitAmountBySupplierId(@Param("supplierId") Long supplierId);
    
    /**
     * Get total credit amount by supplier
     */
    @Query("SELECT SUM(sl.creditAmount) FROM SupplierLedger sl WHERE sl.supplierId = :supplierId")
    Double getTotalCreditAmountBySupplierId(@Param("supplierId") Long supplierId);
    
    /**
     * Get balance by supplier
     */
    @Query("SELECT (COALESCE(SUM(sl.debitAmount), 0) - COALESCE(SUM(sl.creditAmount), 0)) " +
           "FROM SupplierLedger sl WHERE sl.supplierId = :supplierId")
    Double getBalanceBySupplierId(@Param("supplierId") Long supplierId);
    
    /**
     * Get total debit amount by supplier for date range
     */
    @Query("SELECT SUM(sl.debitAmount) FROM SupplierLedger sl " +
           "WHERE sl.supplierId = :supplierId " +
           "AND sl.transactionDate BETWEEN :startDate AND :endDate")
    Double getTotalDebitAmountBySupplierIdAndDateRange(
        @Param("supplierId") Long supplierId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);
    
    /**
     * Get total credit amount by supplier for date range
     */
    @Query("SELECT SUM(sl.creditAmount) FROM SupplierLedger sl " +
           "WHERE sl.supplierId = :supplierId " +
           "AND sl.transactionDate BETWEEN :startDate AND :endDate")
    Double getTotalCreditAmountBySupplierIdAndDateRange(
        @Param("supplierId") Long supplierId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);
    
    /**
     * Get total purchases by supplier
     */
    @Query("SELECT SUM(sl.debitAmount) FROM SupplierLedger sl " +
           "WHERE sl.supplierId = :supplierId AND sl.transactionType = 'PURCHASE'")
    Double getTotalPurchasesBySupplierId(@Param("supplierId") Long supplierId);
    
    /**
     * Get total payments by supplier
     */
    @Query("SELECT SUM(sl.creditAmount) FROM SupplierLedger sl " +
           "WHERE sl.supplierId = :supplierId AND sl.transactionType = 'PAYMENT'")
    Double getTotalPaymentsBySupplierId(@Param("supplierId") Long supplierId);
    
    /**
     * Get total returns by supplier
     */
    @Query("SELECT SUM(sl.creditAmount) FROM SupplierLedger sl " +
           "WHERE sl.supplierId = :supplierId AND sl.transactionType = 'RETURN'")
    Double getTotalReturnsBySupplierId(@Param("supplierId") Long supplierId);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count entries by supplier
     */
    @Query("SELECT COUNT(sl) FROM SupplierLedger sl WHERE sl.supplierId = :supplierId")
    Long countBySupplierId(@Param("supplierId") Long supplierId);
    
    /**
     * Count entries by transaction type
     */
    @Query("SELECT COUNT(sl) FROM SupplierLedger sl WHERE sl.transactionType = :transactionType")
    Long countByTransactionType(@Param("transactionType") String transactionType);
    
    /**
     * Count entries by payment method
     */
    @Query("SELECT COUNT(sl) FROM SupplierLedger sl WHERE sl.paymentMethod = :paymentMethod")
    Long countByPaymentMethod(@Param("paymentMethod") String paymentMethod);
    
    /**
     * Get transaction type distribution
     */
    @Query("SELECT sl.transactionType, COUNT(sl) as entryCount, " +
           "SUM(sl.debitAmount) as totalDebit, SUM(sl.creditAmount) as totalCredit " +
           "FROM SupplierLedger sl GROUP BY sl.transactionType ORDER BY entryCount DESC")
    List<Object[]> getTransactionTypeDistribution();
    
    /**
     * Get payment method distribution
     */
    @Query("SELECT sl.paymentMethod, COUNT(sl) as entryCount, SUM(sl.creditAmount) as totalAmount " +
           "FROM SupplierLedger sl WHERE sl.paymentMethod IS NOT NULL " +
           "GROUP BY sl.paymentMethod ORDER BY totalAmount DESC")
    List<Object[]> getPaymentMethodDistribution();
    
    /**
     * Get daily transaction summary
     */
    @Query("SELECT sl.transactionDate, COUNT(sl) as entryCount, " +
           "SUM(sl.debitAmount) as totalDebit, SUM(sl.creditAmount) as totalCredit " +
           "FROM SupplierLedger sl " +
           "WHERE sl.transactionDate BETWEEN :startDate AND :endDate " +
           "GROUP BY sl.transactionDate ORDER BY sl.transactionDate DESC")
    List<Object[]> getDailyTransactionSummary(@Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);
    
    /**
     * Get monthly transaction summary
     */
    @Query("SELECT YEAR(sl.transactionDate) as year, MONTH(sl.transactionDate) as month, " +
           "COUNT(sl) as entryCount, SUM(sl.debitAmount) as totalDebit, " +
           "SUM(sl.creditAmount) as totalCredit FROM SupplierLedger sl " +
           "GROUP BY YEAR(sl.transactionDate), MONTH(sl.transactionDate) " +
           "ORDER BY year DESC, month DESC")
    List<Object[]> getMonthlyTransactionSummary();
    
    /**
     * Get total debit amount
     */
    @Query("SELECT SUM(sl.debitAmount) FROM SupplierLedger sl")
    Double getTotalDebitAmount();
    
    /**
     * Get total credit amount
     */
    @Query("SELECT SUM(sl.creditAmount) FROM SupplierLedger sl")
    Double getTotalCreditAmount();
    
    /**
     * Get average transaction amount
     */
    @Query("SELECT AVG(GREATEST(sl.debitAmount, sl.creditAmount)) FROM SupplierLedger sl")
    Double getAverageTransactionAmount();
    
    /**
     * Get suppliers with highest debit
     */
    @Query("SELECT sl.supplierId, sl.supplierName, SUM(sl.debitAmount) as totalDebit " +
           "FROM SupplierLedger sl GROUP BY sl.supplierId, sl.supplierName " +
           "ORDER BY totalDebit DESC")
    List<Object[]> getSuppliersWithHighestDebit(Pageable pageable);
    
    /**
     * Get suppliers with highest credit
     */
    @Query("SELECT sl.supplierId, sl.supplierName, SUM(sl.creditAmount) as totalCredit " +
           "FROM SupplierLedger sl GROUP BY sl.supplierId, sl.supplierName " +
           "ORDER BY totalCredit DESC")
    List<Object[]> getSuppliersWithHighestCredit(Pageable pageable);
    
    /**
     * Get opening balance for supplier
     */
    @Query("SELECT sl.balanceAfter FROM SupplierLedger sl " +
           "WHERE sl.supplierId = :supplierId AND sl.transactionDate < :startDate " +
           "ORDER BY sl.transactionDate DESC, sl.createdAt DESC")
    List<Double> getOpeningBalance(@Param("supplierId") Long supplierId,
                                   @Param("startDate") LocalDate startDate,
                                   Pageable pageable);
}
