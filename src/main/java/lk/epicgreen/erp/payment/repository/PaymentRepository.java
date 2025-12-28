package lk.epicgreen.erp.payment.repository;

import lk.epicgreen.erp.payment.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Payment entity
 * Based on ACTUAL database schema: payments table
 * 
 * Fields: payment_number, payment_date, customer_id (BIGINT),
 *         payment_mode (ENUM: CASH, CHEQUE, BANK_TRANSFER, CREDIT_CARD, ONLINE),
 *         total_amount, allocated_amount, unallocated_amount (GENERATED/COMPUTED),
 *         status (ENUM: DRAFT, PENDING, CLEARED, BOUNCED, CANCELLED),
 *         bank_name, bank_branch, cheque_number, cheque_date, cheque_clearance_date,
 *         bank_reference_number, collected_by (BIGINT), collected_at,
 *         approved_by (BIGINT), approved_at, remarks
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find payment by payment number
     */
    Optional<Payment> findByPaymentNumber(String paymentNumber);
    
    /**
     * Find all payments for a customer
     */
    List<Payment> findByCustomerId(Long customerId);
    
    /**
     * Find all payments for a customer with pagination
     */
    Page<Payment> findByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Find payments by status
     */
    List<Payment> findByStatus(String status);
    
    /**
     * Find payments by status with pagination
     */
    Page<Payment> findByStatus(String status, Pageable pageable);
    
    /**
     * Find payments by payment mode
     */
    List<Payment> findByPaymentMode(String paymentMode);
    
    /**
     * Find payments by payment mode with pagination
     */
    Page<Payment> findByPaymentMode(String paymentMode, Pageable pageable);
    
    /**
     * Find payments by payment date
     */
    List<Payment> findByPaymentDate(LocalDate paymentDate);
    
    /**
     * Find payments by payment date range
     */
    List<Payment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find payments by payment date range with pagination
     */
    Page<Payment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find payments by collected by user
     */
    List<Payment> findByCollectedBy(Long collectedBy);
    
    /**
     * Find payments by approved by user
     */
    List<Payment> findByApprovedBy(Long approvedBy);
    
    /**
     * Find payments by cheque number
     */
    List<Payment> findByChequeNumber(String chequeNumber);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if payment number exists
     */
    boolean existsByPaymentNumber(String paymentNumber);
    
    /**
     * Check if payment number exists excluding specific payment ID
     */
    boolean existsByPaymentNumberAndIdNot(String paymentNumber, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search payments by payment number containing (case-insensitive)
     */
    Page<Payment> findByPaymentNumberContainingIgnoreCase(String paymentNumber, Pageable pageable);
    
    /**
     * Search payments by cheque number containing (case-insensitive)
     */
    Page<Payment> findByChequeNumberContainingIgnoreCase(String chequeNumber, Pageable pageable);
    
    /**
     * Search payments by bank reference number
     */
    Page<Payment> findByBankReferenceNumberContainingIgnoreCase(String bankReferenceNumber, Pageable pageable);
    
    /**
     * Search payments by multiple criteria
     */
    @Query("SELECT p FROM Payment p WHERE " +
           "(:paymentNumber IS NULL OR LOWER(p.paymentNumber) LIKE LOWER(CONCAT('%', :paymentNumber, '%'))) AND " +
           "(:customerId IS NULL OR p.customerId = :customerId) AND " +
           "(:paymentMode IS NULL OR p.paymentMode = :paymentMode) AND " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:startDate IS NULL OR p.paymentDate >= :startDate) AND " +
           "(:endDate IS NULL OR p.paymentDate <= :endDate)")
    Page<Payment> searchPayments(
            @Param("paymentNumber") String paymentNumber,
            @Param("customerId") Long customerId,
            @Param("paymentMode") String paymentMode,
            @Param("status") String status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count payments by status
     */
    long countByStatus(String status);
    
    /**
     * Count payments by payment mode
     */
    long countByPaymentMode(String paymentMode);
    
    /**
     * Count payments by customer
     */
    long countByCustomerId(Long customerId);
    
    /**
     * Count payments in date range
     */
    long countByPaymentDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find draft payments
     */
    @Query("SELECT p FROM Payment p WHERE p.status = 'DRAFT' ORDER BY p.paymentDate DESC")
    List<Payment> findDraftPayments();
    
    /**
     * Find pending payments
     */
    @Query("SELECT p FROM Payment p WHERE p.status = 'PENDING' ORDER BY p.paymentDate")
    List<Payment> findPendingPayments();
    
    /**
     * Find cleared payments
     */
    @Query("SELECT p FROM Payment p WHERE p.status = 'CLEARED' ORDER BY p.paymentDate DESC")
    List<Payment> findClearedPayments();
    
    /**
     * Find bounced payments
     */
    @Query("SELECT p FROM Payment p WHERE p.status = 'BOUNCED' ORDER BY p.paymentDate DESC")
    List<Payment> findBouncedPayments();
    
    /**
     * Find cancelled payments
     */
    @Query("SELECT p FROM Payment p WHERE p.status = 'CANCELLED' ORDER BY p.paymentDate DESC")
    List<Payment> findCancelledPayments();
    
    /**
     * Find cash payments
     */
    @Query("SELECT p FROM Payment p WHERE p.paymentMode = 'CASH' ORDER BY p.paymentDate DESC")
    List<Payment> findCashPayments();
    
    /**
     * Find cheque payments
     */
    @Query("SELECT p FROM Payment p WHERE p.paymentMode = 'CHEQUE' ORDER BY p.paymentDate DESC")
    List<Payment> findChequePayments();
    
    /**
     * Find bank transfer payments
     */
    @Query("SELECT p FROM Payment p WHERE p.paymentMode = 'BANK_TRANSFER' ORDER BY p.paymentDate DESC")
    List<Payment> findBankTransferPayments();
    
    /**
     * Find credit card payments
     */
    @Query("SELECT p FROM Payment p WHERE p.paymentMode = 'CREDIT_CARD' ORDER BY p.paymentDate DESC")
    List<Payment> findCreditCardPayments();
    
    /**
     * Find online payments
     */
    @Query("SELECT p FROM Payment p WHERE p.paymentMode = 'ONLINE' ORDER BY p.paymentDate DESC")
    List<Payment> findOnlinePayments();
    
    /**
     * Find payments with unallocated amount
     */
    @Query("SELECT p FROM Payment p WHERE p.totalAmount > p.allocatedAmount ORDER BY p.paymentDate")
    List<Payment> findPaymentsWithUnallocatedAmount();
    
    /**
     * Find fully allocated payments
     */
    @Query("SELECT p FROM Payment p WHERE p.totalAmount = p.allocatedAmount ORDER BY p.paymentDate DESC")
    List<Payment> findFullyAllocatedPayments();
    
    /**
     * Find payments by customer and status
     */
    List<Payment> findByCustomerIdAndStatus(Long customerId, String status);
    
    /**
     * Find payments by customer and payment mode
     */
    List<Payment> findByCustomerIdAndPaymentMode(Long customerId, String paymentMode);
    
    /**
     * Get total payment amount for a customer
     */
    @Query("SELECT SUM(p.totalAmount) FROM Payment p WHERE p.customerId = :customerId " +
           "AND p.status = 'CLEARED'")
    BigDecimal getTotalPaymentByCustomer(@Param("customerId") Long customerId);
    
    /**
     * Get total allocated amount for a customer
     */
    @Query("SELECT SUM(p.allocatedAmount) FROM Payment p WHERE p.customerId = :customerId " +
           "AND p.status = 'CLEARED'")
    BigDecimal getTotalAllocatedByCustomer(@Param("customerId") Long customerId);
    
    /**
     * Get total unallocated amount for a customer
     */
    @Query("SELECT SUM(p.totalAmount - p.allocatedAmount) FROM Payment p WHERE p.customerId = :customerId " +
           "AND p.status = 'CLEARED'")
    BigDecimal getTotalUnallocatedByCustomer(@Param("customerId") Long customerId);
    
    /**
     * Get payment statistics
     */
    @Query("SELECT " +
           "COUNT(p) as totalPayments, " +
           "SUM(CASE WHEN p.status = 'DRAFT' THEN 1 ELSE 0 END) as draftPayments, " +
           "SUM(CASE WHEN p.status = 'PENDING' THEN 1 ELSE 0 END) as pendingPayments, " +
           "SUM(CASE WHEN p.status = 'CLEARED' THEN 1 ELSE 0 END) as clearedPayments, " +
           "SUM(CASE WHEN p.status = 'BOUNCED' THEN 1 ELSE 0 END) as bouncedPayments, " +
           "SUM(p.totalAmount) as totalAmount, " +
           "SUM(p.allocatedAmount) as totalAllocated " +
           "FROM Payment p")
    Object getPaymentStatistics();
    
    /**
     * Get payments grouped by status
     */
    @Query("SELECT p.status, COUNT(p) as paymentCount, SUM(p.totalAmount) as totalAmount " +
           "FROM Payment p GROUP BY p.status ORDER BY paymentCount DESC")
    List<Object[]> getPaymentsByStatus();
    
    /**
     * Get payments grouped by payment mode
     */
    @Query("SELECT p.paymentMode, COUNT(p) as paymentCount, SUM(p.totalAmount) as totalAmount " +
           "FROM Payment p GROUP BY p.paymentMode ORDER BY totalAmount DESC")
    List<Object[]> getPaymentsByMode();
    
    /**
     * Get payments grouped by customer
     */
    @Query("SELECT p.customerId, COUNT(p) as paymentCount, SUM(p.totalAmount) as totalAmount " +
           "FROM Payment p WHERE p.status = 'CLEARED' " +
           "GROUP BY p.customerId ORDER BY totalAmount DESC")
    List<Object[]> getPaymentsByCustomer();
    
    /**
     * Get daily payment summary
     */
    @Query("SELECT p.paymentDate, COUNT(p) as paymentCount, SUM(p.totalAmount) as totalAmount " +
           "FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate " +
           "GROUP BY p.paymentDate ORDER BY p.paymentDate DESC")
    List<Object[]> getDailyPaymentSummary(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find payments collected in time range
     */
    @Query("SELECT p FROM Payment p WHERE p.collectedAt BETWEEN :startTime AND :endTime " +
           "ORDER BY p.collectedAt DESC")
    List<Payment> findPaymentsCollectedBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * Find payments approved in time range
     */
    @Query("SELECT p FROM Payment p WHERE p.approvedAt BETWEEN :startTime AND :endTime " +
           "ORDER BY p.approvedAt DESC")
    List<Payment> findPaymentsApprovedBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * Find today's payments
     */
    @Query("SELECT p FROM Payment p WHERE p.paymentDate = CURRENT_DATE ORDER BY p.createdAt DESC")
    List<Payment> findTodayPayments();
    
    /**
     * Find cheques for clearance
     */
    @Query("SELECT p FROM Payment p WHERE p.paymentMode = 'CHEQUE' " +
           "AND p.chequeDate <= :date AND p.status = 'PENDING' " +
           "ORDER BY p.chequeDate")
    List<Payment> findChequesForClearance(@Param("date") LocalDate date);
    
    /**
     * Find all payments ordered by date
     */
    List<Payment> findAllByOrderByPaymentDateDescCreatedAtDesc();
    
    /**
     * Get top paying customers
     */
    @Query("SELECT p.customerId, SUM(p.totalAmount) as totalPayment " +
           "FROM Payment p WHERE p.status = 'CLEARED' AND p.paymentDate BETWEEN :startDate AND :endDate " +
           "GROUP BY p.customerId ORDER BY totalPayment DESC")
    List<Object[]> getTopPayingCustomers(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
}
