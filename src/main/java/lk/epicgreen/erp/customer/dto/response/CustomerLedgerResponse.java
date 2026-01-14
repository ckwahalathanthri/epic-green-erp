package lk.epicgreen.erp.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for customer ledger response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLedgerResponse {

    private Long id;
    private Long customerId;
    private String customerCode;
    private String customerName;
    private LocalDate transactionDate;
    private String transactionType;
    private String referenceType;
    private Long referenceId;
    private String referenceNumber;
    private String description;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private Long createdBy;
    private String createdByName;
    private String message;
}
