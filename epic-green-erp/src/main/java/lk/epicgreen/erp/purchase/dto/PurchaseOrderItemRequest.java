package lk.epicgreen.erp.purchase.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * PurchaseOrderItem Request DTO
 * DTO for purchase order item operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItemRequest {
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    private String productCode;
    
    private String productName;
    
    @NotNull(message = "Quantity is required")
    private Double quantity;
    
    private Double receivedQuantity;
    
    private String unit;
    
    @NotNull(message = "Unit price is required")
    private Double unitPrice;
    
    private Double totalPrice;
    
    private Double taxPercentage;
    
    private Double taxAmount;
    
    private Double discountPercentage;
    
    private Double discountAmount;
    
    private Double netAmount;
    
    private LocalDate expectedDeliveryDate;
    
    private String specifications;
    
    private String notes;
    
    private Integer lineNumber;
}
