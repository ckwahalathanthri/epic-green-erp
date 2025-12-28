package lk.epicgreen.erp.supplier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for supplier ledger response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierLedgerResponse {

    private Long id;
    private Long supplierId;
    private String supplierCode;
    private String supplierName;
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
}
