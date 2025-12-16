package lk.epicgreen.erp.sales.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * SalesOrderItem Request DTO
 * DTO for sales order item operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderItemRequest {
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    private String productCode;
    
    private String productName;
    
    private String description;
    
    @NotNull(message = "Quantity is required")
    private Double quantity;
    
    private String unit;
    
    @NotNull(message = "Unit price is required")
    private Double unitPrice;
    
    private Double discountAmount;
    
    private Double discountPercentage;
    
    private Double taxAmount;
    
    private Double taxPercentage;
    
    @NotNull(message = "Total price is required")
    private Double totalPrice;
    
    private LocalDate requestedDeliveryDate;
    
    private String notes;
    
    private Integer lineNumber;
}
