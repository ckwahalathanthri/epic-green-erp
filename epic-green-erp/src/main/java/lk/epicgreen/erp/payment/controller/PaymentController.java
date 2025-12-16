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
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<Payment>> createPayment(@Valid @RequestBody PaymentRequest request) {
        Payment created = paymentService.createPayment(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Payment created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Payment>> updatePayment(
        @PathVariable Long id,
        @Valid @RequestBody PaymentRequest request
    ) {
        Payment updated = paymentService.updatePayment(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Payment updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Payment deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER', 'USER')")
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
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER', 'USER')")
    public ResponseEntity<ApiResponse<Page<Payment>>> searchPayments(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<Payment> payments = paymentService.searchPayments(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(payments, "Search results retrieved"));
    }
    
    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<Payment>> completePayment(@PathVariable Long id) {
        Payment completed = paymentService.completePayment(id);
        return ResponseEntity.ok(ApiResponse.success(completed, "Payment completed"));
    }
    
    @PostMapping("/{id}/clear")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Payment>> clearPayment(@PathVariable Long id) {
        Payment cleared = paymentService.clearPayment(id);
        return ResponseEntity.ok(ApiResponse.success(cleared, "Payment cleared"));
    }
    
    @PostMapping("/{id}/fail")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Payment>> failPayment(
        @PathVariable Long id,
        @RequestParam String failureReason
    ) {
        Payment failed = paymentService.failPayment(id, failureReason);
        return ResponseEntity.ok(ApiResponse.success(failed, "Payment marked as failed"));
    }
    
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Payment>> cancelPayment(
        @PathVariable Long id,
        @RequestParam String cancellationReason
    ) {
        Payment cancelled = paymentService.cancelPayment(id, cancellationReason);
        return ResponseEntity.ok(ApiResponse.success(cancelled, "Payment cancelled"));
    }
    
    @PostMapping("/{id}/reconcile")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Payment>> reconcilePayment(
        @PathVariable Long id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reconciliationDate
    ) {
        Payment reconciled = paymentService.reconcilePayment(id, reconciliationDate);
        return ResponseEntity.ok(ApiResponse.success(reconciled, "Payment reconciled"));
    }
    
    @PostMapping("/{paymentId}/allocate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<PaymentAllocation>> allocatePayment(
        @PathVariable Long paymentId,
        @RequestParam Long invoiceId,
        @RequestParam Double amount
    ) {
        PaymentAllocation allocation = paymentService.allocatePayment(paymentId, invoiceId, amount);
        return ResponseEntity.ok(ApiResponse.success(allocation, "Payment allocated"));
    }
    
    @PostMapping("/{paymentId}/allocate-multiple")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<PaymentAllocation>>> allocatePaymentToMultipleInvoices(
        @PathVariable Long paymentId,
        @RequestBody List<Map<String, Object>> allocations
    ) {
        List<PaymentAllocation> result = paymentService.allocatePaymentToMultipleInvoices(paymentId, allocations);
        return ResponseEntity.ok(ApiResponse.success(result, "Payment allocated to multiple invoices"));
    }
    
    @PostMapping("/allocations/{allocationId}/reverse")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<PaymentAllocation>> reverseAllocation(
        @PathVariable Long allocationId,
        @RequestParam String reason
    ) {
        PaymentAllocation reversed = paymentService.reverseAllocation(allocationId, reason);
        return ResponseEntity.ok(ApiResponse.success(reversed, "Allocation reversed"));
    }
    
    @GetMapping("/{paymentId}/allocations")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<List<PaymentAllocation>>> getPaymentAllocations(@PathVariable Long paymentId) {
        List<PaymentAllocation> allocations = paymentService.getPaymentAllocations(paymentId);
        return ResponseEntity.ok(ApiResponse.success(allocations, "Payment allocations retrieved"));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<List<Payment>>> getPendingPayments() {
        List<Payment> payments = paymentService.getPendingPayments();
        return ResponseEntity.ok(ApiResponse.success(payments, "Pending payments retrieved"));
    }
    
    @GetMapping("/completed")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Payment>>> getCompletedPayments() {
        List<Payment> payments = paymentService.getCompletedPayments();
        return ResponseEntity.ok(ApiResponse.success(payments, "Completed payments retrieved"));
    }
    
    @GetMapping("/unallocated")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Payment>>> getUnallocatedPayments() {
        List<Payment> payments = paymentService.getUnallocatedPayments();
        return ResponseEntity.ok(ApiResponse.success(payments, "Unallocated payments retrieved"));
    }
    
    @GetMapping("/unreconciled")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Payment>>> getUnreconciledPayments() {
        List<Payment> payments = paymentService.getUnreconciledPayments();
        return ResponseEntity.ok(ApiResponse.success(payments, "Unreconciled payments retrieved"));
    }
    
    @GetMapping("/cash")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Payment>>> getCashPayments() {
        List<Payment> payments = paymentService.getCashPayments();
        return ResponseEntity.ok(ApiResponse.success(payments, "Cash payments retrieved"));
    }
    
    @GetMapping("/cheque")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Payment>>> getChequePayments() {
        List<Payment> payments = paymentService.getChequePayments();
        return ResponseEntity.ok(ApiResponse.success(payments, "Cheque payments retrieved"));
    }
    
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Page<Payment>>> getPaymentsByCustomer(
        @PathVariable Long customerId,
        Pageable pageable
    ) {
        Page<Payment> payments = paymentService.getPaymentsByCustomer(customerId, pageable);
        return ResponseEntity.ok(ApiResponse.success(payments, "Customer payments retrieved"));
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Payment>>> getPaymentsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<Payment> payments = paymentService.getPaymentsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(payments, "Date range payments retrieved"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<List<Payment>>> getRecentPayments(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<Payment> payments = paymentService.getRecentPayments(limit);
        return ResponseEntity.ok(ApiResponse.success(payments, "Recent payments retrieved"));
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = paymentService.getPaymentStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = paymentService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
}
