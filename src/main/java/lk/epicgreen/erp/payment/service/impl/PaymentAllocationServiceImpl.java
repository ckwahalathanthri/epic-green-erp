package lk.epicgreen.erp.payment.service.impl;

import lk.epicgreen.erp.payment.dto.request.PaymentAllocationRequest;
import lk.epicgreen.erp.payment.dto.response.PaymentAllocationResponse;
import lk.epicgreen.erp.payment.entity.Payment;
import lk.epicgreen.erp.payment.entity.PaymentAllocation;
import lk.epicgreen.erp.payment.mapper.PaymentAllocationMapper;
import lk.epicgreen.erp.payment.repository.PaymentRepository;
import lk.epicgreen.erp.payment.repository.PaymentAllocationRepository;
import lk.epicgreen.erp.payment.service.PaymentAllocationService;
import lk.epicgreen.erp.sales.entity.Invoice;
import lk.epicgreen.erp.sales.repository.InvoiceRepository;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.InvalidOperationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of PaymentAllocationService interface
 * 
 * Handles bill-to-bill settlement operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PaymentAllocationServiceImpl implements PaymentAllocationService {

    private final PaymentAllocationRepository paymentAllocationRepository;
    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentAllocationMapper paymentAllocationMapper;

    @Override
    @Transactional
    public PaymentAllocationResponse createAllocation(Long paymentId, PaymentAllocationRequest request) {
        log.info("Creating payment allocation for payment: {}", paymentId);

        Payment payment = findPaymentById(paymentId);
        Invoice invoice = findInvoiceById(request.getInvoiceId());

        // Validate allocation
        validateAllocation(request.getInvoiceId(), request.getAllocatedAmount());

        // Check if payment has enough unallocated amount
        BigDecimal currentAllocated = payment.getAllocatedAmount() != null ? 
            payment.getAllocatedAmount() : BigDecimal.ZERO;
        BigDecimal available = payment.getTotalAmount().subtract(currentAllocated);

        if (request.getAllocatedAmount().compareTo(available) > 0) {
            throw new InvalidOperationException(
                "Allocation amount (" + request.getAllocatedAmount() + 
                ") exceeds available amount (" + available + ")");
        }

        // Create allocation
        PaymentAllocation allocation = paymentAllocationMapper.toEntity(request);
        allocation.setPayment(payment);
        allocation.setInvoice(invoice);

        PaymentAllocation savedAllocation = paymentAllocationRepository.save(allocation);

        // Update payment allocated amount
        payment.setAllocatedAmount(currentAllocated.add(request.getAllocatedAmount()));
        paymentRepository.save(payment);

        log.info("Payment allocation created successfully");

        return paymentAllocationMapper.toResponse(savedAllocation);
    }

    @Override
    @Transactional
    public void deleteAllocation(Long id) {
        log.info("Deleting payment allocation: {}", id);

        PaymentAllocation allocation = findPaymentAllocationById(id);
        Payment payment = allocation.getPayment();

        // Update payment allocated amount
        BigDecimal currentAllocated = payment.getAllocatedAmount() != null ? 
            payment.getAllocatedAmount() : BigDecimal.ZERO;
        payment.setAllocatedAmount(currentAllocated.subtract(allocation.getAllocatedAmount()));
        paymentRepository.save(payment);

        paymentAllocationRepository.delete(allocation);

        log.info("Payment allocation deleted successfully");
    }

    @Override
    public PaymentAllocationResponse getAllocationById(Long id) {
        PaymentAllocation allocation = findPaymentAllocationById(id);
        return paymentAllocationMapper.toResponse(allocation);
    }

    @Override
    public List<PaymentAllocationResponse> getAllAllocations() {
        List<PaymentAllocation> allocations = paymentAllocationRepository.findAll();
        return allocations.stream()
            .map(paymentAllocationMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<PaymentAllocationResponse> getAllocationsByPayment(Long paymentId) {
        List<PaymentAllocation> allocations = paymentAllocationRepository.findByPaymentId(paymentId);
        return allocations.stream()
            .map(paymentAllocationMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<PaymentAllocationResponse> getAllocationsByInvoice(Long invoiceId) {
        List<PaymentAllocation> allocations = paymentAllocationRepository.findByInvoiceId(invoiceId);
        return allocations.stream()
            .map(paymentAllocationMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<PaymentAllocationResponse> getAllocationsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<PaymentAllocation> allocations = paymentAllocationRepository.findByAllocationDateBetween(startDate, endDate);
        return allocations.stream()
            .map(paymentAllocationMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<PaymentAllocationResponse> getAllocationsByCustomer(Long customerId) {
        List<PaymentAllocation> allocations = paymentAllocationRepository.findByPaymentCustomerId(customerId);
        return allocations.stream()
            .map(paymentAllocationMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public void validateAllocation(Long invoiceId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOperationException("Allocation amount must be greater than zero.");
        }

        Invoice invoice = findInvoiceById(invoiceId);
        BigDecimal outstanding = invoice.getBalanceAmount();

        if (amount.compareTo(outstanding) > 0) {
            throw new InvalidOperationException(
                "Allocation amount (" + amount + 
                ") exceeds invoice outstanding (" + outstanding + ")");
        }
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private PaymentAllocation findPaymentAllocationById(Long id) {
        return paymentAllocationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Payment Allocation not found: " + id));
    }

    private Payment findPaymentById(Long id) {
        return paymentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));
    }

    private Invoice findInvoiceById(Long id) {
        return invoiceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Invoice not found: " + id));
    }
}
