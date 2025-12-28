package lk.epicgreen.erp.accounting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for Journal Entry response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntryResponse {

    private Long id;
    private String journalNumber;
    private LocalDate journalDate;
    private Long periodId;
    private String periodCode;
    private String periodName;
    private String entryType;
    private String sourceType;
    private Long sourceId;
    private String sourceReference;
    private String description;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private String status;
    private Long postedBy;
    private String postedByName;
    private LocalDateTime postedAt;
    private Long approvedBy;
    private String approvedByName;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
    private Long createdBy;
    private String createdByName;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private List<JournalEntryLineResponse> lines;
}
