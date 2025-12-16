package lk.epicgreen.erp.accounting.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JournalEntryLine Request DTO
 * DTO for journal entry line operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntryLineRequest {
    
    @NotNull(message = "Account ID is required")
    private Long accountId;
    
    private String accountCode;
    
    private String accountName;
    
    @NotNull(message = "Debit amount is required")
    private Double debitAmount;
    
    @NotNull(message = "Credit amount is required")
    private Double creditAmount;
    
    private String description;
    
    private String notes;
    
    private String costCenter;
    
    private String department;
    
    private String project;
    
    private Integer lineNumber;
}
