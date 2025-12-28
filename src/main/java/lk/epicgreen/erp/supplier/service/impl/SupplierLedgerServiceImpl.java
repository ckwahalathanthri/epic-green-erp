package lk.epicgreen.erp.supplier.service.impl;

import lk.epicgreen.erp.supplier.dto.request.SupplierLedgerRequest;
import lk.epicgreen.erp.supplier.dto.response.SupplierLedgerResponse;
import lk.epicgreen.erp.supplier.entity.Supplier;
import lk.epicgreen.erp.supplier.entity.SupplierLedger;
import lk.epicgreen.erp.supplier.mapper.SupplierLedgerMapper;
import lk.epicgreen.erp.supplier.repository.SupplierRepository;
import lk.epicgreen.erp.supplier.repository.SupplierLedgerRepository;
import lk.epicgreen.erp.supplier.service.SupplierLedgerService;
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
 * Implementation of SupplierLedgerService interface
 * 
 * CRITICAL: SupplierLedger is IMMUTABLE
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
public class SupplierLedgerServiceImpl implements SupplierLedgerService {

    private final SupplierLedgerRepository supplierLedgerRepository;
    private final SupplierRepository supplierRepository;
    private final SupplierLedgerMapper supplierLedgerMapper;

    @Override
    @Transactional
    public SupplierLedgerResponse createLedgerEntry(SupplierLedgerRequest request) {
        log.info("Creating new supplier ledger entry for supplier: {}", request.getSupplierId());

        // Verify supplier exists
        Supplier supplier = findSupplierById(request.getSupplierId());

        // Create ledger entry
        SupplierLedger ledgerEntry = supplierLedgerMapper.toEntity(request);
        ledgerEntry.setSupplier(supplier);

        // Calculate running balance
        BigDecimal currentBalance = getSupplierBalance(request.getSupplierId());
        BigDecimal newBalance = currentBalance
            .add(request.getDebitAmount())
            .subtract(request.getCreditAmount());
        
        ledgerEntry.setBalance(newBalance);

        // Save ledger entry
        SupplierLedger savedEntry = supplierLedgerRepository.save(ledgerEntry);

        // Update supplier current balance
        supplier.setCurrentBalance(newBalance);
        supplierRepository.save(supplier);

        log.info("Supplier ledger entry created successfully. New balance: {}", newBalance);

        return supplierLedgerMapper.toResponse(savedEntry);
    }

    @Override
    public SupplierLedgerResponse getLedgerEntryById(Long id) {
        SupplierLedger ledgerEntry = findLedgerEntryById(id);
        return supplierLedgerMapper.toResponse(ledgerEntry);
    }

    @Override
    public PageResponse<SupplierLedgerResponse> getLedgerEntriesBySupplier(Long supplierId, Pageable pageable) {
        Page<SupplierLedger> ledgerPage = supplierLedgerRepository.findBySupplierId(supplierId, pageable);
        return createPageResponse(ledgerPage);
    }

