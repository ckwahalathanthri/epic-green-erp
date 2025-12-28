package lk.epicgreen.erp.payment.service.impl;

import lk.epicgreen.erp.payment.dto.request.ChequeRequest;
import lk.epicgreen.erp.payment.dto.response.ChequeResponse;
import lk.epicgreen.erp.payment.entity.Cheque;
import lk.epicgreen.erp.payment.entity.Payment;
import lk.epicgreen.erp.payment.mapper.ChequeMapper;
import lk.epicgreen.erp.payment.repository.ChequeRepository;
import lk.epicgreen.erp.payment.repository.PaymentRepository;
import lk.epicgreen.erp.payment.service.ChequeService;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.repository.CustomerRepository;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ChequeService interface (PDC tracking)
 * 
 * Cheque Status Workflow:
 * RECEIVED → DEPOSITED → CLEARED
 * Can be BOUNCED, RETURNED, or CANCELLED
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ChequeServiceImpl implements ChequeService {

    private final ChequeRepository chequeRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final ChequeMapper chequeMapper;

    @Override
    @Transactional
    public ChequeResponse createCheque(ChequeRequest request) {
        log.info("Creating new Cheque: {}", request.getChequeNumber());

        // Verify payment exists
        Payment payment = findPaymentById(request.getPaymentId());

        // Verify customer exists
        Customer customer = findCustomerById(request.getCustomerId());

        // Create cheque entity
        Cheque cheque = chequeMapper.toEntity(request);
        cheque.setPayment(payment);
        cheque.setCustomer(customer);

        Cheque savedCheque = chequeRepository.save(cheque);
        log.info("Cheque created successfully: {}", savedCheque.getChequeNumber());

        return chequeMapper.toResponse(savedCheque);
    }

    @Override
    @Transactional
    public ChequeResponse updateCheque(Long id, ChequeRequest request) {
        log.info("Updating Cheque: {}", id);

        Cheque cheque = findChequeById(id);

        // Can only update RECEIVED cheques
        if (!canUpdate(id)) {
            throw new InvalidOperationException(
                "Cannot update Cheque. Current status: " + cheque.getStatus() + 
                ". Only RECEIVED cheques can be updated.");
        }

        // Update basic fields
        chequeMapper.updateEntityFromRequest(request, cheque);

        // Update relationships if changed
        if (!cheque.getPayment().getId().equals(request.getPaymentId())) {
            Payment payment = findPaymentById(request.getPaymentId());
            cheque.setPayment(payment);
        }

        if (!cheque.getCustomer().getId().equals(request.getCustomerId())) {
            Customer customer = findCustomerById(request.getCustomerId());
            cheque.setCustomer(customer);
        }

        Cheque updatedCheque = chequeRepository.save(cheque);
        log.info("Cheque updated successfully: {}", updatedCheque.getChequeNumber());

        return chequeMapper.toResponse(updatedCheque);
    }

    @Override
    @Transactional
    public void depositCheque(Long id, LocalDate depositDate) {
        log.info("Depositing Cheque: {} on date: {}", id, depositDate);

        Cheque cheque = findChequeById(id);

        if (!"RECEIVED".equals(cheque.getStatus())) {
            throw new InvalidOperationException(
                "Cannot deposit Cheque. Current status: " + cheque.getStatus() + 
                ". Only RECEIVED cheques can be deposited.");
        }

        cheque.setStatus("DEPOSITED");
        cheque.setDepositDate(depositDate);
        chequeRepository.save(cheque);

        log.info("Cheque deposited successfully: {}", id);
    }

    @Override
    @Transactional
    public void clearCheque(Long id, LocalDate clearanceDate) {
        log.info("Clearing Cheque: {} on date: {}", id, clearanceDate);

        Cheque cheque = findChequeById(id);

        if (!"DEPOSITED".equals(cheque.getStatus())) {
            throw new InvalidOperationException(
                "Cannot clear Cheque. Current status: " + cheque.getStatus() + 
                ". Only DEPOSITED cheques can be cleared.");
        }

        cheque.setStatus("CLEARED");
        cheque.setClearanceDate(clearanceDate);
        chequeRepository.save(cheque);

        log.info("Cheque cleared successfully: {}", id);
    }

    @Override
    @Transactional
    public void bounceCheque(Long id, String bounceReason, BigDecimal bounceCharges) {
        log.info("Bouncing Cheque: {} with reason: {}", id, bounceReason);

        Cheque cheque = findChequeById(id);

        if (!"DEPOSITED".equals(cheque.getStatus())) {
            throw new InvalidOperationException(
                "Cannot bounce Cheque. Current status: " + cheque.getStatus() + 
                ". Only DEPOSITED cheques can be bounced.");
        }

        cheque.setStatus("BOUNCED");
        cheque.setBounceReason(bounceReason);
        cheque.setBounceCharges(bounceCharges != null ? bounceCharges : BigDecimal.ZERO);
        chequeRepository.save(cheque);

        log.info("Cheque bounced successfully: {}", id);
    }

    @Override
    @Transactional
    public void returnCheque(Long id, String reason) {
        log.info("Returning Cheque: {} with reason: {}", id, reason);

        Cheque cheque = findChequeById(id);

        if ("CLEARED".equals(cheque.getStatus()) || "RETURNED".equals(cheque.getStatus())) {
            throw new InvalidOperationException(
                "Cannot return Cheque. Current status: " + cheque.getStatus());
        }

        cheque.setStatus("RETURNED");
        cheque.setRemarks(cheque.getRemarks() != null ? 
            cheque.getRemarks() + "\nReturned: " + reason : 
            "Returned: " + reason);
        chequeRepository.save(cheque);

        log.info("Cheque returned successfully: {}", id);
    }

    @Override
    @Transactional
    public void cancelCheque(Long id, String reason) {
        log.info("Cancelling Cheque: {} with reason: {}", id, reason);

        Cheque cheque = findChequeById(id);

        if (!"RECEIVED".equals(cheque.getStatus())) {
            throw new InvalidOperationException(
                "Cannot cancel Cheque. Current status: " + cheque.getStatus() + 
                ". Only RECEIVED cheques can be cancelled.");
        }

        cheque.setStatus("CANCELLED");
        cheque.setRemarks(cheque.getRemarks() != null ? 
            cheque.getRemarks() + "\nCancelled: " + reason : 
            "Cancelled: " + reason);
        chequeRepository.save(cheque);

        log.info("Cheque cancelled successfully: {}", id);
    }

    @Override
    @Transactional
    public void deleteCheque(Long id) {
        log.info("Deleting Cheque: {}", id);

        if (!canDelete(id)) {
            Cheque cheque = findChequeById(id);
            throw new InvalidOperationException(
                "Cannot delete Cheque. Current status: " + cheque.getStatus() + 
                ". Only RECEIVED cheques can be deleted.");
        }

        chequeRepository.deleteById(id);
        log.info("Cheque deleted successfully: {}", id);
    }

    @Override
    public ChequeResponse getChequeById(Long id) {
        Cheque cheque = findChequeById(id);
        return chequeMapper.toResponse(cheque);
    }

    @Override
    public ChequeResponse getChequeByChequeNumber(String chequeNumber) {
        Cheque cheque = chequeRepository.findByChequeNumber(chequeNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Cheque not found: " + chequeNumber));
        return chequeMapper.toResponse(cheque);
    }

    @Override
    public PageResponse<ChequeResponse> getAllCheques(Pageable pageable) {
        Page<Cheque> chequePage = chequeRepository.findAll(pageable);
        return createPageResponse(chequePage);
    }

    @Override
    public PageResponse<ChequeResponse> getChequesByStatus(String status, Pageable pageable) {
        Page<Cheque> chequePage = chequeRepository.findByStatus(status, pageable);
        return createPageResponse(chequePage);
    }

    @Override
    public List<ChequeResponse> getChequesByPayment(Long paymentId) {
        List<Cheque> cheques = chequeRepository.findByPaymentId(paymentId);
        return cheques.stream()
            .map(chequeMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ChequeResponse> getChequesByCustomer(Long customerId) {
        List<Cheque> cheques = chequeRepository.findByCustomerId(customerId);
        return cheques.stream()
            .map(chequeMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ChequeResponse> getChequesByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Cheque> cheques = chequeRepository.findByChequeDateBetween(startDate, endDate);
        return cheques.stream()
            .map(chequeMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ChequeResponse> getChequesByBank(String bankName) {
        List<Cheque> cheques = chequeRepository.findByBankName(bankName);
        return cheques.stream()
            .map(chequeMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ChequeResponse> getPendingDepositCheques() {
        List<Cheque> cheques = chequeRepository.findByStatus("RECEIVED");
        return cheques.stream()
            .map(chequeMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ChequeResponse> getPendingClearanceCheques() {
        List<Cheque> cheques = chequeRepository.findByStatus("DEPOSITED");
        return cheques.stream()
            .map(chequeMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ChequeResponse> getDueCheques(LocalDate asOfDate) {
        List<Cheque> cheques = chequeRepository.findDueCheques(asOfDate);
        return cheques.stream()
            .map(chequeMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ChequeResponse> getBouncedCheques() {
        List<Cheque> cheques = chequeRepository.findByStatus("BOUNCED");
        return cheques.stream()
            .map(chequeMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<ChequeResponse> searchCheques(String keyword, Pageable pageable) {
        Page<Cheque> chequePage = chequeRepository.searchCheques(keyword, pageable);
        return createPageResponse(chequePage);
    }

    @Override
    public BigDecimal getTotalChequeAmountByCustomer(Long customerId) {
        return chequeRepository.sumChequeAmountByCustomer(customerId)
            .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getTotalPendingClearanceAmount() {
        return chequeRepository.sumPendingClearanceAmount()
            .orElse(BigDecimal.ZERO);
    }

    @Override
    public boolean canDelete(Long id) {
        Cheque cheque = findChequeById(id);
        return "RECEIVED".equals(cheque.getStatus());
    }

    @Override
    public boolean canUpdate(Long id) {
        Cheque cheque = findChequeById(id);
        return "RECEIVED".equals(cheque.getStatus());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private Cheque findChequeById(Long id) {
        return chequeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cheque not found: " + id));
    }

    private Payment findPaymentById(Long id) {
        return paymentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));
    }

    private Customer findCustomerById(Long id) {
        return customerRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
    }

    private PageResponse<ChequeResponse> createPageResponse(Page<Cheque> chequePage) {
        List<ChequeResponse> content = chequePage.getContent().stream()
            .map(chequeMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<ChequeResponse>builder()
            .content(content)
            .pageNumber(chequePage.getNumber())
            .pageSize(chequePage.getSize())
            .totalElements(chequePage.getTotalElements())
            .totalPages(chequePage.getTotalPages())
            .last(chequePage.isLast())
            .first(chequePage.isFirst())
            .empty(chequePage.isEmpty())
            .build();
    }
}
