package lk.epicgreen.erp.supplier.mapper;

import lk.epicgreen.erp.supplier.dto.request.SupplierLedgerRequest;
import lk.epicgreen.erp.supplier.dto.response.SupplierLedgerResponse;
import lk.epicgreen.erp.supplier.entity.SupplierLedger;
import org.springframework.stereotype.Component;

/**
 * Mapper for SupplierLedger entity and DTOs
 * Note: Supplier ledger is IMMUTABLE - no update operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class SupplierLedgerMapper {

    public SupplierLedger toEntity(SupplierLedgerRequest request) {
        if (request == null) {
            return null;
        }

        return SupplierLedger.builder()
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

    public SupplierLedgerResponse toResponse(SupplierLedger ledger) {
        if (ledger == null) {
            return null;
        }

        return SupplierLedgerResponse.builder()
            .id(ledger.getId())
            .supplierId(ledger.getSupplier() != null ? ledger.getSupplier().getId() : null)
            .supplierCode(ledger.getSupplier() != null ? ledger.getSupplier().getSupplierCode() : null)
            .supplierName(ledger.getSupplier() != null ? ledger.getSupplier().getSupplierName() : null)
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