    @Override
    public List<SupplierLedgerResponse> getLedgerEntriesBySupplierAndDateRange(
            Long supplierId, LocalDate startDate, LocalDate endDate) {
        List<SupplierLedger> entries = supplierLedgerRepository
            .findBySupplierIdAndTransactionDateBetween(supplierId, startDate, endDate);
        return entries.stream()
            .map(supplierLedgerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<SupplierLedgerResponse> getLedgerEntriesByType(
            String transactionType, Pageable pageable) {
        Page<SupplierLedger> ledgerPage = supplierLedgerRepository
            .findByTransactionType(transactionType, pageable);
        return createPageResponse(ledgerPage);
    }

    @Override
    public List<SupplierLedgerResponse> getLedgerEntriesByReference(
            String referenceType, Long referenceId) {
        List<SupplierLedger> entries = supplierLedgerRepository
            .findByReferenceTypeAndReferenceId(referenceType, referenceId);
        return entries.stream()
            .map(supplierLedgerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getSupplierBalance(Long supplierId) {
        return supplierLedgerRepository.getSupplierBalance(supplierId)
            .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getSupplierBalanceAsOfDate(Long supplierId, LocalDate asOfDate) {
        return supplierLedgerRepository.getSupplierBalanceAsOfDate(supplierId, asOfDate)
            .orElse(BigDecimal.ZERO);
    }

    @Override
    public PageResponse<SupplierLedgerResponse> getAllLedgerEntries(Pageable pageable) {
        Page<SupplierLedger> ledgerPage = supplierLedgerRepository.findAll(pageable);
        return createPageResponse(ledgerPage);
    }

    @Override
    public List<SupplierLedgerResponse> getPurchaseEntriesBySupplier(Long supplierId) {
        List<SupplierLedger> entries = supplierLedgerRepository
            .findBySupplierIdAndTransactionType(supplierId, "PURCHASE");
        return entries.stream()
            .map(supplierLedgerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SupplierLedgerResponse> getPaymentEntriesBySupplier(Long supplierId) {
        List<SupplierLedger> entries = supplierLedgerRepository
            .findBySupplierIdAndTransactionType(supplierId, "PAYMENT");
        return entries.stream()
            .map(supplierLedgerMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public SupplierLedgerSummary getLedgerSummary(Long supplierId) {
        Supplier supplier = findSupplierById(supplierId);
        
        BigDecimal totalPurchases = supplierLedgerRepository
            .getTotalBySupplierAndType(supplierId, "PURCHASE")
            .orElse(BigDecimal.ZERO);
        
        BigDecimal totalPayments = supplierLedgerRepository
            .getTotalBySupplierAndType(supplierId, "PAYMENT")
            .orElse(BigDecimal.ZERO);
        
        BigDecimal totalDebits = supplierLedgerRepository
            .getTotalDebitsBySupplier(supplierId)
            .orElse(BigDecimal.ZERO);
        
        BigDecimal totalCredits = supplierLedgerRepository
            .getTotalCreditsBySupplier(supplierId)
            .orElse(BigDecimal.ZERO);
        
        BigDecimal currentBalance = getSupplierBalance(supplierId);
        
        Integer transactionCount = supplierLedgerRepository.countBySupplierId(supplierId);

        return new SupplierLedgerSummary(
            supplier.getId(),
            supplier.getSupplierName(),
            totalPurchases,
            totalPayments,
            totalDebits,
            totalCredits,
            currentBalance,
            transactionCount
        );
    }

    @Override
    public SupplierLedgerSummary getLedgerSummaryForDateRange(
            Long supplierId, LocalDate startDate, LocalDate endDate) {
        Supplier supplier = findSupplierById(supplierId);
        
        BigDecimal totalPurchases = supplierLedgerRepository
            .getTotalBySupplierAndTypeAndDateRange(supplierId, "PURCHASE", startDate, endDate)
            .orElse(BigDecimal.ZERO);
        
        BigDecimal totalPayments = supplierLedgerRepository
            .getTotalBySupplierAndTypeAndDateRange(supplierId, "PAYMENT", startDate, endDate)
            .orElse(BigDecimal.ZERO);
        
        BigDecimal totalDebits = supplierLedgerRepository
            .getTotalDebitsBySupplierAndDateRange(supplierId, startDate, endDate)
            .orElse(BigDecimal.ZERO);
        
        BigDecimal totalCredits = supplierLedgerRepository
            .getTotalCreditsBySupplierAndDateRange(supplierId, startDate, endDate)
            .orElse(BigDecimal.ZERO);
        
        BigDecimal balanceAsOfEnd = getSupplierBalanceAsOfDate(supplierId, endDate);
        
        Integer transactionCount = supplierLedgerRepository
            .countBySupplierIdAndTransactionDateBetween(supplierId, startDate, endDate);

        return new SupplierLedgerSummary(
            supplier.getId(),
            supplier.getSupplierName(),
            totalPurchases,
            totalPayments,
            totalDebits,
            totalCredits,
            balanceAsOfEnd,
            transactionCount
        );
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private SupplierLedger findLedgerEntryById(Long id) {
        return supplierLedgerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier ledger entry not found: " + id));
    }

    private Supplier findSupplierById(Long id) {
        return supplierRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier not found: " + id));
    }

    private PageResponse<SupplierLedgerResponse> createPageResponse(Page<SupplierLedger> ledgerPage) {
        List<SupplierLedgerResponse> content = ledgerPage.getContent().stream()
            .map(supplierLedgerMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<SupplierLedgerResponse>builder()
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
