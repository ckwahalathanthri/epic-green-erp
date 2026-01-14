package lk.epicgreen.erp.sales.repository;

import lk.epicgreen.erp.sales.entity.Invoice;
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
 * Repository interface for Invoice entity
 * Based on ACTUAL database schema: invoices table
 * 
 * Fields: invoice_number, invoice_date, order_id (BIGINT), dispatch_id (BIGINT),
 *         customer_id (BIGINT), billing_address_id (BIGINT),
 *         invoice_type (ENUM: TAX_INVOICE, CREDIT_NOTE, DEBIT_NOTE),
 *         payment_terms, due_date, subtotal, tax_amount, discount_amount,
 *         freight_charges, total_amount, paid_amount, balance_amount (GENERATED/COMPUTED),
 *         payment_status (ENUM: UNPAID, PARTIAL, PAID, OVERDUE),
 *         status (ENUM: DRAFT, POSTED, CANCELLED), remarks
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long>, JpaSpecificationExecutor<Invoice> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find invoice by invoice number
     */
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    
    /**
     * Find all invoices for a customer
     */
    List<Invoice> findByCustomerId(Long customerId);
    
    /**
     * Find all invoices for a customer with pagination
     */
    Page<Invoice> findByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Find invoices for an order
     */
    List<Invoice> findByOrderId(Long orderId);
    
    /**
     * Find invoice for a dispatch
     */
    Optional<Invoice> findByDispatchId(Long dispatchId);
    
    /**
     * Find invoices by invoice type
     */
    List<Invoice> findByInvoiceType(String invoiceType);
    
    /**
     * Find invoices by payment status
     */
    List<Invoice> findByPaymentStatus(String paymentStatus);
    
    /**
     * Find invoices by payment status with pagination
     */
    Page<Invoice> findByPaymentStatus(String paymentStatus, Pageable pageable);
    
    /**
     * Find invoices by status
     */
    List<Invoice> findByStatus(String status);
    
    /**
     * Find invoices by invoice date
     */
    List<Invoice> findByInvoiceDate(LocalDate invoiceDate);
    
    /**
     * Find invoices by invoice date range
     */
    List<Invoice> findByInvoiceDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find invoices by invoice date range with pagination
     */
    Page<Invoice> findByInvoiceDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find invoices by due date
     */
    List<Invoice> findByDueDate(LocalDate dueDate);
    
    /**
     * Find invoices by due date range
     */
    List<Invoice> findByDueDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if invoice number exists
     */
    boolean existsByInvoiceNumber(String invoiceNumber);
    
    /**
     * Check if invoice number exists excluding specific invoice ID
     */
    boolean existsByInvoiceNumberAndIdNot(String invoiceNumber, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search invoices by invoice number containing (case-insensitive)
     */
    Page<Invoice> findByInvoiceNumberContainingIgnoreCase(String invoiceNumber, Pageable pageable);
    
    /**
     * Search invoices by multiple criteria
     */
    @Query("SELECT i FROM Invoice i WHERE " +
           "(:invoiceNumber IS NULL OR LOWER(i.invoiceNumber) LIKE LOWER(CONCAT('%', :invoiceNumber, '%'))) AND " +
           "(:customerId IS NULL OR i.customer.id = :customerId) AND " +
           "(:invoiceType IS NULL OR i.invoiceType = :invoiceType) AND " +
           "(:paymentStatus IS NULL OR i.paymentStatus = :paymentStatus) AND " +
           "(:status IS NULL OR i.status = :status) AND " +
           "(:startDate IS NULL OR i.invoiceDate >= :startDate) AND " +
           "(:endDate IS NULL OR i.invoiceDate <= :endDate)")
    Page<Invoice> searchInvoices(
            @Param("invoiceNumber") String invoiceNumber,
            @Param("customerId") Long customerId,
            @Param("invoiceType") String invoiceType,
            @Param("paymentStatus") String paymentStatus,
            @Param("status") String status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count invoices by payment status
     */
    long countByPaymentStatus(String paymentStatus);
    
    /**
     * Count invoices by status
     */
    long countByStatus(String status);
    
    /**
     * Count invoices by customer
     */
    long countByCustomerId(Long customerId);
    
    /**
     * Count invoices in date range
     */
    long countByInvoiceDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find unpaid invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.paymentStatus = 'UNPAID' AND i.status = 'POSTED' " +
           "ORDER BY i.dueDate")
    List<Invoice> findUnpaidInvoices();
    
    /**
     * Find partially paid invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.paymentStatus = 'PARTIAL' AND i.status = 'POSTED' " +
           "ORDER BY i.dueDate")
    List<Invoice> findPartiallyPaidInvoices();
    
    /**
     * Find paid invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.paymentStatus = 'PAID' ORDER BY i.invoiceDate DESC")
    List<Invoice> findPaidInvoices();
    
    /**
     * Find overdue invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.paymentStatus = 'OVERDUE' AND i.status = 'POSTED' " +
           "ORDER BY i.dueDate")
    List<Invoice> findOverdueInvoices();
    
    /**
     * Find tax invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.invoiceType = 'TAX_INVOICE' ORDER BY i.invoiceDate DESC")
    List<Invoice> findTaxInvoices();
    
    /**
     * Find credit notes
     */
    @Query("SELECT i FROM Invoice i WHERE i.invoiceType = 'CREDIT_NOTE' ORDER BY i.invoiceDate DESC")
    List<Invoice> findCreditNotes();
    
    /**
     * Find debit notes
     */
    @Query("SELECT i FROM Invoice i WHERE i.invoiceType = 'DEBIT_NOTE' ORDER BY i.invoiceDate DESC")
    List<Invoice> findDebitNotes();
    
    /**
     * Find draft invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.status = 'DRAFT' ORDER BY i.invoiceDate DESC")
    List<Invoice> findDraftInvoices();
    
    /**
     * Find posted invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.status = 'POSTED' ORDER BY i.invoiceDate DESC")
    List<Invoice> findPostedInvoices();
    
    /**
     * Find cancelled invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.status = 'CANCELLED' ORDER BY i.invoiceDate DESC")
    List<Invoice> findCancelledInvoices();
    
    /**
     * Find invoices due for payment (due date approaching)
     */
    @Query("SELECT i FROM Invoice i WHERE i.dueDate BETWEEN CURRENT_DATE AND :futureDate " +
           "AND i.paymentStatus IN ('UNPAID', 'PARTIAL') AND i.status = 'POSTED' " +
           "ORDER BY i.dueDate")
    List<Invoice> findInvoicesDueForPayment(@Param("futureDate") LocalDate futureDate);
    
    /**
     * Find invoices past due date
     */
    @Query("SELECT i FROM Invoice i WHERE i.dueDate < CURRENT_DATE " +
           "AND i.paymentStatus IN ('UNPAID', 'PARTIAL') AND i.status = 'POSTED' " +
           "ORDER BY i.dueDate")
    List<Invoice> findInvoicesPastDue();
    
    /**
     * Get total outstanding amount for a customer
     */
    @Query("SELECT SUM(i.totalAmount - i.paidAmount) FROM Invoice i " +
           "WHERE i.customer.id = :customerId AND i.paymentStatus IN ('UNPAID', 'PARTIAL') " +
           "AND i.status = 'POSTED'")
    BigDecimal getTotalOutstandingByCustomer(@Param("customerId") Long customerId);
    
    /**
     * Get total invoice amount for a customer
     */
    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.customer.id = :customerId " +
           "AND i.status = 'POSTED'")
    BigDecimal getTotalInvoiceAmountByCustomer(@Param("customerId") Long customerId);
    
    /**
     * Get total paid amount for a customer
     */
    @Query("SELECT SUM(i.paidAmount) FROM Invoice i WHERE i.customer.id = :customerId " +
           "AND i.status = 'POSTED'")
    BigDecimal getTotalPaidAmountByCustomer(@Param("customerId") Long customerId);
    
    /**
     * Get invoice statistics
     */
    @Query("SELECT " +
           "COUNT(i) as totalInvoices, " +
           "SUM(CASE WHEN i.paymentStatus = 'UNPAID' THEN 1 ELSE 0 END) as unpaidInvoices, " +
           "SUM(CASE WHEN i.paymentStatus = 'PARTIAL' THEN 1 ELSE 0 END) as partialInvoices, " +
           "SUM(CASE WHEN i.paymentStatus = 'PAID' THEN 1 ELSE 0 END) as paidInvoices, " +
           "SUM(CASE WHEN i.paymentStatus = 'OVERDUE' THEN 1 ELSE 0 END) as overdueInvoices, " +
           "SUM(i.totalAmount) as totalInvoiceValue, " +
           "SUM(i.totalAmount - i.paidAmount) as totalOutstanding " +
           "FROM Invoice i WHERE i.status = 'POSTED'")
    Object getInvoiceStatistics();
    
    /**
     * Get invoices grouped by payment status
     */
    @Query("SELECT i.paymentStatus, COUNT(i) as invoiceCount, " +
           "SUM(i.totalAmount) as totalAmount, SUM(i.totalAmount - i.paidAmount) as totalOutstanding " +
           "FROM Invoice i WHERE i.status = 'POSTED' " +
           "GROUP BY i.paymentStatus ORDER BY invoiceCount DESC")
    List<Object[]> getInvoicesByPaymentStatus();
    
    /**
     * Get invoices grouped by customer
     */
    @Query("SELECT i.customer.id, COUNT(i) as invoiceCount, " +
           "SUM(i.totalAmount) as totalAmount, SUM(i.totalAmount - i.paidAmount) as totalOutstanding " +
           "FROM Invoice i WHERE i.status = 'POSTED' " +
           "GROUP BY i.customer.id ORDER BY totalAmount DESC")
    List<Object[]> getInvoicesByCustomer();
    
    /**
     * Get daily invoice summary
     */
    @Query("SELECT i.invoiceDate, COUNT(i) as invoiceCount, SUM(i.totalAmount) as totalAmount " +
           "FROM Invoice i WHERE i.invoiceDate BETWEEN :startDate AND :endDate AND i.status = 'POSTED' " +
           "GROUP BY i.invoiceDate ORDER BY i.invoiceDate DESC")
    List<Object[]> getDailyInvoiceSummary(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find today's invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.invoiceDate = CURRENT_DATE ORDER BY i.createdAt DESC")
    List<Invoice> findTodayInvoices();
    
    /**
     * Find invoices by customer and payment status
     */
    List<Invoice> findByCustomerIdAndPaymentStatus(Long customerId, String paymentStatus);
    
    /**
     * Find invoices by customer and status
     */
    List<Invoice> findByCustomerIdAndStatus(Long customerId, String status);
    
    /**
     * Find all invoices ordered by date
     */
    List<Invoice> findAllByOrderByInvoiceDateDescCreatedAtDesc();

    @Query("SELECT i FROM Invoice i WHERE i.dispatch.id = :dispatchId")
    List<Invoice> findByDispatchNoteId(Long dispatchId);

    @Query("SELECT i FROM Invoice i WHERE i.dueDate BETWEEN :now AND :dueDate " +
           "AND i.paymentStatus IN ('UNPAID', 'PARTIAL') AND i.status = 'POSTED' " +
           "ORDER BY i.dueDate")

    List<Invoice> findInvoicesDueSoon(LocalDate now, LocalDate dueDate);

    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.customer.id = :customerId")

    Optional<BigDecimal> sumTotalAmountByCustomer(Long customerId);
    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.invoiceDate BETWEEN :startDate AND :endDate")

    Optional<BigDecimal> sumTotalAmountByDateRange(LocalDate startDate, LocalDate endDate);
}
