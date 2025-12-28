package lk.epicgreen.erp.payment.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.payment.dto.PaymentRequest;
import lk.epicgreen.erp.payment.entity.Payment;
import lk.epicgreen.erp.payment.entity.PaymentAllocation;
import lk.epicgreen.erp.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Payment Controller
 * REST controller for payment operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class PaymentController {
    
    private final PaymentService paymentService;
    
    // CRUD Operations
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<Payment>> createPayment(@Valid @RequestBody PaymentRequest request) {
        log.info("Creating payment for customer: {}", request.getCustomerId());
        Payment created = paymentService.createPayment(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Payment created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Payment>> updatePayment(@PathVariable Long id, @Valid @RequestBody PaymentRequest request) {
        log.info("Updating payment: {}", id);
        Payment updated = paymentService.updatePayment(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Payment updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deletePayment(@PathVariable Long id) {
        log.info("Deleting payment: {}", id);
        paymentService.deletePayment(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Payment deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Payment>> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(ApiResponse.success(payment, "Payment retrieved successfully"));
    }
    
    @GetMapping("/number/{paymentNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER', 'USER')")
    public ResponseEntity<ApiResponse<Payment>> getPaymentByNumber(@PathVariable String paymentNumber) {
        Payment payment = paymentService.getPaymentByNumber(paymentNumber);
        return ResponseEntity.ok(ApiResponse.success(payment, "Payment retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER', 'USER')")
    public ResponseEntity<ApiResponse<Page<Payment>>> getAllPayments(Pageable pageable) {
        Page<Payment> payments = paymentService.getAllPayments(pageable);
        return ResponseEntity.ok(ApiResponse.success(payments, "Payments retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER', 'USER')")
    public ResponseEntity<ApiResponse<List<Payment>>> getAllPaymentsList() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(ApiResponse.success(payments, "Payments list retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER', 'USER')")
    public ResponseEntity<ApiResponse<Page<Payment>>> searchPayments(@RequestParam String keyword, Pageable pageable) {
        Page<Payment> payments = paymentService.searchPayments(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(payments, "Search results retrieved successfully"));
    }
    
    // Status Operations
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<Payment>> completePayment(@PathVariable Long id) {
        log.info("Completing payment: {}", id);
        Payment completed = paymentService.completePayment(id);
        return ResponseEntity.ok(ApiResponse.success(completed, "Payment completed successfully"));
    }
    
    @PutMapping("/{id}/clear")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Payment>> clearPayment(@PathVariable Long id) {
        log.info("Clearing payment: {}", id);
        Payment cleared = paymentService.clearPayment(id);
        return ResponseEntity.ok(ApiResponse.success(cleared, "Payment cleared successfully"));
    }
    
    @PutMapping("/{id}/fail")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Payment>> failPayment(@PathVariable Long id, @RequestParam String failureReason) {
        log.info("Failing payment: {}", id);
        Payment failed = paymentService.failPayment(id, failureReason);
        return ResponseEntity.ok(ApiResponse.success(failed, "Payment marked as failed"));
    }
    
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Payment>> cancelPayment(@PathVariable Long id, @RequestParam String cancellationReason) {
        log.info("Cancelling payment: {}", id);
        Payment cancelled = paymentService.cancelPayment(id, cancellationReason);
        return ResponseEntity.ok(ApiResponse.success(cancelled, "Payment cancelled successfully"));
    }
    
    @PutMapping("/{id}/reconcile")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Payment>> reconcilePayment(@PathVariable Long id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reconciliationDate) {
        log.info("Reconciling payment: {}", id);
        Payment reconciled = paymentService.reconcilePayment(id, reconciliationDate);
        return ResponseEntity.ok(ApiResponse.success(reconciled, "Payment reconciled successfully"));
    }
    
    // Payment Allocation Operations
    @PostMapping("/{paymentId}/allocate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<PaymentAllocation>> allocatePayment(
        @PathVariable Long paymentId,
        @RequestParam Long invoiceId,
        @RequestParam Double amount
    ) {
        log.info("Allocating payment {} to invoice {}", paymentId, invoiceId);
        PaymentAllocation allocation = paymentService.allocatePayment(paymentId, invoiceId, amount);
        return ResponseEntity.ok(ApiResponse.success(allocation, "Payment allocated successfully"));
    }
    
    @PostMapping("/{paymentId}/allocate-multiple")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<PaymentAllocation>>> allocatePaymentToMultipleInvoices(
        @PathVariable Long paymentId,
        @RequestBody List<Map<String, Object>> allocations
    ) {
        log.info("Allocating payment {} to multiple invoices", paymentId);
        List<PaymentAllocation> result = paymentService.allocatePaymentToMultipleInvoices(paymentId, allocations);
        return ResponseEntity.ok(ApiResponse.success(result, "Payment allocated to multiple invoices successfully"));
    }
    
    @PutMapping("/allocations/{allocationId}/reverse")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<PaymentAllocation>> reverseAllocation(@PathVariable Long allocationId, @RequestParam String reason) {
        log.info("Reversing allocation: {}", allocationId);
        PaymentAllocation reversed = paymentService.reverseAllocation(allocationId, reason);
        return ResponseEntity.ok(ApiResponse.success(reversed, "Allocation reversed successfully"));
    }
    
    @GetMapping("/{paymentId}/allocations")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<List<PaymentAllocation>>> getPaymentAllocations(@PathVariable Long paymentId) {
        List<PaymentAllocation> allocations = paymentService.getPaymentAllocations(paymentId);
        return ResponseEntity.ok(ApiResponse.success(allocations, "Payment allocations retrieved successfully"));
    }
    
    @GetMapping("/invoice/{invoiceId}/allocations")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<List<PaymentAllocation>>> getInvoiceAllocations(@PathVariable Long invoiceId) {
        List<PaymentAllocation> allocations = paymentService.getInvoiceAllocations(invoiceId);
        return ResponseEntity.ok(ApiResponse.success(allocations, "Invoice allocations retrieved successfully"));
    }
    
    @GetMapping("/{paymentId}/total-allocated")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Double>> getTotalAllocatedAmount(@PathVariable Long paymentId) {
        Double total = paymentService.getTotalAllocatedAmount(paymentId);
        return ResponseEntity.ok(ApiResponse.success(total, "Total allocated amount retrieved successfully"));
    }
    
    @GetMapping("/{paymentId}/unallocated-amount")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Double>> getUnallocatedAmount(@PathVariable Long paymentId) {
        Double unallocated = paymentService.getUnallocatedAmount(paymentId);
        return ResponseEntity.ok(ApiResponse.success(unallocated, "Unallocated amount retrieved successfully"));
    }
    
    // Query Operations
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<List<Payment>>> getPendingPayments() {
        List<Payment> payments = paymentService.getPendingPayments();
        return ResponseEntity.ok(ApiResponse.success(payments, "Pending payments retrieved successfully"));
    }
    
    @GetMapping("/completed")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Payment>>> getCompletedPayments() {
        List<Payment> payments = paymentService.getCompletedPayments();
        return ResponseEntity.ok(ApiResponse.success(payments, "Completed payments retrieved successfully"));
    }
    
    @GetMapping("/cleared")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Payment>>> getClearedPayments() {
        List<Payment> payments = paymentService.getClearedPayments();
        return ResponseEntity.ok(ApiResponse.success(payments, "Cleared payments retrieved successfully"));
    }
    
    @GetMapping("/failed")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Payment>>> getFailedPayments() {
        List<Payment> payments = paymentService.getFailedPayments();
        return ResponseEntity.ok(ApiResponse.success(payments, "Failed payments retrieved successfully"));
    }
    
    @GetMapping("/unallocated")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Payment>>> getUnallocatedPayments() {
        List<Payment> payments = paymentService.getUnallocatedPayments();
        return ResponseEntity.ok(ApiResponse.success(payments, "Unallocated payments retrieved successfully"));
    }
    
    @GetMapping("/unreconciled")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Payment>>> getUnreconciledPayments() {
        List<Payment> payments = paymentService.getUnreconciledPayments();
        return ResponseEntity.ok(ApiResponse.success(payments, "Unreconciled payments retrieved successfully"));
    }
    
    @GetMapping("/cash")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Payment>>> getCashPayments() {
        List<Payment> payments = paymentService.getCashPayments();
        return ResponseEntity.ok(ApiResponse.success(payments, "Cash payments retrieved successfully"));
    }
    
    @GetMapping("/cheque")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Payment>>> getChequePayments() {
        List<Payment> payments = paymentService.getChequePayments();
        return ResponseEntity.ok(ApiResponse.success(payments, "Cheque payments retrieved successfully"));
    }
    
    @GetMapping("/bank-transfer")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Payment>>> getBankTransferPayments() {
        List<Payment> payments = paymentService.getBankTransferPayments();
        return ResponseEntity.ok(ApiResponse.success(payments, "Bank transfer payments retrieved successfully"));
    }
    
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Page<Payment>>> getPaymentsByCustomer(@PathVariable Long customerId, Pageable pageable) {
        Page<Payment> payments = paymentService.getPaymentsByCustomer(customerId, pageable);
        return ResponseEntity.ok(ApiResponse.success(payments, "Customer payments retrieved successfully"));
    }
    
    @GetMapping("/customer/{customerId}/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<Payment>>> getPaymentsByCustomerList(@PathVariable Long customerId) {
        List<Payment> payments = paymentService.getPaymentsByCustomer(customerId);
        return ResponseEntity.ok(ApiResponse.success(payments, "Customer payments list retrieved successfully"));
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Payment>>> getPaymentsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<Payment> payments = paymentService.getPaymentsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(payments, "Payments by date range retrieved successfully"));
    }
    
    @GetMapping("/bank-account/{bankAccountId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Payment>>> getPaymentsByBankAccount(@PathVariable Long bankAccountId) {
        List<Payment> payments = paymentService.getPaymentsByBankAccount(bankAccountId);
        return ResponseEntity.ok(ApiResponse.success(payments, "Payments by bank account retrieved successfully"));
    }
    
    @GetMapping("/overpayments")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Payment>>> getOverpayments() {
        List<Payment> payments = paymentService.getOverpayments();
        return ResponseEntity.ok(ApiResponse.success(payments, "Overpayments retrieved successfully"));
    }
    
    @GetMapping("/partial-payments")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Payment>>> getPartialPayments() {
        List<Payment> payments = paymentService.getPartialPayments();
        return ResponseEntity.ok(ApiResponse.success(payments, "Partial payments retrieved successfully"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<List<Payment>>> getRecentPayments(@RequestParam(defaultValue = "10") int limit) {
        List<Payment> payments = paymentService.getRecentPayments(limit);
        return ResponseEntity.ok(ApiResponse.success(payments, "Recent payments retrieved successfully"));
    }
    
    @GetMapping("/customer/{customerId}/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<Payment>>> getCustomerRecentPayments(@PathVariable Long customerId, @RequestParam(defaultValue = "10") int limit) {
        List<Payment> payments = paymentService.getCustomerRecentPayments(customerId, limit);
        return ResponseEntity.ok(ApiResponse.success(payments, "Customer recent payments retrieved successfully"));
    }
    
    // Validation
    @GetMapping("/{id}/can-complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<Boolean>> canCompletePayment(@PathVariable Long id) {
        boolean canComplete = paymentService.canCompletePayment(id);
        return ResponseEntity.ok(ApiResponse.success(canComplete, "Complete check completed"));
    }
    
    @GetMapping("/{id}/can-allocate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Boolean>> canAllocatePayment(@PathVariable Long id) {
        boolean canAllocate = paymentService.canAllocatePayment(id);
        return ResponseEntity.ok(ApiResponse.success(canAllocate, "Allocation check completed"));
    }
    
    @GetMapping("/allocations/{allocationId}/can-reverse")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Boolean>> canReverseAllocation(@PathVariable Long allocationId) {
        boolean canReverse = paymentService.canReverseAllocation(allocationId);
        return ResponseEntity.ok(ApiResponse.success(canReverse, "Reverse check completed"));
    }
    
    // Calculations
    @PutMapping("/{id}/calculate-allocations")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> calculatePaymentAllocations(@PathVariable Long id) {
        log.info("Calculating allocations for payment: {}", id);
        paymentService.calculatePaymentAllocations(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Payment allocations calculated successfully"));
    }
    
    @GetMapping("/{id}/total-allocated-calculation")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Double>> calculateTotalAllocated(@PathVariable Long id) {
        Double total = paymentService.calculateTotalAllocated(id);
        return ResponseEntity.ok(ApiResponse.success(total, "Total allocated calculated successfully"));
    }
    
    @GetMapping("/{id}/remaining-amount")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Double>> calculateRemainingAmount(@PathVariable Long id) {
        Double remaining = paymentService.calculateRemainingAmount(id);
        return ResponseEntity.ok(ApiResponse.success(remaining, "Remaining amount calculated successfully"));
    }
    
    // Batch Operations
    @PostMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Payment>>> createBulkPayments(@Valid @RequestBody List<PaymentRequest> requests) {
        log.info("Creating {} payments in bulk", requests.size());
        List<Payment> payments = paymentService.createBulkPayments(requests);
        return ResponseEntity.ok(ApiResponse.success(payments, payments.size() + " payments created successfully"));
    }
    
    @PutMapping("/bulk/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Integer>> completeBulkPayments(@RequestBody List<Long> paymentIds) {
        log.info("Completing {} payments in bulk", paymentIds.size());
        int count = paymentService.completeBulkPayments(paymentIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " payments completed successfully"));
    }
    
    @DeleteMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> deleteBulkPayments(@RequestBody List<Long> paymentIds) {
        log.info("Deleting {} payments in bulk", paymentIds.size());
        int count = paymentService.deleteBulkPayments(paymentIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " payments deleted successfully"));
    }
    
    @PutMapping("/bulk/reconcile")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Integer>> reconcileBulkPayments(
        @RequestBody List<Long> paymentIds,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reconciliationDate
    ) {
        log.info("Reconciling {} payments in bulk", paymentIds.size());
        int count = paymentService.reconcileBulkPayments(paymentIds, reconciliationDate);
        return ResponseEntity.ok(ApiResponse.success(count, count + " payments reconciled successfully"));
    }
    
    // Statistics
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPaymentStatistics() {
        Map<String, Object> statistics = paymentService.getPaymentStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Payment statistics retrieved successfully"));
    }
    
    @GetMapping("/statistics/type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getPaymentTypeDistribution() {
        List<Map<String, Object>> distribution = paymentService.getPaymentTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Payment type distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/method-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getPaymentMethodDistribution() {
        List<Map<String, Object>> distribution = paymentService.getPaymentMethodDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Payment method distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/status-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getStatusDistribution() {
        List<Map<String, Object>> distribution = paymentService.getStatusDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Status distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/monthly")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMonthlyPaymentCount(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<Map<String, Object>> count = paymentService.getMonthlyPaymentCount(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(count, "Monthly payment count retrieved successfully"));
    }
    
    @GetMapping("/statistics/by-customer")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTotalPaymentAmountByCustomer() {
        List<Map<String, Object>> stats = paymentService.getTotalPaymentAmountByCustomer();
        return ResponseEntity.ok(ApiResponse.success(stats, "Payment amount by customer retrieved successfully"));
    }
    
    @GetMapping("/statistics/by-payment-method")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTotalPaymentAmountByPaymentMethod() {
        List<Map<String, Object>> stats = paymentService.getTotalPaymentAmountByPaymentMethod();
        return ResponseEntity.ok(ApiResponse.success(stats, "Payment amount by method retrieved successfully"));
    }
    
    @GetMapping("/statistics/total-amount")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getTotalPaymentAmount() {
        Double total = paymentService.getTotalPaymentAmount();
        return ResponseEntity.ok(ApiResponse.success(total, "Total payment amount retrieved successfully"));
    }
    
    @GetMapping("/statistics/average-amount")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getAveragePaymentAmount() {
        Double average = paymentService.getAveragePaymentAmount();
        return ResponseEntity.ok(ApiResponse.success(average, "Average payment amount retrieved successfully"));
    }
    
    @GetMapping("/statistics/total-unallocated")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getTotalUnallocatedAmount() {
        Double total = paymentService.getTotalUnallocatedAmount();
        return ResponseEntity.ok(ApiResponse.success(total, "Total unallocated amount retrieved successfully"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = paymentService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved successfully"));
    }
}
