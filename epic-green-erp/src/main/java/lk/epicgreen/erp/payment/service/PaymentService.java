package lk.epicgreen.erp.payment.service;

import lk.epicgreen.erp.payment.dto.PaymentRequest;
import lk.epicgreen.erp.payment.entity.Payment;
import lk.epicgreen.erp.payment.entity.PaymentAllocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Payment Service Interface
 * Service for payment operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface PaymentService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    Payment createPayment(PaymentRequest request);
    Payment updatePayment(Long id, PaymentRequest request);
    void deletePayment(Long id);
    Payment getPaymentById(Long id);
    Payment getPaymentByNumber(String paymentNumber);
    List<Payment> getAllPayments();
    Page<Payment> getAllPayments(Pageable pageable);
    Page<Payment> searchPayments(String keyword, Pageable pageable);
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    Payment completePayment(Long id);
    Payment clearPayment(Long id);
    Payment failPayment(Long id, String failureReason);
    Payment cancelPayment(Long id, String cancellationReason);
    Payment reconcilePayment(Long id, LocalDate reconciliationDate);
    
    // ===================================================================
    // PAYMENT ALLOCATION
    // ===================================================================
    
    PaymentAllocation allocatePayment(Long paymentId, Long invoiceId, Double amount);
    List<PaymentAllocation> allocatePaymentToMultipleInvoices(Long paymentId, List<Map<String, Object>> allocations);
    PaymentAllocation reverseAllocation(Long allocationId, String reason);
    List<PaymentAllocation> getPaymentAllocations(Long paymentId);
    List<PaymentAllocation> getInvoiceAllocations(Long invoiceId);
    Double getTotalAllocatedAmount(Long paymentId);
    Double getUnallocatedAmount(Long paymentId);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    List<Payment> getPendingPayments();
    List<Payment> getCompletedPayments();
    List<Payment> getClearedPayments();
    List<Payment> getFailedPayments();
    List<Payment> getUnallocatedPayments();
    List<Payment> getUnreconciledPayments();
    List<Payment> getCashPayments();
    List<Payment> getChequePayments();
    List<Payment> getBankTransferPayments();
    List<Payment> getPaymentsByCustomer(Long customerId);
    Page<Payment> getPaymentsByCustomer(Long customerId, Pageable pageable);
    List<Payment> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate);
    List<Payment> getPaymentsByBankAccount(Long bankAccountId);
    List<Payment> getOverpayments();
    List<Payment> getPartialPayments();
    List<Payment> getRecentPayments(int limit);
    List<Payment> getCustomerRecentPayments(Long customerId, int limit);
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    boolean validatePayment(Payment payment);
    boolean canCompletePayment(Long paymentId);
    boolean canAllocatePayment(Long paymentId);
    boolean canReverseAllocation(Long allocationId);
    
    // ===================================================================
    // CALCULATIONS
    // ===================================================================
    
    void calculatePaymentAllocations(Long paymentId);
    Double calculateTotalAllocated(Long paymentId);
    Double calculateRemainingAmount(Long paymentId);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<Payment> createBulkPayments(List<PaymentRequest> requests);
    int completeBulkPayments(List<Long> paymentIds);
    int deleteBulkPayments(List<Long> paymentIds);
    int reconcileBulkPayments(List<Long> paymentIds, LocalDate reconciliationDate);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    Map<String, Object> getPaymentStatistics();
    List<Map<String, Object>> getPaymentTypeDistribution();
    List<Map<String, Object>> getPaymentMethodDistribution();
    List<Map<String, Object>> getStatusDistribution();
    List<Map<String, Object>> getMonthlyPaymentCount(LocalDate startDate, LocalDate endDate);
    List<Map<String, Object>> getTotalPaymentAmountByCustomer();
    List<Map<String, Object>> getTotalPaymentAmountByPaymentMethod();
    Double getTotalPaymentAmount();
    Double getAveragePaymentAmount();
    Double getTotalUnallocatedAmount();
    Map<String, Object> getDashboardStatistics();
}
