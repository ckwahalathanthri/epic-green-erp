package lk.epicgreen.erp.payment.service.impl;

import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.payment.dto.request.PaymentRequest;
import lk.epicgreen.erp.payment.dto.request.PaymentAllocationRequest;
import lk.epicgreen.erp.payment.dto.response.PaymentResponse;
import lk.epicgreen.erp.payment.entity.Payment;
import lk.epicgreen.erp.payment.entity.PaymentAllocation;
import lk.epicgreen.erp.payment.mapper.PaymentMapper;
import lk.epicgreen.erp.payment.mapper.PaymentAllocationMapper;
import lk.epicgreen.erp.payment.repository.PaymentRepository;
import lk.epicgreen.erp.payment.repository.PaymentAllocationRepository;
import lk.epicgreen.erp.payment.service.PaymentService;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.repository.CustomerRepository;
import lk.epicgreen.erp.sales.entity.Invoice;
import lk.epicgreen.erp.sales.repository.InvoiceRepository;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lk.epicgreen.erp.common.exception.InvalidOperationException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of PaymentService interface
 * 
 * Payment Status Workflow:
 * DRAFT → PENDING → CLEARED
 * Can be BOUNCED (for cheques) or CANCELLED
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentAllocationRepository paymentAllocationRepository;
    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentAllocationMapper paymentAllocationMapper;


    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        log.info("Creating new Payment: {}", request.getPaymentNumber());

        // Validate unique constraint
        validateUniquePaymentNumber(request.getPaymentNumber(), null);

        // Verify customer exists
        Customer customer = findCustomerById(request.getCustomerId());

        // Create payment entity
        Payment payment = paymentMapper.toEntity(request);
        payment.setCustomer(customer);

        // Create payment allocations if provided
        if (request.getAllocations() != null && !request.getAllocations().isEmpty()) {
            List<PaymentAllocation> allocations = new ArrayList<>();
            BigDecimal totalAllocated = BigDecimal.ZERO;

            for (PaymentAllocationRequest allocRequest : request.getAllocations()) {
                Invoice invoice = findInvoiceById(allocRequest.getInvoiceId());
                
                // Validate allocation amount
                validateAllocationAmount(invoice, allocRequest.getAllocatedAmount());

                PaymentAllocation allocation = paymentAllocationMapper.toEntity(allocRequest);
                allocation.setPayment(payment);
                allocation.setInvoice(invoice);
                allocations.add(allocation);

                totalAllocated = totalAllocated.add(allocRequest.getAllocatedAmount());
            }

            // Validate total allocations don't exceed payment amount
            if (totalAllocated.compareTo(payment.getTotalAmount()) > 0) {
                throw new InvalidOperationException(
                    "Total allocations (" + totalAllocated + 
                    ") cannot exceed payment amount (" + payment.getTotalAmount() + ")");
            }

            payment.setAllocations(allocations);
            payment.setAllocatedAmount(totalAllocated);
        }

        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment created successfully: {}", savedPayment.getPaymentNumber());

        return paymentMapper.toResponse(savedPayment);
    }

    @Override
    @Transactional
    public PaymentResponse updatePayment(Long id, PaymentRequest request) {
        log.info("Updating Payment: {}", id);

        Payment payment = findPaymentById(id);

        // Can only update DRAFT payments
        if (!canUpdate(id)) {
            throw new InvalidOperationException(
                "Cannot update Payment. Current status: " + payment.getStatus() + 
                ". Only DRAFT payments can be updated.");
        }

        // Validate unique constraint if payment number changed
        if (!payment.getPaymentNumber().equals(request.getPaymentNumber())) {
            validateUniquePaymentNumber(request.getPaymentNumber(), id);
        }

        // Update basic fields
        paymentMapper.updateEntityFromRequest(request, payment);

        // Update customer if changed
        if (!payment.getCustomer().getId().equals(request.getCustomerId())) {
            Customer customer = findCustomerById(request.getCustomerId());
            payment.setCustomer(customer);
        }

        // Delete existing allocations and create new ones
        if (payment.getAllocations() != null) {
            paymentAllocationRepository.deleteAll(payment.getAllocations());
            payment.getAllocations().clear();
        }

        if (request.getAllocations() != null && !request.getAllocations().isEmpty()) {
            List<PaymentAllocation> newAllocations = new ArrayList<>();
            BigDecimal totalAllocated = BigDecimal.ZERO;

            for (PaymentAllocationRequest allocRequest : request.getAllocations()) {
                Invoice invoice = findInvoiceById(allocRequest.getInvoiceId());
                validateAllocationAmount(invoice, allocRequest.getAllocatedAmount());

                PaymentAllocation allocation = paymentAllocationMapper.toEntity(allocRequest);
                allocation.setPayment(payment);
                allocation.setInvoice(invoice);
                newAllocations.add(allocation);

                totalAllocated = totalAllocated.add(allocRequest.getAllocatedAmount());
            }

            if (totalAllocated.compareTo(payment.getTotalAmount()) > 0) {
                throw new InvalidOperationException(
                    "Total allocations (" + totalAllocated + 
                    ") cannot exceed payment amount (" + payment.getTotalAmount() + ")");
            }

            payment.setAllocations(newAllocations);
            payment.setAllocatedAmount(totalAllocated);
        } else {
            payment.setAllocatedAmount(BigDecimal.ZERO);
        }

        Payment updatedPayment = paymentRepository.save(payment);
        log.info("Payment updated successfully: {}", updatedPayment.getPaymentNumber());

        return paymentMapper.toResponse(updatedPayment);
    }

    @Override
    @Transactional
    public void submitForApproval(Long id) {
        log.info("Submitting Payment for approval: {}", id);

        Payment payment = findPaymentById(id);

        if (!"DRAFT".equals(payment.getStatus())) {
            throw new InvalidOperationException(
                "Cannot submit for approval. Current status: " + payment.getStatus() + 
                ". Only DRAFT payments can be submitted for approval.");
        }

        payment.setStatus("PENDING");
        paymentRepository.save(payment);

        log.info("Payment submitted for approval successfully: {}", id);
    }

    /**
     * Approve Payment (PENDING → CLEARED)
     *
     * @param id
     * @param approvedBy
     */
    @Override
    public void approvePayment(Long id, Long approvedBy) {

    }

    /**
     * Mark as Collected
     *
     * @param id
     * @param collectedBy
     * @param collectedAt
     */
    @Override
    public void markAsCollected(Long id, Long collectedBy, LocalDateTime collectedAt) {

    }


    @Transactional
    public void approvePayment(Long id, User approvedBy) {
        log.info("Approving Payment: {} by user: {}", id, approvedBy);

        Payment payment = findPaymentById(id);

        if (!"PENDING".equals(payment.getStatus())) {
            throw new InvalidOperationException(
                "Cannot approve Payment. Current status: " + payment.getStatus() + 
                ". Only PENDING payments can be approved.");
        }

        payment.setStatus("CLEARED");
        payment.setApprovedBy(approvedBy);
        payment.setApprovedAt(LocalDateTime.now());

        // Update invoice paid amounts for all allocations
        if (payment.getAllocations() != null) {
            for (PaymentAllocation allocation : payment.getAllocations()) {
                Invoice invoice = allocation.getInvoice();
                BigDecimal currentPaid = invoice.getPaidAmount() != null ? 
                    invoice.getPaidAmount() : BigDecimal.ZERO;
                invoice.setPaidAmount(currentPaid.add(allocation.getAllocatedAmount()));

                // Update payment status
                updateInvoicePaymentStatus(invoice);
                invoiceRepository.save(invoice);
            }
        }

        paymentRepository.save(payment);

        log.info("Payment approved successfully: {}", id);
    }


    @Transactional
    public void markAsCollected(Long id, User collectedBy, LocalDateTime collectedAt) {
        log.info("Marking Payment as collected: {} by user: {}", id, collectedBy);

        Payment payment = findPaymentById(id);

        payment.setCollectedBy(collectedBy);
        payment.setCollectedAt(collectedAt);
        paymentRepository.save(payment);

        log.info("Payment marked as collected successfully: {}", id);
    }

    @Override
    @Transactional
    public void markAsBounced(Long id, String reason) {
        log.info("Marking Payment as BOUNCED: {} with reason: {}", id, reason);

        Payment payment = findPaymentById(id);

        if (!"CHEQUE".equals(payment.getPaymentMode())) {
            throw new InvalidOperationException("Only CHEQUE payments can be marked as bounced.");
        }

        if ("CLEARED".equals(payment.getStatus())) {
            throw new InvalidOperationException("Cannot bounce a cleared payment.");
        }

        payment.setStatus("BOUNCED");
        payment.setRemarks(payment.getRemarks() != null ? 
            payment.getRemarks() + "\nBounced: " + reason : 
            "Bounced: " + reason);
        paymentRepository.save(payment);

        log.info("Payment marked as BOUNCED successfully: {}", id);
    }

    @Override
    @Transactional
    public Payment cancelPayment(Long id, String reason) {
        log.info("Cancelling Payment: {} with reason: {}", id, reason);

        Payment payment = findPaymentById(id);

        if (!"DRAFT".equals(payment.getStatus())) {
            throw new InvalidOperationException(
                "Cannot cancel Payment. Current status: " + payment.getStatus() + 
                ". Only DRAFT payments can be cancelled.");
        }

        payment.setStatus("CANCELLED");
        payment.setRemarks(payment.getRemarks() != null ? 
            payment.getRemarks() + "\nCancelled: " + reason : 
            "Cancelled: " + reason);
        paymentRepository.save(payment);

        log.info("Payment cancelled successfully: {}", id);
        return payment;
    }

    @Transactional
    public Payment completePayment(Long id) {
        log.info("Completing Payment: {}", id);

        Payment payment = findPaymentById(id);

        if (!"CLEARED".equals(payment.getStatus())) {
            throw new InvalidOperationException(
                "Cannot complete Payment. Current status: " + payment.getStatus() +
                ". Only CLEARED payments can be completed.");
        }

        payment.setStatus("COMPLETED");
        paymentRepository.save(payment);

        log.info("Payment completed successfully: {}", id);
        return payment;
    }

    @Transactional
    public Payment clearPayment(Long id){
        log.info("Clearing Payment: {}", id);

        Payment payment = findPaymentById(id);

        if (!"PENDING".equals(payment.getStatus())) {
            throw new InvalidOperationException(
                "Cannot clear Payment. Current status: " + payment.getStatus() +
                ". Only PENDING payments can be cleared.");
        }

        payment.setStatus("CLEARED");
        paymentRepository.save(payment);

        log.info("Payment cleared successfully: {}", id);
        return payment;
    }

    @Transactional
    public Payment failPayment(Long id,String failureReason){
        log.info("Failing Payment: {} with reason: {}", id, failureReason);

        Payment payment = findPaymentById(id);

        if (!"PENDING".equals(payment.getStatus())) {
            throw new InvalidOperationException(
                "Cannot fail Payment. Current status: " + payment.getStatus() +
                ". Only PENDING payments can be failed.");
        }

        payment.setStatus("FAILED");
        payment.setRemarks(payment.getRemarks() != null ?
            payment.getRemarks() + "\nFailed: " + failureReason :
            "Failed: " + failureReason);
        paymentRepository.save(payment);

        log.info("Payment failed successfully: {}", id);
        return payment;
    }

    @Transactional
    public Payment reconcilePayment(Long id,LocalDate reconciliationDate){
        log.info("Reconciling Payment: {} on date: {}", id, reconciliationDate);

        Payment payment = findPaymentById(id);

        if (!"CLEARED".equals(payment.getStatus())) {
            throw new InvalidOperationException(
                "Cannot reconcile Payment. Current status: " + payment.getStatus() +
                ". Only CLEARED payments can be reconciled.");
        }

        payment.setReconciliationDate(reconciliationDate);
        paymentRepository.save(payment);

        log.info("Payment reconciled successfully: {}", id);
        return payment;
    }

    @Transactional
    public PaymentAllocation allocatePayment(Long paymentId,Long invoiceId,double amount){
        log.info("Allocating Payment: {} to Invoice: {} with amount: {}", paymentId, invoiceId, amount);

        Payment payment = findPaymentById(paymentId);
        Invoice invoice = findInvoiceById(invoiceId);

        // Validate allocation amount
        validateAllocationAmount(invoice, BigDecimal.valueOf(amount));

        PaymentAllocation allocation = new PaymentAllocation();
        allocation.setPayment(payment);
        allocation.setInvoice(invoice);
        allocation.setAllocatedAmount(BigDecimal.valueOf(amount));

        paymentAllocationRepository.save(allocation);

        log.info("Payment allocated successfully: {}", paymentId);
        return allocation;
    }

    /**
     * @param paymentId
     * @param allocations
     * @return
     */
    @Override
    public List<PaymentAllocation> allocatePaymentToMultipleInvoices(Long paymentId, Map<String, Object> allocations) {
        return null;
    }

