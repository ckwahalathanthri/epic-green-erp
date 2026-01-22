package lk.epicgreen.erp.accounting.service.impl;

import lk.epicgreen.erp.accounting.dto.request.BankAccountRequest;
import lk.epicgreen.erp.accounting.dto.response.BankAccountResponse;
import lk.epicgreen.erp.accounting.entity.BankAccount;
import lk.epicgreen.erp.accounting.entity.ChartOfAccounts;
import lk.epicgreen.erp.accounting.mapper.BankAccountMapper;
import lk.epicgreen.erp.accounting.repository.BankAccountRepository;
import lk.epicgreen.erp.accounting.repository.ChartOfAccountsRepository;
import lk.epicgreen.erp.accounting.service.BankAccountService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final ChartOfAccountsRepository chartOfAccountsRepository;
    private final BankAccountMapper bankAccountMapper;

    @Override
    @Transactional
    public BankAccountResponse createBankAccount(BankAccountRequest request) {
        log.info("Creating new Bank Account: {}", request.getAccountNumber());

        validateUniqueAccountNumber(request.getAccountNumber(), null);

        ChartOfAccounts glAccount = chartOfAccountsRepository.findById(request.getGlAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("GL Account not found: " + request.getGlAccountId()));

        BankAccount account = bankAccountMapper.toEntity(request);
        account.setGlAccount(glAccount);

        BankAccount savedAccount = bankAccountRepository.save(account);
        log.info("Bank Account created successfully: {}", savedAccount.getAccountNumber());

        return bankAccountMapper.toResponse(savedAccount);
    }

    @Override
    @Transactional
    public BankAccountResponse updateBankAccount(Long id, BankAccountRequest request) {
        log.info("Updating Bank Account: {}", id);

        BankAccount account = findBankAccountById(id);

        if (!account.getAccountNumber().equals(request.getAccountNumber())) {
            validateUniqueAccountNumber(request.getAccountNumber(), id);
        }

        bankAccountMapper.updateEntityFromRequest(request, account);

        if (!account.getGlAccount().getId().equals(request.getGlAccountId())) {
            ChartOfAccounts glAccount = chartOfAccountsRepository.findById(request.getGlAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("GL Account not found: " + request.getGlAccountId()));
            account.setGlAccount(glAccount);
        }

        BankAccount updatedAccount = bankAccountRepository.save(account);
        log.info("Bank Account updated successfully: {}", updatedAccount.getAccountNumber());

        return bankAccountMapper.toResponse(updatedAccount);
    }

    @Override
    @Transactional
    public void deleteBankAccount(Long id) {
        log.info("Deleting Bank Account: {}", id);

        if (!canDelete(id)) {
            throw new ResourceNotFoundException("Cannot delete bank account. It may have transactions.");
        }

        bankAccountRepository.deleteById(id);
        log.info("Bank Account deleted successfully: {}", id);
    }

    @Override
    public BankAccountResponse getBankAccountById(Long id) {
        BankAccount account = findBankAccountById(id);
        return bankAccountMapper.toResponse(account);
    }

    @Override
    public BankAccountResponse getBankAccountByNumber(String accountNumber) {
        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Bank Account not found: " + accountNumber));
        return bankAccountMapper.toResponse(account);
    }

    @Override
    public PageResponse<BankAccountResponse> getAllBankAccounts(Pageable pageable) {
        Page<BankAccount> accountPage = bankAccountRepository.findAll(pageable);
        return createPageResponse(accountPage);
    }

    @Override
    public List<BankAccountResponse> getBankAccountsByBank(String bankName) {
        List<BankAccount> accounts = bankAccountRepository.findByBankName(bankName);
        return accounts.stream()
            .map(bankAccountMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<BankAccountResponse> getBankAccountsByType(String accountType) {
        List<BankAccount> accounts = bankAccountRepository.findByAccountType(accountType);
        return accounts.stream()
            .map(bankAccountMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<BankAccountResponse> getActiveBankAccounts() {
        List<BankAccount> accounts = bankAccountRepository.findByIsActiveTrue();
        return accounts.stream()
            .map(bankAccountMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<BankAccountResponse> searchBankAccounts(String keyword, Pageable pageable) {
        Page<BankAccount> accountPage = bankAccountRepository.searchBankAccounts(keyword,null,null,null,null,pageable);
        return createPageResponse(accountPage);
    }

    @Override
    public boolean canDelete(Long id) {
        return true; // Add logic to check if has reconciliations or transactions
    }

    private void validateUniqueAccountNumber(String accountNumber, Long excludeId) {
        boolean exists;
        if (excludeId != null) {
            exists = bankAccountRepository.existsByAccountNumberAndIdNot(accountNumber, excludeId);
        } else {
            exists = bankAccountRepository.existsByAccountNumber(accountNumber);
        }

        if (exists) {
            throw new DuplicateResourceException("Bank Account with number '" + accountNumber + "' already exists");
        }
    }

    private BankAccount findBankAccountById(Long id) {
        return bankAccountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bank Account not found: " + id));
    }

    private PageResponse<BankAccountResponse> createPageResponse(Page<BankAccount> accountPage) {
        List<BankAccountResponse> content = accountPage.getContent().stream()
            .map(bankAccountMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<BankAccountResponse>builder()
            .content(content)
            .pageNumber(accountPage.getNumber())
            .pageSize(accountPage.getSize())
            .totalElements(accountPage.getTotalElements())
            .totalPages(accountPage.getTotalPages())
            .last(accountPage.isLast())
            .first(accountPage.isFirst())
            .empty(accountPage.isEmpty())
            .build();
    }
}
