package lk.epicgreen.erp.accounting.mapper;

import lk.epicgreen.erp.accounting.dto.request.JournalEntryLineRequest;
import lk.epicgreen.erp.accounting.dto.response.JournalEntryLineResponse;
import lk.epicgreen.erp.accounting.entity.JournalEntryLine;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JournalEntryLineMapper {

    public JournalEntryLine toEntity(JournalEntryLineRequest request) {
        if (request == null) return null;

        return JournalEntryLine.builder()
            .lineNumber(request.getLineNumber())
            .debitAmount(request.getDebitAmount() != null ? request.getDebitAmount() : BigDecimal.ZERO)
            .creditAmount(request.getCreditAmount() != null ? request.getCreditAmount() : BigDecimal.ZERO)
            .description(request.getDescription())
            .costCenter(request.getCostCenter())
            .dimension1(request.getDimension1())
            .dimension2(request.getDimension2())
            .build();
    }

    public JournalEntryLineResponse toResponse(JournalEntryLine line) {
        if (line == null) return null;

        return JournalEntryLineResponse.builder()
            .id(line.getId())
            .journalId(line.getJournal() != null ? line.getJournal().getId() : null)
            .lineNumber(line.getLineNumber())
            .accountId(line.getAccount() != null ? line.getAccount().getId() : null)
            .accountCode(line.getAccount() != null ? line.getAccount().getAccountCode() : null)
            .accountName(line.getAccount() != null ? line.getAccount().getAccountName() : null)
            .debitAmount(line.getDebitAmount())
            .creditAmount(line.getCreditAmount())
            .description(line.getDescription())
            .costCenter(line.getCostCenter())
            .dimension1(line.getDimension1())
            .dimension2(line.getDimension2())
            .build();
    }
}
