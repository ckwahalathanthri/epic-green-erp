package lk.epicgreen.erp.purchase.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GoodsReceiptItem Request DTO
 * DTO for goods receipt item operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReceiptItemRequest {
    
    private Long purchaseOrderItemId;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    private String productCode;
    
    private String productName;
    
    @NotNull(message = "Ordered quantity is required")
    private Double orderedQuantity;
    
    @NotNull(message = "Received quantity is required")
    private Double receivedQuantity;
    
    private Double acceptedQuantity;
    
    private Double rejectedQuantity;
    
    private String unit;
    
    private Double unitPrice;
    
    private Double totalPrice;
    
    private String batchNumber;
    
    private String serialNumber;
    
    private String expiryDate;
    
    private String inspectionStatus; // PASSED, FAILED, PENDING
    
    private String rejectionReason;
    
    private String notes;
    
    private Integer lineNumber;
}