//    @Transactional
//    List<PaymentAllocation> allocatePaymentToMultipleInvoices(Long paymentId, Map<String,Object> allocations){
//        log.info("Allocating Payment: {} to multiple Invoices", paymentId);
//
//        Payment payment = findPaymentById(paymentId);
//        List<PaymentAllocation> savedAllocations = new ArrayList<>();
//
//        for (Map.Entry<String, Object> entry : allocations.entrySet()) {
//            Long invoiceId = Long.valueOf(entry.getKey());
//            Double amount = Double.valueOf(entry.getValue().toString());
//
//            Invoice invoice = findInvoiceById(invoiceId);
//
//            // Validate allocation amount
//            validateAllocationAmount(invoice, BigDecimal.valueOf(amount));
//
//            PaymentAllocation allocation = new PaymentAllocation();
//            allocation.setPayment(payment);
//            allocation.setInvoice(invoice);
//            allocation.setAllocatedAmount(BigDecimal.valueOf(amount));
//
//            paymentAllocationRepository.save(allocation);
//            savedAllocations.add(allocation);
//        }
//
//        log.info("Payment allocated to multiple invoices successfully: {}", paymentId);
//        return savedAllocations;
//    }

    @Transactional
    public PaymentAllocation reverseAllocation(Long allocationId,String reason){
        log.info("Reversing Payment Allocation: {} with reason: {}", allocationId, reason);

        PaymentAllocation allocation = findPaymentAllocationById(allocationId);

        allocation.setReversed(true);
        allocation.setRemarks(allocation.getRemarks() != null ?
            allocation.getRemarks() + "\nReversed: " + reason :
            "Reversed: " + reason);
        paymentAllocationRepository.save(allocation);

        log.info("Payment Allocation reversed successfully: {}", allocationId);
        return allocation;
    }

    @Transactional
    public  List<PaymentAllocation> getPaymentAllocations(Long paymentId){
        log.info("Fetching Payment Allocations for Payment: {}", paymentId);

        List<PaymentAllocation> allocations = paymentAllocationRepository.findByPaymentId(paymentId);

        log.info("Fetched {} allocations for Payment: {}", allocations.size(), paymentId);
        return allocations;
    }

    @Transactional
    public List<PaymentAllocation> getInvoiceAllocations(Long invoiceId){
        log.info("Fetching Payment Allocations for Invoice: {}", invoiceId);

        List<PaymentAllocation> allocations = paymentAllocationRepository.findByInvoiceId(invoiceId);

        log.info("Fetched {} allocations for Invoice: {}", allocations.size(), invoiceId);
        return allocations;
    }

    @Transactional
    public Double getTotalAllocatedAmount(Long paymentId){
        log.info("Calculating total allocated amount for Payment: {}", paymentId);

        List<PaymentAllocation> allocations = paymentAllocationRepository.findByPaymentId(paymentId);
        Double totalAllocated = allocations.stream()
            .mapToDouble(allocation -> allocation.getAllocatedAmount().doubleValue())
            .sum();

        log.info("Total allocated amount for Payment {}: {}", paymentId, totalAllocated);
        return totalAllocated;
    }
    @Transactional
    public Double getUnallocatedAmount(Long paymentId){
        log.info("Calculating unallocated amount for Payment: {}", paymentId);

        Payment payment = findPaymentById(paymentId);
        Double totalAllocated = getTotalAllocatedAmount(paymentId);
        Double unallocatedAmount = payment.getTotalAmount().doubleValue() - totalAllocated;

        log.info("Unallocated amount for Payment {}: {}", paymentId, unallocatedAmount);
        return unallocatedAmount;
    }

    @Transactional
    public List<Payment> getPendingPayments(){
        log.info("Fetching all pending Payments");

        List<Payment> pendingPayments = paymentRepository.findByStatus("PENDING");

        log.info("Fetched {} pending Payments", pendingPayments.size());
        return pendingPayments;
    }

    @Transactional
    public List<Payment> getCompletedPayments(){
        log.info("Fetching all completed Payments");

        List<Payment> completedPayments = paymentRepository.findByStatus("COMPLETED");

        log.info("Fetched {} completed Payments", completedPayments.size());
        return completedPayments;
    }

    @Transactional
    public List<Payment> getFailedPayments(){
        log.info("Fetching all failed Payments");

        List<Payment> failedPayments = paymentRepository.findByStatus("FAILED");

        log.info("Fetched {} failed Payments", failedPayments.size());
        return failedPayments;
    }

    @Transactional
    public List<Payment> getUnreconciledPayments(){
        log.info("Fetching all unreconciled Payments");

        List<Payment> unreconciledPayments = paymentRepository.findByReconciliationDateIsNullAndStatus("CLEARED");

        log.info("Fetched {} unreconciled Payments", unreconciledPayments.size());
        return unreconciledPayments;
    }
    @Transactional
    public List<Payment> getClearedPayments(){
        log.info("Fetching all cleared Payments");

        List<Payment> clearedPayments = paymentRepository.findByStatus("CLEARED");

        log.info("Fetched {} cleared Payments", clearedPayments.size());
        return clearedPayments;
    }


    @Transactional
    public List<Payment> getCashPayments(){
        log.info("Fetching all cash Payments");

        List<Payment> cashPayments = paymentRepository.findByPaymentMode("CASH");

        log.info("Fetched {} cash Payments", cashPayments.size());
        return cashPayments;
    }

    @Transactional
    public List<Payment> getChequePayments(){
        log.info("Fetching all cheque Payments");

        List<Payment> chequePayments = paymentRepository.findByPaymentMode("CHEQUE");

        log.info("Fetched {} cheque Payments", chequePayments.size());
        return chequePayments;
    }

    /**
     * @param bankAccountId
     * @return
     */
    @Override
    public List<Payment> getPaymentsByBankAccount(Long bankAccountId) {
        return null;
    }

    @Transactional
    public List<Payment> getBankTransferPayments(){
        log.info("Fetching all bank transfer Payments");

        List<Payment> bankTransferPayments = paymentRepository.findByPaymentMode("BANK_TRANSFER");

        log.info("Fetched {} bank transfer Payments", bankTransferPayments.size());
        return bankTransferPayments;
    }

    @Transactional
    public List<Payment> getOverpayments(){
        log.info("Fetching all overpayments");

        List<Payment> overpayments = paymentRepository.findOverpayments();

        log.info("Fetched {} overpayments", overpayments.size());
        return overpayments;
    }

    @Transactional
    public List<Payment> getRecentPayments(){
        List <Payment> recentPayments = paymentRepository.findAllByOrderByPaymentDateDescCreatedAtDesc();


        return recentPayments;
    }

    @Transactional
    public List<Payment> getCustomerRecentPayments(Long customerId, int limit){
        List <Payment> recentPayments = paymentRepository.findByCustomerIdOrderByPaymentDateDescCreatedAtDesc(customerId);

        return recentPayments.stream().limit(limit).collect(Collectors.toList());
    }

    @Transactional
    public boolean canCompletePayment(Long id){
        Payment payment = findPaymentById(id);
        return "CLEARED".equals(payment.getStatus());
    }

    @Transactional
    public boolean canAllocatePayment(Long id){
        Payment payment = findPaymentById(id);
        return "PENDING".equals(payment.getStatus());
    }

    @Transactional
    public boolean canReverseAllocation(Long allocationId){
        PaymentAllocation allocation = findPaymentAllocationById(allocationId);
        return !allocation.isReversed();
    }

    @Transactional
    public  List<Payment> getPartialPayments(){
        log.info("Fetching all partial payments");

        List<Payment> partialPayments = paymentRepository.findPartialPayments();

        log.info("Fetched {} partial payments", partialPayments.size());
        return partialPayments;
    }

