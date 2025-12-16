package lk.epicgreen.erp.payment.service;

import lk.epicgreen.erp.payment.dto.PaymentRequest;
import lk.epicgreen.erp.payment.entity.Payment;
import lk.epicgreen.erp.payment.entity.PaymentAllocation;
import lk.epicgreen.erp.payment.repository.PaymentAllocationRepository;
import lk.epicgreen.erp.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Payment Service Implementation
 * Implementation of payment service operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentServiceImpl implements PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final PaymentAllocationRepository allocationRepository;
    
    @Override
    public Payment createPayment(PaymentRequest request) {
        log.info("Creating payment for customer: {}", request.getCustomerId());
        
        Payment payment = new Payment();
        payment.setPaymentNumber(generatePaymentNumber());
        payment.setCustomerId(request.getCustomerId());
        payment.setCustomerName(request.getCustomerName());
        payment.setPaymentDate(request.getPaymentDate() != null ? request.getPaymentDate() : LocalDate.now());
        payment.setPaymentType(request.getPaymentType());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaidAmount(request.getPaidAmount() != null ? BigDecimal.valueOf(request.getPaidAmount()) : BigDecimal.ZERO);
        payment.setAllocatedAmount(BigDecimal.ZERO);
        payment.setStatus("PENDING");
        payment.setIsAllocated(false);
        payment.setIsReconciled(false);
        payment.setReferenceType(request.getReferenceType());
        payment.setReferenceId(request.getReferenceId());
        payment.setBankAccountId(request.getBankAccountId());
        payment.setChequeId(request.getChequeId());
        payment.setNotes(request.getNotes());
        
        return paymentRepository.save(payment);
    }
    
    @Override
    public Payment updatePayment(Long id, PaymentRequest request) {
        log.info("Updating payment: {}", id);
        Payment existing = getPaymentById(id);
        
        if ("COMPLETED".equals(existing.getStatus()) || "CLEARED".equals(existing.getStatus())) {
            throw new RuntimeException("Cannot update completed or cleared payment");
        }
        
        existing.setPaymentDate(request.getPaymentDate());
        existing.setPaymentType(request.getPaymentType());
        existing.setPaymentMethod(request.getPaymentMethod());
        existing.setPaidAmount(request.getPaidAmount() != null ? BigDecimal.valueOf(request.getPaidAmount()) : BigDecimal.ZERO);
        existing.setNotes(request.getNotes());
        
        return paymentRepository.save(existing);
    }
    
    @Override
    public void deletePayment(Long id) {
        log.info("Deleting payment: {}", id);
        Payment payment = getPaymentById(id);
        
        if (payment.getIsAllocated()) {
            throw new RuntimeException("Cannot delete allocated payment");
        }
        
        paymentRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentByNumber(String paymentNumber) {
        return paymentRepository.findByPaymentNumber(paymentNumber)
            .orElseThrow(() -> new RuntimeException("Payment not found with number: " + paymentNumber));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Payment> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Payment> searchPayments(String keyword, Pageable pageable) {
        return paymentRepository.searchPayments(keyword, pageable);
    }
    
    @Override
    public Payment completePayment(Long id) {
        log.info("Completing payment: {}", id);
        Payment payment = getPaymentById(id);
        
        payment.setStatus("COMPLETED");
        payment.setReceivedDate(LocalDate.now());
        
        return paymentRepository.save(payment);
    }
    
    @Override
    public Payment clearPayment(Long id) {
        log.info("Clearing payment: {}", id);
        Payment payment = getPaymentById(id);
        
        if (!"COMPLETED".equals(payment.getStatus())) {
            throw new RuntimeException("Only completed payments can be cleared");
        }
        
        payment.setStatus("CLEARED");
        payment.setClearedDate(LocalDate.now());
        
        return paymentRepository.save(payment);
    }
    
    @Override
    public Payment failPayment(Long id, String failureReason) {
        log.info("Failing payment: {}", id);
        Payment payment = getPaymentById(id);
        
        payment.setStatus("FAILED");
        payment.setFailureReason(failureReason);
        payment.setFailedDate(LocalDate.now());
        
        return paymentRepository.save(payment);
    }
    
    @Override
    public Payment cancelPayment(Long id, String cancellationReason) {
        log.info("Cancelling payment: {}", id);
        Payment payment = getPaymentById(id);
        
        if (payment.getIsAllocated()) {
            throw new RuntimeException("Cannot cancel allocated payment");
        }
        
        payment.setStatus("CANCELLED");
        payment.setCancellationReason(cancellationReason);
        payment.setCancelledDate(LocalDate.now());
        
        return paymentRepository.save(payment);
    }
    
    @Override
    public Payment reconcilePayment(Long id, LocalDate reconciliationDate) {
        log.info("Reconciling payment: {}", id);
        Payment payment = getPaymentById(id);
        
        payment.setIsReconciled(true);
        payment.setReconciliationDate(reconciliationDate);
        
        return paymentRepository.save(payment);
    }
    
    @Override
    public PaymentAllocation allocatePayment(Long paymentId, Long invoiceId, Double amount) {
        log.info("Allocating payment {} to invoice {}", paymentId, invoiceId);
        
        Payment payment = getPaymentById(paymentId);
        
        if (!"COMPLETED".equals(payment.getStatus())) {
            throw new RuntimeException("Only completed payments can be allocated");
        }
        
        Double unallocated = getUnallocatedAmount(paymentId);
        if (amount > unallocated) {
            throw new RuntimeException("Allocation amount exceeds unallocated payment amount");
        }
        
        PaymentAllocation allocation = new PaymentAllocation();
        allocation.setPaymentId(paymentId);
        allocation.setInvoiceId(invoiceId);
        allocation.setCustomerId(payment.getCustomerId());
        allocation.setCustomerName(payment.getCustomerName());
        allocation.setAllocationDate(LocalDate.now());
        allocation.setAllocatedAmount(amount != null ? BigDecimal.valueOf(amount) : BigDecimal.ZERO);
        allocation.setAllocationType("INVOICE");
        allocation.setStatus("COMPLETED");
        allocation.setIsReversed(false);
        
        PaymentAllocation saved = allocationRepository.save(allocation);
        
        // Update payment allocation status
        calculatePaymentAllocations(paymentId);
        
        return saved;
    }
    
    @Override
    public List<PaymentAllocation> allocatePaymentToMultipleInvoices(Long paymentId, List<Map<String, Object>> allocations) {
        List<PaymentAllocation> result = new ArrayList<>();
        
        for (Map<String, Object> alloc : allocations) {
            Long invoiceId = ((Number) alloc.get("invoiceId")).longValue();
            Double amount = ((Number) alloc.get("amount")).doubleValue();
            
            PaymentAllocation allocation = allocatePayment(paymentId, invoiceId, amount);
            result.add(allocation);
        }
        
        return result;
    }
    
    @Override
    public PaymentAllocation reverseAllocation(Long allocationId, String reason) {
        log.info("Reversing allocation: {}", allocationId);
        
        PaymentAllocation allocation = allocationRepository.findById(allocationId)
            .orElseThrow(() -> new RuntimeException("Allocation not found"));
        
        allocation.setIsReversed(true);
        allocation.setReversalDate(LocalDate.now());
        allocation.setReversalReason(reason);
        
        PaymentAllocation saved = allocationRepository.save(allocation);
        
        // Recalculate payment allocations
        calculatePaymentAllocations(allocation.getPaymentId());
        
        return saved;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PaymentAllocation> getPaymentAllocations(Long paymentId) {
        return allocationRepository.findByPaymentId(paymentId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PaymentAllocation> getInvoiceAllocations(Long invoiceId) {
        return allocationRepository.findByInvoiceId(invoiceId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalAllocatedAmount(Long paymentId) {
        Double total = allocationRepository.getTotalAllocatedAmountByPayment(paymentId);
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getUnallocatedAmount(Long paymentId) {
        Payment payment = getPaymentById(paymentId);
        Double allocated = getTotalAllocatedAmount(paymentId);
        
        BigDecimal paidAmountBD = payment.getPaidAmount();
        BigDecimal allocatedBD = BigDecimal.valueOf(allocated != null ? allocated : 0.0);
        BigDecimal unallocatedBD = paidAmountBD.subtract(allocatedBD);
        
        return unallocatedBD.doubleValue();
    }
    
    @Override
    public void calculatePaymentAllocations(Long paymentId) {
        Payment payment = getPaymentById(paymentId);
        Double totalAllocated = getTotalAllocatedAmount(paymentId);
        
        payment.setAllocatedAmount(totalAllocated != null ? BigDecimal.valueOf(totalAllocated) : BigDecimal.ZERO);
        
        BigDecimal totalAllocatedBD = BigDecimal.valueOf(totalAllocated != null ? totalAllocated : 0.0);
        BigDecimal paidAmountBD = payment.getPaidAmount();
        payment.setIsAllocated(totalAllocatedBD.compareTo(BigDecimal.ZERO) > 0 && 
                               totalAllocatedBD.compareTo(paidAmountBD) >= 0);
        
        paymentRepository.save(payment);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double calculateTotalAllocated(Long paymentId) {
        return getTotalAllocatedAmount(paymentId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double calculateRemainingAmount(Long paymentId) {
        return getUnallocatedAmount(paymentId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPendingPayments() {
        return paymentRepository.findPendingPayments();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> getCompletedPayments() {
        return paymentRepository.findCompletedPayments();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> getClearedPayments() {
        return paymentRepository.findClearedPayments();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> getFailedPayments() {
        return paymentRepository.findFailedPayments();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> getUnallocatedPayments() {
        return paymentRepository.findUnallocatedPayments();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> getUnreconciledPayments() {
        return paymentRepository.findUnreconciledPayments();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> getCashPayments() {
        return paymentRepository.findCashPayments();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> getChequePayments() {
        return paymentRepository.findChequePayments();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> getBankTransferPayments() {
        return paymentRepository.findBankTransferPayments();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByCustomer(Long customerId) {
        return paymentRepository.findByCustomerId(customerId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Payment> getPaymentsByCustomer(Long customerId, Pageable pageable) {
        return paymentRepository.findByCustomerId(customerId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return paymentRepository.findByPaymentDateBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByBankAccount(Long bankAccountId) {
        return paymentRepository.findByBankAccountId(bankAccountId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> getOverpayments() {
        return paymentRepository.findOverpayments();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPartialPayments() {
        return paymentRepository.findPartialPayments();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> getRecentPayments(int limit) {
        return paymentRepository.findRecentPayments(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> getCustomerRecentPayments(Long customerId, int limit) {
        return paymentRepository.findCustomerRecentPayments(customerId, PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validatePayment(Payment payment) {
        return payment.getCustomerId() != null &&
               payment.getPaymentDate() != null &&
               payment.getPaidAmount() != null &&
               payment.getPaidAmount().compareTo(BigDecimal.ZERO) > 0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canCompletePayment(Long paymentId) {
        Payment payment = getPaymentById(paymentId);
        return "PENDING".equals(payment.getStatus());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canAllocatePayment(Long paymentId) {
        Payment payment = getPaymentById(paymentId);
        return "COMPLETED".equals(payment.getStatus()) && getUnallocatedAmount(paymentId) > 0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canReverseAllocation(Long allocationId) {
        PaymentAllocation allocation = allocationRepository.findById(allocationId)
            .orElseThrow(() -> new RuntimeException("Allocation not found"));
        return !allocation.getIsReversed();
    }
    
    @Override
    public List<Payment> createBulkPayments(List<PaymentRequest> requests) {
        return requests.stream()
            .map(this::createPayment)
            .collect(Collectors.toList());
    }
    
    @Override
    public int completeBulkPayments(List<Long> paymentIds) {
        int count = 0;
        for (Long id : paymentIds) {
            try {
                completePayment(id);
                count++;
            } catch (Exception e) {
                log.error("Error completing payment: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int deleteBulkPayments(List<Long> paymentIds) {
        int count = 0;
        for (Long id : paymentIds) {
            try {
                deletePayment(id);
                count++;
            } catch (Exception e) {
                log.error("Error deleting payment: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int reconcileBulkPayments(List<Long> paymentIds, LocalDate reconciliationDate) {
        int count = 0;
        for (Long id : paymentIds) {
            try {
                reconcilePayment(id, reconciliationDate);
                count++;
            } catch (Exception e) {
                log.error("Error reconciling payment: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getPaymentStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalPayments", paymentRepository.count());
        stats.put("pendingPayments", paymentRepository.countPendingPayments());
        stats.put("unallocatedPayments", paymentRepository.countUnallocatedPayments());
        stats.put("unreconciledPayments", paymentRepository.countUnreconciledPayments());
        stats.put("totalPaymentAmount", getTotalPaymentAmount());
        stats.put("averagePaymentAmount", getAveragePaymentAmount());
        stats.put("totalUnallocatedAmount", getTotalUnallocatedAmount());
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPaymentTypeDistribution() {
        List<Object[]> results = paymentRepository.getPaymentTypeDistribution();
        return convertToMapList(results, "paymentType", "paymentCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPaymentMethodDistribution() {
        List<Object[]> results = paymentRepository.getPaymentMethodDistribution();
        return convertToMapList(results, "paymentMethod", "paymentCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getStatusDistribution() {
        List<Object[]> results = paymentRepository.getStatusDistribution();
        return convertToMapList(results, "status", "paymentCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMonthlyPaymentCount(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = paymentRepository.getMonthlyPaymentCount(startDate, endDate);
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("year", result[0]);
                map.put("month", result[1]);
                map.put("paymentCount", result[2]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTotalPaymentAmountByCustomer() {
        List<Object[]> results = paymentRepository.getTotalPaymentAmountByCustomer();
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("customerId", result[0]);
                map.put("customerName", result[1]);
                map.put("totalAmount", result[2]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTotalPaymentAmountByPaymentMethod() {
        List<Object[]> results = paymentRepository.getTotalPaymentAmountByPaymentMethod();
        return convertToMapList(results, "paymentMethod", "totalAmount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalPaymentAmount() {
        Double total = paymentRepository.getTotalPaymentAmount();
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getAveragePaymentAmount() {
        Double average = paymentRepository.getAveragePaymentAmount();
        return average != null ? average : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalUnallocatedAmount() {
        Double total = paymentRepository.getTotalUnallocatedAmount();
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("statistics", getPaymentStatistics());
        dashboard.put("typeDistribution", getPaymentTypeDistribution());
        dashboard.put("methodDistribution", getPaymentMethodDistribution());
        dashboard.put("statusDistribution", getStatusDistribution());
        
        return dashboard;
    }
    
    private String generatePaymentNumber() {
        return "PAY-" + System.currentTimeMillis();
    }
    
    private List<Map<String, Object>> convertToMapList(List<Object[]> results, String key1, String key2) {
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put(key1, result[0]);
                map.put(key2, result[1]);
                return map;
            })
            .collect(Collectors.toList());
    }
}
