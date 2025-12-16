package lk.epicgreen.erp.payment.repository;

import lk.epicgreen.erp.payment.entity.Payment;
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
 * Payment Repository
 * Repository for payment data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find payment by payment number
     */
    Optional<Payment> findByPaymentNumber(String paymentNumber);
    
    /**
     * Check if payment exists by payment number
     */
    boolean existsByPaymentNumber(String paymentNumber);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find payments by customer ID
     */
    List<Payment> findByCustomerId(Long customerId);
    
    /**
     * Find payments by customer ID with pagination
     */
    Page<Payment> findByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Find payments by payment type
     */
    List<Payment> findByPaymentType(String paymentType);
    
    /**
     * Find payments by payment type with pagination
     */
    Page<Payment> findByPaymentType(String paymentType, Pageable pageable);
    
    /**
     * Find payments by payment method
     */
    List<Payment> findByPaymentMethod(String paymentMethod);
    
    /**
     * Find payments by payment method with pagination
     */
    Page<Payment> findByPaymentMethod(String paymentMethod, Pageable pageable);
    
    /**
     * Find payments by status
     */
    List<Payment> findByStatus(String status);
    
    /**
     * Find payments by status with pagination
     */
    Page<Payment> findByStatus(String status, Pageable pageable);
    
    /**
     * Find payments by reference type
     */
    List<Payment> findByReferenceType(String referenceType);
    
    /**
     * Find payments by reference ID
     */
    List<Payment> findByReferenceId(Long referenceId);
    
    /**
     * Find payments by received by user
     */
    List<Payment> findByReceivedByUserId(Long userId);
    
    /**
     * Find payments by is allocated
     */
    List<Payment> findByIsAllocated(Boolean isAllocated);
    
    /**
     * Find payments by is reconciled
     */
    List<Payment> findByIsReconciled(Boolean isReconciled);
    
    /**
     * Find payments by bank account ID
     */
    List<Payment> findByBankAccountId(Long bankAccountId);
    
    /**
     * Find payments by cheque ID
     */
    List<Payment> findByChequeId(Long chequeId);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find payments by payment date between dates
     */
    List<Payment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find payments by payment date between dates with pagination
     */
    Page<Payment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find payments by received date between dates
     */
    List<Payment> findByReceivedDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find payments by cleared date between dates
     */
    List<Payment> findByClearedDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find payments by created at between dates
     */
    List<Payment> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find payments by customer ID and status
     */
    List<Payment> findByCustomerIdAndStatus(Long customerId, String status);
    
    /**
     * Find payments by customer ID and status with pagination
     */
    Page<Payment> findByCustomerIdAndStatus(Long customerId, String status, Pageable pageable);
    
    /**
     * Find payments by payment type and status
     */
    List<Payment> findByPaymentTypeAndStatus(String paymentType, String status);
    
    /**
     * Find payments by payment method and status
     */
    List<Payment> findByPaymentMethodAndStatus(String paymentMethod, String status);
    
    /**
     * Find payments by reference type and reference ID
     */
    List<Payment> findByReferenceTypeAndReferenceId(String referenceType, Long referenceId);
    
    /**
     * Find payments by is allocated and status
     */
    List<Payment> findByIsAllocatedAndStatus(Boolean isAllocated, String status);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search payments
     */
    @Query("SELECT p FROM Payment p WHERE " +
           "LOWER(p.paymentNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.notes) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Payment> searchPayments(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find pending payments
     */
    @Query("SELECT p FROM Payment p WHERE p.status = 'PENDING' " +
           "ORDER BY p.paymentDate DESC")
    List<Payment> findPendingPayments();
    
    /**
     * Find pending payments with pagination
     */
    @Query("SELECT p FROM Payment p WHERE p.status = 'PENDING' " +
           "ORDER BY p.paymentDate DESC")
    Page<Payment> findPendingPayments(Pageable pageable);
    
    /**
     * Find completed payments
     */
    @Query("SELECT p FROM Payment p WHERE p.status = 'COMPLETED' " +
           "ORDER BY p.paymentDate DESC")
    List<Payment> findCompletedPayments();
    
    /**
     * Find cleared payments
     */
    @Query("SELECT p FROM Payment p WHERE p.status = 'CLEARED' " +
           "ORDER BY p.clearedDate DESC")
    List<Payment> findClearedPayments();
    
    /**
     * Find failed payments
     */
    @Query("SELECT p FROM Payment p WHERE p.status = 'FAILED' " +
           "ORDER BY p.paymentDate DESC")
    List<Payment> findFailedPayments();
    
    /**
     * Find unallocated payments
     */
    @Query("SELECT p FROM Payment p WHERE p.isAllocated = false " +
           "AND p.status = 'COMPLETED' " +
           "ORDER BY p.paymentDate ASC")
    List<Payment> findUnallocatedPayments();
    
    /**
     * Find unreconciled payments
     */
    @Query("SELECT p FROM Payment p WHERE p.isReconciled = false " +
           "AND p.status IN ('COMPLETED', 'CLEARED') " +
           "ORDER BY p.paymentDate ASC")
    List<Payment> findUnreconciledPayments();
    
    /**
     * Find cash payments
     */
    @Query("SELECT p FROM Payment p WHERE p.paymentMethod = 'CASH' " +
           "ORDER BY p.paymentDate DESC")
    List<Payment> findCashPayments();
    
    /**
     * Find cheque payments
     */
    @Query("SELECT p FROM Payment p WHERE p.paymentMethod = 'CHEQUE' " +
           "ORDER BY p.paymentDate DESC")
    List<Payment> findChequePayments();
    
    /**
     * Find bank transfer payments
     */
    @Query("SELECT p FROM Payment p WHERE p.paymentMethod = 'BANK_TRANSFER' " +
           "ORDER BY p.paymentDate DESC")
    List<Payment> findBankTransferPayments();
    
    /**
     * Find recent payments
     */
    @Query("SELECT p FROM Payment p ORDER BY p.paymentDate DESC, p.createdAt DESC")
    List<Payment> findRecentPayments(Pageable pageable);
    
    /**
     * Find customer recent payments
     */
    @Query("SELECT p FROM Payment p WHERE p.customerId = :customerId " +
           "ORDER BY p.paymentDate DESC, p.createdAt DESC")
    List<Payment> findCustomerRecentPayments(@Param("customerId") Long customerId, Pageable pageable);
    
    /**
     * Find payments by date range and status
     */
    @Query("SELECT p FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate " +
           "AND p.status = :status ORDER BY p.paymentDate DESC")
    List<Payment> findByDateRangeAndStatus(@Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate,
                                          @Param("status") String status);
    
    /**
     * Find overpayments
     */
    @Query("SELECT p FROM Payment p WHERE p.paidAmount > p.allocatedAmount " +
           "AND p.isAllocated = true " +
           "ORDER BY p.paymentDate DESC")
    List<Payment> findOverpayments();
    
    /**
     * Find partial payments
     */
    @Query("SELECT p FROM Payment p WHERE p.isAllocated = true " +
           "AND p.allocatedAmount < p.paidAmount " +
           "ORDER BY p.paymentDate DESC")
    List<Payment> findPartialPayments();
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count payments by customer
     */
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.customerId = :customerId")
    Long countByCustomerId(@Param("customerId") Long customerId);
    
    /**
     * Count payments by payment type
     */
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.paymentType = :paymentType")
    Long countByPaymentType(@Param("paymentType") String paymentType);
    
    /**
     * Count payments by payment method
     */
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.paymentMethod = :paymentMethod")
    Long countByPaymentMethod(@Param("paymentMethod") String paymentMethod);
    
    /**
     * Count payments by status
     */
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count pending payments
     */
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = 'PENDING'")
    Long countPendingPayments();
    
    /**
     * Count unallocated payments
     */
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.isAllocated = false " +
           "AND p.status = 'COMPLETED'")
    Long countUnallocatedPayments();
    
    /**
     * Count unreconciled payments
     */
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.isReconciled = false " +
           "AND p.status IN ('COMPLETED', 'CLEARED')")
    Long countUnreconciledPayments();
    
    /**
     * Get payment type distribution
     */
    @Query("SELECT p.paymentType, COUNT(p) as paymentCount FROM Payment p " +
           "GROUP BY p.paymentType ORDER BY paymentCount DESC")
    List<Object[]> getPaymentTypeDistribution();
    
    /**
     * Get payment method distribution
     */
    @Query("SELECT p.paymentMethod, COUNT(p) as paymentCount FROM Payment p " +
           "GROUP BY p.paymentMethod ORDER BY paymentCount DESC")
    List<Object[]> getPaymentMethodDistribution();
    
    /**
     * Get status distribution
     */
    @Query("SELECT p.status, COUNT(p) as paymentCount FROM Payment p " +
           "GROUP BY p.status ORDER BY paymentCount DESC")
    List<Object[]> getStatusDistribution();
    
    /**
     * Get monthly payment count
     */
    @Query("SELECT YEAR(p.paymentDate) as year, MONTH(p.paymentDate) as month, " +
           "COUNT(p) as paymentCount FROM Payment p " +
           "WHERE p.paymentDate BETWEEN :startDate AND :endDate " +
           "GROUP BY YEAR(p.paymentDate), MONTH(p.paymentDate) " +
           "ORDER BY year, month")
    List<Object[]> getMonthlyPaymentCount(@Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);
    
    /**
     * Get total payment amount
     */
    @Query("SELECT SUM(p.paidAmount) FROM Payment p WHERE p.status = 'COMPLETED'")
    Double getTotalPaymentAmount();
    
    /**
     * Get total payment amount by customer
     */
    @Query("SELECT p.customerId, p.customerName, SUM(p.paidAmount) as totalAmount " +
           "FROM Payment p WHERE p.status = 'COMPLETED' " +
           "GROUP BY p.customerId, p.customerName ORDER BY totalAmount DESC")
    List<Object[]> getTotalPaymentAmountByCustomer();
    
    /**
     * Get total payment amount by payment method
     */
    @Query("SELECT p.paymentMethod, SUM(p.paidAmount) as totalAmount FROM Payment p " +
           "WHERE p.status = 'COMPLETED' " +
           "GROUP BY p.paymentMethod ORDER BY totalAmount DESC")
    List<Object[]> getTotalPaymentAmountByPaymentMethod();
    
    /**
     * Get average payment amount
     */
    @Query("SELECT AVG(p.paidAmount) FROM Payment p WHERE p.status = 'COMPLETED'")
    Double getAveragePaymentAmount();
    
    /**
     * Get total unallocated amount
     */
    @Query("SELECT SUM(p.paidAmount - p.allocatedAmount) FROM Payment p " +
           "WHERE p.isAllocated = false AND p.status = 'COMPLETED'")
    Double getTotalUnallocatedAmount();
    
    /**
     * Find payments by tags
     */
    @Query("SELECT p FROM Payment p WHERE p.tags LIKE CONCAT('%', :tag, '%')")
    List<Payment> findByTag(@Param("tag") String tag);
}
