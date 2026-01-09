package lk.epicgreen.erp.accounting.service.impl;

import lk.epicgreen.erp.accounting.dto.request.ChartOfAccountsRequest;
import lk.epicgreen.erp.accounting.dto.response.ChartOfAccountsResponse;
import lk.epicgreen.erp.accounting.entity.ChartOfAccounts;
import lk.epicgreen.erp.accounting.mapper.ChartOfAccountsMapper;
import lk.epicgreen.erp.accounting.repository.ChartOfAccountsRepository;
import lk.epicgreen.erp.accounting.service.ChartOfAccountsService;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ChartOfAccountsServiceImpl implements ChartOfAccountsService {

    private final ChartOfAccountsRepository accountRepository;
    private final ChartOfAccountsMapper accountMapper;

    @Override
    @Transactional
    public ChartOfAccountsResponse createAccount(ChartOfAccountsRequest request) {
        log.info("Creating new Account: {}", request.getAccountCode());

        validateUniqueAccountCode(request.getAccountCode(), null);

        ChartOfAccounts account = accountMapper.toEntity(request);

        if (request.getParentAccountId() != null) {
            ChartOfAccounts parent = findAccountById(request.getParentAccountId());
            account.setParentAccount(parent);
        }

        ChartOfAccounts savedAccount = accountRepository.save(account);
        log.info("Account created successfully: {}", savedAccount.getAccountCode());

        return accountMapper.toResponse(savedAccount);
    }

    @Override
    @Transactional
    public ChartOfAccountsResponse updateAccount(Long id, ChartOfAccountsRequest request) {
        log.info("Updating Account: {}", id);

        ChartOfAccounts account = findAccountById(id);

        if (!account.getAccountCode().equals(request.getAccountCode())) {
            validateUniqueAccountCode(request.getAccountCode(), id);
        }

        accountMapper.updateEntityFromRequest(request, account);

        if (request.getParentAccountId() != null) {
            if (account.getParentAccount() == null || 
                !account.getParentAccount().getId().equals(request.getParentAccountId())) {
                ChartOfAccounts parent = findAccountById(request.getParentAccountId());
                account.setParentAccount(parent);
            }
        } else {
            account.setParentAccount(null);
        }

        ChartOfAccounts updatedAccount = accountRepository.save(account);
        log.info("Account updated successfully: {}", updatedAccount.getAccountCode());

        return accountMapper.toResponse(updatedAccount);
    }

    @Override
    @Transactional
    public void deleteAccount(Long id) {
        log.info("Deleting Account: {}", id);

        if (!canDelete(id)) {
            throw new InvalidOperationException(
                "Cannot delete account. It has child accounts or is used in transactions.");
        }

        accountRepository.deleteById(id);
        log.info("Account deleted successfully: {}", id);
    }

    @Override
    public ChartOfAccountsResponse getAccountById(Long id) {
        ChartOfAccounts account = findAccountById(id);
        return accountMapper.toResponse(account);
    }

    @Override
    public ChartOfAccountsResponse getAccountByCode(String accountCode) {
        ChartOfAccounts account = accountRepository.findByAccountCode(accountCode)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountCode));
        return accountMapper.toResponse(account);
    }

    @Override
    public PageResponse<ChartOfAccountsResponse> getAllAccounts(Pageable pageable) {
        Page<ChartOfAccounts> accountPage = accountRepository.findAll(pageable);
        return createPageResponse(accountPage);
    }

    @Override
    public List<ChartOfAccountsResponse> getAccountsByType(String accountType) {
        List<ChartOfAccounts> accounts = accountRepository.findByAccountType(accountType);
        return accounts.stream()
            .map(accountMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ChartOfAccountsResponse> getAccountsByCategory(String accountCategory) {
        List<ChartOfAccounts> accounts = accountRepository.findByAccountCategory(accountCategory);
        return accounts.stream()
            .map(accountMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ChartOfAccountsResponse> getChildAccounts(Long parentAccountId) {
        List<ChartOfAccounts> accounts = accountRepository.findByParentAccountId(parentAccountId);
        return accounts.stream()
            .map(accountMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ChartOfAccountsResponse> getActiveAccounts() {
        List<ChartOfAccounts> accounts = accountRepository.findByIsActiveTrue();
        return accounts.stream()
            .map(accountMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ChartOfAccountsResponse> getGroupAccounts() {
        List<ChartOfAccounts> accounts = accountRepository.findByIsGroupAccountTrue();
        return accounts.stream()
            .map(accountMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<ChartOfAccountsResponse> searchAccounts(String keyword, Pageable pageable) {
        Page<ChartOfAccounts> accountPage = accountRepository.searchAccounts(keyword,null,null,null,null,pageable);
        return createPageResponse(accountPage);
    }

    @Override
    public boolean canDelete(Long id) {
        ChartOfAccounts account = findAccountById(id);
        
        List<ChartOfAccounts> children = accountRepository.findByParentAccountId(id);
        if (!children.isEmpty()) {
            return false;
        }
        
        return true;
    }

    private void validateUniqueAccountCode(String accountCode, Long excludeId) {
        boolean exists;
        if (excludeId != null) {
            exists = accountRepository.existsByAccountCodeAndIdNot(accountCode, excludeId);
        } else {
            exists = accountRepository.existsByAccountCode(accountCode);
        }

        if (exists) {
            throw new DuplicateResourceException("Account with code '" + accountCode + "' already exists");
        }
    }

    private ChartOfAccounts findAccountById(Long id) {
        return accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + id));
    }

    private PageResponse<ChartOfAccountsResponse> createPageResponse(Page<ChartOfAccounts> accountPage) {
        List<ChartOfAccountsResponse> content = accountPage.getContent().stream()
            .map(accountMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<ChartOfAccountsResponse>builder()
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
