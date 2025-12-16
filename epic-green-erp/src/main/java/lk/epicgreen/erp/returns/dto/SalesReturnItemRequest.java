package lk.epicgreen.erp.returns.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SalesReturnItem Request DTO
 * DTO for sales return item operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesReturnItemRequest {
    
    private Long invoiceItemId;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    private String productCode;
    
    private String productName;
    
    @NotNull(message = "Returned quantity is required")
    private Double returnedQuantity;
    
    private Double acceptedQuantity;
    
    private Double rejectedQuantity;
    
    private String unit;
    
    private Double unitPrice;
    
    private Double totalPrice;
    
    private Double refundAmount;
    
    private String batchNumber;
    
    private String serialNumber;
    
    private String returnReason;
    
    private String inspectionStatus; // APPROVED, REJECTED, PARTIAL
    
    private String rejectionReason;
    
    private String restockingStatus; // RESTOCKED, DISPOSED, RETURNED_TO_SUPPLIER
    
    private String notes;
    
    private Integer lineNumber;
}
