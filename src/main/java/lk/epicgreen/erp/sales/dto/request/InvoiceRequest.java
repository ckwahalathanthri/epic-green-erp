package lk.epicgreen.erp.sales.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for creating/updating Invoice
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequest {

    @NotBlank(message = "Invoice number is required")
    @Size(max = 30, message = "Invoice number must not exceed 30 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Invoice number must contain only uppercase letters, numbers, hyphens and underscores")
    private String invoiceNumber;

    @NotNull(message = "Invoice date is required")
    private LocalDate invoiceDate;

    @NotNull(message = "Order ID is required")
    private Long orderId;

    private Long dispatchId;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    private Long billingAddressId;

    @Pattern(regexp = "^(TAX_INVOICE|CREDIT_NOTE|DEBIT_NOTE)$", 
             message = "Invoice type must be one of: TAX_INVOICE, CREDIT_NOTE, DEBIT_NOTE")
    private String invoiceType;

    @Size(max = 100, message = "Payment terms must not exceed 100 characters")
    private String paymentTerms;

    private LocalDate dueDate;

    @NotNull(message = "Subtotal is required")
    @DecimalMin(value = "0.0", message = "Subtotal must be >= 0")
    private BigDecimal subtotal;

    @DecimalMin(value = "0.0", message = "Tax amount must be >= 0")
    private BigDecimal taxAmount;

    @DecimalMin(value = "0.0", message = "Discount amount must be >= 0")
    private BigDecimal discountAmount;

    @DecimalMin(value = "0.0", message = "Freight charges must be >= 0")
    private BigDecimal freightCharges;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", message = "Total amount must be >= 0")
    private BigDecimal totalAmount;

    @DecimalMin(value = "0.0", message = "Paid amount must be >= 0")
    private BigDecimal paidAmount;

    @Pattern(regexp = "^(UNPAID|PARTIAL|PAID|OVERDUE)$", 
             message = "Payment status must be one of: UNPAID, PARTIAL, PAID, OVERDUE")
    private String paymentStatus;

    @Pattern(regexp = "^(DRAFT|POSTED|CANCELLED)$", 
             message = "Status must be one of: DRAFT, POSTED, CANCELLED")
    private String status;

    @Size(max = 1000, message = "Remarks must not exceed 1000 characters")
    private String remarks;

    @NotEmpty(message = "At least one invoice item is required")
    @Valid
    private List<InvoiceItemRequest> items;
}
