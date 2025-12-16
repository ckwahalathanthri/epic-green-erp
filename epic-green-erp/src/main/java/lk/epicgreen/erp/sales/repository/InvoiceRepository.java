package lk.epicgreen.erp.sales.repository;

import lk.epicgreen.erp.sales.entity.Invoice;
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
 * Invoice Repository
 * Repository for invoice data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find invoice by invoice number
     */
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    
    /**
     * Check if invoice exists by invoice number
     */
    boolean existsByInvoiceNumber(String invoiceNumber);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find invoices by sales order ID
     */
    List<Invoice> findBySalesOrderId(Long salesOrderId);
    
    /**
     * Find invoices by dispatch note ID
     */
    List<Invoice> findByDispatchNoteId(Long dispatchNoteId);
    
    /**
     * Find invoices by customer ID
     */
    List<Invoice> findByCustomerId(Long customerId);
    
    /**
     * Find invoices by customer ID with pagination
     */
    Page<Invoice> findByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Find invoices by invoice type
     */
    List<Invoice> findByInvoiceType(String invoiceType);
    
    /**
     * Find invoices by invoice type with pagination
     */
    Page<Invoice> findByInvoiceType(String invoiceType, Pageable pageable);
    
    /**
     * Find invoices by status
     */
    List<Invoice> findByStatus(String status);
    
    /**
     * Find invoices by status with pagination
     */
    Page<Invoice> findByStatus(String status, Pageable pageable);
    
    /**
     * Find invoices by payment status
     */
    List<Invoice> findByPaymentStatus(String paymentStatus);
    
    /**
     * Find invoices by payment status with pagination
     */
    Page<Invoice> findByPaymentStatus(String paymentStatus, Pageable pageable);
    
    /**
     * Find invoices by payment terms
     */
    List<Invoice> findByPaymentTerms(String paymentTerms);
    
    /**
     * Find invoices by sales representative
     */
    List<Invoice> findBySalesRepId(Long salesRepId);
    
    /**
     * Find invoices by created by user
     */
    List<Invoice> findByCreatedByUserId(Long userId);
    
    /**
     * Find invoices by approved by user
     */
    List<Invoice> findByApprovedByUserId(Long userId);
    
    /**
     * Find invoices by is approved
     */
    List<Invoice> findByIsApproved(Boolean isApproved);
    
    /**
     * Find invoices by is paid
     */
    List<Invoice> findByIsPaid(Boolean isPaid);
    
    /**
     * Find invoices by is overdue
     */
    List<Invoice> findByIsOverdue(Boolean isOverdue);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find invoices by invoice date between dates
     */
    List<Invoice> findByInvoiceDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find invoices by invoice date between dates with pagination
     */
    Page<Invoice> findByInvoiceDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find invoices by due date between dates
     */
    List<Invoice> findByDueDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find invoices by due date between dates with pagination
     */
    Page<Invoice> findByDueDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find invoices by paid date between dates
     */
    List<Invoice> findByPaidDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find invoices by created at between dates
     */
    List<Invoice> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find invoices by customer ID and status
     */
    List<Invoice> findByCustomerIdAndStatus(Long customerId, String status);
    
    /**
     * Find invoices by customer ID and status with pagination
     */
    Page<Invoice> findByCustomerIdAndStatus(Long customerId, String status, Pageable pageable);
    
    /**
     * Find invoices by customer ID and payment status
     */
    List<Invoice> findByCustomerIdAndPaymentStatus(Long customerId, String paymentStatus);
    
    /**
     * Find invoices by customer ID and payment status with pagination
     */
    Page<Invoice> findByCustomerIdAndPaymentStatus(Long customerId, String paymentStatus, Pageable pageable);
    
    /**
     * Find invoices by invoice type and status
     */
    List<Invoice> findByInvoiceTypeAndStatus(String invoiceType, String status);
    
    /**
     * Find invoices by is approved and status
     */
    List<Invoice> findByIsApprovedAndStatus(Boolean isApproved, String status);
    
    /**
     * Find invoices by is paid and is overdue
     */
    List<Invoice> findByIsPaidAndIsOverdue(Boolean isPaid, Boolean isOverdue);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search invoices
     */
    @Query("SELECT i FROM Invoice i WHERE " +
           "LOWER(i.invoiceNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.notes) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Invoice> searchInvoices(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find draft invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.status = 'DRAFT' " +
           "ORDER BY i.invoiceDate DESC")
    List<Invoice> findDraftInvoices();
    
    /**
     * Find pending invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.status = 'PENDING' " +
           "ORDER BY i.invoiceDate DESC")
    List<Invoice> findPendingInvoices();
    
    /**
     * Find pending invoices with pagination
     */
    @Query("SELECT i FROM Invoice i WHERE i.status = 'PENDING' " +
           "ORDER BY i.invoiceDate DESC")
    Page<Invoice> findPendingInvoices(Pageable pageable);
    
    /**
     * Find approved invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.status = 'APPROVED' AND i.isApproved = true " +
           "ORDER BY i.invoiceDate DESC")
    List<Invoice> findApprovedInvoices();
    
    /**
     * Find sent invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.status = 'SENT' " +
           "ORDER BY i.invoiceDate DESC")
    List<Invoice> findSentInvoices();
    
    /**
     * Find paid invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.status = 'PAID' AND i.isPaid = true " +
           "ORDER BY i.paidDate DESC")
    List<Invoice> findPaidInvoices();
    
    /**
     * Find cancelled invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.status = 'CANCELLED' " +
           "ORDER BY i.invoiceDate DESC")
    List<Invoice> findCancelledInvoices();
    
    /**
     * Find unpaid invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.isPaid = false " +
           "AND i.paymentStatus != 'PAID' " +
           "AND i.status NOT IN ('DRAFT', 'CANCELLED') " +
           "ORDER BY i.dueDate ASC")
    List<Invoice> findUnpaidInvoices();
    
    /**
     * Find partially paid invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.paymentStatus = 'PARTIAL' " +
           "AND i.paidAmount > 0 AND i.balanceAmount > 0 " +
           "ORDER BY i.dueDate ASC")
    List<Invoice> findPartiallyPaidInvoices();
    
    /**
     * Find overdue invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.isOverdue = true " +
           "AND i.isPaid = false " +
           "AND i.dueDate < :currentDate " +
           "AND i.status NOT IN ('DRAFT', 'CANCELLED', 'PAID') " +
           "ORDER BY i.dueDate ASC")
    List<Invoice> findOverdueInvoices(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Find invoices pending approval
     */
    @Query("SELECT i FROM Invoice i WHERE i.isApproved = false " +
           "AND i.status NOT IN ('DRAFT', 'CANCELLED') " +
           "ORDER BY i.invoiceDate ASC")
    List<Invoice> findInvoicesPendingApproval();
    
    /**
     * Find invoices due soon
     */
    @Query("SELECT i FROM Invoice i WHERE i.dueDate BETWEEN :startDate AND :endDate " +
           "AND i.isPaid = false " +
           "AND i.status NOT IN ('DRAFT', 'CANCELLED', 'PAID') " +
           "ORDER BY i.dueDate ASC")
    List<Invoice> findInvoicesDueSoon(@Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);
    
    /**
     * Find recent invoices
     */
    @Query("SELECT i FROM Invoice i ORDER BY i.invoiceDate DESC, i.createdAt DESC")
    List<Invoice> findRecentInvoices(Pageable pageable);
    
    /**
     * Find customer recent invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.customerId = :customerId " +
           "ORDER BY i.invoiceDate DESC, i.createdAt DESC")
    List<Invoice> findCustomerRecentInvoices(@Param("customerId") Long customerId, Pageable pageable);
    
    /**
     * Find sales rep recent invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.salesRepId = :salesRepId " +
           "ORDER BY i.invoiceDate DESC, i.createdAt DESC")
    List<Invoice> findSalesRepRecentInvoices(@Param("salesRepId") Long salesRepId, Pageable pageable);
    
    /**
     * Find invoices by date range and status
     */
    @Query("SELECT i FROM Invoice i WHERE i.invoiceDate BETWEEN :startDate AND :endDate " +
           "AND i.status = :status ORDER BY i.invoiceDate DESC")
    List<Invoice> findByDateRangeAndStatus(@Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate,
                                           @Param("status") String status);
    
    /**
     * Find customer outstanding invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.customerId = :customerId " +
           "AND i.isPaid = false AND i.balanceAmount > 0 " +
           "ORDER BY i.dueDate ASC")
    List<Invoice> findCustomerOutstandingInvoices(@Param("customerId") Long customerId);
    
    /**
     * Get customer total outstanding
     */
    @Query("SELECT SUM(i.balanceAmount) FROM Invoice i WHERE i.customerId = :customerId " +
           "AND i.isPaid = false AND i.balanceAmount > 0")
    Double getCustomerTotalOutstanding(@Param("customerId") Long customerId);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count invoices by customer
     */
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.customerId = :customerId")
    Long countByCustomerId(@Param("customerId") Long customerId);
    
    /**
     * Count invoices by sales rep
     */
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.salesRepId = :salesRepId")
    Long countBySalesRepId(@Param("salesRepId") Long salesRepId);
    
    /**
     * Count invoices by invoice type
     */
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.invoiceType = :invoiceType")
    Long countByInvoiceType(@Param("invoiceType") String invoiceType);
    
    /**
     * Count invoices by status
     */
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count unpaid invoices
     */
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.isPaid = false " +
           "AND i.status NOT IN ('DRAFT', 'CANCELLED')")
    Long countUnpaidInvoices();
    
    /**
     * Count overdue invoices
     */
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.isOverdue = true " +
           "AND i.isPaid = false")
    Long countOverdueInvoices();
    
    /**
     * Count invoices pending approval
     */
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.isApproved = false " +
           "AND i.status NOT IN ('DRAFT', 'CANCELLED')")
    Long countInvoicesPendingApproval();
    
    /**
     * Get invoice type distribution
     */
    @Query("SELECT i.invoiceType, COUNT(i) as invoiceCount FROM Invoice i " +
           "GROUP BY i.invoiceType ORDER BY invoiceCount DESC")
    List<Object[]> getInvoiceTypeDistribution();
    
    /**
     * Get status distribution
     */
    @Query("SELECT i.status, COUNT(i) as invoiceCount FROM Invoice i " +
           "GROUP BY i.status ORDER BY invoiceCount DESC")
    List<Object[]> getStatusDistribution();
    
    /**
     * Get payment status distribution
     */
    @Query("SELECT i.paymentStatus, COUNT(i) as invoiceCount FROM Invoice i " +
           "GROUP BY i.paymentStatus ORDER BY invoiceCount DESC")
    List<Object[]> getPaymentStatusDistribution();
    
    /**
     * Get monthly invoice count
     */
    @Query("SELECT YEAR(i.invoiceDate) as year, MONTH(i.invoiceDate) as month, " +
           "COUNT(i) as invoiceCount FROM Invoice i " +
           "WHERE i.invoiceDate BETWEEN :startDate AND :endDate " +
           "GROUP BY YEAR(i.invoiceDate), MONTH(i.invoiceDate) " +
           "ORDER BY year, month")
    List<Object[]> getMonthlyInvoiceCount(@Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);
    
    /**
     * Get total invoice value
     */
    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.status NOT IN ('DRAFT', 'CANCELLED')")
    Double getTotalInvoiceValue();
    
    /**
     * Get total paid amount
     */
    @Query("SELECT SUM(i.paidAmount) FROM Invoice i WHERE i.isPaid = true")
    Double getTotalPaidAmount();
    
    /**
     * Get total outstanding amount
     */
    @Query("SELECT SUM(i.balanceAmount) FROM Invoice i WHERE i.isPaid = false " +
           "AND i.balanceAmount > 0")
    Double getTotalOutstandingAmount();
    
    /**
     * Get total overdue amount
     */
    @Query("SELECT SUM(i.balanceAmount) FROM Invoice i WHERE i.isOverdue = true " +
           "AND i.isPaid = false AND i.balanceAmount > 0")
    Double getTotalOverdueAmount();
    
    /**
     * Get total invoice value by customer
     */
    @Query("SELECT i.customerId, i.customerName, SUM(i.totalAmount) as totalValue " +
           "FROM Invoice i WHERE i.status NOT IN ('DRAFT', 'CANCELLED') " +
           "GROUP BY i.customerId, i.customerName ORDER BY totalValue DESC")
    List<Object[]> getTotalInvoiceValueByCustomer();
    
    /**
     * Get total outstanding by customer
     */
    @Query("SELECT i.customerId, i.customerName, SUM(i.balanceAmount) as totalOutstanding " +
           "FROM Invoice i WHERE i.isPaid = false AND i.balanceAmount > 0 " +
           "GROUP BY i.customerId, i.customerName ORDER BY totalOutstanding DESC")
    List<Object[]> getTotalOutstandingByCustomer();
    
    /**
     * Get average invoice value
     */
    @Query("SELECT AVG(i.totalAmount) FROM Invoice i WHERE i.status NOT IN ('DRAFT', 'CANCELLED')")
    Double getAverageInvoiceValue();
    
    /**
     * Get collection rate
     */
    @Query("SELECT " +
           "(SELECT SUM(i.paidAmount) FROM Invoice i WHERE i.isPaid = true) * 100.0 / " +
           "(SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.status NOT IN ('DRAFT', 'CANCELLED')) " +
           "FROM Invoice i WHERE i.status NOT IN ('DRAFT', 'CANCELLED')")
    Double getCollectionRate();
    
    /**
     * Find invoices by tags
     */
    @Query("SELECT i FROM Invoice i WHERE i.tags LIKE CONCAT('%', :tag, '%')")
    List<Invoice> findByTag(@Param("tag") String tag);
}
