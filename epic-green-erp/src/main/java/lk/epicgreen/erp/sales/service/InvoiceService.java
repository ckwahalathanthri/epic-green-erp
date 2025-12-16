package lk.epicgreen.erp.sales.service;

import lk.epicgreen.erp.sales.dto.InvoiceRequest;
import lk.epicgreen.erp.sales.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Invoice Service Interface
 * Service for invoice operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface InvoiceService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    Invoice createInvoice(InvoiceRequest request);
    Invoice updateInvoice(Long id, InvoiceRequest request);
    void deleteInvoice(Long id);
    Invoice getInvoiceById(Long id);
    Invoice getInvoiceByNumber(String invoiceNumber);
    List<Invoice> getAllInvoices();
    Page<Invoice> getAllInvoices(Pageable pageable);
    Page<Invoice> searchInvoices(String keyword, Pageable pageable);
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    Invoice approveInvoice(Long id, Long approvedByUserId, String approvalNotes);
    Invoice sendInvoice(Long id);
    Invoice markAsPaid(Long id, Double paidAmount);
    Invoice recordPartialPayment(Long id, Double paidAmount);
    Invoice cancelInvoice(Long id, String cancellationReason);
    
    // ===================================================================
    // PAYMENT OPERATIONS
    // ===================================================================
    
    Invoice updatePaymentStatus(Long id, String paymentStatus);
    Invoice calculateBalance(Long id);
    Double getOutstandingBalance(Long customerId);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    List<Invoice> getDraftInvoices();
    List<Invoice> getPendingInvoices();
    List<Invoice> getApprovedInvoices();
    List<Invoice> getSentInvoices();
    List<Invoice> getPaidInvoices();
    List<Invoice> getCancelledInvoices();
    List<Invoice> getUnpaidInvoices();
    List<Invoice> getPartiallyPaidInvoices();
    List<Invoice> getOverdueInvoices();
    List<Invoice> getInvoicesPendingApproval();
    List<Invoice> getInvoicesDueSoon(int days);
    List<Invoice> getInvoicesByCustomer(Long customerId);
    Page<Invoice> getInvoicesByCustomer(Long customerId, Pageable pageable);
    List<Invoice> getCustomerOutstandingInvoices(Long customerId);
    List<Invoice> getInvoicesBySalesRep(Long salesRepId);
    List<Invoice> getInvoicesByDateRange(LocalDate startDate, LocalDate endDate);
    List<Invoice> getRecentInvoices(int limit);
    List<Invoice> getCustomerRecentInvoices(Long customerId, int limit);
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    boolean validateInvoice(Invoice invoice);
    boolean canApproveInvoice(Long invoiceId);
    boolean canCancelInvoice(Long invoiceId);
    boolean canRecordPayment(Long invoiceId);
    
    // ===================================================================
    // CALCULATIONS
    // ===================================================================
    
    void calculateInvoiceTotals(Long invoiceId);
    Double calculateSubtotal(Long invoiceId);
    Double calculateTotalTax(Long invoiceId);
    Double calculateTotalDiscount(Long invoiceId);
    void updateBalanceAmount(Long invoiceId);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<Invoice> createBulkInvoices(List<InvoiceRequest> requests);
    int approveBulkInvoices(List<Long> invoiceIds, Long approvedByUserId);
    int deleteBulkInvoices(List<Long> invoiceIds);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    Map<String, Object> getInvoiceStatistics();
    List<Map<String, Object>> getInvoiceTypeDistribution();
    List<Map<String, Object>> getStatusDistribution();
    List<Map<String, Object>> getPaymentStatusDistribution();
    List<Map<String, Object>> getMonthlyInvoiceCount(LocalDate startDate, LocalDate endDate);
    List<Map<String, Object>> getTotalInvoiceValueByCustomer();
    List<Map<String, Object>> getTotalOutstandingByCustomer();
    Double getTotalInvoiceValue();
    Double getTotalPaidAmount();
    Double getTotalOutstandingAmount();
    Double getTotalOverdueAmount();
    Double getAverageInvoiceValue();
    Double getCollectionRate();
    Map<String, Object> getDashboardStatistics();
}
