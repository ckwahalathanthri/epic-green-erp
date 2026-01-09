package lk.epicgreen.erp.customer.repository;

import lk.epicgreen.erp.customer.entity.CustomerLedger;
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
 * Repository interface for CustomerLedger entity
 * Based on ACTUAL database schema: customer_ledger table
 * 
 * Fields: customer_id (BIGINT), transaction_date,
 *         transaction_type (ENUM: SALE, PAYMENT, RETURN, CREDIT_NOTE, DEBIT_NOTE, ADJUSTMENT),
 *         reference_type, reference_id (BIGINT), reference_number,
 *         description, debit_amount, credit_amount, balance
 * 
 * NOTE: This is an IMMUTABLE ledger - no updates or deletes after creation
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface CustomerLedgerRepository extends JpaRepository<CustomerLedger, Long>, JpaSpecificationExecutor<CustomerLedger> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find all ledger entries for a customer
     */
    List<CustomerLedger> findByCustomerId(Long customerId);
    
    /**
     * Find all ledger entries for a customer with pagination
     */
    Page<CustomerLedger> findByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Find ledger entries by customer ordered by transaction date
     */
    List<CustomerLedger> findByCustomerIdOrderByTransactionDateAscCreatedAtAsc(Long customerId);
    
    /**
     * Find ledger entries by transaction type
     */
    Page<CustomerLedger> findByTransactionType(String transactionType, Pageable pageable);
    
    /**
     * Find ledger entries by customer and transaction type
     */
    List<CustomerLedger> findByCustomerIdAndTransactionType(Long customerId, String transactionType);
    
    /**
     * Find ledger entries by transaction date
     */
    List<CustomerLedger> findByTransactionDate(LocalDate transactionDate);
    
    /**
     * Find ledger entries by transaction date range
     */
    List<CustomerLedger> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find ledger entries by customer and date range
     */
    List<CustomerLedger> findByCustomerIdAndTransactionDateBetween(
            Long customerId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Find ledger entries by reference
     */
    List<CustomerLedger> findByReferenceTypeAndReferenceId(String referenceType, Long referenceId);
    
    /**
     * Find ledger entries by reference number
     */
    List<CustomerLedger> findByReferenceNumber(String referenceNumber);
    
    /**
     * Find latest ledger entry for a customer
     */
    Optional<CustomerLedger> findFirstByCustomerIdOrderByTransactionDateDescCreatedAtDesc(Long customerId);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search ledger entries by multiple criteria
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE " +
           "(:customerId IS NULL OR cl.customerId = :customerId) AND " +
           "(:transactionType IS NULL OR cl.transactionType = :transactionType) AND " +
           "(:startDate IS NULL OR cl.transactionDate >= :startDate) AND " +
           "(:endDate IS NULL OR cl.transactionDate <= :endDate) AND " +
           "(:referenceNumber IS NULL OR cl.referenceNumber = :referenceNumber)")
    Page<CustomerLedger> searchLedger(
            @Param("customerId") Long customerId,
            @Param("transactionType") String transactionType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("referenceNumber") String referenceNumber,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count ledger entries for a customer
     */
    long countByCustomerId(Long customerId);
    
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
     * Find sale transactions
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.transactionType = 'SALE' " +
           "ORDER BY cl.transactionDate DESC, cl.createdAt DESC")
    List<CustomerLedger> findSaleTransactions();
    
    /**
     * Find payment transactions
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.transactionType = 'PAYMENT' " +
           "ORDER BY cl.transactionDate DESC, cl.createdAt DESC")
    List<CustomerLedger> findPaymentTransactions();
    
    /**
     * Find return transactions
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.transactionType = 'RETURN' " +
           "ORDER BY cl.transactionDate DESC, cl.createdAt DESC")
    List<CustomerLedger> findReturnTransactions();
    
    /**
     * Find credit note transactions
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.transactionType = 'CREDIT_NOTE' " +
           "ORDER BY cl.transactionDate DESC, cl.createdAt DESC")
    List<CustomerLedger> findCreditNoteTransactions();
    
    /**
     * Find debit note transactions
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.transactionType = 'DEBIT_NOTE' " +
           "ORDER BY cl.transactionDate DESC, cl.createdAt DESC")
    List<CustomerLedger> findDebitNoteTransactions();
    
    /**
     * Get current balance for a customer (from latest ledger entry)
     */
    @Query("SELECT cl.balance FROM CustomerLedger cl WHERE cl.customerId = :customerId " +
           "ORDER BY cl.transactionDate DESC, cl.createdAt DESC LIMIT 1")
    Optional<BigDecimal> getCurrentBalance(@Param("customerId") Long customerId);
    
    /**
     * Get total debit amount for a customer
     */
    @Query("SELECT SUM(cl.debitAmount) FROM CustomerLedger cl WHERE cl.customerId = :customerId")
    BigDecimal getTotalDebitByCustomer(@Param("customerId") Long customerId);
    
    /**
     * Get total credit amount for a customer
     */
    @Query("SELECT SUM(cl.creditAmount) FROM CustomerLedger cl WHERE cl.customerId = :customerId")
    BigDecimal getTotalCreditByCustomer(@Param("customerId") Long customerId);
    
    /**
     * Get total sales for a customer
     */
    @Query("SELECT SUM(cl.debitAmount) FROM CustomerLedger cl " +
           "WHERE cl.customerId = :customerId AND cl.transactionType = 'SALE'")
    BigDecimal getTotalSalesByCustomer(@Param("customerId") Long customerId);
    
    /**
     * Get total payments from a customer
     */
    @Query("SELECT SUM(cl.creditAmount) FROM CustomerLedger cl " +
           "WHERE cl.customerId = :customerId AND cl.transactionType = 'PAYMENT'")
    BigDecimal getTotalPaymentsByCustomer(@Param("customerId") Long customerId);
    
    /**
     * Get ledger summary by customer and date range
     */
    @Query("SELECT " +
           "SUM(cl.debitAmount) as totalDebit, " +
           "SUM(cl.creditAmount) as totalCredit, " +
           "COUNT(cl) as transactionCount " +
           "FROM CustomerLedger cl WHERE cl.customerId = :customerId " +
           "AND cl.transactionDate BETWEEN :startDate AND :endDate")
    Object getLedgerSummary(
            @Param("customerId") Long customerId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Get transaction statistics by type
     */
    @Query("SELECT cl.transactionType, COUNT(cl) as transactionCount, " +
           "SUM(cl.debitAmount) as totalDebit, SUM(cl.creditAmount) as totalCredit " +
           "FROM CustomerLedger cl GROUP BY cl.transactionType ORDER BY transactionCount DESC")
    List<Object[]> getTransactionStatisticsByType();
    
    /**
     * Get daily transaction summary
     */
    @Query("SELECT cl.transactionDate, cl.transactionType, COUNT(cl) as transactionCount, " +
           "SUM(cl.debitAmount) as totalDebit, SUM(cl.creditAmount) as totalCredit " +
           "FROM CustomerLedger cl WHERE cl.transactionDate BETWEEN :startDate AND :endDate " +
           "GROUP BY cl.transactionDate, cl.transactionType ORDER BY cl.transactionDate DESC")
    List<Object[]> getDailyTransactionSummary(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find ledger entries for today
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.transactionDate = CURRENT_DATE " +
           "ORDER BY cl.createdAt DESC")
    List<CustomerLedger> findTodayTransactions();
    
    /**
     * Find recent transactions for a customer
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.customerId = :customerId " +
           "ORDER BY cl.transactionDate DESC, cl.createdAt DESC")
    List<CustomerLedger> findRecentTransactionsByCustomer(@Param("customerId") Long customerId, Pageable pageable);
    
    /**
     * Get customer account statement
     */
    @Query("SELECT cl FROM CustomerLedger cl WHERE cl.customerId = :customerId " +
           "AND cl.transactionDate BETWEEN :startDate AND :endDate " +
           "ORDER BY cl.transactionDate ASC, cl.createdAt ASC")
    List<CustomerLedger> getAccountStatement(
            @Param("customerId") Long customerId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find customers with transactions in date range
     */
    @Query("SELECT DISTINCT cl.customerId FROM CustomerLedger cl " +
           "WHERE cl.transactionDate BETWEEN :startDate AND :endDate")
    List<Long> findCustomersWithTransactions(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    Optional<BigDecimal> getCustomerBalance(Long customerId);

    Optional<BigDecimal> getCustomerBalanceAsOfDate(Long customerId, LocalDate asOfDate);

    Optional<BigDecimal> getTotalByCustomerAndType(Long customerId, String string);

    Optional<BigDecimal> getTotalDebitsByCustomer(Long id);

       Optional<BigDecimal> getTotalCreditsByCustomer(Long id);

       Optional<BigDecimal> getTotalByCustomerAndTypeAndDateRange(Long customerId, String string, LocalDate startDate,
                     LocalDate endDate);

       Optional<BigDecimal> getTotalDebitsByCustomerAndDateRange(Long customerId, LocalDate startDate, LocalDate endDate);

       Optional<BigDecimal> getTotalCreditsByCustomerAndDateRange(Long customerId, LocalDate startDate, LocalDate endDate);

       Integer countByCustomerIdAndTransactionDateBetween(Long customerId, LocalDate startDate, LocalDate endDate);
}
