package lk.epicgreen.erp.payment.mapper;

import lk.epicgreen.erp.payment.dto.request.PaymentRequest;
import lk.epicgreen.erp.payment.dto.response.PaymentResponse;
import lk.epicgreen.erp.payment.entity.Payment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

/**
 * Mapper for Payment entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class PaymentMapper {

    private final PaymentAllocationMapper paymentAllocationMapper;

    public PaymentMapper(PaymentAllocationMapper paymentAllocationMapper) {
        this.paymentAllocationMapper = paymentAllocationMapper;
    }

    public Payment toEntity(PaymentRequest request) {
        if (request == null) {
            return null;
        }

        return Payment.builder()
            .paymentNumber(request.getPaymentNumber())
            .paymentDate(request.getPaymentDate())
            .paymentMode(request.getPaymentMode())
            .totalAmount(request.getTotalAmount())
            .allocatedAmount(request.getAllocatedAmount() != null ? request.getAllocatedAmount() : BigDecimal.ZERO)
            .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
            .bankName(request.getBankName())
            .bankBranch(request.getBankBranch())
            .chequeNumber(request.getChequeNumber())
            .chequeDate(request.getChequeDate())
            .chequeClearanceDate(request.getChequeClearanceDate())
            .bankReferenceNumber(request.getBankReferenceNumber())
//            .collectedBy(request.getCollectedBy())
            .collectedAt(request.getCollectedAt())
            .remarks(request.getRemarks())
            .build();
    }

    public void updateEntityFromRequest(PaymentRequest request, Payment payment) {
        if (request == null || payment == null) {
            return;
        }

        payment.setPaymentNumber(request.getPaymentNumber());
        payment.setPaymentDate(request.getPaymentDate());
        payment.setPaymentMode(request.getPaymentMode());
        payment.setTotalAmount(request.getTotalAmount());
        payment.setAllocatedAmount(request.getAllocatedAmount());
        payment.setStatus(request.getStatus());
        payment.setBankName(request.getBankName());
        payment.setBankBranch(request.getBankBranch());
        payment.setChequeNumber(request.getChequeNumber());
        payment.setChequeDate(request.getChequeDate());
        payment.setChequeClearanceDate(request.getChequeClearanceDate());
        payment.setBankReferenceNumber(request.getBankReferenceNumber());
//        payment.setCollectedBy(request.getCollectedBy());
        payment.setCollectedAt(request.getCollectedAt());
        payment.setRemarks(request.getRemarks());
    }

    public PaymentResponse toResponse(Payment payment) {
        if (payment == null) {
            return null;
        }

        return PaymentResponse.builder()
            .id(payment.getId())
            .paymentNumber(payment.getPaymentNumber())
            .paymentDate(payment.getPaymentDate())
            .customerId(payment.getCustomer() != null ? payment.getCustomer().getId() : null)
            .customerCode(payment.getCustomer() != null ? payment.getCustomer().getCustomerCode() : null)
            .customerName(payment.getCustomer() != null ? payment.getCustomer().getCustomerName() : null)
            .paymentMode(payment.getPaymentMode())
            .totalAmount(payment.getTotalAmount())
            .allocatedAmount(payment.getAllocatedAmount())
            .unallocatedAmount(payment.getUnallocatedAmount())
            .status(payment.getStatus())
            .bankName(payment.getBankName())
            .bankBranch(payment.getBankBranch())
            .chequeNumber(payment.getChequeNumber())
            .chequeDate(payment.getChequeDate())
            .chequeClearanceDate(payment.getChequeClearanceDate())
            .bankReferenceNumber(payment.getBankReferenceNumber())
//            .collectedBy(payment.getCollectedBy())
            .collectedAt(payment.getCollectedAt())
//            .approvedBy(payment.getApprovedBy())
            .approvedAt(payment.getApprovedAt())
            .remarks(payment.getRemarks())
            .createdAt(payment.getCreatedAt())
            .createdBy(payment.getCreatedBy())
            .updatedAt(payment.getUpdatedAt())
            .updatedBy(payment.getUpdatedBy())
            .allocations(payment.getAllocations() != null ? 
                payment.getAllocations().stream()
                    .map(paymentAllocationMapper::toResponse)
                    .collect(Collectors.toList()) : null)
            .build();
    }
}
