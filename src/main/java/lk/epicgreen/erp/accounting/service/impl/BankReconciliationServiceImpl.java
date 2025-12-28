package lk.epicgreen.erp.accounting.service.impl;

import lk.epicgreen.erp.accounting.dto.request.BankReconciliationRequest;
import lk.epicgreen.erp.accounting.dto.response.BankReconciliationResponse;
import lk.epicgreen.erp.accounting.entity.BankReconciliation;
import lk.epicgreen.erp.accounting.entity.BankAccount;
import lk.epicgreen.erp.accounting.mapper.BankReconciliationMapper;
import lk.epicgreen.erp.accounting.repository.BankReconciliationRepository;
import lk.epicgreen.erp.accounting.repository.BankAccountRepository;
import lk.epicgreen.erp.accounting.service.BankReconciliationService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BankReconciliationServiceImpl implements BankReconciliationService {

    private final BankReconciliationRepository reconciliationRepository;
    private final BankAccountRepository bankAccountRepository;
    private final BankReconciliationMapper reconciliationMapper;

    @Override
    @Transactional
    public BankReconciliationResponse createReconciliation(BankReconciliationRequest request) {
        log.info("Creating new Bank Reconciliation: {}", request.getReconciliationNumber());

        validateUniqueReconciliationNumber(request.getReconciliationNumber(), null);

        BankAccount bankAccount = bankAccountRepository.findById(request.getBankAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("Bank Account not found: " + request.getBankAccountId()));

        BankReconciliation reconciliation = reconciliationMapper.toEntity(request);
        reconciliation.setBankAccount(bankAccount);

        BankReconciliation savedReconciliation = reconciliationRepository.save(reconciliation);
        log.info("Bank Reconciliation created successfully: {}", savedReconciliation.getReconciliationNumber());

        return reconciliationMapper.toResponse(savedReconciliation);
    }

    @Override
    @Transactional
    public BankReconciliationResponse updateReconciliation(Long id, BankReconciliationRequest request) {
        log.info("Updating Bank Reconciliation: {}", id);

        BankReconciliation reconciliation = findReconciliationById(id);

        if (!canUpdate(id)) {
            throw new InvalidOperationException(
                "Cannot update Bank Reconciliation. Current status: " + reconciliation.getStatus() + 
                ". Only DRAFT reconciliations can be updated.");
        }

        if (!reconciliation.getReconciliationNumber().equals(request.getReconciliationNumber())) {
            validateUniqueReconciliationNumber(request.getReconciliationNumber(), id);
        }

        reconciliationMapper.updateEntityFromRequest(request, reconciliation);

        if (!reconciliation.getBankAccount().getId().equals(request.getBankAccountId())) {
            BankAccount bankAccount = bankAccountRepository.findById(request.getBankAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Bank Account not found: " + request.getBankAccountId()));
            reconciliation.setBankAccount(bankAccount);
        }

        BankReconciliation updatedReconciliation = reconciliationRepository.save(reconciliation);
        log.info("Bank Reconciliation updated successfully: {}", updatedReconciliation.getReconciliationNumber());

        return reconciliationMapper.toResponse(updatedReconciliation);
    }

    @Override
    @Transactional
    public void startReconciliation(Long id) {
        log.info("Starting Bank Reconciliation: {}", id);

        BankReconciliation reconciliation = findReconciliationById(id);

        if (!"DRAFT".equals(reconciliation.getStatus())) {
            throw new InvalidOperationException(
                "Cannot start reconciliation. Current status: " + reconciliation.getStatus());
        }

        reconciliation.setStatus("IN_PROGRESS");
        reconciliationRepository.save(reconciliation);

        log.info("Bank Reconciliation started successfully: {}", id);
    }

    @Override
    @Transactional
    public void completeReconciliation(Long id, Long reconciledBy) {
        log.info("Completing Bank Reconciliation: {} by user: {}", id, reconciledBy);

        BankReconciliation reconciliation = findReconciliationById(id);

        if (!"IN_PROGRESS".equals(reconciliation.getStatus())) {
            throw new InvalidOperationException(
                "Cannot complete reconciliation. Current status: " + reconciliation.getStatus());
        }

        reconciliation.setStatus("COMPLETED");
        reconciliation.setReconciledBy(reconciledBy);
        reconciliation.setReconciledAt(LocalDateTime.now());
        reconciliationRepository.save(reconciliation);

        log.info("Bank Reconciliation completed successfully: {}", id);
    }

    @Override
    @Transactional
    public void deleteReconciliation(Long id) {
        log.info("Deleting Bank Reconciliation: {}", id);

        if (!canDelete(id)) {
            BankReconciliation reconciliation = findReconciliationById(id);
            throw new InvalidOperationException(
                "Cannot delete Bank Reconciliation. Current status: " + reconciliation.getStatus() + 
                ". Only DRAFT reconciliations can be deleted.");
        }

        reconciliationRepository.deleteById(id);
        log.info("Bank Reconciliation deleted successfully: {}", id);
    }

    @Override
    public BankReconciliationResponse getReconciliationById(Long id) {
        BankReconciliation reconciliation = findReconciliationById(id);
        return reconciliationMapper.toResponse(reconciliation);
    }

    @Override
    public BankReconciliationResponse getReconciliationByNumber(String reconciliationNumber) {
        BankReconciliation reconciliation = reconciliationRepository.findByReconciliationNumber(reconciliationNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Bank Reconciliation not found: " + reconciliationNumber));
        return reconciliationMapper.toResponse(reconciliation);
    }

    @Override
    public PageResponse<BankReconciliationResponse> getAllReconciliations(Pageable pageable) {
        Page<BankReconciliation> reconciliationPage = reconciliationRepository.findAll(pageable);
        return createPageResponse(reconciliationPage);
    }

    @Override
    public PageResponse<BankReconciliationResponse> getReconciliationsByStatus(String status, Pageable pageable) {
        Page<BankReconciliation> reconciliationPage = reconciliationRepository.findByStatus(status, pageable);
        return createPageResponse(reconciliationPage);
    }

    @Override
    public List<BankReconciliationResponse> getReconciliationsByBankAccount(Long bankAccountId) {
        List<BankReconciliation> reconciliations = reconciliationRepository.findByBankAccountId(bankAccountId);
        return reconciliations.stream()
            .map(reconciliationMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<BankReconciliationResponse> getReconciliationsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<BankReconciliation> reconciliations = reconciliationRepository.findByStatementDateBetween(startDate, endDate);
        return reconciliations.stream()
            .map(reconciliationMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<BankReconciliationResponse> searchReconciliations(String keyword, Pageable pageable) {
        Page<BankReconciliation> reconciliationPage = reconciliationRepository.searchReconciliations(keyword, pageable);
        return createPageResponse(reconciliationPage);
    }

    @Override
    public boolean canDelete(Long id) {
        BankReconciliation reconciliation = findReconciliationById(id);
        return "DRAFT".equals(reconciliation.getStatus());
    }

    @Override
    public boolean canUpdate(Long id) {
        BankReconciliation reconciliation = findReconciliationById(id);
        return "DRAFT".equals(reconciliation.getStatus());
    }

    private void validateUniqueReconciliationNumber(String reconciliationNumber, Long excludeId) {
        boolean exists;
        if (excludeId != null) {
            exists = reconciliationRepository.existsByReconciliationNumberAndIdNot(reconciliationNumber, excludeId);
        } else {
            exists = reconciliationRepository.existsByReconciliationNumber(reconciliationNumber);
        }

        if (exists) {
            throw new DuplicateResourceException("Bank Reconciliation with number '" + reconciliationNumber + "' already exists");
        }
    }

    private BankReconciliation findReconciliationById(Long id) {
        return reconciliationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bank Reconciliation not found: " + id));
    }

    private PageResponse<BankReconciliationResponse> createPageResponse(Page<BankReconciliation> reconciliationPage) {
        List<BankReconciliationResponse> content = reconciliationPage.getContent().stream()
            .map(reconciliationMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<BankReconciliationResponse>builder()
            .content(content)
            .pageNumber(reconciliationPage.getNumber())
            .pageSize(reconciliationPage.getSize())
            .totalElements(reconciliationPage.getTotalElements())
            .totalPages(reconciliationPage.getTotalPages())
            .last(reconciliationPage.isLast())
            .first(reconciliationPage.isFirst())
            .empty(reconciliationPage.isEmpty())
            .build();
    }
}
