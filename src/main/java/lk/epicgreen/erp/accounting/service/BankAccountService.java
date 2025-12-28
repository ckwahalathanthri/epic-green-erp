package lk.epicgreen.erp.accounting.service;

import lk.epicgreen.erp.accounting.dto.request.BankAccountRequest;
import lk.epicgreen.erp.accounting.dto.response.BankAccountResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for Bank Account entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface BankAccountService {

    BankAccountResponse createBankAccount(BankAccountRequest request);
    BankAccountResponse updateBankAccount(Long id, BankAccountRequest request);
    void deleteBankAccount(Long id);
    
    BankAccountResponse getBankAccountById(Long id);
    BankAccountResponse getBankAccountByNumber(String accountNumber);
    PageResponse<BankAccountResponse> getAllBankAccounts(Pageable pageable);
    
    List<BankAccountResponse> getBankAccountsByBank(String bankName);
    List<BankAccountResponse> getBankAccountsByType(String accountType);
    List<BankAccountResponse> getActiveBankAccounts();
    
    PageResponse<BankAccountResponse> searchBankAccounts(String keyword, Pageable pageable);
    boolean canDelete(Long id);
}
