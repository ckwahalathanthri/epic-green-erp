package lk.epicgreen.erp.accounting.mapper;

import lk.epicgreen.erp.accounting.dto.request.BankReconciliationRequest;
import lk.epicgreen.erp.accounting.dto.response.BankReconciliationResponse;
import lk.epicgreen.erp.accounting.entity.BankReconciliation;
import org.springframework.stereotype.Component;

@Component
public class BankReconciliationMapper {

    public BankReconciliation toEntity(BankReconciliationRequest request) {
        if (request == null) return null;

        return BankReconciliation.builder()
            .reconciliationNumber(request.getReconciliationNumber())
            .statementDate(request.getStatementDate())
            .statementBalance(request.getStatementBalance())
            .bookBalance(request.getBookBalance())
            .reconciledBalance(request.getReconciledBalance())
            .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
            .remarks(request.getRemarks())
            .build();
    }

    public void updateEntityFromRequest(BankReconciliationRequest request, BankReconciliation recon) {
        if (request == null || recon == null) return;

        recon.setReconciliationNumber(request.getReconciliationNumber());
        recon.setStatementDate(request.getStatementDate());
        recon.setStatementBalance(request.getStatementBalance());
        recon.setBookBalance(request.getBookBalance());
        recon.setReconciledBalance(request.getReconciledBalance());
        recon.setStatus(request.getStatus());
        recon.setRemarks(request.getRemarks());
    }

    public BankReconciliationResponse toResponse(BankReconciliation recon) {
        if (recon == null) return null;

        java.math.BigDecimal diff = recon.getStatementBalance().subtract(recon.getBookBalance());

        return BankReconciliationResponse.builder()
            .id(recon.getId())
            .reconciliationNumber(recon.getReconciliationNumber())
            .bankAccountId(recon.getBankAccount() != null ? recon.getBankAccount().getId() : null)
            .accountNumber(recon.getBankAccount() != null ? recon.getBankAccount().getAccountNumber() : null)
            .accountName(recon.getBankAccount() != null ? recon.getBankAccount().getAccountName() : null)
            .bankName(recon.getBankAccount() != null ? recon.getBankAccount().getBankName() : null)
            .statementDate(recon.getStatementDate())
            .statementBalance(recon.getStatementBalance())
            .bookBalance(recon.getBookBalance())
            .reconciledBalance(recon.getReconciledBalance())
            .difference(diff)
            .status(recon.getStatus())
            .reconciledBy(recon.getReconciledBy().getId())
            .reconciledAt(recon.getReconciledAt())
            .remarks(recon.getRemarks())
            .createdAt(recon.getCreatedAt())
            .createdBy(recon.getCreatedBy())
            .build();
    }
}
