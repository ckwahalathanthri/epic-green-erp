package lk.epicgreen.erp.payment.repository;

import lk.epicgreen.erp.payment.entity.SalesReturn;
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
 * Repository interface for SalesReturn entity
 * Based on ACTUAL database schema: sales_returns table
 * 
 * Fields: return_number, return_date, order_id (BIGINT), invoice_id (BIGINT),
 *         customer_id (BIGINT), warehouse_id (BIGINT),
 *         return_type (ENUM: QUALITY_ISSUE, WRONG_PRODUCT, DAMAGED, EXPIRED, CUSTOMER_REQUEST, OTHER),
 *         status (ENUM: PENDING, APPROVED, REJECTED, RECEIVED, PROCESSED),
 *         approved_by (BIGINT), approved_at,
 *         subtotal, tax_amount, total_amount,
 *         refund_mode (ENUM: CASH, CHEQUE, CREDIT_NOTE, BANK_TRANSFER),
 *         refund_status (ENUM: PENDING, PROCESSED, COMPLETED), remarks
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface SalesReturnRepository extends JpaRepository<SalesReturn, Long>, JpaSpecificationExecutor<SalesReturn> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find sales return by return number
     */
    Optional<SalesReturn> findByReturnNumber(String returnNumber);
    
    /**
     * Find all returns for a customer
     */
    List<SalesReturn> findByCustomerId(Long customerId);
    
    /**
     * Find all returns for a customer with pagination
     */
    Page<SalesReturn> findByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Find returns by order
     */
    List<SalesReturn> findByOrderId(Long orderId);
    
    /**
     * Find returns by invoice
     */
    List<SalesReturn> findByInvoiceId(Long invoiceId);
    
    /**
     * Find returns by warehouse
     */
    List<SalesReturn> findByWarehouseId(Long warehouseId);
    
    /**
     * Find returns by status
     */
    List<SalesReturn> findByStatus(String status);
    
    /**
     * Find returns by status with pagination
     */
    Page<SalesReturn> findByStatus(String status, Pageable pageable);
    
    /**
     * Find returns by return type
     */
    List<SalesReturn> findByReturnType(String returnType);
    
    /**
     * Find returns by refund mode
     */
    List<SalesReturn> findByRefundMode(String refundMode);
    
    /**
     * Find returns by refund status
     */
    List<SalesReturn> findByRefundStatus(String refundStatus);
    
    /**
     * Find returns by return date
     */
    List<SalesReturn> findByReturnDate(LocalDate returnDate);
    
    /**
     * Find returns by return date range
     */
    List<SalesReturn> findByReturnDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find returns by return date range with pagination
     */
    Page<SalesReturn> findByReturnDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find returns approved by user
     */
    List<SalesReturn> findByApprovedBy(Long approvedBy);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if return number exists
     */
    boolean existsByReturnNumber(String returnNumber);
    
    /**
     * Check if return number exists excluding specific return ID
     */
    boolean existsByReturnNumberAndIdNot(String returnNumber, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search returns by return number containing (case-insensitive)
     */
    Page<SalesReturn> findByReturnNumberContainingIgnoreCase(String returnNumber, Pageable pageable);
    
    /**
     * Search returns by multiple criteria
     */
    @Query("SELECT sr FROM SalesReturn sr WHERE " +
           "(:returnNumber IS NULL OR LOWER(sr.returnNumber) LIKE LOWER(CONCAT('%', :returnNumber, '%'))) AND " +
           "(:customerId IS NULL OR sr.customerId = :customerId) AND " +
           "(:status IS NULL OR sr.status = :status) AND " +
           "(:returnType IS NULL OR sr.returnType = :returnType) AND " +
           "(:refundStatus IS NULL OR sr.refundStatus = :refundStatus) AND " +
           "(:startDate IS NULL OR sr.returnDate >= :startDate) AND " +
           "(:endDate IS NULL OR sr.returnDate <= :endDate)")
    Page<SalesReturn> searchReturns(
            @Param("returnNumber") String returnNumber,
            @Param("customerId") Long customerId,
            @Param("status") String status,
            @Param("returnType") String returnType,
            @Param("refundStatus") String refundStatus,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count returns by status
     */
    long countByStatus(String status);
    
    /**
     * Count returns by return type
     */
    long countByReturnType(String returnType);
    
    /**
     * Count returns by customer
     */
    long countByCustomerId(Long customerId);
    
    /**
     * Count returns in date range
     */
    long countByReturnDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find pending returns
     */
    @Query("SELECT sr FROM SalesReturn sr WHERE sr.status = 'PENDING' ORDER BY sr.returnDate")
    List<SalesReturn> findPendingReturns();
    
    /**
     * Find approved returns
     */
    @Query("SELECT sr FROM SalesReturn sr WHERE sr.status = 'APPROVED' ORDER BY sr.approvedAt DESC")
    List<SalesReturn> findApprovedReturns();
    
    /**
     * Find rejected returns
     */
    @Query("SELECT sr FROM SalesReturn sr WHERE sr.status = 'REJECTED' ORDER BY sr.returnDate DESC")
    List<SalesReturn> findRejectedReturns();
    
    /**
     * Find received returns
     */
    @Query("SELECT sr FROM SalesReturn sr WHERE sr.status = 'RECEIVED' ORDER BY sr.returnDate DESC")
    List<SalesReturn> findReceivedReturns();
    
    /**
     * Find processed returns
     */
    @Query("SELECT sr FROM SalesReturn sr WHERE sr.status = 'PROCESSED' ORDER BY sr.returnDate DESC")
    List<SalesReturn> findProcessedReturns();
    
    /**
     * Find quality issue returns
     */
    @Query("SELECT sr FROM SalesReturn sr WHERE sr.returnType = 'QUALITY_ISSUE' ORDER BY sr.returnDate DESC")
    List<SalesReturn> findQualityIssueReturns();
    
    /**
     * Find damaged returns
     */
    @Query("SELECT sr FROM SalesReturn sr WHERE sr.returnType = 'DAMAGED' ORDER BY sr.returnDate DESC")
    List<SalesReturn> findDamagedReturns();
    
    /**
     * Find expired returns
     */
    @Query("SELECT sr FROM SalesReturn sr WHERE sr.returnType = 'EXPIRED' ORDER BY sr.returnDate DESC")
    List<SalesReturn> findExpiredReturns();
    
    /**
     * Find returns by customer and status
     */
    List<SalesReturn> findByCustomerIdAndStatus(Long customerId, String status);
    
    /**
     * Get total return amount by customer
     */
    @Query("SELECT SUM(sr.totalAmount) FROM SalesReturn sr WHERE sr.customerId = :customerId " +
           "AND sr.status IN ('APPROVED', 'RECEIVED', 'PROCESSED')")
    BigDecimal getTotalReturnAmountByCustomer(@Param("customerId") Long customerId);
    
    /**
     * Get return statistics
     */
    @Query("SELECT " +
           "COUNT(sr) as totalReturns, " +
           "SUM(CASE WHEN sr.status = 'PENDING' THEN 1 ELSE 0 END) as pendingReturns, " +
           "SUM(CASE WHEN sr.status = 'APPROVED' THEN 1 ELSE 0 END) as approvedReturns, " +
           "SUM(CASE WHEN sr.status = 'REJECTED' THEN 1 ELSE 0 END) as rejectedReturns, " +
           "SUM(CASE WHEN sr.status = 'PROCESSED' THEN 1 ELSE 0 END) as processedReturns, " +
           "SUM(sr.totalAmount) as totalReturnValue " +
           "FROM SalesReturn sr")
    Object getReturnStatistics();
    
    /**
     * Get returns grouped by status
     */
    @Query("SELECT sr.status, COUNT(sr) as returnCount, SUM(sr.totalAmount) as totalAmount " +
           "FROM SalesReturn sr GROUP BY sr.status ORDER BY returnCount DESC")
    List<Object[]> getReturnsByStatus();
    
    /**
     * Get returns grouped by return type
     */
    @Query("SELECT sr.returnType, COUNT(sr) as returnCount, SUM(sr.totalAmount) as totalAmount " +
           "FROM SalesReturn sr GROUP BY sr.returnType ORDER BY returnCount DESC")
    List<Object[]> getReturnsByType();
    
    /**
     * Get returns grouped by refund mode
     */
    @Query("SELECT sr.refundMode, COUNT(sr) as returnCount, SUM(sr.totalAmount) as totalAmount " +
           "FROM SalesReturn sr GROUP BY sr.refundMode ORDER BY returnCount DESC")
    List<Object[]> getReturnsByRefundMode();
    
    /**
     * Get returns grouped by customer
     */
    @Query("SELECT sr.customerId, COUNT(sr) as returnCount, SUM(sr.totalAmount) as totalAmount " +
           "FROM SalesReturn sr GROUP BY sr.customerId ORDER BY totalAmount DESC")
    List<Object[]> getReturnsByCustomer();
    
    /**
     * Get daily return summary
     */
    @Query("SELECT sr.returnDate, COUNT(sr) as returnCount, SUM(sr.totalAmount) as totalAmount " +
           "FROM SalesReturn sr WHERE sr.returnDate BETWEEN :startDate AND :endDate " +
           "GROUP BY sr.returnDate ORDER BY sr.returnDate DESC")
    List<Object[]> getDailyReturnSummary(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find returns approved in time range
     */
    @Query("SELECT sr FROM SalesReturn sr WHERE sr.approvedAt BETWEEN :startTime AND :endTime " +
           "ORDER BY sr.approvedAt DESC")
    List<SalesReturn> findReturnsApprovedBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * Find today's returns
     */
    @Query("SELECT sr FROM SalesReturn sr WHERE sr.returnDate = CURRENT_DATE ORDER BY sr.createdAt DESC")
    List<SalesReturn> findTodayReturns();
    
    /**
     * Find all returns ordered by date
     */
    List<SalesReturn> findAllByOrderByReturnDateDescCreatedAtDesc();
    
    /**
     * Find returns pending refund
     */
    @Query("SELECT sr FROM SalesReturn sr WHERE sr.refundStatus = 'PENDING' " +
           "AND sr.status IN ('APPROVED', 'RECEIVED', 'PROCESSED') ORDER BY sr.returnDate")
    List<SalesReturn> findReturnsPendingRefund();
}
