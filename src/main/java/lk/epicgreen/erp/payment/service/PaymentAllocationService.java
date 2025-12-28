package lk.epicgreen.erp.payment.service;

import lk.epicgreen.erp.payment.dto.request.PaymentAllocationRequest;
import lk.epicgreen.erp.payment.dto.response.PaymentAllocationResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for PaymentAllocation entity business logic
 * 
 * Handles bill-to-bill settlement operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface PaymentAllocationService {

    /**
     * Create a new Payment Allocation
     */
    PaymentAllocationResponse createAllocation(Long paymentId, PaymentAllocationRequest request);

    /**
     * Delete Payment Allocation
     */
    void deleteAllocation(Long id);

    /**
     * Get Payment Allocation by ID
     */
    PaymentAllocationResponse getAllocationById(Long id);

    /**
     * Get all Payment Allocations
     */
    List<PaymentAllocationResponse> getAllAllocations();

    /**
     * Get Payment Allocations by payment
     */
    List<PaymentAllocationResponse> getAllocationsByPayment(Long paymentId);

    /**
     * Get Payment Allocations by invoice
     */
    List<PaymentAllocationResponse> getAllocationsByInvoice(Long invoiceId);

    /**
     * Get Payment Allocations by date range
     */
    List<PaymentAllocationResponse> getAllocationsByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Get Payment Allocations by customer
     */
    List<PaymentAllocationResponse> getAllocationsByCustomer(Long customerId);

    /**
     * Validate allocation amount against invoice outstanding
     */
    void validateAllocation(Long invoiceId, java.math.BigDecimal amount);
}
