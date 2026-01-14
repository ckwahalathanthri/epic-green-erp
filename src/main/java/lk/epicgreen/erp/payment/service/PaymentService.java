package lk.epicgreen.erp.payment.service;

import lk.epicgreen.erp.payment.dto.request.PaymentRequest;
import lk.epicgreen.erp.payment.dto.request.PaymentAllocationRequest;
import lk.epicgreen.erp.payment.dto.response.PaymentResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import lk.epicgreen.erp.payment.entity.Payment;
import lk.epicgreen.erp.payment.entity.PaymentAllocation;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Service interface for Payment entity business logic
 * 
 * Payment Status Workflow:
 * DRAFT → PENDING → CLEARED
 * Can be BOUNCED (for cheques) or CANCELLED
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface PaymentService {

    /**
     * Create a new Payment
     */
    PaymentResponse createPayment(PaymentRequest request);

    /**
     * Update an existing Payment (only in DRAFT status)
     */
    PaymentResponse updatePayment(Long id, PaymentRequest request);

    /**
     * Submit for Approval (DRAFT → PENDING)
     */
    void submitForApproval(Long id);

    /**
     * Approve Payment (PENDING → CLEARED)
     */
    void approvePayment(Long id, Long approvedBy);

    /**
     * Mark as Collected
     */
    void markAsCollected(Long id, Long collectedBy, LocalDateTime collectedAt);

    /**
     * Mark as Bounced (for cheque payments)
     */
    void markAsBounced(Long id, String reason);

    /**
     * Cancel Payment (only from DRAFT status)
     */
    Payment cancelPayment(Long id, String reason);

    /**
     * Delete Payment (only in DRAFT status)
     */
    void deletePayment(Long id);

    Payment completePayment(Long id);
    Payment clearPayment(Long id);

    Payment failPayment(Long id,String failureReason);

    Payment reconcilePayment(Long id,LocalDate reconciliationDate);

    PaymentAllocation allocatePayment(Long paymentId,Long invoiceId,double amount);

    List<PaymentAllocation> allocatePaymentToMultipleInvoices(Long paymentId, Map<String,Object> allocations);
    PaymentAllocation reverseAllocation(Long allocationId,String reason);
    List<PaymentAllocation> getPaymentAllocations(Long paymentId);
    List<PaymentAllocation> getInvoiceAllocations(Long invoiceId);

    List<Payment> getCompletedPayments();
    List<Payment> getClearedPayments();

    List<Payment> getFailedPayments();

    List<Payment> getUnreconciledPayments();

    List<Payment> getCashPayments();

    List<Payment> getChequePayments();

    List<Payment> getPaymentsByBankAccount(Long bankAccountId);

    List<Payment> getOverpayments();

    List<Payment> getPartialPayments();

    List<Payment> getBankTransferPayments();

    Double getTotalAllocatedAmount(Long paymentId);

    Double getUnallocatedAmount(Long paymentId);
    List<Payment> getPendingPayments();
    /**
     * Allocate payment to invoice (bill-to-bill settlement)
     */
    void allocateToInvoice(Long paymentId, PaymentAllocationRequest allocationRequest);

    /**
     * Remove allocation from invoice
     */
    void removeAllocation(Long paymentId, Long allocationId);

    /**
     * Get Payment by ID
     */
    PaymentResponse getPaymentById(Long id);

    /**
     * Get Payment by number
     */
    PaymentResponse getPaymentByNumber(String paymentNumber);

    /**
     * Get all Payments (paginated)
     */
    PageResponse<PaymentResponse> getAllPayments(Pageable pageable);

    /**
     * Get Payments by status
     */
    PageResponse<PaymentResponse> getPaymentsByStatus(String status, Pageable pageable);

    /**
     * Get Payments by customer
     */
    List<PaymentResponse> getPaymentsByCustomer(Long customerId);

    /**
     * Get Payments by payment mode
     */
    PageResponse<PaymentResponse> getPaymentsByPaymentMode(String paymentMode, Pageable pageable);

    /**
     * Get Payments by date range
     */
    List<PaymentResponse> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Get pending approval payments
     */
    List<PaymentResponse> getPendingApprovalPayments();

    /**
     * Get unallocated payments (payments with unallocated_amount > 0)
     */
    List<PaymentResponse> getUnallocatedPayments();

    /**
     * Get Payments by collected by
     */
    List<PaymentResponse> getPaymentsByCollectedBy(Long collectedBy);

    /**
     * Search Payments
     */
    PageResponse<PaymentResponse> searchPayments(String keyword, Pageable pageable);

    /**
     * Get total payments amount for a customer
     */
    BigDecimal getTotalPaymentsByCustomer(Long customerId);

    /**
     * Get total unallocated amount for a customer
     */
    BigDecimal getTotalUnallocatedByCustomer(Long customerId);

    /**
     * Get total payments for a date range
     */
    BigDecimal getTotalPaymentsByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Check if can delete
     */
    boolean canDelete(Long id);

    /**
     * Check if can update
     */
    boolean canUpdate(Long id);

    boolean canCompletePayment(Long id);

    boolean canAllocatePayment(Long id);

    boolean canReverseAllocation(Long allocationId);

    double calculateRemainingAmount(Long id);

//    List<Payment> createBulkPayments(PaymentRequest requests);

    int completeBulkPayments(List<Long> paymentIds);

    int deleteBulkPayments(List<Long> paymentIds);

    int  reconcileBulkPayments(List<Long> paymentIds,LocalDate reconciliationDate);

    Map<String,Object> getPaymentStatistics();

    List<Map<String,Object>> getPaymentTypeDistribution();

    Map<String,Object> getDashboardStatistics();

    double getTotalUnallocatedAmount();

    double getAveragePaymentAmount();

    double getTotalPaymentAmount();

    void calculatePaymentAllocations(Long id);
double calculateTotalAllocated(Long id);
    List<Payment> getRecentPayments();
    List<Payment> getCustomerRecentPayments(Long customerId, int limit);
}
