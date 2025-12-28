package lk.epicgreen.erp.payment.mapper;

import lk.epicgreen.erp.payment.dto.request.PaymentAllocationRequest;
import lk.epicgreen.erp.payment.dto.response.PaymentAllocationResponse;
import lk.epicgreen.erp.payment.entity.PaymentAllocation;
import org.springframework.stereotype.Component;

/**
 * Mapper for PaymentAllocation entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class PaymentAllocationMapper {

    public PaymentAllocation toEntity(PaymentAllocationRequest request) {
        if (request == null) {
            return null;
        }

        return PaymentAllocation.builder()
            .allocatedAmount(request.getAllocatedAmount())
            .allocationDate(request.getAllocationDate())
            .remarks(request.getRemarks())
            .build();
    }

    public PaymentAllocationResponse toResponse(PaymentAllocation allocation) {
        if (allocation == null) {
            return null;
        }

        return PaymentAllocationResponse.builder()
            .id(allocation.getId())
            .paymentId(allocation.getPayment() != null ? allocation.getPayment().getId() : null)
            .paymentNumber(allocation.getPayment() != null ? allocation.getPayment().getPaymentNumber() : null)
            .invoiceId(allocation.getInvoice() != null ? allocation.getInvoice().getId() : null)
            .invoiceNumber(allocation.getInvoice() != null ? allocation.getInvoice().getInvoiceNumber() : null)
            .allocatedAmount(allocation.getAllocatedAmount())
            .allocationDate(allocation.getAllocationDate())
            .remarks(allocation.getRemarks())
            .createdAt(allocation.getCreatedAt())
            .createdBy(allocation.getCreatedBy())
            .build();
    }
}
