package lk.epicgreen.erp.sales.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SalesOrderDTO {
    private Long id;
    private String orderNumber;
    private LocalDate orderDate;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String billingAddress;
    private String shippingAddress;
    private String orderStatus;
    private Long quotationId;
    private String referenceNumber;
    private String priority;
    private BigDecimal subtotal;
    private BigDecimal discountPercentage;
    private BigDecimal discountAmount;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;
    private BigDecimal shippingCharge;
    private BigDecimal totalAmount;
    private String currency;
    private String paymentTerms;
    private String paymentStatus;
    private BigDecimal paidAmount;
    private BigDecimal balanceAmount;
    private LocalDate expectedDeliveryDate;
    private LocalDate actualDeliveryDate;
    private String salesPerson;
    private String notes;
    private String internalNotes;
    private String confirmedBy;
    private LocalDateTime confirmedAt;
    private String cancelledBy;
    private LocalDateTime cancelledAt;
    private String cancellationReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SalesOrderItemDTO> items;
}