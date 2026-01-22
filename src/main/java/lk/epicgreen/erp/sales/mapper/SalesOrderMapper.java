package lk.epicgreen.erp.sales.mapper;

import lk.epicgreen.erp.sales.dto.request.SalesOrderRequest;
import lk.epicgreen.erp.sales.dto.response.SalesOrderResponse;
import lk.epicgreen.erp.sales.entity.SalesOrder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

/**
 * Mapper for SalesOrder entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class SalesOrderMapper {

    private final SalesOrderItemMapper salesOrderItemMapper;

    public SalesOrderMapper(SalesOrderItemMapper salesOrderItemMapper) {
        this.salesOrderItemMapper = salesOrderItemMapper;
    }

    public SalesOrder toEntity(SalesOrderRequest request) {
        if (request == null) {
            return null;
        }

        return SalesOrder.builder()
            .orderNumber(request.getOrderNumber())
            .orderDate(request.getOrderDate())
            .customerPoNumber(request.getCustomerPoNumber())
            .customerPoDate(request.getCustomerPoDate())
            .orderType(request.getOrderType() != null ? request.getOrderType() : "REGULAR")
            .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
            .paymentMode(request.getPaymentMode())
            .deliveryMode(request.getDeliveryMode() != null ? request.getDeliveryMode() : "COMPANY_DELIVERY")
            .expectedDeliveryDate(request.getExpectedDeliveryDate())
            .subtotal(request.getSubtotal() != null ? request.getSubtotal() : BigDecimal.ZERO)
            .taxAmount(request.getTaxAmount() != null ? request.getTaxAmount() : BigDecimal.ZERO)
            .discountPercentage(request.getDiscountPercentage() != null ? request.getDiscountPercentage() : BigDecimal.ZERO)
            .discountAmount(request.getDiscountAmount() != null ? request.getDiscountAmount() : BigDecimal.ZERO)
            .freightCharges(request.getFreightCharges() != null ? request.getFreightCharges() : BigDecimal.ZERO)
            .totalAmount(request.getTotalAmount() != null ? request.getTotalAmount() : BigDecimal.ZERO)
            .remarks(request.getRemarks())
            .build();
    }

    public void updateEntityFromRequest(SalesOrderRequest request, SalesOrder order) {
        if (request == null || order == null) {
            return;
        }

        order.setOrderNumber(request.getOrderNumber());
        order.setOrderDate(request.getOrderDate());
        order.setCustomerPoNumber(request.getCustomerPoNumber());
        order.setCustomerPoDate(request.getCustomerPoDate());
        order.setOrderType(request.getOrderType());
        order.setStatus(request.getStatus());
        order.setPaymentMode(request.getPaymentMode());
        order.setDeliveryMode(request.getDeliveryMode());
        order.setExpectedDeliveryDate(request.getExpectedDeliveryDate());
        order.setSubtotal(request.getSubtotal());
        order.setTaxAmount(request.getTaxAmount());
        order.setDiscountPercentage(request.getDiscountPercentage());
        order.setDiscountAmount(request.getDiscountAmount());
        order.setFreightCharges(request.getFreightCharges());
        order.setTotalAmount(request.getTotalAmount());
        order.setRemarks(request.getRemarks());
    }

    public SalesOrderResponse toResponse(SalesOrder order) {
        if (order == null) {
            return null;
        }

        String billingAddress = formatAddress(order.getBillingAddress());
        String shippingAddress = formatAddress(order.getShippingAddress());

        return SalesOrderResponse.builder()
            .id(order.getId())
            .orderNumber(order.getOrderNumber())
            .orderDate(order.getOrderDate())
            .customerId(order.getCustomer() != null ? order.getCustomer().getId() : null)
            .customerCode(order.getCustomer() != null ? order.getCustomer().getCustomerCode() : null)
            .customerName(order.getCustomer() != null ? order.getCustomer().getCustomerName() : null)
            .customerPoNumber(order.getCustomerPoNumber())
            .customerPoDate(order.getCustomerPoDate())
            .billingAddressId(order.getBillingAddress() != null ? order.getBillingAddress().getId() : null)
            .billingAddress(billingAddress)
            .shippingAddressId(order.getShippingAddress() != null ? order.getShippingAddress().getId() : null)
            .shippingAddress(shippingAddress)
            .warehouseId(order.getWarehouse() != null ? order.getWarehouse().getId() : null)
            .warehouseCode(order.getWarehouse() != null ? order.getWarehouse().getWarehouseCode() : null)
            .warehouseName(order.getWarehouse() != null ? order.getWarehouse().getWarehouseName() : null)
            .salesRepId(order.getSalesRep() != null ? order.getSalesRep().getId() : null)
            .salesRepName(order.getSalesRep() != null ? 
                order.getSalesRep().getFirstName() + " " + order.getSalesRep().getLastName() : null)
            .orderType(order.getOrderType())
            .status(order.getStatus())
            .paymentMode(order.getPaymentMode())
            .deliveryMode(order.getDeliveryMode())
            .expectedDeliveryDate(order.getExpectedDeliveryDate())
            .subtotal(order.getSubtotal())
            .taxAmount(order.getTaxAmount())
            .discountPercentage(order.getDiscountPercentage())
            .discountAmount(order.getDiscountAmount())
            .freightCharges(order.getFreightCharges())
            .totalAmount(order.getTotalAmount())
            .approvedBy(order.getApprovedBy().getId())
            .approvedAt(order.getApprovedAt())
            .remarks(order.getRemarks())
            .createdAt(order.getCreatedAt())
            .createdBy(order.getCreatedBy())
            .updatedAt(order.getUpdatedAt())
            .updatedBy(order.getUpdatedBy())
            .items(order.getItems() != null ? 
                order.getItems().stream()
                    .map(salesOrderItemMapper::toResponse)
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
