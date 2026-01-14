package lk.epicgreen.erp.customer.service.impl;

import lk.epicgreen.erp.customer.dto.request.CustomerLedgerRequest;
import lk.epicgreen.erp.customer.dto.response.CustomerLedgerResponse;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.entity.CustomerLedger;
import lk.epicgreen.erp.customer.mapper.CustomerLedgerMapper;
import lk.epicgreen.erp.customer.repository.CustomerRepository;
import lk.epicgreen.erp.customer.repository.CustomerLedgerRepository;
import lk.epicgreen.erp.customer.service.CustomerLedgerService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
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
 * Implementation of CustomerLedgerService interface
 * 
 * CRITICAL: CustomerLedger is IMMUTABLE
 * - CREATE: Allowed with automatic balance calculation
 * - READ: Allowed for all query operations
 * - UPDATE: NOT IMPLEMENTED (financial audit requirement)
 * - DELETE: NOT IMPLEMENTED (financial audit requirement)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CustomerLedgerServiceImpl implements CustomerLedgerService {

    private final CustomerLedgerRepository customerLedgerRepository;
    private final CustomerRepository customerRepository;
    private final CustomerLedgerMapper customerLedgerMapper;

    @Override
    @Transactional
    public CustomerLedgerResponse createLedgerEntry(CustomerLedgerRequest request) {
        log.info("Creating new customer ledger entry for customer: {}", request.getCustomerId());

        // Verify customer exists
        Customer customer = findCustomerById(request.getCustomerId());

        // Create ledger entry
        CustomerLedger ledgerEntry = customerLedgerMapper.toEntity(request);
        ledgerEntry.setCustomer(customer);

        // Calculate running balance
        BigDecimal currentBalance = getCustomerBalance(request.getCustomerId());
        BigDecimal newBalance = currentBalance
            .add(request.getDebitAmount())
            .subtract(request.getCreditAmount());
        
        ledgerEntry.setBalance(newBalance);

        // Save ledger entry
        CustomerLedger savedEntry = customerLedgerRepository.save(ledgerEntry);

        // Update customer current balance
        customer.setCurrentBalance(newBalance);
        customerRepository.save(customer);

        log.info("Customer ledger entry created successfully. New balance: {}", newBalance);

        return customerLedgerMapper.toResponse(savedEntry);
    }

    @Override
    public CustomerLedgerResponse getLedgerEntryById(Long id) {
        CustomerLedger ledgerEntry = findLedgerEntryById(id);
        return customerLedgerMapper.toResponse(ledgerEntry);
    }

    @Override
    public PageResponse<CustomerLedgerResponse> getLedgerEntriesByCustomer(Long customerId, Pageable pageable) {
        Page<CustomerLedger> ledgerPage = customerLedgerRepository.findByCustomerId(customerId, pageable);
        return createPageResponse(ledgerPage);
    }

    @Override
    public List<CustomerLedgerResponse> getLedgerEntriesByCustomerAndDateRange(
            Long customerId, LocalDate startDate, LocalDate endDate) {
        List<CustomerLedger> entries = customerLedgerRepository
            .findByCustomerIdAndTransactionDateBetween(customerId, startDate, endDate);
        return entries.stream()
            .map(customerLedgerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<CustomerLedgerResponse> getLedgerEntriesByType(
            String transactionType, Pageable pageable) {
        Page<CustomerLedger> ledgerPage = customerLedgerRepository
            .findByTransactionType(transactionType, pageable);
        return createPageResponse(ledgerPage);
    }

    @Override
    public List<CustomerLedgerResponse> getLedgerEntriesByReference(
            String referenceType, Long referenceId) {
        List<CustomerLedger> entries = customerLedgerRepository
            .findByReferenceTypeAndReferenceId(referenceType, referenceId);
        return entries.stream()
            .map(customerLedgerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getCustomerBalance(Long customerId) {
        return customerLedgerRepository.getCustomerBalance(customerId)
            .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getCustomerBalanceAsOfDate(Long customerId, LocalDate asOfDate) {
        return customerLedgerRepository.getCustomerBalanceAsOfDate(customerId, asOfDate)
            .orElse(BigDecimal.ZERO);
    }

    @Override
    public PageResponse<CustomerLedgerResponse> getAllLedgerEntries(Pageable pageable) {
        Page<CustomerLedger> ledgerPage = customerLedgerRepository.findAll(pageable);
        return createPageResponse(ledgerPage);
    }

    @Override
    public List<CustomerLedgerResponse> getSaleEntriesByCustomer(Long customerId) {
        List<CustomerLedger> entries = customerLedgerRepository
            .findByCustomerIdAndTransactionType(customerId, "SALE");
        return entries.stream()
            .map(customerLedgerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerLedgerResponse> getPaymentEntriesByCustomer(Long customerId) {
        List<CustomerLedger> entries = customerLedgerRepository
            .findByCustomerIdAndTransactionType(customerId, "PAYMENT");
        return entries.stream()
            .map(customerLedgerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public CustomerLedgerSummary getLedgerSummary(Long customerId) {
        Customer customer = findCustomerById(customerId);
        
        BigDecimal totalSales = customerLedgerRepository
            .getTotalByCustomerAndType(customerId, "SALE")
            .orElse(BigDecimal.ZERO);
        
        BigDecimal totalPayments = customerLedgerRepository
            .getTotalByCustomerAndType(customerId, "PAYMENT")
            .orElse(BigDecimal.ZERO);
        
        BigDecimal totalDebits = customerLedgerRepository
            .getTotalDebitsByCustomer(customerId)
            .orElse(BigDecimal.ZERO);
        
        BigDecimal totalCredits = customerLedgerRepository
            .getTotalCreditsByCustomer(customerId)
            .orElse(BigDecimal.ZERO);
        
        BigDecimal currentBalance = getCustomerBalance(customerId);
        
        long transactionCount = customerLedgerRepository.countByCustomerId(customerId);

        return new CustomerLedgerSummary(
            customer.getId(),
            customer.getCustomerName(),
            totalSales,
            totalPayments,
            totalDebits,
            totalCredits,
            currentBalance,
            transactionCount
        );
    }

    @Override
    public CustomerLedgerSummary getLedgerSummaryForDateRange(
            Long customerId, LocalDate startDate, LocalDate endDate) {
        Customer customer = findCustomerById(customerId);
        
        BigDecimal totalSales = customerLedgerRepository
            .getTotalByCustomerAndTypeAndDateRange(customerId, "SALE", startDate, endDate)
            .orElse(BigDecimal.ZERO);
        
        BigDecimal totalPayments = customerLedgerRepository
            .getTotalByCustomerAndTypeAndDateRange(customerId, "PAYMENT", startDate, endDate)
            .orElse(BigDecimal.ZERO);
        
        BigDecimal totalDebits = customerLedgerRepository
            .getTotalDebitsByCustomerAndDateRange(customerId, startDate, endDate)
            .orElse(BigDecimal.ZERO);
        
        BigDecimal totalCredits = customerLedgerRepository
            .getTotalCreditsByCustomerAndDateRange(customerId, startDate, endDate)
            .orElse(BigDecimal.ZERO);
        
        BigDecimal balanceAsOfEnd = getCustomerBalanceAsOfDate(customerId, endDate);
        
        Integer transactionCount = customerLedgerRepository
            .countByCustomerIdAndTransactionDateBetween(customerId, startDate, endDate);

        return new CustomerLedgerSummary(
            customer.getId(),
            customer.getCustomerName(),
            totalSales,
            totalPayments,
            totalDebits,
            totalCredits,
            balanceAsOfEnd,
            transactionCount
        );
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private CustomerLedger findLedgerEntryById(Long id) {
        return customerLedgerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer ledger entry not found: " + id));
    }

    private Customer findCustomerById(Long id) {
        return customerRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
    }

    private PageResponse<CustomerLedgerResponse> createPageResponse(Page<CustomerLedger> ledgerPage) {
        List<CustomerLedgerResponse> content = ledgerPage.getContent().stream()
            .map(customerLedgerMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<CustomerLedgerResponse>builder()
            .content(content)
            .pageNumber(ledgerPage.getNumber())
            .pageSize(ledgerPage.getSize())
            .totalElements(ledgerPage.getTotalElements())
            .totalPages(ledgerPage.getTotalPages())
            .last(ledgerPage.isLast())
            .first(ledgerPage.isFirst())
            .empty(ledgerPage.isEmpty())
            .build();
    }
}
