package lk.epicgreen.erp.payment.repository;

import lk.epicgreen.erp.payment.entity.PaymentAllocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for PaymentAllocation entity
 * Based on ACTUAL database schema: payment_allocations table
 * 
 * Fields: payment_id (BIGINT), invoice_id (BIGINT), allocated_amount,
 *         allocation_date, remarks
 * 
 * NOTE: This implements Bill-to-Bill settlement where payments are allocated to specific invoices
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface PaymentAllocationRepository extends JpaRepository<PaymentAllocation, Long>, JpaSpecificationExecutor<PaymentAllocation> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find all allocations for a payment
     */
    List<PaymentAllocation> findByPaymentId(Long paymentId);
    
    /**
     * Find all allocations for a payment with pagination
     */
    Page<PaymentAllocation> findByPaymentId(Long paymentId, Pageable pageable);
    
    /**
     * Find all allocations for an invoice
     */
    List<PaymentAllocation> findByInvoiceId(Long invoiceId);
    
    /**
     * Find all allocations for an invoice with pagination
     */
    Page<PaymentAllocation> findByInvoiceId(Long invoiceId, Pageable pageable);

    
    /**
     * Find allocation by payment and invoice
     */
    List<PaymentAllocation> findByPaymentIdAndInvoiceId(Long paymentId, Long invoiceId);
    
    /**
     * Find allocations by allocation date
     */
    List<PaymentAllocation> findByAllocationDate(LocalDate allocationDate);
    
    /**
     * Find allocations by allocation date range
     */
    List<PaymentAllocation> findByAllocationDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find allocations by allocation date range with pagination
     */
    Page<PaymentAllocation> findByAllocationDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count allocations for a payment
     */
    long countByPaymentId(Long paymentId);
    
    /**
     * Count allocations for an invoice
     */
    long countByInvoiceId(Long invoiceId);
    
    // ==================== DELETE METHODS ====================
    
    /**
     * Delete all allocations for a payment
     */
    @Modifying
    @Query("DELETE FROM PaymentAllocation pa WHERE pa.payment.id = :paymentId")
    void deleteAllByPaymentId(@Param("paymentId") Long paymentId);
    
    /**
     * Delete all allocations for an invoice
     */
    @Modifying
    @Query("DELETE FROM PaymentAllocation pa WHERE pa.invoice.id = :invoiceId")
    void deleteAllByInvoiceId(@Param("invoiceId") Long invoiceId);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Get total allocated amount for a payment
     */
    @Query("SELECT SUM(pa.allocatedAmount) FROM PaymentAllocation pa WHERE pa.payment.id = :paymentId")
    BigDecimal getTotalAllocatedByPayment(@Param("paymentId") Long paymentId);
    
    /**
     * Get total allocated amount for an invoice
     */
    @Query("SELECT SUM(pa.allocatedAmount) FROM PaymentAllocation pa WHERE pa.invoice.id = :invoiceId")
    BigDecimal getTotalAllocatedByInvoice(@Param("invoiceId") Long invoiceId);
    
    /**
     * Get allocation statistics
     */
    @Query("SELECT " +
           "COUNT(pa) as totalAllocations, " +
           "SUM(pa.allocatedAmount) as totalAllocatedAmount, " +
           "COUNT(DISTINCT pa.payment.id) as uniquePayments, " +
           "COUNT(DISTINCT pa.invoice.id) as uniqueInvoices " +
           "FROM PaymentAllocation pa")
    Object getAllocationStatistics();
    
    /**
     * Get allocations grouped by payment
     */
    @Query("SELECT pa.payment.id, COUNT(pa) as allocationCount, SUM(pa.allocatedAmount) as totalAllocated " +
           "FROM PaymentAllocation pa GROUP BY pa.payment.id ORDER BY totalAllocated DESC")
    List<Object[]> getAllocationsByPayment();
    
    /**
     * Get allocations grouped by invoice
     */
    @Query("SELECT pa.invoice.id, COUNT(pa) as allocationCount, SUM(pa.allocatedAmount) as totalAllocated " +
           "FROM PaymentAllocation pa GROUP BY pa.invoice.id ORDER BY totalAllocated DESC")
    List<Object[]> getAllocationsByInvoice();
    
    /**
     * Get daily allocation summary
     */
    @Query("SELECT pa.allocationDate, COUNT(pa) as allocationCount, SUM(pa.allocatedAmount) as totalAllocated " +
           "FROM PaymentAllocation pa WHERE pa.allocationDate BETWEEN :startDate AND :endDate " +
           "GROUP BY pa.allocationDate ORDER BY pa.allocationDate DESC")
    List<Object[]> getDailyAllocationSummary(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find today's allocations
     */
    @Query("SELECT pa FROM PaymentAllocation pa WHERE pa.allocationDate = CURRENT_DATE " +
           "ORDER BY pa.createdAt DESC")
    List<PaymentAllocation> findTodayAllocations();
    
    /**
     * Find all allocations ordered by allocation date
     */
    List<PaymentAllocation> findAllByOrderByAllocationDateDescCreatedAtDesc();
    
    /**
     * Check if payment is fully allocated
     */
    @Query("SELECT CASE WHEN SUM(pa.allocatedAmount) >= " +
           "(SELECT p.totalAmount FROM Payment p WHERE p.id = :paymentId) " +
           "THEN true ELSE false END FROM PaymentAllocation pa WHERE pa.payment.id = :paymentId")
    boolean isPaymentFullyAllocated(@Param("paymentId") Long paymentId);
    
    /**
     * Check if invoice is fully paid
     */
    @Query("SELECT CASE WHEN SUM(pa.allocatedAmount) >= " +
           "(SELECT i.totalAmount FROM Invoice i WHERE i.id = :invoiceId) " +
           "THEN true ELSE false END FROM PaymentAllocation pa WHERE pa.invoice.id = :invoiceId")
    boolean isInvoiceFullyPaid(@Param("invoiceId") Long invoiceId);

    @Query("SELECT pa FROM PaymentAllocation pa WHERE pa.payment.customer.id = :customerId")
    List<PaymentAllocation> findByPaymentCustomerId(Long customerId);
}
