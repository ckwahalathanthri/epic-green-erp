package lk.epicgreen.erp.sales.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for creating/updating Sales Order
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderRequest {

    @NotBlank(message = "Order number is required")
    @Size(max = 30, message = "Order number must not exceed 30 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Order number must contain only uppercase letters, numbers, hyphens and underscores")
    private String orderNumber;

    @NotNull(message = "Order date is required")
    private LocalDate orderDate;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @Size(max = 50, message = "Customer PO number must not exceed 50 characters")
    private String customerPoNumber;

    private LocalDate customerPoDate;

    private Long billingAddressId;

    private Long shippingAddressId;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    private Long salesRepId;

    @Pattern(regexp = "^(REGULAR|URGENT|ADVANCE_ORDER)$", 
             message = "Order type must be one of: REGULAR, URGENT, ADVANCE_ORDER")
    private String orderType;

    @Pattern(regexp = "^(DRAFT|CONFIRMED|PENDING_APPROVAL|APPROVED|PROCESSING|PACKED|DISPATCHED|DELIVERED|CANCELLED)$", 
             message = "Status must be one of: DRAFT, CONFIRMED, PENDING_APPROVAL, APPROVED, PROCESSING, PACKED, DISPATCHED, DELIVERED, CANCELLED")
    private String status;

    @NotBlank(message = "Payment mode is required")
    @Pattern(regexp = "^(CASH|CHEQUE|CREDIT|BANK_TRANSFER)$", 
             message = "Payment mode must be one of: CASH, CHEQUE, CREDIT, BANK_TRANSFER")
    private String paymentMode;

    @Pattern(regexp = "^(SELF_PICKUP|COMPANY_DELIVERY|COURIER)$", 
             message = "Delivery mode must be one of: SELF_PICKUP, COMPANY_DELIVERY, COURIER")
    private String deliveryMode;

    private LocalDate expectedDeliveryDate;

    @DecimalMin(value = "0.0", message = "Subtotal must be >= 0")
    private BigDecimal subtotal;

    @DecimalMin(value = "0.0", message = "Tax amount must be >= 0")
    private BigDecimal taxAmount;

    @DecimalMin(value = "0.0", message = "Discount percentage must be >= 0")
    @DecimalMax(value = "100.0", message = "Discount percentage must be <= 100")
    private BigDecimal discountPercentage;

    @DecimalMin(value = "0.0", message = "Discount amount must be >= 0")
    private BigDecimal discountAmount;

    @DecimalMin(value = "0.0", message = "Freight charges must be >= 0")
    private BigDecimal freightCharges;

    @DecimalMin(value = "0.0", message = "Total amount must be >= 0")
    private BigDecimal totalAmount;

    @Size(max = 1000, message = "Remarks must not exceed 1000 characters")
    private String remarks;

    @NotEmpty(message = "At least one order item is required")
    @Valid
    private List<SalesOrderItemRequest> items;
}