//    @Transactional
//    public List<Payment> getPaymentsByBankAccount(Long bankAccountId){
//        log.info("Fetching Payments for Bank Account: {}", bankAccountId);
//
//        List<Payment> payments = paymentRepository.findByBankAccountId(bankAccountId);
//
//        log.info("Fetched {} Payments for Bank Account: {}", payments.size(), bankAccountId);
//        return payments;
//    }
    @Override
    @Transactional
    public void deletePayment(Long id) {
        log.info("Deleting Payment: {}", id);

        if (!canDelete(id)) {
            Payment payment = findPaymentById(id);
            throw new InvalidOperationException(
                "Cannot delete Payment. Current status: " + payment.getStatus() + 
                ". Only DRAFT payments can be deleted.");
        }

        paymentRepository.deleteById(id);
        log.info("Payment deleted successfully: {}", id);
    }
    @Transactional
    public double calculateTotalAllocated(Long id){
        log.info("Calculating total allocated amount for Payment: {}", id);

        List<PaymentAllocation> allocations = paymentAllocationRepository.findByPaymentId(id);
        double totalAllocated = allocations.stream()
            .mapToDouble(allocation -> allocation.getAllocatedAmount().doubleValue())
            .sum();

        log.info("Total allocated amount for Payment {}: {}", id, totalAllocated);
        return totalAllocated;
    }

    @Transactional
    public double calculateRemainingAmount(Long id){
        log.info("Calculating remaining amount for Payment: {}", id);

        Payment payment = findPaymentById(id);
        double totalAllocated = calculateTotalAllocated(id);
        double remainingAmount = payment.getTotalAmount().doubleValue() - totalAllocated;

        log.info("Remaining amount for Payment {}: {}", id, remainingAmount);
        return remainingAmount;
    }
    @Transactional
    public  void calculatePaymentAllocations(Long id){
        log.info("Calculating Payment Allocations for Payment: {}", id);

        Payment payment = findPaymentById(id);
        List<PaymentAllocation> allocations = paymentAllocationRepository.findByPaymentId(id);

        BigDecimal totalAllocated = allocations.stream()
            .map(PaymentAllocation::getAllocatedAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        payment.setAllocatedAmount(totalAllocated);
        paymentRepository.save(payment);

        log.info("Calculated total allocated amount {} for Payment: {}", totalAllocated, id);
    }

    @Override
    @Transactional
    public void allocateToInvoice(Long paymentId, PaymentAllocationRequest allocationRequest) {
        log.info("Allocating payment {} to invoice {}", paymentId, allocationRequest.getInvoiceId());

        Payment payment = findPaymentById(paymentId);
        Invoice invoice = findInvoiceById(allocationRequest.getInvoiceId());

        // Validate allocation amount
        validateAllocationAmount(invoice, allocationRequest.getAllocatedAmount());

        // Check if payment has enough unallocated amount
        BigDecimal currentAllocated = payment.getAllocatedAmount() != null ? 
            payment.getAllocatedAmount() : BigDecimal.ZERO;
        BigDecimal available = payment.getTotalAmount().subtract(currentAllocated);

        if (allocationRequest.getAllocatedAmount().compareTo(available) > 0) {
            throw new InvalidOperationException(
                "Allocation amount (" + allocationRequest.getAllocatedAmount() + 
                ") exceeds available amount (" + available + ")");
        }

        // Create allocation
        PaymentAllocation allocation = paymentAllocationMapper.toEntity(allocationRequest);
        allocation.setPayment(payment);
        allocation.setInvoice(invoice);
        paymentAllocationRepository.save(allocation);

        // Update payment allocated amount
        payment.setAllocatedAmount(currentAllocated.add(allocationRequest.getAllocatedAmount()));
        paymentRepository.save(payment);

        // If payment is CLEARED, update invoice paid amount
        if ("CLEARED".equals(payment.getStatus())) {
            BigDecimal currentPaid = invoice.getPaidAmount() != null ? 
                invoice.getPaidAmount() : BigDecimal.ZERO;
            invoice.setPaidAmount(currentPaid.add(allocationRequest.getAllocatedAmount()));
            updateInvoicePaymentStatus(invoice);
            invoiceRepository.save(invoice);
        }

        log.info("Payment allocated to invoice successfully");
    }

    @Override
    @Transactional
    public void removeAllocation(Long paymentId, Long allocationId) {
        log.info("Removing allocation {} from payment {}", allocationId, paymentId);

        Payment payment = findPaymentById(paymentId);
        PaymentAllocation allocation = findPaymentAllocationById(allocationId);

        if (!allocation.getPayment().getId().equals(paymentId)) {
            throw new InvalidOperationException("Allocation does not belong to this payment.");
        }

        // Update payment allocated amount
        BigDecimal currentAllocated = payment.getAllocatedAmount() != null ? 
            payment.getAllocatedAmount() : BigDecimal.ZERO;
        payment.setAllocatedAmount(currentAllocated.subtract(allocation.getAllocatedAmount()));
        paymentRepository.save(payment);

        // If payment is CLEARED, reverse invoice paid amount
        if ("CLEARED".equals(payment.getStatus())) {
            Invoice invoice = allocation.getInvoice();
            BigDecimal currentPaid = invoice.getPaidAmount() != null ? 
                invoice.getPaidAmount() : BigDecimal.ZERO;
            invoice.setPaidAmount(currentPaid.subtract(allocation.getAllocatedAmount()));
            updateInvoicePaymentStatus(invoice);
            invoiceRepository.save(invoice);
        }

        paymentAllocationRepository.delete(allocation);

        log.info("Allocation removed successfully");
    }

//    @Transactional
//    public List<Payment> createBulkPayments(PaymentRequest requests){
//        log.info("Creating bulk Payments");
//
//        // Implementation for bulk payment creation can be added here
//
//        log.info("Bulk Payments created successfully");
//        return new ArrayList<>();
//    }

    @Transactional
    public int deleteBulkPayments(List<Long> paymentIds){
        log.info("Deleting bulk Payments: {}", paymentIds);

        int deletedCount = 0;
        for (Long paymentId : paymentIds) {
            if (canDelete(paymentId)) {
                paymentRepository.deleteById(paymentId);
                deletedCount++;
            }
        }

        log.info("Deleted {} Payments in bulk operation", deletedCount);
        return deletedCount;
    }

    /**
     * @param paymentIds
     * @param reconciliationDate
     * @return
     */
    @Override
    public int reconcileBulkPayments(List<Long> paymentIds, LocalDate reconciliationDate) {
        return 0;
    }

    @Transactional
    public int  reconcileBulkPayments(Long paymentIds,LocalDate reconciliationDate){
        log.info("Reconciling bulk Payments: {} on date: {}", paymentIds, reconciliationDate);

        int reconciledCount = 0;
        for (Long paymentId : Arrays.asList(paymentIds)) {
            Payment payment = findPaymentById(paymentId);
            if ("CLEARED".equals(payment.getStatus())) {
                payment.setReconciliationDate(reconciliationDate);
                paymentRepository.save(payment);
                reconciledCount++;
            }
        }

        log.info("Reconciled {} Payments in bulk operation", reconciledCount);
        return reconciledCount;
    }

    @Transactional
    public  Map<String,Object> getPaymentStatistics(){
        log.info("Fetching Payment statistics");

        long totalPayments = paymentRepository.count();
        long pendingPayments = paymentRepository.countByStatus("PENDING");
        long clearedPayments = paymentRepository.countByStatus("CLEARED");
        long completedPayments = paymentRepository.countByStatus("COMPLETED");
        long bouncedPayments = paymentRepository.countByStatus("BOUNCED");
        long cancelledPayments = paymentRepository.countByStatus("CANCELLED");


        Map<String,Object> map=new HashMap<>();
        map.put("totalPayments", totalPayments);
        map.put("pendingPayments", pendingPayments);
        map.put("clearedPayments", clearedPayments);
        map.put("completedPayments", completedPayments);
        map.put("bouncedPayments", bouncedPayments);
        map.put("cancelledPayments", cancelledPayments);

        log.info("Fetched Payment statistics: {}", map);
        return map;
    }
    @Transactional
    public Map<String,Object> getDashboardStatistics(){
        log.info("Fetching Dashboard statistics");

        double totalReceivedAmount = paymentRepository.sumTotalReceivedAmount()
            .orElse(BigDecimal.ZERO).doubleValue();
        double totalPendingAmount = paymentRepository.sumTotalPendingAmount()
            .orElse(BigDecimal.ZERO).doubleValue();
        double totalUnallocatedAmount = paymentRepository.sumTotalUnallocatedAmount()
            .orElse(BigDecimal.ZERO).doubleValue();

        Map<String,Object> map=new HashMap<>();
        map.put("totalReceivedAmount", totalReceivedAmount);
        map.put("totalPendingAmount", totalPendingAmount);
        map.put("totalUnallocatedAmount", totalUnallocatedAmount);

        log.info("Fetched Dashboard statistics: {}", map);
        return map;
    }

    @Transactional
    public double getAveragePaymentAmount(){
        log.info("Calculating average Payment amount");

        double averageAmount = paymentRepository.calculateAveragePaymentAmount();


        log.info("Average Payment amount: {}", averageAmount);
        return averageAmount;
    }

    @Transactional
    public double getTotalPaymentAmount(){
        log.info("Calculating total Payment amount");

        double totalAmount = paymentRepository.sumTotalPaymentAmount();


        log.info("Total Payment amount: {}", totalAmount);
        return totalAmount;
    }

    @Transactional
    public double getTotalUnallocatedAmount(){
        log.info("Calculating total unallocated amount across all Payments");

        double totalUnallocatedAmount = paymentRepository.sumTotalUnallocatedAmount()
            .orElse(BigDecimal.ZERO).doubleValue();

        log.info("Total unallocated amount across all Payments: {}", totalUnallocatedAmount);
        return totalUnallocatedAmount;
    }
    @Transactional

    public List<Map<String,Object>> getPaymentTypeDistribution(){
        log.info("Fetching Payment type distribution");

        List<Map<String,Object>> distribution = paymentRepository.getPaymentTypeDistribution();

        log.info("Fetched Payment type distribution: {}", distribution);
        return distribution;
    }


    @Transactional
    public  int completeBulkPayments(List<Long> paymentIds){
        log.info("Completing bulk Payments: {}", paymentIds);

        int completedCount = 0;
        for (Long paymentId : paymentIds) {
            Payment payment = findPaymentById(paymentId);
            if ("CLEARED".equals(payment.getStatus())) {
                payment.setStatus("COMPLETED");
                paymentRepository.save(payment);
                completedCount++;
            }
        }

        log.info("Completed {} Payments in bulk operation", completedCount);
        return completedCount;
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = findPaymentById(id);
        return paymentMapper.toResponse(payment);
    }

    @Override
    public PaymentResponse getPaymentByNumber(String paymentNumber) {
        Payment payment = paymentRepository.findByPaymentNumber(paymentNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentNumber));
        return paymentMapper.toResponse(payment);
    }

    @Override
    public PageResponse<PaymentResponse> getAllPayments(Pageable pageable) {
        Page<Payment> paymentPage = paymentRepository.findAll(pageable);
        return createPageResponse(paymentPage);
    }

    @Override
    public PageResponse<PaymentResponse> getPaymentsByStatus(String status, Pageable pageable) {
        Page<Payment> paymentPage = paymentRepository.findByStatus(status, pageable);
        return createPageResponse(paymentPage);
    }

    @Override
    public List<PaymentResponse> getPaymentsByCustomer(Long customerId) {
        List<Payment> payments = paymentRepository.findByCustomerId(customerId);
        return payments.stream()
            .map(paymentMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<PaymentResponse> getPaymentsByPaymentMode(String paymentMode, Pageable pageable) {
        Page<Payment> paymentPage = paymentRepository.findByPaymentMode(paymentMode, pageable);
        return createPageResponse(paymentPage);
    }

    @Override
    public List<PaymentResponse> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Payment> payments = paymentRepository.findByPaymentDateBetween(startDate, endDate);
        return payments.stream()
            .map(paymentMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponse> getPendingApprovalPayments() {
        List<Payment> payments = paymentRepository.findByStatus("PENDING");
        return payments.stream()
            .map(paymentMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponse> getUnallocatedPayments() {
        List<Payment> payments = paymentRepository.findUnallocatedPayments();
        return payments.stream()
            .map(paymentMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponse> getPaymentsByCollectedBy(Long collectedBy) {
        List<Payment> payments = paymentRepository.findByCollectedBy(collectedBy);
        return payments.stream()
            .map(paymentMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<PaymentResponse> searchPayments(String keyword, Pageable pageable) {
        Page<Payment> paymentPage = paymentRepository.searchPayments(keyword,null,null,null,null,null,pageable);
        return createPageResponse(paymentPage);
    }

    /**
     * Get total payments amount for a customer
     *
     * @param customerId
     */
    @Override
    public BigDecimal getTotalPaymentsByCustomer(Long customerId) {
        return null;
    }

    /**
     * Get total unallocated amount for a customer
     *
     * @param customerId
     */
    @Override
    public BigDecimal getTotalUnallocatedByCustomer(Long customerId) {
        return null;
    }

    /**
     * Get total payments for a date range
     *
     * @param startDate
     * @param endDate
     */
    @Override
    public BigDecimal getTotalPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return null;
    }

//    @Override
//    public BigDecimal getTotalPaymentsByCustomer(Long customerId) {
//        return paymentRepository.sumTotalAmountByCustomer(customerId)
//            .orElse(BigDecimal.ZERO);
//    }
//
//    @Override
//    public BigDecimal getTotalUnallocatedByCustomer(Long customerId) {
//        return paymentRepository.sumUnallocatedAmountByCustomer(customerId)
//            .orElse(BigDecimal.ZERO);
//    }
//
//    @Override
//    public BigDecimal getTotalPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
//        return paymentRepository.sumTotalAmountByDateRange(startDate, endDate)
//            .orElse(BigDecimal.ZERO);
//    }

    @Override
    public boolean canDelete(Long id) {
        Payment payment = findPaymentById(id);
        return "DRAFT".equals(payment.getStatus());
    }

    @Override
    public boolean canUpdate(Long id) {
        Payment payment = findPaymentById(id);
        return "DRAFT".equals(payment.getStatus());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private void validateAllocationAmount(Invoice invoice, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOperationException("Allocation amount must be greater than zero.");
        }

        BigDecimal outstanding = invoice.getBalanceAmount();
        if (amount.compareTo(outstanding) > 0) {
            throw new InvalidOperationException(
                "Allocation amount (" + amount + 
                ") exceeds invoice outstanding (" + outstanding + ")");
        }
    }



    private void updateInvoicePaymentStatus(Invoice invoice) {
        BigDecimal balance = invoice.getBalanceAmount();
        
        if (balance.compareTo(BigDecimal.ZERO) == 0) {
            invoice.setPaymentStatus("PAID");
        } else if (invoice.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {
            invoice.setPaymentStatus("PARTIAL");
        } else {
            invoice.setPaymentStatus("UNPAID");
        }
    }

    private void validateUniquePaymentNumber(String paymentNumber, Long excludeId) {
        boolean exists;
        if (excludeId != null) {
            exists = paymentRepository.existsByPaymentNumberAndIdNot(paymentNumber, excludeId);
        } else {
            exists = paymentRepository.existsByPaymentNumber(paymentNumber);
        }

        if (exists) {
            throw new DuplicateResourceException("Payment with number '" + paymentNumber + "' already exists");
        }
    }

    private Payment findPaymentById(Long id) {
        return paymentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));
    }

    private PaymentAllocation findPaymentAllocationById(Long id) {
        return paymentAllocationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Payment Allocation not found: " + id));
    }

    private Customer findCustomerById(Long id) {
        return customerRepository.findByIdAndDeletedAtIsNull(id);
    }

    private Invoice findInvoiceById(Long id) {
        return invoiceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Invoice not found: " + id));
    }

    private PageResponse<PaymentResponse> createPageResponse(Page<Payment> paymentPage) {
        List<PaymentResponse> content = paymentPage.getContent().stream()
            .map(paymentMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<PaymentResponse>builder()
            .content(content)
            .pageNumber(paymentPage.getNumber())
            .pageSize(paymentPage.getSize())
            .totalElements(paymentPage.getTotalElements())
            .totalPages(paymentPage.getTotalPages())
            .last(paymentPage.isLast())
            .first(paymentPage.isFirst())
            .empty(paymentPage.isEmpty())
            .build();
    }
}
