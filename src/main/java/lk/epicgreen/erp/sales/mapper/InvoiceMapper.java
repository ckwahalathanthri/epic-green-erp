package lk.epicgreen.erp.sales.mapper;

import lk.epicgreen.erp.sales.dto.request.InvoiceRequest;
import lk.epicgreen.erp.sales.dto.response.InvoiceResponse;
import lk.epicgreen.erp.sales.entity.Invoice;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

/**
 * Mapper for Invoice entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class InvoiceMapper {

    private final InvoiceItemMapper invoiceItemMapper;

    public InvoiceMapper(InvoiceItemMapper invoiceItemMapper) {
        this.invoiceItemMapper = invoiceItemMapper;
    }

    public Invoice toEntity(InvoiceRequest request) {
        if (request == null) {
            return null;
        }

        return Invoice.builder()
            .invoiceNumber(request.getInvoiceNumber())
            .invoiceDate(request.getInvoiceDate())
            .invoiceType(request.getInvoiceType() != null ? request.getInvoiceType() : "TAX_INVOICE")
            .paymentTerms(request.getPaymentTerms())
            .dueDate(request.getDueDate())
            .subtotal(request.getSubtotal())
            .taxAmount(request.getTaxAmount() != null ? request.getTaxAmount() : BigDecimal.ZERO)
            .discountAmount(request.getDiscountAmount() != null ? request.getDiscountAmount() : BigDecimal.ZERO)
            .freightCharges(request.getFreightCharges() != null ? request.getFreightCharges() : BigDecimal.ZERO)
            .totalAmount(request.getTotalAmount())
            .paidAmount(request.getPaidAmount() != null ? request.getPaidAmount() : BigDecimal.ZERO)
            .paymentStatus(request.getPaymentStatus() != null ? request.getPaymentStatus() : "UNPAID")
            .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
            .remarks(request.getRemarks())
            .build();
    }

    public void updateEntityFromRequest(InvoiceRequest request, Invoice invoice) {
        if (request == null || invoice == null) {
            return;
        }

        invoice.setInvoiceNumber(request.getInvoiceNumber());
        invoice.setInvoiceDate(request.getInvoiceDate());
        invoice.setInvoiceType(request.getInvoiceType());
        invoice.setPaymentTerms(request.getPaymentTerms());
        invoice.setDueDate(request.getDueDate());
        invoice.setSubtotal(request.getSubtotal());
        invoice.setTaxAmount(request.getTaxAmount());
        invoice.setDiscountAmount(request.getDiscountAmount());
        invoice.setFreightCharges(request.getFreightCharges());
        invoice.setTotalAmount(request.getTotalAmount());
        invoice.setPaidAmount(request.getPaidAmount());
        invoice.setPaymentStatus(request.getPaymentStatus());
        invoice.setStatus(request.getStatus());
        invoice.setRemarks(request.getRemarks());
    }

    public InvoiceResponse toResponse(Invoice invoice) {
        if (invoice == null) {
            return null;
        }

        String billingAddress = formatAddress(invoice.getBillingAddress());

        return InvoiceResponse.builder()
            .id(invoice.getId())
            .invoiceNumber(invoice.getInvoiceNumber())
            .invoiceDate(invoice.getInvoiceDate())
            .orderId(invoice.getOrder() != null ? invoice.getOrder().getId() : null)
            .orderNumber(invoice.getOrder() != null ? invoice.getOrder().getOrderNumber() : null)
            .dispatchId(invoice.getDispatchNote() != null ? invoice.getDispatchNote().getId() : null)
            .dispatchNumber(invoice.getDispatchNote() != null ? invoice.getDispatchNote().getDispatchNumber() : null)
            .customerId(invoice.getCustomer() != null ? invoice.getCustomer().getId() : null)
            .customerCode(invoice.getCustomer() != null ? invoice.getCustomer().getCustomerCode() : null)
            .customerName(invoice.getCustomer() != null ? invoice.getCustomer().getCustomerName() : null)
            .billingAddressId(invoice.getBillingAddress() != null ? invoice.getBillingAddress().getId() : null)
            .billingAddress(billingAddress)
            .invoiceType(invoice.getInvoiceType())
            .paymentTerms(invoice.getPaymentTerms())
            .dueDate(invoice.getDueDate())
            .subtotal(invoice.getSubtotal())
            .taxAmount(invoice.getTaxAmount())
            .discountAmount(invoice.getDiscountAmount())
            .freightCharges(invoice.getFreightCharges())
            .totalAmount(invoice.getTotalAmount())
            .paidAmount(invoice.getPaidAmount())
            .balanceAmount(invoice.getBalanceAmount())
            .paymentStatus(invoice.getPaymentStatus())
            .status(invoice.getStatus())
            .remarks(invoice.getRemarks())
            .createdAt(invoice.getCreatedAt())
            .createdBy(invoice.getCreatedBy())
            .updatedAt(invoice.getUpdatedAt())
            .updatedBy(invoice.getUpdatedBy())
            .items(invoice.getItems() != null ? 
                invoice.getItems().stream()
                    .map(invoiceItemMapper::toResponse)
                    .collect(Collectors.toList()) : null)
            .build();
    }

    private String formatAddress(lk.epicgreen.erp.customer.entity.CustomerAddress address) {
        if (address == null) {
            return null;
        }
        
        StringBuilder sb = new StringBuilder();
        if (address.getAddressLine1() != null) sb.append(address.getAddressLine1());
        if (address.getAddressLine2() != null) sb.append(", ").append(address.getAddressLine2());
        if (address.getCity() != null) sb.append(", ").append(address.getCity());
        if (address.getState() != null) sb.append(", ").append(address.getState());
        if (address.getCountry() != null) sb.append(", ").append(address.getCountry());
        if (address.getPostalCode() != null) sb.append(" - ").append(address.getPostalCode());
        
        return sb.length() > 0 ? sb.toString() : null;
    }
}
