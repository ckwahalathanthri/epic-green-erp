package lk.epicgreen.erp.accounting.mapper;

import lk.epicgreen.erp.accounting.dto.response.GeneralLedgerResponse;
import lk.epicgreen.erp.accounting.entity.GeneralLedger;
import org.springframework.stereotype.Component;

@Component
public class GeneralLedgerMapper {

    public GeneralLedgerResponse toResponse(GeneralLedger ledger) {
        if (ledger == null) return null;

        return GeneralLedgerResponse.builder()
            .id(ledger.getId())
            .transactionDate(ledger.getTransactionDate())
            .periodId(ledger.getPeriod() != null ? ledger.getPeriod().getId() : null)
            .periodCode(ledger.getPeriod() != null ? ledger.getPeriod().getPeriodCode() : null)
            .accountId(ledger.getAccount() != null ? ledger.getAccount().getId() : null)
            .accountCode(ledger.getAccount() != null ? ledger.getAccount().getAccountCode() : null)
            .accountName(ledger.getAccount() != null ? ledger.getAccount().getAccountName() : null)
            .journalId(ledger.getJournal() != null ? ledger.getJournal().getId() : null)
            .journalNumber(ledger.getJournal() != null ? ledger.getJournal().getJournalNumber() : null)
            .journalLineId(ledger.getJournalLine() != null ? ledger.getJournalLine().getId() : null)
            .description(ledger.getDescription())
            .debitAmount(ledger.getDebitAmount())
            .creditAmount(ledger.getCreditAmount())
            .balance(ledger.getBalance())
            .sourceType(ledger.getSourceType())
            .sourceId(ledger.getSourceId())
            .createdAt(ledger.getCreatedAt())
            .build();
    }
}
