package lk.epicgreen.erp.sales.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * SalesOrder Request DTO
 * DTO for sales order operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderRequest {
    
    @NotBlank(message = "Order number is required")
    private String orderNumber;
    
    @NotNull(message = "Order date is required")
    private LocalDate orderDate;
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    private String customerName;
    
    private String orderType; // DIRECT_SALE, QUOTATION, CONTRACT, RECURRING
    
    private String priority; // LOW, MEDIUM, HIGH, URGENT
    
    @NotNull(message = "Expected delivery date is required")
    private LocalDate expectedDeliveryDate;
    
    private LocalDate requestedDeliveryDate;
    
    private String shippingAddress;
    
    private String shippingCity;
    
    private String shippingState;
    
    private String shippingCountry;
    
    private String shippingPostalCode;
    
    private String billingAddress;
    
    private String billingCity;
    
    private String billingState;
    
    private String billingCountry;
    
    private String billingPostalCode;
    
    private Long warehouseId;
    
    private String warehouseName;
    
    private String paymentTerms;
    
    private Integer creditDays;
    
    private String currency;
    
    private Double exchangeRate;
    
    private String shippingMethod; // SELF_PICKUP, COURIER, COMPANY_DELIVERY, THIRD_PARTY
    
    private Double shippingCost;
    
    private String salesRepUserId;
    
    private String salesRepName;
    
    private String termsAndConditions;
    
    private String description;
    
    private String notes;
    
    private String status; // DRAFT, SUBMITTED, CONFIRMED, PROCESSING, COMPLETED, CANCELLED
    
    private String approvalStatus; // PENDING, APPROVED, REJECTED
    
    private Long createdByUserId;
    
    private Long approvedByUserId;
    
    private LocalDate approvedDate;
    
    private String approvalNotes;
    
    @NotNull(message = "Sales order items are required")
    private List<SalesOrderItemRequest> items;
}
