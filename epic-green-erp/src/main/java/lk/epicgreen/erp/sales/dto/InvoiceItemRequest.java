package lk.epicgreen.erp.sales.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * InvoiceItem Request DTO
 * DTO for invoice item operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemRequest {
    
    private Long salesOrderItemId;
    
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
    
    private String batchNumber;
    
    private String serialNumber;
    
    private String notes;
    
    private Integer lineNumber;
}
