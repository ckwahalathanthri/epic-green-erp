package lk.epicgreen.erp.accounting.mapper;

import lk.epicgreen.erp.accounting.dto.request.BankAccountRequest;
import lk.epicgreen.erp.accounting.dto.response.BankAccountResponse;
import lk.epicgreen.erp.accounting.entity.BankAccount;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BankAccountMapper {

    public BankAccount toEntity(BankAccountRequest request) {
        if (request == null) return null;

        return BankAccount.builder()
            .accountNumber(request.getAccountNumber())
            .accountName(request.getAccountName())
            .bankName(request.getBankName())
            .bankBranch(request.getBankBranch())
            .accountType(request.getAccountType())
            .currencyCode(request.getCurrencyCode() != null ? request.getCurrencyCode() : "LKR")
            .openingBalance(request.getOpeningBalance() != null ? request.getOpeningBalance() : BigDecimal.ZERO)
            .currentBalance(request.getCurrentBalance() != null ? request.getCurrentBalance() : BigDecimal.ZERO)
            .isActive(request.getIsActive() != null ? request.getIsActive() : true)
            .build();
    }

    public void updateEntityFromRequest(BankAccountRequest request, BankAccount account) {
        if (request == null || account == null) return;

        account.setAccountNumber(request.getAccountNumber());
        account.setAccountName(request.getAccountName());
        account.setBankName(request.getBankName());
        account.setBankBranch(request.getBankBranch());
        account.setAccountType(request.getAccountType());
        account.setCurrencyCode(request.getCurrencyCode());
        account.setOpeningBalance(request.getOpeningBalance());
        account.setCurrentBalance(request.getCurrentBalance());
        account.setIsActive(request.getIsActive());
    }

    public BankAccountResponse toResponse(BankAccount account) {
        if (account == null) return null;

        return BankAccountResponse.builder()
            .id(account.getId())
            .accountNumber(account.getAccountNumber())
            .accountName(account.getAccountName())
            .bankName(account.getBankName())
            .bankBranch(account.getBankBranch())
            .accountType(account.getAccountType())
            .currencyCode(account.getCurrencyCode())
            .glAccountId(account.getGlAccount() != null ? account.getGlAccount().getId() : null)
            .glAccountCode(account.getGlAccount() != null ? account.getGlAccount().getAccountCode() : null)
            .glAccountName(account.getGlAccount() != null ? account.getGlAccount().getAccountName() : null)
            .openingBalance(account.getOpeningBalance())
            .currentBalance(account.getCurrentBalance())
            .isActive(account.getIsActive())
            .createdAt(account.getCreatedAt())
            .createdBy(account.getCreatedBy())
            .updatedAt(account.getUpdatedAt())
            .updatedBy(account.getUpdatedBy())
            .build();
    }
}
