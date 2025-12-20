package lk.epicgreen.erp.sales.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * SalesOrder Request DTO
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderRequest {
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    private String customerName;
    
    @NotNull(message = "Order date is required")
    private LocalDate orderDate;
    
    private LocalDate deliveryDate;
    
    private LocalDate requiredDate;
    
    @NotBlank(message = "Order type is required")
    private String orderType;
    
    private String priority;
    
    private Long salesRepId;
    
    private String salesRepName;
    
    // Amount fields - use Double in DTO for JSON compatibility
    @NotNull(message = "Subtotal amount is required")
    @DecimalMin(value = "0.0", message = "Subtotal amount must be positive")
    private Double subtotalAmount;
    
    @DecimalMin(value = "0.0", message = "Tax amount must be positive")
    private Double taxAmount;
    
    @DecimalMin(value = "0.0", message = "Discount amount must be positive")
    private Double discountAmount;
    
    @DecimalMin(value = "0.0", message = "Shipping amount must be positive")
    private Double shippingAmount;
    
    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", message = "Total amount must be positive")
    private Double totalAmount;
    
    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;
    
    @Size(max = 500, message = "Shipping address cannot exceed 500 characters")
    private String shippingAddress;
    
    @Size(max = 500, message = "Billing address cannot exceed 500 characters")
    private String billingAddress;
}
