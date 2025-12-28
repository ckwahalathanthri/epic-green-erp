package lk.epicgreen.erp.customer.mapper;

import lk.epicgreen.erp.customer.dto.request.CustomerLedgerRequest;
import lk.epicgreen.erp.customer.dto.response.CustomerLedgerResponse;
import lk.epicgreen.erp.customer.entity.CustomerLedger;
import org.springframework.stereotype.Component;

/**
 * Mapper for CustomerLedger entity and DTOs
 * Note: Customer ledger is IMMUTABLE - no update operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class CustomerLedgerMapper {

    public CustomerLedger toEntity(CustomerLedgerRequest request) {
        if (request == null) {
            return null;
        }

        return CustomerLedger.builder()
            .transactionDate(request.getTransactionDate())
            .transactionType(request.getTransactionType())
            .referenceType(request.getReferenceType())
            .referenceId(request.getReferenceId())
            .referenceNumber(request.getReferenceNumber())
            .description(request.getDescription())
            .debitAmount(request.getDebitAmount())
            .creditAmount(request.getCreditAmount())
            .build();
    }

    public CustomerLedgerResponse toResponse(CustomerLedger ledger) {
        if (ledger == null) {
            return null;
        }

        return CustomerLedgerResponse.builder()
            .id(ledger.getId())
            .customerId(ledger.getCustomer() != null ? ledger.getCustomer().getId() : null)
            .customerCode(ledger.getCustomer() != null ? ledger.getCustomer().getCustomerCode() : null)
            .customerName(ledger.getCustomer() != null ? ledger.getCustomer().getCustomerName() : null)
            .transactionDate(ledger.getTransactionDate())
            .transactionType(ledger.getTransactionType())
            .referenceType(ledger.getReferenceType())
            .referenceId(ledger.getReferenceId())
            .referenceNumber(ledger.getReferenceNumber())
            .description(ledger.getDescription())
            .debitAmount(ledger.getDebitAmount())
            .creditAmount(ledger.getCreditAmount())
            .balance(ledger.getBalance())
            .createdAt(ledger.getCreatedAt())
            .createdBy(ledger.getCreatedBy())
            .build();
    }
}
