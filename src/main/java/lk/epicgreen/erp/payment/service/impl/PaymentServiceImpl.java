package lk.epicgreen.erp.payment.service.impl;

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
import java.util.ArrayList;
import java.util.List;
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

    @Override
    @Transactional
    public void approvePayment(Long id, Long approvedBy) {
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

    @Override
    @Transactional
    public void markAsCollected(Long id, Long collectedBy, LocalDateTime collectedAt) {
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
    public void cancelPayment(Long id, String reason) {
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
    }

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
        Page<Payment> paymentPage = paymentRepository.searchPayments(keyword, pageable);
        return createPageResponse(paymentPage);
    }

    @Override
    public BigDecimal getTotalPaymentsByCustomer(Long customerId) {
        return paymentRepository.sumTotalAmountByCustomer(customerId)
            .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getTotalUnallocatedByCustomer(Long customerId) {
        return paymentRepository.sumUnallocatedAmountByCustomer(customerId)
            .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getTotalPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return paymentRepository.sumTotalAmountByDateRange(startDate, endDate)
            .orElse(BigDecimal.ZERO);
    }

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
        return customerRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
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
