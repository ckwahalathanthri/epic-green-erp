package lk.epicgreen.erp.accounting.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * JournalEntry Request DTO
 * DTO for journal entry operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntryRequest {
    
    @NotBlank(message = "Entry number is required")
    private String entryNumber;
    
    @NotNull(message = "Entry date is required")
    private LocalDate entryDate;
    
    @NotBlank(message = "Entry type is required")
    private String entryType;
    
    private String referenceType;
    
    private Long referenceId;
    
    private String referenceNumber;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    private String notes;
    
    private Long accountingPeriodId;
    
    private Long createdByUserId;
    
    private Long approvedByUserId;
    
    private String approvalNotes;
    
    @NotNull(message = "Journal entry lines are required")
    private List<JournalEntryLineRequest> lines;
}
