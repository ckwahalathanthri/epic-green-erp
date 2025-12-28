package lk.epicgreen.erp.sales.service;

import lk.epicgreen.erp.sales.dto.request.InvoiceRequest;
import lk.epicgreen.erp.sales.dto.response.InvoiceResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for Invoice entity business logic
 * 
 * Invoice Status Workflow:
 * DRAFT → POSTED
 * Can be CANCELLED from DRAFT only
 * 
 * Payment Status:
 * UNPAID → PARTIAL → PAID
 * OVERDUE (auto-calculated based on due_date)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface InvoiceService {

    /**
     * Create a new Invoice
     */
    InvoiceResponse createInvoice(InvoiceRequest request);

    /**
     * Update an existing Invoice (only in DRAFT status)
     */
    InvoiceResponse updateInvoice(Long id, InvoiceRequest request);

    /**
     * Post Invoice (DRAFT → POSTED)
     */
    void postInvoice(Long id);

    /**
     * Cancel Invoice (only in DRAFT status)
     */
    void cancelInvoice(Long id, String reason);

    /**
     * Record Payment
     */
    void recordPayment(Long id, BigDecimal amount);

    /**
     * Update Payment Status
     */
    void updatePaymentStatus(Long id, String paymentStatus);

    /**
     * Delete Invoice (only in DRAFT status)
     */
    void deleteInvoice(Long id);

    /**
     * Get Invoice by ID
     */
    InvoiceResponse getInvoiceById(Long id);

    /**
     * Get Invoice by number
     */
    InvoiceResponse getInvoiceByNumber(String invoiceNumber);

    /**
     * Get all Invoices (paginated)
     */
    PageResponse<InvoiceResponse> getAllInvoices(Pageable pageable);

    /**
     * Get Invoices by status
     */
    PageResponse<InvoiceResponse> getInvoicesByStatus(String status, Pageable pageable);

    /**
     * Get Invoices by payment status
     */
    PageResponse<InvoiceResponse> getInvoicesByPaymentStatus(String paymentStatus, Pageable pageable);

    /**
     * Get Invoices by customer
     */
    List<InvoiceResponse> getInvoicesByCustomer(Long customerId);

    /**
     * Get Invoices by sales order
     */
    List<InvoiceResponse> getInvoicesByOrder(Long orderId);

    /**
     * Get Invoices by dispatch note
     */
    List<InvoiceResponse> getInvoicesByDispatch(Long dispatchId);

    /**
     * Get Invoices by date range
     */
    List<InvoiceResponse> getInvoicesByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Get Invoices by invoice type
     */
    PageResponse<InvoiceResponse> getInvoicesByType(String invoiceType, Pageable pageable);

    /**
     * Get Unpaid Invoices
     */
    List<InvoiceResponse> getUnpaidInvoices();

    /**
     * Get Overdue Invoices
     */
    List<InvoiceResponse> getOverdueInvoices();

    /**
     * Get Invoices due soon (due within specified days)
     */
    List<InvoiceResponse> getInvoicesDueSoon(Integer daysAhead);

    /**
     * Get Partially Paid Invoices
     */
    List<InvoiceResponse> getPartiallyPaidInvoices();

    /**
     * Search Invoices
     */
    PageResponse<InvoiceResponse> searchInvoices(String keyword, Pageable pageable);

    /**
     * Get total invoice amount for a customer
     */
    BigDecimal getTotalInvoiceAmountByCustomer(Long customerId);

    /**
     * Get total outstanding amount for a customer
     */
    BigDecimal getTotalOutstandingByCustomer(Long customerId);

    /**
     * Get total invoice amount for a date range
     */
    BigDecimal getTotalInvoiceAmountByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Check if can delete
     */
    boolean canDelete(Long id);

    /**
     * Check if can update
     */
    boolean canUpdate(Long id);
}
