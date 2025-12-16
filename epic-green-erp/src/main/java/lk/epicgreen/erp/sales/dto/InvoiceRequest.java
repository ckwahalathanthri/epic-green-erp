package lk.epicgreen.erp.sales.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Invoice Request DTO
 * DTO for invoice operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequest {
    
    @NotBlank(message = "Invoice number is required")
    private String invoiceNumber;
    
    @NotNull(message = "Invoice date is required")
    private LocalDate invoiceDate;
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    private String customerName;
    
    private Long salesOrderId;
    
    private String salesOrderNumber;
    
    private String invoiceType; // SALES_INVOICE, PROFORMA_INVOICE, CREDIT_NOTE, DEBIT_NOTE
    
    private String billingAddress;
    
    private String billingCity;
    
    private String billingState;
    
    private String billingCountry;
    
    private String billingPostalCode;
    
    private String shippingAddress;
    
    private String shippingCity;
    
    private String shippingState;
    
    private String shippingCountry;
    
    private String shippingPostalCode;
    
    @NotNull(message = "Due date is required")
    private LocalDate dueDate;
    
    private String paymentTerms;
    
    private Integer creditDays;
    
    private String currency;
    
    private Double exchangeRate;
    
    private Double subtotal;
    
    private Double discountAmount;
    
    private Double discountPercentage;
    
    private Double taxAmount;
    
    private Double taxPercentage;
    
    private Double shippingCharge;
    
    private Double otherCharges;
    
    @NotNull(message = "Total amount is required")
    private Double totalAmount;
    
    private Double paidAmount;
    
    private Double balanceAmount;
    
    private String paymentStatus; // UNPAID, PARTIAL, PAID, OVERDUE
    
    private String termsAndConditions;
    
    private String description;
    
    private String notes;
    
    private String status; // DRAFT, SENT, APPROVED, CANCELLED, VOID
    
    private String approvalStatus; // PENDING, APPROVED, REJECTED
    
    private Long createdByUserId;
    
    private Long approvedByUserId;
    
    private LocalDate approvedDate;
    
    private String approvalNotes;
    
    @NotNull(message = "Invoice items are required")
    private List<InvoiceItemRequest> items;
}
