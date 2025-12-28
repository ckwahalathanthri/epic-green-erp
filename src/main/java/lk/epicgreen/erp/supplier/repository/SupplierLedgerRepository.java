package lk.epicgreen.erp.supplier.repository;

import lk.epicgreen.erp.supplier.entity.SupplierLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for SupplierLedger entity
 * Based on ACTUAL database schema: supplier_ledger table
 * 
 * Fields: supplier_id (BIGINT), transaction_date,
 *         transaction_type (ENUM: PURCHASE, PAYMENT, CREDIT_NOTE, DEBIT_NOTE, ADJUSTMENT),
 *         reference_type, reference_id (BIGINT), reference_number,
 *         description, debit_amount, credit_amount, balance
 * 
 * NOTE: This is an IMMUTABLE ledger - no updates or deletes after creation
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface SupplierLedgerRepository extends JpaRepository<SupplierLedger, Long>, JpaSpecificationExecutor<SupplierLedger> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find all ledger entries for a supplier
     */
    List<SupplierLedger> findBySupplierId(Long supplierId);
    
    /**
     * Find all ledger entries for a supplier with pagination
     */
    Page<SupplierLedger> findBySupplierId(Long supplierId, Pageable pageable);
    
    /**
     * Find ledger entries by supplier ordered by transaction date
     */
    List<SupplierLedger> findBySupplierIdOrderByTransactionDateAscCreatedAtAsc(Long supplierId);
    
    /**
     * Find ledger entries by transaction type
     */
    List<SupplierLedger> findByTransactionType(String transactionType);
    
    /**
     * Find ledger entries by supplier and transaction type
     */
    List<SupplierLedger> findBySupplierIdAndTransactionType(Long supplierId, String transactionType);
    
    /**
     * Find ledger entries by transaction date
     */
    List<SupplierLedger> findByTransactionDate(LocalDate transactionDate);
    
    /**
     * Find ledger entries by transaction date range
     */
    List<SupplierLedger> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find ledger entries by supplier and date range
     */
    List<SupplierLedger> findBySupplierIdAndTransactionDateBetween(
            Long supplierId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Find ledger entries by reference
     */
    List<SupplierLedger> findByReferenceTypeAndReferenceId(String referenceType, Long referenceId);
    
    /**
     * Find ledger entries by reference number
     */
    List<SupplierLedger> findByReferenceNumber(String referenceNumber);
    
    /**
     * Find latest ledger entry for a supplier
     */
    Optional<SupplierLedger> findFirstBySupplierIdOrderByTransactionDateDescCreatedAtDesc(Long supplierId);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search ledger entries by multiple criteria
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE " +
           "(:supplierId IS NULL OR sl.supplierId = :supplierId) AND " +
           "(:transactionType IS NULL OR sl.transactionType = :transactionType) AND " +
           "(:startDate IS NULL OR sl.transactionDate >= :startDate) AND " +
           "(:endDate IS NULL OR sl.transactionDate <= :endDate) AND " +
           "(:referenceNumber IS NULL OR sl.referenceNumber = :referenceNumber)")
    Page<SupplierLedger> searchLedger(
            @Param("supplierId") Long supplierId,
            @Param("transactionType") String transactionType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("referenceNumber") String referenceNumber,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count ledger entries for a supplier
     */
    long countBySupplierId(Long supplierId);
    
    /**
     * Count ledger entries by transaction type
     */
    long countByTransactionType(String transactionType);
    
    /**
     * Count ledger entries in date range
     */
    long countByTransactionDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find purchase transactions
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.transactionType = 'PURCHASE' " +
           "ORDER BY sl.transactionDate DESC, sl.createdAt DESC")
    List<SupplierLedger> findPurchaseTransactions();
    
    /**
     * Find payment transactions
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.transactionType = 'PAYMENT' " +
           "ORDER BY sl.transactionDate DESC, sl.createdAt DESC")
    List<SupplierLedger> findPaymentTransactions();
    
    /**
     * Find credit note transactions
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.transactionType = 'CREDIT_NOTE' " +
           "ORDER BY sl.transactionDate DESC, sl.createdAt DESC")
    List<SupplierLedger> findCreditNoteTransactions();
    
    /**
     * Find debit note transactions
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.transactionType = 'DEBIT_NOTE' " +
           "ORDER BY sl.transactionDate DESC, sl.createdAt DESC")
    List<SupplierLedger> findDebitNoteTransactions();
    
    /**
     * Find adjustment transactions
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.transactionType = 'ADJUSTMENT' " +
           "ORDER BY sl.transactionDate DESC, sl.createdAt DESC")
    List<SupplierLedger> findAdjustmentTransactions();
    
    /**
     * Get current balance for a supplier (from latest ledger entry)
     */
    @Query("SELECT sl.balance FROM SupplierLedger sl WHERE sl.supplierId = :supplierId " +
           "ORDER BY sl.transactionDate DESC, sl.createdAt DESC LIMIT 1")
    Optional<BigDecimal> getCurrentBalance(@Param("supplierId") Long supplierId);
    
    /**
     * Get total debit amount for a supplier
     */
    @Query("SELECT SUM(sl.debitAmount) FROM SupplierLedger sl WHERE sl.supplierId = :supplierId")
    BigDecimal getTotalDebitBySupplier(@Param("supplierId") Long supplierId);
    
    /**
     * Get total credit amount for a supplier
     */
    @Query("SELECT SUM(sl.creditAmount) FROM SupplierLedger sl WHERE sl.supplierId = :supplierId")
    BigDecimal getTotalCreditBySupplier(@Param("supplierId") Long supplierId);
    
    /**
     * Get total purchases from a supplier
     */
    @Query("SELECT SUM(sl.creditAmount) FROM SupplierLedger sl " +
           "WHERE sl.supplierId = :supplierId AND sl.transactionType = 'PURCHASE'")
    BigDecimal getTotalPurchasesBySupplier(@Param("supplierId") Long supplierId);
    
    /**
     * Get total payments to a supplier
     */
    @Query("SELECT SUM(sl.debitAmount) FROM SupplierLedger sl " +
           "WHERE sl.supplierId = :supplierId AND sl.transactionType = 'PAYMENT'")
    BigDecimal getTotalPaymentsBySupplier(@Param("supplierId") Long supplierId);
    
    /**
     * Get ledger summary by supplier and date range
     */
    @Query("SELECT " +
           "SUM(sl.debitAmount) as totalDebit, " +
           "SUM(sl.creditAmount) as totalCredit, " +
           "COUNT(sl) as transactionCount " +
           "FROM SupplierLedger sl WHERE sl.supplierId = :supplierId " +
           "AND sl.transactionDate BETWEEN :startDate AND :endDate")
    Object getLedgerSummary(
            @Param("supplierId") Long supplierId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Get transaction statistics by type
     */
    @Query("SELECT sl.transactionType, COUNT(sl) as transactionCount, " +
           "SUM(sl.debitAmount) as totalDebit, SUM(sl.creditAmount) as totalCredit " +
           "FROM SupplierLedger sl GROUP BY sl.transactionType ORDER BY transactionCount DESC")
    List<Object[]> getTransactionStatisticsByType();
    
    /**
     * Get daily transaction summary
     */
    @Query("SELECT sl.transactionDate, sl.transactionType, COUNT(sl) as transactionCount, " +
           "SUM(sl.debitAmount) as totalDebit, SUM(sl.creditAmount) as totalCredit " +
           "FROM SupplierLedger sl WHERE sl.transactionDate BETWEEN :startDate AND :endDate " +
           "GROUP BY sl.transactionDate, sl.transactionType ORDER BY sl.transactionDate DESC")
    List<Object[]> getDailyTransactionSummary(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find ledger entries for today
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.transactionDate = CURRENT_DATE " +
           "ORDER BY sl.createdAt DESC")
    List<SupplierLedger> findTodayTransactions();
    
    /**
     * Find recent transactions for a supplier
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.supplierId = :supplierId " +
           "ORDER BY sl.transactionDate DESC, sl.createdAt DESC")
    List<SupplierLedger> findRecentTransactionsBySupplier(@Param("supplierId") Long supplierId, Pageable pageable);
    
    /**
     * Get supplier account statement
     */
    @Query("SELECT sl FROM SupplierLedger sl WHERE sl.supplierId = :supplierId " +
           "AND sl.transactionDate BETWEEN :startDate AND :endDate " +
           "ORDER BY sl.transactionDate ASC, sl.createdAt ASC")
    List<SupplierLedger> getAccountStatement(
            @Param("supplierId") Long supplierId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find suppliers with transactions in date range
     */
    @Query("SELECT DISTINCT sl.supplierId FROM SupplierLedger sl " +
           "WHERE sl.transactionDate BETWEEN :startDate AND :endDate")
    List<Long> findSuppliersWithTransactions(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find suppliers with outstanding payables (credit balance)
     */
    @Query("SELECT DISTINCT sl.supplierId FROM SupplierLedger sl " +
           "WHERE sl.supplierId IN " +
           "(SELECT sl2.supplierId FROM SupplierLedger sl2 " +
           "WHERE sl2.id IN (SELECT MAX(sl3.id) FROM SupplierLedger sl3 GROUP BY sl3.supplierId) " +
           "AND sl2.balance > 0)")
    List<Long> findSuppliersWithOutstandingPayables();
    
    /**
     * Get purchase value by supplier in date range
     */
    @Query("SELECT sl.supplierId, SUM(sl.creditAmount) as totalPurchases " +
           "FROM SupplierLedger sl WHERE sl.transactionType = 'PURCHASE' " +
           "AND sl.transactionDate BETWEEN :startDate AND :endDate " +
           "GROUP BY sl.supplierId ORDER BY totalPurchases DESC")
    List<Object[]> getPurchaseValueBySupplier(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Get payment value by supplier in date range
     */
    @Query("SELECT sl.supplierId, SUM(sl.debitAmount) as totalPayments " +
           "FROM SupplierLedger sl WHERE sl.transactionType = 'PAYMENT' " +
           "AND sl.transactionDate BETWEEN :startDate AND :endDate " +
           "GROUP BY sl.supplierId ORDER BY totalPayments DESC")
    List<Object[]> getPaymentValueBySupplier(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
