package lk.epicgreen.erp.sales.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DispatchNoteItem Request DTO
 * DTO for dispatch note item operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DispatchNoteItemRequest {
    
    private Long salesOrderItemId;
    
    private Long invoiceItemId;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    private String productCode;
    
    private String productName;
    
    @NotNull(message = "Ordered quantity is required")
    private Double orderedQuantity;
    
    @NotNull(message = "Dispatched quantity is required")
    private Double dispatchedQuantity;
    
    private String unit;
    
    private String batchNumber;
    
    private String serialNumber;
    
    private String packagingDetails;
    
    private Integer numberOfPackages;
    
    private String notes;
    
    private Integer lineNumber;
}
