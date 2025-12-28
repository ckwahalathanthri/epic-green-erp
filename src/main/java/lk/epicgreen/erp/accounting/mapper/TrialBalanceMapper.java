package lk.epicgreen.erp.accounting.mapper;

import lk.epicgreen.erp.accounting.dto.response.TrialBalanceResponse;
import lk.epicgreen.erp.accounting.entity.TrialBalance;
import org.springframework.stereotype.Component;

@Component
public class TrialBalanceMapper {

    public TrialBalanceResponse toResponse(TrialBalance balance) {
        if (balance == null) return null;

        return TrialBalanceResponse.builder()
            .id(balance.getId())
            .periodId(balance.getPeriod() != null ? balance.getPeriod().getId() : null)
            .periodCode(balance.getPeriod() != null ? balance.getPeriod().getPeriodCode() : null)
            .periodName(balance.getPeriod() != null ? balance.getPeriod().getPeriodName() : null)
            .accountId(balance.getAccount() != null ? balance.getAccount().getId() : null)
            .accountCode(balance.getAccount() != null ? balance.getAccount().getAccountCode() : null)
            .accountName(balance.getAccount() != null ? balance.getAccount().getAccountName() : null)
            .openingDebit(balance.getOpeningDebit())
            .openingCredit(balance.getOpeningCredit())
            .periodDebit(balance.getPeriodDebit())
            .periodCredit(balance.getPeriodCredit())
            .closingDebit(balance.getClosingDebit())
            .closingCredit(balance.getClosingCredit())
            .generatedAt(balance.getGeneratedAt())
            .generatedBy(balance.getGeneratedBy())
            .build();
    }
}
