package lk.epicgreen.erp.warehouse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * StockMovement Request DTO
 * DTO for stock movement operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementRequest {
    
    @NotBlank(message = "Movement number is required")
    private String movementNumber;
    
    @NotNull(message = "Movement date is required")
    private LocalDate movementDate;
    
    @NotBlank(message = "Movement type is required")
    private String movementType; // RECEIPT, ISSUE, TRANSFER, ADJUSTMENT, PRODUCTION, RETURN, DAMAGE, LOSS
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    private String productCode;
    
    private String productName;
    
    @NotNull(message = "Quantity is required")
    private Double quantity;
    
    private String unit;
    
    private Long fromWarehouseId;
    
    private String fromWarehouseCode;
    
    private String fromWarehouseName;
    
    private String fromLocation;
    
    private Long toWarehouseId;
    
    private String toWarehouseCode;
    
    private String toWarehouseName;
    
    private String toLocation;
    
    private String batchNumber;
    
    private String lotNumber;
    
    private String serialNumber;
    
    private LocalDate expiryDate;
    
    private Double unitCost;
    
    private Double totalCost;
    
    private String referenceType; // PURCHASE_ORDER, SALES_ORDER, PRODUCTION_ORDER, GOODS_RECEIPT, DISPATCH, RETURN
    
    private Long referenceId;
    
    private String referenceNumber;
    
    private String reason;
    
    private String description;
    
    private String notes;
    
    private String status; // DRAFT, PENDING, APPROVED, COMPLETED, CANCELLED
    
    private String movedBy;
    
    private String approvedBy;
    
    private Long createdByUserId;
    
    private Long approvedByUserId;
    
    private LocalDate approvedDate;
    
    private String approvalNotes;
}
