package lk.epicgreen.erp.payment.mapper;

import lk.epicgreen.erp.payment.dto.request.ChequeRequest;
import lk.epicgreen.erp.payment.dto.response.ChequeResponse;
import lk.epicgreen.erp.payment.entity.Cheque;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapper for Cheque entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class ChequeMapper {

    public Cheque toEntity(ChequeRequest request) {
        if (request == null) {
            return null;
        }

        return Cheque.builder()
            .chequeNumber(request.getChequeNumber())
            .chequeDate(request.getChequeDate())
            .chequeAmount(request.getChequeAmount())
            .bankName(request.getBankName())
            .bankBranch(request.getBankBranch())
            .accountNumber(request.getAccountNumber())
            .status(request.getStatus() != null ? request.getStatus() : "RECEIVED")
            .depositDate(request.getDepositDate())
            .clearanceDate(request.getClearanceDate())
            .bounceReason(request.getBounceReason())
            .bounceCharges(request.getBounceCharges() != null ? request.getBounceCharges() : BigDecimal.ZERO)
            .remarks(request.getRemarks())
            .build();
    }

    public void updateEntityFromRequest(ChequeRequest request, Cheque cheque) {
        if (request == null || cheque == null) {
            return;
        }

        cheque.setChequeNumber(request.getChequeNumber());
        cheque.setChequeDate(request.getChequeDate());
        cheque.setChequeAmount(request.getChequeAmount());
        cheque.setBankName(request.getBankName());
        cheque.setBankBranch(request.getBankBranch());
        cheque.setAccountNumber(request.getAccountNumber());
        cheque.setStatus(request.getStatus());
        cheque.setDepositDate(request.getDepositDate());
        cheque.setClearanceDate(request.getClearanceDate());
        cheque.setBounceReason(request.getBounceReason());
        cheque.setBounceCharges(request.getBounceCharges());
        cheque.setRemarks(request.getRemarks());
    }

    public ChequeResponse toResponse(Cheque cheque) {
        if (cheque == null) {
            return null;
        }

        return ChequeResponse.builder()
            .id(cheque.getId())
            .paymentId(cheque.getPayment() != null ? cheque.getPayment().getId() : null)
            .paymentNumber(cheque.getPayment() != null ? cheque.getPayment().getPaymentNumber() : null)
            .chequeNumber(cheque.getChequeNumber())
            .chequeDate(cheque.getChequeDate())
            .chequeAmount(cheque.getChequeAmount())
            .bankName(cheque.getBankName())
            .bankBranch(cheque.getBankBranch())
            .accountNumber(cheque.getAccountNumber())
            .customerId(cheque.getCustomer() != null ? cheque.getCustomer().getId() : null)
            .customerCode(cheque.getCustomer() != null ? cheque.getCustomer().getCustomerCode() : null)
            .customerName(cheque.getCustomer() != null ? cheque.getCustomer().getCustomerName() : null)
            .status(cheque.getStatus())
            .depositDate(cheque.getDepositDate())
            .clearanceDate(cheque.getClearanceDate())
            .bounceReason(cheque.getBounceReason())
            .bounceCharges(cheque.getBounceCharges())
            .remarks(cheque.getRemarks())
            .createdAt(cheque.getCreatedAt())
            .createdBy(cheque.getCreatedBy())
            .updatedAt(cheque.getUpdatedAt())
            .updatedBy(cheque.getUpdatedBy())
            .build();
    }
}
