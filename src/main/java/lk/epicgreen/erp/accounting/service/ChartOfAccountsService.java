package lk.epicgreen.erp.accounting.service;

import lk.epicgreen.erp.accounting.dto.request.ChartOfAccountsRequest;
import lk.epicgreen.erp.accounting.dto.response.ChartOfAccountsResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for Chart of Accounts entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface ChartOfAccountsService {

    ChartOfAccountsResponse createAccount(ChartOfAccountsRequest request);
    ChartOfAccountsResponse updateAccount(Long id, ChartOfAccountsRequest request);
    void deleteAccount(Long id);
    
    ChartOfAccountsResponse getAccountById(Long id);
    ChartOfAccountsResponse getAccountByCode(String accountCode);
    PageResponse<ChartOfAccountsResponse> getAllAccounts(Pageable pageable);
    
    List<ChartOfAccountsResponse> getAccountsByType(String accountType);
    List<ChartOfAccountsResponse> getAccountsByCategory(String accountCategory);
    List<ChartOfAccountsResponse> getChildAccounts(Long parentAccountId);
    List<ChartOfAccountsResponse> getActiveAccounts();
    List<ChartOfAccountsResponse> getGroupAccounts();
    
    PageResponse<ChartOfAccountsResponse> searchAccounts(String keyword, Pageable pageable);
    boolean canDelete(Long id);
}
