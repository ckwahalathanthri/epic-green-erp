package lk.epicgreen.erp.payment.repository;

import lk.epicgreen.erp.payment.entity.PaymentAllocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * PaymentAllocation Repository
 * Repository for payment allocation data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface PaymentAllocationRepository extends JpaRepository<PaymentAllocation, Long> {
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find allocations by payment ID
     */
    List<PaymentAllocation> findByPaymentId(Long paymentId);
    
    /**
     * Find allocations by invoice ID
     */
    List<PaymentAllocation> findByInvoiceId(Long invoiceId);
    
    /**
     * Find allocations by customer ID
     */
    List<PaymentAllocation> findByCustomerId(Long customerId);
    
    /**
     * Find allocations by customer ID with pagination
     */
    Page<PaymentAllocation> findByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Find allocations by allocation type
     */
    List<PaymentAllocation> findByAllocationType(String allocationType);
    
    /**
     * Find allocations by status
     */
    List<PaymentAllocation> findByStatus(String status);
    
    /**
     * Find allocations by status with pagination
     */
    Page<PaymentAllocation> findByStatus(String status, Pageable pageable);
    
    /**
     * Find allocations by allocated by user
     */
    List<PaymentAllocation> findByAllocatedByUserId(Long userId);
    
    /**
     * Find allocations by is reversed
     */
    List<PaymentAllocation> findByIsReversed(Boolean isReversed);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find allocations by allocation date between dates
     */
    List<PaymentAllocation> findByAllocationDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find allocations by allocation date between dates with pagination
     */
    Page<PaymentAllocation> findByAllocationDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find allocations by created at between dates
     */
    List<PaymentAllocation> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find allocations by payment ID and status
     */
    List<PaymentAllocation> findByPaymentIdAndStatus(Long paymentId, String status);
    
    /**
     * Find allocations by invoice ID and status
     */
    List<PaymentAllocation> findByInvoiceIdAndStatus(Long invoiceId, String status);
    
    /**
     * Find allocations by customer ID and status
     */
    List<PaymentAllocation> findByCustomerIdAndStatus(Long customerId, String status);
    
    /**
     * Find allocations by allocation type and status
     */
    List<PaymentAllocation> findByAllocationTypeAndStatus(String allocationType, String status);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search allocations
     */
    @Query("SELECT a FROM PaymentAllocation a WHERE " +
           "LOWER(a.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.notes) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<PaymentAllocation> searchAllocations(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find pending allocations
     */
    @Query("SELECT a FROM PaymentAllocation a WHERE a.status = 'PENDING' " +
           "ORDER BY a.allocationDate DESC")
    List<PaymentAllocation> findPendingAllocations();
    
    /**
     * Find completed allocations
     */
    @Query("SELECT a FROM PaymentAllocation a WHERE a.status = 'COMPLETED' " +
           "ORDER BY a.allocationDate DESC")
    List<PaymentAllocation> findCompletedAllocations();
    
    /**
     * Find reversed allocations
     */
    @Query("SELECT a FROM PaymentAllocation a WHERE a.isReversed = true " +
           "ORDER BY a.reversalDate DESC")
    List<PaymentAllocation> findReversedAllocations();
    
    /**
     * Find recent allocations
     */
    @Query("SELECT a FROM PaymentAllocation a ORDER BY a.allocationDate DESC, a.createdAt DESC")
    List<PaymentAllocation> findRecentAllocations(Pageable pageable);
    
    /**
     * Find customer recent allocations
     */
    @Query("SELECT a FROM PaymentAllocation a WHERE a.customerId = :customerId " +
           "ORDER BY a.allocationDate DESC, a.createdAt DESC")
    List<PaymentAllocation> findCustomerRecentAllocations(@Param("customerId") Long customerId, Pageable pageable);
    
    /**
     * Find allocations by date range and status
     */
    @Query("SELECT a FROM PaymentAllocation a WHERE a.allocationDate BETWEEN :startDate AND :endDate " +
           "AND a.status = :status ORDER BY a.allocationDate DESC")
    List<PaymentAllocation> findByDateRangeAndStatus(@Param("startDate") LocalDate startDate,
                                                     @Param("endDate") LocalDate endDate,
                                                     @Param("status") String status);
    
    /**
     * Get total allocated amount by payment
     */
    @Query("SELECT SUM(a.allocatedAmount) FROM PaymentAllocation a " +
           "WHERE a.paymentId = :paymentId AND a.status = 'COMPLETED' " +
           "AND a.isReversed = false")
    Double getTotalAllocatedAmountByPayment(@Param("paymentId") Long paymentId);
    
    /**
     * Get total allocated amount by invoice
     */
    @Query("SELECT SUM(a.allocatedAmount) FROM PaymentAllocation a " +
           "WHERE a.invoiceId = :invoiceId AND a.status = 'COMPLETED' " +
           "AND a.isReversed = false")
    Double getTotalAllocatedAmountByInvoice(@Param("invoiceId") Long invoiceId);
    
    /**
     * Get invoice payment status
     */
    @Query("SELECT a.invoiceId, SUM(a.allocatedAmount) as totalAllocated " +
           "FROM PaymentAllocation a WHERE a.invoiceId = :invoiceId " +
           "AND a.status = 'COMPLETED' AND a.isReversed = false " +
           "GROUP BY a.invoiceId")
    Object[] getInvoicePaymentStatus(@Param("invoiceId") Long invoiceId);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count allocations by payment
     */
    @Query("SELECT COUNT(a) FROM PaymentAllocation a WHERE a.paymentId = :paymentId")
    Long countByPaymentId(@Param("paymentId") Long paymentId);
    
    /**
     * Count allocations by invoice
     */
    @Query("SELECT COUNT(a) FROM PaymentAllocation a WHERE a.invoiceId = :invoiceId")
    Long countByInvoiceId(@Param("invoiceId") Long invoiceId);
    
    /**
     * Count allocations by status
     */
    @Query("SELECT COUNT(a) FROM PaymentAllocation a WHERE a.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count pending allocations
     */
    @Query("SELECT COUNT(a) FROM PaymentAllocation a WHERE a.status = 'PENDING'")
    Long countPendingAllocations();
    
    /**
     * Count reversed allocations
     */
    @Query("SELECT COUNT(a) FROM PaymentAllocation a WHERE a.isReversed = true")
    Long countReversedAllocations();
    
    /**
     * Get allocation type distribution
     */
    @Query("SELECT a.allocationType, COUNT(a) as allocationCount FROM PaymentAllocation a " +
           "GROUP BY a.allocationType ORDER BY allocationCount DESC")
    List<Object[]> getAllocationTypeDistribution();
    
    /**
     * Get status distribution
     */
    @Query("SELECT a.status, COUNT(a) as allocationCount FROM PaymentAllocation a " +
           "GROUP BY a.status ORDER BY allocationCount DESC")
    List<Object[]> getStatusDistribution();
    
    /**
     * Get monthly allocation count
     */
    @Query("SELECT YEAR(a.allocationDate) as year, MONTH(a.allocationDate) as month, " +
           "COUNT(a) as allocationCount FROM PaymentAllocation a " +
           "WHERE a.allocationDate BETWEEN :startDate AND :endDate " +
           "GROUP BY YEAR(a.allocationDate), MONTH(a.allocationDate) " +
           "ORDER BY year, month")
    List<Object[]> getMonthlyAllocationCount(@Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate);
    
    /**
     * Get total allocated amount
     */
    @Query("SELECT SUM(a.allocatedAmount) FROM PaymentAllocation a " +
           "WHERE a.status = 'COMPLETED' AND a.isReversed = false")
    Double getTotalAllocatedAmount();
    
    /**
     * Get average allocation amount
     */
    @Query("SELECT AVG(a.allocatedAmount) FROM PaymentAllocation a " +
           "WHERE a.status = 'COMPLETED' AND a.isReversed = false")
    Double getAverageAllocationAmount();
    
    /**
     * Get total allocated amount by customer
     */
    @Query("SELECT a.customerId, a.customerName, SUM(a.allocatedAmount) as totalAmount " +
           "FROM PaymentAllocation a WHERE a.status = 'COMPLETED' AND a.isReversed = false " +
           "GROUP BY a.customerId, a.customerName ORDER BY totalAmount DESC")
    List<Object[]> getTotalAllocatedAmountByCustomer();
    
    /**
     * Find allocations by tags
     */
    @Query("SELECT a FROM PaymentAllocation a WHERE a.tags LIKE CONCAT('%', :tag, '%')")
    List<PaymentAllocation> findByTag(@Param("tag") String tag);
}
