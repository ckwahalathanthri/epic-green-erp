package lk.epicgreen.erp.accounting.mapper;

import lk.epicgreen.erp.accounting.dto.request.ChartOfAccountsRequest;
import lk.epicgreen.erp.accounting.dto.response.ChartOfAccountsResponse;
import lk.epicgreen.erp.accounting.entity.ChartOfAccounts;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapper for ChartOfAccounts entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class ChartOfAccountsMapper {

    public ChartOfAccounts toEntity(ChartOfAccountsRequest request) {
        if (request == null) {
            return null;
        }

        return ChartOfAccounts.builder()
            .accountCode(request.getAccountCode())
            .accountName(request.getAccountName())
            .accountType(request.getAccountType())
            .accountCategory(request.getAccountCategory())
            .isGroupAccount(request.getIsGroupAccount() != null ? request.getIsGroupAccount() : false)
            .isControlAccount(request.getIsControlAccount() != null ? request.getIsControlAccount() : false)
            .openingBalance(request.getOpeningBalance() != null ? request.getOpeningBalance() : BigDecimal.ZERO)
            .openingBalanceType(request.getOpeningBalanceType() != null ? request.getOpeningBalanceType() : "DEBIT")
            .currentBalance(request.getCurrentBalance() != null ? request.getCurrentBalance() : BigDecimal.ZERO)
            .isActive(request.getIsActive() != null ? request.getIsActive() : true)
            .build();
    }

    public void updateEntityFromRequest(ChartOfAccountsRequest request, ChartOfAccounts account) {
        if (request == null || account == null) {
            return;
        }

        account.setAccountCode(request.getAccountCode());
        account.setAccountName(request.getAccountName());
        account.setAccountType(request.getAccountType());
        account.setAccountCategory(request.getAccountCategory());
        account.setIsGroupAccount(request.getIsGroupAccount());
        account.setIsControlAccount(request.getIsControlAccount());
        account.setOpeningBalance(request.getOpeningBalance());
        account.setOpeningBalanceType(request.getOpeningBalanceType());
        account.setCurrentBalance(request.getCurrentBalance());
        account.setIsActive(request.getIsActive());
    }

    public ChartOfAccountsResponse toResponse(ChartOfAccounts account) {
        if (account == null) {
            return null;
        }

        return ChartOfAccountsResponse.builder()
            .id(account.getId())
            .accountCode(account.getAccountCode())
            .accountName(account.getAccountName())
            .accountType(account.getAccountType())
            .accountCategory(account.getAccountCategory())
            .parentAccountId(account.getParentAccount() != null ? account.getParentAccount().getId() : null)
            .parentAccountCode(account.getParentAccount() != null ? account.getParentAccount().getAccountCode() : null)
            .parentAccountName(account.getParentAccount() != null ? account.getParentAccount().getAccountName() : null)
            .isGroupAccount(account.getIsGroupAccount())
            .isControlAccount(account.getIsControlAccount())
            .openingBalance(account.getOpeningBalance())
            .openingBalanceType(account.getOpeningBalanceType())
            .currentBalance(account.getCurrentBalance())
            .isActive(account.getIsActive())
            .createdAt(account.getCreatedAt())
            .createdBy(account.getCreatedBy())
            .updatedAt(account.getUpdatedAt())
            .updatedBy(account.getUpdatedBy())
            .build();
    }
}
