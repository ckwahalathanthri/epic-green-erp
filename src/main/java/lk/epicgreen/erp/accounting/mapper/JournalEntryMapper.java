package lk.epicgreen.erp.accounting.mapper;

import lk.epicgreen.erp.accounting.dto.request.JournalEntryRequest;
import lk.epicgreen.erp.accounting.dto.response.JournalEntryResponse;
import lk.epicgreen.erp.accounting.entity.JournalEntry;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class JournalEntryMapper {

    private final JournalEntryLineMapper lineMapper;

    public JournalEntryMapper(JournalEntryLineMapper lineMapper) {
        this.lineMapper = lineMapper;
    }

    public JournalEntry toEntity(JournalEntryRequest request) {
        if (request == null) return null;

        return JournalEntry.builder()
            .journalNumber(request.getJournalNumber())
            .journalDate(request.getJournalDate())
            .entryType(request.getEntryType() != null ? request.getEntryType() : "MANUAL")
            .sourceType(request.getSourceType())
            .sourceId(request.getSourceId())
            .sourceReference(request.getSourceReference())
            .description(request.getDescription())
            .totalDebit(request.getTotalDebit())
            .totalCredit(request.getTotalCredit())
            .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
            .build();
    }

    public void updateEntityFromRequest(JournalEntryRequest request, JournalEntry journal) {
        if (request == null || journal == null) return;

        journal.setJournalNumber(request.getJournalNumber());
        journal.setJournalDate(request.getJournalDate());
        journal.setEntryType(request.getEntryType());
        journal.setSourceType(request.getSourceType());
        journal.setSourceId(request.getSourceId());
        journal.setSourceReference(request.getSourceReference());
        journal.setDescription(request.getDescription());
        journal.setTotalDebit(request.getTotalDebit());
        journal.setTotalCredit(request.getTotalCredit());
        journal.setStatus(request.getStatus());
    }

    public JournalEntryResponse toResponse(JournalEntry journal) {
        if (journal == null) return null;

        return JournalEntryResponse.builder()
            .id(journal.getId())
            .journalNumber(journal.getJournalNumber())
            .journalDate(journal.getJournalDate())
            .periodId(journal.getPeriod() != null ? journal.getPeriod().getId() : null)
            .periodCode(journal.getPeriod() != null ? journal.getPeriod().getPeriodCode() : null)
            .periodName(journal.getPeriod() != null ? journal.getPeriod().getPeriodName() : null)
            .entryType(journal.getEntryType())
            .sourceType(journal.getSourceType())
            .sourceId(journal.getSourceId())
            .sourceReference(journal.getSourceReference())
            .description(journal.getDescription())
            .totalDebit(journal.getTotalDebit())
            .totalCredit(journal.getTotalCredit())
            .status(journal.getStatus())
            .postedBy(journal.getPostedBy())
            .postedAt(journal.getPostedAt())
            .approvedBy(journal.getApprovedBy())
            .approvedAt(journal.getApprovedAt())
            .createdAt(journal.getCreatedAt())
            .createdBy(journal.getCreatedBy())
            .updatedAt(journal.getUpdatedAt())
            .updatedBy(journal.getUpdatedBy())
            .lines(journal.getLines() != null ? 
                journal.getLines().stream()
                    .map(lineMapper::toResponse)
                    .collect(Collectors.toList()) : null)
            .build();
    }
}
